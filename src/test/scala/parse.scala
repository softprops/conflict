package conflict

import org.scalatest.FunSpec

class ParseSpec extends FunSpec {
  val Input = """
    |This is the README file.
    |<<<<<<< HEAD
    |    This line was added from the 'master' branch.
    |=======
    |   The 'test' branch version is here.
    |>>>>>>> test
    |One more line.""".stripMargin

  describe ("Parse") {
    val contents = Parse(Input)
    it ("should return conflicts") {
      contents.map { c => assert(c.conflicts.size === 1) }
    }
    it ("should capture refs names") {
      contents.map {
        _.conflicts.headOption.map { conflict =>
          assert(conflict.ours.ref === "HEAD")
          assert(conflict.theirs.ref === "test")
          assert(conflict.divergePoint === 2)
        }
      }
    }
  }
}

