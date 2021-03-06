package com.bennyhuo.qcloud.updater

import com.bennyhuo.qcloud.entities.TaskOptions
import com.bennyhuo.qcloud.entities.UploadHistory
import com.bennyhuo.qcloud.utils.logger
import java.io.File

/**
 * Created by benny on 8/13/17.
 */
class MdFileUpdater(val options: TaskOptions, val uploadHistory: UploadHistory) {

    companion object {
        private const val PATTERN = "!\\[(.*)\\]\\((.*)\\)"
    }

    fun update() {
        val mdFile = options.mdFile
        if (mdFile.isDirectory) {
            updateDirectory(mdFile)
        }else{
            updateFile(mdFile)
        }
    }

    private fun updateDirectory(dir: File) {
        dir.listFiles()?.filter { it.isDirectory || it.extension.toLowerCase() == "md" }
                ?.forEach {
                    if (it.isDirectory){
                        updateDirectory(it)
                    }else{
                        updateFile(it)
                    }
                }
    }

    private fun updateFile(file: File) {
        val parent = file.absoluteFile.parentFile
        val rootFile = if(options.mdFile.isDirectory) options.mdFile else options.mdFile.absoluteFile.parentFile
        val text = file.readText()
        val regex = Regex(PATTERN)

        val updateText = text.replace(regex){
            matchResult ->
           val result = uploadHistory[File(parent, matchResult.groupValues[2]).toRelativeString(parent)]?.let {
                "![${matchResult.groupValues[1]}](${it.remoteUrl})"
            }?: uploadHistory[File(rootFile, matchResult.groupValues[2]).toRelativeString(rootFile)]?.let{
               "![${matchResult.groupValues[1]}](${it.remoteUrl})"
           }?:matchResult.value
            logger.debug("${matchResult.value} -> $result")
            result
        }

        //update in place
        val remoteFile = file
        logger.debug("update ${file.absolutePath} -> ${remoteFile.absolutePath}")
        remoteFile.writeText(updateText)
    }

}