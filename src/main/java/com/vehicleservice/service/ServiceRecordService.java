package com.vehicleservice.service;

import com.vehicleservice.exception.BookingNotFoundException;
import com.vehicleservice.exception.InvalidBookingException;
import com.vehicleservice.model.*;
import com.vehicleservice.repository.ServiceBookingRepository;
import com.vehicleservice.repository.ServiceDetailRepository;
import com.vehicleservice.repository.ServiceRecordRepository;
import com.vehicleservice.repository.ServiceRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceRecordService {
    private final ServiceRecordRepository recordRepository;
    private final ServiceBookingRepository bookingRepository;
    private final ServiceRepository serviceRepository;
    private final ServiceDetailRepository detailRepository;

    public ServiceRecordService(ServiceRecordRepository recordRepository,
                                ServiceBookingRepository bookingRepository,
                                ServiceRepository serviceRepository,
                                ServiceDetailRepository detailRepository) {
        this.recordRepository = recordRepository;
        this.bookingRepository = bookingRepository;
        this.serviceRepository = serviceRepository;
        this.detailRepository = detailRepository;
    }

    public ServiceRecord createRecord(int bookingId, LocalDate completionDate, String remarks, BigDecimal extraPartsCost) {
        ServiceBooking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with ID: " + bookingId));

        if (booking.getStatus() != BookingStatus.COMPLETED) {
            throw new InvalidBookingException("Cannot create service record. Booking must be in COMPLETED status. Current: " + booking.getStatus());
        }

        if (recordRepository.findByBookingId(bookingId).isPresent()) {
            throw new InvalidBookingException("Service record already exists for Booking ID: " + bookingId);
        }

        // Fetch catalog service base price
        Service catalogService = serviceRepository.findById(booking.getServiceId())
                .orElseThrow(() -> new IllegalArgumentException("Catalog service not found with ID: " + booking.getServiceId()));

        BigDecimal basePrice = catalogService.getBasePrice();
        BigDecimal extra = extraPartsCost != null ? extraPartsCost : BigDecimal.ZERO;
        BigDecimal totalCost = basePrice.add(extra);

        LocalDate compDate = completionDate != null ? completionDate : LocalDate.now();
        ServiceRecord record = new ServiceRecord(bookingId, compDate, remarks, totalCost);
        ServiceRecord savedRecord = recordRepository.save(record);

        // Save composition ServiceDetail line item
        ServiceDetail detail = new ServiceDetail(savedRecord.getRecordId(), catalogService.getServiceId(), 1, basePrice);
        detailRepository.save(detail);

        return savedRecord;
    }

    public List<ServiceRecord> getAllRecords() {
        return recordRepository.findAll();
    }

    public ServiceRecord getRecordById(int recordId) {
        return recordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("Service record not found with ID: " + recordId));
    }

    public ServiceRecord getRecordByBookingId(int bookingId) {
        return recordRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Service record not found for Booking ID: " + bookingId));
    }

    public List<ServiceRecord> getHistoryByVehicleId(int vehicleId) {
        List<ServiceBooking> vehicleBookings = bookingRepository.findByVehicleId(vehicleId);
        List<Integer> bookingIds = vehicleBookings.stream().map(ServiceBooking::getBookingId).collect(Collectors.toList());

        return recordRepository.findAll().stream()
                .filter(r -> bookingIds.contains(r.getBookingId()))
                .collect(Collectors.toList());
    }

    public List<ServiceDetail> getDetailsForRecord(int recordId) {
        return detailRepository.findByRecordId(recordId);
    }
}
