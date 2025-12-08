import java.awt.*;
import java.util.Random;

public class Ghost {
    private int x, y;
    private Direction direction;
    private Color color;
    private Random random = new Random();
    private Board board;

    public Ghost(int x, int y, Color color, Board board) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.board = board;
        this.direction = Direction.values()[random.nextInt(4)];
    }

    public void draw(Graphics g, int yOffset) {
        g.setColor(color);
        int size = Pacman.getCharacterSize();
        g.fillOval(x, y + yOffset, size, size);
    }

    public void move() {
        if (random.nextInt(10) == 0) {
            direction = Direction.values()[random.nextInt(4)];
        }
        
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
        int edge = Pacman.getCharacterSize() - 1;
        if (!board.isWall(newX, newY) && 
            !board.isWall(newX + edge, newY) && 
            !board.isWall(newX, newY + edge) && 
            !board.isWall(newX + edge, newY + edge)) {
            x = newX;
            y = newY;
        } else {
            // Cambiar dirección si choca con pared
            direction = Direction.values()[random.nextInt(4)];
        }
        
        // Teletransporte en los túneles laterales
        int boardWidth = board.getBoardWidth();
        int blockSize = board.getBlockSize();
        
        if (x < -blockSize) {
            x = boardWidth - blockSize;
        } else if (x > boardWidth) {
            x = 0;
        }
        
        // Mantener al fantasma dentro de los límites verticales
        int maxY = board.getBoardHeight() - Pacman.getCharacterSize();
        if (y < 0) y = 0;
        if (y > maxY) y = maxY;
    }
    
    public void resetPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
}