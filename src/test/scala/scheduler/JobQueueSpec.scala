package scheduler

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

    val action4 = Action(
      command = "",
      dataSets = List("new_activity/coderwall/27-07-2014/_SUCCESS")
    )

    assert(action1.dataAvailable, true)
    assert(action2.dataAvailable, true)
    assert(!action3.dataAvailable, true)
    assert(action4.dataAvailable, true)

  }
}
