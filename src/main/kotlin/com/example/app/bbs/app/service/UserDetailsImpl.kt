package com.example.app.bbs.app.service

import com.example.app.bbs.domain.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetailsImpl(argUser: User) : UserDetails {
    val user: User = argUser

    // Role未対応の仮実装版
    // ユーザに付与された権限を返す
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        // Listオブジェクトを作成するための記述
        return mutableListOf()
    }

    // ユーザーが有効か無効かを示す
    override fun isEnabled(): Boolean {
        return true
    }

    // ユーザー名を返す
    override fun getUsername(): String {
        return user.name
    }

    // ユーザーの資格情報（パスワード）の有効期限が切れているかを示す
    // パスワードに有効期限を儲けたい時に使用する
    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    // パスワードを返す
    override fun getPassword(): String {
        return user.password
    }

    // ユーザーアカウントの有効期限が切れていないか
    override fun isAccountNonExpired(): Boolean {
        return true
    }

    // ユーザーがロックされているか
    override fun isAccountNonLocked(): Boolean {
        return true
    }
}
