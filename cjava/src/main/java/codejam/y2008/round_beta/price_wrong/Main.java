package codejam.y2008.round_beta.price_wrong;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.ListCompare;
import codejam.utils.utils.ListCompare.ListStringComparator;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    public Main()
    {
     //   super();
        super("B", true,true);
    }
    
    
    @Override
    public InputData readInput(Scanner scanner, int testCase)
    {

        InputData in = new InputData(testCase);
        
        scanner.nextLine();
        
        String str = scanner.nextLine();
        
        
        String[] words = str.split(" ");
        
       // str = scanner.nextLine();
        
        log.debug("Words {}", (Object)words);
        in.words = Lists.newArrayList(words);
        
        in.prices = Lists.newArrayList();
        
        for(int i = 0; i < words.length; ++i) {
            in.prices.add(scanner.nextInt());
        }

        return in;
    }

    
        
    @Override
    public String handleCase(InputData in)
    {
        int N = in.words.size();
        List<List<String>> longestSequence = Lists.newArrayList();
        
        for(int i = 0; i < N; ++i) {
            List<String> words = Lists.newArrayList();
            words.add(in.words.get(i));
            longestSequence.add(words);
        }
        
        ListCompare.ListStringComparator cmp = new ListStringComparator();
        
        for(int i = 0; i < N; ++i)
        {
            for(int j = 0; j < i; ++j) 
            {
                if (in.prices.get(j) < in.prices.get(i)) {
                    List<String> newSequence = Lists.newArrayList(longestSequence.get(j));
                    newSequence.add(in.words.get(i));
                    
                    Collections.sort(newSequence);
                    
                    /**
                     * Since we want the lowest lexographic decreasing sequence,
                     * we want the highest lexographic increasing sequence
                     */
                    
                    boolean longer_sequence = newSequence.size() > longestSequence.get(i).size();
                    
                    boolean later_sequence = newSequence.size() == longestSequence.get(i).size() &&
                            cmp.compare(newSequence, longestSequence.get(i)) > 0;
                         
                  /*  log.debug("huh {} new size {} long size {}", cmp.compare(newSequence, longestSequence.get(i)),
                            newSequence.size(), longestSequence.get(i).size());
                    log.debug("i {} j {} New sequence {} longer {} later {}  current longest {}",
                            i,j,newSequence, longer_sequence, later_sequence,  longestSequence.get(i)); 
                    */        
                    if (longer_sequence || later_sequence) {
                        longestSequence.set(i, newSequence);
                    }
                }
            }
        }             
        
        Ordering<List<String>> reverse = Ordering.from(cmp).reverse();
        
        Collections.sort(longestSequence, reverse);
        
        List<String> longestIncSequence = longestSequence.get(0);
        
        List<String> rest = Lists.newArrayList();
        
        for(String word : in.words) {
            if (!longestIncSequence.contains(word)) {
                rest.add(word);
            }
        }
        
        Collections.sort(rest);
        
        return "Case #" + in.testCase + ": " + Joiner.on(' ').join(rest);
    }

}