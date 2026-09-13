# Smart Home Configurator — Builder Pattern (Assignment #1)

**Course:** Software Design Patterns
**Pattern:** Builder (creational)
**Domain:** Конфигуратор умного дома / Smart Home Configurator

## 1. What the product is

A **smart home configuration** is a complex object: it bundles a central
hub, a network name, a security posture, an automation strategy, and an
arbitrary number of rooms, each containing an arbitrary number of devices
(lights, thermostats, cameras, locks, sensors, speakers, plugs...).

There is no single "right" way to build one — a studio apartment, a family
house and an office all need very different combinations of the same parts,
built in a step-by-step, often optional, order. That is exactly the
situation the **Builder pattern** is designed for: it separates the
*construction* of a complex object from its *representation*, so the same
construction process can produce many different, immutable results.

### Pattern components in this repository

| Component | Class | Role |
|---|---|---|
| **Product** | `model.SmartHomeConfiguration` | The complex, immutable object being built. |
| **Builder** | `builder.SmartHomeBuilder` (interface) + `builder.StandardSmartHomeBuilder` (concrete) | Fluent, chainable steps that accumulate state and validate it in `build()`. |
| **Director** | `director.SmartHomeDirector` | Knows the exact build sequence for reusable presets: studio apartment, family house, smart office. |
| **Client** | `Main` | Uses the Director for presets and the Builder directly for a fully custom configuration; also demonstrates a validation failure. |

### Project structure

```
smart-home-configurator/
├── pom.xml
├── README.md
├── .gitignore
└── src/main/java/com/smarthome/configurator/
    ├── Main.java                          (Client)
    ├── model/
    │   ├── SmartHomeConfiguration.java     (Product)
    │   ├── Room.java
    │   ├── Device.java
    │   ├── DeviceType.java
    │   ├── HubType.java
    │   ├── SecurityLevel.java
    │   └── AutomationMode.java
    ├── builder/
    │   ├── SmartHomeBuilder.java           (Builder interface)
    │   └── StandardSmartHomeBuilder.java   (Concrete Builder)
    └── director/
        └── SmartHomeDirector.java          (Director)
```

### How to run

With Maven:
```bash
mvn compile exec:java -Dexec.mainClass=com.smarthome.configurator.Main
```

Or plain `javac`/`java`:
```bash
find src -name "*.java" > sources.txt
javac -d out @sources.txt
java -cp out com.smarthome.configurator.Main
```

Sample fluent usage:
```java
SmartHomeConfiguration loft = new StandardSmartHomeBuilder()
        .setHomeName("Downtown Loft")
        .setHub(HubType.PRO_HUB)
        .setNetworkSsid("Loft_WiFi_5G")
        .addRoom("Bedroom")
        .addDeviceToRoom("Bedroom", DeviceType.LIGHT, "Bedside Lamp")
        .setSecurityLevel(SecurityLevel.HIGH)
        .setAutomationMode(AutomationMode.AI_ADAPTIVE)
        .enableVoiceControl(true)
        .build();
```

---

## 2. Clean Code principles applied (Section 3)

Five principles are identified below, each with a short **before / after**
excerpt taken from this codebase (or a rejected earlier draft of it).

### 2.1 Meaningful, intention-revealing names

**Before**
```java
public SmartHomeBuilder sh(HubType h) {
    this.hubType = h;
    return this;
}
```
**After** (`StandardSmartHomeBuilder.java`)
```java
public SmartHomeBuilder setHub(HubType hubType) {
    this.hubType = hubType;
    return this;
}
```
`setHub` and the parameter name `hubType` tell the reader exactly what the
method does and what it expects, without needing a comment. The same
philosophy is applied to every class: `SmartHomeDirector.buildFamilyHouse`,
`SmartHomeConfiguration.countDevices`, `Room.addDevice`, etc. — each name
states its purpose, not its implementation.

### 2.2 Small methods, each doing one thing

