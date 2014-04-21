using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using CodeJamUtils;
using Utils;

namespace UnitTest
{
    [TestClass]
    public class TestUtils
    {
        [TestMethod]
        public void testBinarySearch()
        {
            List<int> list = new List<int>(new int[] { 4, 7, 8, 18, 19, 23, 44 });

            Tuple<int, int> a1 = list.binarySearch(5);

            Assert.AreEqual(4, a1.Item1);
            Assert.AreEqual(7, a1.Item2);

            a1 = list.binarySearch(18);
            Assert.AreEqual(18, a1.Item1);
            Assert.AreEqual(19, a1.Item2);

            a1 = list.binarySearch(19);
            Assert.AreEqual(19, a1.Item1);
            Assert.AreEqual(23, a1.Item2);

            a1 = list.binarySearch(43);
            Assert.AreEqual(23, a1.Item1);
            Assert.AreEqual(44, a1.Item2);
        }
    }
}
