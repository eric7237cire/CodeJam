using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

[assembly: System.Runtime.CompilerServices.InternalsVisibleTo
                          ("UnitTest1B")]
namespace Trie
{
    static class Utils
    {
        public static string Format(this string str, params object[] args)
        {
            return String.Format(str, args);
        }
    }
    class Dictionary
    {
        public List<string> words;

        public Dictionary()
        {
            words = new List<string>();
        }
    }

    public class WordMatch
    {
        public string Word { get; private set; }
        internal int dictIdx;
        private int[] changes;

        public enum LeftOrRight
        {
            left,
            right
        }

        /// <summary>
        /// left means indexed 0 1 2 3 ...
        /// right means indexed 3 2 1 0 
        /// </summary>
        /// <param name="i"></param>
        /// <param name="lr"></param>
        /// <returns></returns>
        public int this[int i, LeftOrRight lr = LeftOrRight.left]
        {
            get { return lr == LeftOrRight.left ? changes[i] : Word.Length - 1 - changes[i]; }
        }

        public int ChangeCount
        {
            get
            {
                return changes.Length;
            }
        }

        public string Changes
        {
            get
            {
                return changes.ToString();
            }
        }

        public static WordMatch create(string word, int dictIdx, params int[] changes)
        {
            WordMatch m = new WordMatch();
            m.Word = word;
            m.dictIdx = dictIdx;
            m.changes = changes;

            foreach (int changeIdx in changes)
            {
                if (changeIdx < 0 || changeIdx >= word.Length)
                {
                    throw new ArgumentException("Index {0} not valid {1}".Format(changeIdx, word));
                }
            }

            return m;
        }
    }

    internal class TrieNodePtr
    {
        public TrieNode node;

        //Indexes local to the word
        private List<int> changedIndexes;

        public TrieNodePtr()
        {
            changedIndexes = new List<int>();
        }

        public TrieNodePtr(TrieNodePtr parent, TrieNode childNode)
            : this()
        {

            changedIndexes = new List<int>(parent.changedIndexes);
            node = childNode;
        }

        private void addMatches(List<WordMatch> matches)
        {
            foreach (WordMatch match in node.matches)
            {
                //TODO clone? copy constructor?
                WordMatch newMatch = WordMatch.create(match.Word, match.dictIdx, changedIndexes.ToArray());
                matches.Add(newMatch);
            }
        }

        public static void doMatch(int cIdx, char c, int cInt, ref List<TrieNodePtr> list, List<WordMatch> matches)
        {
            List<TrieNodePtr> newList = new List<TrieNodePtr>();

            foreach (TrieNodePtr nodePtr in list)
            {
                if (nodePtr.node.children[cInt] != null)
                {
                    TrieNodePtr newNp = new TrieNodePtr(nodePtr, nodePtr.node.children[cInt]);
                    newList.Add(newNp);
                    newNp.addMatches(matches);
                }

                //If last change > 5, then add the rest
                if (nodePtr.changedIndexes.Count == 0 || cIdx - nodePtr.changedIndexes[nodePtr.changedIndexes.Count - 1] >= TrieNode.minDistance)
                {
                    foreach (var x in nodePtr.node.childrenList)
                    {
                        int charInt = x.Item1;
                        TrieNode childNode = x.Item2;

                        if (charInt == cInt)
                            continue;

                        TrieNodePtr newNp = new TrieNodePtr(nodePtr, childNode);
                        newNp.changedIndexes.Add(cIdx);

                        newList.Add(newNp);
                        newNp.addMatches(matches);
                    }
                }
            }

            list = newList;



        }
    }

    sealed internal class TrieNode
    {
        public const int minDistance = 5;

        internal TrieNode[] children;
        internal List<Tuple<int, TrieNode>> childrenList;
        private const int aInt = (int)'a';
        private Dictionary dict;


        public int CurrentLength { get; private set; }
        internal List<WordMatch> matches;

        private TrieNode(Dictionary dict)
        {
            this.dict = dict;
            children = new TrieNode[26];
            childrenList = new List<Tuple<int, TrieNode>>();
            matches = new List<WordMatch>();
        }

        public static TrieNode createRootNode(Dictionary dict)
        {
            TrieNode root = new TrieNode(dict);
            root.CurrentLength = 0;

            for (int dIdx = 0; dIdx < dict.words.Count; ++dIdx)
            {
                root.addWord(dict.words[dIdx], dIdx);
            }
            return root;
        }

        

        
        public void parseText(string text, Action<WordMatch> matchHandler, out List<WordMatch> matches, int startIdx = 0)
        {
            matches = new List<WordMatch>();

            List<TrieNodePtr> listPtrs = new List<TrieNodePtr>();
            TrieNodePtr root = new TrieNodePtr();
            root.node = this;
            listPtrs.Add(root);

            for (int cIdx = startIdx; cIdx < text.Length; ++cIdx)
            {
                char c = text[cIdx];
                int cInt = (int)c - aInt;

                TrieNodePtr.doMatch(cIdx - startIdx, c, cInt, ref listPtrs, matches);
            }
        }

        private void addWord(string word, int dictIdx)
        {
            TrieNode node = this;

            for (int cIdx = 0; cIdx < word.Length; ++cIdx)
            {
                char c = word[cIdx];
                int cInt = (int)c - aInt;
                if (node.children[cInt] == null)
                {
                    TrieNode newNode = new TrieNode(node.dict);
                    newNode.CurrentLength = cIdx + 1;
                    node.children[cInt] = newNode;
                    node.childrenList.Add(new Tuple<int, TrieNode>(cInt, newNode));
                }

                node = node.children[cInt];

                if (cIdx == word.Length - 1)
                {
                    node.matches.Add(WordMatch.create(word, dictIdx));
                }
            }
        }
    }
}
