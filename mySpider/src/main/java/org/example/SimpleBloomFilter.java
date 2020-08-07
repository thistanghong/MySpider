package org.example;

import java.util.BitSet;

/**
 * 布隆过滤器实现 visited
 */
public class SimpleBloomFilter{
    private static final int DEFAULT_SIZE = 2 << 24;
    private static final int[] seeds = new int[] { 7, 11, 13, 31, 37, 61, };
    //1，分配 bit 空间
    //2，用 seeds 生成 value 的 hash值（int）
    //3.根据 hash值 (将其认为在bit空间的位置),判断是否重复
    //4.添加
    private BitSet bitSet = new BitSet(DEFAULT_SIZE);
    private SimpleHash[] func = new SimpleHash[seeds.length];

    public static void main(String[] args) {
        String value = "stone2083@yahoo.com";
        SimpleBloomFilter filter = new SimpleBloomFilter();
        System.out.println(filter.contains(value));
        filter.add(value);
        System.out.println(filter.contains(value));
    }
    public SimpleBloomFilter() {
        for(int i = 0; i < seeds.length; i++) {
            func[i] = new SimpleHash(DEFAULT_SIZE, seeds[i]);
        }
    }
    //add CrawUrl
    public void add(CrawlUrl value) {
        if(value != null) {
            add(value.getOriUrl());
        }
    }
    //add String
    public void add(String value) {
        for(SimpleHash f : func) {
            bitSet.set(f.hash(value), true);
        }
    }
    //contain param CrawlUrl
    public boolean contains(CrawlUrl value) {
        return contains(value.getOriUrl());
    }
    //contain param string
    public boolean contains(String value) {
        if(value == null) {
            return false;
        }
        boolean ret = true;
        for (SimpleHash f : func) {
            ret = ret && bitSet.get(f.hash(value));
        }
        return ret;
    }
}
class SimpleHash {
    private int cap;
    private int seed;
    public SimpleHash(int cap, int seed) {
        this.cap = cap;
        this.seed = seed;
    }
    public int hash(String value) {
        int result = 0;
        int len = value.length();
        for(int i = 0; i < len; i++) {
            result = seed * result + value.charAt(i);
        }
        return (cap - 1) & result;
    }
}
