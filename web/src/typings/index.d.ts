export type EditMode = 'CREATE' | 'UPDATE'
export interface Scope<T> {
  row: T
  $index: number
}

export interface Result<T> {
  code: number
  success: boolean
  msg: string
  result: T
}
