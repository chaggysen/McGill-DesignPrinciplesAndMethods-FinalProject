package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Navigation.*;
import static ca.mcgill.ecse211.project.SmartNavigation.*;
import static ca.mcgill.ecse211.project.Resources.*;
import static ca.mcgill.ecse211.project.Localizer.*;
import static ca.mcgill.ecse211.project.Avoidance.*;

import ca.mcgill.ecse211.playingfield.Point;
import ca.mcgill.ecse211.playingfield.RampEdge;

import static java.lang.Math.*;

import java.util.ArrayList;

/**
 * The MapNav class is used to move around the defined playing field through
 * points from the WiFi class.
 * 
 * @author Sen Wang
 * @author Olivier Normandin
 * @author Simon Nakane Marcil
 */
public class MapNav {

  // Minimum distance to the target
  private static final int THRESHOLD = 20;

  /** Boolean indicating the arrival at the search zone. */
  private static Boolean gotToFirstDest = false;

  /*
   * In order to perform the Unit tests modifications are required to method. 1.
   * method must return ArrayList<Point> // Unit test array ArrayList<Point> path
   * = new ArrayList<Point>(); return path; 2. add currentRobotPoint, midPointA,
   * midPointB to Unit test array 3. comment out all methods call related to
   * Robot's hardware such as: Odometer, travelTo(), Localizer, etc.
   */
  /**
   * This method navigates the robot across the tunnel. Given the tunnel's lower
   * left and upper right points, it calculates a path across the tunnel.
   * 
   * @param tunnel_LL The lower left point of the tunnel
   * @param tunnel_UR The upper right point of the tunnel
   */
  // test: LL(4, 7) and UP(6, 8)
  public static ArrayList<Point> crossTunnel(Point tunnel_LL, Point tunnel_UR, RampEdge ramp, boolean returning) {
    // Unit test array
    ArrayList<Point> path = new ArrayList<Point>();

    // determine tunnel orientation
    Boolean isHorizontalTunnel = tunnel_UR.x - tunnel_LL.x > 1;

    // get current robot position and create a new point
    Point currentRobotPoint = new Point(odometer.getXyt()[0] / TILE_SIZE, odometer.getXyt()[1] / TILE_SIZE);
    // Point currentRobotPoint = new Point(0.5, 0.5);
    path.add(currentRobotPoint);

    // get starting point and ending point
    Point middlePointA = new Point(0, 0);
    Point middlePointB = new Point(0, 0);
    Point localizedPointA = new Point(0, 0);
    Point localizedPointB = new Point(0, 0);

    if (!returning) {
      if (isHorizontalTunnel) {
        middlePointA.x = tunnel_LL.x - 0.8;
        middlePointB.x = tunnel_UR.x + 0.8;
        middlePointA.y = middlePointB.y = (tunnel_LL.y + tunnel_UR.y) / 2.0;
      } else {
        middlePointA.x = middlePointB.x = (tunnel_LL.x + tunnel_UR.x) / 2.0;
        middlePointA.y = tunnel_LL.y - 0.8;
        middlePointB.y = tunnel_UR.y + 0.8;
      }
    } else {
      if (isHorizontalTunnel) {
        middlePointA.x = tunnel_LL.x - 0.8;
        middlePointB.x = tunnel_UR.x + 0.8;
        middlePointA.y = middlePointB.y = (tunnel_LL.y + tunnel_UR.y) / 2.0;
        localizedPointA.x = tunnel_LL.x - 1.5;
        localizedPointB.x = tunnel_UR.x + 1.5;
        localizedPointA.y = localizedPointB.y = (tunnel_LL.y + tunnel_UR.y) / 2.0;
      } else {
        middlePointA.x = middlePointB.x = (tunnel_LL.x + tunnel_UR.x) / 2.0;
        middlePointA.y = tunnel_LL.y - 0.8;
        middlePointB.y = tunnel_UR.y + 0.8;
        localizedPointA.y = tunnel_LL.y - 1.5;
        localizedPointB.y = tunnel_UR.y + 1.5;
        localizedPointA.x = localizedPointB.x = (tunnel_LL.x + tunnel_UR.x) / 2.0;
      }
    }

    // get distance to middle points
    double distanceToA = distanceBetween(currentRobotPoint, middlePointA);
    double distanceToB = distanceBetween(currentRobotPoint, middlePointB);
    // travel to closer point then cross
    if (distanceToA < distanceToB) {
      path.add(middlePointA);
      path.add(middlePointB);
      if (returning) {
        String orientation = findRampOrientation(ramp.left, ramp.right);
        smartTravelTo(currentRobotPoint, localizedPointA, ramp, orientation);
        turnTo(getDestinationAngle(localizedPointA, middlePointA));
        lightLocalize();
        travelTo(middlePointA);
      } else {
        travelTo(middlePointA);
      }
      travelTo(middlePointB);
    } else {
      path.add(middlePointB);
      path.add(middlePointA);
      if (returning) {
        String orientation = findRampOrientation(ramp.left, ramp.right);
        smartTravelTo(currentRobotPoint, localizedPointB, ramp, orientation);
        turnTo(getDestinationAngle(localizedPointB, middlePointB));
        lightLocalize();
        travelTo(middlePointB);
      } else {
        travelTo(middlePointB);
      }
      travelTo(middlePointA);
    }

    // localize at exit of tunnel
    lightLocalize();
    return path;
  }

