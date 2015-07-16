#define LOGGING_DEBUG
#define LOGGING_INFO
using CodeJam._2014.Round2;
using CodeJam.Utils.geom;
using CodeJam.Utils.graph;
using CodeJam.Utils.math;
using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;
using Wintellect.PowerCollections;
using Logger = Utils.LoggerFile;



namespace Year2014.Round3.Problem3
{
    public class Event
    {
        public bool isEntering;
        public int ID;

        public Event DeepClone()
        {
            // Deep clone your object
            Event e = new Event(isEntering, ID);
            return e;
        }

        public Event(bool _isEntering, int _ID)
        {
            isEntering = _isEntering;
            ID = _ID;
        }
    }
    public class CrimeHouseInput
    {
        public int N;

        public List<Event> events;
    }

    public class CrimeHouse : InputFileProducer<CrimeHouseInput>, InputFileConsumer<CrimeHouseInput, String>
    {
        public CrimeHouseInput createInput(Scanner scanner)
        {
           // scanner.enablePlayBack();
            CrimeHouseInput input = new CrimeHouseInput();

            input.N = scanner.nextInt();

            input.events = new List<Event>();

            for (int i = 0; i < input.N; ++i)
            {
                Event e = new Event(scanner.nextWord().Equals("E"),
                 scanner.nextInt());

                input.events.Add(e);
            }

            //Logger.LogInfo("Input {}", scanner.finishPlayBack());
            return input;
        }

        public bool IsValid(CrimeHouseInput input, int S, out int T)
        {
            LinkedList<Event> events = new LinkedList<Event>(input.events.Select(e => e.DeepClone()));
            T = 0;
            for(int s = 0; s < S; ++s)
            {
                events.AddFirst(new Event(true, 0));
            }

            int enteringCount = events.Count(e => e.isEntering);
            int leavingCount = events.Count - enteringCount;

            if (leavingCount > enteringCount)
            {
                return false;
            }

            Preconditions.checkState(leavingCount <= enteringCount);


            for(int l = 0; l < enteringCount-leavingCount; ++l)
            {
                ++T;
                events.AddLast(new Event(false, 0));
            }

            Dictionary<int, bool> isInside = new Dictionary<int, bool>();

            int nextUnknownId = 1000000;
            
            foreach(var e in events)
            {
                if (e.ID > 0)
                    isInside[e.ID] = false;
            }
            //events.Select(e => e.ID).Where(id => id > 0).ToDictionary(id => id, id => false);

            while(events.Count > 0)            
            {
                Event currentEvent = events.First.Value;
                events.RemoveFirst();

                if (currentEvent.isEntering == true && currentEvent.ID > 0)
                {
                    if (isInside[currentEvent.ID] == true)
                    {
                        return false;
                    }
                    isInside[currentEvent.ID] = true;
                    continue;
                }

                if (currentEvent.isEntering == false && currentEvent.ID > 0)
                {
                    if (isInside[currentEvent.ID] == false)
                    {
                        return false;
                    }

                    isInside[currentEvent.ID] = false;
                    continue;
                }

                if (currentEvent.isEntering == true && currentEvent.ID == 0)
                {
                    //Find first L X for a known IDs who is outside
                    Dictionary<int, bool> toIgnore = new Dictionary<int, bool>();

                    var first = events.FirstOrDefault(ev => 
                        {
                        if (ev.ID > 0
                        && isInside[ev.ID] == false )
                        {
                            if (ev.isEntering == true)
                            {
                                toIgnore[ev.ID] = true;
                            } else if (toIgnore.ContainsKey(ev.ID) ==false) {
                                return true;
                            }
                        }
                        return false;
                        });

                    //We choose this guy to be the one who entered
                    if (first != null)
                    {
                        isInside[first.ID] = true;
                        continue;
                    }
                    else
                    {
                        //create a new unknown id
                        isInside[nextUnknownId++] = true;
                        continue;
                    }
                }

                if (currentEvent.isEntering == false && currentEvent.ID == 0)
                {
                    var first = events.FirstOrDefault (ev =>
                        ev.ID > 0
                        && isInside[ev.ID] == true
                        && ev.isEntering == true);

                    if (first != null)
                    {
                        isInside[first.ID] = false;
                        continue;
                    }

                    //See if someone is inside the house but has no further events
                    var case2 = isInside.FirstOrDefault(kv => kv.Key > 0 && kv.Value == true && events.Any(e => e.ID == kv.Key) == false);

                    if (case2.Key > 0)
                    {
                        isInside[case2.Key] = false;
                        continue;
                    }

                    //Case 'L 0' (c)

                    //Everyone inside and next known event is they are leaving
                    var lastLeaving = events.LastOrDefault(ev =>
                        ev.ID > 0 
                        && ev.isEntering == false
                        && isInside[ev.ID] == true);

                    if (lastLeaving != null)
                    {
                        isInside[lastLeaving.ID] = false;
                        continue;
                    }
                }

                return false;

            }


            return true;
        }

        public String processInput(CrimeHouseInput input)
        {
            int t;
           // IsValid(input, 3, out t);

            bool lowVal = IsValid(input, 0, out t);
            bool hiVal = IsValid(input, input.N, out t);

            if (hiVal == false)
                return "CRIME TIME";

            int lo = 0;
            int hi = input.events.Count;

            //Precondition [lo, hi] contains first valid s

            //Find lowest hi
            while (lo < hi)
            {
                int mid = lo + (hi - lo) / 2;
                bool midValid = IsValid(input, mid, out t);

                if (midValid == true)
                {
                    hi = mid;
                }
                else
                {
                    lo = mid+1;
                }
            }

            Preconditions.checkState(lo == hi);
            IsValid(input, lo, out t);

                    return t.ToString();
                
            //return "CRIME TIME";
        }
    }

}