package com.eric.codejam;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.utils.Grid;
import com.google.common.base.Preconditions;
import com.google.common.collect.Range;
import com.google.common.collect.Ranges;

public class Main {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    Grid<Integer> grid;

    public static void handleCase(int caseNumber, Scanner scanner,
            PrintStream os) {

        int min = Main.buildMain(scanner);

        log.info("Starting case {}", caseNumber);

        

        os.println("Case #" + caseNumber + ": " + min);

    }

    final static int MAX_WIDTH = 1000;
    final static int MAX_HEIGHT = 15;

    private static int buildMain(Scanner scanner) {
//        Main m = new Main();
        
        Grid<Integer> grid = Grid.buildEmptyGrid(MAX_HEIGHT, MAX_WIDTH, null);
        grid.setyZeroOnTop(false);
        int numPlayers = scanner.nextInt();

        List<Range> ranges = new ArrayList<>();

        for (int playerNum = 0; playerNum < numPlayers; ++playerNum) {
            int x = scanner.nextInt() - 1;
            int y = scanner.nextInt() - 1;
            grid.setEntry(y, x, x);

            Range r = Ranges.closed(Math.max(y - 1, 0),
                    Math.min(MAX_HEIGHT - 1, y + 1));

            // find range
            boolean found = false;

            for (ListIterator<Range> rangeIt = ranges.listIterator(); rangeIt
                    .hasNext();) {
                Range range = rangeIt.next();
                if (range.isConnected(r)) {
                    found = true;
                    rangeIt.set(range.span(r));
                    break;
                }
            }

            if (!found) {
                ranges.add(r);
            }
        }
        
        int minCol = 1;

        for (ListIterator<Range> rangeIt = ranges.listIterator(); rangeIt
                .hasNext();) {
            Range r = rangeIt.next();
            final int lowerBound = (Integer) r.lowerEndpoint();
            final int upperBound = (Integer) r.upperEndpoint();

            List<Node<List<Integer>>> lastNode = new ArrayList<>();

            List<List<List<Integer>>> unmatchedPlayers = new ArrayList<>();

            for (int y = 0; y < MAX_HEIGHT; ++y) {
                lastNode.add(null);
                List<List<Integer>> topCenterBottom = new ArrayList<>();
                for (int i = 0; i < 3; ++i) {
                    topCenterBottom.add(new ArrayList<Integer>());
                }
                unmatchedPlayers.add(topCenterBottom);
            }

            Node rootNode = null;
            Graph<Integer> graph = new Graph<>();
            List<Integer> players = new ArrayList<>();

            for (int x = 0; x < MAX_WIDTH; ++x) {
                for (int y = lowerBound; y <= upperBound; ++y) {

                    Integer player = grid.getEntry(y, x);

                    if (player == null) {
                        continue;
                    }

                    log.debug("Processing player {} x,y {}, {}", player, x, y);
                    players.add(player);
                    if (y < upperBound) {
                        // tops bottom row
                        unmatchedPlayers.get(y + 1).get(0).add(player);
                    }

                    if (y > lowerBound) {
                        // bottoms top row
                        unmatchedPlayers.get(y - 1).get(2).add(player);
                    }
                    
                    List<Integer> playersSameRow = unmatchedPlayers.get(y).get(1);
                    List<Integer> playersAbove = unmatchedPlayers.get(y).get(2);
                    List<Integer> playersBelow = unmatchedPlayers.get(y).get(0);
                    
                    for(Integer p : playersAbove) {
                        graph.addEdge(player, p);
                    }
                    for(Integer p : playersBelow) {
                        graph.addEdge(player, p);
                    }
                    for(Integer p : playersSameRow) {
                        graph.addEdge(player, p);
                    }
                    
                    Preconditions.checkState(playersSameRow.size() <= 1);
                    
                    playersAbove.clear();
                    playersBelow.clear();
                    playersSameRow.clear();
                    
                    // center
                    unmatchedPlayers.get(y).get(1).add(player);

                    /*
                    List<Node<List<Integer>>> rootNodeList = new ArrayList<>();
                    handleFindMatches(unmatchedPlayers, y, lastNode,
                            rootNodeList);
                    
                    if (rootNode == null && rootNodeList.size() > 0) {
                        rootNode = rootNodeList.get(0);
                    }
                    
                    //Clear upper and lower, since they no longer match anything but this row
                    unmatchedPlayers.get(y).get(0).clear();
                    unmatchedPlayers.get(y).get(2).clear();
                    
                    if (unmatchedPlayers.get(y).get(1).size() > 1) {
                        unmatchedPlayers.get(y).get(1).remove(0);
                    }
                    
                    Preconditions.checkState(unmatchedPlayers.get(y).get(1).size() <= 2);*/
                }
            }

            log.debug("Root node {}", rootNode);
            //Main.traverseTree(rootNode);
            boolean found = false;
            for(int colorNum = minCol; colorNum <= 3; ++colorNum) {
                boolean re = backtrack(new int[0], colorNum, graph, players);
                if (re) {
                    minCol = colorNum;
                    found = true;
                    break;
                }
                log.info("Re is {}", re);
            }
            
            if(!found) {
                return 4;
            }
        }

       // m.grid = grid;

        return minCol;
    }
    
