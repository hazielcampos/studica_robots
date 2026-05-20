package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.studica.frc.TitanQuad;
import com.studica.frc.TitanQuadEncoder;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import edu.wpi.first.wpilibj.Servo;

public class DriveTrain extends SubsystemBase {
    private TitanQuad leftMotor;
    private TitanQuad rightMotor;
    private TitanQuad backMotor;

    private TitanQuadEncoder leftEncoder;
    private TitanQuadEncoder rightEncoder;
    private TitanQuadEncoder backEncoder;

    private Servo continuousServo;

    private AHRS navX;

    private ShuffleboardTab tab = Shuffleboard.getTab("Training robot");
    private NetworkTableEntry leftEncoderValue = tab.add("Left encoder", 0)
                                                    .getEntry();
    private NetworkTableEntry rightEncoderValue = tab.add("Right encoder", 0)
                                                    .getEntry();
    private NetworkTableEntry backEncoderValue = tab.add("Back encoder", 0)
                                                    .getEntry();
    private NetworkTableEntry distanceForward = tab.add("Distance forward", 0)
                                                    .getEntry();
    private NetworkTableEntry angleValue = tab.add("Angle", 0)
                                                    .getEntry();

    public DriveTrain() {
        leftMotor = new TitanQuad(Constants.TITAN_ID, Constants.LEFT_MOTOR);
        rightMotor = new TitanQuad(Constants.TITAN_ID, Constants.RIGHT_MOTOR);
        backMotor = new TitanQuad(Constants.TITAN_ID, Constants.BACK_MOTOR);

        leftEncoder = new TitanQuadEncoder(leftMotor, Constants.LEFT_MOTOR, Constants.WHEEL_DIST_PER_TICK);
        rightEncoder = new TitanQuadEncoder(rightMotor, Constants.RIGHT_MOTOR, Constants.WHEEL_DIST_PER_TICK);
        backEncoder = new TitanQuadEncoder(backMotor, Constants.BACK_MOTOR, Constants.WHEEL_DIST_PER_TICK);

        navX = new AHRS(SPI.Port.kMXP);

        Timer.delay(1);

        leftMotor.setInverted(false);
        rightMotor.setInverted(false);
        backMotor.setInverted(false);

        continuousServo = new Servo(Constants.SERVO_Z);


    }

    public void setContinuousServo(double speed) {
        continuousServo.set((speed + 1.0) / 2.0);
    }

    /**
     * Mueve el robot usando cinemática de 3 ruedas (Kiwi Drive).
     * 
     * @param x Speed en el eje X (movimiento lateral).
     * @param y Speed en el eje Y (movimiento adelante/atrás).
     * @param z Speed de rotación (giro sobre su propio eje).
     */
    public void drive(double x, double y, double z) {
        // Matemáticas para chasis triangular (ruedas a 120°)
        // Asumiendo que la rueda "Back" está en la parte trasera y paralela al eje X
        
        double wheelBack = x + z;
        double wheelLeft = -0.5 * x + (Math.sqrt(3) / 2) * y + z;
        double wheelRight = -0.5 * x - (Math.sqrt(3) / 2) * y + z;

        // Normalizar velocidades para que ninguna exceda 1.0 o -1.0
        double max = Math.max(Math.abs(wheelBack), Math.max(Math.abs(wheelLeft), Math.abs(wheelRight)));
        if (max > 1.0) {
            wheelBack /= max;
            wheelLeft /= max;
            wheelRight /= max;
        }

        backMotor.set(wheelBack);
        leftMotor.set(wheelLeft);
        rightMotor.set(wheelRight);
    }

    public double getRightEncoderDistance() {
        return rightEncoder.getEncoderDistance();
    }
    public double getLeftEncoderDistance() {
        return leftEncoder.getEncoderDistance();
    }
    public double getBackEncoderDistance() {
        return backEncoder.getEncoderDistance();
    }

    public double getDistanceForward() {
        return (getLeftEncoderDistance() - getRightEncoderDistance()) / Math.sqrt(3);
    }
    public double getDistanceSideways() {
        // La rueda trasera mide X directamente, pero restamos el promedio de las otras
        // para compensar cualquier rotación o movimiento que no sea puramente lateral.
        return (2.0 * getBackEncoderDistance() - getLeftEncoderDistance() - getRightEncoderDistance()) / 3.0;
    }

    public void resetEncoders() {
        rightEncoder.reset();
        leftEncoder.reset();
        backEncoder.reset();
    }

    

    // --- Métodos del NavX ---
    public void resetYaw() { navX.zeroYaw(); }
    public float getYaw() { return navX.getYaw(); }
    public double getAngle() { return navX.getAngle(); }

    @Override
    public void periodic(){
        leftEncoderValue.setDouble(getLeftEncoderDistance());
        rightEncoderValue.setDouble(getRightEncoderDistance());
        backEncoderValue.setDouble(getBackEncoderDistance());

        distanceForward.setDouble(getDistanceForward());
        angleValue.setDouble(getAngle());
    }
}