package com.vehicleservice.controller;

import com.vehicleservice.exception.BookingNotFoundException;
import com.vehicleservice.exception.InvalidBookingException;
import com.vehicleservice.model.ServiceDetail;
import com.vehicleservice.model.ServiceRecord;
import com.vehicleservice.service.ServiceRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/service-records")
public class ServiceRecordController {

    private final ServiceRecordService serviceRecordService;

    public ServiceRecordController(ServiceRecordService serviceRecordService) {
        this.serviceRecordService = serviceRecordService;
    }

    @GetMapping
    public ResponseEntity<List<ServiceRecord>> getAllRecords() {
        return ResponseEntity.ok(serviceRecordService.getAllRecords());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceRecord> getRecordById(@PathVariable("id") int id) {
        return ResponseEntity.ok(serviceRecordService.getRecordById(id));
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<ServiceRecord> getRecordByBookingId(@PathVariable("bookingId") int bookingId) {
        return ResponseEntity.ok(serviceRecordService.getRecordByBookingId(bookingId));
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<ServiceRecord>> getHistoryByVehicleId(@PathVariable("vehicleId") int vehicleId) {
        return ResponseEntity.ok(serviceRecordService.getHistoryByVehicleId(vehicleId));
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<List<ServiceDetail>> getDetailsForRecord(@PathVariable("id") int id) {
        return ResponseEntity.ok(serviceRecordService.getDetailsForRecord(id));
    }

    @PostMapping
    public ResponseEntity<ServiceRecord> createRecord(
            @RequestBody ServiceRecord record,
            @RequestParam(value = "extraPartsCost", required = false) BigDecimal extraPartsCost) {
        ServiceRecord created = serviceRecordService.createRecord(
                record.getBookingId(),
                record.getCompletionDate(),
                record.getRemarks(),
                extraPartsCost
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleBookingNotFound(BookingNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(InvalidBookingException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidBooking(InvalidBookingException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        if (ex.getMessage() != null && ex.getMessage().contains("not found")) {
            return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        }
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String message) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("success", false);
        error.put("status", status.value());
        error.put("message", message);
        return ResponseEntity.status(status).body(error);
    }
}
