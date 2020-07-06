package com.example

import com.example.join._

import cats.Applicative
import cats.data.EitherT
import cats.effect.{ConcurrentEffect, ContextShift, IO, Timer}
import fs2.Stream
import fs2.Stream.Compiler
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt


class JoinTest extends AnyFlatSpec with Matchers {

  type EF[A] = EitherT[IO, Throwable, A]

  case class IntThrowable(int: Int) extends Throwable

  implicit val contextShift: ContextShift[IO] = IO.contextShift(global)
  implicit val timer: Timer[IO] = IO.timer(global)

  "parallelize" should "not block while parallelizing a stream of streams using IO as context" in {

    def f(int: Int): Stream[IO, String] = Stream(int.toString)

    parallelize(List(1, 2, 3), f)
      .compile.toList.unsafeRunSync should contain allElementsOf List("1", "2", "3")

  }

  it should "not block while parallelizing a stream of streams using EitherT[IO, Throwable, *] as context" in {

    def f(int: Int): Stream[EF, String] = Stream(int.toString)

    parallelize(List(1, 2, 3), f)
      .compile.toList.value.unsafeRunTimed(5.seconds) match {
    
      case Some(Right(list)) => list should contain allElementsOf List("1", "2", "3")
      case other             => fail(s"'$other' was not the expected result.")

    }

  }

  it should "not block while parallelizing a stream of streams using EitherT[IO, Throwable, *] as context if context is a Left" in {

    def f(int: Int): Stream[EF, String] =
      if (int % 2 != 0) Stream(int.toString) else Stream.eval[EF, String](EitherT.leftT(IntThrowable(int)))

    parallelize(List(1, 2, 3), f)
      .compile.toList.value.unsafeRunTimed(5.seconds) match {
    
      case Some(Left(IntThrowable(int))) => int shouldBe 2
      case other                         => fail(s"'$other' was not the expected result.")

    }

  }

  "sequential" should "not block while parallelizing a stream of streams using IO as context" in {

    def f(int: Int): Stream[IO, String] = Stream(int.toString)

    sequential(List(1, 2, 3), f)
      .compile.toList.unsafeRunSync should contain theSameElementsInOrderAs List("1", "2", "3")

  }

  it should "not block while parallelizing a stream of streams using EitherT[IO, Throwable, *] as context" in {

    def f(int: Int): Stream[EF, String] = Stream(int.toString)

    sequential(List(1, 2, 3), f)
      .compile.toList.value.unsafeRunTimed(5.seconds) match {
    
      case Some(Right(list)) => list should contain theSameElementsInOrderAs List("1", "2", "3")
      case other             => fail(s"'$other' was not the expected result.")

    }

  }

  it should "not block while parallelizing a stream of streams using EitherT[IO, Throwable, *] as context if context is a Left" in {

    def f(int: Int): Stream[EF, String] =
      if (int % 2 != 0) Stream(int.toString) else Stream.eval[EF, String](EitherT.leftT(IntThrowable(int)))

    sequential(List(1, 2, 3), f)
      .compile.toList.value.unsafeRunTimed(5.seconds) match {
    
      case Some(Left(IntThrowable(int))) => int shouldBe 2
      case other                         => fail(s"'$other' was not the expected result.")

    }

  }

}
