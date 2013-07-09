package pkr.possTree;

import java.util.Map;
import java.util.Set;

public class TreeNode
{

    public TreeNode() {
        
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
