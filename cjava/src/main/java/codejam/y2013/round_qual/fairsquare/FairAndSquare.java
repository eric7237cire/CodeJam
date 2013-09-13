package codejam.y2013.round_qual.fairsquare;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Scanner;
import java.util.TreeSet;

import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.y2009.round_3.interesting_ranges.BruteForce;

import com.google.common.base.Preconditions;
import com.google.common.math.DoubleMath;

public class FairAndSquare extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>
{
    public FairAndSquare() {
        super("C", 0, 0);
        //setLogInfo();

    }

    public String[] getDefaultInputFiles()
    {

        return new String[] { "C-small-practice.in", "C-large-practice-1.in",
                "C-large-practice-2.in" };
        // return new String[] { "C-small-practice.in", "C-large-practice-1.in"
        // };
        // return new String[] { "C-small-practice.in"};

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
       // return handleCaseBruteForceEvenFaster(in);
        return handleCaseBruteForceEvenEvenFaster(in);
    }
    
    public StringBuffer[] toBaseTwo(int n)
    {
        StringBuffer[] palin = new StringBuffer[4];
        
        StringBuffer rev = new StringBuffer();
        
        
        while (n > 0)
        {
            int digit = n % 2;
            
            rev.append(digit);
            n /= 2;
        }
        
        for(int i = 0; i < 4; ++i)
        {
            palin[i] = new StringBuffer();
        }
        StringBuffer nonRev = new StringBuffer(rev);
        nonRev = nonRev.reverse();
        
        palin[0].append( nonRev).append( rev);
        palin[1].append( nonRev).append('0').append( rev);
        palin[2].append( nonRev).append('1').append( rev);
        palin[3].append( nonRev).append('2').append( rev);
        
        
        

        return palin;
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

    /**
     * En observation, il n'y a que trois forme
     * 
     * 20*[01 ]0*2
     * 22
     * 202
     * 212
     * 2002
     * 20102
     * 20002
     * 
     * ou bien
     * 1....[012].....1 ou il y a maximum 9 1's
     * @param in
     * @return
     */
    public String handleCaseBruteForceEvenEvenFaster(InputData in)
    {
        /*
         * i 1 root 11 Fair square 121 size 3 
i 1 root 101 Fair square 10201 size 5 
i 1 root 111 Fair square 12321 size 5 
i 1 root 121 Fair square 14641 size 5 
i 2 root 22 Fair square 484 size 3 
i 2 root 202 Fair square 40804 size 5 
i 2 root 212 Fair square 44944 size 5 
         */

        if (set2.size() == 0)
        {
            set2.add(BigInteger.valueOf(1));
            set2.add(BigInteger.valueOf(4));
            set2.add(BigInteger.valueOf(9));
            
            
            for( int zeros = 0; zeros <= 25; ++zeros)
            {
                StringBuffer[] sbs = new StringBuffer[3];
                for(int i = 0; i < sbs.length; ++i)
                {
                    sbs[i] = new StringBuffer();
                    sbs[i].append('2');
                }
                
                for(int z = 0; z < zeros; ++z)
                {
                    for(int i = 0; i < sbs.length; ++i)
                    {
                        sbs[i].append('0');
                    }
                }
                
                sbs[1].append('0');
                sbs[2].append('1');
                
                for(int z = 0; z < zeros; ++z)
                {
                    for(int i = 0; i < sbs.length; ++i)
                    {
                        sbs[i].append('0');
                    }
                }
                
                for(int i = 0; i < sbs.length; ++i)
                {
                    sbs[i].append('2');
                    
                    BigInteger bi = new BigInteger(sbs[i].toString());
                    
                    BigInteger sq = bi.multiply(bi);
                    Preconditions.checkState(BruteForce.isPalin(sq));
                    
                    set2.add(sq);
                    log.debug("2 root {} Fair square {} size {} ", sbs[i], sq, sq.toString().length());
                }
                
                
            }
            
            //Pour générer les palins > 100 places
            for (int i = 1; i < 40000000; ++i)
            {
                if (Integer.bitCount(i) > 4)
                    continue;
                
                StringBuffer[] palinBase3 = toBaseTwo(i);
               // log.debug("palinBase3 {}", palinBase3);

                for (int j = 0; j < palinBase3.length; ++j)
                {
                    String palinStr = palinBase3[j].toString();
                    BigInteger palin = new BigInteger(palinStr);
                    
                    //log.debug("Éventuelle racine carrée {} palin", palin);

                    BigInteger sq = palin.multiply(palin);

                    if (BruteForce.isPalin(sq))
                    {
                        /*
                        if (false && set2.size() % 100000 == 0 )
                        {
                            log.debug("root {} i {} Fair square {} size {} ", palin, i, sq, sq.toString().length());
                            log.debug("root {} {} {}  1s : {}", palinStr.substring(0, palinStr.length()/2), palinStr.length()/2, 
                                    palinStr.length(), StringUtils.countMatches(palinStr, "1"));
                        }*/
                        set2.add(sq);
                    }
                }

            }
        }


        int count = set2.headSet(in.stop, true).tailSet(in.start, true).size();

       // if (1==1) throw new RuntimeException("bah");

        return String.format("Case #%d: %d", in.testCase, count);

    }
    
    

    static TreeSet<Long> set = new TreeSet<Long>();

    /**
     * La première idée est il y a beaucoup moins de nombre carré 
     * dans l'intervalle, alors commence par les racines carrées qui
     * sont palindrome plutôt que l'inverse est stocke le tout dans
     * un set
     * @param in
     * @return
     */
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
                int sqRoot = DoubleMath.roundToInt(Math.sqrt(i), RoundingMode.HALF_EVEN);

                //Satisfaire la condition que c'est un palindrome et sa racine carrée est également un palidrome
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
