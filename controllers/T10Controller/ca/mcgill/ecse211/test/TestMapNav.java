package ca.mcgill.ecse211.test;

import static ca.mcgill.ecse211.project.MapNav.crossTunnel;
import static ca.mcgill.ecse211.project.MapNav.getStartPushPosition;
import static ca.mcgill.ecse211.project.MapNav.findRampOrientation;
import static ca.mcgill.ecse211.project.MapNav.getAllPoints;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import ca.mcgill.ecse211.playingfield.Point;
import ca.mcgill.ecse211.playingfield.RampEdge;

/**
 * Test suite for MapNav methods.
 * 
 * @author Sen Wang
 *
 */
public class TestMapNav {
  
	// Test for new getStartPushPosition
	@Test void testGetStartPushPosition1() {
		Point startingTargetPosition = new Point(1, 3);
		Point endingTargetPosition = new Point(1, 5);
		Point startingPushPosition = getStartPushPosition(startingTargetPosition, endingTargetPosition);
		assertEquals(1, Math.round(startingPushPosition.x));
		assertEquals(2, Math.round(startingPushPosition.y));
	}
	
	@Test void testGetStartPushPosition2() {
		Point startingTargetPosition = new Point(6, 6);
		Point endingTargetPosition = new Point(6, 3);
		Point startingPushPosition = getStartPushPosition(startingTargetPosition, endingTargetPosition);
		assertEquals(6, Math.round(startingPushPosition.x));
		assertEquals(7, Math.round(startingPushPosition.y));
	}
	
	@Test void testGetStartPushPosition3() {
		Point startingTargetPosition = new Point(4, 2);
		Point endingTargetPosition = new Point(1, 2);
		Point startingPushPosition = getStartPushPosition(startingTargetPosition, endingTargetPosition);
		assertEquals(5, Math.round(startingPushPosition.x));
		assertEquals(2, Math.round(startingPushPosition.y));
	}
	
	@Test void testGetStartPushPosition4() {
		Point startingTargetPosition = new Point(5, 4);
		Point endingTargetPosition = new Point(5, 1);
		Point startingPushPosition = getStartPushPosition(startingTargetPosition, endingTargetPosition);
		assertEquals(5, Math.round(startingPushPosition.x));
		assertEquals(5, Math.round(startingPushPosition.y));
	}
	
	@Test void testGetStartPushPosition5() {
		Point startingTargetPosition = new Point(3, 3);
		Point endingTargetPosition = new Point(3, 6);
		Point startingPushPosition = getStartPushPosition(startingTargetPosition, endingTargetPosition);
		assertEquals(3, Math.round(startingPushPosition.x));
		assertEquals(2, Math.round(startingPushPosition.y));
	}
	
	@Test void testGetStartPushPosition6() {
		Point startingTargetPosition = new Point(6, 6);
		Point endingTargetPosition = new Point(6, 1);
		Point startingPushPosition = getStartPushPosition(startingTargetPosition, endingTargetPosition);
		assertEquals(6, Math.round(startingPushPosition.x));
		assertEquals(7, Math.round(startingPushPosition.y));
	}
	
	@Test void testGetStartPushPosition7() {
		Point startingTargetPosition = new Point(4, 3);
		Point endingTargetPosition = new Point(6, 5);
		Point startingPushPosition = getStartPushPosition(startingTargetPosition, endingTargetPosition);
		assertEquals(3, Math.round(startingPushPosition.x));
		assertEquals(2, Math.round(startingPushPosition.y));
	}
	
	@Test void testGetStartPushPosition8() {
		Point startingTargetPosition = new Point(1, 7);
		Point endingTargetPosition = new Point(4, 7);
		Point startingPushPosition = getStartPushPosition(startingTargetPosition, endingTargetPosition);
		assertEquals(0, Math.round(startingPushPosition.x));
		assertEquals(7, Math.round(startingPushPosition.y));
	}
	
