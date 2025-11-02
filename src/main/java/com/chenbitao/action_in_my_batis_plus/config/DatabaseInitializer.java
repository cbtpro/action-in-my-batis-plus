package com.chenbitao.action_in_my_batis_plus.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

@Component
// 只在 dev/test 环境生效
@Profile({"dev", "test"})
public class DatabaseInitializer {

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void init() {
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            String dbName = metaData.getDatabaseProductName();

            // 判断是否是内存数据库
            if (isInMemoryDatabase(dbName)) {
                initDatabase(conn);
            } else {
                System.out.println("⚠️ Non in-memory database detected, skipping initialization. Database: " + dbName);
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Failed to get database connection: " + e.getMessage());
        }
    }

    private void initDatabase(Connection conn) {
        try {
            // 执行建表脚本
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("db/schema.sql"));
            // 执行初始化数据脚本
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("db/data.sql"));
            System.out.println("✅ Database schema and data initialized successfully.");
        } catch (Exception e) {
            System.err.println("⚠️ Failed to initialize database: " + e.getMessage());
        }
    }

    /**
     * 判断数据库是否为内存数据库
     */
    private boolean isInMemoryDatabase(String dbName) {
        return "H2".equalsIgnoreCase(dbName) ||
                "HSQL Database Engine".equalsIgnoreCase(dbName) ||
                "Derby".equalsIgnoreCase(dbName);
    }
}