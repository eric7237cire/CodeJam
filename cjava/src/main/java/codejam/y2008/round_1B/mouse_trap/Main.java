package codejam.y2008.round_1B.mouse_trap;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import codejam.utils.datastructures.FenwickTree;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    
    public Main()
    {
        super("C", 1,0, 1);
      //  (( ch.qos.logback.classic.Logger) log).setLevel(Level.INFO);
    }
    
    
   
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
       
        InputData in = new InputData(testCase);
       
        in.K = scanner.nextInt();
        in.n = scanner.nextInt();
        
        in.d = new int[in.n];
        
        for(int i = 0; i < in.n; ++i)
        {
            in.d[i] = scanner.nextInt();
        }
        
        return in;
    }

    public String handleCaseSmall(InputData in) {
       
        int[] deck = new int[in.K];
        
        int currentIndex = 0;
        int currentCount = 1;
        
        for(int currentCard = 1; currentCard <= in.K; ++currentCard)
        {
            while(currentCount < currentCard) {
                currentIndex++;
                currentIndex %= in.K;
                
                if (deck[currentIndex] == 0) {
                    ++currentCount;
                }
            }
            
            Preconditions.checkState(deck[currentIndex] == 0);
            deck[currentIndex] = currentCard;
            
            currentCount = 0;
        }
        
        List<Integer> ans = Lists.newArrayList();
        
        for(Integer ansIdx : in.d) {
            ans.add(deck[ansIdx-1]);
        }
        
        return String.format("Case #%d: %s ", in.testCase, Joiner.on(' ').join(ans));
        
    }
    
        @Test
        public void test() 
        {
            int[] ft = FenwickTree.ft_create(15);
            
            FenwickTree.ft_adjust(ft, 1, 1, 100);
            log.debug("FT {}", ft);
            
            FenwickTree.ft_adjust(ft, 3, 1, 100);
            log.debug("FT {}", ft);
            
            FenwickTree.ft_adjust(ft, 4, 1, 100);
            log.debug("FT {}", ft);
            
            FenwickTree.ft_adjust(ft, 7, 1, 100);
            log.debug("FT {}", ft);
            
            FenwickTree.ft_adjust(ft, 10, 1, 100);
            log.debug("FT {}", ft);
            
            FenwickTree.ft_adjust(ft, 15, 1, 100);
            log.debug("FT {}", ft);
            
            
            
            
            assertEquals(1, FenwickTree.findLowestIndexWithFreq(ft, 1));
            
            assertEquals(3, FenwickTree.findLowestIndexWithFreq(ft, 2));
            
            assertEquals(4, FenwickTree.findLowestIndexWithFreq(ft, 3));
            
            assertEquals(7, FenwickTree.findLowestIndexWithFreq(ft, 4));
            
            assertEquals(10, FenwickTree.findLowestIndexWithFreq(ft, 5));
            
            assertEquals(15, FenwickTree.findLowestIndexWithFreq(ft, 6));
        }
        
    
    
    @Override
    public String handleCase(InputData in) {
       
        
        
        int[] deck = new int[in.K];
        
        int currentIndex = 0;
        
        int[] ft = FenwickTree.ft_create(in.K);
        for(int currentCard = 2; currentCard <= in.K; ++currentCard)
        {
            FenwickTree.ft_adjust(ft, currentCard, 1, in.K);
        }
        deck[0] = 1;
        
        for(int currentCard = 2; currentCard <= in.K; ++currentCard)
        {
            Preconditions.checkState(deck[currentIndex] != 0);
            
            int cardsLeft = in.K - currentCard + 1;
            
            //Loop as much as possible
            int nCardsToSkip = currentCard ;
            
            nCardsToSkip %= cardsLeft;
            if (nCardsToSkip == 0)
                nCardsToSkip = cardsLeft;
                           
            int cardsBeforePos = FenwickTree.ft_rsq(ft, currentIndex+1, in.K);
            
            int cardsAfterPos = cardsLeft - cardsBeforePos;
            
            int checkIndex = currentIndex;
            
            if (nCardsToSkip <= cardsAfterPos)
            {
                /**
                 * Next position to fill does not require wrapping around, find freuency
                 * [] [] cp [] [] [] []
                 * 
                 * say I wanted the 3rd position, then I want frencuency 2 + 3
                 */
                currentIndex = FenwickTree.findLowestIndexWithFreq(ft, 
                        nCardsToSkip + cardsBeforePos) - 1;
                
                Preconditions.checkState(currentIndex > checkIndex && currentIndex < in.K);
            } else {
                //Next position is somewhere before currentp position
                
                //Skip all cards up to the end
                nCardsToSkip -= cardsAfterPos;
                
                currentIndex = FenwickTree.findLowestIndexWithFreq(ft, 
                        nCardsToSkip ) - 1;
                
                Preconditions.checkState(currentIndex>= 0 && currentIndex < checkIndex);
            }
            
            Preconditions.checkState(deck[currentIndex] == 0);
            deck[currentIndex] = currentCard;
            
            FenwickTree.ft_adjust(ft, currentIndex+1, -1, in.K);
            
            
        }
        
        List<Integer> ans = Lists.newArrayList();
        
        for(Integer ansIdx : in.d) {
            ans.add(deck[ansIdx-1]);
        }
        
        return String.format("Case #%d: %s ", in.testCase, Joiner.on(' ').join(ans));
        
    }
        
        
        
}