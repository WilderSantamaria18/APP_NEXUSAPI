# 📱 MvvmNexus - Documentación Técnica Completa

## 🏗️ Arquitectura del Proyecto

### Patrón Arquitectónico: MVVM + Clean Architecture

El proyecto **MvvmNexus** está construido utilizando el patrón **MVVM (Model-View-ViewModel)** combinado con principios de **Clean Architecture**, lo que garantiza una separación clara de responsabilidades y facilita el mantenimiento y escalabilidad del código.

#### Capas de la Arquitectura

```
┌─────────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                        │
│  ┌─────────────┐  ┌──────────────┐  ┌─────────────────┐    │
│  │   Views     │  │  ViewModels  │  │  Compose UI     │    │
│  │ (Activity)  │◄─┤   (State)    │  │  (Screens)      │    │
│  └─────────────┘  └──────────────┘  └─────────────────┘    │
└────────────────────────┬────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│                     DOMAIN LAYER                             │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │  Use Cases   │  │ Repositories │  │    Models    │      │
│  │ (Business    │  │ (Interfaces) │  │   (Domain)   │      │
│  │  Logic)      │  └──────────────┘  └──────────────┘      │
│  └──────────────┘                                            │
└────────────────────────┬────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│                      DATA LAYER                              │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │ Repositories │  │   Database   │  │   Network    │      │
│  │     (Impl)   │  │    (Room)    │  │  (Retrofit)  │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
└─────────────────────────────────────────────────────────────┘
```

### Estructura de Directorios

```
app/src/main/java/com/wilder/mvvmnexus/
├── presentation/              # Capa de Presentación
│   ├── view/                  # Activities (controladores)
│   │   ├── LoginActivity.kt
│   │   ├── RegistroActivity.kt
│   │   ├── MainActivity.kt
│   │   ├── DetalleProductoActivity.kt
│   │   ├── CartActivity.kt
│   │   ├── FavoritosActivity.kt
│   │   └── ProfileActivity.kt
│   ├── viewmodel/             # ViewModels (estado y lógica de presentación)
│   │   ├── AuthViewModel.kt
│   │   ├── MainViewModel.kt
│   │   ├── DetalleProductoViewModel.kt
│   │   ├── CartViewModel.kt
│   │   ├── FavoritosViewModel.kt
│   │   └── ThemeViewModel.kt
│   └── compose/               # UI con Jetpack Compose
│       ├── screens/           # Pantallas completas
│       ├── components/        # Componentes reutilizables
│       └── theme/             # Tema y estilos
│
├── domain/                    # Capa de Dominio
│   ├── model/                 # Modelos de dominio (lógica pura)
│   │   ├── Usuario.kt
│   │   ├── Producto.kt
│   │   ├── CarritoItem.kt
│   │   └── ProductoFavorito.kt
│   ├── repository/            # Interfaces de repositorios
│   │   ├── RepositorioAuth.kt
│   │   ├── ProductoRepository.kt
│   │   ├── RepositorioCarrito.kt
│   │   └── RepositorioFavoritos.kt
│   └── usecase/               # Casos de uso (lógica de negocio)
│       ├── CasosUsoAuth.kt
│       ├── ObtenerProductosUseCase.kt
│       ├── ObtenerDetalleProductoUseCase.kt
│       ├── CasosUsoCarrito.kt
│       └── CasosUsoFavoritos.kt
│
├── data/                      # Capa de Datos
│   ├── repository/            # Implementaciones de repositorios
│   │   ├── RepositorioFirebaseAuth.kt
│   │   ├── ProductoRepositoryImpl.kt
│   │   ├── RepositorioCarritoImpl.kt
│   │   └── RepositorioFavoritosImpl.kt
│   ├── database/              # Room Database (persistencia local)
│   │   ├── AppDatabase.kt
│   │   ├── dao/               # Data Access Objects
│   │   │   ├── UsuarioDao.kt
│   │   │   ├── CarritoDao.kt
│   │   │   └── FavoritoDao.kt
│   │   └── entities/          # Entidades de BD
│   │       ├── UsuarioEntity.kt
│   │       ├── CarritoEntity.kt
│   │       └── FavoritoEntity.kt
│   ├── network/               # Retrofit API
│   │   ├── ProductoApiService.kt
│   │   └── ProductoApiClient.kt
│   └── model/                 # DTOs (Data Transfer Objects)
│       └── ProductoModel.kt
│
└── utils/                     # Utilidades
    └── ThemeManager.kt
```

