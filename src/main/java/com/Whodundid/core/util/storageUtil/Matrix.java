package com.Whodundid.core.util.storageUtil;

import java.util.List;

//Created: 9-16-19
//Author: Hunter Bragg

/**
 * The {@code Matrix} class is a data type representing a matrix structure with {@code Double} values.
 * The {@code Matrix} class provides numerous functions for performing matrix operations both locally and statically.
 * <blockquote><pre>
 *     Matrix m = new Matrix(2, 2, 1.0, 2.0, 3.0, 4.0);
 *     
 *     			|1.0 2.0|
 *     			|3.0 4.0|
 * </pre></blockquote><p>
 * @author Hunter Bragg
 */
public class Matrix {
	
	StorageBoxHolder<Integer, List<Double>> table = new StorageBoxHolder();
	private int rSize = 0, cSize = 0;
	private boolean printCommas = false;
	
	/**
	 * Initializes a {@code Matrix} object with default size of 0 rows and 0 columns.
	 */
	public Matrix() { this(0, 0); }
	
	/**
	 * Initializes a {@code Matrix} object with row and column dimensions specified by a StorageBox<Integer, Integer>.
	 * 
	 * @see StorageBox
	 * @param dimIn {@code StorageBox<Integer, Integer>}
	 */
	public Matrix(StorageBox<Integer, Integer> dimIn) { this(dimIn.getObject(), dimIn.getValue()); }
	
	/**
	 * Initializes a {@code Matrix} with specified int values for rows and columns.
	 * 
	 * @param rowSizeIn {@code Integer}
	 * @param columnSizeIn {@code Integer}
	 */
	public Matrix(int rowSizeIn, int columnSizeIn) {
		create(rowSizeIn, columnSizeIn);
	}
	
	/**
	 * Initializes a {@code Matrix} with specified int values for rows and columns and attempts to fill it with a series of 
	 * EArrayList<Double> containting each row's contents.
	 * 
	 * <p>If the number of given EArrayList<Double> for row contents does not match the specified rowSizeIn value, the initialization
	 * process ignores the provided arguments.
	 * 
	 * @param rowSizeIn {@code Integer}
	 * @param columnSizeIn {@code Integer}
	 * @param rows {@code EArrayList<Double>}
	 * 
	 * @see EArrayList
	 */
	public Matrix(int rowSizeIn, int columnSizeIn, List<Double>... rows) {
		rSize = rowSizeIn;
		cSize = columnSizeIn;
		for (int i = 0; i < rSize; i++) {
			table.add(i, rows[i]);
		}
	}
	
	/**
	 * Initializes a {@code Matrix} with specified int values for rows and columns and attempts to fill it with provided int values.
	 * 
	 * <p>The int values are converted to double in this process.
	 * 
	 * <p>If the number of given vals does not match the intended matrix size of (rowSizeIn * columnSizeIn), the initialization
	 * process will ignore the provided arguments.
	 * 
	 * @param rowSizeIn {@code Integer}
	 * @param columnSizeIn {@code Integer}
	 * @param vals {@code Integer[]}
	 */
	public Matrix(int rowSizeIn, int columnSizeIn, Number... vals) {
		create(rowSizeIn, columnSizeIn);
		if (vals.length == rowSizeIn * columnSizeIn) {
			int q = 0;
			for (int j = 0; j < rSize; j++) {
				for (int i = 0; i < cSize; i++) {
					setVal(j, i, vals[q].doubleValue());
					q++;
				}
			}
		}
	}
	
	/**
	 * Initializes a {@code Matrix} with specified int values for rows and columns and attempts to fill it with a provided list containing double values.
	 * 
	 * <p>The int values are converted to double in this process.
	 * 
	 * <p>If the number of given vals does not match the intended matrix size of (rowSizeIn * columnSizeIn), the initialization
	 * process will ignore the provided arguments.
	 * 
	 * @param rowSizeIn {@code Integer}
	 * @param columnSizeIn {@code Integer}
	 * @param vals {@code List<Double>}
	 */
	public Matrix(int rowSizeIn, int columnSizeIn, List<Double> vals) {
		create(rowSizeIn, columnSizeIn);
		if (vals.size() == rowSizeIn * columnSizeIn) {
			int q = 0;
			for (int j = 0; j < rSize; j++) {
				for (int i = 0; i < cSize; i++) {
					setVal(j, i, vals.get(q));
					q++;
				}
			}
		}
	}
	
