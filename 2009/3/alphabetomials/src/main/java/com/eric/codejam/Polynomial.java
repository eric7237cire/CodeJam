package com.eric.codejam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

public class Polynomial {
    AddTerms addTerms;
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
	    while(didSimp) {
	        didSimp = false;
	        Term sim = addTerms.simplify();
	        if (sim != null) {
	            addTerms = (AddTerms) sim;
	            didSimp = true;
	        }
	    }
		
		//Collections.sort(terms, new Polynomial.CompareTerm());
	}
	
	public void substitute(Map<VariableTerm,Term> terms) {
		for(VariableTerm var : terms.keySet()) {
			addTerms.substitute(var, new VariableTerm(var.getName() + "old"));
		}
		
		for(VariableTerm var : terms.keySet()) {
		    addTerms.substitute(new VariableTerm(var.getName() + "old"), terms.get(var));
		}
    }
	
	public void substitute(VariableTerm old, Term newTerm) {
	    addTerms.substitute(old, newTerm);
	}
	

    public int evaluate(Map<String, Integer> values) {
        return addTerms.evaluate(values);
    }
	
	public void addSelf(Term term) {
	    List<Term> terms = new ArrayList<>();
	    terms.addAll(addTerms.getTerms());
	    terms.add(term);
	    addTerms = new AddTerms(terms);
	}
	public void addSelf(Polynomial term) {
        List<Term> terms = new ArrayList<>();
        terms.addAll(addTerms.getTerms());
        terms.addAll(term.addTerms.getTerms());
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
	
	public static class CompareTerm implements Comparator<Term> {

		@Override
		public int compare(Term lhs, Term rhs) {
			
		    if(lhs instanceof MultTerms && rhs instanceof MultTerms) {
		        return compareMul((MultTerms)lhs, (MultTerms)rhs);
		    }
		    
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
             }
		}
		
        public int compareMul(MultTerms lhs, MultTerms rhs) {
            ComparisonChain cc = ComparisonChain.start();

            int i = 0;
            int j = 0;

            while (i < lhs.getTerms().size() && j < rhs.getTerms().size()) {
                Term lhsTerm = lhs.getTerms().get(i);
                Term rhsTerm = rhs.getTerms().get(j);

                if (lhsTerm instanceof CoefficientTerm) {
                    ++i;
                    continue;
                }
                if (rhsTerm instanceof CoefficientTerm) {
                    ++j;
                    continue;
                }
                cc = cc.compare(lhs.getTerms().get(i), rhs.getTerms().get(j),
                        new CompareTerm());
                ++i;
                ++j;
            }

            cc = cc.compare(lhs.getTerms().size(), rhs.getTerms().size());

            return cc.result();
        }
	}

    @Override
    public String toString() {
        return addTerms.toString();
    }
	
	
}