---

## 🔧 Backend - Dependencias y su Propósito

### 📦 Dependencias Core de Android

#### 1. **AndroidX Core KTX** (`androidx.core:core-ktx:1.17.0`)
- **Propósito**: Extensiones de Kotlin para las APIs de Android
- **Uso**: Proporciona funciones de extensión que hacen que las APIs de Android sean más idiomáticas en Kotlin
- **Ejemplo**: `getString()`, `getDrawable()`, etc.

#### 2. **Lifecycle Runtime KTX** (`androidx.lifecycle:lifecycle-runtime-ktx:2.8.7`)
- **Propósito**: Componentes conscientes del ciclo de vida
- **Uso**: Maneja automáticamente los estados del ciclo de vida (onCreate, onDestroy, etc.)
- **Implementación**: Usado en ViewModels y Activities para gestionar coroutines de forma segura

#### 3. **Activity Compose** (`androidx.activity:activity-compose:1.9.3`)
- **Propósito**: Integración entre Activities tradicionales y Jetpack Compose
- **Uso**: Permite usar `setContent {}` en Activities para renderizar UI de Compose
- **Implementación**: Todas las activities usan esta dependencia para mostrar composables

#### 4. **AppCompat** (`androidx.appcompat:appcompat:1.7.1`)
- **Propósito**: Compatibilidad hacia atrás con versiones antiguas de Android
- **Uso**: Proporciona componentes compatibles con versiones anteriores
- **Implementación**: ComponentActivity hereda de AppCompatActivity

---

### 🎨 Jetpack Compose - UI Declarativa

#### 5. **Compose BOM** (`androidx.compose:compose-bom:2024.09.00`)
- **Propósito**: Bill of Materials - gestiona versiones de todas las librerías Compose
- **Uso**: Asegura que todas las dependencias de Compose sean compatibles entre sí
- **Ventaja**: No necesitas especificar versiones individuales para cada librería Compose

#### 6. **Material 3** (`androidx.compose.material3:material3`)
- **Propósito**: Componentes de Material Design 3 para Compose
- **Uso**: Botones, Cards, TextFields, TopAppBar, NavigationBar, etc.
- **Implementación**: Todos los componentes UI del proyecto usan Material 3
```kotlin
// Ejemplo: Button, Card, TextField
Button(onClick = { }) { Text("Login") }
Card { /* contenido */ }
OutlinedTextField(value = email, onValueChange = { email = it })
```

#### 7. **Material Icons Extended** (`androidx.compose.material:material-icons-extended`)
- **Propósito**: Set completo de iconos de Material Design
- **Uso**: Iconos para botones, navegación, acciones
- **Implementación**: 
```kotlin
Icon(Icons.Filled.Favorite, "Favoritos")
Icon(Icons.Outlined.ShoppingCart, "Carrito")
Icon(Icons.Filled.AccountCircle, "Perfil")
```

#### 8. **Runtime LiveData** (`androidx.compose.runtime:runtime-livedata`)
- **Propósito**: Integración entre LiveData y Compose
- **Uso**: Observar LiveData directamente en composables
- **Implementación**:
```kotlin
val productos by viewModel.productos.observeAsState(emptyList())
```

#### 9. **UI Tooling** (`androidx.compose.ui:ui-tooling`)
- **Propósito**: Herramientas de desarrollo para Compose
- **Uso**: Preview de composables en Android Studio
- **Implementación**: 
```kotlin
@Preview(showBackground = true)
@Composable
fun ProductCardPreview() { /* ... */ }
```

---

### 🔥 Firebase - Backend as a Service

#### 10. **Firebase BOM** (`com.google.firebase:firebase-bom:34.5.0`)
- **Propósito**: Gestiona versiones de todas las librerías Firebase
- **Uso**: Asegura compatibilidad entre servicios Firebase

