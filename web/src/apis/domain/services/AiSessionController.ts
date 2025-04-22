import type { Executor } from '../'
import type { AiSessionDto } from '../model/dto/'
import type { AiSessionInput } from '../model/static/'

export class AiSessionController {
  constructor(private executor: Executor) {}

  /**
   * 批量删除会话
   * @param ids 会话id列表
   */
  delete: (options: AiSessionControllerOptions['delete']) => Promise<void> = async (options) => {
    let _uri = '/session'
    return (await this.executor({
      uri: _uri,
      method: 'DELETE',
      body: options.body
    })) as Promise<void>
  }

  /**
   * 根据id查询会话
   * @param id 会话id
   * @return 会话信息
   */
  findById: (options: AiSessionControllerOptions['findById']) => Promise<AiSessionDto> = async (
    options
  ) => {
    let _uri = '/session/'
    _uri += encodeURIComponent(options.id)
    return (await this.executor({ uri: _uri, method: 'GET' })) as Promise<AiSessionDto>
  }

  /**
   * 查询当前登录用户的会话
   *
   * @return 会话列表
   */
  findByUser: () => Promise<Array<AiSessionDto>> = async () => {
    let _uri = '/session/user'
    return (await this.executor({ uri: _uri, method: 'GET' })) as Promise<Array<AiSessionDto>>
  }

  /**
   * 保存会话
   * @param input 会话dto参考src/main/dto/AiSession.dto
   * @return 创建后的id
   */
  save: (options: AiSessionControllerOptions['save']) => Promise<string> = async (options) => {
    let _uri = '/session/save'
    return (await this.executor({
      uri: _uri,
      method: 'POST',
      body: options.body
    })) as Promise<string>
  }
}

export type AiSessionControllerOptions = {
  findById: {
    /**
     * 会话id
     */
    id: string
  }
  save: {
    /**
     * 会话dto参考src/main/dto/AiSession.dto
     */
    body: AiSessionInput
  }
  findByUser: {}
  delete: {
    /**
     * 会话id列表
     */
    body: Array<string>
  }
}
