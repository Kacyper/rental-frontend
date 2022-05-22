package com.kacyper.rentmefrontend.views;

import com.kacyper.rentmefrontend.api.VinApi;
import com.kacyper.rentmefrontend.api.VinDecoderBodyDto;
import com.kacyper.rentmefrontend.api.VinDecoderDto;
import com.kacyper.rentmefrontend.client.VinDecoderClient;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@UIScope
@Component
public class VinCheckView extends VerticalLayout {

    private final Grid<VinDecoderBodyDto> grid = new Grid<>();

    private final VinDecoderClient vinDecoderClient;

    private final Dialog dialog = new Dialog();

    private final VinApi vinApi = new VinApi();

    private final Binder<VinApi> vinApiBinder = new Binder<>();

    private final TextField vinNum = new TextField("VIN Number");

    public VinCheckView(VinDecoderClient vinDecoderClient) {
        this.vinDecoderClient = vinDecoderClient;

        bindFields();

        VerticalLayout layout = new VerticalLayout();
        Button decodingButton = vinDecodeButton();
        layout.add(vinNum, decodingButton);

        dialog.isCloseOnOutsideClick();
        dialog.add(layout);

        setColumns();

        Button decodeVin = decodeVin();
        add(decodeVin, grid, dialog);

    }

    private Button decodeVin() {
        return new Button("Decoding VIN for you", event -> dialog.open());
    }

    private Button vinDecodeButton() {
        return new Button("Decode VIN", event -> {
            vinApiBinder.writeBeanIfValid(vinApi);
            VinDecoderDto vinDecoderDto = vinDecoderClient.decodingVin(vinApi);
            List<VinDecoderBodyDto> decoderDtoList = vinDecoderDto.getVinDecoderBodyDtoList();
            grid.setItems(decoderDtoList);
            dialog.close();
            clearFields();
        });
    }

    private void clearFields() {
        vinNum.clear();
    }

    private void bindFields() {
        vinApiBinder.forField(vinNum).bind(VinApi::getVinNumber, VinApi::setVinNumber);
    }

    public void clearGrid() {
        List<VinDecoderBodyDto> list = new ArrayList<>();
        grid.setItems(list);
    }

    private void setColumns() {
        grid.addColumn(VinDecoderBodyDto::getCarCompany).setHeader("Car Company");
        grid.addColumn(VinDecoderBodyDto::getModel).setHeader("Car Model");
        grid.addColumn(VinDecoderBodyDto::getFuelType).setHeader("Fuel");
        grid.addColumn(VinDecoderBodyDto::getProductionYear).setHeader("Production Year");
        grid.addColumn(VinDecoderBodyDto::getVehicleType).setHeader("Vehicle Type");
    }

}
