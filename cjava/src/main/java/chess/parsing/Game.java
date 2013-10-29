package chess.parsing;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

public class Game {
    List<Move> moves;
    
    List<Pair<TagName, String>> tags;

    public Game() {
        super();
        
        moves = Lists.newArrayList();
        tags = Lists.newArrayList();
    }

    /**
     * @return the moves
     */
    public List<Move> getMoves() {
        return moves;
    }

    /**
     * @param moves the moves to set
     */
    public void setMoves(List<Move> moves) {
        this.moves = moves;
    }

    /**
     * @return the tags
     */
    public List<Pair<TagName, String>> getTags() {
        return tags;
    }

    /**
     * @param tags the tags to set
     */
    public void setTags(List<Pair<TagName, String>> tags) {
        this.tags = tags;
    }
}
