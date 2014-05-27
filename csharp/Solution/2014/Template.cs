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

namespace Round1C_2014.Problem2
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
           

            Elf e = new Elf();
            Assert.AreEqual("2", e.processInput(i));
        }
    }
}*/