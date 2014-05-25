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

namespace CodeJam.RoundQual_2014
{
    public class CookieInput
    {
        public double farmCost;
        public double farmRate;
        public double goal;
    }

    public class Cookie : InputFileProducer<CookieInput>, InputFileConsumer<CookieInput, string>
    {
        public CookieInput createInput(Scanner scanner)
        {
            CookieInput input = new CookieInput();
            input.farmCost = scanner.nextDouble();
            input.farmRate = scanner.nextDouble();
            input.goal = scanner.nextDouble();
            
            return input;
        }

        public string processInput(CookieInput input)
        {
            
            //X axis is time, Y is cookies
            Line<double> canBuyFarm = LineExt.createFromCoords(0, input.farmCost, 1, input.farmCost);
            Line<double> hitGoal = LineExt.createFromCoords(0, input.goal, 1, input.goal);

            Line<double> rateLine = LineExt.createFromCoords(0, 0, 1, 2d);

            double bestTimeToX = rateLine.intersection(hitGoal).X;
            int lc = 0;
            while(lc++ < 1000000)
            {

                double canBuyFarmTime = canBuyFarm.intersection(rateLine).X;
                rateLine = LineExt.createFromCoords(canBuyFarmTime, 0, canBuyFarmTime+1, rateLine.getSlope() + input.farmRate);

                double newBestTimeToX = rateLine.intersection(hitGoal).X;

                if (newBestTimeToX < bestTimeToX)
                {
                    bestTimeToX = newBestTimeToX;
                } else {
                    break;
                }
            }

            return bestTimeToX.ToUsString(7);
        }
    }
}