/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.I2C;
import com.kauailabs.navx.frc.AHRS;

/**
 * This is a demo program showing how to use Mecanum control with the RobotDrive
 * class.
 */
// Front Left = 0
// Rear Left = 1
// Front Right = 2
// Rear Right = 4
//  
public class Robot extends TimedRobot {
  private static final int kFrontLeftChannel = 0; //2
  private static final int kRearLeftChannel = 1; //4
  private static final int kFrontRightChannel = 2; //1
  private static final int kRearRightChannel = 4; //1

  private static final int kJoystickChannel = 0;

  private MecanumDrive m_robotDrive;
  private Joystick m_stick;
  public AHRS ahrs;

  @Override
  public void robotInit() {
    Spark frontLeft = new Spark(kFrontLeftChannel);
    Spark rearLeft = new Spark(kRearLeftChannel);
    Spark frontRight = new Spark(kFrontRightChannel);
    Spark rearRight = new Spark(kRearRightChannel);

    // Invert the left side motors.
    // You may need to change or remove this to match your robot.
    frontLeft.setInverted(true);
    frontRight.setInverted(true);

    m_robotDrive = new MecanumDrive(frontLeft, rearLeft, frontRight, rearRight);

    m_robotDrive.setExpiration(0.1);

    m_stick = new Joystick(kJoystickChannel);

    try {

      /* Communicate w/navX-MXP via the MXP SPI Bus.                                     */

      /* Alternatively:  I2C.Port.kMXP, SerialPort.Port.kMXP or SerialPort.Port.kUSB     */

      /* See http://navx-mxp.kauailabs.com/guidance/selecting-an-interface/ for details. */

      ahrs = new AHRS(I2C.Port.kOnboard); 

  } catch (RuntimeException ex ) {
        System.out.println("NOT WORKING");
  }
  }

  @Override
  public void teleopPeriodic() {
    // Use the joystick X axis for lateral movement, Y axis for forward
    // movement, and Z axis for rotation.
    m_robotDrive.setSafetyEnabled(true);

      while (isOperatorControl() && isEnabled()) {

        if(m_stick.getRawButton(1)){
            ahrs.reset();
        }

            m_robotDrive.driveCartesian(m_stick.getX(), m_stick.getY() * -1, m_stick.getRawAxis(2), ahrs.getAngle() * -1);
            System.out.println(ahrs.getAngle());
            Timer.delay(0.005);		// wait for a motor update time
      }
  }
}