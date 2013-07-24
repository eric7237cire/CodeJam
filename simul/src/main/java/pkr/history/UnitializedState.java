package pkr.history;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pkr.history.Parser.State;

public class UnitializedState implements State {
    static Logger log = LoggerFactory.getLogger(Parser.class);

    @Override
    public State handleLine(String line) 
    {
        if (Parser.isIgnoreLine(line))
            return this;

        log.debug("uniti line [{}]", line);
        String regex = "_*";

        Pattern pat = Pattern.compile(regex);

        Matcher mat = pat.matcher(line);

        if (mat.matches()) {
            log.debug("Trouvé _ line");
            return new DiscoveryPreflopState();
        }

        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {

        return super.toString();
    }

}