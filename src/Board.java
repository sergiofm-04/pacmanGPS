import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Board extends JPanel implements ActionListener {
    private Timer timer;
    private Pacman pacman;
    private Ghost[] ghosts;
    private int currentLevel = 0;
    private int blockSize = 20;
    private int[][][] levels;
    private boolean[][] points;
    private int totalPoints;
    private int collectedPoints = 0;
    private boolean gameWon = false;
    
    // 0 = espacio vacío, 1 = pared, 2 = punto
    private static final int EMPTY = 0;
    private static final int WALL = 1;
    private static final int POINT = 2;

    public Board() {
        setFocusable(true);
        setBackground(Color.BLACK);
        initLevels();
        loadLevel(0);
        timer = new Timer(40, this);
        timer.start();
        addKeyListener(new PacmanKeyAdapter());
    }
    
    private void initLevels() {
        // 3 niveles diferentes (20 filas x 20 columnas)
        levels = new int[3][20][20];
        
        // Nivel 1 - Diseño simple
        int[][] level1 = {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,2,2,2,2,2,2,2,2,1,1,2,2,2,2,2,2,2,2,1},
            {1,2,1,1,2,1,1,1,2,1,1,2,1,1,1,2,1,1,2,1},
            {1,2,1,1,2,1,1,1,2,1,1,2,1,1,1,2,1,1,2,1},
            {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1},
            {1,2,1,1,2,1,2,1,1,1,1,1,1,2,1,2,1,1,2,1},
            {1,2,2,2,2,1,2,2,2,1,1,2,2,2,1,2,2,2,2,1},
            {1,1,1,1,2,1,1,1,0,1,1,0,1,1,1,2,1,1,1,1},
            {1,1,1,1,2,1,0,0,0,0,0,0,0,0,1,2,1,1,1,1},
            {1,1,1,1,2,1,0,1,1,0,0,1,1,0,1,2,1,1,1,1},
            {0,0,0,0,2,0,0,1,0,0,0,0,1,0,0,2,0,0,0,0},
            {1,1,1,1,2,1,0,1,1,1,1,1,1,0,1,2,1,1,1,1},
            {1,1,1,1,2,1,0,0,0,0,0,0,0,0,1,2,1,1,1,1},
            {1,1,1,1,2,1,0,1,1,1,1,1,1,0,1,2,1,1,1,1},
            {1,2,2,2,2,2,2,2,2,1,1,2,2,2,2,2,2,2,2,1},
            {1,2,1,1,2,1,1,1,2,1,1,2,1,1,1,2,1,1,2,1},
            {1,2,2,1,2,2,2,2,2,2,2,2,2,2,2,2,1,2,2,1},
            {1,1,2,1,2,1,2,1,1,1,1,1,1,2,1,2,1,2,1,1},
            {1,2,2,2,2,1,2,2,2,1,1,2,2,2,1,2,2,2,2,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
        };
        
        // Nivel 2 - Diseño intermedio
        int[][] level2 = {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1},
            {1,2,1,1,1,2,1,1,2,1,1,2,1,1,2,1,1,1,2,1},
            {1,2,1,0,0,2,1,1,2,1,1,2,1,1,2,0,0,1,2,1},
            {1,2,1,1,1,2,2,2,2,2,2,2,2,2,2,1,1,1,2,1},
            {1,2,2,2,2,2,1,1,1,1,1,1,1,1,2,2,2,2,2,1},
            {1,2,1,1,1,2,2,2,2,1,1,2,2,2,2,1,1,1,2,1},
            {1,2,1,1,1,1,1,1,2,1,1,2,1,1,1,1,1,1,2,1},
            {1,2,2,2,2,2,2,2,2,1,1,2,2,2,2,2,2,2,2,1},
            {1,1,1,2,1,1,1,1,0,0,0,0,1,1,1,2,1,1,1,1},
            {1,1,1,2,1,0,0,0,0,0,0,0,0,0,1,2,1,1,1,1},
            {1,2,2,2,2,2,2,2,2,1,1,2,2,2,2,2,2,2,2,1},
            {1,2,1,1,1,1,1,1,2,1,1,2,1,1,1,1,1,1,2,1},
            {1,2,1,1,1,2,2,2,2,1,1,2,2,2,2,1,1,1,2,1},
            {1,2,2,2,2,2,1,1,1,1,1,1,1,1,2,2,2,2,2,1},
            {1,2,1,1,1,2,2,2,2,2,2,2,2,2,2,1,1,1,2,1},
            {1,2,1,0,0,2,1,1,2,1,1,2,1,1,2,0,0,1,2,1},
            {1,2,1,1,1,2,1,1,2,1,1,2,1,1,2,1,1,1,2,1},
            {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
        };
        
        // Nivel 3 - Diseño avanzado
        int[][] level3 = {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,2,2,2,2,2,2,2,2,1,1,2,2,2,2,2,2,2,2,1},
            {1,2,1,1,1,1,1,1,2,1,1,2,1,1,1,1,1,1,2,1},
            {1,2,1,0,0,0,0,1,2,1,1,2,1,0,0,0,0,1,2,1},
            {1,2,1,0,1,1,0,1,2,1,1,2,1,0,1,1,0,1,2,1},
            {1,2,1,0,1,1,0,1,2,2,2,2,1,0,1,1,0,1,2,1},
            {1,2,2,2,1,1,2,2,2,1,1,2,2,2,1,1,2,2,2,1},
            {1,1,1,2,1,1,2,1,1,1,1,1,1,2,1,1,2,1,1,1},
            {0,0,1,2,2,2,2,1,0,0,0,0,1,2,2,2,2,1,0,0},
            {1,1,1,2,1,1,2,1,0,0,0,0,1,2,1,1,2,1,1,1},
            {0,0,0,2,1,1,2,1,0,0,0,0,1,2,1,1,2,0,0,0},
            {1,1,1,2,1,1,2,1,1,1,1,1,1,2,1,1,2,1,1,1},
            {1,2,2,2,1,1,2,2,2,1,1,2,2,2,1,1,2,2,2,1},
            {1,2,1,0,1,1,0,1,2,2,2,2,1,0,1,1,0,1,2,1},
            {1,2,1,0,1,1,0,1,2,1,1,2,1,0,1,1,0,1,2,1},
            {1,2,1,0,0,0,0,1,2,1,1,2,1,0,0,0,0,1,2,1},
            {1,2,1,1,1,1,1,1,2,1,1,2,1,1,1,1,1,1,2,1},
            {1,2,2,2,2,2,2,2,2,1,1,2,2,2,2,2,2,2,2,1},
            {1,2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
        };
        
        levels[0] = level1;
        levels[1] = level2;
        levels[2] = level3;
    }
    
    private void loadLevel(int levelIndex) {
        currentLevel = levelIndex;
        int[][] level = levels[levelIndex];
        points = new boolean[20][20];
        totalPoints = 0;
        collectedPoints = 0;
        gameWon = false;
        
        // Inicializar puntos basados en el nivel
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                if (level[i][j] == POINT) {
                    points[i][j] = true;
                    totalPoints++;
                } else {
                    points[i][j] = false;
                }
            }
        }
        
        // Posicionar Pacman en un lugar seguro del nivel
        pacman = new Pacman(blockSize * 1, blockSize * 1, this);
        
        // Posicionar fantasmas
        ghosts = new Ghost[] {
            new Ghost(blockSize * 9, blockSize * 9, Color.RED, this),
            new Ghost(blockSize * 10, blockSize * 9, Color.PINK, this),
            new Ghost(blockSize * 9, blockSize * 10, Color.CYAN, this)
        };
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        pacman.draw(g);
        for (Ghost ghost : ghosts) {
            ghost.draw(g);
        }
        
        if (gameWon) {
            g.setColor(Color.GREEN);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("¡NIVEL COMPLETADO!", 50, 200);
        }
    }

    private void drawBoard(Graphics g) {
        int[][] level = levels[currentLevel];
        
        // Dibujar paredes y puntos
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                int x = j * blockSize;
                int y = i * blockSize;
                
                if (level[i][j] == WALL) {
                    // Dibujar pared
                    g.setColor(Color.BLUE);
                    g.fillRect(x, y, blockSize, blockSize);
                    g.setColor(Color.CYAN);
                    g.drawRect(x, y, blockSize, blockSize);
                } else if (points[i][j]) {
                    // Dibujar punto
                    g.setColor(Color.WHITE);
                    g.fillOval(x + blockSize/2 - 2, y + blockSize/2 - 2, 4, 4);
                }
            }
        }
        
        // Dibujar información
        g.setColor(Color.YELLOW);
        g.drawString("Score: " + pacman.getScore() + " | Nivel: " + (currentLevel + 1) + " | Puntos: " + collectedPoints + "/" + totalPoints, 10, 410);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameWon) {
            pacman.move();
            for (Ghost ghost : ghosts) {
                ghost.move();
            }
            checkCollisions();
        }
        repaint();
    }
    
    private void checkCollisions() {
        // Verificar colisión con puntos
        int pacRow = pacman.getY() / blockSize;
        int pacCol = pacman.getX() / blockSize;
        
        if (pacRow >= 0 && pacRow < 20 && pacCol >= 0 && pacCol < 20) {
            if (points[pacRow][pacCol]) {
                points[pacRow][pacCol] = false;
                pacman.addScore(10);
                collectedPoints++;
                
                // Verificar si completó el nivel
                if (collectedPoints >= totalPoints) {
                    gameWon = true;
                    timer.stop();
                    
                    // Después de 2 segundos, cargar siguiente nivel
                    Timer nextLevelTimer = new Timer(2000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (currentLevel < 2) {
                                loadLevel(currentLevel + 1);
                                timer.start();
                            } else {
                                // Juego completado
                                JOptionPane.showMessageDialog(Board.this, 
                                    "¡Felicidades! Has completado todos los niveles.\nPuntuación final: " + pacman.getScore());
                            }
                            ((Timer)e.getSource()).stop();
                        }
                    });
                    nextLevelTimer.setRepeats(false);
                    nextLevelTimer.start();
                }
            }
        }
        
        // Verificar colisión con fantasmas
        for (Ghost ghost : ghosts) {
            if (Math.abs(pacman.getX() - ghost.getX()) < blockSize && 
                Math.abs(pacman.getY() - ghost.getY()) < blockSize) {
                timer.stop();
                JOptionPane.showMessageDialog(this, 
                    "¡Game Over! Un fantasma te atrapó.\nPuntuación: " + pacman.getScore());
                System.exit(0);
            }
        }
    }
    
    public boolean isWall(int x, int y) {
        int row = y / blockSize;
        int col = x / blockSize;
        
        // Verificar límites verticales
        if (row < 0 || row >= 20) {
            return true;
        }
        
        // Si está fuera de los límites horizontales, verificar si es un túnel
        if (col < 0 || col >= 20) {
            // Permitir movimiento fuera de límites en las filas de túnel
            // Verificar si la fila actual tiene apertura en los bordes
            int[][] level = levels[currentLevel];
            if (col < 0) {
                // Verificando lado izquierdo - columna 0
                return level[row][0] == WALL;
            } else {
                // Verificando lado derecho - columna 19
                return level[row][19] == WALL;
            }
        }
        
        return levels[currentLevel][row][col] == WALL;
    }
    
    public int getBlockSize() {
        return blockSize;
    }
    
    public int getBoardWidth() {
        return 20 * blockSize;
    }
    
    public int getBoardHeight() {
        return 20 * blockSize;
    }

    private class PacmanKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            pacman.keyPressed(e);
        }
    }
}