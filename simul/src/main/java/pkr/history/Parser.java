package pkr.history;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

public class Parser {
    
    static Logger log = LoggerFactory.getLogger(Parser.class);
    
    static Logger logParsedHandOutput = LoggerFactory.getLogger("handOutput");
    
    private static Logger logMainOutput = LoggerFactory.getLogger("mainOutput");
    
    
    private final static Pattern XP_LINE = 
            Pattern.compile("\\+ \\d+ XP pour avoir (?:joué|gagné) une main.");
    
    private final static Pattern REALISATION_LINE = 
            Pattern.compile(".* a atteint .* !");
    
    private final static Pattern ADJUSTEMENT =
            Pattern.compile(".* a fait un.* automatique.");
    
    private final static Pattern HORS_LIGNE =
            Pattern.compile(".* est hors ligne.");
    
       
    private final static Pattern COMMENT =
            Pattern.compile("\\s*//.*");
    
    static boolean isIgnoreLine(String line) 
    {
        if (line.trim().equals(""))
            return true;
        
        if (XP_LINE.matcher(line).matches())
            return true;
        
        if (REALISATION_LINE.matcher(line).matches())
            return true;
        
        if (ADJUSTEMENT.matcher(line).matches()) 
            return true;
        
        if (HORS_LIGNE.matcher(line).matches())
            return true;
    
        if (COMMENT.matcher(line).matches())
        {
            logParsedHandOutput.debug(line);
            return true;
        }
        
        return false;
    }
    
    
    
    private static Pattern patSuivi = Pattern.compile("(.*) a suivi de ([\\d ]+)\\.");
    private static Pattern patParle = Pattern.compile("(.*) a parlé.");    
    private static Pattern patRelance = Pattern.compile("(.*) a relancé de ([\\d ]+).");
    private static Pattern patCouche = Pattern.compile("(.*) se couche.");
    private static Pattern patTapis = Pattern.compile("(.*) a fait tapis."); 
    private static Pattern patTapis2 = Pattern.compile("(.*) fait automatiquement tapis .*");
    private static Pattern patShowdown = Pattern.compile("(.*) remporte le pot \\(([\\d ]+) \\$\\) .*\\.");
    private static Pattern patGagne = Pattern.compile("(.*) gagne.");
    private static Pattern patHandBoundary = Pattern.compile("_*");
        
    public static void main(String[] args) throws IOException {
        String brutefileName = "C:\\codejam\\CodeJam\\simul\\handshistory.txt";
        String fileName =  "C:\\codejam\\CodeJam\\simul\\output\\cleanhandshistory.txt";
        File file = new File(fileName);
        File inputFile = new File(brutefileName);
        
        List<FlopTurnRiverState[]> hands = parseFile(inputFile, file);
        
        StatsSession stats = computeStats(hands);
    }
    
    public static StatsSession computeStats(List<FlopTurnRiverState[]> hands)
    {
        StatsComputer sc = new StatsComputer(hands);
        
        for(String player : sc.stats.currentPlayerList)
        {
            StatsSessionPlayer ssp = sc.stats.playerSessionStats.get(player);
            logMainOutput.debug("\nPlayer [ {} ] -- \n " +
            		"Hands played {} VPIP %{}  PFR %{} (tapis %{})  Call open %{}", 
            		
            		player,  ssp.totalHands,
                    FlopTurnRiverState.df2.format(100.0 * ssp.vpipNumerator / ssp.vpipDenom),
                    FlopTurnRiverState.df2.format(100.0 * ssp.preFlopRaises / ssp.totalHands),
                    FlopTurnRiverState.df2.format(100.0 * ssp.preFlopTapis / ssp.totalHands),
                    FlopTurnRiverState.df2.format(100.0 * ssp.callOpenNumerator / ssp.callOpenDenom)
                    
                    );
            
            
            for(int round = 0; round < 3; ++round)
            {
                logMainOutput.debug("Round {} stats",
                        
                        round == 0 ? "flop" :
                            (round == 1 ? "turn" : "river")
                        
                        );

                logMainOutput.debug("Seen {} [ checks %{} bets %{} ]  in raised pots {} [ calls %{} folded %{} raised %{} ] re raises : {}  check raises : {}",

                ssp.roundStats[round].seen,
                
                    FlopTurnRiverState.df2.format(100.0 * ssp.roundStats[round].checks / ssp.roundStats[round].seen),
                    FlopTurnRiverState.df2.format(100.0 * ssp.roundStats[round].bets / ssp.roundStats[round].seen),
                    ssp.roundStats[round].seen-ssp.roundStats[round].checkedThrough,
                    FlopTurnRiverState.df2.format(100.0 * ssp.roundStats[round].calls / (ssp.roundStats[round].seen-ssp.roundStats[round].checkedThrough)),
                    FlopTurnRiverState.df2.format(100.0 * ssp.roundStats[round].folded / (ssp.roundStats[round].seen-ssp.roundStats[round].checkedThrough)),
                    FlopTurnRiverState.df2.format(100.0 * ssp.roundStats[round].bets / (ssp.roundStats[round].seen-ssp.roundStats[round].checkedThrough)),
                    ssp.roundStats[round].reraises,
                    ssp.roundStats[round].checkRaises

                );

            }
        }
        
        return sc.stats;
    }
    
