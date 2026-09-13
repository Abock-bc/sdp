package com.smarthome.configurator.model;

import java.util.Objects;


public final class Device {

    private final DeviceType type;
    private final String name;

    public Device(DeviceType type, String name) {
        this.type = Objects.requireNonNull(type, "Device type must not be null");
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Device name must not be blank");
        }
        this.name = name;
    }

    public DeviceType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " (" + type + ")";
    }
}
