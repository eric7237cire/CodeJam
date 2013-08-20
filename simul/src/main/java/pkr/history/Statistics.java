package pkr.history;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class Statistics {

    private static Logger log = LoggerFactory.getLogger(Statistics.class);
    
    public static String formatPercent(double decimalNum, double decimalDenom)
    {
        if (Double.isNaN(decimalDenom) || Double.isNaN(decimalNum) || decimalDenom < 0.0001)
            return "n/a";
        
        
        
        return FlopTurnRiverState.df1.format(100.0 * decimalNum / decimalDenom) + "%";
    }
    
    public static String formatMoney(double amtNum, int amtDenom)
    {
        if (amtDenom == 0)
        {
            return "$0";
        }
        
        return "$" + FlopTurnRiverState.moneyFormat.format( (int) (amtNum / amtDenom) );
    }
}
