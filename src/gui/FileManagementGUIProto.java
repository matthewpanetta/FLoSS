/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import client.File;
import client.ServerAdapter;
import client.User;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author mp755
 */
public class FileManagementGUIProto extends javax.swing.JFrame {
    private ServerAdapter serverAdapt;
    private TestGUI testGUI;
    private File file;
    private User user;
    private List<File> fileList;
    private List<User> friendsList;

    /**
     * Creates new form FileManagementGUIProto
     */
    public FileManagementGUIProto() {
        serverAdapt = ServerAdapter.getInstance();
        initComponents();
    }
    
    public void setFriendsList(List<User> friendsList) {
        this.friendsList = friendsList;
    }
    
    public void setControlledGUI(TestGUI testGUI) {
        this.testGUI = testGUI;
        fileList = testGUI.getFileList();
    }
    
    public void setFile(File file) {
        this.file = file;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public void refreshFileDetails() {   
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        fileNameLabel.setText(file.getFileName());
        ownerField.setText(file.getOwner());
        dateUploadedField.setText(sdf.format(file.getCreationDate()));
        dateLModifiedField.setText(sdf.format(file.getModifiedDate()));
        lastModifiedByField.setText(file.getLastModifier());
        numRevisionsField.setText(Integer.toString(file.getUpdateNum() - 1));
    }
    
    public void rename() {                
        int fileExtIndex = file.getFileName().lastIndexOf(".");
        String fileExt = file.getFileName().substring(fileExtIndex);
        
        String newName = JOptionPane.showInputDialog(this, "Enter a new file name: ");
        if(newName != null) {
            boolean renamed = serverAdapt.renameFile(user.getUserName(), file, newName);
        
            if(!newName.endsWith(fileExt)) {
                newName += fileExt;
            }

            if(renamed) {
                JOptionPane.showMessageDialog(this, "File successfully renamed!");
                fileList.remove(file);
                file.setFileName(newName);
                fileList.add(file);
                testGUI.setFileList(fileList);
                testGUI.refreshFileList();            

                Date date = new Date(System.currentTimeMillis());
                file.setLastModifier(user.getUserName());
                file.setModifiedDate(date);
                refreshFileDetails();
            } else {
                JOptionPane.showMessageDialog(this, "File has not been renamed. Please try again.");
            }
        }
    }
    
    public void delete() {
        if(!file.getOwner().equals(user.getUserName())) {
            JOptionPane.showMessageDialog(this, "You do not have permission to delete this file.");
        } else {
            boolean deletedFile = serverAdapt.deleteFile(file);
            if(deletedFile){
                JOptionPane.showMessageDialog(this, "File deleted!");

                fileList.remove(file);
                testGUI.setFileList(fileList);
                testGUI.refreshFileList();
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "File not deleted. Please try again.");
            }
        }
    }
    
