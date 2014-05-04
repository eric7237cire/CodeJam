#define LOGGING
#define LOGGING_DEBUG
#define LOGGING_INFO
#define LOGGING_TRACE
#define FRAC
using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils.geom;
using Utils.math;
using Utils;

using Logger = Utils.LoggerFile;

namespace Round2.Pong
{
#if FRAC
    using Line = LineNumeric<BigFraction>;
    using Point = Point<BigFraction>;
    using NumType = BigFraction;
    using System.Numerics;
#else
    using Line = LineNumeric<DoubleNumeric>;
    using Point = Point<DoubleNumeric>;
    using NumType = DoubleNumeric;
#endif

    
    public class PongMain : InputFileConsumer<PongInput, string>, InputFileProducer<PongInput>
    {
        public static NumType calcAdd(NumType p0, NumType deltaP, BigInteger n, NumType height)
        {
            
            NumType pos = p0 + n * deltaP;
                
            long rem = (long)(pos / height);

            pos -= (rem * height);

            if (rem % 2 == 1)
            {
                pos = height - pos;
            }
            //Logger.LogDebug("CalcAdd {} + {} * {} = {} w.r.t height {}", p0, n, deltaP, pos, height);
            return pos;            
        }

        public static void calcStats(NumType p0, NumType deltaP, NumType height)
        {
            long rem = (long)(deltaP / (2 * height));
            deltaP -= (rem * 2 * height);

            if (deltaP > height)
            {
                //deltaP -= height ;
            }
            Logger.LogDebug("Adjusted \u0394P {}", deltaP);

            NumType maxDiff = deltaP;

            if (maxDiff > height)
            {
                maxDiff = 2 * height - maxDiff;
            }

            Logger.LogDebug("Max difference {}", maxDiff);

        }

        private static void calcPointsAndDiff(NumType p0, 
            NumType deltaP, BigInteger height, BigInteger start, int count,
            out NumType[] points, out NumType[] diffs
            )
        {
            points = new NumType[count];
            diffs = new NumType[count];

            for(int i = 0; i < count; ++i)
            {
                points[i] = calcAdd(p0, deltaP, start+i, height);

                if (i > 0)
                    diffs[i] = (points[i] - points[i - 1]).Abs();
            }
        }

        
        public static long calcToTargetManual(NumType p0, NumType deltaP, BigInteger height, NumType targetDiff )
        {
            return calcToTargetManual(p0, deltaP, height, targetDiff, (long) (3 * height));
        }
        public static long calcToTargetManual(NumType p0, NumType deltaP, BigInteger height, NumType targetDiff, long limit )
        {
            //Use calcAdd because p0 could be > height
            NumType lastPoint = calcAdd( p0, deltaP, 0, height);
            
            for(int i = 1; i <= limit; ++i)
            {
                NumType p = calcAdd(p0, deltaP, i, height);
                NumType diff = (p - lastPoint).Abs();
                Logger.LogDebug("manual calc.  p {} diff {} ", p, diff);
                if (diff > targetDiff)
                {
                    Logger.LogDebug("Found point {} larger than target {}", i, targetDiff);
                    return i;
                }

                lastPoint = p;
            }

            return -1;
        }

