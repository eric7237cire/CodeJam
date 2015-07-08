
#define LOGGING_DEBUG
#define LOGGING_INFO
using CodeJam._2014.Round2;
using CodeJam.Utils.geom;
using CodeJam.Utils.graph;
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

        public String processInput(TrieShardingInput input)
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
    }
}
