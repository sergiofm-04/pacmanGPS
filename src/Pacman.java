import java.awt.*;
import java.awt.event.*;

public class Pacman {
    private static final int CHARACTER_SIZE = 15;
    private static final int INITIAL_LIVES = 3;
    private static final int POWER_UP_DURATION_MS = 10000; // 10 seconds
    
    private int x, y;
    private int startX, startY;
    private Direction direction = Direction.LEFT;
    private int score = 0;
    private int lives = INITIAL_LIVES;
    private Board board;
    private boolean poweredUp = false;
    private long powerUpEndTime = 0;

    public Pacman(int x, int y, Board board) {
        this.x = x;
        this.y = y;
        this.startX = x;
        this.startY = y;
        this.board = board;
    }

    public void draw(Graphics g, int yOffset) {
        g.setColor(Color.YELLOW);
        g.fillArc(x, y + yOffset, CHARACTER_SIZE, CHARACTER_SIZE, direction.getAngle(), 300);
    }

    public void move() {
        int newX = x;
        int newY = y;
        int speed = 4;
        
        switch (direction) {
            case LEFT: newX -= speed; break;
            case RIGHT: newX += speed; break;
            case UP: newY -= speed; break;
            case DOWN: newY += speed; break;
        }
        
        // Verificar colisión con paredes
        int edge = CHARACTER_SIZE - 1;
        if (!board.isWallForPacman(newX, newY) && 
            !board.isWallForPacman(newX + edge, newY) && 
            !board.isWallForPacman(newX, newY + edge) && 
            !board.isWallForPacman(newX + edge, newY + edge)) {
            x = newX;
            y = newY;
        }
        
        // Teletransporte en los túneles laterales
        int boardWidth = board.getBoardWidth();
        int blockSize = board.getBlockSize();
        
        if (x < -blockSize) {
            x = boardWidth - blockSize;
        } else if (x > boardWidth) {
            x = 0;
        }
        
        // Mantener a Pacman dentro de los límites verticales
        int maxY = board.getBoardHeight() - CHARACTER_SIZE;
        if (y < 0) y = 0;
        if (y > maxY) y = maxY;
    }
    
    public static int getCharacterSize() {
        return CHARACTER_SIZE;
    }
    
    public int getLives() {
        return lives;
    }
    
    public void loseLife() {
        lives--;
    }
    
    public void resetPosition() {
        x = startX;
        y = startY;
        direction = Direction.LEFT;
        // Clear power-up state when respawning
        poweredUp = false;
        powerUpEndTime = 0;
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT: direction = Direction.LEFT; break;
            case KeyEvent.VK_RIGHT: direction = Direction.RIGHT; break;
            case KeyEvent.VK_UP: direction = Direction.UP; break;
            case KeyEvent.VK_DOWN: direction = Direction.DOWN; break;
        }
    }

    public int getScore() {
        return score;
    }
    
    public void addScore(int points) {
        score += points;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public void activatePowerUp() {
        poweredUp = true;
        powerUpEndTime = System.currentTimeMillis() + POWER_UP_DURATION_MS;
    }
    
    public boolean isPoweredUp() {
        if (poweredUp && System.currentTimeMillis() > powerUpEndTime) {
            poweredUp = false;
        }
        return poweredUp;
    }
    
    public long getRemainingPowerUpTime() {
        if (!poweredUp) {
            return 0;
        }
        long remaining = powerUpEndTime - System.currentTimeMillis();
        return remaining > 0 ? remaining : 0;
    }
}