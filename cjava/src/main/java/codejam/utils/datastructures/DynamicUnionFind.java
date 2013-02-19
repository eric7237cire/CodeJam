package codejam.utils.datastructures;

import java.util.BitSet;


import com.google.common.base.Preconditions;

public class DynamicUnionFind
{
    public static class Component
    {
        int id;
        
        BitSet members;
        int size;
        Component(int id) {
            this.id = id;
            members = new BitSet();
            attrs = new BitSetInt(0);
        }
        
        public BitSetInt attrs;
    }

    /**
     * Component id
     */
    int componentCount;
    
    public DynamicUnionFind(int maxNodeId) {
        memberToComponent = new Component[maxNodeId+1];         
    }
    
    
    Component[] memberToComponent;
    Component[] components;

    public void addNode(int nodeId) {
        
        Component com = new Component(nodeId);
        com.members.set(nodeId);
        com.size = 1;
        
        memberToComponent[nodeId] = com;
    }
    
    
    public void mergeComponentsOfNodes(int node1, int node2) {
        Component com1 = memberToComponent[node1];
        Component com2 = memberToComponent[node2];
        
        Preconditions.checkArgument(com1 != null && com2 != null);
        
        if (com1 == com2) {
            //already merged
            return;
        }
        
        Component newComponent = new Component(com1.id);
        
        for(int i=com1.members.nextSetBit(0); i>=0; 
                i=com1.members.nextSetBit(i+1)) {
          
            Preconditions.checkState(memberToComponent[i] == com1);
            
            memberToComponent[i] = newComponent;
        }
        
        for(int i=com2.members.nextSetBit(0); i>=0; 
                i=com2.members.nextSetBit(i+1)) {
                       
            Preconditions.checkState(memberToComponent[i] == com2);
            
            memberToComponent[i] = newComponent;
        }
        
        newComponent.members.or(com1.members);
        newComponent.members.or(com2.members);
        newComponent.attrs.setAllBits( com1.attrs.getBits() | com2.attrs.getBits() );
        
        newComponent.size = newComponent.members.cardinality();
    }
    
    public Integer getGroupNumber(int node1) {
        Component com = memberToComponent[node1];
        
        if (com == null)
            return null;
        
        return com.id;
    }
    
    public Component getGroup(int node) {
        return memberToComponent[node];
    }
}
