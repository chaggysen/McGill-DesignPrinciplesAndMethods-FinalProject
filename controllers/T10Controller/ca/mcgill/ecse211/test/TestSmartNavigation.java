package ca.mcgill.ecse211.test;
import static ca.mcgill.ecse211.project.SmartNavigation.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.mcgill.ecse211.playingfield.Point;
import ca.mcgill.ecse211.playingfield.RampEdge;

/**
 * Test suite for smart navigation
 * @author Sen Wang
 *
 */
public class TestSmartNavigation {
	
	//================================================================================
    // getOrientation unit tests
    //================================================================================

	// 3 colinear tests
	// vertical colinear
	@Test void testGetOrientation1() {
		Point p = new Point (0, 0);
		Point q = new Point (0, 3);
		Point r = new Point (0, 5);
		int result = getOrientation(p, q, r);
		assertEquals(0, result);
	}
	
	// horizontal colinear
	@Test void testGetOrientation2() {
		Point p = new Point (1, 5);
		Point q = new Point (3, 5);
		Point r = new Point (6, 5);
		int result = getOrientation(p, q, r);
		assertEquals(0, result);
	}
	
	// 45 degrees colinear
	@Test void testGetOrientation3() {
		Point p = new Point (0, 0);
		Point q = new Point (1, 1);
		Point r = new Point (2, 2);
		int result = getOrientation(p, q, r);
		assertEquals(0, result);
	}
	
	// 3 counter-clockwise tests
	@Test void testGetOrientation4() {
		Point p = new Point (0, 0);
		Point q = new Point (1, 0);
		Point r = new Point (1, 1);
		int result = getOrientation(p, q, r);
		assertEquals(2, result);
	}
	
	@Test void testGetOrientation5() {
		Point p = new Point (0, 0);
		Point q = new Point (1, 1);
		Point r = new Point (0, 1);
		int result = getOrientation(p, q, r);
		assertEquals(2, result);
	}
	
	@Test void testGetOrientation6() {
		Point p = new Point (0, 0);
		Point q = new Point (2, 1);
		Point r = new Point (1, 2);
		int result = getOrientation(p, q, r);
		assertEquals(2, result);
	}
	
	// 4 clockwise tests
	@Test void testGetOrientation7() {
		Point p = new Point (1, 1);
		Point q = new Point (2, 1);
		Point r = new Point (2, 0);
		int result = getOrientation(p, q, r);
		assertEquals(1, result);
	}
	
	@Test void testGetOrientation8() {
		Point p = new Point (2, 2);
		Point q = new Point (4, 0);
		Point r = new Point (2, 0);
		int result = getOrientation(p, q, r);
		assertEquals(1, result);
	}
	
	@Test void testGetOrientation9() {
		Point p = new Point (1, 1);
		Point q = new Point (0, 0);
		Point r = new Point (0, 1);
		int result = getOrientation(p, q, r);
		assertEquals(1, result);
	}
	
	@Test void testGetOrientation10() {
		Point p = new Point (2, 1);
		Point q = new Point (0, 0);
		Point r = new Point (0, 1);
		int result = getOrientation(p, q, r);
		assertEquals(1, result);
	}
	
	
	//================================================================================
    // intersect unit tests
    //================================================================================
	// 3 non-intersecting tests
	@Test void testIntersect1() {
		Point p1 = new Point (0, 0);
		Point q1 = new Point (0, 5);
		Point p2 = new Point (1, 0);
		Point q2 = new Point (1, 5);
		Boolean result = intersect(p1, q1, p2, q2);
		assertEquals(false, result);
	}
	
	@Test void testIntersect2() {
		Point p1 = new Point (0, 0);
		Point q1 = new Point (5, 0);
		Point p2 = new Point (0, 1);
		Point q2 = new Point (5, 1);
		Boolean result = intersect(p1, q1, p2, q2);
		assertEquals(false, result);
	}
	
	@Test void testIntersect3() {
		Point p1 = new Point (0, 0);
		Point q1 = new Point (5, 5);
		Point p2 = new Point (3, 0);
		Point q2 = new Point (4, 1);
		Boolean result = intersect(p1, q1, p2, q2);
		assertEquals(false, result);
	}
	
	// 7 intersecting tests
	
	// same 45 degrees line
	@Test void testIntersect4() {
		Point p1 = new Point (0, 0);
		Point q1 = new Point (5, 5);
		Point p2 = new Point (0, 0);
		Point q2 = new Point (5, 6);
		Boolean result = intersect(p1, q1, p2, q2);
		assertEquals(true, result);
	}
	
