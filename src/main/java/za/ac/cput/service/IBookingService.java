package za.ac.cput.service;

import org.springframework.transaction.annotation.Transactional;
import za.ac.cput.domain.Booking;

import java.time.LocalDate;
import java.util.List;

public interface IBookingService extends IService<Booking, String>{


    @Transactional

    Booking create(Booking booking);

    List<Booking> getAll(); // Get all bookings

    boolean delete(String bookingID);

    List<Booking> findOverlappingBookings(String licensePlate, LocalDate startDate, LocalDate endDate);
}
//