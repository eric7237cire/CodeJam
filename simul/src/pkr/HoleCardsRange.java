package pkr;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    
    public static String SUITED_ACES= "A2s A3s A4s A5s A6s A7s A8s A9s ATs AJs AQs AKs";
    
    
    
    private static Logger log = LoggerFactory.getLogger("main");
    
    boolean mask[][];
    public boolean inRangeCard1[];
    public boolean inRangeCard2[];
    private String rangeStr;
    private Set<HoleCards> cards;
    private List<HoleCards> cardsList;
    double[] relativeFreq;
    
  //AKo 8Ts 99  len 2 / 3
    //or AsKh 9s9h len 4
    public HoleCardsRange(String rangeStr) {
        
        this.rangeStr = rangeStr;
        
        mask = new boolean[52][52];
        inRangeCard1 = new boolean[52];
        inRangeCard2 = new boolean[52];
        cards = Sets.newHashSet();
        cardsList = Lists.newArrayList();
        
        String[] ranges = rangeStr.split("\\s*[, ]\\s*");
        
        for(String code : ranges) {
            log.debug("Parse code [{}] of {}", code, rangeStr);
            addCode(code);
        }
    }
    
    @Override
    public String toString()
    {
        return rangeStr;
    }

    private void addCode(String code) {
        List<HoleCards> ret = Lists.newArrayList();
        
        Preconditions.checkArgument(code.length() >= 2 && code.length() <= 4);
        
        boolean hasPlus = false;
        if (code.charAt(code.length()-1) == '+') 
        {
            hasPlus = true;
            code = code.substring(0, code.length()-1);
        }
        
        if (code.length() == 4) {
            HoleCards hc = new HoleCards( Card.parseCard(code.substring(0,2)),
                    Card.parseCard(code.substring(2,4)));
            ret.add(hc);
        } else {
            CardRank rank1 = CardRank.fromChar(code.charAt(0));
            CardRank rank2 = CardRank.fromChar(code.charAt(1));
            
            //pairs
            if (rank1 == rank2) {
                
                CardRank toRank = hasPlus ? CardRank.ACE : rank1;
                
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
                            
                            ret.add( new HoleCards( new Card(suit1, rank), new Card(suit2, rank)));
                        }
                    }
                       
                }
            } else {
                boolean suited = code.length() == 2 || (code.length() == 3 && code.charAt(2) == 's');
                boolean unSuited = code.length() == 2 || (code.length() == 3 && code.charAt(2) == 'o');
                
                //First card is highest
                if (rank2.getIndex() > rank1.getIndex()) 
                {
                    CardRank tmp = rank2;
                    rank2 = rank1;
                    rank1 = tmp;                            
                }
                Preconditions.checkState(rank1.getIndex() > rank2.getIndex());
                
                int toRank2Index = hasPlus ? rank1.getIndex()-1 : rank2.getIndex();
                
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
                            ret.add( new HoleCards( new Card(suit1, rank1), new Card(suit2, rank)));
                        } 
                        
                        if (unSuited) {
                            for(int j = 0; j < 4; ++j) 
                            {
                                //We only want unsuited
                                if (unSuited && j==i)
                                    continue;
                                suit2 = Suit.fromIndex(j);
                                
                                ret.add( new HoleCards( new Card(suit1, rank1), new Card(suit2, rank)));
                            }
                        }
                    }
                }
                
            }
        }
        
        
        for(HoleCards hc : ret) {
            Preconditions.checkState(!cards.contains(hc), hc.toString() + "  " + this.toString());
            cards.add(hc);
            cardsList.add(hc);
            //log.debug("hc {} {} {}", hc.getCards(), hc.getCards()[0].toInt(), hc.getCards()[1].toInt());
            mask[hc.getCards()[0].toInt()][hc.getCards()[1].toInt()] = true;
            mask[hc.getCards()[1].toInt()][hc.getCards()[0].toInt()] = true;
            
            inRangeCard1[hc.getCards()[0].toInt()] = true;
            inRangeCard2[hc.getCards()[1].toInt()] = true;
        }
    }
    
    public boolean inRange(Card[] cards) {
        Preconditions.checkArgument(cards.length == 2);
        
        return inRange(cards[0].toInt(), cards[1].toInt());
    }
    /**
     * Order of card1 and card2 does not matter
     * @param card1
     * @param card2
     * @return
     */
    public boolean inRange(int card1, int card2) {
        
        return mask[card1][card2];
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
    
    
}
