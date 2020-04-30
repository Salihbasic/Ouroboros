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

package database;

import sedexlives.ConfigManager;
import sedexlives.SedexLives;

import java.sql.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class SQLManager {
    private static SQLManager sqlManager = null;

    private SedexLives plugin = SedexLives.getSedexLives();

    private String hostname;
    private String port;
    private String username;
    private String password;
    private String database;

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

    /*
    Attempts to close whatever is passed in parameters.
     */
    private void closeEverything(Connection connection, PreparedStatement statement, ResultSet resultSet) {

        if (connection != null) {
            try {
                plugin.debugMessage("Attempting to close database connection.");
                connection.close();
                plugin.debugMessage("Successfully closed database connection.");
            } catch (SQLException e) {
                plugin.debugMessage("Failed while attempting to close database connection.");
            }
        }

        if (statement != null) {
            try {
                plugin.debugMessage("Attempting to close statement.");
                statement.close();
                plugin.debugMessage("Successfully closed statement.");
            } catch (SQLException e) {
                plugin.debugMessage("Failed while attempting to close statement.");
            }
        }

        if (resultSet != null) {
            try {
                plugin.debugMessage("Attempting to close result set.");
                resultSet.close();
                plugin.debugMessage("Successfully closed result set.");
            } catch (SQLException e) {
                plugin.debugMessage("Failed while attempting to close result set.");
            }
        }
    }

    /**
     * Updates table with a prepared statement.
     *
     * @param sql Prepared statement
     */
    public void update(final String sql) {

        PreparedStatement statement = null;

        try {
            Connection connection = this.getConnection();

            assert connection != null;

            statement = connection.prepareStatement(sql);

            plugin.debugMessage("Preparing statement for update.");
            statement.executeUpdate();
            plugin.debugMessage("Successfully executed update statement. (" + sql + ")");

        } catch (SQLException e) {
            plugin.debugMessage("Error while attempting to execute update statement. (" + sql + ")");
            e.printStackTrace();
        } finally {
            this.closeEverything(null, statement, null);
        }

    }

    /**
     * Queries the database with a prepared statement.
     *
     * @param sql Prepared statement
     * @return ResultSet with query values
     */
    public ResultSet query(final String sql) {

        PreparedStatement statement;
        ResultSet resultSet = null;

        try {
            Connection connection = this.getConnection();

            assert connection != null;

            statement = connection.prepareStatement(sql);

            plugin.debugMessage("Preparing statement for query.");
            resultSet = statement.executeQuery(sql);
            plugin.debugMessage("Successfully executed query statement. (" + sql + ")");

        } catch (SQLException e) {
            plugin.debugMessage("Error while attempting to execute query statement. (" + sql + ")");
            e.printStackTrace();
        }

        return resultSet;
    }

    /**
     * Attempts to get a connection to the database.
     *
     * @return {@link Connection} if connection was successful, {@code null} otherwise
     */
    public Connection getConnection() {
        final String connStr = "jdbc:mysql://" + hostname + ":" + port + "/" + database + "?user=" + username + "&password=" +
                password;

        try {

            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection(connStr);

        } catch (ClassNotFoundException | SQLException e) {
            plugin.debugMessage("Error while attempting to open database connection.");
            e.printStackTrace();
        }

        return null;
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
    private CompletableFuture<Integer> getPlayerLivesAsync(final String uuid) {

        return CompletableFuture.supplyAsync(() -> {

            int lives = -1;

            plugin.debugMessage("Attempting to get player lives for UUID (" + uuid + ")");
            final String query = "SELECT * FROM sl_lives WHERE uuid='" + uuid + "'";

            try (ResultSet resultSet = this.query(query)) {

                if (resultSet == null) {
                    plugin.debugMessage("Query failed. ResultSet is null.");
                    return lives;
                }

                if (resultSet.next()) { // Either empty or contains value
                    plugin.debugMessage("Received ResultSet. Attempting to get lives.");
                    lives = resultSet.getInt("lives");
                    plugin.debugMessage("Retrieved lives from ResultSet. Lives value (" + lives + ")");
                    resultSet.close();
                }

            } catch (SQLException e) {
                plugin.debugMessage("Failed to retrieve player lives for UUID (" + uuid + ")");
                e.printStackTrace();
            }

            return lives;

        });
    }

    /*
    Wrapper method for getPlayerLives(final String uuid) method above. Unwraps the completable future.
     */
    public int getPlayerLives(final String uuid) {
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
        plugin.debugMessage("Attempting to set up tables if they do not exist.");
        final String update = "CREATE TABLE IF NOT EXISTS sl_lives(lives int, uuid VARCHAR(36) NOT NULL UNIQUE);";
        this.update(update);
    }
}
