package Week4

import Week4.SentimentReader.Send
import akka.actor.{Actor, ActorLogging, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.unmarshalling.Unmarshal

import scala.collection.mutable.Map
import scala.util.{Failure, Success}

// define an actor that reads sentiments from a given URI
class SentimentReader(uriAddress: String) extends Actor with ActorLogging {
  implicit val system = context.system
  implicit val dispatcher = context.dispatcher
  // create a mutable map to store the sentiments
  val mapSentiments = Map[String, Double]()

  // define the receive function for the actor
  override def receive: Receive = {
    case Send =>
      // get the sender actor for later use
      val sendTo = sender
      // get the URI to request from
      val uri = uriAddress
      // send an HTTP GET request to the URI
      val responseFuture = Http().singleRequest(HttpRequest(uri = uri))
      responseFuture
        .onComplete {
          case Success(res) =>
            // if the request succeeds, unmarshal the response entity to a string
            Unmarshal(res.entity).to[String]
              .onComplete {
                case Success(json) =>
                  // split the response string into lines
                  json.split('\n').foreach(
                    line => {
                      // split each line into an emotion and a value
                      val emotion = line.split('\t')(0)
                      val value = line.split('\t')(1)
                      // add the emotion and value to the map of sentiments
                      mapSentiments += (emotion -> value.toDouble)
                    }
                  )

                  // send the map of sentiments back to the sender actor
                  sendTo ! mapSentiments
                case Failure(_) => sys.error("something wrong")
              }
          case Failure(_) => sys.error("something wrong")
        }
  }
}

// define a companion object for the SentimentReader actor
object SentimentReader {
  // define a props method that creates a Props object for the actor
  def props(uriAddress: String): Props = Props(new SentimentReader(uriAddress))

  // define a case object to use for sending a message to the actor
  case object Send
}
