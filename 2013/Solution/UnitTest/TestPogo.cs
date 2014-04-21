using CodeJamUtils;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Round1C.Pogo;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Pogo = Round1C.Pogo.Pogo;

namespace UnitTest
{
    [TestClass]
    public class TestPogo
    {
        [TestMethod]
        public void test1()
        {
            testPoint(-8, 0);
        }
        [TestMethod]
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

            CodeJamUtils.Point<int> point = Pogo.simulate(s);

            Assert.AreEqual<Point<int>>(pt, point);
        }
    }
}
