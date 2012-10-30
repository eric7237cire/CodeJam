package com.eric.codejam;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Multiset;
import com.google.common.collect.Multiset.Entry;
import com.google.common.collect.TreeMultiset;

public class MultGroup {
    List<Term> terms;

    public MultGroup(String str) {
        Pattern coeff = Pattern.compile("\\d+");
        Matcher m = coeff.matcher(str);

        terms = new ArrayList<>();

        if (m.matches()) {
            terms.add(new CoefficientTerm(Integer.parseInt(m.group(1))));
        }

        Multiset<Character> varDegree = TreeMultiset.create();

        for (int chIdx = 0; chIdx < str.length(); ++chIdx) {
            char ch = str.charAt(chIdx);
            if (Character.isAlphabetic(ch)) {
                varDegree.add(ch);
            }
        }

        for (Entry<Character> chEntry : varDegree.entrySet()) {
            terms.add(new PowerTerm(
                    new VariableTerm("" + chEntry.getElement()), chEntry
                            .getCount()));
        }

    }

    public void substitute(Term old, Term newTerm) {
        for (int i = 0; i < terms.size(); ++i) {
            if (terms.get(i).equals(old)) {
                terms.set(i, newTerm);
            }
        }
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append(StringUtils.join(terms, "*"));

        return sb.toString();
    }
}
