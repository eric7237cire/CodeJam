#if DEBUG
#define LOGGING_DEBUG
#define LOGGING_INFO
#define LOGGING_TRACE
#endif

using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;

using Logger = Utils.LoggerFile;

namespace RoundFinal
{
    public class CantStop : InputFileProducer<CantStopInput>, InputFileConsumer<CantStopInput, string>
    {
        public CantStopInput createInput(Scanner scanner)
        {
            CantStopInput input = new CantStopInput();
            input.N = scanner.nextInt();
            input.D = scanner.nextInt();
            input.k = scanner.nextInt();
            
            input.rolls = new List<int[]>();
            for(int i = 0; i < input.N; ++i)
            {
                int[] die = new int[input.D];
                for(int d = 0; d < input.D; ++d)
                    die[d] = scanner.nextInt();

            	input.rolls.Add( die );
            }
            
            return input;            	
        }

        public static bool ok ( int location, List<int> chosenNumbers, CantStopInput input)
        {
            foreach(int dieVal in input.rolls[location])
            {
                if (chosenNumbers.Contains(dieVal))
                    return true;
            }

            return false;
        }

        public static void expand( ref int bestStart, ref int bestLength, int left, int right, List<int> chosenNumbers, CantStopInput input)
        {
          //  Logger.LogTrace("Expand L {} R {} chosenNumbers {}",
              //   left, right, chosenNumbers.ToCommaString());

            const int indent = 2;
            Preconditions.checkState(right >= left);
            Preconditions.checkState(right < input.N);
            Preconditions.checkState(chosenNumbers.Count <= input.k);

            

            if (chosenNumbers.Count == 0)
            {
                Preconditions.checkState(left == right);
                for(int d = 0; d < input.D; ++d)
                {
                    chosenNumbers.Add(input.rolls[left][d]);
                    Logger.ChangeIndent(indent);
                    expand(ref bestStart, ref bestLength, left, right, chosenNumbers, input);
                    Logger.ChangeIndent(-indent);
                    chosenNumbers.RemoveAt(chosenNumbers.Count - 1);
                }
                return;
            }

            while(left > 0)
            {
                if (!ok(left-1, chosenNumbers, input))
                    break;

                --left;
            }

            while (right < input.N - 1)
            {
                if (!ok(right + 1, chosenNumbers, input))
                    break;

                ++right;
            }
            int length = right - left + 1;
           // Logger.LogTrace("Expanded to L {} R {} len {} chosenNumbers {}",
             //    left, right, right-left+1, chosenNumbers.ToCommaString());

            if (chosenNumbers.Count == input.k || length == input.N )
            {
                if (length > bestLength || (length >= bestLength && left < bestStart))
                {
                    bestLength = length;
                    bestStart = left;
                  //  Logger.LogTrace("Expand NEW best start {} best len {} L {} R {} chosenNumbers {}",
                  //   bestStart, bestLength, left, right, chosenNumbers.ToCommaString());
                }
                return;
            }

            if (left > 0)
            {
                for (int d = 0; d < input.D; ++d)
                {
                    chosenNumbers.Add(input.rolls[left-1][d]);
                    Logger.ChangeIndent(indent);
                    expand(ref bestStart, ref bestLength, left-1, right, chosenNumbers, input);
                    Logger.ChangeIndent(-indent);
                    chosenNumbers.RemoveAt(chosenNumbers.Count - 1);
                }
            }

            if (right < input.N - 1)
            {
                for (int d = 0; d < input.D; ++d)
                {
                    chosenNumbers.Add(input.rolls[right + 1][d]);
                    Logger.ChangeIndent(indent);
                    expand(ref bestStart, ref bestLength, left, right+1, chosenNumbers, input);
                    Logger.ChangeIndent(-indent);
                    chosenNumbers.RemoveAt(chosenNumbers.Count - 1);
                }
            }
        }
        public static void bestInterval(out int bestStart, out int bestLength, int left, int right, CantStopInput input)
        {
            const int indent = 2;

            Preconditions.checkState(right >= left, "right >= left");
            Preconditions.checkState(right < input.N, "right < input.N");
            Preconditions.checkState(input.k >= 1 && input.k <= 3, "k <= 3");

            int middle = (right + left) / 2;
            int length = right - left + 1;

            Logger.LogTrace("BestInterval start L {} R {} len {}", left, right, length);

            //Does not include middle
            int leftLength = middle - left;
            int rightLength = right - middle;

            int midBestStart = 0, midBestLength = 0;
            expand(ref midBestStart, ref midBestLength, middle, middle, new List<int>(), input);

            
            Preconditions.checkState(midBestLength > 0, "mid best > 0");

           // Logger.LogTrace("Best mid start {} len {}", midBestStart, midBestLength);

            int leftBestStart = 0, leftBestLength = 0;
            int rightBestStart = 0, rightBestLength = 0;

            bestLength = midBestLength;
            bestStart = midBestStart;

            if (leftLength >= bestLength && leftLength > 0)
            {
                Logger.ChangeIndent(indent);
                bestInterval(out leftBestStart, out leftBestLength, left, middle - 1, input);
                Logger.ChangeIndent(-indent);

                if (leftBestLength >= bestLength)
                {
                    bestLength = leftBestLength;
                    bestStart = leftBestStart;
                }
            }

           // Logger.LogTrace(" L {} R {} Right len {} best len {}", 
             //   left, right,
              //  rightLength, bestLength);
            if (rightLength > midBestLength && rightLength > leftBestLength)
            {
                Logger.ChangeIndent(indent);
                bestInterval(out rightBestStart, out rightBestLength, middle+1, right, input);
                Logger.ChangeIndent(-indent);

                if (rightBestLength > bestLength)
                {
                    bestLength = rightBestLength;
                    bestStart = rightBestStart;
                }
            }

            Logger.LogTrace("BestInterval done L {} R {} len {} best length {}", left, right, length, bestLength);

            
        }

        public string processInput(CantStopInput input)
        {
            int bestStart = 0, bestLength = 0;

            if (input.N == 0)
            {
                return "0 0";
            }

            bestInterval(out bestStart, out bestLength, 0, input.N - 1, input);

            return "" + bestStart + " " + (bestStart + bestLength - 1);
        }
    }

    public class CantStopInput
    {
        public int N;
        public int D;
        public int k;
        public List<int[]> rolls;
    }
}
