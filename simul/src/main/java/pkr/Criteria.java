package pkr;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pkr.Simulator.iCriteria;
import pkr.possTree.PossibilityNode.HandCategory;
import pkr.possTree.PossibilityNode.TextureCategory;
import pkr.possTree.PossibilityNode.WinningLosingCategory;

import com.google.common.collect.Lists;

public class Criteria implements iCriteria
{
    protected static Logger log = LoggerFactory.getLogger(Simulator.class);
    protected static Logger logOutput = LoggerFactory.getLogger("mainOutput");
    
    public static void addUnpairedBoardCriteria(int round, String roundStr, List<Criteria> unPairedBoardCriteres)
    {
        Criteria highCardOnly  = new Criteria(round, roundStr + " nothing on unpaired board");
        highCardOnly.allMustMatch = true;
        highCardOnly.matchHandCat.add(HandCategory.HIGH_CARD);
                
        //unPairedBoardCriteres.add(highCardOnly);
        
        Criteria reallyNothing = new Criteria(round, roundStr + " no pairs no draws on unpaired board");
        reallyNothing.allMustMatch = true;
        reallyNothing.matchHandCat.add(HandCategory.HIGH_CARD);
        reallyNothing.matchNegHandCat.add(HandCategory.FLUSH_DRAW);
        reallyNothing.matchNegHandCat.add(HandCategory.STRAIGHT_DRAW_2);
        unPairedBoardCriteres.add(reallyNothing);
        
        Criteria meTwoPairFlop = new HeroCriteria(round, roundStr + " 2 pair");
        meTwoPairFlop.matchHandCat.add(HandCategory.TWO_PAIR_USING_BOTH);
                
        Criteria anyTwoPairFlop = new Criteria(round, roundStr +" Anyone 2 pair on unpaired board");
        anyTwoPairFlop.matchHandCat.add( HandCategory.TWO_PAIR_USING_BOTH );
        
      //  unPairedBoardCriteres.add(meTwoPairFlop);
      //  unPairedBoardCriteres.add(anyTwoPairFlop);
        
        
        Criteria any0PairCrit = new Criteria(round, roundStr + " Anyone top/over pair");
        
        any0PairCrit.matchHandCat.add( HandCategory.PAIR_OVERCARDS_0);
        
        Criteria any1PairCrit = new Criteria(round, roundStr + " Anyone pair 1 overcard");
        
        any1PairCrit.matchHandCat.add( HandCategory.PAIR_OVERCARDS_0);
        any1PairCrit.matchHandCat.add( HandCategory.PAIR_OVERCARDS_1);
        
        Criteria any2PairCrit = new Criteria(round, roundStr +" Anyone pair 2 overcards");
        
        any2PairCrit.matchHandCat.add( HandCategory.PAIR_OVERCARDS_0);
        any2PairCrit.matchHandCat.add( HandCategory.PAIR_OVERCARDS_1);
        any2PairCrit.matchHandCat.add( HandCategory.PAIR_OVERCARDS_2);
        
        Criteria any3PairCrit = new Criteria(round, roundStr + " Anyone pair 3 overcards");
        
        any3PairCrit.matchHandCat.add( HandCategory.PAIR_OVERCARDS_0);
        any3PairCrit.matchHandCat.add( HandCategory.PAIR_OVERCARDS_1);
        any3PairCrit.matchHandCat.add( HandCategory.PAIR_OVERCARDS_2);
        any3PairCrit.matchHandCat.add( HandCategory.PAIR_OVERCARDS_3);
        
        /*
        unPairedBoardCriteres.add(any0PairCrit);
        unPairedBoardCriteres.add(any1PairCrit);
        unPairedBoardCriteres.add(any2PairCrit);
        unPairedBoardCriteres.add(any3PairCrit);
        */
        WinningWithCriteria w1 = new WinningWithCriteria(round, roundStr + " winning with high card");
        w1.matchHandCat.add( HandCategory.HIGH_CARD);
        unPairedBoardCriteres.add(w1);
        
        w1 = new WinningWithCriteria(round, roundStr + " winning with any pair");
        w1.matchHandCat.add( HandCategory.PAIR_OVERCARDS_0);
        w1.matchHandCat.add( HandCategory.PAIR_OVERCARDS_1);
        w1.matchHandCat.add( HandCategory.PAIR_OVERCARDS_2);
        w1.matchHandCat.add( HandCategory.PAIR_OVERCARDS_3);
        //unPairedBoardCriteres.add(w1);
        
       
        
        w1 = new WinningWithCriteria(round, roundStr + " winning with pair 3 overcards");
        w1.matchHandCat.add( HandCategory.PAIR_OVERCARDS_3);
        w1.matchHandCat.add( HandCategory.HIGH_CARD);
        unPairedBoardCriteres.add(w1);
        
        w1 = new WinningWithCriteria(round, roundStr + " winning with pair 2 overcards");
        w1.matchHandCat.add( HandCategory.PAIR_OVERCARDS_2);
        w1.matchHandCat.add( HandCategory.PAIR_OVERCARDS_3);
        w1.matchHandCat.add( HandCategory.HIGH_CARD);
        unPairedBoardCriteres.add(w1);
        
        w1 = new WinningWithCriteria(round, roundStr + " winning with pair 1 overcards");
        w1.matchHandCat.add( HandCategory.PAIR_OVERCARDS_1);
        w1.matchHandCat.add( HandCategory.PAIR_OVERCARDS_2);
        w1.matchHandCat.add( HandCategory.PAIR_OVERCARDS_3);
        w1.matchHandCat.add( HandCategory.HIGH_CARD);
        unPairedBoardCriteres.add(w1);
        
        w1 = new WinningWithCriteria(round, roundStr + " winning with pair 0 overcards or worse");
        w1.matchHandCat.add( HandCategory.PAIR_OVERCARDS_0);
        w1.matchHandCat.add( HandCategory.PAIR_OVERCARDS_1);
        w1.matchHandCat.add( HandCategory.PAIR_OVERCARDS_2);
        w1.matchHandCat.add( HandCategory.PAIR_OVERCARDS_3);
        w1.matchHandCat.add( HandCategory.HIGH_CARD);
        unPairedBoardCriteres.add(w1);
        
        w1 = new WinningWithCriteria(round, roundStr + " winning with 2 pair or better");
        w1.matchHandCat.add( HandCategory.TWO_PAIR_USING_BOTH);
        w1.matchHandCat.add( HandCategory.SET_USING_BOTH);
        w1.matchHandCat.add( HandCategory.FLUSH);
        w1.matchHandCat.add( HandCategory.STRAIGHT);
        w1.matchHandCat.add( HandCategory.FULL_HOUSE);
        w1.matchHandCat.add( HandCategory.QUADS);
        w1.matchHandCat.add( HandCategory.STRAIGHT_FLUSH);
//        unPairedBoardCriteres.add(w1);
        
        w1 = new WinningWithCriteria(round, roundStr + " winning with two pair");
        w1.matchHandCat.add( HandCategory.TWO_PAIR_USING_BOTH);
        w1.matchHandCat.add( HandCategory.PAIR_OVERCARDS_0);
        w1.matchHandCat.add( HandCategory.PAIR_OVERCARDS_1);
        w1.matchHandCat.add( HandCategory.PAIR_OVERCARDS_2);
        w1.matchHandCat.add( HandCategory.PAIR_OVERCARDS_3);
        w1.matchHandCat.add( HandCategory.HIGH_CARD);
        unPairedBoardCriteres.add(w1);
        
        w1 = new WinningWithCriteria(round, roundStr + " winning with trips");
        w1.matchHandCat.add( HandCategory.SET_USING_BOTH);
        w1.matchHandCat.add( HandCategory.TWO_PAIR_USING_BOTH);
        w1.matchHandCat.add( HandCategory.PAIR_OVERCARDS_0);
        w1.matchHandCat.add( HandCategory.PAIR_OVERCARDS_1);
        w1.matchHandCat.add( HandCategory.PAIR_OVERCARDS_2);
        w1.matchHandCat.add( HandCategory.PAIR_OVERCARDS_3);
        w1.matchHandCat.add( HandCategory.HIGH_CARD);
        unPairedBoardCriteres.add(w1);
        
        w1 = new WinningWithCriteria(round, roundStr + " winning with straight");
        w1.matchHandCat.add( HandCategory.STRAIGHT);
        w1.matchHandCat.add( HandCategory.SET_USING_BOTH);
        w1.matchHandCat.add( HandCategory.TWO_PAIR_USING_BOTH);
        w1.matchHandCat.add( HandCategory.PAIR_OVERCARDS_0);
        w1.matchHandCat.add( HandCategory.PAIR_OVERCARDS_1);
        w1.matchHandCat.add( HandCategory.PAIR_OVERCARDS_2);
        w1.matchHandCat.add( HandCategory.PAIR_OVERCARDS_3);
        w1.matchHandCat.add( HandCategory.HIGH_CARD);
        unPairedBoardCriteres.add(w1);
        
        w1 = new WinningWithCriteria(round, roundStr + " winning with flush");
        w1.matchHandCat.add( HandCategory.FLUSH);
        w1.matchHandCat.add( HandCategory.STRAIGHT);
        w1.matchHandCat.add( HandCategory.SET_USING_BOTH);
        w1.matchHandCat.add( HandCategory.TWO_PAIR_USING_BOTH);
        w1.matchHandCat.add( HandCategory.PAIR_OVERCARDS_0);
        w1.matchHandCat.add( HandCategory.PAIR_OVERCARDS_1);
        w1.matchHandCat.add( HandCategory.PAIR_OVERCARDS_2);
        w1.matchHandCat.add( HandCategory.PAIR_OVERCARDS_3);
        w1.matchHandCat.add( HandCategory.HIGH_CARD);
        unPairedBoardCriteres.add(w1);
        
        
       // unPairedBoardCriteres.clear();
        
        w1 = new WinningWithCriteria(round, roundStr + " winning with quads");
        w1.matchHandCat.add( HandCategory.QUADS);
        w1.matchHandCat.add( HandCategory.FLUSH);
        w1.matchHandCat.add( HandCategory.STRAIGHT);
        w1.matchHandCat.add( HandCategory.SET_USING_BOTH);
        w1.matchHandCat.add( HandCategory.TWO_PAIR_USING_BOTH);
        w1.matchHandCat.add( HandCategory.PAIR_OVERCARDS_0);
        w1.matchHandCat.add( HandCategory.PAIR_OVERCARDS_1);
        w1.matchHandCat.add( HandCategory.PAIR_OVERCARDS_2);
        w1.matchHandCat.add( HandCategory.PAIR_OVERCARDS_3);
        w1.matchHandCat.add( HandCategory.HIGH_CARD);
        unPairedBoardCriteres.add(w1);
        
        w1 = new WinningWithCriteria(round, roundStr + " winning ");
        w1.matchHandCat.add( HandCategory.STRAIGHT_FLUSH);
        w1.matchHandCat.add( HandCategory.QUADS);
        w1.matchHandCat.add( HandCategory.FULL_HOUSE);
        w1.matchHandCat.add( HandCategory.FLUSH);
        w1.matchHandCat.add( HandCategory.STRAIGHT);
        w1.matchHandCat.add( HandCategory.SET_USING_BOTH);
        w1.matchHandCat.add( HandCategory.TWO_PAIR_USING_BOTH);
        w1.matchHandCat.add( HandCategory.PAIR_OVERCARDS_0);
        w1.matchHandCat.add( HandCategory.PAIR_OVERCARDS_1);
        w1.matchHandCat.add( HandCategory.PAIR_OVERCARDS_2);
        w1.matchHandCat.add( HandCategory.PAIR_OVERCARDS_3);
        w1.matchHandCat.add( HandCategory.HIGH_CARD);
       // unPairedBoardCriteres.add(w1);
        
        for(Criteria crit : unPairedBoardCriteres)
        {
            crit.mustHave.add(TextureCategory.UNPAIRED_BOARD);
            //crit.mustNotHave.add(TextureCategory.STRAIGHT_POSSIBLE);
            crit.mustNotHave.add(TextureCategory.SAME_SUIT_5);
            crit.mustNotHave.add(TextureCategory.SAME_SUIT_4);
        }
    }
    
