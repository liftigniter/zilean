package scheduler

import akka.actor.{Actor, ActorSystem, Props}
import org.json4s._
import org.json4s.jackson.JsonMethods._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits._


// Message
case object RunJob
case object Ticker
case object CreateJob


object Constants {
  val RUNNING = "running"
  val DONE = "done"
  val WAITING = "waiting"
}

class ExecutorActor extends Actor with akka.actor.ActorLogging {

  def receive = {
    case Ticker => print(".")
    case RunJob => {
      println()
      log.info("Job queue size: " + Global.jobQueue.flows.size)
      Global.jobQueue.current() match {
        case Some(wf) => {
          log.info(wf.property)
          // 0 = OK
          // 1 = FAILED
          // 2 = PENDING (data not available yet)
          var status = 0

          log.info("Should start? " + wf.shouldStart())
          if (!wf.shouldStart) {
            status = 2
          }
          log.info("Expired? " + wf.isExpired())
          if (wf.isExpired) {
            status = 1
            log.error("JOB FAILED: Expired")
          }
          while(status == 0 && wf.remains.size > 0) {
            if (wf.remains.front.dataAvailable) {
              log.info("Execute: " + wf.remains.front.command)
              val result = wf.remains.front.execute()
              if (result) {
                wf.actionDone()
                log.info("Remaining actions: " + wf.remains.size)
              } else {
                status = 1
              }
            } else {
              log.info("Data not available!!")
              status = 2
            }
          }
          log.info(wf.toString)
          // If workflow success or failed, remove from queue, otherwise pending it and move to next workflow in the queue
          if (status == 0 || status == 1) Global.jobQueue.remove()
          else Global.jobQueue.skipToNext()
          // Send itself a message to run next job
          if (status == 0) self ! RunJob
        }
        case None => print("X")
      }

    }
  }


}


class JobManagerActor extends Actor with akka.actor.ActorLogging {
  override def receive = {
    case CreateJob => {
      Global.workflows.foreach { wf =>
        wf.toWorkflow match {
          case Some(w) => {
            print("+")
            Global.jobQueue.add(w.copy())
          }
          case None => print("_")
        }
      }
    }
  }
}

object Global {
  implicit val formats = DefaultFormats

  lazy val json = parse(StreamInput(getClass().getResourceAsStream("/workflow.json")))

  // Global workflow configs
  lazy val workflows = json.extract[List[WorkflowConfig]]

  // Global job queue
  lazy val jobQueue = JobQueue()

}

object AkkaScheduler extends App {

  lazy val fmt = org.joda.time.format.DateTimeFormat.forPattern("dd-MM-YYYY kkmm").withZone(org.joda.time.DateTimeZone.forID("US/Pacific"))

  override def main(args: Array[String]) {

    println("Start akka job scheduler...")

    val system = ActorSystem("akka-scheduler")

    val executor = system.actorOf(Props[ExecutorActor], "JobExecutor")

    val manager = system.actorOf(Props[JobManagerActor], "JobManager")

    // Every 5 seconds send a ticker, and print a dot '.' on the screen
    system.scheduler.schedule(0.seconds, 5.seconds, executor, Ticker)

    // Every 10 minutes send a RunJob message, actor run a job if available
    system.scheduler.schedule(30.seconds, 1.minutes, executor, RunJob)

    // Every 10 minutes send a CreateJob message, actor create a job if possible
    system.scheduler.schedule(10.seconds, 2.minutes, manager, CreateJob)

    // Every hour check and add new workflow to queue
  }



}
