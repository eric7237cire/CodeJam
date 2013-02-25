package codejam.y2009.round_final.min_perimeter;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ch.qos.logback.classic.Level;
import codejam.utils.geometry.PointInt;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.DoubleFormat;

public class MinPerimeter extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>{

    public MinPerimeter() {
        super("B", 1,1);
        (( ch.qos.logback.classic.Logger) log).setLevel(Level.INFO);
    }

    @Override
    public String handleCase(InputData input) {

        
        
        log.info("Starting calculating case {}", input.testCase);
        
        double ans = DivideConq.findMinPerimTriangle(input.points);

        log.info("Done calculating answer case {}", input.testCase);
        
        
        return ("Case #" + input.testCase + ": " + DoubleFormat.df7.format(ans));
                

    }
    
    
    @Override
    public InputData readInput(Scanner scanner, int testCase)  {
        
    
        int n = scanner.nextInt();
        
        List<PointInt> points = new ArrayList<>(n);
        
        log.info("Reading data...Test case # {} ", testCase);
        
        for (int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            points.add(new PointInt(x,y));
        }
        log.info("Done Reading data...Test case # {} ", testCase);
        
        InputData  i = new InputData(testCase);
        i.points = points;
        return i;
        
    }

    
}