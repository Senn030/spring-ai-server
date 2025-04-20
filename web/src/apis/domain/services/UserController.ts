import type { Executor } from '..'
import type { UserDto } from '../model/dto'
import type { SaTokenInfo, UserLoginInput, UserRegisterInput } from '../model/static'

export class UserController {
  constructor(private executor: Executor) {}

  login: (options: UserControllerOptions['login']) => Promise<SaTokenInfo> = async (options) => {
    let _uri = '/user/login'
    return (await this.executor({
      uri: _uri,
      method: 'POST',
      body: options.body
    })) as Promise<SaTokenInfo>
  }

  register: (options: UserControllerOptions['register']) => Promise<SaTokenInfo> = async (
    options
  ) => {
    let _uri = '/user/register'
    return (await this.executor({
      uri: _uri,
      method: 'POST',
      body: options.body
    })) as Promise<SaTokenInfo>
  }

  userInfo: () => Promise<UserDto['User']> = async () => {
    let _uri = '/user'
    return (await this.executor({ uri: _uri, method: 'GET' })) as Promise<UserDto['User']>
  }
}

export type UserControllerOptions = {
  userInfo: {}
  login: {
    body: UserLoginInput
  }
  register: {
    body: UserRegisterInput
  }
}
