package tw.momocraft.serverplus.utils.event;

import me.RockinChaos.itemjoin.api.ItemJoinAPI;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import tw.momocraft.serverplus.handlers.ConfigHandler;
import tw.momocraft.serverplus.handlers.ServerHandler;
import tw.momocraft.serverplus.utils.CustomCommands;
import tw.momocraft.serverplus.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.*;

public class EventUtils {

    /**
     * @param player       the player who triggered the event.
     * @param targetPlayer the target Player of the event.
     * @param targetEntity the target Entity of the event.
     * @param targetBlock  the target Block of the event.
     * @param eventProp    the properties of the event.
     * @param eventName    the name of the event.
     * @return if cancel the event.
     */
    public static boolean checkEvent(Player player, Player targetPlayer, Entity targetEntity, Block targetBlock, Map<String, EventMap> eventProp, String eventName) {

        ServerHandler.sendConsoleMessage("checkEvent");
        EventMap eventMap;
        boolean conditions;
        for (String group : eventProp.keySet()) {
            ServerHandler.sendConsoleMessage("group: " + group);
            eventMap = eventProp.get(group);
            // Target
            if (!checkTargets(targetPlayer, targetEntity, targetBlock, eventMap.getTargets())) {
                ServerHandler.sendFeatureMessage("Event", eventName, "Targets", "continue",
                        new Throwable().getStackTrace()[0]);
                continue;
            }
            // Conditions
            conditions = checkConditions(player, targetPlayer, targetEntity, targetBlock, eventMap.getConditions(), eventName);

            ServerHandler.sendConsoleMessage("conditions: " + conditions);
            // Actions
            ActionMap actionMap;
            if (conditions) {
                actionMap = eventMap.getActions();
            } else {
                actionMap = eventMap.getActionsFailed();
            }
            if (actionMap != null) {
                if (executeActions(player, targetPlayer, targetEntity, targetBlock, actionMap, eventName)) {
                    // Cancel event.
                    ServerHandler.sendFeatureMessage("Event", eventName, "final", "cancel",
                            new Throwable().getStackTrace()[0]);
                    return true;
                } else {
                    ServerHandler.sendFeatureMessage("Event", eventName, "final", "return",
                            new Throwable().getStackTrace()[0]);
                    return false;
                }
            }
            ServerHandler.sendFeatureMessage("Event", eventName, "final", "continue",
                    new Throwable().getStackTrace()[0]);
        }
        return false;
    }

    /**
     * @param targetPlayer the target Player of the event.
     * @param targetEntity the target Entity of the event.
     * @param targetBlock  the target Block of the event.
     * @param targetMap    the settings of Targets.
     * @return if the targets matched.
     */
    public static boolean checkTargets(Player targetPlayer, Entity targetEntity, Block targetBlock, Map<String, List<String>> targetMap) {
        if (targetMap != null) {
            for (String type : targetMap.keySet()) {
                switch (type) {
                    case "Player":
                        if (targetPlayer != null) {
                            return true;
                        }
                        break;
                    case "Blocks":
                        try {
                            if (targetMap.get(type).contains(targetBlock.getType().name())) {
                                return true;
                            }
                        } catch (Exception ignored) {
                        }
                        break;
                    case "Entities":
                        try {
                            if (targetMap.get(type).contains(targetEntity.getType().name())) {
                                return true;
                            }
                        } catch (Exception ignored) {
                        }
                        break;
                }
            }
            return false;
        }
        return true;
    }

