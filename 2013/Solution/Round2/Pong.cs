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
#else
    using Line = LineNumeric<DoubleNumeric>;
    using Point = Point<DoubleNumeric>;
    using NumType = DoubleNumeric;
#endif

    
    public class PongMain : InputFileConsumer<PongInput, string>, InputFileProducer<PongInput>
    {
        public static NumType calcAdd(NumType p0, NumType deltaP, long n, NumType height)
        {
            
            NumType pos = p0 + n * deltaP;
                
            long rem = (long)(pos / height);

            pos -= (rem * height);

            if (rem % 2 == 1)
            {
                pos = height - pos;
            }

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

            long fullLengths = (long) (height / deltaP);

            NumType excess = (1+fullLengths) * deltaP - height;

            Logger.LogDebug("Excess {}.  Pattern is +{} or -{}", excess, 2*excess, 2*excess);
        }

        public static long calcToTarget(NumType p0, NumType deltaP, NumType height, NumType targetDiff, bool recurs = false)
        {
            long rem = (long)(deltaP / (2 * height));
            deltaP -= (rem * 2 * height);

            if (deltaP > height)
            {
                deltaP = 2 * height - deltaP ;
            }

            Logger.LogDebug("Adjusted \u0394P {}", deltaP);

            if (targetDiff >= deltaP)
                return -1;

            //Calculate p1 and p2
            NumType p1 = calcAdd(p0, deltaP, 1, height);
            NumType p2 = calcAdd(p0, deltaP, 2, height);

            NumType d1 = (p1 - p0).Abs();
            NumType d2 = (p2 - p1).Abs();

            if (d1 > targetDiff)
                return 1;

            if (d2 > targetDiff)
                return 2;

            NumType deltaD = (d2 - d1).Abs();
                
            //If descending, go to rebound
            if (d2 < d1)
            {
                Preconditions.checkState(!recurs);

                long pointsToZero = (long) (d2 / deltaD);

                NumType pa = calcAdd(p0, deltaP, 1+pointsToZero, height);
                NumType pb = calcAdd(p0, deltaP, 2+pointsToZero, height);
                NumType pc = calcAdd(p0, deltaP, 3 + pointsToZero, height);
                NumType pd = calcAdd(p0, deltaP, 4 + pointsToZero, height);
                Logger.LogDebug("P0 {} P1 {} P2 {}.  D1 {} D2 {}", p0, p1, p2, d1, d2);
                Logger.LogDebug("Points to zero {}.  pa,pb,pc {} {} {} {}", pointsToZero, pa, pb, pc,pd);

                NumType db = (pb - pa).Abs();
                NumType dc = (pc - pb).Abs();
                NumType dd = (pd - pc).Abs();

                Preconditions.checkState(db - deltaD <= 0); 
                Preconditions.checkState(dc - deltaD <= 0);
                Preconditions.checkState((dd-dc).Equals(deltaD)); //now going positive

                long pointsLeft = calcToTarget(pc, deltaP, height, targetDiff, true);
                return pointsLeft + 3 + pointsToZero;
            }

            long pointsToGo =  2l + (long) ((targetDiff - d2) / deltaD);

            NumType pw = calcAdd(p0, deltaP, 1 + pointsToGo, height);
            NumType px = calcAdd(p0, deltaP, 2 + pointsToGo, height);
            NumType py = calcAdd(p0, deltaP, 3 + pointsToGo, height);
            NumType pz = calcAdd(p0, deltaP, 4 + pointsToGo, height);

            NumType dx = (px - pw).Abs();
            NumType dy = (py - px).Abs();
            NumType dz = (pz - py).Abs();

            Logger.LogDebug("PointsToGo {} Pw {} {} {} {}.  diffs {} {} {}", pointsToGo, pw, px, py, pz, dx, dy, dz);
            return pointsToGo  + 2;
        }

        public string processInput(PongInput input)
        {
            return processInputManual(input);

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
            Logger.Log("Initial point {0}, {1} direction {2}, {3}", input.X, input.Y, input.VX, input.VY);
            Logger.Log("P1 {0} P2 {1}", p1, p2);

            NumType yDif = p2.Y.Subtract( p1.Y );
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

            //Try left team
            for(int team = 0; team < 2; ++team)
            {
                NumType deltaT = 2 * teamSize[team] * t1;
                NumType disPossible = deltaT * teamSpeed[team];

                NumType diff = yDif.Multiply(2 * teamSize[team]);

                for(int playerNum = 0; playerNum < teamSize[team]; ++playerNum)
                {
                    if (playerNum != 0)
                        continue;
                    //left team -- pointNum 1 3 5 7 9

                    int firstPoint = 2 * playerNum + 1 - team;
                    Logger.LogTrace("\nPlayer {} ", playerNum);

                    for(int i = 1; i < 45; ++i)
                    {
                        int lastPointNum = firstPoint + (i - 1) * 2 * teamSize[team];
                        int pointNum = firstPoint + i * 2 * teamSize[team];

                        NumType y = getYPosFromPointNum(pointNum);
                        NumType lastY = getYPosFromPointNum(lastPointNum);
                        
                        Logger.LogTrace("player loop.  Point #{} = [{}].  Current player {} Side {}",
                            pointNum, y, playerNum, team);

                        NumType deltaDis = lastY.Subtract(y).Abs();

                        Logger.LogTrace("Delta time {} diff {} delta dis {} possible {}\n", 
                            deltaT, diff, deltaDis, disPossible);
                    }

                    //break;
                }
            }

            const int maxPnum = 400000;
            for (int pNum = 0; pNum < maxPnum; ++pNum )
            {
                int team = pNum % 2 == 0 ? 1 : 0;

                NumType y = getYPosFromPointNum(pNum);

                NumType time = t0 .Add( t1.Multiply(pNum) );
                //if (curPlayer[team] < 10)
                Logger.LogTrace("Side {} Point {} is {}.  Time {}.  Current player {}", team, pNum, y, time, curPlayer[team]);

                if (teamPlayerPos[team].Count < teamSize[team])
                {
                    teamPlayerPos[team].Add(new Tuple<NumType, NumType>(time, y));
                }
                else
                {
                    //Check last position
                    var lastTimePos = teamPlayerPos[team][curPlayer[team]];
                    NumType deltaT = time.Subtract( lastTimePos.Item1 );
                    NumType deltaDis = lastTimePos.Item2.Subtract(y).Abs();

                    NumType disPossible = deltaT.Multiply( teamSpeed[team] );
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
                        if (curPlayer[team] < 10)
                        Logger.LogDebug("lastTime {0} sec  pos:{1}.  Delta dis {2}, could move {3} team {4} curPlayer {5} ", lastTimePos.Item1, lastTimePos.Item2,
                            deltaDis, disPossible, team, curPlayer[team]);
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

                curPlayer[team] ++;
                curPlayer[team] %= teamSize[team];
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
                //if (curPlayer[team] < 10)
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
                        if (curPlayer[team] < 10)
                            Logger.LogDebug("lastTime {0} sec  pos:{1}.  Delta dis {2}, could move {3} team {4} curPlayer {5} ", lastTimePos.Item1, lastTimePos.Item2,
                                deltaDis, disPossible, team, curPlayer[team]);
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
