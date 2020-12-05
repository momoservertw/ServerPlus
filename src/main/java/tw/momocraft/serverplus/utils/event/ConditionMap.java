package tw.momocraft.serverplus.utils.event;

import tw.momocraft.serverplus.utils.blocksutils.BlocksMap;
import tw.momocraft.serverplus.utils.locationutils.LocationMap;

import java.util.ArrayList;
import java.util.List;

public class ConditionMap {
    private String holdingMenu;
    private List<String> placeholders;

    private List<String> reasons;
    private List<String> ignoreReasons;
    private List<String> boimes;
    private List<String> ignoreBoimes;
    private String liquid;
    private String day;
    private List<LocationMap> locMaps = new ArrayList<>();
    private List<BlocksMap> blocksMaps = new ArrayList<>();


    public String getHoldingMenu() {
        return holdingMenu;
    }

    public List<String> getPlaceholders() {
        return placeholders;
    }


    public String getDay() {
        return day;
    }

    public String getLiquid() {
        return liquid;
    }

    public List<String> getReasons() {
        return reasons;
    }
    public List<String> getIgnoreReasons() {
        return ignoreReasons;
    }

    public List<String> getBoimes() {
        return boimes;
    }

    public List<String> getIgnoreBoimes() {
        return ignoreBoimes;
    }

    public List<LocationMap> getLocMaps() {
        return locMaps;
    }

    public List<BlocksMap> getBlocksMaps() {
        return blocksMaps;
    }


    public void setHoldingMenu(String holdingMenu) {
        this.holdingMenu = holdingMenu;
    }

    public void setPlaceholders(List<String> placeholders) {
        this.placeholders = placeholders;
    }


    public void setReasons(List<String> reasons) {
        this.reasons = reasons;
    }

    public void setIgnoreReasons(List<String> ignoreReasons) {
        this.ignoreReasons = ignoreReasons;
    }

    public void setBoimes(List<String> boimes) {
        this.boimes = boimes;
    }

    public void setIgnoreBoimes(List<String> ignoreBoimes) {
        this.ignoreBoimes = ignoreBoimes;
    }

    public void setLiquid(String liquid) {
        this.liquid = liquid;
    }

    public void setDay(String day) {
        this.day = day;
    }


    public void setBlocksMaps(List<BlocksMap> blocksMaps) {
        this.blocksMaps = blocksMaps;
    }

    public void setLocMaps(List<LocationMap> locationMaps) {
        this.locMaps = locationMaps;
    }
}