    /**
     * @param player       the player who triggered the event.
     * @param targetPlayer the target Player of the event.
     * @param targetEntity the target Entity of the event.
     * @param targetBlock  the target Block of the event.
     * @param conditionMap the settings of Conditions.
     * @param eventName    the name of the event.
     * @return if the conditions are matched.
     */
    private static boolean checkConditions(Player player, Player targetPlayer, Entity targetEntity, Block targetBlock, ConditionMap conditionMap, String eventName) {
        if (conditionMap == null) {
            return true;
        }
        String playerName = player.getName();
        // Hand-Slots
        if (conditionMap.getHandSlots() != null) {
            if (!conditionMap.getHandSlots().contains(player.getInventory().getHeldItemSlot())) {
                ServerHandler.sendFeatureMessage("Event", playerName, "Hand-Slots", "return", eventName,
                        new Throwable().getStackTrace()[0]);
                return false;
            }
        }
        // Sneak
        if (Utils.isEnable(conditionMap.getSneak(), true)) {
            if (!player.isSneaking()) {
                ServerHandler.sendFeatureMessage("Event", playerName, "Sneak", "return", eventName,
                        new Throwable().getStackTrace()[0]);
                return false;
            }
        }
        // Fly
        if (Utils.isEnable(conditionMap.getFly(), true)) {
            if (!player.isFlying()) {
                ServerHandler.sendFeatureMessage("Event", playerName, "Fly", "return", eventName,
                        new Throwable().getStackTrace()[0]);
                return false;
            }
        }
        // Day
        if (Utils.isEnable(conditionMap.getDay(), true)) {
            if (!Utils.isDay(player.getWorld().getTime(), conditionMap.getDay())) {
                ServerHandler.sendFeatureMessage("Event", playerName, "Day", "return", eventName,
                        new Throwable().getStackTrace()[0]);
                return false;
            }
        }
        // Liquid
        if (Utils.isEnable(conditionMap.getLiquid(), true)) {
            if (!Utils.isLiquid(player.getLocation().getBlock(), conditionMap.getLiquid())) {
                ServerHandler.sendFeatureMessage("Event", playerName, "Liquid", "return", eventName,
                        new Throwable().getStackTrace()[0]);
                return false;
            }
        }
        // Boimes
        if (conditionMap.getBoimes() != null) {
            if (!Utils.containIgnoreValue(player.getLocation().getBlock().getBiome().name(), conditionMap.getBoimes(), conditionMap.getIgnoreBoimes())) {
                ServerHandler.sendFeatureMessage("Event", playerName, "Boimes", "return", eventName,
                        new Throwable().getStackTrace()[0]);
                return false;
            }
        }
        // Location
        if (conditionMap.getLocMaps() != null) {
            if (!ConfigHandler.getConfigPath().getLocationUtils().checkLocation(player.getLocation(), conditionMap.getLocMaps())) {
                ServerHandler.sendFeatureMessage("Event", playerName, "Location", "return", eventName,
                        new Throwable().getStackTrace()[0]);
                return false;
            }
        }
        // Block
        if (conditionMap.getBlocksMaps() != null) {
            if (!ConfigHandler.getConfigPath().getBlocksUtils().checkBlocks(player.getLocation(), conditionMap.getBlocksMaps())) {
                ServerHandler.sendFeatureMessage("Event", playerName, "Block", "return", eventName,
                        new Throwable().getStackTrace()[0]);
                return false;
            }
        }
        // Holding menu
        if (conditionMap.getHoldingMenu() != null) {
            if (!checkHoldingMenu(player)) {
                ServerHandler.sendFeatureMessage("Event", playerName, "Holding menu", "return", eventName,
                        new Throwable().getStackTrace()[0]);
                return false;
            }
        }
        // Placeholders
        if (conditionMap.getPlaceholders() != null) {
            if (!checkPlaceholders(player, targetPlayer, targetEntity, targetBlock, conditionMap.getPlaceholders(), eventName)) {
                ServerHandler.sendFeatureMessage("Event", playerName, "Placeholders", "return", eventName,
                        new Throwable().getStackTrace()[0]);
                return false;
            }
        }
        return true;
    }

