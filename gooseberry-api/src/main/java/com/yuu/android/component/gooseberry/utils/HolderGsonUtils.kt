package com.yuu.android.component.gooseberry.utils

import android.util.Log
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import java.io.StringReader
import java.lang.reflect.Type


/**
 * @ClassName : HolderGsonUtils
 * @Description:
 * @Author: WuZhuoyu
 * @Date: 2021/10/22 15:00
 */


object GsonUtils {
    // 日志 TAG
    private val TAG = GsonUtils::class.java.simpleName

    // Object 转 JSON 字符串
    private val TO_GSON: Gson = createGson(true).create()

    // JSON 字符串转 T Object
    val FROM_GSON: Gson = createGson(true).create()

    // JSON 缩进
    private val INDENT_GSON: Gson = createGson(true).setPrettyPrinting().create()
    /**
     * 将对象转换为 JSON String
     * @param object [Object]
     * @param gson   [Gson]
     * @return JSON String
     */
    // ============
    // = 转换方法 =
    // ============
    /**
     * 将对象转换为 JSON String
     * @param object [Object]
     * @return JSON String
     */
    @JvmOverloads
    fun <T> toJson(`object`: Any?, gson: Gson? = TO_GSON): String? {
        if (gson != null) {
            try {
                return gson.toJson(`object`)
            } catch (e: Exception) {
                Log.e(TAG,e.toString())
            }
        }
        return null
    }
    // =
    /**
     * 将 JSON String 映射为指定类型对象
     * @param json     JSON String
     * @param classOfT [Class] T
     * @param <T>      泛型
     * @return instance of type
    </T> */
    fun <T> fromJson(json: String?, classOfT: Class<T>?): T? {
        return fromJson(json, classOfT, FROM_GSON)
    }

    /**
     * 将 JSON String 映射为指定类型对象
     * @param json     JSON String
     * @param classOfT [Class] T
     * @param gson     [Gson]
     * @param <T>      泛型
     * @return instance of type
    </T> */
    fun <T> fromJson(json: String?, classOfT: Class<T>?, gson: Gson?): T? {
        if (gson != null) {
            try {
                return gson.fromJson(json, classOfT)
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }
        return null
    }
    // =
    /**
     * 将 JSON String 映射为指定类型对象
     * @param json    JSON String
     * @param typeOfT [Type] T
     * @param <T>     泛型
     * @return instance of type
    </T> */
    fun <T> fromJson(json: String, typeOfT: Type): T? {
        return fromJson(json, typeOfT, FROM_GSON)
    }

    /**
     * 将 JSON String 映射为指定类型对象
     * @param json    JSON String
     * @param typeOfT [Type] T
     * @param gson    [Gson]
     * @param <T>     泛型
     * @return instance of type
    </T> */
    private fun <T> fromJson(json: String?, typeOfT: Type?, gson: Gson?): T? {
        if (gson != null) {
            try {
                return gson.fromJson(json, typeOfT)
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }
        return null
    }
    // ============
    // = 其他方法 =
    // ============
    /**
     * 判断字符串是否 JSON 格式
     * @param json 待校验 JSON String
     * @return `true` yes, `false` no
     */
    fun isJSON(json: String?): Boolean {
        val jsonElement: JsonElement = try {
            JsonParser().parse(json)
        } catch (e: Exception) {
            return false
        } ?: return false
        return jsonElement.isJsonObject
    }
    /**
     * JSON String 缩进处理
     * @param json JSON String
     * @param gson [Gson]
     * @return JSON String
     */
    /**
     * JSON String 缩进处理
     * @param json JSON String
     * @return JSON String
     */
    @JvmOverloads
    fun toJsonIndent(json: String?, gson: Gson? = INDENT_GSON): String? {
        if (gson != null) {
            try {
                val reader: JsonReader = JsonReader(
                    StringReader(json)
                )
                reader.isLenient = true
                val jsonParser: JsonParser = JsonParser()
                val jsonElement: JsonElement = jsonParser.parse(reader)
                return gson.toJson(jsonElement)
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }
        return null
    }
    /**
     * Object 转 JSON String 并进行缩进处理
     * @param object [Object]
     * @param gson   [Gson]
     * @return JSON String
     */
    // =
    /**
     * Object 转 JSON String 并进行缩进处理
     * @param object [Object]
     * @return JSON String
     */
    @JvmOverloads
    fun toJsonIndent(`object`: Any?, gson: Gson? = INDENT_GSON): String? {
        if (gson != null) {
            try {
                return gson.toJson(`object`)
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }
        return null
    }
    // ========
    // = Gson =
    // ========
    /**
     * 创建 GsonBuilder
     * @param serializeNulls 是否序列化 null 值
     * @return [GsonBuilder]
     */
    private fun createGson(serializeNulls: Boolean): GsonBuilder {
        val builder: GsonBuilder = GsonBuilder()
            .setLenient()
            .setPrettyPrinting()    //对结果进行格式化，增加换行
            .disableHtmlEscaping()  //防止特殊字符出现乱码
        if (serializeNulls) builder.registerTypeAdapter(String::class.java,StringConverter())
        return builder
    }

    /**
     * 创建过滤指定修饰符字段 GsonBuilder
     * @param builder   [GsonBuilder]
     * @param modifiers 需过滤不处理的字段修饰符 [Modifier]
     * @return [GsonBuilder]
     */
    fun createGsonExcludeFields(
        builder: GsonBuilder?,
        vararg modifiers: Int
    ): GsonBuilder? {
        return builder?.excludeFieldsWithModifiers(*modifiers)
    }
    // ========
    // = Type =
    // ========
    /**
     * 获取 Array Type
     * @param type Bean.class
     * @return Bean[] Type
     */
    fun getArrayType(type: Type?): Type {
        return TypeToken.getArray(type).type
    }

    /**
     * 获取 List Type
     * @param type Bean.class
     * @return List<Bean> Type
    </Bean> */
    fun getListType(type: Type?): Type {
        return TypeToken.getParameterized(MutableList::class.java, type).type
    }

    /**
     * 获取 Set Type
     * @param type Bean.class
     * @return Set<Bean> Type
    </Bean> */
    fun getSetType(type: Type?): Type {
        return TypeToken.getParameterized(MutableSet::class.java, type).type
    }

    /**
     * 获取 Map Type
     * @param keyType   Key.class
     * @param valueType Value.class
     * @return Map<Bean> Type
    </Bean> */
    fun getMapType(keyType: Type?, valueType: Type?): Type {
        return TypeToken.getParameterized(
            MutableMap::class.java,
            keyType,
            valueType
        ).type
    }

    /**
     * 获取 Type
     * @param rawType       raw type
     * @param typeArguments type arguments
     * @return Type
     */
    fun getType(rawType: Type?, vararg typeArguments: Type?): Type {
        return TypeToken.getParameterized(rawType, *typeArguments).type
    }
}

class StringConverter :JsonSerializer<String>,JsonDeserializer<String>{

    override fun serialize(
        src: String?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement  = if (src == null) JsonPrimitive("") else JsonPrimitive(src.toString())

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): String = json?.asJsonPrimitive?.asString?:""
}

inline fun <reified T> String.fromJson():T?{
    return GsonUtils.fromJson<T>(this,T::class.java, GsonUtils.FROM_GSON)
}


inline fun <reified T> Any.toJson():String?{
    return GsonUtils.toJson<T>(this)
}