	/**
	 * Initializes a {@code Matrix} with dimensions and values from an existing {@code MatrixInt} object.
	 * 
	 * <p>The int values are converted to double in this process.
	 * 
	 * <p>If the specified {@code MatrixInt} object is null, the argument is ignored and an empty matrix will be created instead.
	 * 
	 * @param vals {@code MatrixInt}
	 * @see MatrixInt
	 */
	public Matrix(MatrixInt matrixIn) { if (matrixIn != null) { setValues(matrixIn.toDouble()); } else { create(0, 0); } }
	
	/**
	 * Initializes a {@code Matrix} with dimensions and values from an existing {@code Matrix} object.
	 * 
	 * <p>If the specified {@code Matrix} object is null, the argument is ignored and an empty matrix will be created instead.
	 * 
	 * @param vals {@code Matrix}
	 */
	public Matrix(Matrix matrixIn) { if (matrixIn != null) { setValues(matrixIn); } else { create(0, 0); } }
	
	
	//base methods
	
	/**
	 * Returns true if this {@code Matrix} object will print commas ',' between each value when printing to the console.
	 * 
	 * @return {@code Boolean}
	 */
	public boolean printsCommas() { return printCommas; }
	
	/**
	 * Specifies whether this {@code Matrix} object will print commas ',' between each value when printing to the console.
	 * 
	 * @param valIn {@code Boolean}
	 * @return This {@code Matrix} object.
	 */
	public Matrix setPrintCommas(boolean valIn) { printCommas = valIn; return this; }
	
	
	//matrix util functions
	
	/**
	 * Internal function used to initialize a {@code Matrix} with the specified row and columns sizes.
	 * 
	 * @param rowSizeIn {@code Integer}
	 * @param colSizeIn {@code Integer}
	 */
	private void create(int rowSizeIn, int columnSizeIn) {
		rSize = rowSizeIn;
		cSize = columnSizeIn;
		clearMatrix();
	}
	
	/**
	 * Returns this {@code Matrix} object's row size.
	 * 
	 * @return {@code Integer}
	 */
	public int getRowLength() { return rSize; }
	
	/**
	 * Returns this {@code Matrix} object's column size.
	 * 
	 * @return {@code Integer}
	 */
	public int getColumnLength() { return cSize; }
	
	/**
	 * Returns this {@code Matrix} object's row size and column size contained in a {@code StorageBox<Integer, Integer>}.
	 * 
	 * @return {@code StorageBox<Integer, Integer>}
	 * @see StorageBox
	 */
	public StorageBox<Integer, Integer> getDimensions() { return new StorageBox<Integer, Integer>(rSize, cSize); }
	
	/**
	 * Returns a {@code Double} value at the specified row and column position. If the specified position is out of range, Double.NaN is returned instead.
	 * 
	 * @param rowPos {@code Integer}
	 * @param colPos {@code Integer}
	 * @return {@code Double}
	 */
	public double getVal(int rowPos, int colPos) {
		try {
			return rangeCheck(rowPos, colPos) ? table.getBoxWithObj(rowPos).getValue().get(colPos) : -1;
		} catch (Exception e) { return Double.NaN; }
	}
	
	/**
	 * Sets the {@code Double} value at the specified row and column position. If the specified position is out of range, no operation is performed.
	 * 
	 * @param rowPos {@code Integer}
	 * @param colPos {@code Integer}
	 * @param valIn {@code Double}
	 * @return This {@code Matrix} object.
	 */
	public Matrix setVal(int rowPos, int colPos, double valIn) {
		if (rangeCheck(rowPos, colPos)) {
			table.getBoxWithObj(rowPos).getValue().set(colPos, valIn);
		}
		return this;
	}
	
