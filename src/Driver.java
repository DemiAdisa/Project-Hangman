import javax.swing.*;

/*
This Class Serves as the Driver to This Game
 */
public class Driver
{
    public static void main(String[] args) throws Exception
    {
        JFrame frame = new WordGenerator();
        frame.setTitle("HANGMAN FOR THE COMPETITIVE PLAYER");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);

    }

}
