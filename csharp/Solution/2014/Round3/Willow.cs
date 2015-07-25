//#define LOGGING_DEBUG
#define LOGGING_INFO
using CodeJam._2014.Round2;
using CodeJam.Utils.geom;
using CodeJam.Utils.graph;
using CodeJam.Utils.math;
using CodeJam.Utils.tree;
using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;
using Wintellect.PowerCollections;
using Logger = Utils.LoggerFile;
using PriorityQueueDemo;



namespace Year2014.Round3.Problem4
{

    public class WillowInput
    {
        
        public int N;

        public List<int> C;

        public List<Pair<int, int>> Roads;
    }

    public class Willow : InputFileProducer<WillowInput>, InputFileConsumer<WillowInput, int>
    {
        public WillowInput createInput(Scanner scanner)
        {
            //scanner.enablePlayBack();
            WillowInput input = new WillowInput();

            input.N = scanner.nextInt();

            input.C = new List<int>();
            input.Roads = new List<Pair<int, int>>();

            for (int i = 0; i < input.N; ++i)
            {
                input.C.Add(scanner.nextInt());
            }

            for (int i = 1; i < input.N; ++i)
            {
                input.Roads.Add(new Pair<int, int>(i, scanner.nextInt()));
            }
            //Logger.LogInfo("Input {}", scanner.finishPlayBack());
            return input;
        }

        public class PreCalcArgs
        {
            public WillowInput input;
            public int[][] next_node_to;
            public int[] best_coins;
            public int[][] edge_id;
            public List<int>[] con;
            public int[][] best_nodes;
            public int[][] memo_branch_off;
            public int[][] memo_rec;

            public int GetEdgeId(int from, int to)
            {
                return edge_id[to][from];
            }
        }
        static int ccc = 0;
        // Pre-calculate the next_node_to, best_coins, best_nodes.
        /// <summary>
        /// Calculate from an initial edge from==>first_node
        /// </summary>
        /// <param name="a"></param>
        /// <param name="i"></param>
        /// <param name="pi"></param>
        /// <param name="from"></param>
        /// <param name="first_node"></param>
        /// <param name="depth"></param>
        public static void precalc(PreCalcArgs a, int i, int pi, int from, int first_node, int depth ) {
            ccc++;
            //if (ccc % 100000 == 0)

            //Logger.LogInfo("depth {} count {} -- precalc i={} pi={} from={} first_node={}", depth, ccc, i, pi, from, first_node);

            //If wanting to go to "i" from "from", must take "first_node"
            a.next_node_to[from][i] = first_node;
  
            int best = a.best_coins[a.edge_id[i][pi]];

            //if (best >= 0)
               // return;
  
            best = 0;
  
            List<Pair<int,int>> arr = new List<Pair<int,int>>();

            foreach (int ni in a.con[i])  
            {
                if (ni != pi)
                {
                    precalc(a, ni, i, from, first_node, depth+1);

                    int coins = a.best_coins[a.edge_id[ni][i]];

                    arr.Add(new Pair<int, int>(coins, ni));

                    best = Math.Max(best, coins);
                }
            }

            arr.Sort();
            arr.Reverse();
            
            for (int j = 0; j < arr.Count && j < 3; j++)
            {
                a.best_nodes[a.edge_id[i][pi]][j] = arr[j].Second;
            }
    
            best += a.input.C[i];

            a.best_coins[a.edge_id[i][pi]] = best;
        }

        // Returns the best next vertex when coming from
        // edge (pi -> i), excluding vertex v1 and v2.
        int next_best_except(PreCalcArgs a, int i, int pi, int v1, int v2 = -1)
        {
            int ei = a.edge_id[i][pi];
            int j = 0;
            int[] arr = a.best_nodes[ei];
            if (arr[j] == v1 || arr[j] == v2) j++;
            if (arr[j] == v1 || arr[j] == v2) j++;
            return arr[j];
        }

        // Maximum coins for sub-tree i with parent pi.
        int max_coins(PreCalcArgs a, int i, int pi)
        {
            return (i < 0) ? 0 : a.best_coins[a.edge_id[i][pi]];
        }

