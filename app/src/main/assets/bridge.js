(function(){

  const JAVASCRIPT_API = "javascriptApi"
  const NATIVE_API = "nativeApi"
  const DATA = "data"
  
  
  const SCHEME = 'yuuoffice'
  const HOST = 'host'
  const PARAM = 'param'
  
  const callbackMap = {}
  
  
  /**JS发送消息*/
  function sendMessage(message, callBack) {
    if (!message || !message[NATIVE_API]) {
      return
    }
  
    if(!!callBack){
      callbackMap[message[JAVASCRIPT_API]] = callBack
    }
  
    const result = _execNativeMethod(message)
  
    return result
  }
  
  /**JS处理消息 */
  function receiveMessage(message) {
    console.log(JSON.stringify(message))
    if (!message) {
      return
    }
  
    if (message[JAVASCRIPT_API]) {
      const responseCallback = callbackMap[message[JAVASCRIPT_API]]
      if(!!responseCallback){
        _execJavascriptMethod(message,responseCallback)
      }else {
        console.info('no find javascript method to exec!!')
      }   
    }

    return "exec receiveMessage method success!! "
  }

  /**注册JavaScript方法供原生调用 */
  function registerMessageHandler(message, callBack){
    if (!message) {
      return
    }
  
    if(!!callBack){
      callbackMap[message[JAVASCRIPT_API]] = callBack
    }
  
  }

  /**执行JavaScript方法*/
  function _execJavascriptMethod(message,responseCallback){
    responseCallback(message[DATA])
    delete callbackMap[message[JAVASCRIPT_API]]
  }
  
  /**执行Native方法*/
  function _execNativeMethod(message) {
    // return document.location = SCHEME + '://' + HOST + '?' + PARAM + '=' + JSON.stringify(message)
    // return alert(SCHEME + '://' + HOST + '?' + PARAM + '=' + JSON.stringify(message))
    // return confirm(SCHEME + '://' + HOST + '?' + PARAM + '=' + JSON.stringify(message))
    return prompt(SCHEME + '://' + HOST + '?' + PARAM + '=' + JSON.stringify(message))
  }
  
  /**暴露方法挂载到window*/
  const HybridBridge = window.HybridBridge = {
    sendMessage: sendMessage,
    receiveMessage:receiveMessage,
    registerMessageHandler:registerMessageHandler
  }
})()



