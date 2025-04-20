import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { UserDto } from '@/apis/domain/model/dto'
import { api } from '@/utils/api-instance'

export const useHomeStore = defineStore('home', () => {
  const userInfo = ref<UserDto['User']>()
  const getUserInfo = async () => {
    userInfo.value = await api.userController.userInfo()
    return userInfo.value
  }
  const init = async () => {
    await getUserInfo()
  }
  const logout = () => {
    userInfo.value = undefined
  }
  return { userInfo, getUserInfo, init, logout }
})
