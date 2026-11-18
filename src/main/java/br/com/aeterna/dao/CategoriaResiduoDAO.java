package br.com.aeterna.dao;

import br.com.aeterna.conexao.ConexaoFactory;
import br.com.aeterna.modelo.CategoriaResiduo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaResiduoDAO {

    // 1. INSERÇÃO (INSERT) com PreparedStatement
    public void inserir(CategoriaResiduo categoria) {
        String sql = "INSERT INTO public.categorias_residuos (nome, codigo, descricao) VALUES (?, ?, ?)";

        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, categoria.getNome());
            stmt.setString(2, categoria.getCodigo());
            stmt.setString(3, categoria.getDescricao());

            stmt.executeUpdate();

            // Recupera o ID numérico sequencial gerado automaticamente pelo banco
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    categoria.setId(rs.getLong(1));
                }
            }
            System.out.println("-> Categoria inserida com sucesso! ID gerado: " + categoria.getId());

        } catch (SQLException e) {
            System.err.println("Erro ao inserir categoria: " + e.getMessage());
        }
    }

    // 2. CONSULTA GERAL (SELECT *) com mapeamento ResultSet -> Objeto Java
    public List<CategoriaResiduo> listar() {
        String sql = "SELECT id, nome, codigo, descricao, criado_em, atualizado_em FROM public.categorias_residuos";
        List<CategoriaResiduo> lista = new ArrayList<>();

        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                CategoriaResiduo cat = new CategoriaResiduo();
                
                // Mapeamento dos tipos do Banco para Java (Long)
                cat.setId(rs.getLong("id"));
                cat.setNome(rs.getString("nome"));
                cat.setCodigo(rs.getString("codigo"));
                cat.setDescricao(rs.getString("descricao"));
                
                Timestamp criadoEmTs = rs.getTimestamp("criado_em");
                if (criadoEmTs != null) {
                    cat.setCriadoEm(criadoEmTs.toLocalDateTime());
                }

                Timestamp atualizadoEmTs = rs.getTimestamp("atualizado_em");
                if (atualizadoEmTs != null) {
                    cat.setAtualizadoEm(atualizadoEmTs.toLocalDateTime());
                }

                lista.add(cat);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar categorias: " + e.getMessage());
        }

        return lista;
    }
}