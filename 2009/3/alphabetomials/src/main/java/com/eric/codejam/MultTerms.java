package com.eric.codejam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;
import com.sun.xml.internal.ws.api.ha.StickyFeature;

public class MultTerms extends AbstractTerm {

    private List<Term> terms;
    private int coeff;

    public List<Term> getTerms() {
        return Collections.unmodifiableList(terms);
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

    static Term parseVarTerm(String[] strList) {
        Pattern varExpPat = Pattern
                .compile("\\(([^\\+]+)\\s*\\+\\s*([^\\+]+)\\)(.*)");
        Pattern varPat = Pattern.compile("([a-z](?:_[\\da-z])?)(.*?)");
        Matcher m = varPat.matcher(strList[0]);
        if (m.matches()) {
            strList[0] = m.group(2);
            return new VariableTerm(m.group(1));
        }

        return null;
    }

    MultTerms(String str) {
        this();

        Pattern coeffPat = Pattern.compile("^(\\d+)(.*)");
        Matcher m = coeffPat.matcher(str);
        if (m.matches()) {
            coeff = Integer.parseInt(m.group(1));
            str = m.group(2);
        } else {
            coeff = 1;
        }

        String[] strList = new String[] { str };
        while (!strList[0].isEmpty()) {
            str = strList[0];

            strList[0] = strList[0].replaceFirst("^\\*", "");
            Term t = parseBinomialTerm(strList);

            if (t != null) {
                terms.add(t);
            }

            t = parseVarTerm(strList);

            if (t != null) {
                terms.add(t);
            }

            Preconditions.checkState(!StringUtils.equals(str, strList[0]));
            Preconditions.checkState(str.length() > strList[0].length());
        }

    }

    MultTerms() {
        terms = new ArrayList<>();
        coeff = 1;
    }

    @Override
    public void substitute(VariableTerm old, Term newTerm) {
        for (ListIterator<Term> li = terms.listIterator(); li.hasNext();) {
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
         * if (mult instanceof MultTerms) { coeff terms.addAll( ((MultTerms)
         * mult).terms); }
         */

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

    public static AddTerms multiply(BinomialTerm lhs, BinomialTerm rhs) {
        AddTerms add = new AddTerms();
        MultTerms m = new MultTerms();
        m.multiply(lhs.getX());
        m.multiply(rhs.getX());
        add.add(m);

        m = new MultTerms();
        m.multiply(lhs.getX());
        m.multiply(rhs.getY());
        add.add(m);

        m = new MultTerms();
        m.multiply(lhs.getY());
        m.multiply(rhs.getX());
        add.add(m);

        m = new MultTerms();
        m.multiply(lhs.getY());
        m.multiply(rhs.getY());
        add.add(m);

        return add;
    }

    public static AddTerms multiply(VariableTerm lhs, BinomialTerm rhs) {
        AddTerms add = new AddTerms();
        MultTerms m = new MultTerms();
        m.multiply(lhs);
        m.multiply(rhs.getX());
        add.add(m);

        m = new MultTerms();
        m.multiply(lhs);
        m.multiply(rhs.getY());
        add.add(m);

        return add;
    }

    public static AddTerms multiply(BinomialTerm lhs, VariableTerm rhs) {
        return multiply(rhs, lhs);
    }

    @Override
    public Term simplify() {
        Term ret = null;

        // Distribute coefficient
        if (terms.size() == 1 && !(terms.get(0) instanceof PowerTerm)) {
            terms.get(0).multiply(new CoefficientTerm(coeff));
            return terms.get(0);
        }

        // Simplify any sub elements
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

        // Mult binomials
        boolean found = true;
        while (found) {
            found = false;

            for (int i = 0; i < terms.size(); ++i) {
                for (int j = i + 1; j < terms.size(); ++j) {

                    Term lhs = terms.get(i);
                    Term rhs = terms.get(j);
                    Term replacement = null;
                    if (BinomialTerm.class.isInstance(lhs)
                            && BinomialTerm.class.isInstance(rhs)) {
                        replacement = MultTerms.multiply((BinomialTerm) lhs,
                                (BinomialTerm) rhs);

                    } else if (VariableTerm.class.isInstance(lhs)
                            && BinomialTerm.class.isInstance(rhs)) {
                        replacement = MultTerms.multiply((VariableTerm) lhs,
                                (BinomialTerm) rhs);

                    } else if (BinomialTerm.class.isInstance(lhs)
                            && VariableTerm.class.isInstance(rhs)) {
                        replacement = MultTerms.multiply((BinomialTerm) lhs,
                                (VariableTerm) rhs);

                    }

                    if (replacement != null) {
                        terms.remove(j);
                        terms.remove(i);

                        terms.add(replacement);
                        found = true;
                        ret = this;
                        break;
                    }
                }

                if (found) {
                    break;
                }
            }

        }
       

        // Merge same degrees
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

        for (Map.Entry<VariableTerm, Integer> ent : map.entrySet()) {
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

        for (Term term : terms) {
            r *= term.evaluate(values);
        }

        return r;
    }

    @Override
    public String toString() {
        return (coeff == 1 ? "" : coeff) + StringUtils.join(terms, "*");
    }

}
