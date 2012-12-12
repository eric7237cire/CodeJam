package codejam.utils;

import org.junit.Test;

import codejam.utils.datastructures.TreeInt;
import codejam.utils.datastructures.TreeInt.Node;

import static org.junit.Assert.*;


public class TreeTest {
    @Test
    public void testTreeIntBasic() {
        TreeInt tree = new TreeInt(5);
        
        tree.getRoot().addChild(6);
        tree.getRoot().addChild(4);
        Node node3 = tree.getRoot().addChild(3);
        node3.addChild(2);
        node3.addChild(1);

        assertEquals(1, tree.getNodes().get(1).getHeight() );
        assertEquals(1, tree.getNodes().get(1).getSize() );
        
        assertEquals(1, tree.getNodes().get(2).getHeight() );
        assertEquals(1, tree.getNodes().get(2).getSize() );
        
        assertEquals(1, tree.getNodes().get(4).getHeight() );
        assertEquals(1, tree.getNodes().get(4).getSize() );
        
        assertEquals(1, tree.getNodes().get(6).getHeight() );
        assertEquals(1, tree.getNodes().get(6).getSize() );
        
        assertEquals(2, tree.getNodes().get(3).getHeight() );
        assertEquals(3, tree.getNodes().get(3).getSize() );
        
        assertEquals(3, tree.getNodes().get(5).getHeight() );
        assertEquals(6, tree.getNodes().get(5).getSize() );
        
        // 1
        //2 3
        // 4   5
        // 6    7   8
        //9 10      11
        //           12
        
        tree = new TreeInt(1);
        tree.getNodes().get(1).addChild(2);
        tree.getNodes().get(1).addChild(3);
        
        assertEquals(2, tree.getNodes().get(1).getHeight() );
        assertEquals(3, tree.getNodes().get(1).getSize() );
        
        tree.getNodes().get(3).addChild(4);
        tree.getNodes().get(3).addChild(5);
        
        assertEquals(3, tree.getNodes().get(1).getHeight() );
        assertEquals(5, tree.getNodes().get(1).getSize() );
        
        tree.getNodes().get(4).addChild(6);
        tree.getNodes().get(4).addChild(7);
        tree.getNodes().get(4).addChild(8);
        
        assertEquals(4, tree.getNodes().get(1).getHeight() );
        assertEquals(8, tree.getNodes().get(1).getSize() );
        
        tree.getNodes().get(6).addChild(9);
        tree.getNodes().get(6).addChild(10);
        
        assertEquals(5, tree.getNodes().get(1).getHeight() );
        assertEquals(10, tree.getNodes().get(1).getSize() );
        
        
        tree.getNodes().get(8).addChild(11);
        tree.getNodes().get(11).addChild(12);
        
        assertEquals(6, tree.getNodes().get(1).getHeight() );
        assertEquals(12, tree.getNodes().get(1).getSize() );
        
        TreeInt reRooted = tree.reroot(8);
        
        int nodeNum = 1;
        //1
        assertEquals(2, reRooted.getNodes().get(nodeNum).getHeight() );
        assertEquals(2, reRooted.getNodes().get(nodeNum++).getSize() );
        
        //2
        assertEquals(1, reRooted.getNodes().get(nodeNum).getHeight() );
        assertEquals(1, reRooted.getNodes().get(nodeNum++).getSize() );
        
        //3
        assertEquals(3, reRooted.getNodes().get(nodeNum).getHeight() );
        assertEquals(4, reRooted.getNodes().get(nodeNum++).getSize() );
        
        //4
        assertEquals(4, reRooted.getNodes().get(nodeNum).getHeight() );
        assertEquals(9, reRooted.getNodes().get(nodeNum++).getSize() );
        
        //5
        assertEquals(1, reRooted.getNodes().get(nodeNum).getHeight() );
        assertEquals(1, reRooted.getNodes().get(nodeNum++).getSize() );
        
        //6
        assertEquals(2, reRooted.getNodes().get(nodeNum).getHeight() );
        assertEquals(3, reRooted.getNodes().get(nodeNum++).getSize() );
        
        //7
        assertEquals(1, reRooted.getNodes().get(nodeNum).getHeight() );
        assertEquals(1, reRooted.getNodes().get(nodeNum++).getSize() );
        
        //8
        assertEquals(5, reRooted.getNodes().get(nodeNum).getHeight() );
        assertEquals(12, reRooted.getNodes().get(nodeNum++).getSize() );
        
        //9
        assertEquals(1, reRooted.getNodes().get(nodeNum).getHeight() );
        assertEquals(1, reRooted.getNodes().get(nodeNum++).getSize() );
        
        //10
        assertEquals(1, reRooted.getNodes().get(nodeNum).getHeight() );
        assertEquals(1, reRooted.getNodes().get(nodeNum++).getSize() );
        
        //11
        assertEquals(2, reRooted.getNodes().get(nodeNum).getHeight() );
        assertEquals(2, reRooted.getNodes().get(nodeNum++).getSize() );
        
        //12
        assertEquals(1, reRooted.getNodes().get(nodeNum).getHeight() );
        assertEquals(1, reRooted.getNodes().get(nodeNum++).getSize() );
        
    }
}
