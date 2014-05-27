/*#define LOGGING
#define LOGGING_DEBUG
#define LOGGING_INFO
#define LOGGING_TRACE*/
#define FRAC
using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils.geom;
using Utils.math;
using Utils;

using Logger = Utils.LoggerFile;

namespace Round2_2013.Problem4
{
#if FRAC
    using Line = Line<BigFraction>;
    using Point = Point<BigFraction>;
    using NumType = BigFraction;
    using System.Numerics;
#else
    using Line = LineNumeric<DoubleNumeric>;
    using Point = Point<DoubleNumeric>;
    using NumType = DoubleNumeric;
#endif


    public class PongMain : InputFileConsumer<PongInput, string>, InputFileProducer<PongInput>
    {
        
        public static BigInteger callFirstHit(NumType p0_f, NumType deltaP_f,
        	NumType lower_f, NumType upper_f, BigInteger height)
        {
        	Logger.LogTrace( "callFirstHit p0 {} {} {} {} {}", p0_f, deltaP_f,
        		lower_f, upper_f, height);
        	
        	BigInteger lcd = BigFraction.LeastCommonDenominator(p0_f, deltaP_f, lower_f, upper_f);
        	
        	height *= lcd;;
        	BigInteger height2 = 2 * height;
			BigInteger p0 = (BigInteger) (p0_f * lcd) % height2 ;
			BigInteger deltaP = (BigInteger) (lcd*deltaP_f) % height2 ;
			BigInteger lower = (BigInteger) (lcd*lower_f);
			BigInteger upper = (BigInteger) (lcd*upper_f);
			
			
			Logger.LogTrace( "callFirstHit scaled by {} p0: [{}] \u0394p: [{}] l: {} up: {} h: {} h*2: {}", 
				lcd,
				p0, deltaP,
        		lower, upper, height, height2);
        	
        	Preconditions.checkState( 0 <= lower && lower <= height, "1");
        	Preconditions.checkState( 0 <= upper && upper <= height, "2");
        	Preconditions.checkState( lower <= upper, "3");
        	Preconditions.checkState( 0 <= deltaP && deltaP < height2, "4" );
        	Preconditions.checkState( 0 <= p0 && p0  < height2 , "5");
        	
        	/*
        	int showStart = 0;
			int numPoints = 8;
			for (int pIdx = showStart; pIdx <= showStart+numPoints; ++pIdx)
			{
				NumType pi = ( deltaP * pIdx) % (height2);

				Logger.LogTrace("Scaled point idx {} pi%2h: [{}] pi%h: {} ",
						pIdx, pi, pi % height);
			}*/
			
			List<BigInteger[]> ranges = new List<BigInteger[]>();
			
			BigInteger upperDiff = height - upper;
			
			Preconditions.checkState(upperDiff >= 0);
			Preconditions.checkState(upperDiff <= height);
			
			ranges.Add( new BigInteger[2] { -upperDiff + 1 - p0, lower - 1 - p0 } );
			ranges.Add( new BigInteger[2] { upper + 1 - p0, height+lower - 1 - p0 } );
			
			BigInteger first = -1;
			
			Action<BigInteger> funcUpdateFirst = (newFirstHitVal) => 
			{
				if (newFirstHitVal == -1)
					return;
				
				if (first == -1 || newFirstHitVal < first)
						first = newFirstHitVal;	
			};
			
			foreach( BigInteger[] range in ranges)
			{
				Logger.LogTrace("before Range {} to {}", range[0], range[1]);
				
				for(int i = 0; i < 2; ++i)
				{
					while (range[i] < 0)
						range[i] += height2;
					
					Preconditions.checkState(range[i] >= 0, "6");
					Preconditions.checkState(range[i] < height2, "7");
				}
				
				Logger.LogTrace("Range {} to {}", range[0], range[1]);
				BigInteger firstHit;
				
				if (range[0] > range[1])
				{
					firstHit = ericFirstHit(  deltaP,  height2,
					 range[0],  height2 );
					
					funcUpdateFirst(firstHit);
				
					firstHit = ericFirstHit(  deltaP,  height2,
					 0,  range[1] );
					
					funcUpdateFirst(firstHit);
				} else {
					
					Preconditions.checkState(range[0] <= range[1]);
					
					firstHit = ericFirstHit(  deltaP,  height2,
						 range[0],  range[1] );
					
					funcUpdateFirst(firstHit);
				}
				
			}
			
			return first;
        }

