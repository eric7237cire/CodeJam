package com.eric.codejam;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class Polynomial extends AddTerms {
	Polynomial(String s) {
		super();
		String[] termStrList = s.split("\\+");
		
		for (String termStr : termStrList) {
			terms.add(new MultTerms(termStr));
		}
	}

	public void doSimplify() {
		while(simplify() != null) {
			
		}
	}
	
	public void substitute(Map<VariableTerm,Term> terms) {
		for(VariableTerm var : terms.keySet()) {
			substitute(var, new VariableTerm(var.name + "old"));
		}
		
		for(VariableTerm var : terms.keySet()) {
			substitute(new VariableTerm(var.name + "old"), terms.get(var));
		}
    }
	
	public static class CompareTerm implements Comparator<Term> {

		@Override
		public int compare(Term lhs, Term rhs) {
			
			return lhs.toString().compareTo(rhs.toString());
		}
		
	}
}
