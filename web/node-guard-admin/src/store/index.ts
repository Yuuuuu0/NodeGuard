import { createStore } from 'vuex'
import { UserState } from './modules/user'
import user from './modules/user'

export interface RootState {
  user: UserState
}

const store = createStore<RootState>({
  state: {
    user: {
      token: localStorage.getItem('token') || '',
      username: ''
    }
  },
  mutations: {
  },
  actions: {
  },
  modules: {
    user
  }
})

export default store 