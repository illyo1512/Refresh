<template>
  <div class="danger-alert" v-if="visible">
    <div class="alert-header">
      <span>⚠️ 위험지역에 접근했습니다!</span>
      <button class="close-btn" @click="$emit('close')">✖</button>
    </div>
    <div class="alert-body">
      <p>범죄유형:{{ dangerData.dangerType }}</p>
      <p>위험도:높음</p>
      <p>{{ dangerData.dangerDetail }}</p>
      <p>대응요령: {{ dangerData.dangerCountermeasure }}</p>
    </div>
  </div>
</template>

<script setup>
defineProps({
  visible: Boolean,
  dangerData: Object
})
defineEmits(['close'])
</script>

<style scoped>
.danger-alert {
  position: fixed;
  top: 70px;
  left: 10px;
  right: 10px;
  background-color: #e53935;
  color: white;
  padding: 16px;
  border-radius: 10px;
  z-index: 1000;
  box-shadow: 0 2px 10px rgba(0,0,0,0.3);
}

.alert-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
  margin-bottom: 10px;
}

.alert-body p {
  margin: 4px 0;
}

.close-btn {
  background: none;
  border: none;
  color: white;
  font-size: 18px;
  cursor: pointer;
}
</style>
