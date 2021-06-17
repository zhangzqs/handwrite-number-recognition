package cn.zzq.handwriter.matrix.exception;

public class MatrixIndexOutOfBoundException extends IndexOutOfBoundsException{
    public MatrixIndexOutOfBoundException() {
    }

    public MatrixIndexOutOfBoundException(String s) {
        super(s);
    }

    public MatrixIndexOutOfBoundException(int row,int column,int rowSize,int columnSize) {
        super(String.format("Matrix index out of range: (row: %d, column: %d, rowSize: %d, columnSize: %d)",row,column,rowSize,columnSize));
    }
}
