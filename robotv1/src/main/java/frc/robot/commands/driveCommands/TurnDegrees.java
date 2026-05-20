package frc.robot.commands.driveCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveTrain;

public class TurnDegrees extends CommandBase {
    private static final DriveTrain drive = RobotContainer.driveTrain;

    private double targetAngle;
    private double targetDegrees;
    private double speed; // Velocidad base de rotación

    /**
     * @param degrees Grados a girar relativos a la posición actual.
     *                Positivo para sentido horario, Negativo para antihorario.
     */
    public TurnDegrees(double degrees, double speed) {
        this.targetDegrees = degrees;
        this.speed = speed;
        addRequirements(drive);
    }

    @Override
    public void initialize() {
        // Importante: No reseteamos el NavX aquí para no perder la orientación global,
        // simplemente calculamos el ángulo objetivo relativo al actual.
        targetAngle = drive.getAngle() + targetDegrees;
    }

    @Override
    public void execute() {
        // Calculamos cuánto falta
        double error = targetAngle - drive.getAngle();
        
        // Determinamos la dirección del giro (z positivo es horario en tu DriveTrain)
        if (error > 0) {
            drive.drive(0, 0, speed); // Gira a la derecha
        } else {
            drive.drive(0, 0, -speed); // Gira a la izquierda
        }
    }

    @Override
    public void end(boolean interrupted) {
        drive.drive(0.0, 0.0, 0.0);
    }

    @Override
    public boolean isFinished() {
        double error = targetAngle - drive.getAngle();
        
        // Terminamos cuando el error sea pequeño (umbral de 1-2 grados)
        // Usamos valor absoluto para que funcione en ambos sentidos
        return Math.abs(error) < 2.0;
    }
}