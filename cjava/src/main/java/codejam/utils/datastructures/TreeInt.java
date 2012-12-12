package codejam.utils.datastructures;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

public class TreeInt {


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

        public TreeInt(int root) {
            nodes = new HashMap<>();
            this.root = new Node(root);

        }
        
        public TreeInt reroot(int newRoot) {
            TreeInt newTree = new TreeInt(newRoot);
            
            //Set<Integer> visited = new HashSet<Integer>();
            Queue<Integer> toVisit = new LinkedList<>();
            
            toVisit.add(newRoot);
            
            while(!toVisit.isEmpty()) {
                Integer nodeId = toVisit.poll();
                
                //if (visited.contains(nodeId))
                 //   continue;
                
                //visited.add(nodeId);
                
                Preconditions.checkState(newTree.getNodes().containsKey(nodeId));
                Node newTreeNode = newTree.getNodes().get(nodeId);
                Node oldTreeNode = nodes.get(nodeId);
                //Get all new children from old tree
                for(Node childNode : oldTreeNode.children) {
                    //Add children to new tree node
                    if (newTreeNode.getParent() == null || childNode.id != newTreeNode.getParent().id) {
                        newTreeNode.addChild(childNode.id);
                        toVisit.add(childNode.id);
                    }
                }
                
                if (oldTreeNode.getParent() == null)
                    continue;
                
                //Also check parent
                if (newTreeNode.getParent() == null || oldTreeNode.getParent().id != newTreeNode.getParent().id) {
                    newTreeNode.addChild(oldTreeNode.getParent().id);
                    toVisit.add(oldTreeNode.getParent().id);
                }
            }
            
            return newTree;
        }

        @Override
        public String toString() {
            return "Tree [root=" + root + "]";
        }

        public class Node {
            private int id;
            private int size;
            private int height;
            
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

            @Override
            public String toString() {
                return "Node [id=" + id + ", size=" + size + ", height="
                        + height + ", childrenCount=" + children.size() + "]" ;
            }

            //Root
            private Node(int id) {
                children = new HashSet<>();
                this.parent = null;
                this.id = id;
                height = 1;
                size = 1;
                nodes.put(id, this);
            }
            public Node(int id, Node parent) {
                this(id);
                this.parent = parent;
                this.height = 1;
                this.size = 1;
                
                Node node = parent;
                int h = 1;
                while(node != null) {
                    node.size++;
                    h++;
                    node.height = Math.max(h, node.height);
                    node = node.parent;
                }
            }

            
            public Node getParent() {                
                return parent;
            }

            public Node addChild(int childId) {
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
                return Objects.hashCode(id);
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj)
                    return true;
                if (obj == null)
                    return false;
                if (getClass() != obj.getClass())
                    return false;
                Node other = (Node) obj;

                return Objects.equal(id, other.id);

            }
        }
    }


