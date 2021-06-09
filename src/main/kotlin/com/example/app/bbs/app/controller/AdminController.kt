package com.example.app.bbs.app.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

/**
 * 管理者画面
 */
@Controller
class AdminController {

    @GetMapping("/admin/index")
    fun getAdminIndex(): String {
        return "admin_index"
    }
}