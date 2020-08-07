package org.example;

import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.je.*;

import java.io.File;
import java.io.FileNotFoundException;

public abstract class AbstractFrontier {
    protected Database catalogDb;
    private Environment env;
    protected Database database;
    protected StoredClassCatalog javaCatalog;
    public AbstractFrontier(String homeDirectory) throws DatabaseException, FileNotFoundException  {
        //database 环境配置
        EnvironmentConfig envConfig = new EnvironmentConfig();
        envConfig.setTransactional(true);
        envConfig.setAllowCreate(true);
        env = new Environment(new File(homeDirectory), envConfig);
        //数据库配置
        DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setTransactional(true);
        dbConfig.setAllowCreate(true);
        //打开
        catalogDb = env.openDatabase(null, "java_class_catalog", dbConfig);
        database = env.openDatabase(null, "URL", dbConfig);
        //对序列化 java 对象储存
        javaCatalog = new StoredClassCatalog(catalogDb);
    }
    //关闭数据库，关闭环境
    public void close() throws DatabaseException {
        database.close();
        catalogDb.close();
        env.close();
    }
    //put 方法
    protected abstract void put(Object key, Object value);
    //get 方法
    protected abstract Object get(Object key);
    //delete 方法
    protected abstract Object delete(Object key);
}
