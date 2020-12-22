package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Navigation.*;
import static ca.mcgill.ecse211.project.Resources.*;
import static simlejos.ExecutionController.waitUntilNextStep;

/**
 * The Localizer class localizes the robot on the playing field. 
 * It can also calibrate the Odometer.
 * 
 * @author Simon Nakane Marcil
 * @author Olivier Normandin
 */
public class Localizer {
  
  /** Buffer (array) to store lower US samples. */
  private static float[] usData = new float[LowUsSensor.sampleSize()];
  /** Buffer (array) to store color sensor samples. */
  private static float[] leftColorSensorData = new float[leftColorSensor.sampleSize()];
  /** Buffer (array) to store color sensor samples. */
  private static float[] rightColorSensorData = new float[rightColorSensor.sampleSize()];
    
  /** The maximum wall distance detected by the ultrasonic sensor, in cm. */
  private static final int EDGE_DIST = 50;
  /** Error margin when looking for a  wall edge. */
  private static final int NOISE_MARGIN = 3;
  /** Distance that guarantees to be facing a wall. */
  private static final int FACE_WALL = 30;  //13
  /** Distance that guarantees to be facing a wall. */
  private static final int AWAY_WALL = 80;
  /** Edge angles measured on the wall. */
  private static double[] edges = new double[2];
  
  /**
   * color sensor current and previous value reading for comparison.
   * [leftCur, rightCur, leftPrev, rightPrev]
   */
  private static int[] colorSensorData = new int[4];
  /** Class variable to determine whether one wheel or both wheels are stopped. */
  private static boolean isLeftMotorMoving;
  /** Class variable to determine whether one wheel or both wheels are stopped. */
  private static boolean isRightMotorMoving;
  /** Minimum difference in color sensor lighting to detect change in color such as a black line. */
  private static final int LIGHT_CHANGE = 10;
  
  /**
   * This method localizes the robot orientation to 0 degree from a 360 degree ultrasonic sensor
   * reading at a wall corner. 
   */
  public static void usLocalize() {
    odometer.setXyt(0, 0, 0); // initialize odometer
    // face sensor to wall
    clockwise();
    while (readUsDistance() > FACE_WALL) {
      waitUntilNextStep();
    }
    stopMotors();
    
    // measure left wall edge
    clockwise();
    edges[0] = measureEdgeAngle();
    stopMotors();
    
    // face sensor to wall
    counterclockwise();
    while (readUsDistance() > FACE_WALL) {
      waitUntilNextStep();
    }
    stopMotors();
    
    // measure back wall edge
    counterclockwise();
    edges[1] = measureEdgeAngle();
    stopMotors();
    
    turnTo(calFrontAngle());
    
    // print correction values and set odometer to zero
    System.out.println("Ultrasomic Localizer Correction:");
    odometer.printPosition();
    odometer.setTheta(0);
  }
  
  /**
   * This method reads and filters distance between the US sensor and an obstacle in cm.
   * 
   * @return Filtered ultrasonic sensor reading
   */
  public static int readUsDistance() {
    LowUsSensor.fetchSample(usData, 0);
    // extract from buffer, cast to int
    return (int) (usData[0] * 100.0);
  }
  
  /**
   * This method measures the average angle of robot when sensor is facing an edge.
   * 
   * @return Edge angle in degrees
   */
  public static double measureEdgeAngle() {
    double initAngle = odometer.getXyt()[2];
    double curAngle = initAngle;
    int sensorDist = readUsDistance();
    double angleSum = 0.0;
    int angleCount = 0;
    
    while (sensorDist < AWAY_WALL) {
      if (sensorDist >= (EDGE_DIST - NOISE_MARGIN) && sensorDist <= (EDGE_DIST + NOISE_MARGIN)) {
        // robot is facing an edge
        curAngle = odometer.getXyt()[2];
        if (curAngle < initAngle) {
          curAngle += 360;
        }
        angleSum += curAngle;
        angleCount++;
      }
      waitUntilNextStep();
      sensorDist = readUsDistance();
    }
    // average edge angle
    return (angleSum / angleCount) % 360;
  }
  
  /**
   * This method calculates the angle to face the robot forward.
   * 
   * @return angle to face in degree
   */
  public static double calFrontAngle() {
    double angleDif = edges[1] - edges[0];
    if (angleDif < 0) {
      return angleDif / 2.0 + edges[0] - 225.0;
    } else {
      return angleDif / 2.0 + edges[0] - 45.0;
    }
  }

  /**
   * This method localizes the robot position to the floor grid at 1,1 from color sensor readings.
   */
  public static void lightLocalize() {
    forward();
    stopAtLine();
    moveStraightFor(COLOR_SENSOR_TO_WHEEL_DIST);
    
    turnBy(90);
    forward();
    stopAtLine();
    moveStraightFor(COLOR_SENSOR_TO_WHEEL_DIST);
    
    double[] pos = odometer.getXyt();
    pos[0] = Math.round(pos[0] / TILE_SIZE) * TILE_SIZE;
    pos[1] = Math.round(pos[1] / TILE_SIZE) * TILE_SIZE;
    pos[2] = Math.round(pos[2] / 90.0) * 90.0 % 360;
    odometer.setXyt(pos[0], pos[1], pos[2]);
    
    System.out.println("Light Localized to:");
    odometer.printPosition();
  }
  
  /**
   * This method sets the filtered color reading in respective array.
   */
  public static void readColorSensor() {
    leftColorSensor.fetchSample(leftColorSensorData, 0);
    rightColorSensor.fetchSample(rightColorSensorData, 0);
    
    // set previous values
    colorSensorData[2] = colorSensorData[0];
    colorSensorData[3] = colorSensorData[1];
    // update current values
    colorSensorData[0] = (int) leftColorSensorData[0];
    colorSensorData[1] = (int) rightColorSensorData[0];
  }
  
  /**
   * This method allows the robot to move forward until the light sensors detect a line.
   */
  public static void stopAtLine() {
    isLeftMotorMoving = isRightMotorMoving = true;
    // initialize sensor data for new line readings
    leftColorSensor.fetchSample(leftColorSensorData, 0);
    rightColorSensor.fetchSample(rightColorSensorData, 0);
    colorSensorData[0] = colorSensorData[2] = (int) leftColorSensorData[0];
    colorSensorData[1] = colorSensorData[3] = (int) rightColorSensorData[0];
    
    while (isLeftMotorMoving || isRightMotorMoving) {
      readColorSensor();
      //Check for left color sensor
      //Detect lighting change
      if (Math.abs(colorSensorData[2] - colorSensorData[0]) > LIGHT_CHANGE) {
        leftMotor.stop();               //Stop left motor
        isLeftMotorMoving = false;
      }
      //Check for right color sensor
      //Detect lighting change
      if (Math.abs(colorSensorData[3] - colorSensorData[1]) > LIGHT_CHANGE) {
        rightMotor.stop();               //Stop right motor
        isRightMotorMoving = false;
      }
      waitUntilNextStep();
    }
  }
}