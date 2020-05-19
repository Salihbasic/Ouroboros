/*
MIT License

Copyright (c) 2020 Steinein_

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

package com.github.salihbasicm.sedexlives.util;

import com.github.salihbasicm.sedexlives.SedexLives;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class SQLManager {
    private static SQLManager sqlManager = null;

    private String hostname;
    private String port;
    private String username;
    private String password;
    private String database;

    private HikariDataSource dataSource;

    private SQLManager(final String hostname, final String port, final String username,
                       final String password, final String database) {

        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
        this.database = database;

    }

    /**
     * Attempts to return an instance of {@link SQLManager}.
     *
     * @param plugin Instance of plugin
     * @return Instance of this class
     */
    public static synchronized SQLManager getSQLManager(SedexLives plugin) {
        final ConfigManager configManager = plugin.getConfigManager();

        if (sqlManager == null) {
            sqlManager = new SQLManager(configManager.getHostname(), configManager.getPort(), configManager.getUsername(),
                    configManager.getPassword(), configManager.getDatabase());
        }

        return sqlManager;
    }

    /**
     * Attempts to open a new Hikari data source.
     */
    private void connect() {

        final String jdbcUrl = "jdbc:mysql://" + this.hostname + ":" + this.port + "/" + this.database;

        HikariConfig config = new HikariConfig();

        config.setPoolName("sedexlives-hikari");
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(this.username);
        config.setPassword(this.password);

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("rewriteBatchedStatements", "true");
        config.addDataSourceProperty("maintainTimeStats", "false");

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(10);
        config.setIdleTimeout(300000);
        config.setMaxLifetime(600000);
        config.setConnectionTimeout(5000);
        config.setInitializationFailTimeout(-1);

        try {
            this.dataSource = new HikariDataSource(config);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Updates table with a prepared statement.
     *
     * @param sql Prepared statement
     */
    public void update(final String sql) {

        try (Connection connection = this.getConnection()) {

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Queries the database with a prepared statement.
     *
     * @param sql Prepared statement
     * @return ResultSet with query values
     */
    public ResultSet query(final String sql) {

        try (Connection connection = this.getConnection()){

            PreparedStatement statement = connection.prepareStatement(sql);

            return statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();

            return null;
        }
    }

    /**
     * Attempts to get a connection to the database.
     *
     * @return {@link Connection} if connection was successful, {@code null} otherwise
     */
    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

    /**
     * Enters the database from a separate thread and attempts to query a value based on UUID.
     * Value returned is a {@link CompletableFuture} which can then be further used to obtain the actual query value.
     *
     * The default value (returned if the query failed) is {@code -1}. This can further be used for error checking.
     *
     * @param uuid Player's UUID
     * @return {@link CompletableFuture} with the query result if successful or with {@code -1} if not
     */
    private CompletableFuture<Integer> getPlayerLivesAsync(final UUID uuid) {

        return CompletableFuture.supplyAsync(() -> {

            int lives = -1;

            final String query = "SELECT * FROM sl_lives WHERE uuid='" + uuid + "'";

            try (ResultSet resultSet = this.query(query)) {

                if (resultSet == null)
                    return lives;

                if (resultSet.next()) { // Either empty or contains value
                    lives = resultSet.getInt("lives");
                    resultSet.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return lives;

        });
    }

    /*
    Wrapper method for getPlayerLives(final String uuid) method above. Unwraps the completable future.
     */
    public int getPlayerLives(final UUID uuid) {
        try {
            return getPlayerLivesAsync(uuid).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /*
     * Creates the main table if it does not exist.
     */
    public void setUpTable() {
        this.connect();

        final String update = "CREATE TABLE IF NOT EXISTS sl_lives(lives int, uuid VARCHAR(36) NOT NULL UNIQUE);";
        this.update(update);
    }
}
