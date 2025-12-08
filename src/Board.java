import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Board extends JPanel implements ActionListener {
    private static final int RESPAWN_DELAY_MS = 1000;
    private static final int STATUS_PANEL_HEIGHT = 40;
    private static final Color GHOST_HOUSE_FILL = new Color(100, 50, 150);
    private static final Color GHOST_HOUSE_BORDER = new Color(150, 100, 200);
    
    private Timer timer;
    private Pacman pacman;
    private Ghost[] ghosts;
    private int currentLevel = 0;
    private int blockSize = 20;
    private int[][][] levels;
    private boolean[][] points;
    private boolean[][] powerPellets;
    private int totalPoints;
    private int collectedPoints = 0;
    private boolean gameWon = false;
    private int[] ghostStartX;
    private int[] ghostStartY;
    
    // 0 = empty space, 1 = wall, 2 = point, 3 = ghost house, 4 = power pellet
    private static final int EMPTY = 0;
    private static final int WALL = 1;
    private static final int POINT = 2;
    private static final int GHOST_HOUSE = 3;
    private static final int POWER_PELLET = 4;

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
            {1,4,2,2,2,2,2,2,2,1,1,2,2,2,2,2,2,2,4,1},
            {1,2,1,1,2,1,1,1,2,1,1,2,1,1,1,2,1,1,2,1},
            {1,2,1,1,2,1,1,1,2,1,1,2,1,1,1,2,1,1,2,1},
            {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1},
            {1,2,1,1,2,1,2,1,1,1,1,1,1,2,1,2,1,1,2,1},
            {1,2,2,2,2,1,2,2,2,1,1,2,2,2,1,2,2,2,2,1},
            {1,1,1,1,2,1,1,1,0,1,1,0,1,1,1,2,1,1,1,1},
            {1,1,1,1,2,1,0,0,0,0,0,0,0,0,1,2,1,1,1,1},
            {1,1,1,1,2,1,0,1,1,3,3,1,1,0,1,2,1,1,1,1},
            {0,0,0,0,2,0,0,1,3,3,3,3,1,0,0,2,0,0,0,0},
            {1,1,1,1,2,1,0,1,1,1,1,1,1,0,1,2,1,1,1,1},
            {1,1,1,1,2,1,0,0,0,0,0,0,0,0,1,2,1,1,1,1},
            {1,1,1,1,2,1,0,1,1,1,1,1,1,0,1,2,1,1,1,1},
            {1,2,2,2,2,2,2,2,2,1,1,2,2,2,2,2,2,2,2,1},
            {1,2,1,1,2,1,1,1,2,1,1,2,1,1,1,2,1,1,2,1},
            {1,2,2,1,2,2,2,2,2,2,2,2,2,2,2,2,1,2,2,1},
            {1,1,2,1,2,1,2,1,1,1,1,1,1,2,1,2,1,2,1,1},
            {1,4,2,2,2,1,2,2,2,1,1,2,2,2,1,2,2,2,4,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
        };
        
        // Nivel 2 - Diseño intermedio
        int[][] level2 = {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,4,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,4,1},
            {1,2,1,1,1,2,1,1,2,1,1,2,1,1,2,1,1,1,2,1},
            {1,2,1,0,0,2,1,1,2,1,1,2,1,1,2,0,0,1,2,1},
            {1,2,1,1,1,2,2,2,2,2,2,2,2,2,2,1,1,1,2,1},
            {1,2,2,2,2,2,1,1,1,1,1,1,1,1,2,2,2,2,2,1},
            {1,2,1,1,1,2,2,2,2,1,1,2,2,2,2,1,1,1,2,1},
            {1,2,1,1,1,1,1,1,2,1,1,2,1,1,1,1,1,1,2,1},
            {1,2,2,2,2,2,2,2,2,1,1,2,2,2,2,2,2,2,2,1},
            {1,1,1,2,1,1,1,1,0,3,3,0,1,1,1,2,1,1,1,1},
            {1,1,1,2,1,0,0,0,3,3,3,3,0,0,1,2,1,1,1,1},
            {1,2,2,2,2,2,2,2,2,1,1,2,2,2,2,2,2,2,2,1},
            {1,2,1,1,1,1,1,1,2,1,1,2,1,1,1,1,1,1,2,1},
            {1,2,1,1,1,2,2,2,2,1,1,2,2,2,2,1,1,1,2,1},
            {1,2,2,2,2,2,1,1,1,1,1,1,1,1,2,2,2,2,2,1},
            {1,2,1,1,1,2,2,2,2,2,2,2,2,2,2,1,1,1,2,1},
            {1,2,1,0,0,2,1,1,2,1,1,2,1,1,2,0,0,1,2,1},
            {1,2,1,1,1,2,1,1,2,1,1,2,1,1,2,1,1,1,2,1},
            {1,4,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,4,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
        };
        
        // Nivel 3 - Diseño avanzado
        int[][] level3 = {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,4,2,2,2,2,2,2,2,1,1,2,2,2,2,2,2,2,4,1},
            {1,2,1,1,1,1,1,1,2,1,1,2,1,1,1,1,1,1,2,1},
            {1,2,1,0,0,0,0,1,2,1,1,2,1,0,0,0,0,1,2,1},
            {1,2,1,0,1,1,0,1,2,1,1,2,1,0,1,1,0,1,2,1},
            {1,2,1,0,1,1,0,1,2,2,2,2,1,0,1,1,0,1,2,1},
            {1,2,2,2,1,1,2,2,2,1,1,2,2,2,1,1,2,2,2,1},
            {1,1,1,2,1,1,2,1,1,1,1,1,1,2,1,1,2,1,1,1},
            {0,0,1,2,2,2,2,1,0,3,3,0,1,2,2,2,2,1,0,0},
            {1,1,1,2,1,1,2,1,0,3,3,0,1,2,1,1,2,1,1,1},
            {0,0,0,2,1,1,2,1,0,3,3,0,1,2,1,1,2,0,0,0},
            {1,1,1,2,1,1,2,1,1,1,1,1,1,2,1,1,2,1,1,1},
            {1,2,2,2,1,1,2,2,2,1,1,2,2,2,1,1,2,2,2,1},
            {1,2,1,0,1,1,0,1,2,2,2,2,1,0,1,1,0,1,2,1},
            {1,2,1,0,1,1,0,1,2,1,1,2,1,0,1,1,0,1,2,1},
            {1,2,1,0,0,0,0,1,2,1,1,2,1,0,0,0,0,1,2,1},
            {1,2,1,1,1,1,1,1,2,1,1,2,1,1,1,1,1,1,2,1},
            {1,2,2,2,2,2,2,2,2,1,1,2,2,2,2,2,2,2,2,1},
            {1,4,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,4,1},
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
        powerPellets = new boolean[20][20];
        totalPoints = 0;
        collectedPoints = 0;
        gameWon = false;
        
        // Inicializar puntos y power pellets basados en el nivel
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                if (level[i][j] == POINT) {
                    points[i][j] = true;
                    totalPoints++;
                } else {
                    points[i][j] = false;
                }
                
                if (level[i][j] == POWER_PELLET) {
                    powerPellets[i][j] = true;
                    totalPoints++;
                } else {
                    powerPellets[i][j] = false;
                }
            }
        }
        
        // Posicionar Pacman en un lugar seguro del nivel
        pacman = new Pacman(blockSize * 1, blockSize * 1, this);
        
        // Posicionar fantasmas
        ghostStartX = new int[] {blockSize * 9, blockSize * 10, blockSize * 9};
        ghostStartY = new int[] {blockSize * 9, blockSize * 9, blockSize * 10};
        ghosts = new Ghost[] {
            new Ghost(ghostStartX[0], ghostStartY[0], Color.RED, this),
            new Ghost(ghostStartX[1], ghostStartY[1], Color.PINK, this),
            new Ghost(ghostStartX[2], ghostStartY[2], Color.CYAN, this)
        };
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawStatusPanel(g);
        drawBoard(g);
        pacman.draw(g, STATUS_PANEL_HEIGHT);
        for (Ghost ghost : ghosts) {
            ghost.draw(g, STATUS_PANEL_HEIGHT);
        }
        
        if (gameWon) {
            g.setColor(Color.GREEN);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("¡NIVEL COMPLETADO!", 50, 200 + STATUS_PANEL_HEIGHT);
        }
    }
    
    private void drawStatusPanel(Graphics g) {
        // Draw status panel background
        g.setColor(new Color(20, 20, 20));
        g.fillRect(0, 0, getBoardWidth(), STATUS_PANEL_HEIGHT);
        
        // Draw border
        g.setColor(Color.CYAN);
        g.drawRect(0, 0, getBoardWidth() - 1, STATUS_PANEL_HEIGHT - 1);
        
        // Draw lives
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Vidas: " + pacman.getLives(), 10, 25);
        
        // Draw score
        g.drawString("Puntos: " + pacman.getScore(), 120, 25);
        
        // Draw level info
        g.drawString("Nivel: " + (currentLevel + 1), 250, 25);
        
        // Draw power-up timer if active
        if (pacman.isPoweredUp()) {
            long remaining = pacman.getRemainingPowerUpTime();
            g.setColor(Color.GREEN);
            g.drawString("Power: " + (remaining / 1000) + "s", 320, 25);
        }
    }

    private void drawBoard(Graphics g) {
        int[][] level = levels[currentLevel];
        
        // Dibujar paredes y puntos (offset by STATUS_PANEL_HEIGHT)
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                int x = j * blockSize;
                int y = i * blockSize + STATUS_PANEL_HEIGHT;
                
                if (level[i][j] == WALL) {
                    // Dibujar pared
                    g.setColor(Color.BLUE);
                    g.fillRect(x, y, blockSize, blockSize);
                    g.setColor(Color.CYAN);
                    g.drawRect(x, y, blockSize, blockSize);
                } else if (level[i][j] == GHOST_HOUSE) {
                    // Draw ghost house with special walls
                    g.setColor(GHOST_HOUSE_FILL);
                    g.fillRect(x, y, blockSize, blockSize);
                    g.setColor(GHOST_HOUSE_BORDER);
                    g.drawRect(x, y, blockSize, blockSize);
                } else if (points[i][j]) {
                    // Dibujar punto
                    g.setColor(Color.WHITE);
                    g.fillOval(x + blockSize/2 - 2, y + blockSize/2 - 2, 4, 4);
                } else if (powerPellets[i][j]) {
                    // Dibujar power pellet (más grande y brillante)
                    g.setColor(Color.WHITE);
                    g.fillOval(x + blockSize/2 - 5, y + blockSize/2 - 5, 10, 10);
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameWon) {
            pacman.move();
            for (Ghost ghost : ghosts) {
                ghost.move();
            }
            
            // Update ghost frightened state based on power-up
            if (!pacman.isPoweredUp()) {
                for (Ghost ghost : ghosts) {
                    ghost.setFrightened(false);
                }
            }
            
            checkCollisions();
        }
        repaint();
    }
    
    private void checkLevelCompletion() {
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
    
    private void checkCollisions() {
        // Verificar colisión con puntos
        int pacRow = pacman.getY() / blockSize;
        int pacCol = pacman.getX() / blockSize;
        
        if (pacRow >= 0 && pacRow < 20 && pacCol >= 0 && pacCol < 20) {
            if (points[pacRow][pacCol]) {
                points[pacRow][pacCol] = false;
                pacman.addScore(10);
                collectedPoints++;
                checkLevelCompletion();
            }
            
            // Verificar colisión con power pellets
            if (powerPellets[pacRow][pacCol]) {
                powerPellets[pacRow][pacCol] = false;
                pacman.addScore(50);
                collectedPoints++;
                
                // Activar power-up
                pacman.activatePowerUp();
                for (Ghost ghost : ghosts) {
                    ghost.setFrightened(true);
                }
                
                checkLevelCompletion();
            }
        }
        
        // Verificar colisión con fantasmas
        int characterSize = Pacman.getCharacterSize();
        for (Ghost ghost : ghosts) {
            if (Math.abs(pacman.getX() - ghost.getX()) < characterSize && 
                Math.abs(pacman.getY() - ghost.getY()) < characterSize) {
                if (ghost.isFrightened()) {
                    // Pacman come al fantasma cuando está asustado
                    pacman.addScore(200);
                    ghost.sendToStart();
                } else if (!ghost.isReturning()) {
                    // Fantasma atrapa a Pacman (solo si no está regresando al inicio)
                    handlePacmanCaught();
                    break;
                }
            }
        }
    }
    
    private void handlePacmanCaught() {
        pacman.loseLife();
        
        if (pacman.getLives() <= 0) {
            // Game Over
            timer.stop();
            JOptionPane.showMessageDialog(this, 
                "¡Game Over! Un fantasma te atrapó.\nPuntuación: " + pacman.getScore());
            System.exit(0);
        } else {
            // Resetear posiciones pero mantener el progreso
            timer.stop();
            pacman.resetPosition();
            for (int i = 0; i < ghosts.length; i++) {
                ghosts[i].resetPosition(ghostStartX[i], ghostStartY[i]);
                // Reset ghost states when respawning
                ghosts[i].setFrightened(false);
            }
            
            // Pausar brevemente antes de continuar
            Timer respawnTimer = new Timer(RESPAWN_DELAY_MS, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    timer.start();
                    ((Timer)e.getSource()).stop();
                }
            });
            respawnTimer.setRepeats(false);
            respawnTimer.start();
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
    
    public boolean isWallForPacman(int x, int y) {
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
        
        int cellValue = levels[currentLevel][row][col];
        // Pacman cannot enter walls or ghost house
        return cellValue == WALL || cellValue == GHOST_HOUSE;
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