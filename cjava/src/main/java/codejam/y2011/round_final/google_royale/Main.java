package codejam.y2011.round_final.google_royale;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.collect.Lists;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    public Main() {
        super("E", 0, 0);
    }
    @Override
    public InputData readInput(Scanner scanner, int testCase) {

        InputData in = new InputData(testCase);

        in.A = scanner.nextLong();
        in.M = scanner.nextLong();
        in.V = scanner.nextLong();
        
        return in;
    }
    
    public void testObservation2() {
        //Say A = 95, V = 100, M = 50 and we bet 5
        
        int A = 95;
        int V = 100;
        int M = 240;
        
        int simulations = 10000000;
        
        //"Strategy is to bet 5 and keep doubling
        int hitVCount = 0;
        int loseCount = 0;
        int lostMoney = 0;
        int totalRounds = 0;
        
        Random r = new Random();
        
        nextSimul:
        for(int simul = 0; simul < simulations; ++simul)
        {
            int bet = 5;
            int lost = 0;
        
            ++totalRounds;
            
            while(bet <= M )
            {
                boolean won = r.nextBoolean();
                if (won) {
                    ++hitVCount;
                    continue nextSimul;
                } 
                
                lost += bet;
                bet*=2;
                
            }
            
            loseCount++;
            lostMoney += lost;
        }
        
        log.debug("Won {}, lost {}, expected lose money {}  expected value {}",
                100.0 * hitVCount / totalRounds,
                100.0 * loseCount / totalRounds,
                lostMoney * 1.0d / loseCount,
                1.0d * hitVCount / totalRounds * 5 +
                -1.0d * loseCount / totalRounds * lostMoney / loseCount
                );
    }
    
    

    public String handleCase(InputData in) {

        testObservation2();
        
        return String.format("Case #%d: ", in.testCase);        
    }

}
