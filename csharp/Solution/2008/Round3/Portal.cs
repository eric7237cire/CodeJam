#if DEBUG
#define LOGGING_DEBUG
#define LOGGING_INFO
#define LOGGING_TRACE
#endif

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using CodeJamUtils;
using Utils;
using Utils.tree;
using Logger = Utils.LoggerFile;

using CodeJam.Utils.graph;
using Utils.geom;
using NUnit.Framework;
using Priority_Queue;

namespace Round3_2008.Problem2
{
    public class PortalInput
    {
        public int R;
        public int C;
        public string[] grid;

        public Point<int> start;
        public Point<int> finish;
    }

    public class Portal : InputFileProducer<PortalInput>, InputFileConsumer<PortalInput, string>
    {
        public PortalInput createInput(Scanner scanner)
        {
            PortalInput input = new PortalInput();
            input.R = scanner.nextInt();
            input.C = scanner.nextInt();

            input.grid = new string[input.R];

            for (int r = input.R - 1; r >= 0; --r )
            {
                input.grid[r] = scanner.nextWord();
                int start = input.grid[r].IndexOf('O');
                if (start != -1)
                {
                    input.start = new Point<int>(start, r);
                }
                int stop = input.grid[r].IndexOf('X');
                if (stop != -1)
                {
                    input.finish = new Point<int>(stop, r);
                }
            }

            return input;
        }

        public string processInput(PortalInput input)
        {
            int[][] closestWall;
            int[][][] distToDestPortal;
            calculateClosestWall(out closestWall, out distToDestPortal, input);

            for (int r = closestWall.Length - 1; r >= 0; --r )
            {
                Logger.LogTrace("Row {}: {}", r, closestWall[r].ToCommaString());
            }

            for (int r = closestWall.Length - 1; r >= 0; --r)
            {
                for (int c = 0; c < input.C; ++c)
                {
                    Logger.LogTrace("Nearest Row {} Col {}: {}", r, c, distToDestPortal[r][c].ToCommaString());
                }
            }



            DijkstraNode[][] dijNodes;
            Ext.createArray(out dijNodes, input.R, input.C, null);

            for (int r = 0; r < input.R; ++r)
            {
                for (int c = 0; c < input.C; ++c)
                {
                    dijNodes[r][c] = new DijkstraNode(r, c, int.MaxValue);
                }
            }

            HeapPriorityQueue<DijkstraNode, int> toProcess = new HeapPriorityQueue<DijkstraNode, int>(input.R * input.C);

            toProcess.Enqueue(new DijkstraNode(input.start.Y, input.start.X, 0), 0);

            while(toProcess.Count > 0)
            {
                DijkstraNode nodeU = toProcess.Dequeue();

                Preconditions.checkState(nodeU.distance < int.MaxValue, "NodeU distance is inf");
                //Logger.LogTrace("Current node Row {} Col {}: {}", nodeU.row, nodeU.col, nodeU.distance);

                if (nodeU.row == input.finish.Y && nodeU.col == input.finish.X)
                {
                    //Done
                    return nodeU.distance.ToString();
                }


                Action<int, int, int> handleNextNode = (row, col, distToNodeU) =>
                {
                    //Distance to nodeV via nodeU
                    int alt = distToNodeU + nodeU.distance;
                    DijkstraNode nodeV = dijNodes[row][col];
					
					//Found a new best path to node V
					if (alt < nodeV.distance) {
						
                        nodeV.distance = alt;
						//Clear non shorest paths						
						toProcess.Enqueue(nodeV, nodeV.distance);
					}
                };

                if (nodeU.row > 0 && input.grid[nodeU.row - 1][nodeU.col] != '#')
                    handleNextNode(nodeU.row - 1, nodeU.col, 1);

                if (nodeU.col > 0 && input.grid[nodeU.row ][nodeU.col-1] != '#')
                    handleNextNode(nodeU.row , nodeU.col-1, 1);

                if (nodeU.row < input.R - 1 && input.grid[nodeU.row + 1][nodeU.col] != '#')
                    handleNextNode(nodeU.row + 1, nodeU.col, 1);

                if (nodeU.col < input.C - 1 && input.grid[nodeU.row ][nodeU.col+1] != '#')
                    handleNextNode(nodeU.row, nodeU.col+1, 1);

                handleNextNode(nodeU.row + distToDestPortal[nodeU.row][nodeU.col][NORTH], nodeU.col,
                    closestWall[nodeU.row][nodeU.col]);
                handleNextNode(nodeU.row - distToDestPortal[nodeU.row][nodeU.col][SOUTH], nodeU.col,
                    closestWall[nodeU.row][nodeU.col]);

                handleNextNode(nodeU.row, nodeU.col + distToDestPortal[nodeU.row][nodeU.col][EAST],
                    closestWall[nodeU.row][nodeU.col]);
                handleNextNode(nodeU.row, nodeU.col - distToDestPortal[nodeU.row][nodeU.col][WEST],
                    closestWall[nodeU.row][nodeU.col]);

            }

            return "THE CAKE IS A LIE";
        }

