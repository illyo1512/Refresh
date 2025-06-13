package com.refresh.refresh.controller;

import com.refresh.refresh.dto.BoardCategoryDTO;
import com.refresh.refresh.entity.BoardCategory;
import com.refresh.refresh.service.BoardCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/board-categories")
public class BoardCategoryController {

    @Autowired
    private BoardCategoryService boardCategoryService;

    /**
     * 모든 게시판 카테고리 정보를 가져오는 API
     * @return List<BoardCategoryDTO>
     */
    @GetMapping
    public List<BoardCategoryDTO> getAllCategories() {
        return boardCategoryService.getAllCategories().stream()
                .map(category -> {
                    BoardCategoryDTO categoryDTO = new BoardCategoryDTO();
                    categoryDTO.setCategoryId(category.getCategoryId());
                    categoryDTO.setCategoryDetail(category.getCategoryDetail());
                    categoryDTO.setUpperCategoryId(category.getUpperCategoryId());
                    return categoryDTO;
                }).collect(Collectors.toList());
    }

    /**
     * 특정 ID의 게시판 카테고리 정보를 가져오는 API
     * @param id 카테고리 ID
     * @return BoardCategoryDTO
     */
    @GetMapping("/{id}")
    public BoardCategoryDTO getCategoryById(@PathVariable int id) {
        BoardCategory category = boardCategoryService.getCategoryById(id);
        BoardCategoryDTO categoryDTO = new BoardCategoryDTO();
        categoryDTO.setCategoryId(category.getCategoryId());
        categoryDTO.setCategoryDetail(category.getCategoryDetail());
        categoryDTO.setUpperCategoryId(category.getUpperCategoryId());
        return categoryDTO;
    }
}
