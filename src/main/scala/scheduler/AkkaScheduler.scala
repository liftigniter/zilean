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
case object CleanUp


class ExecutorActor extends Actor with akka.actor.ActorLogging {

  def receive = {
    case Ticker => print(".")
    case RunJob => {
      println()
      log.info("Job queue size: " + Global.jobQueue.flows.size)
      Global.jobQueue.current() match {
        case Some(wf) => {

          wf.status = Workflow.STATUS_RUNNING

          log.info("Should start? " + wf.shouldStart())
          if (!wf.shouldStart) {
            wf.status = Workflow.STATUS_WAITING
          }
          log.info("Expired? " + wf.isExpired())
          if (wf.isExpired) {
            wf.status = Workflow.STATUS_FAILED
            wf.failedReason = "Expired on " + wf.expires
          }
          while(wf.status == Workflow.STATUS_RUNNING && wf.remains.size > 0) {
            if (wf.remains.front.dataAvailable) {
              val result = wf.remains.front.execute()
              if (result) {
                wf.actionDone()
              } else {
                wf.status = Workflow.STATUS_FAILED
                wf.failedReason = "Execute \"" + wf.remains.front.toString + "\" failed"
              }
            } else {
              log.info("Data not available!!")
              wf.status = Workflow.STATUS_WAITING
            }
          }

          if (wf.status == Workflow.STATUS_RUNNING) {
            wf.status = Workflow.STATUS_SUCCESS
          }

          if (wf.status == Workflow.STATUS_FAILED || wf.status == Workflow.STATUS_SUCCESS) {
            log.info(wf.toString)
            Global.jobQueue.remove()
            self ! RunJob
          } else {
            Global.jobQueue.skipToNext()
          }

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
    case CleanUp => {
      val actions = scala.collection.mutable.Queue[Action]()
      scala.io.Source.fromFile("/root/spark-ec2/slaves").getLines().foreach { line =>
        val host = line.trim
        val cmd = s"ssh $host rm -rf /root/spark/work/*"
        val act = Action(cmd, List())
        actions.enqueue(act)
      }
      val wf = Workflow(property =  "cleanup",
        startTime = org.joda.time.DateTime.now.minusMinutes(10),
        expires = org.joda.time.DateTime.now.plusHours(1),
        remains = actions
      )
      Global.jobQueue.insert(wf)
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

  override def main(args: Array[String]) {

    println("Start akka job scheduler...")

    val system = ActorSystem("akka-scheduler")

    val executor = system.actorOf(Props[ExecutorActor], "JobExecutor")

    val manager = system.actorOf(Props[JobManagerActor], "JobManager")

    // Every 5 seconds send a ticker, and print a dot '.' on the screen
    system.scheduler.schedule(0.seconds, 5.seconds, executor, Ticker)

    // Every 1 minute send a RunJob message, actor run a job if available
    system.scheduler.schedule(30.seconds, 1.minutes, executor, RunJob)

    // Every 10 minutes send a CreateJob message, actor create a job if possible
    system.scheduler.schedule(10.seconds, 2.minutes, manager, CreateJob)

    // Every 1 hour, clean up slave work log
    system.scheduler.schedule(1.hour, 1.hour, manager, CleanUp)

  }


}
