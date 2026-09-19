CREATE DATABASE IF NOT EXISTS gameverse_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE gameverse_db;

CREATE TABLE IF NOT EXISTS categorias (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS videojuegos (
    id_videojuego INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    descripcion VARCHAR(500) NOT NULL,
    precio DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    imagen_url VARCHAR(500),
    id_categoria INT NOT NULL,
    CONSTRAINT chk_videojuegos_precio CHECK (precio >= 0),
    CONSTRAINT chk_videojuegos_stock CHECK (stock >= 0),
    CONSTRAINT fk_videojuegos_categoria
        FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria)
);

INSERT INTO categorias (nombre) VALUES
    ('Acción'),
    ('Aventura'),
    ('RPG'),
    ('Deportes'),
    ('Estrategia')
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);

INSERT INTO videojuegos
    (titulo, descripcion, precio, stock, imagen_url, id_categoria)
SELECT 'The Legend of Zelda: Breath of the Wild',
       'Aventura de mundo abierto con exploración y desafíos.',
       59.99,
       10,
       NULL,
       id_categoria
FROM categorias
WHERE nombre = 'Aventura'
  AND NOT EXISTS (
      SELECT 1 FROM videojuegos
      WHERE titulo = 'The Legend of Zelda: Breath of the Wild'
  );
