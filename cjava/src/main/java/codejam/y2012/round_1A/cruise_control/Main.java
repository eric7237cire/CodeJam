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

import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.TreeMultimap;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
        return new String[] {"sample.in"};
    //  return new String[] { "B-small-practice.in", "B-large-practice.in" };
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

        // log.info("TestCase {} Grid {}", testCase, in.grid);
        return in;
    }

    
    Pair<Fraction,Fraction> getTimePosIntersection(int p1, int s1, int p2, int s2) {
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
            return new ImmutablePair<>(new Fraction(0), new Fraction(p1));
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
        
        return new ImmutablePair<>(t, pos1);
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
        Fraction position;
        public Event(int car1, int car2, Type type, Fraction time, Fraction position) {
            super();
            this.car1 = car1;
            this.car2 = car2;
            this.type = type;
            this.time = time;
            this.position = position;
        }
        @Override
        public String toString() {
            return "Event [cars=(" + car1 + ", " + car2 + ") " 
        + type + ", time=" + DoubleFormat.df3.format(time.doubleValue()) +
        ", position=" + DoubleFormat.df3.format(position) + "]";
        }
        @Override
        public int compareTo(Event o) {
            return ComparisonChain.start().compare(type,o.type).compare(car1, o.car1).result();            
        }
        
    }
    
    public class DoubleUpState {
        List<Pair<Integer,Integer>> doubledUp;
        
        public DoubleUpState(List<Pair<Integer, Integer>> doubledUp) {
            super();
            this.doubledUp = doubledUp;
        }
        
        public DoubleUpState(DoubleUpState ds) {
            super();
            this.doubledUp = new ArrayList<>(ds.doubledUp);
        }

        public String toString() {
            StringBuffer sb = new StringBuffer();
            for(Pair<Integer,Integer> pair : doubledUp) {
                sb.append("L: ").append(pair.getLeft())
                .append( " R: ").append(pair.getRight()).append("\n");
            }
            return sb.toString();
        }
        
        // -1 for already left, 0 for no, 1 for already right (ie cannot change to left)
        public int exists(int car) {
            Iterator<Pair<Integer,Integer>> pairIt = doubledUp.iterator();
            
            //Find the pair
            while(pairIt.hasNext()) {
                Pair<Integer,Integer> pair = pairIt.next();
                if (pair.getLeft() == car)
                    return -1;
                
                if (pair.getRight() == car)
                    return 1;
            }
            
            return 0;
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
    }
    
    public String handleCase(InputData in) {

        //Find all doubled up cars
        
        TreeMap<Integer,Integer> leftLane = new TreeMap<>();
        TreeMap<Integer,Integer> rightLane = new TreeMap<>();
        
        List<Pair<Integer,Integer>> doubledUp = Lists.newArrayList();
        
        for(int i = 0; i < in.N; ++i) {
            int pos = in.initialPosition[i];
            if (in.initialLane[i] == 'L') {
                leftLane.put(pos, i);
                //want initialPos - 5 < p < initialPos + 5
                Map<Integer,Integer> inter = rightLane.subMap(pos - 5, false, pos + 5, false);
                for(int carIdx : inter.values()) {
                    doubledUp.add(new ImmutablePair<>(i, carIdx));
                }
            }
            
            if (in.initialLane[i] == 'R') {
                rightLane.put(pos, i);
                Map<Integer,Integer> inter = leftLane.subMap(pos - 5, false, pos + 5, false);
                for(int carIdx : inter.values()) {
                    doubledUp.add(new ImmutablePair<>(carIdx, i));
                }
            }
        }
        
        List<Event> events = Lists.newArrayList();
        
        for(int i = 0; i < in.N; ++i) {
            for(int j = i + 1; j < in.N; ++j) {
                
                int passingCar = ( (in.initialPosition[i] < in.initialPosition[j]) || 
                        (in.initialPosition[i] == in.initialPosition[j] && in.speed[i] > in.speed[j])) ? i : j;
                int passedCar = passingCar == i ? j : i;
                
                
                //Same speed means they will never double or undouble each other,
                //important to avoid false doubling events
                if (in.speed[i] == in.speed[j])
                    continue;
                
                //The order doesn't matter because i + 5 with j is the same intersection as j +5, i
                Pair<Fraction, Fraction> timePosIntersecFB = 
                        getTimePosIntersection(in.initialPosition[passingCar]+5, in.speed[passingCar], 
                                in.initialPosition[passedCar], in.speed[passedCar]);
                
                Pair<Fraction, Fraction> timePosIntersecBF = getTimePosIntersection(in.initialPosition[passingCar], in.speed[passingCar], 
                        in.initialPosition[passedCar]+5, in.speed[passedCar]); 
                
                
                
                if (timePosIntersecFB != null)
                events.add(new Event(passingCar,passedCar,Event.Type.DOUBLING, timePosIntersecFB.getLeft(), timePosIntersecFB.getRight()));
                
                if (timePosIntersecBF != null)
                events.add(new Event(passingCar,passedCar,Event.Type.UNDOUBLING, timePosIntersecBF.getLeft(), timePosIntersecBF.getRight()));
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
        
        DoubleUpState ds = new DoubleUpState(doubledUp);
        
        log.debug("Double up state\n{}", ds);
       //in.levelMin.subList()

        Fraction max = minTime(timeEvents, ds, eventList, firstTime); 
        
        if (max == null)
            return String.format("Case #%d: Possible", in.testCase);
        else
       return String.format("Case #%d: %s", in.testCase, DoubleFormat.df6.format(max.doubleValue()));

    }
    
    Fraction minTime(TreeMultimap<Fraction, Event> timeEvents, DoubleUpState ds, 
            List<Event> currentEvents, Fraction currentTime) {
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
            return minTime(timeEvents, ds, events, nextTime);
        }
        
        Event event = currentEvents.get(0);
        
        if (event.type == Event.Type.UNDOUBLING) {
            DoubleUpState dsNew = new DoubleUpState(ds);
            dsNew.handleUndouble(event);
            return minTime(timeEvents, dsNew, currentEvents.subList(1, currentEvents.size()), currentTime);
        }
        
        //Doubling event
        
        //Check if one of the cars is already doubled
        int car1_pos = ds.exists(event.car1);
        int car2_pos = ds.exists(event.car2);
        
        if (car1_pos == -1 && car2_pos == -1) {
            return currentTime;
        }
        if (car1_pos == 1 && car2_pos == 1) {
            return currentTime;
        }
        if (car1_pos == 1 || car2_pos == -1) {
            DoubleUpState dsNew = new DoubleUpState(ds);
            dsNew.doubledUp.add(new ImmutablePair<>(event.car2, event.car1));
            return minTime(timeEvents, dsNew, currentEvents.subList(1, currentEvents.size()), currentTime);
        }
        if (car1_pos == -1 || car2_pos == 1) {
            DoubleUpState dsNew = new DoubleUpState(ds);
            dsNew.doubledUp.add(new ImmutablePair<>(event.car1, event.car2));
            return minTime(timeEvents, dsNew, currentEvents.subList(1, currentEvents.size()), currentTime);
        }
     
        //Have a choice
        DoubleUpState dsNew = new DoubleUpState(ds);
        dsNew.doubledUp.add(new ImmutablePair<>(event.car1, event.car2));
        Fraction f1 = minTime(timeEvents, dsNew, currentEvents.subList(1, currentEvents.size()), currentTime);
        
        DoubleUpState dsNew2 = new DoubleUpState(ds);
        dsNew2.doubledUp.add(new ImmutablePair<>(event.car2, event.car1));
        
        Fraction f2 =  minTime(timeEvents, dsNew2, currentEvents.subList(1, currentEvents.size()), currentTime);
        
        if (f1 == null || f2 == null) 
            return null;
        
        return f1.compareTo(f2) >= 0 ? f1 : f2;
    }

}
