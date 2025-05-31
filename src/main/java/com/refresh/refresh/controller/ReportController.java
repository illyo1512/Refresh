package com.refresh.refresh.controller;

import com.refresh.refresh.dto.ReportDTO;
import com.refresh.refresh.entity.Report;
import com.refresh.refresh.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 모든 신고 정보를 가져오는 API
     * @return List<ReportDTO>
     */
    @GetMapping
    public List<ReportDTO> getAllReports() {
        return reportService.getAllReports().stream()
                .map(report -> {
                    ReportDTO reportDTO = new ReportDTO();
                    reportDTO.setReportId(report.getReportId());
                    reportDTO.setUserId(report.getUserId());
                    reportDTO.setTargetId(report.getTargetId());
                    reportDTO.setReportContent(report.getReportContent());
                    reportDTO.setReportType(report.getReportType());
                    reportDTO.setReportDate(report.getReportDate());
                    return reportDTO;
                }).collect(Collectors.toList());
    }

    /**
     * 특정 ID의 신고 정보를 가져오는 API
     * @param id 신고 ID
     * @return ReportDTO
     */
    @GetMapping("/{id}")
    public ReportDTO getReportById(@PathVariable int id) {
        Report report = reportService.getReportById(id);
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setReportId(report.getReportId());
        reportDTO.setUserId(report.getUserId());
        reportDTO.setTargetId(report.getTargetId());
        reportDTO.setReportContent(report.getReportContent());
        reportDTO.setReportType(report.getReportType());
        reportDTO.setReportDate(report.getReportDate());
        return reportDTO;
    }

    /**
     * 새로운 신고 정보를 생성하는 API
     * @param reportDTO 신고 DTO
     * @return ReportDTO
     */
    @PostMapping
    public ReportDTO createReport(@RequestBody ReportDTO reportDTO) {
        Report report = new Report();
        report.setUserId(reportDTO.getUserId());
        report.setTargetId(reportDTO.getTargetId());
        report.setReportContent(reportDTO.getReportContent());
        report.setReportType(reportDTO.getReportType());
        report.setReportDate(reportDTO.getReportDate());
        Report savedReport = reportService.createReport(report);
        reportDTO.setReportId(savedReport.getReportId());
        return reportDTO;
    }

    /**
     * 기존 신고 정보를 업데이트하는 API
     * @param id 신고 ID
     * @param reportDTO 신고 DTO
     * @return ReportDTO
     */
    @PutMapping("/{id}")
    public ReportDTO updateReport(@PathVariable int id, @RequestBody ReportDTO reportDTO) {
        Report report = new Report();
        report.setReportId(id);
        report.setUserId(reportDTO.getUserId());
        report.setTargetId(reportDTO.getTargetId());
        report.setReportContent(reportDTO.getReportContent());
        report.setReportType(reportDTO.getReportType());
        report.setReportDate(reportDTO.getReportDate());
        Report updatedReport = reportService.updateReport(report);
        reportDTO.setReportId(updatedReport.getReportId());
        return reportDTO;
    }

    /**
     * 특정 ID의 신고 정보를 삭제하는 API
     * @param id 신고 ID
     * @return 삭제 메시지
     */
    @DeleteMapping("/{id}")
    public String deleteReport(@PathVariable int id) {
        reportService.deleteReport(id);
        return "Report with ID " + id + " has been deleted.";
    }
}
