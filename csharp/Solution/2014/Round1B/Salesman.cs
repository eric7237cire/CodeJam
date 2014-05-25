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

namespace CodeJam.Round1B_2014
{
    public class SalesmanInput
    {
        public double farmCost;
        public double farmRate;
        public double goal;
    }

    public class Salesman : InputFileProducer<SalesmanInput>, InputFileConsumer<SalesmanInput, string>
    {
        public SalesmanInput createInput(Scanner scanner)
        {
            SalesmanInput input = new SalesmanInput();
            input.farmCost = scanner.nextDouble();
            input.farmRate = scanner.nextDouble();
            input.goal = scanner.nextDouble();

            return input;
        }

        public string processInput(SalesmanInput input)
        {
            return "bob";
        }
    }
}