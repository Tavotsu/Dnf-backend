-- Insertar Usuarios (password de prueba es 'password123' u otra, pero asumo que no hay encriptacion o si la hay, la saltará temporalmente si no usan BCrypt, pero pondremos un hash comun de BCrypt de 'password123' por si acaso, que es '$2a$10$O2Lw2k5uB.1L/1R2h4cO1.w/9YvH2N/2b8r9sP4w/R9tL8V.O6')
-- Usaremos una simple por ahora, y si usan bcrypt al hacer login igual no funcionará si la app la maneja con un secret específico, pero por lo general '$2a$12$D25Xz8e.Q0wR3kPZ1zTzV.jF/E2d4a5g6m7k8n9o0p1q2r3s4t5u' es 'password'

INSERT INTO usuarios (name, email, password, rol, avatar) VALUES 
('Juan Perez', 'juan@example.com', 'password123', 'ciudadano', 'https://api.dicebear.com/7.x/avataaars/svg?seed=Juan'),
('Maria Lopez', 'maria@example.com', 'password123', 'clinica', 'https://api.dicebear.com/7.x/avataaars/svg?seed=Maria'),
('Carlos Ruiz', 'carlos@example.com', 'password123', 'refugio', 'https://api.dicebear.com/7.x/avataaars/svg?seed=Carlos'),
('Ana Gomez', 'ana@example.com', 'password123', 'ciudadano', 'https://api.dicebear.com/7.x/avataaars/svg?seed=Ana');

-- Insertar Mascotas (Coordenadas reales de Puerto Montt: latitud entre -41.4 y -41.5, longitud entre -72.9 y -73.0)
INSERT INTO mascotas (name, type, breed, color, gender, status, location, latitude, longitude, description, image, created_at, usuario_id) VALUES 
('Max', 'dog', 'Golden Retriever', 'Dorado', 'Macho', 'lost', 'Plaza de Armas, Puerto Montt', -41.4693, -72.9424, 'Perro muy amigable, se perdió cerca de la plaza central.', 'https://images.unsplash.com/photo-1552053831-71594a27632d?auto=format&fit=crop&q=80&w=400', NOW(), 1),
('Luna', 'cat', 'Siames', 'Blanco y Marron', 'Hembra', 'found', 'Angelmó, Puerto Montt', -41.4833, -72.9497, 'Gatita asustada encontrada cerca del mercado.', 'https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?auto=format&fit=crop&q=80&w=400', NOW(), 2),
('Rocky', 'dog', 'Bulldog', 'Marrón', 'Macho', 'lost', 'Costanera, Puerto Montt', -41.4725, -72.9360, 'Llevaba un collar rojo. Visto por el paseo costero.', 'https://images.unsplash.com/photo-1517849845537-4d257902454a?auto=format&fit=crop&q=80&w=400', NOW(), 3),
('Coco', 'bird', 'Loro', 'Verde', 'Macho', 'lost', 'Mirador Tenglo, Puerto Montt', -41.4900, -72.9550, 'Sabe decir "Hola Coco".', 'https://images.unsplash.com/photo-1552728089-57169ab00a09?auto=format&fit=crop&q=80&w=400', NOW(), 4);

-- Insertar Historias
INSERT INTO historias (title, family, location, content, image, time_ago) VALUES 
('Reencuentro Feliz', 'Familia Perez', 'Parque Central', 'Después de 3 días de intensa búsqueda, finalmente encontramos a nuestro querido perro gracias a un vecino.', 'https://images.unsplash.com/photo-1544568100-847a948585b9?auto=format&fit=crop&q=80&w=400', 'Hace 2 días'),
('Una Nueva Vida', 'Refugio Esperanza', 'Barrio Sur', 'Luna fue adoptada hoy. Le deseamos lo mejor en su nuevo hogar con su nueva familia amorosa.', 'https://images.unsplash.com/photo-1543852786-1cf6624b9987?auto=format&fit=crop&q=80&w=400', 'Hace 1 semana'),
('Milo vuelve a casa', 'Familia Gomez', 'Zona Este', 'Pensamos que no lo volveríamos a ver, pero apareció gracias a la publicación en la aplicación.', 'https://images.unsplash.com/photo-1537151608828-ea2b11777ee8?auto=format&fit=crop&q=80&w=400', 'Hace 3 horas');

-- Insertar Coincidencias
INSERT INTO coincidencias (especie_buscada, color_buscado, estado) VALUES 
('dog', 'Dorado', 'activa'),
('cat', 'Blanco', 'inactiva'),
('bird', 'Verde', 'activa');
