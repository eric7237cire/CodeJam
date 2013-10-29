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

import com.google.gson.Gson;

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
    public void parseTest1() throws IOException
    {
        
        Input input = new Input(TestPgn.class.getResourceAsStream(
                "test1.pgn"));
        PgnParser p = new PgnParser(input);
        
        Game g = p.parseGame();
        
        assertEquals(71, g.getMoves().size());
        JsonNode node = JsonNode.buildFromGame(g);
        
        Gson gson = new Gson();
        String s = gson.toJson(node);
        
        File f = new File("C:\\codejam\\CodeJam\\cjava\\tree\\data.js");
        FileUtils.write(f, "allData=",false);
        FileUtils.write(f, s, true);
        FileUtils.write(f, ";", true);
        log.info(s);
    }
}
