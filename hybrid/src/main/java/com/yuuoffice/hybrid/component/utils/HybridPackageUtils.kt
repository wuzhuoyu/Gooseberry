package com.yuuoffice.hybrid.component.utils

import android.app.Application
import com.yuuoffice.hybrid.component.ext.debugLog
import dalvik.system.DexFile
import java.util.*


/**
 * @ClassName : PackageUtils
 * @Description:
 * @Author: WuZhuoyu
 * @Date: 2021/8/25 16:18
 */

object HybridPackageUtils {


    fun getPackageClasses(context:Application, packagePath:String): MutableSet<String> {
        //创建一个class对象集合
        val classes: MutableSet<String> = mutableSetOf()
        try {
            //通过包管理器  获取到应用信息类然后获取到apk的完整路径
            val sourceDir: String = context.packageManager.getApplicationInfo(context.packageName, 0).sourceDir
            //根据apk的完整路径获取编译后的dex文件
            val dexFile = DexFile(sourceDir)
            //获得编译后的dex文件中的左右class
            val entries: Enumeration<String> = dexFile.entries()
            //然后进行遍历
            while (entries.hasMoreElements()) {
                //通过遍历所有的class 的包名
                val name: String = entries.nextElement()
                //判断累的包名是否符合
                if (name.contains(packagePath)) {
                    //符合添加到集合中
                    classes.add(name)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        debugLog("all classes size：${classes.size}")
        debugLog("all classes scanned：$classes")
        return classes
    }

}