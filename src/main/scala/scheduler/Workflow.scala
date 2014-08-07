package scheduler

import org.joda.time.DateTime
import scala.collection._

/**
 * Created by guanguan on 7/29/14.
 */

case class JobQueue(flows: mutable.ListBuffer[Workflow] = mutable.ListBuffer[Workflow]()) {

  var pointer = 0

  def hasNext(): Boolean = pointer < (flows.length - 1)

  def add(wf: Workflow) {
    this.synchronized {
      flows.append(wf)
    }
  }

  def remove() {
    this.synchronized {
      if (flows.length > 0) flows.remove(pointer)
      if (pointer >= flows.size) pointer = 0
    }
  }

  def current(): Option[Workflow] = {
    if (flows.size == 0) None
    else Some(flows(pointer))
  }

  def insert(wf: Workflow) {
    this.synchronized {
      flows.insert(pointer + 2, wf)
    }
  }

  def skipToNext() {
    this.synchronized {
      pointer += 1
      if (pointer >= flows.length) pointer = 0
    }
  }
}

case class Workflow(
  property: String,
  startTime: DateTime,
  expires: DateTime,
  finished: mutable.Queue[Action] = mutable.Queue[Action](),
  remains: mutable.Queue[Action] = mutable.Queue[Action]()
) {

  var status: String = Workflow.STATUS_NEW

  var failedReason = ""

  def isExpired(): Boolean = expires.isBeforeNow()

  def shouldStart(): Boolean = startTime.isBeforeNow()

  def nextAction(): Option[Action] = {
    finished.enqueue(remains.dequeue())
    if (remains.length > 0) Some(remains.front) else None
  }

  def actionDone() {
    finished.enqueue(remains.dequeue())
  }

  override def toString: String = {
    val fSize = finished.size
    val rSize = remains.size
    val remain = remains.headOption.getOrElse("")
    val jStatus = if (status == Workflow.STATUS_FAILED) s"$status($failedReason)" else status
    s"""
      |================== WORKFLOW ==================
      | PROPERTY:     $property
      | START_TIME:   $startTime
      | EXPIRES:      $expires
      | JOB_STATUS:   $jStatus
      | DONE:         $fSize
      | REMAINS:      $rSize ($remain)
      |==============================================
     """.stripMargin
  }
}

object Workflow {
  val STATUS_NEW = "NEW"
  val STATUS_RUNNING = "RUNNING"
  val STATUS_SUCCESS = "SUCCESS"
  val STATUS_FAILED = "FAILED"
  val STATUS_WAITING = "WAITING"
}

case class Action(
  command: String,
  dataSets: List[String] = List(),
  output: Option[String] = None
) {

  def dataAvailable: Boolean = {
    if (dataSets.length > 0) {
      dataSets.forall { dataPath =>
        val exist = if (dataPath.endsWith("/*")) {
          S3Util.exists(dataPath.substring(0, dataPath.length - 2))
        } else{
          S3Util.exists(dataPath)
        }
        if (!exist) {
          PrintUtil("\""+dataPath+"\" not available.")
        }
        exist
      }
    } else {
      true
    }
  }

  def isDone: Boolean = {
    output match {
      case Some(op) => S3Util.exists(op)
      case None => false
    }
  }

  def execute(): Boolean = CmdUtil.run(command)

  def test(): Boolean = {
    Thread.sleep(scala.util.Random.nextInt(100))
    true
  }

  override def toString: String = {
    val idx = command.indexOf("com.petametrics.api.pipeline.") + "com.petametrics.api.pipeline.".length
    command.substring(idx)
  }
}

