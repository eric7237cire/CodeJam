#define LOGGING
#define LOGGING_DEBUG
#define LOGGING_INFO
#define LOGGING_TRACE
using CodeJamUtils;
using NUnit.Framework;
using Round3;
using System;
using System.Collections.Generic;
using System.Globalization;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace UnitTest
{
    public class TestCheater
    {
        [Test]
        [Category("current")]
        public void testSmall3()
        {
            testInput(Properties.Resources.TestCheaterSmall3, .94285714);
        }
        private void testInput(string inputTxt, double expectedAns)
        {
            Scanner scanner = new Scanner(new StringReader(inputTxt));

            Cheaters cheater = new Cheaters();

            CheatersInput input = cheater.createInput(scanner);

            double ans = Double.Parse(cheater.processInput(input), new CultureInfo("en-us"));

            Assert.AreEqual(expectedAns, ans, "0.0001");

        }
    }
}
