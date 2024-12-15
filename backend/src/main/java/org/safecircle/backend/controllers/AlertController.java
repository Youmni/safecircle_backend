package org.safecircle.backend.controllers;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.http.HttpServletRequest;
import org.safecircle.backend.dto.AlertDTO;
import org.safecircle.backend.models.Alert;
import org.safecircle.backend.services.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alert")
public class AlertController {
    private AlertService alertService;

    @Autowired
    public void AlertService(AlertService alertService) {
        this.alertService = alertService;
    }

    @CrossOrigin
    @PostMapping("/send")
    public ResponseEntity<String> SendAlert(@RequestBody AlertDTO alert) {
        return alertService.sendAlert(alert);
    }


}