#### 11. **Firebase Authentication** (`com.google.firebase:firebase-auth`)
- **Propósito**: Autenticación de usuarios
- **Funcionalidades implementadas**:
  - Registro con email y contraseña
  - Login con email y contraseña
  - Google Sign-In (OAuth 2.0)
  - Recuperación de contraseña
  - Gestión de sesiones
```kotlin
// Ejemplo de uso
firebaseAuth.signInWithEmailAndPassword(email, password).await()
firebaseAuth.createUserWithEmailAndPassword(email, password).await()
```

#### 12. **Firebase Analytics** (`com.google.firebase:firebase-analytics`)
- **Propósito**: Análisis de comportamiento del usuario
- **Uso**: Tracking de eventos, pantallas visitadas, conversiones
- **Métricas**: Usuarios activos, retención, eventos personalizados

#### 13. **Cloud Firestore** (`com.google.firebase:firebase-firestore`)
- **Propósito**: Base de datos NoSQL en tiempo real
- **Uso en el proyecto**: 
  - Sincronización de carrito entre dispositivos
  - Respaldo de datos de usuario
  - Persistencia en la nube
```kotlin
// Ejemplo de uso
firestore.collection("carritos")
    .document(userId)
    .collection("items")
    .add(carritoItem)
```

#### 14. **Google Sign-In** (`com.google.android.gms:play-services-auth:21.3.0`)
- **Propósito**: Autenticación con cuenta de Google
- **Flujo implementado**: OAuth 2.0
- **Ventaja**: Login rápido sin crear contraseña
```kotlin
val signInIntent = googleSignInClient.signInIntent
launcher.launch(signInIntent)
```

---

### 🗄️ Room Database - Persistencia Local

#### 15. **Room Runtime** (`androidx.room:room-runtime:2.6.1`)
- **Propósito**: ORM (Object-Relational Mapping) para SQLite
- **Uso**: Abstracción sobre SQLite con type-safety
- **Ventajas**: 
  - Compile-time verification de consultas SQL
  - Manejo automático de migraciones
  - Integración perfecta con LiveData/Flow

#### 16. **Room KTX** (`androidx.room:room-ktx:2.6.1`)
- **Propósito**: Extensiones Kotlin para Room
- **Uso**: Soporte para Coroutines y Flow
```kotlin
@Query("SELECT * FROM usuarios WHERE id = :userId")
fun obtenerUsuario(userId: String): Flow<UsuarioEntity?>
```

#### 17. **Room Compiler** (`androidx.room:room-compiler:2.6.1`) - KSP
- **Propósito**: Generación de código en tiempo de compilación
- **Uso**: Procesa anotaciones (@Dao, @Entity, @Database)
- **Tecnología**: Usa KSP (Kotlin Symbol Processing) en lugar de KAPT para compilación más rápida

---

### 🌐 Networking - Retrofit & OkHttp

#### 18. **Retrofit 2** (`com.squareup.retrofit2:retrofit:2.9.0`)
- **Propósito**: Cliente HTTP type-safe para Android
- **Uso**: Llamadas a la API REST (Fake Store API)
- **Implementación**:
```kotlin
interface ProductoApiService {
    @GET("products")
    suspend fun obtenerProductos(): Response<List<ProductoModel>>
    
    @GET("products/{id}")
    suspend fun obtenerProductoPorId(@Path("id") id: Int): Response<ProductoModel>
}
```

#### 19. **Gson Converter** (`com.squareup.retrofit2:converter-gson:2.9.0`)
- **Propósito**: Serialización/deserialización JSON ↔ Objetos Kotlin
- **Uso**: Convierte automáticamente respuestas JSON a data classes
```kotlin
data class ProductoModel(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val titulo: String,
    @SerializedName("price") val precio: Double
)
```

#### 20. **OkHttp** (`com.squareup.okhttp3:okhttp:4.12.0`)
- **Propósito**: Cliente HTTP eficiente de bajo nivel
- **Características**:
  - Connection pooling (reutilización de conexiones)
  - Compresión automática (GZIP)
  - Cache de respuestas

