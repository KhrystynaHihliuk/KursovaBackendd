package com.example.kursova;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlserver://trainserverdata.database.windows.net;databaseName=TrainsData;user=khrystyna;password=0123456789.gkt;";

    private static final String CREATE_TICKETS_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS Tickets (" +
            "Id INT PRIMARY KEY IDENTITY(1,1), " +
            "Destination NCHAR(100), " +
            "Departure NCHAR(100), " +
            "FlightNumber NCHAR(20), " +
            "PassengerName NCHAR(100), " +
            "DepartureDate DATE, " +
            "FlightDuration NCHAR(50)" +
            "AircraftType NCHAR(255)"+
            ")";

    private static final String SELECT_ALL_TICKETS_QUERY = "SELECT * FROM Tickets";

    private static final String INSERT_TICKET_QUERY = "INSERT INTO Tickets (Destination, Departure, FlightNumber, PassengerName, DepartureDate, FlightDuration,AircraftType) " +
            "VALUES (?, ?, ?, ?, ?, ?,?)";

    private static final String DELETE_TICKET_QUERY = "DELETE FROM Tickets WHERE Id = ?";

    private static final String UPDATE_TICKET_QUERY = "UPDATE Tickets SET Destination = ?, Departure = ?, FlightNumber = ?, " +
            "PassengerName = ?, DepartureDate = ?, FlightDuration = ? WHERE Id = ?";

    public static void createTicketsTableIfNotExists() {
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(CREATE_TICKETS_TABLE_QUERY);
            System.out.println("Table 'Tickets' created successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Ticket> getTickets() {
        List<Ticket> ticketList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(SELECT_ALL_TICKETS_QUERY);

            while (resultSet.next()) {
                Ticket ticket = mapResultSetToTicket(resultSet);
                ticketList.add(ticket);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ticketList;
    }

    public static int addTicket(Ticket ticket) {
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement statement = connection.prepareStatement(INSERT_TICKET_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            setTicketParameters(statement, ticket);

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                System.out.println("New ticket added successfully.");
                return generatedKeys.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static void deleteTicket(int ticketId) {
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement statement = connection.prepareStatement(DELETE_TICKET_QUERY)) {

            statement.setInt(1, ticketId);
            int rowsAffected = statement.executeUpdate();
            System.out.println("Ticket deleted successfully. Rows affected: " + rowsAffected);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*public static void updateTicket(Ticket ticket) {
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement statement = connection.prepareStatement(UPDATE_TICKET_QUERY)) {

            setTicketParameters(statement, ticket);
            statement.setInt(8, ticket.getId());

            int rowsAffected = statement.executeUpdate();
            System.out.println("Ticket updated successfully. Rows affected: " + rowsAffected);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

    private static Ticket mapResultSetToTicket(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("Id");
        String destination = resultSet.getString("Destination").trim();
        String departure = resultSet.getString("Departure").trim();
        String flightNumber = resultSet.getString("FlightNumber").trim();
        String passengerName = resultSet.getString("PassengerName").trim();
        LocalDate departureDate = resultSet.getDate("DepartureDate").toLocalDate();
        String flightDuration = resultSet.getString("FlightDuration").trim();
        String aircraftType = resultSet.getString("AircraftType").trim();

        Ticket ticket = new Ticket(destination, departure, flightNumber, passengerName, departureDate, flightDuration,aircraftType);
        ticket.setId(id);
        return ticket;
    }

    private static void setTicketParameters(PreparedStatement statement, Ticket ticket) throws SQLException {
        statement.setString(1, ticket.getDestination());
        statement.setString(2, ticket.getDeparture());
        statement.setString(3, ticket.getFlightNumber());
        statement.setString(4, ticket.getPassengerName());
        statement.setDate(5, Date.valueOf(ticket.getDepartureDate()));
        statement.setString(6, ticket.getFlightDuration());
        statement.setString(7, ticket.getAircraftType());
    }

    public static List<Ticket> searchTicketsByFlightNumberAndDepartureDate(String flightNumber, LocalDate departureDate) {
        List<Ticket> tickets = DatabaseManager.getTickets();
        List<Ticket> result = new ArrayList<>();

        for (Ticket ticket : tickets) {
            // Перевіряємо, чи заявка має потрібний номер рейсу та дату вильоту
            if (ticket.getFlightNumber().equals(flightNumber) && ticket.getDepartureDate().equals(departureDate)) {
                result.add(ticket); // Додаємо заявку до результату
            }
        }

        return result;
    }
}
