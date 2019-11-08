package snakeProj;
 
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
 
public class snakefini extends JPanel{
 
    private final int WIDTH = 50;
    private Deque<PartieSerpent> snake = new ArrayDeque<>();
    private Point oeuf = new Point(0,0);
    private Random rand = new Random();
    private boolean isGrowing = false;
    private boolean gameLost = false;
    private int offset = 0;
    private int newDirection = 39;
    

    public static void main(String[] args) {
    	
    	//Fenêtre
        JFrame frame = new JFrame("Snake");
        ImageIcon icone = new ImageIcon("Fond.jpg");
        frame.setIconImage(icone.getImage());
        
        snakefini panel = new snakefini();
        frame.addKeyListener(new KeyListener() {
           
            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
               
            }
           
            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
               
            }
           
            @Override
            public void keyPressed(KeyEvent e) {
                panel.onKeyPressed(e.getKeyCode());
            }
        });
        frame.setContentPane(panel);
        frame.setSize(15*50, 15*50); 
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
 
    public snakefini() {
        creerOeuf();
        snake.add(new PartieSerpent(0, 0, 39));
        setBackground(Color.WHITE);
        new Thread(new Runnable() {
           
            @Override
            public void run() {
                while(true) {
                    repaint();
                    try {
                        Thread.sleep(1000/60l);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
   
    public void creerOeuf() {
        boolean positionAvailable;
        do {
            oeuf.x = rand.nextInt(12);
            oeuf.y = rand.nextInt(12);
            positionAvailable = true;
            for(PartieSerpent p : snake) {
                if(p.x == oeuf.x && p.y == oeuf.y) {
                    positionAvailable = false;
                    break;
                }
            }
        } while(!positionAvailable);
    }
   
    @Override
    protected void paintComponent(Graphics objet) {
        super.paintComponent(objet);
       
        if(gameLost) {
            objet.setColor(Color.RED);
            objet.setFont(new Font("Arial", 90, 90));
            objet.drawString("Partie terminée", 13*50/2 - objet.getFontMetrics().stringWidth("Partie terminée")/2, 13*50/2);
            return;
        }
       
        offset += 5;
        PartieSerpent head = null;
        if(offset == WIDTH) {
            offset = 0;
            try {
                head = (PartieSerpent) snake.getFirst().clone();
                head.move();
                head.direction = newDirection;
                snake.addFirst(head);
                if(head.x == oeuf.x && head.y == oeuf.y) {
                    isGrowing = true;
                    creerOeuf();
                }
                if(!isGrowing)
                    snake.pollLast();
                else
                    isGrowing = false;
            } catch (CloneNotSupportedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
       
        objet.setColor(Color.LIGHT_GRAY);
        objet.fillOval(oeuf.x*WIDTH + WIDTH/4, oeuf.y*WIDTH + WIDTH/4, WIDTH/2, WIDTH/2);
       
        objet.setColor(Color.RED);
        for(PartieSerpent p : snake) {
            if(offset == 0) {
                if(p != head) {
                    if(p.x == head.x && p.y == head.y) {
                        gameLost = true;
                    }
                }
            }
            if(p.direction == 37 || p.direction == 39) {
                objet.fillRect(p.x * WIDTH + ((p.direction == 37) ? -offset : offset), p.y*WIDTH, WIDTH, WIDTH);
            } else {
                objet.fillRect(p.x * WIDTH, p.y*WIDTH + ((p.direction == 38) ? -offset : offset), WIDTH, WIDTH);
            }
        }
       
        objet.setColor(Color.BLUE);
        objet.drawString("Score : "+(snake.size() -1), 9, 15);
       
    }
   
    public void onKeyPressed(int keyCode) {
        if(keyCode >= 37 && keyCode <= 40) {
            if(Math.abs(keyCode - newDirection) != 2) {
                newDirection = keyCode;
            }
        }
    }
   
    class PartieSerpent {
       
        public int x, y, direction;
       
        public PartieSerpent(int x, int y, int direction) {
            this.x = x;
            this.y = y;
            this.direction = direction;
        }
       
        public void move() {
            if(direction == 37 || direction == 39) {
                x += (direction == 37) ? -1 : 1;
                if(x > 14)
                    x = -1;
                else if(x < -1)
                    x = 14;
            }else {
                y += (direction == 38) ? -1 : 1;
                if(y > 14)
                    y = -1;
                else if(y < -1)
                    y = 14;
            }
        }
       
        @Override
        protected Object clone() throws CloneNotSupportedException {
            return new PartieSerpent(x, y, direction);
        }
    } 
   
}