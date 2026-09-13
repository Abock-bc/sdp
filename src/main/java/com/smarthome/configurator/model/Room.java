package com.smarthome.configurator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public final class Room {

    private final String name;
    private final List<Device> devices = new ArrayList<>();

    public Room(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Room name must not be blank");
        }
        this.name = name;
    }

    public void addDevice(Device device) {
        devices.add(device);
    }

    public String getName() {
        return name;
    }

    public List<Device> getDevices() {
        return Collections.unmodifiableList(devices);
    }

    @Override
    public String toString() {
        return name + " -> " + devices;
    }
}
