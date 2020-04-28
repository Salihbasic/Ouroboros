package sedexlives;

public class SedexLivesPermissions {

    // Holds all permissions here for easier checking.

    /*
    When adding new permission, follow the format:

    public static final String PERMISSION_NAME = "permission.string" // Permission description
     */

    public static final String RELOAD_CONFIG = "sedexlives.reload"; // Lets the player reload the config.
    public static final String USE_LIVES = "sedexlives.use"; // Lets the player use the lives system.
    public static final String KEEP_EXP = "sedexlives.keepxp"; // Lets the player keep their XP on death.
    public static final String CHECK_LIVES = "sedexlives.check"; // Lets the player check their own lives.
    public static final String CHECK_LIVES_OTHERS = "sedexlives.check.others"; // Lets the player check others' lives.
    public static final String CHECK_MAXLIVES = "sedexlives.check.maxlives";
    public static final String CHECK_MAXLIVES_OTHERS = "sedexlives.check.maxlives.others";
    public static final String SET_LIVES = "sedexlives.set"; // Lets the player set others' lives.

}
