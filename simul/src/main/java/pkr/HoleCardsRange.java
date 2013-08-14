package pkr;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pkr.history.FlopTurnRiverState;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/*
 * 
 * XX -- 6 / 1326
 * XYs -- 4 / 1326
 * XYo -- 12 / 1326
 * XY -- 16 / 1326
 * 
 * 13 * 6 + 169-13 / 2 * 16   1248
 */

public class HoleCardsRange {
    
  
    
    
    private static Logger log = LoggerFactory.getLogger(HoleCardsRange.class);
    
    
    private String rangeStr;
    private Set<HoleCards> cards;
    private List<HoleCards> cardsList;
    double[] relativeFreq;
    
    int totalHands = 0;
    int[] rankFreq;
    
  //AKo 8Ts 99  len 2 / 3
    //or AsKh 9s9h len 4
    public HoleCardsRange(String rangeStr) {
        
        this.rangeStr = rangeStr;
        
        cards = Sets.newHashSet();
        cardsList = Lists.newArrayList();
        
        rankFreq = new int[13];
        
        String[] ranges = rangeStr.split("[, \\s]+");
        
        for(String code : ranges) {
            //log.debug("Parse code [{}] of {}", code, rangeStr);
            addCode(code);
        }
        
        log.debug("HandRange {}\n has {} hands of {}.  %{}", 
                rangeStr,
                totalHands, 26*51,
                FlopTurnRiverState.df2.format(100.0*totalHands / (26*51)));
        StringBuffer buf = new StringBuffer();
        
        for(CardRank rank : CardRank.values())
        {
            buf.append(rank.name());
            buf.append(": ");
            buf.append(FlopTurnRiverState.df2.format(100.0*rankFreq[rank.getIndex()] / totalHands));
            buf.append("%");
            buf.append("  ");
            /*
            log.debug("Card Rank {} {} of {} is %{} ", rank.name(),
                    rankFreq[rank.getIndex()],
                    totalHands,
                    FlopTurnRiverState.df2.format(100.0*rankFreq[rank.getIndex()] / totalHands));
                    */
        }
        
        buf.append('\n');
        log.debug(buf.toString());
        
    }
    
    @Override
    public String toString()
    {
        return rangeStr;
    }

    private static String cardCode = "([2-9AKQJT][2-9AKQJT][so]?)";
    private static Pattern begStopPat = Pattern.compile(cardCode + "-" + cardCode, Pattern.CASE_INSENSITIVE);
    private static Pattern plusPat = Pattern.compile(cardCode + "\\+?", Pattern.CASE_INSENSITIVE);
    private static Pattern exact = Pattern.compile( "([2-9AKQJT][hscd][2-9AKQJT][hscd])");
    
    private static class SingleCode
    {
        CardRank rankGrand;
        CardRank rankPetit;
        
        boolean suited;
        boolean unsuited;
        private SingleCode(SingleCode other) {
            super();
            this.rankGrand = other.rankGrand;
            this.rankPetit = other.rankPetit;
            this.suited = other.suited;
            this.unsuited = other.unsuited;
        }
        
        private SingleCode() {
            super();            
        }
    }
    
    private SingleCode parseSingleCode(String code)
    {
        SingleCode ret = new SingleCode();
        ret.rankGrand = CardRank.fromChar(code.charAt(0));
        ret.rankPetit = CardRank.fromChar(code.charAt(1));
        
        if (ret.rankGrand.getIndex() < 
                ret.rankPetit.getIndex())
        {
            CardRank ex = ret.rankGrand;
            ret.rankGrand = ret.rankPetit;
            ret.rankPetit = ex;
        }
                
        Preconditions.checkState(ret.rankGrand.getIndex() >= 
                ret.rankPetit.getIndex(), code);
        
        if (code.length() > 2 && code.charAt(2) == 's')
        {
            Preconditions.checkState(ret.rankGrand != ret.rankPetit);
            ret.suited = true;
            ret.unsuited = false;
        } else if (code.length() > 2 && code.charAt(2) == 'o')
        {
            Preconditions.checkState(ret.rankGrand != ret.rankPetit);
            ret.suited = false;
            ret.unsuited = true;
        } else {
            ret.suited = ret.rankGrand != ret.rankPetit;
            ret.unsuited = true;
        }
        
        return ret;
    }
    
