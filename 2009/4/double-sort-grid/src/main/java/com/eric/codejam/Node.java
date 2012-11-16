package com.eric.codejam;

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
    int[][] topBottomWeights;
    int[][] leftRightWeights;
    
    int[][] prevNodeWeights;
    int[][] nextNodeWeights;
    
    int[] count;
    
    Node rightConnectedNode;
    Node bottomConnectedNode;
    
    public static int LETTER_MAX = DoubleRowSolver.LETTER_MAX;

    public Node(int nodeNumber) {
        super();
        this.nodeNumber = nodeNumber;
        subNodes = new SubNode[LETTER_MAX];
        
        
        topBottomWeights = new int[LETTER_MAX][LETTER_MAX];
        leftRightWeights = new int[LETTER_MAX][LETTER_MAX];
        
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
    
    public Node connectSingleNode(int nodeNum, boolean isRight) {
        Node singleNode = Node.createEmptyNode(nodeNum);
        
        if (isRight) {
            this.rightConnectedNode = singleNode;
            singleNode.leftRightWeights = leftRightWeights;
        } else {
            this.bottomConnectedNode = singleNode;
            singleNode.topBottomWeights = topBottomWeights;
        }
        
        for (int letter = 0; letter < LETTER_MAX; ++letter) {

            for (int singleNodeLetter = 0; singleNodeLetter < LETTER_MAX; ++singleNodeLetter) {
                if (singleNodeLetter >= letter) {
                    if (isRight) {
                        leftRightWeights[letter][singleNodeLetter] = count[letter];
                        
                        //Propogate increase
                        topBottomWeights[letter][singleNodeLetter] *= count[letter];
                    } else {
                        topBottomWeights[letter][singleNodeLetter] = count[letter];
                        
                      //Propogate increase
                        leftRightWeights[letter][singleNodeLetter] *= count[letter];
                    }
                }
            }
        }

        // Update node values
        int total = 0;

        for (int letter = 0; letter < LETTER_MAX; ++letter) {
            total = 0;
            for (int singleLetter = 0; singleLetter < LETTER_MAX; ++singleLetter) {
                if (isRight) {
                    total += leftRightWeights[letter][singleLetter];
                } else {
                    total += topBottomWeights[letter][singleLetter];
                }
            }

            count[letter] = total;
            
            for (int singleLetter = 0; singleLetter < LETTER_MAX; ++singleLetter) {
                //Update other connections weights
                
                if (!isRight ) {
                   // leftRightWeights[letter][singleLetter] = count[letter] * leftRightWeights [letter][singleLetter];
                } else {
                  //  topBottomWeights[letter][singleLetter]  = count[letter] * topBottomWeights [letter][singleLetter];
                }
            }
        }
        
        return singleNode;

    }
    
    public static Node connectSingleNode(Node topNode, Node leftNode, int nodeNum) {
        Node singleNode = Node.createEmptyNode(nodeNum);
        
        topNode.bottomConnectedNode = singleNode;
        leftNode.rightConnectedNode = singleNode;
        
        singleNode.leftRightWeights = leftNode.leftRightWeights;
        singleNode.topBottomWeights = topNode.topBottomWeights;
        
        for (int leftLetter = 0; leftLetter < LETTER_MAX; ++leftLetter) {
            for (int topLetter = 0; topLetter < LETTER_MAX; ++topLetter) {

                for (int singleNodeLetter = 0; singleNodeLetter < LETTER_MAX; ++singleNodeLetter) {
                    if (singleNodeLetter >= topLetter
                            && singleNodeLetter >= leftLetter) {

                        leftNode.leftRightWeights[leftLetter][singleNodeLetter] += leftNode.prevNodeWeights[topLetter][leftLetter];

                        topNode.topBottomWeights[topLetter][singleNodeLetter] += topNode.nextNodeWeights[topLetter][leftLetter];
                        // Propogate increase
                        // topBottomWeights[topLetter][singleNodeLetter] *=
                        // count[topLetter];
                    } else {
                       

                        // Propogate increase
                        // leftRightWeights[topLetter][singleNodeLetter] *=
                        // count[topLetter];
                    }
                }
            }
        }

        // Update node values
        int total = 0;

        for (int letter = 0; letter < LETTER_MAX; ++letter) {
            total = 0;
            for (int singleLetter = 0; singleLetter < LETTER_MAX; ++singleLetter) {
                
                    total += singleNode.leftRightWeights[letter][singleLetter];
                
                    total += singleNode.topBottomWeights[letter][singleLetter];
                
            }

            singleNode.count[letter] = total;
            
        }
        
        return singleNode;

    }
    
    public void mergeNode() {
        
        
        this.rightConnectedNode.nextNodeWeights = new int[LETTER_MAX][LETTER_MAX];
        this.bottomConnectedNode.prevNodeWeights  = new int[LETTER_MAX][LETTER_MAX];
        
        for (int letterToMerge = 0; letterToMerge < LETTER_MAX; ++letterToMerge) {
            for (int rightLetter = 0; rightLetter < LETTER_MAX; ++rightLetter) {
                
                int leftRightWeight = rightConnectedNode.leftRightWeights[letterToMerge][rightLetter];

                for (int bottomLetter = 0; bottomLetter < LETTER_MAX; ++bottomLetter) {
                    int topBottomEdgeWeight = bottomConnectedNode.topBottomWeights[letterToMerge][bottomLetter];

                    if (leftRightWeight > 0) {
                    bottomConnectedNode.prevNodeWeights[bottomLetter][rightLetter]
                            += topBottomEdgeWeight / leftRightWeight;
                    
                    rightConnectedNode.nextNodeWeights[bottomLetter][rightLetter]
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
        
        this.rightConnectedNode.leftRightWeights = new int[LETTER_MAX][LETTER_MAX];
        this.bottomConnectedNode.topBottomWeights = new int[LETTER_MAX][LETTER_MAX];
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
