package com.refresh.refresh.service;

import com.refresh.refresh.entity.LocateReview;
import com.refresh.refresh.repository.LocateReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocateReviewService {

    @Autowired
    private LocateReviewRepository locateReviewRepository;

    /**
     * 모든 장소 리뷰를 가져오는 메서드
     * @return List<LocateReview>
     */
    public List<LocateReview> getAllLocateReviews() {
        return locateReviewRepository.findAll();
    }

    /**
     * 특정 ID의 장소 리뷰를 가져오는 메서드
     * @param id 리뷰 ID
     * @return LocateReview
     */
    public LocateReview getLocateReviewById(int id) {
        return locateReviewRepository.findById(id).orElseThrow(() -> new RuntimeException("LocateReview not found"));
    }

    /**
     * 새로운 장소 리뷰를 생성하는 메서드
     * @param locateReview 장소 리뷰 엔티티
     * @return LocateReview
     */
    public LocateReview createLocateReview(LocateReview locateReview) {
        return locateReviewRepository.save(locateReview);
    }

    /**
     * 기존 장소 리뷰를 업데이트하는 메서드
     * @param locateReview 장소 리뷰 엔티티
     * @return LocateReview
     */
    public LocateReview updateLocateReview(LocateReview locateReview) {
        if (!locateReviewRepository.existsById(locateReview.getReviewId())) {
            throw new RuntimeException("LocateReview not found");
        }
        return locateReviewRepository.save(locateReview);
    }

    /**
     * 특정 ID의 장소 리뷰를 삭제하는 메서드
     * @param id 리뷰 ID
     */
    public void deleteLocateReview(int id) {
        if (!locateReviewRepository.existsById(id)) {
            throw new RuntimeException("LocateReview not found");
        }
        locateReviewRepository.deleteById(id);
    }
}
