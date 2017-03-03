package TicTacToe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Денис on 15.02.2017. Стартовое окно
 */
class GameWindow extends JFrame{
    private final int WINDOW_HEIGHT = 500;
    private final int WINDOW_WIGHT = 451;
    private final int WINDOW_POS_X = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2-WINDOW_WIGHT;
    private final int WINDOW_POS_Y = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2- WINDOW_HEIGHT /2;
    private StartNewGameWindow startNewGameWindow;
    private Map map;

    GameWindow(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("TicTacToe");
        setSize(WINDOW_WIGHT, WINDOW_HEIGHT);
        setResizable(false);

        setLocation(WINDOW_POS_X,WINDOW_POS_Y);

        JButton btnNewGame = new JButton("New Game");
        btnNewGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startSNGWindow();
                startNewGameWindow.setVisible(true);
            }
        });
        JButton btnExitGame = new JButton("Exit Game");
        btnExitGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        map = new Map();

        JPanel pBottom = new JPanel();
        pBottom.setLayout(new GridLayout(1,2));
        pBottom.add(btnNewGame);
        pBottom.add(btnExitGame);

        add(map,BorderLayout.CENTER);
        add(pBottom,BorderLayout.SOUTH);

        setVisible(true);
    }

    //запускает окно настроек новой игры
    private void startSNGWindow(){
        startNewGameWindow = new StartNewGameWindow(this);
    }

    //метод передает параметры игры полю отрисовки
    void startNewGame (GameMod gameMode,int fieldSizeX,int fieldSizeY,int winLength){
        map.startNewGame(gameMode,fieldSizeX,fieldSizeY,winLength);

    }
}
