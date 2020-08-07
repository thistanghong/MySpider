package org.example;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.collections.StoredMap;
import com.sleepycat.je.DatabaseException;

import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Set;

/**
 * 实现 TODO 表
 */
public class BDBFrontier extends AbstractFrontier implements Frontier {
    private StoredMap pendingUrisDB = null;
    public BDBFrontier(String homeDirectory) throws DatabaseException, FileNotFoundException {
        super(homeDirectory);
        EntryBinding keyBinding = new SerialBinding(javaCatalog, String.class);
        EntryBinding valueBinding = new SerialBinding(javaCatalog, CrawlUrl.class);
        pendingUrisDB = new StoredMap(database, keyBinding, valueBinding, true);
    }

    @Override
    //存入数据库的方法
    protected void put(Object key, Object value) {
        pendingUrisDB.put(key, value);
    }

    @Override
    // 取出
    protected Object get(Object key) {
        return pendingUrisDB.get(key);
    }

    @Override
    // 删除
    protected Object delete(Object key) {
        return pendingUrisDB.remove(key);
    }

    @Override
    //获取下一条内容
    public CrawlUrl getNext() throws Exception {
        CrawlUrl crawlUrl = null;
        if(!pendingUrisDB.isEmpty()) {
//            Set entrySet = pendingUrisDB.entrySet();
//            System.out.println(entrySet);
            Map.Entry<String, CrawlUrl> entry = (Map.Entry<String, CrawlUrl>) pendingUrisDB.entrySet().iterator().next();
            crawlUrl = entry.getValue();
            delete(entry.getKey());
        }
        return crawlUrl;
    }

    @Override
    //存入 URL
    public boolean putUrl(CrawlUrl url) {
        put(url.getOriUrl(), url);
        return true;
    }
    // 根据 URL 计算键值，
    private String caculateUrl(String url) {
        byte[] source = url.getBytes();
        return new MD5().getMD5(source);
    }

    /**
     * test main
     */
/*    public static void main(String[] strs) {
        try {
            BDBFrontier bBDBFrontier = new BDBFrontier("D:\\document\\bigdata\\berkeryDB");
            CrawlUrl url = new CrawlUrl();
            url.setOriUrl("http://www.163.com");

            for(int i = 0; i < 10; i++) {
                String str = "www.163_" + i + ".com";
                bBDBFrontier.putUrl(url);
                System.out.println(((CrawlUrl)bBDBFrontier.getNext()).getOriUrl());
            }


            bBDBFrontier.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
        }
    }*/
}