        public static long calcToTarget(NumType p0, NumType deltaP, BigInteger height, NumType targetDiff)
        {
            Logger.LogTrace("calcToTarget p0 {} + \u0394p {} * N  height: {}  target: {} ", 
                p0, deltaP, height, targetDiff);

            //Change in position of 2h is a no-op, so reduce ΔP by that amount 
            BigInteger rem = (deltaP / (2 * height)).floor();
            deltaP -= (rem * 2 * height);

            //Anything above H bounces off, so H+1 ==> H-1  H+n ==> H-n
            NumType maxDiff = deltaP;
            if (maxDiff > height)
            {
                maxDiff = 2 * height - maxDiff ;
            }

            Logger.LogTrace("max diff {} \u0394P {}", maxDiff, deltaP);

            if (targetDiff >= maxDiff)
            {
                Logger.LogTrace("targetDiff {} >= maxDiff {}", targetDiff, maxDiff);
                return -1;
            }

            NumType[] points;
            NumType[] diffs;

            calcPointsAndDiff(p0, deltaP, height, 0, 3, out points, out diffs);

            NumType calculatedDeltaD = 2 * (height - maxDiff);

            if (calculatedDeltaD.Equals(0))
            {
                Logger.LogDebug("deltaD % height = 0, using manual");
                return calcToTargetManual(p0, deltaP, height, targetDiff, 3);
            }

            if ((height / calculatedDeltaD) < 10)
            {
                Logger.LogDebug("using calc to target manual");
                long ans = calcToTargetManual(p0, deltaP, height, targetDiff);
                Preconditions.checkState(ans > 0);
                return ans;
            }

            Logger.LogTrace("DeltaD calculated = {}.  Initial Points {} Diffs {}", 
                calculatedDeltaD,
                points.ToCommaString(), diffs.Skip(1).ToCommaString());
                
            if (diffs[1] > targetDiff)
                return 1;

            if (diffs[2] > targetDiff)
                return 2;

            NumType deltaD = (diffs[2] - diffs[1]).Abs();
            long offset = 0;

            if (!deltaD.Equals(calculatedDeltaD))   //diffs[1].Equals(diffs[2]))
            {
                //The 2 diffs during the rebound should = estimatedDelta

                offset = 1;

                calcPointsAndDiff(p0, deltaP, height, 1, 3,
                    out points, out diffs);

                deltaD = (diffs[2] - diffs[1]).Abs();


                Logger.LogDebug("After offset + 1: points {}.  Diffs {}", points.ToCommaString(), diffs.ToCommaString());

            }

            //If descending, go to rebound
            if (diffs[2] < diffs[1])
            {
                Preconditions.checkState(calculatedDeltaD.Equals(deltaD));

                long pointsToZero = (long)(diffs[2] / deltaD).ceil();

                NumType[] pointsAtZero;
                NumType[] diffsAtZero;

                calcPointsAndDiff(p0, deltaP, height, 1 + pointsToZero, 4, out pointsAtZero, out diffsAtZero);

                
                Logger.LogTrace("Points to zero ceil( {} / {} ) = {}.  Point values: {} Diffs {}", 
                    diffs[2], deltaD, pointsToZero, pointsAtZero.ToCommaString(), diffsAtZero.Skip(1).ToCommaString());

                
                Preconditions.checkState(diffsAtZero[1] - deltaD <= 0);
                Preconditions.checkState((diffsAtZero[2] - diffsAtZero[1]).Equals(deltaD));
                Preconditions.checkState((diffsAtZero[3] - diffsAtZero[2]).Equals(deltaD)); //now going positive

                //long pointsLeft = calcToTarget(pb, deltaP, height, targetDiff, true);
                offset += pointsToZero+2;

                calcPointsAndDiff(p0, deltaP, height, 2+pointsToZero, 3, 
                    out points, out diffs);
    
                //DeltaD is already à jour
            }
            

            Preconditions.checkState(calculatedDeltaD.Equals(deltaD));

            NumType div = (targetDiff - diffs[2]) / deltaD;
            div = div.reduce();
            BigInteger pointsToGo =  (div).ceil();

            if (div.Denominator.Equals(BigInteger.One))
            {
                pointsToGo += 1;
            }

            NumType[] finalPoints;
            NumType[] finalDiffs;

            calcPointsAndDiff(p0, deltaP, height, offset + pointsToGo, 5, out finalPoints, out finalDiffs);
            
            Logger.LogTrace("PointsToGo {} offset {} Final Points {}.  Final diffs {}", pointsToGo, offset, finalPoints.ToCommaString(), finalDiffs.Skip(1).ToCommaString() );
            Logger.LogTrace("Return {}", pointsToGo + 2 + offset);
            return offset + (long)pointsToGo + 2;
        }

