// Define the package name
package Week1

// Import necessary Akka libraries
import Week1.StreamReader.Send
import Week2.WorkerPool
import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.ThrottleMode
import akka.stream.alpakka.sse.scaladsl.EventSource
import akka.stream.scaladsl.Sink

// Import necessary Scala libraries
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

// Define the actor that reads the stream of data from the provided URI and sends it to a TextPrinter actor
class StreamReader(uriAddress: String, workerPool: ActorRef) extends Actor with ActorLogging {
  // Import necessary implicit variables
  implicit val system = context.system
  implicit val dispatcher = context.dispatcher
  implicit val requestTimeout = akka.util.Timeout(5.second)
  private val tweetRouter = workerPool

  override def receive: Receive = {
    case Send =>

      // Define a function that sends an HTTP request and returns a future HTTP response
      val request: HttpRequest => Future[HttpResponse] = Http().singleRequest(_)

      // Create an event source that connects to the specified URI and sends requests using the defined function
      val eventSource = EventSource(
        uri = uriAddress,
        send = request,
        initialLastEventId = None,
        retryDelay = 1.second
      )

      // Continuously read and process events from the stream
      while (true) {
        // Send requests at a throttled rate and take the first 30 events in the response
        val responseFuture = eventSource.throttle(1, 1.milliseconds, 1, ThrottleMode.Shaping)
          .take(30).runWith(Sink.seq)

        // Process the events in the response by sending each tweet's text to the TextPrinter actor
        responseFuture.foreach(serverSentEvent => serverSentEvent.foreach(
          event => {
            val tweetData = event.getData()
            //textPrinter ! TextPrinter.PrintTweet(tweetData)
            tweetRouter ! WorkerPool.PrintTweet(tweetData)

          }
        ))

        // Block the thread until the response future is completed to prevent busy waiting
        while (!responseFuture.isCompleted) {}
      }
  }
}

// Define the StreamReader object that contains a factory method for creating StreamReader actors
object StreamReader {
  def props(uriAddress: String, printer: ActorRef): Props = Props(new StreamReader(uriAddress, printer))
  case object Send
}
