package com.application.main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import main.utility.PropertyAssessment;
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
import java.util.Objects;

/**
 * The PropertyAssessmentApplication class is the graphical user interface (GUI)
 * for searching and displaying property assessments. It provides a user-friendly interface
 * to interact with and shows a table of property assessment data.
 * <p>
 * - Allows users to choose a data source (CSV or API)
 * <p>
 * - User-friendly table to show results
 * <p>
 * - Search function with single or advance filters.
 * <p>
 *
 * @author Fahad Ali
 */
public class PropertyAssessmentApplication extends Application {
    private TableView<PropertyAssessment> tableView;
    private PropertyAssessmentDAO dao;
    private Label countLabel;
    private final Label daoSelected = new Label("Select Data Source: ");
    private final Label dataInBackground = new Label("Entries Cached: 0");
    private final Label timeElapsedLabel = new Label("Time to Load: 0 milliseconds");

    /**
     * Initialize and start up the application.
     *
     * @param primaryStage The primary stage for the JavaFX application.
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Edmonton Property Assessments");
        tableView = createTableView();

        ChoiceBox<String> dataSourceChoiceBox = createDataSourceChoiceBox();
        ChoiceBox<String> assessmentsFilter = createAssessmentSortChoiceBox();
        ScrollPane dataResult = createDataResultLayout();
        countLabel = new Label("Entries Displayed: " + tableView.getItems().size());

        GridPane gridPane = createGridPane(dataSourceChoiceBox, assessmentsFilter, dataResult, countLabel);
        Scene scene = new Scene(gridPane);
        setStage(primaryStage, scene);
    }

    /**
     * Configures the stage with the following width and height.
     *
     * @param primaryStage The primary stage of the JavaFX application.
     * @param scene        The scene to set for the primary stage.
     */
    private void setStage(Stage primaryStage, Scene scene) {
        primaryStage.setMinHeight(500);
        primaryStage.setMinWidth(500);
        primaryStage.setMaxWidth(1325);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Helper function to set horizontal lines in GUI.
     *
     * @return The line.
     */
    private Line createHorizontalLine() {
        return new Line(0, 0, 235, 0);
    }

    /**
     * Helper function create text fields.
     *
     * @param promptText The textField prompt.
     * @return The text field.
     */
    private TextField createText(String promptText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        return textField;
    }

    /**
     * Helper function to set grid constraints.
     *
     * @param gridPane The gridPane to set constraints on.
     */
    private void setGridConstraints(GridPane gridPane) {
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(13));

        RowConstraints allowVertical = new RowConstraints();
        allowVertical.setVgrow(Priority.ALWAYS);

        int numRows = 19;
        for (int i = 0; i < numRows; i++) {
            gridPane.getRowConstraints().add(allowVertical);
        }
    }

    /**
     * Creates the grid of the GUI.
     *
     * @param dataSourceChoiceBox The ChoiceBox for selecting the data source.
     * @param assessmentFilter    The ChoiceBox for sorting by filter.
     * @param dataResult          The ScrollPane of the table for displaying data results.
     * @param countLabel          The Label for displaying the count of entries.
     * @return The gridPane.
     */
    private GridPane createGridPane(ChoiceBox<String> dataSourceChoiceBox,
                                    ChoiceBox<String> assessmentFilter, ScrollPane dataResult,
                                    Label countLabel) {
        GridPane gridPane = new GridPane();
        setGridConstraints(gridPane);

        TextField accountInput = createText("Account Number (e.g., 1103530)");
        TextField addressInput = createText("Address (e.g., 104 street nw)");
        TextField neighborhoodInput = createText("Neighborhood (e.g., oliver)");
        TextField assessMinInput = createText("Min Value");
        TextField assessMaxInput = createText("Max Value");

        Button readDataButton = readDataButton(dataSourceChoiceBox);
        Button resetButton = resetButton();
        Button searchDataButton = searchButton(accountInput, addressInput, neighborhoodInput,
                assessmentFilter, assessMinInput, assessMaxInput);

        addToGrid(gridPane, dataSourceChoiceBox, readDataButton,
                accountInput, addressInput, neighborhoodInput,
                assessmentFilter, assessMinInput, assessMaxInput,
                searchDataButton, resetButton, dataResult, countLabel);

        return gridPane;
    }

