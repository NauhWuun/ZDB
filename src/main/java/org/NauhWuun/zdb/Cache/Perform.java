package org.NauhWuun.zdb.Cache;

import org.NauhWuun.zdb.Cache.Cached.KEY;
import org.NauhWuun.zdb.Cache.Cached.VALUE;
import org.NauhWuun.zdb.Mapper;
import org.NauhWuun.zdb.RockRand;

import java.util.Date;

public class Perform
{
    public static void main(String[] args) {
        Mapper mapper = new Mapper(10000);

        System.out.println("Start: " + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(new Date()));

        KEY key = new KEY(RockRand.nextLong());
        VALUE value = new VALUE();
        value.addValue(RockRand.nextLong());

        for (int i = 0; i < 10000; i++) {
            mapper.partition(i).add(key, value);
        }
        System.out.println("Endof: " + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(new Date()));
    }
}