#if DEBUG
#define LOGGING_DEBUG
#define LOGGING_INFO
#define LOGGING_TRACE
#endif

using Utils.math;
using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;
using Utils.geom;
using Logger = Utils.LoggerFile;

namespace RoundFinal
{
    public class Graduation : InputFileProducer<GraduationInput>, InputFileConsumer<GraduationInput,long>
    {

        public GraduationInput createInput(Scanner scanner)
        {
            GraduationInput input = new GraduationInput();

            input.nCars = scanner.nextInt();
            input.totalTime = scanner.nextLong();
            input.nIntersections = scanner.nextLong();

            input.start = new long[input.nCars];
            input.stop = new long[input.nCars];
            input.timeEntered = new long[input.nCars];

            for(int i = 0; i < input.nCars; ++i)
            {
                input.start[i] = scanner.nextLong()-1;
                input.stop[i] = scanner.nextLong()-1;
                input.timeEntered[i] = scanner.nextLong();
            }

            return input;
        }

        public long processInput(GraduationInput input)
        {
            //y axis is time, x axis is position
            long bestLength = 0;

            if (input.nCars == 0)
            {
                return input.totalTime;
            }

            List<Car> cars = new List<Car>();
            for (int i = 0; i < input.nCars; ++i )
            {
                cars.Add(new Car(input, i));
            }

            List<Point<long>> pointsToTest = new List<Point<long>>();
            for (int i = 0; i < input.nCars; ++i )
            {
                for(int dx = -1; dx <= 1; ++dx)
                {
                    for(int dy = -1; dy <= 1; ++dy)
                    {
                        if (dx == dy)
                            continue;

                        pointsToTest.Add(new Point<long>(
                            (input.nIntersections + cars[i].startPosition + dx) % input.nIntersections, cars[i].enterTime + dy));
                        pointsToTest.Add(new Point<long>(
                           (input.nIntersections + cars[i].stopPosition + dx) % input.nIntersections, cars[i].exitTime + dy));
                    }
                }
            }

            foreach(Point<long> point in pointsToTest)
            {
                if (point.Y < 0 || point.Y >= input.totalTime)
                    continue;

                //long localMax = 0;
                Point<long> bestStart = null;
                Point<long> bestStop = null;

                foreach(Car car in cars)
                {
                    List<Point<long>> bestPoints;
                    getLongestSegment(point.X, point.Y, out bestPoints, car);

                    if (bestPoints.Count == 0)
                        continue;

                    if (bestStart == null || bestStart.Y < bestPoints[0].Y)
                        bestStart = bestPoints[0];

                    if (bestStop == null || bestStop.Y > bestPoints[1].Y)
                        bestStop = bestPoints[1];
                    
                    //Short circuit
                	if (bestStop.Y - bestStart.Y <= bestLength)
                	{
                		break;
                	}
                }

                long length = bestStop.Y - bestStart.Y;

                if (length > bestLength)
                {
                    Logger.LogTrace("For point {} best between {} and {}", point, bestStart, bestStop);
                    bestLength = length;
                }
            }
            return bestLength;
        }

        public int processInputSmall(GraduationInput input)
        {
            List<Car> cars = new List<Car>();
            for (int i = 0; i < input.nCars; ++i )
            {
                cars.Add(new Car(input, i));
            }

            int maxTimeTraveled = 0;

            for (int myStartTime = 0; myStartTime < input.totalTime; ++myStartTime )
            {
                for(int myStartPos = 0; myStartPos < input.nIntersections; ++myStartPos)
                {
                    ModdedLong myPos = new ModdedLong(myStartPos, (int) input.nIntersections);
                    int timeTraveled = 0;

                    Logger.LogTrace("\n*** Loop start pos {} start time {}", myStartPos, myStartTime);

                    while(true)
                    {
                        bool ok = true;

                        foreach(Car car in cars)
                        {
                            Logger.LogTrace("Checking car at {}.  time {} - {} "
                            + " for position {} at time {}",
                                car.startPosition, car.enterTime, car.exitTime, myPos, myStartTime+timeTraveled);
                            if (car.isAtPosition(myPos, myStartTime+timeTraveled))
                            {
                                Logger.LogTrace("Car is there");
                                ok = false;
                                break;
                            }

                            if (car.isAtPosition(myPos - 1, myStartTime + timeTraveled + 1))
                            {
                                Logger.LogTrace("Car will be at next spot, cannot advance");
                                ok = false;
                                break;
                            }

                            if (timeTraveled > 0 && car.isAtPosition(myPos, myStartTime + timeTraveled - 1)
                                && car.isAtPosition(myPos+1, myStartTime + timeTraveled ))
                            {
                                Logger.LogTrace("Car was be passed, must exit");
                                ok = false;
                                break;
                            }

                            if ( car.isAtPosition(myPos-1, myStartTime + timeTraveled )
                                && car.isAtPosition(myPos, myStartTime + timeTraveled + 1))
                            {
                                Logger.LogTrace("Car will be passed, must exit");
                                ok = false;
                                break;
                            }
                        }

                        if (!ok)
                            break;

                        ++timeTraveled;
                        --myPos;

                        if (timeTraveled >= maxTimeTraveled)
                        {
                            maxTimeTraveled = timeTraveled;
                            Logger.LogDebug("===Max start {} exit {} time {}",
                                myStartPos, myPos, timeTraveled);
                        }

                        if (myStartTime + timeTraveled == input.totalTime)
                        {
                            break;
                        }
                        
                    }
                }
            }
                return maxTimeTraveled;
        }

