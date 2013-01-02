package codejam.y2011.round_2.airport_walkways;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.DoubleFormat;

import com.google.common.collect.Lists;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
        // return new String[] {"sample.in"};
        return new String[] { "A-small-practice.in", "A-large-practice.in" };
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {


        /*
         * The first line of the input gives the number of test cases, T.
         *  T test cases follow. Each test case begins with a line containing
    five integers: X (the length of the corridor, in meters),
     S (your walking speed, in meters per second), 
     R (your running speed, in meters per second),
      t (the maximum time you can run, in seconds) and N (the number of walkways).

    Each of the next N lines contains three integers: 
    Bi, Ei and wi - the beginning and end of the walkway
     (in meters from your starting point) and the speed of the walkway (in meters per second).
         */
        InputData in = new InputData(testCase);
        in.X = scanner.nextInt();        
        in.S = scanner.nextInt();
        in.R = scanner.nextInt();
        in.t = scanner.nextInt();
        in.N = scanner.nextInt();
        
        in.walkways = new InputData.Walkway[in.N];
        for(int n = 0; n < in.N; ++n) {
            in.walkways[n] = new InputData.Walkway();
            in.walkways[n].B = scanner.nextInt();
            in.walkways[n].E = scanner.nextInt();
            in.walkways[n].w = scanner.nextInt();            
        }

        return in;
    }

    public String handleCase(InputData in) {

        //speed pairs ==> distance
        List<MutablePair<Integer,Integer>> speedDistanceList = Lists.newArrayList();
        
        int totalWalkwayDistance = 0;
        for(int n = 0; n < in.N; ++n) {
            int distance = in.walkways[n].E - in.walkways[n].B;
            totalWalkwayDistance += distance;
            speedDistanceList.add(new MutablePair<>(in.walkways[n].w + in.S, distance));
        }
        
        speedDistanceList.add(new MutablePair<>(in.S, in.X - totalWalkwayDistance));
        
        Collections.sort(speedDistanceList);
        
        double tRunningLeft = in.t;
        double totalTime = 0;
        
        int runningAddition = in.R - in.S;
        
        for(int i = 0; i < speedDistanceList.size(); ++i) {
            MutablePair<Integer,Integer> speedDistance = speedDistanceList.get(i);
            double distanceLeft = speedDistance.getRight();
            
            
            if (tRunningLeft > 0) {
                //How long to run this distance
                double timeNeeded = (double) speedDistance.getRight() / (speedDistance.getLeft() + runningAddition);
            
                if (timeNeeded > tRunningLeft) {
                    //Run for tRunningLeft
                    double distanceCovered = (speedDistance.getLeft() + runningAddition) * tRunningLeft;
                    totalTime += tRunningLeft;
                    distanceLeft -= distanceCovered;
                    tRunningLeft = 0;
                } else {
                    totalTime += timeNeeded;
                    distanceLeft = 0;
                    tRunningLeft -= timeNeeded;
                }
                
            }
            
            
            double timeTaken = distanceLeft / speedDistance.getLeft();
            totalTime += timeTaken;
        }
    
        return String.format("Case #%d: %s", in.testCase, DoubleFormat.df6.format(totalTime));
    }

}
