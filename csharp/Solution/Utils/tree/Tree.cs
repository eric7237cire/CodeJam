using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;
using Wintellect.PowerCollections;

namespace CodeJam.Utils.tree
{
    public class TreeInt<T>
    {

        private bool stats = true;


        public bool isStats()
        {
            return stats;
        }

        public void setStats(bool stats)
        {
            this.stats = stats;
        }


        public void PostOrderTraversal(Node node, Action<Node> action)
        {
            foreach (var childNode in node.getChildren())
            {
                PostOrderTraversal(childNode, action);
            }

            action(node);
        }

        private Node root;

        private Dictionary<int, Node> nodes;

        public Dictionary<int, Node> getNodes()
        {
            return nodes;
        }

        public void setNodes(Dictionary<int, Node> nodes)
        {
            this.nodes = nodes;
        }

        public Node getRoot()
        {
            return root;
        }

        public void reset()
        {
            nodes = new Dictionary<int, Node>();
            this.root = new Node(root.id, this);
        }

        public TreeInt(int root) {
            nodes = new Dictionary<int, Node>();
            this.root = new Node(root, this);
        }

        public TreeInt<T> reroot(int newRoot) {
            TreeInt<T> newTree = new TreeInt<T>(newRoot);

            // Set<int> visited = new HashSet<int>();
            Queue<int> toVisit = new Queue<int>();

            toVisit.Enqueue(newRoot);

            while (toVisit.Count > 0) {
                int nodeId = toVisit.Dequeue();

                // if (visited.contains(nodeId))
                // continue;

                // visited.add(nodeId);

                Preconditions.checkState(newTree.getNodes().ContainsKey(nodeId));
                Node newTreeNode = newTree.getNodes()[nodeId];
                Node oldTreeNode = nodes[nodeId];
                // Get all new children from old tree
                foreach (Node childNode in oldTreeNode.getChildren()) {
                    // Add children to new tree node
                    if (newTreeNode.getParent() == null
                            || childNode.id != newTreeNode.getParent().id) {
                        newTreeNode.addChild(childNode.id);
                        toVisit.Enqueue(childNode.id);
                    }
                }

                if (oldTreeNode.getParent() == null)
                    continue;

                // Also check parent
                if (newTreeNode.getParent() == null
                        || oldTreeNode.getParent().id != newTreeNode.getParent().id) {
                    newTreeNode.addChild(oldTreeNode.getParent().id);
                    toVisit.Enqueue(oldTreeNode.getParent().id);
                }
            }

            return newTree;
        }


        public override String ToString()
        {
            return "Tree\n" + root.ToString();
        }

        public class Node
        {
            internal int id;
            private int size;
            private int height;
            private int depth;
            private T data;
            private Node parent;
            private HashSet<Node> children;
            private TreeInt<T> tree;

            public T getData()
            {
                return data;
            }

            public void setData(T data)
            {
                this.data = data;
            }

            public int getDepth()
            {
                return depth;
            }

            public int getId()
            {
                return id;
            }

            public int getSize()
            {
                return size;
            }

            public int getHeight()
            {
                return height;
            }


            public HashSet<Node> getChildren()
            {
                return children;
            }



            private static int INDENT = 3;

            public override String ToString()
            {
                StringBuilder sb = new StringBuilder();

                sb.Append(String.Concat(Enumerable.Repeat(" ", getDepth() * INDENT)));

                sb.Append(id);
                sb.Append(" [").Append(data).Append("] ");
                sb.Append("\n");
                foreach (Node child in this.children)
                {
                    sb.Append(child.ToString());
                }
                return sb.ToString();
                // return "Node [id=" + id + ", size=" + size + ", height="
                // + height + ", childrenCount=" + children.size() + "]" ;
            }

            // Root
            public Node(int id, TreeInt<T> tree)
            {
                children = new HashSet<Node>();
                this.parent = null;
                this.id = id;
                height = 1;
                size = 1;
                depth = 0;
                this.tree = tree;

                tree.nodes.Add(id, this);
            }

            public Node(int id, Node parent, TreeInt<T> tree) :this(id, tree)
            {
                this.parent = parent;
                this.height = 1;
                this.size = 1;

                if (!tree.isStats())
                    return;

                Node node = parent;

                int h = 1;
                this.depth = 0;
                while (node != null)
                {
                    node.size++;
                    h++;
                    ++depth;
                    node.height = Math.Max(h, node.height);
                    node = node.parent;
                }
            }

            public Node getParent()
            {
                return parent;
            }

            public Node addChild(int childId)
            {

                Preconditions.checkState(!tree.nodes.ContainsKey(childId));

                Node child = new Node(childId, this, tree);
                Preconditions.checkState(!children.Contains(child));

                children.Add(child);


                return child;
            }

            public void setChildren(HashSet<Node> children)
            {
                this.children = children;
            }


            public int hashCode()
            {
                return id.GetHashCode();

            }

            public bool equals(Object obj)
            {

                if (this == obj)
                    return true;
                if (obj == null)
                    return false;

                TreeInt<T>.Node other = (TreeInt<T>.Node)obj;

                return id.Equals(other.id);

            }
        }
    }
}