#### 21. **Logging Interceptor** (`com.squareup.okhttp3:logging-interceptor:4.12.0`)
- **Propósito**: Logging de peticiones y respuestas HTTP
- **Uso en desarrollo**: Debug de llamadas a la API
```kotlin
val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}
```

---

### 🖼️ Carga de Imágenes

#### 22. **Coil** (`io.coil-kt:coil:2.6.0`)
- **Propósito**: Librería de carga de imágenes optimizada
- **Ventajas**:
  - Carga asíncrona de imágenes
  - Cache automático (memoria + disco)
  - Placeholders y error handling
  - Optimizado para Kotlin

#### 23. **Coil Compose** (`io.coil-kt:coil-compose:2.6.0`)
- **Propósito**: Integración de Coil con Jetpack Compose
- **Uso**:
```kotlin
AsyncImage(
    model = producto.imagen,
    contentDescription = producto.titulo,
    placeholder = painterResource(R.drawable.placeholder),
    error = painterResource(R.drawable.error)
)
```

---

### ⚡ Coroutines - Concurrencia

#### 24. **Kotlinx Coroutines Android** (`org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1`)
- **Propósito**: Programación asíncrona con coroutines
- **Uso**: 
  - Llamadas a API sin bloquear UI
  - Operaciones de base de datos
  - Manejo de estados
- **Dispatchers utilizados**:
  - `Dispatchers.Main` - UI updates
  - `Dispatchers.IO` - Network/Database
  - `Dispatchers.Default` - Cómputo pesado
```kotlin
viewModelScope.launch {
    val productos = withContext(Dispatchers.IO) {
        repository.obtenerProductos()
    }
    _productos.value = productos
}
```

---

### 🏛️ MVVM - ViewModel & LiveData

#### 25. **ViewModel KTX** (`androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7`)
- **Propósito**: ViewModels conscientes del ciclo de vida
- **Uso**: Sobrevive a cambios de configuración (rotación de pantalla)
```kotlin
class MainViewModel : ViewModel() {
    private val _productos = MutableLiveData<List<Producto>>()
    val productos: LiveData<List<Producto>> = _productos
}
```

#### 26. **LiveData KTX** (`androidx.lifecycle:lifecycle-livedata-ktx:2.8.7`)
- **Propósito**: Observables conscientes del ciclo de vida
- **Ventaja**: Actualiza UI automáticamente, evita memory leaks

#### 27. **Activity KTX** (`androidx.activity:activity-ktx:1.9.3`)
- **Propósito**: Extensiones Kotlin para Activities
- **Uso**: `by viewModels()` delegate

#### 28. **Fragment KTX** (`androidx.fragment:fragment-ktx:1.8.5`)
- **Propósito**: Extensiones Kotlin para Fragments
- **Nota**: Incluido para compatibilidad futura

---

### 🛠️ Build Tools

#### 29. **KSP (Kotlin Symbol Processing)** (`com.google.devtools.ksp:2.0.21-1.0.27`)
- **Propósito**: Procesamiento de anotaciones optimizado
- **Uso**: Reemplaza KAPT para Room
- **Ventaja**: 2x más rápido que KAPT

#### 30. **Google Services Plugin** (`com.google.gms.google-services:4.4.4`)
- **Propósito**: Integración con servicios de Google
- **Uso**: Procesa `google-services.json` para Firebase

---

## 🎨 Frontend - Jetpack Compose

### Arquitectura de UI

El frontend está completamente construido con **Jetpack Compose**, el toolkit moderno de UI declarativa de Android.

### Principios de Compose Implementados

#### 1. **Composición sobre Herencia**
- No se extienden clases, se componen funciones
- Reutilización mediante composables pequeños

#### 2. **Estado Unidireccional**
```kotlin
@Composable
fun ProductCard(
    producto: Producto,
    onAddToCart: (Producto) -> Unit  // Event up
) {
    // State down
    Card { /* UI basada en producto */ }
}
```

#### 3. **Recomposición Inteligente**
- Solo se redibuja lo que cambia
- Uso de `remember` y `derivedStateOf` para optimización

### Estructura de Componentes

