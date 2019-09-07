package jdkUtils;

import org.jetbrains.annotations.NotNull;

/**
 * @author Explo
 */
public class ModConfig {
    private static  volatile boolean debug = false;


    public static void setDebug(boolean debug) {
        ModConfig.debug = debug;
    }

    public static boolean isDebug() {
        return debug;
    }


    public static void printDebug(@NotNull String line) {
        if (debug){
            System.out.println(line);
        }
    }
}
