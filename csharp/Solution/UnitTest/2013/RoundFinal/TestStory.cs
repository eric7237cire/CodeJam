#if DEBUG
#define LOGGING_DEBUG
#define LOGGING_INFO
#define LOGGING_TRACE
#endif

using NUnit.Framework;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;

using Logger = Utils.LoggerFile;
using RoundFinal_2013.Problem5;

namespace UnitTest
{
    [TestFixture]
    public class TestStory
    {
        [Test]
        public void TestOrder()
        {
            StoryInput input = new StoryInput();
            input.N = 4;
            input.salaries = new List<int>(new int[] { 8, 9, 7, 9});

            Assert.AreEqual(8, input.salaries[0]);
            Assert.AreEqual(9, input.salaries[1]);
            Assert.AreEqual(7, input.salaries[2]);

             int[] newValues;
            Story.transformInput(input, out newValues);
            Logger.LogTrace("Values {}  orig {}", newValues.ToCommaString(), input.salaries.ToCommaString());
            CollectionAssert.AreEqual( new int[] {1, 3, 0, 2}, newValues);

            input.salaries = new List<int>(new int[] { 3, 2, 1, 0 });

            Story.transformInput(input, out newValues);
            Logger.LogTrace("Values {}  orig {}", newValues.ToCommaString(), input.salaries.ToCommaString());
            CollectionAssert.AreEqual(new int[] { 3, 2, 1, 0 }, newValues);

            input.salaries = new List<int>(new int[] { 45, 8, 45, 8, 3, 99, 55, 40, 44 });
            input.N = input.salaries.Count;

            Story.transformInput(input, out newValues);
            Logger.LogTrace("Values {}  orig {}", newValues.ToCommaString(), input.salaries.ToCommaString());
            
            CollectionAssert.AreEqual(new int[] { 6, 2, 5, 1, 0, 8, 7, 3, 4}, newValues);
        }

        [Test]
        public void TestCountDP()
        {
            int[][] dp;
            int[] values = new int[] { 6, 2, 9, 5, 1, 0, 8, 7, 3, 4 };
            Ext.createArray(out dp, values.Length + 1, values.Length, -1);

            int[][] dpCheck;

            Logger.LogTrace("\n Starting DP check");            
            int total = Story.countNonIncSubsequencesBruteForce(values, out dpCheck);

            Story.dynProg(values, out dp);

            Logger.LogTrace("total {}", total);
            for (int len = 0; len <= values.Length; ++len)
            {
                for (int lastElem = 0; lastElem < values.Length; ++lastElem)
                {
                    Logger.LogTrace("Checking Count {} for len {} and last Element {}", dpCheck[len][lastElem], len, lastElem);
                    Assert.AreEqual(dpCheck[len][lastElem], dp[len][lastElem]);
                }
            }
        }

        [Test]
        public void TestCountSubSeqBruteForce()
        {
            int[][] dp;
            int[] test = new int[] { 0 };
            Assert.AreEqual(1, Story.countNonIncSubsequencesBruteForce(test, out dp));

            test = new int[] { 0, 1 };
            Assert.AreEqual(2, Story.countNonIncSubsequencesBruteForce(test, out dp));

            test = new int[] { 1, 0 };
            Assert.AreEqual(3, Story.countNonIncSubsequencesBruteForce(test, out dp));

            test = new int[] { 2, 1, 0 };
            Assert.AreEqual(7, Story.countNonIncSubsequencesBruteForce(test, out dp));

            Logger.LogTrace("\n Starting");
            test = new int[] { 6, 2, 5, 1, 0, 8, 7, 3, 4 };
            int total = Story.countNonIncSubsequencesBruteForce(test, out dp);

            Logger.LogTrace("total {}", total);
            for(int len = 0; len <= test.Length; ++len)
            {
                for(int lastElem = 0; lastElem < test.Length; ++lastElem)
                {
                    Logger.LogTrace("Count {} for len {} and last Element {}", dp[len][lastElem], len, lastElem);
                }
            }
        }
    }
}
