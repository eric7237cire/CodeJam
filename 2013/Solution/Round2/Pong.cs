#define LOGGING
#define LOGGING_DEBUG
#define LOGGING_INFO
#define LOGGING_TRACE
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

namespace Round2.Pong
{
#if FRAC
    using Line = LineNumeric<BigFraction>;
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
        public static long solve_min(long b, long m, long s, long t)
        {
            //assert 0 <= s and s <= t and t < m
            b %= m;
            if (b == 0 || s == 0)
            {
                return 0;
            }
            else if (b * 2 > m)
            {
                return solve_min(m - b, m, m - t, m - s);
            }


            //Can't necessarily set y = ceil(floor(a*s/m) * m/a), because it might
            // be less than s. So treat y = s as a special case
            long best = (b * s) % m;

            long zs = b * s / m + 1;
            long zt = b * t / m;
            if (zs <= zt)
                best = Math.Min(best, solve_min(-m, b, zs, zt));
            return best;
        }

        /*
# Finds smallest non-negative x such that mod(a * x, m) lies in the range [s, t],
# or -1 if there are no solutions.
# Preconditions:
# - 0 <= a
# - 0 <= s <= t < m.
#*/
        public static long first_hit(long a, long m, long s, long t)
        {
            Logger.LogTrace("first_hit a {} m {} s {} t {}", a, m, s, t);
            Preconditions.checkState(0 <= s);
            Preconditions.checkState(s <= t);
            Preconditions.checkState(t < m);
            //assert 0 <= s and s <= t and t < m
            //   # Remove any common factors
            a %= m;
            long g = ModdedLong.gcd_recursive(a, m);
            a = a / g;
            m = m / g;
            s = (s + g - 1) / g;
            t = t / g;

            if (s == 0)
                return 0;
            else if (a == 0 || s > t)
                return -1;
            else
            {
                long b = ModdedLong.mod_inverse_recursive(a, m);
                //# ax mod m = y <=> x mod m = by
                return solve_min(b, m, s, t);
            }
        }

