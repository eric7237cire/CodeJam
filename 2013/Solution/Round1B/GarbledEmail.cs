#define PERF

using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Globalization;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Collections;
using System.Reflection;
using System.Resources;
using Trie;

using Logger = Utils.LoggerFile;

[assembly: System.Runtime.CompilerServices.InternalsVisibleTo
                          ("UnitTest")]


//Using trie with variables min change distance

namespace GarbledEmail
{

    public class Input
    {
        public string word { get; internal set; }


        public static Input createInput(Scanner scanner)
        {
            Input input = new Input();
            input.word = scanner.nextWord();

            return input;
        }
    }

    internal sealed class GarbledEmail : InputFileConsumer<Input, int>
    {
        internal int minCount(int lastChangeDistance, int progress, string S)
        {
            

            int[][] memoize = new int[TrieNodePtr.minDistance + 1][];
            WordMatch[][] bestMatches = null;

#if (!PERF)
            new WordMatch[TrieNode.minDistance + 1][];
#endif
            for (int i = 0; i <= TrieNodePtr.minDistance; ++i)
            {
                memoize[i] = new int[S.Length];
#if (!PERF)
                bestMatches[i] = new WordMatch[S.Length];
#endif
            }

           
            int ret = minCount(lastChangeDistance, progress, S, memoize, bestMatches);

#if (!PERF)
            int rebuildSolutionProg = 0;
            int lastChange = 0;

            while(rebuildSolutionProg < S.Length)
            {
                WordMatch match = bestMatches[lastChange][rebuildSolutionProg];

                if (match == null)
                {
                    Logger.Log("No match at {0} {1}", lastChange, rebuildSolutionProg);
                }
                Debug.Assert(match != null);

                Logger.Log("Best match at index {0} is word {1} with {2} changes at indices {3}", rebuildSolutionProg, match.Word, match.ChangeCount, match.Changes);
                rebuildSolutionProg += match.Word.Length;

                if (match.ChangeCount > 0)
                {
                    lastChange = match[match.ChangeCount - 1, WordMatch.LeftOrRight.right] + 1;

                }
                else if (lastChange > 0)
                {
                    lastChange += match.Word.Length;
                }

                if (lastChange > TrieNode.minDistance)
                {
                    lastChange = 0;
                }
            }
#endif

            return ret;
        }

        internal int minCount(int lastChangeDistance, int progress, string S, int[][] memoize, WordMatch[][] bestMatches)
        {
            if (progress >= S.Length)
            {
                return 0;
            }

            if (memoize[lastChangeDistance][progress] > 0)
            {
                return memoize[lastChangeDistance][progress] - 1;
            }

            Logger.Log("minCount last change: {0} progress: {1}", lastChangeDistance, progress);

            List<WordMatch> matches;
            root.parseText(S, out matches, progress);

            int currentMin = Int32.MaxValue / 2;
            WordMatch bestMatch = null;

            foreach (WordMatch match in matches)
            {
                //check last change distance
                if (lastChangeDistance > 0 && match.NumChanges > 0 && lastChangeDistance + match.LeftmostChange < TrieNodePtr.minDistance)
                {
                    continue;
                }

                string matchWord = match.Word;

                int newLastChangeDistance = 0;
                if (match.NumChanges > 0)
                {
                    newLastChangeDistance = 1 + match.RightmostChange;
                }
                else if (lastChangeDistance > 0)
                {
                    newLastChangeDistance = lastChangeDistance + match.Word.Length;
                }

                if (newLastChangeDistance > TrieNodePtr.minDistance)
                {
                    newLastChangeDistance = 0;
                }

                int matchCost = match.NumChanges + minCount(newLastChangeDistance, progress + matchWord.Length, S, memoize, bestMatches);

                if (matchCost < currentMin)
                {
                    currentMin = Math.Min(matchCost, currentMin);
                    bestMatch = match;
                }

            }

            memoize[lastChangeDistance][progress] = currentMin + 1;
#if (!PERF)
            bestMatches[lastChangeDistance][progress] = bestMatch;
#endif
            return currentMin;
        }

        static void Main(string[] args)
        {
            // Put the following code before InitializeComponent()
            String culture = "en-US";
            // Sets the culture to French (France)
            Thread.CurrentThread.CurrentCulture = new CultureInfo(culture);
            // Sets the UI culture to French (France)
            Thread.CurrentThread.CurrentUICulture = new CultureInfo(culture);

            
            GarbledEmail diamond = new GarbledEmail();

            //string baseDir = @"C:\codejam\CodeJam\2013\1B\Osmos\Osmos\";
            Runner<Input, int> runner = new Runner<Input, int>(diamond, Input.createInput);

            List<string> list = new List<string>();
            // list.Add("sample.txt");

            //string[] strs = Assembly.GetExecutingAssembly().GetManifestResourceNames();

            // ResourceSet resourceSet = new ResourceSet(Assembly.GetExecutingAssembly().GetManifestResourceStream(strs[0]));


            list.Add("C_small_practice");
            list.Add("C_large_practice");

            Stopwatch timer = Stopwatch.StartNew();
            //runner.run(list);
            runner.runMultiThread(list, Round1B.Properties.Resources.ResourceManager);

            timer.Stop();
            TimeSpan timespan = timer.Elapsed;

            Console.WriteLine(String.Format("Total {0:00}:{1:00}:{2:00}", timespan.Minutes, timespan.Seconds, timespan.Milliseconds / 10));

        }



        private Dictionary dict;
        private TrieNode root;

        public GarbledEmail(Dictionary dict)
        {
            this.dict = dict;

            root = TrieNode.createRootNode(dict);
        }

        public GarbledEmail()
        {
            dict = new Dictionary();

            using (StreamReader file =
   new System.IO.StreamReader(@"C:\codejam\CodeJam\2013\Solution\Round1B\garbled_email_dictionary.txt"))
            {
                string line;
                while ((line = file.ReadLine()) != null)
                {
                    //Console.WriteLine(line);
                    dict.words.Add(line);
                }
            }

            root = TrieNode.createRootNode(dict);
        }

        public int processInput(Input input)
        {


            return minCount(0, 0, input.word);
        }


    }
}
