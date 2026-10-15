package frc.robot.subsystems;

import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;

public class Shooter extends MotoredGenericSubsystem {
    public Shooter(String namespaceName, MotorController... motorControllers) {
        super(namespaceName, motorControllers);
    }
}
