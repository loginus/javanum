package org.wterliko.javanum;

import java.math.RoundingMode;
import java.text.FieldPosition;
import java.text.NumberFormat;

public class Matrix {

	/**
	 * LU decompostion with Doolittle's method
	 * 
	 * @param matrix
	 */
	public LUDecomposition lu(double[][] source) {
		double[][] matrixL = new double[source.length][source[0].length];
		double[][] matrixU = new double[source.length][source[0].length];
		for (int i = 0; i < source.length; i++) {
			for (int j = 0; j < source[0].length; j++) {
				if (j < i) {
					matrixU[i][j] = Double.valueOf(0);
				} else {
					double sum = 0;
					for (int k = 0; k < i; k++) {
						sum += matrixL[i][k] * matrixU[k][j];
					}
					matrixU[i][j] = source[i][j] - sum;
				}
				if (j < i) {
					matrixL[j][i] = Double.valueOf(0);
				} else if (j == i) {
					matrixL[j][i] = Double.valueOf(1);
				} else {
					double sum = 0;
					for (int k = 0; k < i; k++) {
						sum += matrixL[j][k] * matrixU[k][i];
					}
					matrixL[j][i] = (source[j][i] - sum) / matrixU[i][i];
				}
			}
		}
		LUDecomposition result = new LUDecomposition();
		result.setMatrixL(matrixL);
		result.setMatrixU(matrixU);
		return result;
	}

	/**
	 * Cauchy matrices multiplication. Naive algorithm.
	 * 
	 * @param left
	 * @param right
	 */
	public double[][] multiply(double[][] left, double[][] right) {
		assert left[0].length == right.length : "martices has not proper dimensions and cannot be multiplied"
				+ left[0].length + "!=" + right.length;
		double[][] product = new double[left.length][right[0].length];
		for (int i = 0; i < left.length; i++) {
			for (int j = 0; j < right[0].length; j++) {
				product[i][j] = 0;
				for (int r = 0; r < right.length; r++) {
					product[i][j] += left[i][r] * right[r][j];
				}
			}
		}
		return product;
	}

	public double det(double[][] matrix) {
		assert matrix.length == matrix[0].length : "Matrix is not square "
				+ matrix.length + " != " + matrix[0].length;
		LUDecomposition lu = lu(matrix);
		return detTriangle(lu.getMatrixU());
	}

	public double detTriangle(double[][] tringleMatrix) {
		double det = 1;
		for (int i = 0; i < tringleMatrix.length; i++) {
			det *= tringleMatrix[i][i];
		}
		return det;
	}

	public double[][] randomMatrix(int rows, int cols) {
		double[][] matrix = new double[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				matrix[i][j] = Math.random();
			}
		}
		return matrix;
	}

	public double[][] scalarMultiply(double a, double[][] source) {
		double[][] result = ArrayUtils.cloneArray(source);
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[0].length; j++) {
				result[i][j] *= a;
			}
		}
		return result;
	}

	public String toString(double[][] matrix) {
		NumberFormat format = NumberFormat.getInstance();
		format.setRoundingMode(RoundingMode.HALF_UP);
		FieldPosition pos = new FieldPosition(NumberFormat.FRACTION_FIELD);
		StringBuffer sb = new StringBuffer();
		for (double[] row : matrix) {
			for (double cell : row) {
				format.format(cell, sb, pos);
				sb.append('\t');
			}
			sb.append('\n');
		}
		return sb.toString();
	}

	public static class LUDecomposition {
		private double[][] matrixL;
		private double[][] matrixU;

		public double[][] getMatrixL() {
			return matrixL;
		}

		public void setMatrixL(double[][] matrixL) {
			this.matrixL = matrixL;
		}

		public double[][] getMatrixU() {
			return matrixU;
		}

		public void setMatrixU(double[][] matrixU) {
			this.matrixU = matrixU;
		}
	}
}
