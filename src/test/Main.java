package test;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        int input;
        while (running) {
            showMainMenu();
            input = scanner.nextInt();
            switch (input) {
                case 1:
                    getInputAndAdd();
                    break;
                case 2:
                    getInputAndMultiplyByConstant();
                    break;
                case 3:
                    getInputAndMultiply();
                    break;
                case 4:
                    getInputAndTranspose();
                    break;
                case 5:
                    getInputAndFindDeterminant();
                    break;
                case 6:
                    getInputAndFindInverse();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    break;
            }
        }
    }

    private static void showMainMenu() {
        System.out.print("1. Add matrices\n" +
                "2. Multiply matrix by a constant\n" +
                "3. Multiply matrices\n" +
                "4. Transpose matrix\n" +
                "5. Calculate a determinant\n" +
                "6. Inverse matrix\n" +
                "0. Exit\n" +
                "Your choice: ");
    }

    private static void showTransposeMenu() {
        System.out.print("1. Main diagonal\n" +
                "2. Side diagonal\n" +
                "3. Vertical line\n" +
                "4. Horizontal line\n" +
                "Your choice: ");
    }

    private static Matrix parseTextMatrix(String[] dims) {
        Scanner scanner = new Scanner(System.in);
        Matrix m = new Matrix(Integer.parseInt(dims[0]), Integer.parseInt(dims[1]));
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < m.rows; i++) {
            builder.append(scanner.nextLine() + " ");
        }
        m.read(builder.toString());
        return m;
    }

    private static void getInputAndAdd() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter size of first matrix: ");
        String[] dims1 = scanner.nextLine().trim().split("\\s");
        System.out.print("Enter first matrix: ");
        Matrix m1 = parseTextMatrix(dims1);
        System.out.print("Enter size of second matrix: ");
        String[] dims2 = scanner.nextLine().trim().split("\\s");
        if (!dims1[0].equals(dims2[0]) || !dims1[1].equals(dims2[1])) {
            System.out.println("The operation cannot be performed.");
            return;
        }
        System.out.print("Enter second matrix: ");
        Matrix m2 = parseTextMatrix(dims2);
        System.out.println("The result is:");
        System.out.println(sum(m1, m2));
    }

    private static void getInputAndMultiplyByConstant() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter size of matrix: ");
        String[] dims = scanner.nextLine().trim().split("\\s");
        System.out.print("Enter matrix: ");
        Matrix m = parseTextMatrix(dims);
        System.out.print("Enter constant: ");
        double c = scanner.nextDouble();
        System.out.println("The result is:");
        System.out.println(multiplyByConstant(m, c));
    }

    private static void getInputAndMultiply() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter size of first matrix: ");
        String[] dims1 = scanner.nextLine().trim().split("\\s");
        System.out.print("Enter first matrix: ");
        Matrix m1 = parseTextMatrix(dims1);
        System.out.print("Enter size of second matrix: ");
        String[] dims2 = scanner.nextLine().trim().split("\\s");
        if (!dims1[1].equals(dims2[0])) {
            System.out.println("The operation cannot be performed.");
            return;
        }
        System.out.print("Enter second matrix: ");
        Matrix m2 = parseTextMatrix(dims2);
        System.out.println("The result is:");
        System.out.println(multiply(m1, m2));
    }

    private static void getInputAndTranspose() {
        Scanner scanner = new Scanner(System.in);
        showTransposeMenu();
        int selection = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter size of matrix: ");
        String[] dims = scanner.nextLine().trim().split("\\s");
        System.out.print("Enter matrix: ");
        Matrix m = parseTextMatrix(dims);
        Matrix result = transpose(m, selection);
        System.out.println("The result is:\n" + result);
    }

    private static void getInputAndFindDeterminant() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter size of matrix: ");
        String[] dims = scanner.nextLine().trim().split("\\s");
        if (!dims[0].equals(dims[1])) {
            System.out.println("The operation cannot be performed.");
            return;
        }
        System.out.print("Enter matrix: ");
        Matrix m = parseTextMatrix(dims);
        System.out.println("The result is:");
        double det = findDeterminant(m);
        if ((long) det == det) {
            System.out.println((long) det);
        } else {
            System.out.println(det);
        }
    }

    private static void getInputAndFindInverse() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter size of matrix: ");
        String[] dims = scanner.nextLine().trim().split("\\s");
        if (!dims[0].equals(dims[1])) {
            System.out.println("The operation cannot be performed.");
            return;
        }
        System.out.print("Enter matrix: ");
        Matrix m = parseTextMatrix(dims);
        ArrayList<Double> data = new ArrayList<>();

        double det = findDeterminant(m);
        if (det == 0) {
            System.out.println("This matrix doesn't have an inverse.");
            return;
        }
        System.out.println("The result is:");
        Matrix inverse = multiplyByConstant(transpose(findAdjoint(m), 1), 1.0 / det);
        System.out.println(inverse);
    }

    private static Matrix sum(Matrix m1, Matrix m2) {
        if (!m1.size.equals(m2.size)) {
            return null;
        } else {
            ArrayList<Double> resultData = new ArrayList<>();
            for (int i = 0; i < m1.data.size(); i++) {
                resultData.add(m1.data.get(i) + m2.data.get(i));
            }
            Matrix result = new Matrix(m1.rows, m1.cols);
            result.data = resultData;
            return result;
        }
    }

    private static Matrix multiplyByConstant(Matrix m, double constant) {
        ArrayList<Double> resultData = new ArrayList<>();
        m.data.stream().forEachOrdered(x -> resultData.add(x * constant));
        Matrix result = new Matrix(m.rows, m.cols);
        result.data = resultData;
        return result;
    }

    private static Matrix multiply(Matrix m1, Matrix m2) {
        if (m1.cols == m2.rows) {
            Matrix result = new Matrix(m1.rows, m2.cols);
            ArrayList<Double> resultData = new ArrayList<>();
            for (int i = 0; i < m1.rows; i++) {
                for (int j = 0; j < m2.cols; j++) {
                    ArrayList<Double> curRow = m1.getRow(i);
                    ArrayList<Double> curCol = m2.getColumn(j);
                    double cellTotal = 0;
                    for (int k = 0; k < curRow.size(); k++) {
                        cellTotal += curRow.get(k) * curCol.get(k);
                    }
                    resultData.add(cellTotal);
                }
            }
            result.data = resultData;
            return result;
        } else {
            return null;
        }
    }

    private static Matrix transpose(Matrix m, int operation) {
        Matrix result;
        ArrayList<Double> resultData = new ArrayList<>();
        switch (operation) {
            case 1:
                result = new Matrix(m.cols, m.rows);
                for (int colIndex = 0; colIndex < m.cols; colIndex++) {
                    for (double n : m.getColumn(colIndex)) {
                        resultData.add(n);
                    }
                }
                break;
            case 2:
                result = new Matrix(m.cols, m.rows);
                for (int colIndex = m.cols - 1; colIndex >= 0; colIndex--) {
                    for (int rowIndex = m.rows - 1; rowIndex >= 0; rowIndex--) {
                        resultData.add(m.getColumn(colIndex).get(rowIndex));
                    }
                }
                break;
            case 3:
                result = new Matrix(m.rows, m.cols);
                for (int rowIndex = 0; rowIndex < m.rows; rowIndex++) {
                    for (int colIndex = m.cols - 1; colIndex >= 0; colIndex--) {
                        resultData.add(m.getRow(rowIndex).get(colIndex));
                    }
                }
                break;
            case 4:
                result = new Matrix(m.rows, m.cols);
                for (int rowIndex = m.rows - 1; rowIndex >= 0; rowIndex--) {
                    for (int colIndex = 0; colIndex < m.cols; colIndex++) {
                        resultData.add(m.getRow(rowIndex).get(colIndex));
                    }
                }
                break;
            default:
                result = null;
                break;
        }
        result.data = resultData;
        return result;
    }

    private static double findDeterminant(Matrix m) {
        if (m.size.equals("2x2")) {
            return m.data.get(0) * m.data.get(3) - m.data.get(1) * m.data.get(2);
        } else {
            double total = 0;
            // make a smaller matrix by excluding rows and columns
            for (int colIndex = 0; colIndex < m.cols; colIndex++) {
                double cofactor = m.data.get(colIndex);
                Matrix minor = new Matrix(m.rows - 1, m.cols - 1);
                ArrayList<Double> minorData = new ArrayList<>();
                for (int index = m.cols; index < m.data.size(); index++) {
                    if (index % m.cols != colIndex) {
                        minorData.add(m.data.get(index));
                    }
                }
                minor.data = minorData;
                total += Math.pow(-1, colIndex) * cofactor * findDeterminant(minor);
            }
            return total;
        }
    }

    private static Matrix findAdjoint(Matrix m) {
        Matrix result = new Matrix(m.rows, m.cols);
        ArrayList<Double> resultData = new ArrayList<>();
        for (int row = 0; row < m.rows; row++) {
            for (int col = 0; col < m.cols; col++) {
                resultData.add(findCofactor(m, row, col));
            }
        }
        result.data = resultData;
        return result;
    }

    private static double findCofactor(Matrix m, int rowIndex, int colIndex) {
        Matrix minor = new Matrix(m.rows - 1, m.cols - 1);
        ArrayList<Double> minorData = new ArrayList<>();
        for (int row = 0; row < m.rows; row++) {
            for (int col = 0; col < m.cols; col++) {
                if (col != colIndex && row != rowIndex) {
                    int index = col + row * m.cols;
                    double datum = m.data.get(index);
                    minorData.add(datum);
                }
            }
        }
        minor.data = minorData;
        return Math.pow(-1, rowIndex + colIndex)  * findDeterminant(minor);
    }
}