    public static void addPairedBoardCriteria(int round, String roundStr,
            List<Criteria> pairedBoardCriteres)
    {
        Criteria anythingOnPairedFlop = new Criteria(round, roundStr + " Anyone on paired board");
        anythingOnPairedFlop.mustHave.add(TextureCategory.PAIRED_BOARD);
        anythingOnPairedFlop.matchHandCat.add( HandCategory.TWO_PAIR_USING_ONE );
        anythingOnPairedFlop.matchHandCat.add( HandCategory.SET_USING_ONE );
        anythingOnPairedFlop.matchHandCat.add( HandCategory.FULL_HOUSE );
        anythingOnPairedFlop.matchHandCat.add( HandCategory.QUADS );
        
        Criteria tripsOnPairedFlop = new Criteria(round, roundStr + " Anyone trips on paired board");
        tripsOnPairedFlop.mustHave.add(TextureCategory.PAIRED_BOARD);            
        tripsOnPairedFlop.matchHandCat.add( HandCategory.SET_USING_ONE );
        
        Criteria twoPairOnPairedFlop = new Criteria(round, roundStr + " 2 pair on paired board");
        twoPairOnPairedFlop.mustHave.add(TextureCategory.PAIRED_BOARD);            
        twoPairOnPairedFlop.matchHandCat.add( HandCategory.TWO_PAIR_USING_ONE );
        
        Criteria nothingOnPairedFlop = new Criteria(round, roundStr + " nothing on paired board");
        nothingOnPairedFlop.mustHave.add(TextureCategory.PAIRED_BOARD);            
        nothingOnPairedFlop.matchHandCat.add( HandCategory.PAIR_ON_PAIRED_BOARD );
        nothingOnPairedFlop.allMustMatch = true;
        
        pairedBoardCriteres.add(anythingOnPairedFlop);
        pairedBoardCriteres.add(tripsOnPairedFlop);
        pairedBoardCriteres.add(twoPairOnPairedFlop);
        pairedBoardCriteres.add(nothingOnPairedFlop);
        
    }
    
