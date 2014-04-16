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

    class GarbledEmail : InputFileConsumer<Input, int>
    {
        
        int minCount(int lastChangeDistance, int progress, string S)
        {
            if (progress >= S.Length)
            {
                return 0;
            }
            List<TrieNode.WordMatch> matches;
            root.parseText(S, null, out matches, progress);

            if (lastChangeDistance < 0 || lastChangeDistance > 5)
            {
                Debug.Assert(lastChangeDistance >= 0 && lastChangeDistance <= 5);
            }
            

            int currentMin = Int32.MaxValue / 2;

            foreach(TrieNode.WordMatch match in matches)
            {
                //check last change distance
                if (lastChangeDistance > 0 && match.changeCount() > 0 && lastChangeDistance + match[0, TrieNode.WordMatch.LeftOrRight.left] < TrieNode.minDistance)
                {
                    continue;
                }

                string matchWord = match.Word;

                int newLastChangeDistance = 0;
                if (match.changeCount() > 0) {
                    newLastChangeDistance = 1 + match[match.changeCount() - 1, TrieNode.WordMatch.LeftOrRight.right];

                    if (newLastChangeDistance > 5)
                    {
                        newLastChangeDistance = 0;
                    }
                }
                int matchCost = match.changeCount() + minCount(newLastChangeDistance, progress + matchWord.Length, S);

                currentMin = Math.Min(matchCost, currentMin);
            }

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
            list.Add("sample.txt");
            //list.Add("B-small-practice.in");
            //list.Add("B-large-practice.in");

            runner.run(list);
        }



        private Dictionary dict;
        private TrieNode root;

        public GarbledEmail()
        {
            dict = new Dictionary();

            using (StreamReader file =
   new System.IO.StreamReader(@"C:\codejam\CodeJam\2013\1B\Osmos\Osmos\garbled_email_dictionary.txt"))
            {
                string line;
                while (( line = file.ReadLine()) != null)
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
