package TicTacToe;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by DENIS on 16.02.2017. Окно выбора режима игры
 */
 class StartNewGameWindow extends JFrame{

    private final int WINDOW_WIGHT = 266;
    private final int WINDOW_HEIGHT = 366;

    private int MIN_WIN_LENGTH = 3;
    private int MIN_FIELD_SIZE = 3;
    private int MAX_FIELD_SIZE = 8;
    private final String STR_WIN_LEN = "Win length ";
    private final String STR_FIELD_SIZE = "Win field size ";

    private JRadioButton rbtn_HUM_vs_AI;
    private JRadioButton rbtn_HUM_vs_HUM;
    private JSlider slWinLen;
    private JSlider slFieldSize;
    private JButton bStartGame;

    private GameWindow gameWindow ;




    StartNewGameWindow(GameWindow gameWindow){
        this.gameWindow = gameWindow;
        setTitle("Game settings");
        setSize(WINDOW_WIGHT, WINDOW_HEIGHT);
        setResizable(false);
        Rectangle rectangle = gameWindow.getBounds();
        int pos_X = (int)rectangle.getCenterX() - WINDOW_WIGHT /2;
        int pos_Y = (int)rectangle.getCenterY() - WINDOW_HEIGHT /2;
        setLocation(pos_X,pos_Y);
        setLayout(new GridLayout(10,2));
        gameModeControl();
        gameControlFieldAndWinLength();

        bStartGame = new JButton("Start Game!");
        bStartGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnStartGame_onClick();
            }
        });
        add(bStartGame);

    }
    //кнопки выбора режима игры
    private void gameModeControl (){

        add(new JLabel("Choose gaming mod"));
        rbtn_HUM_vs_AI = new JRadioButton("Human vs AI",true);
        rbtn_HUM_vs_HUM = new JRadioButton("Human vs Human");
        ButtonGroup gameMode = new ButtonGroup();
        gameMode.add(rbtn_HUM_vs_AI);
        gameMode.add(rbtn_HUM_vs_HUM);
        add(rbtn_HUM_vs_AI);
        add(rbtn_HUM_vs_HUM);
    }
    //слайдеры выбора размеров поля и выигрышной комбинации
    private void gameControlFieldAndWinLength(){
        JLabel lblWinLen = new JLabel(STR_WIN_LEN + MIN_FIELD_SIZE);
        add(lblWinLen);
        slWinLen = new JSlider(MIN_WIN_LENGTH,MIN_FIELD_SIZE,MIN_WIN_LENGTH);
        slWinLen.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                lblWinLen.setText(STR_WIN_LEN + slWinLen.getValue());
            }
        });
        add(slWinLen);
        JLabel lblFieldSize = new JLabel(STR_FIELD_SIZE + MIN_FIELD_SIZE);
        add(lblFieldSize);
        slFieldSize = new JSlider(MIN_FIELD_SIZE,MAX_FIELD_SIZE,MIN_FIELD_SIZE);
        slFieldSize.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int currentFieldSize = slFieldSize.getValue();
                lblFieldSize.setText(STR_FIELD_SIZE + currentFieldSize);
                slWinLen.setMaximum(currentFieldSize);
            }
        });
        add(slFieldSize);

    }
    //кнопка начала игры
    private void btnStartGame_onClick(){
        GameMod gameMod;
        if (rbtn_HUM_vs_AI.isSelected())gameMod = GameMod.Human_vs_AI;
        else if (rbtn_HUM_vs_HUM.isSelected())gameMod = GameMod.Human_vs_Human;
        else throw new RuntimeException("incorrect game mod");
        int fieldSize_X = slFieldSize.getValue();
        int winLen = slWinLen.getValue();
        gameWindow.startNewGame(gameMod,fieldSize_X,fieldSize_X,winLen);

        setVisible(false);
    }
}
