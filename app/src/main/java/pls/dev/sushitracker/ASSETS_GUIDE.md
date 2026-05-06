/**
 * =================================
 *  SUSHI TRACKER - GUIA DE ASSETS
 * =================================
 *
 * ESTRUCTURA DE CARPETAS:
 * 
 * app/src/main/
 * ├── java/pls/dev/sushitracker/
 *     │   ├── data/
 *     │   │   ├── Achievement.kt
 *     │   │   ├── AchievementManager.kt
 *     │   │   ├── AppSettings.kt
 *     │   │   ├── SessionRecord.kt
 *     │   │   ├── SessionStorage.kt
 *     │   │   └── SushiPiece.kt
 *     │   ├── ui/
 *     │   │   ├── theme/
 *     │   │   │   ├── Color.kt
 *     │   │   │   ├── Theme.kt
 *     │   │   │   ├── ThemeColors.kt
 *     │   │   │   └── Type.kt
 *     │   │   ├── screens/
 *     │   │   │   ├── AchievementsScreen.kt
 *     │   │   │   ├── CounterScreen.kt
 *     │   │   │   ├── HistoryScreen.kt
 *     │   │   │   ├── HomeScreen.kt
 *     │   │   │   ├── SessionDetailScreen.kt
 *     │   │   │   ├── SettingsScreen.kt
 *     │   │   │   ├── ShareUtils.kt
 *     │   │   │   ├── SplashScreen.kt
 *     │   │   │   └── StatsScreen.kt
 * │   │   ├── components/
 * │   │   │   ├── CustomPieceCounterItem.kt
 * │   │   │   └── PieceCounterItem.kt
 * │   │   └── navigation/
 * │   │       └── NavGraph.kt
 *     │   ├── AGENT.md
 *     │   ├── ASSETS_GUIDE.md
 *     │   └── MainActivity.kt
 *     ├── AndroidManifest.xml (Incluye FileProvider para exportar imágenes)
 *     └── res/
 *     ├── drawable/
 *     │   ├── nigiri.jpg
 *     │   ├── sashimi.jpg
 *     │   ├── maki.jpg
 *     │   ├── uramaki.jpg
 *     │   ├── gunkan.jpg
 *     │   ├── temaki.jpg
 *     │   ├── gyoza.jpg
 *     │   ├── tempura.jpg
 *     │   ├── california.jpg
 *     │   ├── dragon.jpg
 *     │   ├── edamame.jpg
 *     │   └── takoyaki.jpg
 *     └── values/
 *         ├── colors.xml
 *         └── strings.xml
 *
 * ============================================================
 *  COLORES (Hex)
 * ============================================================
 *
 *  NOMBRE              HSL                     HEX         USO
 *  -------             ---                     ---         ---
 *  Background          hsl(214, 33%, 16%)      #1B2838     Fondo principal
 *  Foreground          hsl(0, 0%, 100%)        #FFFFFF     Texto principal
 *  Card                hsl(210, 25%, 22%)      #2A3A4A     Tarjetas, contenedores
 *  Primary             hsl(174, 55%, 55%)      #4ECDC4     Boton principal, acentos
 *  PrimaryForeground   hsl(214, 33%, 16%)      #1B2838     Texto sobre primario
 *  Secondary           hsl(210, 20%, 30%)      #3D4D5C     Botones secundarios
 *  SecondaryForeground hsl(0, 0%, 100%)        #FFFFFF     Texto sobre secundario
 *  MutedForeground     hsl(210, 15%, 65%)      #94A3B3     Texto apagado / subtitulos
 *  Border              hsl(210, 20%, 28%)      #394959     Bordes
 *  Destructive         hsl(0, 84%, 60%)        #EF4444     Errores, restar pieza
 *  ItemBg              hsl(0, 0%, 100%)        #FFFFFF     Fondo items de sushi
 *  ItemFg              hsl(0, 0%, 10%)         #1A1A1A     Texto items de sushi
 *
 * ============================================================
 *  TIPOGRAFIA
 * ============================================================
 *
 *  - Font Family: Inter (Google Fonts) o la system default sans-serif
 *  - Titulos grandes: 48sp, ExtraBold (800), tracking tight
 *  - Titulos pantalla: 20sp, ExtraBold (800)
 *  - Nombres piezas: 18sp, Bold (700), uppercase
 *  - Cuerpo: 14-16sp, Regular/Medium (400/500)
 *  - Caption: 12-14sp, Regular (400)
 *
 * ============================================================
 *  DIMENSIONES (dp)
 * ============================================================
 *
 *  - Border radius tarjetas: 16dp (rounded-2xl)
 *  - Border radius botones: 9999dp (full rounded / pill shape)
 *  - Border radius imagenes sushi: 12dp (rounded-xl)
 *  - Padding general: 16dp (px-4)
 *  - Padding interior tarjetas: 12dp (p-3)
 *  - Gap entre items lista: 12dp (gap-3)
 *  - Imagen sushi en lista: 56x56dp
 *  - Imagen sushi en detalle: 48x48dp
 *  - Circulo contador: 40x40dp con borde 2dp
 *  - Boton back: 40x40dp circular
 *  - Badge ranking: 36x36dp circular
 *  - Boton principal alto: 56dp
 *  - Tab switcher alto: 40dp
 *  - Bottom bar height: 72dp con padding
 *
 * ============================================================
 *  IMAGENES DE SUSHI
 * ============================================================
 *
 *  Las imagenes están en: res/drawable/
 *  Nombres:
 *    nigiri.jpg, sashimi.jpg, maki.jpg, uramaki.jpg,
 *    gunkan.jpg, temaki.jpg, gyoza.jpg, tempura.jpg,
 *    california.jpg, dragon.jpg, edamame.jpg, takoyaki.jpg
 *
 * ============================================================
 *  DEPENDENCIAS build.gradle (app level)
 * ============================================================
 *
 *  implementation("androidx.core:core-ktx:1.18.0")                      // Funciones de extensión para Kotlin
 *  implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.10.0")    // Ciclo de vida compatible con corrutinas
 *  implementation("androidx.activity:activity-compose:1.13.0")          // Puente entre Activity y Jetpack Compose
 *  implementation(platform("androidx.compose:compose-bom:2024.09.00"))  // Gestor de versiones (Bill of Materials) para Compose
 *  implementation("androidx.compose.ui:ui")                             // Componentes fundamentales de UI de Compose
 *  implementation("androidx.compose.ui:ui-graphics")                    // Herramientas de dibujo y gráficos para Compose
 *  implementation("androidx.compose.ui:ui-tooling-preview")             // Visualización de pantallas en el editor de Android Studio
 *  implementation("androidx.compose.material3:material3")               // Componentes con diseño Material Design 3
 *  implementation("androidx.navigation:navigation-compose:2.9.7")       // Navegación entre pantallas en Compose
 *  implementation("com.google.code.gson:gson:2.11.0")                   // Serialización/Deserialización de objetos JSON
 *  implementation("androidx.compose.animation:animation")               // Soporte para animaciones nativas en Compose 
 *
 *
 * ============================================================
 */
