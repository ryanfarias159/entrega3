package br.com.aeterna.app;

import br.com.aeterna.dao.CategoriaResiduoDAO;
import br.com.aeterna.modelo.CategoriaResiduo;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== AETERNA BIZ: DEMONSTRAÇÃO DA ENTREGA 3 ===\n");

        CategoriaResiduoDAO dao = new CategoriaResiduoDAO();

        CategoriaResiduo novaCat = new CategoriaResiduo();
        novaCat.setNome("Resíduos Eletrônicos Industriais");
        novaCat.setCodigo("ELE-02");
        novaCat.setDescricao("Placas de circuito, baterias e componentes de TI desativados.");

        try {
            System.out.println("[TESTE] Inserindo nova categoria no banco...");
            dao.inserir(novaCat);

            System.out.println("\n[TESTE] Listando categorias cadastradas no banco:");
            List<CategoriaResiduo> categorias = dao.listar();

            for (CategoriaResiduo c : categorias) {
                System.out.println(" - ID: " + c.getId());
                System.out.println("   Nome: " + c.getNome() + " | Código: " + c.getCodigo());
                System.out.println("   Criado em: " + c.getCriadoEm());
                System.out.println("--------------------------------------------------");
            }
        } catch (SQLException e) {
            System.err.println("Erro na operação com o banco de dados: " + e.getMessage());
        }
    }
}