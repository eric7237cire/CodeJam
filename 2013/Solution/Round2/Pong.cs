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
        public string processInput(PongInput input)
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
            for(int team = 0; team < 1; ++team)
            {
                for(int playerNum = 0; playerNum < teamSize[team]; ++playerNum)
                {
                    //left team -- pointNum 1 3 5 7 9

                    int firstPoint = 2 * playerNum + 1 - team;

                    for(int i = 0; i < 5; ++i)
                    {
                        int pointNum = firstPoint + i * 2 * teamSize[team];

                        NumType y = getYPosFromPointNum(pointNum);

                        NumType time = t0.Add(t1.Multiply(pointNum));
                        Logger.LogTrace("player loop Side {} Point {} is {}.  Time {}.  Current player {}",
                            team, pointNum, y, time, playerNum);

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
                if (curPlayer[team] < 10)
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
                        Logger.LogDebug("Loser lastTime {0} sec  pos:{1}.  Delta dis {2}, could move {3}", lastTimePos.Item1, lastTimePos.Item2,
                            deltaDis, disPossible);
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
