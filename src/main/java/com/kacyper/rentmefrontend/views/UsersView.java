package com.kacyper.rentmefrontend.views;

import com.kacyper.rentmefrontend.client.UserClient;
import com.kacyper.rentmefrontend.domain.User;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

import java.util.List;

@UIScope
@Component
public class UsersView extends VerticalLayout {

    private final Grid<User> userGrid = new Grid<>();

    private final UserClient userClient;

    private final Dialog dialog = new Dialog();

    private final Binder<User> userBinder = new Binder<>();

    private final TextField firstName = new TextField("First Name");
    private final TextField lastName = new TextField("Last Name");
    private final TextField email = new TextField("Email");
    private final TextField password = new TextField("Password");
    private final IntegerField phoneNumber = new IntegerField("Phone Number");

    private final User user = new User();

    public UsersView(UserClient userClient) {
        this.userClient = userClient;

        bindFields();

        VerticalLayout dialogLayout = new VerticalLayout();
        Button saveUser = saveUserButton();
        dialogLayout.add(firstName, lastName, email, password, phoneNumber, saveUser);

        dialog.isCloseOnOutsideClick();
        dialog.add(dialogLayout);

        setColumns();

        Button addUser = addUserButton();
        updateUsers();
        add(addUser, userGrid, dialog);

    }

    public void updateUsers() {
        List<User> usersList = userClient.getAllUsers();
        userGrid.setItems(usersList);
    }

    private void saveUser(User user) {
        if (!userClient.doesUserExist(user.getEmail())) {
            userClient.createUser(user);
            updateUsers();
            dialog.close();
            clearFields();
        } else {
            Dialog warning = userExists();
            warning.open();
        }
    }

    private Button addUserButton() {
        return new Button("Add new user", event -> dialog.open());
    }

    private Button saveUserButton() {
        return new Button("Save user", event -> {
            if (fieldsAreFilled()) {
                userBinder.writeBeanIfValid(user);
                saveUser(user);
            } else {
                Dialog emptyFields = emptyFields();
                emptyFields.open();
            }
        });
    }

    private Dialog emptyFields() {
        Dialog emptyFields = new Dialog();
        VerticalLayout emptyFieldsLayout = new VerticalLayout();
        Button dismissWarning = new Button("Dismiss warning", event -> emptyFields.close());
        Label emptyFieldsLabel = new Label("Don't leave empty fields");
        emptyFieldsLayout.add(emptyFieldsLabel, dismissWarning);
        emptyFields.add(emptyFieldsLayout);
        return emptyFields;
    }

    private boolean fieldsAreFilled() {
        return (!firstName.getValue().equals("") &&
                !lastName.getValue().equals("") &&
                !email.getValue().equals("") &&
                !password.getValue().equals("") &&
                phoneNumber.getValue() != null);
    }

    private Dialog userExists() {
        Dialog warningDialog = new Dialog();
        VerticalLayout warningLayout = new VerticalLayout();
        Button cancelWarning = new Button("Cancel", event -> warningDialog.close());
        Label warningLabel = new Label("You're in our DB.");
        warningLayout.add(warningLabel, cancelWarning);
        warningDialog.add(warningLayout);
        return warningDialog;
    }

    private void setColumns() {
        userGrid.addColumn(User::getId).setHeader("User ID");
        userGrid.addColumn(User::getFirstName).setHeader("First Name");
        userGrid.addColumn(User::getLastName).setHeader("Last Name");
        userGrid.addColumn(User::getEmail).setHeader("Email");
        userGrid.addColumn(User::getPassword).setHeader("Password");
        userGrid.addColumn(User::getPhoneNumber).setHeader("Phone Number");
        userGrid.addColumn(User::getAccountCreationDate).setHeader("User Created");
    }

    private void clearFields() {
        firstName.clear();
        lastName.clear();
        email.clear();
        password.clear();
        phoneNumber.clear();
    }

    private void bindFields() {
        userBinder.forField(firstName).bind(User::getFirstName, User::setFirstName);
        userBinder.forField(lastName).bind(User::getLastName, User::setLastName);
        userBinder.forField(email).bind(User::getEmail, User::setEmail);
        userBinder.forField(password).bind(User::getPassword, User::setPassword);
        userBinder.forField(phoneNumber).bind(User::getPhoneNumber, User::setPhoneNumber);
    }

}