	/**
	 * Returns a {@code List} object containing the {@code Double} values on the specified row. If the specified row is out of range, null is returned instead.
	 * 
	 * @param rowNumIn {@code Integer}
	 * @see java.util.List
	 * @return A new List<Double> object.
	 */
	public List<Double> getRow(int rowNumIn) {
		return rangeCheck(rowNumIn, 0) ? table.getBoxWithObj(rowNumIn).getValue() : null;
	}
	
	/**
	 * Returns a {@code List} object containing the {@code Double} values on the specified column. If the specified column is out of range, null is returned instead.
	 * 
	 * @param colNumIn {@code Integer}
	 * @see java.util.List
	 * @return A new List<Double> object.
	 */
	public List<Double> getColumn(int colNumIn) {
		if (rangeCheck(0, colNumIn)) {
			EArrayList<Double> l = new EArrayList(cSize);
			for (int i = 0; i < rSize; i++) {
				StorageBox<Integer, List<Double>> box = table.getBoxWithObj(i);
				if (box != null && box.getValue() != null && box.getValue().get(colNumIn) != null) {
					l.add(i, box.getValue().get(colNumIn));
				}
				else {
					l.set(i, Double.NaN);
				}
			}
			return l;
		}
		return null;
	}
	
	/**
	 * Sets the row values at the specified position with the rowIn. If the specified position is out of range, no operation is performed.
	 * 
	 * @param rowNumIn {@code Integer}
	 * @param rowIn {@code List<Double>}
	 * @return This {@code Matrix} object.
	 */
	public Matrix setRow(int rowNumIn, List<Double> rowIn) {
		if (rangeCheck(rowNumIn, 0)) {
			table.setValueInBox(rowNumIn, rowIn);
		}
		return this;
	}
	
	/**
	 * Sets the column values at the specified position with the columnIn. If the specified position is out of range, no operation is performed.
	 * 
	 * @param rowNumIn {@code Integer}
	 * @param rowIn {@code List<Double>}
	 * @return This {@code Matrix} object.
	 */
	public Matrix setColumn(int columnNumIn, List<Double> columnIn) {
		if (rangeCheck(0, columnNumIn)) {
			for (int i = 0; i < rSize; i++) {
				StorageBox<Integer, List<Double>> box = table.getBoxWithObj(i);
				if (box != null) {
					box.getValue().set(i, columnIn.get(i));
				}
			}
		}
		return this;
	}
	
	/**
	 * Sets the row and column values of this {@code Matrix} with those of a {@code MatrixInt}. If the specified {@code MatrixInt} is null, no operation is performed.
	 * 
	 * @param matrixIn {@code MatrixInt}
	 * @return This {@code Matrix} object.
	 * @see MatrixInt
	 */
	public Matrix setValues(MatrixInt matrixIn) { return matrixIn != null ? setValues(matrixIn.toDouble()) : this; }
	
	/**
	 * Sets the row and column values of this {@code Matrix} with those of another {@code Matrix}. If the specified position {@code Matrix} is null, no operation is performed.
	 * 
	 * @param matrixIn {@code Matrix}
	 * @return This {@code Matrix} object.
	 */
	public Matrix setValues(Matrix matrixIn) {
		if (matrixIn != null) {
			rSize = matrixIn.getRowLength();
			cSize = matrixIn.getColumnLength();
			for (int i = 0; i < rSize; i++) {
				table.add(i, matrixIn.getRow(i));
			}
		}
		return this;
	}
	
	/**
	 * Clears all values stored within this {@code Matrix} object. It should be noted that this does not reset the row and column sizes however.
	 * 
	 * @return This {@code Matrix} object.
	 */
	public Matrix clearMatrix() {
		table.clear();
		for (int i = 0; i < rSize; i++) {
			EArrayList<Double> row = new EArrayList();
			table.add(i, row);
			for (int j = 0; j < cSize; j++) { row.add(0.0); }
		}
		return this;
	}
	
	/**
	 * Returns true if the specified row and column position is inside of this {@code Matrix}'s dimensions.
	 * 
	 * @param rowPos {@code Integer}
	 * @param colPos {@code Integer}
	 * @return {@code Boolean}
	 */
	public boolean rangeCheck(int rowPos, int colPos) {
		return (rowPos >= 0 && rowPos < rSize) && (colPos >= 0 && colPos < cSize);
	}
	
