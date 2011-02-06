using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Diagnostics;

namespace LogicProblems
{
    class UglyNumbers : TestCaseHandler
    {
        public UglyNumbers() {
            memory = new Dictionary<string, long[]>();
        }

        string TestCaseHandler.doTestCase() {
            string n = Console.ReadLine();

            if (n[0] == '-') {
                n.Remove(0,1);
            }

            //2, 3, 5, 7
            ugly = 0;

            long[] possib;

            getPossibilities(n, out possib);

            for (short p = -209; p < 210; ++p) {
                countUgly(p, possib[209+p]);
            }
            return ugly.ToString();
        }

        //Divisible by 2,3,5,7
        long ugly;

        Dictionary<string, long[] > memory;

        const ushort modValue = 210;
        const int modChunkSize = 19; //max of a ulong

        ushort mod(string n) {
            while (n.Length > modChunkSize) {
                n = (ulong.Parse(n.Substring(0,modChunkSize)) % modValue).ToString() + n.Substring(modChunkSize);
            }

            return (ushort) (ulong.Parse(n) % modValue);
        }

        void countUgly(short n, long freq) {
            if (n % 2 == 0 ||
                n % 3 == 0 ||
                n % 5 == 0 ||
                n % 7 == 0) {
                ugly += freq;
            }
        }

        void getPossibilities(string n, out long[] freq) {
            if (memory.ContainsKey(n)) {
                freq = memory[n];
                return;
            }
            Trace.WriteLine("getPossibil called with " + n);
            Trace.Indent();

            freq = new long[2 * modValue];
            
            if (n.Length == 0) {
                return;
            }

            Trace.WriteLine(n + " Incrementing " + (209 + long.Parse(n) % modValue));
            freq[209+mod(n)] += 1;
            
            for (int startStrLen = 1; startStrLen < n.Length; ++startStrLen) {
                string startStr = n.Substring(0, startStrLen);
                
                long startingNum = mod(startStr);
                Trace.WriteLine("Staring number is " + startingNum);
                long[] rhsFreq;
                getPossibilities(n.Substring(startStrLen), out rhsFreq);
                
                for (short p = 0; p <= 418; ++p) {
                    if (rhsFreq[p] == 0) {
                        continue;
                    }
                    //Trace.WriteLine("" + (startingNum + p) + " Incremening " + (209 + (checked(startingNum + p) % modValue)) + " by " + rhsFreq[209 + p]);
                    freq[ 209 + ( (startingNum + p - 209) % modValue) ] += rhsFreq[p];
                    freq[ 209 + ( (startingNum - (p - 209)) % modValue) ] += rhsFreq[p];
                    
                }
                //-209 to 209 
            }

            memory[n] = freq;

            Trace.Unindent();

        }

       


    }
}
