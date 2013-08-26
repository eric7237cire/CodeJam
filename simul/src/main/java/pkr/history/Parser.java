package pkr.history;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.io.Files;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/*
 * Action to win ratio
 * 
check/reraise should count as check/raise

discount single gagne from playerlist
 */
public class Parser {
    
    static Logger log = LoggerFactory.getLogger(Parser.class);
    
    static Logger logParsedHandOutput = LoggerFactory.getLogger("handOutput");
    
    private static Logger logMainOutput = LoggerFactory.getLogger("mainOutput");
    
    
    private final static Pattern XP_LINE = 
            Pattern.compile("\\+ \\d+ XP pour avoir (?:joué|gagné) une main.");
    
    private final static Pattern REALISATION_LINE = 
            Pattern.compile(".* (?:a|avez) atteint .* !");
    
    private final static Pattern ADJUSTEMENT =
            Pattern.compile(".* a fait un.* automatique.");
    
    private final static Pattern HORS_LIGNE =
            Pattern.compile(".* est hors ligne.");
    
       
    private final static Pattern COMMENT =
            Pattern.compile("\\s*//.*");
    
    private static boolean isIgnoreLine(String line) 
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
        
    @SuppressWarnings("unused")
    public static void main(String[] args) throws IOException {
        String brutefileName = "C:\\codejam\\CodeJam\\simul\\input\\handshistory.txt";
        String fileName =  "C:\\codejam\\CodeJam\\simul\\output\\cleanhandshistory.txt";
        
        log.debug("Working Directory = {}",
                System.getProperty("user.dir"));
        
        File file = new File(fileName);
        File inputFile = new File(brutefileName);
        
        HandInfoCollector hands = parseFile(inputFile, file);
        
        StatsSession stats = computeStats(hands);
    }
    
    public static void outputStats(StatsComputer sc)
    {
      //Freemarker configuration object
        Configuration cfg = new Configuration();
        
        cfg.setClassForTemplateLoading(Parser.class, "/");
        
        try {
            //Load template from source folder
            Template template = cfg.getTemplate("output.ftl");
             
            // Build the data-model
            Map<String, Object> data = new HashMap<String, Object>();
           
            template.setEncoding("UTF-8");
 
            data.put("stats", sc.stats);
             
            // Console output
            /*
            Writer out = new OutputStreamWriter(System.out);
            template.process(data, out);
            out.flush();
 */
            // File output
            Writer file = new FileWriter (new File("C:\\codejam\\CodeJam\\simul\\output\\FTL_helloworld.xhtml"));
            template.process(data, file);
            file.flush();
            file.close();
             
        } catch (IOException e) {
            log.error("ex",e);
        } catch (TemplateException e) {
            log.error("ex",e);
        }
    }
    
    public static void outputHands(HandInfoCollector hc)
    {
      //Freemarker configuration object
        Configuration cfg = new Configuration();
        
        cfg.setClassForTemplateLoading(Parser.class, "/");
        
        try {
            //Load template from source folder
            Template template = cfg.getTemplate("handsLog.ftl");
             
            // Build the data-model
            Map<String, Object> data = new HashMap<String, Object>();
           
            template.setEncoding("UTF-8");
 
            data.put("hands", hc.listHandInfo);
             
            // Console output
            /*
            Writer out = new OutputStreamWriter(System.out);
            template.process(data, out);
            out.flush();
 */
            // File output
            Writer file = new FileWriter (new File("C:\\codejam\\CodeJam\\simul\\output\\handsLog.xhtml"));
            template.process(data, file);
            file.flush();
            file.close();
             
        } catch (IOException e) {
            log.error("ex",e);
        } catch (TemplateException e) {
            log.error("ex",e);
        }
    }
    
    public static StatsSession computeStats(HandInfoCollector hands)
    {
        
        StatsComputer sc = new StatsComputer(hands);
        

        Collections.sort(sc.stats.currentPlayerList);
        
        outputStats(sc);
        
        outputHands(hands);
        
        if (1==1)
            return sc.stats;
        
        sc.stats.currentPlayerList.remove("Eric");
        
        //VPIP and PFR / position
        for(String player : sc.stats.currentPlayerList)
        {
            StatsSessionPlayer ssp = sc.stats.playerSessionStats.get(player);
            logMainOutput.debug("\nPlayer [ {} ] -- \n " +
            		"Preflop Hands played {} {} [ {} ] \n  [ {} ] [ {} ]", 
            		
            		player,  ssp.totalHands,
            		ssp.getStatValue("vpip"),
            		ssp.getStatValue("pfr"),
            		ssp.getStatValue("notfpfr"),
            		ssp.getStatValue("3bet")
                    
                    );
            
            
            for(int round = 0; round < 3; ++round)
            {
                
                logMainOutput.debug("\nRound {} stats. " ,
                        
                        Statistics.roundToStr(round + 1)
                           
                        );

                logMainOutput.debug("{}", ssp.getStatValue("dcl" + (round+1)));
                
                /*
                logMainOutput.debug(" Unopened [{}] : [ checks %{} bets %{} all ins %{} calls rr : {} fold rr : {} rr : {} cr: {}]  \n" +
                		" Opened [{}] : [ checks %{} calls %{} folded %{} raised %{} all in %{} ] ",
                        rs.unopened,
                    Statistics.formatPercent( rs.checksUnopened, rs.unopened),
                    Statistics.formatPercent( rs.bets, rs.unopened),
                    Statistics.formatPercent( rs.betAllIn, rs.unopened),
                     rs.callReraise,
                     rs.betFold,
                     rs.reRaiseUnopened,
                     rs.checkRaises,
                    rs.openedBySomeoneElse,
                    Statistics.formatPercent( rs.checksOpened, rs.openedBySomeoneElse),
                    Statistics.formatPercent( rs.calls, rs.openedBySomeoneElse),
                    Statistics.formatPercent( rs.folded, rs.openedBySomeoneElse),
                    Statistics.formatPercent( rs.reRaiseOpened, rs.openedBySomeoneElse),
                    Statistics.formatPercent( rs.raiseCallAllIn, rs.openedBySomeoneElse)
                    

                );*/
                
                /*logMainOutput.debug("Average bet size %{}  average fold to bet size %{} ",
                        Statistics.formatPercent(ssp.roundStats[round].avgBetToPot, 1),
                        Statistics.formatPercent(ssp.roundStats[round].avgFoldToBetToPot, 1));
*/
            }
        }
        
        return sc.stats;
    }
    
   
    public static HandInfoCollector parseFile(File rawHandHistory, File cleanedHandHistory) throws IOException {
        //String fileName = "C:\\codejam\\CodeJam\\simul\\hands.txt";
        
        Preprocessor.clean(rawHandHistory, cleanedHandHistory);
        
        List<String> lines = Files.readLines(cleanedHandHistory, Charsets.UTF_8);
        
        ParserListener curState = null;
        
        HandInfoCollector handInfoCollector = new HandInfoCollector();
        
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
                curState = new FlopTurnRiverState(new ArrayList<String>(), 0, false, 0, handInfoCollector,
                        new HandInfo(i, handInfoCollector.masterList.size()));
                log.debug("Hand # {} starts on line {}", handInfoCollector.masterList.size()+1, i);
                handInfoCollector.startingLines.add(i);
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
                log.error("Problem line {}", i,  ex);
                curState = null;
                continue;
            }
            
            Preconditions.checkState(false, "Line (%s) %s", i,  line);
            //log.debug("Line {}", line);
        }
    
        
        
        return handInfoCollector;
    }
    
    
}
