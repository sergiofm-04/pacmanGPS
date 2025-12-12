# PacmanGPS 🎮

![Test Coverage](https://img.shields.io/badge/coverage-97%25-brightgreen)
![Tests](https://img.shields.io/badge/tests-167%20passing-success)
![Java](https://img.shields.io/badge/Java-17-blue)
![Maven](https://img.shields.io/badge/Maven-3.8%2B-orange)

Juego clásico de Pac-Man desarrollado en Java utilizando Swing para la interfaz gráfica, con soporte de sonido y una arquitectura modular bien testeada.

## 🎯 Características del Juego

- **Laberinto dinámico**: Tablero de juego con paredes, pasillos y puntos coleccionables
- **Control del jugador**: Movimiento fluido de Pac-Man mediante teclas de dirección
- **Inteligencia artificial**: Fantasmas con movimiento automático y comportamiento variado
- **Sistema de puntuación**: Recolección de puntos que incrementan el puntaje del jugador
- **Efectos de sonido**: Gestión de audio con soporte para múltiples sonidos simultáneos
- **Condiciones de juego**:
  - ✅ Victoria: Recolectar todos los puntos del laberinto
  - ❌ Derrota: Ser capturado por un fantasma
- **Interfaz gráfica**: UI sencilla, modular y responsive construida con Java Swing
- **Arquitectura limpia**: Código organizado en clases especializadas y bien documentadas

## 📁 Estructura de Archivos

```
pacmanGPS/
├── src/
│   ├── main/java/                # Código fuente principal
│   │   ├── Game.java             # Clase principal: inicializa la ventana y el juego
│   │   ├── Board.java            # Lógica y renderizado del tablero de juego
│   │   ├── Pacman.java           # Lógica, movimiento y renderizado de Pac-Man
│   │   ├── Ghost.java            # Lógica, IA y renderizado de los fantasmas
│   │   ├── Direction.java        # Enum para las direcciones de movimiento (UP, DOWN, LEFT, RIGHT)
│   │   └── SoundManager.java     # Gestor de efectos de sonido y audio del juego
│   └── test/java/                # Suite de tests unitarios (167 tests, 97% cobertura)
│       ├── GameTest.java         # Tests de la clase Game
│       ├── BoardTest.java        # Tests del tablero y lógica del juego
│       ├── PacmanTest.java       # Tests de Pac-Man y sus movimientos
│       ├── GhostTest.java        # Tests de los fantasmas y su comportamiento
│       ├── DirectionTest.java    # Tests del enum Direction
│       └── SoundManagerTest.java # Tests del sistema de audio
├── .github/
│   └── workflows/                # GitHub Actions para CI/CD
│       ├── ci-ct.yml             # Integración y pruebas continuas
│       └── cd.yml                # Despliegue continuo a GitHub Pages
├── pom.xml                       # Configuración de Maven y dependencias
├── README.md                     # Este archivo
└── TEST_COVERAGE.md              # Documentación detallada de cobertura de tests
```

## 💻 Requisitos

- **Java**: JDK 17 o superior
- **Maven**: 3.8+ (para gestión de dependencias, compilación y tests)
- **Sistema Operativo**: Windows, macOS o Linux
- **IDE** (Opcional): IntelliJ IDEA, Eclipse, Visual Studio Code, o NetBeans

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

## 🧪 Tests

Este proyecto incluye una suite completa de **167 tests unitarios** con **97% de cobertura de código**.

```bash
# Ejecutar todos los tests
mvn test

# Generar reporte de cobertura con JaCoCo
mvn test jacoco:report

# Ver reporte HTML de cobertura
open target/site/jacoco/index.html   # macOS
xdg-open target/site/jacoco/index.html  # Linux
start target/site/jacoco/index.html     # Windows
```

Para más detalles sobre los tests y cobertura, consulta [TEST_COVERAGE.md](TEST_COVERAGE.md).

## 🔄 Integración y Prueba Continua (CI/CT)

Este proyecto utiliza **GitHub Actions** para automatizar el proceso de integración y pruebas continuas.

### Workflow: `CI/CT` (`.github/workflows/ci-ct.yml`)

**Activación**: Se ejecuta automáticamente en cada `push` o `pull_request` a las ramas `main` y `develop`.

**Pasos del proceso**:

1. ✅ **Checkout del código**: Clona el repositorio
2. ⚙️ **Configuración**: Instala JDK 17 y configura caché de Maven
3. 🔨 **Compilación**: Ejecuta `mvn clean install -DskipTests`
4. 🧪 **Pruebas**: Ejecuta todos los tests con `mvn test`
5. 📊 **Reporte de cobertura**: Genera reporte JaCoCo con análisis de cobertura
6. 📦 **Artefactos**: Publica reportes de cobertura y resultados (disponibles 30 días)
7. 💬 **Notificaciones**: Reporta el estado directamente en commits y PRs

**Características especiales**:
- Concurrencia controlada: Cancela ejecuciones duplicadas en PRs
- Artefactos descargables: Reportes de cobertura y resultados de tests
- Estado visible: Checks y badges en GitHub

## 🚀 Despliegue Continuo (CD)

El proyecto implementa **despliegue automático a GitHub Pages** con una landing page para descarga del JAR.

### Workflow: `CD - Deploy to GitHub Pages` (`.github/workflows/cd.yml`)

**Activación**: Se ejecuta automáticamente después de que el workflow `CI/CT` se completa **exitosamente** en la rama `main`.

**Pasos del proceso**:

**Job Build**:
1. 📦 **Empaquetado**: Compila y genera el JAR con `mvn clean package -DskipTests`
2. 🎨 **Landing page**: Crea página HTML responsive con información del proyecto
3. ⬆️ **Preparación**: Sube JAR y página como artefactos

**Job Deploy**:
1. ⚙️ **Configuración**: Configura GitHub Pages
2. 🌐 **Despliegue**: Publica el sitio en GitHub Pages
3. 📢 **Notificación**: Reporta la URL de la landing page

**Resultado**: 

🌐 **Landing page accesible en: [https://sergiofm-04.github.io/pacmanGPS/](https://sergiofm-04.github.io/pacmanGPS/)**

Desde la landing page puedes:
- 📥 Descargar el último JAR compilado
- 📋 Ver requisitos y instrucciones de ejecución
- 🔗 Acceder al repositorio de GitHub

## 🎮 Controles

- **⬅️ Flecha Izquierda**: Mover Pac-Man hacia la izquierda
- **➡️ Flecha Derecha**: Mover Pac-Man hacia la derecha
- **⬆️ Flecha Arriba**: Mover Pac-Man hacia arriba
- **⬇️ Flecha Abajo**: Mover Pac-Man hacia abajo

## Extensiones Futuras

- Mejorar la IA de los fantasmas.
- Añadir niveles y nuevos mapas.
- Implementar efectos de sonido.
- Añadir “power-ups” y más funcionalidades clásicas del juego.

## Autor

Proyecto desarrollado por [paberlo].

---

¡Disfruta programando y jugando Pac-Man!