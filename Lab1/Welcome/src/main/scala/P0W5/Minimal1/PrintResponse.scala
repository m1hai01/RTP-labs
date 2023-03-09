package P0W5.Minimal1

import akka.actor.{Actor, ActorLogging, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, HttpResponse}
import akka.http.scaladsl.unmarshalling.Unmarshal
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.jsoup.Jsoup

import scala.collection.mutable.{HashMap, ListBuffer}
import scala.concurrent.Future
import scala.jdk.CollectionConverters.ListHasAsScala
import scala.util.{Failure, Success}

class PrintResponse extends Actor with ActorLogging {
  // provide the actor's execution context.
  //execute any asynchronous operations
  implicit val system = context.system
  implicit val dispatcher = context.dispatcher

  // to store the quotes extracted from the web page.
  var listQuotes = ListBuffer.empty[HashMap[String, Quote]]

  override def receive: Receive = {
    case PrintResponse.VisitPage(page) =>
      log.info("{} page is visiting", page)

      // Send an HTTP GET request to the specified URL using the Akka HTTP client library
      val responseFuture: Future[HttpResponse] = Http().singleRequest(
        HttpRequest(uri = page, method = HttpMethods.GET))

      // Handle the response from the HTTP request
      responseFuture
        .onComplete {
          case Success(res) =>
            log.info("Response: {}", res)

            // This tells Akka HTTP to unmarshal(scoata) the response entity into a String.
            Unmarshal(res.entity).to[String]
              .onComplete {
                case Success(htmlString) =>
                  log.info("Page {} was visited successfully", page)
                  log.info("Body: {}", htmlString)

                  // Parse the HTML string using the Jsoup library and extract the quotes
                  //which extracts all the quotes from the HTML.
                  val quotes = Jsoup.parse(htmlString).select("div.quote")
                  quotes.forEach(
                    quote => {
                      val quoteText = quote.select("span.text").text()
                      val quoteAuthor = quote.select("small.author").text()
                      val quoteTags = quote.select("div.tags a.tag").eachText().asScala.toList

                      // Create a new Quote object for each quote and add it to the list of quotes
                      val quoteClass = new Quote(quoteText, quoteAuthor, quoteTags)
                      val quoteMap = HashMap[String, Quote]()
                      quoteMap.put(quoteText, quoteClass)
                      listQuotes += quoteMap

                      log.info("Quote: {}", quoteClass)
                    }
                  )

                  // Convert the list of quotes to a JSON string using the Jackson library
                  val mapper = new ObjectMapper().registerModule(DefaultScalaModule);
                  val jsonString = mapper.writeValueAsString(listQuotes)
                  val jsonFile = new java.io.File("src/main/scala/P0W5/Minimal1/quotes.json")
                  mapper.writeValue(jsonFile, jsonString)

                case Failure(_) => sys.error("Error")
              }
          case Failure(_) => sys.error("Sending HTTP request was failed")
        }
  }

}

object PrintResponse {
  def props(): Props = Props(new PrintResponse)

  case class VisitPage(page: String)
}
