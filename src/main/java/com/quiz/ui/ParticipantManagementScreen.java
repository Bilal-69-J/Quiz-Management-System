package com.quiz.ui;

import com.quiz.dao.ParticipantDAO;
import com.quiz.model.Participant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color; // Added for white text styling
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * ParticipantManagementScreen - Dark Theme (All Blue Buttons)
 */
public class ParticipantManagementScreen {

    private BorderPane root;
    private TableView<Participant> table;
    private ObservableList<Participant> data;
    private TextField searchField;

    public ParticipantManagementScreen() {
        createUI();
        loadData();
    }

    private void createUI() {
        root = new BorderPane();
        UIUtil.applyAppBackground(root); // Apply dark background
        root.setPadding(new Insets(20)); // Increased padding

        // -- Search Field --
        searchField = new TextField();
        searchField.setPromptText("Search by name or email");
        // Dark Theme Style for Search Bar
        searchField.setStyle("-fx-background-color: #333333; -fx-text-fill: white; -fx-prompt-text-fill: #888888; -fx-background-radius: 6;");
        searchField.setPrefHeight(35); // Slightly taller
        searchField.textProperty().addListener((obs, oldV, newV) -> applyFilter(newV));

        // -- Table Setup --
        table = new TableView<>();
        table.setPlaceholder(new Label("No participants found"));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Dark Theme CSS for TableView
        table.setStyle("-fx-background-color: transparent; " +
                "-fx-control-inner-background: #252525; " +
                "-fx-control-inner-background-alt: #1E1E1E; " +
                "-fx-text-fill: white; " +
                "-fx-table-cell-border-color: #333333;");

        TableColumn<Participant, Number> idC = new TableColumn<>("ID");
        idC.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getParticipantId()));
        idC.setMaxWidth(60);

        TableColumn<Participant, String> nameC = new TableColumn<>("Name");
        nameC.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getName()));

        TableColumn<Participant, String> emailC = new TableColumn<>("Email");
        emailC.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getEmail()));

        TableColumn<Participant, String> contactC = new TableColumn<>("Contact");
        contactC.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getContact()));

        table.getColumns().addAll(idC, nameC, emailC, contactC);

        // -- Buttons (ALL BLUE) --
        Button add = UIUtil.createPrimaryButton("Add");
        Button edit = UIUtil.createPrimaryButton("Edit");
        Button del = UIUtil.createPrimaryButton("Delete");

        add.setOnAction(e -> showDialog(null));
        edit.setOnAction(e -> {
            Participant sel = table.getSelectionModel().getSelectedItem();
            if (sel != null) showDialog(sel);
        });
        del.setOnAction(e -> {
            Participant sel = table.getSelectionModel().getSelectedItem();
            if (sel != null) {
                Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Delete participant '" + sel.getName() + "'?", ButtonType.YES, ButtonType.NO);
                a.showAndWait().ifPresent(bt -> { if (bt == ButtonType.YES) { try { new ParticipantDAO().delete(sel.getParticipantId()); loadData(); } catch (Exception ex) { showAlert("Error", ex.getMessage()); } } });
            }
        });

        // -- Button Toolbar --
        HBox btns = new HBox(15, add, edit, del);
        btns.setPadding(new Insets(15));
        btns.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        btns.setBackground(new Background(new BackgroundFill(UIUtil.CARD, new CornerRadii(12), Insets.EMPTY)));

        VBox center = new VBox(15, searchField, table, btns);
        // center.setPadding(new Insets(8)); // Removed redundant padding
        root.setCenter(center);
    }

    private void showDialog(Participant p) {
        Dialog<Participant> dialog = new Dialog<>();
        dialog.setTitle(p == null ? "Add Participant" : "Edit Participant");

        // Dark Theme Dialog Styling
        dialog.getDialogPane().setStyle("-fx-background-color: #252525; -fx-text-fill: white;");
        dialog.getDialogPane().getStylesheets().add("data:text/css,.label{ -fx-text-fill: white; }");

        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        // -- Inputs Styled for Dark Mode --
        String fieldStyle = "-fx-background-color: #333333; -fx-text-fill: white; -fx-prompt-text-fill: #888888; -fx-background-radius: 6;";

        TextField nameF = new TextField();
        nameF.setPromptText("Name");
        nameF.setStyle(fieldStyle);

        TextField emailF = new TextField();
        emailF.setPromptText("Email");
        emailF.setStyle(fieldStyle);

        TextField contactF = new TextField();
        contactF.setPromptText("Contact");
        contactF.setStyle(fieldStyle);

        PasswordField passF = new PasswordField();
        passF.setPromptText("Password (plain-text)");
        passF.setStyle(fieldStyle);

        if (p != null) {
            nameF.setText(p.getName());
            emailF.setText(p.getEmail());
            contactF.setText(p.getContact());
            passF.setText(p.getPassword());
        }

        // Labels with White Text
        Label lblName = new Label("Name"); lblName.setTextFill(Color.WHITE);
        Label lblEmail = new Label("Email"); lblEmail.setTextFill(Color.WHITE);
        Label lblContact = new Label("Contact"); lblContact.setTextFill(Color.WHITE);
        Label lblPass = new Label("Password"); lblPass.setTextFill(Color.WHITE);

        VBox content = new VBox(10, lblName, nameF, lblEmail, emailF, lblContact, contactF, lblPass, passF);
        content.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(btn -> {
            if (btn == saveBtn) {
                Participant np = p == null ? new Participant() : p;
                np.setName(nameF.getText());
                np.setEmail(emailF.getText());
                np.setContact(contactF.getText());
                np.setPassword(passF.getText());
                return np;
            }
            return null;
        });

        Optional<Participant> res = dialog.showAndWait();
        res.ifPresent(np -> {
            try {
                ParticipantDAO dao = new ParticipantDAO();
                if (p == null) dao.insert(np);
                else dao.update(np);
                loadData();
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });
    }

    private void loadData() {
        try {
            List<Participant> list = new ParticipantDAO().getAll();
            data = FXCollections.observableArrayList(list);
            table.setItems(data);
        } catch (Exception ex) {
            showAlert("Error loading data", ex.getMessage());
        }
    }

    private void applyFilter(String q) {
        if (q == null || q.trim().isEmpty()) {
            table.setItems(data);
            return;
        }
        String query = q.toLowerCase();
        List<Participant> filtered = data.stream()
                .filter(p -> (p.getName() + " " + p.getEmail()).toLowerCase().contains(query))
                .collect(Collectors.toList());
        table.setItems(FXCollections.observableArrayList(filtered));
    }

    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.setHeaderText(title);
        a.showAndWait();
    }

    public BorderPane getRoot() { return root; }
}