<template>
  <div class="container">
    <div class="header">
      <button class="icon-button" @click="goBack">←</button>
      <input
        v-model="query"
        type="text"
        class="search-input"
        placeholder="검색어 입력"
        @input="handleInput"
      />
      <button class="icon-button" @click="search">🔍</button>
    </div>

    <div class="map-select" @click="goToMap">지도에서 선택</div>

    <ul class="item-list">
      <li
        v-for="(item, index) in displayList"
        :key="index"
        class="item-row"
      >
        <span @click="goToDetail(item)">{{ item }}</span>
        <button
          v-if="!isTyping"
          @click.stop="removeItem(index)"
          class="remove-button"
        >✕</button>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  data() {
    return {
      query: '',
      recentSearches: [
        '광주대학교', '조선대학교', '송원대학교',
        '전남대학교', '호남대학교', '버스터미널',
        '편의점', '맘스터치', '지하철역', '춘자네 소금구이'
      ],
      allSuggestions: [
         '광주대학교', '조선대학교', '송원대학교',
        '전남대학교', '호남대학교', '버스터미널',
        '편의점', '맘스터치', '지하철역', '춘자네 소금구이'
      ],
      isTyping: false,
    };
  },
  computed: {
    displayList() {
      if (this.query.trim()) {
        return this.allSuggestions.filter(item =>
          item.includes(this.query.trim())
        );
      } else {
        return this.recentSearches;
      }
    },
  },
  methods: {
    handleInput() {
      this.isTyping = this.query.trim().length > 0;
    },
    goBack() {
      alert('지도화면으로 돌아갑니다.');
    },
    goToMap() {
      alert('지도에서 선택 화면으로 이동합니다.');
    },
    search() {
      if (!this.query.trim()) return;

      // 검색 기록 추가
      this.recentSearches = [
        this.query,
        ...this.recentSearches.filter(item => item !== this.query),
      ].slice(0, 10);

      this.$router.push({ name: 'SearchResult', params: { name: this.query } });
      this.query = '';
      this.isTyping = false;
    },
    goToDetail(item) {
      this.query = item;
      this.search();
    },
    removeItem(index) {
      this.recentSearches.splice(index, 1);
    },
  },
};
</script>

<style scoped>
.container {
  max-width: 480px;
  margin: 0 auto;
  font-family: 'Noto Sans KR', sans-serif;
}
.header {
  display: flex;
  align-items: center;
  padding: 8px;
  border-bottom: 1px solid #ccc;
}
.icon-button {
  font-size: 20px;
  background: none;
  border: none;
  cursor: pointer;
  width: 32px;
  height: 32px;
}
.search-input {
  flex-grow: 1;
  margin: 0 8px;
  padding: 8px;
  font-size: 16px;
  border: 1px solid #ddd;
  border-radius: 8px;
}
.map-select {
  text-align: center;
  padding: 12px;
  border-bottom: 1px solid #000;
  font-weight: bold;
  cursor: pointer;
}
.item-list {
  list-style: none;
  padding: 0;
  margin: 0;
}
.item-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px;
  border-bottom: 1px solid #000;
}
.remove-button {
  background: none;
  border: none;
  font-size: 18px;
  color: black;
  cursor: pointer;
}
</style>