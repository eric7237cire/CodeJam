

using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;
[assembly: System.Runtime.CompilerServices.InternalsVisibleTo
                          ("UnitTest")]
namespace Round1C.GreatWall
{
#if (PERF)
   
    using Logger = CodeJamUtils.LoggerEmpty;
#else
    using Logger = CodeJamUtils.LoggerReal;
#endif


    internal class GreatWall : InputFileConsumer<Input, int>
    {

        public int processInput(Input input)
        {
            solveSmall(input.tribes);
            return solveLarge(input.tribes);
        }

        static int solveLarge(List<TribeData> tribes)
        {
            Random r = new Random();
            Logger.Log("\nsolveLarge " + r.NextDouble());
            List<Attack> attacks = new List<Attack>();
            List<int> intervalEndPoints = new List<int>();
            IDictionary<int, int> endPointToIntervalIndex = new Dictionary<int, int>();

            foreach (TribeData tribe in tribes)
            {
                for (int n = 0; n < tribe.totalAttacks; ++n)
                {
                    int startX = tribe.w0 + tribe.deltaX * n;
                    int stopX = tribe.e0 + tribe.deltaX * n;

                    //Eliminate doubles
                    if (!endPointToIntervalIndex.ContainsKey(startX))
                    {
                        intervalEndPoints.Add(startX);
                        endPointToIntervalIndex[startX] = intervalEndPoints.Count;
                    }
                    if (!endPointToIntervalIndex.ContainsKey(stopX))
                    {
                        intervalEndPoints.Add(stopX);
                        endPointToIntervalIndex[stopX] = intervalEndPoints.Count;
                    }
                    int strength = tribe.s0 + tribe.deltaS * n;
                    int ptime = tribe.t0 + tribe.deltaT * n;
                    attacks.Add(new Attack { time = ptime, start = startX, stop = stopX, height = strength });
                }
            }

            attacks.Sort((x, y) => x.time.CompareTo(y.time) );
            intervalEndPoints.Sort();

            for (int i = 0; i < intervalEndPoints.Count; ++i)
            {
                endPointToIntervalIndex[intervalEndPoints[i]] = i;
            }

            Wall wall = new Wall(-200, 200);

            int nextUpdateStartIdx = 0;
            int successfulAttackCount = 0;

            for (int attackIdx = 0; attackIdx < attacks.Count; ++attackIdx)
            {
                Attack attack = attacks[attackIdx];
                Logger.Log("Attack on day {0} between {1} and {2} with strength {3}", attack.time, attack.start, attack.stop, attack.height);
                if (wall.attackPasses(attack.start, attack.stop, attack.height))
                {
                    successfulAttackCount++;
                    Logger.Log("Wall was NOT high enough");
                }
                else
                {
                    Logger.Log("Wall was high enough");
                }
                

                if (attackIdx == attacks.Count - 1 || attacks[attackIdx + 1].time > attack.time)
                {
                    Logger.Log("Apply attacks");
                    //apply all attacks
                    for (int upIdx = nextUpdateStartIdx; upIdx <= attackIdx; ++upIdx)
                    {
                        wall.setToAtLeast(attacks[upIdx].start, attacks[upIdx].stop, attacks[upIdx].height);
                    }

                    nextUpdateStartIdx = attackIdx + 1;
                }
            }

            return successfulAttackCount;
        }

        static int solveSmall(List<TribeData> tribes)
        {
            Wall wall = new Wall(-200, 200);

            Logger.Log("\nsolveSmall\n");
            int attacks = 0;

            for (int day = 0; day < 700000; ++day)
            {
                List<Tuple<int, int, int>> wallUpdates = new List<Tuple<int, int, int>>();

                int tn = -1;
                foreach (TribeData tribe in tribes)
                {
                    int n;
                    ++tn;
                    if (!tribe.attacksOn(day, out n))
                        continue;


                    int startX = tribe.w0 + tribe.deltaX * n;
                    int stopX = tribe.e0 + tribe.deltaX * n;
                    int strength = tribe.s0 + tribe.deltaS * n;

                    Logger.Log("Tribe {4} attacks on day {0} between {1} and {2} with strength {3}", day, startX, stopX, strength, tn);

                    if (wall.attackPasses(startX, stopX, strength))
                    {
                        attacks++;
                        wallUpdates.Add(new Tuple<int, int, int>(startX, stopX, strength));
                        Logger.Log("Wall was NOT high enough");
                    }
                    else
                    {
                        Logger.Log("Wall was high enough");
                    }



                }

                foreach (var wallUpdate in wallUpdates)
                {
                    wall.setToAtLeast(wallUpdate.Item1, wallUpdate.Item2, wallUpdate.Item3);
                }
            }

            return attacks;
        }