    public static List<FlopTurnRiverState[]> parseFile(File rawHandHistory, File cleanedHandHistory) throws IOException {
        //String fileName = "C:\\codejam\\CodeJam\\simul\\hands.txt";
        
        Preprocessor.clean(rawHandHistory, cleanedHandHistory);
        
        List<String> lines = Files.readLines(cleanedHandHistory, Charsets.UTF_8);
        
        ParserListener curState = null;
        
        List<FlopTurnRiverState[]> masterList = Lists.newArrayList();
        
        for(int i = 0; i < lines.size(); ++i)
        {
            if (curState != null && curState.replayLine()) {
                log.debug("Replaying line");
                --i;
            }

            String line = lines.get(i);

            log.debug("\nProcessing line [{}]\n", line);
            if (isIgnoreLine(line))
                continue;

            Matcher match = null;

            match = patHandBoundary.matcher(line);
            if (match.matches())
            {
                curState = new FlopTurnRiverState(new ArrayList<String>(), 0, false, 0,  masterList, new FlopTurnRiverState[4]);
                continue;
            }
           
            if (curState == null)
                continue;
              
            try{
            
            match = patSuivi.matcher(line);
            
            if (match.matches()) 
            {
                String playerName = match.group(1);
                String betAmtStr = match.group(2);
                
                int betAmt = Integer.parseInt(betAmtStr.replace(" ", ""), 10);
                log.debug("Suivi player {} bet {} = {} ", playerName, betAmtStr, betAmt);
                curState = curState.handleSuivi(playerName, betAmt);
                continue;
            }
            
            match = patParle.matcher(line);
            
            if (match.matches()) 
            {
                String playerName = match.group(1);
             
                curState = curState.handleParole(playerName);
                continue;
            }
            
            match = patCouche.matcher(line);
            
            if (match.matches()) 
            {
                String playerName = match.group(1);
             
                curState = curState.handleCoucher(playerName);
                continue;
            }
            
            match = patTapis.matcher(line);
            
            if (match.matches()) 
            {
                String playerName = match.group(1);
             
                curState = curState.handleTapis(playerName);
                continue;
            }
            
            match = patTapis2.matcher(line);
            
            if (match.matches()) 
            {
                String playerName = match.group(1);
             
                curState = curState.handleTapis(playerName);
                continue;
            }
            
            match = patGagne.matcher(line);
            
            if (match.matches()) 
            {
                String playerName = match.group(1);
             
                curState = curState.handleGagne(playerName);
                continue;
            }
            
            match = patRelance.matcher(line);
            
            if (match.matches()) 
            {
                String playerName = match.group(1);
                String betAmtStr = match.group(2);
                
                int betAmt = Integer.parseInt(betAmtStr.replace(" ", ""), 10);
                log.debug("Relance player {} bet {} = {} ", playerName, betAmtStr, betAmt);
                curState = curState.handleRelance(playerName, betAmt);
                continue;
            }
            
            match = patShowdown.matcher(line);
            
            if (match.matches()) 
            {
                String playerName = match.group(1);
                String betAmtStr = match.group(2);
                
                int betAmt = Integer.parseInt(betAmtStr.replace(" ", ""), 10);
                log.info("Showdown line {} player {} bet {} = {} ", i, 
                        playerName, betAmtStr, betAmt);
                curState = curState.handleShowdown(playerName, betAmt);
                continue;
            }
            
            } catch (IllegalStateException ex) {
                log.warn("Problem line {}", i,  ex);
                curState = null;
                continue;
            }
            
            Preconditions.checkState(false, line);
            //log.debug("Line {}", line);
        }
    
        
        
        return masterList;
    }
    
    
}
