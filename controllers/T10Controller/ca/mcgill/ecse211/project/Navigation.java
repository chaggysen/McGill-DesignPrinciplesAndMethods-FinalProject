package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.*;
import static java.lang.Math.*;
import static simlejos.ExecutionController.performPhysicsStep;

import java.util.concurrent.TimeUnit;

import ca.mcgill.ecse211.playingfield.Point;
import simlejos.hardware.ev3.LocalEV3;

/**
 * The Navigation class is used to move and operate the robot.
 * 
 * @author Simon Nakane Marcil
 * @author Sen Wang
 */
public class Navigation {
  
  /** Do not instantiate this class. */
  private Navigation() {}
  
  /** Travels to the given destination.
   * 
   * @param destination Point the robot will head to
   * @param distOffset Distance in feet the robot will stop at before arriving to destination
   */
  public static void travelTo(Point destination, double distOffset) {
    var xyt = odometer.getXyt();
    var currentLocation = new Point(xyt[0] / TILE_SIZE, xyt[1] / TILE_SIZE);
    var currentTheta = xyt[2];
    var destinationTheta = getDestinationAngle(currentLocation, destination);
    turnBy(minimalAngle(currentTheta, destinationTheta));
    moveStraightFor(distanceBetween(currentLocation, destination) - distOffset);
  }
  /** Travels to the given destination.
   * 
   * @param destination Point the robot will head to
   * @param distOffset Distance in feet the robot will stop at before arriving to destination
   */
  public static void travelToByYFirst(Point destination, double distOffset) {
     var xyt = odometer.getXyt();
     var currentLocation = new Point(xyt[0] / TILE_SIZE, xyt[1] / TILE_SIZE);
     Point destinationA = new Point(currentLocation.x, destination.y);
     travelTo(destinationA);
     travelTo(destination, distOffset);
  }
  /** Travels to the given destination.
   * 
   * @param destination Point the robot will head to
   * @param distOffset Distance in feet the robot will stop at before arriving to destination
   */
  public static void travelToByXFirst(Point destination, double distOffset) {
     var xyt = odometer.getXyt();
     var currentLocation = new Point(xyt[0] / TILE_SIZE, xyt[1] / TILE_SIZE);
     Point destinationA = new Point(destination.x, currentLocation.y);
     travelTo(destinationA);
     travelTo(destination, distOffset);
  }
  
  /**
   * Travels to the given destination (Avoids obstacle by default).
   * 
   * @param destination The point the robot will head to
   */
  public static void travelTo(Point destination) {
    travelTo(destination, 0.0);
  }
  
  /**
   * Returns the angle that the robot should point towards to face the destination in degrees.
   * 
   * @param current Current coordinate
   * @param destination Coordinate of the destination
   * @return The angle facing the destination from the current point
   */
  public static double getDestinationAngle(Point current, Point destination) {
    return (toDegrees(atan2(destination.x - current.x, destination.y - current.y)) + 360) % 360;
  }
  
  /**
   * Returns the signed minimal angle from the initial angle to the destination angle.
   * 
   * @param initialAngle The current angle of robot
   * @param destAngle The angle to turn to
   * @return The minimal angle in degrees
   */
  public static double minimalAngle(double initialAngle, double destAngle) {
    var dtheta = destAngle - initialAngle;
    if (dtheta < -180) {
      dtheta += 360;
    } else if (dtheta > 180) {
      dtheta -= 360;
    }
    return dtheta;
  }
  
  /**
   * Returns the distance between the two points in tile lengths.
   * 
   * @param p1 The first point
   * @param p2 The second point
   * @return The distance between the two points in feet
   */
  public static double distanceBetween(Point p1, Point p2) {
    var dx = p2.x - p1.x;
    var dy = p2.y - p1.y;
    return sqrt(dx * dx + dy * dy);
  }
  

  /**
   * Moves the robot straight for the given distance.
   * 
   * @param distance in feet (tile sizes), may be negative
   */
  public static void moveStraightFor(double distance) {
    setSpeed(FORWARD_SPEED);
    leftMotor.rotate(convertDistance(distance * TILE_SIZE), true);
    rightMotor.rotate(convertDistance(distance * TILE_SIZE), false);
  }
  
