package com.spring.backend.Controllers;

import com.spring.backend.DTOs.Booking.BookingRequest;
import com.spring.backend.DTOs.Booking.PaymentCallbackResponse;
import com.spring.backend.Exceptions.ActionNotAllowedException;
import com.spring.backend.Exceptions.ResourceNotFoundException;
import com.spring.backend.Models.Booking;
import com.spring.backend.Services.Booking.BookingService;
import com.spring.backend.Services.Booking.PaymentService;
import com.spring.backend.VNPay.VNPayUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @Autowired
    private PaymentService vnPayService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity createBooking(
            @RequestBody BookingRequest request,
            HttpServletRequest httpRequest
    ) {
        try {
            String ipAddress = VNPayUtils.getIpAddress(httpRequest);

            Booking booking = bookingService.createBooking(request);

            String paymentUrl = vnPayService.createPaymentUrl(booking, ipAddress);

            return ResponseEntity.ok().body(paymentUrl);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (ActionNotAllowedException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/callback")
    public ResponseEntity<?> paymentCallback(HttpServletRequest request) {
        try {
            Map<String, String> callbackData = vnPayService.validateCallback(request);

            String vnpayTxnRef = callbackData.get("vnp_TxnRef");
            String transactionNo = callbackData.get("vnp_TransactionNo");
            String responseCode = callbackData.get("vnp_ResponseCode");
            String payDate = callbackData.get("vnp_PayDate");
            boolean isValidSignature = Boolean.parseBoolean(callbackData.get("isValidSignature"));

            if (!isValidSignature) {
                PaymentCallbackResponse response = new PaymentCallbackResponse();
                response.setMessage("Invalid signature");
                response.setStatus(-1);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            boolean isSuccess = "00".equals(responseCode);
            bookingService.updateBookingAfterPayment(vnpayTxnRef, isSuccess);

            PaymentCallbackResponse response = new PaymentCallbackResponse();

            if (isSuccess) {
                response.setStatus(1);
                response.setMessage("Payment successful for booking " + vnpayTxnRef);
                return ResponseEntity.ok(response);
            } else {
                response.setStatus(0);
                response.setMessage("Payment failed for booking " + vnpayTxnRef);
                return ResponseEntity.ok(response);
            }

        } catch (Exception e) {
            PaymentCallbackResponse errorResponse = new PaymentCallbackResponse();
            errorResponse.setMessage("Error processing payment callback: " + e.getMessage());
            errorResponse.setStatus(-1);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
