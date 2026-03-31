import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.ArrayList;

class LocationService {
    public static String getLocation() {
        try {
            URL url = new URL("https://ipinfo.io/json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            String data = response.toString();

            String city = extract(data, "\"city\":");
            String region = extract(data, "\"region\":");
            String country = extract(data, "\"country\":");

            return city + ", " + region + ", " + country;

        } catch (Exception e) {
            return "Location not available";
        }
    }

    private static String extract(String data, String key) {
        int start = data.indexOf(key);
        if (start == -1) return "N/A";
        start = data.indexOf("\"", start + key.length()) + 1;
        int end = data.indexOf("\"", start);
        return data.substring(start, end);
    }
}

class BookingService {
    static ArrayList<String> history = new ArrayList<>();

    public static String bookFlight(String name, String from, String to, String date) {
        if (name.isEmpty() || from.isEmpty() || to.isEmpty() || date.isEmpty()) {
            return "⚠ Fill all flight details!";
        }

        String result = "✈ Flight booked for " + name + "\nFrom: " + from +
                "\nTo: " + to + "\nDate: " + date;

        history.add(result);
        return result;
    }

    public static String bookHotel(String name, String city, String days) {
        if (name.isEmpty() || city.isEmpty() || days.isEmpty()) {
            return "⚠ Fill all hotel details!";
        }

        String result = "🏨 Hotel booked for " + name +
                "\nCity: " + city + "\nDays: " + days;

        history.add(result);
        return result;
    }

    public static String getHistory() {
        if (history.isEmpty()) return "No bookings yet.";
        StringBuilder sb = new StringBuilder();
        for (String s : history) {
            sb.append(s).append("\n\n");
        }
        return sb.toString();
    }
}

public class TravelBookingSystem {

    public static void main(String[] args) {

        JFrame frame = new JFrame("🌍 Travel Booking System");
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();

        // -------- FLIGHT PANEL --------
        JPanel flightPanel = new JPanel(new GridLayout(6, 2, 5, 5));

        JTextField nameField = new JTextField();

        String cities[] = {"Delhi", "Mumbai", "Patna", "Kolkata", "Chennai"};
        JComboBox<String> fromBox = new JComboBox<>(cities);
        JComboBox<String> toBox = new JComboBox<>(cities);

        JTextField dateField = new JTextField("YYYY-MM-DD");

        JButton bookFlightBtn = new JButton("Book Flight");

        JTextArea flightOutput = new JTextArea();
        flightOutput.setEditable(false);

        flightPanel.add(new JLabel("Name:"));
        flightPanel.add(nameField);

        flightPanel.add(new JLabel("From:"));
        flightPanel.add(fromBox);

        flightPanel.add(new JLabel("To:"));
        flightPanel.add(toBox);

        flightPanel.add(new JLabel("Date:"));
        flightPanel.add(dateField);

        flightPanel.add(bookFlightBtn);
        flightPanel.add(new JLabel(""));

        flightPanel.add(new JScrollPane(flightOutput));

        // -------- HOTEL PANEL --------
        JPanel hotelPanel = new JPanel(new GridLayout(5, 2, 5, 5));

        JTextField hNameField = new JTextField();

        JComboBox<String> cityBox = new JComboBox<>(cities);

        JTextField daysField = new JTextField();

        JButton bookHotelBtn = new JButton("Book Hotel");

        JTextArea hotelOutput = new JTextArea();
        hotelOutput.setEditable(false);

        hotelPanel.add(new JLabel("Name:"));
        hotelPanel.add(hNameField);

        hotelPanel.add(new JLabel("City:"));
        hotelPanel.add(cityBox);

        hotelPanel.add(new JLabel("Days:"));
        hotelPanel.add(daysField);

        hotelPanel.add(bookHotelBtn);
        hotelPanel.add(new JLabel(""));

        hotelPanel.add(new JScrollPane(hotelOutput));

        // -------- HISTORY PANEL --------
        JPanel historyPanel = new JPanel(new BorderLayout());

        JTextArea historyArea = new JTextArea();
        historyArea.setEditable(false);

        JButton refreshBtn = new JButton("Refresh History");

        historyPanel.add(new JScrollPane(historyArea), BorderLayout.CENTER);
        historyPanel.add(refreshBtn, BorderLayout.SOUTH);

        // -------- TOP LOCATION --------
        JLabel locationLabel = new JLabel("📍 Your Location: " + LocationService.getLocation());

        // -------- BUTTON ACTIONS --------
        bookFlightBtn.addActionListener(e -> {
            String result = BookingService.bookFlight(
                    nameField.getText(),
                    (String) fromBox.getSelectedItem(),
                    (String) toBox.getSelectedItem(),
                    dateField.getText()
            );
            flightOutput.setText(result);
        });

        bookHotelBtn.addActionListener(e -> {
            String result = BookingService.bookHotel(
                    hNameField.getText(),
                    (String) cityBox.getSelectedItem(),
                    daysField.getText()
            );
            hotelOutput.setText(result);
        });

        refreshBtn.addActionListener(e -> {
            historyArea.setText(BookingService.getHistory());
        });

        // -------- ADD TABS --------
        tabs.add("Flight Booking", flightPanel);
        tabs.add("Hotel Booking", hotelPanel);
        tabs.add("Booking History", historyPanel);

        frame.add(locationLabel, BorderLayout.NORTH);
        frame.add(tabs, BorderLayout.CENTER);

        frame.setVisible(true);
    }
}