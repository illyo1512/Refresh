package com.refresh.refresh.service;

import com.refresh.refresh.entity.RealTimeNotification;
import com.refresh.refresh.repository.RealTimeNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RealTimeNotificationService {

    @Autowired
    private RealTimeNotificationRepository realTimeNotificationRepository;

    /**
     * 모든 실시간 알림 정보를 가져오는 메서드
     * @return List<RealTimeNotification>
     */
    public List<RealTimeNotification> getAllNotifications() {
        return realTimeNotificationRepository.findAll();
    }

    /**
     * 특정 ID의 실시간 알림 정보를 가져오는 메서드
     * @param id 알림 ID
     * @return RealTimeNotification
     */
    public RealTimeNotification getNotificationById(int id) {
        return realTimeNotificationRepository.findById(id).orElseThrow(() -> new RuntimeException("Notification not found"));
    }

    /**
     * 새로운 실시간 알림 정보를 생성하는 메서드
     * @param notification 알림 엔티티
     * @return RealTimeNotification
     */
    public RealTimeNotification createNotification(RealTimeNotification notification) {
        return realTimeNotificationRepository.save(notification);
    }

    /**
     * 기존 실시간 알림 정보를 업데이트하는 메서드
     * @param notification 알림 엔티티
     * @return RealTimeNotification
     */
    public RealTimeNotification updateNotification(RealTimeNotification notification) {
        if (!realTimeNotificationRepository.existsById(notification.getNotificationId())) {
            throw new RuntimeException("Notification not found");
        }
        return realTimeNotificationRepository.save(notification);
    }

    /**
     * 특정 ID의 실시간 알림 정보를 삭제하는 메서드
     * @param id 알림 ID
     */
    public void deleteNotification(int id) {
        if (!realTimeNotificationRepository.existsById(id)) {
            throw new RuntimeException("Notification not found");
        }
        realTimeNotificationRepository.deleteById(id);
    }
}
