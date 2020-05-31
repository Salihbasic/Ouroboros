package com.github.salihbasicm.sedexlives.storage;

import com.github.salihbasicm.sedexlives.LivesUser;

/**
 * Represents data storage for the plugin
 */
public interface LivesStorage {

    /**
     * Adds the user to storage if the user is not already present there.
     * @param user Lives user
     * @param defaultLives Default amount of lives
     */
    void createUser(LivesUser user, int defaultLives);

    /**
     * Updates the lives of a user.
     * @param user Lives user
     * @param newValue New amount of lives
     */
    void updateLives(LivesUser user, int newValue);

    /**
     * Returns the lives of a specified user stored in the plugin storage.
     * @param user Lives user
     * @return Lives of the user
     */
    int getLives(LivesUser user);

}