  /**
   * Moves the robot forward for an indeterminate distance.
   */
  public static void forward() {
    setSpeed(FORWARD_SPEED);
    leftMotor.forward();
    rightMotor.forward();
  }
  
  /**
   * Moves the robot backward for an indeterminate distance.
   */
  public static void backward() {
    setSpeed(FORWARD_SPEED);
    leftMotor.backward();
    rightMotor.backward();
  }
  
  /**
   * This method turns the robot with a minimal angle towards the given input angle in degrees, no
   * matter what its current orientation is. This method is different from {@code turnBy()}.
   * 
   * @param angle Odometer angel to turn to, in degree
   */
  public static void turnTo(double angle) {
    turnBy(minimalAngle(odometer.getXyt()[2], angle));
  }
  
  /**
   * Turns the robot by a specified angle. Note that this method is different from
   * {@code turnTo()}. For example, if the robot is facing 90 degrees, calling
   * {@code turnBy(90)} will make the robot turn to 180 degrees, but calling
   * {@code turnTo(90)} should do nothing (since the robot is already at 90 degrees).
   * 
   * @param angle the angle by which to turn, in degrees
   */
  public static void turnBy(double angle) {
    setSpeed(ROTATE_SPEED);
    leftMotor.rotate(convertAngle(angle), true);
    rightMotor.rotate(-convertAngle(angle), false);
  }
  
  /**
   * Rotates motors clockwise.
   */
  public static void clockwise() {
    setSpeed(ROTATE_SPEED);
    leftMotor.forward();
    rightMotor.backward();
  }
  
  /**
   * Rotates motors counterclockwise.
   */
  public static void counterclockwise() {
    setSpeed(ROTATE_SPEED);
    leftMotor.backward();
    rightMotor.forward();
  }
  
  /**
   * Stops both motors. This also resets the motor speeds to zero.
   */
  public static void stopMotors() {
    leftMotor.stop();
    rightMotor.stop();
  }
  
  /**
   * Converts input distance to the total rotation of each wheel needed to cover that distance.
   * 
   * @param distance the input distance in meters
   * @return the wheel rotations necessary to cover the distance in degrees
   */
  public static int convertDistance(double distance) {
    return (int) toDegrees(distance / WHEEL_RAD);
  }

  /**
   * Converts input angle to total rotation of each wheel needed to rotate robot by that angle.
   * 
   * @param angle the input angle in degrees
   * @return the wheel rotations (in degrees) necessary to rotate the robot by the angle
   */
  public static int convertAngle(double angle) {
    return convertDistance(toRadians((BASE_WIDTH / 2) * angle));
  }
  
  /**
   * Sets the speed of both motors to the same values.
   * 
   * @param speed the speed in degrees per second
   */
  public static void setSpeed(int speed) {
    setSpeeds(speed, speed);
  }
  
  /**
   * Sets the speed of both motors to different values.
   * 
   * @param leftSpeed the speed of the left motor in degrees per second
   * @param rightSpeed the speed of the right motor in degrees per second
   */
  public static void setSpeeds(int leftSpeed, int rightSpeed) {
    leftMotor.setSpeed(leftSpeed);
    rightMotor.setSpeed(rightSpeed);
  }
  
  /**
   * Sets the acceleration of both motors.
   * 
   * @param acceleration the acceleration in degrees per second squared
   */
  public static void setAcceleration(int acceleration) {
    leftMotor.setAcceleration(acceleration);
    rightMotor.setAcceleration(acceleration);
  }
  
  /**
   * Beeps the robot the number of times indicated.
   * 
   * @param numberOfBeep Integer number to beep
   */
  public static void beep( int numberOfBeep ) {
	  for (int i = 0; i < numberOfBeep; i++) {
		  LocalEV3.getAudio().beep(); // beeps once
		  try {
			TimeUnit.MILLISECONDS.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	      System.out.println("BEEP!");
	  }
  }
  
  
  /**
   * Opens the robot arm to a rest position (rotates arm clockwise 180 degrees).
   */
  public static void resetArm() {
	  armMotor.setSpeed(ARM_SPEED);
	  armMotor.rotate(-180, true);
  }
  
  /**
   * Closes the robot arm to hold blocks (rotates arm counterclockwise 180 degrees).
   */
  public static void armReady() {
	  armMotor.setSpeed(ARM_SPEED);
	  armMotor.rotate(180, true);
  }
  
}
