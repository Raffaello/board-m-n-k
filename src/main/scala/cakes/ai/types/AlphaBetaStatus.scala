package cakes.ai.types

import game.types.Status

final case class AlphaBetaStatus[T: Numeric](alphaBetaValues: AlphaBetaValues[T], status: Status[T])
