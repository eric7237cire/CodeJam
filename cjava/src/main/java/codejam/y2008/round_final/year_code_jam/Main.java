package codejam.y2008.round_final.year_code_jam;


import java.util.Scanner;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.datastructures.FlowEdge;
import codejam.utils.datastructures.FlowNetwork;
import codejam.utils.datastructures.FordFulkerson;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.Direction;
import codejam.utils.utils.GridChar;

import com.google.common.collect.Sets;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
         return new String[] {"sample.in"};
        // return new String[] { "D-small-practice.in" };
   //    return new String[] { "D-small-practice.in", "D-large-practice.in" };
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {

        
        InputData in = new InputData(testCase);
       
        in.R = scanner.nextInt(); // rows
        in.C = scanner.nextInt();
        
        in.grid = GridChar.buildFromScanner(scanner, in.R, in.C, '.');
        
        return in;
    }

   
    
    public String handleCase(InputData in) {

   
        FlowNetwork fn = new FlowNetwork(in.C * in.R + 4);
        
        int lastVertex = in.C * in.R - 1;
        
        int s = lastVertex + 1;
        int t = lastVertex + 2;
        
        boolean doSwitch = true;
        
        for(int vertex = 0; vertex <= lastVertex; ++vertex) {
            
            char ch = in.grid.getEntry(vertex);
            
            int[] rowCol = in.grid.getRowCol(vertex);
            int row = rowCol[0];
            int col = rowCol[1];
            
            int outwardEdges = 0;
            if (row == 0 || row == in.grid.getRows() - 1)
                ++outwardEdges;
            
            if (col == 0 || col == in.grid.getCols() - 1)
                ++outwardEdges;
            
            if (outwardEdges > 0 && vertex % 2 == 0) {
                //Create edge from external white vertex to this one
                fn.addEdge(new FlowEdge(s, vertex, outwardEdges));
            } else if (outwardEdges > 0 && vertex % 2 == 1) {
                //Create edge from external flipped to blue vertex to this one
                //fn.addEdge(new FlowEdge(s, vertex, 1));
                fn.addEdge(new FlowEdge(s, vertex, outwardEdges));
                //fn.addEdge(new FlowEdge(vertex, t, outwardEdges));
            }
    
            
            for(int dirIdx = 0; dirIdx <= 3; ++dirIdx) {
                Direction dir = Direction.NORTH.turn(dirIdx * 2);
                
                Integer adj = in.grid.getIndex(vertex, dir);
                
                if (adj == null)
                    continue;
                
                
                //Create edge to adj vertex
                fn.addEdge(new FlowEdge(vertex, adj, 1));
            
            }
            
            if (ch == '?')
                continue;
            
            //Flip if even
            if (doSwitch) {
            if (vertex % 2 == 0) {
                if (ch == '#')
                    ch = '.';
                else
                    ch = '#';
            }
            }
            
            if (ch == '#') {
                // blue edge
                fn.addEdge(new FlowEdge(vertex, t, 4));
            } else if (ch == '.') {
                //white edge
                fn.addEdge(new FlowEdge(s, vertex, 4));
            }
        }
        
        //fn.addEdge(new FlowEdge(s, borderVertex, in.R * in.C));
        
        FordFulkerson ff = new FordFulkerson(fn, s, t);
        
        log.debug("Max flow from " + s + " to " + t);
        for (int v = 0; v < fn.V(); v++) {
            for (FlowEdge e : fn.adj(v)) {
                if ((v == e.from()) && e.flow() > 0)
                    log.debug("   " + e);
            }
        }

        int ans = (int) ff.value();
        
        
        Set<Integer> inCut = Sets.newHashSet();
        
        // print min-cut
        log.debug("Min cut: ");
        for (int v = 0; v < fn.V(); v++) {
            if (ff.inCut(v))  {
                inCut.add(v);
                log.debug(v + " ");
                //If it is on border, add 1
                if (v < in.R * in.C) {
                    int[] rowCol = in.grid.getRowCol(v);
                    int row = rowCol[0];
                    int col = rowCol[1];
                    if (row == 0 || row == in.grid.getRows() - 1)
                        ++ans;
                    
                    if (col == 0 || col == in.grid.getCols() - 1)
                        ++ans;
                    
                }
            }            
        }
        log.debug("");

        log.debug("Max flow value = {}.  Ans adjusted for border {}", ff.value(), ans);
        
        GridChar switched = new GridChar(in.grid);
        for (int v = 0; v < in.grid.getSize(); v++) {
            if (in.grid.getEntry(v) != '?')
                continue;
        
            //The cut contains s, so anything in the cut must be white
            char ch = inCut.contains(v) ? '.' : '#';
            
            if (doSwitch && (v%2==0)) {
//            if ( ( v % 2== 1 && !inCut.contains(v)) || 
//            (v % 2 == 0 && inCut.contains(v)) ) {
                if (ch == '#')
                    ch = '.';
                else
                    ch = '#';
            //}
            }
            
            switched.setEntry(v, ch);                
                
        }
        
        log.debug("switched {}", switched);
        
        int total = 0;
        for (int v = 0; v < switched.getSize(); v++) {
            if (switched.getEntry(v) != '#')
                continue;
            

            for(int dirIdx = 0; dirIdx <= 3; ++dirIdx) {
                Direction dir = Direction.NORTH.turn(dirIdx * 2);
                char adj = switched.getEntry(v,dir);
                
                if (adj == '.')
                    ++total;
            }
        }
        
        //
                        
        return String.format("Case #%d: %d ", in.testCase, total);        
    }

}
