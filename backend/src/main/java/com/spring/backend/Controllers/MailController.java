package com.spring.backend.Controllers;

import com.spring.backend.Services.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/mail")
public class MailController {
    @Autowired
    private MailService service;

    @PostMapping("/send")
    public ResponseEntity sendEmail(@RequestParam("to") String to,
                                    @RequestParam("subject") String subject,
                                    @RequestParam("body") String body) {
        try {
            service.sendEmail(to, subject, body);
            return ResponseEntity.ok("Email sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send email: " + e.getMessage());
        }
    }
}
