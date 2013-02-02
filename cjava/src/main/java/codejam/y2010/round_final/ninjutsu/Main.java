package codejam.y2010.round_final.ninjutsu;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import codejam.utils.geometry.PointInt;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    public Main()
    {
        super("E", true, true);
    }
    
    
    @Override
    public InputData readInput(Scanner scanner, int testCase)
    {

        InputData in = new InputData(testCase);
        
        in.N = scanner.nextInt();
        in.R = scanner.nextInt();
        
        in.points  = Lists.newArrayList();
        
        for(int n = 0; n < in.N; ++n) {
            in.points.add(new PointInt(scanner.nextInt(), scanner.nextInt()));
        }

        return in;
    }

    @Override
    public String handleCase(InputData in) {
       
      //  BruteForce bf = new BruteForce();

      //  String ans = bf.handleCase(in);
        
        Dynamic d = new Dynamic();
        return d.handleCase(in);
    }
    
}