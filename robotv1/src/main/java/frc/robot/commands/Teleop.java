package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.gamepad.OI;
import frc.robot.subsystems.DriveTrain;

public class Teleop extends CommandBase {
    private static final DriveTrain driveTrain = RobotContainer.driveTrain;
    private static final OI oi = RobotContainer.oi;

    /**
     * Joystick inputs
     */
    double inputX = 0;
    double inputY = 0;
    double inputZ = 0; // Rotación

    /**
     * Ramp variables (Simplificadas para el nuevo sistema)
     */
    double prevX = 0;
    double prevY = 0;
    double prevZ = 0;

    private static final double RAMP_UP = 0.05;
    private static final double RAMP_DOWN = 0.05;
    private static final double DELTA_LIMIT = 0.075;

    public Teleop() {
        addRequirements(driveTrain);
    }

    @Override
    public void initialize() {
        driveTrain.resetYaw();
    }

    @Override
    public void execute() {
        /**
         * 1. Obtener datos de Joysticks
         * Usamos el Joystick izquierdo para movimiento (X, Y)
         * Usamos el Joystick derecho para rotación (Z)
         */
        double targetX = -oi.getLeftDriveX();
        double targetY = -oi.getLeftDriveY(); // Invertido comúnmente en WPILib
        double targetZ = oi.getRightDriveX();

        /**
         * 2. Aplicar Rampa (Suavizado de aceleración)
         * Repetimos el proceso para los 3 ejes de movimiento
         */
        inputX = applyRamp(targetX, prevX);
        inputY = applyRamp(targetY, prevY);
        inputZ = applyRamp(targetZ, prevZ);

        // Guardar valores anteriores
        prevX = inputX;
        prevY = inputY;
        prevZ = inputZ;

        /**
         * 3. Enviar al Subsystem
         * Ahora usamos el método drive(x, y, z) que creamos
         */
        driveTrain.drive(inputX, inputY, inputZ);

        if (oi.getDriveRightTrigger()) {
            driveTrain.setContinuousServo(0.5); // Velocidad media adelante
        } else if (oi.getDriveRightBumper()) {
            driveTrain.setContinuousServo(-0.5); // Velocidad media atrás
        } else {
            driveTrain.setContinuousServo(0); // Detener
        }
        if (oi.getDriveLeftTrigger()) {
            driveTrain.setContinuousServoGripper(0.5); // Velocidad media adelante
        } else if (oi.getDriveLeftBumper()) {
            driveTrain.setContinuousServoGripper(-0.5); // Velocidad media atrás
        } else {
            driveTrain.setContinuousServoGripper(0); // Detener
        }

        if(oi.getDriveYButton()) {
            driveTrain.resetEncoders();
        }
    }

    /**
     * Función auxiliar para no repetir el código de la rampa 3 veces
     */
    private double applyRamp(double target, double prev) {
        double delta = target - prev;
        if (delta >= DELTA_LIMIT) {
            return prev + RAMP_UP;
        } else if (delta <= -DELTA_LIMIT) {
            return prev - RAMP_DOWN;
        }
        return target;
    }

    @Override
    public void end(boolean interrupted) {
        // Detener los tres ejes
        driveTrain.drive(0.0, 0.0, 0.0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}