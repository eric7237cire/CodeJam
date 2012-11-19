package com.eric.codejam;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.InputData;
import com.eric.codejam.Main;
import com.eric.codejam.geometry.PointInt;
import com.eric.codejam.main.Runner;
import com.eric.codejam.multithread.Consumer.TestCaseHandler;
import com.eric.codejam.multithread.Producer.TestCaseInputReader;

public class Main implements TestCaseHandler<InputData>, TestCaseInputReader<InputData> {

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
    public String handleCase(int caseNumber, InputData input) {

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
    public InputData readInput(BufferedReader br, int testCase) throws IOException {
        
    
        String[] line = br.readLine().split(" ");
        InputData  i = new InputData(testCase);
        
        int n;
        i.rides = Integer.parseInt(line[0]);
        i.capacity = Integer.parseInt(line[1]);
        n = Integer.parseInt(line[2]);
        
        i.groupSizes = new ArrayList<>(n);
        
        line = br.readLine().split(" ");
        for(int g = 0; g < n; ++g) {
            i.groupSizes.add(Integer.parseInt(line[g]));
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