using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils.geom;

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

        //Where no 3 lines are colinear
        public static List<Point> generateTestSet(int N, int min = 0; int max = 100)
        {

        }

        public Boolean testColinear(List<Point> set)
        {

        }
    }



    public class XSpotInput
    {
        public int N;
        public List<Point<int>> points;
    }
}
