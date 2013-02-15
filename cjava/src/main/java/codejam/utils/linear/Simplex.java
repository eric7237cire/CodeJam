package codejam.utils.linear;

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
    
    final static Logger log = LoggerFactory.getLogger("main");
    
    int variableCount;
    int slackVariableCount;
    int artificialVariableCount;
    
    int rows;
    int columns;
    
    List<Equation> equations;
    int objectiveFunctionRow = -1;
    int phase1Row = -1;
    
    private final static int PHASE_1_COL_ID = -1;
    private final static int PHASE_2_COL_ID = -2;
    
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
        eq.basicVarColumn  = PHASE_2_COL_ID;
        
        
    }
    
    public void addObjectiveFunctionToMinimize(List<Double> coeff) {
        objectiveFunctionRow = equations.size();
        
        /**
         * Transform into Z - c_0 * x_0 - ... = 0
         */
        
        /*
        coeff = Lists.transform(coeff, new Function<Double,Double>(){
            public Double apply(Double arg) {
                return -arg;
            }
        }); */
        
        Equation eq = new Equation(coeff, null,null,0d);
        equations.add(eq);
        
        eq.basicVariable = "-Z";
        eq.basicVarColumn  = PHASE_2_COL_ID;
        
        
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

        @Override
        public String toString()
        {
            return "Equation [coeff=" + coeff + ", slackVar=" + slackVar + ", artificialVar=" + artificialVar + ", rhs=" + rhs + ", basicVariable="
                    + basicVariable + ", basicVarColumn=" + basicVarColumn + "]";
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
            //since it is in the basis.
            
            //Note this is just like adding a LTE constraint, after * -1, the sign flips
            coeff = Lists.transform(coeff, new Function<Double,Double>(){
                public Double apply(Double arg) {
                    return -arg;
                }
            });
            
            equations.add(new Equation(coeff, new ImmutablePair<>(slackVariableCount, 1), null, -rhs));
        }
        
        ++slackVariableCount;
    }
    
    /**
     * 
     * Add 
     * c_0 * x_0 + c_1 * x_1 == rhs
     */
    
    
    public void addConstraintEquals(List<Double> coeff, Double rhs) {
        
        
        equations.add(new Equation(coeff, null, 
                new ImmutablePair<>(artificialVariableCount, 1), rhs));
        
        ++artificialVariableCount;
        
        
    }
    
    //c_1 * x_1 + c_2 * x_2 + c_3 * x_3 + z = RHS
    /**
     * c_1 * x_1 + c_2 * x_2 + c_3 * x_3 <= RHS
     * 
     * if RHS < 0 we have
     * 
     * c_1 * x_1 + c_2 * x_2 + c_3 * x_3 - z + a = -RHS
     * 
     * if 0
     * 
     * c_1 * x_1 + c_2 * x_2 + c_3 * x_3 + z = 0
     * 
     * c_1 * x_1 + c_2 * x_2 + c_3 * x_3 - z = RHS
     */
    public void addConstraintLTE(List<Double> coeff, Double rhs) {
        
        if (rhs < 0) {
            //Convert to a GTE constraint
            coeff = Lists.transform(coeff, new Function<Double,Double>(){
                public Double apply(Double arg) {
                    return -arg;
                }
            });
            
            addConstraintGTE(coeff, -rhs);
            return;
        }
        
        Preconditions.checkState(rhs >= 0);
        
        equations.add(new Equation(coeff, new ImmutablePair<>(slackVariableCount, 1), null, rhs));
        
        ++slackVariableCount;        
    }

    public void buildMatrix()
    {
        //+1 for rhs 
        columns = variableCount + artificialVariableCount + slackVariableCount + 1;
        
        if (artificialVariableCount > 0)
        {
            /**
             * Create phase 1 objective function.  
             * 
             * Set the phase 1 function which is minimizing
             * W = a1 + a2 + a3
             * 
             * W - a1 - a2 - a3 = 0
             * 
             *  To convert into a maximizing , we multiply eveything by -1
             * -W + a1 + a2 + a3 = 0
             *  
             * 
             * all the artificial variables             
             */
            List<Double> coef = Lists.newArrayList();
            for(int c = 0; c < columns - 1; ++c) {
                if (isArtificialVar(c)) {
                    coef.add(1d);
                } else {
                    coef.add(0d);
                }
            }
            Equation eq = new Equation(coef, null, null, 0d);
            eq.basicVariable="-W";
            eq.basicVarColumn = PHASE_1_COL_ID;
            equations.add(0, eq);
            
            phase1Row = 0;
            objectiveFunctionRow ++;
        }
            
        
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
            matrix.setEntry(eqNum, columns-1, eq.rhs);
            
            //Recalculate basic var indexes as needed
            
            if (eqNum == phase1Row || eqNum == objectiveFunctionRow)
                continue;
            
            if (eq.slackVar != null) {
                matrix.setEntry(eqNum, variableCount + eq.slackVar.getKey(), eq.slackVar.getValue());
            }
            
            if (eq.artificialVar != null) {
                matrix.setEntry(eqNum, variableCount + slackVariableCount + eq.artificialVar.getKey(), eq.artificialVar.getValue());
                
                eq.basicVarColumn = (variableCount + slackVariableCount + eq.artificialVar.getKey());
            } else if (eq.slackVar != null) {
                eq.basicVarColumn =(variableCount + eq.slackVar.getKey());
            }
            
        }
        
        if (artificialVariableCount > 0)
        {

            /**
             *  One more step if it is a phase 1 problem.
             *  Proper form says that each basic var is 1 only in it's row,
             *  so we need to remove the artificial vars from the phase I
             *  objective function
             */
            //Remove artificial coeff from objective func
            for (int eqNum = 0; eqNum < equations.size(); ++eqNum)
            {
                if (eqNum == phase1Row)
                    continue;

                Equation eq = equations.get(eqNum);

                if (eq.artificialVar == null)
                    continue;

                //Found a constraint with an artificial var, add the eq * -1 to the phase I  row
                matrix.setRowVector(phase1Row, matrix.getRowVector(phase1Row).combine(1, -1, matrix.getRowVector(eqNum)));
            }

        }
        
    }
    
    public void debugPrint() {
        log.debug("Matrix\n{}", printMatrix(matrix.getData(), rows, columns));
    }
    
    public boolean solve(List<Double> solutions)
    {
        buildMatrix();
        
        if (log.isDebugEnabled()) {
            log.debug("Intial matrix");
            debugPrint();
        }
        
        if (artificialVariableCount > 0) {
            List<Double> phaseOneResults = Lists.newArrayList();
            boolean feasible = doPhase1(phaseOneResults);
            
            log.debug("Phase I complete {}", feasible);
            if (!feasible)
                return false;
            
            //Remove artificial variable columns
            Array2DRowRealMatrix newMatrix = new Array2DRowRealMatrix(rows, columns-artificialVariableCount);
            int newCols = columns-artificialVariableCount;
            
            for(int r = 0; r < rows; ++r) {
                for(int c = 0; c < variableCount + slackVariableCount; ++c) 
                {
                    newMatrix.setEntry(r,c, matrix.getEntry(r,c));                    
                }
                
                newMatrix.setEntry(r, newCols-1, matrix.getEntry(r, columns-1));
            }
            
            matrix = newMatrix;
            columns = newCols;
            
            artificialVariableCount = 0;
        }
        
        int iterCheck = 0;
        
        boolean notDone = true;
        
        while(notDone) {
            notDone = doStep(objectiveFunctionRow);
            if (log.isDebugEnabled()) {
                log.debug("Phase II step done");
                debugPrint();
            }
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
        
        //Add the Z value
        if (equations.get(objectiveFunctionRow).basicVariable.equals("-Z")) {
            //We were minimizing
            ret.add( -1 * matrix.getEntry(objectiveFunctionRow, columns-1));
        } else {
            ret.add( matrix.getEntry(objectiveFunctionRow, columns-1));
        }
        
        return true;
    }
    
    public boolean doPhase1(List<Double> solutions)
    {
        int iterCheck = 0;
        
        boolean notDone = true;
        
        while(notDone) {
            notDone = doStep(phase1Row);
            
            if(log.isDebugEnabled()) {
                log.debug("Phase 1 step done");
                debugPrint();
            }
            ++iterCheck;
            
            Preconditions.checkState(iterCheck < 5000);
        }
        
        
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
        //    log.debug("Step finished Matrix\n{}", printMatrix(matrix.getData(), rows, columns));
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
            if (basisRowLabel < 0) {
                basisRowLabelStr = eq.basicVariable;
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