```
presentation/compose/
├── screens/                    # Pantallas completas
│   ├── LoginScreen.kt
│   ├── RegistroScreen.kt
│   ├── HomeScreen.kt
│   ├── DetalleProductoScreen.kt
│   ├── CartScreen.kt
│   ├── FavoritosScreen.kt
│   └── ProfileScreen.kt
├── components/                 # Componentes reutilizables
│   ├── ProductCard.kt
│   ├── SearchBar.kt
│   ├── CartItemCard.kt
│   ├── BottomNavigationBar.kt
│   └── TopAppBarCustom.kt
└── theme/                      # Tema de la app
    ├── Color.kt
    ├── Theme.kt
    └── Type.kt
```

### Gestión de Estado

#### LiveData + Compose
```kotlin
// ViewModel
class MainViewModel : ViewModel() {
    private val _productos = MutableLiveData<List<Producto>>()
    val productos: LiveData<List<Producto>> = _productos
}

// Composable
@Composable
fun HomeScreen(viewModel: MainViewModel) {
    val productos by viewModel.productos.observeAsState(emptyList())
    
    LazyColumn {
        items(productos) { producto ->
            ProductCard(producto = producto)
        }
    }
}
```

#### StateFlow para Tema
```kotlin
// ThemeManager.kt
object ThemeManager {
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()
}

// Activity
val isDarkTheme by ThemeManager.isDarkTheme.collectAsState()
MvvmNexusTheme(darkTheme = isDarkTheme) {
    // Content
}
```

### Navegación

La app usa **Activity-based navigation** con Compose:
- Cada pantalla principal es una Activity
- La UI se renderiza con `setContent { }`
- Navegación mediante `Intent`

```kotlin
// Navegar a detalle
val intent = Intent(context, DetalleProductoActivity::class.java)
intent.putExtra("PRODUCTO_ID", producto.id)
context.startActivity(intent)
```

---

## 💾 Base de Datos - Room Database

### Arquitectura de Persistencia

El proyecto usa **Room Database** (abstracción sobre SQLite) para persistencia local, con **Firebase Firestore** para sincronización en la nube.

### Estructura de la Base de Datos

```sql
-- Tabla: usuarios
CREATE TABLE usuarios (
    id TEXT PRIMARY KEY,        -- UID de Firebase
    nombre TEXT NOT NULL,
    email TEXT NOT NULL,
    fotoUrl TEXT
);

-- Tabla: carrito
CREATE TABLE carrito (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    usuarioId TEXT NOT NULL,
    productoId INTEGER NOT NULL,
    titulo TEXT NOT NULL,
    precio REAL NOT NULL,
    imagen TEXT NOT NULL,
    cantidad INTEGER NOT NULL,
    FOREIGN KEY(usuarioId) REFERENCES usuarios(id)
);

-- Tabla: favoritos
CREATE TABLE favoritos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    usuarioUid TEXT NOT NULL,
    productoId INTEGER NOT NULL,
    titulo TEXT NOT NULL,
    precio REAL NOT NULL,
    imagen TEXT NOT NULL,
    categoria TEXT NOT NULL,
    fechaAgregado INTEGER NOT NULL,
    FOREIGN KEY(usuarioUid) REFERENCES usuarios(id)
);
```

### Componentes Room

#### 1. **Entities (Entidades)**
Representan tablas en la base de datos:

```kotlin
@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey val id: String,
    val nombre: String,
    val email: String,
    val fotoUrl: String? = null
)

@Entity(tableName = "carrito")
data class CarritoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val usuarioId: String,
    val productoId: Int,
    val titulo: String,
    val precio: Double,
    val imagen: String,
    val cantidad: Int
)

@Entity(tableName = "favoritos")
data class FavoritoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val usuarioUid: String,
    val productoId: Int,
    val titulo: String,
    val precio: Double,
    val imagen: String,
    val categoria: String,
    val fechaAgregado: Long = System.currentTimeMillis()
)
```

#### 2. **DAOs (Data Access Objects)**
Interfaces para operaciones CRUD:

