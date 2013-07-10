package pkr.possTree;

import java.io.FileWriter;
import java.io.IOException;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.google.common.base.Preconditions;

import pkr.CompleteEvaluation;

public class Tree
{

    @SuppressWarnings("unchecked")
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
        
        for(int i = 0; i < 6; ++i) {
            //0 2 4 are textures  1 3 5 are evals
            iDisplayNode dispNode = null;
            
            if (i % 2 == 0) {
                dispNode = eval.getRoundTexture(i / 2);
            } else {
                dispNode = eval.getRoundEval( (i-1) / 2);
            }
        
            TreeNode curChildNode = null;
            if (!curNode.getMapChildren().containsKey(dispNode)) {
                curChildNode = new TreeNode(dispNode);
                curChildNode.setParent(curNode);
                curNode.addChild(curChildNode);
            } 
            
            curChildNode = curNode.getMapChildren().get(dispNode);
            Preconditions.checkState(curChildNode.getParent().equals(curNode));
            
            curChildNode.count++;
            
            curNode = curChildNode;
        }
         
        
        
    }
    
    public void output(String fileName) {
        XMLOutputFactory xof = XMLOutputFactory.newInstance();

        XMLStreamWriter xtw = null;

        // String fileName = "C:\\codejam\\CodeJam\\simul\\out.xml";

        try {
            xtw = xof.createXMLStreamWriter(new FileWriter(fileName));

            final String prefix = "http://www.w3.org/TR/REC-html40";

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
