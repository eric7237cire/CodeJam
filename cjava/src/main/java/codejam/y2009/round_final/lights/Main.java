package codejam.y2009.round_final.lights;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.geometry.Circle;
import codejam.utils.geometry.PointInt;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.collect.Lists;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    public Main()
    {
        super("E");
    }
    
    
   
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
       
        InputData in = new InputData(testCase);
        /*
         * One line containing the coordinates x, y of the 
         * red light source.
    One line containing the coordinates x, y of the green light 
    source.
    One line containing the number of pillars n.
    n lines describing the pillars. 
    Each contains 3 numbers x, y, r. 
    The pillar is a disk with the center (x, y) and radius r.

         */
        in.redLight = new PointInt(scanner.nextInt(), scanner.nextInt());
        in.greenLight = new PointInt(scanner.nextInt(), scanner.nextInt());
        in.N = scanner.nextInt();
        
        in.pillars = Lists.newArrayList();
        for(int i = 0; i < in.N; ++i) {
            in.pillars.add(new Circle(scanner.nextInt(),scanner.nextInt(),scanner.nextInt()));
        }
        return in;
    }

  
    @Override
    public String handleCase(InputData in) {
       

        return String.format("Case #%d: ", in.testCase);
    }
}