    /**
     * Adds all components to GridPane for the GUI.
     *
     * @param gridPane            The gridPane to add components to.
     * @param dataSourceChoiceBox The ChoiceBox for the data source.
     * @param readDataButton      The button to read the data source.
     * @param accountInput        The TextField for searching an account number,
     * @param addressInput        The TextField for searching an address.
     * @param neighborhoodInput   The TextField for searching a neighborhood.
     * @param assessmentFilter    The ChoiceBox for sorting by assessment.
     * @param assessMinInput      The TextField for entering the minimum assessment property values.
     * @param assessMaxInput      The TextField for entering the maximum assessment property values.
     * @param searchDataButton    The Button for searching the given filters.
     * @param resetButton         The Button to reset the data.
     * @param dataResult          The ScrollPane for displaying data results in a table.
     * @param countLabel          The Label for displaying the count of entries.
     */
    private void addToGrid(GridPane gridPane, ChoiceBox<String> dataSourceChoiceBox,
                           Button readDataButton, TextField accountInput,
                           TextField addressInput, TextField neighborhoodInput,
                           ChoiceBox<String> assessmentFilter, TextField assessMinInput,
                           TextField assessMaxInput, Button searchDataButton,
                           Button resetButton, ScrollPane dataResult,
                           Label countLabel) {
        daoSelected.setStyle("-fx-font-weight: bolder; -fx-font-size: 14;");
        gridPane.add(daoSelected, 0, 0, 2, 1);
        gridPane.add(dataSourceChoiceBox, 0, 1, 2, 1);
        gridPane.add(readDataButton, 0, 2, 2, 1);

        gridPane.add(createHorizontalLine(), 0, 3, 2, 1);

        gridPane.add(new Text("Account Number:"), 0, 4, 2, 1);
        gridPane.add(accountInput, 0, 5, 2, 1);

        gridPane.add(new Text("Address:"), 0, 6, 2, 1);
        gridPane.add(addressInput, 0, 7, 2, 1);

        gridPane.add(new Text("Neighborhood:"), 0, 8, 2, 1);
        gridPane.add(neighborhoodInput, 0, 9, 2, 1);

        gridPane.add(new Text("Assessment Class:"), 0, 10, 2, 1);
        gridPane.add(assessmentFilter, 0, 11, 2, 1);

        gridPane.add(new Text("Assessed Value Range:"), 0, 12, 2, 1);
        gridPane.add(assessMinInput, 0, 13);
        gridPane.add(assessMaxInput, 1, 13);

        gridPane.add(searchDataButton, 0, 14, 2, 1);
        gridPane.add(resetButton, 1, 14, 2, 1);

        gridPane.add(dataResult, 2, 0, 1, 19);

        gridPane.add(createHorizontalLine(), 0, 15, 2, 1);
        gridPane.add(countLabel, 0, 16, 2, 1);
        gridPane.add(dataInBackground, 0, 17, 2, 1);
        gridPane.add(timeElapsedLabel, 0, 18, 2, 1);
    }

    /**
     * Creates choice box for selecting by assessment class.
     *
     * @return The ChoiceBox.
     */
    private ChoiceBox<String> createAssessmentSortChoiceBox() {
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.setPrefWidth(260);
        choiceBox.getItems().addAll("", "Residential", "Commercial", "Farmland", "Other Residential");
        return choiceBox;
    }

    /**
     * Creates choice box for selecting by data soure.
     *
     * @return The ChoiceBox.
     */
    private ChoiceBox<String> createDataSourceChoiceBox() {
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll("CSV File", "Edmonton's Online Property Data (API)");
        choiceBox.setValue("Edmonton's Online Property Data (API)");
        choiceBox.setPrefWidth(260);

        readDataButton(choiceBox);
        return choiceBox;
    }

    /**
     * Helper function to set the dao object to selected data source.
     */
    private void dataSourceDAO(String dataSourceChoice) {
        if ("CSV File".equals(dataSourceChoice)) {
            this.dao = new CsvPropertyAssessmentDAO();
        } else if ("Edmonton's Online Property Data (API)".equals(dataSourceChoice)) {
            this.dao = new ApiPropertyAssessmentDAO();
        }
    }

