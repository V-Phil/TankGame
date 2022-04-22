package TankGame3;

import java.util.Vector;

/**
 * @author wang zifan
 * @version 1.0
 * @date 2022/3/29  17:46
 * 自己的坦克
 */
public class Hero extends Tank {
    //定义一个Shoot 对象
    Shot shot = null;
    //可以发射多颗子弹
    Vector<Shot> shots = new Vector<>();
    public Hero(int x, int y) {
        super(x, y);
    }

    //射击
    public void shotEnemyTank() {
        //最多5颗子弹
        if (shots.size() == 5) {
            return;
        }
        //创建Shoot 对象,根据Hero对象的位置和方向创建Shot
        switch (getDirect()) {
            case 0 ://上
                shot = new Shot(getX() + 20, getY(), 0);
                break;
            case 1 ://右
                shot = new Shot(getX() + 60, getY() + 20, 1);
                break;
            case 2 ://下
                shot = new Shot(getX() + 20, getY() + 60, 2);
                break;
            case 3 ://左
                shot = new Shot(getX(), getY() + 20, 3);
                break;

        }
        //把新创建的shot 放入到shots
        shots.add(shot);
        //启动Shot进程
        new Thread(shot).start();
    }
}