	@Test void testGetStartPushPosition9() {
		Point startingTargetPosition = new Point(5, 6);
		Point endingTargetPosition = new Point(2, 6);
		Point startingPushPosition = getStartPushPosition( startingTargetPosition, endingTargetPosition);
		assertEquals(6, Math.round(startingPushPosition.x));
		assertEquals(6, Math.round(startingPushPosition.y));
	}
	
	@Test void testGetStartPushPosition10() {
		Point startingTargetPosition = new Point(7, 7);
		Point endingTargetPosition = new Point(7, 4);
		Point startingPushPosition = getStartPushPosition(startingTargetPosition, endingTargetPosition);
		assertEquals(7, Math.round(startingPushPosition.x));
		assertEquals(8, Math.round(startingPushPosition.y));
	}
	
	// Test for Cross Tunnel
	Point left = new Point(5, 5);
	Point right = new Point(6, 5);
	RampEdge rampEdge = new RampEdge(left, right);
	@Test void testCrossTunnel1() {
		// NOTE: COMMENT OUT ALL travelTo() and method calls inside crossTunnel Method
		// left to right path DONE
		 Point tunnel_LL = new Point(4, 7);
		 Point tunnel_UR = new Point(6, 8);
		 ArrayList <Point> path = crossTunnel(tunnel_LL, tunnel_UR, rampEdge, false);
		 assertEquals(0.5, path.get(0).x);
		 assertEquals(0.5, path.get(0).y);
		 assertEquals(3, Math.round(path.get(1).x));
		 assertEquals(7.5, round(path.get(1).y, 1));
		 assertEquals(6.8, path.get(2).x);
		 assertEquals(7.5, path.get(2).y);
	}
	
	@Test void testCrossTunnel2() {
		// NOTE: COMMENT OUT ALL travelTo() and method calls inside crossTunnel Method
		// left to right path DONE
		 Point tunnel_LL = new Point(2, 1);
		 Point tunnel_UR = new Point(4, 2);
		 ArrayList <Point> path = crossTunnel(tunnel_LL, tunnel_UR, rampEdge,false);
		 assertEquals(0.5, path.get(0).x);
		 assertEquals(0.5, path.get(0).y);
		 assertEquals(1.2, round(path.get(1).x, 1));
		 assertEquals(1.5, round(path.get(1).y, 1));
		 assertEquals(4.8, path.get(2).x);
		 assertEquals(1.5, path.get(2).y);
	}
	
	@Test void testCrossTunnel3() {
		// NOTE: COMMENT OUT ALL travelTo() and method calls inside crossTunnel Method
		// low to high path DONE
		 Point tunnel_LL = new Point(1, 3);
		 Point tunnel_UR = new Point(2, 5);
		 ArrayList <Point> path = crossTunnel(tunnel_LL, tunnel_UR,rampEdge, false);
		 assertEquals(0.5, path.get(0).x);
		 assertEquals(0.5, path.get(0).y);
		 assertEquals(1.5, round(path.get(1).x, 1));
		 assertEquals(2.2, round(path.get(1).y, 1));
		 assertEquals(1.5, path.get(2).x);
		 assertEquals(5.8, path.get(2).y);
	}
	
	@Test void testCrossTunnel4() {
		// NOTE: COMMENT OUT ALL travelTo() and method calls inside crossTunnel Method
		// low to high path DONE
		 Point tunnel_LL = new Point(3, 3);
		 Point tunnel_UR = new Point(4, 5);
		 ArrayList <Point> path = crossTunnel(tunnel_LL, tunnel_UR, rampEdge,false);
		 assertEquals(0.5, path.get(0).x);
		 assertEquals(0.5, path.get(0).y);
		 assertEquals(3.5, round(path.get(1).x, 1));
		 assertEquals(2.2, round(path.get(1).y, 1));
		 assertEquals(3.5, path.get(2).x);
		 assertEquals(5.8, path.get(2).y);
	}
	
