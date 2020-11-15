package tw.momocraft.serverplus.utils;


import javafx.util.Pair;
import org.bukkit.configuration.ConfigurationSection;
import tw.momocraft.serverplus.handlers.ConfigHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigPath {
    public ConfigPath() {
        setUp();
    }

    //  ============================================== //
    //         General Settings                        //
    //  ============================================== //


    //  ============================================== //
    //         Lottery Settings                        //
    //  ============================================== //

    private boolean lottery;

    private Map<String, Map<List<String>, Double>> lotteryProp = new HashMap<>();

    //  ============================================== //
    //         Setup all configuration.                //
    //  ============================================== //
    private void setUp() {
        setupLottery();
    }

    private void setupLottery() {
        lottery = ConfigHandler.getConfig("config.yml").getBoolean("Lottery.Enable");
        ConfigurationSection lotteryConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("Lottery.Groups");
        if (lotteryConfig != null) {
            ConfigurationSection groupConfig;
            Map<List<String>, Double> groupMap;
            for (String group : lotteryConfig.getKeys(false)) {
                if (group.equals("Enable")) {
                    continue;
                }
                groupConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("Lottery.Groups." + group);
                if (groupConfig != null) {
                    groupMap = new HashMap<>();
                    for (String key : groupConfig.getKeys(false)) {
                        if (key.equals("Enable")) {
                            continue;
                        }
                        groupMap.put(ConfigHandler.getConfig("config.yml").getStringList("Lottery.Groups." + group + "." + key + ".List"),
                                ConfigHandler.getConfig("config.yml").getDouble("Lottery.Groups." + group + "." + key + ".Chance"));
                    }
                    lotteryProp.put(group.toLowerCase(), groupMap);
                }
            }
        }
    }

    public boolean isLottery() {
        return lottery;
    }

    public Map<String, Map<List<String>, Double>> getLotteryProp() {
        return lotteryProp;
    }
}
