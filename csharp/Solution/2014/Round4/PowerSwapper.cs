#define LOGGING_DEBUG
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



namespace Year2014.Round4.Problem2
{
   

    public class PowerSwapperInput
    {
        
        public int N;

        public int[] A;
    }

    public class PowerSwapper : InputFileProducer<PowerSwapperInput>, InputFileConsumer<PowerSwapperInput, int>
    {
        public PowerSwapperInput createInput(Scanner scanner)
        {
            scanner.enablePlayBack();

            PowerSwapperInput input = new PowerSwapperInput();
                        
            input.N = scanner.nextInt();
                      
            Ext.createArrayWithFunc(out input.A, 1 << input.N, (i)=>scanner.nextInt());
            
            Logger.LogInfo("Input {}", scanner.finishPlayBack());
            return input;
        }

        public void swap(int[] a, int idx1, int idx2, int blockSize)
        {
            for(int k = 0; k < blockSize; ++k)
            {
                int t = a[idx1 + k];
                a[idx1 + k] = a[idx2 + k];
                a[idx2 + k] = t;
            }
        }



        public int count(int[] a, int power, int maxPower, int nSwapped)
        {
            Logger.LogDebug("Entering count");

            if (power == maxPower)
            {
                int f = 1;
                for(int ii = 1; ii <= nSwapped; ++ii)
                    f *= ii;
                return f;
            }
            int sz = 1 << power;

            List<int> candidates = new List<int>();

            int i=0;

            Func<int, bool> ok = iii =>
            {
                //if (iii + sz >= a.Length)
                    //return a[iii] + sz == a[2 * a.Length - 2 - (iii + sz) ];

                return a[iii] + sz == a[ (iii + sz)  ];
            };

            while(i < (1<<maxPower))
            {
                if (!ok(i))
                    candidates.Add(i);

                i = i + sz * 2;
            }

            int ret = 0;
            if (candidates.Count == 0)
            {
                ret = count(a, power + 1, maxPower, nSwapped);
            } else if (candidates.Count == 1)
            {
                swap(a, candidates[0], candidates[0] + sz, sz);
                if (ok(candidates[0]))
                    ret = count(a, power + 1, maxPower, nSwapped + 1);
                swap(a, candidates[0], candidates[0] + sz, sz);
            } else if (candidates.Count == 2)
            {
                foreach(int fi in new int[] {candidates[0], candidates[0] + sz})
                {
                    foreach(int fy in new int[] {candidates[1], candidates[1] + sz})
                    {
                        swap(a, fi, fy, sz);
                        if (ok(candidates[0]) && ok(candidates[1]))
                        {
                            ret = ret + count(a, power + 1, maxPower, nSwapped + 1);
                        }
                        swap(a, fi, fy, sz);
                    }
                }
            }
            return ret;
            //int[] p2;
            //Ext.createArrayWithFunc(out p2, a.Length, (ii) => a[ii] % power2);

            //Logger.LogDebug(String.Join(", ", a.Select(i => i.ToString().PadLeft(3))));
           // Logger.LogDebug(String.Join(", ", p2.Select(i => i.ToString().PadLeft(3))));

            
        }
        public int processInput(PowerSwapperInput input)
        {
            
            //Logger.LogDebug(String.Join(", ", za));

            int c = count(input.A, 0, input.N, 0);
                       
                        

            return c;
        }
    }

}