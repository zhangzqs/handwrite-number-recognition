package cn.zzq.handwrite.matrix;

import cn.zzq.handwrite.matrix.exception.MatrixDotException;
import cn.zzq.handwrite.matrix.exception.MatrixIndexOutOfBoundException;
import cn.zzq.handwrite.matrix.exception.MatrixReshapeException;
import cn.zzq.handwrite.matrix.exception.MatrixShapeException;

import java.util.Arrays;
import java.util.Random;

public class Matrix{
    private int rowSize;
    private int columnSize;

    final double[] data;

    /**
     * 初始化行列
     * @param rowSize 矩阵的行
     * @param columnSize 矩阵的列
     */
    public Matrix(int rowSize,int columnSize){
        this.rowSize = rowSize;
        this.columnSize = columnSize;
        this.data = new double[rowSize * columnSize];
    }

    /**
     * 从数组初始化一个行向量
     * @param data 行向量元素值
     */
    public Matrix(double[] data){
        this.rowSize = 1;
        this.columnSize = data.length;
        this.data = data;
    }


    /**
     * 通过数组构造一个行向量
     * @param data 行向量所有元素值
     * @return 返回构造出来的矩阵
     */
    public static Matrix valueOf(double... data){
        return new Matrix(data);
    }

    public static Matrix valueOf(int... data){
        return new Matrix(Arrays.stream(data).asDoubleStream().toArray());
    }

