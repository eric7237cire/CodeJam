package codejam.utils.datastructures;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Objects;

public class TreeWithIds<T extends Identifiable> {
    private Node<T> root;

    public Node<T> getRoot() {
        return root;
    }

    public TreeWithIds(T rootData) {
        root = new Node<T>(null);
        root.data = rootData;

    }

    @Override
    public String toString() {
        return "Tree [root=" + root + "]";
    }

    public static class Node<T extends Identifiable> {
        private T data;
        private Node<T> parent;
        private Map<Integer, Node<T>> children;

        public Node<T> getChild(T withData) {
            if (children.containsKey(withData.getId())) {
                
                return children.get(withData.getId());
                
            }
            return null;
        }
        
        public Node<T> getChildWithId(int id) {
            if (children.containsKey(id)) {
                
                return children.get(id);
                
            }
            return null;
        }

        public Node(Node<T> parent) {
            children = new HashMap<>();
            this.parent = parent;
        }

        public Node(T data, Node<T> parent) {
            this(parent);
            this.data = data;

        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public Node<T> getParent() {
            return parent;
        }

        public void setParent(Node<T> parent) {
            this.parent = parent;
        }
        public Node<T> addChild(T childData) {
            Node<T> newNode = new Node<T>(childData, this);
            children.put(childData.getId(), newNode);
            return newNode;
        }
        public Collection<Node<T>> getChildren() {
            return children.values();
        }

        public void setChildren(List<Node<T>> children) {
            this.children.clear();
            
            if (children == null)
                return;
            
            for(Node<T> child : children) {
                this.children.put(child.getData().getId(), child);
            }
        }

        @Override
        public String toString() {
            return "Node [data=" + data + "]";
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(data);
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Node<T> other = (Node<T>) obj;

            return Objects.equal(data, other.data);

        }
    }
}
