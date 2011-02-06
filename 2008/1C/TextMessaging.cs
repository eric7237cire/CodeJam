using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;


using System.Diagnostics;

namespace LogicProblems
{
    /*
     * The first line in the input file contains the number of test cases N. This is followed by N cases. 
     * Each case consists of two lines. On the first line we have the maximum number of letters to place on a key (P),
     * the number of keys available (K) and the number of letters in our alphabet (L) all separated by single spaces. 
     * The second line has L non-negative integers. Each number represents the frequency of a certain letter. 
     * The first number is how many times the first letter is used, the second number is how many times the second letter is used, and so on. 
     */
    class TextMessaging : TestCaseHandler
    {
        

        public string doTestCase() {
            

            string[] firstLineInput = Console.ReadLine().Split(new char[] {' '});

            int maxLettersPerKey = int.Parse(firstLineInput[0]);
            int keysAvailable = int.Parse(firstLineInput[1]);
            int lettersInAlphabet = int.Parse(firstLineInput[2]);

            string[] letterFrequenciesStr = Console.ReadLine().Split(new char[] { ' ' });

            Debug.Assert(letterFrequenciesStr.Count() == lettersInAlphabet);

            int[] letterFrequencies = new int[lettersInAlphabet];

            for(int i = 0; i < letterFrequenciesStr.Count(); ++i) {
                letterFrequencies[i] = int.Parse(letterFrequenciesStr[i]);
            }

            Debug.Assert(letterFrequencies.Count() == lettersInAlphabet);

            //int max = letterFrequencies.Max();
            Array.Sort(letterFrequencies, new CompareInt());

            long totalCost = 0; 
            int cost = 1;
            int keysLeft = keysAvailable; 
            
            foreach (int lf in letterFrequencies) {
                Debug.Assert(cost <= maxLettersPerKey);
                totalCost += cost * lf;
                --keysLeft;
                if (keysLeft == 0) {
                    ++cost;
                    keysLeft = keysAvailable;
                }            
            }

            return totalCost.ToString();
        }


        private class CompareInt : IComparer<int>
        {
            public int Compare(int o1, int o2) {
                return o2.CompareTo(o1);
            }
        }
        
    }
}
