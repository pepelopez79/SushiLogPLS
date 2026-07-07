# 🍣 Sushi Log

Aplicación Android nativa para registrar y analizar tu consumo de sushi en restaurantes de buffet libre.

---

## Descripción general

**Sushi Log** es una app diseñada para los amantes del sushi que quieren llevar un registro completo de sus sesiones. Cuenta piezas, analiza estadísticas nutricionales, desbloquea logros y comparte tus récords. Toda la lógica corre en local (sin backend ni conexión a internet), con persistencia en el propio dispositivo.

---

## Funcionalidades

- 🔢 **Contador rápido** — Tap para sumar, long-press para restar piezas
- 📊 **Estadísticas detalladas** — Filtros por semana, mes, año y total con curiosidades nutricionales
- 🏆 **20 logros desbloqueables** — Con notificaciones animadas y sonido
- 🎨 **3 temas visuales** — Dark, Light y Salmon
- 🌍 **4 idiomas** — Español, English, Français, Italiano (detección automática)
- 🍱 **Piezas personalizadas** — Crea hasta 12 tipos propios con kcal, arroz y pescado
- 📤 **Compartir sesiones** — Genera imagen nativa para redes sociales
- 📋 **Historial expandible** — Con chips de kcal, arroz y pescado por sesión
- 🎯 **70+ iconos PNG personalizados** — Sin Material Icons, estética propia y consistente

---

## Pantallas

| Pantalla | Descripción |
|---|---|
| **Splash** | Logo animado con fade+scale |
| **Home** | Menú principal con logo, título y acceso a todas las secciones |
| **Counter** | 3 fases: restaurante → conteo con cuadrícula → resumen y compartir |
| **History** | Lista cronológica expandible con chips nutricionales y opción de borrar |
| **Stats** | Piezas totales, promedio, récord, desglose por tipo y curiosidades |
| **Achievements** | 20 logros en 5 categorías con barras de progreso |
| **Settings** | Tema, idioma, piezas personalizadas, borrado de datos y contacto |
| **Custom Pieces** | Crear/editar/eliminar piezas con nombre, icono, kcal, arroz y pescado |

---

## Piezas integradas

| Pieza | Kcal | Pescado | Arroz |
|---|---|---|---|
| Nigiri | 50 | 1 corte | 10g |
| Sashimi | 35 | 1 corte | 0g |
| Maki | 40 | — | 15g |
| Onigiri | 120 | — | 80g |
| Uramaki | 45 | — | 15g |
| Gunkan | 60 | — | 15g |
| Temaki | 100 | — | 30g |
| Gyoza | 45 | — | — |
| Tempura | 60 | — | — |
| Edamame | 15 | — | — |
| Takoyaki | 55 | — | — |

Más hasta **12 piezas personalizables** por el usuario.

---

## Stack tecnológico

| Tecnología | Uso |
|---|---|
| **Kotlin** | Lenguaje principal |
| **Jetpack Compose** | UI declarativa |
| **Material3** | Sistema de diseño y componentes |
| **Navigation Compose** | Navegación entre pantallas con transiciones animadas |
| **Gson + SharedPreferences** | Persistencia local de sesiones, ajustes y logros |
| **Canvas/Bitmap** | Generación nativa de imágenes para compartir |
| **FileProvider** | Compartir imágenes generadas vía Intent |
| **MutableSharedFlow** | Notificaciones globales de logros desbloqueados |
| **RingtoneManager** | Sonido de notificación al desbloquear logros |
| **JUnit + Robolectric + Espresso** | Tests unitarios y de instrumentación |
| **Gradle (Kotlin DSL) + Version Catalog** | Build system y gestión de dependencias (`libs.versions.toml`) |

### Requisitos

- Android Studio Ladybug (2024.2) o superior
- JDK 17
- Gradle 8.13+ (incluido via `gradlew`)
- Min SDK: 24 (Android 7.0)
- Target SDK / Compile SDK: 36

---

## Arquitectura

El proyecto sigue una arquitectura simple por capas, sin backend, orientada a Compose:

- **`ui/screens`** — Pantallas (composables) de cada sección de la app. Contienen el estado local y la lógica de presentación.
- **`ui/components`** — Componentes reutilizables (botones de pieza, iconos, etc.) usados por varias pantallas.
- **`ui/navigation`** — Grafo de navegación (`NavGraph`) que define rutas y transiciones entre pantallas.
- **`ui/theme`** — Temas Material3, paletas de color y tipografía.
- **`data`** — Modelos de dominio (`SessionRecord`, `SushiPiece`, `Achievement`) y managers de lógica/persistencia (`SessionStorage`, `AchievementManager`, `AppSettings`) que actúan como fuente de verdad, leyendo/escribiendo en `SharedPreferences` mediante `Gson`.

Flujo de datos: las pantallas leen y mutan el estado a través de los *managers* de `data/`, que persisten los cambios de forma síncrona en `SharedPreferences`. Los logros desbloqueados se comunican de forma global a la UI (p. ej. banners de notificación) mediante un `MutableSharedFlow` expuesto desde `AchievementManager` y observado en `MainActivity`.

---

