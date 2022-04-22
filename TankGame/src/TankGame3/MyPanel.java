package TankGame3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Vector;

/**
 * @author wang zifan
 * @version 1.0
 * @date 2022/3/29  17:48
 * 坦克大战的绘图区
 */

//为了监听键盘事件，实现KeyListener
//为了让Panel 不停的重绘子弹，需要将MyPanel实现Runnable，当作一个线程使用
public class MyPanel extends JPanel implements KeyListener, Runnable{
    //定义我的坦克
    Hero hero = null;
    //定义敌人的坦克，放入到Vector
    Vector<EnemyTank> enemyTanks = new Vector<>();
    //定义一个存放Node对象的Vector,用于恢复敌人坦克的坐标和方向
    Vector<Node> nodes = new Vector<>();
    //定义一个Vector，用于存放炸弹
    //当子弹击中坦克时，加入一个Bomb对象到bombs
    Vector<Bomb> bombs = new Vector<>();
    int enemyTankSize = 3;

    //定义三张炸弹图片，用于显示爆炸效果
    Image image1 = null;
    Image image2 = null;
    Image image3 = null;


    public MyPanel(String key) {
        //判断记录文件是否存在
        //如果存在，正常执行；不存在，只能开启新游戏，key="1“
        File file = new File(Recorder.getRecordFile());
        if (file.exists()) {
            nodes = Recorder.getNodesAndEnemyTankRec();
        } else {
            System.out.println("文件不存在，只能开启新的游戏");
            key = "1";
        }


        //将MyPanel对象的enemyTanks设置给Recorder的enemyTanks
        Recorder.setEnemyTanks(enemyTanks);
        hero = new Hero(320, 100);//初始化自己的坦克
        hero.setSpeed(1);
        switch (key) {
            case "1":
                //初始化敌人的坦克
                for (int i = 0; i < enemyTankSize; i++) {
                    //创建一个敌人的坦克
                    EnemyTank enemyTank = new EnemyTank((100 * (i + 1)), 0);
                    //将enemyTank设置给enemyTank
                    enemyTank.setEmemyTanks(enemyTanks);
                    //设置方向
                    enemyTank.setDirect(2);
                    //启动敌人坦克线程，让它动起来
                    new Thread(enemyTank).start();
                    //给该enemyTank加入一颗子弹
                    Shot shot = new Shot(enemyTank.getX() + 20, enemyTank.getY() + 60, enemyTank.getDirect());
                    //加入到enemyTankVector成员
                    enemyTank.shots.add(shot);
                    //启动shot对象
                    new Thread(shot).start();

                    //加入
                    enemyTanks.add(enemyTank);

                }
                break;
            case "2"://继续上局游戏
                //初始化敌人的坦克
                for (int i = 0; i < nodes.size(); i++) {
                    Node node = nodes.get(i);
                    //创建一个敌人的坦克
                    EnemyTank enemyTank = new EnemyTank(node.getX(), node.getY());
                    //将enemyTank设置给enemyTank
                    enemyTank.setEmemyTanks(enemyTanks);
                    //设置方向
                    enemyTank.setDirect(node.getDirect());
                    //启动敌人坦克线程，让它动起来
                    new Thread(enemyTank).start();
                    //给该enemyTank加入一颗子弹
                    Shot shot = new Shot(enemyTank.getX() + 20, enemyTank.getY() + 60, enemyTank.getDirect());
                    //加入到enemyTankVector成员
                    enemyTank.shots.add(shot);
                    //启动shot对象
                    new Thread(shot).start();

                    //加入
                    enemyTanks.add(enemyTank);

                }
                break;
            default:
                System.out.println("你的输入有误");

        }


        //初始化图片对象
        image1 = Toolkit.getDefaultToolkit().getImage(MyPanel.class.getResource("bom_1.gif"));
        image2 = Toolkit.getDefaultToolkit().getImage(MyPanel.class.getResource("bom_2.gif"));
        image3 = Toolkit.getDefaultToolkit().getImage(MyPanel.class.getResource("bom_3.gif"));
//        image1 = Toolkit.getDefaultToolkit().getImage(MyPanel.class.getResource("/bomb1.gif"));
        //播放指定的音乐
        new AePlayWave("src\\111.wav").start();
    }
    //编写方法，显示我方击毁敌方坦克的数量
    public void showInfo(Graphics g) {
        //画出玩家的总成绩
        g.setColor(Color.black);
        Font font = new Font("宋体", Font.BOLD, 25);
        g.setFont(font);
        g.drawString("您累积击毁敌方坦克", 1020, 30);
        drawTank(1020, 60, g, 0, 0);//画出一个敌方坦克
        g.setColor(Color.BLACK);
        g.drawString(Recorder.getAllEnemyTankNum() + "", 1080, 100);
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.fillRect(0, 0, 1000, 750);//填充矩形，默认黑色
        showInfo(g);
        if (hero != null && hero.isLive) {
            drawTank(hero.getX(), hero.getY(), g, hero.getDirect(), 1);
        }
        //画出Hero射击的子弹
//        if (hero.shot != null && hero.shot.isLive == true) {
//            g.draw3DRect(hero.shot.x, hero.shot.y, 1, 1, false);
//        }
        //将hero的子弹集合shots,遍历取出绘制
        for (int i = 0; i < hero.shots.size(); i++) {
            Shot shot = hero.shots.get(i);
            if (hero.shot != null && shot.isLive == true) {
                g.draw3DRect(shot.x, shot.y, 1, 1, false);
            } else {//如果该shot 对象已经无效，就从shots集合中拿掉
                hero.shots.remove(shot);
            }
        }

        //如果bombs集合中有对象，就画出
        for (int i = 0; i < bombs.size(); i++) {

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //取出炸弹
            Bomb bomb = bombs.get(i);
            //根据当前这个bomb对象的life值去画出对应的图片
            if (bomb.life > 6) {
                g.drawImage(image1, bomb.x, bomb.y, 60, 60, this);
            } else if (bomb.life > 3) {
                g.drawImage(image2, bomb.x, bomb.y, 60, 60, this);
            } else {
                g.drawImage(image3, bomb.x, bomb.y, 60, 60, this);
            }
            //让炸弹生命值减少
            bomb.lifeDown();
            //如果bomb life 为0，就从bombs的集合中删除
            if (bomb.life == 0) {
                bombs.remove(bomb);
            }
        }




        //画出敌人的坦克,遍历Vector

        for (int i = 0; i < enemyTanks.size(); i++) {
            //从Vector取出坦克
            EnemyTank enemyTank = enemyTanks.get(i);
            //判断当前坦克是否存活
            if (enemyTank.isLive) {//当敌人坦克是存活的，才画出该坦克
                drawTank(enemyTank.getX(), enemyTank.getY(), g, enemyTank.getDirect(), 0);
                //画出enemyTank所有子弹
                for (int j = 0; j < enemyTank.shots.size(); j++) {
                    //取出子弹
                    Shot shot = enemyTank.shots.get(j);
                    //绘制
                    if (shot.isLive) {//isLive = true
                        g.draw3DRect(shot.x, shot.y, 1, 1, false);
                    } else {
                        //从Vector移除
                        enemyTank.shots.remove(shot);
                    }
                }
            }
        }
    }
/*
 x 坦克左上角x坐标
 y 坦克左上角y坐标
 g 画笔
 direct 坦克方向
 type 坦克类型
 */
    public void drawTank(int x, int y, Graphics g, int direct, int type) {
        //根据不同类型坦克，设置不同颜色
        switch (type) {
            case 0 : //我们的坦克
                g.setColor(Color.cyan);
                break;
            case 1 : //敌人的坦克
                g.setColor(Color.yellow);
                break;
        }
        //根据坦克方向，来绘制对应形状坦克
        //direct表示方向（0：向上 1向右 2向下 3向右）
        switch (direct) {
            case 0 :
                g.fill3DRect(x, y, 10, 60, false);//画出坦克左边的轮子
                g.fill3DRect(x + 30, y, 10, 60, false);//画出坦克右边的轮子
                g.fill3DRect(x + 10, y + 10, 20, 40, false);//画出坦克的盖子
                g.fillOval(x + 10, y + 20, 20, 20);//画出坦克的圆形盖子
                g.drawLine(x + 20, y + 30, x + 20, y);//坦克的炮管
                break;
            case 1 :
                g.fill3DRect(x, y, 60, 10, false);//画出坦克左边的轮子
                g.fill3DRect(x, y + 30, 60, 10, false);//画出坦克右边的轮子
                g.fill3DRect(x + 10, y + 10, 40, 20, false);//画出坦克的盖子
                g.fillOval(x + 20, y + 10, 20, 20);//画出坦克的圆形盖子
                g.drawLine(x + 30, y + 20, x + 60, y + 20);//坦克的炮管
                break;
            case 2 :
                g.fill3DRect(x, y, 10, 60, false);//画出坦克左边的轮子
                g.fill3DRect(x + 30, y, 10, 60, false);//画出坦克右边的轮子
                g.fill3DRect(x + 10, y + 10, 20, 40, false);//画出坦克的盖子
                g.fillOval(x + 10, y + 20, 20, 20);//画出坦克的圆形盖子
                g.drawLine(x + 20, y + 30, x + 20, y + 60);//坦克的炮管
                break;
            case 3 :
                g.fill3DRect(x, y, 60, 10, false);//画出坦克左边的轮子
                g.fill3DRect(x, y + 30, 60, 10, false);//画出坦克右边的轮子
                g.fill3DRect(x + 10, y + 10, 40, 20, false);//画出坦克的盖子
                g.fillOval(x + 20, y + 10, 20, 20);//画出坦克的圆形盖子
                g.drawLine(x + 30, y + 20, x, y + 20);//坦克的炮管
                break;

            default:
                System.out.println("暂时没有处理");
        }
    }

