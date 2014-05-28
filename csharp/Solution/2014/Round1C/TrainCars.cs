
#if DEBUG
#define LOGGING_DEBUG
#define LOGGING_INFO
#define LOGGING_TRACE
#endif

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using CodeJamUtils;
using Utils;
using Utils.tree;
using Logger = Utils.LoggerFile;

using CodeJam.Utils.graph;
using Utils.geom;
using NUnit.Framework;

namespace Round1C_2014.Problem2
{
    public class TrainInput
    {
        public int N;        
        public string[] cars;
    }

    public class Train : InputFileProducer<TrainInput>, InputFileConsumer<TrainInput, long>
    {
        public TrainInput createInput(Scanner scanner)
        {
            scanner.enablePlayBack();
            TrainInput input = new TrainInput();

            input.N = scanner.nextInt();

            scanner.buildStringArray(out input.cars, input.N);

            Logger.LogTrace("{}", scanner.finishPlayBack());
            return input;
        }

        class CarInfo
        {
           
            internal int hasLetterBitSet = 0;
            internal int firstLetter = -1;
            internal int lastLetter = -1;

            int count = 1;

            
        }

        private List<CarInfo> parseCars(TrainInput input)
        {
            List<CarInfo> carInfos = new List<CarInfo>();

            for (int i = 0; i < input.cars.Length; ++i)
            {
                carInfos.Add( new CarInfo() );

                int idx = 0;

                char curChar = ' '; 

                while (idx < input.cars[i].Length)
                {
                    if (input.cars[i][idx] != curChar)
                    {
                        curChar = input.cars[i][idx];
                        if (carInfos[i].firstLetter == -1)
                            carInfos[i].firstLetter = curChar - 'a';

                        carInfos[i].lastLetter = curChar - 'a';

                        carInfos[i].hasLetterBitSet = carInfos[i].hasLetterBitSet.SetBit(curChar - 'a');
                    }
                    ++idx;
                }
            }
            return carInfos;
        }

        
        const int mod = 1000000007;

        public long processInput(TrainInput input)
        {
            List<CarInfo> carInfos = parseCars(input);
            /*
            if (carInfos[i].hasLetter[curChar - 'a'])
            {
                //Impossible train, same car has same letter seperated by other letters
                return 0;
            }*/

            //Combine cars that have same letter
            for(int letter = 0; letter < 26; ++letter)
            {
                Predicate<CarInfo> hasLetterPredicate = ci => ci.hasLetterBitSet.GetBit(letter);

                List<CarInfo> carsToMerge = carInfos.FindAll(hasLetterPredicate);
                int check = carInfos.RemoveAll(hasLetterPredicate);

                Preconditions.checkState(check == carsToMerge.Count);

                //New car has form xa + aa + aa + .. + ay

                int newLeftLetter = letter;
                int newRightLetter = letter;

                int newCount = 1;
                int monoCars = 0;

                foreach(CarInfo toMerge in carsToMerge)
                {
                    if (
                }

            }

            bool[] interiorLetters = new bool[26];
            
            foreach(var ci in carInfos)
            {
                foreach (char c in ci.letterGroupes.Skip(1).Take(ci.letterGroupes.Count - 2))
                {
                    interiorLetters[c - 'a'] = true;
                }
            }

            List<int>[] leftLetters;
            List<int>[] rightLetters;
            List<int>[] monoLetters; //cars with just 1 letter

            Ext.createArray(out leftLetters, 26);
            Ext.createArray(out rightLetters, 26);
            Ext.createArray(out monoLetters, 26);

            for (int i = 0; i < carInfos.Count; ++i )
            {
                int leftLetter = carInfos[i].letterGroupes[0] - 'a';
                int rightLetter = carInfos[i].letterGroupes.GetLastValue() - 'a';

                if (interiorLetters[leftLetter] || interiorLetters[rightLetter])
                {
                    return 0;
                }

                if (leftLetter == rightLetter)
                {
                    monoLetters[leftLetter].Add(i);
                    continue;
                }

                leftLetters[leftLetter].Add(i);
                rightLetters[rightLetter].Add(i);
            }

            int forceLeft = -1;
            int forceRight = -1;

            for (int let = 0; let < 26; ++let )
            {
                if (leftLetters[let].Count == rightLetters[let].Count)
                    continue;

                if (leftLetters[let].Count == 0 && rightLetters[let].Count == 1)
                    continue;

                if (leftLetters[let].Count == 1 && rightLetters[let].Count == 0)
                    continue;

                if (leftLetters[let].Count == rightLetters[let].Count + 1)
                {
                    if (forceLeft != -1)
                    {
                        return 0;
                    }
                    forceLeft = let;
                    continue;
                }

                if (leftLetters[let].Count + 1 == rightLetters[let].Count )
                {
                    if (forceRight != -1)
                    {
                        return 0;
                    }
                    forceRight = let;
                    continue;
                }

                return 0;
            }

                //Any a[^a]a
                return -9999;
        }
    }
    [TestFixture]
    public class TrainTest
    {
        [Test]
        public void Test()
        {
            TrainInput i = new TrainInput();
           

            Train e = new Train();
            Assert.AreEqual("2", e.processInput(i));
        }
    }
}