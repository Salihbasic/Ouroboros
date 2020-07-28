package com.github.salihbasicm.ouroboros.commands.processor;

import com.github.salihbasicm.ouroboros.Ouroboros;
import com.github.salihbasicm.ouroboros.commands.AbstractCommandGroup;
import com.github.salihbasicm.ouroboros.lang.Message;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * When a command group is provided (as in: {@code /lives <group> <sub-command>}) to this class,
 * the next step is analysis of all the other provided arguments, matching them with a specific sub-command, and
 * then executing it.
 */
public final class CommandProcessor {

    private final Ouroboros plugin;

    private final Class<SubCommand>  methodAnnotation = SubCommand.class;
    private final Class<? extends AbstractCommandGroup> commandClass;

    public CommandProcessor(final Ouroboros plugin, final Class<? extends AbstractCommandGroup> commandClass) {
        this.plugin = plugin;
        this.commandClass = commandClass;
    }

    /**
     * Takes provided sub-command label and arguments for it, attempts to match commands within the provided class using
     * the {@link CommandProcessor#matchMethods}, and then executes all matches as the provided sender.
     * Before any execution can take place it first asserts the validity of the sender and whether the sender
     * has the required permissions.
     *
     * @param sender Sender who will execute the command
     * @param sub Provided sub command label
     * @param args Provided sub command arguments
     */
    public void executeSubCommands(final CommandSender sender, final String sub, final String[] args) {
        for (Method subCommand : matchMethods(sub, args.length)) {

            if (!hasPermission(subCommand, sender)) {
                sender.sendMessage(plugin.getOuroborosMessage().getSimpleMessage(Message.NO_PERMISSION));
                continue;
            }

            if (!isValidSender(subCommand, sender)) {
                sender.sendMessage(plugin.getOuroborosMessage().getSimpleMessage(Message.INVALID_SENDER));
                continue;
            }

            try {
                subCommand.setAccessible(true);
                subCommand.invoke(commandClass.getDeclaredConstructor(Ouroboros.class).newInstance(plugin),
                        sender, args);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Goes through all the sub commands in a given class and maps their help and usage messages
     * to a fully formatted help string.
     *
     * @return List of strings containing fully formatted help messages for every sub command in the class
     */
    public List<String> getSubCommandHelp() {
        return getMethods(commandClass)
                .stream()
                .map(this::formatHelp)
                .collect(Collectors.toList());
    }

    /**
     * Checks whether the provided method has the {@link SubCommand} annotation, thus ensuring that the method
     * is a sub-command.
     *
     * @param method Method to check
     * @return {@code true} if the annotation is present, {@code false} otherwise
     */
    private boolean isAnnotated(final Method method) {
        return method.isAnnotationPresent(methodAnnotation);
    }

    /**
     * Ensures that the sender has the correct permissions for the provided sub-command.
     *
     * @param method Sub-command with permission requirement
     * @param sender Sender whose permissions are to be checked
     * @return {@code true} if and only if the sender has required permissions
     */
    private boolean hasPermission(final Method method, final CommandSender sender) {
        return sender.hasPermission(method.getAnnotation(methodAnnotation).permission());
    }

    /**
     * Any sender can either be a {@link Player} or a {@link ConsoleCommandSender}. If the sender is not one,
     * then it has to be the other.
     *
     * @param sender Sender to check
     * @return {@link SenderType} of the provided sender
     */
    private SenderType getSenderType(final CommandSender sender) {
        return (sender instanceof Player) ? SenderType.PLAYER : SenderType.CONSOLE;
    }

    /**
     * To be valid, {@link SenderType} of the command sender must match the one required by the sub-command.
     * In case the required type is {@link SenderType#BOTH} any command sender will be valid.
     *
     * @param method Sub-command with sender requirement
     * @param sender Comamnd sender to be compared
     * @return {@code true} if the sender is valid based on requirements, {@code false} otherwise
     */
    private boolean isValidSender(final Method method, final CommandSender sender) {
        final SenderType required = method.getAnnotation(methodAnnotation).sender();
        return (getSenderType(sender) == required) || required == SenderType.BOTH;
    }

    /**
     * Returns all declared methods contained in the class.
     *
     * @param subCommandClass Class with methods
     * @return All declared methods in the class
     */
    private List<Method> getMethods(final Class<? extends AbstractCommandGroup> subCommandClass) {
        return Arrays.stream(subCommandClass.getDeclaredMethods())
                .filter(this::isAnnotated)
                .collect(Collectors.toList());
    }

    /**
     * <p>Every sub-command has its own label (as in: {@code /lives <group> <label>} and required arguments count.
     * Therefore the easiest way of finding the correct command is to filter all methods within the class and
     * leave only the one whose label and argument count match.</p>
     *
     * <p>In principle, this method should return a set with only one matched method, since there should be
     * only one method with the same label and argument count. However, if this is not the case, the set
     * will contain more than one element, in which case all elements will be executed by
     * {@link CommandProcessor#executeSubCommands)}.</p>
     *
     * @param sub Provided sub command label
     * @param argCount Provided arguments count
     * @return {@link Set} with <i>at least</i> one element if a match is found; empty otherwise
     */
    private Set<Method> matchMethods(final String sub, final int argCount) {
        return getMethods(commandClass)
                .stream()
                .filter(
                        method -> method.getAnnotation(methodAnnotation).reqArgs() == argCount
                                                &&
                                  method.getAnnotation(methodAnnotation).sub().equalsIgnoreCase(sub)
                )
                .collect(Collectors.toSet());
    }

    /**
     * Formats the command help message according to the standard formatting for this plugin.
     *
     * @param method Method annotated as a sub command
     * @return Formatted help message
     */
    private String formatHelp(final Method method) {
        final String command = method.getAnnotation(methodAnnotation).usage();
        final String description = method.getAnnotation(methodAnnotation).help();

        return ChatColor.RED + command + ChatColor.WHITE + " - " + ChatColor.GREEN + description + "\n";
    }

}