        // Maximum coins for branching off at any vertex in [i, j].
        int branch_off_between(PreCalcArgs a, int i, int pi, int j, int pj)
        {

            int ei = a.edge_id[i][pi];

            int ej = a.edge_id[j][pj];

            int ret = a.memo_branch_off[ei][ej];

            if (ret != -1) return ret;

            if (i == j)
            {
                int ni = next_best_except(a, i, pi, pj);
                int njj = next_best_except(a, i, pi, pj, ni);
                // The other player takes the third best vertex nj since
                // the best two are already taken by the current player.
                return ret = max_coins(a, njj, j);
            }

            int nj = a.next_node_to[j][i];
            int njb = next_best_except(a, j, pj, nj);
            int branch_off_now = max_coins(a, njb, j);
            int branch_off_later = ((nj == i) ? 0 : a.input.C[nj])
              + branch_off_between(a, i, pi, nj, j);

            ret = Math.Max(branch_off_now, branch_off_later);
            a.memo_branch_off[ei][ej] = ret;
            return ret;
        }

        // Minimax for the current player with last edge (pi -> i)
        // and the other player with last edge (pj -> j).
        int rec_large(PreCalcArgs a, int i, int pi, int j, int pj)
        {
            int ei = a.edge_id[i][pi];
            int ej = a.edge_id[j][pj];
            int ret = a.memo_rec[ei][ej];
            if (ret != -1) return ret;

            if (i == j)
            {
                // The current player pick the next best path.
                int nii = next_best_except(a, i, pi, pj);
                // The other player pick the next next best path.
                int nj = next_best_except(a, i, pi, pj, nii);
                return ret = max_coins(a, nii, i) - max_coins(a, nj, j);
            }

            // The first option for the current player:
            // The current player pick the vertex ni
            // that leads to other player last vertex.
            int ni = a.next_node_to[i][j];
            int option1 = ((ni == j) ? 0 : a.input.C[ni]) - rec_large(a, j, pj, ni, i);

            // The second option for the current player:
            // The current player go to the best path other than ni.
            ni = next_best_except(a, i, pi, ni);
            int p1coins = max_coins(a, ni, i);
            // The other player branch off at any point
            // between vertex i and j (inclusive).
            int p2coins = branch_off_between(a, i, pi, j, pj);
            int option2 = p1coins - p2coins;

            // Pick the best outcome for the current player.
            return ret = Math.Max(option1, option2);
        }

        public class Node
        {
            
