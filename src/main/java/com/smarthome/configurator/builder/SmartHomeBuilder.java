package com.smarthome.configurator.builder;

import com.smarthome.configurator.model.AutomationMode;
import com.smarthome.configurator.model.DeviceType;
import com.smarthome.configurator.model.HubType;
import com.smarthome.configurator.model.SecurityLevel;
import com.smarthome.configurator.model.SmartHomeConfiguration;


public interface SmartHomeBuilder {

    SmartHomeBuilder setHomeName(String homeName);

    SmartHomeBuilder setHub(HubType hubType);

    SmartHomeBuilder addRoom(String roomName);

    SmartHomeBuilder addDeviceToRoom(String roomName, DeviceType deviceType, String deviceName);

    SmartHomeBuilder setSecurityLevel(SecurityLevel securityLevel);

    SmartHomeBuilder setAutomationMode(AutomationMode automationMode);

    SmartHomeBuilder setNetworkSsid(String networkSsid);

    SmartHomeBuilder enableEnergySaving(boolean enabled);

    SmartHomeBuilder enableVoiceControl(boolean enabled);


    SmartHomeConfiguration build();
}
