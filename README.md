# Gameverse

Sistema web desarrollado en Java para la gestión y consulta de un catálogo de videojuegos.

## Descripción del proyecto

Gameverse es una aplicación web orientada a la gestión de información relacionada con videojuegos. El proyecto implementa una arquitectura organizada basada en el patrón Modelo-Vista-Controlador (MVC), permitiendo separar la presentación, el procesamiento de solicitudes y el acceso a los datos.

La aplicación utiliza Java como lenguaje principal, JSP para la presentación de información, Servlets para el procesamiento de solicitudes y MySQL como sistema gestor de base de datos.

---

## Tecnologías utilizadas

- Java JDK 25
- IntelliJ IDEA
- Apache Tomcat 10.1.59
- Smart Tomcat
- Apache Maven
- MySQL Server
- MySQL Workbench
- MySQL Connector/J
- Jakarta Servlet
- Jakarta JSP
- Jakarta Faces (JSF)
- CDI (Weld)
- JSTL
- HTML
- CSS
- Git
- GitHub

---

## Requisitos para ejecutar el proyecto

Para ejecutar correctamente el proyecto se requiere:

- JDK 25 instalado.
- IntelliJ IDEA.
- Apache Tomcat 10.1.x.
- Smart Tomcat para la ejecución desde IntelliJ IDEA.
- Apache Maven.
- MySQL Server.
- MySQL Workbench.
- Git.

También se requiere conexión a Internet para descargar las dependencias administradas por Maven.

---

## Configuración del proyecto

El proyecto utiliza **Java JDK 25** como versión del lenguaje.

Las dependencias del proyecto son administradas mediante **Apache Maven** y se encuentran configuradas en el archivo:

```text
pom.xml
```

Para generar el archivo WAR del proyecto, ejecuta:

```bash
mvn clean package
```

Arquitectura del proyecto

Gameverse utiliza el patrón de arquitectura Modelo-Vista-Controlador (MVC) para organizar los diferentes componentes del sistema.

Modelo

El modelo contiene las clases que representan y gestionan los datos de la aplicación.

model/
├── dto/
│ └── Videojuego.java
└── dao/
└── VideojuegoDAO.java
Vista

Las vistas utilizan JSP (JavaServer Pages) para presentar la información al usuario.

src/main/webapp/
├── index.jsp
└── WEB-INF/
└── views/
├── catalogo.jsp
└── error.jsp
Controlador

Los controladores utilizan Servlets para recibir las solicitudes del usuario, procesarlas y comunicarse con el modelo.

El módulo de Inventario incorpora un controlador JSF (`InventarioBean`) y una capa de negocio (`InventoryService`). La capa de negocio depende de `InventoryRepository`; su implementación JDBC (`InventoryDAO`) reutiliza las consultas de `VideojuegoDAO`. `VideojuegoService` expone las operaciones de videojuegos y reutiliza las validaciones del inventario. `VideojuegoJpaDAO` es una implementación separada y no forma parte de este flujo JDBC.

El módulo de usuarios reutiliza `UsuarioService`, `UsuarioDAO` y `Usuario`. El servicio valida y normaliza los datos, almacena contraseñas con PBKDF2 y devuelve modelos que no incluyen contraseñas ni hashes.

El controlador permite listar, registrar, editar y eliminar videojuegos, además de ajustar el stock sin permitir valores negativos. La vista JSF del módulo corresponde a la Persona 4 y todavía no forma parte de esta entrega.

controller/
└── CatalogoServlet.java
Acceso a datos

El acceso a MySQL se implementa con JDBC y el patrón DAO (Data Access Object):

model/dao/
├── VideojuegoDAO.java
├── InventoryDAO.java
└── UsuarioDAO.java

`VideojuegoDAO` contiene las consultas JDBC de videojuegos; `InventoryDAO` adapta esas operaciones a `InventoryRepository`, incluida la actualización de stock; `UsuarioDAO` persiste usuarios sin leer ni devolver `password_hash`. Los servicios (`VideojuegoService`, `InventoryService` y `UsuarioService`) validan los datos antes de delegar la persistencia.

### Guía de integración para la API REST (Actividad 2B)

- Inyectar `VideojuegoService` para listar, buscar, crear, actualizar y eliminar videojuegos. `buscarPorId` devuelve `Optional`; `crear` requiere ID `0`, persiste el videojuego y asigna el ID generado al mismo objeto. Las validaciones inválidas lanzan `IllegalArgumentException`; los errores JDBC se propagan como `InventoryPersistenceException`.
- Inyectar `InventoryService` para `obtenerInventario`, `buscarPorId`, `ajustarStock` y `actualizarStock`. `ajustarStock(id, cambio)` aplica un cambio incremental; `actualizarStock(id, nuevoStock)` establece el valor absoluto. Ambos rechazan IDs inválidos y stock resultante negativo.
- Reutilizar `UsuarioService.registrar`, `listarUsuarios` y `buscarUsuario`. El registro devuelve el ID; el modelo `Usuario` solo contiene datos públicos. `SQLException` puede señalar errores de persistencia, incluido un correo duplicado. No exponer ni registrar contraseñas o hashes.
- Para preparar MySQL, importar `database/fase3_usuarios.sql` sobre `gameverse_db`; es seguro para las otras tablas existentes. `database/gameverse_db.sql`, en cambio, elimina y vuelve a crear la base de datos.
- No agregar endpoints ni controladores como parte de esta capa. El futuro controlador debe traducir errores de validación y persistencia a respuestas HTTP apropiadas y evitar devolver detalles SQL internos.
- Ejecutar `mvn clean test` con MySQL iniciado y las credenciales locales válidas para correr también la prueba de integración de usuarios.
- `src/main/resources/db.properties` ya está versionado en la rama base; no agregar ni modificar allí credenciales locales. Conviene migrar esa configuración fuera del repositorio en una tarea separada.

