/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package views.pegawai;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import database.DMLSQL;
import database.DatabaseManager;
import database.Password;

/**
 *
 * @author syahrul
 */
public class pegawaiForm extends javax.swing.JPanel {
    private DatabaseManager dbManager;
    private DMLSQL dmlSql;
    private JDialog parentDialog;
    private String id = null;
    private pegawaiList parent;
    private JComboBox<ComboItem> comboBox;

    /**
     * Creates new form pegawaiCreate
     */
    public pegawaiForm(JDialog parentDialog, String id) {
        initComponents();
        this.dbManager = new DatabaseManager();
        this.dmlSql = new DMLSQL(this.dbManager);
        setJabatanSelect();
        if (id != null) {
            this.id = id;
            setData(id);
        } else {
            hapusButton.setVisible(false);
        }
        this.parentDialog = parentDialog;
        this.parent = (pegawaiList) parentDialog.getParent();
    }

    private static class ComboItem {
        private String label;
        private String value;

        public ComboItem(String label, String value) {
            this.label = label;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return label; // Displayed label in JComboBox
        }
    }

    private void setJabatanSelect() {
        this.comboBox = jabatanInput;
        String[] columnsToSelect = {"id","nama"};
        List<Object[]> results;
        try {
            results = dmlSql.selectData("jabatan", columnsToSelect, null, null, null, null, null, false);
            for (Object[] row : results) {
                comboBox.addItem(new ComboItem((String)row[1],(String)row[0]));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Query Fail. "+e.getMessage());
        }
    }

    private void setData(String id) {
        String[] columnsToSelect = {"nama","email","no_telp","jenis_kelamin","alamat","password","jabatan_id"};
        String[] conditionColumns = {"pegawai.id"};
        String[] operators = {"="};
        Object[] conditionValues = {id};
        Object[] result;
        try {
            result = dmlSql.findData("pegawai", columnsToSelect, null, null, conditionColumns, operators, conditionValues, false);
            String jabatan_id = (String) result[6];
            for (int i = 0; i < comboBox.getItemCount(); i++) {
                ComboItem item = comboBox.getItemAt(i);
                if (item.getValue().equals(jabatan_id)) {
                    comboBox.setSelectedItem(item);
                    break;
                }
            }
            namaInput.setText((String)result[0]);
            emailInput.setText((String)result[1]);
            notelpInput.setText((String)result[2]);
            if ("Laki-laki".equals(result[3])) {
                jkLInput.setSelected(true);
            } else {
                jkPInput.setSelected(true);
            }
            alamatInput.setText((String)result[4]);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Query Fail. "+e.getMessage());
        }
    }

    private void saveData() {
        try {
            if (this.id != null) {
                String jenis_kelamin = "";
                if (jkLInput.isSelected()) {
                    jenis_kelamin = "Laki-laki";
                } else {
                    jenis_kelamin = "Perempuan";
                }
                ComboItem jabatan = (ComboItem) comboBox.getSelectedItem();
                String[] updateColumns = {"jabatan_id","nama","email","no_telp","jenis_kelamin","alamat"};
                Object[] updateValues = {jabatan.getValue(),namaInput.getText(),emailInput.getText(),notelpInput.getText(),jenis_kelamin,alamatInput.getText()};
                if (passwordInput.getPassword().length > 0) {
                    int newSizeInsertColumns = updateColumns.length + 1;
                    String[] newInsertColumns = new String[newSizeInsertColumns];
                    System.arraycopy(updateColumns, 0, newInsertColumns, 0, updateColumns.length);
                    newInsertColumns[newSizeInsertColumns - 1] = "password";
                    updateColumns = newInsertColumns;

                    int newSizeInsertValues = updateValues.length + 1;
                    Object[] newInsertValues = new Object[newSizeInsertValues];
                    System.arraycopy(updateValues, 0, newInsertValues, 0, updateValues.length);
                    newInsertValues[newSizeInsertValues - 1] = Password.hashPassword(new String(passwordInput.getPassword()));
                    updateValues = newInsertValues;
                }
                dmlSql.updateData("pegawai", updateColumns, updateValues, this.id);
                dbManager.close();
                this.parentDialog.dispose();
                JOptionPane.showMessageDialog(null, "Data Berhasil Diubah!");
            } else {
                if (passwordInput.getPassword().length == 0) {
                    JOptionPane.showMessageDialog(null, "Password Wajib Diisi!");
                } else {
                    UUID uuid = UUID.randomUUID();
                    String jenis_kelamin = "";
                    if (jkLInput.isSelected()) {
                        jenis_kelamin = "Laki-laki";
                    } else {
                        jenis_kelamin = "Perempuan";
                    }
                    ComboItem jabatan = (ComboItem) comboBox.getSelectedItem();
                    String[] insertColumns = {"id","jabatan_id","nama","email","no_telp","jenis_kelamin","alamat","password"};
                    Object[] insertValues = {uuid.toString(),jabatan.getValue(),namaInput.getText(),emailInput.getText(),notelpInput.getText(),jenis_kelamin,alamatInput.getText(),Password.hashPassword(new String(passwordInput.getPassword()))};
                    dmlSql.insertData("pegawai", insertColumns, insertValues);
                    dbManager.close();
                    this.parentDialog.dispose();
                    JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan!");
                }
            }
            parent.datatable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Query Fail. "+e.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jkInputGroup = new javax.swing.ButtonGroup();
        title = new javax.swing.JLabel();
        body = new javax.swing.JPanel();
        jabatanLabel = new javax.swing.JLabel();
        emailLabel = new javax.swing.JLabel();
        notelpLabel = new javax.swing.JLabel();
        jkLabel = new javax.swing.JLabel();
        alamatLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        alamatInput = new javax.swing.JTextArea();
        notelpInput = new javax.swing.JTextField();
        emailInput = new javax.swing.JTextField();
        passwordLabel = new javax.swing.JLabel();
        passwordInput = new javax.swing.JPasswordField();
        jkLInput = new javax.swing.JRadioButton();
        jkPInput = new javax.swing.JRadioButton();
        namaLabel = new javax.swing.JLabel();
        namaInput = new javax.swing.JTextField();
        jabatanInput = new javax.swing.JComboBox<>();
        actionPanel = new javax.swing.JPanel();
        simpanButton = new javax.swing.JButton();
        hapusButton = new javax.swing.JButton();

        title.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        title.setText("Form Pegawai");

        jabatanLabel.setText("Jabatan");

        emailLabel.setText("Email");

        notelpLabel.setText("No Telepon");

        jkLabel.setText("Jenis Kelamin");

        alamatLabel.setText("alamat");

        alamatInput.setColumns(20);
        alamatInput.setRows(5);
        jScrollPane1.setViewportView(alamatInput);

        passwordLabel.setText("Password");

        jkInputGroup.add(jkLInput);
        jkLInput.setText("Laki-laki");

        jkInputGroup.add(jkPInput);
        jkPInput.setText("Perempuan");

        namaLabel.setText("Nama");

        javax.swing.GroupLayout bodyLayout = new javax.swing.GroupLayout(body);
        body.setLayout(bodyLayout);
        bodyLayout.setHorizontalGroup(
            bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bodyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(passwordLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jkLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(notelpLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(emailLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jabatanLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(alamatLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(namaLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addComponent(passwordInput)
                    .addComponent(notelpInput)
                    .addComponent(emailInput)
                    .addComponent(namaInput)
                    .addGroup(bodyLayout.createSequentialGroup()
                        .addComponent(jkLInput)
                        .addGap(18, 18, 18)
                        .addComponent(jkPInput))
                    .addComponent(jabatanInput, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        bodyLayout.setVerticalGroup(
            bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bodyLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jabatanLabel)
                    .addComponent(jabatanInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(namaLabel)
                    .addComponent(namaInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emailLabel)
                    .addComponent(emailInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(notelpLabel)
                    .addComponent(notelpInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jkLabel)
                    .addComponent(jkLInput)
                    .addComponent(jkPInput))
                .addGap(6, 6, 6)
                .addGroup(bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(alamatLabel)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordLabel)
                    .addComponent(passwordInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6))
        );

        simpanButton.setText("Simpan");
        simpanButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simpanButtonActionPerformed(evt);
            }
        });

        hapusButton.setText("Hapus");
        hapusButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hapusButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout actionPanelLayout = new javax.swing.GroupLayout(actionPanel);
        actionPanel.setLayout(actionPanelLayout);
        actionPanelLayout.setHorizontalGroup(
            actionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(actionPanelLayout.createSequentialGroup()
                .addGap(89, 89, 89)
                .addGroup(actionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(simpanButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hapusButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(90, 90, 90))
        );
        actionPanelLayout.setVerticalGroup(
            actionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(actionPanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(simpanButton)
                .addGap(12, 12, 12)
                .addComponent(hapusButton)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(actionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(title, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(body, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(12, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(title)
                .addGap(12, 12, 12)
                .addComponent(body, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(12, 12, 12)
                .addComponent(actionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void simpanButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpanButtonActionPerformed
        saveData();
    }//GEN-LAST:event_simpanButtonActionPerformed

    private void hapusButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hapusButtonActionPerformed
        try {
            dmlSql.deleteData("pegawai", "id", this.id);
            dbManager.close();
            this.parentDialog.dispose();
            JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus!");
            parent.datatable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Query Fail. "+e.getMessage());
        }
    }//GEN-LAST:event_hapusButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel actionPanel;
    private javax.swing.JTextArea alamatInput;
    private javax.swing.JLabel alamatLabel;
    private javax.swing.JPanel body;
    private javax.swing.JTextField emailInput;
    private javax.swing.JLabel emailLabel;
    private javax.swing.JButton hapusButton;
    private javax.swing.JScrollPane jScrollPane1;
    private JComboBox<ComboItem> jabatanInput;
    private javax.swing.JLabel jabatanLabel;
    private javax.swing.ButtonGroup jkInputGroup;
    private javax.swing.JRadioButton jkLInput;
    private javax.swing.JLabel jkLabel;
    private javax.swing.JRadioButton jkPInput;
    private javax.swing.JTextField namaInput;
    private javax.swing.JLabel namaLabel;
    private javax.swing.JTextField notelpInput;
    private javax.swing.JLabel notelpLabel;
    private javax.swing.JPasswordField passwordInput;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JButton simpanButton;
    private javax.swing.JLabel title;
    // End of variables declaration//GEN-END:variables
}