  /**
   * This method performs a carpet search in the search zone (travels to every
   * point inside). On each point, it performs a 360 degrees (or more) US sensor
   * search and checks if a blocks is detected. It also determine the ramp
   * position to avoid it.
   * 
   * @param searchLL      lower left point of search zone
   * @param searchUR      upper right point of search zone
   * @param leftRampEdge  left ramp edge point
   * @param rightRampEdge right ramp edge point
   */
  public static void findBlocks(Point searchLL, Point searchUR, Point leftRampEdge, Point rightRampEdge) {

    // find the orientation of the ramp
    String orientation = findRampOrientation(leftRampEdge, rightRampEdge);
    ArrayList<Point> rampZone = new ArrayList<Point>();
    Point binPosition = new Point(0, 0);
    RampEdge rampEdge = new RampEdge(leftRampEdge, rightRampEdge);

    // add ramp points to the array list according to orientation
    if (orientation.equals("NORTH")) {
      rampZone.add(leftRampEdge);
      rampZone.add(rightRampEdge);
      Point otherLeftEdge1 = new Point(leftRampEdge.x, leftRampEdge.y + 1);
      Point otherRightEdge1 = new Point(rightRampEdge.x, rightRampEdge.y + 1);
      Point otherLeftEdge2 = new Point(leftRampEdge.x, leftRampEdge.y + 2);
      Point otherRightEdge2 = new Point(rightRampEdge.x, rightRampEdge.y + 2);
      rampZone.add(otherLeftEdge1);
      rampZone.add(otherRightEdge1);
      rampZone.add(otherLeftEdge2);
      rampZone.add(otherRightEdge2);
      binPosition.x = leftRampEdge.x + 0.5;
      binPosition.y = leftRampEdge.y + 1;
    } else if (orientation.equals("SOUTH")) {
      rampZone.add(leftRampEdge);
      rampZone.add(rightRampEdge);
      Point otherLeftEdge1 = new Point(leftRampEdge.x, leftRampEdge.y - 1);
      Point otherRightEdge1 = new Point(rightRampEdge.x, rightRampEdge.y - 1);
      Point otherLeftEdge2 = new Point(leftRampEdge.x, leftRampEdge.y - 2);
      Point otherRightEdge2 = new Point(rightRampEdge.x, rightRampEdge.y - 2);
      rampZone.add(otherLeftEdge1);
      rampZone.add(otherRightEdge1);
      rampZone.add(otherLeftEdge2);
      rampZone.add(otherRightEdge2);
      binPosition.x = leftRampEdge.x + 0.5;
      binPosition.y = leftRampEdge.y - 1;
    } else if (orientation.equals("EAST")) {
      rampZone.add(leftRampEdge);
      rampZone.add(rightRampEdge);
      Point otherLeftEdge1 = new Point(leftRampEdge.x + 1, leftRampEdge.y);
      Point otherRightEdge1 = new Point(rightRampEdge.x + 1, rightRampEdge.y);
      Point otherLeftEdge2 = new Point(leftRampEdge.x + 2, leftRampEdge.y);
      Point otherRightEdge2 = new Point(rightRampEdge.x + 2, rightRampEdge.y);
      rampZone.add(otherLeftEdge1);
      rampZone.add(otherRightEdge1);
      rampZone.add(otherLeftEdge2);
      rampZone.add(otherRightEdge2);
      binPosition.x = leftRampEdge.x + 1;
      binPosition.y = leftRampEdge.y - 0.5;
    } else { // WEST
      rampZone.add(leftRampEdge);
      rampZone.add(rightRampEdge);
      Point otherLeftEdge1 = new Point(leftRampEdge.x - 1, leftRampEdge.y);
      Point otherRightEdge1 = new Point(rightRampEdge.x - 1, rightRampEdge.y);
      Point otherLeftEdge2 = new Point(leftRampEdge.x - 2, leftRampEdge.y);
      Point otherRightEdge2 = new Point(rightRampEdge.x - 2, rightRampEdge.y);
      rampZone.add(otherLeftEdge1);
      rampZone.add(otherRightEdge1);
      rampZone.add(otherLeftEdge2);
      rampZone.add(otherRightEdge2);
      binPosition.x = leftRampEdge.x - 1;
      binPosition.y = leftRampEdge.y + 0.5;
    }

    // iterate around all the inner points inside search zone
    getAllPoints(searchLL, searchUR, rampZone).forEach(point -> {
      Point currentRobotPoint = new Point(odometer.getXyt()[0] / TILE_SIZE, odometer.getXyt()[1] / TILE_SIZE);
      System.out.println("CURRENT POINT X: " + currentRobotPoint.x);
      System.out.println("CURRENT POINT Y: " + currentRobotPoint.y);
      Point roundedPoint = new Point(round(point.x, 1), round(point.y, 1));
      System.out.println("Traveling TO: " + round(point.x, 1) + round(point.y, 1));
      // travel to search points
      smartTravelTo(currentRobotPoint, roundedPoint, rampEdge, orientation);
      if (!gotToFirstDest) {
        Navigation.beep(3);
        gotToFirstDest = true;
      }
      // perform 360 degrees search on every point
      performSearch(binPosition, orientation, point, leftRampEdge, rightRampEdge, 0, false);
    });
  }

