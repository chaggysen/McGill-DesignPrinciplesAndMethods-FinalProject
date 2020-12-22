package ca.mcgill.ecse211.project;

import ca.mcgill.ecse211.playingfield.Point;
import static ca.mcgill.ecse211.project.Navigation.*;
import ca.mcgill.ecse211.playingfield.RampEdge;

/**
 * The SmartNavigation class implements navigation around the ramp.
 * 
 * @author Sen Wang
 */
public class SmartNavigation {
	
	 /** Do not instantiate this class. */
	  private SmartNavigation() {}
	  
	  /**
	   * This method returns the best navigation strategy based on the robot's current position, its destination, the ramp edges and the ramp's orientation
	   * 
	   * @param currentRobotPosition
	   * @param destinationPoint
	   * @param rampEdge
	   * @param orientation
	   * @return DEFAULT, X or Y
	   */
	  public static String getNavigationStrategy(Point currentRobotPosition, Point destinationPoint, RampEdge rampEdge, String orientation) {
		  
		  // bottom tops
		  Point bottomLeftPoint = rampEdge.left;
		  Point bottomRightPoint = rampEdge.right;
		  Point topLeftPoint = new Point(0, 0);
		  Point topRightPoint = new Point (0, 0);
		  
		  // set points coordinates based on ramp orientation 
		  if (orientation.equals("NORTH")) {
			  //top points
			  topLeftPoint.x = rampEdge.left.x;
			  topLeftPoint.y = rampEdge.left.y + 2;
			  topRightPoint.x = rampEdge.right.x;
			  topRightPoint.y = rampEdge.right.y + 2;
		  } else if (orientation.equals("SOUTH")) {
			  //top points
			  topLeftPoint.x = rampEdge.left.x;
			  topLeftPoint.y = rampEdge.left.y - 2;
			  topRightPoint.x = rampEdge.right.x;
			  topRightPoint.y = rampEdge.right.y - 2;
		  } else if (orientation.equals("WEST")) {
			  //top points
			  topLeftPoint.x = rampEdge.left.x - 2;
			  topLeftPoint.y = rampEdge.left.y;
			  topRightPoint.x = rampEdge.right.x - 2;
			  topRightPoint.y = rampEdge.right.y;
		  } else {
			  //top points
			  topLeftPoint.x = rampEdge.left.x + 2;
			  topLeftPoint.y = rampEdge.left.y;
			  topRightPoint.x = rampEdge.right.x + 2;
			  topRightPoint.y = rampEdge.right.y;
		  }
		  
		     // check for intersection with bottom ramp line, if yes check if travelByX works (meaning Point currentRobotPosition, Point intermediatePoint)
			 if (intersect(currentRobotPosition, destinationPoint, bottomLeftPoint, bottomRightPoint)) {
				  // check if travel by x works by checking with all ramp lines
				  Point intermediatePoint = new Point(destinationPoint.x, currentRobotPosition.y);
				  // bottom ramp line
				  if (intersect(currentRobotPosition, intermediatePoint, bottomLeftPoint, bottomRightPoint)) {
					  return "Y";
				  // top ramp line
				  } else if (intersect(currentRobotPosition, intermediatePoint, topLeftPoint, topRightPoint)) {
					  return "Y";
				  // left ramp line
				  } else if (intersect(currentRobotPosition, intermediatePoint, bottomLeftPoint, topLeftPoint)) {
					  return "Y";
				  // right ramp line
				  } else if (intersect(currentRobotPosition, intermediatePoint, bottomRightPoint, topRightPoint)) {
					  return "Y";
				  // no intersection 
				  } else {
					  return "X";
				  }
			  }
			  // check for intersection with top ramp line
			  else if (intersect(currentRobotPosition, destinationPoint, topLeftPoint, topRightPoint)) {
				// check if travel by x works by checking with all ramp lines
				  Point intermediatePoint = new Point(destinationPoint.x, currentRobotPosition.y);
				  // bottom ramp line
				  if (intersect(currentRobotPosition, intermediatePoint, bottomLeftPoint, bottomRightPoint)) {
					  return "Y";
				  // top ramp line
				  } else if (intersect(currentRobotPosition, intermediatePoint, topLeftPoint, topRightPoint)) {
					  return "Y";
				  // left ramp line
				  } else if (intersect(currentRobotPosition, intermediatePoint, bottomLeftPoint, topLeftPoint)) {
					  return "Y";
				  // right ramp line
				  } else if (intersect(currentRobotPosition, intermediatePoint, bottomRightPoint, topRightPoint)) {
					  return "Y";
				  // no intersection 
				  } else {
					  return "X";
				  }
			  }
			  // check for intersection with left ramp line
			  else if (intersect(currentRobotPosition, destinationPoint, bottomLeftPoint, topLeftPoint)) {
				// check if travel by x works by checking with all ramp lines
				  Point intermediatePoint = new Point(destinationPoint.x, currentRobotPosition.y);
				  // bottom ramp line
				  if (intersect(currentRobotPosition, intermediatePoint, bottomLeftPoint, bottomRightPoint)) {
					  return "Y";
				  // top ramp line
				  } else if (intersect(currentRobotPosition, intermediatePoint, topLeftPoint, topRightPoint)) {
					  return "Y";
				  // left ramp line
				  } else if (intersect(currentRobotPosition, intermediatePoint, bottomLeftPoint, topLeftPoint)) {
					  return "Y";
				  // right ramp line
				  } else if (intersect(currentRobotPosition, intermediatePoint, bottomRightPoint, topRightPoint)) {
					  return "Y";
				  // no intersection 
				  } else {
					  return "X";
				  }
			  }
			  // check for intersection with right ramp line
			  else if (intersect(currentRobotPosition, destinationPoint, bottomRightPoint, topRightPoint)) {
				// check if travel by x works by checking with all ramp lines
				  Point intermediatePoint = new Point(destinationPoint.x, currentRobotPosition.y);
				  // bottom ramp line
				  if (intersect(currentRobotPosition, intermediatePoint, bottomLeftPoint, bottomRightPoint)) {
					  return "Y";
				  // top ramp line
				  } else if (intersect(currentRobotPosition, intermediatePoint, topLeftPoint, topRightPoint)) {
					  return "Y";
				  // left ramp line
				  } else if (intersect(currentRobotPosition, intermediatePoint, bottomLeftPoint, topLeftPoint)) {
					  return "Y";
				  // right ramp line
				  } else if (intersect(currentRobotPosition, intermediatePoint, bottomRightPoint, topRightPoint)) {
					  return "Y";
				  // no intersection 
				  } else {
					  return "X";
				  }
			  } else {
				  return "DEFAULT";
			  }
	
	  }
	  
