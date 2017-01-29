package ro.riquack.shoppingbasket

import akka.actor.{ActorRef, ActorSystem}
import play.api.{Configuration, Environment}
import play.api.inject._
import ro.riquack.shoppingbasket.actors.ActorFactory
import ro.riquack.shoppingbasket.services.{BasketService, StoreService}

/**
  * https://www.playframework.com/documentation/2.5.x/ScalaDependencyInjection#Play-libraries
 */
class StoreModule extends Module {

  private val actorSystem = ActorSystem(ActorFactory.systemName)
  private val actorFactory = new ActorFactory(actorSystem)

  override def bindings(environment: Environment, configuration: Configuration): Seq[Binding[_]] = {
    Seq(
      bind[StoreService].toSelf,
      bind[BasketService].toSelf,
      bind[ActorFactory].toInstance(actorFactory),
      bind[ActorRef].qualifiedWith("storeActor").toInstance(actorFactory.storeActor),
      bind[ActorRef].qualifiedWith("basketActor").toInstance(actorFactory.basketActor)
    )
  }
}