  /**
   * This method finds the ramp orientation based on the left ramp edge and right
   * ramp edge point. It returns the result in String
   * 
   * @param leftRampEdge  left ramp edge point
   * @param rightRampEdge right ramp edge point
   * @return ramp orientation in String (NORTH, EAST, SOUTH, WEST)
   */
  public static String findRampOrientation(Point leftRampEdge, Point rightRampEdge) {
    if (leftRampEdge.y == rightRampEdge.y && rightRampEdge.x - leftRampEdge.x > 0) {
      return "NORTH";
    } else if (leftRampEdge.x == rightRampEdge.x && leftRampEdge.y - rightRampEdge.y > 0) {
      return "EAST";
    } else if (leftRampEdge.y == rightRampEdge.y && leftRampEdge.x - rightRampEdge.x > 0) {
      return "SOUTH";
    } else {
      return "WEST";
    }
  }

  /**
   * This method finds all the inner point inside a search zone based on the lower
   * left point and upper right point of search zone. The method automatically
   * filters out all the points covered by the ramp.
   * 
   * @param searchLL The lower left point of search zone
   * @param searchUR The upper right point of search zone
   * @param rampZone The ramp points (points inside the search zone covered by the
   *                 ramp)
   * @return Array list of points used for the carpet search path
   */
  public static ArrayList<Point> getAllPoints(Point searchLL, Point searchUR, ArrayList<Point> rampZone) {
    ArrayList<Point> path = new ArrayList<Point>();
    double minX = searchLL.x;
    double maxX = searchUR.x;
    double minY = searchLL.y;
    double maxY = searchUR.y;

    double innerMinX = minX + 1;
    double innerMinY = minY + 1;

    for (double x = innerMinX; x < maxX; x++) {
      for (double y = innerMinY; y < maxY; y++) {
        Point pathPoint = new Point(x, y);
        Boolean isRampPoint = false;
        for (int i = 0; i < rampZone.size(); i++) {
          if (round(pathPoint.x, 1) == round(rampZone.get(i).x, 1)
              && round(pathPoint.y, 1) == round(rampZone.get(i).y, 1)) {
            isRampPoint = true;
          }
        }
        if (!isRampPoint) {
          path.add(pathPoint);
        }
      }
    }
    return path;
  }

