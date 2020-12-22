package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.*;
import static ca.mcgill.ecse211.project.MapNav.*;
import static simlejos.ExecutionController.*;

import java.lang.Thread;
import ca.mcgill.ecse211.playingfield.Point;
import simlejos.hardware.ev3.LocalEV3;

/**
 * Main class of the program.<br>
 * This project is a robotic challenge to push targets into a bin for points. In this main program,
 * methods are called
 * to complete the tasks in order.<br>
 * 
 * <p>The perfomed tasks are as follow:
 * <ol>
 * <li>Initialize threads and start the odometer and the avoider.</li>
 * <li>Fetch the wifi parameters and display on console to confirm.</li>
 * <li>Localize robot to its initial position.</li>
 * <li>Cross the tunnel to the island.</li>
 * <li>Search the island's search zone and push targets in the bin.</li>
 * <li>Re-cross the tunnel to return to home base.</li>
 * </ol>
 */
public class Main {
  
  /**
   * The number of threads used in the program (main, odometer), other than the one used to
   * perform physics steps.
   */
  public static final int NUMBER_OF_THREADS = 2;
  
  /** Main entry point. */
  public static void main(String[] args) {
    initialize();
    
    // Start the odometer thread
    new Thread(odometer).start();
    
    // Start the avoidance thread
    // new Thread(avoider).start();

    // Read Wi-Fi values
    wifiDisplay();
    
    // reset arm before crossing bridge
    Navigation.resetArm();
    
    Localizer.usLocalize();
    Localizer.lightLocalize();
    
    Point startPoint = null;
    switch (corner) {
      case 0:
        odometer.setXyt(TILE_SIZE, TILE_SIZE, 90.0);
        startPoint = new Point(0.75, 0.75);
        break;
      case 1:
        odometer.setXyt(14.0 * TILE_SIZE, TILE_SIZE, 0.0);
        startPoint = new Point(14.25, 0.75);
        break;
      case 2:
        odometer.setXyt(14.0 * TILE_SIZE, 8.0 * TILE_SIZE, 270.0);
        startPoint = new Point(14.25, 8.25);
        break;
      case 3:
        odometer.setXyt(TILE_SIZE, 8.0 * TILE_SIZE, 180.0);
        startPoint = new Point(0.75, 8.25);
        break;
      default:
        System.out.println("Invalid start position");
    }
    System.out.println("Starting from corner " + corner);
    System.out.print("Robot set to ");
    odometer.printPosition();

    //Perform 3 beeps
    Navigation.beep(3);
    
    // go to search zone
    crossTunnel(tunnel.ll, tunnel.ur, ramp, false);
    
    // find the blocks and push them into bin
    findBlocks(searchZone.ll, searchZone.ur, ramp.left, ramp.right);
    
    // come back to original island
    crossTunnel(tunnel.ll, tunnel.ur, ramp, true);
    
    // travel to original point
    Navigation.travelTo(startPoint);
    
    Navigation.beep(5);
  }
    
  /**
   * Prints the recieved Wi-Fi parameters.
   */
  public static void wifiDisplay() {
    System.out.println("Running...");
    
    // Example 1: Print out all received data
    System.out.println("Map:\n" + wifiParameters);

    // Example 2: Print out specific values
    System.out.println("Team Color: " + teamColor);
    System.out.println("Team Zone: " + teamZone);
    System.out.println("Tunnel: " + tunnel);
    System.out.println("Search Zone: " + searchZone);
    System.out.println("Island Zone: " + island);
    
    // Example 4: Calculate the area of a region
    System.out.println("The island area is " + island.getWidth() * island.getHeight() + ".");
  }

  /**
   * Initializes the robot logic. It starts a new thread to perform physics steps regularly.
   */
  private static void initialize() {    
    // Run a few physics steps to make sure everything is initialized and has settled properly
    for (int i = 0; i < 50; i++) {
      performPhysicsStep();
    }

    // We are going to start two threads, so the total number of parties is 2
    setNumberOfParties(NUMBER_OF_THREADS);
    
    // Does not count as a thread because it is only for physics steps
    new Thread(() -> {
      while (performPhysicsStep()) {
        sleepFor(PHYSICS_STEP_PERIOD);
      }
    }).start();
  }
}