	// same horizontal line
	@Test void testIntersect5() {
		Point p1 = new Point (0, 0);
		Point q1 = new Point (5, 0);
		Point p2 = new Point (0, 0);
		Point q2 = new Point (5, 1);
		Boolean result = intersect(p1, q1, p2, q2);
		assertEquals(true, result);
	}
	
	// same vertical line
	@Test void testIntersect6() {
		Point p1 = new Point (0, 0);
		Point q1 = new Point (0, 5);
		Point p2 = new Point (0, 0);
		Point q2 = new Point (1, 6);
		Boolean result = intersect(p1, q1, p2, q2);
		assertEquals(true, result);
	}
	
	// random intersecting lines
	@Test void testIntersect7() {
		Point p1 = new Point (1, 6);
		Point q1 = new Point (7, 0);
		Point p2 = new Point (3, 0);
		Point q2 = new Point (3, 7);
		Boolean result = intersect(p1, q1, p2, q2);
		assertEquals(true, result);
	}
	
	@Test void testIntersect8() {
		Point p1 = new Point (-2, 0);
		Point q1 = new Point (0, 6);
		Point p2 = new Point (2, 0);
		Point q2 = new Point (-2, 4);
		Boolean result = intersect(p1, q1, p2, q2);
		assertEquals(true, result);
	}
	
	@Test void testIntersect9() {
		Point p1 = new Point (1, 6);
		Point q1 = new Point (5, 6);
		Point p2 = new Point (4, 0);
		Point q2 = new Point (1, 8);
		Boolean result = intersect(p1, q1, p2, q2);
		assertEquals(true, result);
	}
	
	@Test void testIntersect10() {
		Point p1 = new Point (-2, 0);
		Point q1 = new Point (6, -2);
		Point p2 = new Point (1, -2);
		Point q2 = new Point (7, -1);
		Boolean result = intersect(p1, q1, p2, q2);
		assertEquals(true, result);
	}
	
	//================================================================================
    // getNavigationStrategy unit tests
    //================================================================================
	
	// DEFAULT strategy for ramp with orientation North
	@Test void testGetNavigationStrategy1() {
		Point currentRobotPosition = new Point(0, 0);
		Point destinationPoint = new Point (1, 1);
		Point leftRampEdge = new Point(3, 3);
		Point rightRampEdge = new Point(4, 3);
		RampEdge rampEdge = new RampEdge(leftRampEdge, rightRampEdge);
		String orientation = "NORTH";
		String strategy = getNavigationStrategy(currentRobotPosition, destinationPoint, rampEdge, orientation);
		assertEquals("DEFAULT", strategy);
	}
	
	// X strategy for ramp with orientation North
	@Test void testGetNavigationStrategy2() {
		Point currentRobotPosition = new Point(3, 2);
		Point destinationPoint = new Point (1, 6);
		Point leftRampEdge = new Point(2, 4);
		Point rightRampEdge = new Point(4, 4);
		RampEdge rampEdge = new RampEdge(leftRampEdge, rightRampEdge);
		String orientation = "NORTH";
		String strategy = getNavigationStrategy(currentRobotPosition, destinationPoint, rampEdge, orientation);
		assertEquals("X", strategy);
	}
	// Y strategy for ramp with orientation North
	@Test void testGetNavigationStrategy3() {
		Point currentRobotPosition = new Point(6, 5);
		Point destinationPoint = new Point (3.5, 3);
		Point leftRampEdge = new Point(3, 4);
		Point rightRampEdge = new Point(5, 4);
		RampEdge rampEdge = new RampEdge(leftRampEdge, rightRampEdge);
		String orientation = "NORTH";
		String strategy = getNavigationStrategy(currentRobotPosition, destinationPoint, rampEdge, orientation);
		assertEquals("Y", strategy);
	}
	
	// DEFAULT strategy for ramp with orientation South
	@Test void testGetNavigationStrategy4() {
		Point currentRobotPosition = new Point(0, 0);
		Point destinationPoint = new Point (1, 1);
		Point leftRampEdge = new Point(4, 3);
		Point rightRampEdge = new Point(3, 3);
		RampEdge rampEdge = new RampEdge(leftRampEdge, rightRampEdge);
		String orientation = "SOUTH";
		String strategy = getNavigationStrategy(currentRobotPosition, destinationPoint, rampEdge, orientation);
		assertEquals("DEFAULT", strategy);
	}
	
