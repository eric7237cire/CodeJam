package com.eric.codejam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.utils.Rational;
import com.google.common.base.Preconditions;

public class Node {
    final static Logger log = LoggerFactory.getLogger(Node.class);
    
   //aka Base, spoke, diagonal node
    boolean isBubbleNode;
    
    int nodeNumber;
    int[][] bottomWeights;
    int[][] rightWeights;
    
    int[][] oldNextNodeWeights;
    int[][] prevNodeWeights;
    int[][] nextNodeWeights;
    
    int[][][] prevDoubleNodeWeights;
    int[][][] nextDoubleNodeWeights;
    
    Node prevDoubleNode;
    Node nextDoubleNode;
    
    int[] count;
    
    Node rightConnectedNode;
    Node bottomConnectedNode;
    
    Node prevConnectedNode;
    Node nextConnectedNode;
    
    public static int LETTER_MAX = DoubleRowSolver.LETTER_MAX;

    public Node(int nodeNumber) {
        super();
        this.nodeNumber = nodeNumber;
        
        
        bottomWeights = new int[LETTER_MAX][LETTER_MAX];
        rightWeights = new int[LETTER_MAX][LETTER_MAX];
        nextNodeWeights = new int[LETTER_MAX][LETTER_MAX];
        prevNodeWeights = new int[LETTER_MAX][LETTER_MAX];
        
        count = new int[LETTER_MAX];
        
        isBubbleNode = false;
    }
    
    public int getCount() {
        int sum = 0;
        for(int letter = 0; letter < LETTER_MAX; ++letter) {
            sum += count[letter];
        }
        return sum;
    }
    
    public void printDiagonalWeights() {
        for (int letter = 0; letter < LETTER_MAX; ++letter) {

            for (int otherLetter = 0; otherLetter < LETTER_MAX; ++otherLetter) {
                if (prevNodeWeights != null) {
                    log.info("Letter {}  top right {}  weight {}", letter,
                            otherLetter, prevNodeWeights[letter][otherLetter]);

                }
                if (nextNodeWeights != null) {
                    log.info("Letter {}  top right {}  weight {}", letter,
                            otherLetter, nextNodeWeights[letter][otherLetter]);
                }
            }

        }
    }
    
    
    public static int BOTTOM = 1;
    public static int RIGHT = 2;
    public static int NEXT = 4;
    public static int PREV = 8;
    
