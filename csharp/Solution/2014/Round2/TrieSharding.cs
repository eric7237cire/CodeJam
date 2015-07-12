//
//#define LOGGING_DEBUG
#define LOGGING_INFO
using CodeJam._2014.Round2;
using CodeJam.Utils.geom;
using CodeJam.Utils.graph;
using CodeJam.Utils.math;
using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;
using Wintellect.PowerCollections;
using Logger = Utils.LoggerFile;


namespace Round2_2014.Problem3
{
    
    public class TrieShardingInput
    {
        public int M;
        public int N;

        public List<String> S;
    }
    public class TrieSharding : InputFileProducer<TrieShardingInput>, InputFileConsumer<TrieShardingInput, String>
    {
        public TrieShardingInput createInput(Scanner scanner)
        {
            // scanner.enablePlayBack();
            TrieShardingInput input = new TrieShardingInput();

            input.M = scanner.nextInt();
            input.N = scanner.nextInt();

            input.S = new List<string>();



            for (int i = 0; i < input.M; ++i)
            {
                input.S.Add(scanner.nextWord());
            }

            //Logger.LogInfo("Input {}", scanner.finishPlayBack());
            return input;
        }

        IEnumerable<IList<T>> EnumerateSets<T>(IList<T> set)
        {
            int max = 1 << set.Count ;
            for(int i = 0; i < max; ++i)
            {
                BitSet bs = new BitSet(i);
                List<T> list = new List<T>();

                for(int idx = 0; idx < set.Count; ++idx)
                {
                    if (i.GetBit(idx))
                    {
                        list.Add(set[idx]);
                    }
                }
                yield return list;
            }
        }

       
        public void GenerateSets(List<IList<String>> sets, List<String> remainingStringsToPlace, int serversLeft, 
            ref int maxTotalNodes, ref int count)
        {
            if (serversLeft == 1)
            {
                sets.Add(remainingStringsToPlace);

                int totalNodes = 0;

                //Logger.LogInfo("Server combination:");
                foreach(var serverSet in sets)
                {
                    Dictionary<String, bool> prefixes = new Dictionary<String, bool>();
                    //Logger.LogInfo(String.Join(", ", serverSet));

                    foreach(String s in serverSet)
                    {
                        for(int pl = 0; pl <= s.Length; ++pl)
                        {
                            prefixes[s.Substring(0, pl)] = true;
                        }
                    }

                    totalNodes += prefixes.Count;
                }

                if (totalNodes > maxTotalNodes)
                {
                    count = 1;
                    maxTotalNodes = totalNodes;
                } else if (totalNodes == maxTotalNodes)
                {
                    ++count;
                }

                sets.Remove(remainingStringsToPlace);
                return;
            }

            foreach (var subList in EnumerateSets(remainingStringsToPlace))
            {
                List<String> newRemaining = new List<String>(remainingStringsToPlace.Where(s => subList.Contains(s) == false));

                if (newRemaining.Count < serversLeft-1)
                    continue;

                if (subList.Count == 0)
                    continue;

                sets.Add(subList);

                GenerateSets(sets, newRemaining, serversLeft - 1, ref maxTotalNodes, ref count);

                sets.Remove(subList);
            }
        }

        public String processInputSmall(TrieShardingInput input)
        {
            //List<String> l = new List<string>() {"bob", "sam", "joe", "ann"};

           /* foreach(var subList in EnumerateSets(input.S))
            {
                Logger.LogInfo(String.Join(", ", subList));
            }*/
            int max=-1;
            int num=-1;
            GenerateSets(new List<IList<string>>(), input.S, input.N, ref max, ref num);

            return String.Join(" ", max, num);
        }

