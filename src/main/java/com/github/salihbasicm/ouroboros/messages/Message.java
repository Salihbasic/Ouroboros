package com.github.salihbasicm.ouroboros.messages;

import org.bukkit.ChatColor;

/**
 * Represents every message specified in {@code messages.yml} used for plugin localization.
 * Each value contains a path as specified in {@code messages.yml}.
 */
public enum Message {

    NO_PERMISSION("&cYou do not have the permissions to execute this command!"), //

    USER_NOT_FOUND("&cCould not find player {0}. Are they online?"),

    INVALID_SENDER("&cThis command can not be executed by the {0}!"), //

    LIVES_MESSAGE("&aYou have {0} lives."), //

    LIVES_MESSAGE_OTHER("&a{0} has {1} lives."), //

    LIVES_MAXIMUM("&aYou can have a maximum of {0} lives."), //

    LIVES_MAXIMUM_OTHER("&a{0} can have a maximum of {1} lives."), //

    LIVES_SET_NONNEGATIVE("&cYou must specify a non-negative value for setting lives. {0} < 0!"), //

    LIVES_SET_GREATERVALUE("&cLives value you have specified is greater than user's maximum lives. {0} > {1}!"), //

    LIVES_SET_SUCCESS("&aYou have successfully set {0}'s lives to {1}."),

    LIVES_TOGGLE("&aYou have toggled your lives {0}."),

    LIVES_TOGGLE_OTHER("&aYou have toggled {0}'s lives {1}."),

    HELP_FOR_GROUP("&aHelp for command group &c{0}:"),

    HELP_NOT_FOUND("&cCould not find group &a{0}!");


    private final String message;

    Message(final String message) {
        this.message = message;
    }

    public String formatMessage(Object... args) {
        String colourFormattedMsg = this.getColourFormat();

        if (args == null || args.length == 0)
            return colourFormattedMsg;

        for (int i = 0; i < args.length; i++) {
            colourFormattedMsg = colourFormattedMsg.replace("{" + i + "}", args[i].toString());
        }

        return colourFormattedMsg;
    }

    private String getColourFormat() {
        return ChatColor.translateAlternateColorCodes('&', getMessage());
    }

    private String getMessage() {
        return this.message;
    }

}
