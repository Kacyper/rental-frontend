package com.kacyper.rentmefrontend.views;

import com.kacyper.rentmefrontend.client.CarClient;
import com.kacyper.rentmefrontend.client.RentalClient;
import com.kacyper.rentmefrontend.domain.Car;
import com.kacyper.rentmefrontend.domain.Rental;
import com.kacyper.rentmefrontend.domain.User;
import com.kacyper.rentmefrontend.form.Status;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

import java.util.List;

@UIScope
@Component
public class CarView extends VerticalLayout {

    private final Grid<Car> carGrid = new Grid<>();
    private final CarClient carClient;
    private final RentalClient rentalClient;
    private final RentalView rentalView;

    private final Car car = new Car();

    private final Dialog carDialog = new Dialog();
    private final Binder<Car> savingCar = new Binder<>();
    private final TextField vin = new TextField("VIN Number");
    private final TextField carManufacture = new TextField("Car Manufacture");
    private final TextField model = new TextField("Model");
    private final IntegerField productionYear = new IntegerField("Production Year");
    private final TextField fuel = new TextField("Fuel");
    private final TextField vehicleClass = new TextField("Vehicle Class");
    private final IntegerField mileage = new IntegerField("Mileage");
    private final BigDecimalField dailyCost = new BigDecimalField("Daily Cost");
    private final TextField status = new TextField("Status");

    private final Dialog updateCarDialog = new Dialog();
    private final Binder<Car> updatingCar = new Binder<>();
    private final IntegerField mileageUpdate = new IntegerField("Mileage");
    private final BigDecimalField dailyCostUpdate = new BigDecimalField("Daily Cost");
    private final TextField statusUpdate = new TextField("Status");

    private final Dialog rentalDialog = new Dialog();
    private final Binder<Rental> rentalBinder = new Binder<>();
    private final DatePicker rentedFrom = new DatePicker("Rented from");
    private final DatePicker rentedTo = new DatePicker("Rented to");

    Button addCar = new Button("Add a new car");

    private User loggedUser;
    private Long carId;

    public CarView(CarClient carClient, RentalClient rentalClient, RentalView rentalView) {
        this.carClient = carClient;
        this.rentalClient = rentalClient;
        this.rentalView = rentalView;

        bindFields();

        VerticalLayout aNewCarDialogLayout = new VerticalLayout();
        Button saveCarButton = saveCarButton();
        aNewCarDialogLayout.add(vin, carManufacture, model, productionYear, fuel,
                vehicleClass, mileage, dailyCost, saveCarButton);
        carDialog.isCloseOnOutsideClick();
        carDialog.add(aNewCarDialogLayout);

        VerticalLayout anUpdateCarDialogLayout = new VerticalLayout();
        Button confirmUpdateButton = confirmUpdateButton();
        anUpdateCarDialogLayout.add(mileageUpdate, dailyCostUpdate, confirmUpdateButton);
        updateCarDialog.isCloseOnOutsideClick();
        updateCarDialog.add(anUpdateCarDialogLayout);

        VerticalLayout aNewRentDialogLayout = new VerticalLayout();
        Button confirmRentButton = confirmRent();
        aNewRentDialogLayout.add(rentedFrom, rentedTo, confirmRentButton);
        rentalDialog.isCloseOnOutsideClick();
        rentalDialog.add(aNewRentDialogLayout);

        setColumns();

        carGrid.addComponentColumn(this::addRentButton);
        carGrid.addComponentColumn(this::updateCarButton);
        carGrid.addComponentColumn(this::deleteButton);

        addCar.addClickListener(c -> carDialog.open());

        updateAdminsCars();
        add(addCar, carGrid, carDialog);

    }

    void updateAdminsCars() {
        loggedUser = null;
        addCar.setEnabled(true);
        List<Car> carList = carClient.getCar();
        carGrid.setItems(carList);
    }

    void updateUserCars(User user) {
        loggedUser = user;
        addCar.setEnabled(false);
        List<Car> carList = carClient.getCar();
        carGrid.setItems(carList);
    }

    private void saveCar(Car car) {
        carClient.saveCar(car);
        updateAdminsCars();
        carDialog.close();
        clearFields();
    }

    private void clearFields() {
        vin.clear();
        carManufacture.clear();
        model.clear();
        productionYear.clear();
        fuel.clear();
        vehicleClass.clear();
        mileage.clear();
        dailyCost.clear();
    }

    private Button saveCarButton() {
        return new Button("Save car", event -> {
            if (newCarFieldsFilled()) {
                savingCar.writeBeanIfValid(car);
                saveCar(car);
            } else {
                Dialog warning = warningDialog();
                warning.open();
            }
        });
    }

    private Button updateCarButton(Car car) {
        Button updateButton = new Button("Update");
        updateButton.addClickListener(c -> {
            carId = car.getId();
            updatingCar.readBean(car);
            updateCarDialog.open();
        });
       if (loggedUser == null) {
           updateButton.setEnabled(!car.getStatus().equals(Status.RENTED));
       } else {
           updateButton.setEnabled(false);
       }
        return updateButton;
    }