    public static Matrix valueOf(byte... data){
        double[] doubleData = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            doubleData[i] = Byte.toUnsignedInt(data[i]);
        }
        return new Matrix(doubleData);
    }

    public void fill(Matrix other){
        System.arraycopy(other.data, 0, this.data, 0, data.length);
    }

    /**
     * 获取一个单位矩阵
     * @param dimension 矩阵的维度
     * @return 单位矩阵
     */
    public static Matrix identity(int dimension){
        Matrix matrix = new Matrix(dimension,dimension);
        for(int i=0;i<dimension;i++){
            matrix.set(i,i,1);
        }
        return matrix;
    }

    public static Matrix ones(int rowSize,int columnSize){
        Matrix matrix = new Matrix(rowSize,columnSize);
        for(int row = 0;row < rowSize;row++){
            for(int column = 0;column < columnSize;column++){
                matrix.set(row,column,1);
            }
        }
        return matrix;
    }
    /**
     * 按照正态分布生成一个随机矩阵
     * @param rowSize 行数
     * @param columnSize 列数
     * @param loc 正态分布的均值
     * @param scale 正态分布的标准差
     * @return 返回该矩阵
     */
    public static Matrix normals(int rowSize,int columnSize,double loc,double scale){
        Random r = new Random();
        Matrix matrix = new Matrix(rowSize,columnSize);
        for(int row = 0;row < rowSize;row++){
            for(int column = 0;column < columnSize;column++){
                matrix.set(row,column,scale*r.nextGaussian()+loc);
            }
        }
        return matrix;
    }

    /**
     * 矩阵清零
     */
    public void clear(){
        Arrays.fill(data, 0);
    }


    /**
     * 拷贝出一个新矩阵
     * @return 新矩阵
     */
    public Matrix copy() {
        Matrix matrix = Matrix.valueOf(this.data.clone());
        matrix.reshape(this.rowSize,this.columnSize);
        return matrix;
    }

    /**
     * 获取矩阵的行数
     * @return 矩阵行数
     */
    public int getRowSize() {
        return rowSize;
    }

    /**
     * 获取矩阵的列数
     * @return 矩阵列数
     */
    public int getColumnSize() {
        return columnSize;
    }

    /**
     * 获取矩阵的值
     * @param row 行
     * @param column 列
     * @return 值
     */
    public double get(int row, int column) {
        if(row >= getRowSize() || column >= getColumnSize()){
            throw new MatrixIndexOutOfBoundException(row,column,getRowSize(),getColumnSize());
        }
        return this.data[row * getColumnSize() + column];
    }

    public IVector get(final int row){
        return new IVector() {
            @Override
            public double get(int index) {
                return Matrix.this.get(row,index);
            }

            @Override
            public void set(int index,double value) {
                Matrix.this.set(row,index,value);
            }

            @Override
            public int size() {
                return getColumnSize();
            }
        };
    }
    /**
     * 设置矩阵的值
     * @param row 行
     * @param column 列
     * @param value 值
     */
    public void set(int row, int column, double value) {
        if(row >= getRowSize() || column >= getColumnSize()){
            throw new MatrixIndexOutOfBoundException(row,column,getRowSize(),getColumnSize());
        }
        this.data[row * getColumnSize() + column] = value;
    }

    /**
     * 重定义矩阵的形状
     * @param rowSize 行数
     * @param columnSize 列数
     */
    public void reshape(int rowSize,int columnSize){
        if(this.rowSize * this.columnSize != rowSize * columnSize){
            throw new MatrixReshapeException(this,rowSize,columnSize);
        }
        this.rowSize = rowSize;
        this.columnSize = columnSize;
    }

    /**
     * 矩阵的转置
     */
    public Matrix transpose(){
        Matrix matrix = new Matrix(getColumnSize(),getRowSize());
        for(int row = 0;row < matrix.getRowSize();row++){
            for(int column = 0;column < matrix.getColumnSize();column++){
                matrix.set(row,column,this.get(column,row));
            }
        }
        return matrix;
    }

    /**
     * 判定矩阵形状是否不同
     * @return 矩阵形状是否不同
     */
    public boolean diffShape(Matrix other){
        boolean rowSame = this.getRowSize() == other.getRowSize();
        boolean columnSame = this.getColumnSize() == other.getColumnSize();
        return !rowSame || !columnSame;
    }

    /**
     * 矩阵加法运算
     */
    public void addWith(Matrix other){
        if(diffShape(other)){
            throw new MatrixShapeException(this,other);
        }
        for (int i = 0; i < data.length; i++) {
            this.data[i] += other.data[i];
        }
    }


    public Matrix add(Matrix other){
        Matrix matrix = this.copy();
        matrix.addWith(other);
        return matrix;
    }
    /**
     * 矩阵的数乘运算
     * @param num 数
     */
    public void mulWith(double num){
        for (int i = 0; i < data.length; i++) {
            data[i] *= num;
        }
    }
    public Matrix mul(double num){
        Matrix matrix = this.copy();
        matrix.mul(num);
        return matrix;
    }

    /**
     * 矩阵的哈达马积，即矩阵的每一个元素对应相乘
     */
    public void mulWith(Matrix other){
        if(diffShape(other)){
            throw new MatrixShapeException(this,other);
        }
        for (int i = 0; i < data.length; i++) {
            this.data[i] *= other.data[i];
        }
    }

    public Matrix mul(Matrix other){
        Matrix matrix = this.copy();
        matrix.mulWith(other);
        return matrix;
    }

    /**
     * 矩阵的减法运算
     * @param other 减矩阵
     */
    public void subWith(Matrix other){
        if(diffShape(other)){
            throw new MatrixShapeException(this,other);
        }
        for (int i = 0; i < data.length; i++) {
            this.data[i] -= other.data[i];
        }
    }

    public Matrix sub(Matrix other){
        Matrix matrix = this.copy();
        matrix.subWith(other);
        return matrix;
    }
    /**
     * 对矩阵的每个数值做一个值映射
     * @param function 映射函数
     */
    public void mapWith(Function function){
        for (int i = 0; i < data.length; i++) {
            data[i] = function.activate(data[i]);
        }
    }

    public Matrix map(Function function){
        Matrix matrix = this.copy();
        matrix.mapWith(function);
        return matrix;
    }

    /**
     * 矩阵的点乘运算，将该矩阵右乘另一个矩阵，前提是矩阵的列数和另一个矩阵的行数相同，
     * 否则将会抛出异常
     * @see MatrixDotException 矩阵的点乘异常
     *
     * @param other 右乘矩阵
     * @return 运算结果
     */
    public Matrix dot(Matrix other){
        //矩阵相乘的前提条件为矩阵的列数和另一个矩阵的行数相同
        if(this.getColumnSize() != other.getRowSize()){
            throw new MatrixDotException(this,other);
        }

        int sameVal = this.getColumnSize(); //保存中间相等的行列数据

        // 乘完之后，mat = this * other
        // 得到新矩阵mat的行数为this的行数
        // 列数为other的列数
        Matrix matrix = new Matrix(this.getRowSize(),other.getColumnSize());
        for(int row = 0;row < matrix.getRowSize();row++){
            for(int column = 0;column < matrix.getColumnSize();column++){
                //新矩阵的行索引为row,列索引为column
                double val = 0;
                for(int i = 0;i<sameVal;i++){
                    val += this.get(row,i) * other.get(i,column);
                }
                matrix.set(row,column,val);
            }
        }
        return matrix;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(int row = 0; row < getRowSize(); row++){
            if(row != 0){
                sb.append(" ");
            }
            sb.append("[");
            for(int column = 0; column < getColumnSize(); column++){
                sb.append(get(row,column));
                if(column != getColumnSize()-1){
                    sb.append(", ");
                }
            }
            sb.append("]");
            if(row != getRowSize() - 1){
                sb.append(",\n");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
