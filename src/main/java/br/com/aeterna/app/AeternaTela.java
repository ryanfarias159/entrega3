package br.com.aeterna.app;

import br.com.aeterna.dao.CategoriaResiduoDAO;
import br.com.aeterna.modelo.CategoriaResiduo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AeternaTela extends JFrame {

    private JTextField txtNome;
    private JTextField txtCodigo;
    private JTextArea txtDescricao;
    private CategoriaResiduoDAO dao;

    public AeternaTela() {
        dao = new CategoriaResiduoDAO();

        setTitle("Aeterna Biz - Cadastro de Categorias de Resíduos");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Painel Principal com Layout de Grade/Margens
        JPanel painel = new JPanel();
        painel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campo: Nome
        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(new JLabel("Nome da Categoria:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        txtNome = new JTextField(20);
        painel.add(txtNome, gbc);

        // Campo: Código
        gbc.gridx = 0; gbc.gridy = 1;
        painel.add(new JLabel("Código (ex: ELE-01):"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        txtCodigo = new JTextField(20);
        painel.add(txtCodigo, gbc);

        // Campo: Descrição
        gbc.gridx = 0; gbc.gridy = 2;
        painel.add(new JLabel("Descrição:"), gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        txtDescricao = new JTextArea(4, 20);
        txtDescricao.setLineWrap(true);
        JScrollPane scrollDescricao = new JScrollPane(txtDescricao);
        painel.add(scrollDescricao, gbc);

        // Botão de Salvar
        gbc.gridx = 1; gbc.gridy = 3;
        JButton btnSalvar = new JButton("Salvar no Banco");
        btnSalvar.setBackground(new Color(46, 139, 87));
        btnSalvar.setForeground(Color.WHITE);
        painel.add(btnSalvar, gbc);

        // Ação do Botão (Integração com o DAO)
        btnSalvar.addActionListener((ActionEvent e) -> {
            String nome = txtNome.getText().trim();
            String codigo = txtCodigo.getText().trim();
            String descricao = txtDescricao.getText().trim();

            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(this, "O campo Nome é obrigatório!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            CategoriaResiduo cat = new CategoriaResiduo();
            cat.setNome(nome);
            cat.setCodigo(codigo.isEmpty() ? null : codigo);
            cat.setDescricao(descricao.isEmpty() ? null : descricao);

            try {
                dao.inserir(cat);
                JOptionPane.showMessageDialog(this, "Categoria cadastrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                
                // Limpa os campos após salvar
                txtNome.setText("");
                txtCodigo.setText("");
                txtDescricao.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao inserir: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(painel);
    }

    public static void main(String[] args) {
        // Executa a interface na thread gráfica padrão do Java
        SwingUtilities.invokeLater(() -> {
            new AeternaTela().setVisible(true);
        });
    }
}