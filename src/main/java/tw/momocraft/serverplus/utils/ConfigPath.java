package tw.momocraft.serverplus.utils;

import org.bukkit.configuration.ConfigurationSection;
import tw.momocraft.coreplus.api.CorePlusAPI;
import tw.momocraft.serverplus.handlers.ConfigHandler;

import java.util.*;

public class ConfigPath {
    public ConfigPath() {
        setUp();
    }

    //  ============================================== //
    //         Message Variables                       //
    //  ============================================== //
    private String msgTitle;
    private String msgHelp;
    private String msgReload;
    private String msgVersion;
    private String msgItemJoinFix;
    private String msgMorphToolRename;
    private String msgMorphToolNetherite;
    private String msgAuthMeMailEmpty;
    private String msgDonateGroupNotFound;

    //  ============================================== //
    //         General Variables                       //
    //  ============================================== //
    private String vanillaTrans;

    //  ============================================== //
    //         MyPet Settings                        //
    //  ============================================== //
    private boolean myPet;
    private boolean mypetSkillAuto;
    private Map<String, List<String>> skillProp;

    //  ============================================== //
    //         MySQLPlayerDataBridge Variables         //
    //  ============================================== //
    private boolean mpdb;
    private boolean mpdbSyncComplete;
    private List<String> mpdbSyncCompleteCmds;

    //  ============================================== //
    //         ItemJoin Variables                      //
    //  ============================================== //
    private boolean itemjoin;
    private boolean ijFixOldItem;
    private boolean ijOneMenu;
    private String ijOneMenuNode;
    private String ijOneMenuName;
    private String ijOneMenuType;
    private Map<String, List<ItemJoinMap>> ijProp;

    //  ============================================== //
    //         MorphTool Variables                     //
    //  ============================================== //
    private boolean morphtool;
    private boolean morphtoolNetherite;
    private String morphtoolName;

    //  ============================================== //
    //         AuthMe Variables                        //
    //  ============================================== //
    private boolean authMe;
    private boolean authMeMail;

    //  ============================================== //
    //         Donate Variables                        //
    //  ============================================== //
    private boolean donate;
    private final Map<String, DonateMap> donateProp = new HashMap<>();

    //  ============================================== //
    //         Setup all configuration                 //
    //  ============================================== //
    private void setUp() {
        setupMsg();
        setGeneral();
        setMyPet();
        setMpdb();
        setItemJoin();
        setMorphTool();
        setAuthMe();
        setDonate();
    }

    //  ============================================== //
    //         Message Setter                          //
    //  ============================================== //
    private void setupMsg() {
        msgTitle = ConfigHandler.getConfig("config.yml").getString("Message.Commands.title");
        msgHelp = ConfigHandler.getConfig("config.yml").getString("Message.Commands.help");
        msgReload = ConfigHandler.getConfig("config.yml").getString("Message.Commands.reload");
        msgVersion = ConfigHandler.getConfig("config.yml").getString("Message.Commands.version");
        msgItemJoinFix = ConfigHandler.getConfig("config.yml").getString("Message.itemJoinFix");
        msgMorphToolRename = ConfigHandler.getConfig("config.yml").getString("Message.morphToolRename");
        msgMorphToolNetherite = ConfigHandler.getConfig("config.yml").getString("Message.morphToolNetherite");
        msgAuthMeMailEmpty = ConfigHandler.getConfig("config.yml").getString("Message.authMeMailEmpty");
        msgDonateGroupNotFound = ConfigHandler.getConfig("config.yml").getString("Message.donateGroupNotFound");
    }


    //  ============================================== //
    //         General Setter                          //
    //  ============================================== //
    private void setGeneral() {
        vanillaTrans = ConfigHandler.getConfig("config.yml").getString("General.Vanilla-Translate.Local");
    }

