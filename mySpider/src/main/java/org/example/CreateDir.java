package org.example;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class CreateDir {
    public static void main(String[] args) {
        Configuration conf=new Configuration();
        conf.set("fs.defaultFS", "hdfs://127.0.0.1:9000");
        try {
            FileSystem HDFS = FileSystem.get(conf);
            HDFS.mkdirs(new Path(args[0]));
            System.out.println("cerate success");
        } catch (IOException e) {
            System.out.println("cerate error");
            e.printStackTrace();
        }
    }
}
