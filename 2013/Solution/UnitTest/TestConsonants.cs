using NUnit.Framework;
using Round1C;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;

namespace UnitTest
{
    [TestFixture]
    public class TestConsonants
    {
        [Test]
        public void Test1()
        {
            TestString("aaba", 1);
        }

        [Test]
        public void Test2()
        {
            TestString("ba", 2);
        }

        [Test]
        public void Test3()
        {
            TestString("ab", 2);
        }

        [Test]
        public void Test4()
        {
            TestString("bab", 1);
        }

        [Test]
        public void Test5()
        {
            TestString("bbb", 2);
        }

        [Test]
        public void Test6()
        {
            TestString("bbb", 3);
        }

        [Test]
        public void Test7()
        {
            TestString("aab", 3);
        }

        [Test]
        public void Test8()
        {
            TestString("bbbb", 3);
        }

        [Test]
        public void Test9()
        {
            TestString("bbbbb", 4);
        }

        [Test]
        public void TestMethod()
        {
            for (int len = 1; len <= 8; ++len )
            {
                int lb = 0;
                int up = 1 << len;

                
                for(int i = lb; i < up; ++i)
                {
                    StringBuilder sb = new StringBuilder();
                    for(int cIdx = 0; cIdx < len; ++cIdx)
                    {
                        if ( (i & (1 << cIdx)) != 0)
                        {
                            sb.Append("b");
                        }
                        else
                        {
                            sb.Append("a");
                        }
                    }

                    for (int n = 1; n <= len; ++n )
                    {
                        TestString(sb.ToString(), n);
                    }
                        
                }
            }
                
        }

        private void TestString(string s, int n)
        {
            int bf = Consonants.bruteForce(s, n);
            long ans = Consonants.calculate(s, n);

            Assert.AreEqual(bf, ans,"S {0} n {1}".FormatThis(s, n));
        }
    }
}
