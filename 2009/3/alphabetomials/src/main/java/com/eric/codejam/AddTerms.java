package com.eric.codejam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;


public class AddTerms extends AbstractTerm {
    
    List<Term> terms;
    
    AddTerms() {
        terms = new ArrayList<>();
    }
    @Override
    public void substitute(VariableTerm old, Term newTerm) {
        for(ListIterator<Term> li = terms.listIterator(); li.hasNext();) {
            Term t = li.next();
            if (t.equals(old)) {
                li.set(newTerm);
            } else {
                t.substitute(old, newTerm);
            }
        }
    }

    @Override
    public void multiply(Term mult) {
        throw new UnsupportedOperationException("mult");        
    }
    
    @Override
    public int evaluate(Map<String, Integer> values) {
        int r = 0;
        
        for(Term term : terms) {
            r += term.evaluate(values);
        }
        
        return r;
    }

    @Override
    public void add(Term addTerm) {
        if (addTerm instanceof AddTerms) {
            this.terms.addAll( ((AddTerms) addTerm).terms ); 
        } else {
            this.terms.add(addTerm);
        }
    }

    @Override
    public Term simplify() {
        Term ret = null;
        
        List<Term> toAdd = new ArrayList<>();
        for(ListIterator<Term> li = terms.listIterator(); li.hasNext();) {
            Term t = li.next();
            
            if (t instanceof AddTerms) {
                toAdd.addAll( ((AddTerms) t).terms );
                li.remove();
                continue;
            }
            Term r = t.simplify();
            if (r != null) {
                if (r instanceof AddTerms) {
                    li.remove();
                    toAdd.addAll( ((AddTerms) r).terms ); 
                } else {
                    li.set(r);
                }
                
                ret = this;
                continue;
            }
        }
        
        this.terms.addAll(toAdd);
        
        for(ListIterator<Term> li = terms.listIterator(); li.hasNext(); ) {
            Term term = li.next();
            if (!(term instanceof MultTerms)) {
                continue;
            }
            MultTerms multTerm = (MultTerms) term;
            
            for(ListIterator<Term> innerLi = terms.listIterator(li.nextIndex()); innerLi.hasNext(); ) {
                Term innerTerm = innerLi.next();
                if (!(innerTerm instanceof MultTerms)) {
                    continue;
                }
                MultTerms innerMultTerm = (MultTerms) innerTerm;
                
                if (multTerm.getTerms().equals(innerMultTerm.getTerms())) {
                    li.remove();
                    innerMultTerm.add(multTerm);
                    break;
                }
            }
        }
        
        
        
        return ret;
    }
    
    @Override
    public String toString() {
        return StringUtils.join(terms, " + ");        
    }
}
