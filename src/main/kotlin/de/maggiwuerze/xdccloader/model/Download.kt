package de.maggiwuerze.xdccloader.model;

import de.maggiwuerze.xdccloader.util.State
import java.time.LocalDateTime;
import lombok.Data;
import javax.persistence.*

@Data
@Entity
class Download (

    @ManyToOne
    @JoinColumn(name="CHANNEL_ID")
    var channel : Channel,

    @ManyToOne
    @JoinColumn(name="SERVER_ID")
    var server : Server,

    @Column(nullable = false)
    var date : LocalDateTime,

    @Column(nullable = false)
    var progress : Long,

    @Column(nullable = false)
    var filename : String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status : State

){

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

}
