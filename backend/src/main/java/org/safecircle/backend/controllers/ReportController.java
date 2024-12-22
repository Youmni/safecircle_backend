package org.safecircle.backend.controllers;

import jakarta.validation.Valid;
import org.safecircle.backend.dto.ReportDTO;
import org.safecircle.backend.enums.ReportStatus;
import org.safecircle.backend.models.Report;
import org.safecircle.backend.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/report")
public class ReportController {
    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @CrossOrigin
    @GetMapping(value = "/{reportId}")
    public ResponseEntity<ReportDTO> showReport(@PathVariable long reportId) {
        return reportService.showReport(reportId);
    }

    @CrossOrigin
    @GetMapping(value = "/{userId}/showAll")
    public ResponseEntity<List<ReportDTO>> showAllReportsFromUser(@PathVariable long userId) {
        return reportService.showAllReportsFromUser(userId);
    }

    @CrossOrigin
    @GetMapping(value = "/showAll")
    public ResponseEntity<List<ReportDTO>> showAllReports() {
        return reportService.showAllReports();
    }

    @CrossOrigin
    @GetMapping(value = "/showPending")
    public ResponseEntity<List<ReportDTO>> showAllPendingReports() {
        return reportService.showPendingReports();
    }

    @CrossOrigin
    @GetMapping(value = "/showClosed")
    public ResponseEntity<List<ReportDTO>> showAllClosedReports() {
        return reportService.showClosedReports();
    }

    @CrossOrigin
    @GetMapping(value = "/showFlagged")
    public ResponseEntity<List<ReportDTO>> showAllFlaggedReports() {
        return reportService.showFlaggedReports();
    }

    @CrossOrigin
    @PostMapping(value = "/{userId}/create")
    public ResponseEntity<String> createReport(@PathVariable long userId, @Valid @RequestBody ReportDTO report) {
        return reportService.createReport(userId, report);
    }

    @CrossOrigin
    @PutMapping(value = "/{reportId}/changeStatus")
    public ResponseEntity<String> changeStatus(@PathVariable long reportId, @RequestParam ReportStatus status) {
        return reportService.changeStatus(reportId, status);
    }
}