	@Test void testCrossTunnel5() {
		// NOTE: COMMENT OUT ALL travelTo() and method calls inside crossTunnel Method
		// right to left path DONE
		 Point tunnel_LL = new Point(-3, 1);
		 Point tunnel_UR = new Point(-1, 2);
		 ArrayList <Point> path = crossTunnel(tunnel_LL, tunnel_UR, rampEdge,false);
		 assertEquals(0.5, path.get(0).x);
		 assertEquals(0.5, path.get(0).y);
		 assertEquals(-0.2, round(path.get(1).x, 1));
		 assertEquals(1.5, round(path.get(1).y, 1));
		 assertEquals(-3.8, path.get(2).x);
		 assertEquals(1.5, path.get(2).y);
	}
	
	@Test void testCrossTunnel6() {
		// NOTE: COMMENT OUT ALL travelTo() and method calls inside crossTunnel Method
		// right to left path DONE
		 Point tunnel_LL = new Point(-8, 2);
		 Point tunnel_UR = new Point(-2, 3);
		 ArrayList <Point> path = crossTunnel(tunnel_LL, tunnel_UR, rampEdge,false);
		 assertEquals(0.5, path.get(0).x);
		 assertEquals(0.5, path.get(0).y);
		 assertEquals(-1.2, round(path.get(1).x, 1));
		 assertEquals(2.5, round(path.get(1).y, 1));
		 assertEquals(-8.8, path.get(2).x);
		 assertEquals(2.5, path.get(2).y);
	}
	
	@Test void testCrossTunnel7() {
		// NOTE: COMMENT OUT ALL travelTo() and method calls inside crossTunnel Method
		// High to Low path DONE
		 Point tunnel_LL = new Point(0, -3);
		 Point tunnel_UR = new Point(1, -1);
		 ArrayList <Point> path = crossTunnel(tunnel_LL, tunnel_UR, rampEdge,false);
		 assertEquals(0.5, path.get(0).x);
		 assertEquals(0.5, path.get(0).y);
		 assertEquals(0.5, round(path.get(1).x, 1));
		 assertEquals(-0.2, round(path.get(1).y, 1));
		 assertEquals(0.5, path.get(2).x);
		 assertEquals(-3.8, path.get(2).y);
	}
	
	@Test void testCrossTunnel8() {
		// NOTE: COMMENT OUT ALL travelTo() and method calls inside crossTunnel Method
		// High to Low path DONE
		 Point tunnel_LL = new Point(-2, -3);
		 Point tunnel_UR = new Point(-1, -1);
		 ArrayList <Point> path = crossTunnel(tunnel_LL, tunnel_UR, rampEdge,false);
		 assertEquals(0.5, path.get(0).x);
		 assertEquals(0.5, path.get(0).y);
		 assertEquals(-1.5, round(path.get(1).x, 1));
		 assertEquals(-0.2, round(path.get(1).y, 1));
		 assertEquals(-1.5, path.get(2).x);
		 assertEquals(-3.8, path.get(2).y);
	}
	
	@Test void testCrossTunnel9() {
		// NOTE: COMMENT OUT ALL travelTo() and method calls inside crossTunnel Method
		// Low to High path DONE
		 Point tunnel_LL = new Point(-5, 3);
		 Point tunnel_UR = new Point(-4, 5);
		 ArrayList <Point> path = crossTunnel(tunnel_LL, tunnel_UR, rampEdge,false);
		 assertEquals(0.5, path.get(0).x);
		 assertEquals(0.5, path.get(0).y);
		 assertEquals(-4.5, round(path.get(1).x, 1));
		 assertEquals(2.2, round(path.get(1).y, 1));
		 assertEquals(-4.5, path.get(2).x);
		 assertEquals(5.8, path.get(2).y);
	}
	
