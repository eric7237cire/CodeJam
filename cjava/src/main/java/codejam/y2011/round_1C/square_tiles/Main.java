package codejam.y2011.round_1C.square_tiles;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.Direction;
import codejam.utils.utils.GridChar;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles{

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    @Override
    public String[] getDefaultInputFiles() {
 //       return new String[] {"sample.in"};
        return new String[] {"A-small-practice.in", "A-large-practice.in"};
    }
    
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        InputData in = new InputData(testCase);
        in.R = scanner.nextInt();
        in.C = scanner.nextInt();
        in.grid = GridChar.buildFromScanner(scanner,in.R,in.C, '.');
        
        return in;
    }
    
    
    @Override
    public String handleCase(InputData input) {
        
        GridChar grid = input.grid;
        
        for(int r = 0; r < input.R; ++r) {
            for(int c = 0; c < input.C; ++c) {
                if (grid.getEntry(r,c) == '#') {
                    if(grid.getEntry(r,c,Direction.EAST) != '#' ||
                            grid.getEntry(r,c,Direction.SOUTH) != '#' ||
                            grid.getEntry(r,c,Direction.SOUTH_EAST) != '#') {
                        return String.format("Case #%d:\nImpossible", input.testCase);
                    }
                    
                    grid.setEntry(r,c, '/');
                    grid.setEntry(r, c+1, '\\');
                    grid.setEntry(r+1, c, '\\');
                    grid.setEntry(r+1, c+1, '/');
                }
            }
        }
        
                
        return String.format("Case #%d:\n%s", input.testCase, grid.toStringCompact());
    }

}