        /*
        Find the first point x in
        Ax % modulus that falls in [a,b]
        */
        public static BigInteger ericFirstHit(BigInteger deltaP, BigInteger modulus, BigInteger a, BigInteger b, int rCheck = 0)
        {
            Preconditions.checkState(rCheck <= 25);
            Preconditions.checkState(modulus > 0);
            Logger.LogTrace("");
            Logger.LogTrace("First hit deltaP {} modulus {}  a {} b {}", deltaP, modulus, a, b);
            Preconditions.checkState(a <= b);

            deltaP %= modulus;
            Logger.LogTrace("\u0394p = {}", deltaP);

            if (deltaP == 0)
            {
                return -1;
            }
            //We want to be able to have at least 2 points within the modulus
            if (2 * deltaP > modulus)
            {
                //Flipping the problem
                return ericFirstHit(modulus - deltaP, modulus, modulus - b, modulus - a, 1+rCheck);
            }

            //The first point that goes past a, the beginning of the interval
            BigInteger bestIndex = (a+deltaP-1) / deltaP;
            BigInteger best = deltaP * bestIndex;
            Logger.LogTrace("bestIndex {} best {}", bestIndex, best);
            if (a <= best && best <= b)
            {
                Logger.LogTrace("Returning {}", bestIndex);
                return bestIndex;
            }

            //Calculate first wrap around paint
            BigInteger firstWrapAroundIndex = 1 + modulus / deltaP;
            BigInteger firstWrapAroundPoint = (firstWrapAroundIndex * deltaP) % modulus;

            Logger.LogTrace("wrap around index {} point {}", firstWrapAroundIndex, firstWrapAroundPoint);

            //This wrap around point (P in the explanation) is a step value, we want to create
            //a sub problem

            //First, we need to know what starting points would have meant a hit in the interval
            BigInteger newA = a % deltaP;
            BigInteger newB = b % deltaP;
            Logger.ChangeIndent(4);
            BigInteger cyclesNeeded = ericFirstHit(firstWrapAroundPoint, deltaP, newA, newB, 1 + rCheck);
            Logger.ChangeIndent(-4);

            if (cyclesNeeded == -1)
                return -1;

            //If we cycle C times, then we must travel modulus * C 
            BigInteger pointsInCycles = (modulus * cyclesNeeded) / deltaP;
            //(firstWrapAroundIndex-1)
            BigInteger ret =  pointsInCycles + a / deltaP + 1;
            Logger.LogTrace("Returning {}+{} + {} = {}", 
                firstWrapAroundIndex-1, pointsInCycles, a / deltaP, ret);
            return ret;
        }


        public static NumType calcAdd(NumType p0, NumType deltaP, BigInteger n, NumType height)
        {

            NumType pos = p0 + n * deltaP;

            BigInteger rem = (BigInteger)(pos / height);

            pos -= (rem * height);

            if (rem % 2 == 1)
            {
                pos = height - pos;
            }
            //Logger.LogDebug("CalcAdd {} + {} * {} = {} w.r.t height {}", p0, n, deltaP, pos, height);
            return pos;
        }

        private static void calcPoints2hAndDiff(NumType p0,
            NumType deltaP, BigInteger height, BigInteger start, int count,
        out NumType[] points,
            out NumType[] points2h, out NumType[] diffs
            )
        {
            if (start < 0)
                start = 0;

            points = new NumType[count];
            points2h = new NumType[count];

            diffs = new NumType[count];

            for (int i = 0; i < count; ++i)
            {
                points[i] = calcAdd(p0, deltaP, start + i, height);
                points2h[i] = (p0 + (start + i) * deltaP) % (2 * height);

                if (i > 0)
                    diffs[i] = (points[i] - points[i - 1]).Abs();
            }
        }

        private static void calcPointsAndDiff(NumType p0,
            NumType deltaP, BigInteger height, BigInteger start, int count,
            out NumType[] points, out NumType[] diffs
            )
        {
            if (start < 0)
                start = 0;

            points = new NumType[count];
            diffs = new NumType[count];

            for (int i = 0; i < count; ++i)
            {
                points[i] = calcAdd(p0, deltaP, start + i, height);

                if (i > 0)
                    diffs[i] = (points[i] - points[i - 1]).Abs();
            }
        }


        public static long calcToTargetManual(NumType p0, NumType deltaP, BigInteger height, NumType targetDiff)
        {
            return calcToTargetManual(p0, deltaP, height, targetDiff, (long)(3 * height));
        }
        public static long calcToTargetManual(NumType p0, NumType deltaP, BigInteger height, NumType targetDiff, long limit)
        {
            //Use calcAdd because p0 could be > height
            NumType lastPoint = calcAdd(p0, deltaP, 0, height);

            for (int i = 1; i <= limit; ++i)
            {
                NumType p = calcAdd(p0, deltaP, i, height);
                NumType diff = (p - lastPoint).Abs();
                Logger.LogDebug("manual calc.  p {} diff {} ", p, diff);
                if (diff > targetDiff)
                {
                    Logger.LogDebug("Found point {} larger than target {}", i, targetDiff);
                    return i;
                }

                lastPoint = p;
            }

            return -1;
        }
       
