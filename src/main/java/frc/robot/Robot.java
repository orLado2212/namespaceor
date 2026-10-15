// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.spikes2212.dashboard.Namespace;
import com.spikes2212.dashboard.RootNamespace;
import com.spikes2212.util.smartmotorcontrollers.TalonFXWrapper;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.ScheduleCommand;
import frc.robot.commands.Shoot;
import frc.robot.subsystems.Shooter;

import java.util.function.Supplier;

public class Robot extends TimedRobot {

    RootNamespace namespace = new RootNamespace("robot");


    @Override
    public void robotInit() {
        MotorController motorController = new TalonFXWrapper(RobotMap.CAN.MOTOR_ID);
        Shooter shooter = new Shooter("shooter", motorController);
        Supplier<Double> speed = namespace.addTunableDouble("shooter speed", 0);
        Shoot shoot = new Shoot(shooter, speed);
        namespace.publishCommand("shoot",shoot);
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
        namespace.update();
    }

    @Override
    public void disabledInit() {
        CommandScheduler.getInstance().cancelAll();
    }

    @Override
    public void disabledPeriodic() {

    }

    @Override
    public void autonomousInit() {

    }

    @Override
    public void autonomousPeriodic() {

    }

    @Override
    public void teleopInit() {

    }

    @Override
    public void teleopPeriodic() {

    }

    @Override
    public void testInit() {
        CommandScheduler.getInstance().cancelAll();
    }

    @Override
    public void testPeriodic() {

    }

    @Override
    public void simulationInit() {

    }

    @Override
    public void simulationPeriodic() {

    }
}
