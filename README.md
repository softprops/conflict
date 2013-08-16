# conflict

A git merge conflict parser

## usage

### Parsing contents

Git has a pretty well defined format for representing merge conflicts in 
textual format.

```
This is the README file.
<<<<<<< HEAD
    This line was added from the 'master' branch.
=======
   The 'test' branch version is here.
>>>>>>> test
One more line.
```

You can parse these with `conflict.Parse(conflictStr)` which returns `Option[Contents]`

`Contents` resents the conflicting file's contents. `Contents` stores a sequence of `Content` which may either be `Text` or a `Conflict`.

A `Conflict` stores a reference to our `Change` and their `Change` including a ref name and contents of the change.

```scala
conflict.ours.contents
conflict.theirs.contents
```

A `Conflict` also exposes the index at which the two contents diverge as `Conflict#divergePoint`. This is exposed as a starting point for extra tooling. When scanning a merge conflict by eye, this point is most likely the first thing you want to find in order to identify what changed. As such, it's included as part of the `Conflict` interface.

### Showing contents

This library also provides an type class for showing the contents of a merge conflict called `Show` which takes the `Contents` of a merge conflict and returns a `String`.

By default `conflict.Show(contents)` will return the same representation as git shows you.

```scala
conflict.Show(conflict)
```

Doug Tangren (softprops) 2013
