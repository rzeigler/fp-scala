import cats._
import cats.data._
import cats.implicits._

object FunctorOverview extends App {
  val add1: Function1[Int, Int] = (x: Int) => x + 1

  // Functors are all about lifting functions into some kind of boxed context
  // What is the common interface of all of these
  println("a list:" + Functor[List].fmap(List(1, 2, 3))(add1))

  println("a vector: " + Functor[Vector].fmap(Vector(1, 2, 3))(add1))

  println("an option: " + Functor[Option].fmap(Some(1))(add1))

  type FnInt[A] = Function1[Int, A]
  println("a function????: " + Functor[FnInt].fmap(add1)(add1)(1)) // = 3

  println("functors compose with eachother")
  val listVectorOption = Functor[List].compose(Functor[Vector]).compose(Functor[Option])
  println("a List[Vector[Option[_]]] " + listVectorOption.fmap(List(Vector(Some(1), Some(3)), Vector(None, Some(6))))(add1))

  println("the functor laws have performance implications")
  def time[A](name: String)(a: => A): A = {
    val start = System.currentTimeMillis
    val result = a
    val end = System.currentTimeMillis
    println(s"$name took ${end - start}")
    result
  }

  val large = (0 to 5000000).toVector

  println("why is this slow?")
  time("sequential")(
    large
    .map(add1)
    .map(add1)
    .map(add1)
    .map(add1)
    .map(add1)
  )
  time("composed")(
    large
    .map(
      add1
      .andThen(add1)
      .andThen(add1)
      .andThen(add1)
      .andThen(add1)
    )
  )
}
