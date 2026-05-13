# Sushi Log - Contexto Tecnico

Este documento contiene toda la informacion tecnica, arquitectura, flujos de navegacion, modelos de datos y detalles de implementacion necesarios para desarrollar y mantener la aplicacion.

---

## Indice

1. [Arquitectura General](#arquitectura-general)
2. [Navegacion y Flujos](#navegacion-y-flujos)
3. [Modelos de Datos](#modelos-de-datos)
4. [Sistema de Persistencia](#sistema-de-persistencia)
5. [Componentes UI](#componentes-ui)
6. [Interacciones y Gestos](#interacciones-y-gestos)
7. [Sistema de Temas](#sistema-de-temas)
8. [Logica de Negocio](#logica-de-negocio)
9. [Estructura de Archivos](#estructura-de-archivos)
10. [Dependencias](#dependencias)
11. [Consideraciones de UX](#consideraciones-de-ux)
12. [Posibles Mejoras Futuras](#posibles-mejoras-futuras)

---

## Arquitectura General

La aplicacion sigue una arquitectura simple basada en:

- **UI Layer**: Jetpack Compose con pantallas (`Screen`) y componentes reutilizables
- **Navigation**: Jetpack Navigation Compose con rutas definidas en `sealed class Screen`
- **Data Layer**: Modelos de datos simples (`data class`) y persistencia con SharedPreferences
- **State Management**: `remember`, `mutableStateOf` y `rememberSaveable` para estado local

### Diagrama de Capas

```
┌─────────────────────────────────────────┐
│               UI Layer                   │
│  ┌─────────────────────────────────────┐│
│  │           Screens                   ││
│  │  Splash, Home, Counter, History,    ││
│  │  SessionDetail, Stats, Settings,    ││
│  │  Achievements                       ││
│  └─────────────────────────────────────┘│
│  ┌─────────────────────────────────────┐│
│  │         Components                  ││
│  │    PieceCounterItem, CustomPiece... ││
│  └─────────────────────────────────────┘│
│  ┌─────────────────────────────────────┐│
│  │           Theme & Utils             ││
│  │    Color, Type, Theme, ShareUtils   ││
│  └─────────────────────────────────────┘│
└─────────────────────────────────────────┘
                    │
                    ▼
┌─────────────────────────────────────────┐
│           Navigation Layer              │
│              NavGraph                   │
└─────────────────────────────────────────┘
                    │
                    ▼
┌─────────────────────────────────────────┐
│             Data Layer                  │
│  ┌─────────────────────────────────────┐│
│  │           Models                    ││
│  │   SushiPiece, SessionRecord,        ││
│  │   Achievement, AppSettings          ││
│  └─────────────────────────────────────┘│
│  ┌─────────────────────────────────────┐│
│  │          Storage / Managers         ││
│  │   SessionStorage, AppSettingsManager││
│  │   AchievementManager                ││
│  └─────────────────────────────────────┘│
└─────────────────────────────────────────┘
```

---

## Navegacion y Flujos

### Rutas Definidas

```kotlin
sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Home : Screen("home")
    data object Counter : Screen("counter")
    data object History : Screen("history")
    data object Stats : Screen("stats")
    data object Settings : Screen("settings")
    data object Achievements : Screen("achievements")
}
```

### Flujo Principal

```
                    ┌──────────┐
                    │  Splash  │
                    └────┬─────┘
                         │ (2.5s auto)
                         ▼
                    ┌──────────┐
              ┌─────│   Home   │─────┐
              │     └────┬─────┘     │
              │          │           │
              ▼          ▼           ▼
        ┌──────────┐ ┌──────────┐ ┌───────────────┐
        │ Counter  │ │ History  │ │Stats/Logros...│
        └──────────┘ └────┬─────┘ └───────────────┘
                          │
                          ▼
                   ┌────────────┐
                   │  Detail    │
                   │ (en dialog)│
                   └────────────┘
```

### Flujo del Counter Screen (3 Fases)

```
┌─────────────────────────────────────────────────────┐
│                    FASE 1                           │
│              Nombre Restaurante                     │
│                                                     │
│  ┌─────────────────────────────────────────────┐   │
│  │  TextField: "Restaurante Sakura"            │   │
│  └─────────────────────────────────────────────┘   │
│                                                     │
│            [ Comenzar ]                            │
└───────────────────────┬─────────────────────────────┘
                        │
                        ▼
┌─────────────────────────────────────────────────────┐
│                    FASE 2                           │
│             Contador de Piezas                      │
│                                                     │
│  ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐                  │
│  │Nigiri│ │Sashi│ │Maki │ │Urama│  ... (12 items)  │
│  │  5   │ │  3  │ │  8  │ │  2  │                  │
│  └─────┘ └─────┘ └─────┘ └─────┘                  │
│                                                     │
│           Total: 18 piezas                         │
│                                                     │
│            [ Terminar Sesion ]                     │
└───────────────────────┬─────────────────────────────┘
                        │
                        ▼
┌─────────────────────────────────────────────────────┐
│                    FASE 3                           │
│               Confirmacion                          │
│                                                     │
│  Restaurante: Sakura                               │
│  Total: 18 piezas                                  │
│                                                     │
│  - Nigiri: 5                                       │
│  - Sashimi: 3                                      │
│  - Maki: 8                                         │
│  - Uramaki: 2                                      │
│                                                     │
│    [ Cancelar ]      [ Guardar ]                   │
└─────────────────────────────────────────────────────┘
```

---

## Modelos de Datos

### SushiPiece

Representa un tipo de pieza de sushi disponible para contar. Contiene información nutricional base.

```kotlin
data class SushiPiece(
    val id: String,       // Identificador unico: "nigiri", "sashimi", etc.
    val name: String,     // Nombre para mostrar: "Nigiri", "Sashimi", etc.
    val imageRes: Int,    // Recurso drawable: R.drawable.nigiri
    val emoji: String,    // Emoji visual
    val kcal: Int = 0,    // Kcalorías base de la pieza
    val salmonCount: Int = 0, // Láminas/cortes de salmón que lleva
    val riceGrams: Int = 0    // Miligramos de arroz de la pieza
)
```

**Lista predefinida**: 12 tipos de piezas en `SUSHI_PIECES`.

### SessionRecord

Representa una sesion de conteo guardada.

```kotlin
data class SessionRecord(
    val id: String,                    // UUID unico
    val date: String,                  // ISO 8601: "2024-03-15T14:30:00"
    val restaurantName: String,        // Nombre del restaurante
    val pieces: Map<String, Int>,      // {"nigiri": 5, "maki": 8, ...}
    val totalPieces: Int               // Suma total de todas las piezas
)
```

### StatsFilter

Enum para filtrar estadisticas por periodo.

```kotlin
enum class StatsFilter {
    ALL,    // Todos los tiempos
    YEAR,   // Este año
    MONTH,  // Este mes
    WEEK    // Esta semana
}
```

### StatsResult

Resultado del calculo de estadisticas.

```kotlin
data class StatsResult(
    val pieceStats: Map<String, Int>,  // Total por tipo de pieza
    val total: Int                      // Gran total
)
```

---

## Sistema de Persistencia

### SharedPreferences Managers

Para la gestión de datos se emplean distintos administradores para aislar responsabilidades:

1. **SessionStorage**: Gestiona el almacenamiento de las sesiones conformadas por el usuario. (`"sushi_log_sessions"`)
2. **AppSettingsManager**: Administra el `AppLanguage`, el `AppTheme` y la personalización de `CustomPieces`. (`"sushi_app_settings"`)
3. **AchievementManager**: Gestiona el progreso, bloqueo y desbloqueo de los logros y récords locales. (`"sushi_achievements"`)

**Archivos**: `data/SessionStorage.kt`, `data/AppSettings.kt`, `data/AchievementManager.kt`.

Formato general de almacenamiento: JSON serializado con Gson.

**Ejemplo de datos almacenados**

```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "date": "2024-03-15T14:30:00",
    "restaurantName": "Restaurante Sakura",
    "pieces": {
      "nigiri": 12,
      "sashimi": 8,
      "maki": 15
    },
    "totalPieces": 35
  }
]
```

---

## Componentes UI

### PieceCounterItem

Componente que muestra un tipo de pieza con su contador y gestiona las interacciones.

**Props**:
- `piece: SushiPiece` - Datos de la pieza
- `count: Int` - Cantidad actual
- `onIncrement: () -> Unit` - Callback al hacer tap
- `onDecrement: () -> Unit` - Callback al completar long-press

**Comportamiento**:
- **Tap**: Incrementa el contador
- **Long-press 5 segundos**: Muestra anillo de progreso circular y decrementa al completar
- **Feedback visual**: Anillo verde que se llena durante el long-press

**Estados internos**:
- `isLongPressing: Boolean` - Si esta en proceso de long-press
- `progress: Float` - Progreso del anillo (0f a 1f)

### TabRow Personalizado (Historial/Ranking, Stats)

Tabs con estilo pill (bordes redondeados completos).

**Estilos**:
- Tab activo: `Background = Primary (#4ECDC4)`, `Text = PrimaryForeground`
- Tab inactivo: `Background = Transparent`, `Text = MutedForeground`

---

## Interacciones y Gestos

### Tap para Sumar

```kotlin
Modifier.pointerInput(Unit) {
    detectTapGestures(
        onTap = { onIncrement() }
    )
}
```

### Long-Press para Restar (5 segundos)

```kotlin
Modifier.pointerInput(Unit) {
    detectTapGestures(
        onLongPress = {
            // Inicia contador de 5 segundos
            // Muestra anillo de progreso
            // Al completar: onDecrement()
        }
    )
}
```

**Implementacion con Coroutines**:
- `LaunchedEffect` que actualiza `progress` cada frame
- Duracion total: 5000ms
- Si se suelta antes: cancela y resetea

---

## Sistema de Temas

### Color.kt

```kotlin
val Background = Color(0xFF1B2838)      // Navy oscuro
val Foreground = Color(0xFFFFFFFF)      // Blanco
val Card = Color(0xFF2A3A4A)            // Navy medio
val Primary = Color(0xFF4ECDC4)         // Verde menta
val PrimaryForeground = Color(0xFF1B2838)
val Secondary = Color(0xFF3D4D5C)       // Gris azulado
val MutedForeground = Color(0xFF94A3B3) // Gris claro
val Border = Color(0xFF394959)
val ItemBg = Color(0xFFFFFFFF)          // Blanco (items sushi)
val ItemFg = Color(0xFF1A1A1A)          // Negro (texto items)
```

### Type.kt

```kotlin
val SushiTypography = Typography(
    displayLarge = TextStyle(
        fontWeight = FontWeight.ExtraBold,
        fontSize = 48.sp,
        letterSpacing = (-0.02).em
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.ExtraBold,
        fontSize = 20.sp
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    labelSmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
)
```

---

## Logica de Negocio

### Calculo de Estadisticas

```kotlin
fun getStats(filter: StatsFilter): StatsResult {
    val sessions = getSessions()
    val now = LocalDate.now()

    val startDate = when (filter) {
        StatsFilter.ALL -> LocalDate.MIN
        StatsFilter.YEAR -> now.withDayOfYear(1)
        StatsFilter.MONTH -> now.withDayOfMonth(1)
        StatsFilter.WEEK -> now.with(previousOrSame(DayOfWeek.MONDAY))
    }

    val filtered = sessions.filter { session ->
        val sessionDate = LocalDateTime.parse(session.date).toLocalDate()
        !sessionDate.isBefore(startDate)
    }

    // Agrupa y suma por tipo de pieza
    val pieceStats = mutableMapOf<String, Int>()
    var total = 0
    for (session in filtered) {
        for ((pieceId, count) in session.pieces) {
            pieceStats[pieceId] = (pieceStats[pieceId] ?: 0) + count
            total += count
        }
    }

    return StatsResult(pieceStats, total)
}
```

### Generacion de Ranking

```kotlin
fun getRanking(limit: Int = 10): List<SessionRecord> {
    return getSessions()
        .sortedByDescending { it.totalPieces }
        .take(limit)
}
```

## Estructura de Archivos

```
app/src/main/java/pls/dev/sushilog/
│
├── MainActivity.kt                    # Entry point + GlobalAchievementNotifier
│
├── data/
│   ├── AppSettings.kt                # Modelos y manager de preferencias
│   ├── Achievement.kt                # Modelos de logros
│   ├── AchievementManager.kt         # Sistema validador de logros
│   ├── SushiPiece.kt                 # Modelo + lista SUSHI_PIECES extendida
│   ├── SessionRecord.kt              # Modelo de sesion guardada
│   └── SessionStorage.kt             # Persistencia SharedPreferences de dietas
│
└── ui/
    ├── theme/
    │   ├── Color.kt                  # Paleta de colores
    │   ├── ThemeColors.kt            # Contenedor Multi-tema
    │   ├── Type.kt                   # Tipografia
    │   └── Theme.kt                  # MaterialTheme y Local SushiColors
    │
    ├── screens/
    │   ├── AchievementsScreen.kt     # Pantalla visualizadora de Logros
    │   ├── SplashScreen.kt           # Splash animado
    │   ├── HomeScreen.kt             # Menu principal
    │   ├── CounterScreen.kt          # Contador interactivo y validador
    │   ├── HistoryScreen.kt          # Historial de sesiones
    │   ├── SessionDetailScreen.kt    # Detalle extendido
    │   ├── SettingsScreen.kt         # Idioma, temas y piezas customizadas
    │   ├── ShareUtils.kt             # Helper de Canvas UI gen export
    │   └── StatsScreen.kt            # Estadisticas y curiosidades calóricas
    │
    ├── components/
    │   ├── PieceCounterItem.kt       # Item base
    │   └── CustomPieceCounterItem.kt # Item adaptado a usuario
    │
    └── navigation/
        └── NavGraph.kt               # Definicion de rutas tipadas
```

---

## Dependencias

```kotlin
// build.gradle (app)
dependencies {
    // Core
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    
    // Compose
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.animation:animation")
    
    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")
    
    // JSON
    implementation("com.google.code.gson:gson:2.10.1")
    
    // Debug
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
```

---

## Consideraciones de UX

### Feedback Tactil
- Usar `Modifier.clickable` con `indication` para ripple effect
- Vibracion al completar long-press (opcional)

### Accesibilidad
- `contentDescription` en todas las imagenes
- Tamanos de toque minimo 48dp
- Contraste de colores WCAG AA

### Animaciones
- Splash: Fade in + flotacion suave del logo
- Transiciones entre pantallas: Slide horizontal
- Long-press: Anillo de progreso circular animado
- Numeros del contador: Animacion de escala al cambiar

### Estados Vacios
- Historial vacio: Mensaje "No hay sesiones guardadas"
- Ranking vacio: Mensaje "Completa tu primera sesion"
- Estadisticas vacias: Mostrar 0 en todos los valores

---

## Posibles Mejoras Futuras

1. **Backup en la nube**: Sincronizacion con Firebase/Supabase
2. **Graficos**: Charts de consumo mensual/semanal visuales en Stats
3. **Widgets**: Widget de Android con acceso rapido a estadísticas vitales

---

## Notas de Implementacion

### Multilenguaje y Multitema (`AppTheme` / `AppLanguage`)
La app implementa soporte multi-lenguaje directo en tiempo de runtime sin recargar Activity, inyectando un objeto de strings genérico (`AppStrings.getStrings(AppLanguage)`). Para los temas, se emplea el CompositionLocal a través del wrapper de `Theme.kt`, propagando objetos custom `SushiColors`.

### Notificaciones Push de Logros in-app
`GlobalAchievementNotifier` usa localmente `MutableSharedFlow` para desencadenar que la interfaz principal en el `setContent` detecte nuevos logros cumplidos. Cuando así sucede y se notifica, salta un `AnimatedVisibility` overlay superior, sumado al uso de `RingtoneManager` en Android nativo para reproducir el sonido predeterminado del sistema.

### Generador de Imágenes Canvas Nativo (`ShareUtils.kt`)
Para exportar el final de sesiones y subirlas a Instagram/WhatsApp Stories se evita dependencias grandes mediante instanciar `Canvas(Bitmap.createBitmap)` en Android que dibuja figuras (RectF), proyecta sombras nativas y acomoda el listado en base a medidas. El mismo empaqueta el JPG, y lo manda a compartir globalmente por el componente genérico en `AndroidManifest.xml`: `<provider ... FileProvider>`.
