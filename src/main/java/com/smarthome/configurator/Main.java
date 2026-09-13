package com.smarthome.configurator;

import com.smarthome.configurator.builder.SmartHomeBuilder;
import com.smarthome.configurator.builder.StandardSmartHomeBuilder;
import com.smarthome.configurator.director.SmartHomeDirector;
import com.smarthome.configurator.model.AutomationMode;
import com.smarthome.configurator.model.DeviceType;
import com.smarthome.configurator.model.HubType;
import com.smarthome.configurator.model.SecurityLevel;
import com.smarthome.configurator.model.SmartHomeConfiguration;

public final class Main {

    public static void main(String[] args) {
        demonstrateDirectorPresets();
        demonstrateCustomConfiguration();
        demonstrateValidationFailure();
    }

    private static void demonstrateDirectorPresets() {
        SmartHomeDirector director = new SmartHomeDirector();

        SmartHomeConfiguration studio = director.buildStudioApartment(new StandardSmartHomeBuilder());
        SmartHomeConfiguration familyHouse = director.buildFamilyHouse(new StandardSmartHomeBuilder());
        SmartHomeConfiguration office = director.buildSmartOffice(new StandardSmartHomeBuilder());

        System.out.println(studio.describe());
        System.out.println(familyHouse.describe());
        System.out.println(office.describe());
    }

    private static void demonstrateCustomConfiguration() {
        SmartHomeConfiguration loft = new StandardSmartHomeBuilder()
                .setHomeName("Downtown Loft")
                .setHub(HubType.PRO_HUB)
                .setNetworkSsid("Loft_WiFi_5G")
                .addRoom("Bedroom")
                .addDeviceToRoom("Bedroom", DeviceType.LIGHT, "Bedside Lamp")
                .addDeviceToRoom("Bedroom", DeviceType.THERMOSTAT, "Bedroom Thermostat")
                .addRoom("Living Area")
                .addDeviceToRoom("Living Area", DeviceType.SPEAKER, "Living Speaker")
                .addDeviceToRoom("Living Area", DeviceType.SECURITY_CAMERA, "Living Camera")
                .setSecurityLevel(SecurityLevel.HIGH)
                .setAutomationMode(AutomationMode.AI_ADAPTIVE)
                .enableEnergySaving(false)
                .enableVoiceControl(true)
                .build();

        System.out.println(loft.describe());
    }

    private static void demonstrateValidationFailure() {
        try {
            new StandardSmartHomeBuilder()
                    .setHomeName("Incomplete House")

                    .build();
        } catch (IllegalStateException expected) {
            System.out.println("Caught expected exception: " + expected.getMessage());
        }
    }
}
