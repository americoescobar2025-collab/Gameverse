START TRANSACTION;

-- Catalogo de videojuegos fisicos para consolas.
-- Los precios y existencias son datos de demostracion.
-- Se conservan los registros anteriores.

INSERT INTO videojuegos
(titulo, descripcion, precio, stock, plataforma,
 desarrollador, fecha_lanzamiento, activo, id_categoria)

SELECT nuevos.*
FROM (

    -- PLAYSTATION

    SELECT
    'Resident Evil 2 Remake' AS titulo,
    'Sobrevive al desastre de Raccoon City junto a Leon Kennedy y Claire Redfield. Explora escenarios aterradores, resuelve acertijos y enfrenta criaturas infectadas.' AS descripcion,
    39.99 AS precio,
    15 AS stock,
    'PS4' AS plataforma,
    'Capcom' AS desarrollador,
    '2019-01-25' AS fecha_lanzamiento,
    1 AS activo,
    1 AS id_categoria

    UNION ALL SELECT
    'Skyrim Special Edition',
    'Explora las tierras de Skyrim como el Sangre de Dragon. Descubre ciudades, mazmorras, criaturas legendarias y una aventura de fantasia en mundo abierto.',
    39.99, 12, 'PS4',
    'Bethesda Game Studios', '2016-10-28', 1, 3

    UNION ALL SELECT
    'Metro Exodus Complete Edition',
    'Acompana a Artyom y los supervivientes del metro de Moscu en un viaje por una Rusia postapocaliptica llena de peligros y criaturas mutantes.',
    29.99, 10, 'PS5',
    '4A Games', '2021-06-18', 1, 1

    UNION ALL SELECT
    'Warhammer 40000: Space Marine 2',
    'Encarnando al teniente Titus, defiende al Imperio de la humanidad frente a las hordas tiranidas y las fuerzas del Caos.',
    59.99, 10, 'PS5',
    'Saber Interactive', '2024-09-09', 1, 1

    UNION ALL SELECT
    'BioShock: The Collection',
    'Descubre los secretos de Rapture y Columbia en una coleccion que incluye las aventuras principales de la saga BioShock.',
    29.99, 15, 'PS4',
    '2K Games', '2016-09-13', 1, 1

    UNION ALL SELECT
    'Silent Hill 2 Remake',
    'Acompana a James Sunderland en su regreso a Silent Hill, una ciudad cubierta de niebla donde debera enfrentar sus recuerdos y temores.',
    69.99, 10, 'PS5',
    'Bloober Team', '2024-10-08', 1, 2

    -- XBOX

    UNION ALL SELECT
    'Halo Infinite',
    'Asume el papel del Jefe Maestro y enfrenta a los Desterrados en una aventura de ciencia ficcion ambientada en el anillo Zeta Halo.',
    39.99, 5, 'Xbox Series X',
    '343 Industries', '2021-12-08', 1, 1

    UNION ALL SELECT
    'Gears 5',
    'Acompana a Kait Diaz en una aventura para descubrir su pasado mientras combate a las fuerzas del Enjambre.',
    29.99, 5, 'Xbox One',
    'The Coalition', '2019-09-10', 1, 1

    -- NINTENDO SWITCH 2

    UNION ALL SELECT
    'Mario Kart World',
    'Compite con Mario y sus amigos en carreras que conectan diferentes regiones. Explora circuitos, participa en competiciones y descubre nuevas rutas.',
    79.99, 10, 'Switch 2',
    'Nintendo', '2025-06-05', 1, 4

    UNION ALL SELECT
    'Zelda: Breath of the Wild - Switch 2 Edition',
    'Explora el reino de Hyrule como Link en una edicion mejorada para Nintendo Switch 2, con mejoras visuales y de rendimiento.',
    69.99, 8, 'Switch 2',
    'Nintendo', '2025-06-05', 1, 2

    -- NINTENDO SWITCH

    UNION ALL SELECT
    'The Legend of Zelda: Breath of the Wild',
    'Despierta como Link y recorre un vasto reino de Hyrule. Explora santuarios, resuelve acertijos y enfrenta a la calamidad Ganon.',
    59.99, 8, 'Switch',
    'Nintendo', '2017-03-03', 1, 2

    UNION ALL SELECT
    'Pokemon Legends: Arceus',
    'Viaja a la antigua region de Hisui, captura Pokemon, explora diferentes ecosistemas y ayuda a completar la primera Pokedex de la region.',
    59.99, 10, 'Switch',
    'Game Freak', '2022-01-28', 1, 3

    UNION ALL SELECT
    'Super Mario Odyssey',
    'Viaja junto a Mario y Cappy por diferentes reinos para rescatar a la princesa Peach. Descubre secretos y colecciona energilunas.',
    49.99, 10, 'Switch',
    'Nintendo', '2017-10-27', 1, 2

) AS nuevos

-- Evitar duplicados al ejecutar nuevamente el script.
WHERE NOT EXISTS (
    SELECT 1
    FROM videojuegos v
    WHERE v.titulo = nuevos.titulo
    AND v.plataforma = nuevos.plataforma
);

COMMIT;

-- Verificar los videojuegos registrados.
SELECT id_videojuego, titulo, plataforma, precio, stock
FROM videojuegos
ORDER BY id_videojuego;

-- Verificar cantidad total.
SELECT COUNT(*) AS total_videojuegos
FROM videojuegos;

-- Incorporar Ocarina of Time para Nintendo Switch 2.
INSERT INTO videojuegos
(titulo, descripcion, precio, stock, plataforma,
desarrollador, fecha_lanzamiento, activo, id_categoria)

SELECT
'The Legend of Zelda: Ocarina of Time',
'Acompana a Link y Navi en una aventura a traves del tiempo para detener a Ganondorf. Disfruta de graficos renovados, musica orquestal, mazmorras y nuevas mecanicas de juego.',
69.99, 10, 'Switch 2',
'Nintendo', '2026-11-05', 1, 2

WHERE NOT EXISTS (
    SELECT 1 FROM videojuegos
    WHERE titulo = 'The Legend of Zelda: Ocarina of Time'
    AND plataforma = 'Switch 2'
);
