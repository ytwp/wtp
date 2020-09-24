<template>
  <div class="app-container documentation-container">
    <a class="document-btn" target="_blank" href="https://github.com/ytwp/wtp/">{{ $t('documentation.github') }}</a>
    <a class="document-btn" target="_blank" href="https://gitee.com/ytwp/wtp/">{{ $t('documentation.gitee') }}</a>
    <a class="document-btn" target="_blank" href="https://github.com/ytwp/wtp/wiki/%E6%8E%A5%E5%85%A5%E6%8C%87%E5%8D%97">{{ $t('documentation.usage') }}</a>
    <a class="document-btn" target="_blank" href="https://github.com/ytwp/wtp/wiki/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98">{{ $t('documentation.FAQ') }}</a>
  </div>
</template>

<script>
import DropdownMenu from '@/components/Share/DropdownMenu'

export default {
  name: 'Documentation',
  components: {DropdownMenu},
  data() {
    return {}
  }
}
</script>

<style lang="scss" scoped>
.documentation-container {
  margin: 50px;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-evenly;

  .document-btn {
    flex-shrink: 0;
    display: block;
    cursor: pointer;
    background: black;
    color: white;
    height: 60px;
    width: 200px;
    margin-bottom: 16px;
    line-height: 60px;
    font-size: 20px;
    text-align: center;
  }
}
</style>
