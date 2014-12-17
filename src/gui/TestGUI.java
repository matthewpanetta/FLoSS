/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import client.File;
import client.ServerAdapter;
import client.User;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author mpwin7
 */
public class TestGUI extends javax.swing.JFrame {
    private ServerAdapter serverAdapt;
    private User user;
    private List<User> friendsList;
    private List<File> fileList;
    private String[] onlineFriendsNames;
    private String[] friendsNames;
    private String[] fileNames;
    private int fileFlag;
    /**
     * Creates new form TestGUI
     */
    public TestGUI() {
        serverAdapt = ServerAdapter.getInstance();
        fileFlag = 0;
        initComponents();
    }
    
    public void setUser(User user) {
        this.user = serverAdapt.getUser(user.getUserName());
        welcomeMessage.setText("Welcome, " + this.user.getFirstName() + " " + this.user.getLastName());
    }
    
    public List<File> getFileList() {
        return fileList;
    }
    
    public void setFileList(List<File> fileList) {
        this.fileList = fileList;
    }
    
    public void refreshFriendsList() {
        friendsList = serverAdapt.getFriends(user);
        onlineFriendsNames = new String[friendsList.size()];
        
        for(User u : friendsList) {
            if(serverAdapt.isOnline(u.getUserName())) {
                onlineFriendsNames[friendsList.indexOf(u)] = u.getUserName();
            }
        }
        
        friendsListHome.setListData(onlineFriendsNames);
    }
    
    public void refreshFileList() {
        fileNames = new String[fileList.size()];
        for(File f : fileList) {
            fileNames[fileList.indexOf(f)] = f.getFileName();
        }
            
        fileListFile.setListData(fileNames);
    }
    
