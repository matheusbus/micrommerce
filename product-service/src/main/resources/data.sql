INSERT INTO categories (name) VALUES
  ('Informática'),
  ('Celulares'),
  ('Eletrodomésticos'),
  ('Eletrônicos'),
  ('Moda Masculina'),
  ('Moda Feminina'),
  ('Acessórios'),
  ('Calçados'),
  ('Beleza e Perfumaria'),
  ('Esporte e Lazer'),
  ('Alimentos e Bebidas'),
  ('Móveis'),
  ('Decoração'),
  ('Papelaria'),
  ('Brinquedos'),
  ('Pet Shop'),
  ('Ferramentas'),
  ('Automotivo'),
  ('Livros'),
  ('Filmes e Séries'),
  ('Games'),
  ('Instrumentos Musicais');

-- Produtos Eletrônicos
INSERT INTO products (id, public_id, name, description, price, active, created_at, category_id) VALUES
   (1, '550e8400-e29b-41d4-a716-446655440000', 'Smartphone X1', 'Celular com 128GB de memória e 6GB de RAM', 2499.9900, true, CURRENT_TIMESTAMP, 2),
   (2, '550e8400-e29b-41d4-a716-446655440001', 'Notebook Ultra 15', 'Notebook com Intel i7 e SSD de 512GB', 4999.9900, true, CURRENT_TIMESTAMP, 1);

-- Produtos Eletrodomésticos
INSERT INTO products (id, public_id, name, description, price, active, created_at, category_id) VALUES
   (3, '550e8400-e29b-41d4-a716-446655440002', 'Geladeira Frost Free', 'Geladeira com 400L e sistema frost free', 3299.9900, true, CURRENT_TIMESTAMP, 3),
   (4, '550e8400-e29b-41d4-a716-446655440003', 'Micro-ondas 20L', 'Micro-ondas digital com 10 níveis de potência', 599.9900, true, CURRENT_TIMESTAMP, 3);

-- Produtos Games
INSERT INTO products (id, public_id, name, description, price, active, created_at, category_id) VALUES
   (5, '550e8400-e29b-41d4-a716-446655440004', 'Console Gamer Pro', 'Console de última geração com 1TB de SSD', 3999.9900, true, CURRENT_TIMESTAMP, 21),
   (6, '550e8400-e29b-41d4-a716-446655440005', 'Controle sem fio', 'Controle ergonômico com bateria recarregável', 349.9900, true, CURRENT_TIMESTAMP, 21);

-- Produtos Acessórios
INSERT INTO products (id, public_id, name, description, price, active, created_at, category_id) VALUES
   (7, '550e8400-e29b-41d4-a716-446655440006', 'Fone Bluetooth', 'Fone de ouvido com cancelamento de ruído', 499.9900, true, CURRENT_TIMESTAMP, 7),
   (8, '550e8400-e29b-41d4-a716-446655440007', 'Carregador Turbo 30W', 'Carregador rápido para smartphones e tablets', 139.9900, true, CURRENT_TIMESTAMP, 7);

-- Produtos Casa e Decoração
INSERT INTO products (id, public_id, name, description, price, active, created_at, category_id) VALUES
   (9, '550e8400-e29b-41d4-a716-446655440008', 'Luminária de Mesa LED', 'Luminária com ajuste de intensidade e cor', 89.9900, true, CURRENT_TIMESTAMP, 13),
   (10, '550e8400-e29b-41d4-a716-446655440009', 'Ventilador Silencioso', 'Ventilador com motor silencioso e 3 velocidades', 219.9900, true, CURRENT_TIMESTAMP, 3);

-- Produtos Beleza e Saúde
INSERT INTO products (id, public_id, name, description, price, active, created_at, category_id) VALUES
   (11, '550e8400-e29b-41d4-a716-446655440010', 'Secador de Cabelos Pro', 'Secador com tecnologia iônica e 3 temperaturas', 289.9900, true, CURRENT_TIMESTAMP, 9),
   (12, '550e8400-e29b-41d4-a716-446655440011', 'Massageador Elétrico', 'Aparelho portátil para massagem corporal', 159.9900, true, CURRENT_TIMESTAMP, 9);
