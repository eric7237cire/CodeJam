#define bob2
#if bob
#define LOGGING
#define LOGGING_DEBUG
#define LOGGING_INFO
#define LOGGING_TRACE
#endif

using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;
using Utils;
using Priority_Queue;
using Logger = Utils.LoggerFile;

namespace Round3
{
	using II = Tuple<int,int>;
	
    public class Lost : InputFileConsumer<LostInput, string>, InputFileProducer<LostInput>
    {
        public class SearchCon
        {
            internal List<List<int>> sp;
            internal LostInput input;
            internal void findAllShortestPaths(List<int> curPath, bool[] visited, int curMinCost, int curMaxCost)
            {
                if ( curPath.Count > 0 && input.to[curPath.GetLastValue()] == 2)
                {
                    Logger.LogDebug("Path found {} min cost {} max cost {}", 
                        curPath.Select( (cIdx) => "Shuttle #{0}. From {1} To {2}.  Cost [{3}-{4}]".FormatThis(cIdx+1, input.from[cIdx], input.to[cIdx], input.lowCost[cIdx], input.highCost[cIdx]) )
                            .ToCommaString(), 
                        curMinCost, curMaxCost);
                    sp.Add(new List<int>(curPath));
                }
                for(int conIdx = 0; conIdx < input.nConnections; ++conIdx)
                {
                    if (curPath.Count > 0 && input.from[conIdx] != input.to[curPath.GetLastValue()])
                        continue;

                    if (curPath.Count == 0 && input.from[conIdx] != 1)
                        continue;

                    int to = input.to[conIdx];

                    if (visited[to])
                        continue;

                    curPath.Add(conIdx);
                    visited[to] = true;
                    findAllShortestPaths(curPath, visited, curMinCost + input.lowCost[conIdx], curMaxCost + input.highCost[conIdx]);
                    visited[to] = false;
                    curPath.RemoveAt(curPath.Count - 1);
                }
            }
        }
        
        public string processInputBruteForce(LostInput input)
        {
            for(int i = 0; i < input.nConnections; ++i)
            {
                Logger.LogTrace("Shuttle #{} from {} to {}  cost [ {} ] - [ {} ]", i + 1, input.from[i], input.to[i],
                    input.lowCost[i], input.highCost[i]);
            }
            SearchCon sc = new SearchCon();
            sc.sp = new List<List<int>>();
            sc.input = input;
            List<int> path = new List<int>();
            //path.Add(1);
            
            sc.findAllShortestPaths(path, new bool[input.nCities+1], 0, 0);
            
            for(int i = 0; i < sc.sp.Count; ++i)
            {
            	List<int> path1 = sc.sp[i];
            	
            	for(int j = i+1; j < sc.sp.Count; ++j)
            	{
					List<int> path2 = sc.sp[j];
					
					//Common 
					IEnumerable<int> common = path1.Where( (edgeIdx) => path2.Contains(edgeIdx) );
					
					IEnumerable<int> path1_f = path1.Where( (edgeIdx) => !common.Contains(edgeIdx) );
					IEnumerable<int> path2_f = path2.Where( (edgeIdx) => !common.Contains(edgeIdx) );
					
					
					
					Func<Tuple<int, int>, int, Tuple<int, int>> funcSumCost = ( Tuple<int, int> seed, int conIdx ) => 
					{ 
						return new Tuple<int,int>( seed.Item1 + input.lowCost[conIdx], seed.Item2 + input.highCost[conIdx]);
					};
					
					II minMaxCost1 = path1_f.Aggregate( new II(0,0), funcSumCost);
					II minMaxCost2 = path2_f.Aggregate( new II(0,0), funcSumCost);
					
					Logger.LogTrace("Path {} cost {}.  Path {} cost {}",
						path1_f.ToCommaString(),
						minMaxCost1,
						path2_f.ToCommaString(),
						minMaxCost2);
					
					if (minMaxCost2.Item1 > minMaxCost1.Item2)
					{
						Logger.LogTrace("Removing 2nd path");
						sc.sp.RemoveAt(j);
						--j;
						continue;
					}
					if (minMaxCost1.Item1 > minMaxCost2.Item2)
					{
						Logger.LogTrace("Removing 1st path");
						sc.sp.RemoveAt(i);
						--i;
						break;
					}
            	}
            }

            Logger.LogTrace("Suggested path {}", input.sugPath.ToCommaString());
            
            for(int spIdx = 0; spIdx < input.sugPath.Length; ++spIdx)
            {
            	bool found = false;
            	
            	IEnumerable<int> sugPathPrefix = input.sugPath.Take(spIdx+1).Select( (idx1) => idx1-1 );
            	Logger.LogDebug( "sugPathPrefix {}", sugPathPrefix.ToCommaString());
            	for(int i = 0; i < sc.sp.Count; ++i)
            	{
					List<int> path1 = sc.sp[i];
					Logger.LogDebug( "path1 {}", path1.ToCommaString());
					
					 Logger.LogDebug("Look at {} ", 
                        path1.Select( (cIdx) => "Shuttle #{0}. From {1} To {2}. ".FormatThis(cIdx+1, input.from[cIdx], input.to[cIdx], input.lowCost[cIdx], input.highCost[cIdx]) )
                            .ToCommaString()  
                        );
                     
					if (path1.Take(spIdx+1).SequenceEqual( sugPathPrefix ))
					{
						found = true;
						break;
					}
            	}
            	
            	if (!found)
            	{
            		return "" + input.sugPath[spIdx];	
            	}
            }
            
            
            return "Looks Good To Me";
        }