	@Test void testCrossTunnel10() {
		// NOTE: COMMENT OUT ALL travelTo() and method calls inside crossTunnel Method
		// Right to Left path DONE
		 Point tunnel_LL = new Point(-6, 1);
		 Point tunnel_UR = new Point(-4, 2);
		 ArrayList <Point> path = crossTunnel(tunnel_LL, tunnel_UR, rampEdge,false);
		 assertEquals(0.5, path.get(0).x);
		 assertEquals(0.5, path.get(0).y);
		 assertEquals(-3.2, round(path.get(1).x, 1));
		 assertEquals(1.5, round(path.get(1).y, 1));
		 assertEquals(-6.8, path.get(2).x);
		 assertEquals(1.5, path.get(2).y);
	}
	
	
	// findRampOrientation() unit tests
	
	// NORTH orientation tests
	@Test void testFindRampOrientation1() {
		Point leftRampEdge = new Point(5, 5);
		Point rightRampEdge = new Point(6, 5);
		String orientation = findRampOrientation(leftRampEdge, rightRampEdge);
		assertEquals("NORTH", orientation);
	}
	
	// NORTH orientation tests
	@Test void testFindRampOrientation2() {
		Point leftRampEdge = new Point(1, 1);
		Point rightRampEdge = new Point(2, 1);
		String orientation = findRampOrientation(leftRampEdge, rightRampEdge);
		assertEquals("NORTH", orientation);
	}
	
	// SOUTH orientation tests
	@Test void testFindRampOrientation3() {
		Point leftRampEdge = new Point(6, 5);
		Point rightRampEdge = new Point(5, 5);
		String orientation = findRampOrientation(leftRampEdge, rightRampEdge);
		assertEquals("SOUTH", orientation);
	}
	
	// SOUTH orientation tests
	@Test void testFindRampOrientation4() {
		Point leftRampEdge = new Point(2, 1);
		Point rightRampEdge = new Point(1, 1);
		String orientation = findRampOrientation(leftRampEdge, rightRampEdge);
		assertEquals("SOUTH", orientation);
	}
	
	// EAST orientation tests
	@Test void testFindRampOrientation5() {
		Point leftRampEdge = new Point(2, 1);
		Point rightRampEdge = new Point(2, 0);
		String orientation = findRampOrientation(leftRampEdge, rightRampEdge);
		assertEquals("EAST", orientation);
	}
	
	// EAST orientation tests
	@Test void testFindRampOrientation6() {
		Point leftRampEdge = new Point(5, 4);
		Point rightRampEdge = new Point(5, 3);
		String orientation = findRampOrientation(leftRampEdge, rightRampEdge);
		assertEquals("EAST", orientation);
	}
	
	// WEST orientation tests
	@Test void testFindRampOrientation7() {
		Point leftRampEdge = new Point(5, 3);
		Point rightRampEdge = new Point(5, 4);
		String orientation = findRampOrientation(leftRampEdge, rightRampEdge);
		assertEquals("WEST", orientation);
	}
	
	// WEST orientation tests
	@Test void testFindRampOrientation8() {
		Point leftRampEdge = new Point(2, 0);
		Point rightRampEdge = new Point(2, 1);
		String orientation = findRampOrientation(leftRampEdge, rightRampEdge);
		assertEquals("WEST", orientation);
	}
	
	// WEST orientation tests
	@Test void testFindRampOrientation9() {
		Point leftRampEdge = new Point(8, 5);
		Point rightRampEdge = new Point(8, 6);
		String orientation = findRampOrientation(leftRampEdge, rightRampEdge);
		assertEquals("WEST", orientation);
	}
	
	// EAST orientation tests
	@Test void testFindRampOrientation10() {
		Point leftRampEdge = new Point(8, 6);
		Point rightRampEdge = new Point(8, 5);
		String orientation = findRampOrientation(leftRampEdge, rightRampEdge);
		assertEquals("EAST", orientation);
	}
	
	// getAllPoints() unit tests

