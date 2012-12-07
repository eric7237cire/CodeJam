package codejam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.Runner;

public class Main  {

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    

    


    public Main() {
        super();
    }
    
    
    public static void main(String args[]) throws Exception {

        if (args.length < 1) {
            //args = new String[] { "sample.txt" };
           // args = new String[] { "B-small-practice.in" };
            args = new String[] { "C:\\codejam\\CodeJam\\cjava\\src\\main\\resources\\y2009\\4\\C-small-practice.in" };
         }
         log.info("Input file {}", args[0]);

         //Round 4 -- 2009
         //codejam.y2009.year_more.Main m = new codejam.y2009.year_more.Main();
         //codejam.y2009.min_perimeter.Main m = new codejam.y2009.min_perimeter.Main();
       //Runner.go(args[0], m, m, new codejam.y2009.min_perimeter.InputData(-1), 3);
        // codejam.y2009.double_sort_grid.Main m = new codejam.y2009.double_sort_grid.Main();
      // Runner.go(args[0], m, m, new codejam.y2009.double_sort_grid.InputData(-1), 3);
         
         //Round 3
         //codejam.y2009.sokoban.Main m = new codejam.y2009.sokoban.Main();
         //codejam.y2009.alphabetomials.Main m = new codejam.y2009.alphabetomials.Main();
         //codejam.y2009.football_team.Main m = new codejam.y2009.football_team.Main();
         //codejam.y2009.interesting_ranges.Main m = new codejam.y2009.interesting_ranges.Main();
         
         //Round 2
         //codejam.y2009.crazy_rows.Main m = new codejam.y2009.crazy_rows.Main();
         //codejam.y2009.digging_problem.Main m = new codejam.y2009.digging_problem.Main();
         //codejam.y2009.stock_charts.Main m = new codejam.y2009.stock_charts.Main();
         //codejam.y2009.watering_plants.Main m = new codejam.y2009.watering_plants.Main();
         
         //2010 1A
         //codejam.y2010.rotate.Main m = new codejam.y2010.rotate.Main();
         //codejam.y2010.smooth.Main m = new codejam.y2010.smooth.Main();
         codejam.y2010.number_game.Main m = new codejam.y2010.number_game.Main();
         Runner.goSingleThread(args[0], m, m);
         

        
       
    }

    
}