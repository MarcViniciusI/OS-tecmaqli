/*
 * The MIT License
 *
 * Copyright 2024 Marcos Vinicius.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package br.com.tecmaqli.telas;

import java.sql.*;
import br.com.tecmaqli.dal.ModuloConexao;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;

/**
 * Tela de gestão de clientes
 *
 * @author Marcos Vinicius
 */
public class TelaClientes extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Creates new form TelaClientes
     */
    public TelaClientes() {
        initComponents();
        conexao = ModuloConexao.conector();
    }

    /**
     * Método responsável por adicionar um novo cliente
     */
    private void adicionar() {
        String sql = "insert into clientes(nome, tipo_pessoa, cpf, cnpj, fone, email, cidade, bairro, logradouro, numero, ponto_referencia, fone1) values(?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtNomeCliente.getText());
            String tipoPessoa = cboPerfil.getSelectedItem().toString();
            pst.setString(2, tipoPessoa);
            pst.setString(3, txtCpfCnpj.getText());
            pst.setString(4, txtCpfCnpj.getText());
            pst.setString(5, txtContato1.getText());
            pst.setString(6, txtEmail.getText());
            pst.setString(7, txtCidade.getText());
            pst.setString(8, txtBairro.getText());
            pst.setString(9, txtLogradouro.getText());
            pst.setString(10, txtNumero.getText());
            pst.setString(11, txtPontoReferencia.getText());
            pst.setString(12, txtContato2.getText());

            if ("Fisica".equals(tipoPessoa)) {
                pst.setString(3, txtCpfCnpj.getText()); // CPF
                pst.setNull(4, java.sql.Types.VARCHAR); // CNPJ is NULL
            } else {
                pst.setNull(3, java.sql.Types.VARCHAR); // CPF is NULL
                pst.setString(4, txtCpfCnpj.getText()); // CNPJ
            }
            //validação se os campos foram preenchidos
            if ((txtNomeCliente.getText().isEmpty()) || (cboPerfil.getSelectedItem().toString().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatorios!");
            } else {

                //confirmar a inserção dos dados
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Cliente adicionado com sucesso!");
                    limpar();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método responsável pela pesquisa de clientes pelo nome com filtro
     */
    private void pesquisar_cliente() {
        String sql = "select idcliente as ID, nome as NOME, tipo_pessoa as PESSOA, cpf as CPF, cnpj as CNPJ, fone as FONE, email as EMAIL, cidade as CIDADE, bairro as BAIRRO, logradouro as LOGRADOURO, numero as Nº, ponto_referencia as PONTO_DE_REFERÊNCIA, fone1 as FONE2 from clientes where nome like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtConsultaCliente.getText() + "%");
            rs = pst.executeQuery();
            //a linha abaixo usa a lib rs2xml para preencher a tabela
            tblClientes.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * método usado para setar os campos de texto com o conteúdo da tabela
     */
    private void setar_campos() {
        int setar = tblClientes.getSelectedRow();
        if (setar >= 0) {
            txtIdCliente.setText(tblClientes.getModel().getValueAt(setar, 0).toString());
            // Verifica se a célula está vazia ou nula antes de definir o texto
            // Recupera e define o nome do cliente
            txtNomeCliente.setText(tblClientes.getModel().getValueAt(setar, 1) != null
                    ? tblClientes.getModel().getValueAt(setar, 1).toString()
                    : "");

            // Define o perfil do cliente
            String perfil = tblClientes.getModel().getValueAt(setar, 2) != null
                    ? tblClientes.getModel().getValueAt(setar, 2).toString()
                    : "";
            cboPerfil.setSelectedItem(perfil);

            // Recupera o tipo de pessoa (Física ou Jurídica)
            String tipoPessoa = perfil;

            // Define o CPF ou CNPJ baseado no tipo de pessoa
            if ("Fisica".equals(tipoPessoa)) {
                txtCpfCnpj.setText(tblClientes.getModel().getValueAt(setar, 3) != null
                        ? tblClientes.getModel().getValueAt(setar, 3).toString()
                        : "");
            } else if ("Juridica".equals(tipoPessoa)) {
                txtCpfCnpj.setText(tblClientes.getModel().getValueAt(setar, 4) != null
                        ? tblClientes.getModel().getValueAt(setar, 4).toString()
                        : "");
            } else {
                // Se o tipo de pessoa não for nem Física nem Jurídica, define como vazio
                txtCpfCnpj.setText("");
            }

            // Define os demais campos, sempre verificando se os valores são nulos
            txtContato1.setText(tblClientes.getModel().getValueAt(setar, 5) != null
                    ? tblClientes.getModel().getValueAt(setar, 5).toString()
                    : "");

            txtEmail.setText(tblClientes.getModel().getValueAt(setar, 6) != null
                    ? tblClientes.getModel().getValueAt(setar, 6).toString()
                    : "");

            txtCidade.setText(tblClientes.getModel().getValueAt(setar, 7) != null
                    ? tblClientes.getModel().getValueAt(setar, 7).toString()
                    : "");

            txtBairro.setText(tblClientes.getModel().getValueAt(setar, 8) != null
                    ? tblClientes.getModel().getValueAt(setar, 8).toString()
                    : "");

            txtLogradouro.setText(tblClientes.getModel().getValueAt(setar, 9) != null
                    ? tblClientes.getModel().getValueAt(setar, 9).toString()
                    : "");

            txtNumero.setText(tblClientes.getModel().getValueAt(setar, 10) != null
                    ? tblClientes.getModel().getValueAt(setar, 10).toString()
                    : "");

            txtPontoReferencia.setText(tblClientes.getModel().getValueAt(setar, 11) != null
                    ? tblClientes.getModel().getValueAt(setar, 11).toString()
                    : "");

            txtContato2.setText(tblClientes.getModel().getValueAt(setar, 12) != null
                    ? tblClientes.getModel().getValueAt(setar, 12).toString()
                    : "");
        }
        btnAdicionar.setEnabled(false);
    }

    /**
     * Método responsável pela edição dos dados do cliente
     */
    private void alterar() {
        String sql = "update clientes set nome=?, tipo_pessoa=?, cpf=?, cnpj=?, fone=?, email=?, cidade=?, bairro=?, logradouro=?, numero=?, ponto_referencia=?, fone1=? where idcliente=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtNomeCliente.getText());
            String tipoPessoa = cboPerfil.getSelectedItem().toString();
            pst.setString(2, tipoPessoa);
            pst.setString(3, txtCpfCnpj.getText());
            pst.setString(4, txtCpfCnpj.getText());
            pst.setString(5, txtContato1.getText());
            pst.setString(6, txtEmail.getText());
            pst.setString(7, txtCidade.getText());
            pst.setString(8, txtBairro.getText());
            pst.setString(9, txtLogradouro.getText());
            pst.setString(10, txtNumero.getText());
            pst.setString(11, txtPontoReferencia.getText());
            pst.setString(12, txtContato2.getText());
            pst.setString(13, txtIdCliente.getText());

            if ("Fisica".equals(tipoPessoa)) {
                pst.setString(3, txtCpfCnpj.getText()); // CPF
                pst.setNull(4, java.sql.Types.VARCHAR); // CNPJ is NULL
            } else {
                pst.setNull(3, java.sql.Types.VARCHAR); // CPF is NULL
                pst.setString(4, txtCpfCnpj.getText()); // CNPJ
            }

            if ((txtNomeCliente.getText().isEmpty()) || (cboPerfil.getSelectedItem().toString().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatorios!");
            } else {
                //confirmar a alteração dos dados
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Dados do cliente alterados com sucesso!");
                    limpar();
                    btnAdicionar.setEnabled(true);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Método responsável por excluir um cliente
     */
    private void deletar() {
        //aviso ao remover usuario
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja deletar este cliente ?");
        if (confirma == JOptionPane.YES_OPTION) {
            String sql = "delete from clientes where idcliente=?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdCliente.getText());
                int apagado = pst.executeUpdate();
                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Cliente deletado com sucesso");
                    limpar();
                    btnAdicionar.setEnabled(true);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    /**
     * Método responsável por limpar os campos e gerenciar os componentes
     */
    private void limpar() {
        txtConsultaCliente.setText(null);
        txtIdCliente.setText(null);
        txtNomeCliente.setText(null);
        cboPerfil.setSelectedItem(null);
        txtCpfCnpj.setText(null);
        txtEmail.setText(null);
        txtCidade.setText(null);
        txtBairro.setText(null);
        txtLogradouro.setText(null);
        txtNumero.setText(null);
        txtPontoReferencia.setText(null);
        txtContato1.setText(null);
        txtContato2.setText(null);
        ((DefaultTableModel) tblClientes.getModel()).setRowCount(0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtNomeCliente = new javax.swing.JTextField();
        txtCpfCnpj = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        txtContato1 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtContato2 = new javax.swing.JTextField();
        txtLogradouro = new javax.swing.JTextField();
        txtCidade = new javax.swing.JTextField();
        txtBairro = new javax.swing.JTextField();
        txtNumero = new javax.swing.JTextField();
        txtPontoReferencia = new javax.swing.JTextField();
        btnAdicionar = new javax.swing.JButton();
        btnDeletar = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        cboPerfil = new javax.swing.JComboBox<>();
        txtConsultaCliente = new javax.swing.JTextField();
        btnConsultar = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblClientes = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtIdCliente = new javax.swing.JTextField();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Clientes");
        setToolTipText("");
        setPreferredSize(new java.awt.Dimension(868, 671));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 51, 51));
        jLabel1.setText("*Nome");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 51, 51));
        jLabel2.setText("*Pessoa fisica/ juridica");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("CPF/CNPJ");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Tel:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Email:");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Endereço");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("Cidade");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("Bairro");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("Logradouro");
        jLabel9.setToolTipText("");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setText("Nº");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setText("Ponto de Referência");

        txtNomeCliente.setToolTipText("Nome do Cliente");

        txtCpfCnpj.setToolTipText("CPF ou CNPJ do Cliente");
        txtCpfCnpj.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCpfCnpjActionPerformed(evt);
            }
        });

        txtEmail.setToolTipText("Email do Cliente");

        txtContato1.setToolTipText("Contato 1 ");
        txtContato1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtContato1ActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setText("Tel:");

        txtContato2.setToolTipText("Contato 2");
        txtContato2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtContato2ActionPerformed(evt);
            }
        });

        txtLogradouro.setToolTipText("Rua, Avenida,Travessa Etc");

        txtCidade.setToolTipText("Cidade");

        txtBairro.setToolTipText("Bairro");

        txtNumero.setToolTipText("Numero do estabelecimento");

        txtPontoReferencia.setToolTipText("Ponto de Referência");

        btnAdicionar.setIcon(new javax.swing.ImageIcon("C:\\Users\\marco\\Downloads\\49576_new_add_plus_user_icon.png")); // NOI18N
        btnAdicionar.setToolTipText("Adicionar");
        btnAdicionar.setOpaque(true);
        btnAdicionar.setPreferredSize(new java.awt.Dimension(55, 55));
        btnAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarActionPerformed(evt);
            }
        });

        btnDeletar.setIcon(new javax.swing.ImageIcon("C:\\Users\\marco\\Downloads\\49612_delete_male_icon.png")); // NOI18N
        btnDeletar.setToolTipText("Deletar");
        btnDeletar.setHideActionText(true);
        btnDeletar.setPreferredSize(new java.awt.Dimension(55, 55));
        btnDeletar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeletarActionPerformed(evt);
            }
        });

        btnAlterar.setIcon(new javax.swing.ImageIcon("C:\\Users\\marco\\Downloads\\49585_human_male_user_icon.png")); // NOI18N
        btnAlterar.setToolTipText("Alterar");
        btnAlterar.setOpaque(true);
        btnAlterar.setPreferredSize(new java.awt.Dimension(55, 55));
        btnAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarActionPerformed(evt);
            }
        });

        cboPerfil.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Fisica", "Juridica" }));
        cboPerfil.setToolTipText("Tipo Pessoa ");

        txtConsultaCliente.setToolTipText("Pesquisar nome do cliente");
        txtConsultaCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtConsultaClienteKeyReleased(evt);
            }
        });

        btnConsultar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/tecmaqli/icones/search_icon.png"))); // NOI18N
        btnConsultar.setToolTipText("");

        tblClientes = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tblClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "NOME", "PESSOA", "CPF", "CNPJ", "FONE", "EMAIL", "CIDADE", "BAIRRO", "LOGRADOURO", "NUMERO", "PONTO DE REFERÊNCIA", "FONE2"
            }
        ));
        tblClientes.setFocusable(false);
        tblClientes.getTableHeader().setReorderingAllowed(false);
        tblClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblClientesMouseClicked(evt);
            }
        });
        tblClientes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblClientesKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblClientes);

        jLabel13.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 0, 51));
        jLabel13.setText("* campos obrigatórios");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setText("ID");

        txtIdCliente.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtIdCliente.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtConsultaCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE)
                                .addComponent(jLabel5)
                                .addComponent(jLabel2)
                                .addComponent(jLabel3)
                                .addComponent(txtCpfCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtNomeCliente)
                                .addComponent(cboPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtContato1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel12))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel4)
                                        .addComponent(txtContato2, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(41, 41, 41)
                                .addComponent(jLabel13)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(55, 55, 55)
                                        .addComponent(btnAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(56, 56, 56)
                                        .addComponent(btnDeletar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(16, 16, 16))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(162, 162, 162)
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel9)
                                    .addComponent(txtLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel7)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(txtCidade, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtBairro, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel11)
                                    .addComponent(txtPontoReferencia, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnConsultar)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 804, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 33, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtConsultaCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnConsultar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(6, 6, 6)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel1)
                                                .addComponent(jLabel14)))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel13)
                                            .addComponent(txtIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtNomeCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jLabel6))
                            .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDeletar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboPerfil, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtLogradouro, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtCpfCnpj, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel4)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtContato1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtContato2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBairro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPontoReferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50))
        );

        setBounds(0, 0, 868, 671);
    }// </editor-fold>//GEN-END:initComponents

    private void txtCpfCnpjActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCpfCnpjActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCpfCnpjActionPerformed

    private void txtContato1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtContato1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtContato1ActionPerformed

    private void txtContato2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtContato2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtContato2ActionPerformed

    private void btnAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarActionPerformed
        //botão adicionar cliente
        adicionar();
    }//GEN-LAST:event_btnAdicionarActionPerformed

    private void txtConsultaClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtConsultaClienteKeyReleased
        // metodo pesquisar cliente enquanto digita 
        pesquisar_cliente();
    }//GEN-LAST:event_txtConsultaClienteKeyReleased

    private void tblClientesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblClientesKeyPressed
        // metodo para mostrar os campos da tabela na tela
        setar_campos();
    }//GEN-LAST:event_tblClientesKeyPressed

    private void tblClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblClientesMouseClicked
        // metodo para mostrar os campos da tabela na tela
        setar_campos();
    }//GEN-LAST:event_tblClientesMouseClicked

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        // botao alterar dados cliente
        alterar();
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnDeletarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeletarActionPerformed
        // botao deletar cliente
        deletar();
    }//GEN-LAST:event_btnDeletarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionar;
    private javax.swing.JButton btnAlterar;
    private javax.swing.JLabel btnConsultar;
    private javax.swing.JButton btnDeletar;
    private javax.swing.JComboBox<String> cboPerfil;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblClientes;
    private javax.swing.JTextField txtBairro;
    private javax.swing.JTextField txtCidade;
    private javax.swing.JTextField txtConsultaCliente;
    private javax.swing.JTextField txtContato1;
    private javax.swing.JTextField txtContato2;
    private javax.swing.JTextField txtCpfCnpj;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtIdCliente;
    private javax.swing.JTextField txtLogradouro;
    private javax.swing.JTextField txtNomeCliente;
    private javax.swing.JTextField txtNumero;
    private javax.swing.JTextField txtPontoReferencia;
    // End of variables declaration//GEN-END:variables

}
