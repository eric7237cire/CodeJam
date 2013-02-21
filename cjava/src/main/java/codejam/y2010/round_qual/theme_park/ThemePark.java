package codejam.y2010.round_qual.theme_park;

import java.util.ArrayList;
import java.util.Scanner;

import ch.qos.logback.classic.Level;

import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

public class ThemePark extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    public ThemePark()
    {
        super("C",1,1);
        (( ch.qos.logback.classic.Logger) log).setLevel(Level.INFO);
    }
    
    
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
        
              
        return i;
        
    }

    
    
}