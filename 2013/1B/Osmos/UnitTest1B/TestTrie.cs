using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Trie;
using GarbledEmail;
namespace UnitTest1B
{
    [TestClass]
    public class TestTrie
    {
        [TestMethod]
        public void testMatch()
        {
            Dictionary dict = new Dictionary();
            dict.words.Add("a");
            dict.words.Add("codejam");

            GarbledEmail.GarbledEmail ge = new GarbledEmail.GarbledEmail(dict);

            int minChange = ge.minCount(0, 0, "codejam");

            Assert.AreEqual(0, minChange);
            
        }

        [TestMethod]
        public void testMatch2Changes()
        {
            Dictionary dict = new Dictionary();
            dict.words.Add("codejam");

            GarbledEmail.GarbledEmail ge = new GarbledEmail.GarbledEmail(dict);

            int minChange = ge.minCount(0, 0, "cxdejax");

            Assert.AreEqual(2, minChange);

        }

        [TestMethod]
        public void testSamples()
        {
            GarbledEmail.GarbledEmail ge = new GarbledEmail.GarbledEmail();

            int minChange = ge.minCount(0, 0, "codejam");
            Assert.AreEqual(0, minChange);

            minChange = ge.minCount(0, 0, "cxdejax");
            Assert.AreEqual(2, minChange);

            minChange = ge.minCount(0, 0, "cooperationaabea");
            Assert.AreEqual(1, minChange);

            minChange = ge.minCount(0, 0, "codejam");
            Assert.AreEqual(0, minChange);
        }

        public void createDict()
        {
            Dictionary dict = new Dictionary();

            using (StreamReader file =
   new System.IO.StreamReader(@"C:\codejam\CodeJam\2013\1B\Osmos\Osmos\garbled_email_dictionary.txt"))
            {
                string line;
                while (( line = file.ReadLine()) != null)
                {
                    Console.WriteLine(line);
                    dict.words.Add(line);
                }
            }

            TrieNode root = TrieNode.createRootNode(dict);

            //TODO Codejam progress 3, match a has a change at location 3 which is not valid

        }
        [TestMethod]
        public void testMatches()
        {
            Dictionary dict = new Dictionary();
            dict.words.Add("abcdef");
            dict.words.Add("bbcdezg");
            dict.words.Add("acc");
            dict.words.Add("bac");
            TrieNode root = TrieNode.createRootNode(dict);

            List<WordMatch> matches;
            root.parseText("abcdefghi", null, out matches);

            Assert.AreEqual(3, matches.Count);
            Assert.AreEqual(dict.words[2], matches[0].Word);
            Assert.AreEqual(1, matches[0][0]);
            Assert.AreEqual(1, matches[0][0, Trie.WordMatch.LeftOrRight.right]);

            Assert.AreEqual(dict.words[0], matches[1].Word);
            Assert.AreEqual(0, matches[1].changeCount());

            Assert.AreEqual(dict.words[1], matches[2].Word);
            Assert.AreEqual(0, matches[2][0]);
            Assert.AreEqual(6, matches[2][0, Trie.WordMatch.LeftOrRight.right]);

            Assert.AreEqual(5, matches[2][1]);
            Assert.AreEqual(1, matches[2][1, Trie.WordMatch.LeftOrRight.right]);
        }


        [TestMethod]
        public void testBasicMatches()
        {
            Dictionary dict = new Dictionary();
            dict.words.Add("abc");
            dict.words.Add("bccd");
            dict.words.Add("abcc");
            dict.words.Add("bab");
            TrieNode root = TrieNode.createRootNode(dict);

            List<WordMatch> matches;
            root.parseText("abccd", null, out matches);

            Assert.AreEqual(2, matches.Count);
            Assert.AreEqual(dict.words[0], matches[0].Word);
            Assert.AreEqual(dict.words[2], matches[1].Word);


        }
        [TestMethod]
        public void testAddWord()
        {
            Dictionary dict = new Dictionary();
            dict.words.Add("abc");
            dict.words.Add("acc");
            TrieNode root = TrieNode.createRootNode(dict);

            Assert.AreEqual(1, root.childrenList.Count);
            TrieNode nodeA = root.children[0];
            Assert.AreSame(root.childrenList[0].Item2, nodeA);

            Assert.AreEqual(2, nodeA.childrenList.Count);

            TrieNode nodeB = nodeA.children[1];
            TrieNode nodeC = nodeA.children[2];

            Assert.AreEqual(1, nodeB.childrenList.Count);
            Assert.AreEqual(1, nodeC.childrenList.Count);

        }
    }
}
