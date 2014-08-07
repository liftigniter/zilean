package scheduler

import org.joda.time.DateTime
import org.scalatest.FunSuite

/**
 * Created by guanguan on 7/30/14.
 */
class JobQueueSpec extends FunSuite {

  test("JobQueue function test") {

  }


  test("Action methods test") {
    val action1 = Action(
      command = "echo '------------TESTING 1------------'",
      dataSets = List("tmp/guan/*")
    )
    val action2 = Action(
      command = "echo '------------TESTING 2------------'",
      dataSets = List("activities/wikihow/30-07-2014/*")
    )
    val action3 = Action(
      command = "echo '------------TESTING 3------------'",
      dataSets = List("tmp/guan/READMEssss1.md")
    )

    val fmt = org.joda.time.format.DateTimeFormat.forPattern("dd-MM-YYYY").withZone(org.joda.time.DateTimeZone.forID("US/Pacific"))

    val actConf = ActionConfig("echo", Some("dashboard/daily/about/{-6,0}/overview_stats_1d.csv"), Some("dashboard/daily/about/{DATE}/overview_stats_1d.csv"))

    val action4 = actConf.toAction(fmt.parseDateTime("04-08-2014"))

    println(action4.dataSets)

    assert(action1.dataAvailable, true)
    assert(action2.dataAvailable, true)
    assert(!action3.dataAvailable, true)
    assert(action4.dataAvailable, true)
    assert(action4.isDone, true)



  }
}
