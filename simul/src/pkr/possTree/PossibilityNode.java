package pkr.possTree;

import java.util.List;

public class PossibilityNode
{

    /*
     * Flop
     * Flop eval (winning / losing etc)
     * Turn (ie pairing board etc, 3 to flush, etc)
     * Turn eval
     * River (ie pairing board etc)
     * River eval
     * 
     * losing to [top pair, mid or lower pair, overpair, two pair or better]
 * out kicked by which level ?
 * 
 * At each level, we have # of instances along with list
 * of top 5 ish villian hole cards with rel freq
 * 
 *  Parent 
 *  
 *  Display
 *  
 *  [Description] % total % relative to parent
 *  (AK 30%, AJ 20%, A9 10%, ...)  --villian winning % OR 2nd best hand %
     */
    public PossibilityNode() {
        // TODO Auto-generated constructor stub
    }

    //Map<iDisplayNode>
    public List<iDisplayNode> getChildren() {
        return null;
    }
}
