package com.example.app.bbs.domain.repository

import com.example.app.bbs.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository

/**
 * この実装だけで対応するフィールドを検索する仕組みが実装できる
 * JPAの仕組みで実現できる
 * 必要な文でSELECT文を書く必要がない
 */
interface UserRepository: JpaRepository<User, Int> {
    fun findByName(name: String): User?
}