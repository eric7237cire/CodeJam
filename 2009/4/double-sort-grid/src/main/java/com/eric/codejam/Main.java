package com.eric.codejam;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.main.Runner;
import com.eric.codejam.multithread.Consumer.TestCaseHandler;
import com.eric.codejam.multithread.Producer.TestCaseInputReader;
import com.eric.codejam.utils.Direction;
import com.eric.codejam.utils.Grid;
import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Ordering;

public class Main implements TestCaseHandler<InputData>, TestCaseInputReader<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    public int[][] genBubble2(int[] bubble) {
        int[][] genBubble = new int[DoubleRowSolver.LETTER_MAX][DoubleRowSolver.LETTER_MAX];
        
        for(int genBubbleLetter1 = 0; genBubbleLetter1 < DoubleRowSolver.LETTER_MAX; ++genBubbleLetter1) {
            for(int genBubbleLetter2 = 0; genBubbleLetter2 < DoubleRowSolver.LETTER_MAX; ++genBubbleLetter2) {
                for(int bubbleLetter = 0; bubbleLetter < DoubleRowSolver.LETTER_MAX; ++bubbleLetter) {
                    if (genBubbleLetter1 >= bubbleLetter && genBubbleLetter2 >= bubbleLetter) {
                    genBubble[genBubbleLetter1][genBubbleLetter2] += bubble[bubbleLetter];
                    }
                }   
            }
        }
        
