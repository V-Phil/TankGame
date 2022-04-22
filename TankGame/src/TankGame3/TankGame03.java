package TankGame3;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Scanner;

/**
 * @author wang zifan
 * @version 1.0
 * @date 2022/3/29  17:50
 */
public class TankGame03 extends JFrame {
    MyPanel mp = null;
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {

        TankGame03 tankGame01 = new TankGame03();
    }

    public TankGame03() {
        System.out.println("请输入选择 1：新游戏 2：继续上局");
        String key = scanner.next();
        mp = new MyPanel(key);
        //将mp放入到Thread,并启动
        Thread thread = new Thread(mp);
        thread.start();
        this.add(mp);
        this.setSize(1300, 950);
        this.addKeyListener(mp);//增加一个监听事件，让JFrame 监听mp的键盘事件
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        //在JFrame 中增加相应关闭窗口的处理
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Recorder.keepRecord();
                System.exit(0);
            }
        });
    }
}
