package conflict

import org.scalatest.FunSpec

class ShowSpec extends FunSpec {
  val Input = """
    |This is the README file.
    |<<<<<<< HEAD
    |    This line was added from the 'master' branch.
    |=======
    |   The 'test' branch version is here.
    |>>>>>>> test
    |One more line.""".stripMargin

  describe ("Show") {
    val contents = Parse(Input)
    it ("should return the git representation of the conflict") {
      assert(contents.map(Show(_)) === Some(Input))
    }
  }
}