	/**
	 * Effectively return this this {@code Matrix} object. This method exists for parallelism between the {@code Matrix} and {@code MatrixInt} data types.
	 * 
	 * @return This {@code Matrix} object.
	 */
	public Matrix toDouble() { return this; }
	
	/**
	 * Returns a new {@code MatrixInt} with this {@code Matrix}'s values.
	 * 
	 * <p>Each double value is directly casted as an int in this operation meaning potential data loss may occur during conversion.
	 * 
	 * @return A new {@code MatrixInt} object.
	 */
	public MatrixInt toInt() {
		MatrixInt m = new MatrixInt(rSize, cSize);
		for (int i = 0; i < rSize; i++) {
			for (int j = 0; j < cSize; j++) {
				m.setVal(i, j, (int) getVal(i, j));
			}
		}
		return m;
	}
	
	/**
	 * Returns an {@code List<Double>} List containing all of this {@code Matrix}'s values. Values are entered row by row.
	 * 
	 * @return A {@code List<Double>} List.
	 */
	public List<Double> toList() {
		EArrayList<Double> l = new EArrayList();
		for (int i = 0; i < rSize; i++) {
			for (int j = 0; j < cSize; j++) {
				l.add(getVal(i, j));
			}
		}
		return l;
	}
	
	
	//math functions
	
	/**
	 * Multiplies each value in this {@code Matrix} object by the specified scalar value.
	 * 
	 * @param scaleFactor {@code Integer}
	 * @return This {@code Matrix} object.
	 */
	public Matrix scale(int scaleFactor) { return scale((double) scaleFactor); }
	
	/**
	 * Multiplies each value in this {@code Matrix} object by the specified scalar value.
	 * 
	 * @param scaleFactor {@code Float}
	 * @return This {@code Matrix} object.
	 */
	public Matrix scale(float scaleFactor) { return scale((double) scaleFactor); }
	
	/**
	 * Multiplies each value in this {@code Matrix} object by the specified scalar value.
	 * 
	 * @param scaleFactor {@code Double}
	 * @return This {@code Matrix} object.
	 */
	public Matrix scale(double scaleFactor) {
		for (int i = 0; i < rSize; i++) {
			for (int j = 0; j < cSize; j++) {
				setVal(i, j, getVal(i, j) * scaleFactor);
			}
		}
		return this;
	}
	
	/**
	 * Adds each value in this {@code Matrix} with each corresponding value of the specified {@code MatrixInt} object.
	 * 
	 * <p>Addition will only succeed if both this {@code Matrix} object and the specified {@code MatrixInt} object have the
	 * same dimensions; otherwise, no operation is performed.
	 * 
	 * @param matrixIn {@code MatrixInt}
	 * @return This {@code Matrix} object.
	 * @see MatrixInt
	 */
	public Matrix add(MatrixInt matrixIn) { return matrixIn != null ? add(matrixIn.toDouble()) : this; }
	
	/**
	 * Adds each value in this {@code Matrix} with each corresponding value of another specified {@code Matrix} object.
	 * 
	 * <p>Addition will only succeed if both this {@code Matrix} object and the specified {@code Matrix} object have the
	 * same dimensions; otherwise, no operation is performed.
	 * 
	 * @param matrixIn {@code Matrix}
	 * @return This {@code Matrix} object.
	 */
	public Matrix add(Matrix matrixIn) {
		if (compareDimensions(this, matrixIn)) {
			for (int i = 0; i < rSize; i++) {
				for (int j = 0; j < cSize; j++) {
					setVal(i, j, getVal(i, j) + matrixIn.getVal(i, j));
				}
			}
		}
		return this;
	}
	
