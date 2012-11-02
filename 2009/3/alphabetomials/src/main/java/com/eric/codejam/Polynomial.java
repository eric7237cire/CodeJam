package com.eric.codejam;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;

public class Polynomial {
    Term addTerms;
	Polynomial(String s) {
		super();
		List<Term> terms = new ArrayList<>();
		String[] termStrList = s.split("\\+");
		
		for (String termStr : termStrList) {
			terms.add(parseTerms(termStr));
		}
		
		addTerms = new AddTerms(terms);
	}
	

    static Term parseBinomialTerm(String[] strList) {

        Pattern binoPat = Pattern
                .compile("\\(([^\\+]+)\\s*\\+\\s*([^\\+]+)\\)(.*)");
        Pattern expPat = Pattern.compile("\\^?(\\d+)(.*)");
        Matcher m = binoPat.matcher(strList[0]);
        if (m.matches()) {
            String v1 = m.group(1).trim();
            String v2 = m.group(2).trim();
            AddTerms bt = new AddTerms(new VariableTerm(v1),
                    new VariableTerm(v2));

            strList[0] = m.group(3);

            Matcher expMat = expPat.matcher(strList[0]);
            if (expMat.matches()) {
                String exp = expMat.group(1);
                strList[0] = expMat.group(2);
                PowerTerm pt = new PowerTerm(bt, Integer.parseInt(exp));
                return pt;
            } else {
                return bt;
            }
        }

        return null;
    }

    static Term parsePowerVarTerm(String[] strList) {
        Pattern varExpPat = Pattern
                .compile("([a-zA-Z_]+\\d*)\\^(\\d+)(.*?)");
        
        Matcher m = varExpPat.matcher(strList[0]);
        if (m.matches()) {
            strList[0] = m.group(3);
            return new PowerTerm(new VariableTerm(m.group(1)),
                    Integer.parseInt(m.group(2)));
        }

        return null;
    }
    
    static Term parseVarTerm(String[] strList) {
        Pattern varPat = Pattern.compile("([a-zA-Z](?:_[a-zA-Z\\d]+){0,1}\\d*)(.*?)");
        Matcher m = varPat.matcher(strList[0]);
        if (m.matches()) {
            strList[0] = m.group(2);
            return new VariableTerm(m.group(1));
        }

        return null;
    }
    
    static Term parseCoeffTerm(String[] strList) {
        Pattern varPat = Pattern.compile("(\\d+)(.*?)");
        Matcher m = varPat.matcher(strList[0]);
        if (m.matches()) {
            strList[0] = m.group(2);
            return new CoefficientTerm(Integer.parseInt(m.group(1)));
        }

        return null;

    }

	 static Term parseTerms(String str) {
	        ArrayList<Term> terms = new ArrayList<>();

	        String[] strList = new String[] { str };
	        while (!strList[0].isEmpty()) {
	            str = strList[0];

	            strList[0] = strList[0].trim().replaceFirst("^\\*", "");
	            Term t = parseBinomialTerm(strList);

	            if (t != null) {
	                terms.add(t);
	            }
	            
	            t = parsePowerVarTerm(strList);

	            if (t != null) {
	                terms.add(t);
	            }
	            
	            t = parseVarTerm(strList);

	            if (t != null) {
	                terms.add(t);
	            }
	            
	            t = parseCoeffTerm(strList);
	            
	            if (t != null) {
	                terms.add(t);
	            }

	            Preconditions.checkState(!StringUtils.equals(str, strList[0]));
	            Preconditions.checkState(str.length() > strList[0].length());
	        }

	        
	        return MultTerms.buildMultTerm(terms);
	        
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
