import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

public class DirectionTest {

    @Test
    public void testLeftDirection() {
        assertEquals(180, Direction.LEFT.getAngle());
    }

    @Test
    public void testRightDirection() {
        assertEquals(0, Direction.RIGHT.getAngle());
    }

    @Test
    public void testUpDirection() {
        assertEquals(90, Direction.UP.getAngle());
    }

    @Test
    public void testDownDirection() {
        assertEquals(270, Direction.DOWN.getAngle());
    }

    @ParameterizedTest
    @EnumSource(Direction.class)
    public void testAllDirectionsHaveValidAngles(Direction direction) {
        int angle = direction.getAngle();
        assertTrue(angle >= 0 && angle < 360, "Angle should be between 0 and 359");
    }

    @Test
    public void testDirectionValuesCount() {
        Direction[] directions = Direction.values();
        assertEquals(4, directions.length, "Should have exactly 4 directions");
    }

    @Test
    public void testDirectionValueOf() {
        assertEquals(Direction.LEFT, Direction.valueOf("LEFT"));
        assertEquals(Direction.RIGHT, Direction.valueOf("RIGHT"));
        assertEquals(Direction.UP, Direction.valueOf("UP"));
        assertEquals(Direction.DOWN, Direction.valueOf("DOWN"));
    }

    @Test
    public void testDirectionValueOfInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            Direction.valueOf("INVALID");
        });
    }
}
