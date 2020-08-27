package com.github.salihbasicm.ouroboros.commands.processor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Method level annotation marking the method as an Ouroboros sub-command.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SubCommand {

    String sub();

    int reqArgs() default 0;

    String usage();

    String help();

    String permission();

    SenderType sender() default SenderType.BOTH;

}
