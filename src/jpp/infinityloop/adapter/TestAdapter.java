package jpp.infinityloop.adapter;

import jpp.infinityloop.impexp.BlobCoder;
import jpp.infinityloop.logic.BacktrackingSolver;
import jpp.infinityloop.logic.Board;
import jpp.infinityloop.logic.Generator;

public class TestAdapter implements ITestAdapter<Board> {
    Generator generator;

    @Override
    public Board decode(byte[] data) {
        // TODO Auto-generated method stub
        BlobCoder bc = new BlobCoder();
        return bc.decode(data);
    }

    @Override
    public byte[] encode(Board board) {
        // TODO Auto-generated method stub
        BlobCoder bc = new BlobCoder();
        return bc.encode(board);
    }

    @Override
    public boolean solve(Board board) {
        // TODO Auto-generated method stub
        BacktrackingSolver bs = new BacktrackingSolver();
        return bs.solve(board);
    }

    @Override
    public void initGenerator(int minWidth, int maxWidth, int minHeight, int maxHeight) {
        // TODO Auto-generated method stub
        this.generator = new Generator(minWidth, maxWidth, minHeight, maxHeight);

    }

    @Override
    public Board generate() {
        // TODO Auto-generated method stub
        Board board = generator.generate();
        return board;
    }

    @Override
    public void rotate(Board board, int column, int row) {
        // TODO Auto-generated method stub
        board.rotate(column, row);

    }

}
