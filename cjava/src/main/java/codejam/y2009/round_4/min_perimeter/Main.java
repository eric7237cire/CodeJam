package codejam.y2009.round_4.min_perimeter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.geometry.PointInt;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>{

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String handleCase(InputData input) {

        
        
        log.info("Starting calculating case {}", input.testCase);
        
        double ans = DivideConq.findMinPerimTriangle(input.points);

        log.info("Done calculating answer case {}", input.testCase);
        
        DecimalFormat decim = new DecimalFormat("0.00000000000");
        decim.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        
        return ("Case #" + input.testCase + ": " + decim.format(ans));
                

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