Configuración

La configuración de la conexión a la base de datos se administra mediante:

config/
└── DatabaseConfig.java
Utilidades

El proyecto cuenta con un filtro para administrar la codificación de caracteres UTF-8.

filter/
└── UTF8Filter.java
Estructura general del proyecto
Gameverse/
│
├── README.md
├── pom.xml
│
└── src/
└── main/
│
├── java/
│ ├── com/
│ │ └── gameverse/
│ │ ├── config/
│ │ │ └── DatabaseConfig.java
│ │ │
│ │ ├── controller/
│ │ │ └── CatalogoServlet.java
│ │ │
│ │ └── model/
│ │ ├── dao/
│ │ │ └── VideojuegoDAO.java
│ │ │
│ │ └── dto/
│ │ └── Videojuego.java
│ │
│ └── filter/
│ └── UTF8Filter.java
│
├── resources/
│ └── db.properties
│
└── webapp/
├── index.jsp
│
└── WEB-INF/
└── views/
├── catalogo.jsp
└── error.jsp
Base de datos

El proyecto utiliza MySQL como sistema gestor de base de datos.

Base de datos utilizada
gameverse_db
Tablas principales

Las tablas de videojuegos son:

categorias
videojuegos

La tabla videojuegos mantiene una relación con la tabla categorias mediante el campo id_categoria.

El módulo de usuarios agrega la tabla `usuarios`, cuyo correo tiene una restricción `UNIQUE` y cuyo campo `password_hash` almacena el hash de la contraseña. Para crear las tablas de videojuegos, `database/gameverse_db.sql` recrea la base de datos; **su ejecución elimina primero la base existente**. Para agregar usuarios sin recrear otras tablas, ejecuta `database/fase3_usuarios.sql`.

Configuración de la conexión a la base de datos

Los parámetros de conexión se encuentran separados del código fuente mediante el archivo:

src/main/resources/db.properties

La estructura del archivo es la siguiente:
db.url=jdbc:mysql://localhost:3306/gameverse_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
db.driver=com.mysql.cj.jdbc.Driver
db.user=root
db.password=
Funcionamiento del catálogo

El catálogo de videojuegos funciona mediante la comunicación entre el controlador, el DAO, el modelo y la vista.

El flujo principal es:

Usuario
↓
CatalogoServlet
↓
VideojuegoDAO
↓
Base de datos MySQL
↓
Videojuego
↓
catalogo.jsp
↓
Usuario

El CatalogoServlet recibe la solicitud del usuario y solicita los datos al VideojuegoDAO.

El VideojuegoDAO ejecuta las consultas correspondientes en MySQL y devuelve la información de los videojuegos.

Finalmente, los datos son enviados a catalogo.jsp, donde son mostrados al usuario.

Configuración del servidor

El proyecto utiliza Apache Tomcat 10.1.59 como servidor de aplicaciones.

La ejecución se realiza mediante Smart Tomcat desde IntelliJ IDEA.

Configuración utilizada:

Servidor: Apache Tomcat 10.1.59
Puerto: 8080
Context Path: /Gameverse
Ejecución del proyecto

Para ejecutar Gameverse correctamente:

Instalar JDK 25.
Instalar IntelliJ IDEA.
Instalar y configurar MySQL Server.
Crear la base de datos gameverse_db.
Crear las tablas necesarias.
Configurar las credenciales de MySQL en db.properties.
Abrir el proyecto en IntelliJ IDEA.
Verificar que Maven haya descargado correctamente las dependencias.
Configurar Apache Tomcat 10.1.59 mediante Smart Tomcat.
Ejecutar el servidor.
Abrir un navegador web.
Acceder a la aplicación mediante:
http://localhost:8080/Gameverse/

Para acceder directamente al catálogo:

http://localhost:8080/Gameverse/catalogo
Gestión de dependencias

Las dependencias del proyecto son administradas mediante Apache Maven.

Las principales dependencias utilizadas son:

Jakarta Servlet API
Jakarta JSP API
JSTL
MySQL Connector/J
Jakarta Faces
Weld CDI
JUnit Jupiter (pruebas)

La configuración de estas dependencias se encuentra en:

pom.xml

Maven permite gestionar y descargar automáticamente las librerías necesarias para la compilación y ejecución del proyecto.

Control de versiones

El proyecto utiliza Git para el control de versiones y GitHub como repositorio remoto.

Los cambios realizados durante el desarrollo se registran mediante commits.

Comandos principales:

git add .
git commit -m "Descripcion del cambio"
git push

Para obtener los cambios más recientes del repositorio:

git pull
Estado actual del proyecto

El proyecto cuenta actualmente con:

Configuración de JDK 25.
Configuración de IntelliJ IDEA.
Configuración de Apache Tomcat 10.1.59.
Configuración de Smart Tomcat.
Configuración de Apache Maven.
Arquitectura MVC.
Organización de paquetes.
Conexión con MySQL.
Configuración externa mediante db.properties.
Modelo DTO para videojuegos.
DAO para acceso a datos.
Servlet para el catálogo.
Vistas JSP.
Página principal.
Catálogo de videojuegos.
Manejo de errores.
Filtro UTF-8.
Control de versiones mediante Git.
Repositorio remoto en GitHub.
Equipo de desarrollo

Proyecto desarrollado como parte de las actividades académicas de la Universidad Don Bosco (UDB).
