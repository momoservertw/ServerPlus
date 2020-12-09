package tw.momocraft.serverplus.utils;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import tw.momocraft.serverplus.handlers.ConfigHandler;
import tw.momocraft.serverplus.utils.blocksutils.BlocksUtils;
import tw.momocraft.serverplus.utils.customcommands.ParticleMap;
import tw.momocraft.serverplus.utils.customcommands.SoundMap;
import tw.momocraft.serverplus.utils.locationutils.LocationUtils;

import java.util.*;

public class ConfigPath {
    public ConfigPath() {
        setUp();
    }

    //  ============================================== //
    //         General Settings                        //
    //  ============================================== //
    private Map<String, String> customCmdProp;
    private final Map<String, SoundMap> soundProp = new HashMap<>();
    private final Map<String, ParticleMap> particleProp = new HashMap<>();

    private LocationUtils locationUtils;
    private BlocksUtils blocksUtils;

    private String menuIJ;
    private String menuType;
    private String menuName;

    private String vanillaTrans;

    //  ============================================== //
    //         MyPet Settings                        //
    //  ============================================== //
    private boolean myPet;
    private boolean mypetSkillAuto;
    private Map<String, List<String>> skillProp;

    //  ============================================== //
    //         MySQLPlayerDataBridge Settings          //
    //  ============================================== //
    private boolean mpdb;
    private boolean mpdbSyncComplete;
    private List<String> mpdbSyncCompleteCmds;

    //  ============================================== //
    //         ItemJoin Settings                        //
    //  ============================================== //
    private boolean itemjoin;
    private boolean ijFixOldItem;
    private boolean ijOneMenu;
    private String ijOneMenuNode;
    private String ijOneMenuName;
    private String ijOneMenuType;
    private Map<String, List<ItemJoinMap>> ijProp;

    //  ============================================== //
    //         MorphTool Settings                        //
    //  ============================================== //
    private boolean morphtool;
    private boolean morphtoolNetherite;
    private String morphtoolName;

    //  ============================================== //
    //         AuthMe Settings                        //
    //  ============================================== //
    private boolean authMe;
    private boolean authMeMail;

    //  ============================================== //
    //         Setup all configuration.                //
    //  ============================================== //
    private void setUp() {
        setGeneral();
        setMyPet();
        setMpdb();
        setItemJoin();
        setMorphTool();
        setAuthMe();
    }


    //  ============================================== //
    //         Setup General.                          //
    //  ============================================== //
    private void setGeneral() {
        ConfigurationSection cmdConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("General.Custom-Commands");
        if (cmdConfig != null) {
            customCmdProp = new HashMap<>();
            for (String group : cmdConfig.getKeys(false)) {
                customCmdProp.put(group, ConfigHandler.getConfig("config.yml").getString("General.Custom-Commands." + group));
            }
        }
        ConfigurationSection particleConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("General.Particles");
        if (particleConfig != null) {
            ParticleMap particleMap;
            for (String group : particleConfig.getKeys(false)) {
                particleMap = new ParticleMap();
                particleMap.setType(ConfigHandler.getConfig("config.yml").getString("General.Particles." + group + ".Type"));
                particleMap.setAmount(ConfigHandler.getConfig("config.yml").getInt("General.Particles." + group + ".Amount", 1));
                particleMap.setTimes(ConfigHandler.getConfig("config.yml").getInt("General.Particles." + group + ".Times", 1));
                particleMap.setInterval(ConfigHandler.getConfig("config.yml").getInt("General.Particles." + group + ".Interval", 20));
                particleProp.put(group, particleMap);
            }
        }
        ConfigurationSection soundConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("General.Sounds");
        if (soundConfig != null) {
            SoundMap soundMap;
            for (String group : soundConfig.getKeys(false)) {
                soundMap = new SoundMap();
                soundMap.setType(ConfigHandler.getConfig("config.yml").getString("General.Sounds." + group + ".Type"));
                soundMap.setVolume(ConfigHandler.getConfig("config.yml").getInt("General.Sounds." + group + ".Volume", 1));
                soundMap.setPitch(ConfigHandler.getConfig("config.yml").getInt("General.Sounds." + group + ".Pitch", 1));
                soundMap.setTimes(ConfigHandler.getConfig("config.yml").getInt("General.Sounds." + group + ".Loop.Times", 1));
                soundMap.setInterval(ConfigHandler.getConfig("config.yml").getInt("General.Sounds." + group + ".Loop.Interval", 20));
                soundProp.put(group, soundMap);
            }
        }
        locationUtils = new LocationUtils();
        blocksUtils = new BlocksUtils();

        menuIJ = ConfigHandler.getConfig("config.yml").getString("General.Menu.ItemJoin");
        menuType = ConfigHandler.getConfig("config.yml").getString("General.Menu.Item.Type");
        menuName = ConfigHandler.getConfig("config.yml").getString("General.Menu.Item.Name");

        vanillaTrans = ConfigHandler.getConfig("config.yml").getString("General.Vanilla-Translate.Local");
    }

