package TankGame3;

/**
 * @author wang zifan
 * @version 1.0
 * @date 2022/4/5  17:18
 */
public class Shot implements Runnable {
    int x;//记录子弹的x坐标
    int y;//记录子弹的y坐标
    int direct = 0;//子弹方向
    int speed = 2;//子弹速度
    boolean isLive = true;//子弹是否存活

    public Shot(int x, int y, int direct) {
        this.x = x;
        this.y = y;
        this.direct = direct;
    }

    @Override
    public void run() {
        while (true) {
            //休眠 50 ms
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            switch (direct) {
                case 0 ://向上移动
                    y -= speed;
                    break;
                case 1 ://右
                    x += speed;
                    break;
                case 2 ://下
                    y += speed;
                    break;
                case 3 ://左
                    x -= speed;
                    break;
            }
//            System.out.println("子弹 x=" + x + " y = " + y);
            //子弹碰到边界，进程撤销
            //当子弹碰到敌人坦克时，也应该结束线程
            if (!(x >= 0 && x <= 1000 && y >= 0 && y <= 750 && isLive)) {
                System.out.println("子弹线程退出");
                isLive = false;
                break;
            }



        }
    }
}
