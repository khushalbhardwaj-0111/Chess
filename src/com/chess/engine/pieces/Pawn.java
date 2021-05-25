package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pawn extends Piece {
	private final static int[] CANDIDATE_MOVE_COORDINATES = {8, 16};

	Pawn(final int piecePosition,final Alliance pieceAlliance) {
		super(piecePosition, pieceAlliance);
	}

	@Override
	public Collection<Move> calculateLegalMoves(Board board) {
		final List<Move> legalMoves = new ArrayList<>();
		for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {
			int candidateDestinationCoordinate = this.piecePosition + (currentCandidateOffset * this.getPieceAlliance().getDirection());
			if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) continue;
			if (currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
				// TODO: Deal with promotions
				legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
			} else if (currentCandidateOffset == 16 && this.isFirstMove() &&
					((BoardUtils.SECOND_ROW[this.piecePosition] && this.getPieceAlliance().isBlack()) ||
					(BoardUtils.SEVENTH_ROW[this.piecePosition] && this.getPieceAlliance().isWhite())) ) {
				final int behindCandidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * 8);
				if (!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() &&
						!board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
					legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
				}
			}
		}
		return ImmutableList.copyOf(legalMoves);
	}
}
