package org.safecircle.backend.controllers;

import org.safecircle.backend.dto.StatisticsDTO;
import org.safecircle.backend.repositories.AlertRepository;
import org.safecircle.backend.repositories.EventRepository;
import org.safecircle.backend.repositories.ReportRepository;
import org.safecircle.backend.repositories.UserRepository;
import org.safecircle.backend.services.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/statistics")
public class StatisticsController {
    private final StatisticsService statisticsService;
    private final UserRepository userRepository;
    private final AlertRepository alertRepository;
    private final ReportRepository reportRepository;
    private final EventRepository eventRepository;

    @Autowired
    public StatisticsController(StatisticsService statisticsService, UserRepository userRepository, AlertRepository alertRepository, ReportRepository reportRepository, EventRepository eventRepository) {
        this.statisticsService = statisticsService;
        this.userRepository = userRepository;
        this.alertRepository = alertRepository;
        this.reportRepository = reportRepository;
        this.eventRepository = eventRepository;
    }

    @CrossOrigin
    @GetMapping(value = "/{className}")
    public ResponseEntity<StatisticsDTO> getAmountByClassName(@PathVariable String className) {
        return statisticsService.getAmountByClassName(className);
    }

    @CrossOrigin
    @GetMapping("/numbers")
    public List<StatisticsDTO> getAmountOfStatisticsByKlasse() {
        List<StatisticsDTO> klasseAantallen = new ArrayList<>();

        klasseAantallen.add(new StatisticsDTO("Users", (int) userRepository.count()));
        klasseAantallen.add(new StatisticsDTO("Alerts", (int) alertRepository.count()));
        klasseAantallen.add(new StatisticsDTO("Reports", (int) reportRepository.count()));
        klasseAantallen.add(new StatisticsDTO("Events", (int) eventRepository.count()));

        return klasseAantallen;
    }
}
