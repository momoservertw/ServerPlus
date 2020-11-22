package tw.momocraft.serverplus.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import tw.momocraft.serverplus.handlers.ConfigHandler;
import tw.momocraft.serverplus.handlers.ServerHandler;

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

    private static String getNearbyPlayer(Player player, int range) {
        try {
            ArrayList<Entity> entities = (ArrayList<Entity>) player.getNearbyEntities(range, range, range);
            ArrayList<Block> sightBlock = (ArrayList<Block>) player.getLineOfSight( null, range);
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
        String playerName = "EXEMPT";
        if (player != null) {
            playerName = player.getName();
        }
        if (player != null && !(player instanceof ConsoleCommandSender)) {
            try {
                input = input.replace("%player%", playerName);
            } catch (Exception e) {
                ServerHandler.sendDebugTrace(e);
            }
            try {
                input = input.replace("%player_interact%", getNearbyPlayer(player, 3));
            } catch (Exception e) {
                ServerHandler.sendDebugTrace(e);
            }
            try {
                // %random_number%500%
                if (input.contains("%random_number%")) {
                    String[] arr = input.split("%");
                    List<Integer> list = new ArrayList<>();
                    for (int i = 0; i < arr.length; i++) {
                        if (arr[i].equals("random_number")) {
                            list.add(Integer.parseInt(arr[i + 1]));
                        }
                    }
                    for (int max : list) {
                        input = input.replace("%random_number%" + max + "%", String.valueOf(new Random().nextInt(max)));
                    }
                }
            } catch (Exception e) {
                ServerHandler.sendDebugTrace(e);
            }
            try {
                // %random_player%
                try {
                    List<Player> playerList = new ArrayList(Bukkit.getOnlinePlayers());
                    String randomPlayer = playerList.get(new Random().nextInt(playerList.size())).getName();
                    input = input.replace("%random_player%", randomPlayer);
                } catch (Exception e) {
                    ServerHandler.sendDebugTrace(e);
                }
            } catch (Exception e) {
                ServerHandler.sendDebugTrace(e);
            }
        } else {
            try {
                input = input.replace("%player%", "CONSOLE");
            } catch (Exception e) {
                ServerHandler.sendDebugTrace(e);
            }
        }
        input = ChatColor.translateAlternateColorCodes('&', input);
        if (ConfigHandler.getDepends().PlaceHolderAPIEnabled()) {
            try {
                try {
                    String s = PlaceholderAPI.setPlaceholders(player, input);
                    return s;
                } catch (NoSuchFieldError e) {
                    ServerHandler.sendDebugMessage("Error has occured when setting the PlaceHolder " + e.getMessage() + ", if this issue persits contact the developer of PlaceholderAPI.");
                    return input;
                }
            } catch (Exception e) {
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