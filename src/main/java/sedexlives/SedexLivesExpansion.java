package sedexlives;

import database.SQLManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

/*
Do not touch. Expansion for PlaceholderAPI that adds custom plugin placeholder.
 */
public class SedexLivesExpansion extends PlaceholderExpansion {

    private SedexLives plugin = SedexLives.getSedexLives();
    private SQLManager sqlManager = SQLManager.getSQLManager(plugin);

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getIdentifier() {
        return "sedexlives";
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {

        if (player == null)
            return "";

        if (identifier.equals("lives"))
            return String.valueOf(sqlManager.getPlayerLives(player.getUniqueId().toString()));

        if (identifier.equals("maxlives"))
            return String.valueOf(plugin.getCommandManager().getPlayerMaxLives(player));

        return null;
    }
}
