package cn.zzq.handwrite;

import cn.zzq.handwrite.matrix.Function;
import cn.zzq.handwrite.matrix.Matrix;
import cn.zzq.handwrite.matrix.exception.MatrixShapeException;

public class BPNetwork {
    private double learningRate;    //学习率

    private Matrix inputHiddenW;    //输入层到隐藏层的权重矩阵
    private Matrix hiddenOutputW;   //隐藏层到输出层的权重矩阵
    private final Function activation;    //激活函数

    /**
     * 初始化一个BP神经网络
     * @param inputNodeCount 输入层网络节点
     * @param hiddenNodeCount 隐藏层网络节点
     * @param outputNodeCount 输出层网络节点
     * @param learningRate 学习率
     */
    public BPNetwork(int inputNodeCount,     //输入层节点数目
                     int hiddenNodeCount,   //隐藏层节点数目
                     int outputNodeCount,//输出层节点数目
                     double learningRate) {
        this.learningRate = learningRate;   //学习率

        //输入层为一个长度为784的行向量input
        //隐藏层为一个长度为100的行向量hidden
        //网络结构可用如下矩阵运算表示 input * inputHidden = hidden
        //则inputHidden为784行100列的矩阵
        this.inputHiddenW = Matrix.normals(
                inputNodeCount,    //行数
                hiddenNodeCount,     //列数
                0,      //正态分布均值
                Math.pow(hiddenNodeCount, -0.5) //正态分布方差
        );

        //隐藏层为一个长度为100的行向量hidden
        //输出层为一个长度为10的行向量output
        //网络结构可用如下矩阵运算表示 hidden * hiddenOutput = output
        //则inputHidden为100行10列的矩阵
        this.hiddenOutputW = Matrix.normals(
                hiddenNodeCount,    //行数
                outputNodeCount,    //列数
                0,      //正态分布均值
                Math.pow(outputNodeCount, -0.5) //正态分布方差
        );

        //激活函数为signmoid函数
        this.activation = x -> 1f / (1 + Math.exp(-x));
    }

    /**
     * 获取输入层到隐藏层的权重矩阵
     * @return 输入层到隐藏层的权重矩阵
     */
    public Matrix getInputHiddenMatrix() {
        return inputHiddenW;
    }

    /**
     * 获取隐藏层到输出层的权重矩阵
     * @return 隐藏层到输出层的权重矩阵
     */
    public Matrix getHiddenOutputMatrix() {
        return hiddenOutputW;
    }

    /**
     * 设置输入层到隐藏层的权重矩阵
     * @param inputHiddenW 输入层到隐藏层的权重矩阵
     */
    public void setInputHiddenMatrix(Matrix inputHiddenW){
        if(this.inputHiddenW.diffShape(inputHiddenW)){  //必须形状相符的矩阵
           throw new MatrixShapeException(this.inputHiddenW,inputHiddenW);
        }
        this.inputHiddenW = inputHiddenW;
    }
    /**
     * 设置隐藏层到输出层的权重矩阵
     * @param hiddenOutputW 隐藏层到输出层的权重矩阵
     */
    public void setHiddenOutputMatrix(Matrix hiddenOutputW){
        if(this.hiddenOutputW.diffShape(hiddenOutputW)){  //必须形状相符的矩阵
            throw new MatrixShapeException(this.hiddenOutputW,hiddenOutputW);
        }
        this.hiddenOutputW = hiddenOutputW;
    }

    /**
     * 正向推理
     *
     * @param input 输入层向量,行向量
     * @return 推理结果,即输出层向量，行向量
     */
    public Matrix query(Matrix input) {
        Matrix hidden = input.dot(inputHiddenW);  //隐藏层节点的输入
        hidden.mapWith(activation);   //应用激活函数,得到隐藏层的输出行向量

        Matrix output = hidden.dot(hiddenOutputW);  //输出层节点输入
        output.mapWith(activation); //激活函数，得到最终输出层的输出向量
        return output;
    }

    /**
     * 训练
     *
     * @param input 输入层向量
     * @param label 期望的输出层向量
     */
    public void train(Matrix input,Matrix label){
        Matrix hidden = input.dot(inputHiddenW);  //隐藏层节点的输入
        hidden.mapWith(activation);   //应用激活函数,得到隐藏层的输出行向量

        Matrix output = hidden.dot(hiddenOutputW);  //输出层节点输入
        output.mapWith(activation); //激活函数，得到最终输出层的输出向量

        //input为(1,784)矩阵
        //inputHiddenW为(784,100)矩阵
        //hidden = input*inputHidden为(1,100)矩阵
        //hiddenOutputW为(100,10)矩阵
        //output为(1,10)矩阵

        //误差反向推算 error = input - output;
        Matrix outputErrors = input.sub(output);

        Matrix deltaHiddenOutput = outputErrors
                .mul(output)
                .mul(output.map(x->1-x))
                .dot(hidden.transpose())
                .mul(learningRate);
        this.hiddenOutputW.addWith(deltaHiddenOutput);

        Matrix hiddenErrors = outputErrors.dot(hiddenOutputW);
    }
}
