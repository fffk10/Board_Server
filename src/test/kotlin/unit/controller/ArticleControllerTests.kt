package com.example.app.bbs.unit.controller

import com.example.app.bbs.app.controller.ArticleController
import com.example.app.bbs.domain.entity.Article
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest
class ArticleControllerTests {
    lateinit var mockMvc: MockMvc

    @Autowired lateinit var target: ArticleController

    @Before
    fun setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(target).build()
    }

    /** 記事1件登録のテスト */
    @Test
    fun registerArticleTest() {
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/")
                                .param("name", "test")
                                .param("title", "test")
                                .param("contents", "test")
                                .param("articleKey", "test"))
                .andExpect(status().is3xxRedirection()) // 正しくリダイレクトを促すレスポンスコード
                .andExpect(view().name("redirect:/")) // リダイレクト先が指定されているか
    }

    /** 記事全取得のテスト */
    fun getArticleListTest() {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk)
                .andExpect(model().attributeExists("articles")) // modelにセットした内容をテスト、中身の検証は行わない
                .andExpect(view().name("index"))
    }

    /** 更新対象の記事取得テスト（非正常系） DBに記事が存在しない場合 */
    @Test
    fun getArticleEditNotExistsIdTest() {
        mockMvc.perform(MockMvcRequestBuilders.get("/edit/" + 0))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"))
    }

    /** 更新対象の記事取得テスト（正常系） DBに記事存在する場合 */
    @Test
    @Sql(
            statements =
                    [
                            "INSERT INTO article (name, title, contents, article_key)" +
                                    " VALUES ('test', 'test', 'test', 'test');"])
    fun getArticleEditExistsIdTest() {
        val latestArticle: Article = target.articleRepository.findAll().last()
        mockMvc.perform(MockMvcRequestBuilders.get("/edit/" + latestArticle.id))
                .andExpect(status().isOk)
                .andExpect(view().name("edit"))
    }

    /** 記事更新のテスト（非正常系） 存在しないIDをリクエストに送った場合 */
    @Test
    fun updateArticleNotExistsArticleTest() {
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/update")
                                .param("id", "0")
                                .param("name", "test")
                                .param("title", "test")
                                .param("contents", "test")
                                .param("articleKey", "err."))
                .andExpect(status().is3xxRedirection)
                .andExpect(view().name("redirect:/"))
    }

    /** 記事更新のテスト（非正常系） 投稿KEYが間違っていた場合 */
    @Test
    @Sql(
            statements =
                    [
                            "INSERT INTO article (name, title, contents article_key," +
                                    " register_at, update_at) VALUES ('test', 'test', 'test', 'test', now(), now());"])
    fun updateArticleNotMatchArticleKeyTest() {
        val latestArticle: Article = target.articleRepository.findAll().last()

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/update")
                                .param("id", latestArticle.id.toString())
                                .param("name", latestArticle.name)
                                .param("title", latestArticle.title)
                                .param("contents", latestArticle.contents)
                                .param("articleKey", "err."))
                .andExpect(status().is3xxRedirection)
                .andExpect(view().name("redirect:/edit/${latestArticle.id.toString()}"))
    }

    /** 記事更新のテスト（正常系） 正常処理 */
    @Test
    @Sql(
            statements =
                    [
                            "INSERT INTO article (name, title, contents, article_key," +
                                    " register_at, update_at) VALUES ('test', 'test', 'test', 'test', now(), now());"])
    fun updateArticleExistsArticleTest() {
        val latestArticle: Article = target.articleRepository.findAll().last()

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/update")
                                .param("id", latestArticle.id.toString())
                                .param("name", latestArticle.name)
                                .param("title", latestArticle.title)
                                .param("contents", latestArticle.contents)
                                .param("articleKey", latestArticle.articleKey))
                .andExpect(status().is3xxRedirection)
                .andExpect(view().name("redirect:/"))
    }

    /** 記事削除のテスト （非正常系） 削除対象の記事が存在しない */
    @Test
    fun getDeleteConfirmNotExistsIdTest() {
        mockMvc.perform(MockMvcRequestBuilders.get("/delete/confirm/0"))
                .andExpect(status().is3xxRedirection)
                .andExpect(view().name("redirect:/"))
    }

    /** 記事削除のテスト （正常系） 削除対象の記事が存在する */
    @Test
    @Sql(
            statements =
                    [
                            "INSERT INTO article (name, title, contents, articleKey)" +
                                    " VALUES ('test', 'test', 'test', 'test');"])
    fun getDeleteConfirmExistsIdTest() {
        val latestArticle: Article = target.articleRepository.findAll().last()
        mockMvc.perform(
                        MockMvcRequestBuilders.get(
                                "/delete/confirm/${latestArticle.id.toString()}"))
                .andExpect(status().isOk)
                .andExpect(view().name("delete_confirm"))
    }
}
