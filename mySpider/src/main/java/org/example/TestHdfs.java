package org.example;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;
import org.apache.log4j.BasicConfigurator;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;

public class TestHdfs {
    static {
        URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());


    }

    public static void main(String[] args) throws Exception {
        Configuration config;
        FileSystem fls;
        System.setProperty("hadoop.home.dir", "D:\\document\\bigdata\\all");
        BasicConfigurator.configure();
        config = new Configuration();
        fls = FileSystem.get(new URI("hdfs://127.0.0.1:9000"), config,"root");

        InputStream in = null;
        try {
//            in = new URL("hdfs://127.0.0.1:9000/")
        }
        catch(Exception e) {

        }
    }

}
