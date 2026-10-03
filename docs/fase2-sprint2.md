# Gameverse — Fase 2

## Persistencia Empresarial e Integración JSF — Sprint II

### 1. Evolución del incremento anterior

Gameverse continúa el desarrollo realizado durante la Fase 1, en la cual se implementó un primer incremento funcional utilizando Java Web, arquitectura MVC, Servlets, JSP, JDBC y una base de datos MySQL.

Para el inicio del Sprint II se realizó una revisión de la estructura actual del proyecto y del incremento desarrollado anteriormente. Se verificó la existencia del modelo de dominio representado principalmente por Videojuego y Categoria, el acceso a datos mediante DAO, los controladores basados en Servlets, las vistas JSP y la conexión con la base de datos MySQL.

Al momento de iniciar esta fase, la retroalimentación detallada y la calificación correspondiente a la Fase 1 todavía no han sido publicadas por el docente. Por esta razón, el equipo continuará con el desarrollo del Sprint II y las observaciones específicas del docente serán incorporadas posteriormente cuando se encuentren disponibles.

### 2. Estado inicial del proyecto para Sprint II

Durante la revisión inicial se comprobó lo siguiente:

- El proyecto utiliza Java JDK 25 y Apache Maven.
- La aplicación mantiene una estructura basada en el patrón MVC.
- La persistencia desarrollada en la fase anterior utiliza JDBC y MySQL.
- La base de datos contiene las tablas categorias y videojuegos.
- La tabla videojuegos incluye información de título, descripción, precio, stock, plataforma, desarrollador, fecha de lanzamiento, imagen, estado y categoría.
- Existe una relación entre videojuegos y categorias mediante id_categoria.
- El catálogo desarrollado en la Fase 1 permite consultar los videojuegos almacenados.
- El proyecto todavía no incorpora JPA/Hibernate ni una interfaz desarrollada con JSF, tecnologías que serán integradas durante el Sprint II.

### 3. Incremento funcional del Sprint II: Inventario

Durante el Sprint II se desarrollará el módulo de Inventario de Gameverse. Este módulo permitirá administrar los videojuegos registrados en el sistema, evolucionando la funcionalidad de catálogo implementada durante la Fase 1.

El módulo de Inventario tendrá como alcance:

- Listar los videojuegos registrados.
- Registrar nuevos videojuegos.
- Editar la información de los videojuegos.
- Administrar y actualizar las existencias (stock).
- Buscar videojuegos por título.
- Filtrar videojuegos por categoría.
- Consultar el detalle de un videojuego.
- Eliminar o desactivar videojuegos respetando la integridad de los datos.

El módulo aprovechará las entidades Videojuego y Categoria y la estructura de base de datos existente. Su implementación durante el Sprint II integrará JPA/Hibernate para persistencia, JSF para la interfaz y controladores, y AJAX, validadores y converters para mejorar la interacción y validación de los datos.

### 4. Product Backlog — Sprint II

El Product Backlog se actualiza para reflejar las actividades necesarias para desarrollar el incremento de Inventario durante la Fase 2.

| ID | Tarea / componente | Responsable | Estado |
|---|---|---|---|
| PB-07 | Correcciones, preparación del proyecto y definición del incremento de Inventario | Alcyr Alexander Figueroa Landaverde | En proceso |
| PB-08 | Persistencia con JPA/Hibernate y mapeo de entidades | Rebeca Sarai Flores de Tejada | Pendiente |
| PB-09 | Managed Beans y lógica de negocio | Edwin Vladimir Rivera Cubias | Pendiente |
| PB-10 | Interfaz JSF y componentes del módulo de Inventario | Eduardo Ezequiel Amaya Montano | Pendiente |
| PB-11 | AJAX, validadores y converters | Americo Gabriel Escobar Alvarenga | Pendiente |
| PB-12 | Pruebas, integración, control de versiones y documentación | Henry Beisson Chacon Garcia | Pendiente |
### 5. Cambios y evolución previstos para el Sprint II

A partir de la revisión del incremento desarrollado en la Fase 1, se establece la evolución del proyecto para el Sprint II.

Los principales cambios previstos son:

- Evolucionar la persistencia basada en JDBC hacia JPA/Hibernate.
- Adaptar el modelo de dominio existente para su utilización mediante entidades y relaciones persistentes.
- Incorporar Managed Beans y lógica de negocio para separar las responsabilidades de presentación, negocio y persistencia.
- Incorporar una interfaz basada en JSF para el módulo de Inventario.
- Implementar operaciones de consulta, registro, edición y administración de videojuegos dentro del Inventario.
- Incorporar búsqueda y filtrado de videojuegos.
- Incorporar AJAX para actualizaciones parciales de la interfaz.
- Incorporar validadores y converters para el tratamiento adecuado de los datos.
- Realizar pruebas de persistencia, relaciones, transacciones, validaciones y comportamiento de la interfaz.
- Mantener el trabajo del equipo versionado mediante Git y realizar la integración de los cambios una vez comprobados.

Las correcciones específicas derivadas de la evaluación de la Fase 1 serán incorporadas a este registro cuando la retroalimentación del docente se encuentre disponible.