package pkr.history;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class Statistics {

    private static Logger log = LoggerFactory.getLogger(Statistics.class);
    
    public static String formatPercent(double decimalNum, double decimalDenom)
    {
        if (Double.isNaN(decimalDenom) || Double.isNaN(decimalNum) || decimalDenom < 0.0001)
            return "n/a";
        
        
        
        return df1.format(100.0 * decimalNum / decimalDenom) + "%";
    }
    
    public static String formatPercent(int decimalNum, int decimalDenom, boolean show)
    {
        if ( decimalDenom == 0)
            return "n/a";
        
        StringBuffer sb = new StringBuffer();
        
        sb.append( df1.format(100.0 * decimalNum / decimalDenom));
        sb.append("%");
        
        if (show)
        {
            sb.append(" (");
            sb.append(decimalNum);
            sb.append("/");
            sb.append(decimalDenom);
            sb.append(")");
        }
        
        return sb.toString();
    }
    
    public static String formatMoney(double amtNum, int amtDenom)
    {
        if (amtDenom == 0)
        {
            return "$0";
        }
        
        return "$" + moneyFormat.format( (int) (amtNum / amtDenom) );
    }
    
    public static String roundToStr(int round)
    {
        return round == 0 ? "Preflop" :
            round == 1 ? "Flop" :
            (round == 2 ? "Turn" : "River");
    }
    
    public final static DecimalFormat df1;
    public final static DecimalFormat df2;
    public final static DecimalFormat moneyFormat;
    
    static {
        
        df1 = new DecimalFormat("0.#");
        df1.setRoundingMode(RoundingMode.HALF_UP);
        df1.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        
        
        df2 = new DecimalFormat("0.##");
        df2.setRoundingMode(RoundingMode.HALF_UP);
        df2.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(' ');

        moneyFormat = new DecimalFormat("###,###", symbols);
    }
}