        return genBubble;
    }
    
    public int[][] genBubbleRight(int[][] bubble) {
        int[][] bubbleGen = new int[DoubleRowSolver.LETTER_MAX][DoubleRowSolver.LETTER_MAX];
        //Upper right is first index
        for(int bubbleLetter1 = 0; bubbleLetter1 < DoubleRowSolver.LETTER_MAX; ++bubbleLetter1) {
            for(int bubbleLetter2 = 0; bubbleLetter2 < DoubleRowSolver.LETTER_MAX; ++bubbleLetter2) {
                for(int genBubbleLetter1 = 0; genBubbleLetter1 < DoubleRowSolver.LETTER_MAX; ++genBubbleLetter1) {
                    for(int genBubbleLetter2 = 0; genBubbleLetter2 < DoubleRowSolver.LETTER_MAX; ++genBubbleLetter2) {
                        if (genBubbleLetter2 >= bubbleLetter1 && 
                                genBubbleLetter2 >= bubbleLetter2 && 
                                genBubbleLetter1 >= bubbleLetter2) {
                            bubbleGen[genBubbleLetter1][genBubbleLetter2]+=bubble[bubbleLetter1][bubbleLetter2];
                        }
                    }
                }
            }
        }
        
        return bubbleGen;
    }
    
    public int[][] genBubbleDown(int[][] bubble) {
        int[][] bubbleGen = new int[DoubleRowSolver.LETTER_MAX][DoubleRowSolver.LETTER_MAX];
        
        //first index is down
        for(int bubbleLetter1 = 0; bubbleLetter1 < DoubleRowSolver.LETTER_MAX; ++bubbleLetter1) {
            for(int bubbleLetter2 = 0; bubbleLetter2 < DoubleRowSolver.LETTER_MAX; ++bubbleLetter2) {
                for(int genBubbleLetter1 = 0; genBubbleLetter1 < DoubleRowSolver.LETTER_MAX; ++genBubbleLetter1) {
                    for(int genBubbleLetter2 = 0; genBubbleLetter2 < DoubleRowSolver.LETTER_MAX; ++genBubbleLetter2) {
                        if (genBubbleLetter1 >= bubbleLetter1 && 
                                genBubbleLetter2 >= bubbleLetter1 && 
                                genBubbleLetter2 >= bubbleLetter2) {
                            bubbleGen[genBubbleLetter1][genBubbleLetter2]+=bubble[bubbleLetter1][bubbleLetter2];
                        }
                    }
                }
            }
        }
        
        return bubbleGen;
    }
    
    public int[][][] genTripBubble(int[][] bubble) {
        int[][][] bubbleGen = new int[DoubleRowSolver.LETTER_MAX][DoubleRowSolver.LETTER_MAX][DoubleRowSolver.LETTER_MAX];

        for (int bubbleLetter1 = 0; bubbleLetter1 < DoubleRowSolver.LETTER_MAX; ++bubbleLetter1) {
            for (int bubbleLetter2 = 0; bubbleLetter2 < DoubleRowSolver.LETTER_MAX; ++bubbleLetter2) {
                // for(int bubbleLetter1 = 0; bubbleLetter1 <
                // DoubleRowSolver.LETTER_MAX; ++bubbleLetter1) {

                for (int genBubbleLetter1 = 0; genBubbleLetter1 < DoubleRowSolver.LETTER_MAX; ++genBubbleLetter1) {
                    for (int genBubbleLetter2 = 0; genBubbleLetter2 < DoubleRowSolver.LETTER_MAX; ++genBubbleLetter2) {
                        for (int genBubbleLetter3 = 0; genBubbleLetter3 < DoubleRowSolver.LETTER_MAX; ++genBubbleLetter3) {
                            if (genBubbleLetter1 >= bubbleLetter1
                                    && genBubbleLetter2 >= bubbleLetter1
                                    && genBubbleLetter2 >= bubbleLetter2
                                    && genBubbleLetter3 >= bubbleLetter2) {
                                 bubbleGen[genBubbleLetter1][genBubbleLetter2][genBubbleLetter3] += bubble[bubbleLetter1][bubbleLetter2];
                                // +=bubble[bubbleTopRightLetter1][bubbleTopRightLetter2];
                            }
                        }

                    }
                }
            }
        }

        return bubbleGen;
    }
    
    public int[][] genBubbleFromTwoBubbles(int[][] bubbleBottomLeft,
            int[][] bubbleTopRight) {
        int[][] bubbleGen = new int[DoubleRowSolver.LETTER_MAX][DoubleRowSolver.LETTER_MAX];
        
        for (int bubbleBottomLeftLetter1 = 0; bubbleBottomLeftLetter1 < DoubleRowSolver.LETTER_MAX; ++bubbleBottomLeftLetter1) {
            for (int bubbleBottomLeftLetter2 = 0; bubbleBottomLeftLetter2 < DoubleRowSolver.LETTER_MAX; ++bubbleBottomLeftLetter2) {
                // for(int bubbleLetter1 = 0; bubbleLetter1 <
                // DoubleRowSolver.LETTER_MAX; ++bubbleLetter1) {
                int bubbleTopRightLetter1 = bubbleBottomLeftLetter2;
                for (int bubbleTopRightLetter2 = 0; bubbleTopRightLetter2 < DoubleRowSolver.LETTER_MAX; ++bubbleTopRightLetter2) {
                    for(int genBubbleLetter1 = 0; genBubbleLetter1 < DoubleRowSolver.LETTER_MAX; ++genBubbleLetter1) {
                        for(int genBubbleLetter2 = 0; genBubbleLetter2 < DoubleRowSolver.LETTER_MAX; ++genBubbleLetter2) {
                            if (genBubbleLetter1 >= bubbleBottomLeftLetter1 && 
                                    genBubbleLetter1 >= bubbleBottomLeftLetter2 && 
                                    genBubbleLetter2 >= bubbleTopRightLetter1 &&
                                    genBubbleLetter2 >= bubbleTopRightLetter2) {
                             //   bubbleGen[genBubbleLetter1][genBubbleLetter2]
                                        //+=bubble[bubbleTopRightLetter1][bubbleTopRightLetter2];
                            }
                        }
                    }
                }
            }
        }

        return null;
    }
    
    public int[] genFinalBubble(int[][] bubble) {
        int[] finalBubble = new int[DoubleRowSolver.LETTER_MAX];
        
        for (int bubble2Letter1 = 0; bubble2Letter1 < DoubleRowSolver.LETTER_MAX; ++bubble2Letter1) {
            for (int bubble2Letter2 = 0; bubble2Letter2 < DoubleRowSolver.LETTER_MAX; ++bubble2Letter2) {
                for (int finalBubbleLetter = 0; finalBubbleLetter < DoubleRowSolver.LETTER_MAX; ++finalBubbleLetter) {
                    if (finalBubbleLetter >= bubble2Letter1
                            && finalBubbleLetter >= bubble2Letter2) {
                        finalBubble[finalBubbleLetter] += bubble[bubble2Letter1][bubble2Letter2];
                    }
                }

            }
        }

        return finalBubble;
    }
    
    void splitBubble2_to_1(int[][] bubble, int[] bottomLeft, int[] topRight) {
        for (int bubbleLetter1 = 0; bubbleLetter1 < DoubleRowSolver.LETTER_MAX; ++bubbleLetter1) {
            for (int bubbleLetter2 = 0; bubbleLetter2 < DoubleRowSolver.LETTER_MAX; ++bubbleLetter2) {
               bottomLeft[bubbleLetter1] += bubble[bubbleLetter1][bubbleLetter2]; 
               topRight[bubbleLetter2] += bubble[bubbleLetter1][bubbleLetter2];
            }
        }
   
    }
    
    void splitBubble3_to_2(int[][][] bubble, int[][] bottomLeft, int[][] topRight) {
        for (int bubbleLetter1 = 0; bubbleLetter1 < DoubleRowSolver.LETTER_MAX; ++bubbleLetter1) {
            for (int bubbleLetter2 = 0; bubbleLetter2 < DoubleRowSolver.LETTER_MAX; ++bubbleLetter2) {
                for (int bubbleLetter3 = 0; bubbleLetter3 < DoubleRowSolver.LETTER_MAX; ++bubbleLetter3) {
               bottomLeft[bubbleLetter1][bubbleLetter2] += bubble[bubbleLetter1][bubbleLetter2][bubbleLetter3]; 
               topRight[bubbleLetter2][bubbleLetter3] += bubble[bubbleLetter1][bubbleLetter2][bubbleLetter3];
            }}
        }
   
    }
    
    
    
    public int someTest() {
        int[] startBubble = new int[DoubleRowSolver.LETTER_MAX];
        
        for(int i= 0; i < DoubleRowSolver.LETTER_MAX; ++i) {
            startBubble[i] = 1;
        }
        
        int[][] bubble2 = genBubble2(startBubble);
        
        int sum = 0;
        
        int[][][] bubble3Trip = genTripBubble(bubble2);
        
        int[] row2_col1 = new int[DoubleRowSolver.LETTER_MAX];
        int[] row1_col2 = new int[DoubleRowSolver.LETTER_MAX];
        splitBubble2_to_1(bubble2,row2_col1,row1_col2);
        
        int[][] test = genBubble2(row2_col1);
        
        int[][] test2 = genBubble2(row1_col2);
        
        int[][] sp1 = new int[DoubleRowSolver.LETTER_MAX][DoubleRowSolver.LETTER_MAX];
        int[][] sp2 = new int[DoubleRowSolver.LETTER_MAX][DoubleRowSolver.LETTER_MAX];
        
        splitBubble3_to_2(bubble3Trip, sp1, sp2);
        
 int[][] bubble3 = genBubbleRight(bubble2);
        
        int[][] bubble4 = genBubbleDown(bubble2);
        
        int[] sq6  = new int[DoubleRowSolver.LETTER_MAX];
        int[] sq5 = new int[DoubleRowSolver.LETTER_MAX];
        splitBubble2_to_1(bubble4, sq6, sq5);
        
        sum = 0;
        int sumSplit = 0;
        for (int bubbleLetter1 = 0; bubbleLetter1 < DoubleRowSolver.LETTER_MAX; ++bubbleLetter1) {
            for (int bubbleLetter2 = 0; bubbleLetter2 < DoubleRowSolver.LETTER_MAX; ++bubbleLetter2) {
                sumSplit += test[bubbleLetter1][bubbleLetter2];
                sumSplit += test2[bubbleLetter1][bubbleLetter2];
                for (int bubbleLetter3 = 0; bubbleLetter3 < DoubleRowSolver.LETTER_MAX; ++bubbleLetter3) {
                sum += bubble3Trip[bubbleLetter1][bubbleLetter2][bubbleLetter3];
                
                //sum += bubble2[bubbleLetter1][bubbleLetter2];
            }}
        }
        
        int[] twoByTwoFinal = genFinalBubble(bubble2);
        log.debug("{}", twoByTwoFinal);
        
       
        
        int[][] bubble3Inv = transpose(bubble3);
        
        sum = 0;
        for (int bubbleLetter1 = 0; bubbleLetter1 < DoubleRowSolver.LETTER_MAX; ++bubbleLetter1) {
            for (int bubbleLetter2 = 0; bubbleLetter2 < DoubleRowSolver.LETTER_MAX; ++bubbleLetter2) {
                sum += bubble3Inv[bubbleLetter1][bubbleLetter2]
                        * bubble4[bubbleLetter1][bubbleLetter2];
            }
        }
        
        sum = 0;
        for (int bubbleLetter1 = 0; bubbleLetter1 < DoubleRowSolver.LETTER_MAX; ++bubbleLetter1) {
            for (int bubbleLetter2 = 0; bubbleLetter2 < DoubleRowSolver.LETTER_MAX; ++bubbleLetter2) {
                sum += bubble3[bubbleLetter1][bubbleLetter2];
                sum += bubble4[bubbleLetter1][bubbleLetter2];
            }
        }
        
        int[] threeByTwoFinal = genFinalBubble(bubble3);
        
        sum = 0;
        for(int i = 0; i < threeByTwoFinal.length; ++i) {
            sum += threeByTwoFinal[i];
        }
        
        return sum;
    }
    
    private static int[][]  transpose (int[][] arr) {
        int[][] ret = new int[arr.length][arr[0].length];
        
        for(int i = 0; i < ret.length; ++i) {
            for(int j = 0; j < ret[0].length; ++j) {
                ret[i][j] = arr[j][i];
            }
        }
        
        return ret;
    }
    
    
    public int[][] solve1d() {
        
        //int [Dimension][Letter]
        /*
         * counts[2][24-1] means
         * 
         * w..
         * 
         * abcde
         * fghij
         * klmno
         * pqrst
         * uvwxy
         * z
         */
        int[][] counts = new int[10][26];
        
        for(int let = 1; let <= 26; ++let) {
            counts[0][26-let] = let;
        }
        
        for(int dim = 1; dim < 4; ++dim) {
            int runningSum = 0;
            for(int let = 26; let >= 1; --let) {
                runningSum += counts[dim-1][let-1];
                
                counts[dim][let-1] = runningSum;
                
        //        log.info("Running counts {}", counts[dim]);
            }
        }
        
        return counts;
    }
    
    @Override
    public String handleCase(int caseNumber, InputData input) {

       
        
        log.info("Starting calculating case {}", caseNumber);
        
        solve1d();
        
        specialCount4_5 = new int[Node.LETTER_MAX][Node.LETTER_MAX];
        int countBF = count(input.grid, new Grid<Integer>(input.grid));
        
        //int[][] count1d = solve1d();
        
        //log.info("count 1 d {}", count1d[input.grid.getCols()-2][input.grid.getEntry(0)-1]);
        
        //SingleRowSolver ss = new SingleRowSolver(input.grid);
        
        //int count = SingleRowSolver.solveGrid(input.grid);
        
        int count = 0;
        DoubleRowSolver ss = new DoubleRowSolver(input.grid);
        if (input.grid.getEntry(0, 0) == 0) {
           // count = ss.solve(0, 0, 1); // a
        } else {
            //count = ss.solve(0, 0, input.grid.getEntry(0));
        }
        //log.info("Count DP {}.  ans {}", caseNumber, countDP);

        log.info("Done calculating answer case # {}.  ans [ {} ] BF [ {} ]", caseNumber, count, countBF);
        
        int r = someTest();
        
        
        
        if (r!=3333) {
            return "Error";
        }
        //DecimalFormat decim = new DecimalFormat("0.00000000000");
        //decim.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        
        return ("Case #" + caseNumber + ": " );
    }
    
    static int[][] specialCount4_5 = new int[Node.LETTER_MAX][Node.LETTER_MAX];
    static int[][] specialCount2_4 = new int[Node.LETTER_MAX][Node.LETTER_MAX];
    static int[][] specialCount2_5 = new int[Node.LETTER_MAX][Node.LETTER_MAX];
    static int[][] specialCount3_6 = new int[Node.LETTER_MAX][Node.LETTER_MAX];
    static int[][] specialCount2_3 = new int[Node.LETTER_MAX][Node.LETTER_MAX];
    static int[][] specialCount3_5 = new int[Node.LETTER_MAX][Node.LETTER_MAX];
    static int[][] specialCount5_6 = new int[Node.LETTER_MAX][Node.LETTER_MAX];
    static int[][] specialCount4_7 = new int[Node.LETTER_MAX][Node.LETTER_MAX];
    static int[][] specialCount5_7 = new int[Node.LETTER_MAX][Node.LETTER_MAX];
    
    //Bruteforce
    public static int count(Grid<Integer> grid, Grid<Integer> gridOrig) {
        //log.info("Count grid {}", grid);
        Ordering<Integer> o = Ordering.natural().nullsFirst();
        int count = 0;
        for(int i = 0; i < grid.getSize(); ++i) {
            
            Integer current = grid.getEntry(i);
            Integer top = grid.getEntry(i, Direction.NORTH);
            Integer left = grid.getEntry(i, Direction.WEST);
            
            //A flexible spot, compute all possibilities
            if (current == 0) {
                for(int j = DoubleRowSolver.LETTER_MAX; j >= 1; --j) {
                    //Must be non decreasing
                    if (o.compare(j, top) < 0) {
                        break;
                    }
                    
                    if (o.compare(j, left) < 0) {
                        break;
                    }
                    Grid<Integer> copy = new Grid<Integer>(grid);
                    copy.setEntry(i, j);
                    count += count(copy, gridOrig);
                }
                return count;
            }
            
            //Can not decrease
            if (o.compare(current, top) < 0) {
                return 0;
            }
            
            if (o.compare(current, left) < 0) {
                return 0;
            }
        }
        
        specialCount4_5[grid.getEntry(0, 2) - 1][grid.getEntry(1,  1)-1] ++;
        specialCount2_4[grid.getEntry(0, 1) - 1][grid.getEntry(0,  2)-1] ++;
        specialCount2_5[grid.getEntry(0, 1) - 1][grid.getEntry(1,  1)-1] ++;
        specialCount3_6[grid.getEntry(1, 0) - 1][grid.getEntry(2,  0)-1] ++;
        specialCount2_3[grid.getEntry(0, 1) - 1][grid.getEntry(1,  0)-1] ++;
        specialCount3_5[grid.getEntry(1, 0) - 1][grid.getEntry(1,  1)-1] ++;
        specialCount5_6[grid.getEntry(1, 1) - 1][grid.getEntry(2,  0)-1] ++;
        specialCount4_7[grid.getEntry(0, 2) - 1][grid.getEntry(1,  2)-1] ++;
        specialCount5_7[grid.getEntry(1, 1) - 1][grid.getEntry(1,  2)-1] ++;
        //No variable squares
        return 1;
    }
    
    
    @Override
    public InputData readInput(BufferedReader br, int testCase) throws IOException {
        
    
        
        InputData  input = new InputData(testCase);
        
        Scanner ls = new Scanner(br.readLine());
       // ls.useDelimiter("");
        
        
        input.R = ls.nextInt();
        input.C = ls.nextInt();
                ls.close();
        
        BiMap<Character, Integer> chMap = HashBiMap.create();
        for(int i = 1; i <= 26; ++i) {
            chMap.put( (char) ((int) 'a' + i - 1), i);
        }
        
        chMap.put('.', 0);
        
        input.grid = Grid.buildFromBufferedReader(br, input.R, input.C, chMap, null);
        //log.info("Reading data...Test case # {} ", testCase);
        log.info("Grid {}", input.grid);
        //log.info("Done Reading data...Test case # {} ", testCase);
        
     //   String line = br.readLine();
        
       // ls.close();
        return input;
        
    }

    


    public Main() {
        super();
    }
    
    
    public static void main(String args[]) throws Exception {

        if (args.length < 1) {
           args = new String[] { "sample.txt" };
           //args = new String[] { "smallInput.txt" };
           //args = new String[] { "largeInput.txt" };
        }
        log.info("Input file {}", args[0]);

        Main m = new Main();
        //Runner.go(args[0], m, m, new InputData(-1));
        Runner.goSingleThread(args[0], m, m);
        
       
    }

    
}