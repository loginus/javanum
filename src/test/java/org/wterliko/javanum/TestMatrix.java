package org.wterliko.javanum;

import static org.junit.Assert.fail;
import static org.wterliko.javanum.test.NumericTestUtils.assertMartixEquals;
import static org.wterliko.javanum.test.NumericTestUtils.assertValueClose;
import static org.wterliko.javanum.test.NumericTestUtils.assertVectorEquals;

import org.junit.Before;
import org.junit.Test;
import org.wterliko.javanum.Matrix.LUDecomposition;

public class TestMatrix {

	private Matrix subject;

	@Before
	public void before() {
		subject = new Matrix();
	}

	@Test
	public void testMultiplicaiton1x1() throws Exception {
		double[][] left = new double[][] { { 7.0 } };
		double[][] right = new double[][] { { 8.0 } };
		double[][] result = subject.multiply(left, right);
		assertMartixEquals(new double[][] { { 56 } }, result);
	}

	@Test
	public void testMultiplicaiton2x3() throws Exception {
		double[][] left = new double[][] { { 1, 0, 2 }, { -1, 3, 1 } };
		double[][] right = new double[][] { { 3, 1 }, { 2, 1 }, { 1, 0 } };
		double[][] result = subject.multiply(left, right);
		assertMartixEquals(new double[][] { { 5, 1 }, { 4, 2 } }, result);
	}

	@Test(expected = AssertionError.class)
	public void testMultiplicationInvalidDimensions() throws Exception {
		double[][] left = new double[][] { { 1, 0, 2 }, { -1, 3, 1 },
				{ 2, 5, 5 } };
		double[][] right = new double[][] { { 3, 1 }, { 2, 1 }, { 1, 0 } };
		subject.multiply(left, right);
		fail();
	}

	@Test
	public void testLUDecomposition() throws Exception {
		double[][] source = new double[][] { { 1, 2, 2 }, { 2, 1, 2 },
				{ 2, 2, 1 } };
		LUDecomposition decompisition = subject.lu(source);
		System.out.println(subject.toString(decompisition.getMatrixL()));
		System.out.println(subject.toString(decompisition.getMatrixU()));

		double[][] expectedL = new double[][] { { 1, 0, 0 }, { 2, 1, 0 },
				{ 2, 2.0 / 3.0, 1 } };
		double[][] expectedU = new double[][] { { 1, 2, 2 }, { 0, -3, -2 },
				{ 0, 0, -5.0 / 3.0 } };
		System.out.println(subject.toString(expectedL));
		System.out.println(subject.toString(expectedU));

		assertMartixEquals(expectedL, decompisition.getMatrixL());
		assertMartixEquals(expectedU, decompisition.getMatrixU());
	}

	@Test
	public void testLUDecompositionInvert() throws Exception {
		double[][] source = new double[][] { { 5, 3, 2 }, { 1, 2, 0 },
				{ 3, 0, 4 } };
		LUDecomposition result = subject.lu(source);
		double[][] actual = subject.multiply(result.getMatrixL(),
				result.getMatrixU());

		System.out.println(subject.toString(source));
		System.out.println(subject.toString(result.getMatrixL()));
		System.out.println(subject.toString(result.getMatrixU()));

		System.out.println(subject.toString(actual));
		assertMartixEquals(source, actual);
	}

	@Test
	public void testDetTriangle() throws Exception {
		double[][] matrix = { { 1, 0, 0 }, { 5, 7, 0 }, { 2, 5, 7 } };
		System.out.println(subject.toString(matrix));
		assertValueClose(49.0, subject.detTriangle(matrix));
	}

	@Test
	public void testDet() throws Exception {
		double[][] matrix = { { 5, 3, 2 }, { 1, 2, 0 }, { 3, 0, 4 } };
		System.out.println(subject.toString(matrix));
		assertValueClose(16, subject.det(matrix));
	}

	@Test
	public void testSolve() throws Exception {
		double[][] matrix = { { 5, 3, 2 }, { 1, 2, 0 }, { 3, 0, 4 } };
		double[] b = { 10, 5, -2 };
		double[] result = subject.solve(matrix, b);
		System.out.println(subject.toString(matrix));
		System.out.println(subject.toString(b));
		assertVectorEquals(
				new double[] { 7.0 / 4.0, 13.0 / 8.0, -29.0 / 16.0 }, result);
	}

	@Test
	public void testTransposeSimple() throws Exception {
		double[][] a = new double[][] { { 2, 3, 1, 4 }, { -1, 2, 0, 1 },
				{ 2, 2, 0, 1 } };
		double[][] transposed = subject.transpose(a);
		double[][] expetced = new double[][] { { 2, -1, 2 }, { 3, 2, 2 },
				{ 1, 0, 0 }, { 4, 1, 1 } };
		assertMartixEquals(expetced, transposed);
	}

	@Test
	public void testTranspose2x() throws Exception {
		double[][] a = new double[][] { { 2, 3, 1, 4 }, { -1, 2, 0, 1 },
				{ 2, 2, 0, 1 } };
		double[][] transposed1 = subject.transpose(a);
		double[][] transposed2 = subject.transpose(transposed1);
		assertMartixEquals(a, transposed2);
	}
}
