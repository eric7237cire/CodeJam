package codejam.y2012.round_1A.cruise_control;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeMap;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.fraction.Fraction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.DoubleFormat;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.TreeMultimap;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
      //  return new String[] {"sample.in"};
      return new String[] { "C-small-practice.in", "C-large-practice.in" };
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {

        InputData in = new InputData(testCase);
        in.N = scanner.nextInt();
        
        in.initialLane = new char[in.N];
        in.initialPosition = new int[in.N];
        in.speed = new int[in.N];
        
        
        for (int i = 0; i < in.N; ++i) {
            in.initialLane[i] = scanner.next().charAt(0);
            in.speed[i] = scanner.nextInt();
            in.initialPosition[i] = scanner.nextInt();
        }

        return in;
    }

    
    Fraction getTimeIntersection(int p1, int s1, int p2, int s2) {
        /*
         * p1 + s1 * t = pos1
         * p2 + s2 * t = pos2
         * 
         * we want pos1 == pos2
         * 
         * p1 + s1 * t == p2 + s2 * t
         * p1 - p2 = s2 * t - s1 * t == t * (s2 - s1)
         * t = (p1 - p2) / (s2 - s1)
         */
        
        if (p1 == p2) {
            return new Fraction(0);
        }
        
        if (s2 == s1)
            return null;
        
        Fraction t = new Fraction(p1-p2, s2-s1);
        
        if (t.compareTo(Fraction.ZERO) < 0) {
            return null;
        }
        
        Fraction pos1 = t.multiply(s1).add(p1);
        Fraction pos2 = t.multiply(s2).add(p2);
        Preconditions.checkState(pos1.equals(pos2));
        
        return t;
    }
    
    private static class Event implements Comparable<Event> {
        int car1;
        int car2;
        public static enum Type {
            UNDOUBLING,
            DOUBLING            
        }
        Type type;
        
        Fraction time;
        public Event(int car1, int car2, Type type, Fraction time) {
            super();
            this.car1 = car1;
            this.car2 = car2;
            this.type = type;
            this.time = time;
        }
        @Override
        public String toString() {
            return "Event [cars=(" + car1 + ", " + car2 + ") " 
        + type + ", time=" + DoubleFormat.df3.format(time.doubleValue())  + "]";
        }
        @Override
        public int compareTo(Event o) {
            return ComparisonChain.start().compare(type,o.type).compare(car1, o.car1).compare(car2,o.car2).result();            
        }
        
    }
    
    public class DoubleUpState {
        List<Pair<Integer,Integer>> doubledUp;
        Fraction time;
        
        @Override
        public int hashCode() {
            return Objects.hashCode(doubledUp, time);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            DoubleUpState other = (DoubleUpState) obj;
            
            return Objects.equal(doubledUp, other.doubledUp) &&
                    Objects.equal(time, other.time);
        }

        public DoubleUpState(List<Pair<Integer, Integer>> doubledUp, Fraction time) {
            super();
            this.doubledUp = doubledUp;
            this.time = time;
        }
        
        public DoubleUpState(DoubleUpState ds) {
            super();
            this.doubledUp = new ArrayList<>(ds.doubledUp);
            this.time = ds.time;
            
        }

        public String toString() {
            StringBuffer sb = new StringBuffer();
            for(Pair<Integer,Integer> pair : doubledUp) {
                sb.append("L: ").append(pair.getLeft())
                .append( " R: ").append(pair.getRight()).append("\n");
            }
            return sb.toString();
        }
        
        public boolean validPairing(int leftCar, int rightCar) {
            Iterator<Pair<Integer,Integer>> pairIt = doubledUp.iterator();
            
            //Find the pair
            while(pairIt.hasNext()) {
                Pair<Integer,Integer> pair = pairIt.next();
                if (pair.getLeft() == rightCar) {
                    return false;                    
                }
                
                if (pair.getRight() == leftCar) {
                    return false;
                }
            }
            
            return true;
        }
        
        public void handleUndouble(Event event) {
            Preconditions.checkArgument(event.type == Event.Type.UNDOUBLING);
            
            Iterator<Pair<Integer,Integer>> pairIt = doubledUp.iterator();
            
            boolean deleted = false;
            //Find the pair
            while(pairIt.hasNext()) {
                Pair<Integer,Integer> pair = pairIt.next();
                
                if ( (pair.getLeft() == event.car1 && pair.getRight() == event.car2) ||
                        (pair.getLeft() == event.car2 && pair.getRight() == event.car1) ) {
                    Preconditions.checkState(!deleted);
                    pairIt.remove();
                    deleted = true;
                }
            }
            
            Preconditions.checkState(deleted);
        }

        private Main getOuterType() {
            return Main.this;
        }
    }
    
    public String handleCase(InputData in) {

        List<Pair<Integer,Integer>> doubledUp = Lists.newArrayList();
        
        List<Event> events = Lists.newArrayList();
        
        for(int i = 0; i < in.N; ++i) {
            for(int j = i + 1; j < in.N; ++j) {
                
                if ( Math.abs(in.initialPosition[i] - in.initialPosition[j] ) < 5 ) {
                    if (in.initialLane[i] == 'L') {
                        doubledUp.add(new ImmutablePair<>(i, j));
                    } else {
                        doubledUp.add(new ImmutablePair<>(j, i));
                    }
                }

                //Same speed means they will never double or undouble each other,
                //important to avoid false doubling events
                if (in.speed[i] == in.speed[j])
                    continue;
                
                if ( Math.abs(in.initialPosition[i] - in.initialPosition[j] ) < 5 ) {
                    //cars are doubled up
                    int passingCar = (in.speed[i] > in.speed[j]) ? i : j;
                    int passedCar = passingCar == i ? j : i;
        
                    //Just find the undoubling
                    Fraction timePosIntersecBF = getTimeIntersection(in.initialPosition[passingCar], in.speed[passingCar], 
                            in.initialPosition[passedCar]+5, in.speed[passedCar]); 
                    
                    Preconditions.checkState(timePosIntersecBF != null);
                    
                    events.add(new Event(passingCar,passedCar,Event.Type.UNDOUBLING, timePosIntersecBF));
                    
                } else {
                    int passingCar = (in.initialPosition[i] < in.initialPosition[j]) ? i : j;
                    int passedCar = passingCar == i ? j : i;
                      
                    
                    //The order doesn't matter because i + 5 with j is the same intersection as j +5, i
                    Fraction timePosIntersecFB = 
                            getTimeIntersection(in.initialPosition[passingCar]+5, in.speed[passingCar], 
                                    in.initialPosition[passedCar], in.speed[passedCar]);
                    
                    Fraction timePosIntersecBF = getTimeIntersection(in.initialPosition[passingCar], in.speed[passingCar], 
                            in.initialPosition[passedCar]+5, in.speed[passedCar]); 
                    
                    if (timePosIntersecFB == null || timePosIntersecBF == null) 
                        continue;
                    
                    events.add(new Event(passingCar,passedCar,Event.Type.DOUBLING, timePosIntersecFB));
                    
                    
                    events.add(new Event(passingCar,passedCar,Event.Type.UNDOUBLING, timePosIntersecBF));
                    
                    
                }
            }
        }
        
        TreeMultimap<Fraction, Event> timeEvents = TreeMultimap.create();
        
        for(Event event : events) {
            timeEvents.put(event.time, event);
        }
        
        for(Fraction time : timeEvents.keySet()) {
            log.debug("\nTime {}", DoubleFormat.df3.format(time.doubleValue()));
            
            for(Event event : timeEvents.get(time)) {
                log.debug("Event {}", event);
            }
        }
        
        
        Fraction firstTime = timeEvents.keySet().first();
        List<Event> eventList =  new ArrayList<>(timeEvents.get(firstTime));
        
        DoubleUpState ds = new DoubleUpState(doubledUp, firstTime);
        Map<DoubleUpState, Fraction> memoize = Maps.newHashMap();
        
        log.debug("Double up state\n{}", ds);
       //in.levelMin.subList()

        Fraction max = minTime(timeEvents, memoize, ds, eventList, firstTime); 
        
        if (max == null)
            return String.format("Case #%d: Possible", in.testCase);
        else
       return String.format("Case #%d: %s", in.testCase, DoubleFormat.df6.format(max.doubleValue()));

    }
    
    Fraction minTime(TreeMultimap<Fraction, Event> timeEvents,  Map<DoubleUpState, Fraction> memoize, DoubleUpState ds, 
            List<Event> currentEvents, Fraction currentTime) {
        
        if (memoize.containsKey(ds) ) {
            return memoize.get(ds);
        }
            
        //base cases
        if (currentEvents.isEmpty()) {
            //fetch next 
            Iterator<Fraction> timeIterator = timeEvents.keySet().tailSet(currentTime).iterator();
            
            //skip over current time
            Preconditions.checkState(timeIterator.hasNext());
            Preconditions.checkState(currentTime.equals(timeIterator.next()));
            
            if (!timeIterator.hasNext()) {
                //Possible to continue forever
                return null;
            }
            
            Fraction nextTime = timeIterator.next();
            List<Event> events =  new ArrayList<>(timeEvents.get(nextTime));
            DoubleUpState dsNew = new DoubleUpState(ds);
            dsNew.time = nextTime;
            Fraction f = minTime(timeEvents, memoize, dsNew, events, nextTime);
            memoize.put(dsNew,f);
            return f;
        }
        
        Event event = currentEvents.get(0);
        
        if (event.type == Event.Type.UNDOUBLING) {
            DoubleUpState dsNew = new DoubleUpState(ds);
            dsNew.handleUndouble(event);
            Fraction f = minTime(timeEvents, memoize, dsNew, currentEvents.subList(1, currentEvents.size()), currentTime);
            memoize.put(dsNew,f);
            return f;
        }
        
        //Doubling event
        
        Fraction f1 = new Fraction(-1);
        Fraction f2 = new Fraction(-1);

        if (ds.validPairing(event.car1, event.car2)) {
            DoubleUpState dsNew = new DoubleUpState(ds);
            dsNew.doubledUp.add(new ImmutablePair<>(event.car1, event.car2));
            f1 = minTime(timeEvents, memoize, dsNew, currentEvents.subList(1, currentEvents.size()), currentTime);
            memoize.put(dsNew, f1);
        }

        if (ds.validPairing(event.car2, event.car1)) {
            DoubleUpState dsNew2 = new DoubleUpState(ds);
            dsNew2.doubledUp.add(new ImmutablePair<>(event.car2, event.car1));

            f2 = minTime(timeEvents, memoize, dsNew2, currentEvents.subList(1, currentEvents.size()), currentTime);
            memoize.put(dsNew2, f2);
        }

        if (f1 == null || f2 == null)
            return null;

        Fraction max = f1.compareTo(f2) >= 0 ? f1 : f2;

        if (max.compareTo(Fraction.ZERO) < 0) {
            return currentTime;
        } else {
            return max;
        }
    }

}
