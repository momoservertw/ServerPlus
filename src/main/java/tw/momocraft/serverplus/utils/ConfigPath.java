package tw.momocraft.serverplus.utils;

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

    private Map<String, Map<List<String>, Double>> lotteryProp;

    //  ============================================== //
    //         MyPet Settings                        //
    //  ============================================== //
    private boolean mypet;
    private boolean mypetSkillAuto;
    private Map<String, List<String>> skillProp;

    //  ============================================== //
    //         Setup all configuration.                //
    //  ============================================== //
    private void setUp() {
        setupLottery();
        setupMyPet();
    }

    private void setupLottery() {
        lottery = ConfigHandler.getConfig("config.yml").getBoolean("Lottery.Enable");
        ConfigurationSection lotteryConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("Lottery.Groups");
        if (lotteryConfig != null) {
            lotteryProp = new HashMap<>();
            ConfigurationSection groupConfig;
            Map<List<String>, Double> groupMap;
            String groupEnable;
            for (String group : lotteryConfig.getKeys(false)) {
                if (group.equals("Enable")) {
                    continue;
                }
                groupEnable = ConfigHandler.getConfig("config.yml").getString("Lottery.Groups." + group + ".Enable");
                if (groupEnable == null || groupEnable.equals("true")) {
                    groupConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("Lottery.Groups." + group);
                    if (groupConfig != null) {
                        groupMap = new HashMap<>();
                        for (String key : groupConfig.getKeys(false)) {
                            if (key.equals("Enable")) {
                                continue;
                            }
                            groupEnable = ConfigHandler.getConfig("config.yml").getString("Lottery.Groups." + group + "." + key + ".Enable");
                            if (groupEnable == null || groupEnable.equals("true")) {
                                groupMap.put(ConfigHandler.getConfig("config.yml").getStringList("Lottery.Groups." + group + "." + key + ".Commands"),
                                        ConfigHandler.getConfig("config.yml").getDouble("Lottery.Groups." + group + "." + key + ".Chance"));
                            }
                        }
                        lotteryProp.put(group.toLowerCase(), groupMap);
                    }
                }
            }
        }
    }

    private void setupMyPet() {
        mypet = ConfigHandler.getConfig("config.yml").getBoolean("MyPet.Enable");
        mypetSkillAuto = ConfigHandler.getConfig("config.yml").getBoolean("MyPet.Skilltree-Auto-Select.Enable");
        ConfigurationSection skillConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("MyPet.Skilltree-Auto-Select.Groups");
        if (skillConfig != null) {
            skillProp = new HashMap<>();
            List<String> commands;
            String groupEnable;
            for (String group : skillConfig.getKeys(false)) {
                if (group.equals("Enable")) {
                    continue;
                }
                groupEnable = ConfigHandler.getConfig("config.yml").getString("MyPet.Skilltree-Auto-Select.Groups." + group + ".Enable");
                if (groupEnable == null || groupEnable.equals("true")) {
                    commands = ConfigHandler.getConfig("config.yml").getStringList("MyPet.Skilltree-Auto-Select.Groups." + group + ".Commands");
                    if (group.equals("Default")) {
                        skillProp.put("Default", commands);
                    }
                    for (String type : ConfigHandler.getConfig("config.yml").getStringList("MyPet.Skilltree-Auto-Select.Groups." + group + ".Types")) {
                        skillProp.put(type, commands);
                    }
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

    public boolean isMypet() {
        return mypet;
    }

    public boolean isMypetSkillAuto() {
        return mypetSkillAuto;
    }

    public Map<String, List<String>> getSkillProp() {
        return skillProp;
    }
}
