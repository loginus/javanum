package org.wterliko.javanum;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.wterliko.javanum.DerivateCalculator;
import org.wterliko.javanum.LevenbergMarquardt;
import org.wterliko.javanum.MatrixCalculator;
import org.wterliko.javanum.interfaces.Function;
import org.wterliko.javanum.test.NumericTestUtils;

public class TestLevenbergMarquardt {

	private LevenbergMarquardt subject;

	@Before
	public void before() {
		subject = new LevenbergMarquardt();
		subject.setDerivateCalculator(new DerivateCalculator());
		subject.setMatrixCalculator(new MatrixCalculator());
	}

	@Test
	public void testSine() {

		Function f = new Function() {

			@Override
			public double evaluate(double[] x) {
				double value = Math.sin(x[0]);
				return value;
			}
		};

		double[] x0 = new double[] { 3.5 };
		double[] result = subject.minimum(x0, f);
		assertEquals(1, result.length);
		assertEquals(Math.PI * 3. / 2., result[0], NumericTestUtils.EPS);
	}

	@Test
	public void testLocalMinimum() {
		Function f = new Function() {

			@Override
			public double evaluate(double[] arg) {
				double x = arg[0];
				double result = Math.pow(x, 4) - 10 * Math.pow(x, 2) + 3 * x;
				return result;
			}
		};

		double[] x0 = new double[] { 300 };
		double[] result = subject.minimum(x0, f);
		assertEquals(1, result.length);
		assertEquals(2.15691, result[0], NumericTestUtils.EPS);
	}

	@Test
	public void testMultidimensional() {

		Function f = new Function() {
			@Override
			public double evaluate(double[] a) {
				return 2 * a[0] * a[0] + 2 * a[0] * a[1] + 2 * a[1] * a[1] - 6
						* a[0] + 6;
			}

		};

		double[] x0 = new double[] { 0, 0 };
		double[] result = subject.minimum(x0, f);
		assertEquals(2, result[0], NumericTestUtils.EPS);
		assertEquals(-1, result[1], NumericTestUtils.EPS);
	}

}