    /**
     * Load data into the TableView.
     */
    private void loadData(ChoiceBox<String> choiceBox) {
        long startTime = System.currentTimeMillis();
        daoSelected.setText("~~~~~~~~~~~Loading Data~~~~~~~~~~~");

        Thread loadDataBackground = new Thread(() -> {
            String dataSourceChoice = choiceBox.getValue();
            dataSourceDAO(dataSourceChoice);
            ObservableList<PropertyAssessment> allEntries = FXCollections.observableArrayList(dao.getAll());

            Platform.runLater(() -> {
                tableView.getItems().clear();
                tableView.getItems().addAll(allEntries.subList(0, 1000));
                updateEntryCount();
                timeElapsed(startTime);
                updateSelectedDAOLabel(choiceBox);
            });
        });
        loadDataBackground.start();
    }

    /**
     * Reads data from selected data source.
     *
     * @return The Button for reading data.
     */
    private Button readDataButton(ChoiceBox<String> choiceBox) {
        Button readDataButton = new Button("Read Data");
        readDataButton.setPrefWidth(260);

        readDataButton.setOnAction(event -> {
            updateSelectedDAOLabel(choiceBox);
            loadData(choiceBox);
        });
        return readDataButton;
    }

    /**
     * Resets all values to default.
     *
     * @return The Button for resetting data.
     */
    private Button resetButton() {
        Button resetButton = new Button("Reset");
        resetButton.setPrefWidth(115);

        resetButton.setOnAction(event -> {
            this.dao = null;
            tableView.getItems().clear();
            timeElapsedLabel.setText("Time to Load: 0 milliseconds");
            dataInBackground.setText("Entries Cached: 0 properties");
            daoSelected.setText("Selected Data Source: None");
            updateEntryCount();
        });
        return resetButton;
    }

    /**
     * Search button based on filters.
     *
     * @param accountInput      The account number to search.
     * @param addressInput      The address to search.
     * @param neighborhoodInput The neighborhood to search,
     * @param assessmentFilter  The assessment filter to search.
     * @param max               The maximum assessment filter.
     * @param min               The minimum assessment filter.
     * @return The Button for searching data.
     */
    private Button searchButton(TextField accountInput, TextField addressInput,
                                TextField neighborhoodInput, ChoiceBox<String> assessmentFilter,
                                TextField min, TextField max) {
        Button searchButton = new Button("Search");
        searchButton.setPrefWidth(115);

        searchButton.setOnAction(event -> {
            long startTime = System.currentTimeMillis();
            String accountNum = accountInput.getText();
            String address = addressInput.getText();
            String neighborhood = neighborhoodInput.getText();
            String assessmentClass = assessmentFilter.getValue();
            String minValue = min.getText();
            String maxValue = max.getText();

            // Contains current selected filters
            List<String> selectedFilters = selectedFilter(accountNum, address, neighborhood, assessmentClass, minValue,
                    maxValue);
            if (dao == null) {
                noDataSourceSelected();
            } else {
                if (!selectedFilters.isEmpty()) {
                    tableView.getItems().clear();
                    if (selectedFilters.size() == 1 && !(selectedFilters.contains("Min") || selectedFilters.contains("Max"))) {
                        singleFilter(selectedFilters, accountNum, address, neighborhood, assessmentClass);
                    }
                    if (selectedFilters.size() >= 2 || selectedFilters.contains("Min") || selectedFilters.contains("Max")) {
                        if (minValue.isEmpty()) {
                            minValue = String.valueOf(0);
                        }
                        if (maxValue.isEmpty()) {
                            maxValue = String.valueOf(999999999);
                        }
                        List<PropertyAssessment> results = dao.multipleFilter(accountNum, neighborhood, assessmentClass,
                                address, minValue, maxValue).getPropertyAssessmentList();
                        displayResults(results);
                    }
                } else {
                    noFiltersSelected();
                }
            }
            updateEntryCount();
            timeElapsed(startTime);
        });
        return searchButton;
    }

