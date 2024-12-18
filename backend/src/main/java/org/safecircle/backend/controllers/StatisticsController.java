package org.safecircle.backend.controllers;

import org.safecircle.backend.dto.StatisticsDTO;
import org.safecircle.backend.services.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/statistics")
public class StatisticsController {
    private final StatisticsService statisticsService;

    @Autowired
    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @CrossOrigin
    @GetMapping(value = "/{className}")
    public ResponseEntity<StatisticsDTO> getAmountByClassName(@PathVariable String className) {
        return statisticsService.getAmountByClassName(className);
    }
}
