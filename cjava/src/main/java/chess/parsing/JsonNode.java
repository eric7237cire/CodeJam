package chess.parsing;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
//import com.google.gson.Gson;

/**
 * Node in the js tree
 * @author Groning-E
 *
 */
public class JsonNode {
    
    private static final Logger log = LoggerFactory.getLogger(JsonNode.class);
    
    Object metaData;
    JsonData data;
    Map<String, Object> attr;
    String state;
    List<JsonNode> children;
    
    JsonNode() {
        
    }
    
    JsonNode(String title) {
        data = new JsonData();
        data.title = title; 
        children = Lists.newArrayList();
        attr = Maps.newHashMap();
    }
    
    void setTitle(String newTitle) {
        data.title = newTitle;
    }
    
    String getTitle() {
        return data.title;
    }
    
    //private static final String newLine = "<br/>";
    
    private static void addVariation(JsonNode parent, List<Move> moveList)
    {
        Move firstMove = moveList.get(0);
        
        
        Ply firstPly = firstMove.whiteMove == null ? firstMove.blackMove : firstMove.whiteMove;
        
        JsonNode plyNode = addPly(parent, firstPly, true, false);
        
        if (firstMove.whiteMove == null) {
            addMovesToNode(plyNode, moveList.subList(1, moveList.size()), false);
        } else {
            firstMove.whiteMove = null;
            addMovesToNode(plyNode, moveList, false);
        }
    }
    
    /**
     * 
     * @param parent
     * @param ply
     * @return newParent
     */
    private static JsonNode addPly(JsonNode parent, Ply ply, 
            boolean returnPlyNodeForce,
            boolean isMainLine
            )
    {
        if (ply == null)
            return parent;
        
        if (ply.getSanTxt() == null)
            return parent;
        
        String txt = ply.toString();
        JsonNode plyNode = new JsonNode(txt);
        
        if (ply == null || ply.boardAfterMove == null) {
            log.error("board is null");
        } else {
            plyNode.attr.put("board", ply.boardAfterMove.toString());
        }
        
        parent.children.add(plyNode);
        
        if (ply.variations.size() > 0) {
        
            
                if (isMainLine ) {
                    plyNode.state = "open";
                } else {
                    plyNode.state = "closed";
                }
            
            for(int varIdx = 0; varIdx < ply.variations.size(); ++varIdx)
                addVariation(parent, ply.variations.get(varIdx));
            
            return plyNode;
        } 
        
        return returnPlyNodeForce ? plyNode : parent;
    }
    private static void addMovesToNode(JsonNode parent, List<Move> moveList, boolean isMainLine)
    {
        for(int i = 0; i < moveList.size(); ++i)
        {
            Move move = moveList.get(i);
            
            parent = addPly(parent, move.whiteMove, false, isMainLine);
            
            parent = addPly(parent, move.blackMove, false, isMainLine);
            
            
            
        }
    }
    
    public static JsonNode buildFromGame(Game g)
    {
        JsonNode game = new JsonNode("game");
        game.state = "open";
        JsonNode parent = game;
        
        addMovesToNode(parent, g.moves, true);
        
        
        return game;
    }
    
    public static void main(String[] args) {
        JsonNode g1 = new JsonNode("g1");
        JsonNode g2 = new JsonNode("g2");
        
        JsonNode m1 = new JsonNode("e4");
        JsonNode m2 = new JsonNode("e5");
        JsonNode m3 = new JsonNode("Nf3");
        
        m1.children.add(m2);
        m2.children.add(m3);
        g1.children.add(m1);
        
        List<JsonNode> roots = Lists.newArrayList();
        roots.add(g1);
        roots.add(g2);
        
        //Gson gson = new Gson();
        //String json = gson.toJson(roots);
        
        //log.info(json);
    }
}