    /**
     * Shows what filters have been selected.
     *
     * @param accountNum      The account number to search.
     * @param address         The address to search.
     * @param neighborhood    The neighborhood to search,
     * @param assessmentClass The assessment filter to search.
     * @param min             The minimum assessment filter.
     * @param max             The maximum assessment filter.
     * @return The selected filters.
     */
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
        if (assessmentClass != null && !assessmentClass.isEmpty()) {
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

    /**
     * Filters data specifically for single filters.
     *
     * @param selectedFilters Show the filter selected
     * @param accountNum      The account number to search.
     * @param address         The address to search.
     * @param neighborhood    The neighborhood to search,
     * @param assessmentClass The assessment filter to search.
     */
    private void singleFilter(List<String> selectedFilters,
                              String accountNum, String address,
                              String neighborhood, String assessmentClass) {

        List<PropertyAssessment> results = new ArrayList<>();
        if (dao == null) {
            noDataSourceSelected();
        } else {
            if (selectedFilters.contains("Account Number")) {
                PropertyAssessment result = dao.getByAccountNumber(accountNum);
                if (result != null) {
                    results.add(result);
                }
            }
            if (selectedFilters.contains("Address")) {
                results.addAll(dao.getByAddress(address).getPropertyAssessmentList());
            }
            if (selectedFilters.contains("Neighborhood")) {
                results.addAll(dao.getByNeighbourhood(neighborhood).getPropertyAssessmentList());
            }
            if (selectedFilters.contains("Assessment Class")) {
                results.addAll(dao.getByAssessmentClass(assessmentClass).getPropertyAssessmentList());
            }
            displayResults(results);
        }
    }

    /**
     * Displays result to table.
     *
     * @param results The list of PropertyAssessments.
     */
    private void displayResults(List<PropertyAssessment> results) {
        if (results.isEmpty()) {
            resultNotFound();
        } else {
            // limit to show 1000 entries, but still has all properties loaded ("cached") in background
            tableView.getItems().addAll(results.subList(0, Math.min(results.size(), 1000)));
        }
    }

    /**
     * Create the table and its columns.
     *
     * @return The TableView.
     */
    private TableView<PropertyAssessment> createTableView() {
        TableView<PropertyAssessment> table = new TableView<>();

        String[] columnTitles = {
                "Account Number", "Address", "Assessed Value", "Assessment Class", "Neighborhood", "(Latitude, Longitude)"
        };

        for (String title : columnTitles) {
            TableColumn<PropertyAssessment, String> column = createColumn(title);
            table.getColumns().add(column);
        }

        table.setPrefHeight(600);
        return table;
    }

    /**
     * Create the columns for the TableView.
     *
     * @param title Name of column.
     * @return The TableColumn.
     */
    private TableColumn<PropertyAssessment, String> createColumn(String title) {
        TableColumn<PropertyAssessment, String> column = new TableColumn<>(title);

        if ("Account Number".equals(title)) {
            column.setPrefWidth(100);
        } else if ("Assessed Value".equals(title)) {
            column.setPrefWidth(100);
        } else if ("(Latitude, Longitude)".equals(title)) {
            column.setPrefWidth(290);
        } else {
            column.setPrefWidth(185);
        }

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

    /**
     * Gets the account number information from PropertyAssessment class.
     *
     * @param property Name of property.
     * @return String of the property.
     */
    private String getAccountNumber(PropertyAssessment property) {
        BuildingInformation buildingInfo = property.getBuildingInfo();
        return (buildingInfo != null) ? String.valueOf(buildingInfo.getAccountNum()) : "";
    }

    /**
     * Gets the address information from PropertyAssessment class.
     *
     * @param property Name of property.
     * @return String of the property.
     */
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

    /**
     * Gets the assessed value information from PropertyAssessment class.
     *
     * @param property Name of property.
     * @return String of the property.
     */
    private String getAssessedValue(PropertyAssessment property) {
        NeighborhoodInfo neighborhoodInfo = property.getNeighborhoodInfo();
        NumberFormat currencyFormat = new DecimalFormat("$#,##0");
        return (neighborhoodInfo != null) ? currencyFormat.format(neighborhoodInfo.getAssessedValue()) :
                currencyFormat.format(0);
    }

    /**
     * Gets the assessment class information from PropertyAssessment class.
     *
     * @param property Name of property.
     * @return String of the property.
     */
    private String getAssessmentClass(PropertyAssessment property) {
        AssessmentClass assessmentClass = property.getAssessmentClass();
        if (assessmentClass != null) {
            String assessmentClass1PercentStr = property.formatPercentage(assessmentClass.getAssessment1Percent());
            String assessmentClass2PercentStr = property.formatPercentage(assessmentClass.getAssessment2Percent());
            String assessmentClass3PercentStr = property.formatPercentage(assessmentClass.getAssessment3Percent());

            StringBuilder result = new StringBuilder();
            result.append(assessmentClass.getAssessmentClass1()).append(" ").append(assessmentClass1PercentStr);

            if (!Objects.equals(assessmentClass.getAssessmentClass2(), "")) {
                result.append(", ").append(assessmentClass.getAssessmentClass2()).append(" ").append(assessmentClass2PercentStr);
            }

            if (!Objects.equals(assessmentClass.getAssessmentClass3(), "")) {
                result.append(", ").append(assessmentClass.getAssessmentClass3()).append(" ").append(assessmentClass3PercentStr);
            }

            return "[" + result + "]";
        }
        return "";
    }

    /**
     * Gets the neighborhood information from PropertyAssessment class.
     *
     * @param property Name of property.
     * @return String of the property.
     */
    private String getNeighborhood(PropertyAssessment property) {
        NeighborhoodInfo neighborhoodInfo = property.getNeighborhoodInfo();
        return (neighborhoodInfo != null) ? neighborhoodInfo.getNeighborhood() + " (" + neighborhoodInfo.getWard() + ")"
                : "";
    }

    /**
     * Gets the coordinate information from PropertyAssessment class.
     *
     * @param property Name of property.
     * @return String of the property.
     */
    private String getLatitudeLongitude(PropertyAssessment property) {
        Location location = property.getLocation();
        return (location != null) ? location.getPointLocation() : "";
    }

    /**
     * Shows alert for no result found.
     */
    private void resultNotFound() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Search Result");
        alert.setHeaderText("No matching result found");
        alert.setContentText("The specified input does not exist in the selected database.");
        alert.showAndWait();
    }

    /**
     * Shows alert for no data source selected.
     */
    private void noDataSourceSelected() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Search Result");
        alert.setHeaderText("No data source selected");
        alert.setContentText("Please select data source and hit \"Read Data\" before searching.");
        alert.showAndWait();
    }

