/**
 * 无影云桌面 Web SDK（需将 WuyingWebSDK.js 与官方 sdk 目录放到 public/wuying/，
 * 或设置 VITE_WUYING_WEB_SDK_URL 指向可访问的 SDK 地址）。
 */

export function loadWuyingSdk() {
  if (typeof window !== 'undefined' && window.Wuying && window.Wuying.WebSDK) {
    return Promise.resolve()
  }
  const src = import.meta.env.VITE_WUYING_WEB_SDK_URL || '/wuying/WuyingWebSDK.js'
  return new Promise((resolve, reject) => {
    const s = document.createElement('script')
    s.src = src
    s.async = true
    s.onload = () => resolve()
    s.onerror = () => reject(new Error('无影 Web SDK 加载失败: ' + src))
    document.head.appendChild(s)
  })
}

/**
 * @param resp后端 VmUrl（authCode、vmGuid、loginRegionId 等）
 */
export async function startWuyingDesktopSession(resp) {
  await loadWuyingSdk()
  const iframeId = 'wuying-desktop-session'
  const sessionParam = {
    openType: resp.wuyingOpenType || 'inline',
    iframeId,
    connectType: 'desktop',
    userInfo: { authCode: resp.authCode },
    desktopInfo: {
      desktopId: resp.vmGuid,
      desktopName: resp.desktopName || '',
      realDesktopId: resp.realDesktopId || resp.vmGuid,
      loginRegionId: resp.loginRegionId,
      connConfig: { decodeType: 0 }
    },
    regionId: resp.loginRegionId
  }
  if (resp.wuyingResourceType) {
    sessionParam.resourceType = resp.wuyingResourceType
  }
  const wuyingSdk = window.Wuying.WebSDK
  const session = wuyingSdk.createSession('appstream', sessionParam)
  session.addHandle('onDisConnected', () => {})
  session.start()
  return session
}
