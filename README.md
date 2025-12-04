# 🛒 MvvmNexus - App de E-Commerce para Android

Una aplicación móvil de comercio electrónico desarrollada en **Android con Kotlin**, utilizando arquitectura **MVVM (Model-View-ViewModel)** y tecnologías modernas como **Jetpack Compose**, **Firebase** y **Room Database**.

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)
![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)

---

## 📱 Descripción

**MvvmNexus** es una aplicación de tienda virtual que permite a los usuarios:

- 🔐 **Registrarse e iniciar sesión** (con email/contraseña o Google Sign-In)
- 🛍️ **Explorar productos** obtenidos desde una API externa
- 📦 **Ver detalles de productos** con imágenes, precios y descripciones
- ❤️ **Guardar productos en favoritos**
- 🛒 **Añadir productos al carrito de compras**
- 👤 **Gestionar perfil de usuario**
- 🌙 **Cambiar entre tema claro y oscuro**

---

## 🏗️ Arquitectura

La aplicación sigue el patrón de arquitectura **MVVM (Model-View-ViewModel)** con **Clean Architecture**:

```
app/
└── src/main/java/com/wilder/mvvmnexus/
    ├── core/                    # Componentes centrales
    ├── data/                    # Capa de datos
    │   ├── database/            # Room Database (SQLite local)
    │   │   ├── dao/             # Data Access Objects
    │   │   └── entities/        # Entidades de la BD
    │   ├── model/               # Modelos de datos (DTOs)
    │   ├── network/             # Retrofit API clients
    │   └── repository/          # Implementación de repositorios
    ├── di/                      # Inyección de dependencias
    ├── domain/                  # Capa de dominio (lógica de negocio)
    │   ├── model/               # Modelos de dominio
    │   ├── repository/          # Interfaces de repositorios
    │   └── usecase/             # Casos de uso
    ├── presentation/            # Capa de presentación (UI)
    │   ├── compose/             # Componentes Jetpack Compose
    │   ├── view/                # Activities
    │   └── viewmodel/           # ViewModels
    └── utils/                   # Utilidades
```

---

## 🖥️ Pantallas de la App

| Pantalla | Descripción |
|----------|-------------|
| 🔐 **Login** | Inicio de sesión con email/contraseña o Google |
| 📝 **Registro** | Crear nueva cuenta de usuario |
| 🏠 **Home** | Lista de productos con búsqueda y filtros |
| 📦 **Detalle Producto** | Información completa del producto |
| ❤️ **Favoritos** | Productos guardados como favoritos |
| 🛒 **Carrito** | Productos añadidos al carrito de compras |
| 👤 **Perfil** | Información y configuración del usuario |
| ⚙️ **Configuración** | Ajustes de tema (claro/oscuro) |

---

## 🛠️ Tecnologías Utilizadas

### Frontend
- **Kotlin** - Lenguaje de programación principal
- **Jetpack Compose** - UI declarativa moderna
- **Material Design 3** - Componentes de diseño

### Backend/Servicios
- **Firebase Authentication** - Autenticación de usuarios
- **Firebase Firestore** - Base de datos en la nube
- **Firebase Analytics** - Análisis de uso

### Almacenamiento Local
- **Room Database** - Base de datos SQLite local
- **DataStore/SharedPreferences** - Almacenamiento de preferencias

### Networking
- **Retrofit 2** - Cliente HTTP para APIs REST
- **OkHttp** - Interceptores y logging de peticiones
- **Gson** - Serialización/deserialización JSON

### Imágenes
- **Coil** - Carga y caché de imágenes

### Arquitectura y Patrones
- **MVVM** - Model-View-ViewModel
- **Clean Architecture** - Separación de capas
- **Repository Pattern** - Abstracción de fuentes de datos
- **Use Cases** - Casos de uso de negocio

### Otros
- **Coroutines** - Programación asíncrona
- **LiveData/StateFlow** - Observables reactivos
- **KSP** - Procesador de anotaciones de Kotlin

---

## ⚙️ Requisitos

- **Android Studio** Hedgehog o superior
- **JDK 11** o superior
- **Android SDK 24** (mínimo) - Android 7.0 Nougat
- **Android SDK 36** (target) - Android 15
- Cuenta de **Firebase** configurada

---

## 🚀 Instalación y Configuración

### 1. Clonar el repositorio

```bash
git clone https://github.com/WilderSantamaria18/APP_NEXUSAPI.git
cd APP_NEXUSAPI
```

### 2. Configurar Firebase

1. Crear un proyecto en [Firebase Console](https://console.firebase.google.com/)
2. Añadir una aplicación Android con el package name: `com.wilder.mvvmnexus`
3. Descargar el archivo `google-services.json`
4. Colocar el archivo en `app/google-services.json`
5. Habilitar **Authentication** (Email/Password y Google Sign-In)
6. Habilitar **Cloud Firestore**

### 3. Sincronizar y compilar

```bash
# Abrir en Android Studio y sincronizar Gradle
# O desde terminal:
./gradlew assembleDebug
```

### 4. Ejecutar la aplicación

- Conectar un dispositivo Android o usar un emulador
- Ejecutar desde Android Studio o:

```bash
./gradlew installDebug
```

---

## 📂 Estructura de Dependencias

```kotlin
// Firebase
firebase-auth
firebase-analytics
firebase-firestore

// Jetpack
lifecycle-viewmodel-ktx
lifecycle-livedata-ktx
room-runtime / room-ktx

// Networking
retrofit2 + gson-converter
okhttp3 + logging-interceptor

// UI
material3
coil (imágenes)
compose

// Concurrencia
kotlinx-coroutines-android
```

---

## 🔐 Autenticación

La app soporta dos métodos de autenticación:

1. **Email y Contraseña**: Registro e inicio de sesión tradicional
2. **Google Sign-In**: Inicio rápido con cuenta de Google

Los datos del usuario se sincronizan entre Firebase Firestore y la base de datos local Room.

---

## 🌐 API de Productos

La aplicación consume productos desde una API externa REST, mostrando:
- Título del producto
- Precio
- Descripción
- Categoría
- Imagen
- Puntuación y votos

---

## 🎨 Temas

La app incluye soporte para **tema claro y oscuro**, configurable desde la pantalla de ajustes. Las preferencias del tema se guardan localmente.

---

## 👨‍💻 Autor

**Wilder Santamaria**

- GitHub: [@WilderSantamaria18](https://github.com/WilderSantamaria18)

---

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Consulta el archivo `LICENSE` para más detalles.

---

## 🙏 Agradecimientos

- [Firebase](https://firebase.google.com/) por los servicios de backend
- [Jetpack Compose](https://developer.android.com/jetpack/compose) por la UI moderna
- [FakeStore API](https://fakestoreapi.com/) por los datos de productos de ejemplo

---

⭐ **¡Si te gusta este proyecto, dale una estrella!** ⭐
