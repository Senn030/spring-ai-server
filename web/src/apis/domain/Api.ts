import type { Executor } from '.'
import { UserController } from './services/UserController'

export class Api {
  readonly userController: UserController

  constructor(executor: Executor) {
    this.userController = new UserController(executor)
  }
}
