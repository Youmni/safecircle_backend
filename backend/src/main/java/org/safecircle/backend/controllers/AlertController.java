package org.safecircle.backend.controllers;

import org.safecircle.backend.dto.AlertDTO;
import org.safecircle.backend.dto.RequestAlertDTO;
import org.safecircle.backend.services.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alert")
public class AlertController {
    private AlertService alertService;

    @Autowired
    public void AlertService(AlertService alertService) {
        this.alertService = alertService;
    }

    @CrossOrigin
    @PostMapping("/{id}/send")
    public ResponseEntity<String> SendAlert(@PathVariable long id, @RequestBody AlertDTO alert) {
        return alertService.sendAlert(id, alert);
    }

    @CrossOrigin
    @PostMapping("/{id}/stop")
    public ResponseEntity<String> StopAlert(@PathVariable long id) {
        return alertService.stopAlert(id);
    }

    @CrossOrigin
    @GetMapping("/latest")
    public List<RequestAlertDTO> getLatestAlert() {
        return alertService.getLatestAlert();
    }

}