	  /**
	   * This method is a better version of the default travelTo. It uses the best navigation strategy (default, x, or y) depending on the robot's current position,
	   * its destination, the ramp edges and the orientation
	   * 
	   * @param currentRobotPosition
	   * @param destinationPoint
	   * @param rampEdge
	   * @param orientation
	   */
	  public static void smartTravelTo(Point currentRobotPosition, Point destinationPoint, RampEdge rampEdge, String orientation) {
		  
		  String method = getNavigationStrategy(currentRobotPosition, destinationPoint, rampEdge, orientation);
		  System.out.println("NAVIGATION METHOD:" + method);
		  if (method.equals("DEFAULT")) {
			  travelTo(destinationPoint);
		  } else if (method.equals("X")) {
			  travelToByXFirst(destinationPoint, 0);
		  } else {
			  travelToByYFirst(destinationPoint, 0);
		  }
	  }
	  
	  /**
	   * This method determines the orientation of three points of this order p, q, and r.
	   * 
	   * @param p First point
	   * @param q Second point
	   * @param r Third point
	   * @return Integer 0 if colinear, 1 if clockwise and 2 if counterclockwise
	   */
	  public static int getOrientation(Point p, Point q, Point r) {
		  double orientationVal = ((q.y - p.y) *(r.x - q.x) - (q.x - p.x) * (r.y - q.y));
		  if (orientationVal == 0) {
			  return 0; // colinear
		  }
		  return (orientationVal > 0) ?  1 : 2; // clock or counterclock wise
	  }
	  
	  /**
	   * This method determines if there will be an intersection between two segments.
	   * 
	   * @param p1 First point for the line between p1 and p2
	   * @param p2 Second point for the line between p1 and p2
	   * @param q1 First point for the line between q1 and q2
	   * @param q2 Second point for the line between q1 and q2
	   * @return Boolean true if the two segments intersect
	   */
	  public static boolean intersect(Point p1, Point q1, Point p2, Point q2) {
		  int o1 = getOrientation(p1, q1, p2);
		  int o2 = getOrientation(p1, q1, q2);
		  int o3 = getOrientation(p2, q2, p1);
		  int o4 = getOrientation(p2, q2, q1);

		  System.out.println("ORIENTATION VAL: " + o1 + o2 + o3 + o4);
		   if (o1 != o2 && o3 != o4) {
			   return true;
		   }
		   
		   return false;
	  }
	  
}