        public static long calcToTarget(NumType p0, NumType deltaP, BigInteger height, NumType targetDiff)
        {
            NumType calculatedDeltaD;
            return calcToTarget(p0, deltaP, height, targetDiff, out calculatedDeltaD);
        }

        public static long calcToTarget(NumType p0, NumType deltaP, BigInteger height, NumType targetDiff, out NumType calculatedDeltaD)
        {
            Logger.LogTrace("calcToTarget p0 {} + \u0394p {} * N  height: {}  target: {} ",
                p0, deltaP, height, targetDiff);

            //Change in position of 2h is a no-op, so reduce ΔP by that amount 
            BigInteger rem = (deltaP / (2 * height)).floor();
            deltaP -= (rem * 2 * height);

            //Anything above H bounces off, so H+1 ==> H-1  H+n ==> H-n
            NumType maxDiff = deltaP;
            if (maxDiff > height)
            {
                maxDiff = 2 * height - maxDiff;
            }

            Logger.LogTrace("max diff {} \u0394P {}", maxDiff, deltaP);

            if (targetDiff >= maxDiff)
            {
                Logger.LogTrace("targetDiff {} >= maxDiff {}", targetDiff, maxDiff);
                calculatedDeltaD = 0;
                return -1;
            }

            NumType[] points;
            NumType[] diffs;

            calcPointsAndDiff(p0, deltaP, height, 0, 3, out points, out diffs);

            calculatedDeltaD = 2 * (height - maxDiff);

            if (calculatedDeltaD.Equals( (NumType)0))
            {
                Logger.LogDebug("deltaD % height = 0, using manual");
                return calcToTargetManual(p0, deltaP, height, targetDiff, 3);
            }

            if ((height / calculatedDeltaD) < 10)
            {
                Logger.LogDebug("using calc to target manual");
                long ans = calcToTargetManual(p0, deltaP, height, targetDiff);
                Preconditions.checkState(ans > 0);
                return ans;
            }

            Logger.LogTrace("DeltaD calculated = {}.  Initial Points {} Diffs {}",
                calculatedDeltaD,
                points.ToCommaString(), diffs.Skip(1).ToCommaString());

            if (diffs[1] > targetDiff)
                return 1;

            if (diffs[2] > targetDiff)
                return 2;

            NumType deltaD = (diffs[2] - diffs[1]).Abs();
            long offset = 0;

            if (!deltaD.Equals(calculatedDeltaD))   //diffs[1].Equals(diffs[2]))
            {
                //The 2 diffs during the rebound should = estimatedDelta

                offset = 1;

                calcPointsAndDiff(p0, deltaP, height, 1, 3,
                    out points, out diffs);

                deltaD = (diffs[2] - diffs[1]).Abs();


                Logger.LogDebug("After offset + 1: points {}.  Diffs {}", points.ToCommaString(), diffs.ToCommaString());

            }

            //If descending, go to rebound
            if (diffs[2] < diffs[1])
            {
                Preconditions.checkState(calculatedDeltaD.Equals(deltaD));

                long pointsToZero = (long)(diffs[2] / deltaD).ceil();

                NumType[] pointsAtZero;
                NumType[] diffsAtZero;

                calcPointsAndDiff(p0, deltaP, height, 1 + pointsToZero, 4, out pointsAtZero, out diffsAtZero);


                Logger.LogTrace("Points to zero ceil( {} / {} ) = {}.  Point values: {} Diffs {}",
                    diffs[2], deltaD, pointsToZero, pointsAtZero.ToCommaString(), diffsAtZero.Skip(1).ToCommaString());


                Preconditions.checkState(diffsAtZero[1] - deltaD <= 0);
                Preconditions.checkState((diffsAtZero[2] - diffsAtZero[1]).Equals(deltaD));
                Preconditions.checkState((diffsAtZero[3] - diffsAtZero[2]).Equals(deltaD)); //now going positive

                //long pointsLeft = calcToTarget(pb, deltaP, height, targetDiff, true);
                offset += pointsToZero + 2;

                calcPointsAndDiff(p0, deltaP, height, 2 + pointsToZero, 3,
                    out points, out diffs);

                //DeltaD is already à jour
            }


            Preconditions.checkState(calculatedDeltaD.Equals(deltaD));

            NumType div = (targetDiff - diffs[2]) / deltaD;
            div = div.reduce();
            BigInteger pointsToGo = (div).ceil();

            if (div.Denominator.Equals(BigInteger.One))
            {
                pointsToGo += 1;
            }

            NumType[] finalPoints;
            NumType[] finalDiffs;

            calcPointsAndDiff(p0, deltaP, height, offset + pointsToGo, 5, out finalPoints, out finalDiffs);

            Logger.LogTrace("PointsToGo {} offset {} Final Points {}.  Final diffs {}", pointsToGo, offset, finalPoints.ToCommaString(), finalDiffs.Skip(1).ToCommaString());
            Logger.LogTrace("Return {}", pointsToGo + 2 + offset);
            return offset + (long)pointsToGo + 2;
        }

