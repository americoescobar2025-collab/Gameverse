-- =====================================================================
-- Gameverse - Script de creación de base de datos
-- Motor: MySQL 8.x
-- Autor: Módulo de Acceso a Datos (JDBC)
-- =====================================================================

DROP DATABASE IF EXISTS gameverse_db;
CREATE DATABASE gameverse_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE gameverse_db;

-- ---------------------------------------------------------------------
-- Tabla: categorias
-- ---------------------------------------------------------------------
CREATE TABLE categorias (
    id_categoria     INT AUTO_INCREMENT PRIMARY KEY,
    nombre           VARCHAR(100)  NOT NULL,
    descripcion      VARCHAR(255)  NULL,
    CONSTRAINT uq_categorias_nombre UNIQUE (nombre)
) ENGINE = InnoDB;

-- ---------------------------------------------------------------------
-- Tabla: videojuegos
-- ---------------------------------------------------------------------
CREATE TABLE videojuegos (
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
    CONSTRAINT fk_videojuegos_categoria
        FOREIGN KEY (id_categoria) REFERENCES categorias (id_categoria)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT chk_videojuegos_precio CHECK (precio >= 0),
    CONSTRAINT chk_videojuegos_stock  CHECK (stock >= 0)
) ENGINE = InnoDB;

-- Índices de apoyo para las consultas más frecuentes del DAO
CREATE INDEX idx_videojuegos_categoria  ON videojuegos (id_categoria);
CREATE INDEX idx_videojuegos_titulo     ON videojuegos (titulo);
CREATE INDEX idx_videojuegos_plataforma ON videojuegos (plataforma);
CREATE INDEX idx_videojuegos_activo     ON videojuegos (activo);

-- ---------------------------------------------------------------------
-- Datos de ejemplo
-- ---------------------------------------------------------------------
INSERT INTO categorias (nombre, descripcion) VALUES
 ('Acción',       'Juegos centrados en combate y reflejos rápidos'),
 ('Aventura',     'Juegos de exploración y narrativa'),
 ('RPG',          'Juegos de rol con progresión de personaje'),
 ('Deportes',     'Simuladores deportivos'),
 ('Estrategia',   'Juegos de planificación y gestión de recursos');

INSERT INTO videojuegos
 (titulo, descripcion, precio, stock, plataforma, desarrollador, fecha_lanzamiento, imagen_url, activo, id_categoria)
VALUES
 ('The Last Frontier', 'Juego de acción post-apocalíptico', 59.99, 25, 'PC',        'Nova Studios',   '2023-05-10', NULL, 1, 1),
 ('Mystic Realms',     'Aventura de fantasía en mundo abierto', 49.99, 15, 'PS5',   'Aurora Games',   '2022-11-02', NULL, 1, 2),
 ('Kingdom Legacy',    'RPG táctico por turnos',               39.99, 30, 'Switch', 'Ironwood',       '2021-09-14', NULL, 1, 3),
 ('Pro Kick 2026',     'Simulador de fútbol',                  69.99, 40, 'Xbox',   'GoalLine Corp',  '2026-08-01', NULL, 1, 4),
 ('Empire Builders',   'Estrategia de gestión de imperios',    29.99, 20, 'PC',     'Grand Design',   '2020-03-20', NULL, 1, 5);
