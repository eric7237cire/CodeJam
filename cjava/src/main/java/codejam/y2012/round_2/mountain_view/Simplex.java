package codejam.y2012.round_2.mountain_view;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.utils.DoubleFormat;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.math.DoubleMath;

/**
 * For now just phase 1
 * 
 */
public class Simplex {

    private RealMatrix matrix;
    
    final static Logger log = LoggerFactory.getLogger(Simplex.class);
    
    int variableCount;
    int slackVariableCount;
    int artificialVariableCount;
    
    int rows;
    int columns;
    
    List<Equation> equations;
    
    List<Integer> basicVarRowLabels = Lists.newArrayList();
    
    public Simplex(int numVariables) {
        this.variableCount = numVariables;
        equations = Lists.newArrayList();
     
    }
    
    static class Equation
    {
        List<Double> coeff;
        
        //slack var index and initial value
        Pair<Integer, Integer> slackVar;
        
        Pair<Integer, Integer> artificialVar;
        
        Double rhs;
    }
    
    
    /*
     * c_1 * x_1 + c_2 * x_2 + c_3 * x_3 >= RHS
     * 
     * if RHS > 0 we have
     * 
     * c_1 * x_1 + c_2 * x_2 + c_3 * x_3 - z + a = RHS
     * 
     * if 0
     * 
     * c_1 * x_1 + c_2 * x_2 + c_3 * x_3 - z = 0
     */
    
    public void addConstraintGTE(List<Double> coeff, Double rhs) {
        
        
        Equation eq = new Equation();
        eq.rhs = rhs;
        eq.coeff = coeff;
        
        equations.add(eq);
        
        eq.slackVar = new MutablePair<>(slackVariableCount, -1);
        ++slackVariableCount;
        
        if (rhs > 0) {
            eq.artificialVar = new ImmutablePair<>(artificialVariableCount, 1);
            ++artificialVariableCount;
        } else {
            //Multiply the everything by -1 to make the slack variable coeff = 1
            //since it is in the basis
            eq.coeff = Lists.transform(eq.coeff, new Function<Double,Double>(){
                public Double apply(Double arg) {
                    return -arg;
                }
            });
            
            eq.slackVar.setValue(1);
        }
    }
    
    public void addConstraintEquals(List<Double> coeff, Double rhs) {
        
        Equation eq = new Equation();
        eq.rhs = rhs;
        eq.coeff = coeff;
        
        equations.add(eq);
        
        if (rhs > 0) {
            eq.artificialVar = new ImmutablePair<>(artificialVariableCount, 1);
            ++artificialVariableCount;
        }
    }
    
    public void addConstraintLTE(List<Double> coeff, Double rhs) {
        Equation eq = new Equation();
        eq.rhs = rhs;
        eq.coeff = coeff;
        
        equations.add(eq);
        
        eq.slackVar = new ImmutablePair<>(slackVariableCount, 1);
        ++slackVariableCount;        
    }

