using System;
using NUnit.Framework;
using System.Collections.Generic;
using Round2_2013.Problem2;

namespace UnitTest
{
    [TestFixture]
    public class TestManyPrizes
    {
        [Test]
        public void TestManyPrizes1()
        {
            List<int> pos = new List<int>(new int[] { 2, 4, 5, 3, 6, 7, 1, 0 });

            List<Team> teams = ManyPrizes.getRanks(pos);

            for (int i = 0; i < 8; ++i)
                Assert.AreEqual(i, teams[i].teamNumber);

                
            Assert.AreEqual(0, teams[0].winlossRank );
            Assert.AreEqual(4, teams[1].winlossRank );

            Assert.AreEqual(1, teams[2].winlossRank );
            Assert.AreEqual(2, teams[3].winlossRank );

            Assert.AreEqual(5, teams[4].winlossRank );
            Assert.AreEqual(6, teams[5].winlossRank );

            Assert.AreEqual(3, teams[6].winlossRank );
            Assert.AreEqual(7, teams[7].winlossRank );

            //best 
            pos = new List<int>(new int[] { 0, 2, 3, 4, 1, 5, 6, 7 });
            teams = ManyPrizes.getRanks(pos);

            //B has 2nd best rank
            Assert.AreEqual(1, teams[1].winlossRank);

            teams = ManyPrizes.getRanks(new List<int>(new int[] { 0, 1, 3, 4, 2, 5, 6, 7 }));

            //C has 2nd best rank
            Assert.AreEqual(1, teams[2].winlossRank);

            teams = ManyPrizes.getRanks(new List<int>(new int[] { 0, 1, 2, 4, 3, 5, 6, 7 }));

            //D has 2nd best rank
            Assert.AreEqual(1, teams[3].winlossRank);

            teams = ManyPrizes.getRanks(new List<int>(new int[] { 0, 1, 2, 3, 4, 5, 6, 7 }));

            //E has 2nd best rank
            Assert.AreEqual(1, teams[4].winlossRank);

        }
    }
}
