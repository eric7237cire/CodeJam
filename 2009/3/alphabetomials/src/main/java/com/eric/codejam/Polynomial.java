package com.eric.codejam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

public class Polynomial extends AddTerms {
	Polynomial(String s) {
		super();
		String[] termStrList = s.split("\\+");
		
		for (String termStr : termStrList) {
			terms.add(new MultTerms(termStr));
		}
	}
	
	Polynomial() {
	    super();
	}

	public void doSimplify() {
		while(simplify() != null) {
			
		}
		
		Collections.sort(terms, new Polynomial.CompareTerm());
	}
	
	public void substitute(Map<VariableTerm,Term> terms) {
		for(VariableTerm var : terms.keySet()) {
			substitute(var, new VariableTerm(var.name + "old"));
		}
		
		for(VariableTerm var : terms.keySet()) {
			substitute(new VariableTerm(var.name + "old"), terms.get(var));
		}
    }
	
	static int getDegree(Term term) {
	    if (term instanceof PowerTerm) {
            PowerTerm p = (PowerTerm) term;
            return p.degree;
	    }
	    
	    return 1;
	}
	
	static String getVarname(Term term) {
	    VariableTerm var = null;
	
	    if (term instanceof VariableTerm) {
            var = (VariableTerm) term;
            return var.name;
        } else if (term instanceof PowerTerm) {
            PowerTerm p = (PowerTerm) term;
            if (p.term instanceof VariableTerm) {
                var = (VariableTerm) p.term;                
            }
            return var.name;
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
		    ComparisonChain cc =
		            ComparisonChain.start();
		    
		    
		        for(int i = 0; i < lhs.getTerms().size() && i < rhs.getTerms().size(); ++i) {
		            cc = cc.compare(lhs.getTerms().get(i), rhs.getTerms().get(i), new CompareTerm());
		        }
		    
		        cc = cc.compare(lhs.getTerms().size(), rhs.getTerms().size());
		        
		    return cc.result();
		}
	}
}
