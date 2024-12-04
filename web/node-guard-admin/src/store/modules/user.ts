import { Module } from 'vuex'
import { RootState } from '../index'

export interface UserState {
  token: string
  username: string
}

const state: UserState = {
  token: localStorage.getItem('token') || '',
  username: ''
}

const user: Module<UserState, RootState> = {
  namespaced: true,
  state,
  mutations: {
    SET_TOKEN(state, token: string) {
      state.token = token
      localStorage.setItem('token', token)
    },
    SET_USERNAME(state, username: string) {
      state.username = username
    },
    CLEAR(state) {
      state.token = ''
      state.username = ''
      localStorage.removeItem('token')
    }
  },
  actions: {
    // 登录
    login({ commit }, userInfo) {
      const { token, username } = userInfo
      commit('SET_TOKEN', token)
      commit('SET_USERNAME', username)
    },
    // 登出
    logout({ commit }) {
      commit('CLEAR')
    }
  }
}

export default user 