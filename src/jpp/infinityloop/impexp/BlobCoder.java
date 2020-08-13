package jpp.infinityloop.impexp;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import jpp.infinityloop.logic.Board;
import jpp.infinityloop.logic.Orient;
import jpp.infinityloop.logic.Tile;
import jpp.infinityloop.logic.TileType;

public class BlobCoder {

    public Board decode(byte[] data) {
        int width;
        int height;
        byte[] result = data;
        ByteBuffer bb;
        Tile[][] tiles;

        //check if file is infi loop file
        if (result[0] == -30 && result[1] == -120 && result[2] == -98) {
            if (result.length < 12)
                throw new IllegalArgumentException();

            //get width
            byte[] widthArr = Arrays.copyOfRange(result, 3, 7);
            bb = ByteBuffer.wrap(widthArr);
            bb.order(ByteOrder.LITTLE_ENDIAN);
            width = bb.getInt();

            //get height
            byte[] heightArr = Arrays.copyOfRange(result, 7, 11);
            bb = ByteBuffer.wrap(heightArr);
            bb.order(ByteOrder.LITTLE_ENDIAN);
            height = bb.getInt();

            //get tiles
            byte[] tileArrByte = Arrays.copyOfRange(result, 11, result.length);
            int even = width * height;
            if (even % 2 != 0)
                even++;
            if (even / 2 != tileArrByte.length)
                throw new IllegalArgumentException();

            String[] tileArrString = new String[tileArrByte.length * 2];

            //convert bytes to single digit hex in String format
            int sCount = 0;
            String temp;
            for (int i = 0; i < tileArrByte.length; i++) {
                temp = Integer.toHexString(tileArrByte[i]);
                if (temp.length() > 2) {
                    temp = temp.substring(6, 8);
                }
                if (temp.length() == 1) {
                    tileArrString[sCount] = "0";
                    sCount++;
                    tileArrString[sCount] = temp;
                    sCount++;
                } else {
                    tileArrString[sCount] = temp.substring(0, 1);
                    sCount++;
                    tileArrString[sCount] = temp.substring(1, 2);
                    sCount++;
                }
            }

            //create tile matrix
            tiles = new Tile[height][width];
            int counter = 0;
            String compare;
            for (int i = 0; i < tiles.length; i++) {
                for (int j = 0; j < tiles[0].length; j++) {
                    compare = tileArrString[counter];

                    //switch for all 16 possible combinations of element/rotation
                    switch (compare) {
                        case "0":
                            tiles[i][j] = new Tile(Orient.ALL, TileType.EMPTY);
                            break;
                        case "1":
                            tiles[i][j] = new Tile(Orient.DOWN, TileType.DEAD_END);
                            break;
                        case "2":
                            tiles[i][j] = new Tile(Orient.RIGHT, TileType.DEAD_END);
                            break;
                        case "3":
                            tiles[i][j] = new Tile(Orient.RIGHT, TileType.BEND);
                            break;
                        case "4":
                            tiles[i][j] = new Tile(Orient.UP, TileType.DEAD_END);
                            break;
                        case "5":
                            tiles[i][j] = new Tile(Orient.UP, TileType.STRAIGHT);
                            break;
                        case "6":
                            tiles[i][j] = new Tile(Orient.UP, TileType.BEND);
                            break;
                        case "7":
                            tiles[i][j] = new Tile(Orient.UP, TileType.TEE);
                            break;
                        case "8":
                            tiles[i][j] = new Tile(Orient.LEFT, TileType.DEAD_END);
                            break;
                        case "9":
                            tiles[i][j] = new Tile(Orient.DOWN, TileType.BEND);
                            break;
                        case "a":
                        case "A":
                            tiles[i][j] = new Tile(Orient.LEFT, TileType.STRAIGHT);
                            break;
                        case "b":
                        case "B":
                            tiles[i][j] = new Tile(Orient.RIGHT, TileType.TEE);
                            break;
                        case "c":
                        case "C":
                            tiles[i][j] = new Tile(Orient.LEFT, TileType.BEND);
                            break;
                        case "d":
                        case "D":
                            tiles[i][j] = new Tile(Orient.DOWN, TileType.TEE);
                            break;
                        case "e":
                        case "E":
                            tiles[i][j] = new Tile(Orient.LEFT, TileType.TEE);
                            break;
                        case "f":
                        case "F":
                            tiles[i][j] = new Tile(Orient.ALL, TileType.CROSS);
                            break;
                        default:
                            tiles[i][j] = new Tile(Orient.ALL, TileType.EMPTY);
                            break;
                    }
                    counter++;
                }
            }
        } else {
            //invalid file
            throw new IllegalArgumentException();
        }

        return new Board(width, height, tiles);
    }

    public byte[] encode(Board board) {
        Tile[][] tiles = board.getTiles();
        int size = board.getWidth() * board.getHeight();
        if (size % 2 != 0) {
            size = size + 1;
        }
        int bSize = size / 2;
        byte[] data = new byte[11 + bSize];

        //fill magic number
        data[0] = -30;
        data[1] = -120;
        data[2] = -98;

        //fill width
        ByteBuffer bbWidth = ByteBuffer.allocate(4);
        bbWidth.order(ByteOrder.LITTLE_ENDIAN);
        bbWidth.putInt(board.getWidth());
        byte[] widthArr = new byte[4];
        bbWidth.position(0);
        bbWidth.get(widthArr, 0, widthArr.length);
        System.arraycopy(widthArr, 0, data, 3, 4);

        //fill height
        ByteBuffer bbHeight = ByteBuffer.allocate(4);
        bbHeight.order(ByteOrder.LITTLE_ENDIAN);
        bbHeight.putInt(board.getHeight());
        byte[] heightArr = new byte[4];
        bbHeight.position(0);
        bbHeight.get(heightArr, 0, heightArr.length);
        System.arraycopy(heightArr, 0, data, 7, 4);

        //fill with hex
        String hex = "";
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                hex = hex + tileToHex(tiles[i][j]);
            }
        }
        if (board.getWidth() * board.getHeight() % 2 != 0) {
            hex = hex + "0";
        }
        byte[] tileArr = new byte[hex.length()];
        tileArr = hexStringToByteArray(hex);
        System.arraycopy(tileArr, 0, data, 11, bSize);

        return data;
    }

    private static String tileToHex(Tile tile) {
        String combo = tile.getTileType().toString() + tile.getOrientation().toString();
        switch (combo) {
            case "EMPTYALL":
                return "0";
            case "DEAD_ENDDOWN":
                return "1";
            case "DEAD_ENDRIGHT":
                return "2";
            case "BENDRIGHT":
                return "3";
            case "DEAD_ENDUP":
                return "4";
            case "STRAIGHTUP":
            case "STRAIGHTDOWN":
                return "5";
            case "BENDUP":
                return "6";
            case "TEEUP":
                return "7";
            case "DEAD_ENDLEFT":
                return "8";
            case "BENDDOWN":
                return "9";
            case "STRAIGHTLEFT":
            case "STRAIGHTRIGHT":
                return "a";
            case "TEERIGHT":
                return "b";
            case "BENDLEFT":
                return "c";
            case "TEEDOWN":
                return "d";
            case "TEELEFT":
                return "e";
            case "CROSSALL":
                return "f";
            default:
                return "0";
        }
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
