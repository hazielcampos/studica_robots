package frc.robot.commands.driveCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveTrain;

public class DriveWithDistance extends CommandBase {
    private static final DriveTrain drive = RobotContainer.driveTrain;

    private double mm;
    private double speed;
    private double startDistance; // Guardaremos la distancia inicial aquí
    private int axis;

    public DriveWithDistance(double mm, double speed, int axis) {
        this.mm = mm;
        this.speed = speed;
        this.axis = axis;
        addRequirements(drive);
    }

    @Override
    public void initialize() {
        // En lugar de resetear el hardware, tomamos una "foto" de la distancia actual
        if(axis == 0) {
            startDistance = drive.getDistanceForward();
        } else {
            startDistance = drive.getDistanceSideways();
        }
    }

    @Override
    public void execute() {
        if(axis == 0) {
            drive.drive(0, speed, 0);
        } else {
            drive.drive(speed, 0, 0);
        }
    }

    @Override
    public void end(boolean interrupted) {
        drive.drive(0.0, 0.0, 0.0);
    }

    @Override
    public boolean isFinished() {
        // Calculamos cuánto hemos avanzado desde que empezó el comando
        if(axis == 0) {
            double currentDistance = drive.getDistanceForward();
            double traveled = Math.abs(currentDistance - startDistance);
            return traveled >= mm;
        } else {
            double currentDistance = drive.getDistanceSideways();
            double traveled = Math.abs(currentDistance - startDistance);
            return traveled >= mm;
        }
        
    }
}