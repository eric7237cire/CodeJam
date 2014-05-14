using CodeJamUtils;
using NUnit.Framework;
using Round1C.Pogo;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils.geom;
using Pogo = Round1C.Pogo.Pogo;

namespace UnitTest
{
    [TestFixture]
    public class TestPogo
    {
        [Test]
        public void test1()
        {
            testPoint(-8, 0);
        }
        [Test]
        public void testBasic()
        {
            int lowerLimit = -10;
            int upperLimit = 10;
            for (int x = lowerLimit; x <= upperLimit; ++x )
            {
                for(int y = lowerLimit; y <= upperLimit; ++y)
                {
                    testPoint(x, y);
                }
            }
                
        }

        private static void testPoint(int x, int y)
        {
            Round1C.Pogo.Pogo p = new Pogo();

            Point<int> pt = new Point<int>(x, y);
            string s = p.processInput(Input.createInput(pt.X, pt.Y));

            Point<int> point = Pogo.simulate(s);

            Assert.AreEqual(pt, point);
        }
    }
}
