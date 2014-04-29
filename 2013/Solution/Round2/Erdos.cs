#define LOGGING_TRACE
//#define LOGGING_DEBUG
#define LOGGING
using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;
using CombPerm;

using Logger = Utils.LoggerFile;

namespace Round2.ErdosNS
{
    public class Input
    {

        internal int length { get; private set; }
        internal int[] A { get; private set; }
        internal int[] B { get; private set; }

        public static Input createInput(Scanner scanner)
        {
            Input input = new Input();
            input.length = scanner.nextInt();

            input.A = new int[input.length];
            input.B = new int[input.length];

            for (int i = 0; i < input.length; ++i )
            {
                input.A[i] = scanner.nextInt();
            }
            for (int i = 0; i < input.length; ++i)
            {
                input.B[i] = scanner.nextInt();
            }


            return input;
        }


    }

    class Graph
    {
        List<int>[] outgoingEdges;
        List<int>[] incomingEdges;

        enum DFS_STATE
        {
            NON_INIT = 0,
            VISITING = 1,
            VISITED = 2
        }
        DFS_STATE[] dfs_num;

        private int[] topoSort;
        internal int topoSortSize;

        internal Graph(int maxNodes)
        {
            outgoingEdges = new List<int>[maxNodes];
            incomingEdges = new List<int>[maxNodes];
            for (int i = 0; i < maxNodes; ++i)
            {
                outgoingEdges[i] = new List<int>();
                incomingEdges[i] = new List<int>();
            }
            
        }

        internal int[] getTopoSortUsingDFS(int root)
        {
            dfs_num = new DFS_STATE[incomingEdges.Count()];
            topoSort = new int[incomingEdges.Count()];
            dfs(root);
            Array.Reverse(topoSort);
            return topoSort;
        }

        internal int[] getTopoSortUsingKahn()
        {
            //Find roots
            SortedSet<int> noIncEdges = new SortedSet<int>();

            for (int i = 0; i < incomingEdges.Count(); ++i)
            {
                if (incomingEdges[i].Count == 0)
                    noIncEdges.Add(i);
            }

            topoSortSize = 0;
            topoSort = new int[incomingEdges.Count()];
            while(noIncEdges.Count > 0)
            {
                int n = noIncEdges.Min;
                topoSort[topoSortSize++] = n;
                Logger.LogDebug("Adding {0} to topo sort", n);
                bool ok = noIncEdges.Remove(n);
                Preconditions.checkState(ok);

                for(int j = outgoingEdges[n].Count - 1; j >= 0 ; --j)
                {
                    int m = outgoingEdges[n][j];
                    Logger.LogDebug("Looking at edge {0}->{1}", n, m);
                    removeConnection(n, m);

                    if (incomingEdges[m].Count == 0)
                    {
                        Logger.LogDebug("{0} has no incoming edges", m);
                        noIncEdges.Add(m);
                    }
                }
            }

            return topoSort;
        }

        internal void removeConnection(int u, int v)
        {
            outgoingEdges[u].Remove(v);
            incomingEdges[v].Remove(u);
        }
        internal void addConnection(int u, int v)
        {
            Logger.LogDebug("Add connection {0} to {1}", u, v);
            outgoingEdges[u].Add(v);
            incomingEdges[v].Add(u);
        }

        //False if topo sort impossible
        private bool dfs(int u)
        {    
            dfs_num[u] = DFS_STATE.VISITING;
            Logger.LogDebug("dfs u: {0}", u);

            for (int j = 0; j < outgoingEdges[u].Count; j++)
            {
                int v = outgoingEdges[u][j];
                Logger.LogDebug("dfs u: {0} connected to v: {1}", u, v);

                if (dfs_num[v] == DFS_STATE.VISITING)
                {
                    Logger.LogDebug("Visting, no toposort");
                    return false;
                }

                if (dfs_num[v] == DFS_STATE.NON_INIT)
                {
                    Logger.LogDebug("Non init {0}", v);
                    bool ok = dfs(v);
                    if (!ok) return false;
                }

                //Ok to be connected to a visited Vertex, since we know there are no back edges
            }

            Logger.LogDebug("Toposort {0} = {1}", topoSortSize, u);
            topoSort[topoSortSize++] = u;

            //Once all children have been explored, visited
            dfs_num[u] = DFS_STATE.VISITED;
            return true;
        }
    }
    public class Erdos : InputFileConsumer<Input, string>
    {
        private void calcAandB(IList<int> list)
        {
            for(int i = 0; i < list.Count; ++i)
            {
                int val = list[i];
                //For LIS, everything to right of i is gone, and all numbers > val
                int[] lis = getLIS(new List<int>(list.Where((value, index) => index <= i && value <= val)));

                //LDS, everything to left of i is gone, and all numbers > val
                int[] lds = getLDS(new List<int> ( list.Where( (value, index) => index >= i && value <= val)));

                Logger.Log("i: {0} x: {1} A[i] = {2} B[i] = {3}", i, val, lis.Count(), lds.Count());
            }
        }


