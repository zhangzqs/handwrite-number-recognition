package cn.zzq.handwrite.matrix.exception;

import cn.zzq.handwrite.matrix.Matrix;

public class MatrixReshapeException extends RuntimeException{

    public MatrixReshapeException(Matrix matrix, int targetRow, int targetColumn){
        super(String.format("Reshape cn.zzq.handwriter.matrix (%d, %d) to (%d, %d) failed!!!",matrix.getRowSize(),matrix.getColumnSize(),targetRow,targetColumn));
    }
}