        private void getIntersection(Fraction queryPosition, Fraction queryTime,
        	out Point<Fraction> forwards, out Point<Fraction> backwards, Car car)
        {
        	long N = car.input.nIntersections;
        	
        	//Get position of car at queryTime, because the car may not have entered yet, take car of 
            //negative positions
            Fraction carPosition = getPositionForTime(car.startPosition, car.enterTime, queryTime, N, 1);
            
            //Since car is advancing [car .... me] it is the left
            Fraction curDifPosition = ModdedLong.diff(carPosition, queryPosition, N);
            
            //Enemy car is already there
            if (curDifPosition == 0 )
            {
                forwards = new Point<Fraction>(queryPosition, queryTime);
                backwards = new Point<Fraction>(queryPosition, queryTime);

                Logger.LogTrace("With car ! {} for query point/time ({}, {})", car, 
                	queryPosition, queryTime);

                return;
            }
            
            Fraction backwardsCurDifPosition = ModdedLong.diff(queryPosition, carPosition, N);
            
            Logger.LogTrace( "Original Diff forward {} diff backward {}  car: ({}, {})",
            	curDifPosition, backwardsCurDifPosition, queryTime, carPosition);
                         
            Fraction timeForwardIntersection = queryTime + curDifPosition / 2;
            
            Preconditions.checkState(timeForwardIntersection >= queryTime, "time forward too small");
            
            Fraction timeBackwardIntersection = queryTime - backwardsCurDifPosition / 2; 
            
            //positive slope so add
            Fraction backPos =  getPositionForTime(queryPosition, queryTime, timeBackwardIntersection, N, -1);
            //negative slope so subtract
            Fraction forwardPos = getPositionForTime(queryPosition, queryTime, timeForwardIntersection, N, -1);
                        
            Logger.LogTrace( "Diff forward {} time {} diff backward {} time {}",
            	curDifPosition, timeForwardIntersection, backwardsCurDifPosition, timeBackwardIntersection);
            
            forwards = new Point<Fraction>(forwardPos, timeForwardIntersection);
            backwards = new Point<Fraction>(backPos, timeBackwardIntersection);

        }

       
        
        private static long getPositionForTime(long curPos, long curTime, long time, long N, int slopePosPerTime)
        {
        	return (long) getPositionForTime(curPos, curTime, (Fraction) time, N, slopePosPerTime);
        }
        
        private static Fraction getPositionForTime(Fraction curPos, Fraction curTime, Fraction time, long N, int slopePosPerTime)
        {
        	//Logger.LogTrace("{} {} {} {}", curPos, curTime, time, N);
        	Preconditions.checkState(0 <= curPos && curPos < N);
        	
        	Fraction pos = (curPos + slopePosPerTime * (time - curTime)) % N;
            
            if (pos < 0)
                pos += N;
            
            Preconditions.checkState(0 <= pos && pos < N);
            
            return pos;
        }

        //private readonly Fraction HALF = new Fraction(1, 2);
        private readonly Fraction QUARTER = new Fraction(1, 4);