    private static boolean prune(int[] solution, Graph<Integer> graph, List<Integer> players) {
        if (solution.length == 0) {
            return false;
        }
        int solutionIndex = solution.length - 1;
        
        int player = players.get(solutionIndex);
        int playerColor = solution[solutionIndex];
        
        Set<Integer> adj = graph.getConnectedGraphNodes(player);
        
        if (adj == null) {
            return false;
        }
        
        for(int connectedPlayer : adj) {
            if (connectedPlayer >= player) {
                continue;
            }
            int connectedPlayerIndex = Collections.binarySearch(players, connectedPlayer);
            int connectedPlayerColor = solution[connectedPlayerIndex];
            if (playerColor == connectedPlayerColor) {
                return true;
            }
        }
        
        return false;
    }
    
    private static boolean accept(int[] solution, Graph<Integer> graph, List<Integer> players) {
        return solution.length == players.size();
    }
    
    private static boolean backtrack(int[] solution, final int colorNum, Graph<Integer> graph, List<Integer> players) {
    
        
        if (prune(solution, graph, players)) {
            return false;
        }
        if (accept(solution, graph, players)) {
            return true;
        }
        log.debug("backtrack.  solution {}", solution);
        
        int[] solFirst = Arrays.copyOf(solution, solution.length+1);
        
        for(int i = 1; i <= colorNum; ++i) {
            solFirst[solFirst.length-1] = i;
            boolean r = backtrack(solFirst, colorNum, graph, players);
            if (r) {
                return true;
            }
        }
        return false;
    
    }

    private static List<Node<List<Integer>>> buildTriangles(
            List<Integer> players, List<Integer> playersSameRow) {

        List<Node<List<Integer>>> ret = new ArrayList<>();
        log.debug("Build triangles");

        if (playersSameRow.size() == 2) {
            Preconditions.checkState(players.size() == 1);
            List<Integer> triangle = Arrays.asList(playersSameRow.get(0),
                    players.get(0), playersSameRow.get(1));
            Node<List<Integer>> node = new Node<List<Integer>>(triangle);
            log.debug("Built triangle {}", triangle);
            ret.add(node);
            return ret;
        }

        for (int i = 0; i <= players.size() - 2; ++i) {
            List<Integer> triangle = Arrays.asList(players.get(i),
                    players.get(i + 1), playersSameRow.get(0));
            Node<List<Integer>> node = new Node<List<Integer>>(triangle);
            log.debug("Built triangle {}", triangle);
            ret.add(node);
            if (ret.size() > 1) {
                ret.get(ret.size() - 2).lhs = node;
            }
        }

        return ret;
    }