    public void upload() {
        // Opens a file chooser dialog GUI where the user selects which file(s) they would like to upload.
        JFileChooser chooser = new JFileChooser();

        // Restrict the user to certain file formats
        FileNameExtensionFilter filter = new FileNameExtensionFilter("All Acceptable Files", 
                "doc", "docx", "xlsx", "pptx", "txt", "png", "jpg", "gif");

        chooser.setFileFilter(filter);

        // Allow the user to upload multiple files.
        chooser.setMultiSelectionEnabled(true);

        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            java.io.File[] fileList = chooser.getSelectedFiles();
            for(java.io.File f : fileList){                       
                serverAdapt.upload(user, f.getPath(), user.getUserName());

                client.File theFile = serverAdapt.getFile(f.getName(), user);
                AddPermissionGUI addPermGUI = new AddPermissionGUI();
                addPermGUI.setUser(user);
                addPermGUI.setFileID(theFile.getFileID());
                addPermGUI.setFriendsList(friendsList);
                addPermGUI.setVisible(true);

                JOptionPane.showMessageDialog(this, "File uploaded successfully!");
                this.fileList.add(theFile);
                refreshFileList();
            }
        }
    }
    
    public String download(File toDownload, int flag) {
        if(!fileListFile.isSelectionEmpty()) {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("All Acceptable Files", "doc", "docx", "xlsx", "pptx", "txt", "png", "jpg",
                "gif");
            chooser.setFileFilter(filter);
            chooser.showSaveDialog(this);

            if(toDownload == null) {
                List<String> selected = fileListFile.getSelectedValuesList();          

                for(File f : fileList) {
                    if(f.getFileName().equals(selected.get(0))) {
                        toDownload = f;
                        break;
                    }
                }
            }

            java.io.File clientFile = chooser.getSelectedFile();
            if(clientFile != null) {
                String clientPath = clientFile.getAbsolutePath();

                return downloadFileFromServer(toDownload, clientPath, flag);
            } else {
                return "";
            }
        } else {
            return "";
        }
    }
    
    public String downloadFileFromServer(File toDownload, String clientPath, int flag) {
            String pathToSave = toDownload.getFilePath() + "//" + toDownload.getFileName();

            // Get the file extension. If the user did not specify a file extension, add it onto the file name.
            int extensionIndex = pathToSave.lastIndexOf(".");
            String extension = pathToSave.substring(extensionIndex);
            if(!clientPath.endsWith(extension)) {
                clientPath += extension;
            }
            
            if(serverAdapt.download(user, pathToSave, clientPath, flag)) {
                JOptionPane.showMessageDialog(this, "File successfully downloaded!");
            }

            else {
                JOptionPane.showMessageDialog(this, "Could not retrieve file. Please try again");
            }
            return clientPath;
    }
    
    public void getSelectedFile() {
        List<String>selectedFileName = fileListFile.getSelectedValuesList();
        File selectedFile = null;
            
        for(File f : fileList) {
            if(f.getFileName().equals(selectedFileName.get(0))) {
                selectedFile = f;
                break;
            }
        }

        FileManagementGUIProto fmgp = new FileManagementGUIProto();
        fmgp.setFile(selectedFile);
        fmgp.setUser(user);
        fmgp.setControlledGUI(this);
        fmgp.setFriendsList(friendsList);
        fmgp.refreshFileDetails();
        fmgp.setVisible(true);
    }
    
    public void recover() {
        RecoverFileGUI recoverFileGUI = new RecoverFileGUI();
        recoverFileGUI.setUserName(user.getUserName());
        recoverFileGUI.setControlledGUI(this);
        recoverFileGUI.setFileList(fileList);
        recoverFileGUI.refreshFileList();
        recoverFileGUI.setVisible(true); 
    }
    
    public void versionControl() {
        List<String>selectedFileName = fileListFile.getSelectedValuesList();
        File selectedFile = null;
            
        for(File f : fileList) {
            if(f.getFileName().equals(selectedFileName.get(0))) {
                selectedFile = f;
                break;
            }
        }
        
        VersionControlGUI vcg = new VersionControlGUI();
        vcg.setUser(user);
        vcg.setControlledGUI(this);
        vcg.setFile(selectedFile);
        vcg.setFileList(fileList);
        vcg.refreshList();
        vcg.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel6 = new javax.swing.JPanel();
        NavTabs = new javax.swing.JTabbedPane();
        HomePanel = new javax.swing.JPanel();
        welcomeMessage = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        friendsListHome = new javax.swing.JList();
        logoutButton = new javax.swing.JButton();
        questionLabel = new javax.swing.JLabel();
        quickUploadButton = new javax.swing.JButton();
        changeUserButton = new javax.swing.JButton();
        FilePanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        fileListFile = new javax.swing.JList();
        generalFileFunctionality = new javax.swing.JPanel();
        uploadButton = new javax.swing.JButton();
        downloadButton = new javax.swing.JButton();
        manageButton = new javax.swing.JButton();
        fileRecovery = new javax.swing.JPanel();
        recoverButton = new javax.swing.JButton();
        versionControlButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        FriendsPanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        friendsListFriends = new javax.swing.JList();
        jPanel4 = new javax.swing.JPanel();
        removeFriendButton = new javax.swing.JButton();
        addFriendButton = new javax.swing.JButton();
        viewProfileButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        ProfilePanel = new javax.swing.JPanel();
        profileSettings = new javax.swing.JPanel();
        changePasswordButton = new javax.swing.JButton();
        updateInfoButton = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JSeparator();
        jPanel5 = new javax.swing.JPanel();
        profileWelcomeMessage = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        jPanel7 = new javax.swing.JPanel();
        firstName = new javax.swing.JLabel();
        lastName = new javax.swing.JLabel();
        gender = new javax.swing.JLabel();
        email = new javax.swing.JLabel();
        firstNameField = new javax.swing.JTextField();
        lastNameField = new javax.swing.JTextField();
        genderField = new javax.swing.JTextField();
        emailField = new javax.swing.JTextField();
        numFilesLabel = new javax.swing.JLabel();
        numFriendsLabel = new javax.swing.JLabel();
        numUpdatesLabel = new javax.swing.JLabel();
        jSeparator8 = new javax.swing.JSeparator();
        numFilesField = new javax.swing.JLabel();
        numFriendsField = new javax.swing.JLabel();
        numUpdatesField = new javax.swing.JLabel();
        MenuBar = new javax.swing.JMenuBar();
        FileMenu = new javax.swing.JMenu();
        UploadItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        ChangeUserItem = new javax.swing.JMenuItem();
        LogoutItem = new javax.swing.JMenuItem();
        EditMenu = new javax.swing.JMenu();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("My FLOSS");
        setMinimumSize(new java.awt.Dimension(500, 400));
        setResizable(false);

        NavTabs.setTabPlacement(javax.swing.JTabbedPane.RIGHT);
        NavTabs.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                NavTabsStateChanged(evt);
            }
        });

        HomePanel.setPreferredSize(new java.awt.Dimension(0, 0));

        welcomeMessage.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        welcomeMessage.setText("Welcome, Eugene Nitka!");

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Online Friends", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        friendsListHome.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "[O] - mp755", "[O] - tommy", "[A] - pewdiepie", "[O] - blazItFiggit", "[A] - lolzzer", "[O] - LukeExtendsVader" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(friendsListHome);

        logoutButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        logoutButton.setText("Logout");
        logoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButtonActionPerformed(evt);
            }
        });

        questionLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        questionLabel.setText("What would you like to do?");

        quickUploadButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        quickUploadButton.setText("Quick Upload");

        changeUserButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        changeUserButton.setText("Change User");
        changeUserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeUserButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout HomePanelLayout = new javax.swing.GroupLayout(HomePanel);
        HomePanel.setLayout(HomePanelLayout);
        HomePanelLayout.setHorizontalGroup(
            HomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HomePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addGroup(HomePanelLayout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addGroup(HomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(HomePanelLayout.createSequentialGroup()
                        .addComponent(changeUserButton, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(logoutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(welcomeMessage))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HomePanelLayout.createSequentialGroup()
                .addContainerGap(115, Short.MAX_VALUE)
                .addComponent(questionLabel)
                .addGap(106, 106, 106))
            .addGroup(HomePanelLayout.createSequentialGroup()
                .addGap(154, 154, 154)
                .addComponent(quickUploadButton, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        HomePanelLayout.setVerticalGroup(
            HomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HomePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(welcomeMessage)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(questionLabel)
                .addGap(23, 23, 23)
                .addComponent(quickUploadButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(HomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(logoutButton, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                    .addComponent(changeUserButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
                .addContainerGap())
        );

        NavTabs.addTab("My Home", HomePanel);

        jScrollPane2.setBorder(null);

        fileListFile.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        fileListFile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fileListFileMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(fileListFile);

        generalFileFunctionality.setBorder(javax.swing.BorderFactory.createTitledBorder("General File Functionality"));

        uploadButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        uploadButton.setText("Upload");
        uploadButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                uploadButtonMouseClicked(evt);
            }
        });

        downloadButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        downloadButton.setText("Download");
        downloadButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                downloadButtonMouseClicked(evt);
            }
        });

        manageButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        manageButton.setText("Manage This File");
        manageButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                manageButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout generalFileFunctionalityLayout = new javax.swing.GroupLayout(generalFileFunctionality);
        generalFileFunctionality.setLayout(generalFileFunctionalityLayout);
        generalFileFunctionalityLayout.setHorizontalGroup(
            generalFileFunctionalityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(generalFileFunctionalityLayout.createSequentialGroup()
                .addComponent(uploadButton, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addComponent(downloadButton, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addComponent(manageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        generalFileFunctionalityLayout.setVerticalGroup(
            generalFileFunctionalityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(generalFileFunctionalityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(uploadButton, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(downloadButton, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(manageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        fileRecovery.setBorder(javax.swing.BorderFactory.createTitledBorder("File Recovery"));

        recoverButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        recoverButton.setText("Recover Files");
        recoverButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                recoverButtonMouseClicked(evt);
            }
        });

        versionControlButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        versionControlButton.setText("Version Control");
        versionControlButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                versionControlButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout fileRecoveryLayout = new javax.swing.GroupLayout(fileRecovery);
        fileRecovery.setLayout(fileRecoveryLayout);
        fileRecoveryLayout.setHorizontalGroup(
            fileRecoveryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fileRecoveryLayout.createSequentialGroup()
                .addComponent(recoverButton, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addComponent(versionControlButton, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        fileRecoveryLayout.setVerticalGroup(
            fileRecoveryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fileRecoveryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(recoverButton, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(versionControlButton, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("My Files and Collaborations");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator2)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout FilePanelLayout = new javax.swing.GroupLayout(FilePanel);
        FilePanel.setLayout(FilePanelLayout);
        FilePanelLayout.setHorizontalGroup(
            FilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSeparator3)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, FilePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(FilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(fileRecovery, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(generalFileFunctionality, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );
        FilePanelLayout.setVerticalGroup(
            FilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FilePanelLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(generalFileFunctionality, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileRecovery, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        NavTabs.addTab("My Files", FilePanel);

        jScrollPane3.setBorder(null);

        friendsListFriends.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(friendsListFriends);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("General Friend Functionality"));

        removeFriendButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        removeFriendButton.setText("Remove Friend");

        addFriendButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        addFriendButton.setText("Add Friend");
        addFriendButton.setToolTipText("");

        viewProfileButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        viewProfileButton.setText("View Profile");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(addFriendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addComponent(removeFriendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addComponent(viewProfileButton, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addFriendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(removeFriendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(viewProfileButton, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setText("My Friends List");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(125, 125, 125)
                .addComponent(jLabel2)
                .addContainerGap(135, Short.MAX_VALUE))
            .addComponent(jSeparator4)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(jSeparator5)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout FriendsPanelLayout = new javax.swing.GroupLayout(FriendsPanel);
        FriendsPanel.setLayout(FriendsPanelLayout);
        FriendsPanelLayout.setHorizontalGroup(
            FriendsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        FriendsPanelLayout.setVerticalGroup(
            FriendsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        NavTabs.addTab("My Friends", FriendsPanel);

        profileSettings.setBorder(javax.swing.BorderFactory.createTitledBorder("Profile Settings"));

        changePasswordButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        changePasswordButton.setText("New Password");

        updateInfoButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        updateInfoButton.setText("Update Info");

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton1.setText("Delete Account");

        javax.swing.GroupLayout profileSettingsLayout = new javax.swing.GroupLayout(profileSettings);
        profileSettings.setLayout(profileSettingsLayout);
        profileSettingsLayout.setHorizontalGroup(
            profileSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(profileSettingsLayout.createSequentialGroup()
                .addComponent(changePasswordButton, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addComponent(updateInfoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        profileSettingsLayout.setVerticalGroup(
            profileSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(profileSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(changePasswordButton, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(updateInfoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        profileWelcomeMessage.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        profileWelcomeMessage.setText("My Profile");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(155, 155, 155)
                .addComponent(profileWelcomeMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jSeparator7)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(profileWelcomeMessage)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        firstName.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        firstName.setText("First Name:");

        lastName.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lastName.setText("Last Name:");

        gender.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        gender.setText("Gender:");

        email.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        email.setText("Email:");

        firstNameField.setText("[first name]");

        lastNameField.setText("[last name]");

        genderField.setText("[gender]");

        emailField.setText("[email]");

        numFilesLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        numFilesLabel.setText("Number of Files:");

        numFriendsLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        numFriendsLabel.setText("Number of Friends:");

        numUpdatesLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        numUpdatesLabel.setText("Number of Updates:");

        numFilesField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        numFilesField.setText("[#]");

        numFriendsField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        numFriendsField.setText("[#]");

        numUpdatesField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        numUpdatesField.setText("[#]");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(90, 90, 90)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(firstName)
                        .addGap(18, 18, 18)
                        .addComponent(firstNameField))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lastName)
                            .addComponent(gender)
                            .addComponent(email))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(genderField)
                            .addComponent(lastNameField)
                            .addComponent(emailField))))
                .addGap(90, 90, 90))
            .addComponent(jSeparator8)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(128, 128, 128)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(numFilesLabel)
                    .addComponent(numUpdatesLabel)
                    .addComponent(numFriendsLabel))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(numFilesField)
                    .addComponent(numFriendsField)
                    .addComponent(numUpdatesField))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(numFilesLabel)
                    .addComponent(numFilesField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(numFriendsLabel)
                    .addComponent(numFriendsField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(numUpdatesLabel)
                    .addComponent(numUpdatesField))
                .addGap(27, 27, 27)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(firstName)
                    .addComponent(firstNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lastName)
                    .addComponent(lastNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(gender)
                    .addComponent(genderField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(email)
                    .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(50, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout ProfilePanelLayout = new javax.swing.GroupLayout(ProfilePanel);
        ProfilePanel.setLayout(ProfilePanelLayout);
        ProfilePanelLayout.setHorizontalGroup(
            ProfilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator6)
            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(ProfilePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ProfilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(profileSettings, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        ProfilePanelLayout.setVerticalGroup(
            ProfilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ProfilePanelLayout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(profileSettings, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );

        NavTabs.addTab("My Profile", ProfilePanel);

        FileMenu.setText("General");

        UploadItem.setText("Upload");
        FileMenu.add(UploadItem);
        FileMenu.add(jSeparator1);

        ChangeUserItem.setText("Change User");
        FileMenu.add(ChangeUserItem);

        LogoutItem.setText("Logout");
        FileMenu.add(LogoutItem);

        MenuBar.add(FileMenu);

        EditMenu.setText("File");
        MenuBar.add(EditMenu);

        jMenu1.setText("Friend");
        MenuBar.add(jMenu1);

        jMenu2.setText("Profile");
        MenuBar.add(jMenu2);

        jMenu3.setText("Help");
        MenuBar.add(jMenu3);

        setJMenuBar(MenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(NavTabs)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(NavTabs, javax.swing.GroupLayout.PREFERRED_SIZE, 537, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButtonActionPerformed
        // TODO add your handling code here:
        //JOptionPane.showMessageDialog(this, "Login Successful");
        int i = JOptionPane.showConfirmDialog(this, "Are you sure you would like to logout and exit?", "Logout", 0);
        if (i == 0){
            serverAdapt.deauthenticate(user);
            dispose();
        }
    }//GEN-LAST:event_logoutButtonActionPerformed

    private void changeUserButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeUserButtonActionPerformed
        int i = JOptionPane.showConfirmDialog(this, "Are you sure you would like to logout?", "Wrong User", 0);
        if (i == 0){
            serverAdapt.deauthenticate(user);
            LoginGUI login = new LoginGUI();
            login.setVisible(true);
            dispose();
        }
    }//GEN-LAST:event_changeUserButtonActionPerformed

    private void NavTabsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_NavTabsStateChanged
        if(NavTabs.getSelectedIndex() == 1) {
            if(fileFlag == 0) {
                fileList = serverAdapt.getAllFiles(user.getUserName());
                fileFlag = 1;
            }
            refreshFileList();
        }
        
        if(NavTabs.getSelectedIndex() == 2) {
            friendsNames = new String[friendsList.size()];
            
            for(User u : friendsList) {
                friendsNames[friendsList.indexOf(u)] = u.getUserName();
            }
            
            friendsListFriends.setListData(friendsNames);
        }
        
        if(NavTabs.getSelectedIndex() == 3) {
            numFilesField.setText(Integer.toString(fileList.size()));
            numFriendsField.setText(Integer.toString(friendsList.size()));
            
            int updates = 0;
            for(File f : fileList) {
               updates += f.getUpdateNum() - 1;
            }
            
            numUpdatesField.setText(Integer.toString(updates));
            
            firstNameField.setText(user.getFirstName());
            lastNameField.setText(user.getLastName());
            genderField.setText(user.getGender());
            emailField.setText(user.getEmail());
        }
    }//GEN-LAST:event_NavTabsStateChanged

    private void uploadButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_uploadButtonMouseClicked
        upload();
    }//GEN-LAST:event_uploadButtonMouseClicked

    private void downloadButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_downloadButtonMouseClicked
        download(null, -1);
    }//GEN-LAST:event_downloadButtonMouseClicked

    private void fileListFileMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fileListFileMouseClicked
        if(evt.getClickCount() == 2) {
            getSelectedFile();
        }
    }//GEN-LAST:event_fileListFileMouseClicked

    private void manageButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_manageButtonMouseClicked
        getSelectedFile();
    }//GEN-LAST:event_manageButtonMouseClicked

    private void recoverButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_recoverButtonMouseClicked
        recover();
    }//GEN-LAST:event_recoverButtonMouseClicked

    private void versionControlButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_versionControlButtonMouseClicked
        versionControl();
    }//GEN-LAST:event_versionControlButtonMouseClicked

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
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TestGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TestGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TestGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TestGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TestGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem ChangeUserItem;
    private javax.swing.JMenu EditMenu;
    private javax.swing.JMenu FileMenu;
    private javax.swing.JPanel FilePanel;
    private javax.swing.JPanel FriendsPanel;
    private javax.swing.JPanel HomePanel;
    private javax.swing.JMenuItem LogoutItem;
    private javax.swing.JMenuBar MenuBar;
    private javax.swing.JTabbedPane NavTabs;
    private javax.swing.JPanel ProfilePanel;
    private javax.swing.JMenuItem UploadItem;
    private javax.swing.JButton addFriendButton;
    private javax.swing.JButton changePasswordButton;
    private javax.swing.JButton changeUserButton;
    private javax.swing.JButton downloadButton;
    private javax.swing.JLabel email;
    private javax.swing.JTextField emailField;
    private javax.swing.JList fileListFile;
    private javax.swing.JPanel fileRecovery;
    private javax.swing.JLabel firstName;
    private javax.swing.JTextField firstNameField;
    private javax.swing.JList friendsListFriends;
    private javax.swing.JList friendsListHome;
    private javax.swing.JLabel gender;
    private javax.swing.JTextField genderField;
    private javax.swing.JPanel generalFileFunctionality;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JLabel lastName;
    private javax.swing.JTextField lastNameField;
    private javax.swing.JButton logoutButton;
    private javax.swing.JButton manageButton;
    private javax.swing.JLabel numFilesField;
    private javax.swing.JLabel numFilesLabel;
    private javax.swing.JLabel numFriendsField;
    private javax.swing.JLabel numFriendsLabel;
    private javax.swing.JLabel numUpdatesField;
    private javax.swing.JLabel numUpdatesLabel;
    private javax.swing.JPanel profileSettings;
    private javax.swing.JLabel profileWelcomeMessage;
    private javax.swing.JLabel questionLabel;
    private javax.swing.JButton quickUploadButton;
    private javax.swing.JButton recoverButton;
    private javax.swing.JButton removeFriendButton;
    private javax.swing.JButton updateInfoButton;
    private javax.swing.JButton uploadButton;
    private javax.swing.JButton versionControlButton;
    private javax.swing.JButton viewProfileButton;
    private javax.swing.JLabel welcomeMessage;
    // End of variables declaration//GEN-END:variables
}