**Before** — a single method that both validates and assembles the object,
mixing two responsibilities:
```java
public SmartHomeConfiguration build() {
    if (homeName == null || hubType == null || roomsByName.isEmpty()
            || networkSsid == null) {
        throw new IllegalStateException("missing fields");
    }
    return new SmartHomeConfiguration(homeName, hubType,
            new ArrayList<>(roomsByName.values()), securityLevel,
            automationMode, networkSsid, energySavingEnabled,
            voiceControlEnabled);
}
```
**After** (`StandardSmartHomeBuilder.java`) — validation is extracted into
its own method with a single responsibility, so `build()` reads as a short,
two-step summary:
```java
public SmartHomeConfiguration build() {
    validateRequiredFieldsArePresent();
    List<Room> rooms = new ArrayList<>(roomsByName.values());
    return new SmartHomeConfiguration(homeName, hubType, rooms,
            securityLevel, automationMode, networkSsid,
            energySavingEnabled, voiceControlEnabled);
}
```

### 2.3 Consistent formatting and small, focused classes

Every model class has exactly one reason to change: `Device` only knows
about a single device, `Room` only knows about a room and its devices,
`SmartHomeConfiguration` only knows how to hold and describe a finished
configuration. None of them exceeds ~120 lines, imports are grouped and
unused, and every file follows the same layout: Javadoc → fields →
constructor → accessors. This is visible by comparing `Device.java` and
`Room.java` side by side — despite modelling different concepts, they
share identical structure and indentation, which makes the codebase
predictable to navigate.

### 2.4 Validated construction (fail fast with a clear exception)

**Before** — an invalid configuration would silently produce a broken
object (e.g. zero rooms), and the failure would only surface much later
when something tried to iterate over the rooms:
```java
public SmartHomeConfiguration build() {
    return new SmartHomeConfiguration(homeName, hubType,
            new ArrayList<>(roomsByName.values()), securityLevel,
            automationMode, networkSsid, energySavingEnabled,
            voiceControlEnabled);
}
```
**After** (`StandardSmartHomeBuilder.java` + `SmartHomeConfiguration.java`)
— validation happens in two layers, both with actionable messages:
```java
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
    if (!missingFields.isEmpty()) {
        throw new IllegalStateException(
            "Cannot build SmartHomeConfiguration, missing required steps: "
                + String.join(", ", missingFields));
    }
}
```
The `Main` class demonstrates this directly: calling `build()` on an
incomplete builder throws `IllegalStateException` with the exact list of
missing steps, instead of returning a half-built product.

### 2.5 No magic numbers / magic strings

**Before** — device categories and hub capacities as raw strings/numbers
scattered through the code:
```java
if (deviceCategory.equals("light")) { ... }
int capacity = 50; // what is this for?
```
**After** (`DeviceType.java`, `HubType.java`) — both become named,
compiler-checked constants:
```java
public enum DeviceType {
    LIGHT, THERMOSTAT, SECURITY_CAMERA, DOOR_LOCK,
    MOTION_SENSOR, SMOKE_DETECTOR, SPEAKER, SMART_PLUG
}

public enum HubType {
    BASIC_HUB(10), PRO_HUB(50), ENTERPRISE_HUB(200);

    private final int maxSupportedDevices;
    HubType(int maxSupportedDevices) { this.maxSupportedDevices = maxSupportedDevices; }
    public int getMaxSupportedDevices() { return maxSupportedDevices; }
}
```
The same idea is applied to default values in the builder
(`DEFAULT_SECURITY_LEVEL`, `DEFAULT_AUTOMATION_MODE` constants instead of
inline literals scattered through the class).

---

## 3. Design notes

* **Fluent API / method chaining**: every setter on `SmartHomeBuilder`
  returns `SmartHomeBuilder`, matching the lecture's requirement.
* **Immutability of the Product**: `SmartHomeConfiguration` has only
  final fields and no setters — once built, a configuration cannot be
  mutated from the outside, which is a natural fit for the Builder
  pattern's "assemble once, use many times" philosophy.
* **Director decoupling**: `SmartHomeDirector` only depends on the
  `SmartHomeBuilder` interface, not on `StandardSmartHomeBuilder`, so a
  different concrete builder (e.g. one that builds a JSON preview instead
  of a real configuration) could be substituted without touching the
  director.
