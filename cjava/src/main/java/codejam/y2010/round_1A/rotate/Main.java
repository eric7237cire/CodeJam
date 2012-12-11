package codejam.y2010.round_1A.rotate;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.Direction;
import codejam.utils.utils.Grid;

import com.google.common.base.Objects;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    @Override
    public String handleCase(InputData input) {

        log.info("Starting calculating case {}", input.testCase);
        
        //double ans = DivideConq.findMinPerimTriangle(input.points);
        Grid<Character> rotate = new Grid<Character>(input.grid);
        
        for(int r = 0; r < rotate.getRows(); ++r) {
            for(int c = 0; c < rotate.getCols(); ++c) {
                rotate.setEntry(r, c, input.grid.getEntry(input.gridSize - c - 1,  r) );
            }
        }

        log.info("Done calculating answer case {} rotate {}", input.testCase, rotate);
        
        //falling
        for(int c = 0; c < rotate.getCols(); ++c) {
            for(int r = rotate.getRows() - 1; r > 0; --r) {
                if ('.' == rotate.getEntry(r, c)) {
                    for(int r2 = r; r2 >= 0; --r2) {
                        if (rotate.getEntry(r2, c) != '.') {
                            rotate.setEntry(r, c, rotate.getEntry(r2, c));
                            rotate.setEntry(r2, c, '.');
                            break;
                        }
                    }
                }
            }
        }
        
        log.info("AFter fall {}", rotate);
        
        int redBlue = 0;
        
        for(int c = 0; c < rotate.getCols(); ++c) {
            for(int r = rotate.getRows() - 1; r > 0; --r) {
                if (rotate.getEntry(r, c) == '.') 
                    continue;
                
                Character color = rotate.getEntry(r, c);
                
                int count = 1;
                int rr = r, cc = c;
                while(color == rotate.getEntry(rr, cc, Direction.NORTH)) {
                    ++count;
                    --rr;
                }
                if (count >= input.k) {
                    redBlue |= 1 << (color == 'B' ? 1 : 0);
                }
                rr = r; cc = c; count=1;
                while(Objects.equal(color ,rotate.getEntry(rr, cc, Direction.EAST))) {
                    ++count;
                    ++cc;
                }
                if (count >= input.k) {
                    redBlue |= 1 << (color == 'B' ? 1 : 0);
                }
                rr = r; cc = c; count=1;
                while(Objects.equal(color ,rotate.getEntry(rr, cc, Direction.NORTH_EAST))) {
                    ++count;
                    ++cc;
                    --rr;
                }
                if (count >= input.k) {
                    redBlue |= 1 << (color == 'B' ? 1 : 0);
                }
                rr = r; cc = c; count=1;
                while(Objects.equal(color ,rotate.getEntry(rr, cc, Direction.NORTH_WEST))) {
                    ++count;
                    --cc;
                    --rr;
                }
                if (count >= input.k) {
                    redBlue |= 1 << (color == 'B' ? 1 : 0);
                }
            }
        }
        
        String ans = "Neither";
        
        if (redBlue == 3) {
            ans = "Both";
        } else if (redBlue == 2) {
            ans = "Blue";
        } else if (redBlue == 1) {
            ans = "Red";
        }
        
        
        //DecimalFormat decim = new DecimalFormat("0.00000000000");
        //decim.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        
        return ("Case #" + input.testCase + ": " + ans);
    }
    
    
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        
    
        
        BiMap<Character, Character> map = HashBiMap.create();
        map.put('.', '.');
        map.put('B', 'B');
        map.put('R', 'R');
        InputData  i = new InputData(testCase);
        i.gridSize = scanner.nextInt();
        i.k = scanner.nextInt();
        
        i.grid = Grid.buildFromScanner(scanner, i.gridSize, i.gridSize, map,null);
        
        log.info("Reading data...Test case # {} grid {} ", testCase, i.grid);
        
        log.info("Done Reading data...Test case # {} ", testCase);
        
        
        return i;
        
    }

    
    
}