	// X strategy for ramp with orientation South
	@Test void testGetNavigationStrategy5() {
		Point currentRobotPosition = new Point(3, 6);
		Point destinationPoint = new Point (5, 1);
		Point leftRampEdge = new Point(4, 4);
		Point rightRampEdge = new Point(2, 4);
		RampEdge rampEdge = new RampEdge(leftRampEdge, rightRampEdge);
		String orientation = "SOUTH";
		String strategy = getNavigationStrategy(currentRobotPosition, destinationPoint, rampEdge, orientation);
		assertEquals("X", strategy);
	}
	
	// Y strategy for ramp with orientation South
	@Test void testGetNavigationStrategy6() {
		Point currentRobotPosition = new Point(5, 1);
		Point destinationPoint = new Point (3, 6);
		Point leftRampEdge = new Point(4, 4);
		Point rightRampEdge = new Point(2, 4);
		RampEdge rampEdge = new RampEdge(leftRampEdge, rightRampEdge);
		String orientation = "SOUTH";
		String strategy = getNavigationStrategy(currentRobotPosition, destinationPoint, rampEdge, orientation);
		assertEquals("X", strategy);
	}
	
	// DEFAULT strategy for ramp with orientation West
	@Test void testGetNavigationStrategy7() {
		Point currentRobotPosition = new Point(0, 0);
		Point destinationPoint = new Point (1, 1);
		Point leftRampEdge = new Point(4, 2);
		Point rightRampEdge = new Point(4, 4);
		RampEdge rampEdge = new RampEdge(leftRampEdge, rightRampEdge);
		String orientation = "WEST";
		String strategy = getNavigationStrategy(currentRobotPosition, destinationPoint, rampEdge, orientation);
		assertEquals("DEFAULT", strategy);
	}
	
	// Y strategy for ramp with orientation West
	@Test void testGetNavigationStrategy8() {
		Point currentRobotPosition = new Point(5, 3);
		Point destinationPoint = new Point (3, 5);
		Point leftRampEdge = new Point(4, 2);
		Point rightRampEdge = new Point(4, 4);
		RampEdge rampEdge = new RampEdge(leftRampEdge, rightRampEdge);
		String orientation = "WEST";
		String strategy = getNavigationStrategy(currentRobotPosition, destinationPoint, rampEdge, orientation);
		assertEquals("Y", strategy);
	}
	
	// X strategy for ramp with orientation West
	@Test void testGetNavigationStrategy9() {
		Point currentRobotPosition = new Point(3, 6);
		Point destinationPoint = new Point (5, 1);
		Point leftRampEdge = new Point(4, 2);
		Point rightRampEdge = new Point(4, 4);
		RampEdge rampEdge = new RampEdge(leftRampEdge, rightRampEdge);
		String orientation = "WEST";
		String strategy = getNavigationStrategy(currentRobotPosition, destinationPoint, rampEdge, orientation);
		assertEquals("X", strategy);
	}
	
	// DEFAULT strategy for ramp with orientation East
	@Test void testGetNavigationStrategy10() {
		Point currentRobotPosition = new Point(0, 0);
		Point destinationPoint = new Point (1, 1);
		Point leftRampEdge = new Point(4, 4);
		Point rightRampEdge = new Point(4, 2);
		RampEdge rampEdge = new RampEdge(leftRampEdge, rightRampEdge);
		String orientation = "EAST";
		String strategy = getNavigationStrategy(currentRobotPosition, destinationPoint, rampEdge, orientation);
		assertEquals("DEFAULT", strategy);
	}
	
	// X strategy for ramp with orientation East
	@Test void testGetNavigationStrategy11() {
		Point currentRobotPosition = new Point(5, 1);
		Point destinationPoint = new Point (7, 6);
		Point leftRampEdge = new Point(4, 4);
		Point rightRampEdge = new Point(4, 2);
		RampEdge rampEdge = new RampEdge(leftRampEdge, rightRampEdge);
		String orientation = "EAST";
		String strategy = getNavigationStrategy(currentRobotPosition, destinationPoint, rampEdge, orientation);
		assertEquals("X", strategy);
	}
	
	@Test void testGetNavigationStrategy12() {
		Point currentRobotPosition = new Point(3, 3);
		Point destinationPoint = new Point (6, 1);
		Point leftRampEdge = new Point(4, 4);
		Point rightRampEdge = new Point(4, 2);
		RampEdge rampEdge = new RampEdge(leftRampEdge, rightRampEdge);
		String orientation = "EAST";
		String strategy = getNavigationStrategy(currentRobotPosition, destinationPoint, rampEdge, orientation);
		assertEquals("Y", strategy);
	}
}