        public string processInput(LostInput input)
        {
            for (int i = 0; i < input.nConnections; ++i)
            {
                Logger.LogTrace("Shuttle #{} from {} to {}  cost [ {} ] - [ {} ]", i + 1, input.from[i], input.to[i],
                    input.lowCost[i], input.highCost[i]);
            }
            
            for(int i = 0; i < input.suggPathLen; ++i)
            {
                List<int> pathPrefix = new List<int>(input.sugPath.Take(i + 1).Select( (idx) => idx - 1));

                bool ok = doDijkstra(pathPrefix, input.nCities, input);

                if (!ok)
                {
                    return "" + input.sugPath[i];
                }
            }

            return "Looks Good To Me";
        }
        
        internal class DijkstraNode : IComparable<DijkstraNode>
        {
        	internal int nodeId;
        
			/**
			 * distance from start to nodeId
			 */
			internal double distance;
        
        
			internal int previous;
			
			public DijkstraNode(int nodeId, double distance) {
				this.nodeId = nodeId;
				this.distance = distance;
				this.previous = -1;
			}
		
            //Highest distance first
            public int CompareTo(DijkstraNode rhs)
            {
 	            return rhs.distance.CompareTo(distance);
            }
        }
		
		/**
		 * Indexes in graph go from 0 to nodeCount - 1
		 */
		 
		internal bool doDijkstra(List<int> pathPrefix, int nodeCount, LostInput input) {

            int sourceNodeId = 1;
            int targetNodeId = 2;

            int[] actualEdgeCosts = new int[input.lowCost.Length];

            //First some processing unique to the problem

            double tokyoCost = 0;
            //All edges
            foreach(int connIdx in pathPrefix)
            {
                actualEdgeCosts[connIdx] = input.lowCost[connIdx];
                tokyoCost += input.lowCost[connIdx];
            }
            tokyoCost -= .5;

            int tokyoIndex = input.to[pathPrefix.GetLastValue()];
			

            //Node ids are the city ids (1 index based)
			DijkstraNode[] dijNodes = new DijkstraNode[nodeCount + 1];
			
			for(int n = 0; n < dijNodes.Length; ++n) {
				dijNodes[n] = new DijkstraNode(n, int.MaxValue);
			}
			
			HeapPriorityQueue<DijkstraNode, double> toProcess = new HeapPriorityQueue<DijkstraNode, double>(nodeCount);
			
			dijNodes[sourceNodeId].distance = 0;
            dijNodes[tokyoIndex].distance = tokyoCost;

			toProcess.Enqueue( dijNodes[sourceNodeId], dijNodes[sourceNodeId].distance );
            toProcess.Enqueue(dijNodes[tokyoIndex], dijNodes[tokyoIndex].distance); 
			
			while(toProcess.Count > 0) 
			{
				DijkstraNode nodeU = toProcess.Dequeue();

                Preconditions.checkState(nodeU.distance < int.MaxValue, "NodeU distance is inf");
                bool isGoodRobot = ((int)((nodeU.distance + 0.001) * 2)) % 2 == 1 ? true : false;

                if (nodeU.nodeId == 2)
                {
                    return isGoodRobot;
                }

				//Find all neighbors
				for(int conIdx = 0; conIdx < input.nConnections; ++conIdx)
                {
                    //Not connected to nodeU
                    if (input.from[conIdx] != nodeU.nodeId)
                        continue;

                    int to = input.to[conIdx];

                    if (isGoodRobot)
                    {
                        if (actualEdgeCosts[conIdx] == 0)
                        	actualEdgeCosts[conIdx] = input.lowCost[conIdx];
                    }
                    else
                    {
                        if (actualEdgeCosts[conIdx] == 0)
                            actualEdgeCosts[conIdx] = input.highCost[conIdx];
                    }
                    double alt = actualEdgeCosts[conIdx] + nodeU.distance;
					
					/**
					 * Is path via u shorter than current distance
					 * of start to v ?
					 */
					DijkstraNode nodeV = dijNodes[to];
					
					//Found a new best path
					if (alt < nodeV.distance) {
						
                        nodeV.previous = nodeU.nodeId;
						nodeV.distance = alt;
						//Clear non shorest paths
						
						toProcess.Enqueue(nodeV, nodeV.distance);
					}
					
				}
			}
			
            //Error!
			return false;
								 
		}
		

