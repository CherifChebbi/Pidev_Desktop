package controllers.Calendar;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import models.Reservation;
import services.ReservationService.ServiceReservation;

import java.net.URL;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.ResourceBundle;

public class CalendarController implements Initializable {

    ZonedDateTime dateFocus;
    ZonedDateTime today;
    ServiceReservation reservationService;

    @FXML
    private Text year;

    @FXML
    private Text month;

    @FXML
    private FlowPane calendar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
        reservationService = new ServiceReservation();
        drawCalendar();
    }

    @FXML
    void backOneMonth(ActionEvent event) {
        dateFocus = dateFocus.minusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    @FXML
    void forwardOneMonth(ActionEvent event) {
        dateFocus = dateFocus.plusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    private void drawCalendar() {
        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(String.valueOf(dateFocus.getMonth()));

        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calendar.getHgap();
        double spacingV = calendar.getVgap();

        int monthMaxDate = dateFocus.getMonth().length(dateFocus.toLocalDate().isLeapYear());
        int dateOffset = dateFocus.getDayOfWeek().getValue();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                StackPane stackPane = new StackPane();

                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth = (calendarWidth / 7) - strokeWidth - spacingH;
                double rectangleHeight = (calendarHeight / 6) - strokeWidth - spacingV;
                rectangle.setWidth(rectangleWidth);
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);

                int calculatedDate = (j + 1) + (7 * i);
                if (calculatedDate > dateOffset && calculatedDate <= dateOffset + monthMaxDate) {
                    int currentDate = calculatedDate - dateOffset;

                    Text date = new Text(String.valueOf(currentDate));
                    double textTranslationY = -(rectangleHeight / 2) * 0.75;
                    date.setTranslateY(textTranslationY);
                    stackPane.getChildren().add(date);

                    try {
                        List<Reservation> reservations = reservationService.getReservationsForDate(
                                dateFocus.getYear(),
                                dateFocus.getMonthValue(),
                                currentDate
                        );
                        if (!reservations.isEmpty()) {
                            // Add reservations to the cell
                            for (Reservation reservation : reservations) {
                                String restaurantName = reservationService.getRestaurantName(reservation.getrestaurant_id());
                                Text reservationText = new Text(
                                        reservation.getNom() + ", " + reservation.getNbrPersonne() + ", " + restaurantName
                                );
                                stackPane.getChildren().add(reservationText);
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                // Highlight today's date
                if (today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth() && today.getDayOfMonth() == calculatedDate) {
                    rectangle.setStroke(Color.BLUE);
                }
                calendar.getChildren().add(stackPane);
            }
        }
    }
}
