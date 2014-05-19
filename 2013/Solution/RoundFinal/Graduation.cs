﻿#if DEBUG
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
    public class Graduation : InputFileProducer<GraduationInput>, InputFileConsumer<GraduationInput,int>
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

        public int processInput(GraduationInput input)
        {
            //y axis is time, x axis is position
            List<LineSegment<long>> segs = new List<LineSegment<long>>();
            for (int i = 0; i < input.nCars; ++i )
            {
                long distance = ModdedLong.diff(input.start[i], input.stop[i], input.nIntersections);
                segs.Add(LineExt.createSegmentFromCoords(input.start[i], input.timeEntered[i], 
                    input.start[i]+distance, distance));
            }
            return 3;
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



        public void getLongestSegment(int queryPosition, int queryTime, 
            out List<Point<int>> intersections, Car car)
        {
            intersections = new List<Point<int>>();
            long N = car.input.nIntersections;

            //Check time
            if (queryTime < car.enterTime || queryTime > car.exitTime)
                return;

            //Get position of car at queryTime
            long carPosition = (car.startPosition + (queryTime - car.enterTime)) % N;

            //Since car is advancing [car .... me] it is the left
            long curDifPosition = ModdedLong.diff(carPosition, queryPosition, N);

            //Time takes to close the distance is the diff / 2

            long backwardsCurDifPosition = ModdedLong.diff(queryPosition, carPosition, N);

            Preconditions.checkState(curDifPosition + backwardsCurDifPosition == N);

            //Just before intersection
            long timeForwardIntersection = queryTime + (curDifPosition - 1) / 2;

            long timeBackwardIntersection = queryTime - (backwardsCurDifPosition - 1) / 2;


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

        internal Car(GraduationInput input, int index)
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
