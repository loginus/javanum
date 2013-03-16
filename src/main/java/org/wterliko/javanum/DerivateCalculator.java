package org.wterliko.javanum;

import org.wterliko.javanum.interfaces.Function;

public class DerivateCalculator {

	private final double h = 1e-5;
	private final long inv4h2 = 2500000000L;
	private final long inv2h = 50000L;

	public double derivate(double[] x, int xk, Function f) {
		double[] xModified = ArrayUtils.cloneArray(x);
		// f(x-h)
		xModified[xk] -= h;
		double fl = f.evaluate(xModified);

		// f(x+h)
		double d = 2 * h;
		xModified[xk] += d;
		double fr = f.evaluate(xModified);

		double result = (fr - fl) * inv2h;
		return result;
	}

	public double derivate2(double[] x, int xk, int xl, Function func) {
		double twoH = 2 * h;
		double[] xModified = ArrayUtils.cloneArray(x);
		// f(x+h,y+h)
		xModified[xk] += h;
		xModified[xl] += h;
		double fkl = func.evaluate(xModified);

		// f(x-h,y+h)
		xModified[xk] -= twoH;
		double fl = func.evaluate(xModified);

		// f(x+h,y-h)
		xModified[xk] += twoH;
		xModified[xl] -= twoH;
		double fk = func.evaluate(xModified);

		// f(x-h,y-h)
		xModified[xk] -= twoH;
		double f = func.evaluate(xModified);

		double value = (fkl - fl - fk + f) * inv4h2;
		return value;
	}

}
