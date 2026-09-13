package com.smarthome.configurator.director;

import com.smarthome.configurator.builder.SmartHomeBuilder;
import com.smarthome.configurator.model.AutomationMode;
import com.smarthome.configurator.model.DeviceType;
import com.smarthome.configurator.model.HubType;
import com.smarthome.configurator.model.SecurityLevel;
import com.smarthome.configurator.model.SmartHomeConfiguration;

public final class SmartHomeDirector {

    public SmartHomeConfiguration buildStudioApartment(SmartHomeBuilder builder) {
        return builder
                .setHomeName("Studio Apartment")
                .setHub(HubType.BASIC_HUB)
                .setNetworkSsid("Studio_WiFi")
                .addRoom("Studio")
                .addDeviceToRoom("Studio", DeviceType.LIGHT, "Main Light")
                .addDeviceToRoom("Studio", DeviceType.THERMOSTAT, "Wall Thermostat")
                .addDeviceToRoom("Studio", DeviceType.SMART_PLUG, "Outlet Plug")
                .setSecurityLevel(SecurityLevel.STANDARD)
                .setAutomationMode(AutomationMode.SCHEDULED)
                .enableEnergySaving(true)
                .enableVoiceControl(false)
                .build();
    }

    /**
     * A full-featured configuration for a multi-room family house.
     */
    public SmartHomeConfiguration buildFamilyHouse(SmartHomeBuilder builder) {
        return builder
                .setHomeName("Family House")
                .setHub(HubType.PRO_HUB)
                .setNetworkSsid("FamilyHouse_5G")
                .addRoom("Living Room")
                .addDeviceToRoom("Living Room", DeviceType.LIGHT, "Ceiling Light")
                .addDeviceToRoom("Living Room", DeviceType.SPEAKER, "Smart Speaker")
                .addDeviceToRoom("Living Room", DeviceType.THERMOSTAT, "Living Room Thermostat")
                .addRoom("Front Door")
                .addDeviceToRoom("Front Door", DeviceType.DOOR_LOCK, "Smart Lock")
                .addDeviceToRoom("Front Door", DeviceType.SECURITY_CAMERA, "Doorbell Camera")
                .addRoom("Kitchen")
                .addDeviceToRoom("Kitchen", DeviceType.SMOKE_DETECTOR, "Smoke Detector")
                .addDeviceToRoom("Kitchen", DeviceType.LIGHT, "Kitchen Light")
                .setSecurityLevel(SecurityLevel.HIGH)
                .setAutomationMode(AutomationMode.AI_ADAPTIVE)
                .enableEnergySaving(true)
                .enableVoiceControl(true)
                .build();
    }


    public SmartHomeConfiguration buildSmartOffice(SmartHomeBuilder builder) {
        return builder
                .setHomeName("Smart Office")
                .setHub(HubType.ENTERPRISE_HUB)
                .setNetworkSsid("Office_Secure_WiFi")
                .addRoom("Entrance")
                .addDeviceToRoom("Entrance", DeviceType.DOOR_LOCK, "Badge Lock")
                .addDeviceToRoom("Entrance", DeviceType.SECURITY_CAMERA, "Entrance Camera")
                .addDeviceToRoom("Entrance", DeviceType.MOTION_SENSOR, "Entrance Motion Sensor")
                .addRoom("Open Space")
                .addDeviceToRoom("Open Space", DeviceType.LIGHT, "Overhead Lights")
                .addDeviceToRoom("Open Space", DeviceType.THERMOSTAT, "Zone Thermostat")
                .setSecurityLevel(SecurityLevel.MAXIMUM)
                .setAutomationMode(AutomationMode.SCHEDULED)
                .enableEnergySaving(true)
                .enableVoiceControl(false)
                .build();
    }
}
