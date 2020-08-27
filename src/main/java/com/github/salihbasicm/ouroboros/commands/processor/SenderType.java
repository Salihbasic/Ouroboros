package com.github.salihbasicm.ouroboros.commands.processor;

/**
 * Commands can be executed when:
 * <ul>
 *     <li>CONSOLE - the sender is the console</li>
 *     <li>PLAYER - the sender is a player</li>
 *     <li>BOTH - the sender is a console or the player</li>
 * </ul>
 */
public enum SenderType {

    CONSOLE,
    PLAYER,
    BOTH

}
