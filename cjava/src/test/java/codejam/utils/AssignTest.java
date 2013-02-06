package codejam.utils;

import org.junit.Test;

import codejam.utils.datastructures.AssignmentProblem;

public class AssignTest
{

@Test
public void assignTest() {
    double[][] weight = new double[][] {
            {-1, -2, -3},
            {-3, -3, -5},
            {-3, -3, -2}
    };
    
    AssignmentProblem ap = new AssignmentProblem(weight);
    
    for(int i = 0; i < 3; ++i) {
        System.out.println(ap.weight());
        System.out.println(ap.sol(i));
    }
}
    
}