            public int prev;
            public int current;
            public Node(int c, int p)
            {
                prev = p;
                current = c;
               
            }
        }
        public static void precalc_iterative(PreCalcArgs a)
        {
            //Edge from N to all other nodes

            Queue<Node> pq = new Queue<Node>();

            Logger.LogInfo("Build structures");


           // HashSet<int>[] prevNodesEdge;
           // Ext.createArrayWithNew(out prevNodesEdge, a.input.N * 3);


            List<int>[] prevNodesListEdge;
            Ext.createArrayWithNew(out prevNodesListEdge, a.input.N * 3);

         //   HashSet<int>[] seenNodes;
           // Ext.createArrayWithNew(out seenNodes, a.input.N+1);

            Logger.LogInfo("Find leaf nodes");

            //Find all leaf nodes
            for(int i = 0; i < a.input.N; ++i)
            {
                //seenNodes[i].Add(i);

                //doesn't mean anything, just to be consistent with other implementation
                a.next_node_to[i][i] = a.input.N;

                if (a.con[i].Count == 1)
                {
                    int j = a.con[i][0];
                    //a.best_coins[a.edge_id[i][a.input.N]] = a.input.C[i];
                    //a.best_coins[ a.edge_id[j][i] ] = a.input.C[i] + a.input.C[j];

                    pq.Enqueue(new Node(i, a.input.N));
                    //prevNodes[ a.edge_id[i][a.input.N] ] 
                }
            }

            Logger.LogInfo("Build more structures");

            bool[] visitedEdges;
            Ext.createArray(out visitedEdges, a.input.N*3, false);

            bool[] visitedNodes;
            Ext.createArray(out visitedNodes, a.input.N, false);

            Logger.LogInfo("Start");

            int count = 1;
            int realCount = 1;
            #region bottom up
            while (pq.Count > 0)
            {
                ++count;
                Node node = pq.Dequeue();

                int edgeId = a.GetEdgeId(node.prev, node.current);
                   
                if (visitedEdges[edgeId])
                    continue;

                int reverseEdgeId = a.edge_id[node.prev][node.current];
                
                if (realCount++ % 100 == 0)
                    Logger.LogInfo("Real count rc = {}, {}.  Count remamining: {}", realCount, count, pq.Count);
                
                if (node.prev < a.input.N)
                {
                    int bestFromPrev = a.input.C[node.prev];
                    bool ok = true;
                    foreach (int j in a.con[node.prev])
                    {
                        if (j != node.current)
                        {
                            int nextEdgeId = a.GetEdgeId(node.prev, j);
                            //Preconditions.checkState(a.best_coins[nextEdgeId] != -1);
                            if (a.best_coins[nextEdgeId] == -1)
                            {
                                Logger.LogDebug("Skipping for now {} to {} because no data for edge id{} {} to {}.  ", 
                                    node.prev, node.current, nextEdgeId, node.prev, j);
                                pq.Enqueue(node);
                                
                                ok = false; break;
                            }
                            bestFromPrev = Math.Max(bestFromPrev, a.best_coins[nextEdgeId] + a.input.C[node.prev]);
                        }
                    }

                    if (ok)
                    {
                        Logger.LogDebug("Setting best coins for edge {} from {} to {} = {}",
                            reverseEdgeId, node.current, node.prev, bestFromPrev);
                        a.best_coins[reverseEdgeId] = bestFromPrev;
                    }
                    else
                    {
                        continue;
                    }
                }

                visitedNodes[node.current] = true;
                
                
                visitedEdges[edgeId] = true;
                if (reverseEdgeId != -1)
                {
                    visitedEdges[reverseEdgeId] = true;
                    prevNodesListEdge[edgeId].Add(node.prev);

                    foreach (int j in a.con[node.prev])
                    {
                        if (j != node.current)
                        {
                            int nextEdgeId = a.GetEdgeId(j, node.prev);

                            prevNodesListEdge[edgeId].AddRange(prevNodesListEdge[nextEdgeId]);
                        }
                    }
                }

                var p = prevNodesListEdge[edgeId];
                
                //var p = prevNodesEdge[edgeId];

               // seenNodes[node.current].UnionWith(p);

                //Logger.LogDebug("!Processing node cur={} prev={}\nvisited={}\nvisited new={}", node.current, node.prev, 
                 //   String.Join(", ", p), String.Join(", ", p2));

                for(int i = 0; i < a.input.N; ++i)
                {
                    if( p.Contains(i) == false )
                    {
                        if (node.prev < a.input.N)
                            a.next_node_to[node.prev][i] = node.current;
                    }
                    else
                    {
                        a.next_node_to[node.current][i] = node.prev;
                    }                   
                }

                
                
                foreach(int j in a.con[node.current])
                {
                    int nextEdgeId = a.edge_id[j][node.current];
                    if (visitedEdges[nextEdgeId])
                        continue;

                    //if (visitedNodes[j])
                      //  continue;
                                        
                    Node nextNode = new Node(j, node.current);
                   // Logger.LogDebug("    Adding node cur={} prev={} visited={}", j, node.current, String.Join(", ", prevNodesEdge[nextEdgeId]));
                    
                   // prevNodesEdge[nextEdgeId].Add(node.current);
                  //  prevNodesEdge[nextEdgeId].UnionWith(seenNodes[node.current]);
                   
                    pq.Enqueue(nextNode);
                }

            }
            #endregion
#region look for nodes of degree > 1 where all paths are known

            Queue<Node> nodes = new Queue<Node>();
            for (int i = 0; i < a.input.N; ++i)
            {
                List<int> unknowns = new List<int>();
                
                foreach(int j in a.con[i])
                {
                    int edgeId = a.GetEdgeId(i, j);
                    Logger.LogDebug("edge {} to {}.  best? {}", i, j, a.best_coins[edgeId]);

                    if (a.best_coins[edgeId] == -1)
                        unknowns.Add(j);

                }

                if (unknowns.Count == 0)
                {
                    foreach (int j in a.con[i])
                    {
                        Logger.LogDebug("Adding node from {} to {}", i, j);
                        nodes.Enqueue(new Node(j, i));
                    }
                }
            }
#endregion
            #region Find best paths for rest of graph
            //Looping through edges such that
            //-every branch from prev is known
            //current->prev is -1
            while (nodes.Count > 0)
            {
                Node node = nodes.Dequeue();
                int edgeId = a.GetEdgeId(node.prev, node.current);
                Logger.LogDebug("Looking at edge id {} from {} to {}",
                    edgeId, node.prev, node.current);

                int bestFromPrev = 0;
                //Assumption, every branch from prev has a know best_coins
                foreach(int prevNeigh in a.con[node.prev])
                {
                    if (prevNeigh == node.current)
                        continue;

                    int prevNeighEdge = a.GetEdgeId(node.prev, prevNeigh);
                    Preconditions.checkState(a.best_coins[prevNeighEdge] != -1);
                    bestFromPrev = Math.Max(bestFromPrev, a.best_coins[prevNeighEdge]);
                }

                //Because every branhc from prev is known, we can fill in current=>prev
                int reverseEdgeId = a.GetEdgeId(node.current, node.prev);
                Preconditions.checkState(a.best_coins[reverseEdgeId] == -1);
                a.best_coins[reverseEdgeId] = bestFromPrev + a.input.C[node.prev];

                bool allPathsFromCurrentKnown = true;

                foreach(int nextNeigh in a.con[node.current])
                {
                    if (nextNeigh == node.prev)
                        continue;

                    int nextNeighEdgeId = a.GetEdgeId(node.current, nextNeigh);
                    if (a.best_coins[nextNeighEdgeId] == -1)
                    {
                        allPathsFromCurrentKnown = false;
                        break;
                    }
                    //int nextNeighEdge = a.GetEdgeId(node.current, nextNeigh);
                    //Preconditions.checkState(a.best_coins[nextNeighEdge] == -1);                        
                }

                if (allPathsFromCurrentKnown)
                {
                    foreach (int nextNeigh in a.con[node.current])
                    {
                        if (nextNeigh == node.prev)
                            continue;

                        int nextNeighEdgeId = a.GetEdgeId(node.current, nextNeigh);
                        Preconditions.checkState(a.best_coins[nextNeighEdgeId] != -1);

                        int reverseNextEdgeId = a.GetEdgeId(nextNeigh, node.current);

                        if (a.best_coins[reverseNextEdgeId] == -1)
                        {
                            nodes.Enqueue(new Node(nextNeigh, node.current));
                        }
                        //int nextNeighEdge = a.GetEdgeId(node.current, nextNeigh);
                        //Preconditions.checkState(a.best_coins[nextNeighEdge] == -1);                        
                    }
                }
            }
            #endregion

            #region Fill in best coins from fake node N to rest
            for (int i = 0; i < a.input.N; ++i)
            {
                PriorityQueueDemo.PriorityQueue<int, int> pq2 = new PriorityQueueDemo.PriorityQueue<int,int>();

                //Assumption, every branch from prev has a know best_coins
                foreach (int neigh in a.con[i])
                {
                    
                    int neighEdge = a.GetEdgeId(i, neigh);
                    Preconditions.checkState(a.best_coins[neighEdge] != -1);

                    pq2.Enqueue(-a.best_coins[neighEdge], neigh);

                    //best = Math.Max(best, a.best_coins[neighEdge]);
                }

                List<int> bestNodes = new List<int>();

                var dq = pq2.Dequeue();                
                a.best_coins[a.GetEdgeId(a.input.N, i)] = -dq.Key + a.input.C[i];
                bestNodes.Add(dq.Value);

                for (int bestIndex = 0; bestIndex < 3 && pq2.Count > 0; ++bestIndex )
                {
                    dq = pq2.Dequeue();
                    bestNodes.Add(dq.Value);
                }

                Logger.LogDebug("Best nodes from node {} = {}", i, String.Join(", ", bestNodes));

                foreach (int neigh in a.con[i])
                {
                    int neighEdge = a.GetEdgeId(neigh, i);
                    Logger.LogDebug("best_nodes edge {} from {} to {}", neighEdge, neigh, i);
                    
                    for (int bestIndex = 0, bnIndex = 0; bestIndex < 3 && bnIndex < bestNodes.Count; ++bestIndex, ++bnIndex)
                    {
                        if (bestNodes[bnIndex] == neigh)
                        {
                            //++bnIndex;
                            --bestIndex;
                            continue;
                        }

                        a.best_nodes[neighEdge][bestIndex] = bestNodes[bnIndex];
                    }
                }

                int nFakeEdge = a.GetEdgeId(a.input.N, i);
                Logger.LogDebug("best_nodes fake edge {} from {} to {}", nFakeEdge, a.input.N, i);
                for (int bestIndex = 0; bestIndex < 3 && bestIndex < bestNodes.Count; ++bestIndex)
                {                 
                    a.best_nodes[nFakeEdge][bestIndex] = bestNodes[bestIndex];
                }
                    
            }
            #endregion
        }

