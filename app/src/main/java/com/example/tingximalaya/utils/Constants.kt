package com.example.tingximalaya.utils

//常量类

object Constants {

    public val INDEX_RECOMMENT = 0

    public val INDEX_SUBSCRIPT = 1

    public val INDEX_HISTORY = 2
    public //
    val INDEX_PAGE_COUNT = 3

    public //获取推荐列表的专辑数量
    val RECOMMAND_COUNT = 50
    public val COUNT_RECOMMEDN = 50
    public //热词
    val CONUT_HOT_WOED = 10

    public val DB_NAME = "ximalayadb"

    public val DB_VERSION_CODE = 1

    public //订阅的表名
    val SUB_TB_NAME = "tb_subscription"
    public val SUB_ID = "_id"
    public val SUB_COVER_URL = "coverUrl"
    public val SUB_TITLE = "title"
    public val SUB_DESCRIPTION = "description"
    public val SUB_TRACKS_COUNT = "tracksCount"
    public val SUB_PLAY_COUNT = "playCount"
    public val SUB_AUTHOR_NAME = "authorName"
    public val SUB_ALBUM_ID = "albumId"
    public //订阅最多个数
    val MAX_SUB_COUNT = 100
    public //历史记录的表名
    val HISTORY_TB_NAME = "tb_history"
    public val HISTORY_ID = "_id"
    public val HISTORY_TRACK_ID = "historyTrackId"
    public val HISTORY_TITLE = "historyTitle"
    public val HISTORY_PLAY_COUNT = "historyPlayCount"
    public val HISTORY_DURATION = "historyDuration"
    public val HISTORY_UPDATE_TIME = "historyUpdateTime"
    public val HISTORY_COVER = "historyCover"
    public val HISTORY_AUTHOR = "history_author"
    public //最大的历史记录数
    val MAX_HISTORY_COUNT = 100
}