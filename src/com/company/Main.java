package com.company;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * An implementation of a Maxwell's Demon GUI game
 *
 * @author Justin
 *
 */

public class Main extends JFrame implements ActionListener {

    JFrame wholeWindow;

    JPanel gamePanel;
    PlayArea gameArea;

    JPanel lowerPanel;
    JButton resetButton;
    JButton addButton;

    JLabel leftTemp;
    JLabel rightTemp;

    double leftT;
    double rightT;

    RedBall[] redBalls;
    BlueBall[] blueBalls;

    int redNum;
    int blueNum;

    Timer ticks;
    int tickNum;
    double delta = 0.05;
    /**
     * a main method to instantiate the Maxwell() class
     *
     * @param args
     */
    public static void main(String[] args) {
        new Main();

    }

    /**
     * the default constructor that creates the JFrames, JPanels, Timer, and
     * MouseAdapter
     */
    public Main() {

        // Naming the window and setting dimensions
        wholeWindow = new JFrame("Maxwell's Demon");
        wholeWindow.setSize(950, 550);
        wholeWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Adding game area panel
        gameArea = new PlayArea();
        wholeWindow.add(gameArea);

        // Adding lower panel for temperatures and reset/add particle buttons
        lowerPanel = new JPanel();
        lowerPanel.setBackground(Color.pink);
        lowerPanel.setLayout(new FlowLayout());
        JLabel emptyLabel = new JLabel("");
        JLabel emptyLabel2 = new JLabel("");
        JLabel emptyLabel3 = new JLabel("");
        JLabel emptyLabel4 = new JLabel("");
        JLabel emptyLabel5 = new JLabel("");
        leftTemp = new JLabel("Left Temp");
        rightTemp = new JLabel("Right Temp");
        resetButton = new JButton("Reset");
        addButton = new JButton("Add Particles");
        resetButton.addActionListener(this);
        addButton.addActionListener(this);
        lowerPanel.add(emptyLabel);
        lowerPanel.add(emptyLabel2);
        lowerPanel.add(emptyLabel3);
        lowerPanel.add(emptyLabel4);
        lowerPanel.add(emptyLabel5);
        lowerPanel.add(leftTemp);
        lowerPanel.add(resetButton);
        lowerPanel.add(addButton);
        lowerPanel.add(rightTemp);
        wholeWindow.add(lowerPanel, BorderLayout.SOUTH);

        // Adding mouse listener to open and close door
        gameArea.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                gameArea.doorClosed = false;
                gameArea.repaint();
            }
            public void mouseReleased(MouseEvent e) {
                gameArea.doorClosed = true;
                gameArea.repaint();
            }

        });

        // Creating the balls
        redNum = 0;
        blueNum = 0;
        redBalls = new RedBall[500];
        blueBalls = new BlueBall[500];
        makeBalls();

        // Create timer and start
        ticks = new Timer((int) (1000 * delta), this);
        ticks.start();

        wholeWindow.setVisible(true);

    }

    /**
     * the PlayArea subclass of JPanel that allows paintComponent to be
     * overridden and to draw the play area and door
     *
     *
     */
    public class PlayArea extends JPanel {

        boolean doorClosed = true;

        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Drawing the play area
            g.setColor(Color.green);
            g.fillRect(0, 0, 950, 550);

            // Drawing the wall
            if (doorClosed) {
                g.setColor(Color.black);
                g.drawRect(468, 0, 0, 477);
            }

            // Drawing the wall when door is open
            if (!doorClosed) {
                g.setColor(Color.green);
                g.drawRect(468, 0, 0, 477);

                g.setColor(Color.black);
                g.drawRect(468, 0, 0, 165);
                g.drawRect(468, 326, 0, 150);
            }

            // Drawing each ball
            for (int i = 0; i < redNum; i++) {
                redBalls[i].draw(g);
                blueBalls[i].draw(g);
            }

        }
    }

    /**
     *
     * the Ball class to keep track of each Ball's coordinates and velocities
     *
     */
    public class Ball {

        double x, y;
        double vx, vy;
        double oldx, oldy;
        double speed;
        boolean inLeft;
        double svx, svy;

        // default constructor
        public Ball() {
            x = (int) (Math.random() * 400 + 100);
            y = (int) (Math.random() * 400 + 100);

            vx = (int) (Math.random() * 100 - 50);
            vy = (int) (Math.random() * 100 - 50);

            if (x < 470) {
                inLeft = true;
            } else {
                inLeft = false;
            }

            svx = vx * vx;
            svy = vy * vy;
            speed = Math.sqrt(svy + svx);
        }

        public Ball(int xClick, int yClick) {
            x = xClick;
            y = yClick;

            vx = (int) (Math.random() * 100 - 50);
            vy = (int) (Math.random() * 100 - 50);

            if (x < 470) {
                inLeft = true;
            } else {
                inLeft = false;
            }

            svx = vx * vx;
            svy = vy * vy;
            speed = Math.sqrt(svy + svx);
        }

        // Moving each individual ball
        public void move(double delta) {
            oldx = x;
            oldy = y;
            x += vx * delta;
            y += vy * delta;

            if (x < 470) {
                inLeft = true;
            } else {
                inLeft = false;
            }

            stayOnScreen();
        }

        // Dictates whether or not the ball will bounce depending on walls and
        // if door is opened
        public void stayOnScreen() {
            if (gameArea.doorClosed) {
                if (x < 0) {
                    vx *= -1;
                }
                if (y < 0) {
                    vy *= -1;
                }
                if (x > 932) {
                    vx *= -1;
                }
                if (y > 476) {
                    vy *= -1;
                }
                if ((x > 463) && (x < 475)) {
                    vx *= -1;
                }
            }

            if (!gameArea.doorClosed) {
                if (x < 0) {
                    vx *= -1;
                }
                if (y < 0) {
                    vy *= -1;
                }
                if (x > 932) {
                    vx *= -1;
                }
                if (y > 476) {
                    vy *= -1;
                }
                if (((x > 463) && (x < 475)) && (y > 315 || y < 165)) {
                    vx *= -1;

                    if (x < 470) {
                        inLeft = true;
                    } else {
                        inLeft = false;
                    }
                }
            }
        }

        // Balls draw themselves
        public void draw(Graphics g) {

        }
    }

    // RedBalls inherit Ball
    public class RedBall extends Ball {
        RedBall() {
            super();
        }

        RedBall(int x, int y) {
            super(x, y);
        }

        public void draw(Graphics g) {
            g.setColor(Color.RED);
            g.fillOval((int) (x - 2), (int) (y - 2), 5, 5);
        }
    }

    // BlueBalls inherit Ball
    public class BlueBall extends Ball {
        BlueBall() {
            super();
        }

        BlueBall(int x, int y) {
            super(x, y);
        }

        public void draw(Graphics g) {
            g.setColor(Color.BLUE);
            g.fillOval((int) (x - 2), (int) (y - 2), 5, 5);
        }
    }

    public void makeBalls() {

        redBalls[redNum++] = new RedBall(150, 250);
        redBalls[redNum++] = new RedBall(650, 250);

        blueBalls[blueNum++] = new BlueBall(300, 250);
        blueBalls[blueNum++] = new BlueBall(800, 250);

    }

    public void moveBalls() {
        for (int i = 0; i < redNum; i++) {
            redBalls[i].move(4 * delta);
            blueBalls[i].move(2 * delta);
        }
    }

    // Computing the temperature based on the speed of the balls in each chamber
    public void computeTemp() {
        int leftBalls = 0;
        int rightBalls = 0;

        double leftSpeed = 0;
        double rightSpeed = 0;

        // for loop to loop through each ball array and calculate the speed in
        // each chamber
        for (int i = 0; i < redNum; i++) {
            double squaredBallSpeed = 0;
            if (redBalls[i].inLeft) {

                leftBalls++;
                squaredBallSpeed = redBalls[i].speed * redBalls[i].speed;
                leftSpeed = leftSpeed + squaredBallSpeed;

            } else {
                rightBalls++;
                squaredBallSpeed = redBalls[i].speed * redBalls[i].speed;
                rightSpeed = rightSpeed + squaredBallSpeed;
            }

            if (blueBalls[i].inLeft) {

                leftBalls++;
                squaredBallSpeed = blueBalls[i].speed * blueBalls[i].speed;
                leftSpeed = leftSpeed + squaredBallSpeed;

            } else {
                rightBalls++;
                squaredBallSpeed = blueBalls[i].speed * blueBalls[i].speed;
                rightSpeed = rightSpeed + squaredBallSpeed;
            }
        }

        leftT = leftSpeed / leftBalls;
        rightT = rightSpeed / rightBalls;

        leftTemp.setText("Left Temp: " + String.valueOf(leftT + " °"));
        rightTemp.setText("Right Temp: " + String.valueOf(rightT + " °"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // if actionEvent is from the ticks timer, move the balls and compute
        // temp
        if (e.getSource() == ticks) {
            moveBalls();
            computeTemp();
        }

        // if actionEvent is from the resetButton, reset the ball arrays and
        // make 4 new balls
        if (e.getSource() == resetButton) {
            RedBall[] newReds = new RedBall[500];
            BlueBall[] newBlues = new BlueBall[500];

            redBalls = newReds;
            blueBalls = newBlues;

            redNum = 0;
            blueNum = 0;

            makeBalls();

        }

        // if actionEvent is from the addButton, create 4 more balls
        if (e.getSource() == addButton) {
            makeBalls();
        }
        gameArea.repaint();
    }

}
