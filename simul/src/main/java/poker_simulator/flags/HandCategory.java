package poker_simulator.flags;

public enum HandCategory implements iFlag {
        
        HIDDEN_PAIR("Pocket pair"),
        
        PAIR_OVERCARDS_0("Pair 0 overcards"), //top pair or overpair
        PAIR_OVERCARDS_1("Pair 1 overcards"), //mid pair or hidden between 1st and 2nd
        PAIR_OVERCARDS_2("Pair 2 overcards"), //low pair or hidden between 2nd and 3rd
        PAIR_OVERCARDS_3("Pair 3 or more overcards"), // very low pair or hidden below 3rd best visible rank
        
        PAIR_USING_HOLE_CARDS("Pair using hole cards"),
        
        PAIR_ON_PAIRED_BOARD("Pair on board"),
        TWO_PAIR_USING_ONE("Two pair on paired board"),
        TWO_PAIR_USING_BOTH("Two pair on unpaired board"),
        TWO_PAIR_USING_NONE("Two pair on 2 pairs on board"),
        SET_USING_NONE("Set on board"),
        SET_USING_ONE("Visible set"),
        SET_USING_BOTH("Hidden set"),
        FLUSH("Flush"),
        FLUSH_DRAW("Flush draw"),
        STRAIGHT("Straight"),
        STRAIGHT_DRAW_2("Straight draw"),
        STRAIGHT_DRAW_1("Gut shot"),
        FULL_HOUSE("Full house"),
        STRAIGHT_FLUSH("Straight flush"),
        HIGH_CARD("High card"),
        QUADS("4 of a kind")
        ;
        
        HandCategory(String desc) {
            this.desc = desc;
        }

        private String desc;

        public String getDesc()
        {
            return desc;
        }

        /* (non-Javadoc)
         * @see pkr.possTree.iFlag#getIndex()
         */
        @Override
        public int getIndex() {
            return ordinal();
        }

        /* (non-Javadoc)
         * @see pkr.possTree.iFlag#getDescription()
         */
        @Override
        public String getDescription() {
            return desc;
        }
        
        
    }

