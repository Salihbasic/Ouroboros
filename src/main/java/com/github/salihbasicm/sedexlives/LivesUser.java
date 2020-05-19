package com.github.salihbasicm.sedexlives;

import com.github.salihbasicm.sedexlives.util.ConfigManager;
import com.github.salihbasicm.sedexlives.util.SQLManager;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.scheduler.BukkitRunnable;
import com.github.salihbasicm.sedexlives.util.SedexLivesPermissions;

import java.util.Objects;
import java.util.UUID;

public class LivesUser {

    private SedexLives plugin;
    private SQLManager sqlManager;
    private ConfigManager configManager;

    private Player user;
    private UUID uuid;

    public LivesUser(SedexLives plugin, Player user) {
        this.plugin = plugin;

        this.user = Objects.requireNonNull(user, "Player could not be found!");

        uuid = user.getUniqueId();

        sqlManager = plugin.getSqlManager();
        this.configManager = plugin.getConfigManager();
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null)
            return false;

        if (!(obj instanceof LivesUser))
            return false;

        for (int i = 0; i < getSignificantFields().length; i++) {

            if (!Objects.equals(this.getSignificantFields()[i], ((LivesUser) obj).getSignificantFields()[i]))
                return false;

        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSignificantFields());
    }

    @Override
    public String toString() {
        return "[Name: " + this.user.getName() + ", UUID: " + this.uuid + "]";
    }

    /*
    Returns fields which are used for comparing two LivesUser objects.
     */
    private Object[] getSignificantFields() {
        return new Object[] {
                uuid, user
        };
    }

    /**
     * Gets the lives of this particular user.
     *
     * @return Number of lives
     */
    public int getLives() {
        return sqlManager.getPlayerLives(uuid);
    }

    /**
     * Iteraters over {@link Player}'s permissions and attempts to find {@code sedexlives.maxlives.#} permission.
     * It then splits this permission and attempts to return number in place of {@code '#'}. If there is no number there,
     * or if the player has no permission, it returns the default maximum lives.
     *
     * @return Player's maximum lives
     */
    public int getMaxLives() {

        for (PermissionAttachmentInfo perm : user.getEffectivePermissions()) {

            if (perm.getPermission().startsWith(SedexLivesPermissions.MAXLIVES_NUMBER)) {

                String[] split = perm.getPermission().split("\\.");

                if (split.length >= 3) {
                    return Integer.parseInt(split[2]);
                }

            }

        }

        return configManager.getDefaultLives();
    }

    /**
     * Checks if a player has their lives toggled off.
     *
     * @return {@code true} if player's lives are toggled off
     */
    public boolean isToggledOff() {
        return this.plugin.getToggledOff().contains(this);
    }

    /**
     * Attempts to toggle on/off lives.
     *
     * @param value {@code false} to toggle on lives, {@code true} to toggle off
     */
    public void setToggledOff(boolean value) {

        if (value) {

            if (!this.plugin.getToggledOff().contains(this)) {

                this.plugin.debugMessage("Attempting to toggle off lives for user.");
                this.plugin.getToggledOff().add(this);
                this.plugin.debugMessage("Successfully toggled off user's lives.");

            } else {

                this.plugin.debugMessage("User already has their lives toggled off");

            }

        } else {

            this.plugin.debugMessage("Attempting to toggle on lives for user.");
            this.plugin.getToggledOff().remove(this);
            this.plugin.debugMessage("Successfully toggled on user's lives.");

        }

    }

    /**
     * Returns {@link Player} representing used by this object.
     *
     * @return {@link Player} or {@code null}
     */
    public Player getUser() {
        return user;
    }

    /**
     * Creates and async {@link org.bukkit.scheduler.BukkitTask} and attempts to update lives of player
     * representing representing this object.
     *
     * @param newValue New value of lives
     */
    public void updateLives(final int newValue) {
        new BukkitRunnable() {

            @Override
            public void run() {
                final String sql = "UPDATE sl_lives SET lives = " + newValue + " WHERE uuid = '" + uuid + "';";
                sqlManager.update(sql);

                plugin.getLivesUserCache().refresh(LivesUser.this);
            }

        }.runTaskAsynchronously(plugin);
    }

    /**
     * Attempts to add the user into the database if the user does not already exist.
     *
     * @param defaultLives Default value of lives to be given to the new user
     */
    public void createUser(final int defaultLives) {
        new BukkitRunnable() {

            @Override
            public void run() {

                // UUID has the UNIQUE constraint. If the UUID already exists in the database, IGNORE will just
                // cancel execution of this statement, thus preventing duplicates.

                final String sql = "INSERT IGNORE INTO sl_lives(lives, uuid) VALUES (" + defaultLives +
                        ", '" + uuid + "');";

                sqlManager.update(sql);
            }

        }.runTaskAsynchronously(plugin);
    }

}
