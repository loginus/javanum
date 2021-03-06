package org.wterliko.javanum;

import java.math.RoundingMode;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.util.Set;

public class MatrixCalculator {

	/**
	 * LU decompostion with Doolittle's method.
	 * 
	 * TODO This implementation is slow for bigger matrices approx 3x slower
	 * than Jama for 500x500. For small matrices is faster.
	 * 
	 * @param matrix
	 */
	public LUDecomposition lu(double[][] source) {
		double[][] matrixL = new double[source.length][source[0].length];
		double[][] matrixU = new double[source.length][source[0].length];
		for (int i = 0; i < source.length; i++) {
			for (int j = 0; j < source[0].length; j++) {
				if (j >= i) {
					double sumU = 0;
					for (int k = 0; k < i; k++) {
						sumU += matrixL[i][k] * matrixU[k][j];
					}
					matrixU[i][j] = source[i][j] - sumU;
					if (j == i) {
						matrixL[j][i] = 1;
					} else {
						double sumL = 0;
						for (int k = 0; k < i; k++) {
							sumL += matrixL[j][k] * matrixU[k][i];
						}
						matrixL[j][i] = (source[j][i] - sumL) / matrixU[i][i];
					}
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
		if (left[0].length != right.length) {
			throw new IllegalArgumentException(
					"Martices have not proper dimensions and cannot be multiplied"
							+ left[0].length + "!=" + right.length);
		}
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
		if (matrix.length != matrix[0].length) {
			throw new IllegalArgumentException("Matrix is not square "
					+ matrix.length + " != " + matrix[0].length);
		}
		LUDecomposition lu = lu(matrix);
		return detTriangle(lu.getMatrixU());
	}

	public double detTriangle(double[][] tringleMatrix) {
		if (tringleMatrix.length != tringleMatrix[0].length) {
			throw new IllegalArgumentException("Matrix is not square "
					+ tringleMatrix.length + " != " + tringleMatrix[0].length);
		}
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

	public double[] randomVector(int rows) {
		double[] vector = new double[rows];
		for (int i = 0; i < rows; i++) {
			vector[i] = Math.random();
		}
		return vector;
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

	public double[] solve(double[][] a, double[] b) {
		if (b.length != a.length || a.length != a[0].length) {
			throw new IllegalArgumentException(
					"Dimensions of matrices doesn't fit");
		}
		LUDecomposition lu = lu(a);
		double[] z = solveTriangle(lu.getMatrixL(), b, true);
		double[] x = solveTriangle(lu.getMatrixU(), z, false);
		return x;
	}

	public double[][] transpose(double[][] a) {
		double[][] at = new double[a[0].length][a.length];
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				at[j][i] = a[i][j];
			}
		}
		return at;
	}

	public boolean checkSymetry(double[][] matrix) {
		if (matrix.length != matrix[0].length) {
			return false;
		}
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				if (matrix[j][i] != matrix[i][j]) {
					return false;
				}
			}
		}
		return true;
	}

	public LUDecomposition choleski(double[][] matrix) {
		if (!checkSymetry(matrix)) {
			throw new IllegalArgumentException("matrix is not symetric");
		}
		double[][] matrixL = new double[matrix.length][matrix.length];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j <= i; j++) {
				if (i == j) {
					double sum = 0;
					for (int k = 0; k < i; k++) {
						sum += matrixL[i][k] * matrixL[i][k];
					}
					if (sum > matrix[i][j]) {
						throw new IllegalArgumentException(
								"matrix is not Positive-definite "
										+ toString(matrix));
					}
					double elem = Math.sqrt(matrix[i][j] - sum);
					matrixL[i][j] = elem;
				} else {
					double sum = 0;
					for (int k = 0; k < j; k++) {
						sum += matrixL[j][k] * matrixL[i][k];
					}
					double elem = (matrix[j][i] - sum) / matrixL[j][j];
					matrixL[i][j] = elem;
				}
			}
		}
		LUDecomposition result = new LUDecomposition();
		result.setMatrixL(matrixL);
		double[][] matrixU = transpose(matrixL);
		result.setMatrixU(matrixU);
		return result;
	}

	public double minor(double[][] matix, Set<Integer> columnsToEliminate,
			Set<Integer> rowsToEliminate) {
		if (matix.length <= rowsToEliminate.size()
				|| matix[0].length <= columnsToEliminate.size()) {
			throw new IllegalArgumentException(
					"Cannot eliminate so many rows matrix lenght:  "
							+ matix.length + " row " + rowsToEliminate.size()
							+ " columns to eliminate "
							+ columnsToEliminate.size());
		}
		double[][] resultMatrix = new double[matix.length
				- rowsToEliminate.size()][matix[0].length
				- columnsToEliminate.size()];
		int k = 0;
		for (int i = 0; i < matix.length; i++) {
			if (rowsToEliminate.contains(i)) {
				continue;
			}
			int l = 0;
			for (int j = 0; j < matix[0].length; j++) {
				if (columnsToEliminate.contains(j)) {
					continue;
				}
				resultMatrix[k][l++] = matix[i][j];
			}
			k++;
		}
		return det(resultMatrix);
	}

	public double[][] minus(double[][] left, double[][] right) {
		if (left.length != right.length || left[0].length != right[0].length) {
			throw new IllegalArgumentException(" left matrix is " + left.length
					+ "x" + left[0].length + " while right " + right.length
					+ "x" + right[0].length);
		}
		double[][] result = new double[left.length][left[0].length];
		for (int i = 0; i < left.length; i++) {
			result[i] = minus(left[i], right[i]);
		}
		return result;
	}

	public double[] minus(double[] left, double[] right) {
		if (left.length != right.length) {
			throw new IllegalArgumentException(" left vector has "
					+ left.length + " elements while right " + right.length);
		}
		double[] result = new double[left.length];
		for (int i = 0; i < left.length; i++) {
			result[i] = left[i] - right[i];
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

	public String toString(double[] vector) {
		double[][] matrix = new double[][] { vector };
		return toString(matrix);
	}

	private double[] solveTriangle(double[][] a, double[] b, boolean lower) {
		double[] z = new double[b.length];
		for (int i = 0; i < b.length; i++) {
			int rowIndex = getIndex(i, b.length, lower);
			double sum = 0;
			for (int k = 0; k < i; k++) {
				int columnIndex = getIndex(k, b.length, lower);
				sum += a[rowIndex][columnIndex] * z[columnIndex];
			}
			z[rowIndex] = (b[rowIndex] - sum) / a[rowIndex][rowIndex];
		}
		return z;
	}

	private int getIndex(int index, int length, boolean lower) {
		return lower ? index : length - 1 - index;
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
