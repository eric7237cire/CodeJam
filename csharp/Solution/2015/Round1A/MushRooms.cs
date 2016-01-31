using CodeJam.Main;
using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Year2015.Round1A.Problem1
{
    class MushRooms
    {
    }
    public class MushRoomInput
    {
        
        public int N;

        
        public int[] M;


    }

    public class MushRoom : InputFileProducer<MushRoomInput>, InputFileConsumer2<MushRoomInput>
    {
        public MushRoomInput createInput(Scanner scanner)
        {
            MushRoomInput input = new MushRoomInput();

            input.N = scanner.nextInt();

            input.M = scanner.nextIntArray(input.N);
            
            return input;
        }

        public void processInput(MushRoomInput input, IAnswerAcceptor answerAcceptor, int testCase)
        {
            int method1 = 0;
            int maxRate = 0;

            for (int i = 1; i < input.M.Length; ++i)
            {
                int numberEaten = Math.Max(0, input.M[i - 1] - input.M[i]);
                method1 += numberEaten;

                maxRate = Math.Max(maxRate, numberEaten);
            }

            int method2 = 0;

            for (int i = 1; i < input.M.Length; ++i)
            {
                int numberEaten = Math.Min(input.M[i - 1],  maxRate);
                method2 += numberEaten;
            }


            answerAcceptor.Accept(method1 + " " + method2);
        }
    }
}