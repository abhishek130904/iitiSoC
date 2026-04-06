package com.example.travel.repository

import com.example.travel.model.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
 
@Repository
interface UserRepository : JpaRepository<UserEntity, Long> {
    fun existsByNameAndAge(name: String, age: Int): Boolean
} 