package codejam.y2008.round_1B.number_sets;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import ch.qos.logback.classic.Level;
import codejam.utils.datastructures.UF;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.Prime;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;

import static com.google.common.base.Preconditions.*;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    
    public Main()
    {
        super("B",1,1);
        (( ch.qos.logback.classic.Logger) log).setLevel(Level.DEBUG);
    }
    
    
   
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
       
        InputData in = new InputData(testCase);
       
        in.A = scanner.nextLong();
        in.B = scanner.nextLong();
        in.P = scanner.nextLong();
        
        return in;
    }
    
    static List<Integer> knownPrimes = null;

    public String handleSmall(InputData in) {
        
        if (knownPrimes == null )
        {
            knownPrimes = Prime.generatePrimes(1000000);
        }
        
        Map<Long, Integer> numToIndex = Maps.newHashMap();
        
        UF uf = new UF(knownPrimes.size() + Ints.checkedCast(in.B - in.A) + 1) ;
        //map these primes and each input number to an index
        for(int i = 0; i < knownPrimes.size(); ++i)
        {
            numToIndex.put((long) knownPrimes.get(i), i);
        }
        
        for(long num = in.A; num <= in.B; ++num)
        {
            int numIdx = Ints.checkedCast(num - in.A) + knownPrimes.size();
            for(int i = 0; i < knownPrimes.size(); ++i)
            {
                int prime = knownPrimes.get(i);
                
                if (prime < in.P)
                    continue;
                
                if (prime > num)
                    break;
                
                if (num % prime == 0) {
                    //log.debug("Num {} divisible by {}", num, prime);
                    uf.union(i, numIdx);
                }
            }
        }
        
        Set<Integer> sets = Sets.newHashSet();
        
        int count  = 0;
        for(long num = in.A; num <= in.B; ++num)
        {
            int numIdx = Ints.checkedCast(num - in.A) + knownPrimes.size();
            sets.add(uf.find(numIdx));
                
        }
         count += sets.size();
        //int count = uf.count() - knownPrimes.size();
        
        
        
        
        return String.format("Case #%d: %d ", in.testCase, count);
        
    }
        
    @Override
    public String handleCase(InputData in) {
       
        if (knownPrimes == null )
        {
            knownPrimes = Prime.generatePrimes(1000000);
        }
        
        
        
        UF uf = new UF( Ints.checkedCast(in.B - in.A) + 1) ;
        //map these primes and each input number to an index
        for(int i = 0; i < knownPrimes.size(); ++i)
        {
            int prime = knownPrimes.get(i);
            
            if (prime < in.P)
                continue;
            
            if (prime > in.B)
                break;
            
            long start = in.A  - in.A % prime;
            
            
            while(start < in.A)
            {
                start += prime;
            }
            
            checkState(start >= in.A && start - prime < in.A);
            
            
            int componentIdx = Ints.checkedCast(start - in.A);
            
            for(long num = start + prime; num <= in.B; num += prime)
            {
                checkState(num >= in.A && num <= in.B);
                checkState(num % prime == 0);
                
                int curIdx = Ints.checkedCast(num - in.A);
                
                uf.union(curIdx, componentIdx);                
            }
            
        }
        
        
        int count  = uf.count();
        
        return String.format("Case #%d: %d ", in.testCase, count);
        
    }
        
        
        
}