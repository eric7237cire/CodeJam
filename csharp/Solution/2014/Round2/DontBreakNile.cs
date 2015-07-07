
#define LOGGING_DEBUG
#define LOGGING_INFO
using CodeJam.Utils.geom;
using CodeJam.Utils.graph;
using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;
using Wintellect.PowerCollections;
using Logger = Utils.LoggerFile;


namespace Round2_2014.Problem3
{
    public class Building
    {
        public int X0;
        public int Y0;
        public int X1;
        public int Y1;
    }
    public class DontBreakNileInput
    {
        public int W;
        public int H;
        public int B;

        
        public Building[] Buildings;
    }
    public class DontBreakNile : InputFileProducer<DontBreakNileInput>, InputFileConsumer<DontBreakNileInput, int>
    {
        public DontBreakNileInput createInput(Scanner scanner)
        {
            scanner.enablePlayBack();
            DontBreakNileInput input = new DontBreakNileInput();

            input.W = scanner.nextInt();
            input.H = scanner.nextInt();
            input.B = scanner.nextInt();

            Ext.createArrayWithNew(out input.Buildings, input.B);

            for (int i = 0; i < input.B; ++i)
            {
                input.Buildings[i].X0 = scanner.nextInt();
                input.Buildings[i].Y0 = scanner.nextInt();
                input.Buildings[i].X1 = scanner.nextInt();
                input.Buildings[i].Y1 = scanner.nextInt();
            }

            Logger.LogInfo("Input {}", scanner.finishPlayBack());
            return input;
        }
        
        static int tc = 0;

        public int processInput(DontBreakNileInput input)
        {
            //Max flow already hit
            if (input.B == 0)
            {
                return input.W;
            }

            bool[][] isWater;
            Ext.createArray(out isWater, input.W, input.H, true);

            int[][] flowDigit;
            Ext.createArray(out flowDigit, input.W, input.H, -1);

            for(int b = 0; b < input.B; ++b)
            {
                for (int x = input.Buildings[b].X0; x <= input.Buildings[b].X1; ++x)
                {
                    for (int y = input.Buildings[b].Y0; y <= input.Buildings[b].Y1; ++y)
                    {
                        isWater[x][y] = false;
                    }
                }
            }

            Logger.LogDebug("Case {}", ++tc);
            for(int y = input.H; y >= -1; --y)
            {
                StringBuilder sb = new StringBuilder();
                for(int x = -1; x <= input.W; ++x)
                {
                    if (x >= 0 && x < input.W &&
                            y >= 0 && y < input.H && isWater[x][y])
                    {
                        sb.Append('.');
                    } else {
                        sb.Append('*');
                    }
                }
                Logger.LogDebug(sb.ToString());
                
            }

            int flow = 0;
            int loopcheck = 0;
            //int minX = 0;
            for (int startX = 0; startX < input.W; ++startX )
            {
                

                

                //start on y = 0, find first free x
                int curX = startX;
                int curY = 0;

                if(!isWater[curX][curY])
                {
                    continue;
                }

                Dictionary<PointInt32, bool> visitedPoints = new Dictionary<PointInt32, bool>();
                Dictionary<PointInt32, PointInt32> previousPoints = new Dictionary<PointInt32, PointInt32>();

                OrderedSet<PointInt32> pointsToVisit = new OrderedSet<PointInt32>((p1, p2) =>
                {
                    if (p1.Y != p2.Y)
                        return p1.Y.CompareTo(p2.Y);

                    //if (p1.X != p2.X)
                        return p1.X.CompareTo(p2.X);

                    //return p1.Y.CompareTo(p2.Y);
                });

                pointsToVisit.Add(new PointInt32(curX, curY));

                bool found = false;
                PointInt32 exitPoint = null;
                while (pointsToVisit.Count > 0)
                {
                    PointInt32 curPoint = pointsToVisit.GetFirst();
                    pointsToVisit.RemoveFirst();

                    if (visitedPoints.ContainsKey(curPoint))
                    {
                        continue;
                    }

                   // if (visitedPoints.Count % 1000 == 0)
                      //  Logger.LogDebug("Visiting point {}.  Number of points to visit {} Visited points {}", curPoint, pointsToVisit.Count, visitedPoints.Count);

                    visitedPoints.Add(curPoint, true);

                    if (curPoint.Y == input.H - 1)
                    {
                        found = true;
                        exitPoint = curPoint;
                        break;
                    }

                    foreach (PointInt32 dir in PointInt32.NESW)
                    {
                        PointInt32 pointToConsider = curPoint + dir;

                        if (pointToConsider.X >= 0 && pointToConsider.X < input.W &&
                            pointToConsider.Y >= 0 && pointToConsider.Y < input.H)
                        {

                            if (isWater[pointToConsider.X][pointToConsider.Y] == true)
                            {
                                pointsToVisit.Add(pointToConsider);
                                if (previousPoints.ContainsKey(pointToConsider) == false)
                                {
                                    previousPoints[pointToConsider] = curPoint;
                                }

                            }
                        }

                    }
                }

                if (found == false)
                {
                    continue;
                }
                else
                {
                    ++flow;

                    int checkLoop = 0;
                    bool[][] isPath;
                    Ext.createArray(out isPath, input.W, input.H, false);

                    PointInt32 a = exitPoint;

                    while (a.Y > 0)
                    {
                        //Logger.LogDebug("path point: {}", a.ToString());
                        isPath[a.X][a.Y] = true;
                        isWater[a.X][a.Y] = false;
                        flowDigit[a.X][a.Y] = flow % 10;
                        a = previousPoints[a];
                        ++checkLoop;
                        Preconditions.checkState(checkLoop <= visitedPoints.Count);
                    }

                    isPath[a.X][a.Y] = true;
                    isWater[a.X][a.Y] = false;
                    flowDigit[a.X][a.Y] = flow % 10;

                    
                    Logger.LogDebug("START PATH for flow {} case {}", flow, tc);

                    for (int y = input.H; y >= -1; --y)
                    {
                        StringBuilder sb = new StringBuilder();
                        for (int x = -1; x <= input.W; ++x)
                        {
                            if (x >= 0 && x < input.W &&
                                    y >= 0 && y < input.H)
                            {
                                if (flowDigit[x][y] >= 0)
                                {
                                    sb.Append(flowDigit[x][y]);
                                }
                                else if (isPath[x][y])
                                {
                                    sb.Append('O');
                                }
                                else if (visitedPoints.ContainsKey(new PointInt32(x, y)))
                                {
                                    sb.Append('f');
                                }
                                else if (isWater[x][y])
                                {
                                    sb.Append('.');
                                }
                                else
                                {
                                    sb.Append('*');
                                }

                            }
                            else
                            {
                                sb.Append('*');
                            }
                        }
                        Logger.LogDebug(sb.ToString());

                    }

                }

            }

            return flow;
        }

