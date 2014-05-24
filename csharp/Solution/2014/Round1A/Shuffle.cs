#if DEBUG
#define LOGGING_DEBUG
#define LOGGING_INFO
#define LOGGING_TRACE
#endif

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;
using Utils.tree;
using Logger = Utils.LoggerFile;

using CodeJam.Utils.graph;

namespace CodeJam.Round1A_2014
{
    public class ShuffleInput
    {
        public int N;
        public Graph graph;
    }
    public class Shuffle : InputFileProducer<ShuffleInput>, InputFileConsumer<ShuffleInput, string>
    {
        public ShuffleInput createInput(Scanner scanner)
        {
            ShuffleInput input = new ShuffleInput();
            input.N = scanner.nextInt();
            input.graph = new Graph(input.N);

            for (int i = 0; i < input.N - 1; ++i)
            {
                int u = scanner.nextInt() - 1;
                int v = scanner.nextInt() - 1;
                input.graph.addConnection(u, v);
                input.graph.addConnection(v, u);
            }

            return input;
        }

        static Random rand = new Random();
        static int[] good(int N)
        {
            int[] ret = new int[N];
           for (int k = 0; k < N; ++k)
           {
               ret[k] = k;
           }
            
            for(int k = 0; k < N; ++k)
            {
                int p = rand.Next(k, N);
                int temp = ret[k];
                ret[k] = ret[p];
                ret[p] = temp;
            }

            return ret;
        }

        static int[] bad(int N)
        {
          int[] ret = new int[N];
           for (int k = 0; k < N; ++k)
           {
               ret[k] = k;
           }

            for(int k = 0; k < N; ++k)
            {
                int p = rand.Next(0, N);
                int temp = ret[k];
                ret[k] = ret[p];
                ret[p] = temp;
            }
            return ret;
        }

        class ListKey : IEquatable<ListKey>
        {
            internal int[] source;
            ulong key;
            internal ListKey(int[] ar)
            {
                source = ar;
                key = 0;

                int L = ar.Length;

                for(int i = 0; i < L; ++i)
                {
                    key <<= L;
                    key |= 1UL << ar[i];
                }
            }

            public override int GetHashCode()
            {
                return key.GetHashCode();
            }

            public override bool Equals(object obj)
            {
                return Equals(obj as ListKey);
            }

            public bool Equals(ListKey other)
            {
                return key == other.key;
            }
        }

        public string processInput(ShuffleInput input)
        {
            
            Dictionary<ListKey, int> counts = new Dictionary<ListKey, int>();

            int iters = 1000000;
            int permLen = 8;

            for (int i = 0; i < iters; ++i )
            {
                //int[] perm = good(permLen);
                int[] perm = bad(permLen);
                ListKey key = new ListKey(perm);
                int count;
                counts.TryGetValue(key, out count);
                counts[key] = count + 1;
            }

            int total = 1;
            for(int i = 1; i <= permLen; ++i)
                total *= i;

            foreach(var kv in counts)
            {
                Logger.LogTrace(" perm {}.  Freq = {} / {} = {}%  Normal %{}", kv.Key.source.ToCommaString(), kv.Value, iters, 100d * kv.Value / iters,
                    (100d / total).ToUsString(3));
            }

                return "bob";
        }
    }
}