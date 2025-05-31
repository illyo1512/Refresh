package com.refresh.refresh.controller;

import com.refresh.refresh.dto.RouteBoardDTO;
import com.refresh.refresh.entity.RouteBoard;
import com.refresh.refresh.service.RouteBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/route-boards")
public class RouteBoardController {

    @Autowired
    private RouteBoardService routeBoardService;

    /**
     * 모든 경로 게시판 정보를 가져오는 API
     * @return List<RouteBoardDTO>
     */
    @GetMapping
    public List<RouteBoardDTO> getAllRouteBoards() {
        return routeBoardService.getAllRouteBoards().stream()
                .map(routeBoard -> {
                    RouteBoardDTO routeBoardDTO = new RouteBoardDTO();
                    routeBoardDTO.setRouteBoardId(routeBoard.getRouteBoardId());
                    routeBoardDTO.setUserId(routeBoard.getUserId());
                    routeBoardDTO.setSavedRouteId(routeBoard.getSavedRouteId());
                    routeBoardDTO.setSelfRouteId(routeBoard.getSelfRouteId());
                    routeBoardDTO.setTitle(routeBoard.getTitle());
                    routeBoardDTO.setContent(routeBoard.getContent());
                    routeBoardDTO.setCategoryId(routeBoard.getCategoryId());
                    routeBoardDTO.setCreatedAt(routeBoard.getCreatedAt());
                    return routeBoardDTO;
                }).collect(Collectors.toList());
    }

    /**
     * 특정 ID의 경로 게시판 정보를 가져오는 API
     * @param id 게시판 ID
     * @return RouteBoardDTO
     */
    @GetMapping("/{id}")
    public RouteBoardDTO getRouteBoardById(@PathVariable int id) {
        RouteBoard routeBoard = routeBoardService.getRouteBoardById(id);
        RouteBoardDTO routeBoardDTO = new RouteBoardDTO();
        routeBoardDTO.setRouteBoardId(routeBoard.getRouteBoardId());
        routeBoardDTO.setUserId(routeBoard.getUserId());
        routeBoardDTO.setSavedRouteId(routeBoard.getSavedRouteId());
        routeBoardDTO.setSelfRouteId(routeBoard.getSelfRouteId());
        routeBoardDTO.setTitle(routeBoard.getTitle());
        routeBoardDTO.setContent(routeBoard.getContent());
        routeBoardDTO.setCategoryId(routeBoard.getCategoryId());
        routeBoardDTO.setCreatedAt(routeBoard.getCreatedAt());
        return routeBoardDTO;
    }

    /**
     * 새로운 경로 게시판 정보를 생성하는 API
     * @param routeBoardDTO 게시판 DTO
     * @return RouteBoardDTO
     */
    @PostMapping
    public RouteBoardDTO createRouteBoard(@RequestBody RouteBoardDTO routeBoardDTO) {
        RouteBoard routeBoard = new RouteBoard();
        routeBoard.setUserId(routeBoardDTO.getUserId());
        routeBoard.setSavedRouteId(routeBoardDTO.getSavedRouteId());
        routeBoard.setSelfRouteId(routeBoardDTO.getSelfRouteId());
        routeBoard.setTitle(routeBoardDTO.getTitle());
        routeBoard.setContent(routeBoardDTO.getContent());
        routeBoard.setCategoryId(routeBoardDTO.getCategoryId());
        routeBoard.setCreatedAt(routeBoardDTO.getCreatedAt());
        RouteBoard savedRouteBoard = routeBoardService.createRouteBoard(routeBoard);
        routeBoardDTO.setRouteBoardId(savedRouteBoard.getRouteBoardId());
        return routeBoardDTO;
    }

    /**
     * 기존 경로 게시판 정보를 업데이트하는 API
     * @param id 게시판 ID
     * @param routeBoardDTO 게시판 DTO
     * @return RouteBoardDTO
     */
    @PutMapping("/{id}")
    public RouteBoardDTO updateRouteBoard(@PathVariable int id, @RequestBody RouteBoardDTO routeBoardDTO) {
        RouteBoard routeBoard = new RouteBoard();
        routeBoard.setRouteBoardId(id);
        routeBoard.setUserId(routeBoardDTO.getUserId());
        routeBoard.setSavedRouteId(routeBoardDTO.getSavedRouteId());
        routeBoard.setSelfRouteId(routeBoardDTO.getSelfRouteId());
        routeBoard.setTitle(routeBoardDTO.getTitle());
        routeBoard.setContent(routeBoardDTO.getContent());
        routeBoard.setCategoryId(routeBoardDTO.getCategoryId());
        routeBoard.setCreatedAt(routeBoardDTO.getCreatedAt());
        RouteBoard updatedRouteBoard = routeBoardService.updateRouteBoard(routeBoard);
        routeBoardDTO.setRouteBoardId(updatedRouteBoard.getRouteBoardId());
        return routeBoardDTO;
    }

    /**
     * 특정 ID의 경로 게시판 정보를 삭제하는 API
     * @param id 게시판 ID
     * @return 삭제 메시지
     */
    @DeleteMapping("/{id}")
    public String deleteRouteBoard(@PathVariable int id) {
        routeBoardService.deleteRouteBoard(id);
        return "RouteBoard with ID " + id + " has been deleted.";
    }
}