    /**
     * 
     * @param prevNode upper / right
     * @param nextNode lower / left
     * @param increases
     * @param origin
     */
    public static void propogateIncrease(Node prevNode, Node nextNode, int[][] increases) {
        
        int[] oldTopRightCount = new int[LETTER_MAX];
        int[] oldBottomLeftCount = new int[LETTER_MAX];
        for (int topRightLetter = 0; topRightLetter < LETTER_MAX; ++topRightLetter) {
            
            
            for (int bottomLeftLetter = 0; bottomLeftLetter < LETTER_MAX; ++bottomLeftLetter) {
                oldTopRightCount[topRightLetter] += prevNode.nextNodeWeights[topRightLetter][bottomLeftLetter];
                
                oldBottomLeftCount[bottomLeftLetter]  += prevNode.nextNodeWeights[topRightLetter][bottomLeftLetter];
            }
        }

        for (int letterFirst = 0; letterFirst < LETTER_MAX; ++letterFirst) {

            for (int letter = 0; letter < LETTER_MAX; ++letter) {

                int factor = increases[letterFirst][letter];

                prevNode.nextNodeWeights[letterFirst][letter] *= factor;
                // nextNode.prevNodeWeights[letterFirst][letter] *= factor;

            }

        }
        
        int[] newTopRightCount = new int[LETTER_MAX];
        int[] newBottomLeftCount = new int[LETTER_MAX];
        for (int topRightLetter = 0; topRightLetter < LETTER_MAX; ++topRightLetter) {
            
            
            for (int bottomLeftLetter = 0; bottomLeftLetter < LETTER_MAX; ++bottomLeftLetter) {
                newTopRightCount[topRightLetter] += prevNode.nextNodeWeights[topRightLetter][bottomLeftLetter];
                
                newBottomLeftCount[bottomLeftLetter]  += prevNode.nextNodeWeights[topRightLetter][bottomLeftLetter];
            }
        }

        
        Rational[] incRatTopRight = new Rational[LETTER_MAX];
        for (int letter = 0; letter < LETTER_MAX; ++letter) {
            incRatTopRight[letter] = new Rational(newTopRightCount[letter], oldTopRightCount[letter]);
        }
        
        prevNode.propogateIncrease(incRatTopRight, BOTTOM | NEXT);
        
        Rational[] incRatBottomLeft = new Rational[LETTER_MAX];
        for (int letter = 0; letter < LETTER_MAX; ++letter) {
            incRatBottomLeft[letter] = new Rational(newBottomLeftCount[letter], oldBottomLeftCount[letter]);
        }
        
        nextNode.propogateIncrease(incRatBottomLeft, RIGHT | PREV);
    }

    
    public void propogateIncrease(Rational[] increases, int origin) {
        
        int[] oldTopRightCount = new int[LETTER_MAX];
        int[] oldBottomLeftCount = new int[LETTER_MAX];

        if (prevConnectedNode != null && (origin & PREV) == 0) {
            prevConnectedNode.oldNextNodeWeights = copyArray(prevConnectedNode.nextNodeWeights);
            
            for (int topRightLetter = 0; topRightLetter < LETTER_MAX; ++topRightLetter) {

                for (int bottomLeftLetter = 0; bottomLeftLetter < LETTER_MAX; ++bottomLeftLetter) {
                    oldTopRightCount[topRightLetter] += prevConnectedNode.nextNodeWeights[topRightLetter][bottomLeftLetter];

                    oldBottomLeftCount[bottomLeftLetter] += prevConnectedNode.nextNodeWeights[topRightLetter][bottomLeftLetter];
                }
            }
        }
        
        for (int increasedLetter = 0; increasedLetter < LETTER_MAX; ++increasedLetter) {
            Rational factor = increases[increasedLetter];
            for (int secondLetter = 0; secondLetter < LETTER_MAX; ++secondLetter) {

                
                
                /*
                if ((origin & NEXT) == 0 && nextDoubleNode != null) {
                    for (int thirdLetter = 0; thirdLetter < LETTER_MAX; ++thirdLetter) {
                        Rational testR = null; 
                        nextDoubleNodeWeights[increasedLetter][secondLetter][thirdLetter]
                                = factor.multiplyToInt(nextDoubleNodeWeights[increasedLetter][secondLetter][thirdLetter]);
                    }
                }*/
                
                if ((origin & BOTTOM) == 0 && nextDoubleNode == null) {
                    bottomWeights[increasedLetter][secondLetter] = factor
                            .multiplyToInt(bottomWeights[increasedLetter][secondLetter]);
                    // bottomConnectedNode.propogateIncrease(pivotLetter,
                    // factor, From.BOTTOM);
                }

                if ((origin & RIGHT) == 0 && prevDoubleNode == null) {
                    rightWeights[increasedLetter][secondLetter] = factor
                            .multiplyToInt(rightWeights[increasedLetter][secondLetter]);
                    // rightConnectedNode.propogateIncrease(pivotLetter, factor,
                    // origin)
                }
                if (prevConnectedNode != null && (origin & PREV) == 0) {
                    // increasedLetter is the 2nd dimension in prevNodeWeights
                    prevNodeWeights[secondLetter][increasedLetter] = factor
                            .multiplyToInt(prevNodeWeights[secondLetter][increasedLetter]);
                }
                if (nextConnectedNode != null && (origin & NEXT) == 0) {
                    nextNodeWeights[increasedLetter][secondLetter] = factor
                            .multiplyToInt(nextNodeWeights[increasedLetter][secondLetter]);
                }
            }

        }
        
     
        int[] newTopRightCount = new int[LETTER_MAX];
        int[] newBottomLeftCount = new int[LETTER_MAX];
        if (prevConnectedNode != null) {

            if (prevConnectedNode.nextDoubleNode != null) {
            for (int increasedLetter = 0; increasedLetter < LETTER_MAX; ++increasedLetter) {
                Rational factor = increases[increasedLetter];
                for (int secondLetter = 0; secondLetter < LETTER_MAX; ++secondLetter) {

                    if ((origin & PREV) == 0 && prevDoubleNode != null) {
                        for (int thirdLetter = 0; thirdLetter < LETTER_MAX; ++thirdLetter) {
                            Rational testR = new Rational(
                                    prevConnectedNode.nextNodeWeights[increasedLetter][secondLetter],
                                    prevConnectedNode.oldNextNodeWeights[increasedLetter][secondLetter]);

                            prevConnectedNode.nextDoubleNodeWeights[increasedLetter][secondLetter][thirdLetter] = testR
                                    .multiplyToInt(prevConnectedNode.nextDoubleNodeWeights[increasedLetter][secondLetter][thirdLetter]);
                        }
                    }
                }
            }
            
            updateVerticalHorizontalWeightsFromDoubleNodes(prevConnectedNode, this);
            }

        for (int topRightLetter = 0; topRightLetter < LETTER_MAX; ++topRightLetter) {
            
            
            for (int bottomLeftLetter = 0; bottomLeftLetter < LETTER_MAX; ++bottomLeftLetter) {
                newTopRightCount[topRightLetter] += prevConnectedNode.nextNodeWeights[topRightLetter][bottomLeftLetter];
                
                newBottomLeftCount[bottomLeftLetter]  += prevConnectedNode.nextNodeWeights[topRightLetter][bottomLeftLetter];
            }
        }
        }
        
        Rational[] incRatTopRight = new Rational[LETTER_MAX];
        for (int letter = 0; letter < LETTER_MAX; ++letter) {
            incRatTopRight[letter] = new Rational(newTopRightCount[letter], oldTopRightCount[letter]);
        }
        
        Rational[] incRatBottomLeft = new Rational[LETTER_MAX];
        for (int letter = 0; letter < LETTER_MAX; ++letter) {
            incRatBottomLeft[letter] = new Rational(newBottomLeftCount[letter], oldBottomLeftCount[letter]);
        }
        
        if ( (origin & PREV) == 0 && prevConnectedNode != null) {
            prevConnectedNode.propogateIncrease(incRatTopRight, NEXT);
            
        }
        if ( (origin & NEXT) == 0 && nextConnectedNode != null) {
            //nextNodeWeights[pivotLetter][letter] *= factor;
            nextConnectedNode.propogateIncrease(increases, PREV);
        }
        
        updateCounts();
    }
    
