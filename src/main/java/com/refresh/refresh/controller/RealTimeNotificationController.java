package com.refresh.refresh.controller;

import com.refresh.refresh.dto.RealTimeNotificationDTO;
import com.refresh.refresh.entity.RealTimeNotification;
import com.refresh.refresh.service.RealTimeNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/real-time-notifications")
public class RealTimeNotificationController {

    @Autowired
    private RealTimeNotificationService realTimeNotificationService;

    /**
     * 모든 실시간 알림 정보를 가져오는 API
     * @return List<RealTimeNotificationDTO>
     */
    @GetMapping
    public List<RealTimeNotificationDTO> getAllNotifications() {
        return realTimeNotificationService.getAllNotifications().stream()
                .map(notification -> {
                    RealTimeNotificationDTO notificationDTO = new RealTimeNotificationDTO();
                    notificationDTO.setNotificationId(notification.getNotificationId());
                    notificationDTO.setUserId(notification.getUserId());
                    notificationDTO.setDangerId(notification.getDangerId());
                    notificationDTO.setDetailId(notification.getDetailId());
                    notificationDTO.setContent(notification.getContent());
                    notificationDTO.setSentAt(notification.getSentAt());
                    return notificationDTO;
                }).collect(Collectors.toList());
    }

    /**
     * 특정 ID의 실시간 알림 정보를 가져오는 API
     * @param id 알림 ID
     * @return RealTimeNotificationDTO
     */
    @GetMapping("/{id}")
    public RealTimeNotificationDTO getNotificationById(@PathVariable int id) {
        RealTimeNotification notification = realTimeNotificationService.getNotificationById(id);
        RealTimeNotificationDTO notificationDTO = new RealTimeNotificationDTO();
        notificationDTO.setNotificationId(notification.getNotificationId());
        notificationDTO.setUserId(notification.getUserId());
        notificationDTO.setDangerId(notification.getDangerId());
        notificationDTO.setDetailId(notification.getDetailId());
        notificationDTO.setContent(notification.getContent());
        notificationDTO.setSentAt(notification.getSentAt());
        return notificationDTO;
    }

}