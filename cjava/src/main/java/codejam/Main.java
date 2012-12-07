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
            args = new String[] { "sample.txt" };
           // args = new String[] { "B-small-practice.in" };
//            args = new String[] { "B-large-practice.in" };
         }
         log.info("Input file {}", args[0]);

         //Round 3
         //codejam.y2009.sokoban.Main m = new codejam.y2009.sokoban.Main();
         //codejam.y2009.alphabetomials.Main m = new codejam.y2009.alphabetomials.Main();
         //codejam.y2009.football_team.Main m = new codejam.y2009.football_team.Main();
         //codejam.y2009.interesting_ranges.Main m = new codejam.y2009.interesting_ranges.Main();
         
         //Round 2
         codejam.y2009.crazy_rows.Main m = new codejam.y2009.crazy_rows.Main();
         Runner.goSingleThread(args[0], m, m);
         //Runner.go(args[0], m, m, new InputData(-1));

        
       
    }

    
}