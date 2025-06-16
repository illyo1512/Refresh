<template>
  <div class="board-wrapper">
    <!-- 상단 헤더: 뒤로가기, 제목, 마이페이지 버튼 -->
    <div class="board-header">
      <button @click="goBack" class="back-btn">←</button>
      <h2 class="board-title">경로 게시판</h2>
      <button class="mypage-btn">👤</button>
    </div>

    <!-- 필터 버튼 및 지역 선택 -->
    <div class="filter-row">
      <button class="filter-btn">전체글</button>
      <button class="filter-btn">공지</button>
      <button class="filter-btn">인기</button>
      <button class="filter-btn">즐겨찾기</button>
      <select class="filter-select">
        <option>지역선택</option>
        <option>광주 광산구</option>
        <option>서울 강남구</option>
      </select>
    </div>

    <!-- 게시글 목록 -->
    <div class="post-list">
      <div
        v-for="post in posts"
        :key="post.route_board_id"
        class="post-card"
        @click="goToDetail(post.route_board_id)"
      >
        <div class="post-title-row">
          <!-- 인기글이면 🔥 표시 -->
          <span v-if="post.isHot">🔥</span>
          <span class="post-title">{{ post.title }}</span>
        </div>
        <div class="post-info">
          <!-- 닉네임, 조회수, 좋아요, 댓글수 표시 -->
          <span>{{ post.nickname }}</span>
          <span>{{ post.view_count }}👁</span>
          <span>{{ post.like_count }}👍</span>
          <span>{{ post.comment_count }}💬</span>
        </div>
      </div>
    </div>

    <!-- 페이지네이션 버튼 -->
    <div class="pagination">
      <button class="page-btn">&laquo;</button>
      <button class="page-btn">1</button>
      <button class="page-btn">2</button>
      <button class="page-btn">3</button>
      <button class="page-btn">&raquo;</button>
    </div>

    <!-- 검색창 -->
    <div class="search-row">
      <select class="search-type">
        <option>전체</option>
        <option>제목</option>
        <option>댓글</option>
        <option>작성자</option>
      </select>
      <input class="search-input" placeholder="검색어 입력" />
      <button class="search-btn">🔍</button>
    </div>

    <!-- 글 작성 버튼 (우측 하단 고정) -->
    <button class="write-btn">글 작성</button>
  </div>
</template>

<script setup>
// 라우터 관련 훅 import
import { ref } from 'vue'
import { useRouter } from 'vue-router'

// 라우터 객체 사용
const router = useRouter()

// 뒤로 가기 버튼 클릭 시 메인으로 이동
function goBack() {
  router.push('/')
}

// 게시글 클릭 시 상세 페이지로 이동
function goToDetail(id) {
  router.push(`/route-board/${id}`)
}

// 게시글 더미 데이터 (임시 하드코딩)
const posts = ref([
  {
    route_board_id: 1,
    user_id: 1,
    saved_route_id: 10,
    self_route_id: null,
    title: '광주 여성 안심귀가 루트',
    content: '밤길 조심하세요! 이 루트 추천드립니다.',
    category_id: 1,
    created_at: '2025-06-12T15:00:00',
    nickname: '민지맘',
    view_count: 134,
    like_count: 27,
    comment_count: 5,
    isHot: true
  },
  {
    route_board_id: 2,
    user_id: 2,
    saved_route_id: null,
    self_route_id: 5,
    title: '광산구 낮 산책 추천 루트',
    content: '햇살 좋을 때 걷기 좋아요!',
    category_id: 2,
    created_at: '2025-06-10T10:00:00',
    nickname: '산책왕',
    view_count: 82,
    like_count: 12,
    comment_count: 2,
    isHot: false
  },
  {
    route_board_id: 3,
    user_id: 1,
    saved_route_id: 12,
    self_route_id: null,
    title: '여성 전용 안전 귀갓길 공유',
    content: '경찰서와 가로등 많은 루트입니다.',
    category_id: 3,
    created_at: '2025-06-11T21:30:00',
    nickname: '민지맘',
    view_count: 200,
    like_count: 41,
    comment_count: 7,
    isHot: true
  }
])
</script>

<style scoped>
/* 전체 화면 wrapper */
.board-wrapper {
  background-color: #E3F4F4;
  padding: 16px;
  min-height: 100vh;
}

/* 상단 헤더 */
.board-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.board-title {
  font-size: 20px;
  font-weight: bold;
}

/* 공통 버튼 스타일 */
.back-btn {
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
  margin-right: 8px;
}
.mypage-btn,
.write-btn,
.search-btn,
.page-btn,
.filter-btn {
  padding: 6px 12px;
  border: none;
  background-color: #ccc;
  border-radius: 6px;
}

/* 필터 영역 */
.filter-row,
.search-row {
  display: flex;
  gap: 8px;
  margin: 10px 0;
  flex-wrap: wrap;
}

/* 게시글 리스트 */
.post-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.post-card {
  background-color: #fff;
  padding: 12px;
  border-radius: 10px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.1);
  cursor: pointer;
}

/* 게시글 제목 라인 */
.post-title-row {
  font-weight: bold;
  display: flex;
  gap: 6px;
  align-items: center;
  font-size: 16px;
  margin-bottom: 4px;
}

/* 게시글 정보 라인 */
.post-info {
  font-size: 13px;
  color: #666;
  display: flex;
  gap: 12px;
}

/* 페이지네이션 */
.pagination {
  display: flex;
  justify-content: center;
  margin: 20px 0;
  gap: 6px;
}

/* 검색창 */
.search-input {
  flex: 1;
  padding: 6px;
  border-radius: 4px;
  border: 1px solid #ccc;
}
.search-type,
.filter-select {
  padding: 6px;
  border-radius: 4px;
  border: 1px solid #ccc;
}

/* 글쓰기 버튼 - 고정 위치 */
.write-btn {
  position: fixed;
  bottom: 20px;
  right: 20px;
  background-color: #558b2f;
  color: white;
  font-weight: bold;
}
</style>