        public static long ericFirstHit(long deltaP, long modulus, long a, long b)
        {
            Logger.LogTrace("first hit deltaP {} modulus {}  a {} b {}", deltaP, modulus, a, b);
            Preconditions.checkState(a <= b);

            //We want to be able to have at least 2 points within the modulus
            if (2 * deltaP > modulus)
            {
                //Flipping the problem
                return ericFirstHit(modulus - deltaP, modulus, modulus - b, modulus - a);
            }

            long bestIndex = (a+deltaP-1) / deltaP;
            long best = deltaP * bestIndex;
            Logger.LogTrace("bestIndex {} best {}", bestIndex, best);
            if (a <= best && best <= b)
            {
                Logger.LogTrace("Returning {}", bestIndex);
                return bestIndex;
            }

            //Calculate first wrap around paint
            long firstWrapAroundIndex = (modulus + 1) / deltaP;
            long firstWrapAroundPoint = (firstWrapAroundIndex * deltaP) % modulus;

            Logger.LogTrace("wrap around index {} point {}", firstWrapAroundIndex, firstWrapAroundPoint);

            //This wrap around point (P in the explanation) is a step value, we want to create
            //a sub problem

            //First, we need to know what starting points would have meant a hit in the interval
            long newA = a % deltaP;
            long newB = b % deltaP;
            Logger.ChangeIndent(4);
            long pointsToGoodStartingPoint = ericFirstHit(firstWrapAroundPoint, deltaP, newA, newB);
            Logger.ChangeIndent(-4);

            long ret = firstWrapAroundIndex*pointsToGoodStartingPoint + a / deltaP;
            Logger.LogTrace("Returning {} + {} = {}", pointsToGoodStartingPoint, a / deltaP, pointsToGoodStartingPoint + a / deltaP);
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
        /*
                public static BigInteger calcToTargetUsingOffset(NumType p0, NumType deltaP, BigInteger height, NumType targetDiff)
                {
                    Logger.LogTrace("\ncalcToTargetUsingOffset \u0394p {} * N  height: {}  target: {} ",
                        deltaP, height, targetDiff);
                    p0 = 3;
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
                        return -1;
                    }


                    NumType offSetGroupSize = height - maxDiff;

                    //Initial group of target = 1
            
                     //offset [1..] [1+ogs...] [1+2*ogs...]
             
                    BigInteger offsetGroupIndex = BigInteger.Divide((BigInteger)p0 - 1, (BigInteger)offSetGroupSize);
            
                    //Adjust
                    BigInteger adjusted = offsetGroupIndex - (maxDiff - targetDiff).floor();

                    Logger.LogTrace("Offset group size {}.  Index = ({} - 1) / {} = {}.  Adjusting for max diff {}, target diff {} = {}\n ",
                        offSetGroupSize, p0, offSetGroupSize, offsetGroupIndex, 
                        maxDiff, targetDiff,
                        adjusted);

                    if (adjusted < BigInteger.One)
                        adjusted = BigInteger.One;

                    return adjusted;

                }*/

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

            if (calculatedDeltaD.Equals(0))
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

                        Logger.LogInfo("{} + n {} * {} = {} = {} : {} [diff {}] with respect to height {}",
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
                CjUtils.swap(ref input.numRightTeam, ref input.numLeftTeam);
                CjUtils.swap(ref input.speedLeftTeam, ref input.speedRightTeam);
                switchTeams = true;
                input.X = input.widthField - input.X;
            }

            Line teamLeft = Line.createFromCoords(0, 0, 0, 1);
            Line teamRight = Line.createFromCoords(input.widthField, 0, input.widthField, 1);

            Line initialVector = Line.createFromCoords(input.X, input.Y, input.X + input.VX, input.Y + input.VY);

            var p1 = initialVector.intersection(teamRight);

            Line rebound = Line.createFromPoints(p1, p1.Add(new Point(-input.VX, input.VY)));

            var p2 = rebound.intersection(teamLeft);
            Logger.LogInfo("Initial point {}, {} direction {}, {}", input.X, input.Y, input.VX, input.VY);
            Logger.LogInfo("First intersection {} 2nd {}", p1, p2);

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

            //  List<Tuple<NumType, NumType>>[] teamPlayerPos = new List<Tuple<NumType,NumType>>[2]; //time, loc pair
            //teamPlayerPos[0] = new List<Tuple<NumType, NumType>>(); //left 
            // teamPlayerPos[1] = new List<Tuple<NumType, NumType>>(); //right
            BigInteger[] teamSize = new BigInteger[] { input.numLeftTeam, input.numRightTeam };
            int[] curPlayer = new int[2] { 0, 0 };
            BigInteger[] teamSpeed = new BigInteger[2] { input.speedLeftTeam, input.speedRightTeam };

            Logger.LogInfo("Width {} Height {}", input.widthField, input.heightField);
            Logger.LogInfo("Left team {} players at {} speed", teamSize[0], teamSpeed[0]);
            Logger.LogInfo("Right team {} players at {} speed", teamSize[1], teamSpeed[1]);
            Logger.LogInfo("Time = {} + {} * n.  Position = {} + {} * n", t0, t1, p1.Y, yDif);

            BigInteger firstPointFail = long.MaxValue;

            // long firstPointFail0 = long.MaxValue;

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

                NumType s = 139700 + 2*input.heightField  - startingPoint % (2*input.heightField);
                s %= (2 * input.heightField);
                NumType t = s + 100 - 1;

                long fp = first_hit( a:(int) (yDif * 2), 
                    m: (int) (2 * input.heightField), 
                    s: (int) (s),
                    t: (int) ( t));

                //139745

                Logger.LogInfo("First point {}.  Starting point {}", fp, startingPoint % (2 * input.heightField));

                for (int pIdx = 0; pIdx <= 50; ++pIdx)
                {
                    NumType pi = ( yDif * 2 * pIdx) % (2 * input.heightField);


                    Logger.LogTrace("offsetted calculate idx {} pi%2h: [{}] pi%h: {} pi: {} upper: {} upper - pi%h: {}",
                            pIdx, pi, pi % input.heightField, calcAdd(0, yDif*2, pIdx, input.heightField), upper, upper - pi % input.heightField);
                }


                for (int pIdx = 0; pIdx <= 50; ++pIdx)
                {
                    BigInteger realIndex = 1 - team + 2 * (pIdx);
                    NumType pi = (p1.Y + yDif * realIndex) % (2 * input.heightField);


                    Logger.LogTrace("debug calculate team point {} idx {} pi%2h: [{}] pi%h: {} pi: {} upper: {} upper - pi%h: {}",
                            pIdx, realIndex, pi, pi % input.heightField, calcAdd(p1.Y, yDif, realIndex, input.heightField), upper, upper - pi % input.heightField);
                }

                for (int j = 0; j < 10 && !foundFirstPointFail; ++j)
                {
                    startingPoint = p1.Y + yDif * (1 - team + 2 * jump);

                    BigInteger nextJump = accel(startingPoint,
                        yDif * 2, 2 * input.heightField, cycleLength, cycleDiff, lower, upper);

                    jump += nextJump;
                    Logger.LogTrace("jump {} nextJump {}", jump, nextJump);




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

                            Logger.LogInfo("testForbiddenIntervalHelper pi [{}] pj [{}] diff [{}] target {} lower {} upper {}",
                            pi, pj, diffBet, disPossible, lower, upper);

                            Logger.LogInfo("Found point realIndex: {} next after realIndex (actual failure point) {}", realIndex, realIndex + 2 * teamSize[team]);
                            firstPointFail = BigInteger.Min(firstPointFail, realIndex + 2 * teamSize[team]);
                            foundFirstPointFail = true;
                            break;
                        }

                    }

