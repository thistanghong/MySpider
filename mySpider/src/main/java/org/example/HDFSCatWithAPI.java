package org.example;

import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
public class HDFSCatWithAPI {
    public static void main(String[] args) throws Exception{
// 指定Configuration
        Configuration conf = new Configuration();
//定义一个DataInputStream
        FSDataInputStream in = null;
        try{
//得到文件系统的实例
            FileSystem fs = FileSystem.get(conf);
//通过FileSystem 的open 方法打开一个指定的文件
            in = fs.open(new
                    Path("hdfs://127.0.0.1:19000/D:/document\\bigdata\\all/test.txt"));
//将InputStream 中的内容通过IOUtils 的copyBytes 方法复制到System.out 中
            IOUtils.copyBytes(in,System.out,4096,false);
//seek 到position 1
            in.seek(1);
//执行一边复制一边输出工作
            IOUtils.copyBytes(in,System.out,4096,false);
        }finally{
            IOUtils.closeStream(in);
        }
    }
}