## Estructura del proyecto

```
app/src/main/java/pls/dev/sushilog/
├── MainActivity.kt                    # Activity principal y notificador de logros
├── data/
│   ├── Achievement.kt                 # Modelo de logros y requisitos
│   ├── AchievementManager.kt          # Lógica de progreso y desbloqueo
│   ├── AppSettings.kt                 # Temas, idiomas, strings i18n, piezas custom
│   ├── SessionRecord.kt               # Modelo de sesión guardada
│   ├── SessionStorage.kt              # Persistencia y estadísticas
│   └── SushiPiece.kt                  # Catálogo de piezas y funciones nutricionales
├── ui/
│   ├── components/
│   │   ├── PieceCounterItem.kt        # Botón de pieza en el contador
│   │   ├── CustomPieceCounterItem.kt  # Botón de pieza personalizada
│   │   └── SushiIcon.kt               # Componente de icono con glow en dark mode
│   ├── navigation/
│   │   └── NavGraph.kt                # Rutas y transiciones
│   ├── screens/
│   │   ├── SplashScreen.kt            # Pantalla de bienvenida animada
│   │   ├── HomeScreen.kt              # Pantalla principal
│   │   ├── CounterScreen.kt           # Contador de piezas (3 fases)
│   │   ├── HistoryScreen.kt           # Historial de sesiones
│   │   ├── StatsScreen.kt             # Estadísticas y curiosidades
│   │   ├── AchievementsScreen.kt      # Visor de logros
│   │   ├── SettingsScreen.kt          # Configuración de la app
│   │   ├── CustomPiecesScreen.kt      # Gestión de piezas personalizadas
│   │   └── ShareUtils.kt              # Generación de imagen para compartir
│   └── theme/
│       ├── Color.kt                   # Constantes de color
│       ├── Theme.kt                   # Tema Material3
│       ├── ThemeColors.kt             # Paletas por tema (Dark/Light/Salmon)
│       └── Type.kt                    # Tipografía
└── res/drawable/                      # 70 iconos PNG personalizados
```

---

## Assets

Todos los iconos son **PNG personalizados** sin dependencia de Material Icons:

- **Piezas de sushi**: nigiri, sashimi, maki, uramaki, gunkan, temaki, gyoza, edamame, takoyaki, onigiri, etc.
- **UI**: back, right, up, down, share, delete, add, settings, info, email, devby
- **Estadísticas**: history, stats, all, kcal, rice, salmon
- **Logros**: 20 iconos únicos (achievement1–achievement20)
- **Banderas**: spanish, english, french, italian
- **Otros**: logo, start, calendar, bowl, bowl2, bowl3

---

## Paleta de colores

### Dark (por defecto)
| Color | Hex | Uso |
|---|---|---|
| Background | `#1B2838` | Fondo principal |
| Surface | `#2A3A4A` | Tarjetas |
| Primary | `#4ECDC4` | Acentos, botones |
| Border | `#5A6A7A` | Bordes |
| Foreground | `#FFFFFF` | Texto principal |

### Light
| Color | Hex | Uso |
|---|---|---|
| Background | `#F8F9FA` | Fondo principal |
| Surface | `#FFFFFF` | Tarjetas |
| Primary | `#2B9D94` | Acentos |

### Salmon
| Color | Hex | Uso |
|---|---|---|
| Background | `#FFF5F0` | Fondo principal |
| Surface | `#FFFFFF` | Tarjetas |
| Primary | `#E8734A` | Acentos |

---

## Tests

Ejecutados con `./gradlew testDebugUnitTest` (ver sección [Instalación y ejecución](#instalación-y-ejecución)):

| Test | Descripción |
|---|---|
| `CustomPieceTest` | Valores nutricionales de piezas personalizadas |
| `StatsLogicTest` | Cálculos de kcal, arroz y pescado |
| `SettingsTest` | Enums de temas, idiomas y strings i18n |
| `AchievementTest` | IDs únicos, progreso y cobertura de strings |

---

## Instalación y ejecución

### Clonar el repositorio

```bash
git clone https://github.com/pepelopez79/SushiLogPLS.git
cd SushiLogPLS
```

### Opción A: Android Studio

1. Abrir el proyecto en Android Studio (Ladybug o superior).
2. Esperar el *Sync* automático de Gradle.
3. Seleccionar un dispositivo/emulador con API 24+.
4. Pulsar **Run ▶** para compilar e instalar la app.

### Opción B: Línea de comandos (Gradle Wrapper)

```bash
# Dar permisos de ejecución (solo macOS/Linux)
chmod +x gradlew

# Compilar el proyecto
./gradlew build

# Generar un APK de debug
./gradlew assembleDebug

# Instalar el APK en un dispositivo/emulador conectado (adb)
./gradlew installDebug

# Ejecutar los tests unitarios
./gradlew testDebugUnitTest
```

El APK generado se encuentra en `app/build/outputs/apk/debug/`.

---

## Contacto

- **Desarrollador**: CodeByPLS
- **Email**: codebypls+sushilog@gmail.com

---

## Licencia

Este proyecto es de uso personal y educativo.

---

**Pepe López** — 2026
