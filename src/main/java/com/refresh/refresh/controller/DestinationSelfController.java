// package com.refresh.refresh.controller;

// import com.refresh.refresh.dto.DestinationSelfDTO;
// import com.refresh.refresh.entity.DestinationSelf;
// import com.refresh.refresh.entity.DestinationSelfId;
// import com.refresh.refresh.service.DestinationSelfService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;
// import java.util.stream.Collectors;

// @RestController
// @RequestMapping("/api/destination-self")
// public class DestinationSelfController {

//     @Autowired
//     private DestinationSelfService destinationSelfService;

//     /**
//      * 모든 목적지 정보를 가져오는 API
//      * @return List<DestinationSelfDTO>
//      */
//     @GetMapping
//     public List<DestinationSelfDTO> getAllDestinations() {
//         return destinationSelfService.getAllDestinations().stream()
//                 .map(destination -> {
//                     DestinationSelfDTO destinationDTO = new DestinationSelfDTO();
//                     destinationDTO.setDestinationId(destination.getDestinationId());
//                     destinationDTO.setSelfRouteId(destination.getSelfRouteId());
//                     destinationDTO.setInfoId(destination.getInfoId());
//                     destinationDTO.setDestinationLocate(destination.getDestinationLocate());
//                     destinationDTO.setDestinationOrder(destination.getDestinationOrder());
//                     return destinationDTO;
//                 }).collect(Collectors.toList());
//     }

//     /**
//      * 특정 ID의 목적지 정보를 가져오는 API
//      * @param destinationId 목적지 ID
//      * @param selfRouteId 경로 ID
//      * @return DestinationSelfDTO
//      */
//     @GetMapping("/{destinationId}/{selfRouteId}")
//     public DestinationSelfDTO getDestinationById(@PathVariable Integer destinationId, @PathVariable Integer selfRouteId) {
//         DestinationSelfId id = new DestinationSelfId();
//         id.setDestinationId(destinationId);
//         id.setSelfRouteId(selfRouteId);
//         DestinationSelf destination = destinationSelfService.getDestinationById(id);
//         DestinationSelfDTO destinationDTO = new DestinationSelfDTO();
//         destinationDTO.setDestinationId(destination.getDestinationId());
//         destinationDTO.setSelfRouteId(destination.getSelfRouteId());
//         destinationDTO.setInfoId(destination.getInfoId());
//         destinationDTO.setDestinationLocate(destination.getDestinationLocate());
//         destinationDTO.setDestinationOrder(destination.getDestinationOrder());
//         return destinationDTO;
//     }

//     /**
//      * 새로운 목적지 정보를 생성하는 API
//      * @param destinationDTO 목적지 DTO
//      * @return DestinationSelfDTO
//      */
//     @PostMapping
//     public DestinationSelfDTO createDestination(@RequestBody DestinationSelfDTO destinationDTO) {
//         DestinationSelf destination = new DestinationSelf();
//         destination.setDestinationId(destinationDTO.getDestinationId());
//         destination.setSelfRouteId(destinationDTO.getSelfRouteId());
//         destination.setInfoId(destinationDTO.getInfoId());
//         destination.setDestinationLocate(destinationDTO.getDestinationLocate());
//         destination.setDestinationOrder(destinationDTO.getDestinationOrder());
//         DestinationSelf savedDestination = destinationSelfService.createDestination(destination);
//         destinationDTO.setDestinationId(savedDestination.getDestinationId());
//         destinationDTO.setSelfRouteId(savedDestination.getSelfRouteId());
//         return destinationDTO;
//     }

//     /**
//      * 기존 목적지 정보를 업데이트하는 API
//      * @param destinationId 목적지 ID
//      * @param selfRouteId 경로 ID
//      * @param destinationDTO 목적지 DTO
//      * @return DestinationSelfDTO
//      */
//     @PutMapping("/{destinationId}/{selfRouteId}")
//     public DestinationSelfDTO updateDestination(@PathVariable Integer destinationId, @PathVariable Integer selfRouteId, @RequestBody DestinationSelfDTO destinationDTO) {
//         DestinationSelf destination = new DestinationSelf();
//         destination.setDestinationId(destinationId);
//         destination.setSelfRouteId(selfRouteId);
//         destination.setInfoId(destinationDTO.getInfoId());
//         destination.setDestinationLocate(destinationDTO.getDestinationLocate());
//         destination.setDestinationOrder(destinationDTO.getDestinationOrder());
//         DestinationSelf updatedDestination = destinationSelfService.updateDestination(destination);
//         destinationDTO.setDestinationId(updatedDestination.getDestinationId());
//         destinationDTO.setSelfRouteId(updatedDestination.getSelfRouteId());
//         return destinationDTO;
//     }

//     /**
//      * 특정 ID의 목적지 정보를 삭제하는 API
//      * @param destinationId 목적지 ID
//      * @param selfRouteId 경로 ID
//      * @return 삭제 메시지
//      */
//     @DeleteMapping("/{destinationId}/{selfRouteId}")
//     public String deleteDestination(@PathVariable Integer destinationId, @PathVariable Integer selfRouteId) {
//         DestinationSelfId id = new DestinationSelfId();
//         id.setDestinationId(destinationId);
//         id.setSelfRouteId(selfRouteId);
//         destinationSelfService.deleteDestination(id);
//         return "DestinationSelf with ID (" + destinationId + ", " + selfRouteId + ") has been deleted.";
//     }
// }
