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
            internal int leftLetter = -1;
            internal int rightLetter = -1;

            internal int count = 1;

            internal int betweenSet()
            {
            	return hasLetterBitSet.ClearBit(leftLetter).ClearBit(rightLetter);	
            }
        }

        private List<CarInfo> parseCars(TrainInput input)
        {
            List<CarInfo> carInfos = new List<CarInfo>();

            for (int i = 0; i < input.cars.Length; ++i)
            {
                carInfos.Add( new CarInfo() );

                int idx = 0;

                int curChar = -1; 

                while (idx < input.cars[i].Length)
                {
                    if ( (input.cars[i][idx] - 'a') != curChar)
                    {
                    	
                        curChar = input.cars[i][idx] - 'a';
                        
                        if (carInfos[i].leftLetter == -1)
                            carInfos[i].leftLetter = curChar ;

                        carInfos[i].rightLetter = curChar ;

                        if (carInfos[i].hasLetterBitSet.GetBit(curChar))
                        {
                        	Logger.LogTrace("Can't have ..axa...");
                        	return null;
                        }
                        
                        carInfos[i].hasLetterBitSet = carInfos[i].hasLetterBitSet.SetBit(curChar );
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
            
            if (carInfos == null)
            {
            	return 0;
            }
            
            int[] fact = ModdedLong.generateModFactorial(101, mod);
                        
            int interiorLettersBitSet = 0;
            foreach(CarInfo ci in carInfos)
            {
            	interiorLettersBitSet |= ci.betweenSet();	
            }
            
            foreach(CarInfo ci in carInfos)
            {
            	if (interiorLettersBitSet.GetBit(ci.leftLetter) ||
            		interiorLettersBitSet.GetBit(ci.rightLetter))
            	{
            		Logger.LogTrace(" xay and azw illegal");
            		return 0;
            	}
            }

            //Combine cars that have same letter
            for(int letter = 0; letter < 26; ++letter)
            {
                Predicate<CarInfo> hasLetterPredicate = ci => ci.hasLetterBitSet.GetBit(letter);

                List<CarInfo> carsToMerge = carInfos.FindAll(hasLetterPredicate);
                int check = carInfos.RemoveAll(hasLetterPredicate);

                Preconditions.checkState(check == carsToMerge.Count);
                
                Logger.LogTrace("Making component letter {}", (char) (letter + 'a'));
                
                if (carsToMerge.Count == 0)
                {
                	continue;
                }

                //New car has form xa + aa + aa + .. + ay

                int newLeftLetter = letter;
                int newRightLetter = letter;
				int newHasLettersBitSet = 0;
                int newCount = 1;
                int monoCars = 0;
                
                CarInfo leftCar = null;
                CarInfo rightCar = null;
                CarInfo interiorCar = null;

                foreach(CarInfo toMerge in carsToMerge)
                {
                	Logger.LogTrace("car left {} right {} count {}", toMerge.leftLetter.ToChar(), 
                		toMerge.rightLetter.ToChar(), toMerge.count);
                	
                	newHasLettersBitSet |= toMerge.hasLetterBitSet;
                	
                    if (toMerge.leftLetter == toMerge.rightLetter)
                    {
                    	if (toMerge.hasLetterBitSet.ClearBit(toMerge.leftLetter) != 0)
                    	{
                    		Logger.LogTrace("Invalid train axyza");
                    		return 0;
                    	}
                    	
                    	Preconditions.checkState(toMerge.leftLetter == letter);
                    	Preconditions.checkState(toMerge.count == 1);
                    	++monoCars;
                    	continue;
                    }
                    
                    if (toMerge.rightLetter == letter)
                    {
                    	if (leftCar != null)
                    	{
                    		Logger.LogTrace("Cannot have two cars like ax ay");
                    		return 0;
                    	}
                    	
                    	leftCar = toMerge;
                    	newLeftLetter = leftCar.leftLetter;
                    	continue;
                    }
                    
                    if (toMerge.leftLetter == letter)
                    {
                    	if (rightCar != null)
                    	{
                    		Logger.LogTrace("Cannot have two cars like xa and ya");
                    		return 0;
                    	}
                    	
                    	rightCar = toMerge;
                    	newRightLetter = rightCar.rightLetter;
                    	continue;
                    }
                    
                    //Letter must be interiour
                    if (interiorCar != null)
                    {
                    	Logger.LogTrace("2 cars with form xay and waz");
                    	return 0;
                    }
                    
                    interiorCar = toMerge;
                    
                    newLeftLetter = interiorCar.leftLetter;
                    newRightLetter = interiorCar.rightLetter;
                    
                    if (carsToMerge.Count > 1)
                    {
                    	Logger.LogTrace("Can only have 1 cars with form xay");
                    	return 0;
                    }
                    	
                }
                
                CarInfo merged = new CarInfo();
                merged.leftLetter = newLeftLetter;
                merged.rightLetter = newRightLetter;
                merged.hasLetterBitSet = newHasLettersBitSet;
                
                merged.count = (int) (1L * (leftCar != null ? leftCar.count % mod: 1) *
                	(rightCar != null ? rightCar.count : 1) *
                	(interiorCar != null ? interiorCar.count : 1) % mod) ;
                merged.count = (int)  ( (long)merged.count * fact[monoCars] % mod ) ;
                Logger.LogTrace("Adding merged left letter {} right letter {} count {}",
                	newLeftLetter.ToChar(),
                	newRightLetter.ToChar(),
                	merged.count);
                carInfos.Add(merged); 

            }

            //Merging done
            int ans = 1;
            
            int chooseOrder = fact[carInfos.Count];
            
            long mult = 1;
            foreach(CarInfo ci in carInfos)
            {
            	mult *= ci.count;
            	mult %= mod;
            }
           
            return (int) (chooseOrder * mult % mod);
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