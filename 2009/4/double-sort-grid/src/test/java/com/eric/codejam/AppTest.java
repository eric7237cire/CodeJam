package com.eric.codejam;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.test.TesterBase;



/**
 * Unit test for simple App.
 */
public class AppTest extends TesterBase {

    final static Logger log = LoggerFactory.getLogger(AppTest.class);

    /**
     * Create the test case
     * 
     * @param testName
     *            name of the test case
     */
    public AppTest() {
        super();
    }

    @Override
    protected String getOutput(String testCaseData) throws IOException {

        Main m = new Main();

        InputData input = m.readInput(new BufferedReader(new StringReader(
                testCaseData)), 1);

        String output = m.handleCase(1, input);

        log.info(output);
        return output.trim();
    }


    @Test
    public void testTree3by2() {
        Node.LETTER_MAX = 4;
        
        List<Node> masterList = new ArrayList<Node>();
        
        Node n = Node.createFirstNode(masterList);
        
       // assertEquals(4, n.getCount());
        
        Node node2 = n.connectSingleNode(2, true);
        
        assertEquals(10, n.getCount());
        
        
        assertEquals(1, n.rightWeights[3][3]);
        assertEquals(0, n.rightWeights[3][2]);
        assertEquals(0, n.rightWeights[3][1]);
        assertEquals(0, n.rightWeights[3][0]);
        
        assertEquals(1, n.rightWeights[2][3]);
        assertEquals(1, n.rightWeights[2][2]);
        assertEquals(0, n.rightWeights[2][1]);
        assertEquals(0, n.rightWeights[2][0]);
        
        assertEquals(1, n.rightWeights[1][3]);
        assertEquals(1, n.rightWeights[1][2]);
        assertEquals(1, n.rightWeights[1][1]);
        assertEquals(0, n.rightWeights[1][0]);
        
        assertEquals(1, n.rightWeights[0][3]);
        assertEquals(1, n.rightWeights[0][2]);
        assertEquals(1, n.rightWeights[0][1]);
        assertEquals(1, n.rightWeights[0][0]);
        
        
        assertEquals(1, n.count[3]);
        assertEquals(2, n.count[2]);
        assertEquals(3, n.count[1]);
        assertEquals(4, n.count[0]);
        
        Node node3 = n.connectSingleNode(3, false);
        
        assertEquals(30, n.getCount());
        
        //The left right node should be updated
        assertEquals(1, n.rightWeights[3][3]);
        assertEquals(0, n.rightWeights[3][2]);
        assertEquals(0, n.rightWeights[3][1]);
        assertEquals(0, n.rightWeights[3][0]);
        
        assertEquals(2, n.rightWeights[2][3]);
        assertEquals(2, n.rightWeights[2][2]);
        assertEquals(0, n.rightWeights[2][1]);
        assertEquals(0, n.rightWeights[2][0]);
        
        assertEquals(3, n.rightWeights[1][3]);
        assertEquals(3, n.rightWeights[1][2]);
        assertEquals(3, n.rightWeights[1][1]);
        assertEquals(0, n.rightWeights[1][0]);
        
        assertEquals(4, n.rightWeights[0][3]);
        assertEquals(4, n.rightWeights[0][2]);
        assertEquals(4, n.rightWeights[0][1]);
        assertEquals(4, n.rightWeights[0][0]);
        
        
        assertEquals(1, n.count[3]);
        assertEquals(4, n.count[2]);
        assertEquals(9, n.count[1]);
        assertEquals(16, n.count[0]);
        
        n.mergeNode();
        /*
        assertEquals(1, node2.nextNodeWeights[0][3]);
        assertEquals(1, node2.nextNodeWeights[0][2]);
        assertEquals(1, node2.nextNodeWeights[0][1]);
        assertEquals(1, node2.nextNodeWeights[0][0]);
        
        assertEquals(2, node2.nextNodeWeights[1][3]);
        assertEquals(2, node2.nextNodeWeights[1][2]);
        assertEquals(2, node2.nextNodeWeights[1][1]);
        assertEquals(1, node2.nextNodeWeights[1][0]);
        
        assertEquals(3, node2.nextNodeWeights[2][3]);
        assertEquals(3, node2.nextNodeWeights[2][2]);
        assertEquals(2, node2.nextNodeWeights[2][1]);
        assertEquals(1, node2.nextNodeWeights[2][0]);
        
        assertEquals(4, node2.nextNodeWeights[3][3]);
        assertEquals(3, node2.nextNodeWeights[3][2]);
        assertEquals(2, node2.nextNodeWeights[3][1]);
        assertEquals(1, node2.nextNodeWeights[3][0]);
        
        assertArrayEquals(node2.nextNodeWeights, node3.prevNodeWeights);
        */
        assertEquals(10, node2.count[3]);
        assertEquals(9, node2.count[2]);
        assertEquals(7, node2.count[1]);
        assertEquals(4, node2.count[0]);
        
        
        
        //assertEquals(30, Node.getTotal(node2.nextNodeWeights));
        //assertEquals(30, Node.getTotal(node3.prevNodeWeights));
        
      //2 to 3 -- [[1, 1, 1, 1], [1, 2, 2, 2], [1, 2, 3, 3], [1, 2, 3, 4]] ==30
        //1 to 2 -- [[4, 4, 4, 4], [0, 3, 3, 3], [0, 0, 2, 2], [0, 0, 0, 1]]
        assertArrayEquals(new int[] {4,4,4,4}, n.rightWeights[0]);
        assertArrayEquals(new int[] {0, 3, 3, 3}, n.rightWeights[1]);
        assertArrayEquals(new int[] {0, 0, 2, 2}, n.rightWeights[2]);
        assertArrayEquals(new int[] {0, 0, 0, 1}, n.rightWeights[3]);
        
        
        Node node5 = Node.connectSingleNode(node2, node3, 5);
        
        assertEquals(50, node5.getCount());
        assertEquals(50, node2.getCount());
        assertEquals(50, node3.getCount());
        
        //1 2 [[10, 9, 7, 4], [0, 6, 5, 3], [0, 0, 3, 2], [0, 0, 0, 1]]
        
        //assertEquals(50, Node.getTotal(node2.nextNodeWeights));
        assertEquals(50, Node.getTotal(node2.bottomWeights));
        
        Node node4 = node2.connectSingleNode(4, true);
        
        //assertEquals(125, Node.getTotal(node2.nextNodeWeights));
        assertEquals(125, Node.getTotal(node2.rightWeights));
        
        assertEquals(125, node4.getCount());
        
        node2.mergeNode();
       // node3.mergeNode();
        
        Node node6 = Node.connectSingleNode(node4, node5, 6);
        
        assertEquals(175, node6.getCount());
        
    }
   
    
    
    
    public void testTree3by3() {
        Node.LETTER_MAX = 4;
        
        List<Node> masterList = new ArrayList<Node>();
        Node node1 = Node.createFirstNode(masterList);
        
        assertEquals(4, node1.getCount());
        
        Node node2 = node1.connectSingleNode(2, true);
        
        assertEquals(10, node1.getCount());
                
        Node node3 = node1.connectSingleNode(3, false);
        
        assertEquals(30, node1.getCount());
        
        node1.mergeNode();
        
        /*
         * Node numbers
         * 1 2 4
         * 3 5 7
         * 6 8 9
         */
        Node node4 = node2.connectSingleNode(4, true);
        
        assertEquals(65, node4.getCount());
        assertEquals(65, node3.getCount());
        
        //Node2 to node 4
        assertArrayEquals(new int[] { 4, 4, 4, 4 }, node2.rightWeights[0]);
        assertArrayEquals(new int[] { 0, 7, 7, 7 }, node2.rightWeights[1]);
        assertArrayEquals(new int[] { 0, 0, 9, 9 }, node2.rightWeights[2]);
        assertArrayEquals(new int[] { 0, 0, 0, 10 }, node2.rightWeights[3]);
        
        //Node 2 to node 3
        /*
        assertArrayEquals(new int[] { 4, 4, 4, 4 }, node2.nextNodeWeights[0]);
        assertArrayEquals(new int[] { 3, 6, 6, 6 }, node2.nextNodeWeights[1]);
        assertArrayEquals(new int[] { 2, 4, 6, 6 }, node2.nextNodeWeights[2]);
        assertArrayEquals(new int[] { 1, 2, 3, 4 }, node2.nextNodeWeights[3]);
        */
        //[[4, 4, 4, 4], [3, 6, 6, 6], [2, 4, 6, 6], [1, 2, 3, 4]]
                
        assertEquals(65, Node.getTotal(node2.rightWeights));
        
        Node node5 = Node.connectSingleNode(node2, node3, 5);
        
        //Node 2 to node 3
        /*
        assertArrayEquals(new int[] { 16, 12, 8, 4 }, node2.nextNodeWeights[0]);
        assertArrayEquals(new int[] { 9, 18, 12, 6 }, node2.nextNodeWeights[1]);
        assertArrayEquals(new int[] { 4, 8, 12, 6 }, node2.nextNodeWeights[2]);
        assertArrayEquals(new int[] { 1, 2, 3, 4 }, node2.nextNodeWeights[3]);
        */
        
        assertEquals(125, node5.getCount());

        //assertEquals(125, Node.getTotal(node3.prevNodeWeights));
        assertEquals(125, Node.getTotal(node3.rightWeights));
        
        //2 to 4
        assertArrayEquals(new int[] { 10, 10, 10, 10 }, node2.rightWeights[0]);
        assertArrayEquals(new int[] { 0, 15, 15, 15 }, node2.rightWeights[1]);
        assertArrayEquals(new int[] { 0, 0, 15, 15 }, node2.rightWeights[2]);
        assertArrayEquals(new int[] { 0, 0, 0, 10 }, node2.rightWeights[3]);
        
        //2 to 5
        //[[4, 8, 12, 16], [0, 9, 15, 21], [0, 0, 12, 18], [0, 0, 0, 10]]
        assertArrayEquals(new int[] { 4, 8, 12, 16}, node2.bottomWeights[0]);
        assertArrayEquals(new int[] { 0, 9, 15, 21 }, node2.bottomWeights[1]);
        assertArrayEquals(new int[] { 0, 0, 12, 18 }, node2.bottomWeights[2]);
        assertArrayEquals(new int[] {0, 0, 0, 10 }, node2.bottomWeights[3]);
        
        //3 to 5
        //[[4, 7, 9, 10], [0, 10, 14, 16], [0, 0, 16, 19], [0, 0, 0, 20]]
        assertArrayEquals(new int[] { 4, 7, 9, 10}, node3.rightWeights[0]);
        assertArrayEquals(new int[] { 0, 10, 14, 16 }, node3.rightWeights[1]);
        assertArrayEquals(new int[] { 0, 0, 16, 19 }, node3.rightWeights[2]);
        assertArrayEquals(new int[] {0, 0, 0, 20 }, node3.rightWeights[3]);
        
        Node node6 = node3.connectSingleNode(6, false);
        
        //2 to 3
        /*
        assertArrayEquals(new int[] { 64, 36, 16, 4 }, node2.nextNodeWeights[0]);
        assertArrayEquals(new int[] { 36, 54, 24, 6 }, node2.nextNodeWeights[1]);
        assertArrayEquals(new int[] { 16, 24, 24, 6 }, node2.nextNodeWeights[2]);
        assertArrayEquals(new int[] { 4, 6, 6, 4 }, node2.nextNodeWeights[3]);
        */
        //2 4 -- [[30, 30, 30, 30], [0, 40, 40, 40], [0, 0, 35, 35], [0, 0, 0, 20]]
        
        //2 3 -- [[64, 36, 16, 4], [36, 54, 24, 6], [16, 24, 24, 6], [4, 6, 6, 4]]

        //3 6 -- [[30, 30, 30, 30], [0, 40, 40, 40], [0, 0, 35, 35], [0, 0, 0, 20]]
        
        assertEquals(330, node6.getCount());
        
        //assertEquals(330, Node.getTotal(node3.prevNodeWeights));
        assertEquals(330, Node.getTotal(node3.rightWeights));
        assertEquals(330, Node.getTotal(node3.bottomWeights));
        
        //assertEquals(330, Node.getTotal(node2.nextNodeWeights));
        assertEquals(330, Node.getTotal(node2.rightWeights));
        assertEquals(330, Node.getTotal(node2.bottomWeights));
        
        //2  4 
        
        assertArrayEquals(new int[] { 30, 30, 30, 30}, node2.rightWeights[0]);
        assertArrayEquals(new int[] { 0, 40, 40, 40 }, node2.rightWeights[1]);
        assertArrayEquals(new int[] { 0, 0, 35, 35 }, node2.rightWeights[2]);
        assertArrayEquals(new int[] {0, 0, 0, 20 }, node2.rightWeights[3]);
        
        
        //3 5
        assertArrayEquals(new int[] { 16, 28, 36, 40}, node3.rightWeights[0]);
        assertArrayEquals(new int[] { 0, 30, 42, 48 }, node3.rightWeights[1]);
        assertArrayEquals(new int[] { 0, 0, 32, 38 }, node3.rightWeights[2]);
        assertArrayEquals(new int[] {0, 0, 0, 20 }, node3.rightWeights[3]);
        //[[16, 28, 36, 40], [0, 30, 42, 48], [0, 0, 32, 38], [0, 0, 0, 20]]
        
        //2 5
        //[[16, 28, 36, 40], [0, 30, 42, 48], [0, 0, 32, 38], [0, 0, 0, 20]]
        assertArrayEquals(new int[] { 16, 28, 36, 40}, node2.bottomWeights[0]);
        assertArrayEquals(new int[] { 0, 30, 42, 48 }, node2.bottomWeights[1]);
        assertArrayEquals(new int[] { 0, 0, 32, 38 }, node2.bottomWeights[2]);
        assertArrayEquals(new int[] {0, 0, 0, 20 }, node2.bottomWeights[3]);
        
        //2 4 -- [[30, 30, 30, 30], [0, 40, 40, 40], [0, 0, 35, 35], [0, 0, 0, 20]]
        node2.mergeNode();
        
        assertArrayEquals(new int[] { 30, 30, 30, 30}, node3.bottomWeights[0]);
        assertArrayEquals(new int[] { 0, 40, 40, 40 }, node3.bottomWeights[1]);
        assertArrayEquals(new int[] { 0, 0, 35, 35 }, node3.bottomWeights[2]);
        assertArrayEquals(new int[] {0, 0, 0, 20 }, node3.bottomWeights[3]);
        
        node3.mergeNode();
        
        //4 to 5 [[4, 7, 9, 10], [4, 17, 23, 26], [4, 17, 39, 45], [4, 17, 39, 65]]
        /*
        assertArrayEquals(new int[] { 4, 7, 9, 10 }, node4.nextNodeWeights[0]);
        assertArrayEquals(new int[] { 4, 17, 23, 26 }, node4.nextNodeWeights[1]);
        assertArrayEquals(new int[] { 4, 17, 39, 45 }, node4.nextNodeWeights[2]);
        assertArrayEquals(new int[] { 4, 17, 39, 65 }, node4.nextNodeWeights[3]);
        */
        //5 to 6
        //[[4, 4, 4, 4], [7, 17, 17, 17], [9, 23, 39, 39], [10, 26, 45, 65]]
        /*
        assertArrayEquals(new int[] { 4, 4, 4, 4 }, node5.nextNodeWeights[0]);
        assertArrayEquals(new int[] { 7, 17, 17, 17 }, node5.nextNodeWeights[1]);
        assertArrayEquals(new int[] { 9, 23, 39, 39 }, node5.nextNodeWeights[2]);
        assertArrayEquals(new int[] { 10, 26, 45, 65 }, node5.nextNodeWeights[3]);
        */
        Node node7 = Node.connectSingleNode(node4, node5, 7);
        
        //4 to 5
        //[[16, 21, 18, 10], [12, 51, 46, 26], [8, 34, 78, 45], [4, 17, 39, 65]]
        
        //5 to 6
        //[[10, 10, 10, 10], [15, 36, 36, 36], [15, 38, 64, 64], [10, 26, 45, 65]]
        
        //4 to 7
        //[[4, 11, 20, 30], [0, 21, 44, 70], [0, 0, 60, 105], [0, 0, 0, 125]]
        
        //good
        //[[4, 11, 20, 30], [0, 21, 44, 70], [0, 0, 60, 105], [0, 0, 0, 125]]
        
        //5 to 7
        //[[4, 8, 12, 16], [0, 24, 41, 58], [0, 0, 71, 110], [0, 0, 0, 146]]
        //good
        //[[4, 8, 12, 16], [0, 24, 41, 58], [0, 0, 71, 110], [0, 0, 0, 146]]
        
        assertEquals(490, node6.getCount());
        
        Node node8 = Node.connectSingleNode(node3, node4, 8);
        
        assertEquals(805, node8.getCount());
        
        node5.mergeNode();
        
        Node node9 = Node.connectSingleNode(node7, node8, 9);
        
        assertEquals(980, node9.getCount());
    }

}
