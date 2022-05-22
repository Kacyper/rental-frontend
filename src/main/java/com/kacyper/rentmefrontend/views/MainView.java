package com.kacyper.rentmefrontend.views;

import com.kacyper.rentmefrontend.domain.User;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;
import com.vaadin.flow.component.button.Button;
@UIScope
@Component
@Route
public class MainView extends VerticalLayout {

    private final CarView carView;
    private final RentalView rentalView;
    private final UsersView usersView;
    private final UserView userView;
    private final VinCheckView vinCheckView;
    private final LogoutView logoutView;
    private final PageTabs tabs = new PageTabs();
    private final Tab carTab = new Tab("Cars");
    private final Tab usersTab = new Tab("Users");
    private final Tab userTab = new Tab("Account");
    private final Tab rentalTab = new Tab("Rental");
    private final Tab vinCheckTab = new Tab("Vin Check");
    private final Tab logoutTab = new Tab();
    private User loggedUser;

    public MainView(CarView carView, RentalView rentalView, UsersView usersView, UserView userView, VinCheckView vinCheckView, LogoutView logoutView) {
        this.carView = carView;
        this.rentalView = rentalView;
        this.usersView = usersView;
        this.userView = userView;
        this.vinCheckView = vinCheckView;
        this.logoutView = logoutView;

        tabs.add(carView, carTab);
        tabs.add(rentalView, rentalTab);
        tabs.add(usersView, usersTab);
        tabs.add(userView, userTab);
        tabs.add(vinCheckView, vinCheckTab);
        tabs.add(logoutView, logoutTab);

        Button logoutButton = createLogout();
        logoutTab.add(logoutButton);

        add(tabs);

    }

    private Button createLogout() {
        return new Button("Logout", event -> logoutView.displayLogoutDialog());
    }

    public void adminView() {
        userTab.setVisible(false);
        usersTab.setVisible(true);
        carView.updateAdminsCars();
        rentalView.updateRentalForAdmin();
        usersView.updateUsers();
        vinCheckView.clearGrid();
    }

    public void userView(User user) {
        loggedUser = user;
        userTab.setVisible(true);
        usersTab.setVisible(false);
        carView.updateUserCars(user);
        rentalView.updateRentalForUser(user);
        userView.updateUsersForUser(user);
        vinCheckView.clearGrid();
    }

    public void setStartingTab() {
        tabs.select(carTab);
    }

}
