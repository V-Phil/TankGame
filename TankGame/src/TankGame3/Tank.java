package TankGame3;

/**
 * @author wang zifan
 * @version 1.0
 * @date 2022/3/29  16:45
 */
public class Tank {
    private int x;//坦克横坐标
    private int y;//坦克纵坐标
    private int direct;//坦克方向 0上 1右 2下 3左
    private int speed = 1;
    boolean isLive = true;

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    //上右下左移动
    public void moveUp() {
        y -= speed;
    }
    public void moveRight() {
        x += speed;
    }
    public void moveDown() {
        y += speed;
    }
    public void moveLeft() {
        x -= speed;
    }
    public int getDirect() {
        return direct;
    }

    public void setDirect(int direct) {
        this.direct = direct;
    }

    public Tank(int x, int y) {
        this.x = x;
        this.y = y;
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
}