```kotlin
@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuarios WHERE id = :userId LIMIT 1")
    fun obtenerUsuarioPorId(userId: String): Flow<UsuarioEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarUsuario(usuario: UsuarioEntity)

    @Query("DELETE FROM usuarios")
    suspend fun borrarTodos()
}

@Dao
interface CarritoDao {
    @Query("SELECT * FROM carrito WHERE usuarioId = :usuarioId")
    fun obtenerCarritoPorUsuario(usuarioId: String): Flow<List<CarritoEntity>>

    @Query("SELECT * FROM carrito WHERE usuarioId = :usuarioId AND productoId = :productoId LIMIT 1")
    suspend fun obtenerProductoEnCarrito(usuarioId: String, productoId: Int): CarritoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarProducto(carritoItem: CarritoEntity)

    @Query("UPDATE carrito SET cantidad = :cantidad WHERE id = :id")
    suspend fun actualizarCantidad(id: Int, cantidad: Int)

    @Delete
    suspend fun eliminarProducto(carritoItem: CarritoEntity)

    @Query("DELETE FROM carrito WHERE usuarioId = :usuarioId")
    suspend fun vaciarCarrito(usuarioId: String)
}

@Dao
interface FavoritoDao {
    @Query("SELECT * FROM favoritos WHERE usuarioUid = :usuarioUid ORDER BY fechaAgregado DESC")
    fun obtenerFavoritos(usuarioUid: String): Flow<List<FavoritoEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favoritos WHERE usuarioUid = :usuarioUid AND productoId = :productoId)")
    fun esFavorito(usuarioUid: String, productoId: Int): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarFavorito(favorito: FavoritoEntity)

    @Query("DELETE FROM favoritos WHERE usuarioUid = :usuarioUid AND productoId = :productoId")
    suspend fun eliminarFavorito(usuarioUid: String, productoId: Int)
}
```

#### 3. **Database Class**
Punto de acceso único (Singleton):

```kotlin
@Database(
    entities = [UsuarioEntity::class, CarritoEntity::class, FavoritoEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun carritoDao(): CarritoDao
    abstract fun favoritoDao(): FavoritoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "nexus_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
```

### Flujo de Datos con Room

```
┌─────────────────────────────────────────────────────────────┐
│                         ViewModel                            │
│                                                               │
│  viewModelScope.launch {                                     │
│      casosUsoCarrito.agregarProducto(producto)               │
│  }                                                            │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│                    Use Case (Domain)                         │
│                                                               │
│  suspend fun agregarProducto(producto: Producto) {           │
│      repository.agregarProducto(usuarioId, producto, 1)      │
│  }                                                            │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│              Repository Implementation (Data)                │
│                                                               │
│  override suspend fun agregarProducto(...) {                 │
│      // 1. Guardar en Room (local)                          │
│      carritoDao.insertarProducto(entity)                     │
│      // 2. Sincronizar con Firestore (nube)                 │
│      firestore.collection("carritos").add(item)              │
│  }                                                            │
└────────────────────────┬────────────────────────────────────┘
                         │
           ┌─────────────┴─────────────┐
           ▼                           ▼
┌──────────────────────┐    ┌──────────────────────┐
│   Room Database      │    │   Firebase           │
│   (SQLite Local)     │    │   Firestore          │
│                      │    │   (Cloud)            │
└──────────────────────┘    └──────────────────────┘
```

### Estrategia de Sincronización

#### 1. **Offline-First**
- Los datos se guardan primero en Room (local)
- La UI se actualiza inmediatamente
- La sincronización con Firestore es secundaria

#### 2. **Reactive Updates**
- Room usa `Flow<T>` para observar cambios
- La UI se actualiza automáticamente cuando cambia la BD
```kotlin
// DAO retorna Flow
fun obtenerCarrito(usuarioId: String): Flow<List<CarritoEntity>>

// Repository mapea a dominio
override fun obtenerCarrito(usuarioId: String): Flow<List<CarritoItem>> {
    return carritoDao.obtenerCarritoPorUsuario(usuarioId)
        .map { entities -> entities.map { it.aDominio() } }
}
```

