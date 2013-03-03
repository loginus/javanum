package org.wterliko.javanum;

public class ArrayUtils {

	public static double[][] cloneArray(double[][] src) {
		int length = src.length;
		double[][] target = new double[length][src[0].length];
		for (int i = 0; i < length; i++) {
			System.arraycopy(src[i], 0, target[i], 0, src[i].length);
		}
		return target;
	}

	public static double[] cloneArray(double[] source) {
		double[] copy = new double[source.length];
		System.arraycopy(source, 0, copy, 0, source.length);
		return copy;
	}
}