        public int processInputUsingMaxFlow(DontBreakNileInput input)
        {
            bool[][] isWater;
            Ext.createArray(out isWater, input.W, input.H, true);

            for (int b = 0; b < input.B; ++b)
            {
                for (int x = input.Buildings[b].X0; x <= input.Buildings[b].X1; ++x)
                {
                    for (int y = input.Buildings[b].Y0; y <= input.Buildings[b].Y1; ++y)
                    {
                        isWater[x][y] = false;
                    }
                }
            }

            //MaxFlowPreflowN3 flow = new MaxFlowPreflowN3();

            /**
             * Each cell has 2 nodes, one to recieve flow and one to send.  This
             * is to make sure the total flow <= 1
             */
            Maxflow2Int32 flow = new Maxflow2Int32(2 * (input.H * input.W) + 3);

            //flow.init(input.H * input.W + 2);
            int sourceIndex = 2 * input.H * input.W;
            int sinkIndex = sourceIndex + 1;

            Func<int, int, int> toReceiverIndex = (x, y) => y * input.W + x;
            Func<int, int, int> toSenderIndex = (x, y) => toReceiverIndex(x, y) + input.H * input.W;

            for (int x = 0; x < input.W; ++x)
            {
                if (isWater[x][0])
                {
                    flow.AddEdge(sourceIndex, toReceiverIndex(x, 0), 1);
                }
                if (isWater[x][input.H - 1])
                {
                    flow.AddEdge(toSenderIndex(x, input.H - 1), sinkIndex, 1);
                }
            }
            for (int x = 0; x < input.W; ++x)
            {
                for (int y = 0; y < input.H; ++y)
                {
                    flow.AddEdge(toReceiverIndex(x, y), toSenderIndex(x, y), 1);

                    if (y < input.H - 1 && isWater[x][y] && isWater[x][y + 1])
                    {
                        Logger.LogDebug("Add edge from {},{} to {},{}",
                            x, y, x, y + 1);

                        flow.AddEdge(toSenderIndex(x, y), toReceiverIndex(x, y + 1), 1);
                        flow.AddEdge(toSenderIndex(x, y + 1), toReceiverIndex(x, y), 1);
                    }
                    if (x < input.W - 1 && isWater[x][y] && isWater[x + 1][y])
                    {
                        Logger.LogDebug("Add edge from {},{} to {},{}",
                            x, y, x + 1, y);


                        flow.AddEdge(toSenderIndex(x, y), toReceiverIndex(x + 1, y), 1);
                        flow.AddEdge(toSenderIndex(x + 1, y), toReceiverIndex(x, y), 1);
                    }
                }
            }

            //return flow.maxFlow(sourceIndex, sinkIndex);
            int maxFLow = flow.GetMaxFlow(sourceIndex, sinkIndex);
            return maxFLow;
        }
    }
}
