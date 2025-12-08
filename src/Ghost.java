import java.awt.*;
import java.util.Random;

public class Ghost {
    private static final int HALF_CIRCLE_DEGREES = 180;
    
    private int x, y;
    private int startX, startY;
    private Direction direction;
    private Color color;
    private Random random = new Random();
    private Board board;
    private boolean frightened = false;
    private boolean returning = false;
    private int returningTimer = 0;

    public Ghost(int x, int y, Color color, Board board) {
        this.x = x;
        this.y = y;
        this.startX = x;
        this.startY = y;
        this.color = color;
        this.board = board;
        this.direction = Direction.values()[random.nextInt(4)];
    }

    public void draw(Graphics g, int yOffset) {
        // Use blue color when frightened, otherwise use normal color
        Color drawColor = (frightened && !returning) ? Color.BLUE : color;
        g.setColor(drawColor);
        int size = Pacman.getCharacterSize();
        int adjustedY = y + yOffset;
        
        // Draw ghost shape: rounded top + wavy bottom
        // Draw the rounded top part (semi-circle)
        g.fillArc(x, adjustedY, size, size, 0, HALF_CIRCLE_DEGREES);
        
        // Draw the body rectangle
        g.fillRect(x, adjustedY + size/2, size, size/2);
        
        // Draw wavy bottom (3 small arcs for the wave effect)
        int waveWidth = size / 3;
        for (int i = 0; i < 3; i++) {
            g.fillArc(x + i * waveWidth, adjustedY + size - waveWidth/2, waveWidth, waveWidth/2, HALF_CIRCLE_DEGREES, HALF_CIRCLE_DEGREES);
        }
        
        // Draw eyes
        g.setColor(Color.WHITE);
        int eyeSize = size / 5;
        int eyeY = adjustedY + size / 3;
        g.fillOval(x + size/4 - eyeSize/2, eyeY, eyeSize, eyeSize);
        g.fillOval(x + 3*size/4 - eyeSize/2, eyeY, eyeSize, eyeSize);
        
        // Draw pupils
        g.setColor(Color.BLACK);
        int pupilSize = eyeSize / 2;
        g.fillOval(x + size/4 - pupilSize/2, eyeY + pupilSize/2, pupilSize, pupilSize);
        g.fillOval(x + 3*size/4 - pupilSize/2, eyeY + pupilSize/2, pupilSize, pupilSize);
    }

    public void move() {
        // If returning to start, handle countdown
        if (returning) {
            returningTimer--;
            if (returningTimer <= 0) {
                x = startX;
                y = startY;
                returning = false;
                frightened = false;
            }
            return; // Don't move while returning countdown
        }
        
        if (random.nextInt(10) == 0) {
            direction = Direction.values()[random.nextInt(4)];
        }
        
        int newX = x;
        int newY = y;
        int speed = frightened ? 2 : 4; // Slower when frightened
        
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
    
    public void setFrightened(boolean frightened) {
        this.frightened = frightened;
    }
    
    public boolean isFrightened() {
        return frightened && !returning;
    }
    
    public void sendToStart() {
        returning = true;
        returningTimer = 50; // About 2 seconds at 40ms per frame
    }
}