package com.example.fuelmoneycalculator;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.text.DecimalFormat;

public class Controller {
    static TextField txtFieldFuelCourse;
    static TextField txtFieldRouteLength;
    static TextField txtFieldAvgFuelConsumption;
    static ComboBox cmbCurrency;
    static ComboBox cmbLengthUnit;
    static Label labelResultPrice;
    static Button btnCalculate;
    static Button btnClear;
    static String currency;
    public static void initializeAndShowStage(Stage primaryStage, VBox vBox){
        primaryStage.setTitle("Gas Money Calculator");
        primaryStage.setScene(new Scene(vBox, 300, 350));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void addNodes(VBox vBox){
        //Labels
        Label labelFuelCourse = new Label("Fuel Course");
        Label labelRouteLength= new Label("Route Length");
        Label labelAvgFuelConsumption= new Label("Average Fuel Consumption");
        Label labelCurrency = new Label("Currency");
        Label labelUnit = new Label("Length unit");
        labelResultPrice = new Label("Price: ");

        //TextFields
        txtFieldFuelCourse = new TextField();
        txtFieldRouteLength = new TextField();
        txtFieldAvgFuelConsumption = new TextField();

        //ComboBoxes
        cmbCurrency = new ComboBox();
        setCurrencyItems();
        cmbCurrency.setValue("PLN"); //default value
        getCurrencySign();
        cmbCurrency.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                getCurrencySign();
                txtFieldFuelCourse.setPromptText("("+currency+"/l)");
            }
        });

        cmbLengthUnit = new ComboBox();
        setLengthUnitItems();
        cmbLengthUnit.setValue("km");
        cmbLengthUnit.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                String unit = String.valueOf(cmbLengthUnit.getSelectionModel().getSelectedItem());
                txtFieldRouteLength.setPromptText("("+unit+")");
                txtFieldAvgFuelConsumption.setPromptText("(l/100"+unit+")");
            }
        });


        //Prompts for TextFields
        txtFieldFuelCourse.setPromptText("("+currency+"/l)");
        txtFieldRouteLength.setPromptText("(km)");
        txtFieldAvgFuelConsumption.setPromptText("(l/100km)");

        //Buttons
        btnCalculate = new Button("Calculate");
        btnClear = new Button("Clear");

        //setting actions for buttons
        btnCalculate.setOnAction(e -> btnCalculateClicked(txtFieldRouteLength.getText(),
                txtFieldFuelCourse.getText(), txtFieldAvgFuelConsumption.getText(), vBox));
        btnClear.setOnAction(e -> btnClearClicked(vBox));

        //HBox for buttons
        HBox hBox = new HBox();
        setHBox(hBox);

        //Vbox for currency ComboBox
        VBox vCurr = new VBox();
        setComboBoxAlignment(vCurr, labelCurrency, cmbCurrency, 10);

        //Vbox for length unit ComboBox
        VBox vUnit = new VBox();
        setComboBoxAlignment(vUnit, labelUnit, cmbLengthUnit, 10);

        //HBox to combine ComboBoxes' VBoxes'
        HBox hCombos = new HBox();
        hCombos.setAlignment(Pos.BASELINE_CENTER);
        hCombos.getChildren().addAll(vCurr, vUnit);

        //Adding everything together
        vBox.getChildren().addAll(hCombos,
                labelRouteLength, txtFieldRouteLength,
                labelFuelCourse, txtFieldFuelCourse,
                labelAvgFuelConsumption, txtFieldAvgFuelConsumption,
                hBox, labelResultPrice);
    }

    private static void setLengthUnitItems() {
        cmbLengthUnit.getItems().addAll(
                "km",
                "mi"
        );
    }

    private static void setCurrencyItems() {
        cmbCurrency.getItems().addAll(
                "PLN",
                "USD",
                "GBP"
        );
    }

    private static void btnClearClicked(VBox vBox) {
        txtFieldAvgFuelConsumption.clear();
        txtFieldFuelCourse.clear();
        txtFieldRouteLength.clear();

        vBox.getChildren().remove(labelResultPrice);
        labelResultPrice = new Label("Price: ");
        vBox.getChildren().add(labelResultPrice);
    }

    private static void btnCalculateClicked(String routeLength, String fuelCourse,
                                            String avgFuelCons, VBox vBox) {
        if(routeLength.equals("") || fuelCourse.equals("") || avgFuelCons.equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Wrong value detected.");
            alert.setContentText("You can't leave empty spaces");
            alert.showAndWait();
        }
        else if(routeLength.contains(",") || fuelCourse.contains(",") || avgFuelCons.contains(",")){
            if(routeLength.contains(",")){
                routeLength = changeCommaToPeriod(routeLength);
            }
            if(fuelCourse.contains(",")){
                fuelCourse = changeCommaToPeriod(fuelCourse);
            }
            if(avgFuelCons.contains(",")){
                avgFuelCons = changeCommaToPeriod(avgFuelCons);
            }
            continueCalculating(routeLength, fuelCourse, avgFuelCons, vBox);
        }
        else {
            continueCalculating(routeLength, fuelCourse, avgFuelCons, vBox);
        }
    }

    private static void calculateAndShow(double routeLengthNum, double fuelCourseNum,
                                         double avgFuelConsNum, VBox vBox) {
        double result = routeLengthNum / 100 * avgFuelConsNum * fuelCourseNum;

        vBox.getChildren().remove(labelResultPrice);
        DecimalFormat f = new DecimalFormat("##.00");
        labelResultPrice = new Label("Price: "+f.format(result)+currency);
        vBox.getChildren().add(labelResultPrice);
    }

    public static void setVBox(VBox vBox, int n){
        vBox.setSpacing(n);
        vBox.setPadding(new Insets(n, n, n, n));
    }
    public static void setHBox(HBox hBox){
        hBox.getChildren().addAll(btnCalculate, btnClear);
        hBox.setAlignment(Pos.BASELINE_CENTER);
        hBox.setSpacing(10);
    }
    public static int returnCommaIndex(String a){
        for(int i = 0; i < a.length(); i++){
            if(String.valueOf(a.charAt(i)).equals(",")){
                return i;
            }
        }
        throw new IllegalArgumentException();
    }
    public static String changeCommaToPeriod(String a){
        StringBuilder newString = new StringBuilder(a);
        newString.setCharAt(returnCommaIndex(a), '.');
        return String.valueOf(newString);
    }
    public static void continueCalculating(String routeLength, String fuelCourse,
                                           String avgFuelCons, VBox vBox){
        try {
            double routeLengthNum = Double.parseDouble(routeLength);
            double fuelCourseNum = Double.parseDouble(fuelCourse);
            double avgFuelConsNum = Double.parseDouble(avgFuelCons);
            calculateAndShow(routeLengthNum, fuelCourseNum, avgFuelConsNum, vBox);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Wrong value detected.");
            alert.setContentText("You should only use numbers.\nDecimal fractions may be seperated " +
                    "by commas or periods.");
            alert.showAndWait();
        }
    }
    public static void getCurrencySign(){
        String a = String.valueOf(cmbCurrency.getSelectionModel().getSelectedItem());
        switch (a){
            case "USD":
                currency = "$";
                break;
            case "PLN":
                currency = "zł";
                break;
            case "GBP":
                currency = "£";
                break;
        }
    }
    public static void setComboBoxAlignment(VBox vBox, Label label, ComboBox comboBox, int i){
        vBox.setAlignment(Pos.BASELINE_CENTER);
        vBox.getChildren().addAll(label, comboBox);
        vBox.setPadding(new Insets(i, i, i, i));
    }

}
