package codejam.y2012.round_final.shifting_paths;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import ch.qos.logback.classic.Level;
import codejam.utils.datastructures.BitSetLong;
import codejam.utils.datastructures.graph.DirectedGraphInt;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.IntegerPair;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import static com.google.common.base.Preconditions.*;

public class ShiftingPaths extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles
{

    public ShiftingPaths() {
        super("E", 1, 1);
        ((ch.qos.logback.classic.Logger) log).setLevel(Level.INFO);
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase)
    {

        InputData in = new InputData(testCase);

        in.N = scanner.nextInt();
        in.leftRight = new IntegerPair[in.N - 1];

        for (int i = 0; i < in.N - 1; ++i)
        {
            in.leftRight[i] = new IntegerPair(scanner.nextInt() - 1, scanner.nextInt() - 1);
        }
        return in;
    }

    DP bruteForce(int stateA, int locA, Partition p, InputData in)
    {
        int state = 0;
        //Convert stateA to combined state
        for(int i = 0; i < p.A.size(); ++i)
        {
            int index = p.A.get(i);
            
            if ((stateA & 1 << i) != 0)
            {
                state |= 1 << index;
            }
        }
        
        int visitedOdd = state;
        int curLoc = p.A.get( locA );

        int maxSteps = (1 << 10) * 10;

        int step = 0;
        for (; step < maxSteps; ++step)
        {
            
            boolean goLeft = (visitedOdd & 1 << curLoc) != 0;
            
            visitedOdd ^= 1 << curLoc;

            curLoc = goLeft ? in.leftRight[curLoc].first() : in.leftRight[curLoc].second();

            if (p.Bset.isSet(curLoc))
            {
                int newStateA = 0;
                for(int i = 0; i < p.A.size(); ++i)
                {
                    int index = p.A.get(i);
                    
                    if ((visitedOdd & 1 << index) != 0)
                    {
                        newStateA |= 1 << i;
                    }
                }
                
                return new DP(newStateA, p.origIndexToBIndex(curLoc), step+1);
            }
            
            //Must go through B before getting to final node
            checkState (curLoc != in.N - 1);
                
        }
        
        return new DP(-1, -1, -1);

    }
    
    void calcDPQueue(int stateA, int locA, Partition p, InputData in, DP[][] dp)
    {
        
        int curLoc = p.A.get( locA );

        int maxSteps = (1 << 20) * 10;
        
        //Aloc, Astate
        List<IntegerPair> path = Lists.newArrayList();

        int step = 0;
        for (; step < maxSteps; ++step)
        {
            if (p.trapNodes.isSet(curLoc)) {
                DP dn = new DP(-1,-1,-1);
                for(IntegerPair ip : path) {
                    dp[ip.first()][ip.second()] = dn; 
                }
                return;
            }
            
            if (p.Bset.isSet(curLoc))
            {
                int locB = p.origIndexToBIndex(curLoc);
                
                for(int i = 0; i < path.size(); ++i)
                {
                    IntegerPair pathElem = path.get(i);
                    dp[pathElem.first()][pathElem.second()] =
                            new DP(stateA, locB, step - i);
                }
                
                return;
            }
            
            int aLoc = p.origIndexToAIndex(curLoc);
            
            if (dp[aLoc][stateA] != null) {
                DP exist = dp[aLoc][stateA];
                
                if (exist.inLoop())
                {
                    for(int i = 0; i < path.size(); ++i)
                    {
                        IntegerPair pathElem = path.get(i);
                        dp[pathElem.first()][pathElem.second()] =
                                exist;
                    }
                    
                    return;
                }
                
                for(int i = 0; i < path.size(); ++i)
                {
                    IntegerPair pathElem = path.get(i);
                    dp[pathElem.first()][pathElem.second()] =
                            new DP(exist.newStateA, 
                                    exist.locB, exist.stepsTaken + step - i);
                }
                return;
                
            }
            
            
            
            path.add(new IntegerPair(aLoc, stateA));
            
            //Calculate new location
            boolean goLeft = (stateA & 1 << aLoc) != 0;
            
            stateA  ^= 1 << aLoc;
            curLoc = goLeft ? in.leftRight[curLoc].first() : in.leftRight[curLoc].second();

            
            
            
            //Must go through B before getting to final node
            checkState (curLoc != in.N - 1);
                
        }
        
        

    }
    
