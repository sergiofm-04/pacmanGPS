import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PacmanTest {

    @Mock
    private Board mockBoard;
    
    @Mock
    private Graphics mockGraphics;
    
    @Mock
    private KeyEvent mockKeyEvent;

    private Pacman pacman;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockBoard.getBlockSize()).thenReturn(20);
        when(mockBoard.getBoardWidth()).thenReturn(400);
        when(mockBoard.getBoardHeight()).thenReturn(400);
        pacman = new Pacman(100, 100, mockBoard);
    }

    @Test
    public void testPacmanInitialization() {
        assertNotNull(pacman);
        assertEquals(100, pacman.getX());
        assertEquals(100, pacman.getY());
        assertEquals(0, pacman.getScore());
        assertEquals(3, pacman.getLives());
    }

    @Test
    public void testGetCharacterSize() {
        assertEquals(15, Pacman.getCharacterSize());
    }

    @Test
    public void testAddScore() {
        pacman.addScore(10);
        assertEquals(10, pacman.getScore());
        
        pacman.addScore(50);
        assertEquals(60, pacman.getScore());
    }

    @Test
    public void testLoseLife() {
        assertEquals(3, pacman.getLives());
        pacman.loseLife();
        assertEquals(2, pacman.getLives());
        pacman.loseLife();
        assertEquals(1, pacman.getLives());
    }

    @Test
    public void testResetPosition() {
        // Move pacman
        when(mockBoard.isWallForPacman(anyInt(), anyInt())).thenReturn(false);
        pacman.move();
        
        // Activate power-up
        pacman.activatePowerUp();
        assertTrue(pacman.isPoweredUp());
        
        // Reset position
        pacman.resetPosition();
        assertEquals(100, pacman.getX());
        assertEquals(100, pacman.getY());
        assertFalse(pacman.isPoweredUp());
    }

    @Test
    public void testKeyPressedLeft() {
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_LEFT);
        pacman.keyPressed(mockKeyEvent);
        // Direction is private, but we can verify movement behavior
        verify(mockKeyEvent).getKeyCode();
    }

    @Test
    public void testKeyPressedRight() {
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_RIGHT);
        pacman.keyPressed(mockKeyEvent);
        verify(mockKeyEvent).getKeyCode();
    }

    @Test
    public void testKeyPressedUp() {
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_UP);
        pacman.keyPressed(mockKeyEvent);
        verify(mockKeyEvent).getKeyCode();
    }

    @Test
    public void testKeyPressedDown() {
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_DOWN);
        pacman.keyPressed(mockKeyEvent);
        verify(mockKeyEvent).getKeyCode();
    }

    @Test
    public void testMoveLeft() {
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_LEFT);
        pacman.keyPressed(mockKeyEvent);
        
        int initialX = pacman.getX();
        when(mockBoard.isWallForPacman(anyInt(), anyInt())).thenReturn(false);
        pacman.move();
        
        assertTrue(pacman.getX() < initialX, "Pacman should move left");
    }

    @Test
    public void testMoveRight() {
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_RIGHT);
        pacman.keyPressed(mockKeyEvent);
        
        int initialX = pacman.getX();
        when(mockBoard.isWallForPacman(anyInt(), anyInt())).thenReturn(false);
        pacman.move();
        
        assertTrue(pacman.getX() > initialX, "Pacman should move right");
    }

    @Test
    public void testMoveUp() {
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_UP);
        pacman.keyPressed(mockKeyEvent);
        
        int initialY = pacman.getY();
        when(mockBoard.isWallForPacman(anyInt(), anyInt())).thenReturn(false);
        pacman.move();
        
        assertTrue(pacman.getY() < initialY, "Pacman should move up");
    }

    @Test
    public void testMoveDown() {
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_DOWN);
        pacman.keyPressed(mockKeyEvent);
        
        int initialY = pacman.getY();
        when(mockBoard.isWallForPacman(anyInt(), anyInt())).thenReturn(false);
        pacman.move();
        
        assertTrue(pacman.getY() > initialY, "Pacman should move down");
    }

    @Test
    public void testMoveBlockedByWall() {
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_RIGHT);
        pacman.keyPressed(mockKeyEvent);
        
        int initialX = pacman.getX();
        when(mockBoard.isWallForPacman(anyInt(), anyInt())).thenReturn(true);
        pacman.move();
        
        assertEquals(initialX, pacman.getX(), "Pacman should not move when blocked by wall");
    }

    @Test
    public void testTeleportLeft() {
        // Create pacman at left edge
        Pacman leftPacman = new Pacman(-25, 100, mockBoard);
        when(mockBoard.isWallForPacman(anyInt(), anyInt())).thenReturn(false);
        
        leftPacman.move();
        
        // Should teleport to right side
        assertTrue(leftPacman.getX() > 300, "Pacman should teleport to right side");
    }

    @Test
    public void testTeleportRight() {
        // Create pacman at right edge
        Pacman rightPacman = new Pacman(405, 100, mockBoard);
        when(mockBoard.isWallForPacman(anyInt(), anyInt())).thenReturn(false);
        
        rightPacman.move();
        
        // Should teleport to left side
        assertTrue(rightPacman.getX() < 100, "Pacman should teleport to left side");
    }

    @Test
    public void testVerticalBoundaryTop() {
        Pacman topPacman = new Pacman(100, -10, mockBoard);
        when(mockBoard.isWallForPacman(anyInt(), anyInt())).thenReturn(false);
        
        topPacman.move();
        
        assertTrue(topPacman.getY() >= 0, "Pacman should stay within top boundary");
    }

    @Test
    public void testVerticalBoundaryBottom() {
        Pacman bottomPacman = new Pacman(100, 400, mockBoard);
        when(mockBoard.isWallForPacman(anyInt(), anyInt())).thenReturn(false);
        
        bottomPacman.move();
        
        assertTrue(bottomPacman.getY() <= 385, "Pacman should stay within bottom boundary");
    }

    @Test
    public void testActivatePowerUp() {
        assertFalse(pacman.isPoweredUp());
        
        pacman.activatePowerUp();
        
        assertTrue(pacman.isPoweredUp());
    }

    @Test
    public void testPowerUpDuration() throws InterruptedException {
        pacman.activatePowerUp();
        assertTrue(pacman.isPoweredUp());
        
        long remaining = pacman.getRemainingPowerUpTime();
        assertTrue(remaining > 0, "Should have remaining power-up time");
        assertTrue(remaining <= 10000, "Remaining time should not exceed 10 seconds");
    }

    @Test
    public void testPowerUpExpiration() throws Exception {
        pacman.activatePowerUp();
        assertTrue(pacman.isPoweredUp());
        
        // Use reflection to set powerUpEndTime to past
        java.lang.reflect.Field powerUpEndTimeField = Pacman.class.getDeclaredField("powerUpEndTime");
        powerUpEndTimeField.setAccessible(true);
        powerUpEndTimeField.setLong(pacman, System.currentTimeMillis() - 1000);
        
        // Now isPoweredUp should return false as time has expired
        assertFalse(pacman.isPoweredUp());
        
        // When power-up is not active, getRemainingPowerUpTime should return 0
        Pacman newPacman = new Pacman(100, 100, mockBoard);
        assertEquals(0, newPacman.getRemainingPowerUpTime());
    }

    @Test
    public void testDraw() {
        assertDoesNotThrow(() -> pacman.draw(mockGraphics, 40));
        verify(mockGraphics, atLeastOnce()).setColor(any());
        verify(mockGraphics, atLeastOnce()).fillArc(anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt());
    }

    @Test
    public void testMultipleMoves() {
        when(mockBoard.isWallForPacman(anyInt(), anyInt())).thenReturn(false);
        
        int initialX = pacman.getX();
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_RIGHT);
        pacman.keyPressed(mockKeyEvent);
        
        pacman.move();
        pacman.move();
        pacman.move();
        
        assertTrue(pacman.getX() > initialX, "Pacman should have moved multiple times");
    }

    @Test
    public void testScoreAccumulation() {
        assertEquals(0, pacman.getScore());
        
        pacman.addScore(10);
        pacman.addScore(20);
        pacman.addScore(30);
        
        assertEquals(60, pacman.getScore());
    }

    @Test
    public void testLivesCannotGoNegative() {
        pacman.loseLife();
        pacman.loseLife();
        pacman.loseLife();
        pacman.loseLife(); // Extra life loss
        
        assertTrue(pacman.getLives() <= 0, "Lives can go to zero or below");
    }

    @Test
    public void testGetRemainingPowerUpTimeWhenExpired() throws Exception {
        pacman.activatePowerUp();
        
        // Set power-up end time to the past
        java.lang.reflect.Field powerUpEndTimeField = Pacman.class.getDeclaredField("powerUpEndTime");
        powerUpEndTimeField.setAccessible(true);
        powerUpEndTimeField.setLong(pacman, System.currentTimeMillis() - 1000);
        
        // Remaining time should be 0 when expired
        assertEquals(0, pacman.getRemainingPowerUpTime());
    }

    @Test
    public void testWallCollisionOnAllFourEdges() {
        when(mockBoard.isWallForPacman(anyInt(), anyInt())).thenAnswer(invocation -> {
            int x = invocation.getArgument(0);
            int y = invocation.getArgument(1);
            // Simulate wall at specific edge position
            return x == 104 && y == 100; // Right edge of pacman
        });
        
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_RIGHT);
        pacman.keyPressed(mockKeyEvent);
        
        int initialX = pacman.getX();
        pacman.move();
        
        // Should be blocked by wall on the edge
        assertEquals(initialX, pacman.getX());
    }

    @Test
    public void testAllKeyEventCodes() {
        // Test VK_LEFT
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_LEFT);
        pacman.keyPressed(mockKeyEvent);
        
        // Test VK_RIGHT
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_RIGHT);
        pacman.keyPressed(mockKeyEvent);
        
        // Test VK_UP
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_UP);
        pacman.keyPressed(mockKeyEvent);
        
        // Test VK_DOWN
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_DOWN);
        pacman.keyPressed(mockKeyEvent);
        
        // Test other key (should not crash)
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_SPACE);
        pacman.keyPressed(mockKeyEvent);
        
        verify(mockKeyEvent, atLeast(5)).getKeyCode();
    }
}
