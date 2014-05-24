using NUnit.Framework;
using RoundFinal;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils.geom;

namespace UnitTest
{
    [TestFixture]
    public class TestDrummer
    {
        [Test]
        public void Test()
        {
            DrummerInput input = new DrummerInput();
            //3, 5, 7
            input.T = new int[] { 2, 6, 6 };

            Drummer drummer = new Drummer();

            Line<double> line;
            double E;
            
            drummer.findE(input, 0, 2, 1, out E, out line);

            Assert.AreEqual(1, E, 1e-6);
        }
    }
}
