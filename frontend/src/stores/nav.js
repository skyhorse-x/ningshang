import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useNavStore = defineStore('nav', () => {
  const currentPage = ref('home')
  const activeSubNav = ref('')

  function setCurrentPage(page) {
    currentPage.value = page
  }

  function setActiveSubNav(nav) {
    activeSubNav.value = nav
  }

  return { currentPage, activeSubNav, setCurrentPage, setActiveSubNav }
})
