package codejam.y2011.round_final.program_within;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;




public class Simulator
{
    final static Logger log = LoggerFactory.getLogger(Simulator.class);
    

    /**
     * Location ==> Mark
     */
    Map<Integer, Integer> tape;
    int currentState;
    int currentLocation;
    
    int MAX = 10000;
    
    List<Rule> rules;
    
    public int printStateLimit = 20;
    
    public Simulator(List<Rule> rules) {
        tape = Maps.newHashMap();
        
        for(int i = -MAX; i <= MAX; ++i)
            tape.put(i, 0);
        
        currentLocation = 0;
        currentState = 0;
        
        this.rules = rules;
    }
    
    static public Simulator fromStr(String rulesStr) {
        Scanner scanner = new Scanner(rulesStr);
        
        int nRules = scanner.nextInt();
        
        List<Rule> rules = Lists.newArrayList();
        
        for(int r = 0; r < nRules; ++r) {
            Rule rule = new Rule();
            
            rule.state = scanner.nextInt();
            rule.mark = scanner.nextInt();
            
            scanner.next();
            
            rule.Direction = scanner.next().charAt(0);
            
            if (rule.Direction != 'R') {
                rule.newState = scanner.nextInt();
                rule.newMark = scanner.nextInt();
                
            } else {
                
            }
            
            rules.add(rule);
        }
        
        scanner.close();
        return new Simulator(rules);
        
        
    }
    
    public Integer go() {
        try {
        for(int x = 1; x <= 150000; ++x) {
           // log.debug("Step {}", x);
            Integer release = goOneStep();
            if (release != null) {
                 log.debug("Step {}", x);
                return release;
            }
            
            //printState();
        }
        } catch (Exception ex) {
            log.debug(ex.toString());
            return -187;
        }
        
        return -999;
    }
    
    void printState() {
        StringBuffer sb = new StringBuffer();
        StringBuffer index = new StringBuffer();
        
        int fieldWidth = 7;
        
        for(int i = 0; i <= printStateLimit; ++i) {
            sb.append( StringUtils.leftPad(tape.get(i).toString(), fieldWidth, ' ') );
            index.append( StringUtils.leftPad(Integer.toString(i), fieldWidth, ' ') );
        }
        
        log.debug(index.toString());
        log.debug(sb.toString());
        
    }
    /**
     * 
     * @return null or location robot blew up and dropped cake
     */
    public Integer goOneStep() throws Exception {
        
        
        final int currentMark = tape.get(currentLocation);
        
        Iterator<Rule> matchingRules =  Iterables.filter(rules, new Predicate<Rule>() {
            public boolean apply(Rule rule) {
                if (rule.mark == currentMark && rule.state == currentState)
                    return true;
                
                return false;
            }
            }).iterator();
        
        
        if (!matchingRules.hasNext())
            throw new Exception("Confused, ate cake.  Current loc: " + currentLocation +
                " Current state " + currentState);
        
        Rule rule = matchingRules.next();
        
        if (matchingRules.hasNext())
            throw new Exception("Misbehave, destory cake");
        
        if (rule.Direction =='R') {
            return currentLocation;
        }
        
        tape.put(currentLocation, rule.newMark);
        
        currentState = rule.newState;
        
        if (rule.Direction == 'E')
            currentLocation ++;
        
        else if (rule.Direction == 'W')
            currentLocation--;
        else
            throw new Exception("Invalid direction");
        
        return null;
    }

}
