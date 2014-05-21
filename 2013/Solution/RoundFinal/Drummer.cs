#if DEBUG
#define LOGGING_DEBUG
#define LOGGING_INFO
#define LOGGING_TRACE
#endif

using Utils.math;
using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;
using Utils.geom;
using Logger = Utils.LoggerFile;


namespace RoundFinal
{
	using LineSeg = LineSegment<double>;
	
	public class Drummer : InputFileProducer<DrummerInput>, InputFileConsumer<DrummerInput,string>
    {
    	
	 public DrummerInput createInput(Scanner scanner)
        {
            DrummerInput input = new DrummerInput();

            input.N = scanner.nextInt();
            input.T = new int[input.N];
            for(int i = 0; i < input.N; ++i)
            {
                input.T[i] = scanner.nextInt();
            }

            return input;
        }
        
        public string processInput(DrummerInput input)
        {
        	return "3";
        }
        
        //X + Y * index
        public bool IsEPossible(double E, DrummerInput input, out double X, out double Y)
        {
        	X = 0;
        	Y = 0;
        	List<LineSeg> segments = new List<LineSeg>();
        	
        	for(int i  = 0; i < input.T.Length; ++i)
        	{
        		segments.Add( LineExt.createSegmentFromCoords(i, input.T[i] - E, i, input.T[i] + E) );	
        	}
        	
        	return true;
        }
	}
	
	 public class DrummerInput
    {
        public int N;
        public int[] T;
    }
}
