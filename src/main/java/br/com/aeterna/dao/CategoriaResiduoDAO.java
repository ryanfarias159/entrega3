package br.com.aeterna.dao;

import br.com.aeterna.conexao.ConexaoFactory;
import br.com.aeterna.modelo.CategoriaResiduo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaResiduoDAO {

    // CREATE
    public Long inserir(CategoriaResiduo categoria) throws SQLException {
        String sql = "INSERT INTO public.categorias_residuos (nome, codigo, descricao) VALUES (?, ?, ?) RETURNING id";

        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categoria.getNome());
            stmt.setString(2, categoria.getCodigo());
            stmt.setString(3, categoria.getDescricao());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Long idGerado = rs.getLong("id");
                    categoria.setId(idGerado);
                    return idGerado;
                }
            }
        }
        return null;
    }

    // READ ALL
    public List<CategoriaResiduo> listar() throws SQLException {
        String sql = "SELECT id, nome, codigo, descricao, criado_em, atualizado_em " +
                     "FROM public.categorias_residuos ORDER BY id ASC";
        List<CategoriaResiduo> lista = new ArrayList<>();

        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearResultSet(rs));
            }
        }
        return lista;
    }

    // READ BY ID
    public CategoriaResiduo buscarPorId(Long id) throws SQLException {
        String sql = "SELECT id, nome, codigo, descricao, criado_em, atualizado_em " +
                     "FROM public.categorias_residuos WHERE id = ?";

        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSet(rs);
                }
            }
        }
        return null;
    }

    // UPDATE
    public boolean atualizar(CategoriaResiduo categoria) throws SQLException {
        String sql = "UPDATE public.categorias_residuos " +
                     "SET nome = ?, codigo = ?, descricao = ?, atualizado_em = CURRENT_TIMESTAMP " +
                     "WHERE id = ?";

        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categoria.getNome());
            stmt.setString(2, categoria.getCodigo());
            stmt.setString(3, categoria.getDescricao());
            stmt.setLong(4, categoria.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    // DELETE
    public boolean excluir(Long id) throws SQLException {
        String sql = "DELETE FROM public.categorias_residuos WHERE id = ?";

        try (Connection conn = ConexaoFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    private CategoriaResiduo mapearResultSet(ResultSet rs) throws SQLException {
        CategoriaResiduo cat = new CategoriaResiduo();
        cat.setId(rs.getLong("id"));
        cat.setNome(rs.getString("nome"));
        cat.setCodigo(rs.getString("codigo"));
        cat.setDescricao(rs.getString("descricao"));

        Timestamp criadoEm = rs.getTimestamp("criado_em");
        if (criadoEm != null) {
            cat.setCriadoEm(criadoEm.toLocalDateTime());
        }

        Timestamp atualizadoEm = rs.getTimestamp("atualizado_em");
        if (atualizadoEm != null) {
            cat.setAtualizadoEm(atualizadoEm.toLocalDateTime());
        }

        return cat;
    }
}