using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;

using Logger = Utils.LoggerFile;

[assembly: System.Runtime.CompilerServices.InternalsVisibleTo("UnitTest")]

//Merging boundaries, counting substrings.  Similar in idea to inclusion-exclusion principal http://en.wikipedia.org/wiki/Inclusion%E2%80%93exclusion_principle
namespace Round1C_2013.Problem1
{

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
            //int[] cons = new int[word.Length];
            
            //blocks counts of vowels / consonants / vowels etc...
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


            //ONly vowels
            if (blocks.Count == 1)
            {
                return 0;
            }
            
            //Start with assuming everything counts
            long total = summation(word.Length-1);

            //All substrings in 1st block (vowels) + up to n - 1 cononants from 1st block don count
			total -= summation(blocks[0] + Math.Min(n - 1, blocks[1]));
		
			total -= summation(blocks[blocks.Count - 1] + Math.Min(blocks[blocks.Count - 2], n - 1));
		

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
                
        //Find all substrings and count their consecutive consants, counts if >= n
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
        
        public long processInputBruteForce(Input input)
        {
            int ans =  bruteForce(input.name, input.n);
            Logger.LogDebug("ans is {0}", ans);
            return ans;
        }

        public long processInput(Input input)
        {
            long ans = calculate(input.name, input.n);
            Logger.LogDebug("ans is {0}", ans);
            return ans;
        }
        
         public static Input createInput(Scanner scanner)
        {
            Input input = new Input();
            input.name = scanner.nextWord();
            input.n = scanner.nextInt();

            return input;
        }


    }




    public class Input
    {
        public string name { get; internal set; }
        public int n { get; internal set; }


       
    }

}
