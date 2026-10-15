package com.spikes2212.dashboard;

import com.spikes2212.command.drivetrains.swerve.SwerveDrivetrain;
import com.spikes2212.control.FFController;
import com.spikes2212.control.FFSettings;
import com.spikes2212.control.PIDSettings;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.networktables.*;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilderImpl;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class RootNamespace implements Namespace {

    protected final NetworkTable table;
    protected final String name;

    private final List<PublishedField<?>> publishedFields = new ArrayList<>();
    private final HashMap<String, Sendable> sendableTable = new HashMap<>();

    public RootNamespace(String name) {
        this.name = name;
        this.table = NetworkTableInstance.getDefault().getTable(this.name);
    }

    @Override
    public Supplier<Double> addTunableDouble(String key, double defaultValue) {
        DoubleEntry entry = table.getDoubleTopic(key).getEntry(defaultValue);
        entry.setDefault(defaultValue);
        return entry::get;
    }

    @Override
    public Supplier<Boolean> addTunableBoolean(String key, boolean defaultValue) {
        BooleanEntry entry = table.getBooleanTopic(key).getEntry(defaultValue);
        entry.setDefault(defaultValue);
        return entry::get;
    }

    @Override
    public Supplier<Long> addTunableInteger(String key, long defaultValue) {
        IntegerEntry entry = table.getIntegerTopic(key).getEntry(defaultValue);
        entry.setDefault(defaultValue);
        return entry::get;
    }

    @Override
    public Supplier<String> addTunableString(String key, String defaultValue) {
        StringEntry entry = table.getStringTopic(key).getEntry(defaultValue);
        entry.setDefault(defaultValue);
        return entry;
    }

    @Override
    public void publishDouble(String key, Supplier<Double> value) {
        DoublePublisher publisher = table.getDoubleTopic(key).publish();
        publish(value, publisher::set);
    }

    @Override
    public void publishBoolean(String key, Supplier<Boolean> value) {
        BooleanPublisher publisher = table.getBooleanTopic(key).publish();
        publish(value, publisher::set);
    }

    @Override
    public void publishInteger(String key, Supplier<Long> value) {
        IntegerPublisher publisher = table.getIntegerTopic(key).publish();
        publish(value, publisher::set);
    }

    @Override
    public void publishString(String key, Supplier<String> value) {
        StringPublisher publisher = table.getStringTopic(key).publish();
        publish(value, publisher::set);
    }

    @Override
    public void publishSendable(String key, Sendable sendable) {
        sendableTable.put(key, sendable);
        NetworkTable dataTable = table.getSubTable(key);
        SendableBuilderImpl builder = new SendableBuilderImpl();
        builder.setTable(dataTable);
        SendableRegistry.publish(sendable, builder);
        builder.startListeners();
        dataTable.getEntry(".name").setString(key);
    }

    @Override
    public void publishCommand(String key, Command command) {
        publishSendable(key, command);
    }

    @Override
    public void publishRunnable(String key, Runnable runnable) {
        publishCommand(key, new InstantCommand(runnable));
    }

    public void publishDifferentialDrive(String key, DifferentialDrive differentialDrive) {
        publishSendable(key, differentialDrive);
    }

    public void publishSwerveDrive(String key, SwerveDrivetrain drivetrain) {
        publishSendable(key, drivetrain);
    }

    public PIDController addPIDController(String key, PIDSettings initialSettings) {
        PIDController controller = PIDSettings.buildControllerFromSetting(initialSettings);
        publishSendable(key, controller);
        return controller;
    }

    public FFController addFFController(String key, FFSettings initialSettings) {
        FFController controller = new FFController(initialSettings);
        publishSendable(key, controller);
        return controller;
    }

    public void publishCommandScheduler(String key) {
        publishSendable(key, CommandScheduler.getInstance());
    }

    public void publishRobotFieldPose(String key, Field2d robotFieldPose) {
        publishSendable(key, robotFieldPose);
    }

    @Override
    public Sendable getSendable(String key) {
        return sendableTable.get(key);
    }

    private <T> void publish(Supplier<T> valueSupplier, Consumer<T> valuePublisher) {
        PublishedField<T> publishedField = new PublishedField<>(valueSupplier, valuePublisher);
        publishedFields.add(publishedField);
    }

    @Override
    public void update() {
        for (PublishedField<?> value : publishedFields) {
            value.update();
        }
        for (Sendable data : sendableTable.values()) {
            SendableRegistry.update(data);
        }
    }
}

