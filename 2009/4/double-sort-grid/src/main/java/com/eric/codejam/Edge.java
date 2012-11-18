package com.eric.codejam;

import com.google.common.base.Objects;

public class Edge {
    //L to R, then T to B
    Node firstNode;
    Node secondNode;
    
    int firstLetter;
    int secondLetter;
    
    int originalWeight;

    public Edge(Node firstNode, Node secondNode, int firstLetter, int secondLetter, int originalWeight) {
        super();
        this.firstNode = firstNode;
        this.secondNode = secondNode;
        this.firstLetter = firstLetter;
        this.secondLetter = secondLetter;
        this.originalWeight = originalWeight;
    }
    
    public void increaseWeight(int mult) {
  
        if (secondNode.equals(firstNode.rightConnectedNode)) {
            firstNode.rightWeights[firstLetter][secondLetter] += 
                     mult * originalWeight;
            return;
//            secondNode.leftWeights[firstLetter][secondLetter] 
  //                  = res * secondNode.leftWeights[firstLetter][secondLetter] + mult * originalWeight;
        } else if (secondNode.equals(firstNode.bottomConnectedNode)) {
            firstNode.bottomWeights[firstLetter][secondLetter] = 
                    
                    mult * originalWeight;
            //secondNode.leftWeights[firstLetter][secondLetter] *= mult;
            return;
        }
        
        throw new RuntimeException("hmm");
    }
    
    public void addWeight(int toAdd) {
        
        if (secondNode.equals(firstNode.rightConnectedNode)) {
            firstNode.rightWeights[firstLetter][secondLetter] += 
                     toAdd;
            return;
//            secondNode.leftWeights[firstLetter][secondLetter] 
  //                  = res * secondNode.leftWeights[firstLetter][secondLetter] + mult * originalWeight;
        } else if (secondNode.equals(firstNode.bottomConnectedNode)) {
            firstNode.bottomWeights[firstLetter][secondLetter] += 
                    
                    toAdd;
            //secondNode.leftWeights[firstLetter][secondLetter] *= mult;
            return;
        }
        
        throw new RuntimeException("hmm");
    }

    @Override
    public String toString() {
        return "Edge [firstNode=" + firstNode + ", secondNode=" + secondNode
                + ", firstLetter=" + firstLetter + ", secondLetter="
                + secondLetter + ", originalWeight=" + originalWeight + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(firstNode.nodeNumber, secondNode.nodeNumber, firstLetter, secondLetter);

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Edge other = (Edge) obj;
        return Objects.equal(firstLetter, other.firstLetter)
                && Objects.equal(firstNode.nodeNumber, other.firstNode.nodeNumber)
                && Objects.equal(secondLetter, other.secondLetter)
                && Objects.equal(firstNode.nodeNumber, other.firstNode.nodeNumber);

    }
    
    
    
}