        public int processInput(WillowInput input)
        {
            ccc = 0;
            //return 3;
            PreCalcArgs a = new PreCalcArgs();
            a.input = input;
            Ext.createArray(out a.edge_id, input.N+1, input.N+1, -1);
            Ext.createArrayWithNew(out a.con, input.N);
            int id = 0;
            foreach(var e in input.Roads)
            {
                int i = e.First-1;
                int j = e.Second-1;
                a.con[i].Add(j);
                a.con[j].Add(i);
                a.edge_id[i][j] = id++;
                a.edge_id[j][i] = id++;
            }

            for (int i = 0; i < input.N; i++)
            {
                a.edge_id[i][input.N] = id++;
            }
            int MAXE = input.N * 3;
            // These memoizations are reset per test case.
            Ext.createArray(out a.best_coins, MAXE, -1);
            Ext.createArray(out a.best_nodes, MAXE, 3, -1);
            Ext.createArray(out a.next_node_to, input.N,input.N,-1);

            Ext.createArray(out a.memo_rec, MAXE, MAXE,-1);
            Ext.createArray(out a.memo_branch_off, MAXE, MAXE, -1);

            //return 1;
            //Logger.LogInfo("{}", printInput(input));
            Logger.LogInfo("Starting precalc");
            // Pre-calculation.
            /*for (int i = 0; i < input.N; i++)
            {
               // Logger.LogInfo("precalc i={} N={}", i, input.N);
               // precalc(a, i, input.N, i, input.N, 0);
                foreach (int j in a.con[i])
                {
                   // precalc(a, j, i, i, j, 0);
                }
            }
            
           

            var check_best_coins = a.best_coins;
            var check_best_nodes = a.best_nodes;
            var check_next_node_to = a.next_node_to;
            Ext.createArray(out a.best_coins, MAXE, -1);
            Ext.createArray(out a.best_nodes, MAXE, 3, -1);
            Ext.createArray(out a.next_node_to, input.N, input.N, -1);
             */
            precalc_iterative(a);


           // Logger.LogDebug("best coins? {}",  Ext.CheckEquals("best_coins", check_best_coins, a.best_coins));
           // Logger.LogDebug("Best nodes {}",  Ext.CheckEquals("best_nodes", check_best_nodes, a.best_nodes));
          //  Logger.LogDebug("next node to {}", Ext.CheckEquals("next_node_to", check_next_node_to, a.next_node_to));

           // return 1;

            Logger.LogInfo("Starting loop");
            int max_diff = -1000000000;
            for (int i = 0; i < input.N; i++)
            {
                Logger.LogInfo("Starting loop i={}", i);
                int min_diff = 1000000000;
                for (int j = 0; j < input.N; j++)
                {
                    int cost = input.C[i] - (i == j ? 0 : input.C[j]);
                    min_diff = Math.Min(min_diff, cost + rec_large(a, i, input.N, j, input.N));
                }
                max_diff = Math.Max(max_diff, min_diff);
            }
            return max_diff;
        }

       
        /// <summary>
        /// 
        /// </summary>
        /// <param name="i">last vertex picked by current player</param>
        /// <param name="j">last vertex of other player</param>
        /// <param name="turn">who is making the turn.0 for first player, 1 second player</param>
        /// <returns></returns>
        public int rec(int i, int j, int turn, bool[][][] visited, WillowInput input, 
            GraphUndirectedLinkedList g, GraphUndirected gu) 
        {
            if (visited[i][j][turn])
                return 0;
  
            visited[i][j][turn] = true;

            int ci = input.C[i];
            input.C[i] = 0; //  # Remove the coins at vertex i

            int ret = int.MinValue;

            
            //foreach(var ni in neighbors)
            foreach(int ni in gu.getOutboundConnected(i))
            {
                if (g.GetNode(i, ni) == null)
                    continue;
                
                g.removeConnection(i, ni);

                ret = Math.Max(ret, -rec(j, ni, 1 - turn, visited, input, g, gu));

                g.addConnection(i, ni);                
            }
  
            if (ret == int.MinValue) 
            {
                ret = -rec(j, i, 1 - turn, visited, input, g, gu); //  # See note 5
            }
    
            input.C[i] = ci;  //# Restore the coins at vertex i

            return ret + ci;
      }

