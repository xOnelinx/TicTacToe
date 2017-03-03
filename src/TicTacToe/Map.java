package TicTacToe;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

/**
 * Created by Денис on 15.02.2017. Игровое поле
 */
class Map extends JPanel {

    private GameMod gameMode;
    private GameEnd gameEnd;

    //массив игрового поля
    private int [][] field;
    //переменные размера массивва игр. поля
    private int fieldSizeX;
    private int fieldSizeY;
    //длинна выигрышной последовательности
    private int winLen;

    //переменные размера клеток
    private int cellWight;
    private int cellHeight;

    //элементы массива обозначающие игроков и пустые клетыки
    private final int EMPTY_DOT = 0;
    private final int PLAYER2_DOT = 1;
    private final int PLAYER1_DOT = 2;

    //флаг конца игры
    private boolean gameOver = false;
    //флаг инициализации игровых переменных
    private boolean initiolize = false;
    //флаг хода оппонента
    private boolean turn =false;

    private final Random random = new Random();

    private final Font font = new Font("Times new roman",Font.BOLD,40);


    Map(){

    addMouseListener(new MouseAdapter() {
        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            update(e);

        }
    });


    }
    // обработка ходов игроков или аи и игрока
    private void update (MouseEvent e){
        if (!initiolize)return;
        if (gameOver)return;
        int dot = PLAYER1_DOT;
        if (gameMode==GameMod.Human_vs_Human&turn) {dot = PLAYER2_DOT; }
        int cellx = e.getX()/ cellWight;
        int celly = e.getY()/ cellHeight;
        if(!isValidCell(cellx,celly)||!isEmptyCell(cellx,celly))return;
        field[cellx][celly] = dot;
        repaint();

        if (isLastTurn(dot))return;
        if (gameMode==GameMod.Human_vs_AI)aiTurn();
        if (gameMode==GameMod.Human_vs_Human&!turn) {turn=true; return;}

        repaint();
        if (isLastTurn(PLAYER2_DOT)) return;
        turn = false;
    }


    //метод не только возвращает значение но и выполняет побочное действие
    //устнавливает флаг конца игры и тип конца игры
    private boolean isLastTurn(int dot){
        if (checkWin(dot)){
            if (dot == PLAYER1_DOT)
            {gameEnd = GameEnd.Player1Win;}
            else{
                if (gameMode == GameMod.Human_vs_Human){
                    if (dot == PLAYER2_DOT) gameEnd = GameEnd.Player2Win;
                    else throw new RuntimeException("Unknown Player wins!");
                }else if (gameMode == GameMod.Human_vs_AI){
                    if (dot == PLAYER2_DOT) gameEnd = GameEnd.AIWin;
                    else throw new RuntimeException("Unknown Player wins!");}
            }
            gameOver = true;
            return true;
        }
        if (isMapFull()) {
            gameEnd = GameEnd.GameDraw;
            gameOver = true;
            return true;
        }
        return false;
    }

    void startNewGame (GameMod gameMode,int fieldSizeX,int fieldSizeY,int winLength){
        this.gameMode = gameMode;
        this.fieldSizeX = fieldSizeX;
        this.fieldSizeY = fieldSizeY;
        this.winLen = winLength;
        field = new int[fieldSizeX][fieldSizeY];
        gameOver = false;
        initiolize = true;
        repaint();

    }

    @Override
    protected void paintComponent (Graphics g){
        super.paintComponent(g);
        render(g);

    }

    //отрисовка графики
    private void render (Graphics g){
        if (!initiolize){drawStartImg(g,-3,-3);return;}
        int panelWight = getWidth();
        int panelHeight = getHeight();
        cellHeight = panelHeight/fieldSizeY;
        cellWight = panelWight/fieldSizeX;

        int bottom = cellHeight /5;

        fieldDraw(g, panelWight, panelHeight);
        plTurnDraw(g, bottom);

        if (gameOver) showGameOverMessage(g);
    }

    ///отрисовка ходов игрока или АИ
    private void plTurnDraw(Graphics g, int bottom) {
        for (int i = 0; i <fieldSizeX; i++) {
            for (int j = 0; j <fieldSizeY ; j++) {
                if (isEmptyCell(i,j)) continue;
                if (field[i][j]== PLAYER1_DOT){
                    g.setColor(Color.RED);
                    drawX(g, cellHeight, cellWight,i,j,bottom);

                }
                else if (field[i][j]== PLAYER2_DOT){
                    g.setColor(Color.BLUE);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setStroke(new BasicStroke(bottom/1.7f));
                    g.drawOval(i* cellWight +bottom,
                            j* cellHeight +bottom/2,
                            cellWight -bottom*2,
                            cellHeight -bottom);
                }
                else {
                    throw new RuntimeException("Unexpected value in cell X:"+i+"Y:"+j);
                }
            }
        }
    }

    //отрисовка поля
    private void fieldDraw(Graphics g, int panelWight, int panelHeight) {
        g.setColor(Color.RED);
        for (int i = 0; i <fieldSizeY; i++) {
            int y = i* cellHeight;
            g.drawLine(0,y,panelWight,y);
        }
        for (int i = 0; i <fieldSizeX; i++) {
            int x = i* cellHeight;
            g.drawLine(x,0,x,panelHeight);
        }
    }
    //стартовая картинка
    private void drawStartImg(Graphics g,int x,int y){
        BufferedImage img;

        try {
            // чтение
            img = ImageIO.read(new File("X-circle.png"));
            g.drawImage(img, x, y, null);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    //нарисовать крестик
    private void drawX(Graphics g, int h, int w, int x, int y, int bottom){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(h/5));
        g2d.drawLine(x*w+bottom,y*h+bottom,x*w+w-bottom,y*h+h-bottom);
        g2d.drawLine(x*w+w-bottom,y*h+bottom,x*w+bottom,y*h+h-bottom);

    }
    //сообщение о конце игры
    private void showGameOverMessage(Graphics g){
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0,130,getWidth(),120);
        g.setColor(Color.YELLOW);
        g.setFont(font);

        switch (gameEnd){
            case GameDraw:
                drawMesseg(gameEnd.getMesseg(),g);
                break;
            case Player1Win:
                drawMesseg(gameEnd.getMesseg(),g);
                break;
            case Player2Win:
                drawMesseg(gameEnd.getMesseg(),g);
                break;
            case AIWin:
                drawMesseg(gameEnd.getMesseg(),g);
                break;

            default:
                throw new RuntimeException("Unexpected Game Over:"+gameEnd);
        }

    }
    //отрисовка сообщения
    private void drawMesseg(String msg, Graphics g){
        FontMetrics fm = g.getFontMetrics(font);
        g.drawString(msg,(getWidth()-fm.stringWidth(msg))/2 ,
                (getHeight()-fm.getHeight())/2);}


    // Ход компьютера
    private  void aiTurn() {
        if(turnAIWinCell()) return;		// проверим, не выиграет-ли игрок на следующем ходу
        if(turnHumanWinCell()) return;	// проверим, не выиграет-ли комп на следующем ходу
        int x, y;
        do {							// или комп ходит в случайную клетку
            x = random.nextInt(fieldSizeX);
            y = random.nextInt(fieldSizeY);
        } while (!isEmptyCell(x, y));
        field[x][y] = PLAYER2_DOT;
    }
    // Проверка, может ли выиграть комп
    private  boolean turnAIWinCell() {
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (isEmptyCell(i, j)) {				// поставим нолик в каждую клетку поля по очереди
                    field[i][j] = PLAYER2_DOT;
                    if (checkWin(PLAYER2_DOT)) return true;	// если мы выиграли, вернём истину, оставив нолик в выигрышной позиции
                    field[i][j] = EMPTY_DOT;			// если нет - вернём обратно пустоту в клетку и пойдём дальше
                }
            }
        }
        return false;
    }
    // Проверка, выиграет-ли игрок своим следующим ходом
    private  boolean turnHumanWinCell() {
        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY; j++) {
                if (isEmptyCell(i, j)) {
                    field[i][j] = PLAYER1_DOT;			// поставим крестик в каждую клетку по очереди
                    if (checkWin(PLAYER1_DOT)) {			// если игрок победит
                        field[i][j] = PLAYER2_DOT;			// поставить на то место нолик
                        return true;
                    }
                    field[i][j] = EMPTY_DOT;			// в противном случае вернуть на место пустоту
                }
            }
        }
        return false;
    }
    // проверка на победу
    private  boolean checkWin(int dot) {
        for (int i = 0; i < fieldSizeX; i++) {			// ползём по всему полю
            for (int j = 0; j < fieldSizeY; j++) {
                if (checkLine(i, j, 1, 0, winLen, dot)) return true;	// проверим линию по х
                if (checkLine(i, j, 1, 1, winLen, dot)) return true;	// проверим по диагонали х у
                if (checkLine(i, j, 0, 1, winLen, dot)) return true;	// проверим линию по у
                if (checkLine(i, j, 1, -1, winLen, dot)) return true;	// проверим по диагонали х -у
            }
        }
        return false;
    }

    // проверка линии
    private  boolean checkLine(int x, int y, int vx, int vy, int len, int dot) {
        final int far_x = x + (len - 1) * vx;			// посчитаем конец проверяемой линии
        final int far_y = y + (len - 1) * vy;
        if (!isValidCell(far_x, far_y)) return false;	// проверим не выйдет-ли проверяемая линия за пределы поля
        for (int i = 0; i < len; i++) {					// ползём по проверяемой линии
            if (field[x + i * vx][y + i * vy] != dot) return false;	// проверим одинаковые-ли символы в ячейках
        }
        return true;
    }
    // ничья?
    private  boolean isMapFull() {
        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY; j++) {
                if (field[i][j] == EMPTY_DOT) return false;
            }
        }
        return true;
    }
    // ячейка-то вообще правильная?
    private  boolean isValidCell(int x, int y) { return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY; }
    // а пустая?
    private  boolean isEmptyCell(int x, int y) { return field[x][y] == EMPTY_DOT; }

}
