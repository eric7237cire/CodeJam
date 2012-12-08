package codejam.y2009.round_1C.center_mass;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

	

	final static Logger log = LoggerFactory.getLogger(Main.class);

	
    /* (non-Javadoc)
     * @see codejam.utils.main.Runner.TestCaseInputScanner#readInput(java.util.Scanner, int)
     */
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        int numFireflies = scanner.nextInt();

        double[] coordsAvg = new double[3];
        int[] coords = new int[3];

        double[] velocityAvg = new double[3];
        int[] velocity = new int[3];

        for (int i = 0; i < numFireflies; ++i) {
            for (int j = 0; j < 3; ++j) {
                coords[j] = scanner.nextInt();              
                coordsAvg[j] += coords[j];
                log.debug("ff {} coords {} cord avg {}", new Object[] {i, coords[j], coordsAvg[j] } );
            }
            for (int j = 0; j < 3; ++j) {
                velocity[j] = scanner.nextInt();
                velocityAvg[j] += velocity[j];
            }
        }
        InputData input = new InputData(testCase);
        input.numFireflies = numFireflies;
        input.coords = coords;
        input.velocity = velocity;
        input.coordsAvg = coordsAvg;
        input.velocityAvg = velocityAvg;
        return input;
    }

    /* (non-Javadoc)
     * @see codejam.utils.multithread.Consumer.TestCaseHandler#handleCase(java.lang.Object)
     */
    @Override
    public String handleCase(InputData input) {
        int numFireflies = input.numFireflies;

        double[] coordsAvg = input.coordsAvg;
        //int[] coords = new int[3];

        double[] velocityAvg = input.velocityAvg;
//        int[] velocity = new int[3];

        

        for (int j = 0; j < 3; ++j) {
            coordsAvg[j] = coordsAvg[j] / (double) numFireflies;
            velocityAvg[j] = velocityAvg[j] / (double) numFireflies;
            
            log.debug("Dimension {} coord {} velocity {}",  j,coordsAvg[j], velocityAvg[j] );
        }
        
        //find where derivative of distance formula is 0
        
        double a = 0;
        double b = 0;

        double vSum = 0;
        for (int j = 0; j < 3; ++j) { 
            a += 2 * coordsAvg[j] * velocityAvg[j];
            b += 2 * velocityAvg[j] * velocityAvg[j] ;
            vSum += velocityAvg[j];
        }
        double t = 0;
        
        if (b != 0) {
            t = -a / b;
        }
        
        log.info("caseNumber {} is {} a {} b {}", input.testCase, t, a, b);
        if (t < 0) {
            //t = 0;
            for (int j = 0; j < 3; ++j) {
                log.info("CN {} Dimension {} coord {} velocity {}", input.testCase, j,coordsAvg[j], velocityAvg[j] );
            }
            t = 0;
        }
        
        double d = 0;
        
        for (int j = 0; j < 3; ++j) {
            d += (coordsAvg[j] + velocityAvg[j] * t)* (coordsAvg[j] + velocityAvg[j] * t);
        }
        
        d = Math.sqrt(d);
        
        log.info("D is " + d);
        
        DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance(Locale.US);
        NumberFormat df = new DecimalFormat("#.######", dfs);
        return ("Case #" + input.testCase + ": " + df.format(d) + " " + df.format(t));
    }
}