        public void getLongestSegment(long queryPosition, long queryTime, 
            out List<Point<long>> furthestWithoutIntersecting, Car car)
        {
            furthestWithoutIntersecting = new List<Point<long>>();
            long N = car.input.nIntersections;
            
            Logger.LogTrace("\nStart getLongestSegment ({},{})", queryPosition, queryTime);
            
            Point<Fraction> forwards;
            Point<Fraction> backwards;

            Fraction posAtCarEnterTime = getPositionForTime(queryPosition, queryTime, car.enterTime+QUARTER, N, -1);
            long posAtCarExitTime = getPositionForTime(queryPosition, queryTime, car.exitTime, N, -1);

            Point<Fraction>[] intPoints = new Point<Fraction>[4];
            
            getIntersection(posAtCarEnterTime, car.enterTime+QUARTER, out intPoints[1], out intPoints[0], car);
            Logger.LogTrace("At enter time intersection {}, backward {}", intPoints[1], intPoints[0]);
            //Preconditions.checkState(forwards1.Y >= car.enterTime);
            
            getIntersection(posAtCarExitTime, car.exitTime, out  intPoints[3], out intPoints[2], car);
            
            Logger.LogTrace("At exit time intersection {}, backward {}", intPoints[3], intPoints[2]);
            //Preconditions.checkState(backwards2.Y <= car.exitTime);
            
            Func<Fraction, Boolean> isValidFunc = (pTime) => pTime >= car.enterTime && pTime <= car.exitTime;

            forwards = new Point<Fraction>(0, car.input.totalTime + 2);	
            
            for(int i = 0; i < 4; ++i)
            {
            	if (isValidFunc(intPoints[i].Y) && intPoints[i].Y >= queryTime)
            	{            
            		forwards = intPoints[i];
            		break;
            	}
            }
            
            backwards = new Point<Fraction>(0, -2);
            
            for(int i = 3; i >=0; --i)
            {
            	if (isValidFunc(intPoints[i].Y) && intPoints[i].Y <= queryTime)
            	{
            		backwards = intPoints[i];
            		break;
            	}
            }
            	

            Logger.LogTrace("Chosen forward intersection {}, backward {}", forwards, backwards);

            Logger.LogTrace("0 Back {}", backwards);
            
            forwards.Y -= new Fraction(1, 2);
            
            forwards.Y = forwards.Y.floor();
            
            backwards.Y += new Fraction(1, 2);
            
            Logger.LogTrace("1 Back {}", backwards);
            
            backwards.Y = backwards.Y.ceil();
            
            Logger.LogTrace("2 Back {}", backwards); 
            
            if (backwards.Y < 0)
            	backwards.Y = 0;
            
            if (forwards.Y > car.input.totalTime)
            	forwards.Y = car.input.totalTime;
            
            
            
            forwards.X = getPositionForTime(queryPosition, queryTime, forwards.Y, N, -1);
            backwards.X = getPositionForTime(queryPosition, queryTime, backwards.Y, N, -1);

            furthestWithoutIntersecting.Add(new Point<long>((long)backwards.X, (long)backwards.Y));
            furthestWithoutIntersecting.Add(new Point<long>((long)forwards.X, (long)forwards.Y));
                        
            
            Logger.LogTrace("With car {} best line {} for query point/time ({}, {})", car, furthestWithoutIntersecting.ToCommaString(), queryPosition, queryTime);

        }
    }

    public class Car
    {
        public long exitTime
        {
            get;
            internal set;
        }
        internal GraduationInput input;
        int carIndex;

        public long startPosition
        {
            get
            {
                return input.start[carIndex];
            }
        }

        public long stopPosition
        {
            get
            {
                return input.stop[carIndex];
            }
        }

        public long enterTime
        {
            get
            {
                return input.timeEntered[carIndex];
            }
        }

        public Car(GraduationInput input, int index)
        {
            this.input = input;
            this.carIndex = index;
            exitTime = enterTime + ModdedLong.diff(input.start[index], input.stop[index], input.nIntersections) ;
        }
        
       

        internal bool isAtPosition(long pos, long time)
        {
            if (time < enterTime || time > exitTime)
            {
                Logger.LogTrace("Not in time");
                return false;
            }

            long curPos = (startPosition + time - enterTime) % input.nIntersections;

            Logger.LogTrace("query pos {} time {}.  curPos {}", pos, time, curPos);

            return pos == curPos;
        }

        public override string ToString()
        {
 	         return "Car start ({0}, {1}) stop ({2}, {3}) ".FormatThis(startPosition, enterTime, stopPosition, exitTime);
        }
    }

    public class GraduationInput
    {
        public int nCars;
        public long totalTime;
        public long nIntersections;

        public long[] start;
        public long[] stop;
        public long[] timeEntered;
    }
}
