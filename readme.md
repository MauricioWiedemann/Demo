# Demo - ToDo List

Aplicación Android desarrollada en **Kotlin** como demo de una **ToDo List**, enfocada en mostrar una arquitectura simple con **ViewModel**, persistencia local con **Room** y pruebas automatizadas.

## Objetivo del proyecto

Este proyecto fue creado como práctica para trabajar con:

- Persistencia local con **Room**
- Manejo de estado con **ViewModel**
- Coroutines en Android
- Pruebas unitarias e instrumentadas

## Funcionalidades

La aplicación permite:

- Agregar tareas
- Listar tareas guardadas localmente
- Marcar tareas como completadas
- Desmarcar tareas
- Eliminar tareas
- Mantener la información persistida en la base de datos local

## Tecnologías utilizadas

- **Kotlin**
- **Android SDK**
- **RecyclerView**
- **ViewModel**
- **Coroutines**
- **Room**
- **JUnit**
- **MockK**
- **Espresso**

## Arquitectura

El proyecto está organizado en capas simples:

### `data`
Contiene la lógica de acceso a datos.

- `local/`
    - `TaskEntity`
    - `TaskDao`
    - `AppDatabase`
- `repository/`
    - `TaskRepository`

### `ui`
Contiene la lógica de presentación.

- `ui.main/`
    - `TaskViewModel`
    - `TaskUiState`

### `util`
Clases auxiliares.

- `ViewModelFactory`

### raíz del módulo
- `MainActivity`

## Estructura del proyecto

```text
app/
└── src/
    ├── main/
    │   ├── Kotlin/com/example/demo/
    │   │   ├── data/
    │   │   │   ├── local/
    │   │   │   └── repository/
    │   │   ├── ui/
    │   │   ├── util/
    │   │   └── MainActivity.kt
    │   ├── res/
    │   └── AndroidManifest.xml
    ├── test/
    └── androidTest/
```

### Ejecución
Con los siguientes comandos se ejecutan los **UnitTest** y **AndroidTest** con la opción de **Coverage** habilitada.

- **UnitTest**:  `./gradlew :app:createDebugUnitTestCoverageReport`
- **AndroidTest**:  `./gradlew :app:createDebugTestCoverageReport`