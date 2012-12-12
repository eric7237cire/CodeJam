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

       // String dir = ".";
        //String[] files = new String[] { "a", "b", "c" };

        //String dir = ".\\src\\main\\resources\\y2008\\apac";
        //codejam.y2008.round_apac.millionaire.Main m = new codejam.y2008.round_apac.millionaire.Main();
        //String[] files = new String[] { "sample.in" };
        //String[] files = new String[] { "C-small-practice.in", "C-large-practice.in" };
        //String[] files = new String[] { "C-small-practice.in"  };
        
        //codejam.y2008.round_apac.modern_art.Main m = new codejam.y2008.round_apac.modern_art.Main();
        
         //Round 1B (2)
         //codejam.y2009.round_1B.next_number.Main m = new codejam.y2009.round_1B.next_number.Main();
         //codejam.y2009.round_1B.square_math.Main m = new codejam.y2009.round_1B.square_math.Main();
         
         //Round 1C      
         //codejam.y2009.round_1C.all_your_base.Main m = new codejam.y2009.round_1C.all_your_base.Main();
         //codejam.y2009.round_1C.center_mass.Main m = new codejam.y2009.round_1C.center_mass.Main();
         //codejam.y2009.round_1C.bribe_prisoner.Main m = new codejam.y2009.round_1C.bribe_prisoner.Main();
         
         //Round 2
        String dir = ".\\src\\main\\resources\\y2009\\2";
         //codejam.y2009.crazy_rows.Main m = new codejam.y2009.crazy_rows.Main();
         //codejam.y2009.digging_problem.Main m = new codejam.y2009.digging_problem.Main();
         codejam.y2009.round_2.stock_charts.Main m = new codejam.y2009.round_2.stock_charts.Main();
        String[] files = new String[] { "C-small-practice.in", "C-large-practice.in" };
        
         //codejam.y2009.watering_plants.Main m = new codejam.y2009.watering_plants.Main();
         
         //Round 3
         //codejam.y2009.sokoban.Main m = new codejam.y2009.sokoban.Main();
         //codejam.y2009.alphabetomials.Main m = new codejam.y2009.alphabetomials.Main();
        // codejam.y2009.round_3.football_team.Main m = new codejam.y2009.round_3.football_team.Main();
         //codejam.y2009.interesting_ranges.Main m = new codejam.y2009.interesting_ranges.Main();
         
       //Round 4 -- 2009
         //codejam.y2009.year_more.Main m = new codejam.y2009.year_more.Main();
         
         //Both of these still a bit slow, especially min perimeter
         //codejam.y2009.min_perimeter.Main m = new codejam.y2009.min_perimeter.Main();
       //Runner.go(args[0], m, m, new codejam.y2009.min_perimeter.InputData(-1), 3);
        // codejam.y2009.double_sort_grid.Main m = new codejam.y2009.double_sort_grid.Main();
      // Runner.go(args[0], m, m, new codejam.y2009.double_sort_grid.InputData(-1), 3);

         //2010 qual
         //codejam.y2010.fair_warning.Main m = new codejam.y2010.fair_warning.Main();
         //codejam.y2010.theme_park.Main m = new codejam.y2010.theme_park.Main();
         
         //2010 1A
         //codejam.y2010.rotate.Main m = new codejam.y2010.rotate.Main();
         //codejam.y2010.smooth.Main m = new codejam.y2010.smooth.Main();
         //codejam.y2010.number_game.Main m = new codejam.y2010.number_game.Main();
         
         //2010 1B
         
         //codejam.y2010.file_fix.Main m = new codejam.y2010.file_fix.Main();
         //codejam.y2010.chicks.Main m = new codejam.y2010.chicks.Main();
         //codejam.y2010.rank_pure.Main m = new codejam.y2010.rank_pure.Main();
         
         //2010 1C
         //large a bit slow
         //codejam.y2010.chess_boards.Main m = new codejam.y2010.chess_boards.Main();
         //codejam.y2010.load_testing.Main m = new codejam.y2010.load_testing.Main();         
         //codejam.y2010.rope_intranet.Main m = new codejam.y2010.rope_intranet.Main();

         //2010 2
         //dir = ".\\src\\main\\resources\\y2010\\2";
         //TODO large slow
         //codejam.y2010.round_2.diamond.Main m = new codejam.y2010.round_2.diamond.Main();
         //files = new String[] { "A-small-practice.in", "A-large-practice.in" };
         
         //codejam.y2010.round_2.world_cup.Main m = new codejam.y2010.round_2.world_cup.Main();
         //files = new String[] { "B-small-practice.in", "B-large-practice.in" };
         
         //codejam.y2010.round_2.bacteria.Main m = new codejam.y2010.round_2.bacteria.Main();
         //files = new String[] { "C-small-practice.in", "C-large-practice.in" };
         
         //codejam.y2010.round_2.goats.Main m = new codejam.y2010.round_2.goats.Main();
         //TODO correct
         //files = new String[] { "D-small-practice.in", "D-large-practice.in" };
         
         //2010 3
         //String dir = ".\\src\\main\\resources\\y2010\\3";
         
         //codejam.y2010.round_3.rng.Main m = new codejam.y2010.round_3.rng.Main();
         //files = new String[] { "A-small-practice.in", "A-large-practice.in" };
         
         //codejam.y2010.round_3.hotdog.Main m = new codejam.y2010.round_3.hotdog.Main();
         //files = new String[] { "C-small-practice.in", "C-large-practice.in" };
         
       // String dir = ".\\src\\main\\resources\\y2011\\1A";
         //2011 1A

        
        //TODO large is quite slow 14 secs
         //codejam.y2011.round_1A.killer_word.Main m = new codejam.y2011.round_1A.killer_word.Main();
        //String[] files = new String[] { "B-small-practice.in", "B-large-practice.in" };
        
         //codejam.y2011.round_1A.dominion.Main m = new codejam.y2011.round_1A.dominion.Main();
         
         //2011 1B
         //codejam.y2011.round_1B.hotdog_revenge.Main m = new codejam.y2011.round_1B.hotdog_revenge.Main();
         //codejam.y2011.round_1B.house_kittens.Main m = new codejam.y2011.round_1B.house_kittens.Main();
         
        
        if (args.length >= 1) {
            //args = new String[] { "sample.txt" };
           // args = new String[] { "B-small-practice.in" };
            //args = new String[] { "C:\\codejam\\CodeJam\\cjava\\src\\main\\resources\\y2011\\1B\\C-small-practice.in" };
            files = args;
         }
        for (String file : files) {
         log.info("Input file {}", file);
         
         Runner.goSingleThread(dir + "\\" + file, m, m);
        }
         

        
       
    }

    
}