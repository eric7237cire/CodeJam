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
using NUnit.Framework;
using Utils.math;

namespace CodeJam.Round1C_2014
{
    public class ElfInput
    {
        public long P;
        public long Q;        
    }

    public class Elf : InputFileProducer<ElfInput>, InputFileConsumer<ElfInput, string>
    {
        public ElfInput createInput(Scanner scanner)
        {
            ElfInput input = new ElfInput();
            string w = scanner.nextWord();
            string[] st = w.Split('/');
            input.P = long.Parse(st[0]);
            input.Q = long.Parse(st[1]);

            return input;
        }

        public string processInput(ElfInput input)
        {
            BigFraction test = 1;
            for (int i = 0; i < 40; ++i)
                test /= 2;

            BigFraction f = 1;
            BigFraction pq = new BigFraction(input.P, input.Q);

            for(int gen = 1; gen < 50; ++gen)
            {
                f /= 2;
                BigFraction diff = pq - f;

                if (diff < 0)
                    continue;

                BigFraction div = diff / test;

                if (div.Denominator == 1)
                {
                    return (gen).ToString();
                }
            }

            return "impossible";
        }
    }
    [TestFixture]
    public class ElfTest
    {
        [Test]
        public void Test()
        {
            ElfInput i = new ElfInput();
            i.P = 3;
            i.Q = 4;

            Elf e = new Elf();
            Assert.AreEqual("1", e.processInput(i));
        }

        [Test]
        public void Test2()
        {
            ElfInput i = new ElfInput();
            i.P = 1;
            i.Q = 4;

            Elf e = new Elf();
            Assert.AreEqual("2", e.processInput(i));
        }
    }
}