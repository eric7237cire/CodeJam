package codejam.y2009.bribe_prisoner;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>{

	

	

	final static Logger log = LoggerFactory.getLogger(Main.class);

	

    /* (non-Javadoc)
     * @see codejam.utils.main.Runner.TestCaseInputScanner#readInput(java.util.Scanner, int)
     */
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        final int numCells = scanner.nextInt();
        final int numToBeFreed = scanner.nextInt();

        


        List<Integer> listToBeFree = new ArrayList<>();
        for (int i = 0; i < numToBeFreed; ++i) {
            int index = scanner.nextInt();

            listToBeFree.add(index);
            
        }
        InputData input = new InputData(testCase);
        input.numCells = numCells;
        input.numToBeFreed = numToBeFreed;
        input.listToBeFree = listToBeFree;
        
        return input;
    }

    /* (non-Javadoc)
     * @see codejam.utils.multithread.Consumer.TestCaseHandler#handleCase(java.lang.Object)
     */
    @Override
    public String handleCase(InputData input) {
        final int numCells = input.numCells;
        
        List<Integer> listToBeFree = input.listToBeFree;
        
        PrisonSelectionAlgorithm alg ;
        alg = new Dynamic();
        int cost = 0;
        
        cost = alg.findMinCost(1, numCells , listToBeFree);

        return ("Case #" + input.testCase + ": " + cost);
    }
}