package codejam.utils.datastructures;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

public class TreeInt<T> {

    private boolean stats = true;
    private boolean uniqueNodeIds = true;
    
    public boolean isUniqueNodeIds() {
        return uniqueNodeIds;
    }

    public void setUniqueNodeIds(boolean uniqueNodeIds) {
        this.uniqueNodeIds = uniqueNodeIds;
    }

    public boolean isStats() {
        return stats;
    }

    public void setStats(boolean stats) {
        this.stats = stats;
    }

    private Node root;

    private Map<Integer, Node> nodes;

    public Map<Integer, Node> getNodes() {
        return nodes;
    }

    public void setNodes(Map<Integer, Node> nodes) {
        this.nodes = nodes;
    }

    public Node getRoot() {
        return root;
    }

    public void reset() {
        nodes = new HashMap<>();
        this.root = new Node(root.id);
    }

    public TreeInt(int root) {
        nodes = new HashMap<>();
        this.root = new Node(root);
    }

    public TreeInt<T> reroot(int newRoot) {
        TreeInt<T> newTree = new TreeInt<T>(newRoot);

        // Set<Integer> visited = new HashSet<Integer>();
        Queue<Integer> toVisit = new LinkedList<>();

        toVisit.add(newRoot);

        while (!toVisit.isEmpty()) {
            Integer nodeId = toVisit.poll();

            // if (visited.contains(nodeId))
            // continue;

            // visited.add(nodeId);

            Preconditions.checkState(newTree.getNodes().containsKey(nodeId));
            Node newTreeNode = newTree.getNodes().get(nodeId);
            Node oldTreeNode = nodes.get(nodeId);
            // Get all new children from old tree
            for (Node childNode : oldTreeNode.children) {
                // Add children to new tree node
                if (newTreeNode.getParent() == null
                        || childNode.id != newTreeNode.getParent().id) {
                    newTreeNode.addChild(childNode.id);
                    toVisit.add(childNode.id);
                }
            }

            if (oldTreeNode.getParent() == null)
                continue;

            // Also check parent
            if (newTreeNode.getParent() == null
                    || oldTreeNode.getParent().id != newTreeNode.getParent().id) {
                newTreeNode.addChild(oldTreeNode.getParent().id);
                toVisit.add(oldTreeNode.getParent().id);
            }
        }

        return newTree;
    }

    @Override
    public String toString() {
        return "Tree\n" + root.toString();
    }

    public class Node {
        private int id;
        private int size;
        private int height;
        private int depth;
        private T data;

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public int getDepth() {
            return depth;
        }

        public int getId() {
            return id;
        }

        public int getSize() {
            return size;
        }

        public int getHeight() {
            return height;
        }

        private Node parent;
        private Set<Node> children;

        public Set<Node> getChildren() {
            return children;
        }
        
        public Set<Integer> getNext2Levels() {
            Set<Integer> ret = Sets.newHashSet();
            ret.add(getId());
            for(Node node : children) {
                ret.add(node.getId());
                for(Node grandChild : node.children) {
                    ret.add(grandChild.getId());
                }
            }
            
            return ret;
        }

        private static final int INDENT = 3;

        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer();

            sb.append(StringUtils.repeat(" ", getDepth() * INDENT));
            sb.append(id);
            sb.append(" [").append(data).append("] ");
            sb.append("\n");
            for (Node child : children) {
                sb.append(child.toString());
            }
            return sb.toString();
            // return "Node [id=" + id + ", size=" + size + ", height="
            // + height + ", childrenCount=" + children.size() + "]" ;
        }

        // Root
        private Node(int id) {
            children = new HashSet<>();
            this.parent = null;
            this.id = id;
            height = 1;
            size = 1;
            depth = 0;
            if (uniqueNodeIds)
                nodes.put(id, this);
        }

        public Node(int id, Node parent) {
            this(id);
            this.parent = parent;
            this.height = 1;
            this.size = 1;

            if (!isStats())
                return;
            
            Node node = parent;

            int h = 1;
            this.depth = 0;
            while (node != null) {
                node.size++;
                h++;
                ++depth;
                node.height = Math.max(h, node.height);
                node = node.parent;
            }
        }

        public Node getParent() {
            return parent;
        }

        public Node addChild(int childId) {
            if (uniqueNodeIds)
                Preconditions.checkState(!nodes.containsKey(childId));

            Node child = new Node(childId, this);
            Preconditions.checkState(!children.contains(child));

            children.add(child);
            return child;
        }

        public void setChildren(Set<Node> children) {
            this.children = children;
        }

        @Override
        public int hashCode() {
            if (uniqueNodeIds)
                return Objects.hashCode(id);
            else
                return super.hashCode();
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(Object obj) {
            if (!uniqueNodeIds)
                return super.equals(obj);
            
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            TreeInt<T>.Node other = (TreeInt<T>.Node) obj;

            return Objects.equal(id, other.id);

        }
    }
}
