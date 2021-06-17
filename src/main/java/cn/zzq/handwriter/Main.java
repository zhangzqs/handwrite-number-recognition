package cn.zzq.handwriter;

import cn.zzq.handwriter.matrix.Matrix;
import cn.zzq.handwriter.matrix.MatrixLoader;
import cn.zzq.handwriter.mnistparser.MnistImage;
import processing.core.PApplet;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Main extends PApplet {
    public static void main(String[] args) {
        Main.main(Main.class);
    }

    /**
     * 初始化界面
     */
    @Override
    public void settings() {
        size(280*2, 280*2);
    }

    final Matrix image = new Matrix(28, 28);           //手写数字存放的矩阵

    BPNetwork bpNetwork;    //BP神经网络
    MnistImage mnistImage;  //MNIST图片集

    @Override
    public void setup() {
        //初始化手写数字矩阵
        try {
            mnistImage = new MnistImage(new FileInputStream(FileDefinition.TEST_IMAGE_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //加载训练好的权重文件
        Matrix hiddenOutput = MatrixLoader.loadCsv("assets/hiddenOutput.csv");
        Matrix inputHidden = MatrixLoader.loadCsv("assets/inputHidden.csv");
        if(hiddenOutput == null || inputHidden == null){
            System.err.println("权重文件找不到！！！");
            exit();
        }

        //初始化网络及权重
        bpNetwork = new BPNetwork(784, 100, 10, 0.05);
        bpNetwork.setInputHiddenMatrix(inputHidden);
        bpNetwork.setHiddenOutputMatrix(hiddenOutput);
    }

    //将手写数字矩阵渲染到显示窗口
    void render() {
        int sx = width / image.getColumnSize();
        int sy = height / image.getRowSize();
        for (int row = 0; row < image.getRowSize(); row++) {
            for (int col = 0; col < image.getColumnSize(); col++) {
                int pixel = (int) image.get(row, col);
                fill(pixel);
                noStroke();
                rect(col * sx, row * sy, (col + 1) * sx, (row + 1) * sy);
            }
        }
    }

    @Override
    public void draw() {
        int sx = width / image.getColumnSize();     //获取单个网格占用的像素
        int sy = height / image.getRowSize();
        int mx = mouseX / sx;                       //判定鼠标当前在哪个网格
        int my = mouseY / sy;

        //网格越界判断
        if (    mx < 0 ||
                my < 0 ||
                mx >= image.getColumnSize() ||
                my >= image.getRowSize()||
                mx - 1< 0 ||
                my - 1< 0 ||
                mx + 1 >= image.getColumnSize() ||
                my + 1>= image.getRowSize()
        ) {
            return;
        }

        if (mousePressed) { //当鼠标按下时候
            int cx = (mx * sx + (mx+1) * sx) / 2;   //获取该网格的中心点的坐标
            int cy = (my * sy + (my+1) * sy) / 2;
            int dx = mouseX - cx;   //获取鼠标偏离该网格中心的x,y偏差
            int dy = mouseY - cy;
            double max = Math.sqrt(sx*sx+sy*sy)/2;  //外接圆的半径为偏差的最大值
            double d = Math.sqrt(dx*dx+dy*dy);      //计算偏差

            double oldPixel = image.get(my,mx);     //老像素点的灰度
            double deltaPixel = (1 - d/max) * 180;  //要新增的灰度值,离中心越近，灰度增量越大
            double newPixel = oldPixel + deltaPixel < 255 ? oldPixel + deltaPixel : 255;    //像素值不得越界255

            //一次设置两个像素，可以变粗画笔
            image.set(my,mx,newPixel);  //设置像素
            image.set(my,mx+1,newPixel);  //设置像素
        }

        clear();    //清空屏幕
        render();   //渲染image矩阵

        long ns = System.nanoTime();
        Matrix m1 = image.copy();               //拷贝图像矩阵
        m1.reshape(1, 784);     //转成列向量
        m1.mulWith(1 / 255f);               //归一化
        String confStr = getResult(bpNetwork.query(m1)).toString(); //识别并将识别结果输出为字符串
        long deltaNs = System.nanoTime() - ns;
        String str = String.format("%s    %f ms", confStr, deltaNs / 1000000f); //得到识别结果与识别时间的字符串，ns与ms的进制转换
        fill(255);          //设置画笔填充色为白色
        text(str, 0, height-5); //输出识别结果
    }

    /**
     * 获取识别结果
     *
     * @param m 置信度向量
     * @return 按置信度从高到低对手写数字进行排序
     */
    List<Integer> getResult(Matrix m) {
        //生成索引(0,9)
        List<Integer> index = IntStream.range(0, m.getColumnSize()).boxed().toList();

        //获得置信度向量
        double[] confidences = index.stream().mapToDouble(i -> m.get(0, i)).toArray();

        //排序
        ArrayList<Integer> sortedIndex = new ArrayList<>(index);    //由于Stream的Range不支持排序，故需要转换成ArrayList
        sortedIndex.sort((o1, o2) -> Double.compare(confidences[o2], confidences[o1]));

        return sortedIndex;
    }

    @Override
    public void keyTyped() {
        switch (key) {
            case ' ':
                image.clear();
                break;
            case 'c': {
                long cm = System.currentTimeMillis();
                Matrix m1 = image.copy();
                m1.reshape(1, 784);
                m1.mulWith(1 / 255f);
                System.out.println(getResult(bpNetwork.query(m1)));
                System.out.println(System.currentTimeMillis() - cm);
                break;
            }
            case 'r':
                //随机填充一个测试集图片
                image.fill(mnistImage.getImages().get((int)(Math.random()*mnistImage.getSize())));
                break;
            default:
                break;
        }
    }
}
