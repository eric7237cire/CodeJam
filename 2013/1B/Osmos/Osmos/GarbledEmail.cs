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
using Trie;

[assembly: System.Runtime.CompilerServices.InternalsVisibleTo
                          ("UnitTest1B")]




namespace GarbledEmail
{
#if (PERF)
    using Logger = CodeJamUtils.LoggerEmpty;
#else
    using Logger = CodeJamUtils.LoggerReal;
#endif

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
            int[][] memoize = new int[TrieNode.minDistance + 1][];
            WordMatch[][] bestMatches = null;

#if (!PERF)
            new WordMatch[TrieNode.minDistance + 1][];
#endif
            for (int i = 0; i <= TrieNode.minDistance; ++i)
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

            Logger.Log("minCount last change: {0} progress: {1}".Format(lastChangeDistance, progress));

            List<WordMatch> matches;
            root.parseText(S, null, out matches, progress);

            if (lastChangeDistance < 0 || lastChangeDistance > 5)
            {
                Debug.Assert(lastChangeDistance >= 0 && lastChangeDistance <= 5);
            }


            int currentMin = Int32.MaxValue / 2;
            WordMatch bestMatch = null;

            foreach (WordMatch match in matches)
            {
                //check last change distance
                if (lastChangeDistance > 0 && match.ChangeCount > 0 && lastChangeDistance + match[0, WordMatch.LeftOrRight.left] < TrieNode.minDistance)
                {
                    continue;
                }

                string matchWord = match.Word;

                int newLastChangeDistance = 0;
                if (match.ChangeCount > 0)
                {
                    newLastChangeDistance = 1 + match[match.ChangeCount - 1, WordMatch.LeftOrRight.right];
                }
                else if (lastChangeDistance > 0)
                {
                    newLastChangeDistance = lastChangeDistance + match.Word.Length;
                }

                if (newLastChangeDistance > 5)
                {
                    newLastChangeDistance = 0;
                }

                int matchCost = match.ChangeCount + minCount(newLastChangeDistance, progress + matchWord.Length, S, memoize, bestMatches);

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

            /*
            DiamondInfo di = DiamondInfo.getDiamondInfo(21);
            IDictionary<Node,double> nodeProb;
            processProb(di, out nodeProb);
            foreach(var kv in nodeProb)
            {
                Logger.Log("Left: {0} Right: {1} Prob : {2}", kv.Key.Left, kv.Key.Right, kv.Value);
            }

            Logger.Log("Done");
            return;
            */
            GarbledEmail diamond = new GarbledEmail();

            string baseDir = @"C:\codejam\CodeJam\2013\1B\Osmos\Osmos\";
            Runner<Input, int> runner = new Runner<Input, int>(baseDir, diamond, Input.createInput);

            List<string> list = new List<string>();
            // list.Add("sample.txt");
            // list.Add("C-small-practice.in");
            list.Add("C-large-practice.in");


            runner.run(list);

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
   new System.IO.StreamReader(@"C:\codejam\CodeJam\2013\1B\Osmos\Osmos\garbled_email_dictionary.txt"))
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