        public void ShowOutput()
        {
            //calcToTarget p0 9224407 + Δp 47571681280 * N  height: 654329  target: 654320 
            int height = 40;

            long toAddStart = 30;
            long toAddEnd = toAddStart + 0;

            NumType offsetStart = new BigFraction(51, 4);
            offsetStart = 0;
            NumType offsetStop = 10;
            NumType offsetStep = 1;

            int points = 10;

            for (long toAdd = toAddStart; toAdd <= toAddEnd; ++toAdd)
            {
                for (NumType offset = offsetStart; offset <= offsetStop; offset += offsetStep)
                {
                    List<NumType> posList = new List<NumType>();
                    posList.Add(offset);

                    for (int pNum = 0; pNum <= points; ++pNum)
                    {
                        NumType ans = PongMain.calcAdd(offset, toAdd, pNum, height);

                        Logger.LogDebug("{} + n {} * {} = {} = {} : {} [diff {}] with respect to height {}",
                            offset, pNum, toAdd, offset + pNum * toAdd,
                            (offset + pNum * toAdd) % (2 * height),
                            ans, (ans - posList.GetLastValue()), height);

                        posList.Add(ans);
                    }
                }
            }
        }

        public static bool calculateForbiddenInterval(
            NumType target, NumType deltaP, BigInteger height, out NumType lower, out NumType upper)
        {
            Logger.LogTrace("calculateForbiddenInterval deltaP {} target {} height {}", deltaP, target, height);

            deltaP %= 2 * height;


            NumType maxDiff = deltaP;
            if (maxDiff > height)
            {
                maxDiff = 2 * height - maxDiff;
            }

            Logger.LogTrace("calculateForbiddenInterval deltaP {} maxDiff {} ", deltaP, maxDiff);
            if (maxDiff <= target)
            {
                lower = -1; upper = -1;
                return false;
            }

            NumType cutOff = height - (deltaP + target) / 2;

            lower = cutOff;

            NumType upperCutoff = (deltaP - target) / 2;

            upper = height - upperCutoff; // - (height - maxDiff);

            return true;
        }


        [Obsolete("From my attempt, finds how long it takes to cycle (each elem in cycle has constant diff with corr. element in next cycle")]
        public static int findCycle(NumType p0, NumType deltaP, BigInteger mod, out NumType diff)
        {
            Func<int, NumType> funcCalcP = (i) => { return (p0 + deltaP * i) % mod; };
            diff = 0;

            for (int cycleLength = 1; cycleLength <= 20; ++cycleLength)
            {
                diff = funcCalcP(cycleLength) - funcCalcP(0);
                bool cycleOK = true;

                for (int i = 0; i < cycleLength && cycleOK; ++i)
                {
                    NumType pLast = funcCalcP(i);

                    for (int n = 1; n <= 3; ++n)
                    {
                        NumType p = funcCalcP(i + n * cycleLength);
                        NumType diffCheck = p - pLast;

                        if (diff != diffCheck)
                        {
                            cycleOK = false;

                            Logger.LogTrace("cycle len {}.  pts {} {}  diff {} {}",
                                cycleLength, i + (n - 1) * cycleLength, i + n * cycleLength,
                                diff, diffCheck);
                            break;
                        }

                        pLast = p;
                    }

                }

                if (cycleOK)
                {
                    return cycleLength;
                }

            }

            return -1;

        }

