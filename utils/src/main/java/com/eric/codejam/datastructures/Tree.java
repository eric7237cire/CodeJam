package com.eric.codejam.datastructures;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Objects;


    public class Tree<T> {
        private Node<T> root;

        public Node<T> getRoot() {
            return root;
        }

        public Tree(T rootData) {
            root = new Node<T>(null);
            root.data = rootData;
            
        }

        @Override
        public String toString() {
            return "Tree [root=" + root + "]";
        }

        public static class Node<T> {
            private T data;
            private Node<T> parent;
            private List<Node<T>> children;
            
            public Node<T> getChild(T withData) {
                for(Node<T> child : children) {
                    if (Objects.equal(child.data, withData)) {
                        return child;
                    }
                }
                return null;
            }
            public Node(Node<T> parent) {
                children = new ArrayList<>();
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
            public List<Node<T>> getChildren() {
                return children;
            }
            public void setChildren(List<Node<T>> children) {
                this.children = children;
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

