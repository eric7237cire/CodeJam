package com.eric.codejam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

public class MultTerms extends AbstractTerm {

    private List<Term> terms;

    public List<Term> getTerms() {
        return terms;
    }
    
    MultTerms(Term... args) {
        terms = new ArrayList<>();
        terms.addAll(Arrays.asList(args));
        Collections.sort(terms, new Polynomial.CompareTerm());
        
        terms = ImmutableList.copyOf(terms);
        
    }
    MultTerms(List<Term> args) {
        terms = new ArrayList<>();
        terms.addAll(args);
        Collections.sort(terms, new Polynomial.CompareTerm());
        
        terms = ImmutableList.copyOf(terms);
        
    }

    static Term parseBinomialTerm(String[] strList) {

        Pattern binoPat = Pattern
                .compile("\\(([^\\+]+)\\s*\\+\\s*([^\\+]+)\\)(.*)");
        Pattern expPat = Pattern.compile("\\^?(\\d+)(.*)");
        Matcher m = binoPat.matcher(strList[0]);
        if (m.matches()) {
            String v1 = m.group(1).trim();
            String v2 = m.group(2).trim();
            BinomialTerm bt = new BinomialTerm(new VariableTerm(v1),
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
        Pattern varPat = Pattern.compile("([a-zA-Z_]+\\d*)(.*?)");
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

    MultTerms(String str) {
        this();

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

    }

    MultTerms() {
        terms = new ArrayList<>();
    }

    @Override
    public void substitute(VariableTerm old, Term newTerm) {
        List<Term> terms = new ArrayList<>(this.terms);
        
        for (ListIterator<Term> li = terms.listIterator(); li.hasNext();) {
            Term t = li.next();
            if (t.equals(old)) {
                li.set(newTerm);
            } else {
                t.substitute(old, newTerm);
            }
        }
        
        this.terms = terms;
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
        terms.addAll(getTerms());
        terms.add(lhs);
        return new MultTerms(terms);
    }

    

    @Override
    public Term multiplyAsRhs(CoefficientTerm lhs) {
        return multiplyAsRhsImpl(lhs);
    }

    @Override
    public boolean canMultiplyAsRhs(CoefficientTerm lhs) {
        return true;
    }

    @Override
    public Term multiplyAsRhs(VariableTerm lhs) {
        return multiplyAsRhsImpl(lhs);
    }

    @Override
    public boolean canMultiplyAsRhs(VariableTerm lhs) {
        return true;
    }

    @Override
    public boolean canMultiplyAsRhs(MultTerms lhs) {
        return true;
    }

    @Override
    public Term multiplyAsRhs(MultTerms lhs) {
        List<Term> terms = new ArrayList<>();
        terms.addAll(getTerms());
        terms.addAll(lhs.getTerms());
        return new MultTerms(terms);
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
    public Term simplify() {
        List<Term> simTerms = new ArrayList<>();
        simTerms.addAll(getTerms());
        boolean hasSimp = false;

        // Distribute coefficient
        if (terms.size() == 1 && !(terms.get(0) instanceof PowerTerm)) {
            return terms.get(0);
        }

        // Simplify any sub elements
        for (ListIterator<Term> li = simTerms.listIterator(); li.hasNext();) {
            Term t = li.next();

            Term r = t.simplify();
            if (r != null) {
            
                li.set(r);
                hasSimp = true;
            }
        }

        
        
        boolean found = true;
        while (found) {
            found = false;

            for (int i = 0; i < simTerms.size(); ++i) {
                for (int j = i + 1; j < simTerms.size(); ++j) {

                    Term lhs = simTerms.get(i);
                    Term rhs = simTerms.get(j);
                    Term replacement = null;
                    
                    if (lhs.canMultiply(rhs)) {
                        replacement = lhs.multiply(rhs);
                        
                    } else if (rhs.canMultiply(lhs)) {
                        replacement = rhs.multiply(lhs);
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
           return new MultTerms(simTerms);
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
    public Term addAsRhs(MultTerms lhs) {
        
        List<Term> lhsTerms = null;
        List<Term> rhsTerms = null;
        int coeffLhs = 1;
        int coeffRhs = 1;
        if (lhs.getTerms().get(0) instanceof CoefficientTerm) {
            lhsTerms = new ArrayList<>(lhs.getTerms().subList(1, lhs.getTerms().size()));
            coeffLhs = ((CoefficientTerm) lhs.getTerms().get(0)).getValue();
        } else {
            lhsTerms = new ArrayList<>(lhs.getTerms());
        }
        
        MultTerms rhs = this;
        if (rhs.getTerms().get(0) instanceof CoefficientTerm) {
            rhsTerms = rhs.getTerms().subList(1, rhs.getTerms().size());
            coeffRhs = ((CoefficientTerm) rhs.getTerms().get(0)).getValue();
        } else {
            rhsTerms = rhs.getTerms();
        }
        Preconditions.checkArgument(lhsTerms.equals(rhsTerms));
        lhsTerms.add(0, new CoefficientTerm(coeffLhs + coeffRhs));
        return new MultTerms(lhsTerms);
    }

    @Override
    public boolean canAddAsRhs(MultTerms lhs) {
        List<Term> lhsTerms = null;
        List<Term> rhsTerms = null;
        if (lhs.getTerms().get(0) instanceof CoefficientTerm) {
            lhsTerms = lhs.getTerms().subList(1, lhs.getTerms().size());
        } else {
            lhsTerms = lhs.getTerms();
        }
        
        MultTerms rhs = this;
        if (rhs.getTerms().get(0) instanceof CoefficientTerm) {
            rhsTerms = rhs.getTerms().subList(1, rhs.getTerms().size());
        } else {
            rhsTerms = rhs.getTerms();
        }
        return lhsTerms.equals(rhsTerms);
    }

    @Override
    public int evaluate(Map<String, Integer> values) {
        int r = 1;

        for (Term term : terms) {
            r *= term.evaluate(values);
        }

        return r;
    }

    @Override
    public String toString() {
        return StringUtils.join(terms, "*");
    }

}
