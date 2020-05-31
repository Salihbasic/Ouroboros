package com.github.salihbasicm.sedexlives.storage;

import com.github.salihbasicm.sedexlives.LivesUser;
import com.github.salihbasicm.sedexlives.SedexLives;
import com.github.salihbasicm.sedexlives.util.LivesConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Provides implementation for storing the user data in a MySQL database.
 * This class should not be initialised anywhere outside of main plugin class.
 */
public final class MySQLStorageProvider implements LivesStorage {

    private final SedexLives plugin;

    private final String hostname;
    private final String port;
    private final String username;
    private final String password;
    private final String database;

    private HikariDataSource dataSource;

    public MySQLStorageProvider(final SedexLives plugin) {

        this.plugin = plugin;
        LivesConfig config = plugin.getLivesConfig();

        this.hostname = config.getHostname();
        this.port = config.getPort();
        this.username = config.getUsername();
        this.password = config.getPassword();
        this.database = config.getDatabase();

        this.setUpTable();
        plugin.getLogger().info("Data storage set to: " + this.getClass().getSimpleName());
    }

    @Override
    public void createUser(final LivesUser user, final int defaultLives) {
        new BukkitRunnable() {

            @Override
            public void run() {

                // UUID has the UNIQUE constraint. If the UUID already exists in the database, IGNORE will just
                // cancel execution of this statement, thus preventing duplicates.

                final String mysql = "INSERT IGNORE INTO sl_lives(lives, uuid) VALUES (" + defaultLives +
                        ", '" + user.getUniqueId() + "');";

                MySQLStorageProvider.this.update(mysql);

            }

        }.runTaskAsynchronously(plugin);
    }

    @Override
    public void updateLives(final LivesUser user, final int newValue) {

        new BukkitRunnable() {

            @Override
            public void run() {
                final String sql = "UPDATE sl_lives SET lives = " + newValue + " WHERE uuid = '" + user.getUniqueId() + "';";
                MySQLStorageProvider.this.update(sql);

                plugin.getLivesUserCache().refresh(user);

            }

        }.runTaskAsynchronously(plugin);

    }

    @Override
    public int getLives(final LivesUser user) {
        return getPlayerLives(user.getUniqueId());
    }

    /**
     * Attempts to open a new Hikari data source.
     */
    private void connect() {

        final String mysqlUrl = "jdbc:mysql://" + this.hostname + ":" + this.port + "/" + this.database;

        HikariConfig config = new HikariConfig();

        config.setPoolName("sedexlives-hikari");

        config.setJdbcUrl(mysqlUrl);
        config.setUsername(this.username);
        config.setPassword(this.password);
        plugin.getLogger().info("Initialising MySQL connection...");

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
     * Attempts to get a connection to the database.
     *
     * @return {@link Connection} if connection was successful, {@code null} otherwise
     */
    private Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

    /**
     * Queries the database with a prepared statement.
     *
     * @param sql Prepared statement
     * @return ResultSet with query values
     */
    private ResultSet query(final String sql) {

        try (Connection connection = this.getConnection()){

            PreparedStatement statement = connection.prepareStatement(sql);

            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();

            return null;
        }
    }

    /**
     * Updates table with a prepared statement.
     *
     * @param sql Prepared statement
     */
    private void update(final String sql) {

        try (Connection connection = this.getConnection()) {

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

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

                if (resultSet == null) {
                    return lives;
                }

                if (resultSet.next()) { // Either empty or contains value
                    lives = resultSet.getInt("lives");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return lives;

        });
    }

    /**
     * Unwraps the result of {@code getPlayerLivesAsync}.
     *
     * @param uuid User's UUID
     */
    private int getPlayerLives(final UUID uuid) {
        try {
            return getPlayerLivesAsync(uuid).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /*
     * Creates the main table if it does not exist.
     */
    private void setUpTable() {
        this.connect();

        final String update = "CREATE TABLE IF NOT EXISTS sl_lives(lives int, uuid VARCHAR(36) NOT NULL UNIQUE);";
        this.update(update);
    }

}