    /**
     * Shows alert for no filters selected.
     */
    private void noFiltersSelected() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Search Result");
        alert.setHeaderText("No filters selected");
        alert.setContentText("Please select filters before searching.");
        alert.showAndWait();
    }

    /**
     * Gives scroll functionality to the table.
     *
     * @return ScrollPane for tableView.
     */
    private ScrollPane createDataResultLayout() {
        ScrollPane scrollPane = new ScrollPane(tableView);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        return scrollPane;
    }

    /**
     * Updates label for selected data source.
     *
     * @param choiceBox The ChoiceBox for the data source.
     */
    private void updateSelectedDAOLabel(ChoiceBox<String> choiceBox) {
        if (Objects.equals(choiceBox.getValue(), "Edmonton's Online Property Data (API)")) {
            daoSelected.setText("Selected Data Source -> API");
        } else if (Objects.equals(choiceBox.getValue(), "CSV File")) {
            daoSelected.setText("Selected Data Source -> CSV");
        }
    }

    /**
     * Updates label for amount of entries displayed and/or cached.
     */
    private void updateEntryCount() {
        countLabel.setText("Entries Displayed: " + tableView.getItems().size() + " properties");
        if (dao != null) {
            dataInBackground.setText("Entries Cached: " + dao.getAll().size() + " properties");
        }
    }

    /**
     * Updates label for total time taken to load.
     */
    private void timeElapsed(long startTime) {
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        timeElapsedLabel.setText("Time to Load: " + elapsedTime + " milliseconds");
    }
}