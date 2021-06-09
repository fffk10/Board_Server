package com.example.app.bbs.config

import com.example.app.bbs.app.service.UserDetailsServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@Configuration
@EnableWebSecurity
class BbsAdminWebSecurityConfig : WebSecurityConfigurerAdapter() {
    @Autowired lateinit var userDetailsService: UserDetailsServiceImpl

    /** PasswordEncoder パスワードを安全に処理するためにハッシュ化する仕組みになっている */
    @Bean
    fun passwordEncorder(): PasswordEncoder {
        // ハッシュ化の仕組みで使用
        return BCryptPasswordEncoder()
    }

    /** WebSecurity 公式ドキュメントではWebベースのセキュリティを追加できると記載があるが 実際にはセキュリティをかける必要がないものに対して除外する設定を用いる */
    @Override
    override fun configure(web: WebSecurity) {
        // ここに設定したものはセキュリティ設定を無視
        web.ignoring().antMatchers("/favicon.ico", "/css/**", "/js/**")
    }

    @Override
    override fun configure(http: HttpSecurity) {
        // 許可の設定
        // /admin と /admin/ 配下は認証が必要
        // それ以外は全てアクセス許可
        http.authorizeRequests()
                .antMatchers("/admin", "/admin/*")
                .authenticated()
                .anyRequest()
                .permitAll()

        // ログイン設定（デフォルト設定を使用）
        http.formLogin()

        // ログアウト
        http.logout()
                // Springのログアウト処理はデフォルトでPOSTで行う
                // GETで行う場合は以下記述が必要
                .logoutRequestMatcher(AntPathRequestMatcher("/user/logout**"))
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
    }

    @Override
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService)
        // 以下の処理を上記で書き換え

        // // 認証をメモリで行うこと示す
        // auth.inMemoryAuthentication().withUser("admin")
        // .password("$2a$10$2ztOZPIWw1XVPpMnniPCienSggo9rgKTvLVdaRK4.uDEcjE05CRZ.")
        // .authorities("ROLE_ADMIN")
    }
}
