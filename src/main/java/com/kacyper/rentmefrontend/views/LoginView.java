package com.kacyper.rentmefrontend.views;

import com.kacyper.rentmefrontend.client.LoginClient;
import com.kacyper.rentmefrontend.client.UserClient;
import com.kacyper.rentmefrontend.domain.Login;
import com.kacyper.rentmefrontend.domain.User;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@UIScope
@Component
@Route(value = "")
public class LoginView extends VerticalLayout {

    private final MainView mainView;

    private LoginClient loginClient;

    private UserClient userClient;

    private final EmailField email = new EmailField("EMAIL");
    private final PasswordField password = new PasswordField("Password");
    private final Binder<Login> binder = new Binder<>();
    public User user;

    public LoginView(@Qualifier("mainView") MainView mainView, LoginClient loginClient, UserClient userClient) {
        this.mainView = mainView;
        this.loginClient = loginClient;
        this.userClient = userClient;

        bindFields();

        Button login = newLoginButton();
        add(email, password, login);
        setAlignItems(Alignment.CENTER);
    }

    private void login() {
        Login login = new Login();
        binder.writeBeanIfValid(login);
        clearFields();

        if (login.getEmail().equals("admin") && (login.getPassword().equals("admin"))) {
            mainView.adminView();
            mainView.setStartingTab();
            getUI().ifPresent(ui -> ui.navigate("mainView"));
        } else  {
            if (loginClient.isLoginExisting(login)) {
                user = userClient.getUserByEmail(login.getEmail());
                mainView.userView(user);
                mainView.setStartingTab();
                getUI().ifPresent(ui -> ui.navigate("mainView"));
            } else {
                Dialog userDoesntExist = invalidUser();
                userDoesntExist.open();
            }
        }

    }
    private Dialog invalidUser() {
        Dialog userDoesntExist = new Dialog();
        VerticalLayout invalidLayout = new VerticalLayout();
        Button cancel = new Button("Cancel", event -> userDoesntExist.close());
        Label userDoesntExistLabel = new Label("Something is wrong!");
        invalidLayout.add(userDoesntExistLabel, cancel);
        userDoesntExist.add(invalidLayout);
        return userDoesntExist;
    }

    private Button newLoginButton() {
        return new Button("Login", event -> login());
    }

    private void bindFields() {
        binder.forField(email).bind(Login::getEmail, Login::setEmail);
        binder.forField(password).bind(Login::getPassword, Login::setPassword);
    }

    private void clearFields() {
        email.clear();
        password.clear();
    }

}
