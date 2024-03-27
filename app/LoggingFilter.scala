import akka.stream.Materializer
import play.api.Logging
import play.api.http.{DefaultHttpFilters, EnabledFilters}
import play.api.mvc._

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class LoggingFilter @Inject()(implicit val mat: Materializer, ec: ExecutionContext) extends Filter with Logging {
  def apply(nextFilter: RequestHeader => Future[Result])(requestHeader: RequestHeader): Future[Result] = {
    val startTime = System.currentTimeMillis

    nextFilter(requestHeader).map { result =>
      val endTime = System.currentTimeMillis
      val requestTime = endTime - startTime

      println(
        s">>> ${requestHeader.method} ${requestHeader.uri} took ${requestTime}ms and returned ${result.header.status}"
      )

      result.withHeaders("Request-Time" -> requestTime.toString)
    }
  }
}

class MyFilters @Inject()(
                                 defaultFilters: EnabledFilters,
                                 log: LoggingFilter
                               ) extends DefaultHttpFilters(defaultFilters.filters :+ log: _*)



