import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This Class is Responsible for Generating the Word and
 * performing the necessary checks and operations on it
 * <p>
 * Also responsible for the GUI elements
 */
public class WordGenerator extends JFrame
{
    private final int WIDTH = 700;
    private final int HEIGHT = 400;
    private int life = 10;
    private Map<String, String> wordAndHints = new HashMap<>();
    private List<String> wordValue;
    int playerScore = 0;
    String word, wordForCode, userName;
    char[] wordBlanks;
    User userStuff = new User();


    JButton submitButton, hintButton, leaderBoard, saveButton, instructionsButton;
    JTextField guessField, userField;
    JTextArea gameTerminal;
    JPanel buttonPanel, centerPanel, areaPanel;
    JLabel hint = new JLabel();
    JLabel userLabel = new JLabel("Username:");
    JLabel guessLabel = new JLabel("~Guess");
    JLabel lifeLeft = new JLabel("Life Remaining: " + life);


    public WordGenerator() throws Exception
    {
        setSize(WIDTH, HEIGHT);
        readGameFile();//Reads the File

        word = getWord();
        wordForCode = word.toUpperCase();

        createPanels();
        createButtons();
        createFields();

        generateWordForUse();

    }

    private void generateWordForUse()
    {
        wordBlanks = new char[word.length()];

        for (int i = 0; i < word.length(); i++)
        {
            if (word.charAt(i) == ' ')
            {
                wordBlanks[i] = ' ';
            } else
            {
                wordBlanks[i] = '~';
            }
        }

        gameTerminal.append("New Word Generated\n");
        gameTerminal.append("Guess a Letter\n");
        gameTerminal.append(String.valueOf(wordBlanks) + "\n");
    }

    /**
     * This method reads the "WordList.txt" file
     * which contains Words to be used in the Game
     * and Puts the Words and Its Hint into a Map
     */
    private void readGameFile() throws Exception
    {
        Scanner lineReader;
        wordValue = new ArrayList<>();

        //Stream to read all the lines in a file and store in a list
        List<String> linesInFile = Files.lines(Paths.get("GameFiles/WordList.txt"))
                .collect(Collectors.toList());

        for (String line : linesInFile)
        {
            lineReader = new Scanner(line).useDelimiter("-");
            String wordSupplied = lineReader.next();
            String associatedHint = lineReader.next();

            wordAndHints.put(wordSupplied, associatedHint);
        }

        Set<String> words = wordAndHints.keySet();
        wordValue.addAll(words);
    }

    private void createButtons()
    {
        submitButton = new JButton("SUBMIT");
        ActionListener submitButtonAction = new SubmitButtonListener();
        submitButton.addActionListener(submitButtonAction);

        saveButton = new JButton("SAVE");
        ActionListener saveButtonAction = new SaveButtonListener();
        saveButton.addActionListener(saveButtonAction);

        leaderBoard = new JButton("LEADERBOARD");
        ActionListener leader = new LeaderboardListener();
        leaderBoard.addActionListener(leader);

        hintButton = new JButton("GET HINT");
        ActionListener hintButtonAction = new HintButtonListener();
        hintButton.addActionListener(hintButtonAction);

        instructionsButton = new JButton("How To Play");
        ActionListener howToPlay = new InstructionsButtonListener();
        instructionsButton.addActionListener(howToPlay);

        buttonPanel.add(instructionsButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(submitButton);
        buttonPanel.add(hintButton);
        buttonPanel.add(leaderBoard);
        buttonPanel.add(lifeLeft);
    }

    private void createPanels()
    {
        buttonPanel = new JPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        centerPanel = new JPanel();
        centerPanel.add(hint);
        add(centerPanel, BorderLayout.CENTER);

        areaPanel = new JPanel();
        add(areaPanel, BorderLayout.NORTH);

    }

    private void createFields()
    {
        gameTerminal = new JTextArea(15, 50);
        gameTerminal.setEditable(false);
        JScrollPane scroll = new JScrollPane(gameTerminal);
        areaPanel.add(scroll);

        areaPanel.add(userLabel);
        userField = new JTextField(5);
        areaPanel.add(userField);

        guessField = new JTextField(3);
        centerPanel.add(guessLabel);
        centerPanel.add(guessField);
    }

    /**
     * This method serves as a Word Getter for this Class
     *
     * @return Randomly Selected Word
     */
    public String getWord()
    {
        int range = wordValue.size();
        int randomNumber = (int) (Math.random() * range);

        String wordToGuess = wordValue.get(randomNumber);

        return wordToGuess;
    }

    /**
     * Gets the Hint associated with Supplied Word String
     *
     * @param w Word in which hint is requested
     * @return Hint of Supplied Word
     */
    public String getHint(String w)
    {
        String hint = wordAndHints.get(w);
        return hint;
    }

    class InstructionsButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            try
            {
                List<String> instructions = Files.lines(Paths.get("GameFiles/HowToPlay"))
                        .collect(Collectors.toList());

                for(String line : instructions)
                {
                    gameTerminal.append(line +"\n");
                }
            } catch (IOException ex)
            {
                ex.printStackTrace();
            }



        }
    }
    class HintButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            hint.setText(getHint(word));
            playerScore -= 5;

        }
    }

    class LeaderboardListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            try
            {
                List<String> linesOfFile = userStuff.getLeaderboard();
                gameTerminal.append("Here is the Leaderboard\n");

                for (String line : linesOfFile)
                {
                    gameTerminal.append(line + "\n");
                }
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    class SaveButtonListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            userName = userField.getText();
            User newUser = new User(userName, playerScore);
            try
            {
                newUser.saveUserDetails();
                gameTerminal.append("Profile Saved\n");
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    class SubmitButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            char guess = guessField.getText().toUpperCase().charAt(0);

            /*Slight Bug here
            When word is completed it waits for the submit button to be clicked before progressing
            Fix later
             */
            if (wordForCode.equals(String.valueOf(wordBlanks)))
            {
                gameTerminal.append("Correct, On to the Next One!\n");
                word = getWord();
                wordForCode = word.toUpperCase();
                playerScore += 10;
                generateWordForUse();

            } else if (life > 0 && wordForCode.contains(guess + ""))
            {
                for (int i = 0; i < wordForCode.length(); i++)
                {
                    if (wordForCode.charAt(i) == guess)
                    {
                        wordBlanks[i] = guess;

                    }
                }

                gameTerminal.append(String.valueOf(wordBlanks) + "\n");
            } else if (life == 0)
            {
                gameTerminal.append("Life Depleted, Game Over!\n");
                gameTerminal.append("Score Achieved: " + playerScore + "\n");

                userName = userField.getText();

                gameTerminal.append("Click Save Button to Store Username and " +
                        "Score to Leaderboard\n");
            } else
            {
                life--;
                gameTerminal.append(guess+" is an incorrect guess, Lost a Life, Try Again" + "\n");
                lifeLeft.setText("Life Remaining: " + life);
            }

        }
    }
}
