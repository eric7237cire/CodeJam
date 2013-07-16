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

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import pkr.HoleCards;

import com.google.common.base.Joiner;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

public class TreeNode
{

    PossibilityNode data;
    
    int count;
    
    Set<TreeNode> children;
    TreeNode parent;
    
    Map<iDisplayNode, TreeNode> mapChildren;
    
    Map<String, Integer> bestHandCardsFreqMap;
    Map<String, Integer> secondBestHandCardsFreqMap;

    public TreeNode(PossibilityNode data) {
        this.data = data;
        mapChildren = Maps.newHashMap();
        count = 0;
        children = Sets.newHashSet();
        parent = null;
        
        bestHandCardsFreqMap = Maps.newHashMap();
        secondBestHandCardsFreqMap = Maps.newHashMap();
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
        {
            writer.writeAttribute("desc", data.toString());
        
            
        }
        
        String hands = getTopFive(bestHandCardsFreqMap);
        if (hands.length() > 0) {
            writer.writeAttribute("bestHands", hands);
        }
        
        hands = getTopFive(secondBestHandCardsFreqMap);
        if (hands.length() > 0) {
            writer.writeAttribute("secondBestHands", hands);
        }
        
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
    
    private static void addHand(HoleCards hc, Map<String, Integer> map) 
    {
        if (hc == null) 
            return;
        
        String hcStr = hc.toStartingHandString();
        if (!map.containsKey(hcStr)) {
            map.put(hcStr, 0);
        }
        
        int count = map.get(hcStr);
        ++count;
        map.put(hcStr, count);
    }
    
    public void addBestHand(HoleCards hc) {
        addHand(hc, bestHandCardsFreqMap);
        
    }
    
    public void addSecondBestHand(HoleCards hc) {
        addHand(hc, secondBestHandCardsFreqMap);
    }
     
    
    public String getTopFive( Map<String, Integer> map) {
        List<Pair<Integer, String>> liste = Lists.newArrayList();
        for(String key : map.keySet()) {
            Pair<Integer, String> freqPair = 
                    new ImmutablePair<>(map.get(key), key);
            liste.add(freqPair);
        }
        
        Collections.sort(liste, Ordering.natural().reverse() );
        
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < 5 && i < liste.size(); ++i) {
            sb.append(liste.get(i).getValue());
            sb.append(" (");
            sb.append(liste.get(i).getKey());
            sb.append(") ");
        }
                
        return sb.toString();
    }

    /**
     * @return the data
     */
    public PossibilityNode getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(PossibilityNode data) {
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
