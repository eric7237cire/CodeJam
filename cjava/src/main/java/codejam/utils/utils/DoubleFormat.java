package codejam.utils.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class DoubleFormat {

    private DoubleFormat() {
       
    }

    public final static DecimalFormat df6;
    public final static DecimalFormat df7;
    public final static DecimalFormat df3;
    
    static {
        df6 = new DecimalFormat("0.######");
        df6.setRoundingMode(RoundingMode.HALF_UP);
        df6.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        
        df7 = new DecimalFormat("0.#######");
        df7.setRoundingMode(RoundingMode.HALF_UP);
        df7.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        
        df3 = new DecimalFormat("0.###");
        df3.setRoundingMode(RoundingMode.HALF_UP);
        df3.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
    }
}
