package com.refresh.refresh.controller;

import com.refresh.refresh.dto.ReportResultDTO;
import com.refresh.refresh.entity.ReportResult;
import com.refresh.refresh.entity.ReportResultId;
import com.refresh.refresh.service.ReportResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/report-results")
public class ReportResultController {

    @Autowired
    private ReportResultService reportResultService;

    /**
     * 모든 신고 결과 정보를 가져오는 API
     * @return List<ReportResultDTO>
     */
    @GetMapping
    public List<ReportResultDTO> getAllReportResults() {
        return reportResultService.getAllReportResults().stream()
                .map(reportResult -> {
                    ReportResultDTO reportResultDTO = new ReportResultDTO();
                    reportResultDTO.setResultId(reportResult.getResultId());
                    reportResultDTO.setReportId(reportResult.getReportId());
                    reportResultDTO.setResultContent(reportResult.getResultContent());
                    reportResultDTO.setBanPeriod(reportResult.getBanPeriod());
                    reportResultDTO.setResultDate(reportResult.getResultDate());
                    return reportResultDTO;
                }).collect(Collectors.toList());
    }

    /**
     * 특정 ID의 신고 결과 정보를 가져오는 API
     * @param resultId 결과 ID
     * @param reportId 신고 ID
     * @return ReportResultDTO
     */
    @GetMapping("/{resultId}/{reportId}")
    public ReportResultDTO getReportResultById(@PathVariable Integer resultId, @PathVariable Integer reportId) {
        ReportResultId id = new ReportResultId();
        id.setResultId(resultId);
        id.setReportId(reportId);
        ReportResult reportResult = reportResultService.getReportResultById(id);
        ReportResultDTO reportResultDTO = new ReportResultDTO();
        reportResultDTO.setResultId(reportResult.getResultId());
        reportResultDTO.setReportId(reportResult.getReportId());
        reportResultDTO.setResultContent(reportResult.getResultContent());
        reportResultDTO.setBanPeriod(reportResult.getBanPeriod());
        reportResultDTO.setResultDate(reportResult.getResultDate());
        return reportResultDTO;
    }

    /**
     * 새로운 신고 결과 정보를 생성하는 API
     * @param reportResultDTO 신고 결과 DTO
     * @return ReportResultDTO
     */
    @PostMapping
    public ReportResultDTO createReportResult(@RequestBody ReportResultDTO reportResultDTO) {
        ReportResult reportResult = new ReportResult();
        reportResult.setResultId(reportResultDTO.getResultId());
        reportResult.setReportId(reportResultDTO.getReportId());
        reportResult.setResultContent(reportResultDTO.getResultContent());
        reportResult.setBanPeriod(reportResultDTO.getBanPeriod());
        reportResult.setResultDate(reportResultDTO.getResultDate());
        ReportResult savedReportResult = reportResultService.createReportResult(reportResult);
        reportResultDTO.setResultId(savedReportResult.getResultId());
        reportResultDTO.setReportId(savedReportResult.getReportId());
        return reportResultDTO;
    }

    /**
     * 기존 신고 결과 정보를 업데이트하는 API
     * @param resultId 결과 ID
     * @param reportId 신고 ID
     * @param reportResultDTO 신고 결과 DTO
     * @return ReportResultDTO
     */
    @PutMapping("/{resultId}/{reportId}")
    public ReportResultDTO updateReportResult(@PathVariable Integer resultId, @PathVariable Integer reportId, @RequestBody ReportResultDTO reportResultDTO) {
        ReportResult reportResult = new ReportResult();
        reportResult.setResultId(resultId);
        reportResult.setReportId(reportId);
        reportResult.setResultContent(reportResultDTO.getResultContent());
        reportResult.setBanPeriod(reportResultDTO.getBanPeriod());
        reportResult.setResultDate(reportResultDTO.getResultDate());
        ReportResult updatedReportResult = reportResultService.updateReportResult(reportResult);
        reportResultDTO.setResultId(updatedReportResult.getResultId());
        reportResultDTO.setReportId(updatedReportResult.getReportId());
        return reportResultDTO;
    }

    /**
     * 특정 ID의 신고 결과 정보를 삭제하는 API
     * @param resultId 결과 ID
     * @param reportId 신고 ID
     * @return 삭제 메시지
     */
    @DeleteMapping("/{resultId}/{reportId}")
    public String deleteReportResult(@PathVariable Integer resultId, @PathVariable Integer reportId) {
        ReportResultId id = new ReportResultId();
        id.setResultId(resultId);
        id.setReportId(reportId);
        reportResultService.deleteReportResult(id);
        return "ReportResult with ID (" + resultId + ", " + reportId + ") has been deleted.";
    }
}
