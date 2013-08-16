package conflict

sealed trait Content
case class Text(str: String) extends Content
case class Change(ref: String, content: String)
case class Conflict(ours: Change, theirs: Change) extends Content {
  def divergePoint =
    ours.content.zip(theirs.content)
      .takeWhile({ case (o, t) => o == t })
      .size - 1
}

case class Contents(contents: Seq[Content]) {
  lazy val conflicts: List[Conflict] =
    (List.empty[Conflict] /: contents) {
      case (cx, c: Conflict) => c :: cx
      case (cx, _) => cx
    }.reverse
}