        const int NORTH = 0;
        const int EAST = 1;
        const int SOUTH = 2;
        const int WEST = 3;

        void calculateClosestWall(out int[][] closestWall, out int[][][] distToDestPortal, PortalInput input)
        {
            Ext.createArray(out closestWall, input.R, input.C, 1000);
            Ext.createArray(out distToDestPortal, input.R, input.C, 4, -1);
            for(int r = 0; r < input.R; ++r)
            {
                for(int c = 0; c < input.C; ++c)
                {
                    if (input.grid[r][c] == '#')  
                    {
                        closestWall[r][c] = 0;
                        //Already 0 by default...nearestPortal[r][c][SOUTH] = nearestPortal[r][c][NORTH] = nearestPortal[r][c][EAST] = nearestPortal[r][c][WEST] = 0;
                        continue;
                    }

                    if (r == 0)
                    {
                        distToDestPortal[r][c][SOUTH] = 0;
                    }
                    else
                    {
                        distToDestPortal[r][c][SOUTH] = distToDestPortal[r-1][c][SOUTH] +1 ;
                    }

                    if (c == 0)
                    {
                        distToDestPortal[r][c][WEST] = 0;                        
                    }
                    else
                    {
                        distToDestPortal[r][c][WEST] = distToDestPortal[r][c - 1][WEST] +1;
                    }

                    if (r == 0 || c == 0)
                    {
                        closestWall[r][c] = 1;
                        continue;
                    }
                    
                    closestWall[r][c] = Math.Min(1 + closestWall[r][c-1], 1 + closestWall[r-1][c]);                    
                }
            }

            for (int r = input.R - 1; r >= 0; --r)
            {
                for (int c = input.C - 1; c >= 0; --c)
                {
                    if (input.grid[r][c] == '#')
                    {
                        continue;
                    }

                    if (r == input.R - 1)
                    {
                        distToDestPortal[r][c][NORTH] = 0;
                    }
                    else
                    {
                        distToDestPortal[r][c][NORTH] = distToDestPortal[r + 1][c][NORTH]+1;
                    }

                    if (c == input.C - 1)
                    {
                        distToDestPortal[r][c][EAST] = 0;                        
                    }
                    else
                    {
                        distToDestPortal[r][c][EAST] = distToDestPortal[r][c + 1][EAST]+1;
                    }

                    if (c == input.C - 1 || r == input.R - 1)
                    {
                        closestWall[r][c] = 1;
                        continue;
                    }

                    closestWall[r][c] = Math.Min(closestWall[r][c], 
                        Math.Min(1 + closestWall[r][c + 1], 1 + closestWall[r + 1][c]));
                }
            }
        }
    }

    internal class DijkstraNode : IComparable<DijkstraNode>
    {
        internal int row;
        internal int col;
        internal int distance;

        public DijkstraNode(int row, int col, int distance)
        {
            this.row = row;
            this.col = col;
            this.distance = distance;
        }

        //because PQ takes highest, reverse the order to get lowest distance
        public int CompareTo(DijkstraNode rhs)
        {
            return rhs.distance.CompareTo(distance);
        }
    }

    [TestFixture]
    public class PortalTest
    {
        [Test]
        public void Test()
        {
            PortalInput i = new PortalInput();
           

            Portal e = new Portal();
            Assert.AreEqual("2", e.processInput(i));
        }
    }
}