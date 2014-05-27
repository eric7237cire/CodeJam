/*
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

namespace CodeJam.Round1C_2014
{
    public class ElfInput
    {
        public int A;
        public int B;
        public int K;
    }

    public class Elf : InputFileProducer<ElfInput>, InputFileConsumer<ElfInput, string>
    {
        public ElfInput createInput(Scanner scanner)
        {
            ElfInput input = new ElfInput();
            input.A = scanner.nextInt();
            input.B = scanner.nextInt();
            input.K = scanner.nextInt();

            return input;
        }

        public string processInput(ElfInput input)
        {
            throw new NotImplementedException();
        }
    }
    [TestFixture]
    public class ElfTest
    {
        [Test]
        public void Test()
        {
            ElfInput i = new ElfInput();
            i.P = 1;
            i.Q = 4;

            Elf e = new Elf();
            Assert.AreEqual("2", e.processInput(i));
        }
    }
}*/