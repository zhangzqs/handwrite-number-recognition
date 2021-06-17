package cn.zzq.handwrite.matrix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 矩阵加载器，从外部读入矩阵
 */
public final class MatrixLoader {
    /**
     * 从Csv文件加载矩阵，如果找不到文件，返回null
     * @param fileName 文件名
     * @return 矩阵
     */
    public static Matrix loadCsv(String fileName) {
        try {
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);

            String line;
            ArrayList<double[]> doubleLines = new ArrayList<>();
            int rowSize = 0;
            int columnSize = 0;
            while (null != (line = br.readLine())){
                //得到csv一行
                double[] doubleLine = Arrays.stream(line.split(",")).mapToDouble(Double::parseDouble).toArray();
                doubleLines.add(doubleLine);
                rowSize++;
                columnSize = doubleLine.length;
            }
            Matrix matrix = new Matrix(rowSize,columnSize);
            for(int row = 0;row < rowSize;row++){
                for(int column = 0;column < columnSize;column++){
                    matrix.set(row,column,doubleLines.get(row)[column]);
                }
            }

            fr.close();
            return matrix;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
