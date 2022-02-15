package org.example.form;

import org.example.client.ChatClient;
import org.example.dto.Response;
import org.example.handler.ClientHandler;
import org.example.util.DialogUtils;

import javax.swing.*;

public class MessengerForm extends JFrame {
    private JButton btnLogin;
    private JButton btnChatWith;
    private JButton btnSend;
    private JTextArea txtMessageList;
    private JTextField txtUsername;
    private JTextField txtMessage;
    private ChatClient chatClient;
    private ClientHandler clientHandler;

    private String clientName;
    private String currentReceiver;

    public MessengerForm() {
        initComponent();
    }

    private void initConnection() {
        clientHandler = new ClientHandler();
        chatClient = new ChatClient(clientHandler);

        new Thread(this::handleResponse).start();
    }

    private void initComponent() {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        txtUsername = new JTextField();
        btnLogin = new JButton();
        btnLogin.setText("Login");
        btnLogin.addActionListener(e -> login());

        btnChatWith = new JButton();
        btnChatWith.setVisible(false);
        btnChatWith.setText("Chat");
        btnChatWith.addActionListener(e -> chatWithUser());

        txtMessageList = new JTextArea();

        txtMessage = new JTextField();
        txtMessage.setEnabled(false);

        btnSend = new JButton();
        btnSend.setText("Send");
        btnSend.setEnabled(false);
        btnSend.addActionListener(e -> sendMessage());

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(txtMessageList)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(txtUsername, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnLogin)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnChatWith)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(txtMessage)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnSend)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtUsername, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnLogin)
                                        .addComponent(btnChatWith))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtMessageList, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtMessage, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnSend))
                                .addContainerGap())
        );

        pack();
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(this);
    }

    private void login() {
        initConnection();
        String username = txtUsername.getText();
        if (username == null || username.trim().length() == 0) {
            DialogUtils.showErrorDialog("Tên đăng nhập không được để trống!");
            return;
        }
        try {
            txtUsername.setEnabled(false);
            btnLogin.setEnabled(false);
            chatClient.login(username);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void chatWithUser() {
        String username = txtUsername.getText();
        if (username == null || username.trim().length() == 0) {
            DialogUtils.showErrorDialog("Bạn chưa nhập username người nhận!");
            return;
        }
        currentReceiver = username;
        txtMessage.setEnabled(true);
        btnSend.setEnabled(true);

        try {
            chatClient.getHistoryMessages(clientName, username);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        String text = txtMessage.getText();
        try {
            chatClient.sendMessage(clientName, currentReceiver, text);
            appendMessage(clientName, text);
            txtMessage.setText(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleResponse() {
        while (true) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!clientHandler.isNewMessage()) {
                continue;
            }

            var response = clientHandler.getMessage();
            switch (response.getType()) {
                case LOGIN -> handleLoginDone(response);
                case GET_MESSAGE -> handleGetHistoryMessage(response);
                case CHAT -> handleNewMessage(response);
            }
        }
    }

    private void handleNewMessage(Response response) {
        String from = response.getFrom();
        String text = response.getMessage();
        appendMessage(from, text);
    }

    private void handleGetHistoryMessage(Response response) {
        response.getMessages().
                forEach(message -> appendMessage(message.getFrm(), message.getContent()));
    }

    private void handleLoginDone(Response response) {
        txtUsername.setEnabled(true);
        if (response.getStatus() == Response.Status.SUCCESS) {
            DialogUtils.showInfoDialog("Đăng nhập thành công");
            clientName = txtUsername.getText();
            txtUsername.setText(null);

            btnLogin.setVisible(false);
            btnChatWith.setVisible(true);

            this.setTitle("Loged in with username " + clientName);
        } else {
            DialogUtils.showErrorDialog("Tên đăng nhập đã được sử dụng, vui lòng chọn tên khác!");
            btnLogin.setEnabled(true);
        }
    }

    private void appendMessage(String from, String text) {
        String currentMessages = txtMessageList.getText();
        txtMessageList.setText(currentMessages + from + ": " + text+ "\n");
    }
}
