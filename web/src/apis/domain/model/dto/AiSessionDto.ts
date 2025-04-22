import type { MessageType } from '../enums'
import type { AiMessage_Media } from '../static'

export type AiSessionDto = {
  AiSessionDto: {
    id: string
    createdTime: string
    editedTime: string
    /**
     * 会话名称
     */
    name: string
    /**
     * 一对多关联消息，按创建时间升序
     */
    messages: Array<{
      id: string
      createdTime: string
      editedTime: string
      /**
       * 消息类型(用户/助手/系统)
       */
      type: MessageType
      /**
       * 消息内容
       */
      textContent: string
      medias?: Array<AiMessage_Media> | undefined
      sessionId: string
    }>
  }
}
