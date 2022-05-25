package com.kacyper.rentmefrontend.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;
@UIScope
@Component
@Route
public class MainView extends VerticalLayout {

    private final CarView carView;
    private final UsersView usersView;
    private final VinCheckView vinCheckView;

    private PageTabs tabs = new PageTabs();
    private Tab carTab = new Tab("Cars");
    private Tab usersTab = new Tab("Users");
    private Tab userTab = new Tab("Account");
    private Tab vinCheckTab = new Tab("Vin Check");


    public MainView(CarView carView, UsersView usersView, VinCheckView vinCheckView) {
        this.carView = carView;
        this.usersView = usersView;
        this.vinCheckView = vinCheckView;

        tabs.add(carView, carTab);
        tabs.add(usersView, usersTab);
        tabs.add(vinCheckView, vinCheckTab);

        add(tabs);

    }

}
