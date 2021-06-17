package cn.zzq.handwrite.mnistparser;

import cn.zzq.handwrite.matrix.Matrix;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MnistImage {
    final int size;   //图片数目
    final int height; //图片高度
    final int width;  //图片宽度
    final ArrayList<Matrix> images;   //图片数据

    public int getSize() {
        return size;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public ArrayList<Matrix> getImages() {
        return images;
    }

    public MnistImage(InputStream in) throws IOException {
        if(ParseUtil.parseU32bit(in.readNBytes(4)) != 0x00000803){
            throw new IOException("不合法的图像文件");
        }
        size = (int) ParseUtil.parseU32bit(in.readNBytes(4));
        images = new ArrayList<>(size);
        height = (int) ParseUtil.parseU32bit(in.readNBytes(4));
        width = (int) ParseUtil.parseU32bit(in.readNBytes(4));
        for (int i = 0; i < size; i++) {
            byte[] bs = in.readNBytes(height * width);
            Matrix image = Matrix.valueOf(bs);
            image.reshape(height, width);
            images.add(image);
        }
    }

    @Override
    public String toString() {
        return "MnistImage{" +
                "size=" + size +
                ", height=" + height +
                ", width=" + width +
                '}';
    }
}
