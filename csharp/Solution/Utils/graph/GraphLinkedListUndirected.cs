#if DEBUG
//#define LOGGING_DEBUG
#define LOGGING_INFO
//#define LOGGING_TRACE
#endif

using CodeJam.Utils.tree;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;
using Logger = Utils.LoggerFile;

namespace CodeJam.Utils.graph
{
    public class GraphUndirectedLinkedList
    {
        LinkedList<int>[] outgoingEdges;
        LinkedListNode<int>[][] nodes;
        
        public LinkedList<int> getOutboundConnected(int node)
        {
            return outgoingEdges[node];
        }

        
        public GraphUndirectedLinkedList(int maxNodes)
        {
            Ext.createArray(out nodes, maxNodes, maxNodes, null);
            outgoingEdges = new LinkedList<int>[maxNodes];
            
            for (int i = 0; i < maxNodes; ++i)
            {
                outgoingEdges[i] = new LinkedList<int>();
            
            }

        }

        public LinkedListNode<int> GetNode(int u, int v)
        {
            return nodes[u][v];
        }

        /// <summary>
        /// Remove directed edge u->v
        /// </summary>
        /// <param name="u"></param>
        /// <param name="v"></param>
        public void removeConnection(int u, int v)
        {
            outgoingEdges[u].Remove(nodes[u][v]);
            outgoingEdges[v].Remove(nodes[v][u]);

            nodes[u][v] = null;
            nodes[v][u] = null;
        }
        /// <summary>
        /// Add directed edge u-v
        /// </summary>
        /// <param name="u"></param>
        /// <param name="v"></param>
        public void addConnection(int u, int v)
        {
            Logger.LogDebug("Add connection {0} to {1}", u, v);
            nodes[u][v] = outgoingEdges[u].AddLast(v);
            nodes[v][u] = outgoingEdges[v].AddLast(u);
        }


    }

    public class GraphUndirected
    {
        List<int>[] outgoingEdges;

        public List<int> getOutboundConnected(int node)
        {
            return outgoingEdges[node];
        }


        public GraphUndirected(int maxNodes)
        {
            outgoingEdges = new List<int>[maxNodes];

            for (int i = 0; i < maxNodes; ++i)
            {
                outgoingEdges[i] = new List<int>();

            }

        }

        /// <summary>
        /// Remove directed edge u->v
        /// </summary>
        /// <param name="u"></param>
        /// <param name="v"></param>
        public void removeConnection(int u, int v)
        {
            outgoingEdges[u].Remove(v);
            outgoingEdges[v].Remove(u);
        }
        /// <summary>
        /// Add directed edge u-v
        /// </summary>
        /// <param name="u"></param>
        /// <param name="v"></param>
        public void addConnection(int u, int v)
        {
            Logger.LogDebug("Add connection {0} to {1}", u, v);
            outgoingEdges[u].Add(v);
            outgoingEdges[v].Add(u);
        }


    }
}