	// empty ramp zone
	@Test void testGetAllPoints1() {
		Point searchLL = new Point(0, 5);
		Point searchUR = new Point(4, 9);
		ArrayList <Point> rampZone = new ArrayList<Point>();
		ArrayList <Point> path = getAllPoints(searchLL, searchUR, rampZone);
		assertEquals(1, path.get(0).x);
		assertEquals(6, path.get(0).y);
		assertEquals(1, path.get(1).x);
		assertEquals(7, path.get(1).y);
		assertEquals(1, path.get(2).x);
		assertEquals(8, path.get(2).y);
		
		assertEquals(2, path.get(3).x);
		assertEquals(6, path.get(3).y);
		assertEquals(2, path.get(4).x);
		assertEquals(7, path.get(4).y);
		assertEquals(2, path.get(5).x);
		assertEquals(8, path.get(5).y);
		
		assertEquals(3, path.get(6).x);
		assertEquals(6, path.get(6).y);
		assertEquals(3, path.get(7).x);
		assertEquals(7, path.get(7).y);
		assertEquals(3, path.get(8).x);
		assertEquals(8, path.get(8).y);
	}
	
	// empty ramp zone
	@Test void testGetAllPoints2() {
		Point searchLL = new Point(11, 5);
		Point searchUR = new Point(15, 9);
		ArrayList <Point> rampZone = new ArrayList<Point>();
		ArrayList <Point> path = getAllPoints(searchLL, searchUR, rampZone);
		assertEquals(12, path.get(0).x);
		assertEquals(6, path.get(0).y);
		assertEquals(12, path.get(1).x);
		assertEquals(7, path.get(1).y);
		assertEquals(12, path.get(2).x);
		assertEquals(8, path.get(2).y);
		
		assertEquals(13, path.get(3).x);
		assertEquals(6, path.get(3).y);
		assertEquals(13, path.get(4).x);
		assertEquals(7, path.get(4).y);
		assertEquals(13, path.get(5).x);
		assertEquals(8, path.get(5).y);
		
		assertEquals(14, path.get(6).x);
		assertEquals(6, path.get(6).y);
		assertEquals(14, path.get(7).x);
		assertEquals(7, path.get(7).y);
		assertEquals(14, path.get(8).x);
		assertEquals(8, path.get(8).y);
	}
	
	// empty ramp zone
	@Test void testGetAllPoints3() {
		Point searchLL = new Point(11, 0);
		Point searchUR = new Point(15, 4);
		ArrayList <Point> rampZone = new ArrayList<Point>();
		ArrayList <Point> path = getAllPoints(searchLL, searchUR, rampZone);
		assertEquals(12, path.get(0).x);
		assertEquals(1, path.get(0).y);
		assertEquals(12, path.get(1).x);
		assertEquals(2, path.get(1).y);
		assertEquals(12, path.get(2).x);
		assertEquals(3, path.get(2).y);
			
		assertEquals(13, path.get(3).x);
		assertEquals(1, path.get(3).y);
		assertEquals(13, path.get(4).x);
		assertEquals(2, path.get(4).y);
		assertEquals(13, path.get(5).x);
		assertEquals(3, path.get(5).y);
			
		assertEquals(14, path.get(6).x);
		assertEquals(1, path.get(6).y);
		assertEquals(14, path.get(7).x);
		assertEquals(2, path.get(7).y);
		assertEquals(14, path.get(8).x);
		assertEquals(3, path.get(8).y);
	}
	
	@Test void testGetAllPoints4() {
		
		// initialize variables
		Point searchLL = new Point(0, 5);
		Point searchUR = new Point(4, 9);
		ArrayList <Point> rampZone = new ArrayList<Point>();
		Point rampPoint1 = new Point(1, 7);
		Point rampPoint2 = new Point(2, 7);
		Point rampPoint3 = new Point(1, 8);
		Point rampPoint4 = new Point(2, 8);
		Point rampPoint5 = new Point(1, 9);
		Point rampPoint6 = new Point(2, 9);
		rampZone.add(rampPoint1);
		rampZone.add(rampPoint2);
		rampZone.add(rampPoint3);
		rampZone.add(rampPoint4);
		rampZone.add(rampPoint5);
		rampZone.add(rampPoint6);
		
		ArrayList <Point> path = getAllPoints(searchLL, searchUR, rampZone);
		assertEquals(1, path.get(0).x);
		assertEquals(6, path.get(0).y);
		assertEquals(2, path.get(1).x);
		assertEquals(6, path.get(1).y);
		assertEquals(3, path.get(2).x);
		assertEquals(6, path.get(2).y);
		assertEquals(3, path.get(3).x);
		assertEquals(7, path.get(3).y);
		assertEquals(3, path.get(4).x);
		assertEquals(8, path.get(4).y);
		
	}
	
