# PacmanProject

Juego básico de Pac-Man desarrollado en Java utilizando Swing para la interfaz gráfica.

## Características

- Laberinto donde Pac-Man y los fantasmas se mueven.
- Movimiento de Pac-Man controlado por el usuario (teclas de flecha).
- Fantasmas con movimiento automático.
- Puntos coleccionables que aumentan el puntaje.
- Condiciones de victoria (comer todos los puntos) y derrota (ser atrapado por un fantasma).
- Interfaz gráfica sencilla y modular.

## Estructura de Archivos

```
src/
├── Game.java             # Clase principal: inicializa la ventana y el juego
├── Board.java            # Lógica y renderizado del tablero
├── Pacman.java           # Lógica y renderizado de Pac-Man
├── Ghost.java            # Lógica y renderizado de los fantasmas
├── Direction.java        # Enum para las direcciones de movimiento
├── SoundManager.java     # Gestor de efectos de sonido
└── test/java/            # Tests unitarios (132+ tests, 100% cobertura)
    ├── GameTest.java
    ├── BoardTest.java
    ├── PacmanTest.java
    ├── GhostTest.java
    ├── DirectionTest.java
    └── SoundManagerTest.java
```

## Requisitos

- Java 17 o superior
- Maven 3.8+ (para gestión de dependencias y tests)
- (Opcional) IDE como IntelliJ IDEA, Eclipse, VSCode, etc.

## Compilación y Ejecución

### Con Maven (Recomendado)

```bash
# Compilar el proyecto
mvn compile

# Ejecutar el juego
mvn exec:java -Dexec.mainClass="Game"

# Compilar y empaquetar
mvn package
```

### Compilación Manual

```bash
javac -d bin src/main/java/*.java
java -cp bin Game
```

O si usas un IDE, simplemente importa el proyecto Maven y ejecuta la clase `Game`.

## Tests

Este proyecto incluye una suite completa de 132 tests unitarios con 93% de cobertura de código.

```bash
# Ejecutar todos los tests
mvn test

# Generar reporte de cobertura
mvn test jacoco:report

# Ver reporte HTML
open target/site/jacoco/index.html
```

Para más detalles sobre los tests, consulta [TEST_COVERAGE.md](TEST_COVERAGE.md).

## Controles

- **Flechas del teclado** para mover a Pac-Man: izquierda, derecha, arriba, abajo.

## Extensiones Futuras

- Mejorar la IA de los fantasmas.
- Añadir niveles y nuevos mapas.
- Implementar efectos de sonido.
- Añadir “power-ups” y más funcionalidades clásicas del juego.

## Autor

Proyecto desarrollado por [paberlo].

---

¡Disfruta programando y jugando Pac-Man!