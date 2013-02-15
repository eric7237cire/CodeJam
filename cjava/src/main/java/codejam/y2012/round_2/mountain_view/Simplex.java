package codejam.y2012.round_2.mountain_view;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
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
    int objectiveFunctionRow = -1;
    
    boolean needsPhase1 = false;
    
    private final static double tolerance = 1e-5;
    
    List<Integer> basicVarRowLabels = Lists.newArrayList();
    
    public Simplex(int numVariables) {
        this.variableCount = numVariables;
        equations = Lists.newArrayList();
     
    }
    
    public Simplex(Simplex o) {
        equations = Lists.newArrayList(o.equations);
        variableCount = o.variableCount;
        artificialVariableCount = o.artificialVariableCount;
        slackVariableCount = o.slackVariableCount;
    }
    
    /**
     * Add maximize Z = c_0 * x_0 + c_0 * x_1 + ...
     * @param coef
     */
    public void addObjectiveFunction(List<Double> coeff) {
        objectiveFunctionRow = equations.size();
        
        /**
         * Transform into Z - c_0 * x_0 - ... = 0
         */
        
        coeff = Lists.transform(coeff, new Function<Double,Double>(){
            public Double apply(Double arg) {
                return -arg;
            }
        });
        
        Equation eq = new Equation(coeff, null,null,0d);
        equations.add(eq);
        
        eq.basicVariable = "Z";
        eq.basicVarColumn  = -1;
        
        
    }
    
    static class Equation
    {
        final List<Double> coeff;
        
        //slack var index and initial value
        final ImmutablePair<Integer, Integer> slackVar;        
        final ImmutablePair<Integer, Integer> artificialVar;
        
        final Double rhs;
        
        String basicVariable;
        /**
         * The column of the variable which is basic (currently assigned a non zero value)
         */
        int basicVarColumn;

        public Equation(List<Double> coeff, ImmutablePair<Integer, Integer> slackVar, ImmutablePair<Integer, Integer> artificialVar, Double rhs) {
            super();
            this.coeff = Collections.unmodifiableList(coeff);
            this.slackVar = slackVar;
            this.artificialVar = artificialVar;
            this.rhs = rhs;
        }
    }
    
    
    /*
     * c_1 * x_1 + c_2 * x_2 + c_3 * x_3 >= RHS
     * 
     * if RHS > 0 we have
     * 
     * c_1 * x_1 + c_2 * x_2 + c_3 * x_3 - slack + a = RHS
     * 
     * if 0
     * 
     * c_1 * x_1 + c_2 * x_2 + c_3 * x_3 - z = 0
     * 
     * c_1 * x_1 + c_2 * x_2 + c_3 * x_3 - z = -RHS
     */
    
    public void addConstraintGTE(List<Double> coeff, Double rhs) {
        
        
        if (rhs > 0) {
            equations.add(new Equation(coeff, new ImmutablePair<>(slackVariableCount, -1), 
                    new ImmutablePair<>(artificialVariableCount, 1), rhs));
            
            ++artificialVariableCount;
            
        } else {
            //Multiply the everything by -1 to make the slack variable coeff = 1
            //since it is in the basis
            coeff = Lists.transform(coeff, new Function<Double,Double>(){
                public Double apply(Double arg) {
                    return -arg;
                }
            });
            
            equations.add(new Equation(coeff, new ImmutablePair<>(slackVariableCount, 1), null, -rhs));
        }
        
        ++slackVariableCount;
    }
    
    public void addConstraintEquals(List<Double> coeff, Double rhs) {
        /*
        Equation eq = new Equation();
        eq.rhs = rhs;
        eq.coeff = coeff;
        
        equations.add(eq);
        
        if (rhs > 0) {
            eq.artificialVar = new ImmutablePair<>(artificialVariableCount, 1);
            ++artificialVariableCount;
        }*/
    }
    
    //c_1 * x_1 + c_2 * x_2 + c_3 * x_3 + z = RHS
    public void addConstraintLTE(List<Double> coeff, Double rhs) {
        Preconditions.checkState(rhs >= 0);
        
        equations.add(new Equation(coeff, new ImmutablePair<>(slackVariableCount, 1), null, rhs));
        
        ++slackVariableCount;        
    }

    public void buildMatrix()
    {
        //+1 for rhs 
        columns = variableCount + artificialVariableCount + slackVariableCount + 1;
        
        //For minimization
        rows = equations.size() ;
        
        this.matrix = new Array2DRowRealMatrix(rows, columns);
        
        /**
         * Add all the constraints
         */
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
                
                eq.basicVarColumn = (variableCount + slackVariableCount + eq.artificialVar.getKey());
            } else if (eq.slackVar != null) {
                eq.basicVarColumn =(variableCount + eq.slackVar.getKey());
            }
            matrix.setEntry(eqNum, columns-1, eq.rhs);
        }
        
        final int phase1Row = rows - 2;
        
        /**
         * Set the phase 1 function which is maximizing 
         * w = -a_1 - a_2 - a_3 ...
         * 
         * all the artificial variables
         */
        /*
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
        }*/
        
        
        
        
        
        
    }
    
    public void debugPrint() {
        log.debug("Matrix\n{}", printMatrix(matrix.getData(), rows, columns));
    }
    
    public boolean solve(List<Double> solutions)
    {
        buildMatrix();
        
        if (needsPhase1) {
            doPhase1(solutions);
        }
        
        int iterCheck = 0;
        
        boolean notDone = true;
        
        while(notDone) {
            notDone = doStep(objectiveFunctionRow);
            ++iterCheck;
            
            Preconditions.checkState(iterCheck < 5000);
        }
        
        List<Double> ret = solutions;
        ret.clear();
        
        for(int var = 0; var < variableCount; ++var) {
            ret.add(0d);
        }
        
        for (int eq =  0; eq < equations.size(); ++eq) {
            int basicVarColIdx = equations.get(eq).basicVarColumn; 
            if (isVar(basicVarColIdx)) {
                Double value = matrix.getEntry(eq, columns - 1);
                
                ret.set(basicVarColIdx, value);
            }
        }
        
        return true;
    }
    
    public boolean doPhase1(List<Double> solutions)
    {
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
    
    public boolean doStep(int objectiveFunctionRow)  
    {

        /**
         * Choose the pivot column by finding
         * the most negative value in objective function row
         */
        boolean foundNegValue = false;
        
        double lowestColumnValueInObjectiveRow = 0;
        int pivotColIdx = -1;

        for (int c = 0; c < columns - 1; ++c)
        {
            double coefVal  = matrix.getEntry(objectiveFunctionRow, c);
            if (DoubleMath.fuzzyCompare(coefVal, lowestColumnValueInObjectiveRow, tolerance) < 0)
            {
                foundNegValue = true;
                pivotColIdx = c;
                lowestColumnValueInObjectiveRow = coefVal;
            }
        }

        //We are done
        if (!foundNegValue)
        {
            return false;
        }

        double lowestRatio = Double.MAX_VALUE;

        int pivotRowIdx = -1;

        //cycle through to find best ratio
        for (int r = 0; r < rows; ++r)
        {
            Equation eq = equations.get(r);
            
            //Skip over objective rows (can be 2 if it is a 2 phase)
            if (eq.basicVarColumn < 0)
                continue;

            //Skip over entries that equal 0 or are negative
            if (DoubleMath.fuzzyCompare(matrix.getEntry(r, pivotColIdx), 0, tolerance) <= 0)
                continue;

            double ratio = matrix.getEntry(r, columns - 1) / matrix.getEntry(r, pivotColIdx);
            if (DoubleMath.fuzzyCompare(ratio, 0, tolerance) >= 0 && ratio < lowestRatio)
            {
                lowestRatio = ratio;
                pivotRowIdx = r;
            }
        }

        Preconditions.checkState(pivotRowIdx >= 0);

        log.debug("Choosing pivot row {} col {}", pivotRowIdx, pivotColIdx);

        /**
         * The pivot column is the entering basic varable 
         * and the row is the exiting basic variable.
         * 
         * Use the column index and the ordering of
         * basic 0 1 2 .. slack 0 1 2 ... artificial 0 1 2 ... RHS
         */
        //basicVarRowLabels.set(pivotRowIdx, pivotColIdx);
        equations.get(pivotRowIdx).basicVarColumn = pivotColIdx;

        Double pivotEntry = matrix.getEntry(pivotRowIdx, pivotColIdx);
        RealVector pivotRow = matrix.getRowVector(pivotRowIdx);
        pivotRow.mapDivideToSelf(pivotEntry);

        matrix.setRowVector(pivotRowIdx, pivotRow);

        for (int r = 0; r < rows; ++r)
        {
            if (r == pivotRowIdx)
                continue;

            double entry = matrix.getEntry(r, pivotColIdx);

            RealVector row = matrix.getRowVector(r);

            row.combineToSelf(1, -entry, pivotRow);

            matrix.setRowVector(r, row);
        }

        //log.debug("Matrix is now : \n{}", printMatrix(matrix.getData(), rows, columns));

        if (log.isDebugEnabled()) {
            log.debug("Step finished Matrix\n{}", printMatrix(matrix.getData(), rows, columns));
        }
        
        return true;
        
    }
    
    private boolean isArtificialVar(int columnIdx) {
        int start = this.variableCount+this.slackVariableCount;
        int end = this.columns - 1;
        
        return (start <= columnIdx && end >= columnIdx);
    }
    
    /**
     * Is this column one of the entered variables
     * 
     */
    private boolean isVar(int columnIdx) {
        return columnIdx >= 0 && columnIdx < variableCount;
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
            Equation eq = equations.get(rIdx);
            int r = rIdx;
            
            
            int basisRowLabel = eq.basicVarColumn;
            String basisRowLabelStr = "";
            if (basisRowLabel == -1) {
                basisRowLabelStr = "Z";
            } else
            if (basisRowLabel < variableCount) {
                basisRowLabelStr = "bv" + basisRowLabel;
            } else if (basisRowLabel < variableCount + slackVariableCount) {
                basisRowLabelStr = "sv" + (basisRowLabel - variableCount);
            } else {
                basisRowLabelStr = "a" + (basisRowLabel - variableCount - slackVariableCount);
            }
            gridStr.append(StringUtils.rightPad(basisRowLabelStr, printWidth));
        
            
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
