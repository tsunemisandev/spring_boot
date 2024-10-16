package com.example.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "post")
class Post {
    @Id
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @Column(length = 100)
    var title:String? = null
}