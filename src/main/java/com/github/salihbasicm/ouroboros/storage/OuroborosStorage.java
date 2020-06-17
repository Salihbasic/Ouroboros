package com.github.salihbasicm.ouroboros.storage;

import com.github.salihbasicm.ouroboros.OuroborosUser;

/**
 * Represents data storage for the plugin
 */
public interface OuroborosStorage {

    /**
     * Adds the user to storage if the user is not already present there.
     * @param user Lives user
     * @param defaultLives Default amount of lives
     */
    void createUser(OuroborosUser user, int defaultLives);

    /**
     * Updates the lives of a user.
     * @param user Lives user
     * @param newValue New amount of lives
     */
    void updateLives(OuroborosUser user, int newValue);

    /**
     * Returns the lives of a specified user stored in the plugin storage.
     * @param user Lives user
     * @return Lives of the user
     */
    int getLives(OuroborosUser user);

}