#### 3. **Dual-Write Pattern**
```kotlin
// Cada operación escribe en ambos:
override suspend fun agregarProducto(usuarioId: String, producto: Producto, cantidad: Int) {
    // 1. Local (Room) - Inmediato
    val entity = CarritoEntity(...)
    carritoDao.insertarProducto(entity)
    
    // 2. Cloud (Firestore) - Background
    try {
        firestore.collection("carritos")
            .document(usuarioId)
            .collection("items")
            .add(mapOf(...))
            .await()
    } catch (e: Exception) {
        // Fallar silenciosamente si no hay conexión
    }
}
```

### Ventajas de esta Arquitectura de BD

1. ✅ **Offline-First**: La app funciona sin conexión
2. ✅ **Reactive**: UI se actualiza automáticamente
3. ✅ **Type-Safe**: Compile-time verification de queries
4. ✅ **Performance**: Room cachea queries automáticamente
5. ✅ **Multi-dispositivo**: Firestore sincroniza entre dispositivos
6. ✅ **Persistencia**: Los datos sobreviven al cierre de la app

---

## 🔄 Flujo de Autenticación

### Proceso de Login

```
Usuario ingresa email/password
         │
         ▼
┌─────────────────────────┐
│   LoginActivity         │
│   (Compose UI)          │
└───────────┬─────────────┘
            │ onClick
            ▼
┌─────────────────────────┐
│   AuthViewModel         │
│   viewModelScope.launch │
└───────────┬─────────────┘
            │
            ▼
┌─────────────────────────┐
│   CasosUsoAuth          │
│   login(email, pass)    │
└───────────┬─────────────┘
            │
            ▼
┌──────────────────────────────────┐
│  RepositorioFirebaseAuth         │
│  override suspend fun login()    │
│  ├─ Firebase Auth login          │
│  ├─ Obtener FirebaseUser         │
│  ├─ Mapear a Usuario (domain)    │
│  └─ Guardar en Room (local)      │
└───────────┬──────────────────────┘
            │
    ┌───────┴────────┐
    ▼                ▼
Firebase         Room DB
  Auth           usuarios
```

### Proceso de Google Sign-In

```
Usuario click "Continuar con Google"
         │
         ▼
┌─────────────────────────┐
│   GoogleSignInService   │
│   obtenerClienteParaLogin()
└───────────┬─────────────┘
            │
            ▼
┌─────────────────────────┐
│   ActivityResultLauncher│
│   launch(signInIntent)  │
└───────────┬─────────────┘
            │
            ▼
    [Google Sign-In UI]
            │
            ▼
┌─────────────────────────┐
│   onActivityResult      │
│   obtener idToken       │
└───────────┬─────────────┘
            │
            ▼
┌─────────────────────────┐
│   AuthViewModel         │
│   loginConGoogle()      │
└───────────┬─────────────┘
            │
            ▼
┌──────────────────────────────────┐
│  RepositorioFirebaseAuth         │
│  ├─ Crear GoogleAuthCredential   │
│  ├─ signInWithCredential()       │
│  ├─ Obtener FirebaseUser         │
│  └─ Guardar en Room              │
└───────────┬──────────────────────┘
            │
            ▼
      [MainActivity]
```

---

## 📊 Reflexión Final

### Logros del Proyecto

#### 1. **Arquitectura Sólida y Escalable**
Este proyecto demuestra una implementación profesional de **Clean Architecture + MVVM**, separando claramente las responsabilidades en capas bien definidas. Esta arquitectura no solo facilita el mantenimiento, sino que permite agregar nuevas funcionalidades sin afectar el código existente.

#### 2. **Tecnologías Modernas**
La elección de **Jetpack Compose** para la UI representa una decisión estratégica hacia el futuro del desarrollo Android. A diferencia del sistema de Views tradicional basado en XML, Compose ofrece:
- UI declarativa (menos código, más legible)
- Recomposición inteligente (mejor performance)
- Type-safety en la construcción de UI
- Mejor testabilidad

#### 3. **Offline-First con Sincronización Cloud**
La combinación de **Room + Firestore** implementa un patrón offline-first que garantiza:
- Experiencia de usuario fluida sin conexión
- Sincronización automática cuando hay internet
- Persistencia de datos entre sesiones
- Acceso multi-dispositivo

#### 4. **Separación de Concerns**
Cada capa tiene responsabilidades claras:
- **Presentation**: Solo UI y estado de presentación
- **Domain**: Lógica de negocio pura, sin dependencias de Android
- **Data**: Implementación de fuentes de datos (API, BD, Firebase)

