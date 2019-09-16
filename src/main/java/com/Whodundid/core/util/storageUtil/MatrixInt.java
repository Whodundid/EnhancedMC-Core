package com.Whodundid.core.util.storageUtil;

import java.util.List;

//Created: 9-16-19
//Author: Hunter Bragg

/**
 * The {@code MatrixInt} class is a data type representing a matrix structure with {@code Integer} values.
 * The {@code MatrixInt} class provides numerous functions for performing matrix operations both locally and statically.
 * <blockquote><pre>
 *     MatrixInt m = new MatrixInt(2, 2, 1, 2, 3, 4);
 *     
 *     			|1 2|
 *     			|3 4|
 * </pre></blockquote><p>
 * @author Hunter Bragg
 */
public class MatrixInt {
	
	StorageBoxHolder<Integer, List<Integer>> table = new StorageBoxHolder();
	private int rSize = 0, cSize = 0;
	public boolean printCommas = false;
	
	/**
	 * Initializes a {@code MatrixInt} object with default size of 0 rows and 0 columns.
	 */
	public MatrixInt() { this(0, 0); }
	
	/**
	 * Initializes a {@code MatrixInt} object with row and column dimensions specified by a StorageBox<Integer, Integer>.
	 * 
	 * @see StorageBox
	 * @param dimIn {@code StorageBox<Integer, Integer>}
	 */
	public MatrixInt(StorageBox<Integer, Integer> dimIn) { this(dimIn.getObject(), dimIn.getValue()); }
	
	/**
	 * Initializes a {@code MatrixInt} with specified int values for rows and columns.
	 * 
	 * @param rowSizeIn {@code Integer}
	 * @param columnSizeIn {@code Integer}
	 */
	public MatrixInt(int rowSizeIn, int columnSizeIn) {
		create(rowSizeIn, columnSizeIn);
	}
	
	/**
	 * Initializes a {@code MatrixInt} with specified int values for rows and columns and attempts to fill it with a series of 
	 * EArrayList<Integer> containting each row's contents.
	 * 
	 * <p>If the number of given EArrayList<Integer> for row contents does not match the specified rowSizeIn value, the initialization
	 * process ignores the provided arguments.
	 * 
	 * @param rowSizeIn {@code Integer}
	 * @param columnSizeIn {@code Integer}
	 * @param rows {@code EArrayList<Integer>}
	 * 
	 * @see EArrayList
	 */
	public MatrixInt(int rowSizeIn, int columnSizeIn, List<Integer>... rows) {
		rSize = rowSizeIn;
		cSize = columnSizeIn;
		for (int i = 0; i < rSize; i++) {
			table.add(i, rows[i]);
		}
	}
	
	/**
	 * Initializes a {@code MatrixInt} with specified int values for rows and columns and attempts to fill it with provided double values.
	 * 
	 * <p>The double values are converted to integers in this process.
	 * 
	 * <p>If the number of given vals does not match the intended matrix size of (rowSizeIn * columnSizeIn), the initialization
	 * process will ignore the provided arguments.
	 * 
	 * @param rowSizeIn {@code Integer}
	 * @param columnSizeIn {@code Integer}
	 * @param vals {@code Double[]}
	 */
	public MatrixInt(int rowSizeIn, int columnSizeIn, Number... vals) {
		create(rowSizeIn, columnSizeIn);
		if (vals.length == rowSizeIn * columnSizeIn) {
			int q = 0;
			for (int j = 0; j < rSize; j++) {
				for (int i = 0; i < cSize; i++) {
					setVal(j, i, (int) vals[q].intValue());
					q++;
				}
			}
		}
	}
	
