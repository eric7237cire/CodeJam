package codejam.y2009.round_2.crazy_rows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

public class Main implements TestCaseInputScanner<InputData>, TestCaseHandler<InputData>,
DefaultInputFiles {
    
    @Override
    public String[] getDefaultInputFiles() {
        return new String[] { "A-small-practice.in", "A-large-practice.in" };
    }

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

    
    public String handleCaseMine(InputData data) {
        
        int cost = findMin(0, data.liste);
        
        return("Case #" + data.testCase + ": " + cost);
    }
    
    public String handleCase(InputData in) {
        
        int N = in.liste.size();
        int i,j;
        
        int[] a = new int[N];
        //Only rightmost 1 matters
        for(int rowIdx = 0; rowIdx < N; ++rowIdx) {
            a[rowIdx] = in.liste.get(rowIdx).rightOne;
        }
        
        int[] b = new int[N];
     // -1 means no position is assigned for a[j].
        Arrays.fill(b, -1);

        for (i = 0; i < N; i++)
        {
            for (j = 0; j < N; j++)
            {
                //Find left most value that has not been assigned
                if (b[j] < 0 && a[j] <= i)
                {
                    //Assign a[i]'s position
                    b[j] = i;
                    break;
                }
            }
        }
        int r=0;
        for(i=0;i<N;i++) {
            for(j=i+1;j<N;j++) {
                //Counting disorder
                if(b[i]>b[j]) {
                    r++;
                }
            }
        }
          
        // output r as the answer        
        return String.format("Case #%d: %d", in.testCase, r);
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