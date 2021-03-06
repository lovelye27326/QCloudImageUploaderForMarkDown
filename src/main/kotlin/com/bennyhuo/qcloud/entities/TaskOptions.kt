package com.bennyhuo.qcloud.entities

import com.bennyhuo.qcloud.prop.AbsProperties
import java.io.File

/**
 * Created by benny on 8/12/17.
 */
class TaskOptions(val file: File, val appInfo: AppInfo, val mdFile: File, val removeAfterUploading: Boolean)

class AppInfo(path: String): AbsProperties(path){
    var APP_ID: Long by prop
    var APP_SECRET_ID: String by prop
    var APP_SECRET_KEY: String by prop
    var BUCKET: String by prop
    var REGION: String by prop
}
