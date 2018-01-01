// THIS FILE is not yet generated by "common tests"

package sodium

//import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test

import scala.collection.mutable.ListBuffer

class TestCommon {

  @Test
  def testBaseSend1(): Unit = {
    val s: StreamSink[String] = Transaction(_ => new StreamSink[String])
    val out = ListBuffer[String]()
    val l: Listener = Transaction(_ => {
      val l_ : Listener = s.listen(out.+=(_))
      l_
    })
    Transaction(_ => s.send("a"))
    Transaction(_ => s.send("b"))
    l.unlisten()
    assertEquals(List("a", "b"), out)
  }

  @Test
  def testOperationalSplit(): Unit = {
    val a: StreamSink[List[String]] = Transaction(_ => new StreamSink[List[String]])
    val b: Stream[String] = Transaction(_ => Operational.split(a))
    val b0 = ListBuffer[String]()
    val b0l: Listener = Transaction(_ => {
      val b0l_ : Listener = b.listen(b0.+=(_))
      b0l_
    })
    Transaction(_ => a.send(List("a", "b")))
    b0l.unlisten()
    assertEquals(List("a", "b"), b0)
  }

  @Test
  def testOperationalDefer1(): Unit = {
    val a: StreamSink[String] = Transaction(_ => new StreamSink[String])
    val b: Stream[String] = Transaction(_ => Operational.defer(a))
    val b0 = ListBuffer[String]()
    val b0l: Listener = Transaction(_ => {
      val b0l_ : Listener = b.listen(b0.+=(_))
      b0l_
    })
    Transaction(_ => a.send("a"))
    b0l.unlisten()
    assertEquals(List("a"), b0)
    val b1 = ListBuffer[String]()
    val b1l: Listener = Transaction(_ => {
      val b1l_ : Listener = b.listen(b1.+=(_))
      b1l_
    })
    Transaction(_ => a.send("b"))
    b1l.unlisten()
    assertEquals(List("b"), b1)
  }

  @Test
  def testOperationalDefer2(): Unit = {
    val a: StreamSink[String] = Transaction(_ => new StreamSink[String])
    val b: StreamSink[String] = Transaction(_ => new StreamSink[String])
    val c: Stream[String] = Transaction(_ => Operational.defer(a).orElse(b))
    val c0 = ListBuffer[String]()
    val c0l: Listener = Transaction(_ => {
      val c0l_ : Listener = c.listen(c0.+=(_))
      c0l_
    })
    Transaction(_ => a.send("a"))
    c0l.unlisten()
    assertEquals(List("a"), c0)
    val c1 = ListBuffer[String]()
    val c1l: Listener = Transaction(_ => {
      val c1l_ : Listener = c.listen(c1.+=(_))
      c1l_
    })
    Transaction(_ => {
      a.send("b")
      b.send("B")
    })
    c1l.unlisten()
    assertEquals(List("B", "b"), c1)
  }

  @Test
  def testStreamOrElse1(): Unit = {
    val a: StreamSink[Int] = Transaction(_ => new StreamSink[Int])
    val b: StreamSink[Int] = Transaction(_ => new StreamSink[Int])
    val c: Stream[Int] = Transaction(_ => a.orElse(b))
    val c0 = ListBuffer[Int]()
    val c0l: Listener = Transaction(_ => {
      val c0l_ : Listener = c.listen(c0.+=(_))
      c0l_
    })
    Transaction(_ => a.send(0))
    c0l.unlisten()
    assertEquals(List(0), c0)
    val c1 = ListBuffer[Int]()
    val c1l: Listener = Transaction(_ => {
      val c1l_ : Listener = c.listen(c1.+=(_))
      c1l_
    })
    Transaction(_ => b.send(10))
    c1l.unlisten()
    assertEquals(List(10), c1)
    val c2 = ListBuffer[Int]()
    val c2l: Listener = Transaction(_ => {
      val c2l_ : Listener = c.listen(c2.+=(_))
      c2l_
    })
    Transaction(_ => {
      a.send(2)
      b.send(20)
    })
    c2l.unlisten()
    assertEquals(List(2), c2)
    val c3 = ListBuffer[Int]()
    val c3l: Listener = Transaction(_ => {
      val c3l_ : Listener = c.listen(c3.+=(_))
      c3l_
    })
    Transaction(_ => b.send(30))
    c3l.unlisten()
    assertEquals(List(30), c3)
  }

  @Test
  def testOperationalDeferSimultaneous(): Unit = {
    val a: StreamSink[String] = Transaction(_ => new StreamSink[String])
    val b: StreamSink[String] = Transaction(_ => new StreamSink[String])
    val c: Stream[String] = Transaction(_ => Operational.defer(a).orElse(Operational.defer(b)))
    val c0 = ListBuffer[String]()
    val c0l: Listener = Transaction(_ => {
      val c0l_ : Listener = c.listen(c0.+=(_))
      c0l_
    })
    Transaction(_ => a.send("A"))
    c0l.unlisten()
    assertEquals(List("A"), c0)
    val c1 = ListBuffer[String]()
    val c1l: Listener = Transaction(_ => {
      val c1l_ : Listener = c.listen(c1.+=(_))
      c1l_
    })
    Transaction(_ => {
      a.send("b")
      b.send("B")
    })
    c1l.unlisten()
    assertEquals(List("b"), c1)
  }
}