    public static void addAnyBoardCriteria(int round, String roundStr, List<Criteria> allBoardCriteres)
    {
        Criteria flushDrawCrit = new Criteria(round, roundStr + " flush draw");
        //nothingOnPairedFlop.mustHave.add(TextureCategory.PAIRED_BOARD);            
        flushDrawCrit.matchHandCat.add( HandCategory.FLUSH_DRAW );
        
        Criteria straightDrawCrit = new Criteria(round, roundStr + " straight draw (inc gutshots)");
        //nothingOnPairedFlop.mustHave.add(TextureCategory.PAIRED_BOARD);            
        straightDrawCrit.matchHandCat.add( HandCategory.STRAIGHT_DRAW_2 );
        straightDrawCrit.matchHandCat.add( HandCategory.STRAIGHT_DRAW_1 );

        Criteria straight2DrawCrit = new Criteria(round, roundStr + " straight draw");
        //nothingOnPairedFlop.mustHave.add(TextureCategory.PAIRED_BOARD);            
        straight2DrawCrit.matchHandCat.add( HandCategory.STRAIGHT_DRAW_2 );
        

        allBoardCriteres.add(flushDrawCrit);
        allBoardCriteres.add(straightDrawCrit);
        allBoardCriteres.add(straight2DrawCrit);
        

            
            
       
        
        Criteria tripsCrit = new Criteria(round, roundStr + " Anyone Trips ");

        tripsCrit.matchHandCat.add( HandCategory.SET_USING_BOTH );
        tripsCrit.matchHandCat.add( HandCategory.SET_USING_ONE );
        
        allBoardCriteres.add(tripsCrit);
        
        Criteria straightCrit = new Criteria(round, roundStr + " Anyone Straight");

        straightCrit.matchHandCat.add( HandCategory.STRAIGHT );
        
        allBoardCriteres.add(straightCrit);
        
        Criteria flushCrit = new Criteria(round, roundStr + " Anyone Flush");

        flushCrit.matchHandCat.add( HandCategory.FLUSH );
        
        allBoardCriteres.add(flushCrit);
        
        Criteria fullHouseOrBetter = new Criteria(round, roundStr + " Anyone Full house or better");

        fullHouseOrBetter.matchHandCat.add( HandCategory.FULL_HOUSE );
        fullHouseOrBetter.matchHandCat.add( HandCategory.QUADS );
        fullHouseOrBetter.matchHandCat.add( HandCategory.STRAIGHT_FLUSH);
        
        allBoardCriteres.add(fullHouseOrBetter);
        
        
        HeroCriteria winWithFlush = new HeroCriteria(round,  roundStr + " me win with flush");
        winWithFlush.matchHandCat.add(HandCategory.FLUSH);
        winWithFlush.winLoseCat.add(WinningLosingCategory.WINNING);
        
        HeroCriteria loseWithFlush = new HeroCriteria(round,  roundStr + " me lose with flush");
        loseWithFlush.matchHandCat.add(HandCategory.FLUSH);
        loseWithFlush.winLoseCat.add(WinningLosingCategory.LOSING);
        loseWithFlush.winLoseCat.add(WinningLosingCategory.SECOND_BEST_HAND);
        
        allBoardCriteres.add(winWithFlush);
        allBoardCriteres.add(loseWithFlush);
    }
    