    //  ============================================== //
    //         MyPet Setter                            //
    //  ============================================== //
    private void setMyPet() {
        myPet = ConfigHandler.getConfig("config.yml").getBoolean("MyPet.Enable");
        mypetSkillAuto = ConfigHandler.getConfig("config.yml").getBoolean("MyPet.Skilltree-Auto-Select.Enable");
        ConfigurationSection skillConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("MyPet.Skilltree-Auto-Select.Groups");
        if (skillConfig != null) {
            skillProp = new HashMap<>();
            List<String> commands;
            for (String group : skillConfig.getKeys(false)) {
                if (group.equals("Enable")) {
                    continue;
                }
                if (!ConfigHandler.getConfig("config.yml").getBoolean("MyPet.Skilltree-Auto-Select.Groups." + group + ".Enable", true)) {
                    continue;
                }
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

    //  ============================================== //
    //         MySQLPlayerDataBridge Setter            //
    //  ============================================== //
    private void setMpdb() {
        mpdb = ConfigHandler.getConfig("config.yml").getBoolean("MySQLPlayerDataBridge.Enable");
        mpdbSyncComplete = ConfigHandler.getConfig("config.yml").getBoolean("MySQLPlayerDataBridge.SyncComplete.Enable");
        mpdbSyncCompleteCmds = ConfigHandler.getConfig("config.yml").getStringList("MySQLPlayerDataBridge.SyncComplete.Commands");
    }

    //  ============================================== //
    //         ItemJoin Setter                         //
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
                ijMap.setName(CorePlusAPI.getUtilsManager().translateColorCode(ConfigHandler.getConfig("config.yml").getString("ItemJoin.Fix-Old-Item.Groups." + group + ".Name")));
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
    //         MorphTool Setter                        //
    //  ============================================== //
    private void setMorphTool() {
        morphtool = ConfigHandler.getConfig("config.yml").getBoolean("MorphTool.Enable");
        morphtoolNetherite = ConfigHandler.getConfig("config.yml").getBoolean("MorphTool.Prvent-Update-Netherite");
        morphtoolName = CorePlusAPI.getUtilsManager().translateColorCode(ConfigHandler.getConfig("config.yml").getString("MorphTool.ToolName"));
    }

    //  ============================================== //
    //         AuthMe Setter                           //
    //  ============================================== //
    private void setAuthMe() {
        authMe = ConfigHandler.getConfig("config.yml").getBoolean("AuthMe.Enable");
        authMeMail = ConfigHandler.getConfig("config.yml").getBoolean("AuthMe.Mail-Warning.Enable");
    }

    //  ============================================== //
    //         MorphTool Setter                        //
    //  ============================================== //
    private void setDonate() {
        donate = ConfigHandler.getConfig("config.yml").getBoolean("Donate.Enable");
        ConfigurationSection donateConfig = ConfigHandler.getConfig("config.yml").getConfigurationSection("Donate.Groups");
        if (donateConfig == null)
            return;
        DonateMap donateMap;
        for (String group : donateConfig.getKeys(false)) {
            if (!ConfigHandler.getConfig("config.yml").getBoolean("Donate.Groups." + group + ".Enable", true))
                continue;
            donateMap = new DonateMap();
            donateMap.setGroup(ConfigHandler.getConfig("config.yml").getString("Donate.Groups." + group + ".Group"));
            donateMap.setNextGroup(ConfigHandler.getConfig("config.yml").getString("Donate.Groups." + group + ".Next-Group"));
            donateMap.setCommands(ConfigHandler.getConfig("config.yml").getStringList("Donate.Groups." + group + ".Commands"));
            donateMap.setFailedCommands(ConfigHandler.getConfig("config.yml").getStringList("Donate.Groups." + group + ".Failed-Commands"));
            donateProp.put(group, donateMap);
        }
    }

    //  ============================================== //
    //         Message Getter                          //
    //  ============================================== //
    public String getMsgTitle() {
        return msgTitle;
    }

    public String getMsgHelp() {
        return msgHelp;
    }

    public String getMsgReload() {
        return msgReload;
    }

    public String getMsgVersion() {
        return msgVersion;
    }

    public String getMsgItemJoinFix() {
        return msgItemJoinFix;
    }

    public String getMsgMorphToolRename() {
        return msgMorphToolRename;
    }

    public String getMsgMorphToolNetherite() {
        return msgMorphToolNetherite;
    }

    public String getMsgAuthMeMailEmpty() {
        return msgAuthMeMailEmpty;
    }

    public String getMsgDonateGroupNotFound() {
        return msgDonateGroupNotFound;
    }

    //  ============================================== //
    //         General Getter                          //
    //  ============================================== //
    public String getVanillaTrans() {
        return vanillaTrans;
    }

    //  ============================================== //
    //         MyPet Getter                            //
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
    //         MySQLPlayerDataBridge Getter            //
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
    //         ItemJoin Getter                         //
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
    //         MorphTool Getter                        //
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
    //         AuthMe Getter                           //
    //  ============================================== //
    public boolean isAuthMe() {
        return authMe;
    }

    public boolean isAuthMeMail() {
        return authMeMail;
    }

    //  ============================================== //
    //         Donate Getter                           //
    //  ============================================== //
    public boolean isDonate() {
        return donate;
    }

    public Map<String, DonateMap> getDonateProp() {
        return donateProp;
    }
}
