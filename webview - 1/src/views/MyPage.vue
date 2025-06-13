<template>
  <div class="container">
    <div class="top-bar">
      <button class="back-btn" @click="goBack">←</button>
      <h2 class="title">프로필 수정</h2>
      <button class="settings-btn" @click="goToSettings">⚙</button>
    </div>

    <!-- 프로필 정보 -->
    <div class="profile-box">
      <div class="user-icon">
        <img :src="profile.profileImage" alt="프로필" v-if="profile.profileImage" />
        <span v-else>👤</span>
      </div>
      <p class="username">{{ profile.username }}</p>
    </div>

    <!-- 탭 -->
    <div class="tab-bar">
      <button :class="{ active: tab === 'posts' }" @click="tab = 'posts'">
        작성글 {{ posts.length }}
      </button>
      <button :class="{ active: tab === 'comments' }" @click="tab = 'comments'">
        작성한 댓글 {{ comments.length }}
      </button>
    </div>

    <!-- 콘텐츠 목록 -->
    <div class="list-box">
      <div v-if="tab === 'posts'">
        <div v-for="post in posts" :key="post.id" class="item" @click="goToPost(post.id)">
          <p class="item-title">{{ post.title }}</p>
          <p class="item-meta">{{ post.date }} · 댓글 {{ post.comments }} · 좋아요 {{ post.likes }}</p>
        </div>
      </div>
      <div v-else>
        <div v-for="comment in comments" :key="comment.id" class="item">
          <p class="item-title">{{ comment.content }}</p>
          <p class="item-meta">{{ comment.date }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const tab = ref('posts')

// ✅ API에서 받아올 데이터
const profile = ref({ username: '', profileImage: '' })
const posts = ref([])
const comments = ref([])

const fetchProfile = async () => {
  try {
    const token = localStorage.getItem('token')
    const headers = { Authorization: `Bearer ${token}` }

    const res = await axios.get('/api/user/profile', { headers })
    profile.value = res.data.user
    posts.value = res.data.posts
    comments.value = res.data.comments
  } catch (error) {
    console.error('프로필 로딩 실패:', error)
  }
}

const goBack = () => router.back()
const goToSettings = () => router.push('/settings')
const goToPost = (id) => router.push(`/post/${id}`)

onMounted(fetchProfile)
</script>

<style scoped>
.container {
  background-color: #e0f7fa;
  min-height: 100vh;
  font-family: sans-serif;
}

.top-bar {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  border-bottom: 1px solid #ccc;
  position: relative;
}
.back-btn, .settings-btn {
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
}
.back-btn { margin-right: auto; }
.settings-btn { margin-left: auto; }
.title {
  font-size: 18px;
  font-weight: bold;
  margin: 0 auto;
}

.profile-box {
  text-align: center;
  padding: 20px 0;
}
.user-icon {
  width: 80px;
  height: 80px;
  margin: 0 auto;
  background-color: #ddd;
  border-radius: 50%;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
}
.user-icon img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.username {
  font-size: 18px;
  font-weight: bold;
  margin-top: 8px;
}

.tab-bar {
  display: flex;
  justify-content: center;
  border-bottom: 1px solid #bbb;
}
.tab-bar button {
  flex: 1;
  padding: 10px;
  font-weight: bold;
  border: none;
  background: none;
  cursor: pointer;
}
.tab-bar .active {
  border-bottom: 3px solid #333;
}

.list-box {
  padding: 10px 16px;
}
.item {
  padding: 14px 8px;
  border-bottom: 1px solid #ccc;
  cursor: pointer;
}
.item-title {
  font-weight: bold;
}
.item-meta {
  font-size: 12px;
  color: #666;
  margin-top: 4px;
}
</style>
