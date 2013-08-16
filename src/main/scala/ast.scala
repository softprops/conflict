package conflict

sealed trait Content
case class Text(str: String) extends Content
case class Change(ref: String, content: String)
case class Conflict(ours: Change, theirs: Change) extends Content {
  def divergePoint =
    ours.content.zip(theirs.content).indexWhere { case (a, b) => a != b }
}

case class Contents(contents: Seq[Content]) {
  lazy val conflicts: Seq[Conflict] =
    contents.collect { case c: Conflict => c }
}
