package de.maggiwuerze.xdccloader.model;

import java.time.LocalDateTime;
import lombok.Data;
import javax.persistence.*

@Data
@Entity
class User (

    @Column(nullable = false, unique = true)
    var name : String,

    @Column(nullable = false, unique = true)
    var password : String,

    @Column(nullable = false)
    var creationDate : LocalDateTime

){

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

}
