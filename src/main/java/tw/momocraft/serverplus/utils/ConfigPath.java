package tw.momocraft.serverplus.utils;

import org.bukkit.configuration.ConfigurationSection;
import tw.momocraft.serverplus.handlers.ConfigHandler;

import java.util.ArrayList;
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
    //         MyPet Settings                        //
    //  ============================================== //
    private boolean mypet;
    private boolean mypetSkillAuto;
    private boolean mypetSkillCreate;
    private boolean mypetSkillCall;
    private Map<String, List<String>> skillProp = new HashMap<>();

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

    private void setupMyPet() {
        mypet = ConfigHandler.getConfig("config.yml").getBoolean("MyPet.Enable");
        mypetSkillAuto = ConfigHandler.getConfig("config.yml").getBoolean("MyPet.Skilltree-Auto-Select.Enable");
        mypetSkillCreate = ConfigHandler.getConfig("config.yml").getBoolean("MyPet.Skilltree-Auto-Select.Settings.Create");
        mypetSkillCall = ConfigHandler.getConfig("config.yml").getBoolean("MyPet.Skilltree-Auto-Select.Settings.Call");
        ConfigurationSection skillConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("MyPet.Skilltree-Auto-Select.Groups");
        if (skillConfig != null) {
            List<String> cmdList;
            for (String group : skillConfig.getKeys(false)) {
                if (group.equals("Enable")) {
                    continue;
                }
                cmdList = ConfigHandler.getConfig("config.yml").getStringList("MyPet.Skilltree-Auto-Select.Groups." + group + ".List");
                for (String type : ConfigHandler.getConfig("config.yml").getStringList("MyPet.Skilltree-Auto-Select.Groups." + group + ".Types")) {
                    skillProp.put(type, cmdList);
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

    public boolean isMypetSkillCall() {
        return mypetSkillCall;
    }

    public boolean isMypetSkillCreate() {
        return mypetSkillCreate;
    }

    public Map<String, List<String>> getSkillProp() {
        return skillProp;
    }
}
