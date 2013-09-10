package codejam.y2013.round_qual.fairsquare;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;

import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.y2009.round_3.interesting_ranges.BruteForce;

import com.google.common.collect.Lists;
import com.google.common.math.DoubleMath;

public class FairAndSquare extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>
{
    public FairAndSquare() {
        super("C", 0, 0);
        //setLogInfo();

    }

    public String[] getDefaultInputFiles()
    {
        if (false)
        {
            return super.getDefaultInputFiles();
        } else
        {
           // return new String[] { "C-small-practice.in", "C-large-practice-1.in", "C-large-practice-2.in" };
            return new String[] { "C-small-practice.in", "C-large-practice-1.in" };
        }

    }

    /* (non-Javadoc)
     * @see codejam.utils.main.Runner.TestCaseInputScanner#readInput(java.util.Scanner, int)
     */
    @Override
    public InputData readInput(Scanner scanner, int testCase)
    {

        InputData in = new InputData(testCase);

        in.start = scanner.nextBigInteger(10);
        in.stop = scanner.nextBigInteger(10);

        return in;
    }

    /* (non-Javadoc)
     * @see codejam.utils.multithread.Consumer.TestCaseHandler#handleCase(java.lang.Object)
     */
    @Override
    public String handleCase(InputData in)
    {

        // return handleCaseBruteForce(in);
        //return handleCaseBruteForceFaster(in);
        return handleCaseBruteForceEvenFaster(in);

    }

    public StringBuffer[] toBaseThree(int n)
    {
        StringBuffer[] palin = new StringBuffer[4];
        
        palin[0] = new StringBuffer();
        palin[1] = new StringBuffer("0");
        palin[2] = new StringBuffer("1");
        palin[3] = new StringBuffer("2");
        
        
        while (n > 0)
        {
            int digit = n % 3;
            
            for(int i = 0; i < 4; ++i)
            {
                palin[i].append(digit);
                palin[i].insert(0, digit);
            }
            
            n /= 3;
        }

        return palin;
    }

    static TreeSet<BigInteger> set2 = new TreeSet<BigInteger>();

    public String handleCaseBruteForceEvenFaster(InputData in)
    {

        if (set2.size() == 0)
        {
            set2.add(BigInteger.valueOf(1));
            set2.add(BigInteger.valueOf(4));
            set2.add(BigInteger.valueOf(9));
            
            List<BigInteger> list = Lists.newArrayList();
            
            for (int i = 1; i < 500; ++i)
            {
                StringBuffer[] palinBase3 = toBaseThree(i);
               // log.debug("palinBase3 {}", palinBase3);

                for (int j = 0; j < 4; ++j)
                {
                    BigInteger palin = new BigInteger(palinBase3[j].toString());
                    
                 //   log.debug("Éventuelle racine carrée {} palin", palin);

                    BigInteger sq = palin.multiply(palin);

                    if (BruteForce.isPalin(sq))
                    {
                        log.debug("i {} root {} Fair square {} size {} ", i, palin, sq, sq.toString().length());
                       // set2.add(sq);
                        list.add(sq);
                    }
                }

            }
        }


        int count = set2.headSet(in.stop, true).tailSet(in.start, true).size();

        if (1==1) throw new RuntimeException("bah");

        return String.format("Case #%d: %d", in.testCase, count);

    }

    static TreeSet<Long> set = new TreeSet<Long>();

    public String handleCaseBruteForceFaster(InputData in)
    {

        if (set.size() == 0)
        {
            for (long i = 0; i < 100000000; ++i)
            {
                if (BruteForce.isPalin(BigInteger.valueOf(i)))
                {
                    long sq = i * i;
                    if (BruteForce.isPalin(BigInteger.valueOf(sq)))
                    {
                        log.debug("Adding {}*{}= {}", i, i, sq);
                        //log.debug("Adding 100000000000000");
                        set.add(sq);
                    }
                }
            }
        }

        long start = in.start.longValue();
        long stop = in.stop.longValue();

        int count = set.headSet(stop, true).tailSet(start, true).size();

        log.debug("Start {} stop  {}", start, stop);

        return String.format("Case #%d: %d", in.testCase, count);

    }

    public String handleCaseBruteForce(InputData in)
    {

        int start = (int) in.start.longValue();
        int stop = (int) in.stop.longValue();

        int count = 0;
        for (int i = start; i <= stop; ++i)
        {
            if (BruteForce.isPalin(i))
            {
                //log.debug("Palin {}", i);
                int sqRoot = DoubleMath.roundToInt(Math.sqrt(i), RoundingMode.HALF_EVEN);

                if (sqRoot * sqRoot == i && BruteForce.isPalin(sqRoot))
                {
                    log.debug("Palin fair & square {} root {}", i, sqRoot);
                    ++count;
                }
            }

        }
        return String.format("Case #%d: %d", in.testCase, count);

    }

}
