#define LOGGING_TRACE
#define LOGGING
using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;

using Logger = Utils.LoggerFile;

namespace Round2
{
    public class Erdos : InputFileConsumer<Input, string>
    {

        public string processInput(Input input)
        {
            List<int> seq = new List<int>(new int[] { 4, 5, 3, 7, 6, 2, 8, 1 });

            int[] lis = getLIS(seq);

            for (int listSize = 1; listSize <= 3; ++listSize)
            {
                int[] permBase = new int[listSize];
                for (int i = 0; i < listSize; ++i)
                    permBase[i] = i + 1;

                foreach (IList<int> list in nextPermutation(permBase))
                {
                    Logger.Log(string.Join(", ", list));
                    for (int greatlistSize = 1; listSize <= 3; ++listSize)
                }
            }
            return string.Join(" ", lis);
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
                Logger.LogTrace("i: [{0}] X[i]: [{1}] lo {2} hi {3} M {4}", i, X[i], lo, hi, M.ToCommaString());
            
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
            Logger.Log("LIS of {0} is {1}", string.Join(", ", X), string.Join(", ", S));
            return S;
        }

        IEnumerable<IList<T>> nextPermutation<T>(IList<T> array) where T : IComparable<T>
        {
            while (true)
            {
                yield return array;

                // Find longest non-increasing suffix
                int i = array.Count - 1;
                while (i > 0 && array[i - 1].CompareTo(array[i]) >= 0)
                    i--;
                // Now i is the head index of the suffix

                // Are we at the last permutation already?
                if (i == 0)
                    yield break;

                // Let array[i - 1] be the pivot
                // Find rightmost element that exceeds the pivot
                int j = array.Count - 1;
                while (array[j].CompareTo(array[i - 1]) <= 0)
                    j--;
                // Now the value array[j] will become the new pivot
                // Assertion: j >= i

                // Swap the pivot with j
                T temp = array[i - 1];
                array[i - 1] = array[j];
                array[j] = temp;

                // Reverse the suffix
                j = array.Count - 1;
                while (i < j)
                {
                    temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                    i++;
                    j--;
                }

                // Successfully computed the next permutation
                //yield return array;
            }
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
