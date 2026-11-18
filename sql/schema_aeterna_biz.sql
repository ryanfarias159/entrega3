-- ============================================================================
-- AETERNA BIZ - SCRIPT DDL DE CRIAÇÃO DO BANCO DE DADOS (POSTGRESQL + POSTGIS)
-- ============================================================================

-- 1. Habilitação de extensões necessárias (UUID e Geoprocessamento PostGIS)
CREATE EXTENSION IF NOT EXISTS "pgcrypto";
CREATE EXTENSION IF NOT EXISTS "postgis";

-- 2. Criação de tipos enumerados (ENUM)
DO $$ BEGIN
    CREATE TYPE public."StatusConformidade" AS ENUM (
        'PENDENTE',
        'CONFORME',
        'NAO_CONFORME',
        'AUDITADO'
    );
EXCEPTION
    WHEN duplicate_object THEN NULL;
END $$;

-- ============================================================================
-- 3. TABELAS INDEPENDENTES (SEM CHAVES ESTRANGEIRAS)
-- ============================================================================

-- Tabela: categorias_residuos
CREATE TABLE IF NOT EXISTS public.categorias_residuos
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    nome character varying(100) COLLATE pg_catalog."default" NOT NULL,
    codigo character varying(50) COLLATE pg_catalog."default",
    descricao text COLLATE pg_catalog."default",
    criado_em timestamp(3) without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em timestamp(3) without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT categorias_residuos_pkey PRIMARY KEY (id),
    CONSTRAINT categorias_residuos_codigo_key UNIQUE (codigo),
    CONSTRAINT categorias_residuos_nome_key UNIQUE (nome)
);

-- Tabela: empresas
CREATE TABLE IF NOT EXISTS public.empresas
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    nome character varying(255) COLLATE pg_catalog."default" NOT NULL,
    cnpj_cpf character varying(50) COLLATE pg_catalog."default" NOT NULL,
    email character varying(255) COLLATE pg_catalog."default" NOT NULL,
    criado_em timestamp(3) without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em timestamp(3) without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT empresas_pkey PRIMARY KEY (id),
    CONSTRAINT empresas_cnpj_cpf_key UNIQUE (cnpj_cpf),
    CONSTRAINT empresas_email_key UNIQUE (email)
);

-- Tabela: centros_coleta
CREATE TABLE IF NOT EXISTS public.centros_coleta
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    nome character varying(255) COLLATE pg_catalog."default" NOT NULL,
    endereco text COLLATE pg_catalog."default" NOT NULL,
    localizacao geography(Point,4326),
    criado_em timestamp(3) without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em timestamp(3) without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT centros_coleta_pkey PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_centros_coleta_localizacao
    ON public.centros_coleta USING gist (localizacao);

-- ============================================================================
-- 4. TABELAS DEPENDENTES (COM CHAVES ESTRANGEIRAS)
-- ============================================================================

-- Tabela: tipos_residuos
CREATE TABLE IF NOT EXISTS public.tipos_residuos
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    categoria_id uuid NOT NULL,
    nome character varying(100) COLLATE pg_catalog."default" NOT NULL,
    descricao text COLLATE pg_catalog."default",
    unidade_medida character varying(20) COLLATE pg_catalog."default" NOT NULL DEFAULT 'kg'::character varying,
    criado_em timestamp(3) without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em timestamp(3) without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT tipos_residuos_pkey PRIMARY KEY (id),
    CONSTRAINT tipos_residuos_nome_key UNIQUE (nome),
    CONSTRAINT tipos_residuos_categoria_id_fkey FOREIGN KEY (categoria_id)
        REFERENCES public.categorias_residuos (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
);

CREATE INDEX IF NOT EXISTS idx_tipos_residuos_categoria
    ON public.tipos_residuos USING btree (categoria_id ASC NULLS LAST);

-- Tabela: unidades_operacionais
CREATE TABLE IF NOT EXISTS public.unidades_operacionais
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    empresa_id uuid NOT NULL,
    nome character varying(255) COLLATE pg_catalog."default" NOT NULL,
    endereco text COLLATE pg_catalog."default" NOT NULL,
    localizacao geography(Point,4326),
    criado_em timestamp(3) without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em timestamp(3) without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unidades_operacionais_pkey PRIMARY KEY (id),
    CONSTRAINT unidades_operacionais_empresa_id_fkey FOREIGN KEY (empresa_id)
        REFERENCES public.empresas (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_unidades_empresa
    ON public.unidades_operacionais USING btree (empresa_id ASC NULLS LAST);

CREATE INDEX IF NOT EXISTS idx_unidades_localizacao
    ON public.unidades_operacionais USING gist (localizacao);

-- Tabela: passaportes_residuos
CREATE TABLE IF NOT EXISTS public.passaportes_residuos
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    codigo_rastreio character varying(100) COLLATE pg_catalog."default" NOT NULL,
    volume numeric(12,4) NOT NULL,
    status_conformidade public."StatusConformidade" NOT NULL DEFAULT 'PENDENTE'::public."StatusConformidade",
    taxa_desvio_aterro numeric(5,2) NOT NULL,
    unidade_origem_id uuid NOT NULL,
    unidade_destino_id uuid NOT NULL,
    tipo_residuo_id uuid NOT NULL,
    metadados_auditoria jsonb,
    criado_em timestamp(3) without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em timestamp(3) without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT passaportes_residuos_pkey PRIMARY KEY (id),
    CONSTRAINT passaportes_residuos_codigo_rastreio_key UNIQUE (codigo_rastreio),
    CONSTRAINT passaportes_residuos_volume_check CHECK (volume > 0),
    CONSTRAINT passaportes_residuos_taxa_desvio_check CHECK (taxa_desvio_aterro >= 0 AND taxa_desvio_aterro <= 100),
    CONSTRAINT passaportes_residuos_tipo_residuo_id_fkey FOREIGN KEY (tipo_residuo_id)
        REFERENCES public.tipos_residuos (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT passaportes_residuos_unidade_destino_id_fkey FOREIGN KEY (unidade_destino_id)
        REFERENCES public.unidades_operacionais (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT passaportes_residuos_unidade_origem_id_fkey FOREIGN KEY (unidade_origem_id)
        REFERENCES public.unidades_operacionais (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE INDEX IF NOT EXISTS idx_passaportes_codigo
    ON public.passaportes_residuos USING btree (codigo_rastreio COLLATE pg_catalog."default" ASC NULLS LAST);

CREATE INDEX IF NOT EXISTS idx_passaportes_status
    ON public.passaportes_residuos USING btree (status_conformidade ASC NULLS LAST);