    public Node connectSingleNode(int nodeNum, boolean isRight) {
        Node singleNode = Node.createEmptyNode(nodeNum);
        
        if (isRight) {
            this.rightConnectedNode = singleNode;
            singleNode.rightConnectedNode = this;
            singleNode.rightWeights = rightWeights;
        } else {
            this.bottomConnectedNode = singleNode;
            singleNode.bottomConnectedNode = this;
            singleNode.bottomWeights = bottomWeights;
        }
        
        int[] increases = new int[LETTER_MAX];
        for (int letter = 0; letter < LETTER_MAX; ++letter) {

            for (int singleNodeLetter = 0; singleNodeLetter < LETTER_MAX; ++singleNodeLetter) {
                if (singleNodeLetter >= letter) {
                    if (isRight) {
                        rightWeights[letter][singleNodeLetter] = count[letter];
                        
                        
                        //topBottomWeights[letter][singleNodeLetter] *= count[letter];
                    } else {
                        bottomWeights[letter][singleNodeLetter] = count[letter];
                    }
                    
                  //Propogate increase
                    increases[letter] += 1;
                }
            }
        }

        Rational[] incRat = new Rational[LETTER_MAX];
        for (int letter = 0; letter < LETTER_MAX; ++letter) {
            incRat[letter] = Rational.fromInt(increases[letter]);
        }
        propogateIncrease(incRat, isRight ? RIGHT : BOTTOM);
        
        
        singleNode.updateCounts();
        //updateCounts();
        
        return singleNode;

    }
    
