package com.application.main;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import main.PropertyAssessment;
import main.dao.ApiPropertyAssessmentDAO;
import main.dao.CsvPropertyAssessmentDAO;
import main.dao.PropertyAssessmentDAO;
import main.utility.AssessmentClass;
import main.utility.BuildingInformation;
import main.utility.Location;
import main.utility.NeighborhoodInfo;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class PropertyAssessmentApplication extends Application {
    private TableView<PropertyAssessment> tableView;
    private PropertyAssessmentDAO dao;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Edmonton Property Assessments");

        // defaults to API when application start
        this.dao = new ApiPropertyAssessmentDAO();

        tableView = createTableView();

        ChoiceBox<String> dataSourceChoiceBox = createDataSourceChoiceBox();
        ChoiceBox<String> assessmentsFilter = createAssessmentSortChoiceBox();
        VBox dataResult = createDataResultLayout();

        GridPane gridPane = createGridPane(dataSourceChoiceBox, assessmentsFilter, dataResult);
        Scene scene = new Scene(gridPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setGridConstraints(GridPane gridPane) {
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10, 15, 10, 15));

        ColumnConstraints allColumns = new ColumnConstraints();
        allColumns.setHgrow(Priority.ALWAYS);

        RowConstraints allRows = new RowConstraints();
        allRows.setVgrow(Priority.NEVER);

        gridPane.getColumnConstraints().addAll(allColumns, allColumns);
        gridPane.getRowConstraints().addAll(allRows, allRows, allRows, allRows);
    }

    private GridPane createGridPane(ChoiceBox<String> dataSourceChoiceBox,
                                    ChoiceBox<String> assessmentFilter, VBox dataResult) {
        GridPane gridPane = new GridPane();
        setGridConstraints(gridPane);
        TextField accountInput = new TextField();
        TextField addressInput = new TextField();
        TextField neighborhoodInput = new TextField();
        TextField assessMinInput = new TextField();
        assessMinInput.setPromptText("Min Value");
        TextField assessMaxInput = new TextField();
        assessMaxInput.setPromptText("Max Value");

        Button readDataButton = readDataButton(dataSourceChoiceBox);
        Button searchDataButton = searchDataButton(accountInput, addressInput, neighborhoodInput,
                assessmentFilter, assessMinInput, assessMaxInput);

        gridPane.add(new Text("Select Data Source:"), 0, 0, 2, 1);
        gridPane.add(dataSourceChoiceBox, 0, 1, 2, 1);
        gridPane.add(readDataButton, 0, 2, 2, 1);

        gridPane.add(new Text("Account Number:"), 0, 3, 2, 1);
        gridPane.add(accountInput, 0, 4, 2, 1);

        gridPane.add(new Text("Address:"), 0, 5, 2, 1);
        gridPane.add(addressInput, 0, 6, 2, 1);

        gridPane.add(new Text("Neighborhood:"), 0, 7, 2, 1);
        gridPane.add(neighborhoodInput, 0, 8, 2, 1);

        gridPane.add(new Text("Assessment Class:"), 0, 9, 2, 1);
        gridPane.add(assessmentFilter, 0, 10, 2, 1);

        gridPane.add(new Text("Assessed Value Range:"), 0, 11, 2, 1);
        gridPane.add(assessMinInput, 0, 12);
        gridPane.add(assessMaxInput, 1, 12);

        gridPane.add(searchDataButton, 0, 14, 2, 1);
//        gridPane.add(resetButton, 1, 13, 2, 1);

        gridPane.add(dataResult, 2, 0, 1, 15);

        return gridPane;
    }

    private ChoiceBox<String> createDataSourceChoiceBox() {
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll("CSV File", "Edmonton's Online Property Data (API)");
        choiceBox.setValue("Edmonton's Online Property Data (API)");
        choiceBox.setPrefWidth(275);

        readDataButton(choiceBox);

        return choiceBox;
    }

    private Button readDataButton(ChoiceBox<String> choiceBox) {
        Button readDataButton = new Button("Read Data");
        readDataButton.setPrefWidth(100);

        readDataButton.setOnAction(event -> {
            String dataSourceChoice = choiceBox.getValue();
            dataSourceDAO(dataSourceChoice);
        });
        return readDataButton;
    }

    private List<String> selectedFilter(String accountNum, String address,
                                        String neighborhood, String assessmentClass,
                                        String min, String max) {
        List<String> selectedFilters = new ArrayList<>();
        if (!accountNum.isEmpty()) {
            selectedFilters.add("Account Number");
        }
        if (!address.isEmpty()) {
            selectedFilters.add("Address");
        }
        if (!neighborhood.isEmpty()) {
            selectedFilters.add("Neighborhood");
        }
        if (assessmentClass != null && !assessmentClass.equals("All")) {
            selectedFilters.add("Assessment Class");
        }
        if (!min.isEmpty()) {
            selectedFilters.add("Min");
        }
        if (!max.isEmpty()) {
            selectedFilters.add("Max");
        }
        return selectedFilters;
    }

    private Button searchDataButton(TextField accountInput, TextField addressInput,
                                    TextField neighborhoodInput, ChoiceBox<String> assessmentFilter,
                                    TextField min, TextField max) {
        Button searchButton = new Button("Search");
        searchButton.setPrefWidth(100);

        searchButton.setOnAction(event -> {
            String accountNum = accountInput.getText();
            String address = addressInput.getText();
            String neighborhood = neighborhoodInput.getText();
            String assessmentClass = assessmentFilter.getValue();
            String minValue = min.getText();
            String maxValue = max.getText();

            // Contains current selected filters
            List<String> selectedFilters = selectedFilter(accountNum, address, neighborhood, assessmentClass, minValue, maxValue);
            // testing
            System.out.println(selectedFilters);

            if (!selectedFilters.isEmpty()) {
                if (selectedFilters.size() == 1) {
                    if (selectedFilters.contains("Account Number")) {
                        PropertyAssessment result = dao.getByAccountNumber(accountNum);
                        showSingleResult(result, accountNum);
                    }
//                    if (selectedFilters.contains("Address")) {
//                        PropertyAssessment result = dao.getByAddress(address);
//                        showSingleResult(result, address);
//                    }
                    if (selectedFilters.contains("Neighborhood")) {
                        ArrayList<PropertyAssessment> result = dao.getByNeighbourhood(neighborhood).getPropertyAssessmentList();
                        loopProperties(result, neighborhood);
                    }
                    if (selectedFilters.contains("Assessment Class")) {
                        ArrayList<PropertyAssessment> result = dao.getByAssessmentClass(assessmentClass).getPropertyAssessmentList();
                        loopProperties(result, assessmentClass);
                    }
//                    if (selectedFilters.contains("Min") || selectedFilters.contains("Max")){
//
//                    }
                }
                else{
                    ArrayList<PropertyAssessment> combinedResults = new ArrayList<>();
                    // more than one filter
                }
            } else {
                // no filter selected
            }
        });
        return searchButton;
    }

    private void dataSourceDAO(String dataSourceChoice) {
        if ("CSV File".equals(dataSourceChoice)) {
            this.dao = new CsvPropertyAssessmentDAO();
        } else if ("Edmonton's Online Property Data (API)".equals(dataSourceChoice)) {
            this.dao = new ApiPropertyAssessmentDAO();
        }
        ObservableList<PropertyAssessment> data = FXCollections.observableArrayList(dao.getAll());
        tableView.setItems(data);
    }

    private ChoiceBox<String> createAssessmentSortChoiceBox() {
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.setPrefWidth(175);
        choiceBox.getItems().addAll("All", "Residential", "Commercial", "Farmland");//, "Other Residential");
        choiceBox.setValue("All");

        return choiceBox;
    }

    private TableView<PropertyAssessment> createTableView() {
        TableView<PropertyAssessment> table = new TableView<>();

        String[] columnTitles = {
                "Account Number", "Address", "Assessed Value", "Assessment Class", "Neighborhood", "(Latitude, Longitude)"
        };

        for (String title : columnTitles) {
            TableColumn<PropertyAssessment, String> column = createColumn(title);
            table.getColumns().add(column);
        }

        ObservableList<PropertyAssessment> data = FXCollections.observableArrayList(dao.getAll());
        table.setItems(data);
        table.setPrefHeight(600);
        return table;
    }

    private TableColumn<PropertyAssessment, String> createColumn(String title) {
        TableColumn<PropertyAssessment, String> column = new TableColumn<>(title);
        column.setPrefWidth(200);

        column.setCellValueFactory(param -> {
            PropertyAssessment property = param.getValue();

            if ("Account Number".equals(title)) {
                return new SimpleStringProperty(getAccountNumber(property));
            } else if ("Address".equals(title)) {
                return new SimpleStringProperty(getAddress(property));
            } else if ("Assessed Value".equals(title)) {
                return new SimpleStringProperty(getAssessedValue(property));
            } else if ("Assessment Class".equals(title)) {
                return new SimpleStringProperty(getAssessmentClass(property));
            } else if ("Neighborhood".equals(title)) {
                return new SimpleStringProperty(getNeighborhood(property));
            } else if ("(Latitude, Longitude)".equals(title)) {
                return new SimpleStringProperty(getLatitudeLongitude(property));
            } else {
                return new SimpleStringProperty("");
            }
        });

        return column;
    }

    private String getAccountNumber(PropertyAssessment property) {
        BuildingInformation buildingInfo = property.getBuildingInfo();
        return (buildingInfo != null) ? String.valueOf(buildingInfo.getAccountNum()) : "";
    }

    private String getAddress(PropertyAssessment property) {
        BuildingInformation buildingInfo = property.getBuildingInfo();
        if (buildingInfo != null) {
            String suite = buildingInfo.getSuite() == 0 ? "" : String.valueOf(buildingInfo.getSuite());
            String houseNum = buildingInfo.getHouseNum() == 0 ? "" : String.valueOf(buildingInfo.getHouseNum());
            String streetName = buildingInfo.getStreetName();
            return suite + " " + houseNum + " " + streetName;
        }
        return "";
    }

    private String getAssessedValue(PropertyAssessment property) {
        NeighborhoodInfo neighborhoodInfo = property.getNeighborhoodInfo();
        NumberFormat currencyFormat = new DecimalFormat("$#,##0");
        return (neighborhoodInfo != null) ? currencyFormat.format(neighborhoodInfo.getAssessedValue()) : currencyFormat.format(0);
    }

    private String getAssessmentClass(PropertyAssessment property) {
        AssessmentClass assessmentClass = property.getAssessmentClass();
        if (assessmentClass != null) {
            String assessmentClass1PercentStr = property.formatPercentage(assessmentClass.getAssessment1Percent());
            String assessmentClass2PercentStr = property.formatPercentage(assessmentClass.getAssessment2Percent());
            String assessmentClass3PercentStr = property.formatPercentage(assessmentClass.getAssessment3Percent());
            return String.format("[%s %s, %s %s, %s %s]",
                    assessmentClass.getAssessmentClass1(), assessmentClass1PercentStr,
                    assessmentClass.getAssessmentClass2(), assessmentClass2PercentStr,
                    assessmentClass.getAssessmentClass3(), assessmentClass3PercentStr);
        }
        return "";
    }

    private String getNeighborhood(PropertyAssessment property) {
        NeighborhoodInfo neighborhoodInfo = property.getNeighborhoodInfo();
        return (neighborhoodInfo != null) ? neighborhoodInfo.getNeighborhood() + " (" + neighborhoodInfo.getWard() + ")" : "";
    }

    private String getLatitudeLongitude(PropertyAssessment property) {
        Location location = property.getLocation();
        return (location != null) ? location.getPointLocation() : "";
    }

    private void showSingleResult(PropertyAssessment result, String input) {
        if (result != null) {
            tableView.getItems().add(result);
        } else {
            resultNotFound(input);
        }
    }

    private void loopProperties(ArrayList<PropertyAssessment> result, String input) {
        tableView.getItems().clear();
        if (!result.isEmpty()) {
            for (PropertyAssessment assessment : result) {
                showSingleResult(assessment, input);
            }
        } else {
            resultNotFound(input);
        }
    }

    private void resultNotFound(String input) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Search Result");
        alert.setHeaderText("No matching result found");
        alert.setContentText("The specified input " +
                '"' + input + '"' + " does not exist in the selected database or filter.");
        alert.showAndWait();
    }

    private VBox createDataResultLayout() {
        return new VBox(tableView);
    }
}