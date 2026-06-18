-- Troca o catálogo de exemplo (celulares) por peças automotivas reais.
-- Moeda mista: importadas em USD (convertidas pelo currency-service), nacionais em BRL.
-- Imagens: loremflickr (foto real por palavra-chave, fixada por lock).

-- limpa avaliações e produtos antigos (tb_review referencia product_id)
DELETE FROM tb_review;
DELETE FROM tb_product;

INSERT INTO tb_product
    (description, brand, model, currency, price, stock, image_url, category, seller_id, seller_name) VALUES
-- Injeção Eletrônica (FuelTech — nacional, BRL)
('FuelTech FT550 Injeção Programável', 'FuelTech', 'FT550', 'BRL', 2490.00, 15, 'https://loremflickr.com/600/400/engine?lock=1',     'Injeção Eletrônica', 1, 'AutoParts Oficial'),
('FuelTech FT450 Injeção Programável', 'FuelTech', 'FT450', 'BRL', 1990.00, 12, 'https://loremflickr.com/600/400/engine?lock=2',     'Injeção Eletrônica', 1, 'AutoParts Oficial'),
('FuelTech FT600 Injeção e Ignição',   'FuelTech', 'FT600', 'BRL', 3290.00,  8, 'https://loremflickr.com/600/400/engine?lock=3',     'Injeção Eletrônica', 1, 'Performance Racing'),
('FuelTech Racepro 600 Standalone',    'FuelTech', 'Racepro 600', 'BRL', 4500.00, 5, 'https://loremflickr.com/600/400/engine?lock=4', 'Injeção Eletrônica', 1, 'Performance Racing'),
('FuelTech Dash 5.0 Display Digital',  'FuelTech', 'Dash 5.0', 'BRL', 2800.00, 7, 'https://loremflickr.com/600/400/dashboard?lock=5', 'Injeção Eletrônica', 1, 'AutoParts Oficial'),
('Sonda Wideband Nano O2 Banda Larga', 'FuelTech', 'Nano O2', 'BRL', 690.00, 20, 'https://loremflickr.com/600/400/sensor?lock=6',     'Injeção Eletrônica', 1, 'AutoParts Oficial'),
-- Turbinas
('Turbina Garrett GTX3582R Gen II',    'Garrett',     'GTX3582R',  'USD', 1450.00, 6, 'https://loremflickr.com/600/400/turbocharger?lock=7',  'Turbina', 1, 'Turbo Brasil'),
('Turbina BorgWarner EFR 7670',        'BorgWarner',  'EFR 7670',  'USD', 1890.00, 4, 'https://loremflickr.com/600/400/turbocharger?lock=8',  'Turbina', 1, 'Turbo Brasil'),
('Turbina Master Power R474 Roller',   'Master Power','R474',      'BRL', 2200.00, 10,'https://loremflickr.com/600/400/turbo?lock=9',         'Turbina', 1, 'AutoParts Oficial'),
('Turbina Garrett G25-660',            'Garrett',     'G25-660',   'USD', 1650.00, 5, 'https://loremflickr.com/600/400/turbocharger?lock=10', 'Turbina', 1, 'Turbo Brasil'),
-- Motor
('Pistão Forjado Mahle Motorsport (jogo 4)', 'Mahle',     'CD-30',  'BRL', 1800.00, 9, 'https://loremflickr.com/600/400/piston?lock=11',  'Motor', 1, 'Race Motorsport'),
('Pistão Forjado JE Pistons SRP (jogo 4)',   'JE Pistons','SRP',    'USD', 950.00,  7, 'https://loremflickr.com/600/400/piston?lock=12',  'Motor', 1, 'Race Motorsport'),
('Comando de Válvulas SPA 280°',             'SPA',       '280',    'BRL', 1200.00, 11,'https://loremflickr.com/600/400/camshaft?lock=13','Motor', 1, 'Performance Racing'),
('Biela Forjada Eagle H-Beam (jogo 4)',      'Eagle',     'H-Beam', 'USD', 780.00,  8, 'https://loremflickr.com/600/400/engine?lock=14',  'Motor', 1, 'Race Motorsport'),
('Bronzina de Biela Mahle STD',              'Mahle',     'STD',    'BRL', 320.00,  25,'https://loremflickr.com/600/400/bearing?lock=15', 'Motor', 1, 'AutoParts Oficial'),
('Junta de Cabeçote Cometic MLS',            'Cometic',   'MLS',    'USD', 240.00,  14,'https://loremflickr.com/600/400/gasket?lock=16',  'Motor', 1, 'Performance Racing'),
('Volante Motor Billet Aço Cromo',           'TM Racing', 'Billet', 'BRL', 980.00,  9, 'https://loremflickr.com/600/400/flywheel?lock=17','Motor', 1, 'Race Motorsport'),
-- Alimentação
('Bico Injetor Bosch 1000cc (jogo 4)',   'Bosch',     '1000cc',     'BRL', 1600.00, 13,'https://loremflickr.com/600/400/injector?lock=18',   'Alimentação', 1, 'AutoParts Oficial'),
('Bomba de Combustível Walbro 460lph',   'Walbro',    'F90000267',  'USD', 180.00,  18,'https://loremflickr.com/600/400/fuel?lock=19',       'Alimentação', 1, 'Performance Racing'),
('Intercooler Air-to-Air 600x300x76',    'TM Racing', '600x300',    'BRL', 1400.00, 10,'https://loremflickr.com/600/400/intercooler?lock=20','Alimentação', 1, 'Race Motorsport'),
('Coletor de Admissão Race Billet',      'TM Racing', 'Race',       'BRL', 890.00,  12,'https://loremflickr.com/600/400/manifold?lock=21',   'Alimentação', 1, 'Race Motorsport'),
-- Suspensão
('Amortecedor Esportivo Cofap Turbo Gás (par)', 'Cofap',   'Turbo Gás', 'BRL', 480.00, 22,'https://loremflickr.com/600/400/suspension?lock=22','Suspensão', 1, 'AutoParts Oficial'),
('Kit Coilover Bilstein B14 PSS',               'Bilstein','B14',       'USD', 1100.00, 6,'https://loremflickr.com/600/400/suspension?lock=23','Suspensão', 1, 'Performance Racing'),
('Mola Esportiva Eibach Pro-Kit',               'Eibach',  'Pro-Kit',   'USD', 380.00,  14,'https://loremflickr.com/600/400/suspension?lock=24','Suspensão', 1, 'Performance Racing'),
-- Freios
('Kit Big Brake Brembo GT 4-pistões', 'Brembo', 'GT',         'USD', 2400.00, 4, 'https://loremflickr.com/600/400/brake?lock=25', 'Freios', 1, 'Performance Racing'),
('Pastilha de Freio Cerâmica HiPa',   'HiPa',   'Ceramic',    'BRL', 290.00,  30,'https://loremflickr.com/600/400/brake?lock=26', 'Freios', 1, 'AutoParts Oficial'),
('Disco de Freio Ventilado Fremax',   'Fremax', 'BD-Series',  'BRL', 360.00,  20,'https://loremflickr.com/600/400/brake?lock=27', 'Freios', 1, 'AutoParts Oficial'),
-- Escapamento
('Escapamento Inox Full 3 polegadas', 'TM Racing', 'Full 3"', 'BRL', 1700.00, 8, 'https://loremflickr.com/600/400/exhaust?lock=28', 'Escapamento', 1, 'Race Motorsport'),
-- Transmissão
('Embreagem Multidisco Cerâmica',     'Ceramic Power', 'Multi','USD', 890.00,  7, 'https://loremflickr.com/600/400/clutch?lock=29', 'Transmissão', 1, 'Performance Racing'),
-- Arrefecimento
('Radiador de Alumínio Race 3 Fileiras','TM Racing', 'Alu 3R','BRL', 760.00,  11,'https://loremflickr.com/600/400/radiator?lock=30','Arrefecimento', 1, 'Race Motorsport'),
-- Elétrica
('Vela de Ignição NGK Iridium IX (jogo 4)','NGK', 'Iridium IX','BRL', 220.00, 35,'https://loremflickr.com/600/400/engine?lock=31', 'Elétrica', 1, 'AutoParts Oficial'),
('Bateria 60Ah Heliar Free',           'Heliar', '60Ah',       'BRL', 540.00,  16,'https://loremflickr.com/600/400/battery?lock=32','Elétrica', 1, 'AutoParts Oficial');