    public void update() {
        // Opens a file chooser dialog GUI where the user selects which file(s) they would like to upload.
        JFileChooser chooser = new JFileChooser();

        // Restrict the user to certain file formats
        FileNameExtensionFilter filter = new FileNameExtensionFilter("All Acceptable Files", 
                "doc", "docx", "xlsx", "pptx", "txt", "png", "jpg", "gif", "pdf");

        chooser.setFileFilter(filter);
        chooser.setMultiSelectionEnabled(false);

        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            java.io.File fileIO = chooser.getSelectedFile();

            String filePath = fileIO.getPath();
            int index = filePath.lastIndexOf("\\");
            String modFilePath = fileIO.getName();

            serverAdapt.reupload(user, filePath, file.getOwner(), file.getFileName(), file.getUpdateNum());

            boolean rename = serverAdapt.renameFileUploadedWithDiffName(user.getUserName(), file, modFilePath, file.getFileName());

            if(rename) {
                JOptionPane.showMessageDialog(this, "File updated successfully!");
                
                Date date = new Date(System.currentTimeMillis());
                
                file.setLastModifier(user.getUserName());
                file.setUpdateNum(file.getUpdateNum() + 1);
                file.setModifiedDate(date);
                refreshFileDetails();
            } else {
                JOptionPane.showMessageDialog(this, "Could not update the file. Please try again later.");
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        fileNameLabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        numRevisionsField = new javax.swing.JLabel();
        numRevisionsLabel = new javax.swing.JLabel();
        lastModifedByLabel = new javax.swing.JLabel();
        dateLModifiedLabel = new javax.swing.JLabel();
        dateUploadedLabel = new javax.swing.JLabel();
        ownerLabel = new javax.swing.JLabel();
        ownerField = new javax.swing.JLabel();
        dateUploadedField = new javax.swing.JLabel();
        dateLModifiedField = new javax.swing.JLabel();
        lastModifiedByField = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        deleteButton = new javax.swing.JButton();
        updateButton = new javax.swing.JButton();
        renameButton = new javax.swing.JButton();
        permissionsButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        fileNameLabel.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        fileNameLabel.setText("[filename]");

        numRevisionsField.setText("[number]");

        numRevisionsLabel.setText("Number of Revisions:");

        lastModifedByLabel.setText("Last Modified By:");

        dateLModifiedLabel.setText("Date Last Modified:");

        dateUploadedLabel.setText("Date Uploaded:");

        ownerLabel.setText("Owner:");

        ownerField.setText("[owner username]");

        dateUploadedField.setText("[date uploaded]");

        dateLModifiedField.setText("[date last modified]");

        lastModifiedByField.setText("[username]");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(dateUploadedLabel)
                    .addComponent(dateLModifiedLabel)
                    .addComponent(ownerLabel)
                    .addComponent(lastModifedByLabel)
                    .addComponent(numRevisionsLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dateLModifiedField)
                    .addComponent(lastModifiedByField)
                    .addComponent(numRevisionsField)
                    .addComponent(ownerField)
                    .addComponent(dateUploadedField))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ownerLabel)
                    .addComponent(ownerField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dateUploadedLabel)
                    .addComponent(dateUploadedField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dateLModifiedLabel)
                    .addComponent(dateLModifiedField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lastModifedByLabel)
                    .addComponent(lastModifiedByField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(numRevisionsLabel)
                    .addComponent(numRevisionsField))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("File Management"));

        deleteButton.setText("Delete");
        deleteButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                deleteButtonMouseClicked(evt);
            }
        });

        updateButton.setText("Update");
        updateButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                updateButtonMouseClicked(evt);
            }
        });

        renameButton.setText("Rename");
        renameButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                renameButtonMouseClicked(evt);
            }
        });

        permissionsButton.setText("Permissions");
        permissionsButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                permissionsButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(updateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(renameButton, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(permissionsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(deleteButton, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                    .addComponent(updateButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(renameButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(permissionsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(fileNameLabel))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(98, 98, 98)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fileNameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void deleteButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_deleteButtonMouseClicked
        delete();
    }//GEN-LAST:event_deleteButtonMouseClicked

    private void updateButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_updateButtonMouseClicked
        update();
    }//GEN-LAST:event_updateButtonMouseClicked

    private void renameButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_renameButtonMouseClicked
        rename();
    }//GEN-LAST:event_renameButtonMouseClicked

    private void permissionsButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_permissionsButtonMouseClicked
        ViewPermissionGUI viewPermGUI = new ViewPermissionGUI();
        viewPermGUI.setUser(user);
        viewPermGUI.setFileID(file.getFileID());
        viewPermGUI.setFile(file);
        viewPermGUI.setFriends(friendsList);
        viewPermGUI.getPermissions();
        viewPermGUI.setVisible(true);
    }//GEN-LAST:event_permissionsButtonMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FileManagementGUIProto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FileManagementGUIProto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FileManagementGUIProto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FileManagementGUIProto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FileManagementGUIProto().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel dateLModifiedField;
    private javax.swing.JLabel dateLModifiedLabel;
    private javax.swing.JLabel dateUploadedField;
    private javax.swing.JLabel dateUploadedLabel;
    private javax.swing.JButton deleteButton;
    private javax.swing.JLabel fileNameLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lastModifedByLabel;
    private javax.swing.JLabel lastModifiedByField;
    private javax.swing.JLabel numRevisionsField;
    private javax.swing.JLabel numRevisionsLabel;
    private javax.swing.JLabel ownerField;
    private javax.swing.JLabel ownerLabel;
    private javax.swing.JButton permissionsButton;
    private javax.swing.JButton renameButton;
    private javax.swing.JButton updateButton;
    // End of variables declaration//GEN-END:variables
}
