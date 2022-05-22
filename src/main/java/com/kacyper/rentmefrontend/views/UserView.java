package com.kacyper.rentmefrontend.views;

import com.kacyper.rentmefrontend.client.UserClient;
import com.kacyper.rentmefrontend.domain.User;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

@UIScope
@Component
public class UserView extends VerticalLayout {

    private final UserClient userClient;
    private final Binder<User> userBinder = new Binder<>();
    private final TextField firstName = new TextField("First Name");
    private final TextField lastName = new TextField("Last Name");
    private final TextField email = new TextField("Email");
    private final TextField password = new TextField("Password");
    private final IntegerField phoneNumber = new IntegerField("Phone Number");
    private User loggedUser = new User();
    private Long userId;

    public UserView(UserClient userClient) {
        this.userClient = userClient;

        bindFields();

        VerticalLayout accountLayout = new VerticalLayout();
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        Button deleteUser = deleteUserAccountButton();
        Button updateUser = updateUserAccountButton();
        horizontalLayout.add(deleteUser, updateUser);

        accountLayout.add(firstName, lastName, email, password, phoneNumber, horizontalLayout);

        add(accountLayout);
        setHorizontalComponentAlignment(Alignment.CENTER, accountLayout);
    }

    private Button updateUserAccountButton() {
        Dialog updateUser = updateUserDialog();
        return new Button("Updating data", event -> updateUser.open());
    }

    private Dialog updateUserDialog() {
        Dialog updateUser = new Dialog();
        VerticalLayout updateLayout = new VerticalLayout();
        Button updateUserButton = confirmUpdatingButton(updateUser);
        Button dismissUpdate = dismissUpdateButton(updateUser);
        Label confirmLabel = new Label("You're bout to update your data.");
        updateLayout.add(confirmLabel, updateUserButton, dismissUpdate);
        updateUser.add(updateLayout);
        return updateUser;
    }

    private Button dismissUpdateButton(Dialog dialog) {
        return new Button("Dismiss", event -> dialog.close());
    }

    private Button confirmUpdatingButton(Dialog dialog) {
        return new Button("Update", event -> {
            if (fieldsAreFilled()) {
                if (userBinder.writeBeanIfValid(loggedUser)) {
                    updateUser(loggedUser);
                }
                dialog.close();
            } else {
                Dialog warningDialog = warningAlert();
                warningDialog.open();
            }
        });
    }

    private boolean fieldsAreFilled() {
        return (!firstName.getValue().equals("") &&
                !lastName.getValue().equals("") &&
                !email.getValue().equals("") &&
                !password.getValue().equals("") &&
                phoneNumber.getValue() != null);
    }

    private Dialog warningAlert() {
        Dialog warningAlert = new Dialog();
        VerticalLayout layout = new VerticalLayout();
        Button cancel = new Button("Cancel", event -> warningAlert.close());
        Label label = new Label("Your account cannot be deleted.");
        layout.add(label, cancel);
        warningAlert.add(layout);
        return warningAlert;
    }

    private void updateUser(User user) {
        user.setId(userId);
        userClient.updateUser(user);
    }

    private Button deleteUserAccountButton() {
        Dialog deletingAccount = deleteUserAccount();
        return new Button("Deleting account", event -> deletingAccount.open());
    }

    private Dialog deleteUserAccount() {
        Dialog confirmDeleteUserAccount = new Dialog();
        VerticalLayout deleteUserAccountLayout = new VerticalLayout();
        Button deleteAccount = confirmDeleteUserAccountButton(confirmDeleteUserAccount);
        Button cancelDeleteAccount = cancelDeleteAccountButton(confirmDeleteUserAccount);
        Label deleteLabel = new Label("Bye bye, your account is bout to be deleted.");
        deleteUserAccountLayout.add(deleteLabel, deleteAccount, cancelDeleteAccount);
        confirmDeleteUserAccount.add(deleteUserAccountLayout);
        return confirmDeleteUserAccount;
    }

    private Button cancelDeleteAccountButton(Dialog dialog) {
        return new Button("Cancel", event -> dialog.close());
    }

    private Button confirmDeleteUserAccountButton(Dialog dialog) {
        return new Button("Delete", event -> {
            deleteUser(loggedUser);
            dialog.close();
        });
    }

    private void deleteUser(User user) {
        userClient.deleteUser(user.getId());
    }

    public void updateUsersForUser(User user) {
        loggedUser = user;
        userId = user.getId();
        userBinder.readBean(user);
    }


    private void bindFields() {
        userBinder.forField(firstName).bind(User::getFirstName, User::setFirstName);
        userBinder.forField(lastName).bind(User::getLastName, User::setLastName);
        userBinder.forField(email).bind(User::getEmail, User::setEmail);
        userBinder.forField(password).bind(User::getPassword, User::setPassword);
        userBinder.forField(phoneNumber).bind(User::getPhoneNumber, User::setPhoneNumber);
    }

}
