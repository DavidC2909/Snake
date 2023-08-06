import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener {

    //screen width and height
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;

    //set game units in the panel
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/(UNIT_SIZE*UNIT_SIZE);

    //create delay for timer
    static final int DELAY = 75; //the higher the number of the delay, the slower the game will be
                                 //the lower the number of the delay, the faster the game will be

    //create arrays(x,y) to hold the coordinates of the snake
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];

    //set initial number of body parts for the snake
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R'; //snake begin by going to the right
    boolean running = false;
    Timer timer;
    Random random;


    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
//Creating methods for the game
    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            /*
            //turn the background into a grid to help visualize
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.setColor(Color.GRAY);
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
             */
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.red);
            g.setFont( new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " +applesEaten))/2, g.getFont().getSize());
        }
        else {
            gameOver(g);
        }
    }


    public void newApple(){
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }

    public void move(){
        for(int i = bodyParts;i>0;i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch (direction){
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;

        }

    }

    public void checkApple(){
        if((x[0] == appleX) && y[0] == appleY){
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions(){
            //checks if head collides with body
            for(int i = bodyParts;i>0;i--){
                if((x[0] == x[i]) && (y[0] == y[i]))
                    running = false;
            }
            //check if head touches the left border
            if(x[0] < 0){
                running = false;
            }
            //check if head touches the right border
            if(x[0] > SCREEN_WIDTH){
                running = false;
            }
            //check if head touches the top border
            if(y[0] < 0){
                running = false;
            }
            //check if head touches the bottom border
            if(y[0] > SCREEN_HEIGHT){
                running = false;
            }

            if(!running){
                timer.stop();
            }
    }

    public void gameOver(Graphics g){
        //Score
        g.setColor(Color.red);
        g.setFont( new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " +applesEaten))/2, g.getFont().getSize());

        //Game Over text
        g.setColor(Color.red);
        g.setFont( new Font("Ink Free", Font.BOLD, 70));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT -> {
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;
                }
                case KeyEvent.VK_RIGHT -> {
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;
                }
                case KeyEvent.VK_UP -> {
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;
                }
                case KeyEvent.VK_DOWN -> {
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;
                }
            }
        }
    }

}
