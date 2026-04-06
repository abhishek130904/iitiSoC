package com.example.travel.controller

import com.example.travel.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    @GetMapping("/exists")
    fun userExistsByNameAndAge(
        @RequestParam name: String,
        @RequestParam age: Int
    ): Boolean {
        return userService.userExistsByNameAndAge(name, age)
    }
} 