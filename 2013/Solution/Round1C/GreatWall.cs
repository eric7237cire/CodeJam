using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;
using DataStructures;
using Logger = Utils.LoggerFile;

[assembly: System.Runtime.CompilerServices.InternalsVisibleTo
                          ("UnitTest")]
namespace Round1C
{
	
	public struct HighLow
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

	public class Updater : BinaryTreeUpdater<HighLow>
	{
		int minToSet;
		
		public Updater(int val)
		{
			minToSet = val;
		}
		
		//Once update is done
		public void ApplyLeftRightData(HighLow lhs, HighLow rhs, ref HighLow cur)
		{
			int lowestLocalMinimumOfChildren = Math.Min(lhs.minimumLocalHeight, rhs.minimumLocalHeight);
                
			cur.minimumLocalHeight = Math.Max(lowestLocalMinimumOfChildren, cur.explicitHeight);
		}
		
		//Called for each matching interval
		public void Update(ref HighLow data, int nodeIndex)
		{
			 Logger.LogTrace("SetMinimum data {0}.  minToSet {1}", data, minToSet);
            data.explicitHeight = Math.Max(minToSet, data.explicitHeight);
            data.minimumLocalHeight = Math.Max(data.minimumLocalHeight, minToSet);
            Logger.LogTrace("SetMinimum data is now {0}.  ", data);
		}
		
	}
	
	public sealed class ExistLowerThan : BinaryTreeQuery<HighLow>
	{
		public int lowestHeightFound;
		
		public int searchForLowerThan;
		
		BinaryTree<HighLow> bt;
		
		public ExistLowerThan(int sflt, BinaryTree<HighLow> b)
		{
			searchForLowerThan = sflt;
			bt = b;
			lowestHeightFound = int.MaxValue;
		}
		
		public void Query(HighLow data, int nodeIndex)
		{
			//Logger.LogTrace("Find height.  data {0}.  lowest height for this interval: {1} lowestHeight so far {2}", data, intervalLowestHeight, lowestHeightFound);
			lowestHeightFound = Math.Min(lowestHeightFound, data.minimumLocalHeight);
		}
		
		public void PassingParent(HighLow data, int nodeIndex, ref bool stop)
		{
			if (data.explicitHeight >= searchForLowerThan)
			{
				stop = true;
			}	
		}
		
	}

    public class GreatWall : InputFileConsumer<GreatWallInput, int>
    {

        public int processInput(GreatWallInput input)
        {
            return solveUsingHighLow(input.tribes);
        }

        public int processInputSmall(GreatWallInput input)
        {
            return solveSmall(input.tribes);
        }
        
        

        

        static int solveUsingHighLow(List<TribeData> tribes)
        {
            Random r = new Random();
            Logger.LogInfo("\nsolveHighLow " + r.NextDouble());
            List<Attack> attacks = new List<Attack>();
            
            //Used to normalize the points in order
            List<int> intervalEndPoints = new List<int>();
            
            //Used to find the index it intervalEndPoints given the original coordinate
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
                        //Added to check for doubles
                        endPointToIntervalIndex[startX] = intervalEndPoints.Count;
                    }
                    if (!endPointToIntervalIndex.ContainsKey(stopX))
                    {
                        intervalEndPoints.Add(stopX);
                        //Added to check for doubles
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
            	//The real order
                endPointToIntervalIndex[intervalEndPoints[i]] = i;
            }

            BinaryTree<HighLow> bt = BinaryTree<HighLow>.create(intervalEndPoints.Count+1);

            int nextUpdateStartIdx = 0;
            int successfulAttackCount = 0;

            for (int attackIdx = 0; attackIdx < attacks.Count; ++attackIdx)
            {
                Attack attack = attacks[attackIdx];
                Logger.LogTrace("Attack {6} on day {0} between {1}:{2} idx {3}:{4} with strength {5}",
                    attack.time,
                    attack.start, attack.stop,
                    endPointToIntervalIndex[attack.start], endPointToIntervalIndex[attack.stop],
                    attack.height, attackIdx);

                ExistLowerThan query = new ExistLowerThan(attack.height, bt); 
                bt.traverse(endPointToIntervalIndex[attack.start]+1, endPointToIntervalIndex[attack.stop], query);

                if (query.lowestHeightFound < attack.height)
                {
                    successfulAttackCount++;
                    Logger.LogTrace("Wall was NOT high enough");
                }
                else
                {
                    Logger.LogTrace("Wall was high enough");
                }


                if (attackIdx == attacks.Count - 1 || attacks[attackIdx + 1].time > attack.time)
                {
                    Logger.LogTrace("Apply attacks");
                    //apply all attacks
                    for (int upIdx = nextUpdateStartIdx; upIdx <= attackIdx; ++upIdx)
                    {
                        int updateStart = attacks[upIdx].start;
                        int updateStartIdx = endPointToIntervalIndex[updateStart];
                        int updateStop = attacks[upIdx].stop;
                        int updateStopIdx = endPointToIntervalIndex[updateStop];
                        Logger.LogTrace("Update wall attack {0}.  range: {1}:{2} rangeIdx: {3}:{4} height {5}",
                            upIdx, updateStart, updateStop, updateStartIdx, updateStopIdx, attacks[upIdx].height);

                        bt.traverse(endPointToIntervalIndex[attacks[upIdx].start]+1, 
                        	endPointToIntervalIndex[attacks[upIdx].stop],
                        	new Updater(attacks[upIdx].height) );
                            

                    }

                    nextUpdateStartIdx = attackIdx + 1;
                }
            }

            return successfulAttackCount;
        }

        

        static int solveSmall(List<TribeData> tribes)
        {
            Wall wall = new Wall(-200, 200);

            Logger.LogTrace("\nsolveSmall\n");
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

                    Logger.LogTrace("Tribe {4} attacks on day {0} between {1} and {2} with strength {3}", day, startX, stopX, strength, tn);

                    if (wall.attackPasses(startX, stopX, strength))
                    {
                        attacks++;
                        wallUpdates.Add(new Tuple<int, int, int>(startX, stopX, strength));
                        Logger.LogTrace("Wall was NOT high enough");
                    }
                    else
                    {
                        Logger.LogTrace("Wall was high enough");
                    }



                }

                foreach (var wallUpdate in wallUpdates)
                {
                    wall.setToAtLeast(wallUpdate.Item1, wallUpdate.Item2, wallUpdate.Item3);
                }
            }

            return attacks;
        }

        public static GreatWallInput createInput(Scanner scanner)
        {
            GreatWallInput input = new GreatWallInput();
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
    public class GreatWallInput
    {

        internal int nTribes { get; set; }
        internal List<TribeData> tribes { get; set; }

        


    }

}