    /**
     * @param player          the player who triggered the event.
     * @param targetEntity    the target Entity of this event.
     * @param targetBlock     the target Block of this event.
     * @param placeholderList the Placeholder list.
     * @param eventName       the name of event.
     * @return if the conditions are matched.
     */
    private static boolean checkPlaceholders(Player player, Player targetPlayer, Entity targetEntity, Block targetBlock, List<String> placeholderList, String eventName) {
        String[] placeholders;
        back:
        for (String placeholder : placeholderList) {
            placeholder = translateEventLayout(placeholder, player, targetPlayer, targetEntity, targetBlock, eventName);
            ServerHandler.sendConsoleMessage(placeholder);
            if (placeholder.contains(";")) {
                placeholders = placeholder.split(";");
                for (String value : placeholders) {
                    if (checkPlaceholderValue(value)) {
                        break back;
                    }
                }
            } else {
                if (!checkPlaceholderValue(placeholder)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean checkPlaceholderValue(String placeholder) {
        String[] values = placeholder.split("\\s+");
        if (values.length != 3) {
            ServerHandler.sendErrorMessage("There is an error occurred. Please check the \"Event - Conditions\" format.");
            ServerHandler.sendErrorMessage("Placeholder: " + placeholder);
            return true;
        }
        if (values[1].equals("=")) {
            return values[0].equals(values[2]);
        } else if (values[1].equals("!=")) {
            return !values[0].equals(values[2]);
        } else {
            try {
                return Utils.getCompare(values[1], Double.parseDouble(values[0]), Double.parseDouble(values[2]));
            } catch (Exception ex) {
                ServerHandler.sendErrorMessage("There is an error occurred. Please check the \"Event - Conditions\" format.");
                ServerHandler.sendErrorMessage("Placeholder: " + placeholder);
                return true;
            }
        }
    }

    /**
     * @param player       the player who triggered the event.
     * @param targetEntity the target Entity of this event.
     * @param targetBlock  the target Block of this event.
     * @param actionMap    the executing Actions.
     * @return if the event canceled.
     */
    private static boolean executeActions(Player player, Player targetPlayer, Entity targetEntity, Block targetBlock, ActionMap actionMap, String eventName) {
        if (actionMap == null) {
            ServerHandler.sendErrorMessage("There is an error occurred. Please check the \"Event - Actions\".");
            ServerHandler.sendErrorMessage("Triggered event: " + eventName);
            return false;
        }
        String playerName;
        try {
            playerName = player.getName();
        } catch (Exception ex) {
            playerName = "CONSOLE";
        }
        String targetPlayerName;
        try {
            targetPlayerName = targetPlayer.getName();
        } catch (Exception ex) {
            targetPlayerName = "CONSOLE";
        }
        if (player != null) {
            if (actionMap.getCommands() != null) {
                String command;
                for (String cmd : actionMap.getCommands()) {
                    command = translateEventLayout(cmd, player, targetPlayer, targetEntity, targetBlock, eventName);
                    CustomCommands.executeMultiCmds(player, command);
                    ServerHandler.sendFeatureMessage("Event", playerName, "execute", "return", command,
                            new Throwable().getStackTrace()[0]);
                }
            }
            if (Utils.isEnable(actionMap.getKill(), false)) {
                player.setHealth(0);
            }
        }
        if (targetPlayer != null) {
            if (actionMap.getCommandsTarget() != null) {
                String command;
                for (String cmd : actionMap.getCommandsTarget()) {
                    command = translateEventLayout(cmd, player, targetPlayer, targetEntity, targetBlock, eventName);
                    CustomCommands.executeMultiCmds(targetPlayer, command);
                    ServerHandler.sendFeatureMessage("Event", targetPlayerName, "execute", "return", command,
                            new Throwable().getStackTrace()[0]);
                }
            }
            if (Utils.isEnable(actionMap.getKillTarget(), false)) {
                targetPlayer.setHealth(0);
            }
        }
        return actionMap.getCancel() != null && actionMap.getCancel().equals("true");
    }

    private static boolean checkHoldingMenu(Player player) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        // Holding ItemJoin menu.
        if (ConfigHandler.getDepends().ItemJoinEnabled()) {
            ItemJoinAPI itemJoinAPI = new ItemJoinAPI();
            String menuIJ = ConfigHandler.getConfigPath().getMenuIJ();
            if (!menuIJ.equals("")) {
                if (itemJoinAPI.getNode(itemStack) != null) {
                    return itemJoinAPI.getNode(itemStack).equals(menuIJ);
                }
                return false;
            }
        }
        // Holding a menu item.
        if (itemStack.getType().name().equals(ConfigHandler.getConfigPath().getMenuType())) {
            String itemName;
            try {
                itemName = itemStack.getItemMeta().getDisplayName();
            } catch (Exception ex) {
                itemName = "";
            }
            String menuName = ConfigHandler.getConfigPath().getMenuName();
            return menuName.equals("") || itemName.equals(Utils.translateColorCode(menuName));
        }
        return false;
    }

    private static String translateEventLayout(String input, Player player, Player targetPlayer, Entity targetEntity, Block targetBlock, String eventName) {
        if (player != null) {
            input = Utils.translateLayout(input, player);
        }
        if (targetPlayer != null) {
            input = translatePlayer(input, targetPlayer);
        } else if (targetEntity != null) {
            input = translateEntity(input, targetEntity);
        } else if (targetBlock != null) {
            input = translateBlock(input, targetBlock);
        }
        // %event%
        try {
            input = input.replace("%event%", eventName);
        } catch (Exception e) {
            ServerHandler.sendDebugTrace(e);
        }
        // %target% => CONSOLE
        if (targetEntity == null && targetBlock == null) {
            try {
                input = input.replace("%target%", "CONSOLE");
            } catch (Exception e) {
                ServerHandler.sendDebugTrace(e);
            }
        }
        // %server_name%
        try {
            input = input.replace("%server_name%", Bukkit.getServer().getName());
        } catch (Exception e) {
            ServerHandler.sendDebugTrace(e);
        }
        // %localtime_time% => 2020/08/08 12:30:00
        try {
            input = input.replace("%localtime_time%", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        } catch (Exception e) {
            ServerHandler.sendDebugTrace(e);
        }
        // %random_number%500%
        if (input.contains("%random_number%")) {
            try {
                String[] arr = input.split("%");
                for (int i = 0; i < arr.length; i++) {
                    if (arr[i].equals("random_number") && arr[i + 1].matches("^[0-9]*$")) {
                        input = input.replace("%random_number%" + arr[i + 1] + "%", String.valueOf(new Random().nextInt(Integer.parseInt(arr[i + 1]))));
                    }
                }
            } catch (Exception e) {
                ServerHandler.sendDebugTrace(e);
            }
        }
        // %random_player%
        if (input.contains("%random_player%")) {
            try {
                List<Player> playerList = new ArrayList(Bukkit.getOnlinePlayers());
                String randomPlayer = playerList.get(new Random().nextInt(playerList.size())).getName();
                input = input.replace("%random_player%", randomPlayer);
            } catch (Exception e) {
                ServerHandler.sendDebugTrace(e);
            }
        }
        // Translate color codes.
        input = ChatColor.translateAlternateColorCodes('&', input);
        // Translate PlaceHolderAPI's placeholders.
        if (ConfigHandler.getDepends().PlaceHolderAPIEnabled()) {
            try {
                return PlaceholderAPI.setPlaceholders(null, input);
            } catch (NoSuchFieldError e) {
                ServerHandler.sendDebugMessage("Error has occurred when setting the PlaceHolder " + e.getMessage() + ", if this issue persist contact the developer of PlaceholderAPI.");
                return input;
            }
        }
        return input;
    }

    private static String translateEntity(String input, Entity targetEntity) {
        // %target%
        try {
            input = input.replace("%target%", targetEntity.getName());
        } catch (Exception e) {
            ServerHandler.sendDebugTrace(e);
        }
        // %target_display_name%
        try {
            input = input.replace("%target_display_name%", targetEntity.getName());
        } catch (Exception e) {
            ServerHandler.sendDebugTrace(e);
        }
        // %target_uuid%
        try {
            input = input.replace("%target_uuid%", targetEntity.getUniqueId().toString());
        } catch (Exception e) {
            ServerHandler.sendDebugTrace(e);
        }
        // %target_type%
        try {
            input = input.replace("%target_type%", targetEntity.getType().name());
        } catch (Exception e) {
            ServerHandler.sendDebugTrace(e);
        }
        Location loc = targetEntity.getLocation();
        // %target_world%
        if (input.contains("%target_world%")) {
            input = input.replace("%target_world%", loc.getWorld().getName());
        }
        // %target_loc%
        // %target_loc_x%, %player_loc_y%, %player_loc_z%
        // %target_loc_x_NUMBER%, %target_loc_y_NUMBER%, %target_loc_z_NUMBER%
        if (input.contains("%target_loc")) {
            try {
                String loc_x = String.valueOf(loc.getBlockX());
                String loc_y = String.valueOf(loc.getBlockY());
                String loc_z = String.valueOf(loc.getBlockZ());
                String[] arr = input.split("%");
                for (int i = 0; i < arr.length; i++) {
                    switch (arr[i]) {
                        case "target_loc_x":
                            if (arr[i + 1].matches("^-?[0-9]\\d*(\\.\\d+)?$")) {
                                input = input.replace("%target_loc_x%" + arr[i + 1] + "%", loc_x + Integer.parseInt(arr[i + 1]));
                            }
                            break;
                        case "target_loc_y":
                            if (arr[i + 1].matches("^-?[0-9]\\d*(\\.\\d+)?$")) {
                                input = input.replace("%target_loc_y%" + arr[i + 1] + "%", loc_y + Integer.parseInt(arr[i + 1]));
                            }
                            break;
                        case "target_loc_z":
                            if (arr[i + 1].matches("^-?[0-9]\\d*(\\.\\d+)?$")) {
                                input = input.replace("%target_loc_z%" + arr[i + 1] + "%", loc_z + Integer.parseInt(arr[i + 1]));
                            }
                            break;
                    }
                }
                input = input.replace("%target_loc%", loc_x + ", " + loc_y + ", " + loc_z);
                input = input.replace("%target_loc_x%", loc_x);
                input = input.replace("%target_loc_y%", loc_y);
                input = input.replace("%target_loc_z%", loc_z);
            } catch (Exception e) {
                ServerHandler.sendDebugTrace(e);
            }
        }
        return input;
    }

    private static String translatePlayer(String input, Player targetPlayer) {
        // %target%
        try {
            input = input.replace("%target%", targetPlayer.getName());
        } catch (Exception e) {
            ServerHandler.sendDebugTrace(e);
        }
        // %target_display_name%
        try {
            input = input.replace("%target_display_name%", targetPlayer.getDisplayName());
        } catch (Exception e) {
            ServerHandler.sendDebugTrace(e);
        }
        // %target_uuid%
        try {
            input = input.replace("%target_uuid%", targetPlayer.getUniqueId().toString());
        } catch (Exception e) {
            ServerHandler.sendDebugTrace(e);
        }
        // %target_type%
        try {
            input = input.replace("%target_type%", targetPlayer.getType().name());
        } catch (Exception e) {
            ServerHandler.sendDebugTrace(e);
        }
        // %target_sneaking%
        try {
            input = input.replace("%target_sneaking%", String.valueOf(targetPlayer.isSneaking()));
        } catch (Exception e) {
            ServerHandler.sendDebugTrace(e);
        }
        // %target_flying%
        try {
            input = input.replace("%target_flying%", String.valueOf(targetPlayer.isFlying()));
        } catch (Exception e) {
            ServerHandler.sendDebugTrace(e);
        }
        // %target_interact%
        try {
            input = input.replace("%target_interact%", Utils.getNearbyPlayer(targetPlayer, 3));
        } catch (Exception e) {
            ServerHandler.sendDebugTrace(e);
        }
        Location loc = targetPlayer.getLocation();
        // %target_world%
        if (input.contains("%target_world%")) {
            input = input.replace("%target_world%", loc.getWorld().getName());
        }
        // %target_loc%
        // %target_loc_x%, %player_loc_y%, %player_loc_z%
        // %target_loc_x_NUMBER%, %target_loc_y_NUMBER%, %target_loc_z_NUMBER%
        if (input.contains("%target_loc")) {
            try {
                String loc_x = String.valueOf(loc.getBlockX());
                String loc_y = String.valueOf(loc.getBlockY());
                String loc_z = String.valueOf(loc.getBlockZ());
                String[] arr = input.split("%");
                for (int i = 0; i < arr.length; i++) {
                    switch (arr[i]) {
                        case "target_loc_x":
                            if (arr[i + 1].matches("^-?[0-9]\\d*(\\.\\d+)?$")) {
                                input = input.replace("%target_loc_x%" + arr[i + 1] + "%", loc_x + Integer.parseInt(arr[i + 1]));
                            }
                            break;
                        case "target_loc_y":
                            if (arr[i + 1].matches("^-?[0-9]\\d*(\\.\\d+)?$")) {
                                input = input.replace("%target_loc_y%" + arr[i + 1] + "%", loc_y + Integer.parseInt(arr[i + 1]));
                            }
                            break;
                        case "target_loc_z":
                            if (arr[i + 1].matches("^-?[0-9]\\d*(\\.\\d+)?$")) {
                                input = input.replace("%target_loc_z%" + arr[i + 1] + "%", loc_z + Integer.parseInt(arr[i + 1]));
                            }
                            break;
                    }
                }
                input = input.replace("%target_loc%", loc_x + ", " + loc_y + ", " + loc_z);
                input = input.replace("%target_loc_x%", loc_x);
                input = input.replace("%target_loc_y%", loc_y);
                input = input.replace("%target_loc_z%", loc_z);
            } catch (Exception e) {
                ServerHandler.sendDebugTrace(e);
            }
        }
        // %target_money%
        if (ConfigHandler.getDepends().VaultEnabled()) {
            if (input.contains("%target_money%")) {
                input = input.replace("%target_money%", String.valueOf(ConfigHandler.getDepends().getVaultApi().getBalance(Bukkit.getOfflinePlayer(targetPlayer.getUniqueId()))));
            }
        }
        // %target_points%
        if (ConfigHandler.getDepends().PlayerPointsEnabled()) {
            if (input.contains("%target_points%")) {
                input = input.replace("%target_points%", String.valueOf(ConfigHandler.getDepends().getPlayerPointsApi().getBalance(Bukkit.getOfflinePlayer(targetPlayer.getUniqueId()))));
            }
        }
        // Translate PlaceHolderAPI's placeholders.
        if (ConfigHandler.getDepends().PlaceHolderAPIEnabled()) {
            try {
                return PlaceholderAPI.setPlaceholders(targetPlayer, input);
            } catch (NoSuchFieldError e) {
                ServerHandler.sendDebugMessage("Error has occurred when setting the PlaceHolder " + e.getMessage() + ", if this issue persist contact the developer of PlaceholderAPI.");
                return input;
            }
        }
        return input;
    }

    private static String translateBlock(String input, Block targetBlock) {
        // %target%
        try {
            input = input.replace("%target%", targetBlock.getType().name());
        } catch (Exception e) {
            ServerHandler.sendDebugTrace(e);
        }
        // %target_display_name%
        try {
            input = input.replace("%target_display_name%", targetBlock.getType().name());
        } catch (Exception e) {
            ServerHandler.sendDebugTrace(e);
        }
        // %target_type%
        try {
            input = input.replace("%target_type%", targetBlock.getType().name());
        } catch (Exception e) {
            ServerHandler.sendDebugTrace(e);
        }
        // %target_world%
        Location loc = targetBlock.getLocation();
        if (input.contains("%target_world%")) {
            input = input.replace("%target_world%", loc.getWorld().getName());
        }
        // %target_loc%
        // %target_loc_x%, %player_loc_y%, %player_loc_z%
        // %target_loc_x_NUMBER%, %target_loc_y_NUMBER%, %target_loc_z_NUMBER%
        if (input.contains("%target_loc")) {
            try {
                String loc_x = String.valueOf(loc.getBlockX());
                String loc_y = String.valueOf(loc.getBlockY());
                String loc_z = String.valueOf(loc.getBlockZ());
                String[] arr = input.split("%");
                for (int i = 0; i < arr.length; i++) {
                    switch (arr[i]) {
                        case "target_loc_x":
                            if (arr[i + 1].matches("^-?[0-9]\\d*(\\.\\d+)?$")) {
                                input = input.replace("%target_loc_x%" + arr[i + 1] + "%", loc_x + Integer.parseInt(arr[i + 1]));
                            }
                            break;
                        case "target_loc_y":
                            if (arr[i + 1].matches("^-?[0-9]\\d*(\\.\\d+)?$")) {
                                input = input.replace("%target_loc_y%" + arr[i + 1] + "%", loc_y + Integer.parseInt(arr[i + 1]));
                            }
                            break;
                        case "target_loc_z":
                            if (arr[i + 1].matches("^-?[0-9]\\d*(\\.\\d+)?$")) {
                                input = input.replace("%target_loc_z%" + arr[i + 1] + "%", loc_z + Integer.parseInt(arr[i + 1]));
                            }
                            break;
                    }
                }
                input = input.replace("%target_loc%", loc_x + ", " + loc_y + ", " + loc_z);
                input = input.replace("%target_loc_x%", loc_x);
                input = input.replace("%target_loc_y%", loc_y);
                input = input.replace("%target_loc_z%", loc_z);
            } catch (Exception e) {
                ServerHandler.sendDebugTrace(e);
            }
        }
        return input;
    }

}
