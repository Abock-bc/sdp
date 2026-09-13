package com.smarthome.configurator.model;
public enum HubType {
    BASIC_HUB(10),
    PRO_HUB(50),
    ENTERPRISE_HUB(200);

    private final int maxSupportedDevices;

    HubType(int maxSupportedDevices) {
        this.maxSupportedDevices = maxSupportedDevices;
    }

    public int getMaxSupportedDevices() {
        return maxSupportedDevices;
    }
}
