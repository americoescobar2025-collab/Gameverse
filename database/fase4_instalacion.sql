-- GAMEVERSE - FASE 4
-- Instalacion segura de la estructura de base de datos.
-- No elimina registros existentes.

-- =====================================================================
-- Gameverse - Script de creación de base de datos
-- Motor: MySQL 8.x
-- Autor: Módulo de Acceso a Datos (JDBC)
-- =====================================================================


CREATE DATABASE IF NOT EXISTS gameverse_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE gameverse_db;

-- ---------------------------------------------------------------------
-- Tabla: categorias
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS categorias (
    id_categoria     INT AUTO_INCREMENT PRIMARY KEY,
    nombre           VARCHAR(100)  NOT NULL,
    descripcion      VARCHAR(255)  NULL,
    CONSTRAINT uq_categorias_nombre UNIQUE (nombre)
) ENGINE = InnoDB;

-- ---------------------------------------------------------------------
-- Tabla: videojuegos
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS videojuegos (
    id_videojuego     INT AUTO_INCREMENT PRIMARY KEY,
    titulo            VARCHAR(150)   NOT NULL,
    descripcion       TEXT           NULL,
    precio            DECIMAL(10,2)  NOT NULL DEFAULT 0.00,
    stock             INT            NOT NULL DEFAULT 0,
    plataforma        VARCHAR(50)    NULL,
    desarrollador     VARCHAR(100)   NULL,
    fecha_lanzamiento DATE           NULL,
    imagen_url        VARCHAR(255)   NULL,
    activo            TINYINT(1)     NOT NULL DEFAULT 1,
    id_categoria      INT            NOT NULL,
    fecha_creacion    TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
                                     ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_videojuegos_categoria (id_categoria),
    INDEX idx_videojuegos_titulo (titulo),
    INDEX idx_videojuegos_plataforma (plataforma),
    INDEX idx_videojuegos_activo (activo),
    CONSTRAINT fk_videojuegos_categoria
        FOREIGN KEY (id_categoria) REFERENCES categorias (id_categoria)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT chk_videojuegos_precio CHECK (precio >= 0),
    CONSTRAINT chk_videojuegos_stock  CHECK (stock >= 0)
) ENGINE = InnoDB;



-- Fase 3: usuarios basicos de Gameverse
-- No elimina ni modifica tablas existentes.

CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    activo TINYINT(1) NOT NULL DEFAULT 1,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;


-- Categorias iniciales sin duplicados.
INSERT INTO categorias (nombre, descripcion) VALUES
('Acción', 'Juegos de combate'),
('Aventura', 'Juegos de exploración'),
('RPG', 'Juegos de rol'),
('Deportes', 'Simuladores deportivos'),
('Estrategia', 'Juegos de planificación')
ON DUPLICATE KEY UPDATE nombre = categorias.nombre;