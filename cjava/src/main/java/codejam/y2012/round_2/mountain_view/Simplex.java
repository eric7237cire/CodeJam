package codejam.y2012.round_2.mountain_view;

import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

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
        
        eq.slackVar = new ImmutablePair<>(slackVariableCount, -1);
        ++slackVariableCount;
        
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

    public void doPhase1()  
    {
        //+1 for rhs 
        int columns = variableCount + artificialVariableCount + slackVariableCount + 1;
        
        //For minimization
        int rows = equations.size() + 1;
        
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
        
        log.debug("{}", matrix);
    
        
        
        while(true)
        {
          //Choose a negative value in objective function
            boolean foundNegValue = false;
            
            for(int c = 0; c < columns - 1; ++c) {
                
                if (matrix.getEntry(rows-1, c) < 0) {
                    double lowestRatio = Double.MAX_VALUE;
                    int pivotCol = c;
                    int pivotRow = -1;
                    
                    //cycle through to find best ratio
                    for(int r = 0; r < rows - 1; ++r) {
                        if (matrix.getEntry(r, columns - 1) > 0) {
                            double ratio = matrix.getEntry(r, columns - 1) / matrix.getEntry(r,c);
                            if (ratio < lowestRatio) {
                                lowestRatio = ratio;
                                pivotRow = r;
                            }
                        }
                    }
                    
                    Preconditions.checkState(pivotRow >= 0);
                }
            }
            
            break;
        }
    }
    
    
    
}
