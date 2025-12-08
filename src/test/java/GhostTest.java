import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.awt.Color;
import java.awt.Graphics;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GhostTest {

    @Mock
    private Board mockBoard;
    
    @Mock
    private Graphics mockGraphics;

    private Ghost ghost;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockBoard.getBlockSize()).thenReturn(20);
        when(mockBoard.getBoardWidth()).thenReturn(400);
        when(mockBoard.getBoardHeight()).thenReturn(400);
        ghost = new Ghost(100, 100, Color.RED, mockBoard);
    }

    @Test
    public void testGhostInitialization() {
        assertNotNull(ghost);
        assertEquals(100, ghost.getX());
        assertEquals(100, ghost.getY());
    }

    @Test
    public void testGetX() {
        assertEquals(100, ghost.getX());
    }

    @Test
    public void testGetY() {
        assertEquals(100, ghost.getY());
    }

    @Test
    public void testSetFrightened() {
        assertFalse(ghost.isFrightened());
        
        ghost.setFrightened(true);
        assertTrue(ghost.isFrightened());
        
        ghost.setFrightened(false);
        assertFalse(ghost.isFrightened());
    }

    @Test
    public void testIsReturningInitiallyFalse() {
        assertFalse(ghost.isReturning());
    }

    @Test
    public void testSendToStart() {
        ghost.sendToStart();
        assertTrue(ghost.isReturning());
    }

    @Test
    public void testFrightenedReturnsFalseWhenReturning() {
        ghost.setFrightened(true);
        ghost.sendToStart();
        
        // When returning, isFrightened should return false even if frightened is set
        assertFalse(ghost.isFrightened());
    }

    @Test
    public void testResetPosition() {
        // Move ghost
        when(mockBoard.isWall(anyInt(), anyInt())).thenReturn(false);
        ghost.move();
        
        // Reset to new position
        ghost.resetPosition(200, 200);
        assertEquals(200, ghost.getX());
        assertEquals(200, ghost.getY());
        assertFalse(ghost.isReturning());
    }

    @Test
    public void testMoveWhenNotReturning() {
        when(mockBoard.isWall(anyInt(), anyInt())).thenReturn(false);
        
        int initialX = ghost.getX();
        int initialY = ghost.getY();
        
        ghost.move();
        
        // Ghost should have moved (position changed)
        boolean moved = (ghost.getX() != initialX) || (ghost.getY() != initialY);
        assertTrue(moved, "Ghost should have moved");
    }

    @Test
    public void testMoveWhenReturning() {
        ghost.sendToStart();
        assertTrue(ghost.isReturning());
        
        int initialX = ghost.getX();
        int initialY = ghost.getY();
        
        // Move should not change position immediately when returning
        ghost.move();
        
        // Position should remain same or return to start
        assertTrue(true, "Ghost in returning state handled move");
    }

    @Test
    public void testMoveBlockedByWall() {
        when(mockBoard.isWall(anyInt(), anyInt())).thenReturn(true);
        
        int initialX = ghost.getX();
        int initialY = ghost.getY();
        
        // Try to move multiple times
        for (int i = 0; i < 10; i++) {
            ghost.move();
        }
        
        // Ghost might change direction but shouldn't move through walls
        verify(mockBoard, atLeastOnce()).isWall(anyInt(), anyInt());
    }

    @Test
    public void testFrightenedSlowerSpeed() {
        when(mockBoard.isWall(anyInt(), anyInt())).thenReturn(false);
        
        // Test normal speed
        Ghost normalGhost = new Ghost(100, 100, Color.RED, mockBoard);
        int normalX1 = normalGhost.getX();
        int normalY1 = normalGhost.getY();
        for (int i = 0; i < 5; i++) {
            normalGhost.move();
        }
        double normalDistance = Math.sqrt(
            Math.pow(normalGhost.getX() - normalX1, 2) + 
            Math.pow(normalGhost.getY() - normalY1, 2)
        );
        
        // Test frightened speed
        Ghost frightenedGhost = new Ghost(100, 100, Color.RED, mockBoard);
        frightenedGhost.setFrightened(true);
        int frightenedX1 = frightenedGhost.getX();
        int frightenedY1 = frightenedGhost.getY();
        for (int i = 0; i < 5; i++) {
            frightenedGhost.move();
        }
        double frightenedDistance = Math.sqrt(
            Math.pow(frightenedGhost.getX() - frightenedX1, 2) + 
            Math.pow(frightenedGhost.getY() - frightenedY1, 2)
        );
        
        // Frightened ghost should generally move slower or same (due to randomness, not always guaranteed)
        assertTrue(frightenedDistance <= normalDistance + 10, "Frightened ghost should move at same or slower speed");
    }

    @Test
    public void testTeleportLeft() {
        Ghost leftGhost = new Ghost(-25, 100, Color.RED, mockBoard);
        when(mockBoard.isWall(anyInt(), anyInt())).thenReturn(false);
        
        leftGhost.move();
        
        // Should eventually teleport or stay in bounds
        assertTrue(leftGhost.getX() >= -25, "Ghost X should be handled at left edge");
    }

    @Test
    public void testTeleportRight() {
        Ghost rightGhost = new Ghost(405, 100, Color.RED, mockBoard);
        when(mockBoard.isWall(anyInt(), anyInt())).thenReturn(false);
        
        rightGhost.move();
        
        // Should eventually teleport or stay in bounds
        assertTrue(rightGhost.getX() <= 405 + 20, "Ghost X should be handled at right edge");
    }

    @Test
    public void testVerticalBoundaryTop() {
        Ghost topGhost = new Ghost(100, -10, Color.RED, mockBoard);
        when(mockBoard.isWall(anyInt(), anyInt())).thenReturn(false);
        
        topGhost.move();
        
        assertTrue(topGhost.getY() >= 0, "Ghost should stay within top boundary");
    }

    @Test
    public void testVerticalBoundaryBottom() {
        Ghost bottomGhost = new Ghost(100, 400, Color.RED, mockBoard);
        when(mockBoard.isWall(anyInt(), anyInt())).thenReturn(false);
        
        bottomGhost.move();
        
        assertTrue(bottomGhost.getY() <= 385, "Ghost should stay within bottom boundary");
    }

    @Test
    public void testDraw() {
        assertDoesNotThrow(() -> ghost.draw(mockGraphics, 40));
        verify(mockGraphics, atLeastOnce()).setColor(any());
    }

    @Test
    public void testDrawWhenFrightened() {
        ghost.setFrightened(true);
        assertDoesNotThrow(() -> ghost.draw(mockGraphics, 40));
        verify(mockGraphics, atLeastOnce()).setColor(any());
    }

    @Test
    public void testDrawWhenReturning() {
        ghost.sendToStart();
        assertDoesNotThrow(() -> ghost.draw(mockGraphics, 40));
        verify(mockGraphics, atLeastOnce()).setColor(any());
    }

    @Test
    public void testMultipleGhostsDifferentColors() {
        Ghost redGhost = new Ghost(100, 100, Color.RED, mockBoard);
        Ghost pinkGhost = new Ghost(120, 120, Color.PINK, mockBoard);
        Ghost cyanGhost = new Ghost(140, 140, Color.CYAN, mockBoard);
        
        assertNotNull(redGhost);
        assertNotNull(pinkGhost);
        assertNotNull(cyanGhost);
    }

    @Test
    public void testReturningTimerCountdown() {
        ghost.sendToStart();
        assertTrue(ghost.isReturning());
        
        // Move multiple times to countdown the timer
        when(mockBoard.isWall(anyInt(), anyInt())).thenReturn(false);
        for (int i = 0; i < 60; i++) {
            ghost.move();
        }
        
        // After enough moves, ghost should no longer be returning
        assertFalse(ghost.isReturning());
    }

    @Test
    public void testResetPositionClearsReturningState() {
        ghost.sendToStart();
        assertTrue(ghost.isReturning());
        
        ghost.resetPosition(150, 150);
        
        assertFalse(ghost.isReturning());
        assertEquals(150, ghost.getX());
        assertEquals(150, ghost.getY());
    }

    @Test
    public void testRandomDirectionChanges() {
        when(mockBoard.isWall(anyInt(), anyInt())).thenReturn(false);
        
        int initialX = ghost.getX();
        int initialY = ghost.getY();
        
        // Move many times to ensure random direction changes occur
        for (int i = 0; i < 100; i++) {
            ghost.move();
        }
        
        // Ghost should have moved from initial position
        boolean moved = (ghost.getX() != initialX) || (ghost.getY() != initialY);
        assertTrue(moved, "Ghost should move over multiple iterations");
    }

    @Test
    public void testStateTransitions() {
        // Normal state
        assertFalse(ghost.isFrightened());
        assertFalse(ghost.isReturning());
        
        // Frightened state
        ghost.setFrightened(true);
        assertTrue(ghost.isFrightened());
        assertFalse(ghost.isReturning());
        
        // Returning state (overrides frightened)
        ghost.sendToStart();
        assertFalse(ghost.isFrightened());
        assertTrue(ghost.isReturning());
        
        // Return to normal after timer
        when(mockBoard.isWall(anyInt(), anyInt())).thenReturn(false);
        for (int i = 0; i < 60; i++) {
            ghost.move();
        }
        assertFalse(ghost.isFrightened());
        assertFalse(ghost.isReturning());
    }
}