        public string processInput(Input input)
        {
            Graph g = new Graph(input.length);

            int[] lastValueIndex = new int[input.length+1];
            for (int u = 0; u < input.length; ++u)
            {
                lastValueIndex[u] = -1;
            }

            for (int u = 0; u < input.length; ++u )
            {
                int val = input.A[u];
                Logger.LogDebug("Element {0} A[i] {1}", u, val);
                if (lastValueIndex[val] != -1)
                {
                    g.addConnection(u, lastValueIndex[val]); // u < lvi[val]
                }
                if (lastValueIndex[val-1] != -1)
                {
                    g.addConnection(lastValueIndex[val-1], u); 
                }

                lastValueIndex[val] = u;
            }

            for (int u = 0; u < input.length; ++u)
            {
                lastValueIndex[u] = -1;
            }

            for(int u = input.length - 1; u >= 0; --u)
            {
                int val = input.B[u];
                Logger.LogDebug("Element {0} B[i]= {1}", u, val);

                if (lastValueIndex[val] != -1)
                {
                    g.addConnection(u, lastValueIndex[val]); // u < lvi[val]
                }
                if (lastValueIndex[val-1] != -1)
                {
                    g.addConnection(lastValueIndex[val-1], u); 
                }

                lastValueIndex[val] = u;
            }

            int root = -1;
            for (int u = 0; u < input.length; ++u )
            {
                if (input.A[u] == 1 && input.B[u] == 1) {
                    root = u;
                    break;
                }
            }

            //int[] topoSort = g.getTopoSortUsingDFS(root);
            int[] topoSort = g.getTopoSortUsingKahn();

           // ;

            int[] ans = new int[input.length];
            for (int u = 0; u < input.length; ++u)
            {
                ans[topoSort[u]] = u + 1;
            }
            //return g.topoSort.Select((elem, idx) => { return 1 + elem; }).ToCommaString(); 
            calcAandB(ans);

            return string.Join(" ", ans);
            //return g.topoSort.ToCommaString();
        }

        public string processInput2(Input input)
        {
            calcAandB(new int[] { 4, 5, 3, 7, 6, 2, 8, 1 });
            //List<int> seq = new List<int>(new int[] { 4, 5, 3, 7, 6, 2, 8, 1 });

           // int[] lis = getLIS(seq);

            for (int listSize = 1; listSize <= 6; ++listSize)
            {
                int[] permBase = new int[listSize];
                for (int i = 0; i < listSize; ++i)
                    permBase[i] = i + 1;

                Logger.Log("\n\nList size {0}", listSize);

                foreach (int[] list in Combinations.nextPermutation<int, int[]>(permBase))
                {
                    Logger.Log("\nPermutation {0}", list.ToCommaString());

                    calcAandB(list);
                    /*
                    for (int maxElement = listSize; maxElement >= 1; --maxElement)
                    {
                        IEnumerable<int> sub = list.Where(i => i <= maxElement);

                        Logger.Log("Sub list {0}", sub.ToCommaString());

                        List<int> subList = new List<int>(sub);

                        int[] lis = getLIS(subList);
                        int[] lds = getLDS(subList);

                        Logger.Log("Longest increasing subsequence size {0} : {1}", lis.Count(), lis.ToCommaString());
                        Logger.Log("Longest decreasing subsequence size B[ {0} : {1}", lds.Count(), lds.ToCommaString());
                    }*/
                }
            }
            return " ";
        }

