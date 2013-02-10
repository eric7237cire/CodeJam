package codejam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner;

public class Main  {

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    static void africa(String[] args) {
        //2010 africa
        
        //A to C in C++
        /**
         * Africa 2010 Problem A to C in C++.
         * 
         * C is particularly interesting -- Very interesting inductive reasoning -- " africa 2010 qualification round"
         */
        //codejam.y2010aa.round_all.polygraph.Main m = new codejam.y2010aa.round_all.polygraph.Main();
        
        //2011 africa / arabia
        //codejam.y2011aa.round_all.vanishing_numbers.Main m = new codejam.y2011aa.round_all.vanishing_numbers.Main();
        //codejam.y2011aa.round_all.battlefield.Main m = new codejam.y2011aa.round_all.battlefield.Main();
        
        codejam.y2011aa.round_all.radio_receiver.Main m = new codejam.y2011aa.round_all.radio_receiver.Main();
        
        
        String[] files = Main.getFiles(m, args);
        for (String file : files) {
            log.info("Input file {}", file);

            Runner.goSingleThread(file, m, m);
          //Runner.go(file, m, m, 5);
        }       
        
    }
    
    static void practiceRound2008(String args[]) {
        /**
         * 2008 -- Practice round
         * Problem 1
         * Converting from one base to another
         */
        //codejam.y2008.round_pracProb.alien_numbers.Main m = new codejam.y2008.round_pracProb.alien_numbers.Main();
        
        /**
         * 2008 -- Practice round
         * Problem 2
         * 
         * Tracing through a maze and reproducing it.
         */
        //codejam.y2008.round_pracProb.alwaysLeft.Main m = new codejam.y2008.round_pracProb.alwaysLeft.Main();
        
        /**
         * 2008 -- Practice round
         * Problem 3
         * 
         * Egg problem
         */
        //codejam.y2008.round_pracProb.egg_drop.Main m = new codejam.y2008.round_pracProb.egg_drop.Main();
        
        /**
         * 2008 -- Practice round
         * Problem 4
         * 
         * Finding route to stores with added constraint that perishable items
         * must return home immediately.
         * 
         * Used lots of bit manipulations, solution from Plan3.cpp
         */
        //codejam.y2008.round_pracProb.shopping_plan.Main m = new codejam.y2008.round_pracProb.shopping_plan.Main(); 
        
    }
    
    /**
     * Year 2008
     * Practice round -- Beta
     * 
     */
    static void beta2008(String args[]) {

        /**
         * Problem 1.  
         * 
         * CLassifying triangles
         * 
         * Law of cosines
         */
       // codejam.y2008.round_beta.triangle_trilemma.Main m = new codejam.y2008.round_beta.triangle_trilemma.Main(); 
        
        /**
         * Problem 2
         * 
         * Longest increasing subsequence
         * Lexigraphic order
         */
       // codejam.y2008.round_beta.price_wrong.Main m = new codejam.y2008.round_beta.price_wrong.Main();
        
        /**
         * Problem 3.
         * 
         * Enumerating all shortest paths in a directed weighted graph.
         * Used Dijkstras and kept all previous instead of just one..
         */
        //codejam.y2008.round_beta.random_route.Main m = new codejam.y2008.round_beta.random_route.Main();
        
        /**
         * Problem 4 Hexagons
         * 
         * Tried using Floyd–Warshall all pairs, but was too slow.  Directly calculate hex distance
         * 
         * assignment problem
         */
        codejam.y2008.round_beta.hexagon_game.Main m = new codejam.y2008.round_beta.hexagon_game.Main();
        
        String[] files = Main.getFiles(m, args);
        for (String file : files) {
            log.info("Input file {}", file);

            Runner.goSingleThread(file, m, m);
          //Runner.go(file, m, m, 5);
        }       
        
    }
    
    static void practiceContest2008(String args[]) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        
        /**
         * Problem 1
         * Recurence relationship ; patterns
         */
        //codejam.y2008.round_pracContest.old_magician.Main m1 = new codejam.y2008.round_pracContest.old_magician.Main();
        
