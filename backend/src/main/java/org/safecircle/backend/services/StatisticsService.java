package org.safecircle.backend.services;

import org.safecircle.backend.dto.StatisticsDTO;
import org.safecircle.backend.models.Alert;
import org.safecircle.backend.models.Report;
import org.safecircle.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticsService {
    private AlertRepository alertRepository;
    private UserRepository userRepository;
    private CircleRepository circleRepository;
    private EventRepository eventRepository;
    private ReportRepository reportRepository;

    @Autowired
    public StatisticsService(AlertRepository alertRepository, UserRepository userRepository, CircleRepository circleRepository, EventRepository eventRepository, ReportRepository reportRepository) {
        this.alertRepository = alertRepository;
        this.userRepository = userRepository;
        this.circleRepository = circleRepository;
        this.eventRepository = eventRepository;
        this.reportRepository = reportRepository;
    }

    public ResponseEntity<StatisticsDTO> getAmountByClassName(String className) {
        StatisticsDTO amountByClassName = new StatisticsDTO();
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        switch (className){
            case "Alert": List<Alert> recentAlerts = alertRepository.findByCreatedAtAfter(thirtyDaysAgo);
                amountByClassName = new StatisticsDTO("Alert", recentAlerts.size());
                break;
            case "User": amountByClassName = new StatisticsDTO("User", (int) userRepository.count());
                break;
            case "Circle": amountByClassName = new StatisticsDTO("Circle", (int) circleRepository.count());
                break;
            case "Event": amountByClassName = new StatisticsDTO("Event", (int) eventRepository.count());
                break;
            case "Report": List<Report> recentReports = reportRepository.findByCreatedAtAfter(thirtyDaysAgo);
                amountByClassName = new StatisticsDTO("Report", recentReports.size());
                break;
            default: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(amountByClassName);
    }
}