        [Obsolete("Using cycle info, find when a point will wrap around")        ]
        public static BigInteger accel(NumType p0, NumType deltaP,
            BigInteger twiceHeight, int cycleLength,
            NumType cycleDiff, NumType lower, NumType upper)
        {
            Logger.LogTrace("Cycle length {} diff {}", cycleLength, cycleDiff);
            Func<int, NumType> funcCalcP = (i) => { return (p0 + deltaP * i) % twiceHeight; };

            BigInteger minJump = 0;

            for (int i = 0; i < cycleLength; ++i)
            {
                NumType p = funcCalcP(i) % (twiceHeight / 2);
                if (p < lower || p > upper)
                {
                    Logger.LogTrace("Already in forbidden interval");
                    return 0;
                }
            }
            if (cycleDiff < 0)
            {
                for (int i = 0; i < cycleLength; ++i)
                {
                    NumType p = funcCalcP(i);
                    BigInteger jump = ((p - lower) / -cycleDiff).floor();
                    if (i == 0 || jump < minJump)
                        minJump = jump;
                }
            }
            else if (cycleDiff > 0)
            {
                for (int i = 0; i < cycleLength; ++i)
                {
                    NumType p = funcCalcP(i) % (twiceHeight / 2);
                    Logger.LogTrace("Point {} value {} upper {}", i, p, upper);
                    BigInteger jump = ((upper - p) / cycleDiff).floor();
                    if (i == 0 || jump < minJump)
                    {
                        minJump = jump;
                        Logger.LogTrace("Minimum jump from point {}, index {}",
                            p, i);
                    }
                }
            }

            return minJump * cycleLength;

        }

        public string processInput(PongInput input)
        {
            //ShowOutput();
            //return "hoetnuh";
            // processInputManual(input);

            bool switchTeams = false;

            if (input.VX < 0)
            {
                input.VX = -input.VX;
                Ext.swap(ref input.numRightTeam, ref input.numLeftTeam);
                Ext.swap(ref input.speedLeftTeam, ref input.speedRightTeam);
                switchTeams = true;
                input.X = input.widthField - input.X;
            }

            Line teamLeft = LineExt.createFromCoords<BigFraction>(0, 0, 0, 1);
            Line teamRight = LineExt.createFromCoords<BigFraction>(input.widthField, 0, input.widthField, 1);

            Line initialVector = LineExt.createFromCoords<BigFraction>(input.X, input.Y, input.X + input.VX, input.Y + input.VY);

            var p1 = initialVector.intersection(teamRight);

            Line rebound = LineExt.createFromPoints(p1, p1.Add(new Point(-input.VX, input.VY)));

            var p2 = rebound.intersection(teamLeft);
            Logger.LogDebug("Initial point {}, {} direction {}, {}", input.X, input.Y, input.VX, input.VY);
            Logger.LogDebug("First intersection {} 2nd {}", p1, p2);

            NumType yDif = p2.Y.Subtract(p1.Y);

            if (yDif < 0)
            {
                yDif += input.heightField;
            }

            BigFraction t0 = new BigFraction(input.widthField - input.X, input.VX);
            BigFraction t1 = new BigFraction(input.widthField, input.VX);

            BigInteger[] teamSize = new BigInteger[] { input.numLeftTeam, input.numRightTeam };
            
            BigInteger[] teamSpeed = new BigInteger[2] { input.speedLeftTeam, input.speedRightTeam };

            Logger.LogDebug("Width {} Height {}", input.widthField, input.heightField);
            Logger.LogDebug("Left team {} players at {} speed", teamSize[0], teamSpeed[0]);
            Logger.LogDebug("Right team {} players at {} speed", teamSize[1], teamSpeed[1]);
            Logger.LogDebug("Time = {} + {} * n.  Position = {} + {} * n", t0, t1, p1.Y, yDif);

            BigInteger firstPointFail = 0;
            int failTeam = -1;
            

            for (int team = 0; team <= 1; ++team)
            {

                Logger.LogDebug("\n\nTeam {} ", team);
                NumType deltaT = 2 * teamSize[team] * t1;
                NumType disPossible = deltaT * teamSpeed[team];

                NumType diff = yDif.Multiply(2 * teamSize[team]);

                BigFraction lower, upper;
                bool found = PongMain.calculateForbiddenInterval(disPossible,
                    diff, input.heightField, out lower, out upper);

                Logger.LogTrace("testForbiddenInterval deltaP {} target {} lower {} upper {} found {}",
                    diff, disPossible, lower, upper, found);

                if (!found)
                    continue;

                
                NumType startingPoint = p1.Y + yDif * (1 - team);
                                
				BigInteger firstPointFail0 = teamSize[team] + callFirstHit(startingPoint, yDif * 2, lower, upper, input.heightField);
				
				Logger.LogTrace("failTeam {} firstPointFail {} firstPointFail0 {}", failTeam, firstPointFail, firstPointFail0);
				
				//In the case of a tie, the LEFT team (teamNum 0) wins
				if (failTeam == -1 || firstPointFail0 <= firstPointFail )
				{
					firstPointFail = firstPointFail0 ;	
					failTeam = team;
				}
                Logger.LogDebug("firstPointFail {}.",  firstPointFail);

                
                int showStart = 0;
                int numPoints = 8;
                

                for (int pIdx = showStart; pIdx <= showStart+numPoints; ++pIdx)
                {
                    BigInteger realIndex = 1 - team + 2 * (pIdx);
                    NumType pi = (p1.Y + yDif * realIndex) % (2 * input.heightField);


                    Logger.LogTrace("debug calculate team point {} idx {} pi%2h: [{}] pi%h: {} pi: {} upper: {} upper - pi%h: {}",
                            pIdx, realIndex, pi, pi % input.heightField, calcAdd(p1.Y, yDif, realIndex, input.heightField), upper, upper - pi % input.heightField);
                }

               
            }

            
            if (failTeam != -1)
            {
            	//firstPointFail = BigInteger.Max(firstPointFail, teamSize[failTeam]);
                int team = failTeam; //firstPointFail % 2 == 0 ? 1 : 0;
               // BigInteger num = (firstPointFail + team) / 2;
                string teamName = ((!switchTeams && team == 0) || (switchTeams && team == 1)) ? "RIGHT" : "LEFT";
                Logger.LogDebug(teamName + " " + firstPointFail);
                return teamName + " " + firstPointFail;
            }


            return "DRAW";
        }
        
