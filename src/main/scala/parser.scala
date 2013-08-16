package conflict

import scala.util.parsing.combinator.RegexParsers

object Tokens {
  val Div = "\r?\n=======\r?\n".r
  val Ours = "<<<<<<<"
  val Theirs = ">>>>>>>"
  val Head = "HEAD"
  val OursPrefix = "%s %s\r?\n".format(Ours, Head).r
  val TheirsPrefix = ("\r?\n" + Theirs + " ").r
}

class Parser extends RegexParsers {
  import Tokens._

  override def skipWhitespace = false

  def contents: Parser[Seq[Content]] =
    (conflict | anythingBut(conflict)).*

  def anythingBut[T](p: Parser[T]): Parser[Text] =
    (guard(p) ^^ { _ => Text("") }
     | rep1(not(p) ~> any) ^^ {
       case xs => Text(xs.mkString(""))
     })

  def conflict: Parser[Conflict] =
    ours ~ (Div ~> theirs) ^^ {
      case o ~ t => Conflict(o, t)
    }

  def ours: Parser[Change] =
    (OursPrefix) ~> anythingBut(Div) ^^ {
      case Text(content) => Change(Head, content)
    }

  def theirs: Parser[Change] =
    anythingBut(TheirsPrefix) ~ (TheirsPrefix ~> ref) ^^ {
      case Text(content) ~ ref => Change(ref, content)
    }

  def ref: Parser[String] = """[0-9A-Za-z-_.]+|[.]""".r

  def any: Parser[String] = """.|(\r?\n)+""".r

  def apply(in: String) = parseAll(contents, in)
}

object Parse {
  def apply(in: String): Option[Contents] =
    new Parser()(in) match {
      case s if (s.successful) =>
        Some(Contents(s.get))
      case _ =>
        None
    }
}
