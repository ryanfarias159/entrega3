package br.com.aeterna.app;

import br.com.aeterna.dao.CategoriaResiduoDAO;
import br.com.aeterna.modelo.CategoriaResiduo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class AeternaTela extends JFrame {

    private final CategoriaResiduoDAO dao = new CategoriaResiduoDAO();

    private JTextField txtId;
    private JTextField txtNome;
    private JTextField txtCodigo;
    private JTextArea txtDescricao;
    private DefaultTableModel tableModel;
    private JTable tabela;

    public AeternaTela() {
        setTitle("Aeterna Biz - Gestão de Categorias de Resíduos (CRUD)");
        setSize(760, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(criarPainelFormulario(), BorderLayout.NORTH);
        add(criarPainelTabela(), BorderLayout.CENTER);

        carregarTabela();
    }

    private JPanel criarPainelFormulario() {
        JPanel container = new JPanel(new BorderLayout(8, 8));
        container.setBorder(BorderFactory.createTitledBorder("Dados da Categoria"));

        JPanel campos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtId = new JTextField(8);
        txtId.setEditable(false);
        txtNome = new JTextField(22);
        txtCodigo = new JTextField(12);
        txtDescricao = new JTextArea(2, 22);
        txtDescricao.setLineWrap(true);
        txtDescricao.setWrapStyleWord(true);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        campos.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.2;
        campos.add(txtId, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0;
        campos.add(new JLabel("Código:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 0.3;
        campos.add(txtCodigo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        campos.add(new JLabel("Nome *:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 3; gbc.weightx = 1;
        campos.add(txtNome, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        campos.add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 3; gbc.weightx = 1;
        campos.add(new JScrollPane(txtDescricao), gbc);
        gbc.gridwidth = 1;

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        JButton btnSalvar = new JButton("Salvar Novo");
        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnLimpar = new JButton("Limpar");

        btnSalvar.addActionListener(e -> salvarCategoria());
        btnAtualizar.addActionListener(e -> atualizarCategoria());
        btnExcluir.addActionListener(e -> excluirCategoria());
        btnLimpar.addActionListener(e -> limparCampos());

        botoes.add(btnSalvar);
        botoes.add(btnAtualizar);
        botoes.add(btnExcluir);
        botoes.add(btnLimpar);

        container.add(campos, BorderLayout.CENTER);
        container.add(botoes, BorderLayout.SOUTH);
        return container;
    }

    private JScrollPane criarPainelTabela() {
        String[] colunas = {"ID", "Nome", "Código", "Descrição"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(tableModel);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                preencherFormularioComLinhaSelecionada();
            }
        });

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createTitledBorder("Categorias Cadastradas"));
        return scroll;
    }

    private boolean validarEntrada(boolean exigirId) {
        if (exigirId && txtId.getText().trim().isEmpty()) {
            mostrarAviso("Selecione uma categoria na tabela antes de continuar.");
            return false;
        }

        String nome = txtNome.getText().trim();
        String codigo = txtCodigo.getText().trim();

        if (nome.isEmpty()) {
            mostrarAviso("O campo Nome é obrigatório.");
            txtNome.requestFocus();
            return false;
        }

        if (nome.length() > 100) {
            mostrarAviso("O campo Nome deve ter no máximo 100 caracteres.");
            txtNome.requestFocus();
            return false;
        }

        if (codigo.length() > 50) {
            mostrarAviso("O campo Código deve ter no máximo 50 caracteres.");
            txtCodigo.requestFocus();
            return false;
        }

        return true;
    }

    private CategoriaResiduo lerFormulario() {
        CategoriaResiduo cat = new CategoriaResiduo();
        if (!txtId.getText().trim().isEmpty()) {
            cat.setId(Long.parseLong(txtId.getText().trim()));
        }
        cat.setNome(txtNome.getText().trim());

        String codigo = txtCodigo.getText().trim();
        cat.setCodigo(codigo.isEmpty() ? null : codigo);

        String descricao = txtDescricao.getText().trim();
        cat.setDescricao(descricao.isEmpty() ? null : descricao);

        return cat;
    }

    private void salvarCategoria() {
        if (!validarEntrada(false)) {
            return;
        }

        try {
            CategoriaResiduo nova = lerFormulario();
            Long idGerado = dao.inserir(nova);
            carregarTabela();
            limparCampos();
            mostrarInfo("Categoria cadastrada com sucesso! ID: " + idGerado);
        } catch (SQLException ex) {
            mostrarErro(traduzirErroSql(ex));
        }
    }

    private void atualizarCategoria() {
        if (!validarEntrada(true)) {
            return;
        }

        try {
            CategoriaResiduo editada = lerFormulario();
            boolean atualizou = dao.atualizar(editada);
            carregarTabela();
            if (atualizou) {
                limparCampos();
                mostrarInfo("Categoria atualizada com sucesso.");
            } else {
                mostrarAviso("Nenhuma categoria encontrada com o ID informado.");
            }
        } catch (SQLException ex) {
            mostrarErro(traduzirErroSql(ex));
        }
    }

    private void excluirCategoria() {
        if (txtId.getText().trim().isEmpty()) {
            mostrarAviso("Selecione uma categoria na tabela para excluir.");
            return;
        }

        Long id = Long.parseLong(txtId.getText().trim());
        int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente excluir a categoria de ID " + id + "?",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacao != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            boolean excluiu = dao.excluir(id);
            carregarTabela();
            limparCampos();
            if (excluiu) {
                mostrarInfo("Categoria excluída com sucesso.");
            } else {
                mostrarAviso("Registro não encontrado para exclusão.");
            }
        } catch (SQLException ex) {
            mostrarErro(traduzirErroSql(ex));
        }
    }

    private void carregarTabela() {
        try {
            List<CategoriaResiduo> categorias = dao.listar();
            tableModel.setRowCount(0);
            for (CategoriaResiduo c : categorias) {
                tableModel.addRow(new Object[]{
                        c.getId(),
                        c.getNome(),
                        c.getCodigo(),
                        c.getDescricao()
                });
            }
        } catch (SQLException ex) {
            mostrarErro("Não foi possível carregar as categorias: " + traduzirErroSql(ex));
        }
    }

    private void preencherFormularioComLinhaSelecionada() {
        int linha = tabela.getSelectedRow();
        if (linha >= 0) {
            txtId.setText(String.valueOf(tableModel.getValueAt(linha, 0)));
            txtNome.setText(valorSeguro(tableModel.getValueAt(linha, 1)));
            txtCodigo.setText(valorSeguro(tableModel.getValueAt(linha, 2)));
            txtDescricao.setText(valorSeguro(tableModel.getValueAt(linha, 3)));
        }
    }

    private void limparCampos() {
        txtId.setText("");
        txtNome.setText("");
        txtCodigo.setText("");
        txtDescricao.setText("");
        tabela.clearSelection();
    }

    private String valorSeguro(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    private String traduzirErroSql(SQLException ex) {
        String state = ex.getSQLState();
        if ("23505".equals(state)) {
            return "Já existe um registro com esse Nome ou Código cadastrado.";
        }
        if ("23503".equals(state)) {
            return "Esta categoria está vinculada a outros registros e não pode ser removida.";
        }
        if (state != null && (state.startsWith("08") || "28P01".equals(state))) {
            return "Falha de conexão ou autenticação com o banco PostgreSQL.";
        }
        return "Erro de banco de dados: " + ex.getMessage();
    }

    private void mostrarInfo(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarAviso(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Atenção", JOptionPane.WARNING_MESSAGE);
    }

    private void mostrarErro(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AeternaTela().setVisible(true));
    }
}