    int round;
    String desc;
    boolean allMustMatch;

    //Any match will do
    List<HandCategory> matchHandCat;
    List<HandCategory> matchNegHandCat;


    //For applicable
    List<TextureCategory> mustHave;
    List<TextureCategory> mustNotHave;

    int applicableCount = 0;
    int matches = 0;

    Criteria(int round, String desc) {
        super();
        this.round = round;
        this.desc = desc;

        mustHave = Lists.newArrayList();
        mustNotHave = Lists.newArrayList();

        matchHandCat = Lists.newArrayList();
        matchNegHandCat = Lists.newArrayList();
    }

    public void printMsg()
    {
        if (applicableCount == 0)
        {
            logOutput.debug("{}.  No applicable cases", desc);
            return;
        }
        logOutput.debug("{}.  {} / {} =  %{} ", desc, matches, applicableCount, 100.0 * matches / applicableCount);
    }

    /* (non-Javadoc)
     * @see pkr.Simulator.iCriteria#matches(pkr.CompleteEvaluation[])
     */
    @Override
    public boolean matches(CompleteEvaluation[] evals)
    {
        if (allMustMatch)
        {
            for (CompleteEvaluation eval : evals)
            {
                boolean ok = false;
                for (HandCategory cat : matchHandCat)
                {
                    if (eval.hasFlag(round, cat))
                    {
                        ok = true;
                        break;
                    }
                }

                if (!ok)
                    return false;

                for (HandCategory cat : matchNegHandCat)
                {
                    if (eval.hasFlag(round, cat))
                    {
                        return false;
                    }
                }

            }

            return true;
        } else
        {
            for (CompleteEvaluation eval : evals)
            {
                for (HandCategory cat : matchHandCat)
                {
                    if (eval.hasFlag(round, cat))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void calculate(CompleteEvaluation[] evals)
    {
        if (!isApplicable(evals[0]))
            return;

        ++applicableCount;

        if (matches(evals))
        {
            ++matches;
        }
    }

    /* (non-Javadoc)
     * @see pkr.Simulator.iCriteria#isApplicable(pkr.CompleteEvaluation)
     */
    @Override
    public boolean isApplicable(CompleteEvaluation eval)
    {
        for (TextureCategory cat : mustHave)
        {
            if (!eval.hasFlag(round, cat))
                return false;
        }

        for (TextureCategory cat : mustNotHave)
        {
            if (eval.hasFlag(round, cat))
                return false;
        }
        return true;
    }
}