        public string processInput(PongInput input)
        {
           // processInputManual(input);

            bool switchTeams = false;

            if (input.VX < 0)
            {
                input.VX = -input.VX;
                CjUtils.swap(ref input.numRightTeam, ref input.numLeftTeam);
                CjUtils.swap(ref input.speedLeftTeam, ref input.speedRightTeam);
                switchTeams = true;
                input.X = input.widthField - input.X;
            }

            Line teamLeft = Line.createFromCoords(0, 0, 0, 1);
            Line teamRight = Line.createFromCoords(input.widthField, 0, input.widthField, 1);

            Line initialVector = Line.createFromCoords(input.X, input.Y, input.X + input.VX, input.Y + input.VY);

            var p1  = initialVector.intersection(teamRight);

            
            
            Line rebound = Line.createFromPoints(p1, p1.Add(new Point(-input.VX, input.VY)));

            var p2 = rebound.intersection(teamLeft);
            Logger.LogInfo("Initial point {}, {} direction {}, {}", input.X, input.Y, input.VX, input.VY);
            Logger.LogInfo("First intersection {} 2nd {}", p1, p2);

            NumType yDif = p2.Y.Subtract( p1.Y );

            if (yDif < 0)
            {
                yDif += input.heightField;
            }

#if FRAC
            BigFraction t0 = new BigFraction( input.widthField - input.X, input.VX );
            BigFraction t1 = new BigFraction(input.widthField, input.VX);
#else
            NumType t0 = (input.widthField - input.X) / (double) input.VX;
            NumType t1 = input.widthField / (double) input.VX;
#endif
            
            List<Tuple<NumType, NumType>>[] teamPlayerPos = new List<Tuple<NumType,NumType>>[2]; //time, loc pair
            teamPlayerPos[0] = new List<Tuple<NumType, NumType>>(); //left 
            teamPlayerPos[1] = new List<Tuple<NumType, NumType>>(); //right
            int[] teamSize = new int[] { input.numLeftTeam, input.numRightTeam};
            int[] curPlayer = new int[2] {0,0};
            long[] teamSpeed = new long[2] {input.speedLeftTeam, input.speedRightTeam};

            Logger.LogInfo("Width {} Height {1}", input.widthField, input.heightField);
            Logger.LogInfo("Left team {} players at {} speed", teamSize[0], teamSpeed[0]);
            Logger.LogInfo("Right team {} players at {} speed", teamSize[1], teamSpeed[1]);
            Logger.LogInfo("Time = {} + {} * n.  Position = {} + {} * n", t0, t1, p1.Y, yDif);

            long firstPointFail = long.MaxValue;

            for(int team = 0; team < 2; ++team)
            {
                NumType deltaT = 2 * teamSize[team] * t1;
                NumType disPossible = deltaT * teamSpeed[team];

                NumType diff = yDif.Multiply(2 * teamSize[team]);

                for(int playerNum = 0; playerNum < teamSize[team]; ++playerNum)
                {
                    //if (playerNum != 166 || team != 0)
                       // continue;

                    int firstPoint = 2 * playerNum + 1 - team;
                    NumType firstPointLoc = p1.Y + yDif * firstPoint;

                    Logger.LogTrace("\n\nCalculate player {} team {}:  firstPoint index {} location {}", playerNum, team, firstPoint, firstPointLoc);
                    long pointFail = calcToTarget(firstPointLoc, diff, input.heightField, disPossible);

                    //long pointFailCheck = calcToTargetManual(firstPointLoc, diff, input.heightField, disPossible);

                    Logger.LogTrace("\nPlayer {} team {} first point {} pointFail {}", playerNum, team, firstPoint, pointFail);

                    Logger.LogTrace("fails points {}", new int[]{1,2,3}.Select( (i) => firstPoint + i * 2 * teamSize[team]).ToCommaString());
                    if (pointFail != -1)
                    {
                        pointFail = firstPoint + pointFail * 2 * teamSize[team];
                        firstPointFail = Math.Min(firstPointFail, pointFail);

                        
                        NumType[] failValues;
                        NumType[] failDiffs;

                        calcPointsAndDiff(p1.Y, yDif, input.heightField, pointFail - 5, 10, out failValues, out failDiffs);
                        //calcPointsAndDiff(p1.Y, yDif, input.heightField, pointFail - 5, 10, out failValues, out failDiffs);

                        Logger.LogTrace("Point fail real {}  minimum overall point fail {}.  \nValues {}.  \nDiffs {}", pointFail.ToString("0,0"), firstPointFail.ToString("0,0"),
                            failValues.ToCommaString(), failDiffs.Skip(1).ToCommaString());

                    }

                    
                }
            }

            if (firstPointFail != long.MaxValue)
            {
                int team = firstPointFail % 2 == 0 ? 1 : 0;
                long num = (firstPointFail + team) / 2;
                string teamName = ((!switchTeams && team == 0) || (switchTeams && team == 1)) ? "RIGHT" : "LEFT";
                
                return teamName + " " + num;
            }
             

            return "DRAW";
        }

