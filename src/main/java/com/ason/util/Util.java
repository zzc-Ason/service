package com.ason.util;

import com.google.common.collect.Maps;
import com.zzc.ason.common.ConfigMgr;
import com.zzc.ason.common.Symbol;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * author : Ason
 * createTime : 2018 年 05 月 19 日 14:39
 */
public class Util {

    public static final Map<String, String> PROPS = Maps.newHashMap();
    private static String defaultPath = "config.properties";
    private static final String PACKAGE = "com.ason.service";

    public static void load() {
        load(defaultPath);
    }

    public static void load(String path) {
        if (StringUtils.isBlank(path)) path = defaultPath;
        try {
            InputStream input = FileUtils.getFile(path).exists() ? new FileInputStream(path) : ConfigMgr.class.getClassLoader().getResourceAsStream(path);
            if (input != null) {
                Properties properties = new Properties();
                properties.load((InputStream) input);
                properties.forEach((k, v) -> {
                    PROPS.put(PACKAGE + Symbol.DOT + k, PACKAGE + Symbol.DOT + v);
                });
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
