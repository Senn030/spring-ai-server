export const MessageType_CONSTANTS = ['USER', 'ASSISTANT', 'SYSTEM', 'TOOL'] as const
export type MessageType = (typeof MessageType_CONSTANTS)[number]
