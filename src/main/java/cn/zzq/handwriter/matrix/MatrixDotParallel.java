package cn.zzq.handwriter.matrix;

import cn.zzq.handwriter.matrix.exception.MatrixDotException;

import java.util.function.IntConsumer;
import java.util.stream.IntStream;

/**
 * 矩阵乘法并行
 */
public class MatrixDotParallel {

    public static Matrix dot(Matrix m1, Matrix m2) {
        //矩阵相乘的前提条件为矩阵的列数和另一个矩阵的行数相同
        if (m1.getColumnSize() != m2.getRowSize()) {
            throw new MatrixDotException(m1, m2);
        }

        int sameVal = m1.getColumnSize(); //保存中间相等的行列数据

        // 乘完之后，mat = this * other
        // 得到新矩阵mat的行数为this的行数
        // 列数为other的列数
        Matrix matrix = new Matrix(m1.getRowSize(), m2.getColumnSize());

        IntStream.range(0, matrix.getRowSize() * matrix.getColumnSize())    //生成一个用于循环的range
                .parallel() //并行化流
                .forEach(value -> { //并行化for循环
                    int row = value / matrix.getColumnSize();
                    int column = value % matrix.getColumnSize();

                    //新矩阵的行索引为row,列索引为column
                    double val = 0;
                    for (int i = 0; i < sameVal; i++) {
                        val += m1.get(row, i) * m2.get(i, column);
                    }
                    matrix.set(row, column, val);
                });
        return matrix;
    }

    public static void main(String[] args) {

        final int SIZE = 600;  //矩阵的规模为600*600
        //一次矩阵乘法需要进行600*600*600 = 216000000次(两亿一千六百万)次乘法运算
        for (int j = 0; j < 10; j++) {
            Matrix m1 = Matrix.normals(SIZE, SIZE, 0, 1);
            Matrix m2 = Matrix.normals(SIZE, SIZE, 0, 1);
            long ms = System.currentTimeMillis();
            m1.dot(m2);
            long deltaMs = System.currentTimeMillis() - ms;
            System.out.println(deltaMs);
        }
        for (int j = 0; j < 10; j++) {
            Matrix m1 = Matrix.normals(SIZE, SIZE, 0, 1);
            Matrix m2 = Matrix.normals(SIZE, SIZE, 0, 1);
            long ms = System.currentTimeMillis();
            dot(m1, m2);
            long deltaMs = System.currentTimeMillis() - ms;
            System.out.println(deltaMs);
        }
    }
}