    private void addCode(String code) {
        List<HoleCards> ret = Lists.newArrayList();
        
        Preconditions.checkArgument(code.length() >= 2 && code.length() <= 7, code);
        
        if (exact.matcher(code).matches())
        {
            HoleCards hc = new HoleCards( Card.parseCard(code.substring(0,2)),
                    Card.parseCard(code.substring(2,4)));
            ret.add(hc);
            return;
        }
        
        Matcher plusMatch = plusPat.matcher(code);
        
        SingleCode start = null;
        SingleCode stop = null;
        
        if (plusMatch.matches())
        {
            start = parseSingleCode(plusMatch.group(1));
            stop = new SingleCode(start);
            
            boolean toEnd = code.charAt(code.length()-1) =='+';
            
            if (toEnd)
            {
                if (start.rankGrand == start.rankPetit)
                {
                    stop.rankPetit = CardRank.ACE;
                    stop.rankGrand = CardRank.ACE;
             
                } else {
                    stop.rankPetit = CardRank.getFromZeroBasedValue(start.rankGrand.getIndex() - 1);
                }
            }
        }
        
        Matcher begStopMatch = begStopPat.matcher(code); 
        
        if (begStopMatch.matches())
        {
            start = parseSingleCode(begStopMatch.group(1));
            stop = parseSingleCode(begStopMatch.group(2));
            
            //Vérifier que la fin est plus grande que le début
            if (start.rankPetit.getIndex() > stop.rankPetit.getIndex())
            {
                SingleCode tmp = stop;
                stop = start;
                start = tmp;
            }
        }
        
        Preconditions.checkNotNull(stop, code);
        
        
        CardRank rank1 = start.rankGrand;
        CardRank rank2 = start.rankPetit;
        
        //pairs
        if (rank1 == rank2) {
            
            CardRank toRank = stop.rankGrand;
            
            for(int rankIndex = rank1.getIndex(); rankIndex <= toRank.getIndex(); ++rankIndex) 
            {
                CardRank rank = CardRank.getFromZeroBasedValue(rankIndex);
                
                Suit suit1;
                Suit suit2;
                for(int i = 0; i < 4; ++i) 
                {
                    suit1 = Suit.fromIndex(i);
                    for(int j = i+1; j < 4; ++j) 
                    {
                        suit2 = Suit.fromIndex(j);
                        
                        ret.add( new HoleCards( Card.getCard(suit1, rank), 
                                Card.getCard(suit2, rank)));
                    }
                }
                   
            }
        } else {
            boolean suited = start.suited;
            boolean unSuited = start.unsuited;
            
            
            Preconditions.checkState(rank1.getIndex() > rank2.getIndex());
            
            int toRank2Index = stop.rankPetit.getIndex();
            
            for(int rankIndex = rank2.getIndex(); rankIndex <= toRank2Index; ++rankIndex)
            {
                CardRank rank = CardRank.getFromZeroBasedValue(rankIndex);
                Preconditions.checkNotNull(rank, code + "r index " + rankIndex);
                
                Suit suit1;
                Suit suit2;
                for(int i = 0; i < 4; ++i) 
                {
                    suit1 = Suit.fromIndex(i);
                    
                    if (suited) {
                        suit2 = Suit.fromIndex(i);
                        ret.add( new HoleCards( Card.getCard(suit1, rank1),
                                Card.getCard(suit2, rank)));
                    } 
                    
                    if (unSuited) {
                        for(int j = 0; j < 4; ++j) 
                        {
                            //We only want unsuited
                            if (unSuited && j==i)
                                continue;
                            suit2 = Suit.fromIndex(j);
                            
                            ret.add( new HoleCards( Card.getCard(suit1, rank1), Card.getCard(suit2, rank)));
                        }
                    }
                }
            }
                
            
        }
        
        
        for(HoleCards hc : ret) {
            Preconditions.checkState(!cards.contains(hc), hc.toString() + "  " + this.toString());
            cards.add(hc);
            cardsList.add(hc);
            
            ++totalHands;
            
            if (hc.getHigherRank() != hc.getLowerRank())
            {
                this.rankFreq[hc.getHigherRank().getIndex()]++;
                this.rankFreq[hc.getLowerRank().getIndex()]++;
            } else {
                this.rankFreq[hc.getHigherRank().getIndex()]++;
            }
            //log.debug("hc {} {} {}", hc.getCards(), hc.getCards()[0].index, hc.getCards()[1].index);
           
        }
    }
    
  

    /**
     * @return the cards
     */
    public Set<HoleCards> getCards() {
        return cards;
    }

    /**
     * @return the cardsList
     */
    public List<HoleCards> getCardsList() {
        return cardsList;
    }
    
    public boolean inRange(Card[] cards) 
    {
        HoleCards hc = new HoleCards(cards);
        return this.cards.contains(hc);
    }
    
}