        public String processInput(TrieShardingInput input)
        {
            //Used to count T_p, number of strings containing prefix p
            //will be p=>count
            Dictionary<String, int> prefixToStringsWithCount = new Dictionary<string, int>();

            Dictionary<String, int> prefixToNodeIndex = new Dictionary<string, int>();
            List<String> nodeIndexToString = new List<string>();

            foreach(String s in input.S)
            {
                for(int prefixLen = 0; prefixLen <= s.Length; ++prefixLen)
                {
                    String prefix = s.Substring(0, prefixLen);

                    int count;
                    bool gotIt = prefixToStringsWithCount.TryGetValue(prefix, out count);

                    if (gotIt == false)
                    {
                        count = 0;

                        //Also add string to indexes
                        prefixToNodeIndex.Add(prefix, nodeIndexToString.Count);
                        nodeIndexToString.Add(prefix);
                        
                    }

                    prefixToStringsWithCount[prefix] = count + 1;
                }
            }

            //Tally up K_p
            int maxNumberNodes = prefixToStringsWithCount.Select(kv => Math.Min(input.N, kv.Value)).Sum();

            //Initialize K_p also with artificial nodes
            int totalNodes = nodeIndexToString.Count+input.S.Count;
            int[] K_p = new int[totalNodes];

            foreach(var kv in prefixToStringsWithCount)
            {
                K_p[prefixToNodeIndex[kv.Key]] = Math.Min(input.N, kv.Value);
            }

            Preconditions.checkState(K_p.Sum() == maxNumberNodes);

            //Construct trie graph
            Graph g = new Graph(totalNodes);

            for (int prefixIndex = 0; prefixIndex < nodeIndexToString.Count; ++prefixIndex )
            {
                String prefix = nodeIndexToString[prefixIndex];
                if (prefix.Length == 0)
                    continue;

                String parentPrefix = prefix.Substring(0, prefix.Length - 1);
                int parentIndex = prefixToNodeIndex[parentPrefix];

                g.addConnection(parentIndex, prefixIndex);
            }

            const String ArtificialPrefix = "a ";
            //Add artificial nodes
            foreach(String s in input.S)
            {
                int artificialNodeIndex = nodeIndexToString.Count;
                prefixToNodeIndex[ArtificialPrefix + s] = artificialNodeIndex ;
                K_p[nodeIndexToString.Count] = 1;

                nodeIndexToString.Add(ArtificialPrefix + s);   
             
                g.addConnection( prefixToNodeIndex[s], artificialNodeIndex);
            }

            //Post order traversal
            int[] W_p = new int[nodeIndexToString.Count];

            int rootIndex = prefixToNodeIndex[""];

            Logger.LogDebug("Root index is " + rootIndex);

            g.PostOrderTraversal(rootIndex, nodeIndex =>
            {
                Logger.LogDebug("Visiting node index={} prefix={}", nodeIndex, nodeIndexToString[nodeIndex]);
                List<int> children = g.getOutboundConnected(nodeIndex);

                if (children.Count == 0)
                {
                    Logger.LogDebug("Node index={} prefix={}.  No children", nodeIndex, nodeIndexToString[nodeIndex]);
                    W_p[nodeIndex] = 1;
                    return;
                }

                long C2 = 1;

                List<int> marbleColors = new List<int>();

                foreach(int childIndex in children)
                {
                    C2 *= W_p[childIndex];
                    C2 %= MOD;
                    Logger.LogDebug("Node index={} prefix={}.  C2 *= W_p[{}] {} = {}", nodeIndex, nodeIndexToString[nodeIndex], childIndex, W_p[childIndex], C2);

                    marbleColors.Add(K_p[childIndex]);
                }

                long C1 = DistributeMarbles(marbleColors, K_p[nodeIndex]);
                Logger.LogDebug("Node index={} prefix={}.  C1 (Distributing colored marbles of counts {} into {} bins) = {}", nodeIndex, 
                    nodeIndexToString[nodeIndex], String.Join(", ", marbleColors),  K_p[nodeIndex], C1);

                W_p[nodeIndex] = (int) ( (C1 * C2) % MOD );

                Logger.LogDebug("Node index={} prefix={}.  Setting W_p = {}", nodeIndex,
                    nodeIndexToString[nodeIndex], W_p[nodeIndex]);
            });
            return maxNumberNodes + " " + W_p[rootIndex];
        }

        const int MOD = 1000000007;
        long DistributeMarbles(List<int> colorCounts, int k_numberOfBins)
        {
            Logger.LogDebug("Distributing marbels [{}] into {} bins", String.Join(", ", colorCounts), k_numberOfBins);

            long[] OC = new long[k_numberOfBins];
            int[][] combin = LargeNumberUtils.GenerateModedCombin(k_numberOfBins, MOD);

            for (int i = 1; i <= k_numberOfBins; ++i)
            {
                OC[i-1] = 1;
                foreach (int colorCount in colorCounts)
                {                    
                    int kChooseX = i >= colorCount ? combin[i][colorCount] : 0;
                    OC[i-1] *= kChooseX;
                    OC[i - 1] %= MOD;
                }
            }

            long[] count = new long[k_numberOfBins];

            //Count_1 = OC_1
            count[0] = OC[0];

            for (int i = 2; i <= k_numberOfBins; ++i )
            {
                long OC_i = OC[i - 1];
                for(int a =1; a <= i-1; ++a)
                {
                    long count_a = count[a - 1];
                    OC_i -= count_a * combin[i][i - a];

                    OC_i %= MOD;

                    if (OC_i < 0)
                        OC_i += MOD;

                    Preconditions.checkState(OC_i >= 0);
                }
                count[i - 1] = OC_i;
                //count[i - 1] %= MOD;

                if (count[i - 1] < 0)
                    count[i - 1] += MOD;
            }

            return count[k_numberOfBins-1];
        }
    }
}