  /**
   * This method performs a US sensor search on the point it is currently at. It
   * turns by approximatively 360 degrees. If the method detects a block, it calls
   * the grabBlock() and pushTargetToBin() methods. This method automatically
   * filters out the ramp.
   * 
   * @param binPosition       Absolute center point of the bin and ramp
   * @param orientation       orientation of the ramp
   * @param currentRobotPoint the point where the robot is currently located
   * @param leftRampEdge      left ramp edge point
   */
  public static void performSearch(Point binPosition, String orientation, Point currentRobotPoint, Point leftRampEdge,
      Point rightRampEdge, int counter, boolean restart) {
    if (!restart) {
      counter = 0;
    }
    do {
      clockwise();
      counter++;
    } while (((readUsDistance() > THRESHOLD && readObstacleUsDistance() > THRESHOLD)
        | (Math.abs(readUsDistance() - readObstacleUsDistance()) <= 20)) && (counter < 12999));
    stopMotors();
    if (Math.abs(readUsDistance() - readObstacleUsDistance()) <= 2) {
      System.out.println("FOUND OBSTACLE");
    } else if (readUsDistance() <= THRESHOLD) {
      System.out.println("FOUND BLOCK");
      Point currentRobotPosition = new Point(odometer.getXyt()[0] / TILE_SIZE, odometer.getXyt()[1] / TILE_SIZE);
      Point targetPosition = getBlockPoint(currentRobotPosition, odometer.getXyt()[2], readUsDistance());
      // Check if the detected object is within the bin boundaries -> if so : not a
      // target!
      if (orientation.equals("NORTH") && targetPosition.x >= leftRampEdge.x - 0.20
          && targetPosition.x <= leftRampEdge.x + 1.20 && targetPosition.y >= leftRampEdge.y - 0.20
          && targetPosition.y <= leftRampEdge.y + 2.20) {
        System.out.println("Orientation is north and probably detected the bin: DO NOTHING");
        performSearch(binPosition, orientation, currentRobotPoint, leftRampEdge, rightRampEdge, counter, true);
      } else if (orientation.equals("SOUTH") && targetPosition.x <= leftRampEdge.x + 0.20
          && targetPosition.x >= leftRampEdge.x - 1.20 && targetPosition.y <= leftRampEdge.y + 0.20
          && targetPosition.y >= leftRampEdge.y - 2.20) {
        System.out.println("Orientation is south and probably detected the bin: DO NOTHING");
        performSearch(binPosition, orientation, currentRobotPoint, leftRampEdge, rightRampEdge, counter, true);
      } else if (orientation.equals("EAST") && targetPosition.x >= leftRampEdge.x - 0.20
          && targetPosition.x <= leftRampEdge.x + 2.20 && targetPosition.y <= leftRampEdge.y + 0.20
          && targetPosition.y >= leftRampEdge.y - 1.20) {
        System.out.println("Orientation is north and probably detected the bin: DO NOTHING");
        performSearch(binPosition, orientation, currentRobotPoint, leftRampEdge, rightRampEdge, counter, true);
      } else if (orientation.equals("WEST") && targetPosition.x <= leftRampEdge.x + 0.20
          && targetPosition.x >= leftRampEdge.x - 2.20 && targetPosition.y >= leftRampEdge.y - 0.20
          && targetPosition.y <= leftRampEdge.y + 1.20) {
        System.out.println("Orientation is north and probably detected the bin: DO NOTHING");
        performSearch(binPosition, orientation, currentRobotPoint, leftRampEdge, rightRampEdge, counter, true);
      } else {
        Navigation.beep(3);
        grabBlock(targetPosition);
        pushTargetToBin(targetPosition, binPosition, orientation, currentRobotPoint, leftRampEdge, rightRampEdge);
      }
    } else {
      System.out.println("360 complete");
    }
    return;
  }

  /**
   * This method calculates the approximate block's point based on the robot's
   * current position, its orientation and the distance to the block.
   * 
   * @param currentRobotPosition The point where the robot is at currently
   * @param currentRobotTheta    The current orientation of the robot
   * @param distanceToRobot      Distance between the target and the robot.
   * @return The target's point
   */
  public static Point getBlockPoint(Point currentRobotPosition, double currentRobotTheta, int distanceToRobot) {
    // To account for detection of first side of block and for middle of block
    currentRobotTheta = currentRobotTheta + 5;
    distanceToRobot = distanceToRobot + 10;

    double x;
    double y;
    Point blockPoint;
    System.out.println("Robot X: " + currentRobotPosition.x);
    System.out.println("Robot Y: " + currentRobotPosition.y);
    System.out.println("Theta: " + currentRobotTheta);
    if (currentRobotTheta > 90 && currentRobotTheta < 270) {
      x = (Math.sin(Math.toRadians(currentRobotTheta)) * distanceToRobot) / 100.0;
      y = (Math.cos(Math.toRadians(currentRobotTheta + 180)) * distanceToRobot) / 100.0;
      System.out.println("distance: " + distanceToRobot);
      System.out.println("x: " + x);
      System.out.println("y: " + y);
      blockPoint = new Point(currentRobotPosition.x + x / TILE_SIZE, currentRobotPosition.y - y / TILE_SIZE);
    } else {
      x = (Math.sin(Math.toRadians(currentRobotTheta)) * distanceToRobot) / 100.0;
      y = (Math.cos(Math.toRadians(currentRobotTheta)) * distanceToRobot) / 100.0;
      blockPoint = new Point(currentRobotPosition.x + x / TILE_SIZE, currentRobotPosition.y + y / TILE_SIZE);
    }
    System.out.println("Getting block position: " + blockPoint.x + " " + blockPoint.y);
    return blockPoint;
  }

