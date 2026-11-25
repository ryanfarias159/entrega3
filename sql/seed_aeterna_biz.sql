-- ============================================================================
-- AETERNA BIZ - SCRIPT DE CARGA INICIAL (SEED)
-- ============================================================================

-- 1. Categorias de resíduos (IDs sequenciais gerados automaticamente: 1, 2, 3...)
INSERT INTO public.categorias_residuos (nome, codigo, descricao) VALUES
    ('Resíduos Eletrônicos Industriais', 'ELE-01', 'Placas eletrônicas, cabos, fontes e equipamentos de TI obsoletos.'),
    ('Polímeros Recicláveis', 'POL-02', 'Aparas plásticas, embalagens industriais e resinas reaproveitáveis.'),
    ('Metais Ferrosos e Não Ferrosos', 'MET-03', 'Sucatas de alumínio, cobre, aço e ligas metálicas.')
ON CONFLICT (nome) DO NOTHING;

-- 2. Empresas geradoras / receptoras
INSERT INTO public.empresas (nome, cnpj_cpf, email) VALUES
    ('Indústria Alpha S.A.', '12.345.678/0001-90', 'esg@industriaalpha.com.br'),
    ('EcoCiclo Logística Reversa Ltda', '98.765.432/0001-10', 'contato@ecociclo.com.br')
ON CONFLICT (cnpj_cpf) DO NOTHING;

-- 3. Centros de coleta com localização geográfica (PostGIS - SRID 4326)
INSERT INTO public.centros_coleta (nome, endereco, localizacao)
SELECT
    'Ecoponto Industrial Oeste',
    'Av. Brasil, 4500 - Cascavel/PR',
    ST_GeographyFromText('SRID=4326;POINT(-53.4550 -24.9555)')
WHERE NOT EXISTS (
    SELECT 1 FROM public.centros_coleta WHERE nome = 'Ecoponto Industrial Oeste'
);

INSERT INTO public.centros_coleta (nome, endereco, localizacao)
SELECT
    'Central de Triagem Sul',
    'Rua Paraná, 1200 - Cascavel/PR',
    ST_GeographyFromText('SRID=4326;POINT(-53.4620 -24.9610)')
WHERE NOT EXISTS (
    SELECT 1 FROM public.centros_coleta WHERE nome = 'Central de Triagem Sul'
);

-- 4. Tipos de resíduos vinculados às categorias existentes
INSERT INTO public.tipos_residuos (categoria_id, nome, descricao, unidade_medida)
SELECT
    c.id,
    'Placas de Circuito Impresso',
    'Componentes eletrônicos retirados de hardware industrial.',
    'kg'
FROM public.categorias_residuos c
WHERE c.codigo = 'ELE-01'
ON CONFLICT (nome) DO NOTHING;

INSERT INTO public.tipos_residuos (categoria_id, nome, descricao, unidade_medida)
SELECT
    c.id,
    'Polímeros PET / PEAD',
    'Sobras de produção e embalagens plásticas limpas.',
    'kg'
FROM public.categorias_residuos c
WHERE c.codigo = 'POL-02'
ON CONFLICT (nome) DO NOTHING;

-- 5. Unidades operacionais vinculadas às empresas existentes
INSERT INTO public.unidades_operacionais (empresa_id, nome, endereco, localizacao)
SELECT
    e.id,
    'Planta Industrial Sul',
    'Rua das Indústrias, 800 - Cascavel/PR',
    ST_GeographyFromText('SRID=4326;POINT(-53.4700 -24.9700)')
FROM public.empresas e
WHERE e.cnpj_cpf = '12.345.678/0001-90'
  AND NOT EXISTS (
      SELECT 1 FROM public.unidades_operacionais u WHERE u.nome = 'Planta Industrial Sul'
  );

INSERT INTO public.unidades_operacionais (empresa_id, nome, endereco, localizacao)
SELECT
    e.id,
    'Unidade de Triagem e Destinação',
    'Rodovia BR-277, km 590 - Cascavel/PR',
    ST_GeographyFromText('SRID=4326;POINT(-53.4800 -24.9500)')
FROM public.empresas e
WHERE e.cnpj_cpf = '98.765.432/0001-10'
  AND NOT EXISTS (
      SELECT 1 FROM public.unidades_operacionais u WHERE u.nome = 'Unidade de Triagem e Destinação'
  );

-- 6. Passaportes de resíduos
INSERT INTO public.passaportes_residuos (
    codigo_rastreio,
    volume,
    status_conformidade,
    taxa_desvio_aterro,
    unidade_origem_id,
    unidade_destino_id,
    tipo_residuo_id,
    metadados_auditoria
)
SELECT
    'PDR-10001',
    1250.5000,
    'AUDITADO'::public."StatusConformidade",
    98.40,
    uo_orig.id,
    uo_dest.id,
    tr.id,
    '{"lote": "L-2026-11", "responsavel": "Auditoria Ambiental Aeterna"}'::jsonb
FROM public.unidades_operacionais uo_orig
CROSS JOIN public.unidades_operacionais uo_dest
CROSS JOIN public.tipos_residuos tr
WHERE uo_orig.nome = 'Planta Industrial Sul'
  AND uo_dest.nome = 'Unidade de Triagem e Destinação'
  AND tr.nome = 'Polímeros PET / PEAD'
ON CONFLICT (codigo_rastreio) DO NOTHING;