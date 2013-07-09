package pkr.possTree;

import java.util.List;

import javax.xml.stream.XMLStreamWriter;

public interface iDisplayNode
{
    public void serialize(XMLStreamWriter writer);
    
    public long toLong();
    
    
}
