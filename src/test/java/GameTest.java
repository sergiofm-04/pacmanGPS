import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import javax.swing.JFrame;
import java.awt.GraphicsEnvironment;
import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    @Test
    public void testGameClassExists() {
        assertNotNull(Game.class);
    }

    @Test
    public void testGameExtendsJFrame() {
        assertTrue(JFrame.class.isAssignableFrom(Game.class));
    }

    @Test
    public void testGameConstructorExists() throws Exception {
        Constructor<Game> constructor = Game.class.getConstructor();
        assertNotNull(constructor);
    }

    @Test
    public void testMainMethodExists() throws Exception {
        assertNotNull(Game.class.getMethod("main", String[].class));
    }

    @Test
    @EnabledIfSystemProperty(named = "java.awt.headless", matches = "false")
    public void testGameInitializationWithDisplay() {
        if (!GraphicsEnvironment.isHeadless()) {
            Game game = new Game();
            assertNotNull(game);
            assertEquals("Pac-Man", game.getTitle());
            assertEquals(400, game.getWidth());
            assertEquals(455, game.getHeight());
            assertEquals(JFrame.EXIT_ON_CLOSE, game.getDefaultCloseOperation());
            assertFalse(game.isResizable());
            game.dispose();
        }
    }

    @Test
    public void testGameConstructorBehavior() {
        // Test in headless-compatible way
        if (GraphicsEnvironment.isHeadless()) {
            assertThrows(Exception.class, () -> {
                new Game();
            }, "Should throw exception in headless environment");
        } else {
            assertDoesNotThrow(() -> {
                Game game = new Game();
                game.dispose();
            });
        }
    }

    @Test
    public void testMainMethodSignature() throws Exception {
        // Verify main method has correct signature
        var method = Game.class.getMethod("main", String[].class);
        assertTrue(java.lang.reflect.Modifier.isStatic(method.getModifiers()));
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
        assertEquals(void.class, method.getReturnType());
    }

    @Test
    public void testGameClassIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(Game.class.getModifiers()));
    }

    @Test
    public void testGameInheritance() {
        assertEquals(JFrame.class, Game.class.getSuperclass());
    }
}

