import java.awt.*;
import java.awt.event.*;

public class Pacman {
    private int x, y;
    private Direction direction = Direction.LEFT;
    private int score = 0;
    private Board board;

    public Pacman(int x, int y, Board board) {
        this.x = x;
        this.y = y;
        this.board = board;
    }

    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillArc(x, y, 20, 20, direction.getAngle(), 300);
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
        if (!board.isWall(newX, newY) && 
            !board.isWall(newX + 19, newY) && 
            !board.isWall(newX, newY + 19) && 
            !board.isWall(newX + 19, newY + 19)) {
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
        int maxY = board.getBoardHeight() - 20;
        if (y < 0) y = 0;
        if (y > maxY) y = maxY;
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
}