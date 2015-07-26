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



namespace Year2015.RoundQual.Problem1
{

    public class StandingOvationInput
    {

        public int Smax;

        public int[] S;
    }

    public class StandingOvation : InputFileProducer<StandingOvationInput>, InputFileConsumer<StandingOvationInput, int>
    {
        public StandingOvationInput createInput(Scanner scanner)
        {
            //scanner.enablePlayback();
            StandingOvationInput input = new StandingOvationInput();

            input.Smax = scanner.nextInt();
                    
            String s = scanner.nextWord();

            Ext.createArrayWithFunc(out input.S, input.Smax + 1, i => int.Parse(s[i].ToString()));
            
            //Logger.LogInfo("Input {}", scanner.finishPlayBack());
            return input;
        }

        public int processInput(StandingOvationInput input)
        {
            int currentClapping = 0;
            int addedPeople = 0;
            for (int threshold = 0; threshold < input.S.Length; ++threshold )
            {
                int amt = input.S[threshold];

                if (currentClapping < threshold)
                {
                    addedPeople += threshold - currentClapping;
                    currentClapping += threshold - currentClapping;
                }
                
                currentClapping += amt;
            }

                
            return addedPeople;
        }
    }

}