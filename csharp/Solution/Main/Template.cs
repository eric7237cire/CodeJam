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



namespace Year2015.Round3.Problem2
{
    
    public class LastHitInput
    {
        public int M;
        public int N;

        public List<String> S;
    }

	public class LastHit : InputFileProducer<LastHitInput>, InputFileConsumer<LastHitInput, String>
    {
        public LastHitInput createInput(Scanner scanner)
        {
			LastHitInput input = new LastHitInput();

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

		public String processInput(LastHitInput input)
        {
			return "-1";
		}
	}

}