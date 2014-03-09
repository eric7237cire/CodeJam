package chess;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chess.parsing.Game;
import chess.parsing.Input;
import chess.parsing.JsonNode;
import chess.parsing.PgnParser;

//import com.google.gson.Gson;

public class TestGenerateDataJs {
    

    Logger log = LoggerFactory.getLogger(TestGenerateDataJs.class);
    
    @Test
    public void parseTest1() throws IOException
    {
        
        Input input = new Input(TestPgn.class.getResourceAsStream(
                "test2.pgn"));
        PgnParser p = new PgnParser(input);
        
        Game g = p.parseGame();
        
        //assertEquals(71, g.getMoves().size());
        JsonNode node = JsonNode.buildFromGame(g);
        
       // Gson gson = new Gson();
      //  String s = gson.toJson(node);
        
        File f = new File("C:\\codejam\\CodeJam\\cjava\\tree\\data.js");
        FileUtils.write(f, "allData=",false);
       // FileUtils.write(f, s, true);
        FileUtils.write(f, ";", true);
      //  log.info(s);
    }
}