    public static int[][] copyArray(int[][] matrix) {
        int [][] myInt = new int[matrix.length][matrix.length];
        for(int i = 0; i < matrix.length; i++)
        {
            for(int j  = 0; j < matrix.length; ++j) {
                myInt[i][j] = matrix[i][j];
            }
        }
        
        
        return myInt;
    }
    
    public static void updateVerticalHorizontalWeightsFromDoubleNodes(Node topNode, Node leftNode) {
        topNode.bottomWeights = new int[LETTER_MAX][LETTER_MAX];
        leftNode.rightWeights = new int[LETTER_MAX][LETTER_MAX];
        
        topNode.nextDoubleNode.bottomWeights = topNode.bottomWeights;
        topNode.nextDoubleNode.rightWeights = leftNode.rightWeights;
        
        for (int leftLetter = 0; leftLetter < LETTER_MAX; ++leftLetter) {
            for (int topLetter = 0; topLetter < LETTER_MAX; ++topLetter) {

                for (int singleNodeLetter = 0; singleNodeLetter < LETTER_MAX; ++singleNodeLetter) {
                    topNode.bottomWeights[topLetter][singleNodeLetter] += topNode.nextDoubleNodeWeights[topLetter][leftLetter][singleNodeLetter];
                    leftNode.rightWeights[leftLetter][singleNodeLetter] += topNode.nextDoubleNodeWeights[topLetter][leftLetter][singleNodeLetter];
                }
            }
        }
    }
    
    public static Node connectSingleNode(Node topNode, Node leftNode, int nodeNum) {
        Node singleNode = Node.createEmptyNode(nodeNum);
        
        topNode.bottomConnectedNode = singleNode;
        leftNode.rightConnectedNode = singleNode;
        
        topNode.nextDoubleNode = singleNode;
        leftNode.prevDoubleNode = singleNode;
        
        int[][][] weights = new int[LETTER_MAX][LETTER_MAX][LETTER_MAX];
        topNode.nextDoubleNodeWeights = weights;
        leftNode.prevDoubleNodeWeights = weights;
        
        singleNode.rightWeights = leftNode.rightWeights;
        singleNode.bottomWeights = topNode.bottomWeights;
        
        singleNode.bottomConnectedNode = topNode;
        singleNode.rightConnectedNode = leftNode;
        
        int[][] increases = new int[LETTER_MAX][LETTER_MAX];
        
        for (int leftLetter = 0; leftLetter < LETTER_MAX; ++leftLetter) {
            for (int topLetter = 0; topLetter < LETTER_MAX; ++topLetter) {

                for (int singleNodeLetter = 0; singleNodeLetter < LETTER_MAX; ++singleNodeLetter) {
                    if (singleNodeLetter >= topLetter
                            && singleNodeLetter >= leftLetter) {

                        weights[topLetter][leftLetter][singleNodeLetter] += leftNode.prevNodeWeights[topLetter][leftLetter];
                        
                        Preconditions.checkState(leftNode.prevNodeWeights[topLetter][leftLetter] == topNode.nextNodeWeights[topLetter][leftLetter]);
                                                // Propogate increase
                        // topBottomWeights[topLetter][singleNodeLetter] *=
                        // count[topLetter];
                        
                        increases[topLetter][leftLetter] ++;
                    } 
                    
                    
                }
            }
        }
    
        updateVerticalHorizontalWeightsFromDoubleNodes(topNode, leftNode);
        
        Node.propogateIncrease(topNode, leftNode, increases);
                    

        // Update node values
        singleNode.updateCounts();
        topNode.updateCounts();
        leftNode.updateCounts();
        
        return singleNode;

    }
    
    
    private static int[] getCounts(int[][] weights) {
        int sum = 0;
        int[] counts = new int[LETTER_MAX];
    
        for (int letter = 0; letter < LETTER_MAX; ++letter) {
            int total1 = 0;
            for (int singleLetter = 0; singleLetter < LETTER_MAX; ++singleLetter) {
                
                    total1 += weights[letter][singleLetter];
                
                
            }

            counts[letter] = total1;
        }
        return counts;
    }
    private void updateCounts() {
        

        if (rightConnectedNode != null) {
            count = getCounts(rightWeights);
        } else if (bottomConnectedNode != null) {
            count = getCounts(bottomWeights);
        } else if (prevConnectedNode != null) {
            count = getCounts(prevNodeWeights);
        }else if (nextConnectedNode != null) {
            count = getCounts(nextNodeWeights);
        }
        
        
    }
    