	/**
	 * Subtracts the each value of the specified {@code MatrixInt} object from this {@code Matrix} object.
	 * 
	 * <p>Subtraction will only succeed if both this {@code Matrix} object and the specified {@code MatrixInt} object have the
	 * same dimensions; otherwise, no operation is performed.
	 * 
	 * @param matrixIn {@code MatrixInt}
	 * @return This {@code Matrix} object.
	 * @see MatrixInt
	 */
	public Matrix subtract(MatrixInt matrixIn) { return matrixIn != null ? subtract(matrixIn.toDouble()) : this; }
	
	/**
	 * Subtracts the each value of the specified {@code Matrix} object from this {@code Matrix} object.
	 * 
	 * <p>Subtraction will only succeed if both this {@code Matrix} object and the specified {@code Matrix} object have the
	 * same dimensions; otherwise, no operation is performed.
	 * 
	 * @param matrixIn {@code Matrix}
	 * @return This {@code Matrix} object.
	 */
	public Matrix subtract(Matrix matrixIn) {
		if (compareDimensions(this, matrixIn)) {
			for (int i = 0; i < rSize; i++) {
				for (int j = 0; j < cSize; j++) {
					setVal(i, j, getVal(i, j) - matrixIn.getVal(i, j));
				}
			}
		}
		return this;
	}
	
	/**
	 * Returns a new {@code Matrix} with the result of multiplying this {@code Matrix} object against a {@code MatrixInt} object.
	 * 
	 * <p>Integer values in the specified {@code MatrixInt} are first cast as doubles and are then multiplied.
	 * 
	 * <p>Multiplication will only succeed if both this {@code Matrix} object's rows equal the specified {@code MatrixInt}'s columns; 
	 * otherwise, this {@code Matrix} object is returned.
	 * 
	 * @param matrixIn {@code MatrixInt}
	 * @return A new {@code Matrix} object.
	 * @see MatrixInt
	 */
	public Matrix multiply(MatrixInt matrixIn) { return matrixIn != null ? multiply(matrixIn.toDouble()) : this; }
	
	/**
	 * Returns a new {@code Matrix} with the result of multiplying this {@code Matrix} object against another {@code Matrix} object.
	 * 
	 * <p>Integer values in the specified {@code Matrix} are first cast as doubles and are then multiplied.
	 * 
	 * <p>Multiplication will only succeed if both this {@code Matrix} object's rows equal the specified {@code Matrix}'s columns; 
	 * otherwise, this {@code Matrix} object is returned.
	 * 
	 * @param matrixIn {@code Matrix}
	 * @return A new {@code Matrix} object.
	 */
	public Matrix multiply(Matrix matrixIn) {
		if (multiplyCheck(this, matrixIn)) {
			Matrix m = new Matrix(rSize, matrixIn.getColumnLength());
			for (int t = 0; t < matrixIn.getColumnLength(); t++) {
				for (int i = 0; i < rSize; i++) {
					double sum = 0.0;
					for (int j = 0; j < cSize; j++) {
						sum += (getVal(i, j) * matrixIn.getVal(j, t));
					}
					m.setVal(i, t, sum);
				}
			}
			return m;
		}
		return this;
	}
	
	/**
	 * Swaps this {@code Matrix}'s rows and columns, effectively modifiying the original dimensions of this {@code Matrix} object.
	 * 
	 * @return This {@code Matrix} object.
	 */
	public Matrix transpose() {
		List<List<Double>> columns = new EArrayList();
		for (int i = 0; i < cSize; i++) { columns.add(getColumn(i)); } //grab old columns
		
		create(cSize, rSize);
		for (int i = 0; i < rSize; i++) { setRow(i, columns.get(i)); } //set rows as old columns
		
		return this;
	}
	
	
	//static methods
	
	/**
	 * Returns a {@code Matrix} object with values specified by the addition of two {@code Matrix} objects.
	 * 
	 * <p>If either matrix is null, no operation is performed.
	 * Depending on how many of the specified matrix arguments are null will determine what is actually returned by this method.
	 * 
	 * @param m1 {@code Matrix}
	 * @param m2 {@code Matrix}
	 * @return A new {@code Matrix} object.
	 */
	public static Matrix add(Matrix m1, Matrix m2) { return m1 != null ? (m2 != null ? m1.add(m2) : m1) : m2 != null ? m2 : null; }
	
