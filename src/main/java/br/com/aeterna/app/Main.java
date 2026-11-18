package br.com.aeterna.app;

import br.com.aeterna.dao.CategoriaResiduoDAO;
import br.com.aeterna.modelo.CategoriaResiduo;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== AETERNA BIZ: DEMONSTRAÇÃO DA ENTREGA 3 ==\n");

        CategoriaResiduoDAO dao = new CategoriaResiduoDAO();

        // 1. Criando um objeto de teste em memória
        CategoriaResiduo novaCat = new CategoriaResiduo();
        novaCat.setNome("Resíduos Eletrônicos Industriais");
        novaCat.setCodigo("ELE-02");
        novaCat.setDescricao("Placas de circuito, baterias e componentes de TI desativados.");
       
        System.out.println("[TESTE] Inserindo nova categoria no banco...");
        dao.inserir(novaCat);

        // 3. Testando a Consulta (SELECT) e o Mapeamento
        System.out.println("\n[TESTE] Listando categorias cadastradas no banco:");
        List<CategoriaResiduo> categorias = dao.listar();

        for (CategoriaResiduo c : categorias) {
            System.out.println(" - ID: " + c.getId());
            System.out.println("   Nome: " + c.getNome() + " | Código: " + c.getCodigo());
            System.out.println("   Criado em: " + c.getCriadoEm());
            System.out.println("--------------------------------------------------");
        }
    }
}