        public LostInput createInput(Scanner scanner)
        {
            Logger.LogTrace("create Input");
            LostInput input = new LostInput();

            input.nCities = scanner.nextInt();
            input.nConnections = scanner.nextInt();
            input.suggPathLen = scanner.nextInt();

            input.from = new int[input.nConnections];
            input.to = new int[input.nConnections];
            input.lowCost = new int[input.nConnections];
            input.highCost = new int[input.nConnections];

            input.sugPath = new int[input.suggPathLen];

            for(int i = 0; i < input.nConnections;++i)
            {
                input.from[i] = scanner.nextInt();
                input.to[i] = scanner.nextInt();
                input.lowCost[i] = scanner.nextInt();
                input.highCost[i] = scanner.nextInt();
            }

            for (int i = 0; i < input.suggPathLen; ++i)
            {
                input.sugPath[i] = scanner.nextInt();
            }

            return input;
            //typeof(LostInput).getm
            // get the member information and use it to
            // retrieve the custom attributes
            System.Reflection.MemberInfo inf = typeof(LostInput);
            object[] attributes;
            attributes =
               inf.GetCustomAttributes(
                    typeof(AutoReadAttribute), false);

            // iterate through the attributes, retrieving the 
            // properties
            foreach (var attribute in attributes)
            {
                AutoReadAttribute bfa = (AutoReadAttribute)attribute;
                Logger.LogInfo("\nBugID: {}", bfa.readOrder);
            }

            FieldInfo[] fields = input.GetType().GetFields(BindingFlags.Instance | BindingFlags.NonPublic | BindingFlags.Public | BindingFlags.FlattenHierarchy);

            FieldInfo[] inReadOrder = new FieldInfo[10];

            foreach (FieldInfo field in fields)
            {
                foreach (Attribute at in field.GetCustomAttributes(true))
                    if (at is AutoReadAttribute)
                    {
                        //field.SetValue(obj, Utils.DefaultValue(field.FieldType));
                        Logger.LogInfo("Found {}, field {}", at, field);
                        inReadOrder[((AutoReadAttribute)at).readOrder] = field;
                        
                    }
            }

            for (int i = 0; i < inReadOrder.Length; ++i )
            {
                if (inReadOrder[i] == null)
                    break;

                FieldInfo field = inReadOrder[i];
                if (field.FieldType == typeof(int))
                {
                    field.SetValue(input, scanner.nextInt());
                }
            }

                Logger.LogInfo("nCities {} {} {}", input.nCities, input.nConnections, input.suggPathLen);
            return input;
        }

        
    }

    [AttributeUsage(AttributeTargets.Field | AttributeTargets.Property)]
    public class AutoReadAttribute : System.Attribute
    {
        public int readOrder { get; set; }
        public int subReadOrder { get; set; }
        public string loopField { get; set; }
        public AutoReadAttribute(int ro)
        {
            readOrder = ro;
        }

        public AutoReadAttribute(int ro, int subRO, string loopField = null)
        {
            readOrder = ro;
            subReadOrder = subRO;
            this.loopField = loopField;
        }
    }

    public class LostInput
    {
        [AutoRead(0)]
        internal int nCities;
        [AutoRead(1)]
        internal int nConnections;
        [AutoRead(2)]
        internal int suggPathLen;

        [AutoRead(3, 0, "nConnections")]
        internal int[] from;
        [AutoRead(3, 1)]
        internal int[] to;
        [AutoRead(3, 2)]
        internal int[] lowCost;
        [AutoRead(3, 3)]
        internal int[] highCost;

        [AutoRead(4, 0, "suggPathLen")]
        internal int[] sugPath;

    }
}
