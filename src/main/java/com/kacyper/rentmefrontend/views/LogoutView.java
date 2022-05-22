package com.kacyper.rentmefrontend.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

@UIScope
@Component
public class LogoutView extends VerticalLayout {

    Dialog logoutDialog = new Dialog();

    private LogoutView() {
        Button logout = logoutButton();
        Button cancel = cancelButton();

        HorizontalLayout dialogLayout = new HorizontalLayout();
        dialogLayout.add(logout, cancel);

        logoutDialog.add(dialogLayout);
    }

    public void logout() {
        logoutDialog.close();
        getUI().ifPresent(u -> u.navigate(""));
    }

    public void cancel() {
        logoutDialog.close();
    }

    private Button logoutButton() {
        return new Button("Logout", event -> logout());
    }

    private Button cancelButton() {
        return new Button("Cancel", event -> cancel());
    }

    public void displayLogoutDialog() {
        logoutDialog.open();
    }
}
