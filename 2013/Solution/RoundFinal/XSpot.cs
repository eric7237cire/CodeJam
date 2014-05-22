#if DEBUG
#define LOGGING_DEBUG
#define LOGGING_INFO
//#define LOGGING_TRACE
#endif

using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils.geom;
using Utils.math;
using Utils;

using Logger = Utils.LoggerFile;

namespace RoundFinal
{
    using Point = Utils.geom.Point<int>;

    public class XSpot : InputFileProducer<XSpotInput>, InputFileConsumer<XSpotInput, string>
    {
        public XSpotInput createInput(Scanner scanner)
        {
            throw new NotImplementedException();
        }

        public string processInput(XSpotInput input)
        {
            throw new NotImplementedException();
        }

        //Where no 3 lines are colinear, attempts N then removes colinear
        public static List<Point> generateTestSet(int N, int min = 0, int max = 100)
        {
        	 Random r = new Random(3);
        	 
        	 List<Point> points = new List<Point>();
        	 
        	 for(int i = 0; i < N; ++i)
        	 {
        	 	points.Add(new Point(r.Next(min, max+1), r.Next(min, max+1)) );
        	 }
        	 
        	 List<int> listIdxColinear;
        	 List<int> duplicates;
        	 
        	 FindColinear(points, out listIdxColinear, out duplicates);
        	 
        	 for(int dupIdx = 1; dupIdx < duplicates.Count; dupIdx += 2)
        	 {
        	 	points[dupIdx] = null;
        	 }
        	 
        	 for(int coLinIdx = 2; coLinIdx < coLinIdx.Count; coLinIdx += 3)
        	 {
        	 	points[coLinIdx] = null;
        	 }
        	 
        	 points.RemoveAll(p => p == null);
        	 
        	 #if DEBUG
        	 FindColinear(points, out listIdxColinear, out duplicates);
        	 Preconditions.checkState(listIdxColinear.Count == 0);
        	 Preconditions.checkState(duplicates.Count == 0);
        	 #endif
        	 
        	 return points;
        }

        //Returns list of colinear points in O(N), so for(idx=0; idx+=3) idx idx+1 idx+2 are colinear.  dups are not included in colinear list
        public static void FindColinear(List<Point> points, out List<int> listIdxColinear, out List<int> duplicates)
        {
        	listIdxColinear = new List<int>();
        	duplicates = new List<int>();
        	
        	bool[] isDup = new bool[points.Count];
        	
        	for(int p0Idx = 0; p0Idx < points.Count; ++p0Idx)
        	{
        		Dictionary<Point, int> slopePointMap = new Dictionary<Point, int>();
        		
        		Logger.LogTrace("Starting {} {}", p0Idx, points[p0Idx]);
        		
        		//Find all slopes with other points
        		for(int pIdx = p0Idx + 1; pIdx < points.Count; ++pIdx)
        		{
        			if (isDup[pIdx])
        				continue;
        		
        			Point dxdy = points[pIdx] - points[p0Idx];
        			
        			if (dxdy.Y == 0 && dxdy.X == 0)
        			{
        				duplicates.Add(p0Idx);
        				duplicates.Add(pIdx);
        				isDup[pIdx] = true;
        				continue;
        			}
        			
        			int gcd = (int) ModdedLong.gcd_recursive(dxdy.X, dxdy.Y);
        			if (gcd != 0) dxdy /= gcd;
        			
        			Logger.LogTrace("\u0394x\u0394y between [idx : point] {} {} and {} {} is {}",
        				p0Idx, points[p0Idx], pIdx, points[pIdx], dxdy); 
        			
        			if (slopePointMap.ContainsKey(dxdy))
        			{
        				int pPrevIdx = slopePointMap[dxdy];
        				
        				if (points[pPrevIdx].Equals(points[pIdx]))
        				{
        					//Check 2 other points are not the exact same
        					isDup[pIdx] = true;
        					duplicates.Add(pPrevIdx);
        					duplicates.Add(pIdx);
        					continue;
        				}
        				listIdxColinear.Add(p0Idx);
        				
        				listIdxColinear.Add(pPrevIdx);
        				listIdxColinear.Add(pIdx);
        				continue;
        			}
        			
        			slopePointMap[dxdy] = pIdx;
        		}
        			
        	}
        	
        	//return listIdxColinear;
        }
    }



    public class XSpotInput
    {
        public int N;
        public List<Point<int>> points;
    }
}
