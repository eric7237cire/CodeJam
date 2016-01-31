#define LOGGING_DEBUG
#define LOGGING_INFO
using CodeJam._2014.Round2;
using CodeJam.Utils.geom;
using CodeJam.Utils.graph;
using CodeJam.Utils.math;
using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;
using Wintellect.PowerCollections;
using CodeJam.Main;
using Logger = Utils.LoggerFile;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace Year2015.Round1A.Problem2
{

    public class HairCutInput
    {
        public int B;
        public int N;

        public int[] M;
        
    }

    [TestClass]
    public class HairCut : InputFileProducer<HairCutInput>, InputFileConsumer2<HairCutInput>
    {
        public HairCutInput createInput(Scanner scanner)
        {
            //scanner.enablePlayback();
            HairCutInput input = new HairCutInput();

            input.B = scanner.nextInt();
            input.N = scanner.nextInt();

            input.M = scanner.nextIntArray(input.B);

            //Logger.LogInfo("Input {}", scanner.finishPlayBack());
            return input;
        }


        public long howManyPeople(long minutes, HairCutInput input)
        {
            if (input.B <= 0)
                return 0;

            if (minutes < 0)
            {
                return 0;
            }

            long numPeople = 0;

            for (int barber = 0; barber < input.B; ++barber)
            {
                Preconditions.checkState(input.M[barber] > 0);

                numPeople += minutes / input.M[barber];
            }

            return input.B + numPeople;
        }

        [TestMethod]
        public void TestHowManyPeople()
        {
            HairCutInput hi = new HairCutInput();
            hi.B = 0;
            hi.M = new int[0];

            Assert.AreEqual(0, howManyPeople(1000, hi));

            hi.B = 1;
            hi.M = new int[1] { 7 } ;

            Assert.AreEqual(0, howManyPeople(-1, hi));
            Assert.AreEqual(1, howManyPeople(0, hi));
            Assert.AreEqual(1, howManyPeople(1, hi));
            Assert.AreEqual(1, howManyPeople(6, hi));
            Assert.AreEqual(2, howManyPeople(7, hi));
            Assert.AreEqual(2, howManyPeople(13, hi));
            Assert.AreEqual(3, howManyPeople(14, hi));

        }

        public long minMinutesToHande(long numberOfPeople, HairCutInput input)
        {
            if (numberOfPeople <= 0)
                return 0;

            //a closed interval which surely contains the first x for which p(x) is true.
            long lo = 0;
            long hi = numberOfPeople * input.M[0];

            while (lo < hi)
            {
                long mid = lo + (hi - lo) / 2;

                bool isFastEnough = howManyPeople(mid, input) >= numberOfPeople;

                if (isFastEnough)
                {
                    hi = mid;
                } else
                {
                    lo = mid + 1;
                }
            }

            Preconditions.checkState(lo == hi);
            Preconditions.checkState(howManyPeople(lo, input) >= numberOfPeople);
            Preconditions.checkState(howManyPeople(lo - 1, input) < numberOfPeople);

            return lo;
        }

        [TestMethod]
        public void TestMinMinutesToHandle()
        {
            HairCutInput input = new HairCutInput();
            input.B = 2;
            input.M = new int[] { 3, 4 };

            Assert.AreEqual(0, minMinutesToHande(0, input));
            Assert.AreEqual(0, minMinutesToHande(1, input));
            Assert.AreEqual(0, minMinutesToHande(2, input));
            Assert.AreEqual(3, minMinutesToHande(3, input));
            Assert.AreEqual(4, minMinutesToHande(4, input));
            Assert.AreEqual(6, minMinutesToHande(5, input));
            Assert.AreEqual(8, minMinutesToHande(6, input));
        }

        public void processInput(HairCutInput input, IAnswerAcceptor answerAcceptor, int testCase)
        {
            //Make 0 based
            int placesLeftInLine = input.N - 1;

            long howLongIsTheWait = minMinutesToHande(input.N, input);

            long howManyPeopleJustBefore = howManyPeople(howLongIsTheWait - 1, input);

            long barbersTakingAtTimeT = input.N - howManyPeopleJustBefore;

            int barber = -1;
            int modCount = 0;

            for(int b = 0; b < input.B; ++b)
            {
                if (howLongIsTheWait % input.M[b] == 0)
                {
                    ++modCount;
                    if (modCount == barbersTakingAtTimeT)
                    {
                        barber = b;
                    }
                }
            }

            
            answerAcceptor.Accept(1 + barber);
               
        }
    }

}