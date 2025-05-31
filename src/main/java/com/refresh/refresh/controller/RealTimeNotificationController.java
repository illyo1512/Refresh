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

    /**
     * 새로운 실시간 알림 정보를 생성하는 API
     * @param notificationDTO 알림 DTO
     * @return RealTimeNotificationDTO
     */
    @PostMapping
    public RealTimeNotificationDTO createNotification(@RequestBody RealTimeNotificationDTO notificationDTO) {
        RealTimeNotification notification = new RealTimeNotification();
        notification.setUserId(notificationDTO.getUserId());
        notification.setDangerId(notificationDTO.getDangerId());
        notification.setDetailId(notificationDTO.getDetailId());
        notification.setContent(notificationDTO.getContent());
        notification.setSentAt(notificationDTO.getSentAt());
        RealTimeNotification savedNotification = realTimeNotificationService.createNotification(notification);
        notificationDTO.setNotificationId(savedNotification.getNotificationId());
        return notificationDTO;
    }

    /**
     * 기존 실시간 알림 정보를 업데이트하는 API
     * @param id 알림 ID
     * @param notificationDTO 알림 DTO
     * @return RealTimeNotificationDTO
     */
    @PutMapping("/{id}")
    public RealTimeNotificationDTO updateNotification(@PathVariable int id, @RequestBody RealTimeNotificationDTO notificationDTO) {
        RealTimeNotification notification = new RealTimeNotification();
        notification.setNotificationId(id);
        notification.setUserId(notificationDTO.getUserId());
        notification.setDangerId(notificationDTO.getDangerId());
        notification.setDetailId(notificationDTO.getDetailId());
        notification.setContent(notificationDTO.getContent());
        notification.setSentAt(notificationDTO.getSentAt());
        RealTimeNotification updatedNotification = realTimeNotificationService.updateNotification(notification);
        notificationDTO.setNotificationId(updatedNotification.getNotificationId());
        return notificationDTO;
    }

    /**
     * 특정 ID의 실시간 알림 정보를 삭제하는 API
     * @param id 알림 ID
     * @return 삭제 메시지
     */
    @DeleteMapping("/{id}")
    public String deleteNotification(@PathVariable int id) {
        realTimeNotificationService.deleteNotification(id);
        return "RealTimeNotification with ID " + id + " has been deleted.";
    }
}
