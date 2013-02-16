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
            int[] ft = FenwickTree.ft_create(10);
            
            FenwickTree.ft_adjust(ft, 1, 1, 100);
            FenwickTree.ft_adjust(ft, 3, 1, 100);
            FenwickTree.ft_adjust(ft, 4, 1, 100);
            FenwickTree.ft_adjust(ft, 7, 1, 100);
            FenwickTree.ft_adjust(ft, 10, 1, 100);
            
            assertEquals(7, FenwickTree.findIndexWithFreq(ft, 4));
        }
        
    
    
    @Override
    public String handleCase(InputData in) {
       
        
        
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
        
        
        
}