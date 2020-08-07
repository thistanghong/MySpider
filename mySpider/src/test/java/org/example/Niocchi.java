package org.example;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class TestRunnable implements Runnable {

    @Override
    public void run() {
        try {
            RandomAccessFile aFile = null;
            aFile = new RandomAccessFile("C:\\Users\\tanghong\\Desktop\\demo\\a.txt", "rw");

            FileChannel fileChannel = aFile.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int byteRead = fileChannel.read(buffer);
            System.out.println(byteRead);
            while (byteRead != -1) {
                buffer.flip();
                while (buffer.hasRemaining()){
                    System.out.print((char)buffer.get());
                }
                //预防重复
                buffer.compact();
                byteRead = fileChannel.read(buffer);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
