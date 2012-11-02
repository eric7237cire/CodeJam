package com.eric.codejam;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

public class Polynomial {
    Term addTerms;
	Polynomial(String s) {
		super();
		List<Term> terms = new ArrayList<>();
		String[] termStrList = s.split("\\+");
		
		for (String termStr : termStrList) {
			terms.add(new MultTerms(termStr));
		}
		
		addTerms = new AddTerms(terms);
	}
	
	Polynomial() {
	    addTerms = new AddTerms();
	}

	public void doSimplify() {
	    boolean didSimp = true;
	    int stop = 0;
	    while(didSimp) {
	        stop++;
	        Preconditions.checkState(stop < 100);
	        didSimp = false;
	        Term sim = addTerms.simplify();
	        if (sim != null) {
	            addTerms = sim;
	            didSimp = true;
	        }
	    }
		
		//Collections.sort(terms, new Polynomial.CompareTerm());
	}
	
	public void substitute(Map<VariableTerm,Term> terms) {
		addTerms = addTerms.substitute(terms);
    }
	
	public void substituteVals(Map<String, Integer> terms) {
	    Map<VariableTerm, Term> subs = new HashMap<>();
	    
	    
        for(Map.Entry<String, Integer> entry : terms.entrySet()) {
            subs.put(new VariableTerm(entry.getKey()), new CoefficientTerm(entry.getValue())); 
        }
        
        substitute(subs);
    }
	
	public void substitute(VariableTerm old, Term newTerm) {
	    addTerms = addTerms.substitute(old, newTerm);
	}
	

    public int evaluate(Map<String, Integer> values) {
        return addTerms.evaluate(values);
    }
	
	public void addSelf(Term term) {
	    List<Term> terms = new ArrayList<>();
	    terms.add(addTerms);
	    terms.add(term);
	    addTerms = new AddTerms(terms);
	}
	public void addSelf(Polynomial term) {
        List<Term> terms = new ArrayList<>();        
        terms.add(addTerms);
        terms.add(term.addTerms);
        addTerms = new AddTerms(terms);
    }
	static int getDegree(Term term) {
	    if (term instanceof PowerTerm) {
            PowerTerm p = (PowerTerm) term;
            return p.getDegree();
	    }
	    
	    return 1;
	}
	
	static String getVarname(Term term) {
	    VariableTerm var = null;
	
	    if (term instanceof VariableTerm) {
            var = (VariableTerm) term;
            return var.getName();
        } else if (term instanceof PowerTerm) {
            PowerTerm p = (PowerTerm) term;
            if (p.getTerm() instanceof VariableTerm) {
                var = (VariableTerm) p.getTerm();
                return var.getName();
            }
            
        } else if (term instanceof MultTerms) {
            MultTerms m = (MultTerms) term;
            return getVarname(m.getTerms().get(0));
        }
	    
        return null;
    }
	
	public static class MultCompareTerm implements Comparator<Term> {

        @Override
        public int compare(Term lhs, Term rhs) {
            return ComparisonChain.start()
                    .compare(lhs.getCoefPart(), rhs.getCoefPart(), Ordering.natural().nullsLast())
                    .compare(lhs.getFirstNonCoefPart(), rhs.getFirstNonCoefPart(), Ordering.natural().nullsFirst())
                    .compare(rhs.getDegree(), lhs.getDegree())                    
                    .result();
        }
	}
	
	public static class CompareTerm implements Comparator<Term> {

		@Override
		public int compare(Term lhs, Term rhs) {
		    return ComparisonChain.start()
		            .compare(lhs.getFirstNonCoefPart(), rhs.getFirstNonCoefPart(), Ordering.natural().nullsFirst())
		            .compare(rhs.getDegree(), lhs.getDegree())
		            .compare(lhs.getCoefPart(), rhs.getCoefPart(), Ordering.natural().nullsFirst())
		            .result();
			/*
		    if(lhs instanceof MultTerms && rhs instanceof MultTerms) {
		        return compareMul((MultTerms)lhs, (MultTerms)rhs);
		    }(
		    
	         String lhsVar = getVarname(lhs);
             String rhsVar = getVarname(rhs);
             int lhsDegree = getDegree(lhs);
             int rhsDegree = getDegree(rhs);
             
             int cc = ComparisonChain.start()
                     .compare(lhsVar, rhsVar, Ordering.natural().nullsFirst())
                     .compare(rhsDegree, lhsDegree).result();
		    
             if (cc == 0) {
                 return lhs.toString().compareTo(rhs.toString());
             } else {
                 return cc;
             }*/
		}
		
        public int compareMul(MultTerms lhs, MultTerms rhs) {
            ComparisonChain cc = ComparisonChain.start();

            int i = 0;
            int j = 0;
            int lhsCoef = 1;
            int rhsCoef = 1;

            while (i < lhs.getTerms().size() && j < rhs.getTerms().size()) {
                Term lhsTerm = lhs.getTerms().get(i);
                Term rhsTerm = rhs.getTerms().get(j);

                if (lhsTerm instanceof CoefficientTerm) {
                    ++i;
                    lhsCoef = ((CoefficientTerm) lhsTerm).getValue();
                    continue;
                }
                if (rhsTerm instanceof CoefficientTerm) {
                    ++j;
                    rhsCoef = ((CoefficientTerm) rhsTerm).getValue();
                    continue;
                }
                cc = cc.compare(lhs.getTerms().get(i), rhs.getTerms().get(j),
                        new CompareTerm());
                ++i;
                ++j;
            }

            cc = cc.compare(lhsCoef, rhsCoef);
            cc = cc.compare(i, j);
            cc = cc.compare(lhs.getTerms().size(), rhs.getTerms().size());

            return cc.result();
        }
	}

    @Override
    public String toString() {
        String s = addTerms.toString();
        return addTerms instanceof AddTerms ? s.substring(1, s.length() - 1) : s;
    }
	
	
}
