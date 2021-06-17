package cn.zzq.handwrite.mnistparser;

import java.io.IOException;
import java.io.InputStream;

public class MnistLabel {
    final int size; //标签数目
    final byte[] labels; //标签
    public MnistLabel(InputStream in) throws IOException {
        if(ParseUtil.parseU32bit(in.readNBytes(4)) != 0x00000801){
            throw new IOException("不合法的标签文件");
        }
        size = (int) ParseUtil.parseU32bit(in.readNBytes(4));
        labels = new byte[size];
        for(int i = 0;i<size;i++){
            labels[i] = (byte) in.read();
        }
    }

    @Override
    public String toString() {
        return "MnistLabel{" +
                "size=" + size +
                '}';
    }
}
