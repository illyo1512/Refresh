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

    /**
     * 새로운 게시판 카테고리 정보를 생성하는 API
     * @param categoryDTO 카테고리 DTO
     * @return BoardCategoryDTO
     */
    @PostMapping
    public BoardCategoryDTO createCategory(@RequestBody BoardCategoryDTO categoryDTO) {
        BoardCategory category = new BoardCategory();
        category.setCategoryDetail(categoryDTO.getCategoryDetail());
        category.setUpperCategoryId(categoryDTO.getUpperCategoryId());
        BoardCategory savedCategory = boardCategoryService.createCategory(category);
        categoryDTO.setCategoryId(savedCategory.getCategoryId());
        return categoryDTO;
    }

    /**
     * 기존 게시판 카테고리 정보를 업데이트하는 API
     * @param id 카테고리 ID
     * @param categoryDTO 카테고리 DTO
     * @return BoardCategoryDTO
     */
    @PutMapping("/{id}")
    public BoardCategoryDTO updateCategory(@PathVariable int id, @RequestBody BoardCategoryDTO categoryDTO) {
        BoardCategory category = new BoardCategory();
        category.setCategoryId(id);
        category.setCategoryDetail(categoryDTO.getCategoryDetail());
        category.setUpperCategoryId(categoryDTO.getUpperCategoryId());
        BoardCategory updatedCategory = boardCategoryService.updateCategory(category);
        categoryDTO.setCategoryId(updatedCategory.getCategoryId());
        return categoryDTO;
    }

    /**
     * 특정 ID의 게시판 카테고리 정보를 삭제하는 API
     * @param id 카테고리 ID
     * @return 삭제 메시지
     */
    @DeleteMapping("/{id}")
    public String deleteCategory(@PathVariable int id) {
        boardCategoryService.deleteCategory(id);
        return "BoardCategory with ID " + id + " has been deleted.";
    }
}
