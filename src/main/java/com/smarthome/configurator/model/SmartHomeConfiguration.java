package com.smarthome.configurator.model;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class SmartHomeConfiguration {

    private final String homeName;
    private final HubType hubType;
    private final List<Room> rooms;
    private final SecurityLevel securityLevel;
    private final AutomationMode automationMode;
    private final String networkSsid;
    private final boolean energySavingEnabled;
    private final boolean voiceControlEnabled;

    public SmartHomeConfiguration(String homeName,
                                   HubType hubType,
                                   List<Room> rooms,
                                   SecurityLevel securityLevel,
                                   AutomationMode automationMode,
                                   String networkSsid,
                                   boolean energySavingEnabled,
                                   boolean voiceControlEnabled) {
        this.homeName = requireNonBlank(homeName, "Home name must not be blank");
        this.hubType = Objects.requireNonNull(hubType, "Hub type must not be null");
        this.rooms = requireNonEmptyRooms(rooms);
        this.securityLevel = Objects.requireNonNull(securityLevel, "Security level must not be null");
        this.automationMode = Objects.requireNonNull(automationMode, "Automation mode must not be null");
        this.networkSsid = requireNonBlank(networkSsid, "Network SSID must not be blank");
        this.energySavingEnabled = energySavingEnabled;
        this.voiceControlEnabled = voiceControlEnabled;
    }

    private static String requireNonBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    private static List<Room> requireNonEmptyRooms(List<Room> rooms) {
        if (rooms == null || rooms.isEmpty()) {
            throw new IllegalArgumentException("A smart home configuration needs at least one room");
        }
        return Collections.unmodifiableList(rooms);
    }

    public String getHomeName() {
        return homeName;
    }

    public HubType getHubType() {
        return hubType;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public SecurityLevel getSecurityLevel() {
        return securityLevel;
    }

    public AutomationMode getAutomationMode() {
        return automationMode;
    }

    public String getNetworkSsid() {
        return networkSsid;
    }

    public boolean isEnergySavingEnabled() {
        return energySavingEnabled;
    }

    public boolean isVoiceControlEnabled() {
        return voiceControlEnabled;
    }

    public int countDevices() {
        int total = 0;
        for (Room room : rooms) {
            total += room.getDevices().size();
        }
        return total;
    }

    public String describe() {
        StringBuilder sb = new StringBuilder();
        sb.append("Smart Home: ").append(homeName).append(System.lineSeparator());
        sb.append("  Hub: ").append(hubType)
                .append(" (max ").append(hubType.getMaxSupportedDevices()).append(" devices)")
                .append(System.lineSeparator());
        sb.append("  Network SSID: ").append(networkSsid).append(System.lineSeparator());
        sb.append("  Security level: ").append(securityLevel).append(System.lineSeparator());
        sb.append("  Automation mode: ").append(automationMode).append(System.lineSeparator());
        sb.append("  Energy saving: ").append(energySavingEnabled ? "ON" : "OFF").append(System.lineSeparator());
        sb.append("  Voice control: ").append(voiceControlEnabled ? "ON" : "OFF").append(System.lineSeparator());
        sb.append("  Rooms (").append(rooms.size()).append("), total devices: ").append(countDevices())
                .append(System.lineSeparator());
        for (Room room : rooms) {
            sb.append("    - ").append(room).append(System.lineSeparator());
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return describe();
    }
}
