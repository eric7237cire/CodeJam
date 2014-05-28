using NUnit.Framework;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Trie;
using Round1B_2013.Problem3;
using CodeJamUtils;

using Logger = Utils.LoggerFile;

namespace UnitTest
{
    [TestFixture]
    public class TestTrie
    {
       
      

        
        [Test]
        public void testMatches()
        {
            Dictionary dict = new Dictionary();
            dict.words.Add("abcdef");
            dict.words.Add("bbcdezg");
            dict.words.Add("acc");
            dict.words.Add("bac");
            TrieNode root = TrieNode.createRootNode(dict);

            List<WordMatch> matches;
            root.parseText("abcdefghi", out matches);

            Assert.AreEqual(3, matches.Count);
            Assert.AreEqual(dict.words[2], matches[0].Word); //acc
            Assert.AreEqual(1, matches[0].LeftmostChange);
            Assert.AreEqual(1, matches[0].RightmostChange);

            Assert.AreEqual(dict.words[0], matches[1].Word);  //abcdef
            Assert.AreEqual(0, matches[1].NumChanges);

            Assert.AreEqual(dict.words[1], matches[2].Word);  //bbcdezg
            Assert.AreEqual(0, matches[2].LeftmostChange);
            Assert.AreEqual(1, matches[2].RightmostChange);

        }


        [Test]
        public void testBasicMatches()
        {
            Dictionary dict = new Dictionary();
            dict.words.Add("abc");
            dict.words.Add("bccd");
            dict.words.Add("abcc");
            dict.words.Add("bab");
            TrieNode root = TrieNode.createRootNode(dict);

            List<WordMatch> matches;
            root.parseText("abccd", out matches);

            Assert.AreEqual(2, matches.Count);
            Assert.AreEqual(dict.words[0], matches[0].Word);
            Assert.AreEqual(dict.words[2], matches[1].Word);


        }
        [Test]
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