    //在判断我方子弹是否击中敌人坦克时，就需要把我们的子弹集合中所有的子弹
    //都取出和敌人所有坦克进行判断
    public void hitEnemyTank() {
        //遍历我们的子弹
        for (int j = 0; j < hero.shots.size(); j++) {
            Shot shot = hero.shots.get(j);

            //判断是否击中了敌人坦克
            if (shot != null && shot.isLive) {//子弹存活
                //遍历敌人所有的坦克
                for (int i = 0; i < enemyTanks.size(); i++) {
                    EnemyTank enemyTank = enemyTanks.get(i);
                    hitTank(shot, enemyTank);
                }
            }
        }

    }

    //判断敌人坦克是否击中我的坦克
    public void hitHero() {
        //遍历所有的敌人坦克
        for (int i = 0; i < enemyTanks.size(); i++) {
            //取出敌人的坦克
            EnemyTank enemyTank = enemyTanks.get(i);
            //遍历enemyTank对象的所有子弹
            for (int j = 0; j < enemyTank.shots.size(); j++) {
                //取出子弹
                Shot shot = enemyTank.shots.get(j);
                //判断shot 是否击中我的坦克
                if (hero.isLive && shot.isLive) {
                    hitTank(shot, hero);
                }
            }
        }
    }

    //编写方法，判断我方的子弹是否命中敌人坦克
    public void hitTank(Shot s, Tank enemyTank) {
        //判断s 击中坦克
        switch (enemyTank.getDirect()) {
            case 0 ://坦克向上
            case 2 ://坦克向下
                if (s.x > enemyTank.getX() && s.x < enemyTank.getX() + 40
                && s.y > enemyTank.getY() && s.y < enemyTank.getY() + 60) {
                    s.isLive = false;
                    enemyTank.isLive = false;
                    //当我的子弹击中敌人坦克后，将enemyTank从Vector拿掉
                    enemyTanks.remove(enemyTank);
                    //当我方击毁一个敌人坦克。allEnemyTankNum++
                    if (enemyTank instanceof  EnemyTank) {
                        Recorder.addAllEnemyTankNum();
                    }

                    //创建一个Bomb对象，加入到Bombs集合
                    Bomb bomb = new Bomb(enemyTank.getX(), enemyTank.getY());
                    bombs.add(bomb);
                }
                break;
            case 1 ://坦克向右
            case 3 ://坦克向左
                if (s.x > enemyTank.getX() && s.x < enemyTank.getX() + 60
                && s.y > enemyTank.getY() && s.y < enemyTank.getY() + 40) {
                    s.isLive = false;
                    enemyTank.isLive = false;
                    //当我的子弹击中敌人坦克后，将enemyTank从Vector拿掉
                    enemyTanks.remove(enemyTank);
                    if (enemyTank instanceof  EnemyTank) {
                        Recorder.addAllEnemyTankNum();
                    }
                    //创建一个Bomb对象，加入到Bombs集合
                    Bomb bomb = new Bomb(enemyTank.getX(), enemyTank.getY());
                    bombs.add(bomb);
                }
                break;

        }
    }




    @Override
    public void keyTyped(KeyEvent e) {

    }
//处理 w d a s 键按下的情况
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {//按下W键
            //改变坦克方向
            hero.setDirect(0);
            //修改坦克坐标
            if (hero.getY() > 0) {
                hero.moveUp();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_D) {//D键
            hero.setDirect(1);
            if (hero.getX() + 60 < 1000) {
                hero.moveRight();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_S) {//S键
            hero.setDirect(2);
            if (hero.getY() + 60 < 750) {
                hero.moveDown();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_A) {//A键
            hero.setDirect(3);
            if (hero.getX() > 0) {
                hero.moveLeft();
            }
        }
        //如果用户按下J，发射子弹
        if (e.getKeyCode() == KeyEvent.VK_J) {
            //判断hero的子弹是否销毁，发射一颗子弹
//            if (hero.shot == null || ! hero.shot.isLive) {
//                hero.shotEnemyTank();
//            }
            //发射多颗子弹
            hero.shotEnemyTank();
        }
        //让面板重绘
        this.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void run() {//每隔100ms,重绘区域,刷新绘图区域，子弹就移动
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            hitEnemyTank();
            //判断敌人坦克是否击中我们
            hitHero();
            this.repaint();
        }
    }
}
