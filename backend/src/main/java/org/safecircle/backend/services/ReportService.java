package org.safecircle.backend.services;

import org.safecircle.backend.dto.ReportDTO;
import org.safecircle.backend.enums.ReportStatus;
import org.safecircle.backend.models.Report;
import org.safecircle.backend.models.User;
import org.safecircle.backend.repositories.ReportRepository;
import org.safecircle.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    @Autowired
    public ReportService(ReportRepository reportRepository, UserRepository userRepository) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<ReportDTO> showReport(long reportId) {
        if(!reportRepository.existsByReportId(reportId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Report report = reportRepository.findByReportId(reportId).getFirst();
        ReportDTO reportDTO = new ReportDTO(
                report.getSubject(),
                report.getDescription(),
                report.getEmail(),
                report.getStatus()
        );
        reportDTO.setUserId(report.getUser().getUserId());
        return ResponseEntity.ok(reportDTO);
    }

    public ResponseEntity<List<ReportDTO>> showAllReports() {
        List<Report> reports = reportRepository.findAll();
        return getReportDTOs(reports);
    }

    public ResponseEntity<List<ReportDTO>> showAllReportsFromUser(long userId) {
        if(!userRepository.existsByUserId(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        User user = userRepository.findByUserId(userId).getFirst();
        List<Report> reports = reportRepository.findByUser(user);
        return getReportDTOs(reports);
    }

    public ResponseEntity<List<ReportDTO>> showPendingReports() {
        List<Report> reports = reportRepository.findByStatus(ReportStatus.PENDING);
        return getReportDTOs(reports);
    }

    private ResponseEntity<List<ReportDTO>> getReportDTOs(List<Report> reports) {
        List<ReportDTO> reportDTOs = new ArrayList<>();
        for(Report report : reports) {
            ReportDTO reportDTO = new ReportDTO(
                    report.getSubject(),
                    report.getDescription(),
                    report.getEmail(),
                    report.getStatus()
            );
            reportDTO.setUserId(report.getUser().getUserId());
            reportDTOs.add(reportDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(reportDTOs);
    }

    public ResponseEntity<List<ReportDTO>> showClosedReports() {
        List<Report> reports = reportRepository.findByStatus(ReportStatus.CLOSED);
        return getReportDTOs(reports);
    }

    public ResponseEntity<List<ReportDTO>> showFlaggedReports() {
        List<Report> reports = reportRepository.findByStatus(ReportStatus.FLAGGED);
        return getReportDTOs(reports);
    }

    public ResponseEntity<String> createReport(long userId, ReportDTO reportDTO) {
        try{
            if(!userRepository.existsByUserId(userId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            User user = userRepository.findByUserId(userId).getFirst();
            Report report = new Report(
                    reportDTO.getSubject(),
                    reportDTO.getDescription(),
                    reportDTO.getEmail(),
                    reportDTO.getStatus(),
                    user
            );
            reportRepository.save(report);
            return ResponseEntity.status(HttpStatus.CREATED).body("Report created");
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong creating report");
        }
    }

    public ResponseEntity<String> changeStatus(long reportId, ReportStatus reportStatus) {
        try{
            if(!reportRepository.existsByReportId(reportId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Report not found");
            }
            if(reportStatus.equals(ReportStatus.PENDING) || reportStatus.equals(ReportStatus.CLOSED) || reportStatus.equals(ReportStatus.FLAGGED)) {
                Report report = reportRepository.findByReportId(reportId).getFirst();
                report.setStatus(reportStatus);
                reportRepository.save(report);
                return ResponseEntity.status(HttpStatus.OK).body("Report updated");
            }
            else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Report status not correct");
            }
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong changing status");
        }
    }
}