                    jump += cycleLength;

                }
            }

            /*
                        for(int team = 0; team < 2; ++team)
                        {
                            NumType deltaT = 2 * teamSize[team] * t1;
                            NumType disPossible = deltaT * teamSpeed[team];

                            NumType diff = yDif.Multiply(2 * teamSize[team]);

                            for(int playerNum = 0; playerNum < teamSize[team]; ++playerNum)
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
            
            
            
                        Logger.LogInfo("\n\nChecking {} == {}\n\n", firstPointFail0, firstPointFail);
                       // Preconditions.checkState(firstPointFail0 == firstPointFail);
            */
            if (firstPointFail != long.MaxValue)
            {
                int team = firstPointFail % 2 == 0 ? 1 : 0;
                BigInteger num = (firstPointFail + team) / 2;
                string teamName = ((!switchTeams && team == 0) || (switchTeams && team == 1)) ? "RIGHT" : "LEFT";
                Logger.LogInfo(teamName + " " + num);
                return teamName + " " + num;
            }


            return "DRAW";
        }

        public string processInputOld(PongInput input)
        {
            //ShowOutput();
            //return "hoetnuh";
            // processInputManual(input);

            bool switchTeams = false;

            if (input.VX < 0)
            {
                input.VX = -input.VX;
                CjUtils.swap(ref input.numRightTeam, ref input.numLeftTeam);
                CjUtils.swap(ref input.speedLeftTeam, ref input.speedRightTeam);
                switchTeams = true;
                input.X = input.widthField - input.X;
            }

            Line teamLeft = Line.createFromCoords(0, 0, 0, 1);
            Line teamRight = Line.createFromCoords(input.widthField, 0, input.widthField, 1);

            Line initialVector = Line.createFromCoords(input.X, input.Y, input.X + input.VX, input.Y + input.VY);

            var p1 = initialVector.intersection(teamRight);



            Line rebound = Line.createFromPoints(p1, p1.Add(new Point(-input.VX, input.VY)));

            var p2 = rebound.intersection(teamLeft);
            Logger.LogInfo("Initial point {}, {} direction {}, {}", input.X, input.Y, input.VX, input.VY);
            Logger.LogInfo("First intersection {} 2nd {}", p1, p2);

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
            int[] curPlayer = new int[2] { 0, 0 };
            long[] teamSpeed = new long[2] { (long)input.speedLeftTeam, (long)input.speedRightTeam };

            Logger.LogInfo("Width {} Height {1}", input.widthField, input.heightField);
            Logger.LogInfo("Left team {} players at {} speed", teamSize[0], teamSpeed[0]);
            Logger.LogInfo("Right team {} players at {} speed", teamSize[1], teamSpeed[1]);
            Logger.LogInfo("Time = {} + {} * n.  Position = {} + {} * n", t0, t1, p1.Y, yDif);

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

        public string processInputManual(PongInput input)
        {
            bool switchTeams = false;

            if (input.VX < 0)
            {
                input.VX = -input.VX;
                CjUtils.swap(ref input.numRightTeam, ref input.numLeftTeam);
                CjUtils.swap(ref input.speedLeftTeam, ref input.speedRightTeam);
                switchTeams = true;
                input.X = input.widthField - input.X;
            }

            Line teamLeft = Line.createFromCoords(0, 0, 0, 1);
            Line teamRight = Line.createFromCoords(input.widthField, 0, input.widthField, 1);

            Line initialVector = Line.createFromCoords(input.X, input.Y, input.X + input.VX, input.Y + input.VY);

            var p1 = initialVector.intersection(teamRight);



            Line rebound = Line.createFromPoints(p1, p1.Add(new Point(-input.VX, input.VY)));

            var p2 = rebound.intersection(teamLeft);
            Logger.Log("Initial point {0}, {1} direction {2}, {3}", input.X, input.Y, input.VX, input.VY);
            Logger.Log("P1 {0} P2 {1}", p1, p2);

            NumType yDif = p2.Y.Subtract(p1.Y);
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
            int[] curPlayer = new int[2] { 0, 0 };
            long[] teamSpeed = new long[2] { (long)input.speedLeftTeam, (long)input.speedRightTeam };


            Logger.LogInfo("Width {} Height {1}", input.widthField, input.heightField);
            Logger.LogInfo("Left team {0} players at {1} speed", teamSize[0], teamSpeed[0]);
            Logger.LogInfo("Right team {0} players at {1} speed", teamSize[1], teamSpeed[1]);
            Logger.LogInfo("Time = {} + {} * n.  Position = {2} + {3} * n", t0, t1, p1.Y, yDif);
            //HashSet<int>[] playerZeroPos = new HashSet<int>[] { new HashSet<int>(), new HashSet<int>() };

            Func<int, NumType> getYPosFromPointNum = (pNum) =>
            {
                NumType y = p1.Y.Add(yDif.Multiply(pNum));
                double x = pNum % 2 != 0 ? 0 : (long)input.widthField;

                long rem = (long)(y.Divide(input.heightField));

                y = y.Subtract(rem * input.heightField);

                if (rem % 2 == 1)
                {
                    y = ((NumType)input.heightField).Subtract(y);
                }

                return y;
            };


            const int maxPnum = 400000;
            for (int pNum = 0; pNum < maxPnum; ++pNum)
            {
                int team = pNum % 2 == 0 ? 1 : 0;

                NumType y = getYPosFromPointNum(pNum);

                NumType time = t0.Add(t1.Multiply(pNum));

                if (curPlayer[team] == 166)
                    Logger.LogTrace("Side {} Point {} is {}.  Time {}.  Current player {}", team, pNum, y, time, curPlayer[team]);

                if (teamPlayerPos[team].Count < teamSize[team])
                {
                    teamPlayerPos[team].Add(new Tuple<NumType, NumType>(time, y));
                }
                else
                {
                    //Check last position
                    var lastTimePos = teamPlayerPos[team][curPlayer[team]];
                    NumType deltaT = time.Subtract(lastTimePos.Item1);
                    NumType deltaDis = lastTimePos.Item2.Subtract(y).Abs();

                    NumType disPossible = deltaT.Multiply(teamSpeed[team]);
                    if (disPossible.CompareTo(deltaDis) < 0)
                    {
                        int num = (pNum + team) / 2;
                        string teamName = ((!switchTeams && team == 0) || (switchTeams && team == 1)) ? "RIGHT" : "LEFT";
                        Logger.LogDebug("Loser lastTime: {}   pos: {}.  Delta dis {}, could move {}.  player {} team {}",
                            lastTimePos.Item1, lastTimePos.Item2,
                            deltaDis, disPossible, curPlayer[team], team);
                        return teamName + " " + num;
                    }
                    else
                    {
                        /* if (curPlayer[team] < 10)
                             Logger.LogDebug("lastTime {0} sec  pos:{1}.  Delta dis {2}, could move {3} team {4} curPlayer {5} ", lastTimePos.Item1, lastTimePos.Item2,
                                 deltaDis, disPossible, team, curPlayer[team]);*/
                    }

                    teamPlayerPos[team][curPlayer[team]] = new Tuple<NumType, NumType>(time, y);
                }

                if (curPlayer[team] == 0)
                {
                    //if (playerZeroPos[team].Contains(y))
                    {
                        //return "DRAW";
                    }
                }

                curPlayer[team]++;
                curPlayer[team] %= teamSize[team];
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
