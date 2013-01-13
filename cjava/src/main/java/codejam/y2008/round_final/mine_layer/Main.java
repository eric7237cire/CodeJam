package codejam.y2008.round_final.mine_layer;


import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.Grid;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
    //     return new String[] {"sample.in"};
     //    return new String[] { "C-small-practice.in" };
       return new String[] { "C-small-practice.in", "C-large-practice.in" };
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {

        
        InputData in = new InputData(testCase);
       
        in.R = scanner.nextInt();
        in.C = scanner.nextInt();
        
        in.grid = Grid.buildFromScanner(scanner, in.R, in.C, Grid.fromScannerInt, 0);
        
        return in;
    }

    int countRow(Grid<Integer> grid, int row) {
        int startCol = grid.getCols() % 3 != 0 ? 0 : 1;
        
        int sumMines = 0;
        for(int c = startCol; c < grid.getCols(); c+=3) {
            sumMines += grid.getEntry(row, c);
        }
        
        return sumMines;
    }
    
    public String handleCase(InputData in) {

        int startRow = 1;
        
        if (in.R % 3 != 0)
            startRow = 0;
        
        int sumMines = 0;
        
        for(int r = startRow; r < in.R; r+=3) {
            sumMines += countRow(in.grid, r);
        }
        
        int h = (in.R - 1) / 2;
        int ans = 0;
        
        if (h % 3 == 1) {
            //Sum the first half and last half overlapping middle row
            int sumOverlapping = 0;
            // TotalRest + 2M - total = M
            for(int r = h - 1; r >= 0; r -= 3) {
                sumOverlapping += countRow(in.grid, r);
            }
            for(int r = h + 1; r < in.R; r += 3) {
                sumOverlapping += countRow(in.grid, r);
            }
            ans = sumOverlapping - sumMines;
        } else {
            int sumNotTouchingMiddle =  0;
            //sum the rows that are not touching the middle row
            for(int r = h - 2; r >= 0; r -= 3) {
                sumNotTouchingMiddle += countRow(in.grid, r);
            }
            for(int r = h + 2; r < in.R; r += 3) {
                sumNotTouchingMiddle += countRow(in.grid, r);
            }
            ans = sumMines - sumNotTouchingMiddle;
        }
        
        
        return String.format("Case #%d: %d", in.testCase, ans);        
    }

}