    public static int getTotal(int[][] weights) {
        int sum = 0;
        
    
        for (int letter = 0; letter < LETTER_MAX; ++letter) {
            int total1 = 0;
            for (int singleLetter = 0; singleLetter < LETTER_MAX; ++singleLetter) {
                
                    total1 += weights[letter][singleLetter];
                
                
            }

            sum+= total1;
        }
        return sum;
    }
    
    public void mergeNode() {
        
        
        this.rightConnectedNode.nextNodeWeights = new int[LETTER_MAX][LETTER_MAX];
        this.bottomConnectedNode.prevNodeWeights  =rightConnectedNode.nextNodeWeights ;// new int[LETTER_MAX][LETTER_MAX];
        
        for (int letterToMerge = 0; letterToMerge < LETTER_MAX; ++letterToMerge) {
            
            int totalRightWeight = 0;
            for (int rightLetter = 0; rightLetter < LETTER_MAX; ++rightLetter) {
                totalRightWeight += rightWeights[letterToMerge][rightLetter];
            }
            
            int totalBottomWeight = 0;
            for (int bottomLetter = 0; bottomLetter < LETTER_MAX; ++bottomLetter) {
                totalBottomWeight += bottomWeights[letterToMerge][bottomLetter];
            }
            
            Preconditions.checkState(totalBottomWeight == totalRightWeight);
            
            for (int rightLetter = 0; rightLetter < LETTER_MAX; ++rightLetter) {
                
                int leftRightWeight = rightWeights[letterToMerge][rightLetter];

                for (int bottomLetter = 0; bottomLetter < LETTER_MAX; ++bottomLetter) {
                    int topBottomEdgeWeight = bottomWeights[letterToMerge][bottomLetter];

                    if (leftRightWeight > 0) {
                   // bottomConnectedNode.prevNodeWeights[rightLetter][bottomLetter]
                     //       += topBottomEdgeWeight * leftRightWeight / totalRightWeight;
                    
                        rightConnectedNode.nextNodeWeights[rightLetter][bottomLetter]
                            += topBottomEdgeWeight * leftRightWeight / totalRightWeight;
                    }
                }

            }
        }
        
        
        for (int letter = 0; letter < LETTER_MAX; ++letter) {
            int total = 0;
            for (int singleLetter = 0; singleLetter < LETTER_MAX; ++singleLetter) {
                
                    total += bottomConnectedNode.prevNodeWeights[letter][singleLetter];
                
                  //  total += rightConnectedNode.nextNodeWeights[letter][singleLetter];
                
            }

            count[letter] = total;    
            //TODO wrong probably
            rightConnectedNode.count[letter] = total;
            bottomConnectedNode.count[letter] = total;
        }
        
        this.rightConnectedNode.nextConnectedNode = bottomConnectedNode;
        this.bottomConnectedNode.prevConnectedNode = rightConnectedNode;
        
        
        this.rightConnectedNode.makeBubbleNode();
        
        this.bottomConnectedNode.makeBubbleNode();
    }
    
    private void makeBubbleNode() {
        bottomWeights = new int[LETTER_MAX][LETTER_MAX];
        rightWeights = new int[LETTER_MAX][LETTER_MAX];
        rightConnectedNode = null;
        bottomConnectedNode = null;
        
        this.isBubbleNode = true;
    }
    
   

    static Node createEmptyNode(int nodeNum) {
        Node n = new Node(nodeNum);
        
        return n;
    }
    
    static Node createFirstNode() {
        Node n = new Node(1);
        for(int i = 0; i < LETTER_MAX; ++i) {
            n.count[i] = 1;
        }
        
        return n;
    }
    
    
}
