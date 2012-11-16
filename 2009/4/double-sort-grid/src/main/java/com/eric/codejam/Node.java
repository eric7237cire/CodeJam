package com.eric.codejam;

public class Node {
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
    
    int[] count;
    
    int rightLeftConnectedNode;
    int topBottomConnectedNode;
    
    public static int LETTER_MAX = DoubleRowSolver.LETTER_MAX;

    public Node(int nodeNumber) {
        super();
        this.nodeNumber = nodeNumber;
        subNodes = new SubNode[LETTER_MAX];
        
        topBottomConnectedNode = -1;
        rightLeftConnectedNode = -1;
        
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
    
    public Node connectSingleNode(int nodeNum, boolean isRight) {
        Node singleNode = Node.createEmptyNode(nodeNum);
        for (int letter = 0; letter < LETTER_MAX; ++letter) {

            for (int singleNodeLetter = 0; singleNodeLetter < LETTER_MAX; ++singleNodeLetter) {
                if (singleNodeLetter >= letter) {
                    if (isRight) {
                    singleNode.leftRightWeights[singleNodeLetter][letter] = count[letter];
                    leftRightWeights[letter][singleNodeLetter] = count[letter];
                    } else {
                        singleNode.topBottomWeights[singleNodeLetter][letter] = count[letter];
                        topBottomWeights[letter][singleNodeLetter] = count[letter];
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
        }
        
        return singleNode;

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