	/**
	 * Returns a {@code Matrix} object with values specified by the addition of two {@code MatrixInt} objects.
	 * 
	 * <p>If either matrix is null, no operation is performed.
	 * Depending on how many of the specified matrix arguments are null will determine what is actually returned by this method.
	 * 
	 * @param m1 {@code MatrixInt}
	 * @param m2 {@code MatrixInt}
	 * @return A new {@code Matrix} object.
	 * @see MatrixInt
	 */
	public static Matrix add(MatrixInt m1, MatrixInt m2) { return m1 != null ? (m2 != null ? m1.toDouble().add(m2) : m1.toDouble()) : m2 != null ? m2.toDouble() : null; }
	
	/**
	 * Returns a {@code Matrix} object with values specified by the subtraction (m2 from m1) of two {@code Matrix} objects.
	 * 
	 * <p>If either matrix is null, no operation is performed.
	 * Depending on how many of the specified matrix arguments are null will determine what is actually returned by this method.
	 * 
	 * @param m1 {@code Matrix}
	 * @param m2 {@code Matrix}
	 * @return A new {@code Matrix} object.
	 */
	public static Matrix subtract(Matrix m1, Matrix m2) { return m1 != null ? (m2 != null ? m1.subtract(m2) : m1) : m2 != null ? m2 : null; }
	
	/**
	 * Returns a {@code Matrix} object with values specified by the subtraction (m2 from m1) of two {@code MatrixInt} objects.
	 * 
	 * <p>If either matrix is null, no operation is performed.
	 * Depending on how many of the specified matrix arguments are null will determine what is actually returned by this method.
	 * 
	 * @param m1 {@code MatrixInt}
	 * @param m2 {@code MatrixInt}
	 * @return A new {@code Matrix} object.
	 * @see MatrixInt
	 */
	public static Matrix subtract(MatrixInt m1, MatrixInt m2) { return m1 != null ? (m2 != null ? m1.toDouble().subtract(m2) : m1.toDouble()) : m2 != null ? m2.toDouble() : null; }
	
	/**
	 * Returns a {@code Matrix} object with values specified by the multiplication (m1 * m2) of two {@code Matrix} objects.
	 * 
	 * <p>If either matrix is null, no operation is performed.
	 * Depending on how many of the specified matrix arguments are null will determine what is actually returned by this method.
	 * 
	 * @param m1 {@code Matrix}
	 * @param m2 {@code Matrix}
	 * @return A new {@code Matrix} object.
	 */
	public static Matrix multiply(Matrix m1, Matrix m2) { return m1 != null ? (m2 != null ? m1.multiply(m2) : m1) : m2 != null ? m2 : null; }
	
	/**
	 * Returns a {@code Matrix} object with values specified by the multiplication (m1 * m2) of two {@code MatrixInt} objects.
	 * 
	 * <p>If either matrix is null, no operation is performed.
	 * Depending on how many of the specified matrix arguments are null will determine what is actually returned by this method.
	 * 
	 * @param m1 {@code MatrixInt}
	 * @param m2 {@code MatrixInt}
	 * @return A new {@code Matrix} object.
	 * @see MatrixInt
	 */
	public static Matrix multiply(MatrixInt m1, MatrixInt m2) { return m1 != null ? (m2 != null ? m1.toDouble().multiply(m2) : m1.toDouble()) : m2 != null ? m2.toDouble() : null; }
	
	/**
	 * Returns a {@code Matrix} object with values specified by the tranpose operation performed on a given {@code Matrix} object.
	 * 
	 * <p>If the specified matrixIn is null, no operation is performed and null is returned.
	 * 
	 * @param matrixIn {@code Matrix}
	 * @return The given matrixIn {@code Matrix} object.
	 */
	public static Matrix transpose(Matrix matrixIn) { return matrixIn != null ? matrixIn.transpose() : null; }
	
