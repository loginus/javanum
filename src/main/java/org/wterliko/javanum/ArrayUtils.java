package org.wterliko.javanum;

public class ArrayUtils {

	public static double[][] cloneArray(double[][] src) {
		int length = src.length;
		double[][] target = new double[length][src[0].length];
		for (int i = 0; i < length; i++) {
			target[i] = doCloneVector(src[i], target[i]);
		}
		return target;
	}

	public static double[] cloneArray(double[] source) {
		double[] copy = new double[source.length];
		doCloneVector(source, copy);
		return copy;
	}

	private static double[] doCloneVector(double[] source, double[] target) {
		System.arraycopy(source, 0, target, 0, source.length);
		return target;
	}
}
