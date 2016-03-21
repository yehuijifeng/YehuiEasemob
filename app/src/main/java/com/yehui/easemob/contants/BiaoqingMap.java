package com.yehui.easemob.contants;

import com.yehui.easemob.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Luhao on 2016/3/10.
 */
public class BiaoqingMap {
    private static volatile BiaoqingMap instance;

    private BiaoqingMap() {
    }

    public static synchronized BiaoqingMap getInstance() {
        if (instance == null) {
            synchronized (BiaoqingMap.class) {
                if (instance == null) {
                    instance = new BiaoqingMap();
                }
            }
        }
        return instance;
    }

    private static Map<String, Integer> biaoqingMap;
    private static final String ee = "[ee_";
    public static final String ee_1 = ee+"1]";
    public static final String ee_2 = ee+"2]";
    public static final String ee_3 = ee+"3]";
    public static final String ee_4 = ee+"4]";
    public static final String ee_5 = ee+"5]";

    public void initMap() {
        biaoqingMap = new HashMap<>();
        biaoqingMap.put(ee_1, R.drawable.ee_1);
        biaoqingMap.put(ee_2, R.drawable.ee_2);
        biaoqingMap.put(ee_3, R.drawable.ee_3);
        biaoqingMap.put(ee_4, R.drawable.ee_4);
        biaoqingMap.put(ee_5, R.drawable.ee_5);
    }


    public Map<String, Integer> getBiaoqingMap() {
        return biaoqingMap;
    }
}
