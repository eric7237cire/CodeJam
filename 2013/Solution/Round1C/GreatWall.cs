
#define PERF
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
           // solveSmall(input.tribes);
            return solveLarge(input.tribes);
           // return solveUsingHighLow(input.tribes);
        }

        
        struct HighLow
        {
            //Meaning height was explicity set (via an attack)
            internal int explicitHeight;

            //Current minimum contained height
            internal int minimumLocalHeight; 

            public override string ToString()
{
    return " explicity height: " + explicitHeight + ", minimum local: " + minimumLocalHeight;
}
        }

        static int solveUsingHighLow(List<TribeData> tribes)
        {
            Random r = new Random();
            Logger.Log("\nsolveHighLow " + r.NextDouble());
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

            attacks.Sort((x, y) => x.time.CompareTo(y.time));
            intervalEndPoints.Sort();

            for (int i = 0; i < intervalEndPoints.Count; ++i)
            {
                endPointToIntervalIndex[intervalEndPoints[i]] = i;
            }

            BinaryTree<HighLow> bt = BinaryTree<HighLow>.create(intervalEndPoints.Count);
            bt.ApplyLeftRightDataFunc = (HighLow lhs, HighLow rhs, ref HighLow cur) =>
            {
                int lowestLocalMinimumOfChildren = Math.Min(lhs.minimumLocalHeight, rhs.minimumLocalHeight);
                cur.minimumLocalHeight = Math.Max(lowestLocalMinimumOfChildren, cur.explicitHeight);
            };

            int minToSet = 0;
            BinaryTree<HighLow>.ProcessDelegate setMinimum = (ref HighLow data, Stack<HighLow> parents) =>
            {
                Logger.Log("SetMinimum data {0}.  minToSet {1}", data, minToSet);
                data.explicitHeight = Math.Max(minToSet, data.explicitHeight);
                data.minimumLocalHeight = Math.Max(data.minimumLocalHeight, minToSet);
                Logger.Log("SetMinimum data is now {0}.  ", data);
            };

            int lowestHeight = Int32.MaxValue;
            BinaryTree<HighLow>.ProcessDelegate isAnyLower = (ref HighLow data, Stack<HighLow> parents) =>
            {
                

                int intervalLowestHeight = parents.Max(x => x.explicitHeight);

                intervalLowestHeight = Math.Max(data.minimumLocalHeight, intervalLowestHeight);

                Logger.Log("Find height.  data {0}.  lowest height for this interval: {1} lowestHeight so far {2}", data, intervalLowestHeight, lowestHeight);
                lowestHeight = Math.Min(lowestHeight, intervalLowestHeight);

            };



            int nextUpdateStartIdx = 0;
            int successfulAttackCount = 0;

            for (int attackIdx = 0; attackIdx < attacks.Count; ++attackIdx)
            {
                Attack attack = attacks[attackIdx];
                Logger.Log("Attack {6} on day {0} between {1}:{2} idx {3}:{4} with strength {5}",
                    attack.time,
                    attack.start, attack.stop,
                    endPointToIntervalIndex[attack.start], endPointToIntervalIndex[attack.stop],
                    attack.height, attackIdx);

                lowestHeight = Int32.MaxValue;
                
                bt.traverse(endPointToIntervalIndex[attack.start], endPointToIntervalIndex[attack.stop], isAnyLower);

                if (lowestHeight < attack.height)
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
                        int updateStart = attacks[upIdx].start;
                        int updateStartIdx = endPointToIntervalIndex[updateStart];
                        int updateStop = attacks[upIdx].stop;
                        int updateStopIdx = endPointToIntervalIndex[updateStop];
                        Logger.Log("Update wall attack {0}.  range: {1}:{2} rangeIdx: {3}:{4} height {5}",
                            upIdx, updateStart, updateStop, updateStartIdx, updateStopIdx, attacks[upIdx].height);

                        minToSet = attacks[upIdx].height;
                        bt.traverse(endPointToIntervalIndex[attacks[upIdx].start], endPointToIntervalIndex[attacks[upIdx].stop], setMinimum);

                    }

                    nextUpdateStartIdx = attackIdx + 1;
                }
            }

            return successfulAttackCount;
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

            attacks.Sort((x, y) => x.time.CompareTo(y.time));
            intervalEndPoints.Sort();

            for (int i = 0; i < intervalEndPoints.Count; ++i)
            {
                endPointToIntervalIndex[intervalEndPoints[i]] = i;
            }
            
            BinaryTree<int> bt = BinaryTree<int>.create(intervalEndPoints.Count);
            bt.ApplyParentDataFunc = (int parentData, ref int childData) =>
            {
                childData = Math.Max(childData, parentData);
            };
            bt.ApplyLeftRightDataFunc = (int lhs, int rhs, ref int cur) =>
            {
                cur = Math.Max(cur, Math.Min(lhs, rhs));
            };

            int minToSet = 0;
            BinaryTree<int>.ProcessDelegate setMinimum = (ref int data, Stack<int> isParent) =>
            {
                
                //Logger.Log("SetMinimum {0} to {1} data {2}.  minToSet {3}", startEndpointIndex, stopEndPointIndex, data, minToSet);
                data = Math.Max(minToSet, data);
            };

            Boolean anyLower = false;
            int toFind = 3;
            BinaryTree<int>.ProcessDelegate isAnyLower = (ref int data, Stack<int> isParent) =>
            {
               
                //Logger.Log("Find Lower {0} to {1} data {2}.  query {3}", startEndpointIndex, stopEndPointIndex, data, toFind);
                if (data < toFind)
                {
                    anyLower = true;
                }

            };



            int nextUpdateStartIdx = 0;
            int successfulAttackCount = 0;

            for (int attackIdx = 0; attackIdx < attacks.Count; ++attackIdx)
            {
                Attack attack = attacks[attackIdx];
                Logger.Log("Attack {6} on day {0} between {1}:{2} idx {3}:{4} with strength {5}",
                    attack.time,
                    attack.start, attack.stop,
                    endPointToIntervalIndex[attack.start], endPointToIntervalIndex[attack.stop],
                    attack.height, attackIdx);

                anyLower = false; toFind = attack.height;
                bt.traverse(endPointToIntervalIndex[attack.start], endPointToIntervalIndex[attack.stop], isAnyLower);

                if (anyLower)
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
                        int updateStart = attacks[upIdx].start;
                        int updateStartIdx = endPointToIntervalIndex[updateStart];
                        int updateStop = attacks[upIdx].stop;
                        int updateStopIdx = endPointToIntervalIndex[updateStop];
                        Logger.Log("Update wall attack {0}.  range: {1}:{2} rangeIdx: {3}:{4} height {5}",
                            upIdx, updateStart, updateStop, updateStartIdx, updateStopIdx, attacks[upIdx].height);
                        
                        minToSet = attacks[upIdx].height;
                        bt.traverse(endPointToIntervalIndex[attacks[upIdx].start], endPointToIntervalIndex[attacks[upIdx].stop], setMinimum);

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

            //Consonants.Main2(args);
            //Round1C.Pogo.Pogo.Main2(args);
            //if (args != null) return;
            GreatWall main = new GreatWall();

            List<string> list = new List<string>();

           // list.Add("sample");
            list.Add("C_small_practice");
            list.Add("C_large_practice");

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


