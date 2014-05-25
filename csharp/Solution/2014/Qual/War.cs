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
using TDB = System.Tuple<double, bool>;

namespace CodeJam.RoundQual_2014
{
    public class WarInput
    {
        public int N;
        public double[] naomi;
        public double[] ken;
    }
    public class War : InputFileProducer<WarInput>, InputFileConsumer<WarInput, string>
    {
        public WarInput createInput(Scanner scanner)
        {
            WarInput input = new WarInput();
            input.N = scanner.nextInt();
            input.naomi = new double[input.N];
            input.ken = new double[input.N];

            for (int i = 0; i < input.N; ++i)
            {
                input.naomi[i] = scanner.nextDouble();
            }
            for (int i = 0; i < input.N; ++i)
            {
                input.ken[i] = scanner.nextDouble();
            }
            return input;
        }

        public string processInput(WarInput input)
        {
            //Get war score
            List<Tuple<double, bool>> allWeight = new List<Tuple<double, bool>>();
            for (int i = 0; i < input.N; ++i )
            {
                allWeight.Add(new TDB(input.naomi[i], true));
                allWeight.Add(new TDB(input.ken[i], false));
            }

            allWeight.Sort();

            int kenScore = 0;

            int nCount = 0;
            //Ken's score when she doesn't cheat
            for (int i = 0; i < allWeight.Count; ++i )
            {
                if (allWeight[i].Item2)
                {
                    ++nCount;
                }
                else if (nCount > 0)
                {
                    --nCount;
                    ++kenScore;
                }
            }

            //Now playing optimally
            int kCount = 0;
            int nScore = 0;
            for (int i = 0; i < allWeight.Count; ++i)
            {
                if (!allWeight[i].Item2)
                {
                    ++kCount;
                    continue;
                }

                if (kCount > 0)
                {
                    --kCount;
                    ++nScore;
                }
                else
                {
                    //Remove ken's highest weight as naomi will deceive him saying it is just a hair under
                    for(int j = allWeight.Count - 1; j > i; --j)
                    {
                        if (!allWeight[j].Item2)
                        {
                            allWeight.RemoveAt(j);
                            break;
                        }
                    }
                }

            }
            
            return "" + nScore + " " + (input.N - kenScore);
        }
    }
}