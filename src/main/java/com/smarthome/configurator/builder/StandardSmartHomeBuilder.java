package com.smarthome.configurator.builder;

import com.smarthome.configurator.model.AutomationMode;
import com.smarthome.configurator.model.Device;
import com.smarthome.configurator.model.DeviceType;
import com.smarthome.configurator.model.HubType;
import com.smarthome.configurator.model.Room;
import com.smarthome.configurator.model.SecurityLevel;
import com.smarthome.configurator.model.SmartHomeConfiguration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class StandardSmartHomeBuilder implements SmartHomeBuilder {

    private static final SecurityLevel DEFAULT_SECURITY_LEVEL = SecurityLevel.STANDARD;
    private static final AutomationMode DEFAULT_AUTOMATION_MODE = AutomationMode.MANUAL;

    private String homeName;
    private HubType hubType;
    private final Map<String, Room> roomsByName = new LinkedHashMap<>();
    private SecurityLevel securityLevel = DEFAULT_SECURITY_LEVEL;
    private AutomationMode automationMode = DEFAULT_AUTOMATION_MODE;
    private String networkSsid;
    private boolean energySavingEnabled;
    private boolean voiceControlEnabled;

    @Override
    public SmartHomeBuilder setHomeName(String homeName) {
        this.homeName = homeName;
        return this;
    }

    @Override
    public SmartHomeBuilder setHub(HubType hubType) {
        this.hubType = hubType;
        return this;
    }

    @Override
    public SmartHomeBuilder addRoom(String roomName) {
        roomsByName.computeIfAbsent(roomName, Room::new);
        return this;
    }

    @Override
    public SmartHomeBuilder addDeviceToRoom(String roomName, DeviceType deviceType, String deviceName) {
        Room room = roomsByName.get(roomName);
        if (room == null) {
            throw new IllegalStateException(
                    "Cannot add a device to unknown room '" + roomName + "'. Call addRoom(...) first.");
        }
        room.addDevice(new Device(deviceType, deviceName));
        return this;
    }

    @Override
    public SmartHomeBuilder setSecurityLevel(SecurityLevel securityLevel) {
        this.securityLevel = securityLevel;
        return this;
    }

    @Override
    public SmartHomeBuilder setAutomationMode(AutomationMode automationMode) {
        this.automationMode = automationMode;
        return this;
    }

    @Override
    public SmartHomeBuilder setNetworkSsid(String networkSsid) {
        this.networkSsid = networkSsid;
        return this;
    }

    @Override
    public SmartHomeBuilder enableEnergySaving(boolean enabled) {
        this.energySavingEnabled = enabled;
        return this;
    }

    @Override
    public SmartHomeBuilder enableVoiceControl(boolean enabled) {
        this.voiceControlEnabled = enabled;
        return this;
    }

    @Override
    public SmartHomeConfiguration build() {
        validateRequiredFieldsArePresent();
        List<Room> rooms = new ArrayList<>(roomsByName.values());
        return new SmartHomeConfiguration(
                homeName,
                hubType,
                rooms,
                securityLevel,
                automationMode,
                networkSsid,
                energySavingEnabled,
                voiceControlEnabled);
    }


    private void validateRequiredFieldsArePresent() {
        List<String> missingFields = new ArrayList<>();

        if (homeName == null || homeName.isBlank()) {
            missingFields.add("home name (setHomeName)");
        }
        if (hubType == null) {
            missingFields.add("hub type (setHub)");
        }
        if (roomsByName.isEmpty()) {
            missingFields.add("at least one room (addRoom)");
        }
        if (networkSsid == null || networkSsid.isBlank()) {
            missingFields.add("network SSID (setNetworkSsid)");
        }

        if (!missingFields.isEmpty()) {
            throw new IllegalStateException(
                    "Cannot build SmartHomeConfiguration, missing required steps: "
                            + String.join(", ", missingFields));
        }
    }
}