	@Test void testGetAllPoints5() {
		
		// initialize variables
		Point searchLL = new Point(11, 5);
		Point searchUR = new Point(15, 9);
		ArrayList <Point> rampZone = new ArrayList<Point>();
		Point rampPoint1 = new Point(12, 7);
		Point rampPoint2 = new Point(13, 7);
		Point rampPoint3 = new Point(12, 8);
		Point rampPoint4 = new Point(13, 8);
		Point rampPoint5 = new Point(12, 9);
		Point rampPoint6 = new Point(13, 9);
		rampZone.add(rampPoint1);
		rampZone.add(rampPoint2);
		rampZone.add(rampPoint3);
		rampZone.add(rampPoint4);
		rampZone.add(rampPoint5);
		rampZone.add(rampPoint6);
		
		ArrayList <Point> path = getAllPoints(searchLL, searchUR, rampZone);
		assertEquals(12, path.get(0).x);
		assertEquals(6, path.get(0).y);
		assertEquals(13, path.get(1).x);
		assertEquals(6, path.get(1).y);
		assertEquals(14, path.get(2).x);
		assertEquals(6, path.get(2).y);
		assertEquals(14, path.get(3).x);
		assertEquals(7, path.get(3).y);
		assertEquals(14, path.get(4).x);
		assertEquals(8, path.get(4).y);
		
	}
	
	@Test void testGetAllPoints6() {
		
		// initialize variables
		Point searchLL = new Point(11, 0);
		Point searchUR = new Point(15, 4);
		ArrayList <Point> rampZone = new ArrayList<Point>();
		Point rampPoint1 = new Point(12, 2);
		Point rampPoint2 = new Point(13, 2);
		Point rampPoint3 = new Point(12, 3);
		Point rampPoint4 = new Point(13, 3);
		Point rampPoint5 = new Point(12, 4);
		Point rampPoint6 = new Point(13, 4);
		rampZone.add(rampPoint1);
		rampZone.add(rampPoint2);
		rampZone.add(rampPoint3);
		rampZone.add(rampPoint4);
		rampZone.add(rampPoint5);
		rampZone.add(rampPoint6);
		
		ArrayList <Point> path = getAllPoints(searchLL, searchUR, rampZone);
		assertEquals(12, path.get(0).x);
		assertEquals(1, path.get(0).y);
		assertEquals(13, path.get(1).x);
		assertEquals(1, path.get(1).y);
		assertEquals(14, path.get(2).x);
		assertEquals(1, path.get(2).y);
		assertEquals(14, path.get(3).x);
		assertEquals(2, path.get(3).y);
		assertEquals(14, path.get(4).x);
		assertEquals(3, path.get(4).y);
		
	}
	
	@Test void testGetAllPoints7() {
		
		// initialize variables
		Point searchLL = new Point(8, 5);
		Point searchUR = new Point(12, 9);
		ArrayList <Point> rampZone = new ArrayList<Point>();
		Point rampPoint1 = new Point(9, 7);
		Point rampPoint2 = new Point(10, 7);
		Point rampPoint3 = new Point(9, 8);
		Point rampPoint4 = new Point(10, 8);
		Point rampPoint5 = new Point(9, 9);
		Point rampPoint6 = new Point(10, 9);
		rampZone.add(rampPoint1);
		rampZone.add(rampPoint2);
		rampZone.add(rampPoint3);
		rampZone.add(rampPoint4);
		rampZone.add(rampPoint5);
		rampZone.add(rampPoint6);
		
		ArrayList <Point> path = getAllPoints(searchLL, searchUR, rampZone);
		assertEquals(9, path.get(0).x);
		assertEquals(6, path.get(0).y);
		assertEquals(10, path.get(1).x);
		assertEquals(6, path.get(1).y);
		assertEquals(11, path.get(2).x);
		assertEquals(6, path.get(2).y);
		assertEquals(11, path.get(3).x);
		assertEquals(7, path.get(3).y);
		assertEquals(11, path.get(4).x);
		assertEquals(8, path.get(4).y);
		
	}
	
