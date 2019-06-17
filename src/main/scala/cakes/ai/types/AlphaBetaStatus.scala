package cakes.ai.types

import cakes.game.types.Status

final case class AlphaBetaStatus[T: Numeric](alphaBetaValues: AlphaBetaValues[T], status: Status[T])
