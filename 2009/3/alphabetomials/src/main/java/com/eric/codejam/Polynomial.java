package com.eric.codejam;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Polynomial {
    List<MultGroup> terms;
    
    Polynomial(String s) {
        String[] termStrList = s.split("\\+");
        terms = new ArrayList<>();
        for(String termStr : termStrList) {
            terms.add(new MultGroup(termStr));
        }
    }
    
    public void substitute(VariableTerm old, Term newTerm) {
        for(MultGroup grp : terms) {
            grp.substitute(old, newTerm);
        }
    }
    
    @Override
    public String toString() {
        return StringUtils.join(terms, " + ");
    }
}