        public string processInputManual(PongInput input)
        {
            bool switchTeams = false;

            if (input.VX < 0)
            {
                input.VX = -input.VX;
                CjUtils.swap(ref input.numRightTeam, ref input.numLeftTeam);
                CjUtils.swap(ref input.speedLeftTeam, ref input.speedRightTeam);
                switchTeams = true;
                input.X = input.widthField - input.X;
            }

            Line teamLeft = Line.createFromCoords(0, 0, 0, 1);
            Line teamRight = Line.createFromCoords(input.widthField, 0, input.widthField, 1);

            Line initialVector = Line.createFromCoords(input.X, input.Y, input.X + input.VX, input.Y + input.VY);

            var p1 = initialVector.intersection(teamRight);



            Line rebound = Line.createFromPoints(p1, p1.Add(new Point(-input.VX, input.VY)));

            var p2 = rebound.intersection(teamLeft);
            Logger.Log("Initial point {0}, {1} direction {2}, {3}", input.X, input.Y, input.VX, input.VY);
            Logger.Log("P1 {0} P2 {1}", p1, p2);

            NumType yDif = p2.Y.Subtract(p1.Y);
#if FRAC
            BigFraction t0 = new BigFraction(input.widthField - input.X, input.VX);
            BigFraction t1 = new BigFraction(input.widthField, input.VX);
#else
            NumType t0 = (input.widthField - input.X) / (double) input.VX;
            NumType t1 = input.widthField / (double) input.VX;
#endif

            List<Tuple<NumType, NumType>>[] teamPlayerPos = new List<Tuple<NumType, NumType>>[2]; //time, loc pair
            teamPlayerPos[0] = new List<Tuple<NumType, NumType>>(); //left 
            teamPlayerPos[1] = new List<Tuple<NumType, NumType>>(); //right
            int[] teamSize = new int[] { input.numLeftTeam, input.numRightTeam };
            int[] curPlayer = new int[2] { 0, 0 };
            long[] teamSpeed = new long[2] { input.speedLeftTeam, input.speedRightTeam };

            Logger.LogInfo("Width {} Height {1}", input.widthField, input.heightField);
            Logger.LogInfo("Left team {0} players at {1} speed", teamSize[0], teamSpeed[0]);
            Logger.LogInfo("Right team {0} players at {1} speed", teamSize[1], teamSpeed[1]);
            Logger.LogInfo("Time = {} + {} * n.  Position = {2} + {3} * n", t0, t1, p1.Y, yDif);
            //HashSet<int>[] playerZeroPos = new HashSet<int>[] { new HashSet<int>(), new HashSet<int>() };

            Func<int, NumType> getYPosFromPointNum = (pNum) =>
            {
                NumType y = p1.Y.Add(yDif.Multiply(pNum));
                double x = pNum % 2 != 0 ? 0 : input.widthField;

                long rem = (long)(y.Divide(input.heightField));

                y = y.Subtract(rem * input.heightField);

                if (rem % 2 == 1)
                {
                    y = ((NumType)input.heightField).Subtract(y);
                }

                return y;
            };


            const int maxPnum = 400000;
            for (int pNum = 0; pNum < maxPnum; ++pNum)
            {
                int team = pNum % 2 == 0 ? 1 : 0;

                NumType y = getYPosFromPointNum(pNum);

                NumType time = t0.Add(t1.Multiply(pNum));
                
                if (curPlayer[team] == 166)
                Logger.LogTrace("Side {} Point {} is {}.  Time {}.  Current player {}", team, pNum, y, time, curPlayer[team]);

                if (teamPlayerPos[team].Count < teamSize[team])
                {
                    teamPlayerPos[team].Add(new Tuple<NumType, NumType>(time, y));
                }
                else
                {
                    //Check last position
                    var lastTimePos = teamPlayerPos[team][curPlayer[team]];
                    NumType deltaT = time.Subtract(lastTimePos.Item1);
                    NumType deltaDis = lastTimePos.Item2.Subtract(y).Abs();

                    NumType disPossible = deltaT.Multiply(teamSpeed[team]);
                    if (disPossible.CompareTo(deltaDis) < 0)
                    {
                        int num = (pNum + team) / 2;
                        string teamName = ((!switchTeams && team == 0) || (switchTeams && team == 1)) ? "RIGHT" : "LEFT";
                        Logger.LogDebug("Loser lastTime: {}   pos: {}.  Delta dis {}, could move {}.  player {} team {}",
                            lastTimePos.Item1, lastTimePos.Item2,
                            deltaDis, disPossible, curPlayer[team], team);
                        return teamName + " " + num;
                    }
                    else
                    {
                       /* if (curPlayer[team] < 10)
                            Logger.LogDebug("lastTime {0} sec  pos:{1}.  Delta dis {2}, could move {3} team {4} curPlayer {5} ", lastTimePos.Item1, lastTimePos.Item2,
                                deltaDis, disPossible, team, curPlayer[team]);*/
                    }

                    teamPlayerPos[team][curPlayer[team]] = new Tuple<NumType, NumType>(time, y);
                }

                if (curPlayer[team] == 0)
                {
                    //if (playerZeroPos[team].Contains(y))
                    {
                        //return "DRAW";
                    }
                }

                curPlayer[team]++;
                curPlayer[team] %= teamSize[team];
            }

            return "DRAW";
        }


        public PongInput createInput(Scanner scanner)
        {
            PongInput input = new PongInput();
            input.heightField = scanner.nextInt();
            input.widthField = scanner.nextInt();

            input.numLeftTeam = scanner.nextInt();
            input.numRightTeam = scanner.nextInt();

            input.speedLeftTeam = scanner.nextLong();
            input.speedRightTeam = scanner.nextLong();

            input.Y = scanner.nextInt();
            input.X = scanner.nextInt();

            input.VY = scanner.nextLong();
            input.VX = scanner.nextLong();

            return input;
        }
    }

    public class PongInput
    {
        internal int heightField { get; set; }
        internal int widthField { get; set; }

        internal int numLeftTeam; //{ get; set; }
        internal int numRightTeam; //{ get; set; }

        internal long speedLeftTeam; //{ get; set; }
        internal long speedRightTeam; // { get; set; }

        internal int Y { get; set; }
        internal int X { get; set; }
        internal long VY { get; set; }
        internal long VX { get; set; }
                 
        


    }
}
