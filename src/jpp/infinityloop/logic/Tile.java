package jpp.infinityloop.logic;

public class Tile {
    private Orient orient;
    private TileType tType;

    public Tile(Orient orient, TileType tType) {
        this.orient = orient;
        this.tType = tType;
    }

    public void rotate() {
        Orient back;
        String check = this.orient.toString();

        switch (check) {
            case "UP":
                back = Orient.RIGHT;
                break;
            case "RIGHT":
                back = Orient.DOWN;
                break;
            case "DOWN":
                back = Orient.LEFT;
                break;
            case "LEFT":
                back = Orient.UP;
                break;
            default:
                back = Orient.ALL;
        }
        this.orient = back;
    }

    public Orient getOrientation() {
        return this.orient;
    }

    public TileType getTileType() {
        return this.tType;
    }

    public boolean hasUpCon() {
        String orient = this.getOrientation().toString();
        String tType = this.getTileType().toString();
        String check = tType + orient;

        switch (check) {
            case "EMPTYALL":
                return false;
            case "DEAD_ENDDOWN":
                return false;
            case "DEAD_ENDRIGHT":
                return false;
            case "BENDRIGHT":
                return false;
            case "DEAD_ENDUP":
                return true;
            case "STRAIGHTUP":
            case "STRAIGHTDOWN":
                return true;
            case "BENDUP":
                return true;
            case "TEEUP":
                return true;
            case "DEAD_ENDLEFT":
                return false;
            case "BENDDOWN":
                return false;
            case "STRAIGHTLEFT":
            case "STRAIGHTRIGHT":
                return false;
            case "TEERIGHT":
                return false;
            case "BENDLEFT":
                return true;
            case "TEEDOWN":
                return true;
            case "TEELEFT":
                return true;
            case "CROSSALL":
                return true;
            default:
                return false;
        }
    }

    public boolean hasRightCon() {
        String orient = this.getOrientation().toString();
        String tType = this.getTileType().toString();
        String check = tType + orient;

        switch (check) {
            case "EMPTYALL":
                return false;
            case "DEAD_ENDDOWN":
                return false;
            case "DEAD_ENDRIGHT":
                return true;
            case "BENDRIGHT":
                return true;
            case "DEAD_ENDUP":
                return false;
            case "STRAIGHTUP":
            case "STRAIGHTDOWN":
                return false;
            case "BENDUP":
                return true;
            case "TEEUP":
                return true;
            case "DEAD_ENDLEFT":
                return false;
            case "BENDDOWN":
                return false;
            case "STRAIGHTLEFT":
            case "STRAIGHTRIGHT":
                return true;
            case "TEERIGHT":
                return true;
            case "BENDLEFT":
                return false;
            case "TEEDOWN":
                return false;
            case "TEELEFT":
                return true;
            case "CROSSALL":
                return true;
            default:
                return false;
        }
    }

    public boolean hasLeftCon() {
        String orient = this.getOrientation().toString();
        String tType = this.getTileType().toString();
        String check = tType + orient;

        switch (check) {
            case "EMPTYALL":
                return false;
            case "DEAD_ENDDOWN":
                return false;
            case "DEAD_ENDRIGHT":
                return false;
            case "BENDRIGHT":
                return false;
            case "DEAD_ENDUP":
                return false;
            case "STRAIGHTUP":
            case "STRAIGHTDOWN":
                return false;
            case "BENDUP":
                return false;
            case "TEEUP":
                return false;
            case "DEAD_ENDLEFT":
                return true;
            case "BENDDOWN":
                return true;
            case "STRAIGHTLEFT":
            case "STRAIGHTRIGHT":
                return true;
            case "TEERIGHT":
                return true;
            case "BENDLEFT":
                return true;
            case "TEEDOWN":
                return true;
            case "TEELEFT":
                return true;
            case "CROSSALL":
                return true;
            default:
                return false;
        }
    }

    public boolean hasDownCon() {
        String orient = this.getOrientation().toString();
        String tType = this.getTileType().toString();
        String check = tType + orient;

        switch (check) {
            case "EMPTYALL":
                return false;
            case "DEAD_ENDDOWN":
                return true;
            case "DEAD_ENDRIGHT":
                return false;
            case "BENDRIGHT":
                return true;
            case "DEAD_ENDUP":
                return false;
            case "STRAIGHTUP":
            case "STRAIGHTDOWN":
                return true;
            case "BENDUP":
                return false;
            case "TEEUP":
                return true;
            case "DEAD_ENDLEFT":
                return false;
            case "BENDDOWN":
                return true;
            case "STRAIGHTLEFT":
            case "STRAIGHTRIGHT":
                return false;
            case "TEERIGHT":
                return true;
            case "BENDLEFT":
                return false;
            case "TEEDOWN":
                return true;
            case "TEELEFT":
                return false;
            case "CROSSALL":
                return true;
            default:
                return false;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((orient == null) ? 0 : orient.hashCode());
        result = prime * result + ((tType == null) ? 0 : tType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Tile other = (Tile) obj;
        if (orient != other.orient)
            return false;
        if (tType != other.tType)
            return false;
        return true;
    }
}
