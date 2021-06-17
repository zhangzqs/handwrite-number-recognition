package cn.zzq.handwriter;

import java.io.File;

public class FileDefinition {
    /**
     * 训练集图片
     */
    public static final File TRAIN_IMAGE_FILE = new File("assets/train-images.idx3-ubyte");

    /**
     * 训练集标签
     */
    public static final File TRAIN_LABELS_FILE = new File("assets/train-labels.idx1-ubyte");

    /**
     * 测试集图片
     */
    public static final File TEST_IMAGE_FILE = new File("assets/t10k-images.idx3-ubyte");

    /**
     * 测试集标签
     */
    public static final File TEST_LABELS_FILE = new File("assets/t10k-labels.idx1-ubyte");
}