        #if OLDCODE
        //Trying forbidden interval + jumping until a point wraps around
        public string processInputUsingJumps(PongInput input)
        {
           

            bool switchTeams = false;

            if (input.VX < 0)
            {
                input.VX = -input.VX;
                Ext.swap(ref input.numRightTeam, ref input.numLeftTeam);
                Ext.swap(ref input.speedLeftTeam, ref input.speedRightTeam);
                switchTeams = true;
                input.X = input.widthField - input.X;
            }

            Line teamLeft = LineExt.createFromCoords<BigFraction>(0, 0, 0, 1);
            Line teamRight = LineExt.createFromCoords<BigFraction>(input.widthField, 0, input.widthField, 1);

            Line initialVector = LineExt.createFromCoords<BigFraction>(input.X, input.Y, input.X + input.VX, input.Y + input.VY);

            var p1 = initialVector.intersection(teamRight);

            Line rebound = LineExt.createFromPoints(p1, p1.Add(new Point(-input.VX, input.VY)));

            var p2 = rebound.intersection(teamLeft);
            Logger.LogDebug("Initial point {}, {} direction {}, {}", input.X, input.Y, input.VX, input.VY);
            Logger.LogDebug("First intersection {} 2nd {}", p1, p2);

            NumType yDif = p2.Y.Subtract(p1.Y);

            if (yDif < 0)
            {
                yDif += input.heightField;
            }

#if FRAC
            BigFraction t0 = new BigFraction(input.widthField - input.X, input.VX);
            BigFraction t1 = new BigFraction(input.widthField, input.VX);
#else
            NumType t0 = (input.widthField - input.X) / (double) input.VX;
            NumType t1 = input.widthField / (double) input.VX;
#endif

            BigInteger[] teamSize = new BigInteger[] { input.numLeftTeam, input.numRightTeam };
            
            BigInteger[] teamSpeed = new BigInteger[2] { input.speedLeftTeam, input.speedRightTeam };

            Logger.LogDebug("Width {} Height {}", input.widthField, input.heightField);
            Logger.LogDebug("Left team {} players at {} speed", teamSize[0], teamSpeed[0]);
            Logger.LogDebug("Right team {} players at {} speed", teamSize[1], teamSpeed[1]);
            Logger.LogDebug("Time = {} + {} * n.  Position = {} + {} * n", t0, t1, p1.Y, yDif);

            BigInteger firstPointFail = long.MaxValue;

            BigInteger firstPointFail0 = long.MaxValue;

            for (int team = 0; team <= 1; ++team)
            {

                Logger.LogDebug("\n\nTeam {} ", team);
                NumType deltaT = 2 * teamSize[team] * t1;
                NumType disPossible = deltaT * teamSpeed[team];

                NumType diff = yDif.Multiply(2 * teamSize[team]);

                BigFraction lower, upper;
                bool found = PongMain.calculateForbiddenInterval(disPossible,
                    diff, input.heightField, out lower, out upper);

                Logger.LogTrace("testForbiddenInterval deltaP {} target {} lower {} upper {} found {}",
                    diff, disPossible, lower, upper, found);

                if (!found)
                    continue;

                NumType cycleDiff;
                int cycleLength = findCycle(p1.Y + yDif * (1 - team), yDif * 2, 2 * input.heightField, out cycleDiff);

                Logger.LogTrace("Cycle every {}", cycleLength);

                NumType startingPoint = p1.Y + yDif * (1 - team);
                BigInteger jump = 0;

                bool foundFirstPointFail = false;

                
				firstPointFail0 = callFirstHit(startingPoint, yDif * 2, lower, upper, input.heightField);
				
                Logger.LogDebug("First point {}.  Starting point {}", firstPointFail0, startingPoint % (2 * input.heightField));

                for (int j = 0; j < 1000 && !foundFirstPointFail; ++j)
                {
                    startingPoint = p1.Y + yDif * (1 - team + 2 * jump);

                    BigInteger nextJump = accel(startingPoint,
                        yDif * 2, 2 * input.heightField, cycleLength, cycleDiff, lower, upper);

                    jump += nextJump;
                    Logger.LogTrace("jump {} nextJump {} yDif*2 {}", jump, nextJump, yDif*2);




                    for (int pIdx = 0; pIdx <= 2 * cycleLength && !foundFirstPointFail; ++pIdx)
                    {
                        BigInteger realIndex = 1 - team + 2 * (pIdx + jump);
                        NumType pi = (p1.Y + yDif * realIndex) % (2 * input.heightField);

                        NumType pi_mh = pi % input.heightField;

                        Logger.LogTrace("calculate team point {} idx {} pi%2h [{}] pi%h {}, pi {} upper: {} upper - pi%h: {}",
                            pIdx, realIndex, pi, pi_mh, calcAdd(p1.Y, yDif, realIndex, input.heightField), upper, upper - pi_mh);



                        if (pi_mh < lower || pi_mh > upper)
                        {
                            NumType pj = PongMain.calcAdd(p1.Y, yDif, realIndex + 2 * teamSize[team], input.heightField);
                            NumType diffBet = (pi - pj).Abs();

                            Logger.LogDebug("testForbiddenIntervalHelper pi [{}] pj [{}] diff [{}] target {} lower {} upper {}",
                            pi, pj, diffBet, disPossible, lower, upper);

                            Logger.LogDebug("Found point realIndex: {} next after realIndex (actual failure point) {}", realIndex, realIndex + 2 * teamSize[team]);
                            firstPointFail = BigInteger.Min(firstPointFail, realIndex + 2 * teamSize[team]);
                            foundFirstPointFail = true;
                            break;
                        }

                    }

                    jump += cycleLength;

                }
            }

            
            if (firstPointFail != long.MaxValue)
            {
                int team = firstPointFail % 2 == 0 ? 1 : 0;
                BigInteger num = (firstPointFail + team) / 2;
                string teamName = ((!switchTeams && team == 0) || (switchTeams && team == 1)) ? "RIGHT" : "LEFT";
                Logger.LogDebug(teamName + " " + num);
                return teamName + " " + num;
            }


            return "DRAW";
        }
        
