package TankGame3;

/**
 * @author wang zifan
 * @version 1.0
 * @date 2022/4/21  16:42
 * 一个Node对象，表示一个敌人坦克的信息
 */
public class Node {
    private int x;
    private int y;
    private int direct;

    public Node(int x, int y, int direct) {
        this.x = x;
        this.y = y;
        this.direct = direct;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDirect() {
        return direct;
    }

    public void setDirect(int direct) {
        this.direct = direct;
    }
}
