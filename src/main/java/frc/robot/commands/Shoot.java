package frc.robot.commands;

import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import frc.robot.subsystems.Shooter;

import java.util.function.Supplier;

public class Shoot extends MoveGenericSubsystem {

    public Shoot(Shooter shooter, Supplier<Double> speedSupplier) {
        super(shooter, speedSupplier);
    }
}

