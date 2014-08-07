package scheduler

import org.joda.time.DateTime



/**
 * Created by guanguan on 7/31/14.
 */

case class WorkflowConfig(property: String, start: String, end: String, repeat: Int, expires: Int, actions: List[ActionConfig]) {
  var now = start

  def toWorkflow(): Option[Workflow] = {
    val fmt = org.joda.time.format.DateTimeFormat.forPattern("dd-MM-YYYY kkmm").withZone(org.joda.time.DateTimeZone.forID("US/Pacific"))
    val startTime = fmt.parseDateTime(now)

    if (startTime.minusHours(3).isAfterNow) None
    else {
      val expTime = if (startTime.isBeforeNow) DateTime.now.plusMinutes(expires) else startTime.plusMinutes(expires)
      val acts = scala.collection.mutable.Queue[Action]()
      actions.foreach(x => acts.enqueue(x.toAction(startTime)))

      now = fmt.print(startTime.plusMinutes(repeat))
      Some(Workflow(property = property, startTime = startTime, expires = expTime, remains = acts).copy())
    }
  }
}

case class ActionConfig(command: String, datasets: Option[String], output: Option[String] = None) {
  def toAction(date: DateTime): Action = {
    val fmt = org.joda.time.format.DateTimeFormat.forPattern("dd-MM-YYYY").withZone(org.joda.time.DateTimeZone.forID("US/Pacific"))
    val Ptrn1 = """[^\{]+\{DATE\}[^\}]+""".r
    val Ptrn2 = """[^\{]+\{([0-9,-]+)\}[^\}]+""".r
    val ReplacePtrn = """\{[0-9,-]+\}""".r

    val cmd = command.replace("{DATE}", fmt.print(date))
    val op = output.map(_.replace("{DATE}", fmt.print(date)))

    datasets match {
      case Some(ds) =>  {
          ds match {
            case Ptrn1() => {  // e.g. activities/wikihow/{DATE}/*
              val dataPath = ds.replace("{DATE}", fmt.print(date))
              Action(cmd, List(dataPath), op)
            }
            case Ptrn2(m) => {  // e.g. activities/wikihow/{-6,0}/*
              val start = m.split(",")(0).toInt
              val end = m.split(",")(1).toInt
              val dataPath = (start to end).map { i =>
                ReplacePtrn.replaceAllIn(ds, fmt.print(date.plusDays(i)))
              }.toList
              Action(cmd, dataPath, op)
            }
            case _ => Action(cmd, List(ds), op)
          }
      }
      case None => Action(cmd, List(), op)
    }
  }
}
