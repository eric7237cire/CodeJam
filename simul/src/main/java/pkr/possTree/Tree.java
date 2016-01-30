package pkr.possTree;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pkr.CompleteEvaluation;
import pkr.possTree.PossibilityNode.TextureCategory;
import poker_simulator.flags.HandCategory;
import poker_simulator.flags.HandSubCategory;
import poker_simulator.flags.WinningLosingCategory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

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
                
                if (possLevel == PossibilityNode.Levels.HAND_SUB_CATEGORY.ordinal())
                    continue;
                if (possLevel == PossibilityNode.Levels.TEXTURE.ordinal())
                    continue;
                
                PossibilityNode dispNode = eval.getPossibilityNode(round, possLevel);
                
                if (possLevel == PossibilityNode.Levels.HAND_CATEGORY.ordinal() && dispNode.hasFlag(HandCategory.SET_USING_BOTH))
                {
                	log.debug("Prob");
                }
                
                //Clear all flop flags
                if (round < 2 || (round == 2 && possLevel == 1))
                {
                   // continue;
                    /*
                    for( Levels level : PossibilityNode.Levels.values())
                    {
                        if (possLevel == level.ordinal())
                        {
                            for(iFlag flag : level.getFlags())
                            {
                                dispNode.clearFlag(flag);
                            }
                        }
                    }*/
                }
                
              //Don't show gutshots
                if (possLevel == PossibilityNode.Levels.HAND_CATEGORY.ordinal()) {
                    //dispNode.clearFlag(HandCategory.FLUSH_DRAW);
                   // dispNode.clearFlag(HandCategory.STRAIGHT_DRAW_1);
                   // dispNode.clearFlag(HandCategory.STRAIGHT_DRAW_2);
                }
                
                //2nd best hand ==> losing
                if (possLevel == PossibilityNode.Levels.WIN_LOSE.ordinal() && 
                        dispNode.hasFlag(WinningLosingCategory.SECOND_BEST_HAND)) {
                    dispNode.clearFlag(WinningLosingCategory.SECOND_BEST_HAND);
                    dispNode.setFlag(WinningLosingCategory.LOSING);
                }
                
                if (possLevel == PossibilityNode.Levels.HAND_CATEGORY.ordinal())
                {
                	List<HandCategory> l = Lists.newArrayList();
                	l.add(HandCategory.HIDDEN_PAIR);
                	l.add(HandCategory.PAIR_OVERCARDS_0);
                	l.add(HandCategory.PAIR_OVERCARDS_1);
                	l.add(HandCategory.PAIR_OVERCARDS_2);
                	l.add(HandCategory.PAIR_OVERCARDS_3);
                	
                	for(HandCategory tc : l)
                	{
                		if (dispNode.hasFlag(tc)) {
                			dispNode.clearFlag(tc);
                			dispNode.setFlag(HandCategory.PAIR_USING_HOLE_CARDS);
                		}
                	}
                    
                	dispNode.clearFlag(HandCategory.STRAIGHT_DRAW_1);
                	dispNode.clearFlag(HandCategory.STRAIGHT_DRAW_2);
                	dispNode.clearFlag(HandCategory.FLUSH_DRAW);
                }
                
                
                //ne montre pas unsuited ni tirage de couleur
                
                if (possLevel == PossibilityNode.Levels.TEXTURE.ordinal()) {
                    dispNode.clearFlag(TextureCategory.UNSUITED);
                    dispNode.clearFlag(TextureCategory.SAME_SUIT_2);
                    dispNode.clearFlag(TextureCategory.STRAIGHT_POSSIBLE);
                    dispNode.clearFlag(TextureCategory.SAME_SUIT_3);
                    dispNode.clearFlag(TextureCategory.PAIRED_BOARD);
                    
                    for(TextureCategory tc : TextureCategory.values())
                    {
                    	dispNode.clearFlag(tc);
                    }
                    
                    //dispNode.setFlag(TextureCategory.UNPAIRED_BOARD);
                }
                
                //Fusion kicker + 2 et kicker + 1
                
                if (possLevel == PossibilityNode.Levels.HAND_SUB_CATEGORY.ordinal()
                        && dispNode.hasFlag(HandSubCategory.BY_KICKER_2_PLUS))
                         {

                    dispNode.clearFlag(HandSubCategory.BY_KICKER_2_PLUS);
                    dispNode.setFlag(HandSubCategory.BY_KICKER_1);
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
            //xtw = xof.createXMLStreamWriter(new FileWriter(fileName));

            xtw = xof.createXMLStreamWriter(new FileOutputStream(fileName), "UTF-8");
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
        	log.error("ex", ex);
        } catch (IOException ex) {
        	log.error("ex", ex);
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
