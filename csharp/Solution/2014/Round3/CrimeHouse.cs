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
        public bool seen;

        public Event DeepClone()
        {
            // Deep clone your object
            Event e = new Event(isEntering, ID);
            e.seen = false;
            return e;
        }

        public Event(bool _isEntering, int _ID)
        {
            isEntering = _isEntering;
            ID = _ID;
            seen = false;
        }
    }

    public class State
    {
        public LinkedList<Event> nextEvents;
        public bool isInside;

        public State()
        {
            nextEvents = new LinkedList<Event>();
            isInside = false;
        }

        public Event nextKnownEvent()
        {
            while (nextEvents.First != null && nextEvents.First.Value.seen)
            {
                nextEvents.RemoveFirst();
            }
            

            return nextEvents.First != null ? nextEvents.First.Value : null;
                
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

            Dictionary<int, State> state = new Dictionary<int, State>();

            int nextUnknownId = 1000000;
            
            //Build isInside set
            foreach(var e in events)
            {
                if (e.ID == 0)
                    continue;

                if (state.ContainsKey(e.ID) == false)
                {
                    state[e.ID] = new State();
                }

                state[e.ID].nextEvents.AddLast(e);
            }
            

            while(events.Count > 0)            
            {
                Event currentEvent = events.First.Value;
                currentEvent.seen = true;
                events.RemoveFirst();

                if (currentEvent.isEntering == true && currentEvent.ID > 0)
                {
                    if (state[currentEvent.ID].isInside == true)
                    {
                        return false;
                    }
                    state[currentEvent.ID].isInside = true;
                    continue;
                }

                if (currentEvent.isEntering == false && currentEvent.ID > 0)
                {
                    if (state[currentEvent.ID].isInside == false)
                    {
                        return false;
                    }

                    state[currentEvent.ID].isInside = false;
                    continue;
                }

                if (currentEvent.isEntering == true && currentEvent.ID == 0)
                {
                   
                    //Find first L X for a known IDs who is outside
                    
                    var first = events.FirstOrDefault(ev =>                         
                        ev.ID > 0
                        && state[ev.ID].isInside == false 
                        && state[ev.ID].nextKnownEvent().isEntering == false
                        );

                    //We choose this guy to be the one who entered
                    if (first != null)
                    {
                        state[first.ID].isInside = true;
                        continue;
                    }
                    else
                    {
                        State s = new State();
                        s.isInside = true;
                        //create a new unknown id
                        state[nextUnknownId++] = s;
                        continue;
                    }
                }

                if (currentEvent.isEntering == false && currentEvent.ID == 0)
                {
                    var first = events.FirstOrDefault (ev =>
                        ev.ID > 0
                        && state[ev.ID].isInside == true
                        && state[ev.ID].nextKnownEvent().isEntering == true);

                    if (first != null)
                    {
                        state[first.ID].isInside = false;
                        continue;
                    }

                    //See if someone is inside the house but has no further events
                    var case2 = state.FirstOrDefault(kv => kv.Key > 0 && kv.Value.isInside == true 
                        && kv.Value.nextKnownEvent() == null);

                    if (case2.Key > 0)
                    {
                        state[case2.Key].isInside = false;
                        continue;
                    }

                    //Case 'L 0' (c)

                    //Everyone inside and next known event is they are leaving
                    var lastLeaving = events.LastOrDefault(ev =>
                        ev.ID > 0
                        && state[ev.ID].isInside == true
                        && ev == state[ev.ID].nextKnownEvent()
                        && state[ev.ID].nextKnownEvent().isEntering == false
                        );

                    if (lastLeaving != null)
                    {
                        state[lastLeaving.ID].isInside = false;
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