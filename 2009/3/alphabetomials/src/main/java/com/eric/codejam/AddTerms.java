package com.eric.codejam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;


public class AddTerms extends AbstractTerm {
    
    final private List<Term> terms;
    
    public AddTerms(List<Term> args) {
        List<Term> terms = new ArrayList<>();
        terms.addAll(args);
        Collections.sort(terms, new Polynomial.CompareTerm());
        
        this.terms = ImmutableList.copyOf(terms);
        
    }
    public AddTerms(Term... args) {
        List<Term> terms = new ArrayList<>();
        terms.addAll(Arrays.asList(args));
        Collections.sort(terms, new Polynomial.CompareTerm());
        
        this.terms = ImmutableList.copyOf(terms);
        
    }
    
    @Override
    public Term substitute(VariableTerm old, Term newTerm) {
        List<Term> terms = new ArrayList<>(this.terms);
        
        for (ListIterator<Term> li = terms.listIterator(); li.hasNext();) {
            Term t = li.next();
            if (t.equals(old)) {
                li.set(newTerm);
                continue;
            } 
            
            Term sub = t.substitute(old, newTerm);
            if (sub != null) {
                li.set(sub);
                continue;
            }
        }
        
        return new AddTerms(terms);
    }
    
    @Override
    public AddTerms substitute(Map<VariableTerm, Term> termsToSub) {
        List<Term> terms = new ArrayList<>(this.terms);
        
        for (ListIterator<Term> li = terms.listIterator(); li.hasNext();) {
            Term t = li.next();
            Term value = termsToSub.get(t);
            if (value != null) {
                li.set(value);
                continue;
            } 
            
            Term sub = t.substitute(termsToSub);
            if (sub != null) {
                li.set(sub);
                continue;
            }
        }
        
        return new AddTerms(terms);
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
    public Term simplify() {
                
        
        List<Term> simTerms = new ArrayList<>();
        simTerms.addAll(getTerms());
        boolean hasSimp = false;
        
        if (terms.size() == 1 ) {
            return terms.get(0);
        }
        
        List<Term> toAdd = new ArrayList<>();
        
        // Simplify any sub elements
        for (ListIterator<Term> li = simTerms.listIterator(); li.hasNext();) {
            Term t = li.next();
            
            if (t instanceof AddTerms) {
                li.remove();
                toAdd.addAll(((AddTerms) t).getTerms());
                continue;
            }

            Term r = t.simplify();
            if (r != null) {
            
                li.set(r);
                hasSimp = true;
            }
        }
        
        if (!toAdd.isEmpty()) {
            simTerms.addAll(toAdd);
            hasSimp = true;
        }
        
        boolean found = true;
        while (found) {
            found = false;

            for (int i = 0; i < simTerms.size(); ++i) {
                for (int j = i + 1; j < simTerms.size(); ++j) {

                    Term lhs = simTerms.get(i);
                    Term rhs = simTerms.get(j);
                    Term replacement = null;
                    
                    if (lhs.canAdd(rhs)) {
                        replacement = lhs.add(rhs);                        
                    } else if (rhs.canAdd(lhs)) {
                        replacement = rhs.add(lhs);
                    } else {
                        continue;
                    }
                    
                    
                    
                    Preconditions.checkState(replacement != null);
                    simTerms.remove(j);
                    simTerms.remove(i);

                    simTerms.add(replacement);
                    found = true;
                    hasSimp = true;
                    break;
                
                }

                if (found) {
                    break;
                }
            }

        }
       

       if (hasSimp) {
           return new AddTerms(simTerms);
       }

        return null;
        
        
    }
    
    @Override
    public boolean canAdd(Term rhs) {
        return rhs.canAddAsRhs(this);
    }
    @Override
    public Term add(Term rhs) {
        return rhs.addAsRhs(this);
    }
    
    @Override
    public boolean canMultiply(Term rhs) {
        //concrete lhs
        return rhs.canMultiplyAsRhs(this);
    }
    @Override
    public Term multiply(Term rhs) {
        //concrete lhs
        return rhs.multiplyAsRhs(this);
    }
    public Term multiplyAsRhsImpl(Term lhs) {
        List<Term> terms = new ArrayList<>();
        for(Term term : getTerms()) {
            List<Term> mTerms = new ArrayList<>();
            mTerms.add(lhs);
            mTerms.add(term);
            MultTerms m = new MultTerms(mTerms);
            terms.add(m);
        }

        return new AddTerms(terms);
    }
    @Override
    public Term multiplyAsRhs(MultTerms lhs) {
        return multiplyAsRhsImpl(lhs);
    }
   
   
    @Override
    public boolean canMultiplyAsRhs(AddTerms lhs) {
        return true;
    }
    @Override
    public Term multiplyAsRhs(AddTerms lhs) {

        List<Term> terms = new ArrayList<>();
        for(Term outerTerm : getTerms()) {
            for(Term innerTerm : lhs.getTerms()) {
                List<Term> mTerms = new ArrayList<>();
                mTerms.add(innerTerm);
                mTerms.add(outerTerm);
                MultTerms m = new MultTerms(mTerms);
                terms.add(m);
            }
        }
        return new AddTerms(terms);

    }
    @Override
    public boolean canMultiplyAsRhs(MultTerms lhs) {
        return true;
    }
    @Override
    public Term multiplyAsRhs(CoefficientTerm lhs) {
        return multiplyAsRhsImpl(lhs);
    }
    @Override
    public Term multiplyAsRhs(VariableTerm lhs) {
        return multiplyAsRhsImpl(lhs);
    }
    @Override
    public boolean canMultiplyAsRhs(PowerTerm lhs) {
        return true;
    }
    @Override
    public Term multiplyAsRhs(PowerTerm lhs) {
        return multiplyAsRhsImpl(lhs);
    }
    @Override
    public boolean canMultiplyAsRhs(VariableTerm lhs) {
        return true;
    }
    @Override
    public boolean canMultiplyAsRhs(CoefficientTerm lhs) {
        return true;
    }
    
    @Override
    public boolean canAddAsRhs(AddTerms lhs) {
        return true;
    }
    @Override
    public Term addAsRhs(AddTerms lhs) {
        List<Term> terms = new ArrayList<>();
        terms.addAll(lhs.getTerms());
        terms.addAll(this.getTerms());
        return new AddTerms(terms);
    }
    public List<Term> getTerms() {
        return terms;
    }
    @Override
    public String toString() {
        return "(" + StringUtils.join(terms, " + ") + ")";        
    }
}