        public String printInput(WillowInput input)
        {
            Graph g = new Graph(input.N);

            foreach (var road in input.Roads)
            {
               // Logger.LogDebug("Connection {} to {}", road.First - 1, road.Second - 1);
                g.addUndirectedConnection(road.First - 1, road.Second - 1);
             
            }

            TreeInt<int> tree = g.GetTree<int>(0);
            tree.PostOrderTraversal(tree.getRoot(), node =>
            {
                node.setData(input.C[node.getId()]);
               // Logger.LogDebug("Node {} has children {}", node.getId(), String.Join(", ", node.getChildren().Select(n => n.getId())));

            });

            return tree.ToString();
        }

        public int processInputSmall(WillowInput input)
        {
            //TreeInt<int> tree = new TreeInt<int>(0);
            GraphUndirectedLinkedList g = new GraphUndirectedLinkedList(input.N);
            GraphUndirected gu = new GraphUndirected(input.N);
            foreach(var road in input.Roads)
            {
                g.addConnection(road.First - 1, road.Second - 1);
                gu.addConnection(road.First - 1, road.Second - 1);
            }

            int max = int.MinValue;

            bool[][][] visited;
            Ext.createArray(out visited, input.N, input.N, 2, false);

            for(int i = 0; i < input.N; ++i)
            {
                Logger.LogInfo("Looking at i {}", i);
                int minResponse = int.MaxValue;

                for(int j = 0; j < input.N; ++j)
                {
                    Ext.resetArray(visited, input.N, input.N, 2, false);
                   // Logger.LogInfo("Looking at j {}", j);
                    
                    /*
                     TreeInt<int> tree = g.GetTree<int>(i);
                     tree.PostOrderTraversal(tree.getRoot(), node =>
                     {
                         node.setData(input.C[node.getId()]);
                     });
                    */
                    int bestScore = rec(i,j, 0, visited, input, g, gu);

                    //Logger.LogDebug("For tree {} score is {}", tree, bestScore);
                    minResponse = Math.Min(bestScore, minResponse);
                }

                max = Math.Max(max, minResponse);
            }

            return max;
        }
        public int processInputFail(WillowInput input)
        {
            //TreeInt<int> tree = new TreeInt<int>(0);
            Graph g = new Graph(input.N);
            foreach(var road in input.Roads)
            {
                g.addUndirectedConnection(road.First - 1, road.Second - 1);
            }


            int maxScore = -1;

            for (int hannaRootNode = 0; hannaRootNode < input.N; ++hannaRootNode )
            {
                TreeInt<int> tree = g.GetTree<int>(hannaRootNode);

                int[] sums = new int[input.N];
                tree.PostOrderTraversal(tree.getRoot(), node =>
                {
                    int childSums = node.getChildren().Count == 0 ? 0 :  node.getChildren().Max(cn => cn.getData());
                    node.setData(childSums + input.C[node.getId()]);
                });

                List<int> subTreeTotals = new List<int>();
                foreach (var cn in tree.getRoot().getChildren())
                {
                    subTreeTotals.Add(cn.getData());
                }

                subTreeTotals.Sort();
                int otherScore = 0;

                if (subTreeTotals.Count > 0)
                {
                    otherScore = subTreeTotals.GetLastValue();
                    subTreeTotals.RemoveAt(subTreeTotals.Count - 1);
                }

                int myScore = input.C[hannaRootNode] + (subTreeTotals.Count == 0 ? 0 : subTreeTotals.GetLastValue()) - otherScore;

                maxScore = Math.Max(maxScore, myScore);

                tree.getRoot().setData(input.C[hannaRootNode]);
                Logger.LogDebug("Tree is {}, my score {}", tree, myScore);
            }
                
            return maxScore;
        }
    }

}