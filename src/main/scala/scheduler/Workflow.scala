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
    val status = if (remains.length > 0) {
      s"""
        | JOB_STATUS:   FAILED
        | DONE:         $finished
        | REMAINS:      $remains""".stripMargin
    } else {
      """
        | JOS_STATUS:  SUCCESS""".stripMargin
    }

    s"""
      |================== WORKFLOW ==================
      | PROPERTY:     $property
      | START_TIME:   $startTime
      | EXPIRES:      $expires$status
      |==============================================
     """.stripMargin
  }
}

case class Action(
  command: String,
  dataSets: List[String]
) {

  def dataAvailable: Boolean = {
    if (dataSets.length > 0) {
      dataSets.forall { dataPath =>
        if (dataPath.endsWith("/*")) {
          S3Util.exists(dataPath.substring(0, dataPath.length - 2))
        } else{
          S3Util.exists(dataPath)
        }
      }
    } else {
      true
    }
  }

  def execute(): Boolean = CmdUtil.run(command)

  def test(): Boolean = {
    Thread.sleep(scala.util.Random.nextInt(100))
    true
  }

  override def toString: String = command
}

