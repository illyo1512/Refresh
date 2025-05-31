package com.refresh.refresh.controller;

import com.refresh.refresh.dto.LocateReviewDTO;
import com.refresh.refresh.entity.LocateReview;
import com.refresh.refresh.service.LocateReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/locate-reviews")
public class LocateReviewController {

    @Autowired
    private LocateReviewService locateReviewService;

    /**
     * 모든 장소 리뷰를 가져오는 API
     * @return List<LocateReviewDTO>
     */
    @GetMapping
    public List<LocateReviewDTO> getAllLocateReviews() {
        return locateReviewService.getAllLocateReviews().stream()
                .map(locateReview -> {
                    LocateReviewDTO locateReviewDTO = new LocateReviewDTO();
                    locateReviewDTO.setReviewId(locateReview.getReviewId());
                    locateReviewDTO.setUserId(locateReview.getUserId());
                    locateReviewDTO.setLocateInfoId(locateReview.getLocateInfoId());
                    locateReviewDTO.setReviewDetail(locateReview.getReviewDetail());
                    locateReviewDTO.setCreatedAt(locateReview.getCreatedAt());
                    return locateReviewDTO;
                }).collect(Collectors.toList());
    }

    /**
     * 특정 ID의 장소 리뷰를 가져오는 API
     * @param id 리뷰 ID
     * @return LocateReviewDTO
     */
    @GetMapping("/{id}")
    public LocateReviewDTO getLocateReviewById(@PathVariable int id) {
        LocateReview locateReview = locateReviewService.getLocateReviewById(id);
        LocateReviewDTO locateReviewDTO = new LocateReviewDTO();
        locateReviewDTO.setReviewId(locateReview.getReviewId());
        locateReviewDTO.setUserId(locateReview.getUserId());
        locateReviewDTO.setLocateInfoId(locateReview.getLocateInfoId());
        locateReviewDTO.setReviewDetail(locateReview.getReviewDetail());
        locateReviewDTO.setCreatedAt(locateReview.getCreatedAt());
        return locateReviewDTO;
    }

    /**
     * 새로운 장소 리뷰를 생성하는 API
     * @param locateReviewDTO 장소 리뷰 DTO
     * @return LocateReviewDTO
     */
    @PostMapping
    public LocateReviewDTO createLocateReview(@RequestBody LocateReviewDTO locateReviewDTO) {
        LocateReview locateReview = new LocateReview();
        locateReview.setUserId(locateReviewDTO.getUserId());
        locateReview.setLocateInfoId(locateReviewDTO.getLocateInfoId());
        locateReview.setReviewDetail(locateReviewDTO.getReviewDetail());
        locateReview.setCreatedAt(locateReviewDTO.getCreatedAt());
        LocateReview savedLocateReview = locateReviewService.createLocateReview(locateReview);
        locateReviewDTO.setReviewId(savedLocateReview.getReviewId());
        return locateReviewDTO;
    }

    /**
     * 기존 장소 리뷰를 업데이트하는 API
     * @param id 리뷰 ID
     * @param locateReviewDTO 장소 리뷰 DTO
     * @return LocateReviewDTO
     */
    @PutMapping("/{id}")
    public LocateReviewDTO updateLocateReview(@PathVariable int id, @RequestBody LocateReviewDTO locateReviewDTO) {
        LocateReview locateReview = new LocateReview();
        locateReview.setReviewId(id);
        locateReview.setUserId(locateReviewDTO.getUserId());
        locateReview.setLocateInfoId(locateReviewDTO.getLocateInfoId());
        locateReview.setReviewDetail(locateReviewDTO.getReviewDetail());
        locateReview.setCreatedAt(locateReviewDTO.getCreatedAt());
        LocateReview updatedLocateReview = locateReviewService.updateLocateReview(locateReview);
        locateReviewDTO.setReviewId(updatedLocateReview.getReviewId());
        return locateReviewDTO;
    }

    /**
     * 특정 ID의 장소 리뷰를 삭제하는 API
     * @param id 리뷰 ID
     * @return 삭제 메시지
     */
    @DeleteMapping("/{id}")
    public String deleteLocateReview(@PathVariable int id) {
        locateReviewService.deleteLocateReview(id);
        return "LocateReview with ID " + id + " has been deleted.";
    }
}
