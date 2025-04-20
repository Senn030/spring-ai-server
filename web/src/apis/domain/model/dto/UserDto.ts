export type UserDto = {
  User: {
    id: string
    createdTime: string
    editedTime: string
    /**
     * 手机号
     */
    phone: string
    /**
     * 密码
     */
    password: string
    /**
     * 头像
     */
    avatar?: string | undefined
    /**
     * 昵称
     */
    nickname?: string | undefined
    /**
     * 性别
     */
    gender?: string | undefined
  }
}
