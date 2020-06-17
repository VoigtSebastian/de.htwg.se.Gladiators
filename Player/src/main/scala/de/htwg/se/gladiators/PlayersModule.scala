package de.htwg.se.gladiators.playerModule
import com.google.inject.AbstractModule
import com.google.inject.name.Names
import de.htwg.se.gladiators.database.PlayerDatabase
import de.htwg.se.gladiators.database.relational.SlickDatabase
import de.htwg.se.gladiators.database.document.MongoDb
import net.codingwell.scalaguice.ScalaModule

class PlayersModule extends AbstractModule with ScalaModule {
    override def configure(): Unit = {
        bind[PlayerDatabase].to[SlickDatabase]
    }
}

