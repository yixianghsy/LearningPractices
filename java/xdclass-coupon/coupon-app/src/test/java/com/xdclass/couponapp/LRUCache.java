package com.xdclass.couponapp;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache<K,V> extends LinkedHashMap<K,V> {

    private int cacheSize;

    public LRUCache(int cacheSize){
        super(16,0.75f,true);
        this.cacheSize = cacheSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        boolean r = size()>cacheSize;
        if(r){
            System.out.println("清除缓存key:" +eldest.getKey());
        }
        return r;
    }

    public static void main(String[] args) {
        LRUCache<String,String> cache = new LRUCache<>(5);
        cache.put("A","A");
        cache.put("B","B");
        cache.put("C","C");
        cache.put("D","D");
        cache.put("E","E");

        System.out.println("初始化:"+cache.keySet());
        System.out.println("访问值:"+cache.get("C"));
        System.out.println("访问C后:"+cache.keySet());
        System.out.println("访问值:"+cache.put("F","F"));
        System.out.println("put F后:"+cache.keySet());
    }

}
