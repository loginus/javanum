package org.wterliko.javanum;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.wterliko.javanum.interfaces.Function;
import org.wterliko.javanum.test.NumericTestUtils;

public class TestDerivate {

	private DerivateCalculator subject;

	@Before
	public void before() {
		subject = new DerivateCalculator();
	}

	@Test
	public void testDerivateSin() throws Exception {
		Function sin = new Function() {
			@Override
			public double evaluate(double[] x) {
				return Math.sin(x[0]);
			}
		};
		Function cos = new Function() {
			@Override
			public double evaluate(double[] x) {
				return Math.cos(x[0]);
			}
		};
		testValues(sin, cos, -Math.PI, Math.PI);
	}

	@Test
	public void testDerivateExp() throws Exception {
		Function exp = new Function() {
			@Override
			public double evaluate(double[] x) {
				return Math.exp(x[0]);
			}
		};
		testValues(exp, exp, -Math.PI, Math.PI);
	}

	@Test
	public void testDerivatePoly() throws Exception {
		Function poly = new Function() {
			@Override
			public double evaluate(double[] args) {
				double x = args[0];
				double value = 3 * x * x * x * x * x - 4 * x * x * x * x + 5
						* x * x * x - 6 * x * x + 7 * x - 8;
				return value;
			}
		};
		Function poly1 = new Function() {

			@Override
			public double evaluate(double[] args) {
				double x = args[0];
				double value = 15 * x * x * x * x - 16 * x * x * x + 15 * x * x
						- 12 * x + 7;
				return value;
			}
		};
		testValues(poly, poly1, -10, 10);
	}

	@Test
	public void testDerivate2Sin() throws Exception {
		Function f = new Function() {
			@Override
			public double evaluate(double[] x) {
				return Math.sin(x[0]) * Math.cos(x[1]);
			}
		};
		Function f1 = new Function() {
			@Override
			public double evaluate(double[] x) {
				return -Math.cos(x[0]) * Math.sin(x[1]);
			}
		};
		test2Devivate(f, f1, -Math.PI, Math.PI);
	}

	@Test
	public void testDerivate2Exp() throws Exception {
		Function exp = new Function() {
			@Override
			public double evaluate(double[] x) {
				return Math.exp(x[0]) * Math.exp(x[1]);
			}
		};
		test2Devivate(exp, exp, -1.5, 1.5);
	}

	@Test
	public void testDerivate2Poly() throws Exception {
		Function poly = new Function() {
			@Override
			public double evaluate(double[] args) {
				double x = args[0];
				double y = args[1];
				double value = 3 * x * x - 2 * y + 3 * x - 1 + 3 * x * x * y;
				return value;
			}
		};
		Function poly1 = new Function() {

			@Override
			public double evaluate(double[] args) {
				double x = args[0];
				double value = 6 * x;
				return value;
			}
		};
		test2Devivate(poly, poly1, -Math.PI, Math.PI);
	}

	private void testValues(Function f, Function f1, double xmin, double xmax) {
		double[] x = new double[] { xmin };
		while (x[0] < xmax) {
			double actual = subject.derivate(x, 0, f);
			double exptected = f1.evaluate(x);
			assertEquals(exptected, actual, NumericTestUtils.EPS);
			x[0] += 0.1;
		}
	}

	private void test2Devivate(Function f, Function f1, double xmin, double xmax) {
		double[] x = new double[] { xmin, xmin };
		while (x[0] < xmax) {
			while (x[1] < xmax) {
				double actual = subject.derivate2(x, 0, 1, f);
				double exptected = f1.evaluate(x);
				assertEquals(exptected, actual, NumericTestUtils.EPS);
				x[0] += 0.1;
				x[1] += 0.1;
			}
		}
	}
}