	/**
	 * Initializes a {@code MatrixInt} with specified int values for rows and columns and attempts to fill it with a provided list containing Integer values.
	 * 
	 * <p>If the number of given vals does not match the intended matrix size of (rowSizeIn * columnSizeIn), the initialization
	 * process will ignore the provided arguments.
	 * 
	 * @param rowSizeIn {@code Integer}
	 * @param columnSizeIn {@code Integer}
	 * @param vals {@code List<Integer>}
	 */
	public MatrixInt(int rowSizeIn, int columnSizeIn, List<Integer> vals) {
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
	 * Initializes a {@code MatrixInt} with dimensions and values from an existing {@code Matrix} object.
	 * 
	 * <p>The double values are converted to integers in this process.
	 * 
	 * <p>If the specified {@code Matrix} object is null, the argument is ignored and an empty matrix will be created instead.
	 * 
	 * @param vals {@code Matrix}
	 * @see Matrix
	 */
	public MatrixInt(Matrix matrixIn) { if (matrixIn != null) { setValues(matrixIn.toInt()); } else { create(0, 0); } }
	
	/**
	 * Initializes a {@code MatrixInt} with dimensions and values from an existing {@code MatrixInt} object.
	 * 
	 * <p>If the specified {@code MatrixInt} object is null, the argument is ignored and an empty matrix will be created instead.
	 * 
	 * @param vals {@code MatrixInt}
	 */
	public MatrixInt(MatrixInt matrixIn) { if (matrixIn != null) { setValues(matrixIn); } else { create(0, 0); } }
	
	
	//base methods
	
	/**
	 * Returns true if this {@code MatrixInt} object will print commas ',' between each value when printing to the console.
	 * 
	 * @return {@code Boolean}
	 */
	public boolean printsCommas() { return printCommas; }
	
	/**
	 * Specifies whether this {@code MatrixInt} object will print commas ',' between each value when printing to the console.
	 * 
	 * @param valIn {@code Boolean}
	 * @return This {@code MatrixInt} object.
	 */
	public MatrixInt setPrintCommas(boolean valIn) { printCommas = valIn; return this; }
	
	
	//matrix util functions
	
	/**
	 * Internal function used to initialize a {@code MatrixInt} with the specified row and columns sizes.
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
	 * Returns this {@code MatrixInt} object's row size.
	 * 
	 * @return {@code Integer}
	 */
	public int getRowLength() { return rSize; }
	
	/**
	 * Returns this {@code MatrixInt} object's column size.
	 * 
	 * @return {@code Integer}
	 */
	public int getColumnLength() { return cSize; }
	
	/**
	 * Returns this {@code MatrixInt} object's row size and column size contained in a {@code StorageBox<Integer, Integer>}.
	 * 
	 * @return {@code StorageBox<Integer, Integer>}
	 * @see StorageBox
	 */
	public StorageBox<Integer, Integer> getDimensions() { return new StorageBox<Integer, Integer>(rSize, cSize); }
	
	/**
	 * Returns a {@code Integer} value at the specified row and column position. If the specified position is out of range, Integer.MIN_VALUE is returned instead.
	 * 
	 * @param rowPos {@code Integer}
	 * @param colPos {@code Integer}
	 * @return {@code Integer}
	 */
	public int getVal(int rowPos, int colPos) {
		try {
			return rangeCheck(rowPos, colPos) ? table.getBoxWithObj(rowPos).getValue().get(colPos) : -1;
		} catch (Exception e) { return Integer.MIN_VALUE; }
	}
	
	/**
	 * Sets the {@code Integer} value at the specified row and column position. If the specified position is out of range, no operation is performed.
	 * 
	 * @param rowPos {@code Integer}
	 * @param colPos {@code Integer}
	 * @param valIn {@code Integer}
	 * @return This {@code MatrixInt} object.
	 */
	public MatrixInt setVal(int rowPos, int colPos, int valIn) {
		if (rangeCheck(rowPos, colPos)) {
			table.getBoxWithObj(rowPos).getValue().set(colPos, valIn);
		}
		return this;
	}
	
	/**
	 * Returns a {@code List} object containing the {@code Integer} values on the specified row. If the specified row is out of range, null is returned instead.
	 * 
	 * @param rowNumIn {@code Integer}
	 * @see java.util.List
	 * @return A new List<Integer> object.
	 */
	public List<Integer> getRow(int rowNumIn) {
		return rangeCheck(rowNumIn, 0) ? table.getBoxWithObj(rowNumIn).getValue() : null;
	}
	
	/**
	 * Returns a {@code List} object containing the {@code Integer} values on the specified column. If the specified column is out of range, null is returned instead.
	 * 
	 * @param colNumIn {@code Integer}
	 * @see java.util.List
	 * @return A new List<Integer> object.
	 */
	public List<Integer> getColumn(int colNumIn) {
		if (rangeCheck(0, colNumIn)) {
			EArrayList<Integer> l = new EArrayList(cSize);
			for (int i = 0; i < rSize; i++) {
				StorageBox<Integer, List<Integer>> box = table.getBoxWithObj(i);
				if (box != null && box.getValue() != null && box.getValue().get(colNumIn) != null) {
					l.add(i, box.getValue().get(colNumIn));
				}
				else {
					l.set(i, Integer.MIN_VALUE);
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
	 * @param rowIn {@code List<Integer>}
	 * @return This {@code MatrixInt} object.
	 */
	public MatrixInt setRow(int rowNumIn, List<Integer> rowIn) {
		if (rangeCheck(rowNumIn, 0)) {
			table.setValueInBox(rowNumIn, rowIn);
		}
		return this;
	}
	
	/**
	 * Sets the column values at the specified position with the columnIn. If the specified position is out of range, no operation is performed.
	 * 
	 * @param rowNumIn {@code Integer}
	 * @param rowIn {@code List<Integer>}
	 * @return This {@code MatrixInt} object.
	 */
	public MatrixInt setColumn(int columnNumIn, List<Integer> columnIn) {
		if (rangeCheck(0, columnNumIn)) {
			for (int i = 0; i < rSize; i++) {
				StorageBox<Integer, List<Integer>> box = table.getBoxWithObj(i);
				if (box != null) {
					box.getValue().set(i, columnIn.get(i));
				}
			}
		}
		return this;
	}
	
	/**
	 * Sets the row and column values of this {@code MatrixInt} with those of a {@code Matrix}. If the specified {@code Matrix} is null, no operation is performed.
	 * 
	 * @param matrixIn {@code Matrix}
	 * @return This {@code MatrixInt} object.
	 * @see Matrix
	 */
	public MatrixInt setValues(Matrix matrixIn) { return matrixIn != null ? setValues(matrixIn.toInt()) : this; }
	
	/**
	 * Sets the row and column values of this {@code MatrixInt} with those of another {@code MatrixInt}. If the specified {@code MatrixInt} is null, no operation is performed.
	 * 
	 * @param matrixIn {@code MatrixInt}
	 * @return This {@code MatrixInt} object.
	 */
	public MatrixInt setValues(MatrixInt matrixIn) {
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
	 * @return This {@code MatrixInt} object.
	 */
	public MatrixInt clearMatrix() {
		table.clear();
		for (int i = 0; i < rSize; i++) {
			EArrayList<Integer> row = new EArrayList();
			table.add(i, row);
			for (int j = 0; j < cSize; j++) { row.add(0); }
		}
		return this;
	}
	
	/**
	 * Returns true if the specified row and column position is inside of this {@code MatrixInt}'s dimensions.
	 * 
	 * @param rowPos {@code Integer}
	 * @param colPos {@code Integer}
	 * @return {@code Boolean}
	 */
	public boolean rangeCheck(int rowPos, int colPos) {
		return (rowPos >= 0 && rowPos < rSize) && (colPos >= 0 && colPos < cSize);
	}
	
	/**
	 * Effectively return this this {@code MatrixInt} object. This method exists for parallelism between the {@code Matrix} and {@code MatrixInt} data types.
	 * 
	 * @return This {@code MatrixInt} object.
	 */
	public MatrixInt toInt() { return this; }
	
	/**
	 * Returns a new {@code Matrix} with this {@code MatrixInt}'s values.
	 * 
	 * <p>Each int value is directly casted as an double in this operation.
	 * 
	 * @return A new {@code Matrix} object.
	 */
	public Matrix toDouble() {
		Matrix m = new Matrix(rSize, cSize);
		for (int i = 0; i < rSize; i++) {
			for (int j = 0; j < cSize; j++) {
				m.setVal(i, j, (double) getVal(i, j));
			}
		}
		return m;
	}
	
	/**
	 * Returns an {@code List<Integer>} List containing all of this {@code MatrixInt}'s values. Values are entered row by row.
	 * 
	 * @return A {@code List<Integer>} List.
	 */
	public List<Integer> toList() {
		EArrayList<Integer> l = new EArrayList();
		for (int i = 0; i < rSize; i++) {
			for (int j = 0; j < cSize; j++) {
				l.add(getVal(i, j));
			}
		}
		return l;
	}
	
	
	//math functions
	
	/**
	 * Multiplies each value in this {@code MatrixInt} object by the specified scalar value.
	 * 
	 * @param scaleFactor {@code Double}
	 * @return This {@code MatrixInt} object.
	 */
	public MatrixInt scale(double scaleFactor) { return scale((int) scaleFactor); }
	
	/**
	 * Multiplies each value in this {@code MatrixInt} object by the specified scalar value.
	 * 
	 * @param scaleFactor {@code Float}
	 * @return This {@code MatrixInt} object.
	 */
	public MatrixInt scale(float scaleFactor) { return scale((int) scaleFactor); }
	
	/**
	 * Multiplies each value in this {@code MatrixInt} object by the specified scalar value.
	 * 
	 * @param scaleFactor {@code Integer}
	 * @return This {@code MatrixInt} object.
	 */
	public MatrixInt scale(int scaleFactor) {
		for (int i = 0; i < rSize; i++) {
			for (int j = 0; j < cSize; j++) {
				setVal(i, j, getVal(i, j) * scaleFactor);
			}
		}
		return this;
	}
	
	/**
	 * Adds each value in this {@code MatrixInt} with each corresponding value of the specified {@code Matrix} object.
	 * 
	 * <p>Addition will only succeed if both this {@code MatrixInt} object and the specified {@code Matrix} object have the
	 * same dimensions; otherwise, no operation is performed.
	 * 
	 * @param matrixIn {@code Matrix}
	 * @return This {@code MatrixInt} object.
	 * @see Matrix
	 */
	public MatrixInt add(Matrix matrixIn) { return matrixIn != null ? add(matrixIn.toInt()) : this; }
	
	/**
	 * Adds each value in this {@code MatrixInt} with each corresponding value of another specified {@code MatrixInt} object.
	 * 
	 * <p>Addition will only succeed if both this {@code MatrixInt} object and the specified {@code MatrixInt} object have the
	 * same dimensions; otherwise, no operation is performed.
	 * 
	 * @param matrixIn {@code MatrixInt}
	 * @return This {@code MatrixInt} object.
	 */
	public MatrixInt add(MatrixInt matrixIn) {
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
	 * Subtracts the each value of the specified {@code Matrix} object from this {@code MatrixInt} object.
	 * 
	 * <p>Subtraction will only succeed if both this {@code MatrixInt} object and the specified {@code Matrix} object have the
	 * same dimensions; otherwise, no operation is performed.
	 * 
	 * @param matrixIn {@code Matrix}
	 * @return This {@code MatrixInt} object.
	 * @see Matrix
	 */
	public MatrixInt subtract(Matrix matrixIn) { return matrixIn != null ? subtract(matrixIn.toInt()) : this; }
	
	/**
	 * Subtracts the each value of the specified {@code MatrixInt} object from this {@code MatrixInt} object.
	 * 
	 * <p>Subtraction will only succeed if both this {@code MatrixInt} object and the specified {@code MatrixInt} object have the
	 * same dimensions; otherwise, no operation is performed.
	 * 
	 * @param matrixIn {@code MatrixInt}
	 * @return This {@code MatrixInt} object.
	 */
	public MatrixInt subtract(MatrixInt matrixIn) {
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
	 * Returns a new {@code MatrixInt} with the result of multiplying this {@code MatrixInt} object against a {@code Matrix} object.
	 * 
	 * <p>Double values in the specified {@code Matrix} are first cast as integers and are then multiplied.
	 * 
	 * <p>Multiplication will only succeed if both this {@code MatrixInt} object's rows equal the specified {@code Matrix}'s columns; 
	 * otherwise, this {@code MatrixInt} object is returned.
	 * 
	 * @param matrixIn {@code Matrix}
	 * @return A new {@code MatrixInt} object.
	 * @see Matrix
	 */
	public MatrixInt multiply(Matrix matrixIn) { return matrixIn != null ? multiply(matrixIn.toInt()) : this; }
	
	/**
	 * Returns a new {@code MatrixInt} with the result of multiplying this {@code MatrixInt} object against another {@code MatrixInt} object.
	 * 
	 * <p>Double values in the specified {@code MatrixInt} are first cast as integers and are then multiplied.
	 * 
	 * <p>Multiplication will only succeed if both this {@code MatrixInt} object's rows equal the specified {@code MatrixInt}'s columns; 
	 * otherwise, this {@code MatrixInt} object is returned.
	 * 
	 * @param matrixIn {@code MatrixInt}
	 * @return A new {@code MatrixInt} object.
	 */
	public MatrixInt multiply(MatrixInt matrixIn) {
		if (multiplyCheck(this, matrixIn)) {
			MatrixInt m = new MatrixInt(rSize, matrixIn.getColumnLength());
			for (int t = 0; t < matrixIn.getColumnLength(); t++) {
				for (int i = 0; i < rSize; i++) {
					int sum = 0;
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
	 * Swaps this {@code MatrixInt}'s rows and columns, effectively modifiying the original dimensions of this {@code MatrixInt} object.
	 * 
	 * @return This {@code MatrixInt} object.
	 */
	public MatrixInt transpose() {
		List<List<Integer>> columns = new EArrayList();
		for (int i = 0; i < cSize; i++) { columns.add(getColumn(i)); } //grab old columns
		
		create(cSize, rSize);
		for (int i = 0; i < rSize; i++) { setRow(i, columns.get(i)); } //set rows as old columns
		
		return this;
	}
	
	
	//static methods
	
	/**
	 * Returns a {@code MatrixInt} object with values specified by the addition of two {@code Matrix} objects.
	 * 
	 * <p>If either matrix is null, no operation is performed.
	 * Depending on how many of the specified matrix arguments are null will determine what is actually returned by this method.
	 * 
	 * @param m1 {@code Matrix}
	 * @param m2 {@code Matrix}
	 * @return A new {@code MatrixInt} object.
	 * @see Matrix
	 */
	public static MatrixInt add(Matrix m1, Matrix m2) { return m1 != null ? (m2 != null ? m1.toInt().add(m2.toInt()) : m1.toInt()) : m2 != null ? m2.toInt() : null; }
	
	/**
	 * Returns a {@code MatrixInt} object with values specified by the addition of two {@code MatrixInt} objects.
	 * 
	 * <p>If either matrix is null, no operation is performed.
	 * Depending on how many of the specified matrix arguments are null will determine what is actually returned by this method.
	 * 
	 * @param m1 {@code MatrixInt}
	 * @param m2 {@code MatrixInt}
	 * @return A new {@code MatrixInt} object.
	 */
	public static MatrixInt add(MatrixInt m1, MatrixInt m2) { return m1 != null ? (m2 != null ? m1.add(m2) : m1) : m2 != null ? m2 : null; }
	
	/**
	 * Returns a {@code MatrixInt} object with values specified by the subtraction (m2 from m1) of two {@code Matrix} objects.
	 * 
	 * <p>If either matrix is null, no operation is performed.
	 * Depending on how many of the specified matrix arguments are null will determine what is actually returned by this method.
	 * 
	 * @param m1 {@code Matrix}
	 * @param m2 {@code Matrix}
	 * @return A new {@code MatrixInt} object.
	 * @see Matrix
	 */
	public static MatrixInt subtract(Matrix m1, Matrix m2) { return m1 != null ? (m2 != null ? m1.toInt().subtract(m2.toInt()) : m1.toInt()) : m2 != null ? m2.toInt() : null; }
	
	/**
	 * Returns a {@code MatrixInt} object with values specified by the subtraction (m2 from m1) of two {@code MatrixInt} objects.
	 * 
	 * <p>If either matrix is null, no operation is performed.
	 * Depending on how many of the specified matrix arguments are null will determine what is actually returned by this method.
	 * 
	 * @param m1 {@code MatrixInt}
	 * @param m2 {@code MatrixInt}
	 * @return A new {@code MatrixInt} object.
	 */
	public static MatrixInt subtract(MatrixInt m1, MatrixInt m2) { return m1 != null ? (m2 != null ? m1.subtract(m2) : m1) : m2 != null ? m2 : null; }
	
	/**
	 * Returns a {@code MatrixInt} object with values specified by the multiplication (m1 * m2) of two {@code Matrix} objects.
	 * 
	 * <p>If either matrix is null, no operation is performed.
	 * Depending on how many of the specified matrix arguments are null will determine what is actually returned by this method.
	 * 
	 * @param m1 {@code Matrix}
	 * @param m2 {@code Matrix}
	 * @return A new {@code MatrixInt} object.
	 * @see Matrix
	 */
	public static MatrixInt multiply(Matrix m1, Matrix m2) { return m1 != null ? (m2 != null ? m1.toInt().multiply(m2.toInt()) : m1.toInt()) : m2 != null ? m2.toInt() : null; }
	
	/**
	 * Returns a {@code MatrixInt} object with values specified by the multiplication (m1 * m2) of two {@code MatrixInt} objects.
	 * 
	 * <p>If either matrix is null, no operation is performed.
	 * Depending on how many of the specified matrix arguments are null will determine what is actually returned by this method.
	 * 
	 * @param m1 {@code MatrixInt}
	 * @param m2 {@code MatrixInt}
	 * @return A new {@code MatrixInt} object.
	 */
	public static MatrixInt multiply(MatrixInt m1, MatrixInt m2) { return m1 != null ? (m2 != null ? m1.multiply(m2) : m1) : m2 != null ? m2 : null; }
	
	/**
	 * Returns a {@code MatrixInt} object with values specified by the tranpose operation performed on a given {@code MatrixInt} object.
	 * 
	 * <p>If the specified matrixIn is null, no operation is performed and null is returned.
	 * 
	 * @param matrixIn {@code MatrixInt}
	 * @return The given matrixIn {@code MatrixInt} object.
	 */
	public static MatrixInt transpose(MatrixInt matrixIn) { return matrixIn != null ? matrixIn.transpose() : null; }
	
	/**
	 * Returns a {@code MatrixInt} object with values specified by the tranpose operation performed on a given {@code Matrix} object.
	 * 
	 * <p>The Double values in the specified {@code Matrix} object are first cast to integers and are then transposed.
	 * 
	 * <p>If the specified matrixIn is null, no operation is performed and null is returned.
	 * 
	 * @param matrixIn {@code Matrix}
	 * @return The given matrixIn {@code MatrixInt} object.
	 */
	public static MatrixInt transpose(Matrix matrixIn) { return matrixIn != null ? matrixIn.toInt().transpose() : null; }
	
	/**
	 * Returns a new {@code Matrix} with the specified {@code MatrixInt}'s values.
	 * 
	 * <p>If the specified {@code MatrixInt} object is null, no operation is performed and null is returned.
	 * 
	 * @param matrixIn {@code MatrixInt}
	 * @return A new {@code Matrix} object.
	 */
	public static Matrix toDouble(MatrixInt matrixIn) {
		if (matrixIn != null) {
			Matrix m = new Matrix(matrixIn.getDimensions());
			for (int i = 0; i < matrixIn.getRowLength(); i++) {
				for (int j = 0; j < matrixIn.getColumnLength(); j++) {
					m.setVal(i, j, (double) matrixIn.getVal(i, j));
				}
			}
			return m;
		}
		return null;
	}
	
	/**
	 * Returns an {@code List<Integer>} List containing all of the specified {@code MatrixInt}'s values. Values are entered row by row.
	 * 
	 * <p>If the specified {@code MatrixInt} object is null, no operation is performed and an empty {@code List<Integer>} is returned.
	 * 
	 * @param matrixIn {@code MatrixInt}
	 * @return A {@code List<Integer>}.
	 */
	public static List<Integer> toList(MatrixInt matrixIn) {
		EArrayList<Integer> l = new EArrayList();
		for (int i = 0; i < matrixIn.getRowLength(); i++) {
			for (int j = 0; j < matrixIn.getColumnLength(); j++) {
				l.add(matrixIn.getVal(i, j));
			}
		}
		return l;
	}
	
	/**
	 * Returns true if both specified {@code MatrixInt} objects have the same row and column sizes.
	 * 
	 * <p>If either {@code MatrixInt} is null, false is returned by default.
	 * 
	 * @param m1 {@code MatrixInt}
	 * @param m2 {@code MatrixInt}
	 * @return {@code Boolean}
	 */
	public static boolean compareDimensions(MatrixInt m1, MatrixInt m2) {
		 return m1 != null && m2 != null ? m1.getDimensions().compareContents(m2.getDimensions()) : false;
	}
	
	/**
	 * Returns true if the specified {@code MatrixInt} objects can actually be multiplied together.
	 * 
	 * <p>If either {@code MatrixInt} is null, false is returned by default.
	 * 
	 * @param m1 {@code MatrixInt}
	 * @param m2 {@code MatrixInt}
	 * @return {@code Boolean}
	 */
	public static boolean multiplyCheck(MatrixInt m1, MatrixInt m2) {
		return m1 != null && m2 != null ? (m1.getColumnLength() == m2.getRowLength()) : false;
	}
	
	
	//objects overrides
	
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
	
	private int getLongest(List<Integer> list) {
		int returnVal = 0;
		
		for (int i : list) {
			String s = String.valueOf(i);
			if (s.length() > returnVal) { returnVal = s.length(); }
		}
		
		return returnVal;
	}
}
