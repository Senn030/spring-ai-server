export interface SaTokenInfo {
  tokenName: string;
  tokenValue: string;
  isLogin?: boolean | undefined;
  loginId: any;
  loginType: string;
  tokenTimeout: number;
  sessionTimeout: number;
  tokenSessionTimeout: number;
  tokenActiveTimeout: number;
  loginDevice: string;
  tag: string;
}
