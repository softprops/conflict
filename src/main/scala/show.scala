package conflict

trait Show {
  def apply(c: Contents): String
}

object Show {
  implicit object LikeGit extends Show {
    def apply(c: Contents) =
      c.contents.map {
        case Text(text) =>
          text
        case Conflict(Change(or, oc),
                      Change(tr, tc)) =>
          """<<<<<< %s
          |%s
          |======
          |%s
          |>>>>>> %s""".stripMargin.format(
            or, oc, tc, tr
          )
      }.mkString("")
  }
  def apply(c: Contents)(implicit s: Show) =
    s(c)
}
