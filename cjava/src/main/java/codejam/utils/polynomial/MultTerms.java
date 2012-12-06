package codejam.utils.polynomial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

public class MultTerms extends AbstractTerm {

    final private List<Term> terms;

    public List<Term> getTerms() {
        return terms;
    }
    
    
    private MultTerms(List<Term> args) {
        
        this.terms = args;
        
        Preconditions.checkState(this.terms.size() > 1);
    }
    
    static Term  buildMultTerm(Term...args) {
        return buildMultTerm(Arrays.asList(args));
    }
   
    static Term buildMultTerm(List<Term> args) {
        ArrayList<Term> terms = new ArrayList<>();
        terms.addAll(args);
        simplifyImpl(terms);
        Collections.sort(terms, new Polynomial.MultCompareTerm());
        
        if (terms.size() > 1) {
            return new MultTerms( ImmutableList.copyOf(terms) );
        } else {
            Preconditions.checkState(terms.size() == 1);
            return terms.get(0);
        }
        
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
        
        return MultTerms.buildMultTerm(terms);
    }

    @Override
    public Term substitute(Map<VariableTerm, Term> termsToSub) {
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
        
        return MultTerms.buildMultTerm(terms);
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
        return buildMultTerm(terms);
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
        return buildMultTerm(terms);
    }

    @Override
    public boolean canMultiplyAsRhs(PowerTerm lhs) {
        return true;
    }

    @Override
    public Term multiplyAsRhs(PowerTerm lhs) {
        return multiplyAsRhsImpl(lhs);
    }

    private static void simplifyImpl(List<Term> simTerms) {
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
                    break;
                
                }

                if (found) {
                    break;
                }
            }

        }
        
    }
    @Override
    public Term simplify() {
        List<Term> simTerms = new ArrayList<>();
        simTerms.addAll(getTerms());
        boolean hasSimp = false;

        if (terms.size() == 1 ) {
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

        
        
       

       if (hasSimp) {
           return buildMultTerm(simTerms);
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
        return buildMultTerm(lhsTerms);
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
    public boolean canAddAsRhs(PowerTerm lhs) {
        List<Term> rhsTerms = null;
                
        MultTerms rhs = this;
        if (rhs.getTerms().get(0) instanceof CoefficientTerm) {
            rhsTerms = rhs.getTerms().subList(1, rhs.getTerms().size());
        } else {
            rhsTerms = rhs.getTerms();
        }
        return rhsTerms.size() == 1 && lhs.equals(rhsTerms.get(0));
    }

    @Override
    public Term addAsRhs(PowerTerm lhs) {

        List<Term> rhsTerms = new ArrayList<>();
        int coeffLhs = 1;
        int coeffRhs = 1;
        
        MultTerms rhs = this;
        if (rhs.getTerms().get(0) instanceof CoefficientTerm) {
            Preconditions.checkState(rhs.getTerms().size() == 2);
            rhsTerms.add( rhs.getTerms().get(1) );
            coeffRhs = ((CoefficientTerm) rhs.getTerms().get(0)).getValue();
        } else {
            Preconditions.checkState(rhs.getTerms().size() == 1);
            rhsTerms.add( rhs.getTerms().get(0) );
        }
        Preconditions.checkArgument(lhs.equals(rhsTerms.get(0)));
        rhsTerms.add(0, new CoefficientTerm(coeffLhs + coeffRhs));
        return buildMultTerm(rhsTerms);
    }
    
    @Override
    public boolean canAddAsRhs(VariableTerm lhs) {
        List<Term> rhsTerms = null;
                
        MultTerms rhs = this;
        if (rhs.getTerms().get(0) instanceof CoefficientTerm) {
            rhsTerms = rhs.getTerms().subList(1, rhs.getTerms().size());
        } else {
            rhsTerms = rhs.getTerms();
        }
        return rhsTerms.size() == 1 && lhs.equals(rhsTerms.get(0));
    }

    @Override
    public Term addAsRhs(VariableTerm lhs) {

        List<Term> rhsTerms = new ArrayList<>();
        int coeffLhs = 1;
        int coeffRhs = 1;
        
        MultTerms rhs = this;
        if (rhs.getTerms().get(0) instanceof CoefficientTerm) {
            Preconditions.checkState(rhs.getTerms().size() == 2);
            rhsTerms.add( rhs.getTerms().get(1) );
            coeffRhs = ((CoefficientTerm) rhs.getTerms().get(0)).getValue();
        } else {
            Preconditions.checkState(rhs.getTerms().size() == 1);
            rhsTerms.add( rhs.getTerms().get(0) );
        }
        Preconditions.checkArgument(lhs.equals(rhsTerms.get(0)));
        rhsTerms.add(0, new CoefficientTerm(coeffLhs + coeffRhs));
        return buildMultTerm(rhsTerms);
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
    
    @Override
    public String getFirstNonCoefPart() {
        
        if (terms.get(0) instanceof CoefficientTerm) {
            if (terms.size() > 1 )
                return    terms.get(1).getFirstNonCoefPart();
            else
                return null;
        }
        
        return terms.get(0).getFirstNonCoefPart();
    }
    @Override
    public String getCoefPart() {
        
        return terms.get(0) instanceof CoefficientTerm ?
                terms.get(0).toString() :
                    null;
    }
    @Override
    public int getDegree() {
        if (terms.get(0) instanceof CoefficientTerm) {
            if (terms.size() > 1 )
                return terms.get(1).getDegree();
            else 
                return 1;
        }          
         return terms.get(0).getDegree();
    }

    @Override
    public String getNonCoefPart() {
        if (terms.get(0) instanceof CoefficientTerm) {
            return StringUtils.join(terms.subList(1, terms.size()), "*");
        }
        
        return StringUtils.join(terms.subList(0, terms.size()), "*");
    }

}
