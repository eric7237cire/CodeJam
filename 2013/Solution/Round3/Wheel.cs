#define LOGGING
#define LOGGING_DEBUG
#define LOGGING_INFO
//#define LOGGING_TRACE

using CodeJamUtils;
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

    class DynamicProgramming
    {
        public string FormatGondolas(int state)
        {
            return string.Join("", state.ToBinaryString(nTotal).Replace('1', 'X').Replace('0', '.').Reverse());
        }
        readonly int endPosition;
        BigInteger[] expectedValue;
        int nTotal;

        public DynamicProgramming(int nTotal)
        {

            this.nTotal = nTotal;
            //nTotal bits == 1
            endPosition = (1 << nTotal) - 1;
            expectedValue = new BigInteger[1 << 20];

            Logger.LogTrace("total {} end {} = {}.", nTotal, endPosition.ToBinaryString(8), FormatGondolas(endPosition));
            for(int i = 0; i < expectedValue.Length; ++i)
            {
                expectedValue[i] = -1;
            }
        }

        public BigInteger calc(int curState, BigInteger pMult)
        {
            Logger.LogTrace("calc {} pMult {}", string.Join("", curState.ToBinaryString(nTotal).Replace('1', 'X').Replace('0', '.').Reverse()), pMult);
            if (curState == endPosition)
            {
                return 0;
            }

            if (expectedValue[curState] != -1)
            {
                return expectedValue[curState];
            }

            BigInteger totalReturn = 0;

            //Choose each possibility
            for(int i = 0; i < nTotal; ++i)
            {
                int pos = i;
                int price = nTotal;

                BitSet csBitSet = new BitSet(curState);
                while(true)
                {
                    if (!csBitSet[pos])
                    {
                        Logger.LogTrace("Hit gondola {} 1st free Pos {} Price {}", i, pos, price);
                        Logger.ChangeIndent(4);
                        totalReturn += price * pMult + calc(curState | 1 << pos, pMult / nTotal);
                        Logger.ChangeIndent(-4);
                        break;
                    }

                    ++pos;
                    if (pos == nTotal)
                        pos = 0;
                    --price;
                    Preconditions.checkState(price >= 1);
                }
            }

            return expectedValue[curState] = totalReturn;
        }
        
    }

    public class Wheel : InputFileConsumer<WheelInput, string>, InputFileProducer<WheelInput>
    {
          
        public string processInput(WheelInput input)
        {
            int nTotal = input.wheel.Length;

            int initialState = 0;
            int holeCount = 0;
            for( int i = 0; i < nTotal; ++i)
            {
                if (input.wheel[i] == 'X') initialState = initialState.SetBit(i);
                if (input.wheel[i] == '.') holeCount++;

            }

            BigInteger denom = 1;

            for (int i = 0; i < holeCount; ++i)
                denom *= nTotal;

            DynamicProgramming dp = new DynamicProgramming(nTotal);

            BigInteger num = dp.calc(initialState, denom / nTotal);

            Logger.LogDebug(" {} / {} ", num, denom);

            return ((double) new BigFraction(num, denom)).ToUsString(9);
        }

        

        public WheelInput createInput(Scanner scanner)
        {
            WheelInput input = new WheelInput();
            input.wheel = scanner.nextWord();
            return input;
        }
    }
}
