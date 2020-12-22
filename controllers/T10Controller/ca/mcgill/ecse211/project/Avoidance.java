package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.*;
import static ca.mcgill.ecse211.project.Navigation.*;
import static simlejos.ExecutionController.waitUntilNextStep;
import ca.mcgill.ecse211.playingfield.Point;

/**
 * The avoidance class is a thread that actively avoids obstacles in front of the robot.
 * 
 * @author Simon Nakane Marcil
 */
public class Avoidance implements Runnable {
  
  // Minimum distance before taking the detour
  private static final int OBSTACLE = 15;
  
  /** Indicates if the thread is actively looking for obstacles to avoid. */
  private static boolean isAvoid = false;
  
  /** Indicates if an obstacle is found or not. */
  private static boolean obstacleFound = false;
  
  /** The singleton odometer instance. */
  private static Avoidance avo;
  
  // Obstacle US related variables
  /** Buffer (array) to store obstacle US samples. */
  public static float[] obstacleUSData = new float[obstacleUSSensor.sampleSize()];
  
  /** The number of invalid samples seen by filter() so far. */
  private static int invalidSampleCount;
  /** The distance remembered by the filter() method. */
  private static int prevDistance;
  
  
  /** Default constructor of this class. It cannot be accessed externally. */
  private Avoidance() {}
  
  /**
   * Returns the Avoidance Object. Use this method to obtain an instance of Avoidance.
   * 
   * @return Filtered ultrasonic sensor reading
   */
  public static synchronized Avoidance getAvoidance() {
    if (avo == null) {
      avo = new Avoidance();
    }
    return avo;
  }
  
  /** This method is where the logic for the avoider will run. */
  @Override
  public void run() {
    
    while (true) {
      if(isAvoid) {
        // check for obstacle
        if(readObstacleUsDistance() < OBSTACLE) {
          obstacleFound = true;
          stopMotors();
          detourManeuver();
          obstacleFound = false;
        }
      }
      waitUntilNextStep();
    }
  }
  
  /** This method activates the avoidance algorithm for the robot to actively look for obstacles. */
  public static void doAvoid() {
    isAvoid = true;
  }
  
  /** This method deactivates the avoidance algorithm for the robot and will not avoid obstacles. */
  public static void stopAvoid() {
    isAvoid = false;
  }
  
  /** This method is the movements the robot will perform to contour the found obstacle. */
  public static void detourManeuver() {
    turnBy(90);
    moveStraightFor(1);
    turnBy(-90);
    moveStraightFor(2);
  }
  
  /** 
   * This method will stall any other threads while the robot is avoiding an obstacle.
   * 
   * @return Boolean value indicating if robot avoided
   */
  public static boolean waitDetour() {
    if(obstacleFound) {
      while(obstacleFound) {
        waitUntilNextStep();
      }
      return true;
    }
    return false;
  }
  
  /** Returns the filtered distance between the US sensor and an obstacle in cm. */
  public static int readObstacleUsDistance() {
    obstacleUSSensor.fetchSample(obstacleUSData, 0);
    return filter((int) (obstacleUSData[0] * 100.0));
  }
  
  /**
   * Rudimentary filter - toss out invalid samples corresponding to null signal.
   * 
   * @param distance raw distance measured by the sensor in cm
   * @return the filtered distance in cm
   */
  static int filter(int distance) {
    if (distance >= MAX_SENSOR_DIST && invalidSampleCount < INVALID_SAMPLE_LIMIT) {
      // bad value, increment the filter value and return the distance remembered from before
      invalidSampleCount++;
      return prevDistance;
    } else {
      if (distance < MAX_SENSOR_DIST) {
        invalidSampleCount = 0; // reset filter and remember the input distance.
      }
      prevDistance = distance;
      return distance;
    }
  }
}

