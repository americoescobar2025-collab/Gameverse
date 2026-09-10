# Gameverse

Sistema web desarrollado en Java para la gestión y consulta de un catálogo de videojuegos.

## Descripción del proyecto

Gameverse es una aplicación web orientada a la gestión de información relacionada con videojuegos. El proyecto implementa una arquitectura organizada basada en el patrón Modelo-Vista-Controlador (MVC), permitiendo separar la presentación, el procesamiento de solicitudes y el acceso a los datos.

La aplicación utiliza Java como lenguaje principal, JSP para la presentación de información, Servlets para el procesamiento de solicitudes y MySQL como sistema gestor de base de datos.

---

## Tecnologías utilizadas

- Java JDK 21
- IntelliJ IDEA
- Apache Tomcat 10.1.59
- Smart Tomcat
- Apache Maven
- MySQL Server
- MySQL Workbench
- MySQL Connector/J
- Jakarta Servlet
- Jakarta JSP
- JSTL
- HTML
- CSS
- Git
- GitHub

---

## Requisitos para ejecutar el proyecto

Para ejecutar correctamente el proyecto se requiere:

- JDK 21 instalado.
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

El proyecto utiliza **Java JDK 21** como versión del lenguaje.

Las dependencias del proyecto son administradas mediante **Apache Maven** y se encuentran configuradas en el archivo:

```text
pom.xml
Arquitectura del proyecto

Gameverse utiliza el patrón de arquitectura Modelo-Vista-Controlador (MVC) para organizar los diferentes componentes del sistema.

Modelo

El modelo contiene las clases que representan y gestionan los datos de la aplicación.

model/
├── dto/
│   └── Videojuego.java
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

controller/
└── CatalogoServlet.java
Acceso a datos

El acceso a la base de datos se implementa mediante el patrón DAO (Data Access Object).

model/dao/
└── VideojuegoDAO.java

Esta clase se encarga de realizar las consultas necesarias a la base de datos.

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
        │   ├── com/
        │   │   └── gameverse/
        │   │       ├── config/
        │   │       │   └── DatabaseConfig.java
        │   │       │
        │   │       ├── controller/
        │   │       │   └── CatalogoServlet.java
        │   │       │
        │   │       └── model/
        │   │           ├── dao/
        │   │           │   └── VideojuegoDAO.java
        │   │           │
        │   │           └── dto/
        │   │               └── Videojuego.java
        │   │
        │   └── filter/
        │       └── UTF8Filter.java
        │
        ├── resources/
        │   └── db.properties
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

Actualmente se utilizan las siguientes tablas:

categorias
videojuegos

La tabla videojuegos mantiene una relación con la tabla categorias mediante el campo id_categoria.

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

Instalar JDK 21.
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

Configuración de JDK 21.
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