    public String handleSmall(InputData in)
    {

        long visitedOdd = (1L << in.N) - 1;
        int curLoc = 0;

        int maxSteps = (1 << 10) * 10;
        // maxSteps = 10;

        int step = 0;
        for (; step < maxSteps; ++step)
        {
           // if (step < 150)
              //  log.debug("Step {} visited {} cur location {}", step, StringUtils.reverse(Long.toBinaryString(visitedOdd)), curLoc + 1);
            boolean goLeft = (visitedOdd & 1 << curLoc) != 0;

            visitedOdd ^= 1 << curLoc;

            curLoc = goLeft ? in.leftRight[curLoc].first() : in.leftRight[curLoc].second();

            if (curLoc == in.N - 1)
                break;
        }

        if (step == maxSteps)
        {
            return String.format("Case #%d: Infinity", in.testCase);
        }
        return String.format("Case #%d: %d", in.testCase, 1 + step);

    }

    

    
    private static class DP
    {
        //Bit mask state of A (1 means odd # of times visited)
        int newStateA;
        
        //Index of clearing in set B, -1 for invalid
        int locB;
        
        int stepsTaken;

        public DP(int newStateA, int locB, int stepsTaken) {
            super();
            this.newStateA = newStateA;
            this.locB = locB;
            this.stepsTaken = stepsTaken;
        }
        
        boolean inLoop() {
            return locB < 0;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + locB;
            result = prime * result + newStateA;
            result = prime * result + stepsTaken;
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            DP other = (DP) obj;
            if (locB != other.locB)
                return false;
            if (newStateA != other.newStateA)
                return false;
            if (stepsTaken != other.stepsTaken)
                return false;
            return true;
        }

        @Override
        public String toString()
        {
            return "DP [newStateA=" + stateToString(newStateA) 
                    + ", locB=" + (locB) + ", stepsTaken=" + stepsTaken + "]";
        }
        
        
        
        
    }
    
    /*
    private static DP dpOnSetA(Partition p, int stateA, int locA, InputData in, DP[][] dp)
    {
        if (dp[locA][stateA] != null)
            return dp[locA][stateA];
        
        // (loc in A, state A) ==> (loc in B, new State in A)
        boolean goLeft = (stateA & 1 << locA) != 0;
        
        int curLocation = p.A.get(locA);
        
        int nextNode = goLeft ? in.leftRight[curLocation].first() :
            in.leftRight[curLocation].second();
        
        int nextStateA = stateA ^ 1 << locA;
        
        //We left set A
        if (p.Bset.isSet(nextNode)) {
            int indexInB = p.origIndexToBIndex(nextNode);
            
            DP dn  = new DP(nextStateA,  indexInB,
            1 );
            
            dp[locA][stateA] = dn;
            return dn;
        }
        
        if (p.trapNodes.isSet(nextNode))
        {
            DP dn = new DP(-1,-1,-1);
            dp[locA][stateA] = dn;
            return dn;
        }
        
        checkState(p.Aset.isSet(nextNode));
        
        int indexInA = p.A.indexOf(nextNode);
        
        DP next = dpOnSetA(p, nextStateA, indexInA, in, dp);
        
        if (next.inLoop()) {
            dp[locA][stateA] = next;
            return next;
        }
        
        DP dn  = new DP( next.newStateA,
         next.locB,
         1 + next.stepsTaken);
        
        
        
        dp[locA][stateA] = dn;
        return dn;
    }*/
    
    private static class Partition
    {
        BitSetLong trapNodes = new BitSetLong(0);
        
        BitSetLong Aset = new BitSetLong(0);
        BitSetLong Bset = new BitSetLong(0);
        
        List<Integer> B = Lists.newArrayList();
        List<Integer> A = Lists.newArrayList();
        
        int[] origToAIndex;
        int[] origToBIndex;
        
        int origIndexToAIndex(int index)
        {
            int r = origToAIndex[index];
            checkState(r >= 0 && r < A.size());
            return r;
        }
        
        int origIndexToBIndex(int index)
        {
            int r = origToBIndex[index];
            checkState(r >= 0 && r < B.size());
            return r;
        }
        
        
        
        @Override
        public String toString()
        {
            return "Partition [trapNodes=" + 
        StringUtils.reverse(Long.toBinaryString(trapNodes.getBits()))
        + ", B=" + B + ", A=" + A + "]";
        }
        
        
        
    }
    
