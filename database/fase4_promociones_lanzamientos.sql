USE gameverse_db;

-- Promociones vinculadas al inventario existente.
CREATE TABLE IF NOT EXISTS promociones (
    id_promocion INT AUTO_INCREMENT PRIMARY KEY,
    id_videojuego INT NOT NULL UNIQUE,
    precio_oferta DECIMAL(10,2) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (id_videojuego)
        REFERENCES videojuegos(id_videojuego)
        ON DELETE CASCADE,
    CHECK (precio_oferta > 0)
);

-- Promociones iniciales de Gameverse.
INSERT INTO promociones (id_videojuego, precio_oferta)
SELECT id_videojuego,
    CASE titulo
        WHEN 'Resident Evil 2 Remake' THEN 24.99
        WHEN 'Skyrim Special Edition' THEN 29.99
        WHEN 'Metro Exodus Complete Edition' THEN 19.99
        WHEN 'Halo Infinite' THEN 29.99
        WHEN 'Super Mario Odyssey' THEN 39.99
    END
FROM videojuegos
WHERE titulo IN (
    'Resident Evil 2 Remake',
    'Skyrim Special Edition',
    'Metro Exodus Complete Edition',
    'Halo Infinite',
    'Super Mario Odyssey'
)
ON DUPLICATE KEY UPDATE activo = TRUE;

-- Registro independiente de futuros lanzamientos.
CREATE TABLE IF NOT EXISTS proximos_lanzamientos (
    id_lanzamiento INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL UNIQUE,
    descripcion TEXT NOT NULL,
    plataforma VARCHAR(100) NOT NULL,
    fecha_lanzamiento DATE NULL,
    imagen_url VARCHAR(255) NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

-- Verificar las promociones registradas.
SELECT v.titulo, v.precio AS precio_original,
       p.precio_oferta
FROM promociones p
JOIN videojuegos v
ON p.id_videojuego = v.id_videojuego;

SHOW TABLES;

-- Registrar futuros lanzamientos sin afectar el inventario.
INSERT INTO proximos_lanzamientos
(titulo, descripcion, plataforma, fecha_lanzamiento, activo)
VALUES
(
    'Metro 2039',
    'Regresa a las ruinas de Moscu en una aventura de supervivencia, exploracion y combate protagonizada por un antiguo guerrero espartano.',
    'PS5 / Xbox Series X',
    '2027-02-04',
    1
),
(
    'Pokemon Vientos',
    'Descubre una nueva aventura Pokemon en un mundo abierto lleno de islas, criaturas y paisajes naturales. Lanzamiento previsto para 2027.',
    'Switch 2',
    NULL,
    1
),
(
    'Pokemon Olas',
    'Explora un mundo de islas y oceanos, conoce nuevos Pokemon y comienza una aventura con tu companero inicial. Lanzamiento previsto para 2027.',
    'Switch 2',
    NULL,
    1
)
ON DUPLICATE KEY UPDATE activo = TRUE;

-- Verificar lanzamientos registrados.
SELECT titulo, plataforma, fecha_lanzamiento
FROM proximos_lanzamientos;
