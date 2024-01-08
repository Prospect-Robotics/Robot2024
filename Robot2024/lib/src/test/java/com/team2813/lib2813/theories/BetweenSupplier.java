package com.team2813.lib2813.theories;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.experimental.theories.ParameterSignature;
import org.junit.experimental.theories.ParameterSupplier;
import org.junit.experimental.theories.PotentialAssignment;

public class BetweenSupplier extends ParameterSupplier {
	@Override
	public List<PotentialAssignment> getValueSources(ParameterSignature sig) {
		BetweenRepeated repeatedAnnotation = sig.getAnnotation(BetweenRepeated.class);
		if (repeatedAnnotation != null) {
			Set<Integer> vals = new HashSet<>();
			for (Between annotation : repeatedAnnotation.value()) {
				for (int i = annotation.first(); i <= annotation.last(); i++) {
					vals.add(i);
				}
			}
			List<PotentialAssignment> result = new ArrayList<>();
			for (Integer val : vals) {
				result.add(PotentialAssignment.forValue("ints", val));
			}
			return result;
		} else {
			Between annotation = sig.getAnnotation(Between.class);
			List<PotentialAssignment> result = new ArrayList<>();
			for (int i = annotation.first(); i <= annotation.last(); i++) {
				result.add(PotentialAssignment.forValue("ints", i));
			}
			return result;
		}

	}
}