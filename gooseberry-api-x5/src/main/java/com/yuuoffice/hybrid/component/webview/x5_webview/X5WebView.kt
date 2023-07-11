package com.yuuoffice.hybrid.component.webview.x5_webview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import com.jeremyliao.liveeventbus.BuildConfig
import com.yuuoffice.hybrid.component.ext.infoLog
import com.yuuoffice.hybrid.component.webview.base_webview.BaseWebView
import com.tencent.smtt.sdk.*


/**
 * @ClassName : X5WebView
 * @Description:
 * @Author: WuZhuoyu
 * @Date: 2021/12/1 16:57
 */

abstract class X5WebView(context: Context):BaseWebView(context) {

    override fun drawChild(canvas: Canvas, child: View?, drawingTime: Long): Boolean {

        val ret = super.drawChild(canvas, child, drawingTime)
        if(BuildConfig.DEBUG){
            canvas.save()
            val paint = Paint()
            paint.color = 0x7fff0000
            paint.textSize = 24f
            paint.isAntiAlias = true
            if (x5WebViewExtension != null) {
                infoLog("X5内核加载成功-----X5 core version:${QbSdk.getTbsVersion(this.context)}------X5 sdk version:${QbSdk.getTbsSdkVersion()}")
                canvas.drawText("X5内核加载成功", 10f, 50f, paint)
                canvas.drawText(
                    "X5 core version:" + QbSdk.getTbsVersion(this.context),
                    10f,
                    100f,
                    paint
                )

                canvas.drawText(
                    "X5 sdk version:" + QbSdk.getTbsSdkVersion(), 10f,
                    150f,
                    paint
                )
            } else {
                infoLog("X5内核加载失败-----system core: ------X5 sdk version:${QbSdk.getTbsSdkVersion()}")
                canvas.drawText("X5内核加载失败", 10f, 50f, paint)
                canvas.drawText(
                    "system core:" ,
                    10f,
                    100f,
                    paint
                )

                canvas.drawText(
                    "X5 sdk version:" + QbSdk.getTbsSdkVersion(), 10f,
                    150f,
                    paint
                )
            }
            canvas.restore()
        }
        return ret
    }
}