package cn.zzq.handwriter.matrix.exception;

import cn.zzq.handwriter.matrix.Matrix;

public class MatrixShapeException extends RuntimeException{
    public MatrixShapeException(int expectedRow,int expectedColumn,int receivedRow,int receivedColumn){
        super(String.format("Excepted a cn.zzq.handwriter.matrix(%d, %d), but received (%d, %d)", expectedRow, expectedColumn, receivedRow, receivedColumn));
    }

    public MatrixShapeException(Matrix expectedMatrix, Matrix receivedMatrix){
        this(expectedMatrix.getRowSize(),expectedMatrix.getRowSize(), receivedMatrix.getRowSize(), receivedMatrix.getColumnSize());
    }
}