    private Button confirmUpdateButton() {
        return new Button("Confirm", event -> {
            if (updateCarFieldsFilled()) {
                updatingCar.writeBeanIfValid(car);
                car.setId(carId);
                carClient.updateCar(car);
                updateAdminsCars();
                updateCarDialog.close();
            } else {
                Dialog warning = warningDialog();
                warning.open();
            }
        });
    }

    private Button addRentButton(Car car) {
        Button rentButton = new Button("Rent");
        rentButton.addClickListener(r -> {
            carId = car.getId();
            rentalDialog.open();
        });
        if (loggedUser == null) {
            rentButton.setEnabled(false);
        } else {
            rentButton.setEnabled(!car.getStatus().equals(Status.RENTED));
        }
        return rentButton;
    }

    private Button confirmRent() {
        return new Button("Confirm car rent", event -> {
            Rental rental = new Rental();
            rentalBinder.writeBeanIfValid(rental);
            rental.setCarId(carId);
            rental.setUserId(loggedUser.getId());
            rentalClient.createRent(rental);
            rentalView.updateRentalForUser(loggedUser);
            updateUserCars(loggedUser);
            rentalDialog.close();
        });
    }

    private Button deleteButton(Car car) {
        Dialog deleteCar = deleteCarDialog(car);
        Button deleteButton = new Button("Delete", event -> deleteCar.open());
        if (loggedUser == null) {
            deleteButton.setEnabled(!car.getStatus().equals(Status.RENTED));
        }
        return deleteButton;
    }

    private Dialog deleteCarDialog(Car car) {
        Dialog confirmDeleteCarDialog = new Dialog();
        VerticalLayout confirmLayout = new VerticalLayout();
        Button confirmDeleteCarButton = confirmDeleteCarButton(confirmDeleteCarDialog, car);
        Button cancelDeleteCarButton = cancelDeleteCarButton(confirmDeleteCarDialog);
        Label confirmLabel = new Label("Do you want to delete this car?");
        confirmLayout.add(confirmLabel, confirmDeleteCarButton, cancelDeleteCarButton);
        confirmDeleteCarDialog.add(confirmLayout);
        return confirmDeleteCarDialog;
    }
    private Button confirmDeleteCarButton(Dialog dialog, Car car) {
        return new Button("Delete", event -> {
            carClient.deleteCar(car.getId());
            updateAdminsCars();
            dialog.close();
        });
    }

    private Button cancelDeleteCarButton(Dialog dialog) {
        return new Button("Cancel", event -> dialog.close());

    }

    private Dialog warningDialog() {
        Dialog warning = new Dialog();
        VerticalLayout warningLayout = new VerticalLayout();
        Button dismissWarning = new Button("Dismiss Warning", event -> warning.close());
        Label warningLable = new Label("Don't miss any field.");
        warningLayout.add(warningLable, dismissWarning);
        warning.add(warningLayout);
        return warning;
    }

    private boolean newCarFieldsFilled() {
        return (!vin.getValue().equals("") &&
                !carManufacture.getValue().equals("") &&
                !model.getValue().equals("") &&
                productionYear.getValue() != null &&
                !fuel.getValue().equals("") &&
                !vehicleClass.getValue().equals("") &&
                mileage.getValue() != null &&
                dailyCost.getValue() != null);

    }

    private boolean updateCarFieldsFilled() {
        return (mileageUpdate.getValue() != null &&
                dailyCostUpdate.getValue() != null);
    }

    private void setColumns() {
        carGrid.addColumn(Car::getId).setHeader("Car Id");
        carGrid.addColumn(Car::getCarManufacture).setHeader("Car Manufacture");
        carGrid.addColumn(Car::getModel).setHeader("Model");
        carGrid.addColumn(Car::getProductionYear).setHeader("Production Year");
        carGrid.addColumn(Car::getFuel).setHeader("Fuel");
        carGrid.addColumn(Car::getVehicleClass).setHeader("Vehicle Class");
        carGrid.addColumn(Car::getMileage).setHeader("Mileage");
        carGrid.addColumn(Car::getDailyCost).setHeader("Daily Cost");
        carGrid.addColumn(Car::getStatus).setHeader("Rental Status");
    }

    private void bindFields() {
        savingCar.forField(vin).bind(Car::getVin, Car::setVin);
        savingCar.forField(carManufacture).bind(Car::getCarManufacture, Car::setCarManufacture);
        savingCar.forField(model).bind(Car::getModel, Car::setModel);
        savingCar.forField(productionYear).bind(Car::getProductionYear, Car::setProductionYear);
        savingCar.forField(mileage).bind(Car::getMileage, Car::setMileage);
        savingCar.forField(dailyCost).bind(Car::getDailyCost, Car::setDailyCost);

        updatingCar.forField(mileageUpdate).bind(Car::getMileage, Car::setMileage);
        updatingCar.forField(dailyCostUpdate).bind(Car::getDailyCost, Car::setDailyCost);

        rentalBinder.forField(rentedFrom).bind(Rental::getRentedFrom, Rental::setRentedFrom);
        rentalBinder.forField(rentedTo).bind(Rental::getRentedTo, Rental::setRentedTo);

    }


}
