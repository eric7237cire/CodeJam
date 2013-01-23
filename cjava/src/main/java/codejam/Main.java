package codejam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner;

public class Main  {

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    public static void main(String args[]) throws Exception {


        //2008 emea semis
        //Geometry triangles -- rotation / translation / scaling of triangle.  Finding a point that transforms to itself
        //codejam.y2008.round_emea.scaled_triangle.Main m = new codejam.y2008.round_emea.scaled_triangle.Main();
        
        //Breadth first search
        //codejam.y2008.round_emea.painting_fence.Main m = new codejam.y2008.round_emea.painting_fence.Main();
        
        //Chromatic number of a tree
        //codejam.y2008.round_emea.rainbow_trees.Main m = new codejam.y2008.round_emea.rainbow_trees.Main();
        
        // State transitions / matrix multiplication / binary manipulation
       // codejam.y2008.round_emea.bus_stops.Main m = new codejam.y2008.round_emea.bus_stops.Main();
        
        //2008 amer semis
        // Tree traversal depth first
       // codejam.y2008.round_amer.mixing.Main m = new codejam.y2008.round_amer.mixing.Main();
        
        // Iteratively guessing a code sequence, binary / using incomplete information / assumptions
       // codejam.y2008.round_amer.code_sequence.Main m = new codejam.y2008.round_amer.code_sequence.Main();
        //codejam.y2008.round_amer.code_sequence.Main.example();
        
        // Maintain a list of N best paths ; Probability
        //codejam.y2008.round_amer.test_passing.Main m = new codejam.y2008.round_amer.test_passing.Main();
        
       //Non bipartite matching using Edmonds algorithm (code found...)
        //codejam.y2008.round_amer.king.Main m = new codejam.y2008.round_amer.king.Main();
        
        //2008 apac semis
        //A and B are in c++        
        //C++ for millionaire exists that is very fast
        //Probability ; Making a problem discrete that appears continous
        //codejam.y2008.round_apac.millionaire.Main m = new codejam.y2008.round_apac.millionaire.Main();
        
        //Subtree isomorhisme.  Requires multiple bipartite matchings recursively down the tree
        //codejam.y2008.round_apac.modern_art.Main m = new codejam.y2008.round_apac.modern_art.Main();
        
        
        //2008 Finals  Cheated all the way...
        
        //Psuedo linear optimization.  Visualing solution 2d line
        //codejam.y2008.round_final.juice.Main m = new codejam.y2008.round_final.juice.Main();
        
        //Defining a coord system, recognizing non decreasing sequence
        //codejam.y2008.round_final.ping_pong_balls.Main m = new codejam.y2008.round_final.ping_pong_balls.Main();
        
        //A clever trick, also understanding there are not really multiple solutions to watch out for
        //codejam.y2008.round_final.mine_layer.Main m = new codejam.y2008.round_final.mine_layer.Main();
       
        //Using minimum spanning tree MST primms algorithm
        //codejam.y2008.round_final.bridge_builders.Main m = new codejam.y2008.round_final.bridge_builders.Main();
        
        //Using maxflow to find a maximum perimeter, switches nodes in a bipartite graph to turn a maxcut to a mincut
        //codejam.y2008.round_final.year_code_jam.Main m = new codejam.y2008.round_final.year_code_jam.Main();
        
        //Round 1A -- 2009
        //In C++
        
         //Round 1B -- 2009
        //A in c++
        //Finding next permutation in a sequence
         //codejam.y2009.round_1B.next_number.Main m = new codejam.y2009.round_1B.next_number.Main();
        
        //A Breadth first search, did not really do what solution suggested, used a sortedSet instead of queue
         //codejam.y2009.round_1B.square_math.Main m = new codejam.y2009.round_1B.square_math.Main();
         
         //Round 1C -- 2009 
        //Finding min number in a large base
         //codejam.y2009.round_1C.all_your_base.Main m = new codejam.y2009.round_1C.all_your_base.Main();
        
        //A bit of physics / calculus 
         //codejam.y2009.round_1C.center_mass.Main m = new codejam.y2009.round_1C.center_mass.Main();
        
        //DP.  TODO bottom up
         //codejam.y2009.round_1C.bribe_prisoner.Main m = new codejam.y2009.round_1C.bribe_prisoner.Main();
         
         //Round 2 -- 2009
        
        //Number of swaps to put in order, can only swap adjacent
         //codejam.y2009.crazy_rows.Main m = new codejam.y2009.crazy_rows.Main();
        
        //DP TODO bottom up
         //codejam.y2009.digging_problem.Main m = new codejam.y2009.digging_problem.Main();
        
        //Depth first search, augmenting path
         //codejam.y2009.round_2.stock_charts.Main m = new codejam.y2009.round_2.stock_charts.Main();
        
        //Hardcore circle geometry / polar coordinates
         //codejam.y2009.round_2.watering_plants.Main m = new codejam.y2009.round_2.watering_plants.Main();
         
         //Round 3 -- 2009
        //Grid shortest path
         //codejam.y2009.sokoban.Main m = new codejam.y2009.sokoban.Main();
        
        //Set multiplying, polynomial symbolic manipulation.  TODO re-explain
         //codejam.y2009.alphabetomials.Main m = new codejam.y2009.alphabetomials.Main();
        
        // 3 coloring a particular graph (triangles)
        // codejam.y2009.round_3.football_team.Main m = new codejam.y2009.round_3.football_team.Main();
        
        //Counting ranges.  Had to use complex caching to solve in time.  TODO more comments
         codejam.y2009.round_3.interesting_ranges.Main m = new codejam.y2009.round_3.interesting_ranges.Main();
         
         //Round 4 -- 2009
         //codejam.y2009.round_4.year_more.Main m = new codejam.y2009.round_4.year_more.Main();
         
         //Both of these still a bit slow, especially min perimeter
         //codejam.y2009.min_perimeter.Main m = new codejam.y2009.min_perimeter.Main();
        
        //Using lattice paths to make a complex Dynamic programming DP
        // codejam.y2009.double_sort_grid.Main m = new codejam.y2009.double_sort_grid.Main();
        
         //Network flow
     //   codejam.y2009.round_4.wifi_towers.Main m = new codejam.y2009.round_4.wifi_towers.Main(); 
      
         //2010 qual
         //Bignumber GCD
         //codejam.y2010.fair_warning.Main m = new codejam.y2010.fair_warning.Main();
         
         //Finding repeated state?  
         //codejam.y2010.theme_park.Main m = new codejam.y2010.theme_park.Main();
         
         //2010 1A
         //codejam.y2010.rotate.Main m = new codejam.y2010.rotate.Main();
         //codejam.y2010.smooth.Main m = new codejam.y2010.smooth.Main();
         //codejam.y2010.number_game.Main m = new codejam.y2010.number_game.Main();
         
         //2010 1B
         
         //Tree
         //codejam.y2010.file_fix.Main m = new codejam.y2010.file_fix.Main();
         
         //Reasing through problem, speed physics
        // codejam.y2010.round_1B.chicks.Main m = new codejam.y2010.round_1B.chicks.Main();
         
         //DP of some kind, TODO bottom up
         //codejam.y2010.rank_pure.Main m = new codejam.y2010.rank_pure.Main();
         
         //2010 1C
         
         //codejam.y2010.load_testing.Main m = new codejam.y2010.load_testing.Main();         
         //codejam.y2010.rope_intranet.Main m = new codejam.y2010.rope_intranet.Main();
        
        //Finding rectangles efficiently
      //large a bit slow
        //codejam.y2010.round_1C.chess_boards.Main m = new codejam.y2010.round_1C.chess_boards.Main();

         //2010 2         
         //TODO large slow
         //codejam.y2010.round_2.diamond.Main m = new codejam.y2010.round_2.diamond.Main();
         //codejam.y2010.round_2.world_cup.Main m = new codejam.y2010.round_2.world_cup.Main();         
         //codejam.y2010.round_2.bacteria.Main m = new codejam.y2010.round_2.bacteria.Main(); 
        //TODO corrections small
         //codejam.y2010.round_2.goats.Main m = new codejam.y2010.round_2.goats.Main();
                  
         //2010 3
         
         //codejam.y2010.round_3.rng.Main m = new codejam.y2010.round_3.rng.Main();         
        //codejam.y2010.round_3.boards.Main m = new codejam.y2010.round_3.boards.Main();
        //codejam.y2010.round_3.hotdog.Main m = new codejam.y2010.round_3.hotdog.Main();       
        //TODO large is slow
        //codejam.y2010.round_3.different_sum.Main m = new codejam.y2010.round_3.different_sum.Main();
       
        //2010 final
        //Dynamic programming.  has both recursive and bottom up
       // codejam.y2010.round_final.letter_stamper.Main m = new codejam.y2010.round_final.letter_stamper.Main();
        
        //Partial 2 trees, decomposition to find longest path that does not intersect nodes twice
      //  codejam.y2010.round_final.city_tour.Main m = new codejam.y2010.round_final.city_tour.Main();
        
        //Greedy algorithm, proof by induction.  Very easy implementation to a seemingly hard problem
       // codejam.y2010.round_final.candy_store.Main m = new codejam.y2010.round_final.candy_store.Main();
        
        //codejam.y2010.round_final.travel_plan.Main m = new codejam.y2010.round_final.travel_plan.Main();
                 
         //2011 1A
        
         //A in C++
         //TODO large is quite slow 14 secs -- 30 secs.  Can improve the algorithm
        // codejam.y2011.round_1A.killer_word.Main m = new codejam.y2011.round_1A.killer_word.Main();
         //codejam.y2011.round_1A.dominion.Main m = new codejam.y2011.round_1A.dominion.Main();
         
         //2011 1B
         //A in C++
         //codejam.y2011.round_1B.hotdog_revenge.Main m = new codejam.y2011.round_1B.hotdog_revenge.Main();
         //codejam.y2011.round_1B.house_kittens.Main m = new codejam.y2011.round_1B.house_kittens.Main();
        
        //2011 1C
        //codejam.y2011.round_1C.square_tiles.Main m = new codejam.y2011.round_1C.square_tiles.Main();
        //codejam.y2011.round_1C.space_emergency.Main m = new codejam.y2011.round_1C.space_emergency.Main();
        //codejam.y2011.round_1C.perfect_harmony.Main m = new codejam.y2011.round_1C.perfect_harmony.Main();
         
        //2011 2
        //codejam.y2011.round_2.airport_walkways.Main m = new codejam.y2011.round_2.airport_walkways.Main();
        //codejam.y2011.round_2.spinning_blade.Main m = new codejam.y2011.round_2.spinning_blade.Main();
        //codejam.y2011.round_2.expensive_dinner.Main m = new codejam.y2011.round_2.expensive_dinner.Main();
        
        //codejam.y2011.round_2.ai_wars.Main m = new codejam.y2011.round_2.ai_wars.Main();


        //2011 round 3
        //A discrete binary search problem
       // codejam.y2011.round_3.irregular_cakes.Main m = new codejam.y2011.round_3.irregular_cakes.Main();
        
        //Greedy algorithm (used solution)
        //codejam.y2011.round_3.dire_straights.Main m = new codejam.y2011.round_3.dire_straights.Main();
        
        //2011 final round
        
        //Dynamic programming, combinatorics stars and bars counting (distributing n stars in k buckets )
        //codejam.y2011.round_final.runs.Main m = new codejam.y2011.round_final.runs.Main(); 
        
        //Simulation with a trick to do batch processing.  TODO look at proof via paths / tree justifying the algo
        //codejam.y2011.round_final.rains_atlantis.Main m = new codejam.y2011.round_final.rains_atlantis.Main();
        
        
        //2012 qual
        //A,B,C  in c++
        //TODO a bit slow
       //  codejam.y2012.round_qual.hall_of_mirrors.Main m = new codejam.y2012.round_qual.hall_of_mirrors.Main();
        
        //2012 1A
        //codejam.y2012.round_1A.password.Main m = new codejam.y2012.round_1A.password.Main();
        //codejam.y2012.round_1A.kingdom_rush.Main m = new codejam.y2012.round_1A.kingdom_rush.Main();
        
        //Constraint problem
        //codejam.y2012.round_1A.cruise_control.Main m = new codejam.y2012.round_1A.cruise_control.Main();
        
        //2012 1B
        //codejam.y2012.round_1B.safety_numbers.Main m = new codejam.y2012.round_1B.safety_numbers.Main();
        //codejam.y2012.round_1B.tide.Main m = new codejam.y2012.round_1B.tide.Main();
         
         //Pigeon hole principal / birthday paradox
        //codejam.y2012.round_1B.equal_sums.Main m = new codejam.y2012.round_1B.equal_sums.Main();
        
        //2012 1C
        //codejam.y2012.round_1C.diamond_inheritance.Main m = new codejam.y2012.round_1C.diamond_inheritance.Main();
        //codejam.y2012.round_1C.out_of_gas.Main m = new codejam.y2012.round_1C.out_of_gas.Main();
        //codejam.y2012.round_1C.boxes.Main m = new codejam.y2012.round_1C.boxes.Main();
        
        //2012 2
        //codejam.y2012.round_2.swinging_wild.Main m = new codejam.y2012.round_2.swinging_wild.Main();
        //codejam.y2012.round_2.aerobics.Main m = new codejam.y2012.round_2.aerobics.Main();
        //TODO implement their simpler approach
       // codejam.y2012.round_2.mountain_view.Main m = new codejam.y2012.round_2.mountain_view.Main();
        
        //Did not solve.  FSM intersection / dynamic programming.  TODO Look again why it works        
        //codejam.y2012.round_2.descending_dark.Main m = new codejam.y2012.round_2.descending_dark.Main();
        
        //2012 Final
        
        //Dijkstras using an indexed priority queue
        //codejam.y2012.round_final.zombie_smash.Main m = new codejam.y2012.round_final.zombie_smash.Main();
        
        //Probability, optimizing, state transitions, maybe matrix multiplication
        //codejam.y2012.round_final.upstairs_downstairs.Main m = new codejam.y2012.round_final.upstairs_downstairs.Main();
        
        //codejam.y2012.round_final.xeno_archaeology.Main m = new codejam.y2012.round_final.xeno_archaeology.Main(); 
        
        String[] files = {};
        if (m instanceof DefaultInputFiles) {
            files = ((DefaultInputFiles) m).getDefaultInputFiles();
        }
        
        if (args.length >= 1) {            
            files = args;
        }
        for (String file : files) {
            log.info("Input file {}", file);

             Runner.goSingleThread(file, m, m);
           //Runner.go(file, m, m, 5);
        }       
    }

    
}