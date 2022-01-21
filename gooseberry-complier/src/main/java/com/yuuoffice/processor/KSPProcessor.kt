package com.yuuoffice.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate
import com.yuuoffice.annotation.base.HybridBridgeControllerAnnotation
import com.squareup.kotlinpoet.*
import com.yuuoffice.annotation.base.HybridBridgeMessage
import com.yuuoffice.annotation.base.firstCharUpCase

class BuilderProcessor(
    val codeGenerator: CodeGenerator,
    val logger: KSPLogger
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {

        //获取所有带注释的symbol（类、方法、属性。。。）
        val symbols = resolver.getSymbolsWithAnnotation(HybridBridgeControllerAnnotation::class.java.name)
        val ret = symbols.filter { !it.validate() }.toList()

        symbols
            //过滤出类
            .filter { it is KSClassDeclaration && it.validate() }
            .forEach { it.accept(BuilderVisitor(), Unit) }
        return ret
    }


    inner class BuilderVisitor : KSVisitorVoid() {
        @KspExperimental
        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
            val controller = ClassName(classDeclaration.getPackageName(), classDeclaration.simpleName.asString())

            val className = "${classDeclaration.annotations.toList()[0].arguments[0].value.toString().firstCharUpCase()}_Helper"

            //File
            val fileSpec = FileSpec
                .builder("gooseberry", className)
                .addImport(controller,"")

            //Class
            val typeSpec = TypeSpec.objectBuilder(className)
            classDeclaration.getAllFunctions().forEach { function ->

                logger.warn("function number ${classDeclaration.getAllFunctions().toList().size}")

                function.annotations.forEach {

                    if (it.shortName.asString() == "HybridBridgeNativeApiAnnotation") {

                        val nativeApi = it.arguments[0].value.toString().removePrefix("/")

                        //function
                        val functionSpec = FunSpec.builder(nativeApi)
                            .addParameter("hybridBridgeMessage", HybridBridgeMessage::class.java)
                            .addStatement("${classDeclaration.simpleName.asString()}.${function.simpleName.asString()}(hybridBridgeMessage)")

                        typeSpec.addFunction(functionSpec.build())
                    }
                }


            }

            fileSpec.addType(typeSpec.build())
            writeFile(fileSpec.build())

        }

        override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: Unit) {

            logger.warn("function name ${function.simpleName.asString()}")
        }

        @KspExperimental
        override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: Unit) {
            super.visitPropertyDeclaration(property, data)


        }
    }

    fun writeFile(fileSpec: FileSpec) {
        val file = codeGenerator.createNewFile(
            Dependencies.ALL_FILES,
            fileSpec.packageName,
            fileSpec.name
        )
        file.use {
            val content = fileSpec.toString().toByteArray()
            it.write(content)
        }
    }


}

class BuilderProcessorProvider : SymbolProcessorProvider {
    override fun create(
        environment: SymbolProcessorEnvironment
    ): SymbolProcessor {
        return BuilderProcessor(environment.codeGenerator, environment.logger)
    }
}

fun KSClassDeclaration.getPackageName():String = this.containingFile!!.packageName.asString()

