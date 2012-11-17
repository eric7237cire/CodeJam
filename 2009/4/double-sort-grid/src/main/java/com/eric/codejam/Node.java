package com.eric.codejam;

import org.apache.commons.math3.stat.regression.UpdatingMultipleLinearRegression;
import org.apache.commons.math3.util.MathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public class Node {
    final static Logger log = LoggerFactory.getLogger(Node.class);
    
    static class SubNode {
        int letter;
        
        int parentNodeNumber;

        public SubNode(int letter, int parentNodeNumber) {
            super();
            this.letter = letter;
            this.parentNodeNumber = parentNodeNumber;
                    
        }
    }
    
    
    SubNode[] subNodes;
    
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
        subNodes = new SubNode[LETTER_MAX];
        
        
        bottomWeights = new int[LETTER_MAX][LETTER_MAX];
        rightWeights = new int[LETTER_MAX][LETTER_MAX];
        nextNodeWeights = new int[LETTER_MAX][LETTER_MAX];
        prevNodeWeights = new int[LETTER_MAX][LETTER_MAX];
        
        count = new int[LETTER_MAX];
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
            nextNode.prevNodeWeights[letterFirst][letter] *= factor;
            
            
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

    
    public void propogateIncrease(int pivotLetter, int factor, From origin) {
        for (int letter = 0; letter < LETTER_MAX; ++letter) {

            if (origin != From.BOTTOM) {
                bottomWeights[pivotLetter][letter] *= factor;
                //bottomConnectedNode.propogateIncrease(pivotLetter, factor, From.BOTTOM);
            }

            if (origin != From.RIGHT) {
                rightWeights[pivotLetter][letter] *= factor;
                //rightConnectedNode.propogateIncrease(pivotLetter, factor, origin)
            }
            if (prevNodeWeights != null && origin != From.PREV) {
                prevNodeWeights[pivotLetter][letter] *= factor;
            }
            if (nextNodeWeights != null && origin != From.NEXT) {
                nextNodeWeights[pivotLetter][letter] *= factor;
            }
        }

        if (origin != From.BOTTOM) {
            //bottomConnectedNode.propogateIncrease(pivotLetter, factor, From.BOTTOM);
        }

        if (origin != From.RIGHT) {
            //rightConnectedNode.propogateIncrease(pivotLetter, factor, origin)
        }
        if (origin != From.PREV && prevConnectedNode != null) {
            prevConnectedNode.propogateIncrease(pivotLetter, factor, From.PREV);
            
        }
        if (origin != From.NEXT) {
            //nextNodeWeights[pivotLetter][letter] *= factor;
        }
    }
    
    public Node connectSingleNode(int nodeNum, boolean isRight) {
        Node singleNode = Node.createEmptyNode(nodeNum);
        
        if (isRight) {
            this.rightConnectedNode = singleNode;
            singleNode.rightWeights = rightWeights;
        } else {
            this.bottomConnectedNode = singleNode;
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
                        
                      //Propogate increase
                        //leftRightWeights[letter][singleNodeLetter] *= count[letter];
                        //increases[letter] += count[letter];
                    }
                    
                  //Propogate increase
                    increases[letter] += 1;
                }
            }
        }

        for (int letter = 0; letter < LETTER_MAX; ++letter) {
        propogateIncrease(letter, increases[letter], isRight ? From.RIGHT : From.BOTTOM);
        }
        
        singleNode.updateCounts();
        updateCounts();
        
        return singleNode;

    }
    
    public static Node connectSingleNode(Node topNode, Node leftNode, int nodeNum) {
        Node singleNode = Node.createEmptyNode(nodeNum);
        
        topNode.bottomConnectedNode = singleNode;
        leftNode.rightConnectedNode = singleNode;
        
        singleNode.rightWeights = leftNode.rightWeights;
        singleNode.bottomWeights = topNode.bottomWeights;
        
        int[][] increases = new int[LETTER_MAX][LETTER_MAX];
        
        for (int leftLetter = 0; leftLetter < LETTER_MAX; ++leftLetter) {
            for (int topLetter = 0; topLetter < LETTER_MAX; ++topLetter) {

                for (int singleNodeLetter = 0; singleNodeLetter < LETTER_MAX; ++singleNodeLetter) {
                    if (singleNodeLetter >= topLetter
                            && singleNodeLetter >= leftLetter) {

                        leftNode.rightWeights[leftLetter][singleNodeLetter] += leftNode.prevNodeWeights[topLetter][leftLetter];

                        //topNode.topBottomWeights[topLetter][singleNodeLetter] += topNode.nextNodeWeights[topLetter][leftLetter];
                        // Propogate increase
                        // topBottomWeights[topLetter][singleNodeLetter] *=
                        // count[topLetter];
                        
                        increases[topLetter][leftLetter] ++;
                    } 
                    
                    
                }
            }
        }
        
        
        Node.propogateIncrease(topNode, leftNode, increases, From.RIGHT);
                    

        // Update node values
        singleNode.updateCounts();
        topNode.updateCounts();
        leftNode.updateCounts();
        
        return singleNode;

    }
    
    private void updateCounts() {
        

        for (int letter = 0; letter < LETTER_MAX; ++letter) {
            int total1 = 0, total2 = 0, total3 = 0, total4 = 0;
            for (int singleLetter = 0; singleLetter < LETTER_MAX; ++singleLetter) {
                
                    total1 += rightWeights[letter][singleLetter];
                
                    total2 += bottomWeights[letter][singleLetter];
                    
                    total3 += nextNodeWeights[letter][singleLetter];
                    
                    total4 += prevNodeWeights[letter][singleLetter];
                
            }

            count[letter] = Math.max(Math.max(Math.max(total1, total2), total3), total4);
            
            
            
        }
        
    }
    
    public void mergeNode() {
        
        
        this.rightConnectedNode.nextNodeWeights = new int[LETTER_MAX][LETTER_MAX];
        this.bottomConnectedNode.prevNodeWeights  = new int[LETTER_MAX][LETTER_MAX];
        
        for (int letterToMerge = 0; letterToMerge < LETTER_MAX; ++letterToMerge) {
            for (int rightLetter = 0; rightLetter < LETTER_MAX; ++rightLetter) {
                
                int leftRightWeight = rightConnectedNode.rightWeights[letterToMerge][rightLetter];

                for (int bottomLetter = 0; bottomLetter < LETTER_MAX; ++bottomLetter) {
                    int topBottomEdgeWeight = bottomConnectedNode.bottomWeights[letterToMerge][bottomLetter];

                    if (leftRightWeight > 0) {
                    bottomConnectedNode.prevNodeWeights[rightLetter][bottomLetter]
                            += topBottomEdgeWeight / leftRightWeight;
                    
                    rightConnectedNode.nextNodeWeights[rightLetter][bottomLetter]
                            += topBottomEdgeWeight / leftRightWeight;
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
        
        this.rightConnectedNode.rightWeights = new int[LETTER_MAX][LETTER_MAX];
        this.bottomConnectedNode.bottomWeights = new int[LETTER_MAX][LETTER_MAX];
    }
    
   

    static Node createEmptyNode(int nodeNum) {
        Node n = new Node(nodeNum);
        for(int i = 0; i < LETTER_MAX; ++i) {
            n.subNodes[i] = new SubNode(i, n.nodeNumber);
        }
        
        return n;
    }
    
    static Node createFirstNode() {
        Node n = new Node(1);
        for(int i = 0; i < LETTER_MAX; ++i) {
            n.subNodes[i] = new SubNode(i, n.nodeNumber);
            n.count[i] = 1;
        }
        
        return n;
    }
    
    
}
