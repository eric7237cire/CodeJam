package pkr.possTree;

import java.io.FileWriter;
import java.io.IOException;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import pkr.CompleteEvaluation;
import pkr.EvalHands;
import pkr.possTree.PossibilityNode.TextureCategory;

import static pkr.possTree.PossibilityNode.*;

public class Tree
{

    private static Logger log = LoggerFactory.getLogger(Tree.class);
    
    //@SuppressWarnings("unchecked")
    public Tree() {
        rootNode = new TreeNode(null);
    }
    
    /*
     * 6 levels
     * flop texture, eval
     * turn texture, eval
     * river texture, eval
     */
    TreeNode rootNode;
        
    public void addCompleteEvaluation(CompleteEvaluation eval) 
    {
        //rootNode.count++;
        
        TreeNode curNode = rootNode;
        curNode.count++;
               
        for(int round = 0; round < 3; ++round) 
        {
            for(int possLevel = 0; possLevel < PossibilityNode.Levels.values().length; ++possLevel) 
            {
                PossibilityNode dispNode = eval.getPossibilityNode(round, possLevel);
                
                if (possLevel == PossibilityNode.Levels.WIN_LOSE.ordinal() && 
                        dispNode.hasFlag(WinningLosingCategory.SECOND_BEST_HAND)) {
                    dispNode.clearFlag(WinningLosingCategory.SECOND_BEST_HAND);
                    dispNode.setFlag(WinningLosingCategory.LOSING);
                }
            
                TreeNode curChildNode = null;
                if (!curNode.getMapChildren().containsKey(dispNode)) {
                    curChildNode = new TreeNode(dispNode);
                    curChildNode.setParent(curNode);
                    curNode.addChild(curChildNode);
                } 
                
                curChildNode = curNode.getMapChildren().get(dispNode);
                Preconditions.checkState(curChildNode.getParent().equals(curNode));
                
                curChildNode.addBestHand(eval.getBestHand(round));
                curChildNode.addSecondBestHand(eval.getSecondBestHand(round));
                
                /*if (possLevel == 3 && round == 0 && check1) {
                    if (curChildNode.bestHandCardsFreqMap.size() > 2) {
                        log.warn("hmmm");
                    }
                }*/
                
                curChildNode.count++;
                
                curNode = curChildNode;
            }
        }
         
        
        
    }
    
    public void output(String fileName) {
        XMLOutputFactory xof = XMLOutputFactory.newInstance();

        XMLStreamWriter xtw = null;

        // String fileName = "C:\\codejam\\CodeJam\\simul\\out.xml";

        try {
            xtw = xof.createXMLStreamWriter(new FileWriter(fileName));

           // final String prefix = "http://www.w3.org/TR/REC-html40";

            xtw.writeStartDocument("utf-8", "1.0");
            //xtw.setPrefix("html", "http://www.w3.org/TR/REC-html40");
            xtw.writeStartElement("nodes");

            rootNode.serialize(xtw);

            xtw.writeEndElement();
            xtw.writeEndDocument();

            xtw.flush();
            xtw.close();
        } catch (XMLStreamException ex) {

        } catch (IOException ex) {

        }
    }

    public static void main(String[] args) 
    {
        XMLOutputFactory xof =  XMLOutputFactory.newInstance();
        
        XMLStreamWriter xtw = null;
        
        String fileName = "C:\\codejam\\CodeJam\\simul\\out.xml";
        
        try {
        xtw = xof.createXMLStreamWriter(new FileWriter(fileName));


        xtw.writeStartDocument("utf-8","1.0");
        xtw.setPrefix("html", "http://www.w3.org/TR/REC-html40");
        xtw.writeStartElement("http://www.w3.org/TR/REC-html40","html");
        xtw.writeNamespace("html", "http://www.w3.org/TR/REC-html40");
        xtw.writeStartElement("http://www.w3.org/TR/REC-html40", "head");
        xtw.writeStartElement("http://www.w3.org/TR/REC-html40", "title");
        xtw.writeCharacters("Frobnostication");
        xtw.writeEndElement();
        xtw.writeEndElement();
        xtw.writeComment("all elements here are explicitly in the HTML namespace");
        xtw.writeStartElement("http://www.w3.org/TR/REC-html40", "body");
        xtw.writeStartElement("http://www.w3.org/TR/REC-html40", "p");
        xtw.writeCharacters("Moved to");
        xtw.writeStartElement("http://www.w3.org/TR/REC-html40", "a");
        xtw.writeAttribute("href","http://frob.com");
        xtw.writeCharacters("here");
        xtw.writeEndElement();
        xtw.writeEndElement();
        xtw.writeEndElement();
        xtw.writeEndElement();
        xtw.writeEndDocument();

        xtw.flush();
        xtw.close();
        } catch (XMLStreamException ex) {
            
        } catch (IOException ex) {
            
        }
    }
}
