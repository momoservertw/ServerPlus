package tw.momocraft.serverplus.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import tw.momocraft.serverplus.handlers.ConfigHandler;
import tw.momocraft.serverplus.handlers.ServerHandler;

import java.text.SimpleDateFormat;
import java.util.*;

public class Utils {

    public static boolean containsIgnoreCase(String string1, String string2) {
        return string1 != null && string2 != null && string1.toLowerCase().contains(string2.toLowerCase());
    }

    public static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static int getRandom(int lower, int upper) {
        Random random = new Random();
        return random.nextInt((upper - lower) + 1) + lower;
    }

    public static Integer returnInteger(String text) {
        if (text == null) {
            return null;
        } else {
            char[] characters = text.toCharArray();
            Integer value = null;
            boolean isPrevDigit = false;
            for (char character : characters) {
                if (!isPrevDigit) {
                    if (Character.isDigit(character)) {
                        isPrevDigit = true;
                        value = Character.getNumericValue(character);
                    }
                } else {
                    if (Character.isDigit(character)) {
                        value = (value * 10) + Character.getNumericValue(character);
                    } else {
                        break;
                    }
                }
            }
            return value;
        }
    }

    /**
     * @param value      the spawn reason of this entity.
     * @param list       the spawn Reasons in configuration.
     * @param ignoreList the spawn Ignore-Reasons in configuration.
     * @return if the entity spawn reason match the config setting.
     */
    public static boolean containValue(String value, List<String> list, List<String> ignoreList) {
        if (ignoreList.contains(value)) {
            return false;
        }
        if (list.isEmpty()) {
            return true;
        }
        return list.contains(value);
    }

    /**
     * @param operator the comparison operator to compare two numbers.
     * @param number1  first number.
     * @param number2  second number.
     */
    public static boolean getCompare(String operator, int number1, int number2) {
        switch (operator) {
            case ">":
                return number1 > number2;
            case "<":
                return number1 < number2;
            case ">=":
            case "=>":
                return number1 >= number2;
            case "<=":
            case "=<":
                return number1 <= number2;
            case "==":
            case "=":
                return number1 == number2;
        }
        return false;
    }

    /**
     * @param operator the comparison operator to compare two numbers.
     * @param number1  first number.
     * @param number2  second number.
     */
    public static boolean getCompare(String operator, double number1, double number2) {
        switch (operator) {
            case ">":
                return number1 > number2;
            case "<":
                return number1 < number2;
            case ">=":
            case "=>":
                return number1 >= number2;
            case "<=":
            case "=<":
                return number1 <= number2;
            case "==":
            case "=":
                return number1 == number2;
        }
        return false;
    }

    /**
     * @param number the checking number.
     * @param r1     the first side of range.
     * @param r2     another side of range.
     * @return if the check number is inside the range.
     * It will return false if the two side of range numbers are equal.
     */
    public static boolean getRange(int number, int r1, int r2) {
        return r1 <= number && number <= r2 || r2 <= number && number <= r1;
    }

    /**
     * @param number the location of event.
     * @param r      the side of range.
     * @return if the check number is inside the range.
     */
    public static boolean getRange(int number, int r) {
        return -r <= number && number <= r || r <= number && number <= -r;
    }

    public static String getNearbyPlayer(Player player, int range) {
        try {
            ArrayList<Entity> entities = (ArrayList<Entity>) player.getNearbyEntities(range, range, range);
            ArrayList<Block> sightBlock = (ArrayList<Block>) player.getLineOfSight(null, range);
            ArrayList<Location> sight = new ArrayList<>();
            for (Block block : sightBlock) sight.add(block.getLocation());
            for (Location location : sight) {
                for (Entity entity : entities) {
                    if (Math.abs(entity.getLocation().getX() - location.getX()) < 1.3) {
                        if (Math.abs(entity.getLocation().getY() - location.getY()) < 1.5) {
                            if (Math.abs(entity.getLocation().getZ() - location.getZ()) < 1.3) {
                                if (entity instanceof Player) {
                                    return entity.getName();
                                }
                            }
                        }
                    }
                }
            }
            return "INVALID";
        } catch (NullPointerException e) {
            return "INVALID";
        }
    }

