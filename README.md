# Sushi Log

Aplicacion Android nativa para contabilizar piezas de sushi y comida japonesa consumidas en restaurantes de buffet libre o sesiones de sushi.

---

## Descripcion

**Sushi Log** es una app interactiva disenada para los amantes del sushi que quieren no solo llevar un registro exhaustivo de cuantās piezas consumen, sino tambien controlar sus estadisticas nutricionales, conseguir logros y compartir sus récords sociales. La app permite:

- Contar piezas por tipo con un simple tap (sumar) o long-press (restar)
- Guardar sesiones con fecha y nombre del restaurante
- Crear piezas de sushi personalizadas (con nombre, emoji, kcals, arroz y cantidad de salmón)
- Gestionar un perfil multidioma (español, inglés, francés e italiano) y multitema (Dark, Light, Salmon)
- Exportar y compartir una captura nativa atractiva de tus sesiones para Stories en redes sociales
- Sistema lúdico de logros con notificaciones pop-up animadas y sonido
- Analizar estadisticas por periodo (semana, mes, ano, total) e información curiosa como las calorías aproximadas, gramos de arroz y salmones enteros consumidos.

---

## Pantallas

### 1. Splash Screen
Pantalla de bienvenida con el logo de la app y animacion de entrada.

### 2. Home Screen (Pantalla Principal)
Menu principal con cuatro botones e interacciones clave:
- **Comenzar Sesion**: Inicia una nueva sesion de conteo
- **Historial**: Accede al historial
- **Estadisticas**: Consulta estadisticas acumuladas y récords nutricionales
- **Logros**: Consulta qué retos has superado
- Además cuenta con enlace rápido a **Configuración**

### 3. Counter Screen (Contador)
Flujo dinámico:
1. **Fase 1 - Restaurante**: Ingresar el nombre del restaurante (autocapitalizado)
2. **Fase 2 - Conteo**: Cuadricula con los tipos de piezas integradas y tus piezas personalizadas. Tap para sumar, long-press para restar
3. **Fase 3 - Confirmacion y Compartir**: Resumen de totales, opción instantánea para generar la captura de pantalla (`ShareSessionAsImage`) y botón para guardar la sesion de manera persistente.

### 4. History Screen (Historial)
- Lista cronologica inteligente que muestra totales e íconos destcando macro-nutrientes clave (Kcal consumidas, arroces y tipo de salmones).
- Al pulsar permite expandir el total desgajado, así como borrar o re-compartir la sesión al momento, reconectando con los datos históricos y recalculando los logros automáticamente.

### 5. Stats Screen (Estadisticas)
- Filtros temporales (Todos, Ano, Mes, Semana).
- Sección interactiva de `Curiosidades` con estimación calórica (Kcal), total de gramos de arroz ingeridos y el equivalente en salmones enteros consumidos en relación a los cortes/láminas.

### 6. Achievements Screen
- Visor de trofeos y retos disponibles desbloqueados de forma local según varíen tus estadísticas.

### 7. Settings Screen
- Sección de personalización de temas (Dark, Light, Salmon).
- Selección de idioma on-the-fly.
- Gestión CRUD de **Piezas Personalizadas** para añadir nuevos elementos adaptados a tu dieta, con asignación calórica y nutricional.
- Borrado completo de datos.

---

## Tipos de Piezas Incluidas

| ID | Nombre | Descripcion | Kcal Base | Salmón / Arroz |
|---|---|---|---|---|
| nigiri | Nigiri | Arroz prensado con pescado encima | 50 | 1 lámina / 10g |
| sashimi | Sashimi | Loncha de pescado crudo | 35 | 1 lámina / 0g  |
| maki | Maki | Rollo con alga por fuera | 40 | 0 láminas / 15g |
| uramaki | Uramaki | Rollo con arroz por fuera | 45 | 0 láminas / 15g |
| gunkan | Gunkan | "Barco" de arroz envuelto con alga | 60 | 0 láminas / 15g |
| temaki | Temaki | Cono de alga relleno | 100 | 0 láminas / 30g |
| gyoza | Gyoza | Empanadilla japonesa | 45 | 0 láminas / 0g  |
| tempura | Tempura | Langostino rebozado | 60 | 0 láminas / 0g |
| edamame | Edamame | Vainas de soja | 15 | 0 láminas / 0g |
| takoyaki| Takoyaki | Bolitas de pulpo | 55 | 0 láminas / 0g |
* Y un máximo de *12 piezas personalizables* extras por el usuario.

---

## Tecnologias