	/**
	 * Returns a {@code Matrix} object with values specified by the tranpose operation performed on a given {@code MatrixInt} object.
	 * 
	 * <p>The Integer values in the specified {@code MatrixInt} object are first cast to double and are then transposed.
	 * 
	 * <p>If the specified matrixIn is null, no operation is performed and null is returned.
	 * 
	 * @param matrixIn {@code MatrixInt}
	 * @return The given matrixIn {@code Matrix} object.
	 * @see MatrixInt
	 */
	public static Matrix transpose(MatrixInt matrixIn) { return matrixIn != null ? matrixIn.toDouble().transpose() : null; }
	
	/**
	 * Returns a new {@code MatrixInt} with the specified {@code Matrix}'s values.
	 * 
	 * <p>If the specified {@code Matrix} object is null, no operation is performed and null is returned.
	 * 
	 * @param matrixIn {@code Matrix}
	 * @return A new {@code MatrixInt} object.
	 */
	public static MatrixInt toInt(Matrix matrixIn) {
		if (matrixIn != null) {
			MatrixInt m = new MatrixInt(matrixIn.getDimensions());
			for (int i = 0; i < matrixIn.getRowLength(); i++) {
				for (int j = 0; j < matrixIn.getColumnLength(); j++) {
					m.setVal(i, j, (int) matrixIn.getVal(i, j));
				}
			}
			return m;
		}
		return null;
	}
	
	/**
	 * Returns an {@code List<Double>} List containing all of the specified {@code Matrix}'s values. Values are entered row by row.
	 * 
	 * <p>If the specified {@code Matrix} object is null, no operation is performed and an empty {@code List<Double>} is returned.
	 * 
	 * @param matrixIn {@code Matrix}
	 * @return A {@code List<Double>}.
	 */
	public static List<Double> toList(Matrix matrixIn) {
		EArrayList<Double> l = new EArrayList();
		for (int i = 0; i < matrixIn.getRowLength(); i++) {
			for (int j = 0; j < matrixIn.getColumnLength(); j++) {
				l.add(matrixIn.getVal(i, j));
			}
		}
		return l;
	}
	
	/**
	 * Returns true if both specified {@code Matrix} objects have the same row and column sizes.
	 * 
	 * <p>If either {@code Matrix} is null, false is returned by default.
	 * 
	 * @param m1 {@code Matrix}
	 * @param m2 {@code Matrix}
	 * @return {@code Boolean}
	 */
	public static boolean compareDimensions(Matrix m1, Matrix m2) {
		 return m1 != null && m2 != null ? m1.getDimensions().compareContents(m2.getDimensions()) : false;
	}
	
	/**
	 * Returns true if the specified {@code Matrix} objects can actually be multiplied together.
	 * 
	 * <p>If either {@code Matrix} is null, false is returned by default.
	 * 
	 * @param m1 {@code Matrix}
	 * @param m2 {@code Matrix}
	 * @return {@code Boolean}
	 */
	public static boolean multiplyCheck(Matrix m1, Matrix m2) {
		return m1 != null && m2 != null ? (m1.getColumnLength() == m2.getRowLength()) : false;
	}
	
	
	//object overrides
	
	@Override
	public String toString() {
		String returnString = "";
		int longest = getLongest(toList());
		int long1 = getLongest(getColumn(0));
		
		for (int i = 0; i < rSize; i++) {
			returnString += "|";
			for (int j = 0; j < cSize; j++) {
				String val = String.valueOf(getVal(i, j));
				int offset = j == 0 ? long1 - val.length() : longest - val.length();
				for (int p = 0; p < offset; p++) { returnString += " "; }
				
				returnString += getVal(i, j) + (printCommas ? ", " : " ");
			}
			returnString = returnString.substring(0, returnString.length() >= (printCommas ? 2 : 1) ? returnString.length() - (printCommas ? 2 : 1) : returnString.length());
			returnString += "|\n";
		}
		
		returnString = returnString.substring(0, returnString.length() >= 1 ? returnString.length() - 1 : returnString.length());
		
		return returnString;
	}
	
	private int getLongest(List<Double> list) {
		int returnVal = 0;
		
		for (double i : list) {
			String s = String.valueOf(i);
			if (s.length() > returnVal) { returnVal = s.length(); }
		}
		
		return returnVal;
	}
}
