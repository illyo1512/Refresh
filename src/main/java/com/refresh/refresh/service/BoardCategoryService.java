package com.refresh.refresh.service;

import com.refresh.refresh.entity.BoardCategory;
import com.refresh.refresh.repository.BoardCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardCategoryService {

    @Autowired
    private BoardCategoryRepository boardCategoryRepository;

    /**
     * 모든 게시판 카테고리 정보를 가져오는 메서드
     * @return List<BoardCategory>
     */
    public List<BoardCategory> getAllCategories() {
        return boardCategoryRepository.findAll();
    }

    /**
     * 특정 ID의 게시판 카테고리 정보를 가져오는 메서드
     * @param id 카테고리 ID
     * @return BoardCategory
     */
    public BoardCategory getCategoryById(int id) {
        return boardCategoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
    }

    /**
     * 새로운 게시판 카테고리 정보를 생성하는 메서드
     * @param category 카테고리 엔티티
     * @return BoardCategory
     */
    public BoardCategory createCategory(BoardCategory category) {
        return boardCategoryRepository.save(category);
    }

    /**
     * 기존 게시판 카테고리 정보를 업데이트하는 메서드
     * @param category 카테고리 엔티티
     * @return BoardCategory
     */
    public BoardCategory updateCategory(BoardCategory category) {
        if (!boardCategoryRepository.existsById(category.getCategoryId())) {
            throw new RuntimeException("Category not found");
        }
        return boardCategoryRepository.save(category);
    }

    /**
     * 특정 ID의 게시판 카테고리 정보를 삭제하는 메서드
     * @param id 카테고리 ID
     */
    public void deleteCategory(int id) {
        if (!boardCategoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found");
        }
        boardCategoryRepository.deleteById(id);
    }
}
