
#define LOGGING_DEBUG
using CodeJam.Utils.graph;
using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;
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

            return input;
        }

        public int processInput(DontBreakNileInput input)
        {
            bool[][] isWater;
            Ext.createArray(out isWater, input.W, input.H, true);

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

            //MaxFlowPreflowN3 flow = new MaxFlowPreflowN3();

            /**
             * Each cell has 2 nodes, one to recieve flow and one to send.  This
             * is to make sure the total flow <= 1
             */
            Maxflow2Int32 flow = new Maxflow2Int32(2 * (input.H * input.W) + 3);

            //flow.init(input.H * input.W + 2);
            int sourceIndex = 2 * input.H * input.W;
            int sinkIndex = sourceIndex + 1;

            Func<int,int,int> toReceiverIndex = (x, y) => y * input.W + x;
            Func<int, int, int> toSenderIndex = (x, y) => toReceiverIndex(x,y) + input.H*input.W;

            for (int x = 0; x < input.W ; ++x)
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

                    if (y < input.H - 1 && isWater[x][y] && isWater[x][y+1])
                    {
                        Logger.LogDebug("Add edge from {},{} to {},{}",
                            x, y, x, y + 1);

                        flow.AddEdge(toSenderIndex(x, y), toReceiverIndex(x, y + 1), 1);
                        flow.AddEdge(toSenderIndex(x, y + 1), toReceiverIndex(x, y), 1);
                    }
                    if (x < input.W - 1 && isWater[x][y] && isWater[x+1][y])
                    {
                        Logger.LogDebug("Add edge from {},{} to {},{}",
                            x, y, x+1, y);

                        
                        flow.AddEdge(toSenderIndex(x, y), toReceiverIndex(x+1, y), 1);
                        flow.AddEdge(toSenderIndex(x+1, y), toReceiverIndex(x, y), 1);
                    }
                }
            }

            //return flow.maxFlow(sourceIndex, sinkIndex);
            int maxFLow = flow.GetMaxFlow(sourceIndex, sinkIndex);
            return maxFLow;
        }
    }
}
