package de.maggiwuerze.xdccloader.model;

import java.time.LocalDateTime;
import lombok.Data;
import javax.persistence.*

@Data
@Entity
class User (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long,

    @Column(nullable = false)
    var name : String,

    @Column(nullable = false)
    var creationDate : LocalDateTime

)