    private static void attachNodeIfNeeded(int branchIndex,
            List<Node<List<Integer>>> lastNode, Node<List<Integer>> node) {
        // Is it attatched / branched to the tree or not?
        if (lastNode.get(branchIndex) == null) {
            // non attatched

            Node<List<Integer>> bottomNode = branchIndex > 0 ? lastNode
                    .get(branchIndex - 1) : null;
            Node<List<Integer>> topNode = branchIndex < Main.MAX_HEIGHT - 1 ? lastNode
                    .get(branchIndex + 1) : null;

            if (topNode == null && bottomNode == null) {
                // stays non attached
            } else if (bottomNode == null
                    || Collections.max(topNode.data) > Collections
                            .max(bottomNode.data)) {
                log.debug(
                        "Attaching node with triangle {} above with triangle {}",
                        node.data, topNode.data);
                topNode.rhs = node;
            } else {
                log.debug(
                        "Attaching node with triangle {} below with triangle {}",
                        node.data, bottomNode.data);
                bottomNode.rhs = node;
            }
        } else {
            // attached, just need to add to currenty node
            lastNode.get(branchIndex).lhs = node;
        }

        log.debug("Branch index {} now has node {}", branchIndex, node.data);
        lastNode.set(branchIndex, node);
    }

    private static void handleFindMatches(
            List<List<List<Integer>>> unmatchedPlayers, int y,
            List<Node<List<Integer>>> lastNode,
            List<Node<List<Integer>>> rootNodeList) {

        // either 2 above, 1 same row or 1 above 2 same row
        List<Integer> playersSameRow = unmatchedPlayers.get(y).get(1);
        List<Integer> playersAbove = unmatchedPlayers.get(y).get(2);
        List<Integer> playersBelow = unmatchedPlayers.get(y).get(0);

        Preconditions.checkArgument(playersSameRow.size() >= 1);

        if (playersAbove.size() > 0) {
            List<Node<List<Integer>>> nodes = buildTriangles(playersAbove,
                    playersSameRow);
            if (nodes.size() > 0) {
                if (rootNodeList.size() == 0) {
                    rootNodeList.add(nodes.get(0));
                }
                attachNodeIfNeeded(y, lastNode, nodes.get(nodes.size() - 1));
            }
        }

        if (playersBelow.size() > 0) {
            List<Node<List<Integer>>> nodes = buildTriangles(playersBelow,
                    playersSameRow);
            if (nodes.size() > 0) {
                if (rootNodeList.size() == 0) {
                    rootNodeList.add(nodes.get(0));
                }
                attachNodeIfNeeded(y - 1, lastNode, nodes.get(nodes.size() - 1));
            }
        }

    }

    private static int traverseTree(Node root) {
        List<Node> toProcess = new ArrayList<>();
        Map<Integer, Integer> coloring = new HashMap<>();

        toProcess.add(root);

        while (toProcess.size() > 0) {
            Node<List<Integer>> n = toProcess.remove(0);

            boolean[] colorsTaken = new boolean[3];
            // check
            for (int i = 0; i < n.data.size(); ++i) {
                Integer player = n.data.get(i);
                Integer color = coloring.get(player);
                if (colorsTaken[color]) {
                    return 4;
                }
                colorsTaken[color] = true;
            }
            // assign colors
            for (int i = 0; i < n.data.size(); ++i) {
                Integer player = n.data.get(i);
                Integer color = coloring.get(player);
                if (color == null) {
                    for (int colorChoice = 0; colorChoice < 3; ++colorChoice) {
                        if (!colorsTaken[colorChoice]) {
                            colorsTaken[colorChoice] = true;
                            coloring.put(player, colorChoice);
                            continue;
                        }
                    }
                }
                colorsTaken[color] = true;
            }

            if (n.lhs != null) {
                toProcess.add(0, n.lhs);
            }

            if (n.rhs != null) {
                toProcess.add(n.rhs);
            }
        }

        return 3;
    }

    public Main() {

        // TODO Add test case vars
        super();
    }

    public static void main(String args[]) throws Exception {

        if (args.length < 1) {
            //args = new String[] { "sample.txt" };
            args = new String[] { "C-small-practice.in" };
        }
        log.info("Input file {}", args[0]);

        Scanner scanner = new Scanner(new File(args[0]));

        int t = scanner.nextInt();

        for (int i = 1; i <= t; ++i) {

            handleCase(i, scanner, System.out);

        }

        scanner.close();
    }
}