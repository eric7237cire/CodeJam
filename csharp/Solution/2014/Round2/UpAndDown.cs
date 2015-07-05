using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Round2_2014.Problem2
{
    public class UpAndDownInput
    {
        public int N;
        
        public int[] A;
    }
    public class UpAndDown : InputFileProducer<UpAndDownInput>, InputFileConsumer<UpAndDownInput, int>
    {
        public UpAndDownInput createInput(Scanner scanner)
        {
            UpAndDownInput input = new UpAndDownInput();

            input.N = scanner.nextInt();
            
            input.A = new int[input.N];

            for (int i = 0; i < input.N; ++i)
            {
                input.A[i] = scanner.nextInt();
            }

            return input;
        }

        public int processInput(UpAndDownInput input)
        {
            List<int> list = new List<int>(input.A);
            LinkedList<int> sortedList = new LinkedList<int>(input.A.OrderBy(e => e));

            int swaps = 0;
            while(list.Count > 0)
            {
                var minValueNode = sortedList.First;

                int minValueLoc = list.IndexOf(minValueNode.Value);

                //pay minimum, either put on left or right side
                swaps += Math.Min(minValueLoc - 0, list.Count - 1 - minValueLoc);

                sortedList.RemoveFirst();
                list.RemoveAt(minValueLoc);
            }

            return swaps;
        }
    }
}
