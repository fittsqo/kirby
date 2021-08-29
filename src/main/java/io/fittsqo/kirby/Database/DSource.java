package io.fittsqo.kirby.Database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DSource {

    private final HikariConfig config = new HikariConfig();
    private final String username;
    private final String password;

    public DSource(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Connection getConnection() throws SQLException {
        config.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/kirbybase?useUnicode=yes&characterEncoding=UTF-8");
        config.setUsername(username);
        config.setPassword(password);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setIdleTimeout(28800 - 60);
        config.setMaxLifetime(28800 - 60);
        HikariDataSource ds = new HikariDataSource(config);
        return ds.getConnection();
    }
}