        static void Main(string[] args)
        {
            GreatWall main = new GreatWall();

            List<string> list = new List<string>();

            //list.Add("sample");
            list.Add("C_small_practice");
            //list.Add("B_large_practice");

            CjUtils.RunMain(list, main, Input.createInput, Round1C.Properties.Resources.ResourceManager);

        }
    }

    class Wall
    {
        public int Begin { get; private set; }
        public int End { get; private set; }

        private int offset;

        private int[] data;

        private int this[int i]
        {
            get { return data[i + offset]; }
            set { data[i + offset] = value; }
        }

        internal Wall(int begin, int end)
        {
            offset = -2 * begin;
            data = new int[2 * (end - begin + 1)];
        }

        internal bool attackPasses(int startX, int stopX, int strength)
        {
            for (int x = 2 * startX; x <= 2 * stopX; ++x)
            {
                if (this[x] < strength)
                {
                    return true;
                }
            }

            return false;
        }

        internal void setToAtLeast(int startX, int stopX, int strength)
        {
            for (int x = 2 * startX; x <= 2 * stopX; ++x)
            {
                if (this[x] < strength)
                {
                    this[x] = strength;
                }
            }
        }
    }

    class Attack
    {
        internal int time;
        internal int start;
        internal int stop;
        internal int height;
    }

    #region TribeData
    internal class TribeData
    {
        public int t0 { get; internal set; }
        public int totalAttacks { get; internal set; }
        public int w0 { get; internal set; }
        public int e0 { get; internal set; }
        public int s0 { get; internal set; }

        public int deltaT { get; internal set; }
        public int deltaX { get; internal set; }
        public int deltaS { get; internal set; }

        public Boolean attacksOn(int day, out int n)
        {
            //t0 + n * deltaT = day
            int rhs = day - t0;
            if (rhs % deltaT != 0)
            {
                n = -1;
                return false;
            }

            n = rhs / deltaT;

            if (n >= totalAttacks || n < 0)
            {
                return false;
            }
            return true;
        }
    }
    #endregion

    /// <summary>
    /// di – the day of the tribe's first attack (where 1st January, 250BC, is considered day 0)
    /// ni – the number of attacks from this tribe
    /// wi, ei – the westmost and eastmost points respectively of the Wall attacked on the first attack
    /// si – the strength of the first attack
    /// delta_di – the number of days between subsequent attacks by this tribe
    /// delta_pi – the distance this tribe travels to the east between subsequent attacks (if this is negative, the tribe travels to the west)
    /// delta_si – the change in strength between subsequent attacks*/
    /// </summary>
    public class Input
    {

        internal int nTribes { get; private set; }
        internal List<TribeData> tribes { get; private set; }

        public static Input createInput(Scanner scanner)
        {
            Input input = new Input();
            input.nTribes = scanner.nextInt();
            input.tribes = new List<TribeData>();

            for (int i = 0; i < input.nTribes; ++i)
            {
                TribeData td = new TribeData
                {
                    t0 = scanner.nextInt(),
                    totalAttacks = scanner.nextInt(),
                    w0 = scanner.nextInt(),
                    e0 = scanner.nextInt(),
                    s0 = scanner.nextInt(),
                    deltaT = scanner.nextInt(),
                    deltaX = scanner.nextInt(),
                    deltaS = scanner.nextInt()
                };

                input.tribes.Add(td);

            }

            return input;
        }


    }

}


