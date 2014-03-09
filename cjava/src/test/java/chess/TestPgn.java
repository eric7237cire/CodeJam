package chess;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chess.parsing.Game;
import chess.parsing.Input;
import chess.parsing.JsonNode;
import chess.parsing.Move;
import chess.parsing.PgnParser;
import chess.parsing.TagName;

//import com.google.gson.Gson;

public class TestPgn {
    
    Logger log = LoggerFactory.getLogger(TestPgn.class);
    
    @Test
    public void testParseTag() throws IOException
    {
        /*
        InputStream is = TestPgn.class.getResourceAsStream(
                "test1.pgn");
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        
        String line;
        while( (line = reader.readLine()) != null )
        {
            log.debug(line);
        }
        */
        Pair<TagName, String> tag1 = PgnParser.parseTag("[TimeControl \"20/300:0\"]");
        assertEquals(TagName.TimeControl, tag1.getKey());
        assertEquals("20/300:0", tag1.getValue()); 
        
    }
    
    @Test
    public void parseMove()
    {
        Input input = new Input("45. d5 {test comment} e4");
        PgnParser p = new PgnParser(input);
        
        Move m = p.parseMove();
        
        assertEquals(45, m.getMoveNumber());
        assertEquals("d5", m.getWhiteMove().getSanTxt());
        assertEquals("test comment", m.getWhiteMove().getComment());
        
        assertEquals("e4", m.getBlackMove().getSanTxt());
    }
    
    @Test
    public void parseVariation()
    {
        Input input = new Input("45. d5 {test comment} (45. N3xe4 $2) 45... e4");
        PgnParser p = new PgnParser(input);
        
        Move m = p.parseMove();
        
        assertEquals(45, m.getMoveNumber());
        assertEquals("d5", m.getWhiteMove().getSanTxt());
        assertEquals("test comment", m.getWhiteMove().getComment());
        assertEquals(1, m.getWhiteMove().getVariations().size());
        
        Move varMove1 = m.getWhiteMove().getVariations().get(0).get(0);
        assertEquals("N3xe4", varMove1.getWhiteMove().getSanTxt());
        assertEquals((Integer)2, varMove1.getWhiteMove().getNagValue());
        
        assertEquals("e4", m.getBlackMove().getSanTxt());
    }
    
    @Test
    public void parseVariations()
    {
        Input input = new Input("45. d5 {test comment} (45. N3xe4 $2) 45... e4 (45... Nexh3) (45... Nbxh3) 46.  f3 *");
        PgnParser p = new PgnParser(input);
        
        Move m = p.parseMove();
        

        assertEquals( 2, m.getBlackMove().getVariations().size() );
        
        Move var1Move1 = m.getBlackMove().getVariations().get(0).get(0);
        Move var2Move1 = m.getBlackMove().getVariations().get(1).get(0);
        
        assertEquals("Nexh3", var1Move1.getBlackMove().getSanTxt());
        assertEquals("Nbxh3", var2Move1.getBlackMove().getSanTxt());
        
    }
    
    
    
    @Test
    public void testPositions() throws Exception
    {
        Input input = new Input("1. e4 d5 2. e5 f5 3. exf6 *");
        
        Board[] boards = new Board[] { new Board("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"),
         new Board("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1"),
         new Board("rnbqkbnr/ppp1pppp/8/3p4/4P3/8/PPPP1PPP/RNBQKBNR w KQkq d6 0 2"),
         new Board("rnbqkbnr/ppp1pppp/8/3pP3/8/8/PPPP1PPP/RNBQKBNR b KQkq - 0 2"),
         new Board("rnbqkbnr/ppp1p1pp/8/3pPp2/8/8/PPPP1PPP/RNBQKBNR w KQkq f6 0 3"),
         new Board("rnbqkbnr/ppp1p1pp/5P2/3p4/8/8/PPPP1PPP/RNBQKBNR b KQkq - 0 3")};
        
        for (int i = 0; i < boards.length; ++i) {
            log.debug(boards[i].toFenString());
        }
        PgnParser p = new PgnParser(input);
        
        Game g = p.parseGame();
        
        assertEquals(boards[0], g.getMoves().get(0).getWhiteMove().getBoardBeforeMove());
        
       // Board chk = g.getMoves().get(0).getWhiteMove().getBoardAfterMove();
        
        assertEquals(boards[1], g.getMoves().get(0).getWhiteMove().getBoardAfterMove());
        
        assertEquals(boards[1], g.getMoves().get(0).getBlackMove().getBoardBeforeMove());
        assertEquals(boards[2], g.getMoves().get(0).getBlackMove().getBoardAfterMove());

        assertEquals(boards[2], g.getMoves().get(1).getWhiteMove().getBoardBeforeMove());
        assertEquals(boards[3], g.getMoves().get(1).getWhiteMove().getBoardAfterMove());
        
        assertEquals(boards[3], g.getMoves().get(1).getBlackMove().getBoardBeforeMove());
        assertEquals(boards[4], g.getMoves().get(1).getBlackMove().getBoardAfterMove());
        
        assertEquals(boards[4], g.getMoves().get(2).getWhiteMove().getBoardBeforeMove());
        assertEquals(boards[5], g.getMoves().get(2).getWhiteMove().getBoardAfterMove());
        
       // assertEquals(boards[0], g.getMoves().get(0).getBlackMove().getBoardBeforeMove());
       // assertEquals(boards[1], g.getMoves().get(0).getBlackMove().getBoardAfterMove());
        
        LaunchUCI uci = new LaunchUCI();
        uci.go();
        
        for (int moveIdx = 0; moveIdx < g.getMoves().size(); ++moveIdx) {
            
            Move move = g.getMoves().get(moveIdx);
            
            Board before = g.getPly(moveIdx, true).getBoardBeforeMove();
            Board after = g.getPly(moveIdx, true).getBoardAfterMove();
            
            PositionEval s1 = uci.evalPosition(before.toFenString(), 200);
            
            PositionEval s2 = uci.evalPosition(after.toFenString(), 200);
            
            log.debug("before {}\n{} after {}\n{}", move.getWhiteMove().getSanTxt(),
                    before.toString(), move.getWhiteMove().getSanTxt(),after.toString());
            log.debug("Move {} Difference {} {} = {}", moveIdx+1, s1, s2, s2.scoreCp-s1.scoreCp);
        }
    }
    
    @Test
    public void testPositions2() throws Exception
    {
        Input input = new Input("1. d4 e6 *");
        
        
        PgnParser p = new PgnParser(input);
        
        Game g = p.parseGame();
        
       
        LaunchUCI uci = new LaunchUCI();
        uci.go();
        
        for (int moveIdx = 0; moveIdx < g.getMoves().size(); ++moveIdx) {
            
                        
            Board before = g.getPly(moveIdx, true).getBoardBeforeMove();
            Board after = g.getPly(moveIdx, true).getBoardAfterMove();
            
            PositionEval s1 = uci.evalPosition(before.toFenString(), 200);
            
            PositionEval s2 = uci.evalPosition(after.toFenString(), 200);
            
            log.debug("before {} after {}", before.toString(), after.toString());
            log.debug("Move {} Difference {} {} = {}", moveIdx+1, s1, s2, -s2.scoreCp-s1.scoreCp);
        }
    }
}
