package pkr.possTree;

import java.io.FileWriter;
import java.io.IOException;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class Tree
{

    public Tree() {
        // TODO Auto-generated constructor stub
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
