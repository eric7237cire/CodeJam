#define LOGGING

using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils.geom;
using Utils;

using Logger = Utils.LoggerFile;

namespace Round2.Pong
{
    class PongMain : InputFileConsumer<PongInput, string>, InputFileProducer<PongInput>
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

            
            
            Line rebound = Line.createFromPoints(p1, p1.Add(new Point<double>(-input.VX, input.VY)));

            var p2 = rebound.intersection(teamLeft);
            Logger.Log("Initial point {0}, {1} direction {2}, {3}", input.X, input.Y, input.VX, input.VY);
            Logger.Log("P1 {0} P2 {1}", p1, p2);

            double yDif = p2.Y - p1.Y;

            double t0 = (input.widthField - input.X) / (double)input.VX;
            double t1 = input.widthField / (double)input.VX;

            List<Tuple<double, double>>[] teamPlayerPos = new List<Tuple<double,double>>[2]; //time, loc pair
            teamPlayerPos[0] = new List<Tuple<double, double>>(); //left 
            teamPlayerPos[1] = new List<Tuple<double, double>>(); //right
            int[] teamSize = new int[] { input.numLeftTeam, input.numRightTeam};
            int[] curPlayer = new int[2] {0,0};
            int[] teamSpeed = new int[2] {input.speedLeftTeam, input.speedRightTeam};

            Logger.Log("Left team {0} players at {1} speed", teamSize[0], teamSpeed[0]);
            Logger.Log("Right team {0} players at {1} speed", teamSize[1], teamSpeed[1]);

            for (int pNum = 0; pNum < 25; ++pNum )
            {
                int team = pNum % 2 == 0 ? 1 : 0;

                double y = p1.Y + pNum * yDif;
                double x = pNum % 2 == 0 ? 0 : input.widthField;

                int rem = (int) (y / input.heightField);
                while (y > input.heightField) {
                    y -= input.heightField;
                }
                if (rem % 2 == 1)
                {
                    y = input.heightField - y;
                }

                double time = t0 + t1 * pNum;
                Logger.Log("Point {0} is {1}, {2}.  Time {3}.  Current player {4}", pNum, x, y, time, curPlayer[team]);

                if (teamPlayerPos[team].Count < teamSize[team])
                {
                    teamPlayerPos[team].Add(new Tuple<double, double>(time, y));
                }
                else
                {
                    //Check last position
                    var lastTimePos = teamPlayerPos[team][curPlayer[team]];
                    double deltaT = time - lastTimePos.Item1;
                    double deltaDis = Math.Abs(lastTimePos.Item2 - y);

                    double disPossible = deltaT * teamSpeed[team];
                    if (disPossible < deltaDis)
                    {
                        int num = (pNum + team) / 2;
                        string teamName = ((!switchTeams && team == 0) || (switchTeams && team == 1)) ? "RIGHT" : "LEFT";
                        Logger.Log("Loser lastTime {0} sec  pos:{1}.  Delta dis {2}, could move {3}", lastTimePos.Item1, lastTimePos.Item2,
                            deltaDis, disPossible);
                        return teamName + " " + num;
                    }

                    teamPlayerPos[team][curPlayer[team]] = new Tuple<double, double>(time, y);
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

            input.speedLeftTeam = scanner.nextInt();
            input.speedRightTeam = scanner.nextInt();

            input.Y = scanner.nextInt();
            input.X = scanner.nextInt();

            input.VY = scanner.nextInt();
            input.VX = scanner.nextInt();

            return input;
        }
    }

    public class PongInput
    {
        internal int heightField { get; set; }
        internal int widthField { get; set; }

        internal int numLeftTeam; //{ get; set; }
        internal int numRightTeam; //{ get; set; }

        internal int speedLeftTeam; //{ get; set; }
        internal int speedRightTeam; // { get; set; }

        internal int Y { get; set; }
        internal int X { get; set; }
        internal int VY { get; set; }
        internal int VX { get; set; }
                 
        


    }
}
