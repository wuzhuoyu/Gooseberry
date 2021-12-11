package com.yuuoffice.hybrid.component.event

data class X5ReadyEvent(
    val isReady:Boolean
){
    companion object{
        const val key = "X5ReadyEvent"
    }
}
