#define LOGGING_DEBUG
#define LOGGING_INFO
using CodeJam._2014.Round2;
using CodeJam.Utils.geom;
using CodeJam.Utils.graph;
using CodeJam.Utils.math;
using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;
using Wintellect.PowerCollections;
using Logger = Utils.LoggerFile;



namespace Year2014.Round4.Problem3
{
    
    public class SymmetricTreesInput
    {
       
        public int N;

        public char[] Colors;
        //public Dictionary<int,int>[] Conn;
        public Dictionary<int,String>[] Conn;
    }

	public class SymmetricTrees : InputFileProducer<SymmetricTreesInput>, InputFileConsumer<SymmetricTreesInput, String>
    {
        public SymmetricTreesInput createInput(Scanner scanner)
        {
			//scanner.enablePlayback();
			SymmetricTreesInput input = new SymmetricTreesInput();
			           
            input.N = scanner.nextInt();

            Ext.createArrayWithFunc(out input.Colors, input.N, (idx) => scanner.nextWord()[0]);

            Ext.createArrayWithNew(out input.Conn, input.N);
            //Ext.createArrayWithNew(out input.Conn, input.N);

            for (int i = 0; i < input.N-1; ++i)
            {
                int a = scanner.nextInt();
                int b = scanner.nextInt();

                input.Conn[a - 1][b - 1] = "-1";
                input.Conn[b - 1][a - 1] = "-1" ;
            }

            //Logger.LogInfo("Input {}", scanner.finishPlayBack());
            return input;
		}

        public static String encode_subtree(int a, int parent, SymmetricTreesInput input)
        {
            List<String> children = new List<String>();

            List<int> connections = new List<int>(input.Conn[a].Keys);

            foreach (int b in connections)
            {
                //int b = kv.Key;
                if ( b != parent)
                {
                    if (input.Conn[a][b] == "-1")
                    {
                        input.Conn[a][b] = encode_subtree(b, a, input);
                        
                    }
                    children.Add(input.Conn[a][b]);
                }
            }
    
            StringBuilder m = new StringBuilder();
            m.Append('(').Append(input.Colors[a]);

            children.Sort();

            foreach(String c in children)
            {
                m.Append(',').Append( c );
            }
            m.Append(')');
            return m.ToString();
        }

        public bool rec_symmetric(int a, int parent, SymmetricTreesInput input)
        {
            Dictionary<String, int> first_pair = 
                new Dictionary<string,int>();

            foreach (var bkv in  input.Conn[a])
            {
                int b = bkv.Key;
                if (b != parent)
                {
                    if (first_pair.ContainsKey( input.Conn[a][b] ))
                    {
                        first_pair.Remove(input.Conn[a][b]);
                    } else {
                        first_pair[ input.Conn[a][b] ] = b;
                    }
                }
            }
    
            var keys = new List<int>(first_pair.Values);

            if (keys.Count == 0)
                return true;

            bool ok = rec_symmetric(keys[0], a, input);
  
            if (keys.Count == 1 || !ok)
                return ok;

            //# Non-root is only allowed one unpaired branch.
            if (keys.Count > 2 || parent != -1)
                return false;

            return rec_symmetric(keys[1], a, input);
        }
        public bool symmetric(SymmetricTreesInput input)
        {
            //# No vertex in the middle line.
            for(int a = 0; a < input.N; ++a)
            {
                foreach(var kv in input.Conn[a])
                {
                    int b = kv.Key;
                    if (input.Conn[a][b] == input.Conn[b][a])
                        return true;
                }

            }

            for (int a = 0; a < input.N; ++a)
            {
                if (rec_symmetric(a, -1, input))
                    return true;
            }
            return false;
        }
  

		public String processInput(SymmetricTreesInput input)
        {
            for (int a = 0; a < input.N; ++a )
            {
                encode_subtree(a, -1, input);
            }

            if (symmetric(input))
                return "SYMMETRIC";
            else
                return "NOT SYMMETRIC";
		}
	}

}