Esta separación permite:
- Testing independiente de cada capa
- Reutilización de lógica de negocio
- Cambio de frameworks sin afectar el dominio

#### 5. **Manejo Reactivo de Estado**
El uso de **Flow**, **LiveData** y **StateFlow** permite:
- Actualizaciones automáticas de UI
- Prevención de memory leaks
- Gestión consciente del ciclo de vida
- Propagación eficiente de cambios

### Desafíos Superados

#### 1. **Integración Firebase + Room**
Implementar el dual-write pattern para mantener sincronizados Firebase Firestore y Room Database, manejando:
- Conflictos de sincronización
- Operaciones offline
- Transformación de datos entre capas

#### 2. **Gestión de Autenticación**
Coordinar Firebase Auth con persistencia local, permitiendo:
- Login con email/password
- Google Sign-In (OAuth 2.0)
- Recuperación de contraseña
- Persistencia de sesión

#### 3. **UI Declarativa con Compose**
Transición del paradigma imperativo (Views XML) al declarativo (Compose), requiriendo:
- Nuevo modelo mental de construcción de UI
- Gestión de estado con recomposición
- Integración con ViewModels tradicionales

### Aprendizajes Clave

#### 1. **La Arquitectura Importa**
Un proyecto bien arquitecturado desde el inicio ahorra horas de refactorización. La inversión en diseño arquitectónico se recupera rápidamente en:
- Menor tiempo de desarrollo de nuevas features
- Bugs más fáciles de identificar y corregir
- Código más fácil de entender para otros desarrolladores

#### 2. **Kotlin + Coroutines = Código Elegante**
El uso de coroutines con `suspend functions` y `Flow` produce código asíncrono que es:
- Más legible que callbacks
- Más fácil de debuggear
- Más eficiente que threads tradicionales

#### 3. **Compose es el Futuro**
A pesar de la curva de aprendizaje inicial, Jetpack Compose ofrece:
- Desarrollo más rápido una vez dominado
- Menos bugs relacionados con el estado de la UI
- Mejor experiencia de desarrollo (hot reload, previews)

#### 4. **Testing Facilita el Desarrollo**
Una arquitectura limpia permite:
- Unit testing del dominio sin dependencias de Android
- Integration testing de repositorios
- UI testing de pantallas Compose

### Mejoras Futuras

#### 1. **Inyección de Dependencias**
Implementar **Hilt/Dagger** para:
- Gestión automática de dependencias
- Mejor testabilidad con mocks
- Reducción de boilerplate

#### 2. **Navigation Component**
Migrar a **Compose Navigation** para:
- Navegación más declarativa
- Mejor manejo de back stack
- Deep linking

#### 3. **Paging 3**
Implementar paginación para:
- Carga eficiente de listas grandes
- Mejor performance de memoria
- Scroll infinito

#### 4. **Testing Completo**
Agregar:
- Unit tests para ViewModels y UseCases
- Integration tests para Repositories
- UI tests con Compose Testing

#### 5. **CI/CD Pipeline**
Configurar:
- GitHub Actions para builds automáticos
- Linting automático con detekt
- Tests automáticos en PRs
- Deployment automático a Play Store

### Conclusión

**MvvmNexus** representa un proyecto completo de e-commerce para Android que implementa las mejores prácticas actuales de desarrollo móvil. La arquitectura limpia, el uso de tecnologías modernas como Jetpack Compose y Firebase, y la estrategia offline-first, demuestran un enfoque profesional y escalable.

Este proyecto no solo cumple con los requerimientos funcionales de una app de comercio electrónico, sino que establece una base sólida para futuras expansiones, manteniendo código mantenible, testeable y siguiendo los principios SOLID.

La experiencia adquirida en este proyecto proporciona una base sólida para:
- Desarrollo de aplicaciones empresariales
- Trabajo en equipos de desarrollo Android
- Adopción de nuevas tecnologías del ecosistema Android

---

**Autor**: Wilder Santamaría  
**Fecha**: Diciembre 2024  
**Versión**: 1.0
