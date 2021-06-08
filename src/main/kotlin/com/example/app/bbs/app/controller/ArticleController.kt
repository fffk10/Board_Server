package com.example.app.bbs.app.controller

import com.example.app.bbs.domain.entity.Article
import com.example.app.bbs.domain.repository.ArticleRepository
import com.example.app.request.ArticleRequest
import java.util.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
class ArticleController {

    @Autowired lateinit var articleRepository: ArticleRepository

    /** 記事を1件登録する */
    @PostMapping("/")
    fun registerArticle(@ModelAttribute articleRequest: ArticleRequest): String {
        articleRepository.save(
                Article(
                        articleRequest.id,
                        articleRequest.name,
                        articleRequest.title,
                        articleRequest.contents,
                        articleRequest.articleKey,
                ))

        return "redirect:/"
    }

    /** 記事を全取得する */
    @GetMapping("/")
    fun getArticleList(model: Model): String {
        model.addAttribute("articles", articleRepository.findAll())

        return "index"
    }

    /**
     * 更新対象の記事を取得する {id} リクエストパラメータ
     * @PathVariable @GetMapping で{id}のように指定したパスを受け取ることを宣言するアノテーション 今回はid: Intでパラメータを受け取っている
     */
    @GetMapping("/edit/{id}")
    fun getArticleEdit(@PathVariable id: Int, model: Model): String {
        // IDの存在チェック
        // return if Kotlin の言語仕様
        // if 文が値を返す
        // Javaでいう三項演算子、Kotlinnには三項演算子は存在しない
        return if (articleRepository.existsById(id)) {
            model.addAttribute("article", articleRepository.findById(id))
            "edit"
        } else {
            // ありえないIDを指定された場合には画面に表示しない
            "redirect:/"
        }
    }

    /** 記事の更新処理 */
    @PostMapping("/update")
    fun updateArticle(articleRequest: ArticleRequest): String {
        // 更新対象のidが存在しない場合はindex画面にリダイレクト
        if (!articleRepository.existsById(articleRequest.id)) {
            return "redirect:/"
        }

        // 更新対象の記事を取得
        val article: Article = articleRepository.findById(articleRequest.id).get()

        // 更新対象のKEYとリクエストのKEYが一致しない場合は編集画面へ
        if (articleRequest.articleKey != article.articleKey) {
            return "redirect:/edit/${articleRequest.id}"
        }

        // リクエストで記事を更新後にindex画面に
        article.name = articleRequest.name
        article.title = articleRequest.title
        article.contents = articleRequest.contents
        article.updateAt = Date()
        articleRepository.save(article)
        return "redirect:/"
    }

    /** 削除対象の記事を取得 */
    @GetMapping("/delete/confirm/{id}")
    fun getDeleteConfirm(@PathVariable id: Int, model: Model): String {
        // 記事が存在しなければindex画面にリダイレクト
        if (!articleRepository.existsById(id)) {
            return "redirect:/"
        }

        // 存在する場合は
        model.addAttribute("article", articleRepository.findById(id).get())

        return "delete_confirm"
    }

    /** 記事の削除 */
    @PostMapping("/delete")
    fun deleteArticle(@ModelAttribute articleRequest: ArticleRequest): String {
        if (!articleRepository.existsById(articleRequest.id)) {
            return "redirect:/"
        }

        val article: Article = articleRepository.findById(articleRequest.id).get()

        if (articleRequest.articleKey != article.articleKey) {
            return "redirect:/delete/confirm/${article.id}"
        }

        articleRepository.deleteById(articleRequest.id)

        return "redirect:/"
    }
}
