package com.eric.codejam;

import java.io.BufferedReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.geometry.Circle;
import com.eric.codejam.geometry.Point;
import com.eric.codejam.geometry.PointInt;
import com.eric.codejam.main.Runner;
import com.eric.codejam.multithread.Consumer.TestCaseHandler;
import com.eric.codejam.multithread.Producer.TestCaseInputReader;

public class Main implements TestCaseHandler<InputData>, TestCaseInputReader<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    @Override
    public String handleCase(int caseNumber, InputData input) {

        log.info("Starting calculating case {}", caseNumber);
        
        //double ans = DivideConq.findMinPerimTriangle(input.points);
        for(PointInt bucketPos : input.bucketPositions) {
            Circle[] circles = new Circle[input.goatPolePositions.length];
            for(int gp = 0; gp < input.N; ++gp) {
                PointInt goatPos = input.goatPolePositions[gp];
                circles[gp] = new Circle(goatPos.getX(), goatPos.getY(), goatPos.distance(bucketPos));
            }
            
            
        }

        log.info("Done calculating answer case {}", caseNumber);
        
        //DecimalFormat decim = new DecimalFormat("0.00000000000");
        //decim.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        
        return ("Case #" + caseNumber + ": " );
    }
    
    
   
    
    @Override
    public InputData readInput(BufferedReader br, int testCase) throws IOException {
        
    
        String[] line = br.readLine().split(" ");
        
        InputData  input = new InputData(testCase);
        
        input.N = Integer.parseInt(line[0]);
        input.M = Integer.parseInt(line[1]);
        
        input.goatPolePositions = new PointInt[input.N];
        input.bucketPositions = new PointInt[input.M];
        
        for(int n = 0; n < input.N; ++n) {
            line = br.readLine().split(" ");
            input.goatPolePositions[n] = new PointInt(Integer.parseInt(line[0]),Integer.parseInt(line[1]));
        }
        
        for(int m = 0; m < input.M; ++m) {
            line = br.readLine().split(" ");
            input.bucketPositions[m] = new PointInt(Integer.parseInt(line[0]),Integer.parseInt(line[1]));
        }
        
        return input;
        
    }

    


    public Main() {
        super();
    }
    
    
    public static void main(String args[]) throws Exception {

        if (args.length < 1) {
            args = new String[] { "sample.txt" };
           // args = new String[] { "B-small-practice.in" };
//            args = new String[] { "B-large-practice.in" };
         }
         log.info("Input file {}", args[0]);

         Main m = new Main();
         Runner.goSingleThread(args[0], m, m);
         //Runner.go(args[0], m, m, new InputData(-1));

        
       
    }

    
}