package org.wterliko.javanum;

import org.wterliko.javanum.interfaces.Function;
import org.wterliko.javanum.interfaces.Optimimsator;

public class LevenbergMarquardt implements Optimimsator {
	private double initialLambda = 1e-3;

	private int maxiterations = 100;
	private double termEpsilon = 0.01;
	private int errorNotChangeIterations = 4;

	private DerivateCalculator derivateCalclator;
	private MatrixCalculator matrixCalculator;

	@Override
	public double[] minimum(double[] initialParams, Function f) {
		double[] xi = ArrayUtils.cloneArray(initialParams);
		double valuei = f.evaluate(xi);
		double lambda = initialLambda;

		double[][] hessian = new double[xi.length][xi.length];
		double[] gradient = new double[xi.length];

		int term = 0;

		boolean calculateGradient = true;

		for (int iter = 0; iter < maxiterations; iter++) {
			if (calculateGradient) {
				gradient = calcualteGradient(xi, f, gradient);

			}
			hessian = calculateHessian(xi, f, hessian);
			hessian = boostWithLambda(lambda, hessian);

			double[] d = matrixCalculator.solve(hessian, gradient);
			double[] xi1 = matrixCalculator.minus(xi, d);

			double valuei1 = f.evaluate(xi1);

			if (Math.abs(valuei1 - valuei) > this.termEpsilon) {
				term = 0;
			} else {
				if (++term >= errorNotChangeIterations) {
					break;
				}
			}

			if (valuei1 > valuei || Double.isNaN(valuei1)) {
				lambda *= 8.;
				calculateGradient = false;
			} else {
				calculateGradient = true;
				lambda *= 0.125;
				valuei = valuei1;
				for (int i = 0; i < xi.length; i++) {
					xi[i] = xi1[i];
				}
			}

		}

		return xi;
	}

	public double[][] boostWithLambda(double lambda, double[][] hessian) {
		double lambdaFactor = 1. + lambda;
		for (int i = 0; i < hessian.length; i++) {
			hessian[i][i] *= lambdaFactor;
		}
		return hessian;
	}

	private double[] calcualteGradient(double[] a, Function f, double[] gradient) {
		for (int k = 0; k < a.length; k++) {
			gradient[k] = derivateCalclator.derivate(a, k, f);
		}
		return gradient;
	}

	private double[][] calculateHessian(double[] a, Function f,
			double[][] hessian) {
		for (int row = 0; row < a.length; row++) {
			for (int column = 0; column < a.length; column++) {
				hessian[row][column] = derivateCalclator.derivate2(a, row,
						column, f);
			}
		}
		return hessian;
	}

	public void setErrorNotChangeIterations(int errorNotChangeIterations) {
		this.errorNotChangeIterations = errorNotChangeIterations;
	}

	public void setMaxiterations(int maxiterations) {
		this.maxiterations = maxiterations;
	}

	public void setTermEpsilon(double termEpsilon) {
		this.termEpsilon = termEpsilon;
	}

	public void setDerivateCalculator(DerivateCalculator derivate) {
		this.derivateCalclator = derivate;
	}

	public void setMatrixCalculator(MatrixCalculator matrix) {
		this.matrixCalculator = matrix;
	}

	public void setInitialLambda(double initialLambda) {
		this.initialLambda = initialLambda;
	}

}
