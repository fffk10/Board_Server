package com.example.app.bbs.domain.entity

import java.util.*
import javax.persistence.*

/**
 * @Column クラスのプロパティ名とテーブルのフィールド名が違う時に使う
 * Springのフレームワーク的にはプロパティをキャメルケースで書きたい
 * テーブルはスネークケースで書きたいのでその差をアノテーションで埋める
 * Springでソートを指定する際にスネークケースでは動作しない
 */
@Entity
data class Article(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Int = 0,
        var name: String = "",
        var title: String = "",
        var contents: String = "",
        @Column(name = "article_key") var articleKey: String = "",
        @Column(name = "register_at") var registerAt: Date = Date(),
        @Column(name = "update_at") var updateAt: Date = Date()
)
