package org.wterliko.javanum.test;

import static org.junit.Assert.assertEquals;

public class NumericTestUtils {

	public static final double EPS = 1e-4;

	public static void assertMartixEquals(double[][] expected, double[][] actual) {
		assertEquals(expected.length, actual.length);
		assertEquals(expected[0].length, actual[0].length);
		for (int i = 0; i < actual.length; i++) {
			assertVectorEquals(expected[i], actual[i]);
		}
	}

	public static void assertVectorEquals(double[] expected, double[] actual) {
		for (int i = 0; i < actual.length; i++) {
			assertEquals(expected[i], actual[i], EPS);
		}
	}
}
