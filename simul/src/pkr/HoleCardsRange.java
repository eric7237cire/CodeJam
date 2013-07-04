package pkr;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class HoleCardsRange {
    
    private static Logger log = LoggerFactory.getLogger("main");
    
    boolean mask[][];
    
    List<HoleCards> cards;
    
  //AKo 8Ts 99  len 2 / 3
    //or AsKh 9s9h len 4
    public HoleCardsRange(String rangeStr) {
        
        mask = new boolean[52][52];
        cards = Lists.newArrayList();
        
        String[] ranges = rangeStr.split("\\s*[, ]\\s*");
        
        for(String code : ranges) {
            log.debug("Parse code [{}] of {}", code, rangeStr);
            addCode(code);
        }
    }
    
    private void addCode(String code) {
List<HoleCards> ret = Lists.newArrayList();
        
        Preconditions.checkArgument(code.length() >= 2 && code.length() <= 4);
        
        if (code.length() == 4) {
            HoleCards hc = new HoleCards( Card.parseCard(code.substring(0,2)),
                    Card.parseCard(code.substring(2,4)));
            ret.add(hc);
        } else {
            CardRank rank1 = CardRank.fromChar(code.charAt(0));
            CardRank rank2 = CardRank.fromChar(code.charAt(1));
            
            //pairs
            if (rank1 == rank2) {
                
                Suit suit1;
                Suit suit2;
                for(int i = 0; i < 4; ++i) 
                {
                    suit1 = Suit.fromIndex(i);
                    for(int j = i+1; j < 4; ++j) 
                    {
                        suit2 = Suit.fromIndex(j);
                        
                        ret.add( new HoleCards( new Card(suit1, rank1), new Card(suit2, rank2)));
                    }
                }
                    
            } else {
                boolean suited = code.length() == 3 && code.charAt(2) == 's';
                
                Suit suit1;
                Suit suit2;
                for(int i = 0; i < 4; ++i) 
                {
                    suit1 = Suit.fromIndex(i);
                    
                    if (suited) {
                        suit2 = Suit.fromIndex(i);
                        ret.add( new HoleCards( new Card(suit1, rank1), new Card(suit2, rank2)));
                    } else {
                        for(int j = 0; j < 4; ++j) 
                        {
                            suit2 = Suit.fromIndex(j);
                            
                            ret.add( new HoleCards( new Card(suit1, rank1), new Card(suit2, rank2)));
                        }
                    }
                }
                
            }
        }
        
        cards.addAll(ret);
        
        for(HoleCards hc : cards) {
            //log.debug("hc {} {} {}", hc.getCards(), hc.getCards()[0].toInt(), hc.getCards()[1].toInt());
            mask[hc.getCards()[0].toInt()][hc.getCards()[1].toInt()] = true;
        }
    }
    
    public boolean inRange(int card1, int card2) {
        Preconditions.checkState(card2 > card1);
        
        return mask[card1][card2];
    }
}
