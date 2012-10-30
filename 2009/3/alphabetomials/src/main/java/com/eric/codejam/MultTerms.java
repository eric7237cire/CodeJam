package com.eric.codejam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;

public class MultTerms extends AbstractTerm {

    private List<Term> terms;
    private int coeff;

    public List<Term> getTerms() {
        return Collections.unmodifiableList(terms);
    }

    MultTerms(String str) {
        this();

        for (int i = 0; i < str.length(); ++i) {
            terms.add(new VariableTerm("" + str.charAt(i)));
        }
    }

    MultTerms() {
        terms = new ArrayList<>();
        coeff = 1;
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
        if (mult instanceof CoefficientTerm) {
            coeff *= ((CoefficientTerm) mult).value;
            return;        
        }
        
        if (mult instanceof PowerTerm || mult instanceof VariableTerm) {
            terms.add(mult);
            return;
        }
        
        /*
        if (mult instanceof MultTerms) {
            coeff 
            terms.addAll( ((MultTerms) mult).terms);
        }*/
        
        throw new UnsupportedOperationException("mult");
    }

    @Override
    public void add(Term addTerm) {
        if (addTerm instanceof MultTerms) {
            MultTerms rhs = (MultTerms) addTerm;
            Preconditions.checkArgument(this.terms.equals(rhs.terms));
            coeff += rhs.coeff;
            return;
        }
        throw new UnsupportedOperationException("add");
    }

    @Override
    public Term simplify() {
        Term ret = null;
        
        if (terms.size() == 1 && coeff == 1) {
            return terms.get(0);
        }

        for (ListIterator<Term> li = terms.listIterator(); li.hasNext();) {
            Term t = li.next();
            
            if (t.equals(new CoefficientTerm(1))) {
                li.remove();
                continue;
            }
            
            Term r = t.simplify();
            if (r != null) {
                if (r instanceof CoefficientTerm) {
                    coeff *= ((CoefficientTerm) r).value;
                    li.remove();
                } else {
                    li.set(r);
                }
                ret = this;
            }
        }
        
        //Merge same degrees
        Map<VariableTerm, Integer> map = new HashMap<>();
        
        for (ListIterator<Term> li = terms.listIterator(); li.hasNext();) {
            Term term = li.next();
            
            VariableTerm var = null;
            int degree = 0;
            
            if (term instanceof VariableTerm) {
                var = (VariableTerm) term;
                degree = 1;
            } else if (term instanceof PowerTerm) {
                PowerTerm p = (PowerTerm) term;
                if (p.term instanceof VariableTerm) {
                    var = (VariableTerm) p.term;
                    degree = p.degree;
                }
            }
            
            if (var == null) {
                continue;
            }
            
            li.remove();
            Integer savedDegree = map.get(var);
            
            savedDegree = savedDegree == null ? 0 : savedDegree;
            
            map.put(var, savedDegree + degree);
        }
        
        for(Map.Entry<VariableTerm,Integer> ent : map.entrySet()) {
            if (ent.getValue() == 1) {
                terms.add(ent.getKey());
            } else {
                terms.add(new PowerTerm(ent.getKey(), ent.getValue()));
            }
        }
        
        Collections.sort(terms, new Polynomial.CompareTerm());
        
        return ret;
    }
    
    public int getCoeff() {
        return coeff;
    }

    @Override
    public int evaluate(Map<String, Integer> values) {
        int r = coeff;
        
        for(Term term : terms) {
            r *= term.evaluate(values);
        }
        
        return r;
    }

    @Override
    public String toString() {
        return (coeff == 1 ? "" : coeff) + StringUtils.join(terms, "*");
    }

}
