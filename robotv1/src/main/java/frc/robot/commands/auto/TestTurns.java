package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.driveCommands.DriveWithDistance;
import frc.robot.commands.driveCommands.TurnDegrees;
import frc.robot.commands.driveCommands.WaitStop;

public class TestTurns extends AutoCommand
{
    public TestTurns ()
    {
        super(
            new DriveWithDistance(500, 0.3, 0),
            new TurnDegrees(90, 0.4),
            new DriveWithDistance(500, 0.3, 0),
            new TurnDegrees(90, 0.4),
            new DriveWithDistance(500, 0.3, 0),
            new TurnDegrees(90, 0.4),
            new DriveWithDistance(500, 0.3, 0),
            new TurnDegrees(90, 0.4)

        );
    }
}