        static public int[] getLIS(IList<int> X )
        {
            return getLongestSequence(X, Comparer<int>.Default.Compare);
        }

        static public int[] getLDS(IList<int> X)
        {
            return getLongestSequence(X, ( lhs,  rhs) => { return rhs.CompareTo(lhs); }); 
        }

        //Stricly increasing
        static public int[] getLongestSequence(IList<int> X, Comparison<int> cmp )
        {
            
            int N = X.Count;
            /**
             * P stores the previous element in the longest subsequence (as an index of x), 
             * for each number, and is updated as the algorithm advances. 
             * For example, when we process 8, we know it comes after 0, 
             * so store the fact that 8 is after 0 in P.
             * You can work backwards from the last number like a linked-list to get the whole sequence.
             */
            int[] P = new int[N]; 
            /*
             * m keeps track of the best subsequence of each length found so far.
             * The best is the one with the smallest ending value (allowing a wider range of
             * values to be added after it). The length and ending value is the 
             * only data needed to be stored for each subsequence.
             * 
             * Each element of m represents a subsequence. For m[j],
             * j is the length of the subsequence.
             * m[j] is the index (in x) of the last element of the subsequence.
             * so, x[m[j]] is the value of the last element of the subsequence.
             */
            int[] M = new int[N + 1];

            int L = 0;
            
            for (int i = 0; i < N; ++i)
            {
                // Binary search for the largest positive j ≤ L
                // such that X[M[j]] < X[i].  
                int lo = 1;
                int hi = L;
                while (lo <= hi)
                {
                    int mid = (lo+hi)/2;
                    if ( cmp(X[M[mid]], X[i]) < 0 )
                        //Actually looking for first/lowest element that is >= X[i], X[i] would then take it's place possibily
                        lo = mid+1;
                    else
                        hi = mid-1;
                }
               
                
               // After searching, lo is 1 greater than the
                // length of the longest prefix of X[i]
                int newL = lo;

                //Either we have found a new longer prefix or the first element i in M such that X[M[i]] is <= X[i]
                Preconditions.checkState(newL > L || cmp(X[M[lo]], X[i]) >= 0);
                //Logger.LogTrace("i: [{0}] X[i]: [{1}] lo {2} hi {3} M {4}", i, X[i], lo, hi, M.ToCommaString());
            
                // The predecessor of X[i] is the last index of 
                // the subsequence of length newL-1
                P[i] = M[newL-1];

                 if (newL > L)
                 {
                     // If we found a subsequence longer than any we've
                     // found yet, update M and L
                     M[newL] = i;
                     L = newL;
                 } else if (cmp(X[i], X[M[newL]]) < 0)
                 {
                     // If we found a smaller last value for the
                     // subsequence of length newL, only update M
                     M[newL] = i;
                 }
            }
     
            // Reconstruct the longest increasing subsequence
            int[] S = new int[L];
            int k = M[L];
            for(int i = L-1; i >= 0; --i)
            {
                S[i] = X[k];
                k = P[k];
            }
            //Logger.Log("LIS of {0} is {1}", string.Join(", ", X), string.Join(", ", S));
            return S;
        }

        

        static public List<int> getLIS2(List<int> seq)
        {
            int[] maxLISLength = new int[seq.Count]; //vi dp(seq.size(), 0);
            int[] pred = new int[seq.Count];

            //This is not the actual subsequence in seq, but a record of the lowest endpoint of a longest increasing subsequence 
            //lis[i] = the lowest endpoint of a lis of length i + 1
            List<int> lis = new List<int>();
	    
	        lis.Add(seq[0]);
	
	        int globalMax = 0;

	        for(int dpIdx = 1; dpIdx < seq.Count; ++dpIdx)
	        {
		        //Eveything before it is strictly less than seq[dpIdx]
		        int it = lis.lowerBound(seq[dpIdx]);
		
		        //This will be at most the length of the sequence - 1
		        int curMax = it;

		        maxLISLength[dpIdx] = curMax;
		
		        if (it == lis.Count)
		        {
                    //can make a new longest sequence
			        lis.Add(seq[dpIdx]);
		        } else {
			        //Found an element 
			        lis[it] = seq[dpIdx];
		        }

		        globalMax = Math.Max(globalMax, curMax);
	        }

            return lis;
	

	
        }
    }
}
