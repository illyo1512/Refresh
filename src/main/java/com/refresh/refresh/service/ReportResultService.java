package com.refresh.refresh.service;

import com.refresh.refresh.entity.ReportResult;
import com.refresh.refresh.entity.ReportResultId;
import com.refresh.refresh.repository.ReportResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportResultService {

    @Autowired
    private ReportResultRepository reportResultRepository;

    /**
     * 모든 신고 결과 정보를 가져오는 메서드
     * @return List<ReportResult>
     */
    public List<ReportResult> getAllReportResults() {
        return reportResultRepository.findAll();
    }

    /**
     * 특정 ID의 신고 결과 정보를 가져오는 메서드
     * @param id 다중 키 ID
     * @return ReportResult
     */
    public ReportResult getReportResultById(ReportResultId id) {
        return reportResultRepository.findById(id).orElseThrow(() -> new RuntimeException("ReportResult not found"));
    }

    /**
     * 새로운 신고 결과 정보를 생성하는 메서드
     * @param reportResult 신고 결과 엔티티
     * @return ReportResult
     */
    public ReportResult createReportResult(ReportResult reportResult) {
        return reportResultRepository.save(reportResult);
    }

    /**
     * 기존 신고 결과 정보를 업데이트하는 메서드
     * @param reportResult 신고 결과 엔티티
     * @return ReportResult
     */
    public ReportResult updateReportResult(ReportResult reportResult) {
        ReportResultId id = new ReportResultId();
        id.setResultId(reportResult.getResultId());
        id.setReportId(reportResult.getReportId());
        if (!reportResultRepository.existsById(id)) {
            throw new RuntimeException("ReportResult not found");
        }
        return reportResultRepository.save(reportResult);
    }

    /**
     * 특정 ID의 신고 결과 정보를 삭제하는 메서드
     * @param id 다중 키 ID
     */
    public void deleteReportResult(ReportResultId id) {
        if (!reportResultRepository.existsById(id)) {
            throw new RuntimeException("ReportResult not found");
        }
        reportResultRepository.deleteById(id);
    }
}
