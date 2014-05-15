#define PERF
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Logger = Utils.LoggerFile;

[assembly: System.Runtime.CompilerServices.InternalsVisibleTo("UnitTest")]

namespace DataStructures
{
	
	
	
	public class Interval<K, T> : IComparable<Interval<K, T>> where K : IComparable<K>
	{
		public K start {get; private set;}
		public K stop {get; private set;}
		
		public T value {get; private set;}
		
		public Interval(K s, K ss, T v)
		{
			start = s;
			stop = ss;
			value = v;
		}
		
		public int CompareTo( Interval<K, T> rhs)
		{
			int cmp = start.CompareTo(rhs.start);
			
			if (cmp != 0)
				return cmp;
			
			return stop.CompareTo(rhs.stop);
		}
	}
	
    /**
     * 
     * Implements an interval tree
     * K is the numeric type in the interval
     * T is whatever data is associated with the interval
     */
    public class IntervalTree<K, T> where K : IComparable<K>, IEquatable<K>, new()
    {
    	
    	List<Interval<K, T>> intervals;
    	IntervalTree<K, T> left;
    	IntervalTree<K, T> right;
    	K center;
    	

    	public static IntervalTree<K,T> CreateIntervalTree(List<Interval<K,T>> ivals,
				K leftextent ,
				K rightextent,
				int depth = 16) 
        {
        	// sort intervals by start
			ivals.Sort();
			IntervalTree<K,T> ret = new IntervalTree<K,T>(ivals, leftextent, rightextent, depth);
			return ret;
        }

        
		
		
		private IntervalTree(
				List<Interval<K,T>> ivals,
				K leftextent ,
				K rightextent,
				int depth = 16,
				int minbucket = 64,
				int maxbucket = 512				
				)
			
		{
			Logger.LogTrace("IntervalTree() ivals count {} extents {} to {}.  Depth {}",
				ivals.Count, leftextent, rightextent, depth);
			--depth;
			
			if (depth == 0 || (ivals.Count < minbucket && ivals.Count < maxbucket)) {
				//ivals.Sort(); 
				intervals = ivals;
			} else {
				
				
				if (leftextent.Equals( new K() ) && rightextent.Equals( new K()) ) {
					 
				}
	
				K leftp = new K();
				K rightp = new K();
				K centerp = new K();
				
				if ( !leftextent.Equals( new K() ) || !rightextent.Equals( new K()) ) {
					leftp = leftextent;
					rightp = rightextent;
				} else {
					leftp = ivals[0].start;
					rightp = ivals.Max( (i) => i.stop );
				}
	
				//centerp = ( leftp + rightp ) / 2;
				centerp = ivals[ivals.Count / 2].start;
				center = centerp;
	
				intervals = new List<Interval<K,T>>(); 
				List<Interval<K,T>> lefts = new List<Interval<K,T>>();
				List<Interval<K,T>> rights = new List<Interval<K,T>>();
				
				Logger.LogTrace("Classing left/right contains using center as {}", centerp); 
	
				foreach ( Interval<K,T> interval in ivals) 
				{
					Logger.LogTrace( "Looking at interval [{} - {}]", interval.start, interval.stop);
					if (interval.stop.CompareTo(center) < 0) {
						Logger.LogTrace("Adding left");
						lefts.Add(interval);
					} else if (interval.start.CompareTo(center) > 0) {
						Logger.LogTrace("Adding right");
						rights.Add(interval);
					} else {
						Logger.LogTrace("Adding center");
						intervals.Add(interval);
					}
				}
	
				if (lefts.Count > 0) {
					Logger.ChangeIndent(4);
					//Logger.LogTrace("Bumping indent");
					left = new IntervalTree<K,T>(lefts, leftp, centerp, depth, minbucket);
					Logger.ChangeIndent(-4);
				}
				if (rights.Count > 0) {
					Logger.ChangeIndent(4);
					right = new IntervalTree<K,T>(rights, centerp, rightp, depth, minbucket);
					Logger.ChangeIndent(-4);
				}
			}
		}
		
		public void findOverlapping(K start, K stop, List<Interval<K,T>> overlapping) 
		{
			Logger.LogTrace("Find overlap query [{} - {}]", start, stop);
			if (intervals.Count > 0 && ! (stop.CompareTo(intervals[0].start) < 0)) 
			{
				Logger.LogTrace("Checking contained intervals because {} >= first interval start {}",
					stop, intervals[0].start);
				
				foreach ( Interval<K,T> interval in intervals)
				{
					Logger.LogTrace("Checking [{} - {}]", interval.start, interval.stop);
					if (interval.stop.CompareTo(start) >= 0 && interval.start.CompareTo(stop) <= 0) {
						Logger.LogTrace("Overlaps");
						overlapping.Add(interval);
					}
				}
			}
	
			if (left != null && start.CompareTo(center) <= 0) 
			{
				Logger.ChangeIndent(4);
				left.findOverlapping(start, stop, overlapping);
				Logger.ChangeIndent(-4);
			}
	
			if (right != null && stop.CompareTo(center) >= 0) 
			{
				Logger.ChangeIndent(4);
				right.findOverlapping(start, stop, overlapping);
				Logger.ChangeIndent(-4);
			}
	
		}
	
		public void findContained(K start, K stop, List<Interval<K,T>> contained) 
		{
			if (intervals.Count > 0 && ! (stop.CompareTo(intervals[0].start) < 0))
			{
				foreach ( Interval<K,T> interval in intervals)
				{
					if (interval.start.CompareTo(start) >= 0 && interval.stop.CompareTo(stop) <= 0) 
					{
						contained.Add(interval);
					}
				}
			}
	
			if (left != null && start.CompareTo(center) <= 0)
			{
				left.findContained(start, stop, contained);
			}
	
			if (right != null && stop.CompareTo(center) >= 0) 
			{
				right.findContained(start, stop, contained);
			}
	
		}

	}
}
