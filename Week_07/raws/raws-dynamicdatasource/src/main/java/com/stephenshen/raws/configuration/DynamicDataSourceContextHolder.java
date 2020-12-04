package com.stephenshen.raws.configuration;

import com.stephenshen.raws.common.DataSourceKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName : DynamicDataSourceContextHolder  //类名
 * @Description : 数据源上下文配置，用于切换数据源  //描述
 * @Author : StephenShen  //作者
 * @Date: 2020-12-03 00:34  //时间
 */
public class DynamicDataSourceContextHolder {

    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceContextHolder.class);

    /**
     * 用于轮循的计数器
     */
    private static int counter = 0;

    /**
     * 用于在切换数据源时保证不会被其他线程修改
     */
    private static Lock lock = new ReentrantLock();

    private static final ThreadLocal<String> CONTEXT_HOLDER = ThreadLocal.withInitial(() -> DataSourceKey.master.name());

    /**
     * all dataSource list
     */
    public static List<Object> dataSourceKeys = new ArrayList<>();

    /**
     *
     */
    public static List<Object> slaveDataSourceKeys = new ArrayList<>();

    /**
     * To switch dataSource
     */
    public static void setDataSource(String key){
        CONTEXT_HOLDER.set(key);
    }

    /**
     * Use master of dataSource
     */
    public static void useMasterDataSource(){
        CONTEXT_HOLDER.set(DataSourceKey.master.name());
    }

    /**
     * Use slave of dataSource
     */
    public static void useSlaveDataSource(){
        try {
            int datasourceKeyIndex = counter % slaveDataSourceKeys.size();
            CONTEXT_HOLDER.set(String.valueOf(slaveDataSourceKeys.get(datasourceKeyIndex)));
            counter++;
        } catch (Exception e) {
            logger.error("Switch slave datasource failed, error message is {}", e.getMessage());
            useMasterDataSource();
            e.printStackTrace();
        }
    }

    /**
     * Get current DataSource
     */
    public static String getDataSourceKey(){
        return CONTEXT_HOLDER.get();
    }

    /**
     * To set DataSource as default
     */
    public static void clearDataSourceKey() {
        CONTEXT_HOLDER.remove();
    }

    /**
     * Check if give DataSource is in current DataSource list
     *
     * @param key the key
     * @return boolean boolean
     */
    public static boolean containDataSourceKey(String key) {
        return dataSourceKeys.contains(key);
    }
}
