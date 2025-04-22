import type { Executor } from '.'
import { AiMessageController, AiSessionController, UserController } from './services/'

export class Api {
  readonly userController: UserController

  readonly aiSessionController: AiSessionController

  readonly aiMessageController: AiMessageController

  constructor(executor: Executor) {
    this.userController = new UserController(executor)
    this.aiSessionController = new AiSessionController(executor)
    this.aiMessageController = new AiMessageController(executor)
  }
}
