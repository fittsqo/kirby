package io.fittsqo.kirby.Database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DSource {

    private static final HikariConfig config = new HikariConfig();

    static {
        config.setJdbcUrl("jdbc:mysql://localhost:3306/kirbybase?useUnicode=yes&characterEncoding=UTF-8");
        config.setUsername("java");
        config.setPassword("password");
    }

    public static Connection getConnection() throws SQLException {
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        HikariDataSource ds = new HikariDataSource(config);
        return ds.getConnection();
    }
}
