package com.eric.codejam;

import org.apache.commons.math3.stat.regression.UpdatingMultipleLinearRegression;
import org.apache.commons.math3.util.MathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public class Node {
    final static Logger log = LoggerFactory.getLogger(Node.class);
    
   //aka Base, spoke, diagonal node
    boolean isBubbleNode;
    
    int nodeNumber;
    int[][] bottomWeights;
    int[][] rightWeights;
    
    int[][] prevNodeWeights;
    int[][] nextNodeWeights;
    
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
    
    enum From {
        BOTTOM,
        RIGHT,
        NEXT,
        PREV;
    }
    
    /**
     * 
     * @param prevNode upper / right
     * @param nextNode lower / left
     * @param increases
     * @param origin
     */
    public static void propogateIncrease(Node prevNode, Node nextNode, int[][] increases, From origin) {
        for (int letterFirst = 0; letterFirst < LETTER_MAX; ++letterFirst) {
            
        
        for (int letter = 0; letter < LETTER_MAX; ++letter) {

            int factor = increases[letterFirst][letter];
            if (origin != From.BOTTOM) {
               // bottomWeights[pivotLetter][letter] *= factor;
                //bottomConnectedNode.propogateIncrease(pivotLetter, factor, From.BOTTOM);
            }

            if (origin != From.RIGHT) {
                //rightWeights[pivotLetter][letter] *= factor;
                //rightConnectedNode.propogateIncrease(pivotLetter, factor, origin)
            }
            
            prevNode.nextNodeWeights[letterFirst][letter] *= factor;
            //nextNode.prevNodeWeights[letterFirst][letter] *= factor;
            
            
        }

        }
        if (origin != From.BOTTOM) {
            //bottomConnectedNode.propogateIncrease(pivotLetter, factor, From.BOTTOM);
        }

        if (origin != From.RIGHT) {
            //rightConnectedNode.propogateIncrease(pivotLetter, factor, origin)
        }
        if (origin != From.PREV ) {
            //prevConnectedNode.propogateIncrease(pivotLetter, factor, From.PREV);
            
        }
        if (origin != From.NEXT) {
            //nextNodeWeights[pivotLetter][letter] *= factor;
        }
    }

    
    public void propogateIncrease(int[] increases, From origin) {
        for (int pivotLetter = 0; pivotLetter < LETTER_MAX; ++pivotLetter) {
            int factor = increases[pivotLetter];
        for (int letter = 0; letter < LETTER_MAX; ++letter) {

            if (origin != From.BOTTOM) {
                bottomWeights[pivotLetter][letter] *= factor;
                //bottomConnectedNode.propogateIncrease(pivotLetter, factor, From.BOTTOM);
            }

            if (origin != From.RIGHT) {
                rightWeights[pivotLetter][letter] *= factor;
                //rightConnectedNode.propogateIncrease(pivotLetter, factor, origin)
            }
            if (prevConnectedNode != null && origin != From.PREV) {
               // prevNodeWeights[pivotLetter][letter] *= factor;
            }
            if ( nextConnectedNode != null && origin != From.NEXT) {
                nextNodeWeights[pivotLetter][letter] *= factor;
            }
        }

        }
        
        if (origin != From.BOTTOM) {
            //bottomConnectedNode.propogateIncrease(pivotLetter, factor, From.BOTTOM);
        }

        if (origin != From.RIGHT) {
            //rightConnectedNode.propogateIncrease(pivotLetter, factor, origin)
        }
        if (origin != From.PREV && prevConnectedNode != null) {
            prevConnectedNode.propogateIncrease(increases, From.NEXT);
            
        }
        if (origin != From.NEXT && nextConnectedNode != null) {
            //nextNodeWeights[pivotLetter][letter] *= factor;
            nextConnectedNode.propogateIncrease(increases, From.PREV);
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

        
        propogateIncrease(increases, isRight ? From.RIGHT : From.BOTTOM);
        
        
        singleNode.updateCounts();
        //updateCounts();
        
        return singleNode;

    }
    
    public static Node connectSingleNode(Node topNode, Node leftNode, int nodeNum) {
        Node singleNode = Node.createEmptyNode(nodeNum);
        
        topNode.bottomConnectedNode = singleNode;
        leftNode.rightConnectedNode = singleNode;
        
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

                        leftNode.rightWeights[leftLetter][singleNodeLetter] += leftNode.prevNodeWeights[topLetter][leftLetter];

                        topNode.bottomWeights[topLetter][singleNodeLetter] += topNode.nextNodeWeights[topLetter][leftLetter];
                        // Propogate increase
                        // topBottomWeights[topLetter][singleNodeLetter] *=
                        // count[topLetter];
                        
                        increases[topLetter][leftLetter] ++;
                    } 
                    
                    
                }
            }
        }
        
        
        Node.propogateIncrease(topNode, leftNode, increases, null);
                    

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
                totalRightWeight += rightConnectedNode.rightWeights[letterToMerge][rightLetter];
            }
            
            int totalBottomWeight = 0;
            for (int bottomLetter = 0; bottomLetter < LETTER_MAX; ++bottomLetter) {
                totalBottomWeight += bottomConnectedNode.bottomWeights[letterToMerge][bottomLetter];
            }
            
            Preconditions.checkState(totalBottomWeight == totalRightWeight);
            
            for (int rightLetter = 0; rightLetter < LETTER_MAX; ++rightLetter) {
                
                int leftRightWeight = rightConnectedNode.rightWeights[letterToMerge][rightLetter];

                for (int bottomLetter = 0; bottomLetter < LETTER_MAX; ++bottomLetter) {
                    int topBottomEdgeWeight = bottomConnectedNode.bottomWeights[letterToMerge][bottomLetter];

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
