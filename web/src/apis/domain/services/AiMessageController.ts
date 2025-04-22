import type { Executor } from '../'
import type { AiMessageInput, Flux, ServerSentEvent } from '../model/static/'

export class AiMessageController {
  constructor(private executor: Executor) {}

  /**
   * 为了支持文件问答，需要同时接收json（AiMessageWrapper json体）和 MultipartFile（文件）
   * Content-Type 从 application/json 修改为 multipart/form-data
   * 之前接收请求参数是用@RequestBody, 现在使用@RequestPart 接收json字符串再手动转成AiMessageWrapper.
   * SpringMVC的@RequestPart是支持自动将Json字符串转换为Java对象，也就是说可以等效`@RequestBody`，
   * 但是由于前端FormData无法设置Part的Content-Type，所以只能手动转json字符串再转成Java对象。
   *
   * @param input 消息包含文本信息，会话id，多媒体信息（图片语言）
   * @param file 文件问答
   * @return SSE流
   */
  chat: (options: AiMessageControllerOptions['chat']) => Promise<Flux<ServerSentEvent<string>>> =
    async (options) => {
      let _uri = '/message/chat'
      const _formData = new FormData()
      const _body = options.body
      _formData.append(
        'input',
        new Blob([JSON.stringify(_body.input)], { type: 'application/json' })
      )
      if (_body.file) {
        _formData.append('file', _body.file)
      }
      return (await this.executor({ uri: _uri, method: 'POST', body: _formData })) as Promise<
        Flux<ServerSentEvent<string>>
      >
    }

  deleteHistory: (options: AiMessageControllerOptions['deleteHistory']) => Promise<void> = async (
    options
  ) => {
    let _uri = '/message/history/'
    _uri += encodeURIComponent(options.sessionId)
    return (await this.executor({ uri: _uri, method: 'DELETE' })) as Promise<void>
  }

  /**
   * 消息保存
   *
   * @param input 用户发送的消息/AI回复的消息
   */
  save: (options: AiMessageControllerOptions['save']) => Promise<void> = async (options) => {
    let _uri = '/message'
    return (await this.executor({ uri: _uri, method: 'POST', body: options.body })) as Promise<void>
  }
}

export type AiMessageControllerOptions = {
  deleteHistory: {
    sessionId: string
  }
  save: {
    /**
     * 用户发送的消息/AI回复的消息
     */
    body: AiMessageInput
  }
  chat: {
    body: {
      input: string
      file?: File | undefined
    }
  }
}
