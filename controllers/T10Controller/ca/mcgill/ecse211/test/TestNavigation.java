package ca.mcgill.ecse211.test;

import static ca.mcgill.ecse211.project.Navigation.minimalAngle;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * Tests the Navigation class. This test runs in Eclipse (right-click > Run as > Unit test) and
 * on the command line, not in Webots!
 * 
 * @author Younes Boubekeur
 */
public class TestNavigation {
  
  /** Tolerate up to this amount of error due to double imprecision. */
  private static final double ERROR_MARGIN = 0.01;
  
  @Test void testMinimalAngle() {
    // Going from 45° to 135° means turning by +90°
    assertEquals(90, minimalAngle(45, 135), ERROR_MARGIN);
    
    // Going from 185° to 175° means turning by -10°
    assertEquals(-10, minimalAngle(185, 175), ERROR_MARGIN);
 
  }
  
}
