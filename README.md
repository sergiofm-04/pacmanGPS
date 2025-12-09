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
├── main/java/            # Código fuente principal
│   ├── Game.java         # Clase principal: inicializa la ventana y el juego
│   ├── Board.java        # Lógica y renderizado del tablero
│   ├── Pacman.java       # Lógica y renderizado de Pac-Man
│   ├── Ghost.java        # Lógica y renderizado de los fantasmas
│   ├── Direction.java    # Enum para las direcciones de movimiento
│   └── SoundManager.java # Gestor de efectos de sonido
└── test/java/            # Tests unitarios (137+ tests)
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

Este proyecto incluye una suite completa de 137 tests unitarios con 93% de cobertura de código.

```bash
# Ejecutar todos los tests
mvn test

# Generar reporte de cobertura
mvn test jacoco:report

# Ver reporte HTML
open target/site/jacoco/index.html
```

Para más detalles sobre los tests, consulta [TEST_COVERAGE.md](TEST_COVERAGE.md).

## Integración y Prueba Continua (CI/CT)

Este proyecto utiliza GitHub Actions para automatizar el proceso de integración y pruebas continuas. El workflow `CI/CT` se ejecuta automáticamente en cada push o pull request a las ramas `main` y `develop`.

### Proceso automatizado:

1. **Compilación**: Instala dependencias y compila el proyecto con `mvn clean install`
2. **Pruebas**: Ejecuta todas las pruebas unitarias con `mvn test`
3. **Cobertura**: Genera reporte de cobertura de código con JaCoCo
4. **Artefactos**: Publica reportes de cobertura y resultados de tests como artefactos del workflow
5. **Notificaciones**: Reporta el estado (éxito/fallo) directamente en el commit o pull request

Los reportes de cobertura y resultados de tests están disponibles como artefactos descargables en cada ejecución del workflow durante 30 días.

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