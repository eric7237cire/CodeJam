using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Logger = Utils.LoggerFile;
using Utils;

[assembly: System.Runtime.CompilerServices.InternalsVisibleTo
                          ("UnitTest")]
namespace Trie
{

    
    public class Dictionary
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
        public int NumChanges { get; private set; }

        //Distance from right side 43210  (-1 for invalid)
        public int RightmostChange { get; private set; }

        //Distance from left side 012345
        public int LeftmostChange { get; private set; }

        /// <summary>
        /// Indicies are left based
        /// </summary>
        /// <param name="word"></param>
        /// <param name="numChanges"></param>
        /// <param name="rmc"></param>
        /// <param name="lmc"></param>
        /// <returns></returns>
        public static WordMatch create(string word, int numChanges, int lmc, int rmc)
        {
            WordMatch m = new WordMatch();
            m.Word = word;
            m.NumChanges = numChanges;
            m.RightmostChange = m.Word.Length - 1 - rmc;
            m.LeftmostChange = lmc;

            return m;
        }
    }

    public class TrieNodePtr
    {
        private TrieNode node;

        //Indexes local to the word
        
        private readonly int leftChangeIndex;
        private readonly int rightChangeIndex;
        private readonly int numChanges;

        //Move to TrieNodePtr
        public const int minDistance = 5;

        public TrieNodePtr(TrieNode childNode)
        {
            node = childNode;
            leftChangeIndex = -1;
            rightChangeIndex = -1;
            numChanges = 0;
        }

        public TrieNodePtr(TrieNodePtr parent, TrieNode childNode, int newIndex)
            : this(childNode)
        {
            leftChangeIndex = parent.numChanges == 0 ? newIndex : parent.leftChangeIndex;
            rightChangeIndex = newIndex;
            numChanges = parent.numChanges + 1;
        }

        public TrieNodePtr(TrieNodePtr parent, TrieNode childNode)
            : this(childNode)
        {
            leftChangeIndex = parent.leftChangeIndex;
            rightChangeIndex = parent.rightChangeIndex;
            numChanges = parent.numChanges;
        }

        private void addMatches(List<WordMatch> matches)
        {
            if (node.WordMatch == null)
                return;

            WordMatch newMatch = WordMatch.create(node.WordMatch, numChanges, leftChangeIndex, rightChangeIndex);
            
            matches.Add(newMatch);

        }

        public static void doMatch(int cIdx, int cInt, ref List<TrieNodePtr> list, List<WordMatch> matches)
        {
            List<TrieNodePtr> newList = new List<TrieNodePtr>();
            Logger.LogTrace("doMatch cIdx: {} char: {} list size {} matches size {}", 
            	cIdx, (char)(cInt + (int)'a'), list.Count, matches.Count);

            foreach (TrieNodePtr nodePtr in list)
            {
            	//Found a direct match
                if (nodePtr.node.children[cInt] != null)
                {
                    TrieNodePtr newNp = new TrieNodePtr(nodePtr, nodePtr.node.children[cInt]);
                    newList.Add(newNp);
                    newNp.addMatches(matches);
                }

                //If last change > 5, then add the rest of the children
                if (nodePtr.numChanges == 0 || cIdx - nodePtr.rightChangeIndex >= minDistance)
                {
                    foreach (var x in nodePtr.node.childrenList)
                    {
                        int charInt = x.Item1;
                        TrieNode childNode = x.Item2;

                        //Ignore direct match, was taken care of above
                        if (charInt == cInt)
                            continue;

                        TrieNodePtr newNp = new TrieNodePtr(nodePtr, childNode, cIdx);
                        newList.Add(newNp);
                        newNp.addMatches(matches);
                    }
                }
            }

            list = newList;



        }
    }

    /// <summary>
    /// Edges are letters, word formed by characters traversed
    /// </summary>
    sealed public class TrieNode
    {
        

        internal TrieNode[] children;
        //to avoid having to loop through the 26 children
        internal List<Tuple<int, TrieNode>> childrenList;
        private const int aInt = (int)'a';

        public int CurrentLength { get; private set; }
        public string WordMatch { get; private set; }

        private TrieNode()
        {
            children = new TrieNode[26];
            childrenList = new List<Tuple<int, TrieNode>>();
        }

        public static TrieNode createRootNode(Dictionary dict)
        {
            TrieNode root = new TrieNode();
            root.CurrentLength = 0;

            for (int dIdx = 0; dIdx < dict.words.Count; ++dIdx)
            {
                root.addWord(dict.words[dIdx]);
            }
            return root;
        }

        //Given text, find out what words match starting from startIdx
        public void parseText(string text, out List<WordMatch> matches, int startIdx = 0)
        {
            matches = new List<WordMatch>(10);

            List<TrieNodePtr> listPtrs = new List<TrieNodePtr>();
            TrieNodePtr root = new TrieNodePtr(this);
            
            listPtrs.Add(root);

            //Start from the root node and walk down text, we maintain a list
            //because we can change 1 character every TrieNode.minDistance 
            for (int cIdx = startIdx; cIdx < text.Length; ++cIdx)
            {
                char c = text[cIdx];
                int cInt = (int)c - aInt;

                TrieNodePtr.doMatch(cIdx - startIdx, cInt, ref listPtrs, matches);

                if (listPtrs.Count == 0)
                {
                    return;
                }
            }
        }

        private void addWord(string word)
        {
            TrieNode node = this;
            //Start at root
            Preconditions.checkState(node.CurrentLength == 0);
            

            for (int cIdx = 0; cIdx < word.Length; ++cIdx)
            {
                char c = word[cIdx];
                int cInt = (int)c - aInt;
                if (node.children[cInt] == null)
                {
                    TrieNode newNode = new TrieNode();
                    newNode.CurrentLength = cIdx + 1;
                    node.children[cInt] = newNode;
                    node.childrenList.Add(new Tuple<int, TrieNode>(cInt, newNode));
                }

                node = node.children[cInt];
            }

            Preconditions.checkState(node.WordMatch == null);
            node.WordMatch = word;
        }
    }
}
