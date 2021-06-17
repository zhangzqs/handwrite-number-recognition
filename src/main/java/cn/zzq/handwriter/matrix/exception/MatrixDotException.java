package cn.zzq.handwriter.matrix.exception;

import cn.zzq.handwriter.matrix.Matrix;

public class MatrixDotException extends RuntimeException{
    public MatrixDotException(Matrix m1, Matrix m2){
        super(String.format("Matrix1(%d, %d) and Matrix2(%d, %d) cannot to dot",m1.getRowSize(),m1.getColumnSize(),m2.getRowSize(),m2.getColumnSize()));
    }
}
