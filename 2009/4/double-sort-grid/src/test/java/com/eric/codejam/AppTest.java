package com.eric.codejam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

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
        
        Node n = Node.createFirstNode();
        
        assertEquals(4, n.getCount());
        
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
        
        assertEquals(10, node2.count[3]);
        assertEquals(9, node2.count[2]);
        assertEquals(7, node2.count[1]);
        assertEquals(4, node2.count[0]);
        
        assertEquals(30, Node.getTotal(node2.nextNodeWeights));
        assertEquals(30, Node.getTotal(node3.prevNodeWeights));
        
        Node node5 = Node.connectSingleNode(node2, node3, 5);
        
        assertEquals(50, node5.getCount());
        assertEquals(50, node2.getCount());
        assertEquals(50, node3.getCount());
        
        assertEquals(50, Node.getTotal(node2.nextNodeWeights));
        assertEquals(50, Node.getTotal(node2.bottomWeights));
        
        Node node4 = node2.connectSingleNode(4, true);
        
        assertEquals(125, Node.getTotal(node2.nextNodeWeights));
        assertEquals(125, Node.getTotal(node2.rightWeights));
        
        assertEquals(125, node4.getCount());
        
        node2.mergeNode();
       // node3.mergeNode();
        
        Node node6 = Node.connectSingleNode(node4, node5, 6);
        
        assertEquals(175, node6.getCount());
        
    }
   
    
    @Test
    public void testTree3by3() {
        Node.LETTER_MAX = 4;
        
        Node node1 = Node.createFirstNode();
        
        assertEquals(4, node1.getCount());
        
        Node node2 = node1.connectSingleNode(2, true);
        
        assertEquals(10, node1.getCount());
                
        Node node3 = node1.connectSingleNode(3, false);
        
        assertEquals(30, node1.getCount());
        
        node1.mergeNode();
        
        
        Node node4 = node2.connectSingleNode(4, true);
        
        assertEquals(65, node4.getCount());
        assertEquals(65, node3.getCount());
        
        Node node5 = Node.connectSingleNode(node2, node3, 5);
        
        assertEquals(125, node5.getCount());

        assertEquals(125, Node.getTotal(node3.prevNodeWeights));
        assertEquals(125, Node.getTotal(node3.rightWeights));
        
        Node node6 = node3.connectSingleNode(6, false);
        
        assertEquals(330, node6.getCount());
        
        node2.mergeNode();
        node3.mergeNode();
        
        Node node7 = Node.connectSingleNode(node4, node5, 7);
        
        assertEquals(490, node6.getCount());
        
        Node node8 = Node.connectSingleNode(node3, node4, 8);
        
        assertEquals(805, node8.getCount());
        
        node5.mergeNode();
        
        Node node9 = Node.connectSingleNode(node7, node8, 9);
        
        assertEquals(980, node9.getCount());
    }

}
