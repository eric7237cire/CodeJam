using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;
[assembly: System.Runtime.CompilerServices.InternalsVisibleTo
          
                ("UnitTest")]

//Merging boundaries, counting substrings
namespace Round1C
{
#if (PERF)
   
    using Logger = CodeJamUtils.LoggerEmpty;
#else
    using Logger = CodeJamUtils.LoggerReal;
#endif

    static class Utils
    {
        internal static bool IsVowel(this Char ch)
        {
            if (ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u')
                return true;

            return false;
        }
    }

    internal class Consonants : InputFileConsumer<Input, long>
    {
        private static long summation(long n)
        {
            return (n * (n + 1)) / 2;
        }
        public static long calculate(string word, int n)
        {
            int[] cons = new int[word.Length];
            
            List<int> blocks = new List<int>();

            int curLen = 0;
            bool countingVowels = true;

            //also end with a vowel
            word += "a";

            for (int startIndex = 0; startIndex < word.Length; ++startIndex)
            {
                char c = word[startIndex];
                bool isVowel = c.IsVowel();
                if ( (countingVowels && isVowel) || (!countingVowels && !isVowel) )
                {
                    ++curLen;
                    continue;
                }

                //Special case, do not add blocks of consonants less than n
                if (!countingVowels && curLen < n)
                {
                    countingVowels = true;
                    curLen += blocks.GetLastValue() + 1;
                    blocks.RemoveAt(blocks.Count - 1);
                    continue;
                }
                blocks.Add(curLen);
                curLen = 1;
                countingVowels = !countingVowels;
            }

            if (curLen != 0 )
            {
                blocks.Add(curLen);
            }
            
            //take off last vowel added
            blocks[blocks.Count - 1] -= 1;

            

            //first and last block are vowels
            //if (blocks.Count % 2 == 0)
            {
              //  blocks.Add(0);
            }

            //ONly vowels
            if (blocks.Count == 1)
            {
                return 0;
            }
            long total = summation(word.Length-1);

            //first block
            
            if (true) // || blocks[0] > 0 || blocks[1] > n-1)
            {
                total -= summation(blocks[0] + Math.Min(n - 1, blocks[1]));
            }
            //last block
            if (true) // || blocks[blocks.Count - 1] > 0 || blocks[blocks.Count - 2] > n-1)
            {
                total -= summation(blocks[blocks.Count - 1] + Math.Min(blocks[blocks.Count - 2], n - 1));
            }

            for (int consBlockStart = 1; consBlockStart < blocks.Count - 2; consBlockStart += 2)
            {
                int leftConsBlockSize = blocks[consBlockStart];
                int vowelsInMiddle = blocks[consBlockStart + 1];
                int rightConsBlockSize = blocks[consBlockStart + 2];
                total -= summation( Math.Min(leftConsBlockSize,n-1) +
                    vowelsInMiddle + Math.Min(rightConsBlockSize, n-1) );
            }

            //take into account substrings within consonant blocks
            for (int consBlockStart = 1; consBlockStart < blocks.Count; consBlockStart += 2)
            {
                int consBlockSize = blocks[consBlockStart];

                int usableSize = consBlockSize - 2;  //chop off both ends

                int maxSubStrLen = n - 1;

                //Correct overcounting
                /*
                 * ..vvv]vv
                 * vv[vvv..
                 * correct overlap vv[v]vv
                 */
                int overLapSize = consBlockSize - 2 * (consBlockSize - maxSubStrLen);

                if (overLapSize > 0)
                {
                    total += summation(overLapSize);
                }

                //Making non valid all consonant substrings within a consonant block
                if (maxSubStrLen <= usableSize)
                {
                    long toRemove = summation(usableSize);
                    toRemove -= summation(usableSize - maxSubStrLen);
                    total -= toRemove;

                    //Correct on overcorrection
                    /* block = 4
                     * n = 3
                     * from left
                     *..vv]vv
                     * from right
                     * vv[vv..
                     * middle
                     * v[vv]..
                     * 
                     * So must remove the overlap between middle part (counted in toRemove) and n - 1
                     * */

                    int overLap = maxSubStrLen - 1;
                    if (overLap > 0)
                    {
                        total += 2 * summation(overLap);
                    }

                    //Correcting the correction
                    if (overLapSize > 0)
                    {
                        total -= summation(overLapSize);
                    }
                }

                
            }
            return total;
        }
                
        public static int bruteForce(string word, int n)
        {
            int count = 0;

            for (int startIndex = 0; startIndex < word.Length; ++startIndex)
            {
                for (int endIndex = startIndex + 1; endIndex <= word.Length; ++endIndex)
                {
                    string subString = word.Substring(startIndex, endIndex - startIndex);
                    //Logger.Log("substring {0}", subString);

                    int consecCons = 0;

                    foreach(char c in subString)
                    {
                        if (c.IsVowel())
                        {
                            consecCons = 0;
                        }
                        else
                        {
                            ++consecCons;
                            if (consecCons == n)
                            {
                                //Logger.Log("counts");
                                ++count;
                                break;
                            }
                        }
                    }
                }
            }

            return count;
        }

        public long processInput(Input input)
        {
            //int ans =  bruteForce(input.name, input.n);
            long ans = calculate(input.name, input.n);
            Logger.Log("ans is {0}", ans);
            return ans;
        }

        static void Main2(string[] args)
        {



            Consonants main = new Consonants();

            Runner<Input, long> runner = new Runner<Input, long>(main, Input.createInput);

            List<string> list = new List<string>();

            //list.Add("sample");
            list.Add("A_small_practice");
            list.Add("A-large-practice.in");

            Stopwatch timer = Stopwatch.StartNew();
            runner.run(list, Round1C.Properties.Resources.ResourceManager);
            // runner.runMultiThread(list);

            timer.Stop();
            TimeSpan timespan = timer.Elapsed;

            Console.WriteLine(String.Format("Total {0:00}:{1:00}:{2:00}", timespan.Minutes, timespan.Seconds, timespan.Milliseconds / 10));

        }
    }




    public class Input
    {
        public string name { get; private set; }
        public int n { get; private set; }


        public static Input createInput(Scanner scanner)
        {
            Input input = new Input();
            input.name = scanner.nextWord();
            input.n = scanner.nextInt();

            return input;
        }


    }

}
