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
            if (input.N <= 2)
            {
                return "0";
            }
            double smallestE = Double.MaxValue;

            //O(N^4) solution

            for(int i = 0; i < input.N; ++i)
            {
                for(int j = i+1; j < input.N; ++j)
                {
                    for(int k = 0; k < input.N; ++k)
                    {
                        if (k == i || k == j)
                            continue;

                        Line<double> line;
                        double E;

                        findE(input, i, j, k, out E, out line);

                        bool ok = true;

                        E = Math.Abs(E);

                        for(int idx = 0; idx < input.N; ++idx)
                        {
                            double y = (line.C - line.A * idx) / line.B;
                            double diff = Math.Abs(y - input.T[idx]);
                            if (diff > E + 1e-6)
                            {
                                ok = false;
                                break;
                            }
                        }

                        if (ok && E < smallestE)
                        {
                            smallestE = E;
                            Logger.LogDebug("Found best line y = {} + {}x with E {}", line.C/line.B,
                                -line.A/line.B, E);
                        }
                    }
                }
            }

            return smallestE.ToUsString(8);
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

        public void findE(DrummerInput input, int p1, int p2, int p3, out double E, out Line<double> line)
        {
            /*
             * Assume p1 and p2 both add or subtract E and p3 does opposite
             * 
             * delta (p2 - p1) == delta (p3 - p1)
             */

            double deltaY_p1p2 = input.T[p2] - input.T[p1];  // E's cancel out
            double deltaX_p1p2 = p2 - p1;

            double deltaY_p1p3 = input.T[p3] - input.T[p1];  // + 2E or - 2E
            double deltaX_p1p3 = p3 - p1;

            double lhs = deltaY_p1p2 / deltaX_p1p2 * deltaX_p1p3 - input.T[p3] + input.T[p1];

            E = lhs / 2;
            E *= -1;

            //E is positive means p1 - E, p2 - E, p3 + E is the line
            line = LineExt.createFromCoords(p1, input.T[p1] +  E,
                p2, input.T[p2] + E);

            Preconditions.checkState(line.onLine(p3, input.T[p3] - E));
           
            
        }
    }
	
	 public class DrummerInput
    {
        public int N;
        public int[] T;
    }
}
