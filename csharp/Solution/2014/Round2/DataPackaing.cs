using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Round2_2014.Problem1
{
    public class DataPackingInput
    {
        public int N;
        public int X;

        public int[] S;
    }
    public class DataPacking : InputFileProducer<DataPackingInput>, InputFileConsumer<DataPackingInput, int>
    {
        public DataPackingInput createInput(Scanner scanner)
        {
            DataPackingInput input = new DataPackingInput();

            input.N = scanner.nextInt();
            input.X = scanner.nextInt();
            input.S = new int[input.N];

            for (int i = 0; i < input.N; ++i )
            {
                input.S[i] = scanner.nextInt();
            }
                
            return input;
        }

        public int processInput(DataPackingInput input)
        {
            //sort S
            Array.Sort(input.S);

            LinkedList<int> list = new LinkedList<int>(input.S);

            int nCDs = 0;
            
            //A greedy algorithm, fit biggest one we have
            while(list.Count > 0)
            {
                var node = list.Last;
                int room = input.X - node.Value;
                list.Remove(node);

                for(var n = list.Last; n != null; n = n.Previous)
                {
                    if (n.Value <= room)
                    {
                        list.Remove(n);
                        break;
                    }
                }


                ++nCDs;
            }

            return nCDs;
        }
    }
}