- **Lenguaje**: Kotlin
- **UI Framework**: Jetpack Compose
- **Navegacion**: Navigation Compose
- **Android Nativas**: `FileProvider` con Intents y `Canvas/Bitmap` render generator, `RingtoneManager`
- **Estados Reactivos**: StateFlow / MutableSharedFlow inyectados a `GlobalAchievementNotifier` para animaciones y UI global
- **Persistencia**: SharedPreferences + Gson para estado del usuario, ajustes y almacenamiento de historial
- **Minimo SDK**: 26 (Android 8.0)
- **Target SDK**: 34 (Android 14)

---

## Requisitos Previos

- Android Studio Hedgehog (2023.1.1) o superior
- JDK 17
- Gradle 8.x

---

## Instalacion

### 1. Crear nuevo proyecto en Android Studio

```
File > New > New Project > Empty Activity (Compose)
```

Configuracion:
- Name: `SushiLog`
- Package: `pls.dev.sushilog`
- Language: Kotlin
- Minimum SDK: API 26

### 2. Configurar build.gradle (app level)

Agregar las dependencias en `dependencies {}`:

```kotlin
implementation("androidx.core:core-ktx:1.12.0")
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
implementation("androidx.activity:activity-compose:1.8.2")
implementation(platform("androidx.compose:compose-bom:2024.02.00"))
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.ui:ui-graphics")
implementation("androidx.compose.ui:ui-tooling-preview")
implementation("androidx.compose.material3:material3")
implementation("androidx.navigation:navigation-compose:2.7.7")
implementation("com.google.code.gson:gson:2.10.1")
implementation("androidx.compose.animation:animation")
```

### 3. Copiar los archivos de codigo

Estructura de carpetas:

```
app/src/main/java/pls.dev.sushilog/
├── data/
│   ├── SushiPiece.kt
│   ├── SessionRecord.kt
│   └── SessionStorage.kt
├── ui/
│   ├── theme/
│   │   ├── Color.kt
│   │   ├── Type.kt
│   │   └── Theme.kt
│   ├── screens/
│   │   ├── SplashScreen.kt
│   │   ├── HomeScreen.kt
│   │   ├── CounterScreen.kt
│   │   ├── HistoryScreen.kt
│   │   ├── SessionDetailScreen.kt
│   │   └── StatsScreen.kt
│   ├── components/
│   │   └── PieceCounterItem.kt
│   └── navigation/
│       └── NavGraph.kt
└── MainActivity.kt
```

### 4. Copiar imagenes de sushi

Copiar las 12 imagenes de `public/sushi/*.jpg` a:

```
app/src/main/res/drawable/
├── nigiri.jpg
├── sashimi.jpg
├── maki.jpg
├── uramaki.jpg
├── gunkan.jpg
├── temaki.jpg
├── gyoza.jpg
├── tempura.jpg
├── california.jpg
├── dragon.jpg
├── edamame.jpg
└── takoyaki.jpg
```

### 5. Sync y Build

```
Build > Rebuild Project
```

### 6. Ejecutar

Conectar un dispositivo o usar un emulador y ejecutar la app.

---

## Uso

### Contar piezas
1. Desde Home, pulsa "Comenzar Sesion"
2. Escribe el nombre del restaurante y pulsa "Comenzar"
3. Tap en una pieza para sumar (+1)
4. Long-press 5 segundos en una pieza para restar (-1). Aparece un anillo de progreso
5. Pulsa "Terminar Sesion" cuando hayas acabado
6. Confirma para guardar

### Ver historial
1. Desde Home, pulsa "Historial"
2. En la pestana "Historial" ves todas las sesiones
3. Pulsa una sesion para ver el desglose

### Ver estadisticas
1. Desde Home, pulsa "Estadisticas"
2. Selecciona el filtro temporal: Todos, Ano, Mes, Semana
3. Consulta el total de cada tipo de pieza

---

## Paleta de Colores

| Color | Hex | Uso |
|---|---|---|
| Background | `#1B2838` | Fondo principal |
| Card | `#2A3A4A` | Tarjetas |
| Primary | `#4ECDC4` | Botones, acentos |
| Secondary | `#3D4D5C` | Botones secundarios |
| Foreground | `#FFFFFF` | Texto principal |
| Muted | `#94A3B3` | Texto secundario |

---

## Tipografia

- **Fuente**: Inter (o system sans-serif)
- **Titulos grandes**: 48sp, ExtraBold
- **Titulos pantalla**: 20sp, ExtraBold
- **Nombres piezas**: 18sp, Bold, UPPERCASE
- **Cuerpo**: 14-16sp, Regular/Medium
- **Caption**: 12-14sp, Regular

---

## Licencia

Este proyecto es de uso personal y educativo.

---

## Autor

Pepe López