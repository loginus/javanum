package org.wterliko.javanum;

import static org.junit.Assert.*;

import org.junit.Test;
import org.wterliko.javanum.test.NumericTestUtils;

public class TestArrayUtils {

	@Test
	public void testCloneVector() throws Exception {
		double[] source = new double[] { 2.1, 4, 5 };
		double[] result = ArrayUtils.cloneArray(source);
		NumericTestUtils.assertVectorEquals(source, result);
		assertFalse(source == result);
	}

	@Test
	public void testCloneArray() throws Exception {
		double[][] source = new double[][] { { 2.1, 4, 5 }, { 5, 6.2, 7 } };
		double[][] result = ArrayUtils.cloneArray(source);
		NumericTestUtils.assertMartixEquals(source, result);
		assertFalse(source == result);
		for (int i = 0; i < source.length; i++) {
			assertFalse(source[i] == result[i]);
		}
	}
}