    public boolean doPhase1(List<Double> solutions)  
    {
        //+1 for rhs 
        columns = variableCount + artificialVariableCount + slackVariableCount + 1;
        
        //For minimization
        rows = equations.size() + 1;
        
        this.matrix = new Array2DRowRealMatrix(rows, columns);
        
        for(int eqNum = 0; eqNum < equations.size(); ++eqNum) {
            Equation eq = equations.get(eqNum);
            for(int c = 0; c < eq.coeff.size(); ++c) {
                matrix.setEntry(eqNum, c, eq.coeff.get(c));
            }
            
            if (eq.slackVar != null) {
                matrix.setEntry(eqNum, variableCount + eq.slackVar.getKey(), eq.slackVar.getValue());
            }
            
            if (eq.artificialVar != null) {
                matrix.setEntry(eqNum, variableCount + slackVariableCount + eq.artificialVar.getKey(), eq.artificialVar.getValue());
                
                basicVarRowLabels.add(variableCount + slackVariableCount + eq.artificialVar.getKey());
            } else {
                basicVarRowLabels.add(variableCount + eq.slackVar.getKey());
            }
            matrix.setEntry(eqNum, columns-1, eq.rhs);
            
            
        }
        
        for(int av = 0; av < artificialVariableCount; ++av) {
            matrix.setEntry(rows-1, av+variableCount+slackVariableCount, 1);
        }
        
        //Also set the initial minimization equation w = a_1 + a_2 + ...
        //Trying to maximize -w = a_1 + a_2 + ...
        
        //Remove artificial coeff from objective func
        for(int eqNum = 0; eqNum < equations.size(); ++eqNum) {
            Equation eq = equations.get(eqNum);
            if (eq.artificialVar == null) 
                continue;
                
            matrix.setRowVector(rows-1, 
                    matrix.getRowVector(rows-1).combine(1, -1,matrix.getRowVector(eqNum)));
        }
        
        log.debug("Matrix\n{}", printMatrix(matrix.getData(), rows, columns));
        
    
        
        
        while(true)
        {
          //Choose a negative value in objective function
            boolean foundNegValue = false;
            double lowestColumnValue = 0;
            int pivotColIdx = -1;
            
            for(int c = 0; c < columns - 1; ++c) {
                if (DoubleMath.fuzzyCompare(matrix.getEntry(rows-1, c), lowestColumnValue, 1e-5) < 0) {
                    foundNegValue = true;
                    pivotColIdx = c;
                    lowestColumnValue = matrix.getEntry(rows-1, c); 
                }
            }
            
            if (!foundNegValue) {
                break;
            }
            
                
            double lowestRatio = Double.MAX_VALUE;
    
            int pivotRowIdx = -1;
            
            //cycle through to find best ratio
            for (int r = 0; r < rows - 1; ++r) {
                
                if (DoubleMath.fuzzyCompare(matrix.getEntry(r, pivotColIdx), 0, 1e-5) <= 0)
                    continue;
                
                double ratio = matrix.getEntry(r, columns - 1) / 
                        matrix.getEntry(r, pivotColIdx);
                if (DoubleMath.fuzzyCompare(ratio, 0, 1e-5) >= 0 && ratio < lowestRatio) {
                    lowestRatio = ratio;
                    pivotRowIdx = r;

                }
            }
            
            Preconditions.checkState(pivotRowIdx >= 0);
            
            log.debug("Choosing pivot row {} col {}", pivotRowIdx, pivotColIdx);
            
            basicVarRowLabels.set(pivotRowIdx, pivotColIdx);
            
            Double pivotEntry = matrix.getEntry(pivotRowIdx,pivotColIdx);
            RealVector pivotRow = matrix.getRowVector(pivotRowIdx);
            pivotRow.mapDivideToSelf(pivotEntry);
            
            matrix.setRowVector(pivotRowIdx, pivotRow);
            
            
            for(int r = 0; r < rows; ++r) {
                if (r == pivotRowIdx)
                    continue;
                
                double entry = matrix.getEntry(r, pivotColIdx);
                
                RealVector row = matrix.getRowVector(r);
                
                row.combineToSelf(1, -entry, pivotRow);
                
                matrix.setRowVector(r, row);
            }
            
            
           //log.debug("Matrix is now : \n{}", printMatrix(matrix.getData(), rows, columns));
        }
    
            
            
            
        
        
        //log.debug("Matrix\n{}", printMatrix(matrix.getData(), rows, columns));
        
        List<Double> ret = solutions;
        
        for(int var = 0; var < columns - 1; ++var) {
            ret.add(0d);
        }
        
        boolean isFeasible = true;
        
        //Use basic labels to return initial basic feasible solution
        for(int idx = 0; idx < basicVarRowLabels.size(); ++idx) {
            int varIdx = basicVarRowLabels.get(idx);
            Double value = matrix.getEntry(idx, columns - 1);
            
            if (isArtificialVar(varIdx) && value > 0) {
                isFeasible = false;
            }
            
            ret.set(varIdx, value);
        }
        
        return isFeasible;
    }
    
    private boolean isArtificialVar(int columnIdx) {
        int start = this.variableCount+this.slackVariableCount;
        int end = this.columns - 1;
        
        return (start <= columnIdx && end >= columnIdx);
    }
    
    public String printMatrix(double[][] matrix, int rows, int cols) {
        int printWidth = 7;

        StringBuffer gridStr = new StringBuffer();
        
        gridStr.append(StringUtils.rightPad("bas", printWidth));
        
        for(int var = 0; var < variableCount; ++var) {
            String s = "bv" + var;
            s = StringUtils.rightPad(s, printWidth);
            gridStr.append(s);
        }
        
        for(int var = 0; var < slackVariableCount; ++var) {
            String s = "sv" + var;
            s = StringUtils.rightPad(s, printWidth);
            gridStr.append(s);
        }
        
        for(int var = 0; var < artificialVariableCount; ++var) {
            String s = "a" + var;
            s = StringUtils.rightPad(s, printWidth);
            gridStr.append(s);
        }
        
        gridStr.append("\n");
        
        for (int rIdx = 0; rIdx < rows; ++rIdx) {
            int r = rIdx;
            
            if (rIdx < rows - 1) {
                int basisRowLabel = basicVarRowLabels.get(rIdx);
                String basisRowLabelStr = "";
                if (basisRowLabel < variableCount) {
                    basisRowLabelStr = "bv" + basisRowLabel;
                } else if (basisRowLabel < variableCount + slackVariableCount) {
                    basisRowLabelStr = "sv" + (basisRowLabel - variableCount);
                } else {
                    basisRowLabelStr = "a" + (basisRowLabel - variableCount - slackVariableCount);
                }
            gridStr.append(StringUtils.rightPad(basisRowLabelStr, printWidth));
            } else {
                gridStr.append(StringUtils.rightPad("", printWidth));
            }
            
            for (int c = 0; c < cols; ++c) {
                String s = DoubleFormat.df3.format(matrix[r][c]);
                s = StringUtils.rightPad(s, printWidth);
                gridStr.append(s);

            }
            gridStr.append("\n");
        }

        return gridStr.toString();
    }
}
