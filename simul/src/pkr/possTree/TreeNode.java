package pkr.possTree;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class TreeNode
{

    public TreeNode(iDisplayNode data) {
        this.data = data;
        mapChildren = Maps.newHashMap();
        count = 0;
        children = Sets.newHashSet();
        parent = null;
    }
    
    public static DecimalFormat df = new DecimalFormat("0.#");
    static {
        df.setRoundingMode(RoundingMode.HALF_UP);
        df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
    }
    
    public void serialize(XMLStreamWriter writer) throws XMLStreamException
    {
        if (data == null) {
            writer.writeStartElement("root");
        } else {
            String perc = "P_" + df.format(100.0 * count / getParent().count) + "%";
            writer.writeStartElement(perc + "_" + data.toString());
        }
        writer.writeAttribute("count", Integer.toString(count));
        if (data != null)
            writer.writeAttribute("desc", data.toString());
        
        List<TreeNode> sortedByCountRev = Lists.newArrayList(children);
        
        Collections.sort(sortedByCountRev, new Comparator<TreeNode>() {

            /* (non-Javadoc)
             * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
             */
            @Override
            public int compare(TreeNode o1, TreeNode o2) {
                return ComparisonChain.start().compare(o2.count, o1.count).result(); 
            }
            
        });
        
        
        for(TreeNode child : sortedByCountRev) {
            child.serialize(writer);
        }
        
        writer.writeEndElement();
    }
    
    public void addChild(TreeNode child) 
    {
        children.add(child);
        mapChildren.put(child.data, child);
    }
    
    iDisplayNode data;
    
    int count;
    
    Set<TreeNode> children;
    TreeNode parent;
    
    Map<iDisplayNode, TreeNode> mapChildren;

    /**
     * @return the data
     */
    public iDisplayNode getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(iDisplayNode data) {
        this.data = data;
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(int count) {
        this.count = count;
    }

    

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((data == null) ? 0 : data.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TreeNode other = (TreeNode) obj;
        if (data == null) {
            if (other.data != null)
                return false;
        } else if (!data.equals(other.data))
            return false;
        return true;
    }

    /**
     * @return the children
     */
    public Set<TreeNode> getChildren() {
        return children;
    }

    /**
     * @param children the children to set
     */
    public void setChildren(Set<TreeNode> children) {
        this.children = children;
    }

    /**
     * @return the parent
     */
    public TreeNode getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    /**
     * @return the mapChildren
     */
    public Map<iDisplayNode, TreeNode> getMapChildren() {
        return mapChildren;
    }

    /**
     * @param mapChildren the mapChildren to set
     */
    public void setMapChildren(Map<iDisplayNode, TreeNode> mapChildren) {
        this.mapChildren = mapChildren;
    }

}
