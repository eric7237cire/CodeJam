#define LOGGING

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using C5;
using CodeJamUtils;
using System.Diagnostics;

using Utils;

using Logger = Utils.LoggerFile;

namespace Round2.TicketSwapping
{



    class TicketSwap : InputFileConsumer<Input, int>
    {


        



        static long calculateCost(IEnumerable<Pair> points, long Nl)
        {
            //i * N - (i * (i-1) ) / 2
            ModdedLong N = new ModdedLong(Nl);
            long sum = points.Sum(p =>
            {
                ModdedLong i = new ModdedLong(p.stop - p.start);
                ModdedLong nPass = new ModdedLong(p.nPassengers);
                long r = (nPass * (i * N - (i * (i - 1)) / 2)).Value;
                Preconditions.checkState(r >= 0);
                return r;
            });

            Preconditions.checkState(sum % ModdedLong.mod >= 0);
            return sum % ModdedLong.mod;
        }

        static long cost(int i_start, int i_stop, int nPass, int N)
        {
            ModdedLong i = (i_stop - i_start);            
            return (nPass * (i * N - (i * (i - 1)) / 2)).Value;            
        }



        private static void addPair(TreeSet<Pair> points, Pair newp)
        {
            Pair existPair = newp.searchCopy();
            bool found = points.Find(ref existPair);

            if (!found)
            {
                points.Add(newp);
            }
            else
            {
                existPair.nPassengers += newp.nPassengers;
            }
        }

        //Had to cheat, didn't realize that a stack was the key to the problem.  At least the mod arithmetic class is still handy
        public int processInput(Input input)
        {


            long beforeCost = calculateCost(input.points, input.nStops);

            List<Event> events = new List<Event>();

            foreach (Pair p in input.points)
            {
                events.Add(new Event { location = p.start, nPassengers = p.nPassengers });
                events.Add(new Event { location = p.stop, nPassengers = -p.nPassengers });
            }

            events.Sort();

            Stack<Event> boarding = new Stack<Event>();
            long afterCost = 0;

            for (int index = 0; index < events.Count; ++index)
            {
                Event ev = events[index];

                if (ev.nPassengers >= 0)
                {
                    boarding.Push(ev);
                    continue;
                }

                while(ev.nPassengers < 0)
                {
                    //Get passengers who boarded the earliest
                    Event recentlyBoarded = boarding.Peek();

                    int nPassLeaving = Math.Min(recentlyBoarded.nPassengers, -ev.nPassengers);
                    
                    //Add cost
                    long costItem = cost(recentlyBoarded.location, ev.location, nPassLeaving, input.nStops);
                    afterCost += costItem;
                    afterCost %= ModdedLong.mod;

                    recentlyBoarded.nPassengers -= nPassLeaving;
                    ev.nPassengers += nPassLeaving;
                    if (recentlyBoarded.nPassengers == 0)
                        boarding.Pop();

                    
                }
            }


            int ansRet = (int)new ModdedLong(beforeCost - afterCost + ModdedLong.mod).Value;
            Preconditions.checkState(ansRet >= 0);
            return ansRet;
        }

        public int processInputSmall(Input input)
        {
            TreeSet<Pair> points = new TreeSet<Pair>();

            long beforeCost = calculateCost(input.points, input.nStops);

            foreach (Pair p in input.points)
            {

                Pair newp = new Pair { start = p.start, stop = p.stop, nPassengers = p.nPassengers };
                addPair(points, newp);
            }



            int loopCheck = 0;
            while (true)
            {
                ++loopCheck;
                if (loopCheck % 500 == 0)
                    Logger.Log("Loopcheck {0}", loopCheck);

                if (loopCheck > 7200)
                {
                    return -1;
                }

                bool found = false;

                //Logger.Log("\n\nSTART\n\n");
                for (int i = 0; i < points.Count; ++i)
                {
                    //Logger.Log("Points {0}", i);
                    Pair p = points[i];

                    //Another point starting earlier that intersects p
                    //Pair lesser = points.Find((pt) =>
                    //{
                    //    return pt.start < p.start && isBetween(p.start, p.stop, pt.stop);
                    //});

                    IDirectedCollectionValue<Pair> dirCol = points.RangeFromTo(Pair.createWithStart(p.start + 1), Pair.createWithStart(p.stop + 1));

                    Pair greater = null;
                    foreach (Pair findPoint in dirCol)
                    {
                        if (findPoint.stop > p.stop &&
                            isBetween(p.start, p.stop, findPoint.start) &&
                            findPoint.start > p.start)
                        {
                            greater = findPoint;
                            break;
                        }
                    }
                    //Another point ending later that intersects p

                    if (greater == null)
                        continue;


                    int maxToSwitch = Math.Min(greater.nPassengers, p.nPassengers);

                    addPair(points, new Pair { start = p.start, stop = greater.stop, nPassengers = maxToSwitch });
                    addPair(points, new Pair { start = greater.start, stop = p.stop, nPassengers = maxToSwitch });

                    if (maxToSwitch == greater.nPassengers)
                    {
                        //points.RemoveAt(greaterIndex);
                        points.Remove(greater);
                    }
                    else
                    {
                        greater.nPassengers -= maxToSwitch;
                    }

                    if (maxToSwitch == p.nPassengers)
                    {
                        //points.RemoveAt(i);
                        points.Remove(p);
                    }
                    else
                    {
                        p.nPassengers -= maxToSwitch;
                    }

                    found = true;
                    break;

                }

                if (!found)
                {
                    break;
                }
            }

            long afterCost = calculateCost(points, input.nStops);


            return (int)new ModdedLong(beforeCost - afterCost + ModdedLong.mod).Value;
        }
        private static bool isBetween(int x, int y, int a)
        {
            return x <= a && a <= y;
        }
    }

    class Event : IComparable<Event>
    {
        internal int location { get; set; }
        internal int nPassengers { get; set; }

        public int CompareTo(Event other)
        {
            // Trace.WriteLine("Compare");
            int cmp = location.CompareTo(other.location);

            if (cmp != 0)
                return cmp;

            cmp = nPassengers.CompareTo(other.nPassengers);

            return -cmp;
        }

    }

    class Pair : IComparable<Pair>
    {
        internal int start;
        internal int stop { get; set; }
        internal int nPassengers { get; set; }

        public Pair searchCopy()
        {
            return new Pair { start = this.start, stop = this.stop, nPassengers = 0 };
        }

        public static Pair createWithStart(int start)
        {
            Pair p = new Pair();
            p.start = start;
            return p;
        }

        public int CompareTo(Pair other)
        {
            // Trace.WriteLine("Compare");
            int cmp = start.CompareTo(other.start);

            if (cmp != 0)
                return cmp;

            cmp = stop.CompareTo(other.stop);

            return cmp;
        }

        public override string ToString()
        {
            return "Start {0} Stop {1} nPass {2}".FormatThis(start, stop, nPassengers);
        }
    }

    public class Input
    {

        internal int nStops { get; private set; }
        internal int pointPairs { get; private set; }

        internal List<Pair> points { get; private set; }

        public static Input createInput(Scanner scanner)
        {
            Input input = new Input();
            input.nStops = scanner.nextInt();
            input.pointPairs = scanner.nextInt();

            input.points = new List<Pair>();

            for (int i = 0; i < input.pointPairs; ++i)
            {
                Pair td = new Pair
                {
                    start = scanner.nextInt(),
                    stop = scanner.nextInt(),
                    nPassengers = scanner.nextInt()
                };

                input.points.Add(td);

            }

            return input;
        }


    }
}
