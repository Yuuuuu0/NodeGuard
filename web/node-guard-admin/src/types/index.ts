export interface UserInfo {
  id: number
  username: string
  email: string
  mobile: string
  status: number
}

export interface DictItem {
  id: number
  dictType: string
  dictLabel: string
  dictValue: string
  sort: number
  status: number
  remark: string
}

export interface VlessNode {
  id: number
  uuid: string
  server: string
  port: number
  type: string
  network?: string
  udp: number
  tls: number
  serverName?: string
  grpcServiceName?: string
  security?: string
  publicKey?: string
  shortId?: string
  clientFingerprint?: string
  name: string
  status: number
} 