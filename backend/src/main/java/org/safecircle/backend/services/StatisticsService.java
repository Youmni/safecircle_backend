package org.safecircle.backend.services;

import org.safecircle.backend.dto.StatisticsDTO;
import org.safecircle.backend.models.Alert;
import org.safecircle.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
        switch (className){
            case "Alert": amountByClassName = new StatisticsDTO("Alert", (int) alertRepository.count());
            break;
            case "User": amountByClassName = new StatisticsDTO("User", (int) userRepository.count());
            break;
            case "Circle": amountByClassName = new StatisticsDTO("Circle", (int) circleRepository.count());
            break;
            case "Event": amountByClassName = new StatisticsDTO("Event", (int) eventRepository.count());
            break;
            case "Report": amountByClassName = new StatisticsDTO("Report", (int) reportRepository.count());
            break;
            default: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(amountByClassName);
    }
}
