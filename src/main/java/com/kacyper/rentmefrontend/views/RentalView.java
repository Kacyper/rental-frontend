package com.kacyper.rentmefrontend.views;

import com.kacyper.rentmefrontend.client.RentalClient;
import com.kacyper.rentmefrontend.domain.Rental;
import com.kacyper.rentmefrontend.domain.RentalsExtended;
import com.kacyper.rentmefrontend.domain.User;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

import java.util.List;

@UIScope
@Component
public class RentalView extends VerticalLayout {

    private final Grid<RentalsExtended> rentalGrid = new Grid<>();

    private final RentalClient rentalClient;

    private final Dialog modifyRentDialog = new Dialog();

    private final Binder<Rental> binderForModificationRent = new Binder<>();

    private final DatePicker extendFrom = new DatePicker("Rent from");
    private final DatePicker extendTo = new DatePicker("Rent to");

    private User loggedUser;
    private Long rentalId;
    private Long carId;

    public RentalView(RentalClient rentalClient) {
        this.rentalClient = rentalClient;

        bindFields();

        VerticalLayout extendLayout = new VerticalLayout();
        Dialog longerRentDialog = new Dialog();
        longerRentDialog.isCloseOnOutsideClick();
        longerRentDialog.add(extendLayout);

        VerticalLayout modifyLayout = new VerticalLayout();
        Button confirmModification = confirmModification();
        modifyLayout.add(extendFrom, extendTo, confirmModification);
        modifyRentDialog.isCloseOnOutsideClick();
        modifyRentDialog.add(modifyLayout);

        setColumns();

        rentalGrid.addComponentColumn(this::closeRental);
        rentalGrid.addComponentColumn(this::modifyRentalButton);

        add(rentalGrid);

    }

    public void updateRentalForUser(User user) {
        loggedUser = user;
        List<RentalsExtended> rentalsDtoList = rentalClient.getRentalsByUsersId(user.getId());
        rentalGrid.setItems(rentalsDtoList);
    }

    public void updateRentalForAdmin() {
        loggedUser = null;
        List<RentalsExtended> rentalsDtoList = rentalClient.getAllRentals();
        rentalGrid.setItems(rentalsDtoList);
    }

    private Button closeRental(RentalsExtended rentalsExtended) {
        Dialog confirmCloseRental = closeRentalDialog(rentalsExtended);

        Button closeRental = new Button("Close", event -> confirmCloseRental.open());

        closeRental.setEnabled(loggedUser != null);
        return closeRental;
    }

    private Dialog closeRentalDialog(RentalsExtended rentalsExtended) {
        Dialog confirmCloseRental = new Dialog();
        VerticalLayout confirmLayout = new VerticalLayout();
        Button confirmCloseRentalButton = closeRentalButton(confirmCloseRental, rentalsExtended);
        Button cancelCloseRentalButton = cancelConfirmationButton(confirmCloseRental);
        Label confirmLabel = new Label("Do you want to end it?");
        confirmLayout.add(confirmLabel, confirmCloseRentalButton, cancelCloseRentalButton);
        confirmCloseRental.add(confirmLayout);
        return confirmCloseRental;
    }

    private Button closeRentalButton(Dialog dialog, RentalsExtended rentalsExtended) {
        return new Button("Confirm", event -> {
            rentalId = rentalsExtended.getId();
            closeRental(rentalId);
            dialog.close();
        });
    }

    private Button cancelConfirmationButton(Dialog dialog) {
        return new Button("Cancel", event -> dialog.close());
    }

    private Button modifyRentalButton(RentalsExtended rentalsExtended) {
        Button modifyRental = new Button("Modify");
        modifyRental.addClickListener(event -> {
            rentalId = rentalsExtended.getId();
            carId = rentalsExtended.getCarId();
            modifyRentDialog.open();
        });

        modifyRental.setEnabled(loggedUser != null);
        return modifyRental;
    }

    private Button confirmModification() {
        return new Button("Confirm", event -> {
            Rental rental = new Rental();
            binderForModificationRent.writeBeanIfValid(rental);
            rental.setId(rentalId);
            rental.setCarId(carId);
            rental.setUserId(loggedUser.getId());
            rentalClient.modifyRental(rental);
            updateRentalForUser(loggedUser);
            modifyRentDialog.close();
        });
    }

    private void closeRental(Long rentId) {
        rentalClient.endRent(rentId);
        updateRentalForUser(loggedUser);
    }

    private void setColumns() {
        rentalGrid.addColumn(RentalsExtended::getId).setHeader("Rental ID");
        rentalGrid.addColumn(RentalsExtended::getRentedFrom).setHeader("Rented from");
        rentalGrid.addColumn(RentalsExtended::getRentedTo).setHeader("Rented to");
        rentalGrid.addColumn(RentalsExtended::getRentalCost).setHeader("Cost");
        rentalGrid.addColumn(RentalsExtended::getCarManufacture).setHeader("Car Maker");
        rentalGrid.addColumn(RentalsExtended::getCarModel).setHeader("Car Model");
        rentalGrid.addColumn(RentalsExtended::getUserFirstName).setHeader("First Name");
        rentalGrid.addColumn(RentalsExtended::getUserLastName).setHeader("Last Name");
    }

    private void bindFields() {
        binderForModificationRent.forField(extendFrom).bind(Rental::getRentedFrom, Rental::setRentedTo);
        binderForModificationRent.forField(extendTo).bind(Rental::getRentedTo, Rental::setRentedTo);
    }



}
