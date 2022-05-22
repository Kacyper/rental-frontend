package com.kacyper.rentmefrontend.views;

import com.kacyper.rentmefrontend.client.UserClient;
import com.kacyper.rentmefrontend.domain.User;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

@UIScope
@Component
public class RegisterView extends VerticalLayout {

    private final UserClient userClient;

    private final User user = new User();

    private final Binder<User> binder = new Binder<>();

    private final TextField firstName = new TextField("First name");
    private final TextField lastName = new TextField("Last name");
    private final EmailField email = new EmailField("EMAIL");
    private final PasswordField password = new PasswordField("Password");
    private final IntegerField phoneNumber = new IntegerField("Phone number");

    public RegisterView(UserClient userClient) {
        this.userClient = userClient;

        bindFields();

        Span welcome = new Span("We welcome you in our service!");
        Span blank = new Span(" ");
        Span registration = new Span("New account or login");
        Button login = loginButton();
        Button register = registerButton();

        add(welcome, blank, registration, firstName, lastName, email, password, password, register, login);
        setAlignItems(Alignment.CENTER);
    }

    private void save(User user) {
        if (!userClient.doesUserExist(user.getEmail())) {
            userClient.createUser(user);
            getUI().ifPresent(u -> u.navigate("loginView"));
            clearFields();
        } else {
            Dialog warning = userExistsDialog();
            warning.open();
        }
    }

    private Button loginButton() {
        return new Button("Log in", event -> getUI().ifPresent(u -> u.navigate("loginView")));
    }

    private Button registerButton() {
        return new Button("New account", event -> {
            if (fieldsAreFilled()) {
                binder.writeBeanIfValid(user);
                save(user);
            } else {
                Dialog warningFields = emptyFields();
                warningFields.open();
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

    private Dialog emptyFields() {
        Dialog warningFields = new Dialog();
        VerticalLayout warningLayout = new VerticalLayout();
        Button dismissWarning = new Button("Dismiss", event -> warningFields.close());
        Label warningLabel = new Label("Don't miss any field.");
        warningLayout.add(warningLabel, dismissWarning);
        warningFields.add(warningLayout);
        return warningFields;
    }

    private Dialog userExistsDialog() {
        Dialog warningUser = new Dialog();
        VerticalLayout warningUserLayout = new VerticalLayout();
        Button cancel = new Button("Cancel", event -> warningUser.close());
        Label warningUserLabel = new Label("You exist in our database.");
        warningUserLayout.add(warningUserLabel, cancel);
        warningUser.add(warningUserLayout);
        return warningUser;
    }

    private void clearFields() {
        firstName.clear();
        lastName.clear();
        email.clear();
        password.clear();
        phoneNumber.clear();
    }

    private void bindFields() {
        binder.forField(firstName).bind(User::getFirstName, User::setFirstName);
        binder.forField(lastName).bind(User::getLastName, User::setLastName);
        binder.forField(email).bind(User::getEmail, User::setEmail);
        binder.forField(password).bind(User::getPassword, User::setPassword);
        binder.forField(phoneNumber).bind(User::getPhoneNumber, User::setPhoneNumber);
    }
}