    //  ============================================== //
    //         Setup MyPet.                            //
    //  ============================================== //
    private void setMyPet() {
        myPet = ConfigHandler.getConfig("config.yml").getBoolean("MyPet.Enable");
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
                if (Utils.isEnable(groupEnable, true)) {
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

    //  ============================================== //
    //         MySQLPlayerDataBridge Settings          //
    //  ============================================== //
    private void setMpdb() {
        mpdb = ConfigHandler.getConfig("config.yml").getBoolean("MySQLPlayerDataBridge.Enable");
        mpdbSyncComplete = ConfigHandler.getConfig("config.yml").getBoolean("MySQLPlayerDataBridge.SyncComplete.Enable");
        mpdbSyncCompleteCmds = ConfigHandler.getConfig("config.yml").getStringList("MySQLPlayerDataBridge.SyncComplete.Commands");
    }

    //  ============================================== //
    //         ItemJoin Settings                       //
    //  ============================================== //
    private void setItemJoin() {
        itemjoin = ConfigHandler.getConfig("config.yml").getBoolean("ItemJoin.Enable");
        ijFixOldItem = ConfigHandler.getConfig("config.yml").getBoolean("ItemJoin.Fix-Old-Item.Enable");
        ijOneMenu = ConfigHandler.getConfig("config.yml").getBoolean("ItemJoin.Fix-Old-Item.Settings.One-Menu.Enable");
        ijOneMenuNode = ConfigHandler.getConfig("config.yml").getString("ItemJoin.Fix-Old-Item.Settings.One-Menu.ItemNode");
        ijOneMenuName = ConfigHandler.getConfig("config.yml").getString("ItemJoin.Fix-Old-Item.Settings.One-Menu.Name");
        ijOneMenuType = ConfigHandler.getConfig("config.yml").getString("ItemJoin.Fix-Old-Item.Settings.One-Menu.Type");
        ConfigurationSection ijConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("ItemJoin.Fix-Old-Item.Groups");
        if (ijConfig != null) {
            ijProp = new HashMap<>();
            ItemJoinMap ijMap;
            String itemType;
            for (String group : ijConfig.getKeys(false)) {
                ijMap = new ItemJoinMap();
                ijMap.setItemNode(group);
                ijMap.setName(Utils.translateColorCode(ConfigHandler.getConfig("config.yml").getString("ItemJoin.Fix-Old-Item.Groups." + group + ".Name")));
                itemType = ConfigHandler.getConfig("config.yml").getString("ItemJoin.Fix-Old-Item.Groups." + group + ".Type");
                //ijMap.setLore(Utils.translateColorCode(ConfigHandler.getConfig("config.yml").getString("ItemJoin.Fix-Old-Item.Groups." + group + ".Lore")));
                try {
                    ijProp.get(itemType).add(ijMap);
                } catch (Exception ex) {
                    ijProp.put(itemType, new ArrayList<>());
                    ijProp.get(itemType).add(ijMap);
                }
            }
        }
    }

    //  ============================================== //
    //         MorphTool Settings                      //
    //  ============================================== //
    private void setMorphTool() {
        morphtool = ConfigHandler.getConfig("config.yml").getBoolean("MorphTool.Enable");
        morphtoolNetherite = ConfigHandler.getConfig("config.yml").getBoolean("MorphTool.Prvent-Update-Netherite");
        morphtoolName = Utils.translateColorCode(ConfigHandler.getConfig("config.yml").getString("MorphTool.ToolName"));
    }

    //  ============================================== //
    //         AuthMe Settings          //
    //  ============================================== //
    private void setAuthMe() {
        authMe = ConfigHandler.getConfig("config.yml").getBoolean("AuthMe.Enable");
        authMeMail = ConfigHandler.getConfig("config.yml").getBoolean("AuthMe.Mail-Warning.Enable");
    }

    //  ============================================== //
    //         General Settings                        //
    //  ============================================== //
    public Map<String, String> getCustomCmdProp() {
        return customCmdProp;
    }
    public Map<String, ParticleMap> getParticleProp() {
        return particleProp;
    }
    public Map<String, SoundMap> getSoundProp() {
        return soundProp;
    }

    public String getMenuIJ() {
        return menuIJ;
    }

    public String getMenuName() {
        return menuName;
    }

    public String getMenuType() {
        return menuType;
    }

    public String getVanillaTrans() {
        return vanillaTrans;
    }

    public LocationUtils getLocationUtils() {
        return locationUtils;
    }

    public BlocksUtils getBlocksUtils() {
        return blocksUtils;
    }

    //  ============================================== //
    //         MyPet Settings                          //
    //  ============================================== //
    public boolean isMyPet() {
        return myPet;
    }

    public boolean isMypetSkillAuto() {
        return mypetSkillAuto;
    }

    public Map<String, List<String>> getSkillProp() {
        return skillProp;
    }

    //  ============================================== //
    //         MySQLPlayerDataBridge Settings          //
    //  ============================================== //
    public boolean isMpdb() {
        return mpdb;
    }

    public boolean isMpdbSyncComplete() {
        return mpdbSyncComplete;
    }

    public List<String> getMpdbSyncCompleteCmds() {
        return mpdbSyncCompleteCmds;
    }

    //  ============================================== //
    //         ItemJoin Settings                       //
    //  ============================================== //
    public boolean isItemjoin() {
        return itemjoin;
    }

    public boolean isIjFixOldItem() {
        return ijFixOldItem;
    }

    public boolean isIjOneMenu() {
        return ijOneMenu;
    }

    public String getIjOneMenuNode() {
        return ijOneMenuNode;
    }

    public String getIjOneMenuName() {
        return ijOneMenuName;
    }

    public String getIjOneMenuType() {
        return ijOneMenuType;
    }

    public Map<String, List<ItemJoinMap>> getIjProp() {
        return ijProp;
    }

    //  ============================================== //
    //         MorphTool Settings                      //
    //  ============================================== //
    public boolean isMorphtool() {
        return morphtool;
    }

    public boolean isMorphtoolNetherite() {
        return morphtoolNetherite;
    }

    public String getMorphtoolName() {
        return morphtoolName;
    }

    //  ============================================== //
    //         AuthMe Settings                      //
    //  ============================================== //
    public boolean isAuthMe() {
        return authMe;
    }

    public boolean isAuthMeMail() {
        return authMeMail;
    }

    //  ============================================== //
    //         Others                                  //
    //  ============================================== //
    public List<String> getTypeList(String file, String path, String listType) {
        List<String> list = new ArrayList<>();
        List<String> customList;
        for (String type : ConfigHandler.getConfig(file).getStringList(path)) {
            try {
                if (listType.equals("Entities")) {
                    list.add(EntityType.valueOf(type).name());
                } else if (listType.equals("Materials")) {
                    list.add(Material.valueOf(type).name());
                }
            } catch (Exception e) {
                customList = ConfigHandler.getConfig("groups.yml").getStringList(listType + "." + type);
                if (customList.isEmpty()) {
                    continue;
                }
                // Add Custom Group.
                for (String customType : customList) {
                    try {
                        if (listType.equals("Entities")) {
                            list.add(EntityType.valueOf(customType).name());
                        } else if (listType.equals("Materials")) {
                            list.add(Material.valueOf(customType).name());
                        }
                    } catch (Exception ex) {
                    }
                }
            }
        }
        return list;
    }
}
