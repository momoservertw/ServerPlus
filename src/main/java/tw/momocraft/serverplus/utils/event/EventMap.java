package tw.momocraft.serverplus.utils.event;

import java.util.List;
import java.util.Map;

public class EventMap {

    private long priority;

    private List<String> events;
    private Map<String, List<String>> targets;
    private List<ConditionMap> conditions;
    private List<ActionMap> actions;

    public long getPriority() {
        return priority;
    }

    public List<String> getEvents() {
        return events;
    }

    public Map<String, List<String>> getTargets() {
        return targets;
    }

    public List<ConditionMap> getConditions() {
        return conditions;
    }

    public List<ActionMap> getActions() {
        return actions;
    }

    public void setPriority(long priority) {
        this.priority = priority;
    }

    public void setEvents(List<String> events) {
        this.events = events;
    }

    public void setTargets(Map<String, List<String>> targets) {
        this.targets = targets;
    }

    public void setConditions(List<ConditionMap> conditions) {
        this.conditions = conditions;
    }

    public void setActions(List<ActionMap> actions) {
        this.actions = actions;
    }
}
