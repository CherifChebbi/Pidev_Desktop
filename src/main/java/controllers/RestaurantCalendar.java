package controllers;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

public class RestaurantCalendar extends Application {

    private final int NUM_DAYS_IN_WEEK = 7;

    // Example: Map of restaurant IDs to reservations (for demonstration purposes)
    private Map<Integer, Map<LocalDate, Integer>> reservationsByRestaurant = new HashMap<>();

    @Override
    public void start(Stage primaryStage) {
        // Populate reservations (example data)
        populateReservations();

        GridPane calendarGrid = createCalendarGrid(reservationsByRestaurant);

        Scene scene = new Scene(calendarGrid);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Restaurant Calendar");
        primaryStage.show();
    }

    private GridPane createCalendarGrid(Map<Integer, Map<LocalDate, Integer>> reservations) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Add row constraints to allow the calendar to expand vertically
        for (int i = 0; i < NUM_DAYS_IN_WEEK; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100.0 / (NUM_DAYS_IN_WEEK + 1)); // Ensure each row takes equal space
            gridPane.getRowConstraints().add(rowConstraints);
        }

        // Get the current year and month
        YearMonth currentYearMonth = YearMonth.now();

        // Add header row with days of the week
        String[] daysOfWeek = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (int i = 0; i < NUM_DAYS_IN_WEEK; i++) {
            Label dayLabel = new Label(daysOfWeek[i]);
            dayLabel.setTextAlignment(TextAlignment.CENTER);
            gridPane.add(dayLabel, i, 0);
        }

        // Start on the first day of the month and iterate through each day
        LocalDate date = currentYearMonth.atDay(1);
        int row = 1;
        while (date.getMonth().equals(currentYearMonth.getMonth())) {
            int col = date.getDayOfWeek().getValue() % NUM_DAYS_IN_WEEK;
            Label cell = new Label(String.valueOf(date.getDayOfMonth()));
            cell.setPadding(new Insets(5));

            // Example: Display reservations count for each day
            int reservationsCount = reservations.getOrDefault(date, new HashMap<>()).values().stream().mapToInt(Integer::intValue).sum();
            if (reservationsCount > 0) {
                cell.setStyle("-fx-background-color: lightblue;"); // Change cell color
                cell.setText(cell.getText() + " (" + reservationsCount + ")"); // Display reservation count

                // Display reservation details on hover
                StringBuilder reservationDetails = new StringBuilder();
                reservationDetails.append("Reservations:\n");
                for (Map.Entry<Integer, Map<LocalDate, Integer>> restaurantEntry : reservations.entrySet()) {
                    Map<LocalDate, Integer> restaurantReservations = restaurantEntry.getValue();
                    Integer restaurantId = restaurantEntry.getKey();
                    if (restaurantReservations.containsKey(date)) {
                        LocalDate reservationDate = date;
                        int reservationCount = restaurantReservations.get(date);
                        reservationDetails.append("- Restaurant ID: ").append(restaurantId).append(", Date: ").append(reservationDate).append(", Count: ").append(reservationCount).append("\n");
                    }
                }
                Tooltip tooltip = new Tooltip(reservationDetails.toString());
                Tooltip.install(cell, tooltip);
            }

            gridPane.add(cell, col, row);
            date = date.plusDays(1);
            if (col == NUM_DAYS_IN_WEEK - 1) {
                row++;
            }
        }

        return gridPane;
    }


    private void populateReservations() {
        // Example: Populate reservations (for demonstration purposes)
        // This is where you would retrieve reservations from the database
        // For demonstration, I'll add some example reservations
        Map<LocalDate, Integer> reservations = new HashMap<>();
        reservations.put(LocalDate.now(), 2); // Today has 2 reservations
        reservations.put(LocalDate.now().plusDays(1), 1); // Tomorrow has 1 reservation

        // Assuming restaurant ID 1
        reservationsByRestaurant.put(1, reservations);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
