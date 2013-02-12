package codejam.y2012.round_2.swinging_wild;

import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import codejam.utils.datastructures.IndexMaxPQ;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;

/**
 * Implement the alternative solution
 * using Dijkstras
 * 
 *
 */
public class Solution2 extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles 
 {

    public Solution2() 
    {
        super("A", 1, 1, 0);
        
//        (( ch.qos.logback.classic.Logger) log).setLevel(Level.INFO);
        
        //super("A", 0, 0, 1);
    }


@Override
public InputData readInput(Scanner scanner, int testCase) {
    InputData in = new InputData(testCase);
    
    /*
     * The first line of each test case contains the number N of vines.
     *  N lines describing the vines follow, each with a pair of integers
     *   di and li - the distance of the vine from your ledge, and
     *    the length of the vine, respectively. The last line of the 
     *    test case contains the distance D to the ledge with your one 
     *    true love. You start by holding the first vine in hand.
     */
    
    in.nRopes = scanner.nextInt() + 1;
    in.ropePosition = new int[in.nRopes];
    in.ropeLength = new int[in.nRopes];
    
    for(int n = 0; n < in.nRopes - 1; ++n) {
        in.ropePosition[n] = scanner.nextInt();
        in.ropeLength[n] = scanner.nextInt();
    }
    
    
    in.goalDistance = scanner.nextInt();

    //Create a dummy rope at goal
    in.ropeLength[in.nRopes-1] = 1;
    in.ropePosition[in.nRopes-1] = in.goalDistance;
    
    return in;
}

@Override
public String handleCase(InputData in) {
    
    
    
    TreeMap<Integer,Integer> positionToIndex = new TreeMap<>();
    
    IndexMaxPQ<Integer> toVisit = new IndexMaxPQ<>(in.nRopes);
    
    for(int r = 0; r < in.nRopes; ++r)
    {
        toVisit.insert(r, 0);
        positionToIndex.put(in.ropePosition[r], r);
    }
    
    
    boolean found = false;    
    
    Preconditions.checkState(in.ropeLength[0] >= in.ropePosition[0]);
    
    toVisit.increaseKey(0, in.ropePosition[0]);
        
    while(!toVisit.isEmpty()) {
        
        int currentSwingLength = toVisit.maxKey();
        int currentRopeIndex = toVisit.delMax();
        
        log.debug("Current rope {} swing length {}", currentRopeIndex, currentSwingLength);
        
        //Unreachable nodes
        if (currentSwingLength == 0)
            continue;
        
        if (currentRopeIndex == in.nRopes-1) {
            found = true;
            break;
        }
           
        //Remove node from future consideration
        positionToIndex.remove(in.ropePosition[currentRopeIndex]);
        
        int curPos = in.ropePosition[currentRopeIndex];
        
        /*
         * backwards and forwards
        Map<Integer,Integer> reachable = positionToIndex.
                subMap(curPos - currentSwingLength, true, curPos + currentSwingLength, true);
        */
        
        //Never go backward strategy
        Map<Integer,Integer> reachable = positionToIndex.
                subMap(curPos , false, curPos + currentSwingLength, true);
        
        for(int ri : reachable.values())
        {
            int d = Math.abs(curPos - in.ropePosition[ri]);
            
            Preconditions.checkState(d <= currentSwingLength);
            
            int riSwingLength = Math.min(in.ropeLength[ri], d);
            
            int curSwingLength = toVisit.keyOf(ri);
            
            if (riSwingLength > curSwingLength) {
                toVisit.increaseKey(ri, riSwingLength);
            }
            
        }
    }
    

    return String.format("Case #%d: %s", in.testCase, found ? "YES" : "NO");
} 

}
