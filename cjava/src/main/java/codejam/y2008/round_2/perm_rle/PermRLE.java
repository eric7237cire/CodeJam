package codejam.y2008.round_2.perm_rle;

import static com.google.common.base.Preconditions.checkState;

import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;

import ch.qos.logback.classic.Level;
import codejam.utils.datastructures.graph.WeightedGraphInt;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.DoubleFormat;

import com.google.common.collect.Sets;
import com.google.common.math.IntMath;

public class PermRLE extends InputFilesHandler
implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

        
        public PermRLE()
        {
            super("D", 1, 1);
            //(( ch.qos.logback.classic.Logger) log).setLevel(Level.INFO);
        }
        
        private static class SubSetData
        {
            final int size;
            final int nSubSets;
            
            //elements[subSetIndex][elementIndex]  with subSetIndex < nSubSets and elementIndex < size
            final int[][] elements;
            
            //bitMask[subSetIndex] = all elementIndexes in a bitmask 0000101 is size = 2, elements 0 and 2
            final int[] bitMask;
            
            SubSetData(int maxSize, int size)
            {
                this.size = size;
                nSubSets = IntMath.binomial(maxSize, size);
                elements  = new int[nSubSets][size];
                
                bitMask = new int[nSubSets];
            }
        }
        
        static SubSetData[] preComputeSubsets(int maxSize)
        {
            SubSetData[] subsets = new SubSetData[maxSize];
            
            //Current index of each subset, incremented as each subset is found
            int[] cur = new int[maxSize];
            
            //Intialize
            for(int s = 0; s < maxSize;++s)
            {
                SubSetData ssd = new SubSetData(maxSize,s+1);
                subsets[s] = ssd;                
            }
            
            //Go through each subset
            for(int i = 1; i < 1 << maxSize; ++i)
            {
                //How large?
                int pc = Integer.bitCount(i);
                
                
                int curSubset = cur[pc-1]++;
                
                //Set bitmask
                subsets[pc-1].bitMask[curSubset] = i;
                
                //Find each actual element
                int curSubsetElem = 0;
                for(int j = 0; j < maxSize; ++j)
                {
                    if ( (i & 1 << j) == 0)
                        continue;
                    
                    subsets[pc-1].elements[ curSubset ][ curSubsetElem ++ ] = j;
                }
                
                 
            }
            
            return subsets;
            
        }
        
        static int[][] dp= new int[ 1 << 16 ][16];
        int shortestHamiltonianCycle( SubSetData[] ssd, int[][] weights, InputData in)
        {
            /**
             * Shortest path starts from x, visits everything in A
             * and ends up at node 0 
             * dp[A][x] 
             * 
             * A is a bit mask
             * 
             */
            //Advantage of bottom up , we initialize as we go
            
            //int[][] dp = new int[ 1 << in.k ][in.k];
            
            
            dp[1][0] = 0;
            dp[0][0] = 0;
            
            
            
            for(int n = 0; n < ssd[0].nSubSets; ++n)
            {
                int subSet = ssd[0].bitMask[n];
                                
                dp[subSet][n] = weights[ ssd[0].elements[n][0] ][0];
            }
            
            for(int subsetSize = 2; subsetSize <= in.k; ++subsetSize)
            {
                SubSetData curSSD = ssd[subsetSize-1];
                                
                //Loop through all subsets of the next size
                for(int ssNum = 0; ssNum < curSSD.nSubSets; ++ssNum)
                {
                    //Get the bitmask
                    int ssBitMask = curSSD.bitMask[ssNum];
                    
                    //Go through each element to select a start node
                    for(int ssElemIdx = 0; ssElemIdx < subsetSize; ++ssElemIdx)
                    {
                        int startNode = curSSD.elements[ssNum][ssElemIdx];
                        
                        //Reset the existing value
                        dp[ssBitMask][startNode] = Integer.MAX_VALUE;
                        
                        int prevSSBitMask = ssBitMask  &  ~(1 << startNode);
                        
                        /**
                         * Here we want to start from startNode and go to any element remaining 
                         * in the subset, taking the minimum cost of adding startNode to the subset
                         */
                        for(int ssExistElemIdx = 0; ssExistElemIdx < subsetSize; ++ssExistElemIdx)
                        {
                            if (ssElemIdx == ssExistElemIdx)
                                continue;
                            
                            int nodeInCycle = curSSD.elements[ssNum][ssExistElemIdx];
                            
                            int edgeCost = weights[startNode][nodeInCycle];
                            
                            dp[ssBitMask][startNode] = Math.min(dp[ssBitMask][startNode],
                                    edgeCost + dp[prevSSBitMask][nodeInCycle]);
                        }
                        
                        
                    }
                }
            }
                
            return dp[ (1<<in.k) - 1][0];
            
        }
        
        int shortestHamiltonianCycleOld( int[][] weights, InputData in)
        {
            /**
             * Shortest path starts from x, visits everything in A
             * and ends up at node 0 
             * dp[A][x] 
             * 
             * A is a bit mask
             * 
             */
            int[][] dp = new int[ 1 << in.k ][in.k];
            for(int[] a : dp)  {
                Arrays.fill(a, Integer.MAX_VALUE);
            }
            
            dp[1][0] = 0;
            dp[0][0] = 0;
            
            Set<Integer> prevSubSets = Sets.newHashSet();
            
            for(int n = 0; n < in.k; ++n)
            {
                int subSet = 1 << n;
                prevSubSets.add(subSet);
                
                dp[subSet][n] = weights[n][0];
            }
            
            for(int subsetSize = 2; subsetSize <= in.k; ++subsetSize)
            {
                Set<Integer> nextSubSets = Sets.newHashSet();
                
                for(int startNode = 0; startNode < in.k; ++startNode)
                {
                    for(Integer prevSS : prevSubSets)
                    {
                        int ss = prevSS | 1 << startNode;
                        
                        if (ss == prevSS)
                            continue;
                        
                        //log.debug("Trying ss {} from prev {}", Integer.toBinaryString(ss), Integer.toBinaryString(prevSS));
                        nextSubSets.add(ss);
                        
                        //Loop through all nodes in prevSS
                        for(int nodeInCycle = 0; nodeInCycle < in.k; ++nodeInCycle)
                        {
                            if ( (prevSS & 1 << nodeInCycle) == 0)
                                continue;
                            
                            int edgeCost = weights[startNode][nodeInCycle];
                            
                            checkState(dp[prevSS][nodeInCycle] < Integer.MAX_VALUE);
                            
                            dp[ss][startNode] = Math.min(dp[ss][startNode],
                                    edgeCost + dp[prevSS][nodeInCycle]);
                        }
                    }
                }
                
                prevSubSets = nextSubSets;
            }
            
            return dp[ dp.length - 1][0];
            
        }
        
       
        @Override
        public InputData readInput(Scanner scanner, int testCase) {
           
            InputData in = new InputData(testCase);
           
            in.k = scanner.nextInt();
            in.S = scanner.next();
            
            return in;
        }

        /**
         * 
         * @param in
         * @param T last element in permutation
         * @return
         */
        int[][] buildGraph(InputData in, int T)
        {
            checkState(0 <= T && T < in.k);
            
            /**
             * Build graph of k verticies
             */
            int m = in.S.length() / in.k;
            
            int[][] weights = new int[in.k][in.k];
            
            for(int row = 0; row < m; ++row)
            {
                for(int x = 0; x < in.k; ++x)
                {
                    for(int y = 0; y < in.k; ++y)
                    {
                        if (x==y)
                            continue;
                        
                        if (x == T && row < m - 1) 
                        {
                            //get y from next block
                            char xCh = in.S.charAt(row * in.k + x );
                            char yCh = in.S.charAt(row * in.k + in.k+y );
                            
                            if (xCh != yCh) 
                            {
                                weights[x][y] ++;
                            }
                        } else if (x != T) {
                    
                            char xCh = in.S.charAt(row * in.k + x );
                            char yCh = in.S.charAt(row * in.k + y );
                            
                            if (xCh != yCh) 
                            {
                                weights[x][y] ++;
                            }
                        }
                    }
                }
            }
            
            
            /*If T is the last node, then we always count the switch from
            the last block to nothing, that if we have 
            
            aaab
            aaab
            
            0->0->0->2
            
            
            
            */
            
            /*
            for(int y = 0; y < in.k; ++y)
            {
                if (T==y)
                    continue;
                
                weights[T][y] ++;
            }*/
            
            
            
            
            return weights;
        }

        @Override
        public String handleCase(InputData in)
        {
            SubSetData[] ssd = preComputeSubsets(in.k);
            
            int minimum = Integer.MAX_VALUE;
            for (int lastInPermutation = 0; lastInPermutation < in.k; ++lastInPermutation)
            {
                int[][] directedGraph = buildGraph(in, lastInPermutation);
                
                if (log.isDebugEnabled())
                {
                    for(int x = 0; x < in.k; ++x)
                    {
                        for(int y = 0; y < in.k; ++y)
                        {
                            log.debug("Last node {} Edge from {} to {} ; weight {}", 1+lastInPermutation, 1+x, 1+y, directedGraph[x][y]);
                        }
                    }
                }
                
                int cur = 1+shortestHamiltonianCycle(ssd, directedGraph, in);
                
                minimum = Math.min(minimum, cur);
            }
            
            return String.format("Case #%d: %d", in.testCase, minimum);
        }

}
