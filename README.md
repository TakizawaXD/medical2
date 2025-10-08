# Sistema de Gestión Médica

Este proyecto es una aplicación de escritorio autónoma desarrollada en JavaFX. Utiliza una base de datos local SQLite para gestionar usuarios y pacientes de un sistema médico, adaptado a un contexto colombiano.

## Características Principales

-   **Autenticación de Usuarios:** Sistema de inicio de sesión y registro.
-   **Control de Acceso Basado en Roles:** Diferentes usuarios (como administradores, médicos, etc.) tienen distintos permisos.
-   **Gestión de Usuarios:** Funcionalidades para crear, ver, editar y eliminar usuarios.
-   **Gestión de Pacientes Mejorada:** Registro de pacientes completo, incluyendo campos como nombre, apellidos, **cédula**, **email**, fecha de nacimiento, **edad (calculada automáticamente)**, dirección, teléfono, y más.
-   **Adaptación Cultural para Colombia:** La terminología y los campos, como el uso de "Cédula" en lugar de "DNI", han sido ajustados para el contexto colombiano.
-   **Datos de Ejemplo:** La base de datos se inicializa con un conjunto de pacientes de ejemplo para facilitar las pruebas y la demostración.
-   **Código Limpio:** Se han eliminado los comentarios innecesarios para mejorar la legibilidad y el mantenimiento del código.
-   **Historial de Visitas:** Permite registrar y consultar el historial de encuentros médicos de cada paciente.
-   **Interfaz de Usuario Nativa:** Construida con JavaFX estándar para una apariencia y comportamiento nativos.

## Requisitos Previos

-   **Java Development Kit (JDK):** Versión 17 o superior.
-   **Maven:** Para compilar el proyecto y gestionar las dependencias.

## Instalación y Ejecución

Puedes compilar y ejecutar el proyecto usando Maven.

1.  **Compila el proyecto:**
    ```bash
    mvn clean install
    ```

2.  **Ejecuta la aplicación:**
    ```bash
    mvn javafx:run
    ```
    También puedes importar el proyecto en tu IDE favorito (como IntelliJ IDEA o Eclipse) y ejecutar la clase `com.example.demo.Main`.

## Arquitectura

La aplicación sigue una arquitectura simple de tres capas, ideal para una aplicación de escritorio:

```mermaid
graph TD
    subgraph "Aplicación de Escritorio (JavaFX)"
        A[Vistas (FXML)]
        B[Controladores de Vista]
        C[Lógica de Base de Datos (Database.java)]
    end

    subgraph "Base de Datos Local"
        D[(SQLite)]
    end

    A -- "Interacción del Usuario" --> B
    B -- "Llama a" --> C
    C -- "Consulta/Actualiza" --> D
```

## Tecnologías Utilizadas

-   **Lenguaje:** Java 17
-   **Framework de UI:** JavaFX
-   **Base de Datos:** SQLite (a través de `sqlite-jdbc`)
-   **Gestión de Dependencias:** Maven
-   **Hashing de Contraseñas:** BCrypt
