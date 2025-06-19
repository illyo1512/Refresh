<template>
  <div class="board-wrapper">
    <div class="board-header">
      <button @click="goBack" class="back-btn">←</button>
      <h2 class="board-title">경로 게시판</h2>
      <button class="mypage-btn">👤</button>
    </div>

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

    <div class="post-list">
      <div
        v-for="post in posts"
        :key="post.route_board_id"
        class="post-card"
        @click="goToDetail(post.route_board_id)"
      >
        <div class="post-title-row">
          <span v-if="post.isHot">🔥</span>
          <span class="post-title">{{ post.title }}</span>
        </div>
        <div class="post-info">
          <span>{{ post.nickname }}</span>
          <span>
            {{ post.view_count }}
            <img src="/눈.png" alt="조회수" class="icon" />
          </span>
          <span>
            {{ post.like_count }}
            <img src="/좋아요.png" alt="좋아요" class="icon" />
          </span>
          <span>
            {{ post.comment_count }}
            <img src="/댓글.png" alt="댓글수" class="icon" />
          </span>
        </div>
      </div>
    </div>

    <div class="pagination">
      <button class="page-btn">&laquo;</button>
      <button class="page-btn">1</button>
      <button class="page-btn">2</button>
      <button class="page-btn">3</button>
      <button class="page-btn">&raquo;</button>
    </div>

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

    <!-- ✨ 글작성 버튼 연결됨 -->
    <button class="write-btn" @click="goToWrite">글 작성</button>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const posts = ref([])

const defaultPosts = [
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
]

onMounted(() => {
  const savedPosts = JSON.parse(localStorage.getItem('posts'))
  if (savedPosts && savedPosts.length > 0) {
    posts.value = savedPosts
  } else {
    posts.value = defaultPosts
    localStorage.setItem('posts', JSON.stringify(defaultPosts))
  }
})

function goBack() {
  router.push('/')
}

function goToDetail(id) {
  router.push(`/route-board/${id}`)
}

function goToWrite() {
  router.push('/writePost') // 글작성 페이지 경로
}
</script>

<style scoped>
.board-wrapper {
  background-color: #E3F4F4;
  padding: 16px;
  min-height: 100vh;
}
.board-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.board-title {
  font-size: 20px;
  font-weight: bold;
}
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
.filter-row,
.search-row {
  display: flex;
  gap: 8px;
  margin: 10px 0;
  flex-wrap: wrap;
}
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
.post-title-row {
  font-weight: bold;
  display: flex;
  gap: 6px;
  align-items: center;
  font-size: 16px;
  margin-bottom: 4px;
}
.post-info {
  font-size: 13px;
  color: #666;
  display: flex;
  gap: 12px;
  align-items: center;
}
.icon {
  width: 30px;
  height: 30px;
  vertical-align: middle;
}
.pagination {
  display: flex;
  justify-content: center;
  margin: 20px 0;
  gap: 6px;
}
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
.write-btn {
  position: fixed;
  bottom: 20px;
  right: 20px;
  background-color: #558b2f;
  color: white;
  font-weight: bold;
}
</style>
