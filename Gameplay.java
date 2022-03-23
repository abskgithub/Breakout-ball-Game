package com.company;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Timer;
import javax.swing.JPanel;



public class Gameplay extends JPanel implements ActionListener, KeyListener {

    private boolean play = false;
    private int score = 0;
    private int totalbricks = 36;

    //Timer
    private Timer timer;
    private int delay = 8;

    //position of paddle
    private int playerX = 310;

    //position of ball
    private int ballposX = 120;
    private int ballposY = 350;
    private int ballXdir = -1;
    private int ballYdir = -2;

    //start-step4
    private MapGenerator map;

    public Gameplay()
    {
        map = new MapGenerator(4,9);
        addKeyListener(this); //to detect keys
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start(); //to start the game cycle
    }

    //end-step4

    public void paint(Graphics g)
    {
        // background
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);

        //drawing map
        map.draw((Graphics2D) g);

        // borders
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(681, 0, 3, 592);

        // the paddle
        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);

        // the ball
        g.setColor(Color.yellow);
        g.fillOval(ballposX, ballposY, 20, 20);

        // Displaying result when game is over
        // the scores
        g.setColor(Color.white);
        g.setFont(new Font("serif",Font.BOLD, 25));
        g.drawString(""+score, 590,30);

        // when you won the game
        if(totalbricks <= 0)
        {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif",Font.BOLD, 30));
            g.drawString("You Won", 260,300);

            g.setColor(Color.RED);
            g.setFont(new Font("serif",Font.BOLD, 20));
            g.drawString("Press (Enter) to Restart", 230,350);
        }

        // when you lose the game
        if(ballposY > 570)
        {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif",Font.BOLD, 30));
            g.drawString("Game Over, Scores: "+score, 190,300);

            g.setColor(Color.RED);
            g.setFont(new Font("serif",Font.BOLD, 20));
            g.drawString("Press (Enter) to Restart", 230,350);
        }
        g.dispose();

    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        //when right key is pressed
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            //checking if it doesn't go outside the panel
            if(playerX >= 600) {
                playerX = 600;
            }
            else {
                moveRight();
            }
        }

        //when right key is pressed
        if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            //checking if it doesn't go outside the panel
            if(playerX < 10) {
                playerX = 10;
            }
            else {
                moveLeft();
            }
        }

        //if enter key is pressed
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            if(!play)
            {
                //to restart the game
                play = true;
                ballposX = 120;
                ballposY = 350;
                ballXdir = -1;
                ballYdir = -2;
                playerX = 310;
                score = 0;
                totalbricks = 36;
                map = new MapGenerator(4, 9);

                repaint();
            }
        }

    }

    public void moveLeft() {
        play = true;
        playerX -=15;
    }

    public void moveRight() {
        play = true;
        playerX += 15;
    }

    public void actionPerformed(ActionEvent e) {

        timer.start();
        if(play) {

            //to move the ball when game is started
            ballposX += ballXdir;
            ballposY += ballYdir;

            if(ballposX < 0)
            {
                ballXdir = -ballXdir;
            }
            if(ballposY < 0)
            {
                ballYdir = -ballYdir;
            }
            if(ballposX > 670)
            {
                ballXdir = -ballXdir;
            }

            //collision between ball and paddle
            if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8)))
            {
                ballYdir = -ballYdir;
            }

            // check map collision with the ball
            //here in map.map.length first map is object we have created and second map is the 2d array we have created

            A: for(int i = 0; i<map.map.length; i++)
            {
                for(int j =0; j<map.map[0].length; j++)
                {
                    if(map.map[i][j] > 0)
                    {
                        //for intersection we need to first detect the position of ball and brick with respect to height and width of the brick
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
                        Rectangle brickRect = rect;

                        if(ballRect.intersects(brickRect))
                        {
                            map.setBrickValue(0, i, j);
                            score+=5;
                            totalbricks--;

                            // when ball hit right or left of brick
                            if(ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width)
                            {
                                ballXdir = -ballXdir;
                            }
                            // when ball hits top or bottom of brick
                            else
                            {
                                ballYdir = -ballYdir;
                            }

                            break A;
                        }
                    }
                }
            }



            repaint();
        }
    }

}