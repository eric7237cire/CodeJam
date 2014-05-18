//#define LOGGING
#define LOGGING_DEBUG
#define LOGGING_INFO
//#define LOGGING_TRACE


using CodeJamUtils;
using CombPerm;
using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Numerics;
using System.Text;
using System.Threading.Tasks;
using Utils;
using Utils.math;
using Logger = Utils.LoggerFile;




namespace Round3
{

    public class WheelInput
    {
        public string wheel;
    }

    
    public class Wheel : InputFileConsumer<WheelInput, string>, InputFileProducer<WheelInput>
    {

        public string processInput(WheelInput input)
        {
            int nTotal = input.wheel.Length;

            bool[] initialState = new bool[nTotal];
            
            for (int i = 0; i < nTotal; ++i)
            {
                if (input.wheel[i] == 'X') initialState[i] = true;
                if (input.wheel[i] == '.') initialState[i] = false;

            }


            WheelFast wf = new WheelFast(input.wheel.Length);
            double ans2 = wf.solve(input.wheel);
            Logger.LogInfo(" wheel fast {}", ans2);
            return ((double)ans2).ToUsString(9);

            //Logger.LogInfo("Wheel: {}", input.wheel);
           // var ans = WheelLargeSlow.computeAnswer(initialState);


            ///return ((double) ans).ToUsString(9);
        }

        public string processInputFast(WheelInput input)
        {
            WheelFast wf = new WheelFast(input.wheel.Length);
            double ans2 = wf.solve(input.wheel);
            return ans2.ToUsString(9);
        }

        public string processInputSmall(WheelInput input)
        {
            return DynamicProgramming.processInput(input);
        }

        /// <summary>
        /// Start can be > stop, then wraps around.  Copies [start, stop] to source
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="source"></param>
        /// <param name="start"></param>
        /// <param name="stop"></param>
        /// <returns></returns>
        public static T[] copyArray<T>(T[] source, int start, int stop)
        {
            int len = 1+ ModdedLong.diff(start, stop, source.Length);

            T[] ret = new T[len];
            for(int i = 0; i < len; ++i)
            {
                ret[i] = source[(start + i) % source.Length];
            }

            return ret;
        }

        
        

        

        

       
        public WheelInput createInput(Scanner scanner)
        {
            WheelInput input = new WheelInput();
            input.wheel = scanner.nextWord();
            return input;
        }


        

        
    }

    
    

    public sealed class CombinArray
    {
        private static readonly Lazy<CombinArray> lazy =
            new Lazy<CombinArray>(() => new CombinArray());

        public static CombinArray Instance { get { return lazy.Value; } }

        private CombinArray()
        {
            Logger.LogTrace("Init combin array");
            combinArray = Combinations.generateCombin(200);
        }

        public BigInteger[][] combinArray;
    }
}
