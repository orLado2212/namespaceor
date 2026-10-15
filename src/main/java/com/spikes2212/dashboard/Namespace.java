package com.spikes2212.dashboard;

import com.spikes2212.control.FFController;
import com.spikes2212.control.FFSettings;
import com.spikes2212.control.PIDSettings;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj2.command.Command;

import java.util.function.Supplier;

public interface Namespace {

    Supplier<Boolean> addTunableBoolean(String name, boolean defaultValue);

    Supplier<Double> addTunableDouble(String name, double defaultValue);

    Supplier<Long> addTunableInteger(String name, long defaultValue);

    Supplier<String> addTunableString(String name, String defaultValue);

    void publishSendable(String key, Sendable sendable);

    void publishCommand(String key, Command command);

    default void publishCommand(Command command) {
        publishCommand(command.getName(), command);
    }

    void publishRunnable(String key, Runnable runnable);

    Sendable getSendable(String key);

    void publishString(String key, Supplier<String> value);

    void publishInteger(String key, Supplier<Long> value);

    default void publishString(String key, String value) {
        publishString(key, () -> value);
    }

    void publishDouble(String key, Supplier<Double> value);

    default void publishDouble(String key, Double value) {
        this.publishDouble(key, () -> value);
    }

    void publishBoolean(String key, Supplier<Boolean> value);

    default void publishBoolean(String key, boolean value) {
        this.publishBoolean(key, () -> value);
    }

    default PIDSettings addPIDNamespace(String name, PIDSettings initialPIDSettings) {
        Namespace child = new RootNamespace(name + " pid");
        Supplier<Double> kP = child.addTunableDouble(name + " kP", initialPIDSettings.getkP());
        Supplier<Double> kI = child.addTunableDouble(name + " kI", initialPIDSettings.getkI());
        Supplier<Double> kD = child.addTunableDouble(name + " kD", initialPIDSettings.getkD());
        Supplier<Double> IZone = child.addTunableDouble(name + " i zone", initialPIDSettings.getIZone());
        Supplier<Double> tolerance = child.addTunableDouble(name + " tolerance", initialPIDSettings.getTolerance());
        Supplier<Double> waitTime = child.addTunableDouble(name + " wait time", initialPIDSettings.getWaitTime());
        return new PIDSettings(kP, kI, kD, IZone, tolerance, waitTime);
    }

    default PIDSettings addPIDNamespace(String name) {
        return addPIDNamespace(name, PIDSettings.EMPTY_PID_SETTINGS);
    }

    default FFSettings addFeedForwardNamespace(String name, FFSettings initialFFSettings) {
        Namespace child = new RootNamespace(name + " feed forward");
        Supplier<Double> kS = child.addTunableDouble(name + " kS", initialFFSettings.getkS());
        Supplier<Double> kV = child.addTunableDouble(name + " kV", initialFFSettings.getkV());
        Supplier<Double> kA = child.addTunableDouble(name + " kA", initialFFSettings.getkA());
        Supplier<Double> kG = child.addTunableDouble(name + " kG", initialFFSettings.getkG());
        return new FFSettings(kS, kV, kA, kG, initialFFSettings.getControlMode());
    }

    default FFSettings addFeedForwardNamespace(String name, FFController.ControlMode controlMode) {
        return addFeedForwardNamespace(name, new FFSettings(controlMode));
    }

    void update();
}
