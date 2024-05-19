package com.example.kursova;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.time.LocalDate;
@RestController
@RequestMapping("/tickets")
public class TicketController {

    @GetMapping("/")
    public ResponseEntity<String> welcome() {
        return ResponseEntity.ok("Welcome to the ticketing system!");
    }
    @GetMapping
    public ResponseEntity<List<Ticket>> getTickets() {
        List<Ticket> tickets = DatabaseManager.getTickets();
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> addTicket(@RequestBody Ticket ticket) {
        DatabaseManager.addTicket(ticket);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        DatabaseManager.deleteTicket(Math.toIntExact(id));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/search")
    public ResponseEntity<List<Ticket>> searchTickets(@RequestParam(name = "flightNumber") String flightNumber, @RequestParam(name = "departureDate") String departureDateStr) {
        LocalDate departureDate = LocalDate.parse(departureDateStr);
        List<Ticket> tickets = DatabaseManager.searchTicketsByFlightNumberAndDepartureDate(flightNumber, departureDate);
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }
}
