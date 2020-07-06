package com.example

import cats.effect.ConcurrentEffect
import fs2.Stream


object join {

    def parallelize[F[_] : ConcurrentEffect, I, O](elements: List[I],
                                                   streamF: I => Stream[F, O]): Stream[F, O] =
    
      Stream.emits(elements).map(streamF).parJoinUnbounded

}
