package org.app.newsreader.project.navigation

object Routes {
    const val LIST = "list"
    const val DETAIL = "detail/{articleId}"

    fun detail(articleId: Int) = "detail/$articleId"
}
