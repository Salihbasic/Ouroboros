package com.github.salihbasicm.ouroboros;

import com.github.salihbasicm.ouroboros.lang.Message;
import com.github.salihbasicm.ouroboros.lang.MessageType;
import com.github.salihbasicm.ouroboros.lang.OuroborosMessage;
import com.github.salihbasicm.ouroboros.storage.OuroborosStorage;
import com.github.salihbasicm.ouroboros.util.OuroborosConfig;
import com.github.salihbasicm.ouroboros.util.OuroborosPermissions;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.Objects;
import java.util.UUID;

public class OuroborosUser {

    private final Ouroboros plugin;
    private final OuroborosConfig ouroborosConfig;

    private final OuroborosStorage storage;

    private final Player user;
    private final UUID uuid;

    public OuroborosUser(Ouroboros plugin, Player user) {
        this.plugin = plugin;

        this.user = Objects.requireNonNull(user, "Player could not be found!");

        this.uuid = user.getUniqueId();

        this.storage = plugin.getStorage();
        this.ouroborosConfig = plugin.getOuroborosConfig();
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null)
            return false;

        if (!(obj instanceof OuroborosUser))
            return false;

        for (int i = 0; i < getSignificantFields().length; i++) {

            if (!Objects.equals(this.getSignificantFields()[i], ((OuroborosUser) obj).getSignificantFields()[i]))
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
    Returns fields which are used for comparing two OuroborosUser objects.
     */
    private Object[] getSignificantFields() {
        return new Object[] {
                uuid, user
        };
    }

    /**
     * Sends a fully formatted {@link Message} to the specified user.
     *
     * @param message {@link Message} to be send
     * @param messageType {@link MessageType} can be either {@code FORMAT} with placeholders or {@code SIMPLE} without
     */
    public void sendMessage(final Message message, final MessageType messageType) {
        switch (messageType) {
            case FORMAT:
                this.getUser().sendMessage(plugin.getOuroborosMessage().getMessage(this, message));
                return;
            case SIMPLE:
                this.getUser().sendMessage(plugin.getOuroborosMessage().getSimpleMessage(message));
        }
    }

    /**
     * Gets the lives of this particular user.
     *
     * @return Number of lives
     */
    public int getLives() {
        this.plugin.debugMessage("Attempting to retrieve lives for UUID[ " + this.uuid + "] ...");
        int lives = Objects.requireNonNull(plugin.getOuroborosUserCache().get(this));
        this.plugin.debugMessage("Retrieved lives for UUID[" + this.uuid + "] with value[" + lives + "]!");

        return lives;
    }

    /**
     * Iteraters over {@link Player}'s permissions and attempts to find {@code ouroboros.maxlives.#} permission.
     * It then splits this permission and attempts to return number in place of {@code '#'}. If there is no number there,
     * or if the player has no permission, it returns the default maximum lives.
     *
     * @return Player's maximum lives
     */
    public int getMaxLives() {

        this.plugin.debugMessage("Attempting to retrieve maximum lives for UUID[" + this.uuid + "] ...");

        for (PermissionAttachmentInfo perm : user.getEffectivePermissions()) {

            if (perm.getPermission().startsWith(OuroborosPermissions.MAXLIVES_NUMBER)) {

                String[] split = perm.getPermission().split("\\.");

                if (split.length >= 3) {
                    final int max = Integer.parseInt(split[2]);

                    this.plugin.debugMessage("Retrieved maimum lives for UUID[" + this.uuid + "] with value[" + max + "]!");

                    return max;
                }

            }

        }

        return ouroborosConfig.getDefaultLives();
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

                this.plugin.debugMessage("Attempting to toggle off lives for UUID[" + this.uuid + "] ...");
                this.plugin.getToggledOff().add(this);
                this.plugin.debugMessage("Successfully toggled off lives for UUID[" + this.uuid + "]!");

            } else {

                this.plugin.debugMessage("UUID[" + this.uuid + "] already has lives toggled off!");

            }

        } else {

            this.plugin.debugMessage("Attempting to toggle on lives for UUID[" + this.uuid + "] ...");
            this.plugin.getToggledOff().remove(this);
            this.plugin.debugMessage("Successfully toggled on lives for UUID[" + this.uuid + "]!");

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

    public UUID getUniqueId() {
        return this.uuid;
    }

    /**
     * Creates and async {@link org.bukkit.scheduler.BukkitTask} and attempts to update lives of player
     * representing representing this object.
     *
     * @param newValue New value of lives
     */
    public void updateLives(final int newValue) {
        this.storage.updateLives(this, newValue);
    }

    /**
     * Attempts to add the user into the database if the user does not already exist.
     *
     * @param defaultLives Default value of lives to be given to the new user
     */
    public void createUser(final int defaultLives) {
        this.storage.createUser(this, defaultLives);
    }

}
