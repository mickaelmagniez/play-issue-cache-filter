package controllers

import play.api.cache.AsyncCacheApi
import play.api.mvc._

import javax.inject._
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt


@Singleton
class HomeController @Inject()(
                                val controllerComponents: ControllerComponents,
                                asyncCache: AsyncCacheApi
                              ) extends BaseController {
  def index() = Action { implicit request: Request[AnyContent] =>
    println("-----")
    println(Await.result(asyncCache.get("foo"), 2.seconds))
    println("get1 ok")
    println(Await.result(asyncCache.set("foo", "bar"), 2.seconds))
    println("set ok")
    println(Await.result(asyncCache.get("foo"), 2.seconds))
    println("get2 ok")
    Ok("ok")
  }
}
