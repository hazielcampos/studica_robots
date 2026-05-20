package frc.robot.commands.driveCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveTrain;

public class WaitStop extends CommandBase {
    private static final DriveTrain drive = RobotContainer.driveTrain;

    /**
     * @param degrees Grados a girar relativos a la posición actual.
     *                Positivo para sentido horario, Negativo para antihorario.
     */
    public WaitStop() {
        addRequirements(drive);
    }

    @Override
    public void initialize() {
        // Importante: No reseteamos el NavX aquí para no perder la orientación global,
        // simplemente calculamos el ángulo objetivo relativo al actual.
        drive.drive(0, 0, 0);
    }

    @Override
    public void execute() {
        drive.drive(0, 0, 0);
    }

    @Override
    public void end(boolean interrupted) {
        drive.drive(0.0, 0.0, 0.0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}