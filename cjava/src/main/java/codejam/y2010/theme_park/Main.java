package codejam.y2010.theme_park;

import java.util.ArrayList;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.Runner;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    static class GroupStart {
        long money;
        int rides;
        public GroupStart(long money, int rides) {
            super();
            this.money = money;
            this.rides = rides;
        }
        
    }
    @Override
    public String handleCase(InputData input) {
        
        int caseNumber = input.testCase;

        log.info("Starting calculating case {}", caseNumber);
        
        //double ans = DivideConq.findMinPerimTriangle(input.points);
        
        int ridesLeft = input.rides;
        int groupAtFront = 0;
        long moneyGained = 0;
        
        GroupStart[] groupStart = new GroupStart[input.groupSizes.size()];
        
        while(ridesLeft > 0) {
           // log.debug("Rides left {}", ridesLeft);
            int capacityRemaining = input.capacity;
            int oldGroupAtFront = groupAtFront;
            int moneyRide = 0;
            
            groupStart[groupAtFront] = new GroupStart(moneyGained, input.rides - ridesLeft);
            
            while(capacityRemaining >= input.groupSizes.get(groupAtFront)) {
                capacityRemaining -= input.groupSizes.get(groupAtFront);
                moneyRide += input.groupSizes.get(groupAtFront);
                groupAtFront++;
                if (groupAtFront == input.groupSizes.size()) {
                    groupAtFront = 0;
                }
                if (groupAtFront == oldGroupAtFront) {
                    break;
                }
                
                if (moneyRide == 0) {
                    return ("Case #" + caseNumber + ": " + moneyGained);
                }
            }
            --ridesLeft;
            moneyGained += moneyRide;
            oldGroupAtFront = groupAtFront;
            
            if (groupStart[groupAtFront] != null) {
                GroupStart start = groupStart[groupAtFront];
                int ridesDone = input.rides - ridesLeft - start.rides;
                int numRidesLoopsPossible = ridesLeft / ridesDone;
                
                moneyGained = moneyGained + numRidesLoopsPossible * (moneyGained - start.money);
                ridesLeft -= ridesDone * numRidesLoopsPossible;
            }
        }
        

        log.info("Done calculating answer case {}", caseNumber);
        
        //DecimalFormat decim = new DecimalFormat("0.00000000000");
        //decim.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        
        return ("Case #" + caseNumber + ": " + moneyGained );
    }
    
    
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        
    
        InputData  i = new InputData(testCase);
        
        int n;
        i.rides = scanner.nextInt();
        i.capacity = scanner.nextInt();
        n = scanner.nextInt();
        
        i.groupSizes = new ArrayList<>(n);
        
        for(int g = 0; g < n; ++g) {
            i.groupSizes.add(scanner.nextInt());
        }
        
        //log.info("Reading data...Test case # {} ", testCase);
        
        //log.info("Done Reading data...Test case # {} ", testCase);
        
        
        return i;
        
    }

    


    public Main() {
        super();
    }
    
    
    public static void main(String args[]) throws Exception {

        if (args.length < 1) {
           args = new String[] { "sample.txt" };
           //args = new String[] { "smallInput.txt" };
           //args = new String[] { "largeInput.txt" };
        }
        log.info("Input file {}", args[0]);

        Main m = new Main();
        Runner.goSingleThread(args[0], m, m);
        
       
    }

    
}