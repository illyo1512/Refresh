// package com.refresh.refresh.service;

// import com.refresh.refresh.entity.DestinationSelf;
// import com.refresh.refresh.entity.DestinationSelfId;
// import com.refresh.refresh.repository.DestinationSelfRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import java.util.List;

// @Service
// public class DestinationSelfService {

//     @Autowired
//     private DestinationSelfRepository destinationSelfRepository;

//     /**
//      * 모든 목적지 정보를 가져오는 메서드
//      * @return List<DestinationSelf>
//      */
//     public List<DestinationSelf> getAllDestinations() {
//         return destinationSelfRepository.findAll();
//     }

//     /**
//      * 특정 ID의 목적지 정보를 가져오는 메서드
//      * @param id 다중 키 ID
//      * @return DestinationSelf
//      */
//     public DestinationSelf getDestinationById(DestinationSelfId id) {
//         return destinationSelfRepository.findById(id).orElseThrow(() -> new RuntimeException("Destination not found"));
//     }

//     /**
//      * 새로운 목적지 정보를 생성하는 메서드
//      * @param destination 목적지 엔티티
//      * @return DestinationSelf
//      */
//     public DestinationSelf createDestination(DestinationSelf destination) {
//         return destinationSelfRepository.save(destination);
//     }

//     /**
//      * 기존 목적지 정보를 업데이트하는 메서드
//      * @param destination 목적지 엔티티
//      * @return DestinationSelf
//      */
//     public DestinationSelf updateDestination(DestinationSelf destination) {
//         if (!destinationSelfRepository.existsById(new DestinationSelfId(destination.getDestinationId(), destination.getSelfRouteId()))) {
//             throw new RuntimeException("Destination not found");
//         }
//         return destinationSelfRepository.save(destination);
//     }

//     /**
//      * 특정 ID의 목적지 정보를 삭제하는 메서드
//      * @param id 다중 키 ID
//      */
//     public void deleteDestination(DestinationSelfId id) {
//         if (!destinationSelfRepository.existsById(id)) {
//             throw new RuntimeException("Destination not found");
//         }
//         destinationSelfRepository.deleteById(id);
//     }
// }
