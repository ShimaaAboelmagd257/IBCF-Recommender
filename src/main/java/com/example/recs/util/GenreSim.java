package com.example.recs.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class GenreSim {
    private static final Logger log = LoggerFactory.getLogger(GenreSim.class);

    public static double jaccardSim(Set<Integer> setA, Set<Integer> setB) {

        if (setA==null || setB==null) return 0.0;

        if (setA.isEmpty() && setB.isEmpty()) return 0.0;

        Set<Integer> intersection = new HashSet<>(setA);
        intersection.retainAll(setB);

        Set<Integer> union = new HashSet<>(setA);
        union.addAll(setB);
        double result = union.isEmpty() ? 0.0 :(double) intersection.size() / union.size();
        log.info("jaccardSim result is: ", result);
        return result;

    }
}

