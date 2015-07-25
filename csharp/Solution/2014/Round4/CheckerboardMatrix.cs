//#define LOGGING_DEBUG
#define LOGGING_INFO
using CodeJam._2014.Round2;
using CodeJam.Utils;
using CodeJam.Utils.geom;
using CodeJam.Utils.graph;
using CodeJam.Utils.math;
using CodeJamUtils;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;
using Wintellect.PowerCollections;
using Logger = Utils.LoggerFile;



namespace Year2014.Round4.Problem4
{

    public class CheckerboardMatrixInput
    {
       
        public int N;

        public BitBlockSet[] M;
        public BitBlockSet[] M_transpose;
    }

    public class CheckerboardMatrix : InputFileProducer<CheckerboardMatrixInput>, InputFileConsumer<CheckerboardMatrixInput, String>
    {
        public CheckerboardMatrixInput createInput(Scanner scanner)
        {
            CheckerboardMatrixInput input = new CheckerboardMatrixInput();
            
            input.N = scanner.nextInt();

            Func<BitBlockSet> init = () => new BitBlockSet(input.N*2, false);

            Ext.createArrayWithFunc(out input.M, input.N * 2, init);
            Ext.createArrayWithFunc(out input.M_transpose, input.N * 2, init);

            for (int i = 0; i < input.N*2; ++i)
            {
                scanner.nextWord().ForEach((j, ch) =>
                {
                    if (ch == '1')
                    {
                        input.M[i][j] = true;
                        input.M_transpose[j][i] = true;
                    }
                });
            }

            return input;
        }
                

        public String processInput(CheckerboardMatrixInput input)
        {
            int rowCost = cost(input.N, input.M);

            if (rowCost == -1)
                return "IMPOSSIBLE";
            
            int colCost = cost(input.N, input.M_transpose);

            if (colCost == -1)
                return "IMPOSSIBLE";

            return (rowCost + colCost).ToString();

        }

        public int cost(int N, BitBlockSet[] M)
        {                    
            int firstRowDiff = -1;

            if (M[0].NumberOfSetBits() != N)
                return -1;

            List<int> likeFirstRowLocations = new List<int>();
            likeFirstRowLocations.Add(0);
            for (int i = 1; i < M.Length; ++i )
            {
                if (M[0].Equals(M[i]))
                {
                    likeFirstRowLocations.Add(i);
                }
                else if (firstRowDiff == -1)
                {
                    firstRowDiff = i;
                    if (M[i].NumberOfSetBits() != N)
                        return -1;

                }
                else if (M[i].Equals(M[firstRowDiff]) == false)
                {
                    return -1;
                }
            }

            if (likeFirstRowLocations.Count != N)
                return -1;

            //count how many not in place
            int evenCost = 0;           

            foreach(int loc in likeFirstRowLocations)
            {
                if (loc % 2 == 0)
                    ++evenCost;                
            }

            return Math.Min(evenCost, N - evenCost);
        }
    }

}