    public static String translateLayout(String input, Player player) {
        if (player != null && !(player instanceof ConsoleCommandSender)) {
            String playerName = player.getName();
            // %player%
            try {
                input = input.replace("%player%", playerName);
            } catch (Exception e) {
                ServerHandler.sendDebugTrace(e);
            }
            // %player_display_name%
            try {
                input = input.replace("%player_display_name%", player.getDisplayName());
            } catch (Exception e) {
                ServerHandler.sendDebugTrace(e);
            }
            // %player_uuid%
            try {
                input = input.replace("%player_uuid%", player.getUniqueId().toString());
            } catch (Exception e) {
                ServerHandler.sendDebugTrace(e);
            }
            // %player_interact%
            try {
                input = input.replace("%player_interact%", getNearbyPlayer(player, 3));
            } catch (Exception e) {
                ServerHandler.sendDebugTrace(e);
            }
            Location loc = player.getLocation();
            // %player_world%
            if (input.contains("%player_world%")) {
                input = input.replace("%player_world%", loc.getWorld().getName());
            }
            // %player_loc%
            // %player_loc_x%, %player_loc_y%, %player_loc_z%
            // %player_loc_x_NUMBER%, %player_loc_y_NUMBER%, %player_loc_z_NUMBER%
            if (input.contains("%player_loc")) {
                try {
                    String loc_x = String.valueOf(loc.getBlockX());
                    String loc_y = String.valueOf(loc.getBlockY());
                    String loc_z = String.valueOf(loc.getBlockZ());
                    String[] arr = input.split("%");
                    for (int i = 0; i < arr.length; i++) {
                        switch (arr[i]) {
                            case "player_loc_x":
                                if (arr[i + 1].matches("^-?[0-9]\\d*(\\.\\d+)?$")) {
                                    input = input.replace("%player_loc_x%" + arr[i + 1] + "%", loc_x + Integer.parseInt(arr[i + 1]));
                                }
                                break;
                            case "player_loc_y":
                                if (arr[i + 1].matches("^-?[0-9]\\d*(\\.\\d+)?$")) {
                                    input = input.replace("%player_loc_y%" + arr[i + 1] + "%", loc_y + Integer.parseInt(arr[i + 1]));
                                }
                                break;
                            case "player_loc_z":
                                if (arr[i + 1].matches("^-?[0-9]\\d*(\\.\\d+)?$")) {
                                    input = input.replace("%player_loc_z%" + arr[i + 1] + "%", loc_z + Integer.parseInt(arr[i + 1]));
                                }
                                break;
                        }
                    }
                    input = input.replace("%player_loc%", loc_x + ", " + loc_y + ", " + loc_z);
                    input = input.replace("%player_loc_x%", loc_x);
                    input = input.replace("%player_loc_y%", loc_y);
                    input = input.replace("%player_loc_z%", loc_z);
                } catch (Exception e) {
                    ServerHandler.sendDebugTrace(e);
                }
            }
            if (ConfigHandler.getDepends().VaultEnabled()) {
                if (input.contains("%money%")) {
                    input = input.replace("%money%", String.valueOf(ConfigHandler.getDepends().getVaultApi().getBalance(Bukkit.getOfflinePlayer(player.getUniqueId()))));
                }
            }
            if (ConfigHandler.getDepends().PlayerPointsEnabled()) {
                if (input.contains("%points%")) {
                    input = input.replace("%points%", String.valueOf(ConfigHandler.getDepends().getPlayerPointsApi().getBalance(Bukkit.getOfflinePlayer(player.getUniqueId()))));
                }
            }
        }
        // %player% => CONSOLE
        if (player == null) {
            try {
                input = input.replace("%player%", "CONSOLE");
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
                return PlaceholderAPI.setPlaceholders(player, input);
            } catch (NoSuchFieldError e) {
                ServerHandler.sendDebugMessage("Error has occurred when setting the PlaceHolder " + e.getMessage() + ", if this issue persist contact the developer of PlaceholderAPI.");
                return input;
            }
        }
        return input;
    }

    /**
     * Sort Map keys by values.
     * High -> Low
     *
     * @param map the input map.
     * @param <K>
     * @param <V>
     * @return the sorted map.
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    /**
     * Sort Map keys by values.
     * Low -> High
     *
     * @param map the input map.
     * @param <K>
     * @param <V>
     * @return the sorted map.
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueLow(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static String translateColorCode(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);

    }
}