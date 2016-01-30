#define LOGGING_DEBUG
#define LOGGING_INFO
using CodeJam._2014.Round2;
using CodeJam.Main;
using CodeJam.Main.Plumbing;
using CodeJam.Utils.geom;
using CodeJam.Utils.graph;
using CodeJam.Utils.math;
using CodeJamUtils;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;
using Wintellect.PowerCollections;


using Logger = Utils.LoggerFile;



namespace Year2015.RoundQual.Problem3
{
    public class DijChar : IEquatable<DijChar>
    {
        public static readonly DijChar one = new DijChar('1');
        public static readonly DijChar i = new DijChar('i');
        public static readonly DijChar j = new DijChar('j');
        public static readonly DijChar k = new DijChar('k');

        private int _index;

        private static int[][] mult;
        static DijChar()
        {
            mult = new int[4][];

            mult[0] = new int[] { 1, 2, 3, 4 };
            mult[1] = new int[] { 2, -1, 4, -3 };
            mult[2] = new int[] { 3, -4, -1, 2 };
            mult[3] = new int[] { 4, 3, -2, -1 };
        }
        private DijChar(char c)
        {
            build(c, this);
        }

        private static void build(char c, DijChar r)
        {

            
            if (c == '1')
            {
                r._index = 1;
                return;
            }

            r._index =  (2 + c - 'i');

            return;
        }

        public bool Equals(DijChar other)
        {
            return _index == other._index;
        }

        public override bool Equals(System.Object obj)
        {
            // If parameter is null return false.
            if (obj == null)
            {
                return false;
            }

            // If parameter cannot be cast to Point return false.
            DijChar p = obj as DijChar;
            if ((System.Object)p == null)
            {
                return false;
            }

            // Return true if the fields match:
            return this.Equals(p);
        }

        public DijChar(int index)
        {
            if ( Math.Abs(index) < 1 || Math.Abs(index) > 4) 
                throw new InvalidOperationException("only 1 to 4");

            _index = index;

        }

        public static implicit operator DijChar(char c)
        {
            return new DijChar(c);
        }
        public static implicit operator DijChar(int i)
        {
            if (i != 1)
                throw new InvalidCastException("Only 1");

            return new DijChar('1');
        }
        public static DijChar operator -(DijChar lhs)
        {
            return new DijChar(lhs._index * -1);
        }
        public static DijChar operator *(DijChar lhs, DijChar rhs)
        {
            int resultNegative = lhs._index * rhs._index;
            int index = mult[Math.Abs(lhs._index)-1][Math.Abs(rhs._index)-1];

            if (resultNegative < 0)
                index *= -1;

            return new DijChar(index);
        }
    }


    [TestClass]
    public class DijCharTests
    {
        [TestMethod]
        public void test()
        {
            DijChar[] chars = new DijChar[]
            {
               1, 'i', 'j', 'k'
            };

            Assert.AreEqual(-DijChar.k, DijChar.j * DijChar.i);

        }
    }

    public class DijkstraInput
    {
        //length input string
        public int L;

        //How many times it is repeated
        public long X;

        //The input string
        public String S;
        
        public Dictionary<char, int> d = new Dictionary<char, int>();

    }

    public class Dijkstra : InputFileProducer<DijkstraInput>, InputFileConsumer2<DijkstraInput>
    {
        public DijkstraInput createInput(Scanner scanner)
        {
            //scanner.enablePlayback();
            DijkstraInput input = new DijkstraInput();

            input.L = scanner.nextInt();
            input.X = scanner.nextLong();

            input.S = scanner.nextWord();





            //Logger.LogInfo("Input {}", scanner.finishPlayBack());
            return input;
        }

        public void processInput(DijkstraInput input, IAnswerAcceptor answerAcceptor, int testCase)
        {
            //

            DijChar dj = DijChar.one;

            if (input.L * input.X < 3)
            {
                answerAcceptor.Accept("NO");
                return;
            }
            foreach (char c in input.S)
            {
                dj *= c;
            }

            DijChar productOfS = dj;

            //Now square it at most X % 4 times

            //Note any one to the 4th power is always 1
            dj = DijChar.one;

            for(int i = 0; i < input.X % 4; ++i)
            {
                dj *= productOfS;
            }

            if (dj.Equals(-DijChar.one) == false)
            {
                answerAcceptor.Accept("NO");
                return;
            }

            bool foundI = false;

            //Take at most 8 copies since will repeat after 4 times
            String finalString = String.Concat(Enumerable.Repeat(input.S, (int) Math.Min(8, input.X)));
            dj = DijChar.one;
            foreach (char c in finalString)
            {
                dj *= c;

                if (foundI == false)
                {
                    if (dj.Equals(DijChar.i))
                    {
                        foundI = true;
                        dj = DijChar.one;
                        continue;
                    }
                }
                else
                {
                    if (dj.Equals(DijChar.j))
                    {
                        answerAcceptor.Accept("YES");
                        return;
                    }
                }
            }

            answerAcceptor.Accept("NO");
        }
        public void processInput_slow(DijkstraInput input, IAnswerAcceptor answerAcceptor, int testCase)
        {

            String finalString = String.Concat(Enumerable.Repeat(input.S, (int)input.X));

            DijChar dj = DijChar.one;

            if (finalString.Length < 3)
            {
                answerAcceptor.Accept("NO");
                return;
            }
            foreach (char c in finalString)
            {
                dj *= c;
            }

            if (dj.Equals(-DijChar.one) == false)
            {
                answerAcceptor.Accept("NO");
                return;
            }

            bool foundI = false;
            

            dj = DijChar.one;
            foreach(char c in finalString)
            {
                dj *= c;

                if (foundI == false)
                {
                    if (dj.Equals(DijChar.i))
                    {
                        foundI = true;
                        dj = DijChar.one;
                        continue;
                    }
                } else 
                {
                    if (dj.Equals(DijChar.j))
                    {
                        answerAcceptor.Accept("YES");
                        return;
                    }
                }
            }

            answerAcceptor.Accept("NO");
        }
        
    }

}