        /**
         * Problem 2
         * 
         * Finding minimum length for k squares to cover n points
         * 
         * Binary search
         * Bit manipulations -- set / all subsets
         * Dynamic programming
         * 
         * 
         */
        //codejam.y2008.round_pracContest.square_fields.Main m2 = new codejam.y2008.round_pracContest.square_fields.Main();
        
        
        codejam.y2008.round_pracContest.cycles.Main m = new
                codejam.y2008.round_pracContest.cycles.Main();
        
        
       // List<Pair<TestCaseInputScanner<AbstractInputData>,TestCaseHandler<AbstractInputData>>> l = Lists.newArrayList();
        
       // l.add(new ImmutablePair<TestCaseInputScanner<AbstractInputData>,TestCaseHandler<AbstractInputData> >(m,m));
       // l.add(new ImmutablePair<TestCaseInputScanner<AbstractInputData>,TestCaseHandler<AbstractInputData> >(m2,m2));
        
        
       // for(int i = 0; i < l.size(); ++i) {
            //String[] files = Main.getFiles(l.get(i).getLeft(), args);
        String[] files = Main.getFiles(m, args);
            for (String file : files) {
                log.info("Input file {}", file);
    
              //  Runner.goSingleThread(file, l.get(i).getLeft(), l.get(i).getValue());
                Runner.goSingleThread(file, m,m);
              //Runner.go(file, m, m, 5);
            }     
        //}
    }
    
    static void roundEMEA_2008(String args[]) {
        /**
         * 2008 emea semis -- problem 1
         * 
         * Geometry triangles -- rotation / translation / scaling of triangle. 
         * Finding a point that transforms to itself
         * 
         * matrix multiplication / transformations.  
         * 
         * Can always research more into eigen values / vectors
         * Implement remaining proposed solution.
         * 1. Used a solution space line intersection type solution,
         * 2. Proposed matrix manipulation solution         * 
         * 3.  idea -- Remains a third, using estimation 
         * 
         * bmerries insane solution uses complex numbers
         */
       codejam.y2008.round_emea.scaled_triangle.Main m = new codejam.y2008.round_emea.scaled_triangle.Main();
                
        /**
         * 2008 emea semis -- problem 2 
         * 
         * Given a fence, find best offers using maximum of 3 colors.
         * 
         * Proposed solution was a brute force / greedy scanline.
         * 
         * Used a BFS.  Compared to bmerries implementation of proposed 
         * solution (at least it looks like it), what we did is faster.
         * 
         * 
         */
        //codejam.y2008.round_emea.painting_fence.Main m = new codejam.y2008.round_emea.painting_fence.Main();
        
        /**
         * 2008 emea semis -- problem 3
         * 
         * Coloring tree such that any path of length 2 or 3 has different colors.
         * Chromatic number of a tree
         * 
         * 1. Contains the brute force / exponential way of computing chromatic polynomial
         * 2. Child first calculation
         * 3. A cleaner implementation using their solution
         */
       // codejam.y2008.round_emea.rainbow_trees.Main m = new codejam.y2008.round_emea.rainbow_trees.Main();
        
        /**
         * 2008 emea semis -- problem 3
         * 
         * State transitions / matrix multiplication / binary manipulation
         * 
         * 1.  Solved very similiar to proposed solution
         * 2.  Maybe could be improved using proposed, but seems OK.
         */
        //codejam.y2008.round_emea.bus_stops.Main m = new codejam.y2008.round_emea.bus_stops.Main();
        
        
       String[] files = Main.getFiles(m, args);
       for (String file : files) {
           log.info("Input file {}", file);

           Runner.goSingleThread(file, m, m);
         //Runner.go(file, m, m, 5);
       }   
    }
    
    static void roundFinal_2009(String args[]) {
        //Round Final -- 2009
        //codejam.y2009.round_4.year_more.Main m = new codejam.y2009.round_4.year_more.Main();
        
        //Both of these still a bit slow, especially min perimeter
        /**
         * Problem 2
         * 
         * Used proposed solution -- takes around 60 seconds
         * Best C++ answer takes around 20 seconds
         * 
         */
        codejam.y2009.round_final.min_perimeter.Main m = new codejam.y2009.round_final.min_perimeter.Main();
       
       //Using lattice paths to make a complex Dynamic programming DP
       // codejam.y2009.double_sort_grid.Main m = new codejam.y2009.double_sort_grid.Main();
       
        //Network flow
      // codejam.y2009.round_final.wifi_towers.Main m = new codejam.y2009.round_final.wifi_towers.Main();
       
        /**
         * 2009 finals problem 5.
         * 
         * The solution in Sol.java written by Vitaliy was annotated.  
         * 
         * The one todo that seems magical is the direction (up or down) of each path is assigned only once.
         * Perhaps it is due to the flipping / setting minimum code at the end of the main solve method.
         * 
         * And maybe try a bottom up, but meh...
         */
        //codejam.y2009.round_final.marbles.Sol m = new codejam.y2009.round_final.marbles.Sol();
      
       
       /**
        * 2009 Round Final
        * Problem 6
        * 
        * Triangle - Triangle intersection
        * Circle disk area
        * polar angles
        * 
        * Fast -- 400 ms for large.  
        * 
        * idea -- try approximation and line sweep suggested solutions
        */
       //codejam.y2009.round_final.lights.Main m = new codejam.y2009.round_final.lights.Main();
       
       
        String[] files = Main.getFiles(m, args);
        for (String file : files) {
            log.info("Input file {}", file);

         //   Runner.goSingleThread(file, m, m);
          Runner.go(file, m, m, 5);
        }    
    }
    
    static void round2_2010(String args[]) {
        //2010 2         
        /**
         * 2010 Round 2
         * Problem 1.
         * 
         * 1.  My solution -- a binary search to find the size that fits
         * 2.  A better solution -- calculate all symmetric points and use that
         * to try centers that would work
         */
        //codejam.y2010.round_2.diamond.Main m = new codejam.y2010.round_2.diamond.Main();
       //codejam.y2010.round_2.diamond.OldMain m = new codejam.y2010.round_2.diamond.OldMain();
       
       /**
        * 2010 Round 2
        * Problem 2
        * 
        * 1. DP on a tree but not memoized.  Looks like it follows the given solution too
        */
        //codejam.y2010.round_2.world_cup.Main m = new codejam.y2010.round_2.world_cup.Main();
       
        /**
         * Problem 3
         * 
         * Intersecting rectangles / connected components.
         * 
         * Y intercepts ; patters
         */
        //codejam.y2010.round_2.bacteria.Main m = new codejam.y2010.round_2.bacteria.Main(); 

       /**
        * Problem 4
        * Intersection of circles, overlapping area
        * 
        * 1.  Solution
        * 2.  Have TestInversions showing sample1, how intersection
        * of the inverted circles (lines) correspond to the intersection
        * of the circles.  See lecture notes pdf for more about
        * dual line to point / lower envelope and convex hull.
        *
        * Idea actually implement convex hull solution
        */
        codejam.y2010.round_2.goats.Main m = new codejam.y2010.round_2.goats.Main();
        
        String[] files = Main.getFiles(m, args);
        for (String file : files) {
            log.info("Input file {}", file);

            Runner.goSingleThread(file, m, m);
          //Runner.go(file, m, m, 5);
        }    
    }
    
    static void round2_2011(String args[]) {
        //2011 2
        
        /**
         * 2011  Round 2
         * Problem 1
         * 
         * Greedy algorithm, velocity/time
         */
        //codejam.y2011.round_2.airport_walkways.Main m = new codejam.y2011.round_2.airport_walkways.Main();
        
        /**
         * Problem 2 -- Spinning blade
         * Computational geometry
         * 
         * Computing center of mass along grid of squares.  
         * 1.  my solution using larger and larger squares
         * 2.  theirs which computes all rectangles sharing a corner with 0,0 and then using that to compute an arbitrary square
         * 
         *  Theirs is about 20-30% faster
         */
        //codejam.y2011.round_2.spinning_blade.Main m = new codejam.y2011.round_2.spinning_blade.Main();
        
        /**
         * 2011 Round 2
         * Problem 3
         * 
         * Prime factorization, LCM
         */
        //codejam.y2011.round_2.expensive_dinner.Main m = new codejam.y2011.round_2.expensive_dinner.Main();
        
        /**
         * Problem 4
         * 
         * Shortest paths ; interesting observation about
         * neighbors on  the shortest path.
         * 
         * 
         * 1.  Mine (Main), use a queue that favors number of planets threatened
         * 2.  Theirs (Solution), using the attribute that a threatened planet can only be adjacent to the 2 preceding nodes
         * to build a DP solution.
         * 
         * Their solution is about the same speed
         */
        codejam.y2011.round_2.ai_wars.Main m = new codejam.y2011.round_2.ai_wars.Main();
        //codejam.y2011.round_2.ai_wars.Solution m = new codejam.y2011.round_2.ai_wars.Solution();

        String[] files = Main.getFiles(m, args);
        for (String file : files) {
            log.info("Input file {}", file);

            Runner.goSingleThread(file, m, m);
          //Runner.go(file, m, m, 5);
        }      
    }
    
    static void roundFinal_2011(String args[]) {
        
        //Dynamic programming, combinatorics stars and bars counting (distributing n stars in k buckets )
        //codejam.y2011.round_final.runs.Main m = new codejam.y2011.round_final.runs.Main(); 
        
        //Simulation with a trick to do batch processing.  TODO look at proof via paths / tree justifying the algo
        //codejam.y2011.round_final.rains_atlantis.Main m = new codejam.y2011.round_final.rains_atlantis.Main();
        
        /**
         * 2011 final round
         * Problem 3
         * 1.  Creates a turning machine
         * 2.  idea -- Can optimize the number of iterations
         */
        //codejam.y2011.round_final.program_within.Main m = new codejam.y2011.round_final.program_within.Main();

        /**
         * 2011 Final
         * Problem 4
         * 1.  Used proposed solution -- TODO go through reasoning
         */
        //codejam.y2011.round_final.ace_in_hole.Main m = new codejam.y2011.round_final.ace_in_hole.Main();
        
        /**
         * Problem 5
         * 
         * Probability, expected value
         * Reducing nearly unbounded search space
         */
       codejam.y2011.round_final.google_royale.Main m = new codejam.y2011.round_final.google_royale.Main();
       
       String[] files = Main.getFiles(m, args);
       for (String file : files) {
           log.info("Input file {}", file);

           Runner.goSingleThread(file, m, m);
         //Runner.go(file, m, m, 5);
       }      
    }
    
    static void round3_2012(String args[]) {
        /**
         * 2012 Round 3
         * Problem 1
         * 
         * Probability Expected value until event happens.  see docs/2012/perfectGame.tex, sorting
         * 1.  Has a simulator / calculation of expected value that worked for the small
         * 2.  See solution explanation for the Greedy algorithm that works for Large
         * 
         * idea -- see if the comparison made can be translated to how it actually changes the overall
         * expected value
         */
        //codejam.y2012.round_3.perfect_game.Main m = new codejam.y2012.round_3.perfect_game.Main();
        
        /**
         * 2012 Round 3
         * Problem 2
         * 
         * Union find / Connected components
         * 
         * 1.  The check for a ring is clever, basically, if the C's are connected
         * but there are gaps (in the X's and Y's) such that they must have connected by making a loop
         */
        codejam.y2012.round_3.havannah.Main m = new codejam.y2012.round_3.havannah.Main(); 

        String[] files = Main.getFiles(m, args);
        for (String file : files) {
            log.info("Input file {}", file);

            Runner.goSingleThread(file, m, m);
          //Runner.go(file, m, m, 5);
        }
    }
    
    static void roundFinal_2012(String args[]) 
    {
        //2012 Final
        
        //Dijkstras using an indexed priority queue
        //codejam.y2012.round_final.zombie_smash.Main m = new codejam.y2012.round_final.zombie_smash.Main();
        
        //Probability, optimizing, state transitions, maybe matrix multiplication
        //codejam.y2012.round_final.upstairs_downstairs.Main m = new codejam.y2012.round_final.upstairs_downstairs.Main();
        
        /**
         * 2012 Final -- problem 3
         * 
         * Building constraints to find rectangles ; absolute value line
         * intersection.
         * 
         * 1. Used proposed solution
         * 2.  Can speed it up by not checking all rectangles ; but runtime
         * is already 1sec.  Eatmore's solution is instant.
         */
        //codejam.y2012.round_final.xeno_archaeology.Main m = new codejam.y2012.round_final.xeno_archaeology.Main(); 

        
        codejam.y2012.round_final.twirling_freedom.Main m = new codejam.y2012.round_final.twirling_freedom.Main();
        
        String[] files = Main.getFiles(m, args);
        for (String file : files) {
            log.info("Input file {}", file);

            Runner.goSingleThread(file, m, m);
          //Runner.go(file, m, m, 5);
        }
    }
    
    public static void main(String args[]) throws Exception {

        //africa(args);
        
        //beta2008(args);
        //practiceContest2008(args);
        
        roundFinal_2009(args);
        
      //  round2_2010(args);
        
        //round2_2011(args);        
       // roundFinal_2011(args);
        
       // round3_2012(args);
      //  roundFinal_2012(args);

       // Main m = null;
        
       
       
        
        /**
         * 2008 amer semis -- problem 1
         * Tree traversal depth first
         *
         * 1.  A reverse BFS search 
         */
       // codejam.y2008.round_amer.mixing.Main m = new codejam.y2008.round_amer.mixing.Main();
        
        /**
         *
         * 2008 amer semis -- problem 2
         * 
         * There are K keys, each corresponding to a binary bit position.  
         * As the sequence progresses, n is incremented, and those keys that
         * are 'activated' are added together.
         *  
         * Iteratively guessing a code sequence, binary / using incomplete information / assumptions
         * 
         * Run example to see more
         */
        // 
       //codejam.y2008.round_amer.code_sequence.Main m = new codejam.y2008.round_amer.code_sequence.Main();
        //codejam.y2008.round_amer.code_sequence.Main.example();
        
        /**
         * 2008 -- Round semis Amer
         * Maintain a list of N best paths ; Probability
         * 
         * Found solution to speed up (30X) considerably the execution speed using an array
         * instead of objects.  Also each node does not have it's own list of M paths
         */
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
        
        /**
         * Round 1C -- 2009 
         * Problem 3
         * Dynamic programming.
         * 
         * 1.  Proposed solution, top down
         * 2.  Also found LucaB's solution which is bottom up and like 10x faster
         */
         //codejam.y2009.round_1C.bribe_prisoner.Main m = new codejam.y2009.round_1C.bribe_prisoner.Main();
         
         //Round 2 -- 2009
        
         /**
          * Round 2 -- 2009
          * Problem 1
          * 
          * Number of swaps to put in order, can only swap adjacent.
          * "Disorder" count equals number of swaps. 
          */
         //codejam.y2009.round_2.crazy_rows.Main m = new codejam.y2009.round_2.crazy_rows.Main();
        
        //DP TODO bottom up
         //codejam.y2009.digging_problem.Main m = new codejam.y2009.digging_problem.Main();
        
        //Depth first search, augmenting path
         //codejam.y2009.round_2.stock_charts.Main m = new codejam.y2009.round_2.stock_charts.Main();
        
        //Hardcore circle geometry / polar coordinates
     //    codejam.y2009.round_2.watering_plants.Main m = new codejam.y2009.round_2.watering_plants.Main();
         
         //Round 3 -- 2009
        //Grid shortest path
         //codejam.y2009.sokoban.Main m = new codejam.y2009.sokoban.Main();
        
        //
        /**
         * Round 3 -- 2009
         * Problem 2
         * 
         * Summing a matrix 
         * 
         * Set multiplying, polynomial symbolic manipulation.  
         * 1. My solution, which is slow, using symbol polynomial manipulations
         * 2. A much faster solution based on proposed help / other competitors solutions
         */
       // codejam.y2009.round_3.alphabetomials.Main m = new codejam.y2009.round_3.alphabetomials.Main();
        
        // 3 coloring a particular graph (triangles)
        // codejam.y2009.round_3.football_team.Main m = new codejam.y2009.round_3.football_team.Main();
        
        //Counting ranges.  Had to use complex caching to solve in time.  TODO more comments and their solution
        // codejam.y2009.round_3.interesting_ranges.Main m = new codejam.y2009.round_3.interesting_ranges.Main();
         
     
         //2010 qual
         //Bignumber GCD
         //codejam.y2010.fair_warning.Main m = new codejam.y2010.fair_warning.Main();
         
         //Finding repeated state?  
         //codejam.y2010.theme_park.Main m = new codejam.y2010.theme_park.Main();
         
         //2010 1A
        //Grid rotation
         //codejam.y2010.rotate.Main m = new codejam.y2010.rotate.Main();
        
        //DP todo bottom up
         //codejam.y2010.smooth.Main m = new codejam.y2010.smooth.Main();
        
        /**
         * 2010 Round 1A
         * Problem 3
         * 
         * Combinatoric game, golden ratio
         */
        // codejam.y2010.round_1A.number_game.Main m = new codejam.y2010.round_1A.number_game.Main();
         
         //2010 1B
         
         //Tree
         //codejam.y2010.file_fix.Main m = new codejam.y2010.file_fix.Main();
         
         //Reasing through problem, speed physics
        // codejam.y2010.round_1B.chicks.Main m = new codejam.y2010.round_1B.chicks.Main();
         
       
        
         /**
          * Dynamic programming / Choose number / counting problem
          * 
          * 1.  Solved using top down
          * 2.  Solved again using bottom / up (difference in speed is negligible)
          * 
          * See BruteForce for a listing of the possible sets for n = 12, problem description is
          * not so clear (as a Set does not have order....)
          * 
          *  codejam.y2010.round_1B.rank_pure.BruteForce.showPattern(i);
          */
        // codejam.y2010.round_1B.rank_pure.Main m = new codejam.y2010.round_1B.rank_pure.Main();
         
         //2010 1C
         
         //codejam.y2010.load_testing.Main m = new codejam.y2010.load_testing.Main();         
         //codejam.y2010.rope_intranet.Main m = new codejam.y2010.rope_intranet.Main();
        
        //Finding rectangles efficiently
      //large a bit slow
        //codejam.y2010.round_1C.chess_boards.Main m = new codejam.y2010.round_1C.chess_boards.Main();

      
                  
         //2010 3
         
        //Used lattice to find random number generator points
         //codejam.y2010.round_3.rng.Main m = new codejam.y2010.round_3.rng.Main();
        
        /**
         * 2010 -- Round 3
         * Problem 2
         * 
         * Used BFS to find modulo length.
         */
       // codejam.y2010.round_3.boards.Main m = new codejam.y2010.round_3.boards.Main();
        
        //Did my own algo, used GAP class.  TODO use their solution
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
        
        //Intervals, Splitting interval into 2.  Binary search.  
        //codejam.y2010.round_final.travel_plan.Main m = new codejam.y2010.round_final.travel_plan.Main();
                
        /**
         * 2010 -- Round final          
         * Problem 5
         * 
         * Interesting DP problem ; loop detection
         */
        //codejam.y2010.round_final.ninjutsu.Main m = new codejam.y2010.round_final.ninjutsu.Main();
        
        /**
         * 2010 -- Round final
         * Problem 6
         * 
         * Symmetry, patters /topology
         * 
         * TODO optimize the connectedness checking
         */
       // codejam.y2010.round_final.ying_yang.Main m = new codejam.y2010.round_final.ying_yang.Main();
        
         //2011 1A
        
         //A in C++
         //TODO large is quite slow 14 secs -- 30 secs.  Can improve the algorithm
        // codejam.y2011.round_1A.killer_word.Main m = new codejam.y2011.round_1A.killer_word.Main();
        
        /**
         * 2011 Round 1A
         * 
         * Weighted / directed graph
         * 
         * Using DP to find max length.  Bottom up
         * would need a topologicial sort (reverse post order)
         * which is itself recursive, so it would defeat
         * the point of doing a bottom up DP...
         */
        // codejam.y2011.round_1A.dominion.Main m = new codejam.y2011.round_1A.dominion.Main();
         
         //2011 1B
         //A in C++
        /**
         * 2011 Round 1B
         * Problem 2
         * 
         * 1.  My solution, merging vendors who intersect 
         * 2.  idea -- they have a mathematical solution 
         * 3.  idea -- Binary search
         */
         //codejam.y2011.round_1B.hotdog_revenge.Main m = new codejam.y2011.round_1B.hotdog_revenge.Main();
        
        //Polygon breadth first search
         //codejam.y2011.round_1B.house_kittens.Main m = new codejam.y2011.round_1B.house_kittens.Main();
        
        //2011 1C
        //Easy grid search / replace
        //codejam.y2011.round_1C.square_tiles.Main m = new codejam.y2011.round_1C.square_tiles.Main();
        
        /**
         * 2011 Round 1C
         * 
         * problem 2
         * 
         * Straightforward Greedy algorithm. 
         */
       // codejam.y2011.round_1C.space_emergency.Main m = new codejam.y2011.round_1C.space_emergency.Main();
        
        /**
         * 2011 Round 1C
         * 
         * Problem 3
         * 
         * Finding a number that is divisible or divides a range of numbers
         * 
         * Uses GCD and LCM to find the answer, which is restricted to an interval
         */
        //codejam.y2011.round_1C.perfect_harmony.Main m = new codejam.y2011.round_1C.perfect_harmony.Main();

         
    

        //2011 round 3
        //A discrete binary search problem
       // codejam.y2011.round_3.irregular_cakes.Main m = new codejam.y2011.round_3.irregular_cakes.Main();
        
        //Greedy algorithm (used solution)
        //codejam.y2011.round_3.dire_straights.Main m = new codejam.y2011.round_3.dire_straights.Main();
        
        //
       
        
        
       
        
        //2012 qual
        //A,B,C  in c++
        //TODO a bit slow
       //  codejam.y2012.round_qual.hall_of_mirrors.Main m = new codejam.y2012.round_qual.hall_of_mirrors.Main();
        
        //2012 1A

        /**
         * 2012 Round 1A
         * Problem 1
         * 
         * Straightforward expected value / probability
         */
        //codejam.y2012.round_1A.password.Main m = new codejam.y2012.round_1A.password.Main();
        
        /**
         * 2012 Round 1A
         * Problem 2
         * 
         * Greedy algorithm
         */
      //  codejam.y2012.round_1A.kingdom_rush.Main m = new codejam.y2012.round_1A.kingdom_rush.Main();
        
        //Constraint problem
        //codejam.y2012.round_1A.cruise_control.Main m = new codejam.y2012.round_1A.cruise_control.Main();
        
        //2012 1B
        //Easier problem.  Did some trick that did not seem to correspond to their solution.  TODO doc/compare?
        //codejam.y2012.round_1B.safety_numbers.Main m = new codejam.y2012.round_1B.safety_numbers.Main();
        
        //BFS with an interesting state, leaving a cave
        //codejam.y2012.round_1B.tide.Main m = new codejam.y2012.round_1B.tide.Main();
         
         //Pigeon hole principal / birthday paradox
        //codejam.y2012.round_1B.equal_sums.Main m = new codejam.y2012.round_1B.equal_sums.Main();
        
        //2012 1C
        /**
         * 2012 round 1C
         * Problem 1
         * 
         * Finding multiple paths in a directed graph
         */ 
       // codejam.y2012.round_1C.diamond_inheritance.Main m = new codejam.y2012.round_1C.diamond_inheritance.Main();
         
         //Physics, velocity acceleration. Intersection parabola / lines
        //codejam.y2012.round_1C.out_of_gas.Main m = new codejam.y2012.round_1C.out_of_gas.Main();
         
         //Did this the hard way, LCS DP on steroids, could have done simpler using their soltion TODO 
        //codejam.y2012.round_1C.boxes.Main m = new codejam.y2012.round_1C.boxes.Main();
        
        //2012 2
         //Used a graph search problem.  Some proof to "never go backward strategy".  TODO understand a bit more their graph and
         //edge traversal strategy
        //codejam.y2012.round_2.swinging_wild.Main m = new codejam.y2012.round_2.swinging_wild.Main();
         
         //Placing circles in confined space ; used rectangles.  Geometry
        //codejam.y2012.round_2.aerobics.Main m = new codejam.y2012.round_2.aerobics.Main();
        
         //TODO implement their simpler approach
       // codejam.y2012.round_2.mountain_view.Main m = new codejam.y2012.round_2.mountain_view.Main();
        
        //Did not solve.  FSM intersection / dynamic programming.  TODO Look again why it works        
        //codejam.y2012.round_2.descending_dark.Main m = new codejam.y2012.round_2.descending_dark.Main();
        
        //2012 3
        
       
                

    }

    public static String[] getFiles(Object m, String[] args) {
        String[] files = {};
        if (m instanceof DefaultInputFiles) {
            files = ((DefaultInputFiles) m).getDefaultInputFiles();
        }
        
        if (args.length >= 1) {            
            files = args;
        }
        
        return files;
    }
    
}