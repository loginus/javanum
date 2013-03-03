package org.wterliko.javanum;

public class Derivate {

	private double h = 1e-5;
	private double h2inv = 1e10;

	public double derivate(double[] x, int xk, Function f) {
		double[] xModified = ArrayUtils.cloneArray(x);
		xModified[xk] -= h;
		double fl = f.evaluate(xModified);
		double d = 2 * h;
		xModified[xk] += d;
		double fr = f.evaluate(xModified);
		double result = (fr - fl) / d;
		return result;
	}

	public double derivate2(double[] x, int xk, int xl, Function func) {
		double[] xModified = ArrayUtils.cloneArray(x);
		xModified[xk] += h;
		xModified[xl] += h;
		double fkl = func.evaluate(xModified);

		xModified[xk] -= h;
		double fl = func.evaluate(xModified);

		xModified[xk] += h;
		xModified[xl] -= h;
		double fk = func.evaluate(xModified);

		double f = func.evaluate(x);

		double value = (fkl - fl - fk + f) * h2inv;
		return value;
	}

	public void setH(double h) {
		this.h = h;
	}
}
