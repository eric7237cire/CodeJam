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
using Utils;
using Utils.tree;
using Logger = Utils.LoggerFile;

using CodeJam.Utils.graph;

namespace CodeJam.Round1A_2014
{
    public class ShuffleInput
    {
        public int N;
        public int[] perm;
    }
    public class Shuffle : InputFileProducer<ShuffleInput>, InputFileConsumer<ShuffleInput, string>
    {
        public ShuffleInput createInput(Scanner scanner)
        {
            ShuffleInput input = new ShuffleInput();
            input.N = scanner.nextInt();
            input.perm = new int[input.N];

            for (int i = 0; i < input.N; ++i)
            {
                input.perm[i] = scanner.nextInt();
            }

            return input;
        }

        public string processInput(ShuffleInput input)
        {
            double[][] f;
            computeNextPrev(input.N, out f);

            double prob = 1d;
            for (int j = 0; j < input.N; ++j)
            {
                prob *= f[input.perm[j]][j] * input.N;
            }

            return prob > 0.5 ? "BAD" : "GOOD";

        }

        static Random rand = new Random();
        static int[] good(int N)
        {
            int[] ret = new int[N];
            for (int k = 0; k < N; ++k)
            {
                ret[k] = k;
            }

            for (int k = 0; k < N; ++k)
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

            for (int k = 0; k < N; ++k)
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

                for (int i = 0; i < L; ++i)
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


        public bool isBad(int[] ar)
        {
            int score = 0;
            int N = ar.Length;
            for (int i = 0; i < N; ++i)
            {
                if (ar[i] <= i)
                    score++;
            }

            if (score < (472 + 500) / 2)
                return true;

            return false;
        }



        static void computeP(out double[][] P, int N)
        {
            Ext.createArray(out P, N, N, 0d);
            double pp = 1d / N;

            for (int n = 0; n < N; ++n) P[n][n] = 1d;
            for (int k = 0; k < N; ++k)
            {
                double[][] NP;
                Ext.createArray(out NP, N, N, 0d);

                for (int q = 0; q < N; ++q) if (q != k)
                    {
                        for (int i = 0; i < N; ++i) NP[q][i] += (1 - pp) * P[q][i];
                        for (int i = 0; i < N; ++i) NP[q][i] += pp * P[k][i];
                        for (int i = 0; i < N; ++i) NP[k][i] += pp * P[q][i];
                    }
                for (int i = 0; i < N; ++i) NP[k][i] += pp * P[k][i];
                /*
                double sum = 0.;
                for (int q=0; q<N; ++q) for (int i=0; i<N; ++i) sum += NP[q][i];
                cout << sum << endl;
                */
                P = NP; // lol, cool :D
            }
        }

        public Boolean isBadMisof(double[][] P, int[] A)
        {
            int N = A.Length;
            double pp = 1d / N;
            double llgood = 0, llbad = 0;
            for (int n = 0; n < N; ++n) llgood += Math.Log(pp);
            for (int n = 0; n < N; ++n) llbad += Math.Log(P[n][A[n]]);

            return llgood <= llbad;
        }

        public void computeNextPrev(int N, out double[][] f)
        {
            //double[][] f;
            Ext.createArray(out f, N, N, 0d);
            double[] k = new double[N];
            double[] b = new double[N];
            for (int i = 0; i < N; ++i)
            {
                f[i][i] = 1.0;
                k[i] = 1.0;
                b[i] = 0.0;
            }

            double p = (double)(N - 1) / N;
            double q = 1d / N;

            for (int it = 0; it < N; ++it)
            {
                for (int i = 0; i < N; ++i)
                {
                    double add = (k[i] * f[i][it] + b[i]) * q;
                    k[i] = k[i] * p;
                    b[i] = b[i] * p + add;
                    f[i][it] = (q - b[i]) / k[i];
                }
            }

            for (int i = 0; i < N; ++i)
            {
                for (int j = 0; j < N; ++j)
                {
                    f[i][j] = k[i] * f[i][j] + b[i];
                }
            }
        }

        public string simulate(ShuffleInput input)
        {

            //Dictionary<ListKey, int> counts = new Dictionary<ListKey, int>();

            int permLen = 1000;

            //double[][] P;
            // computeP(out P, permLen);

            double[][] f;
            computeNextPrev(permLen, out f);

            int iters = 10000;


            //int[][] relFreq; 
            //Ext.createArray(out relFreq, permLen, permLen, 0);
            int numCorrect = 0;

            for (int i = 0; i < iters; ++i)
            {
                bool useBad = rand.Next(0, 2) == 0;
                int[] perm = !useBad ? good(permLen) : bad(permLen);
                
                //bool bIsBad = isBad(perm);
                // bool bIsBad = isBadMisof(P, perm);


                double prob = 1d;
                for (int j = 0; j < perm.Length; ++j)
                {
                    prob *= f[perm[j]][j] * perm.Length;
                }

              //  bool bIsBad = prob > 0.5;

                /*
                if (useBad == bIsBad)
                {
                    ++numCorrect;
                }*/
                
            }

            Logger.LogTrace("Number correct {} / {} = {}%", numCorrect, iters, 100d * numCorrect / iters);
            /*int total = 1;
            for(int i = 1; i <= permLen; ++i)
                total *= i;

            foreach(var kv in counts)
            {
                Logger.LogTrace(" perm {}.  Freq = {} / {} = {}%  Normal %{}", kv.Key.source.ToCommaString(), kv.Value, iters, 100d * kv.Value / iters,
                    (100d / total).ToUsString(3));
            }
            */
            return "bob";
        }
    }
}