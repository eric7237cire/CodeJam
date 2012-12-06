package codejam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        // Main m = new Main();
         //Runner.goSingleThread(args[0], m, m);
         //Runner.go(args[0], m, m, new InputData(-1));

        
       
    }

    
}