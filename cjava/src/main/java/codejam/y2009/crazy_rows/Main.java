package codejam.y2009.crazy_rows;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

public class Main implements TestCaseInputScanner<InputData>, TestCaseHandler<InputData> {
    
    static class Row {
        final int leftOne;
        final int rightOne;

        public Row(String s) {
            boolean hasOne = -1 != s.indexOf('1');

            leftOne = hasOne ? s.indexOf('1') : 0;
            rightOne = hasOne ? s.lastIndexOf('1') : 0;

        }

        @Override
        public String toString() {
            return "(" + leftOne + ", " + rightOne + ")";
        }

    }

    /**
     * 
     * @param listeProg part of list that already satisfies the constraint (all 1's on or below the main diagonal)
     * @param liste.  The remaining unchecked part
     * @return minimum number of swaps needed
     */
    static int findMin(int listeProg, List<Row> liste) {
        Integer bottomRowIndex = null;
        Row  bottomRow = null;
        
       
        log.debug("findMin {}", liste);

        for (int i = 0; i < liste.size(); ++i) {
            Row r = liste.get(i);

            if (bottomRow == null) {
                
                //Found a row that does not satisfy condition
                if (r.rightOne > i + listeProg ) {
                    bottomRow = r;
                    bottomRowIndex = i;
                    continue;
                }
            } else {
                /*
                 * Found a row to put in bottomRow's place (bottomRow stays where it is)
                 * 
                 * It is OK to just take the first one because the problem guarantees a solution.
                 * Say that the invalid row is found at row 3
                 * this means that row 1 has no 1's after col 1
                 * and row 2 has no 1's after col 2.
                 * 
                 * The row to replace the invalid row must have no 1's after
                 * col 3.  If it has no 1's after col 1 or 2, it does not matter as
                 * the first and second row already have rows which satisfy the
                 * condition.
                 * 
                 * 
                 */
                if (r.rightOne <= bottomRowIndex + listeProg) {
                    List<Row> copyListe = new ArrayList<>(liste);
                    
                    copyListe.remove(i);

                    log.debug("findMin {} new liste {}", liste, copyListe);

                    //Cost of swapping + the rest of the list
                    return  i - bottomRowIndex + findMin(listeProg + 1, copyListe);
                }
            }

        }

        if (bottomRow == null) {
            return 0;
        }

        log.debug("findMin {} returns {}", liste, 0);
        
        return 0;
    }

   
    final static Logger log = LoggerFactory.getLogger(Main.class);

    
    @Override
    public String handleCase(InputData data) {
        int cost = findMin(0, data.liste);
        
        return("Case #" + data.testCase + ": " + cost);
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        int dimension = scanner.nextInt();

        List<Row> liste = new ArrayList<>();

        for (int i = 0; i < dimension; ++i) {
            liste.add(new Row(scanner.next()));
        }
        InputData input = new InputData(testCase);
        input.dimension = dimension;
        input.liste = liste;
        return input;
    }

   
}