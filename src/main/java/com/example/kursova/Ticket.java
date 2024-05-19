package com.example.kursova;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class Ticket {
        @JsonFormat(pattern = "dd.MM.yyyy")
        public int id;
        public String destination;
        public String departure;
        public String flightNumber;
        public String passengerName;
        public String flightDuration;
        private LocalDate departureDate;
        public String aircraftType;
        public Ticket() {}
        public Ticket(String destination, String departure, String flightNumber,
                      String passengerName, LocalDate departureDate, String flightDuration, String aircraftType) {
            this.destination = destination;
            this.departure = departure;
            this.flightNumber = flightNumber;
            this.passengerName = passengerName;
            this.departureDate = departureDate;
            this.flightDuration = flightDuration;
            this.aircraftType = aircraftType;
        }
        public int getId() {
            return id;
        }
        public String getDestination() {
            return destination;
        }
        public String getDeparture() {
            return departure;
        }
        public String getFlightNumber() {
            return flightNumber;
        }
        public String getPassengerName() {
            return passengerName;
        }
        public LocalDate getDepartureDate() {
            return departureDate;
        }
        public String getFlightDuration() {
            return flightDuration;
        }
        public String getAircraftType() {
        return aircraftType;
        }
        public void setId(int id) {
            this.id = id;
        }

}
