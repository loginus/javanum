package org.wterliko.javanum.test;

import static org.junit.Assert.assertEquals;

public class NumericTestUtils {

	private static final double EPS = 1e-4;

	public static void assertValueClose(double expected, double actual) {
		assertEquals(expected, actual, EPS);
		// if (Math.abs(actual) < EPS) {
		// assertTrue(expected + " != " + actual,
		// Math.abs(expected - actual) < EPS);
		// } else {
		// assertTrue(expected + " != " + actual,
		// Math.abs(1 - expected / actual) < EPS);
		// }

	}

	public static void assertMartixEquals(double[][] expected, double[][] actual) {
		assertEquals(expected.length, actual.length);
		assertEquals(expected[0].length, actual[0].length);
		for (int i = 0; i < actual.length; i++) {
			assertVectorEquals(expected[i], actual[i]);
		}
	}

	public static void assertVectorEquals(double[] expected, double[] actual) {
		for (int i = 0; i < actual.length; i++) {
			assertValueClose(expected[i], actual[i]);
		}
	}
}