        #endif

        //Works for small, cycles through each player, finding when the player
        //will reach a target difference
        public string processInputSmall(PongInput input)
        {
        	bool switchTeams = false;

            if (input.VX < 0)
            {
                input.VX = -input.VX;
                Ext.swap(ref input.numRightTeam, ref input.numLeftTeam);
                Ext.swap(ref input.speedLeftTeam, ref input.speedRightTeam);
                switchTeams = true;
                input.X = input.widthField - input.X;
            }

            Line teamLeft = LineExt.createFromCoords<BigFraction>(0, 0, 0, 1);
            Line teamRight = LineExt.createFromCoords<BigFraction>(input.widthField, 0, input.widthField, 1);

            Line initialVector = LineExt.createFromCoords<BigFraction>(input.X, input.Y, input.X + input.VX, input.Y + input.VY);

            var p1 = initialVector.intersection(teamRight);



            Line rebound = LineExt.createFromPoints(p1, p1.Add(new Point(-input.VX, input.VY)));

            var p2 = rebound.intersection(teamLeft);
            Logger.LogDebug("Initial point {}, {} direction {}, {}", input.X, input.Y, input.VX, input.VY);
            Logger.LogDebug("First intersection {} 2nd {}", p1, p2);

            NumType yDif = p2.Y.Subtract(p1.Y);

            if (yDif < 0)
            {
                yDif += input.heightField;
            }

#if FRAC
            BigFraction t0 = new BigFraction(input.widthField - input.X, input.VX);
            BigFraction t1 = new BigFraction(input.widthField, input.VX);
#else
            NumType t0 = (input.widthField - input.X) / (double) input.VX;
            NumType t1 = input.widthField / (double) input.VX;
#endif

            List<Tuple<NumType, NumType>>[] teamPlayerPos = new List<Tuple<NumType, NumType>>[2]; //time, loc pair
            teamPlayerPos[0] = new List<Tuple<NumType, NumType>>(); //left 
            teamPlayerPos[1] = new List<Tuple<NumType, NumType>>(); //right
            int[] teamSize = new int[] { (int)input.numLeftTeam, (int)input.numRightTeam };
            
            long[] teamSpeed = new long[2] { (long)input.speedLeftTeam, (long)input.speedRightTeam };

            Logger.LogDebug("Width {} Height {1}", input.widthField, input.heightField);
            Logger.LogDebug("Left team {} players at {} speed", teamSize[0], teamSpeed[0]);
            Logger.LogDebug("Right team {} players at {} speed", teamSize[1], teamSpeed[1]);
            Logger.LogDebug("Time = {} + {} * n.  Position = {} + {} * n", t0, t1, p1.Y, yDif);

            long firstPointFail = long.MaxValue;
                       

            for (int team = 0; team < 2; ++team)
            {
                NumType deltaT = 2 * teamSize[team] * t1;
                NumType disPossible = deltaT * teamSpeed[team];

                NumType diff = yDif.Multiply(2 * teamSize[team]);

                for (int playerNum = 0; playerNum < teamSize[team]; ++playerNum)
                {
                    //if (playerNum != 166 || team != 0)
                    // continue;

                    int firstPoint = 2 * playerNum + 1 - team;
                    NumType firstPointLoc = p1.Y + yDif * firstPoint;

                    Logger.LogTrace("\n\nCalculate player {} team {}:  firstPoint index {} value {}", playerNum, team, firstPoint, firstPointLoc);
                    long pointFailPlayer = calcToTarget(firstPointLoc, diff, input.heightField, disPossible);

                    //long pointFailCheck = calcToTargetManual(firstPointLoc, diff, input.heightField, disPossible);

                    Logger.LogTrace("\nPlayer {} team {} first point index of player: [{}] \n player point index where target diff exceeded: [{}]", playerNum, team, firstPoint, pointFailPlayer);

                    //Logger.LogTrace("fails points {}", new int[]{1,2,3}.Select( (i) => firstPoint + i * 2 * teamSize[team]).ToCommaString());

                    if (pointFailPlayer != -1)
                    {
                        //Change coordinates from player [every 2 * teamsize points]
                        long pointFail = firstPoint + pointFailPlayer * 2 * teamSize[team];
                        firstPointFail = Math.Min(firstPointFail, pointFail);


                        NumType[] failValues;
                        NumType[] failDiffs;

                        //calcPointsAndDiff(p1.Y, yDif, input.heightField, pointFail - 5, 10, out failValues, out failDiffs);
                        calcPointsAndDiff(firstPointLoc, diff, input.heightField, pointFailPlayer - 15, 25, out failValues, out failDiffs);

                        Logger.LogTrace("Point fail real {} + {}= {}  minimum overall point fail {}.  \nValues {}.  \nDiffs {}",
                            firstPoint, pointFailPlayer * 2 * teamSize[team],
                            pointFail.ToString("0,0"), firstPointFail.ToString("0,0"),
                            failValues.ToCommaString(), failDiffs.Skip(1).ToCommaString());

                    }


                }
            }


            if (firstPointFail != long.MaxValue)
            {
                int team = firstPointFail % 2 == 0 ? 1 : 0;
                long num = (firstPointFail + team) / 2;
                string teamName = ((!switchTeams && team == 0) || (switchTeams && team == 1)) ? "RIGHT" : "LEFT";

                return teamName + " " + num;
            }


            return "DRAW";
        }

        

        public PongInput createInput(Scanner scanner)
        {
            PongInput input = new PongInput();
            input.heightField = scanner.nextBigInteger();
            input.widthField = scanner.nextBigInteger();

            input.numLeftTeam = scanner.nextBigInteger();
            input.numRightTeam = scanner.nextBigInteger();

            input.speedLeftTeam = scanner.nextBigInteger();
            input.speedRightTeam = scanner.nextBigInteger();

            input.Y = scanner.nextBigInteger();
            input.X = scanner.nextBigInteger();

            input.VY = scanner.nextBigInteger();
            input.VX = scanner.nextBigInteger();

            return input;
        }
    }

    public class PongInput
    {
        internal BigInteger heightField { get; set; }
        internal BigInteger widthField { get; set; }

        internal BigInteger numLeftTeam; //{ get; set; }
        internal BigInteger numRightTeam; //{ get; set; }

        internal BigInteger speedLeftTeam; //{ get; set; }
        internal BigInteger speedRightTeam; // { get; set; }

        internal BigInteger Y { get; set; }
        internal BigInteger X { get; set; }
        internal BigInteger VY { get; set; }
        internal BigInteger VX { get; set; }




    }
}