class Matrix {
    int rows;
    int cols;
    String size;
    ArrayList<Double> data = new ArrayList<>();

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.size = rows + "x" + cols;
    }

    public ArrayList<Double> getColumn(int colIndex) {
        if (colIndex >= this.cols || colIndex < 0) {
            return null;
        } else {
            ArrayList<Double> results = new ArrayList<>();
            for (int index = colIndex; index < this.data.size(); index += this.cols) {
                results.add(this.data.get(index));
            }
            return results;
        }
    }

    public ArrayList<Double> getRow(int rowIndex) {
        if (rowIndex >= this.rows || rowIndex < 0) {
            return null;
        } else {
            ArrayList<Double> results = new ArrayList<>();
            for (int i = rowIndex * this.cols; i < (rowIndex + 1) * this.cols; i++) {
                results.add(this.data.get(i));
            }
            return results;
        }
    }

    public boolean read(String input) {
        String[] splitInput = input.trim().split("\\s");
        ArrayList<Double> buffer = new ArrayList<>();
        for (String cur : splitInput) {
            try {
                buffer.add(Double.parseDouble(cur));
            } catch (Exception e) {
                return false;
            }
        }
        data.addAll(buffer);
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                int index = i * this.cols + j;
                builder.append(format(this.data.get(index)) + " ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    private String format(double d) {
        if(d == (long) d)
            return String.format("%d",(long)d);
        else
            return String.format("%s",d);
    }
}
