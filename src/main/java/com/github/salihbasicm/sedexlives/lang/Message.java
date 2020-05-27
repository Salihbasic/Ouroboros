package com.github.salihbasicm.sedexlives.lang;

/**
 * Represents every message specified in {@code messages.yml} used for plugin localization.
 * Each value contains a path as specified in {@code messages.yml}.
 */
public enum Message {

    // General command messages

    NO_PERMISSION("no-permission"),

    USER_NOT_FOUND("user-not-found"),

    NOT_ENOUGH_ARGUMENTS("not-enough-arguments"),

    TOO_MANY_ARGUMENTS("too-many-arguments"),

    CORRECT_USAGE("correct-usage"),

    INVALID_SENDER("invalid-sender"),

    LIVES_UNRECOGNISED("unrecognised-command"),

    // Command specific messages

    LIVES_MESSAGE("lives-message"),

    LIVES_MESSAGE_OTHER("lives-message-other"),

    LIVES_MAXIMUM("lives-maximum"),

    LIVES_MAXIMUM_OTHER("lives-maximum-other"),

    LIVES_SET_NONNEGATIVE("lives-set-nonnegative"),

    LIVES_SET_GREATERVALUE("lives-set-greatervalue"),

    LIVES_SET_SUCCESS("lives-set-success"),

    LIVES_TOGGLE_ON("lives-toggle-on"),

    LIVES_TOGGLE_OFF("lives-toggle-off"),

    // Help messages

    LIVES_HELP("lives-help"),

    LIVES_CHECK_HELP("lives-check"),

    LIVES_MAXLIVES_HELP("lives-maxlives"),

    LIVES_INFO_HELP("lives-info"),

    LIVES_RELOAD_HELP("lives-reload"),

    LIVES_SET_HELP("lives-set"),

    LIVES_TOGGLE_HELP("lives-toggle");


    private final String path;

    Message(final String path) {
        this.path = path;
    }

    /**
     * Retrieves the path represented by the specified enum.
     *
     * @return Message path
     */
    public String getPath() {
        return this.path;
    }

}
