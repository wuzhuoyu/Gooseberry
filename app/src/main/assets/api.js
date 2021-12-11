document.write("<script language=javascript src='./bridge.js'></script>");

function callAndroid() {
    var message = { nativeApi: 'TestController/ControllerA',javascriptApi:'TestJSController/ControllerJSA',data:'sendMessage'}
    var result = HybridBridge.sendMessage(message, function (message) {
        alert(message.data)
        console.log(message)
    })
}

function registerListener(){
    var message = {javascriptApi:'TestJSController/RegisterListener'}
    var result = HybridBridge.registerMessageHandler(message, function (message) {
        alert(message.data)
        console.log(message)
    })
}




