#if DEBUG
#define LOGGING_DEBUG
#define LOGGING_INFO
#define LOGGING_TRACE
#endif

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



        public void getLongestSegment(long queryPosition, long queryTime, 
            out List<Point<long>> furthestWithoutIntersecting, Car car)
        {
            furthestWithoutIntersecting = new List<Point<long>>();
            long N = car.input.nIntersections;
            
            //Get position of car at queryTime, because the car may not have entered yet, take car of 
            //negative positions
            long carPosition = (N + ((car.startPosition + (queryTime - car.enterTime)) % N)) % N;

            //Since car is advancing [car .... me] it is the left
            long curDifPosition = ModdedLong.diff(carPosition, queryPosition, N);

            //Enemy car is already there
            if (curDifPosition == 0 && queryTime >= car.enterTime && queryTime <= car.exitTime)
            {
                furthestWithoutIntersecting.Add(new Point<long>(queryPosition, queryTime));
                furthestWithoutIntersecting.Add(new Point<long>(queryPosition, queryTime));

                Logger.LogTrace("With car ! {} best line {} for query point/time ({}, {})", car, furthestWithoutIntersecting.ToCommaString(), queryPosition, queryTime);

                return;
            }

            //Time takes to close the distance is the diff / 2

            long backwardsCurDifPosition = ModdedLong.diff(queryPosition, carPosition, N);

            //This occurs if the car intersects exactly but outside the bounds of enterTime and exitTime
            if (curDifPosition == 0)
            {
                if (queryTime > car.exitTime)
                {
                    curDifPosition = N;
                } else if (queryTime < car.enterTime)
                {
                    backwardsCurDifPosition = N;
                }
                else
                {
                    //queryTime is withing [enter, exit] and should have been caught earlier
                    Preconditions.checkState(false);
                }
            }

            Preconditions.checkState(curDifPosition + backwardsCurDifPosition == N);

            //Just before intersection.  So if difference is 2, 4, 6, 8 then
            //just before the intersection is 0, 1, 2
            //If diff is 1,3, 5, 7 then
            //time is 0, 1, 2, etc
            long timeForwardIntersection = queryTime + (curDifPosition + 1) / 2 - 1;

            //Bounds checking
            if (timeForwardIntersection > car.input.totalTime)
                timeForwardIntersection = car.input.totalTime;

            //Would have intersected but the car exited before.  >= because timeForwardIntersection is the time *right before* intersecting, so
            //car would need 1 more unit time
            if (timeForwardIntersection >= car.exitTime)
                timeForwardIntersection = car.input.totalTime;

            long timeBackwardIntersection = queryTime - ( (backwardsCurDifPosition + 1) / 2 - 1 );

            //Similar logic
            if (timeBackwardIntersection <= car.enterTime)
                timeBackwardIntersection = 0;

            //Bounds checking
            if (timeBackwardIntersection < 0)
                timeBackwardIntersection = 0;

            long backPos =  ((queryTime - timeBackwardIntersection) + queryPosition) % N;
            long forwardPos = (queryPosition - (timeForwardIntersection - queryTime)) % N;
            if (forwardPos < 0)
                forwardPos += N;

            furthestWithoutIntersecting.Add(new Point<long>(backPos, timeBackwardIntersection));
            furthestWithoutIntersecting.Add(new Point<long>(forwardPos, timeForwardIntersection));

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
