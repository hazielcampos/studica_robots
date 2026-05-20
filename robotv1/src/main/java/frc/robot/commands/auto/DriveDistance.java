package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.driveCommands.DriveWithDistance;
import frc.robot.commands.driveCommands.TurnDegrees;
import frc.robot.commands.driveCommands.WaitStop;

public class DriveDistance extends AutoCommand
{
    public DriveDistance ()
    {
        super(
            new DriveWithDistance(300, 0.3, 0).withTimeout(20),
            new WaitStop().withTimeout(2),
            new DriveWithDistance(130, -0.3, 1).withTimeout(20),
            new WaitStop().withTimeout(2),
            new DriveWithDistance(130, -0.3, 1),
            new WaitStop().withTimeout(2),
            new DriveWithDistance(130, -0.3, 1),
            new WaitStop().withTimeout(2),
            new DriveWithDistance(300, -0.3, 0),
            new DriveWithDistance(390, 0.3, 1),
            new DriveWithDistance(300, 0.3, 0).withTimeout(20),
            new WaitStop().withTimeout(2),
            new DriveWithDistance(130, -0.3, 1).withTimeout(20),
            new WaitStop().withTimeout(2),
            new DriveWithDistance(130, -0.3, 1),
            new WaitStop().withTimeout(2),
            new DriveWithDistance(130, -0.3, 1),
            new WaitStop().withTimeout(2),
            new DriveWithDistance(300, -0.3, 0),
            new DriveWithDistance(390, 0.3, 1)
        );
    }
}