	@Test void testGetAllPoints8() {
		
		// initialize variables
		Point searchLL = new Point(6, 5);
		Point searchUR = new Point(10, 9);
		ArrayList <Point> rampZone = new ArrayList<Point>();
		Point rampPoint1 = new Point(9, 7);
		Point rampPoint2 = new Point(10, 7);
		Point rampPoint3 = new Point(9, 8);
		Point rampPoint4 = new Point(10, 8);
		Point rampPoint5 = new Point(9, 9);
		Point rampPoint6 = new Point(10, 9);
		rampZone.add(rampPoint1);
		rampZone.add(rampPoint2);
		rampZone.add(rampPoint3);
		rampZone.add(rampPoint4);
		rampZone.add(rampPoint5);
		rampZone.add(rampPoint6);
		
		ArrayList <Point> path = getAllPoints(searchLL, searchUR, rampZone);
		assertEquals(7, path.get(0).x);
		assertEquals(6, path.get(0).y);
		assertEquals(7, path.get(1).x);
		assertEquals(7, path.get(1).y);
		assertEquals(7, path.get(2).x);
		assertEquals(8, path.get(2).y);
		assertEquals(8, path.get(3).x);
		assertEquals(6, path.get(3).y);
		assertEquals(8, path.get(4).x);
		assertEquals(7, path.get(4).y);
		assertEquals(8, path.get(5).x);
		assertEquals(8, path.get(5).y);
		assertEquals(9, path.get(6).x);
		assertEquals(6, path.get(6).y);
	}
	
	// unusual search zone tests
	@Test void testGetAllPoints9() {
		
		// initialize variables
		Point searchLL = new Point(0, 0);
		Point searchUR = new Point(2, 2);
		ArrayList <Point> rampZone = new ArrayList<Point>();
		
		ArrayList <Point> path = getAllPoints(searchLL, searchUR, rampZone);
		assertEquals(1, path.get(0).x);
		assertEquals(1, path.get(0).y);
	}
	
	@Test void testGetAllPoints10() {
		
		// initialize variables
		Point searchLL = new Point(0, 0);
		Point searchUR = new Point(3, 4);
		ArrayList <Point> rampZone = new ArrayList<Point>();
		Point rampPoint1 = new Point(2, 1);
		Point rampPoint2 = new Point(2, 2);
		Point rampPoint3 = new Point(3, 1);
		Point rampPoint4 = new Point(3, 2);
		Point rampPoint5 = new Point(4, 1);
		Point rampPoint6 = new Point(4, 2);
		rampZone.add(rampPoint1);
		rampZone.add(rampPoint2);
		rampZone.add(rampPoint3);
		rampZone.add(rampPoint4);
		rampZone.add(rampPoint5);
		rampZone.add(rampPoint6);
		
		ArrayList <Point> path = getAllPoints(searchLL, searchUR, rampZone);
		assertEquals(1, path.get(0).x);
		assertEquals(1, path.get(0).y);
		assertEquals(1, path.get(1).x);
		assertEquals(2, path.get(1).y);
	}
	
	
	// HELLPER METHODS
	/**
	   * 
	   * @param value to be rounded
	   * @param places after decimals
	   * @return rounded value
	   */
	  public static double round(double value, int places) {
		    if (places < 0) throw new IllegalArgumentException();

		    long factor = (long) Math.pow(10, places);
		    value = value * factor;
		    long tmp = Math.round(value);
		    return (double) tmp / factor;
	 }

}
