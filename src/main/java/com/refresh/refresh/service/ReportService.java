package com.refresh.refresh.service;

import com.refresh.refresh.entity.Report;
import com.refresh.refresh.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    /**
     * 모든 신고 정보를 가져오는 메서드
     * @return List<Report>
     */
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    /**
     * 특정 ID의 신고 정보를 가져오는 메서드
     * @param id 신고 ID
     * @return Report
     */
    public Report getReportById(int id) {
        return reportRepository.findById(id).orElseThrow(() -> new RuntimeException("Report not found"));
    }

    /**
     * 새로운 신고 정보를 생성하는 메서드
     * @param report 신고 엔티티
     * @return Report
     */
    public Report createReport(Report report) {
        return reportRepository.save(report);
    }

    /**
     * 기존 신고 정보를 업데이트하는 메서드
     * @param report 신고 엔티티
     * @return Report
     */
    public Report updateReport(Report report) {
        if (!reportRepository.existsById(report.getReportId())) {
            throw new RuntimeException("Report not found");
        }
        return reportRepository.save(report);
    }

    /**
     * 특정 ID의 신고 정보를 삭제하는 메서드
     * @param id 신고 ID
     */
    public void deleteReport(int id) {
        if (!reportRepository.existsById(id)) {
            throw new RuntimeException("Report not found");
        }
        reportRepository.deleteById(id);
    }
}
