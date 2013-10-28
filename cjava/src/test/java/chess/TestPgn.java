package chess;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chess.parsing.Input;
import chess.parsing.PgnParser;
import chess.parsing.TagName;

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
    public void parseTest1() 
    {
        
        Input input = new Input(TestPgn.class.getResourceAsStream(
                "test1.pgn"));
        PgnParser p = new PgnParser(input);
        
        p.parse();
    }
}