    private static String stateToString(long l)
    {
        return StringUtils.reverse(Long.toBinaryString(l));
    }
    /**
     * Partition into 2 equal ish sized sets A and B
     * based on ideal distance to final node.
     * 
     * Node B contains the closest nodes
     * 
     * @param in
     * @return
     */
    Partition computePartition(InputData in)
    {
        int[] distToN = new int[in.N];

        Arrays.fill(distToN, Integer.MAX_VALUE);

        DirectedGraphInt g = new DirectedGraphInt();

        for (int i = 0; i < in.N - 1; ++i)
        {
            g.addOneWayConnection(i, in.leftRight[i].first());
            g.addOneWayConnection(i, in.leftRight[i].second());
        }

        BitSetLong visited = new BitSetLong(0);

        Queue<Integer> toVisit = new LinkedList<>();
        toVisit.add(in.N - 1);

        distToN[in.N - 1] = 0;

        while (!toVisit.isEmpty())
        {
            Integer cur = toVisit.poll();

            if (visited.isSet(cur))
                continue;

            visited.set(cur);

            Set<Integer> adj = g.getInbound(cur);

            for (Integer adjNode : adj)
            {
                toVisit.add(adjNode);
                distToN[adjNode] = Math.min(distToN[adjNode], 1 + distToN[cur]);
            }
        }

        Partition p = new Partition();
        

        List<IntegerPair> l = Lists.newArrayList();

        for (int i = 0; i < in.N - 1; ++i)
        {
            log.debug("Clearing {} has ideal distance {} to final clearing ", i+1, distToN[i]);
            if (distToN[i] == Integer.MAX_VALUE)
            {
                p.trapNodes.set(i);
                continue;
            }

            l.add(new IntegerPair(distToN[i], i));
        }

        Collections.sort(l);
        
        int half = l.size() / 2;
        
        p.origToAIndex = new int[in.N-1];
        p.origToBIndex = new int[in.N-1];
        
        Arrays.fill(p.origToAIndex, -1);
        Arrays.fill(p.origToBIndex, -1);
        
        for(int i = 0; i < l.size(); ++i)
        {
            int origIndex  = l.get(i).second();
            
            if (i <= half) {
                p.origToBIndex[origIndex] = p.B.size();
                
                p.B.add(origIndex);
                p.Bset.set(origIndex);
                
            } else {
                p.origToAIndex[origIndex] = p.A.size();
                
                p.A.add(origIndex);
                p.Aset.set(origIndex);
            }
        }
        
        
        
        log.debug("Partition {}", p);
        return p;
    }

    @Override
    public String handleCase(InputData in)
    {
        Partition p = computePartition(in);
        
        DP[][] dp = new DP[p.A.size()][ 1 << p.A.size()];
        
        
        for(int stateA = 0; stateA < 1 << p.A.size(); ++stateA)
        {
            if (stateA % 50000 == 0) {
                log.info("Precalculating state A {} of {}", stateA, 1 << p.A.size());
            }
            for(int locA = 0; locA < p.A.size(); ++locA)
            {
                //dpOnSetA(p, stateA, locA, in, dp);
                calcDPQueue(stateA, locA, p, in, dp);
                
                /*
                if (in.N < 30) {
                DP check = bruteForce(stateA, locA, p, in);
                
                DP val = dp[locA][stateA];
                
                
                checkState(val.equals(check));
                
                log.debug("DP for state A {} and loc A {} = {}",stateToString(stateA),locA,val);
                }*/
            }
        }
        
        
        int stateB = (1 << p.B.size()) - 1;
        int stateA = (1 << p.A.size()) - 1;
        
        int curLoc = 0;
        
        long steps = 0;
        
        long maxSteps = (1L << in.N) * in.N;

       // maxSteps = 150;
        
        while(steps < maxSteps)
        {
            log.debug("Cur location {} state A {} state B {} steps {}",
                    curLoc, stateToString(stateA), stateToString(stateB), steps);
            
            //Find out which set curLoc belongs to
            if (p.Aset.isSet(curLoc))
            {
                int locA = p.origIndexToAIndex(curLoc);
                DP val = dp[locA][stateA]; //dpOnSetA(p, stateA, locA, in, dp);
                
                if (val.inLoop()) {
                    return String.format("Case #%d: Infinity", in.testCase);
                }
                
                steps += val.stepsTaken;
                curLoc = p.B.get(val.locB);
                checkState(p.Bset.isSet(curLoc));
                
                stateA = val.newStateA;
                continue;
            }
            
            if (p.trapNodes.isSet(curLoc)) {
                return String.format("Case #%d: Infinity", in.testCase);
            }
            
            int locB = p.origIndexToBIndex(curLoc);
            
            boolean goLeft = (stateB & 1 << locB) != 0;
            
            stateB ^= 1 << locB;

            curLoc = goLeft ? in.leftRight[curLoc].first() : in.leftRight[curLoc].second();

            ++steps;
            
            if (curLoc == in.N - 1) {
                return String.format("Case #%d: %d", in.testCase, steps);
            }
                
        }
        
        log.error("Error");
        
       // return handleSmall(in);
        return String.format("Case #%d: Error", in.testCase);
    }

}
