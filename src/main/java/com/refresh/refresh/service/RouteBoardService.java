package com.refresh.refresh.service;

import com.refresh.refresh.entity.RouteBoard;
import com.refresh.refresh.repository.RouteBoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteBoardService {

    @Autowired
    private RouteBoardRepository routeBoardRepository;

    /**
     * 모든 경로 게시판 정보를 가져오는 메서드
     * @return List<RouteBoard>
     */
    public List<RouteBoard> getAllRouteBoards() {
        return routeBoardRepository.findAll();
    }

    /**
     * 특정 ID의 경로 게시판 정보를 가져오는 메서드
     * @param id 게시판 ID
     * @return RouteBoard
     */
    public RouteBoard getRouteBoardById(int id) {
        return routeBoardRepository.findById(id).orElseThrow(() -> new RuntimeException("RouteBoard not found"));
    }

    /**
     * 새로운 경로 게시판 정보를 생성하는 메서드
     * @param routeBoard 게시판 엔티티
     * @return RouteBoard
     */
    public RouteBoard createRouteBoard(RouteBoard routeBoard) {
        return routeBoardRepository.save(routeBoard);
    }

    /**
     * 기존 경로 게시판 정보를 업데이트하는 메서드
     * @param routeBoard 게시판 엔티티
     * @return RouteBoard
     */
    public RouteBoard updateRouteBoard(RouteBoard routeBoard) {
        if (!routeBoardRepository.existsById(routeBoard.getRouteBoardId())) {
            throw new RuntimeException("RouteBoard not found");
        }
        return routeBoardRepository.save(routeBoard);
    }

    /**
     * 특정 ID의 경로 게시판 정보를 삭제하는 메서드
     * @param id 게시판 ID
     */
    public void deleteRouteBoard(int id) {
        if (!routeBoardRepository.existsById(id)) {
            throw new RuntimeException("RouteBoard not found");
        }
        routeBoardRepository.deleteById(id);
    }
}
