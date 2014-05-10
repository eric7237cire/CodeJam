using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Round3
{
    class Rural : InputFileConsumer<RuralInput, string>, InputFileProducer<RuralInput>
    {
        public string processInput(RuralInput input)
        {
            return "3";
        }

        public RuralInput createInput(Scanner scanner)
        {
            RuralInput input = new RuralInput();
            input.budget = scanner.nextLong();
            input.nBets = scanner.nextInt();

            input.bets = new long[input.nBets];
            for (int i = 0; i < input.nBets; ++i)
            {
                input.bets[i] = scanner.nextLong();
            }

            return input;
        }
    }

    public class RuralInput
    {
        internal long budget { get; set; }
        internal int nBets { get; set; }

        internal long[] bets;

    }
}
