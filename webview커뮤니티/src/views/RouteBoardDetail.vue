<template>
  <div class="detail-wrapper">
    <!-- 상단 헤더 카드 -->
    <div class="header-card">
      <div class="top-row">
        <!-- 뒤로가기 버튼 -->
        <button class="back-btn" @click="goBack">←</button>

        <!-- 메뉴(글 수정/삭제/신고) 드롭다운 -->
        <div class="dropdown">
          <button class="menu-btn" @click="toggleMenu">☰</button>
          <ul v-if="menuOpen" class="menu-list">
            <!-- 내가 쓴 글일 경우: 수정/삭제 가능 -->
            <li v-if="isMyPost" @click="editPost">글 수정</li>
            <li v-if="isMyPost" @click="deletePost">글 삭제</li>
            <!-- 남이 쓴 글일 경우: 신고만 가능 -->
            <li v-else @click="reportPost">신고</li>
          </ul>
        </div>
      </div>

      <!-- 게시글 제목 -->
      <div class="title">{{ post.title }}</div>

      <!-- 작성자 닉네임 및 작성일 -->
      <div class="meta">
        <span class="nickname">@{{ post.nickname }}</span>
        <span class="date">{{ formatDate(post.created_at) }}</span>
      </div>
    </div>

    <!-- 게시글 본문 -->
    <div class="content-area">
      <p class="content-text">{{ post.content }}</p>

      <!-- 경로 이미지 (없으면 기본 이미지) -->
      <div class="image-wrapper">
        <img :src="post.mapImageUrl || '/경로추천.png'" alt="경로 이미지" />
      </div>

      <!-- 좋아요 버튼 및 개수 -->
      <div class="like-section">
        <button class="like-btn" @click="toggleLike">
          {{ liked ? '💖' : '🤍' }}
        </button>
        <div class="like-count">{{ post.like_count }}</div>
      </div>
    </div>

    <!-- 구분선 -->
    <hr class="divider" />

    <!-- 댓글 영역 -->
    <div class="comment-section">
      <h3>댓글</h3>

      <!-- 댓글 입력창 -->
      <div class="comment-input">
        <input v-model="newComment" placeholder="댓글을 남겨주세요" />
        <button @click="submitComment">등록</button>
      </div>

      <!-- 댓글 리스트 -->
      <ul class="comment-list">
        <li v-for="comment in comments" :key="comment.id" class="comment-card">
          <div class="comment-nick">{{ comment.writer }}</div>
          <div class="comment-content">{{ comment.content }}</div>
          <div class="comment-date">{{ formatDate(comment.created_at) }}</div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'

// 라우터 설정
const router = useRouter()
const route = useRoute()

// 게시글 객체
const post = ref({})
// 댓글 목록
const comments = ref([])
// 새 댓글 입력 값
const newComment = ref('')
// 메뉴 오픈 여부
const menuOpen = ref(false)
// 좋아요 여부
const liked = ref(false)
// 현재 글이 내 글인지 여부 (실제로는 로그인 유저와 비교 필요)
const isMyPost = true

// 컴포넌트 마운트 시 게시글 및 댓글 더미 데이터 설정
onMounted(() => {
  const id = route.params.id

  post.value = {
    title: '여기 좋아요',
    nickname: 'abcd123',
    created_at: '2025-04-14T12:46:00',
    content: '여기 무등산에서 큰길쪽으로 쭉 가다보면 경치도 좋고 맛있는 것도 많이 있어요',
    like_count: 42,
    mapImageUrl: ''  // 지도 이미지 없을 경우 대체 이미지 사용
  }

  comments.value = [
    { id: 1, writer: '장예지', content: '완전 감사합니다!', created_at: '2025-04-17T14:24:00' },
    { id: 2, writer: 'user2', content: '좋은 정보네요', created_at: '2025-04-17T14:26:00' }
  ]
})

// ← 버튼 누르면 게시판으로 이동
function goBack() {
  router.push('/route_board')
}

// 메뉴 토글
function toggleMenu() {
  menuOpen.value = !menuOpen.value
}

// 글 수정 (실제로는 페이지 이동 필요)
function editPost() {
  alert('글 수정 기능')
}

// 글 삭제 (실제로는 API 요청 필요)
function deletePost() {
  alert('글 삭제 기능')
}

// 글 신고 (실제로는 API 요청 필요)
function reportPost() {
  alert('신고하기 기능')
}

// 좋아요 버튼 클릭
function toggleLike() {
  liked.value = !liked.value
  post.value.like_count += liked.value ? 1 : -1
}

// 댓글 등록 버튼 클릭
function submitComment() {
  if (!newComment.value.trim()) return

  // 새 댓글 추가
  comments.value.push({
    id: Date.now(),
    writer: 'me', // 실제 유저 닉네임 사용 예정
    content: newComment.value,
    created_at: new Date().toISOString()
  })
  newComment.value = ''
}

// 날짜 포맷 변환 (YYYY-MM-DD | HH:MM)
function formatDate(str) {
  const d = new Date(str)
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')} | ${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`
}
</script>

<style scoped>
/* 전체 wrapper */
.detail-wrapper {
  background: #e3f4f4;
  min-height: 100vh;
  padding: 16px;
  font-family: sans-serif;
  max-width: 480px;
  margin: 0 auto;
}

/* 상단 카드 */
.header-card {
  background: #fff;
  padding: 14px;
  border-radius: 12px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  margin-bottom: 12px;
}
.top-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.back-btn,
.menu-btn {
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
}
.dropdown {
  position: relative;
}
.menu-list {
  position: absolute;
  top: 36px;
  right: 0;
  background: white;
  border: 1px solid #ccc;
  border-radius: 4px;
  list-style: none;
  padding: 4px 0;
  z-index: 100;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}
.menu-list li {
  padding: 6px 12px;
  cursor: pointer;
  font-size: 14px;
  white-space: nowrap;
}
.title {
  font-weight: bold;
  font-size: 18px;
  margin-top: 6px;
}
.meta {
  font-size: 12px;
  color: #666;
  margin-top: 4px;
}

/* 본문 */
.content-area {
  padding: 10px 0;
}
.content-text {
  font-size: 14px;
  line-height: 1.6;
  margin-bottom: 10px;
}
.image-wrapper {
  display: flex;
  justify-content: center;
  margin-bottom: 10px;
}
.image-wrapper img {
  max-width: 100%;
  border-radius: 8px;
  border: 1px solid #ccc;
}
.like-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 16px 0;
}
.like-btn {
  font-size: 32px;
  background: none;
  border: none;
  cursor: pointer;
}
.like-count {
  font-size: 14px;
  margin-top: 4px;
}

/* 구분선 */
.divider {
  border: none;
  border-top: 1px solid #ccc;
  margin: 12px 0;
}

/* 댓글 섹션 */
.comment-section {
  padding: 14px;
  border-radius: 12px;
}
.comment-input {
  display: flex;
  gap: 6px;
  margin-bottom: 12px;
}
.comment-input input {
  flex: 1;
  padding: 8px;
  border: 1px solid #aaa;
  border-radius: 6px;
}
.comment-input button {
  background: #558b2f;
  color: white;
  padding: 8px 12px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
}
.comment-card {
  background: #e3f4f4;
  padding: 10px;
  border-radius: 6px;
  margin-bottom: 8px;
  font-size: 14px;
}
.comment-nick {
  font-weight: bold;
  margin-bottom: 4px;
}
.comment-content {
  margin-bottom: 4px;
}
.comment-date {
  font-size: 12px;
  color: #666;
}
</style>