  /**
   * This method grabs the block at target position
   * 
   * @param targetPosition The target position
   */
  public static void grabBlock(Point targetPosition) {
    travelTo(targetPosition, 0.20);
    armReady();
    System.out.println("Grabbed block");
  }

  /**
   * This method pushes the grabbed target to the bin. It first calculates the
   * pushReadyPoint (point in front of the ramp) based on binPosition and ramp
   * orientation. It then navigates the robot to pushReadyPoint and pushes the
   * target to the bin. The method does a lightLocalization at the end.
   * 
   * @param targetPosition    the block's point
   * @param binPosition       the absolute center point of the bin and ramp
   * @param orientation       orientation of the ramp
   * @param currentRobotPoint the current robot's position
   */
  public static void pushTargetToBin(Point targetPosition, Point binPosition, String orientation,
      Point currentRobotPoint, Point leftRampEdge, Point rightRampEdge) {
    // check bin orientation to find pushReady point
    // we assume the binPosition Point is at the absolute center of ramp + bin
    Point pushReadyPoint = new Point(0, 0);
    if (orientation.equals("NORTH")) {
      System.out.println("ORIENTATION IS NORTH");
      pushReadyPoint.x = binPosition.x;
      pushReadyPoint.y = binPosition.y - 1.5;
      System.out.println("Push ready point: " + pushReadyPoint.x + " " + pushReadyPoint.y);
    } else if (orientation.equals("SOUTH")) {
      System.out.println("ORIENTATION IS SOUTH");
      pushReadyPoint.x = binPosition.x;
      pushReadyPoint.y = binPosition.y + 1.5;
    } else if (orientation.equals("EAST")) {
      System.out.println("ORIENTATION IS EAST");
      pushReadyPoint.x = binPosition.x - 1.5;
      pushReadyPoint.y = binPosition.y;
    } else {
      System.out.println("ORIENTATION IS WEST");
      pushReadyPoint.x = binPosition.x + 1.5;
      pushReadyPoint.y = binPosition.y;
    }

    RampEdge rampEdge = new RampEdge(leftRampEdge, rightRampEdge);
    smartTravelTo(currentRobotPoint, pushReadyPoint, rampEdge, orientation);
    travelTo(binPosition);
    resetArm();
    moveStraightFor(-1.5);
    Navigation.turnBy(90);
    Localizer.lightLocalize();

  }


  /**
   * This method pushes a target from its initial location to a destination on the
   * field.
   * 
   * @param head Starting target position
   * @param tail Ending target position
   */
  public static void pushTarget(Point head, Point tail) {

    // First travel position for the robot to push the block in the right direction
    Point startPoint = getStartPushPosition(head, tail);
    // move to the start position
    travelTo(startPoint);

    // move to tail with bumper off-set
    travelTo(tail, BUMPER_OFFSET);

    // move back to avoid bumping into the block when turning for next way-point
    moveStraightFor(-BUMPER_OFFSET);
  }

  /**
   * This method calculates the point where the robot should be at in order to
   * push a target in the right direction.
   * 
   * @param head The starting position of the target
   * @param tail The ending position of the target
   * @return startPoint to push a target
   */
  public static Point getStartPushPosition(Point head, Point tail) {
    // get the direction the target will be pushed
    double travelAngle = getDestinationAngle(head, tail);
    // calculate opposite direction to position start point to
    double startPointAngle = (travelAngle + 180.0) % 360.0;
    double startPointRadian = toRadians(startPointAngle);

    // x coordinate for the start point
    double xStart = head.x + sin(startPointRadian);
    // y coordinate for the start point
    double yStart = head.y + cos(startPointRadian);

    // new Point for the robot to start from
    return new Point(xStart, yStart);
  }

  /**
   * Rounds value at a certain decimal places
   * 
   * @param value  to be rounded
   * @param places after decimals
   * @return rounded value
   */
  public static double round(double value, int places) {
    if (places < 0)
      throw new IllegalArgumentException();

    long factor = (long) Math.pow(10, places);
    value = value * factor;
    long tmp = Math.round(value);
    return (double) tmp / factor;
  }

}
