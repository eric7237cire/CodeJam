package com.eric.codejam;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.utils.Grid;
import com.google.common.collect.Range;
import com.google.common.collect.Ranges;

public class Main {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    Grid<Integer> grid;

    public static void handleCase(int caseNumber, Scanner scanner,
            PrintStream os) {

        Main m = Main.buildMain(scanner);

        log.info("Starting case {}", caseNumber);

        log.info("{}", m.grid);

        os.println("Case #" + caseNumber + ": ");

    }

    private static Main buildMain(Scanner scanner) {
        Main m = new Main();
        final int MAX_WIDTH = 15;
        final int MAX_HEIGHT = 15;
        Grid<Integer> grid = Grid.buildEmptyGrid(MAX_HEIGHT, MAX_WIDTH, null);
        grid.setyZeroOnTop(false);
        int numPlayers = scanner.nextInt();

        List<Range> ranges = new ArrayList<>();

        for (int playerNum = 0; playerNum < numPlayers; ++playerNum) {
            int x = scanner.nextInt() - 1;
            int y = scanner.nextInt() - 1;
            grid.setEntry(y, x, playerNum);

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

        for (ListIterator<Range> rangeIt = ranges.listIterator(); rangeIt
                .hasNext();) {
            Range r = rangeIt.next();
            final int lowerBound = (Integer) r.lowerEndpoint();
            final int upperBound = (Integer) r.upperEndpoint();

            List<Node<List<Integer>>> lastNode = new ArrayList<>();

            List<Integer> lastPlayerInRow = new ArrayList<>();

            for (int y = 0; y < MAX_HEIGHT; ++y) {
                lastNode.add(null);
                lastPlayerInRow.add(null);
            }

            Node rootNode = null;

            for (int x = 0; x < MAX_WIDTH; ++x) {
                for (int y = lowerBound; y <= upperBound; ++y) {

                    Integer player = grid.getEntry(y, x);

                    if (player == null) {
                        continue;
                    }

                    Integer playerSameRow = lastPlayerInRow.get(y);
                    Integer playerTop = y < MAX_HEIGHT - 1 ? lastPlayerInRow
                            .get(y + 1) : null;
                    Integer playerBottom = y > 0 ? lastPlayerInRow.get(y - 1)
                            : null;
                    if (ObjectUtils.compare(playerTop, playerSameRow) < 0) {
                        playerTop = null;
                    }
                    if (ObjectUtils.compare(playerBottom, playerSameRow) < 0) {
                        playerBottom = null;
                    }

                    // Current row player is now "masked"
                    lastPlayerInRow.set(y, player);

                    int topBranchIndex = y;
                    int bottomBranchIndex = y - 1;

                    if (playerSameRow != null && playerTop != null) {
                        List<Integer> topTriangle = Arrays.asList(
                                playerSameRow, playerTop, player);
                        Node<List<Integer>> node = new Node<List<Integer>>(
                                topTriangle);
                        
                        if (rootNode == null) {
                            rootNode = node;
                        }

                        // Is it attatched / branched to the tree or not?
                        if (lastNode.get(topBranchIndex) == null) {
                            // non attatched

                            Node<List<Integer>> bottomNode = topBranchIndex > 0 ? lastNode
                                    .get(topBranchIndex - 1) : null;
                            Node<List<Integer>> topNode = topBranchIndex < MAX_HEIGHT - 1 ? lastNode
                                    .get(topBranchIndex + 1) : null;

                            if (topNode == null && bottomNode == null) {
                                // stays non attached
                            } else if (bottomNode == null
                                    || Collections.max(topNode.data) > Collections
                                            .max(bottomNode.data)) {
                                topNode.rhs = node;
                            } else {
                                bottomNode.rhs = node;
                            }
                        } else {
                            // attached, just need to add to currenty node
                            lastNode.get(topBranchIndex).lhs = node;
                        }

                        lastNode.set(topBranchIndex, node);
                    }

                    if (playerSameRow != null && playerBottom != null) {
                        List<Integer> bottomTriangle = Arrays.asList(
                                playerSameRow, playerBottom, player);
                        Node<List<Integer>> node = new Node<List<Integer>>(
                                bottomTriangle);
                        
                        if (rootNode == null) {
                            rootNode = node;
                        }

                        // Is it attatched / branched to the tree or not?
                        if (lastNode.get(bottomBranchIndex) == null) {
                            // non attatched

                            Node<List<Integer>> bottomNode = bottomBranchIndex > 0 ? lastNode
                                    .get(bottomBranchIndex - 1) : null;
                            Node<List<Integer>> topNode = bottomBranchIndex < MAX_HEIGHT - 1 ? lastNode
                                    .get(bottomBranchIndex + 1) : null;

                            if (topNode == null && bottomNode == null) {
                                // stays non attached
                            } else if (bottomNode == null
                                    || Collections.max(topNode.data) > Collections
                                            .max(bottomNode.data)) {
                                topNode.rhs = node;
                            } else {
                                bottomNode.rhs = node;
                            }
                        } else {
                            // attached, just need to add to currenty node
                            lastNode.get(bottomBranchIndex).lhs = node;
                        }

                        lastNode.set(bottomBranchIndex, node);
                    }
                }
            }
            
            log.debug("Root node {}", rootNode);
        }

        m.grid = grid;

        return m;
    }

    public Main() {

        // TODO Add test case vars
        super();
    }

    public static void main(String args[]) throws Exception {

        if (args.length < 1) {
            args = new String[] { "sample.txt" };
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