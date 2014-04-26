#define LOGGING
#define TRACE

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
    

    class MainC : InputFileConsumer<Input, int>
    {
        

        static void Main(string[] args)
        {
            Logger.Log("test");

            MainC main = new MainC();

            List<string> list = new List<string>();

            list.Add("sample.in");
            list.Add("A-small-practice.in");
            //list.Add("C_large_practice");

            CjUtils.RunMain(list, main, Input.createInput, null); //Round1C.Properties.Resources.ResourceManager
        }

        static int calculateCost(IEnumerable<Pair> points, int N)
        {
            //i * N - (i * (i-1) ) / 2
            
            return points.Sum( p => {
                checked
                {
                    int i = p.stop - p.start;
                    int r = p.nPassengers * (i * N - (i * (i - 1)) / 2);
                    return r;
                }
            });
        }


        public int processInput(Input input)
        {
            TreeSet<Pair> points = new TreeSet<Pair>();

            int beforeCost = calculateCost(input.points, input.nStops);

            foreach(Pair p in input.points)
            {
                
                Pair newp = new Pair { start = p.start, stop = p.stop, nPassengers = p.nPassengers};
                bool added = points.Add(newp);
                Preconditions.checkState(added);
            }

            

            int loopCheck = 0;
            while(true)
            {
                ++loopCheck;
                if (loopCheck > 1200)
                {
                    return -1;
                }

                bool found = false;

                //Logger.Log("\n\nSTART\n\n");
                for(int i=0; i < points.Count; ++i)
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
                    foreach(Pair findPoint in dirCol)
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

                    points.Add( new Pair { start = p.start, stop = greater.stop, nPassengers = maxToSwitch});
                    points.Add( new Pair { start = greater.start, stop = p.stop, nPassengers = maxToSwitch});

                    if (maxToSwitch == greater.nPassengers) {
                        //points.RemoveAt(greaterIndex);
                        points.Remove(greater);
                    } else {
                        greater.nPassengers -= maxToSwitch;
                    }

                    if (maxToSwitch == p.nPassengers) {
                        //points.RemoveAt(i);
                        points.Remove(p);
                    } else {
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

            int afterCost = calculateCost(points,input.nStops);

            return beforeCost -afterCost;
        }
        private static bool isBetween(int x, int y, int a)
        {
            return x <= a && a <= y;
        }
    }



    class Pair : IComparable<Pair>
    {
        internal int start;
        internal int stop { get;  set; }
        internal int nPassengers { get;  set; }

        public static Pair createWithStart(int start)
        {
            Pair p = new Pair();
            p.start = start;
            return p;
        }

        public int CompareTo(Pair other)
        {
            Trace.WriteLine("Compare");
           int cmp = start.CompareTo(other.start);

           if (cmp != 0)
               return cmp;

           cmp = stop.CompareTo(other.stop);

           return cmp;
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
