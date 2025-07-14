package com.example.travel.service

import com.example.travel.repository.UserRepository
import org.springframework.stereotype.Service
 
@Service
class UserService(private val userRepository: UserRepository) {
    fun userExistsByNameAndAge(name: String, age: Int): Boolean =
        userRepository.existsByNameAndAge(name, age)
} 