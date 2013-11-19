package chess;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    final static Pattern score = Pattern.compile("score cp (-?\\d+)");
    final static Pattern time = Pattern.compile("time (\\d+)");
    final static Pattern mateDepth = Pattern.compile("score mate (-?\\d+)");
    final static Pattern depth = Pattern.compile("depth (\\d+)");
    final static Pattern seldepth = Pattern.compile("seldepth (\\d+)");

    // When search is done
    final static Pattern bestMove = Pattern.compile("bestmove (.*)(?: ponder .*)?");
    
    static Integer getInteger(String infoStr, Pattern pattern) {
        Matcher m = pattern.matcher(infoStr);

        if (m.find()) {
            return Integer.parseInt(m.group(1));
        }

        return null;

    }
}
