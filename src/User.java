import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class User
{
    String userName;
    int playerScore;
    File file = new File("GameFiles/Leaderboard.txt");

    //Default Constructor
    public User()
    {

    }

    public User(String name, int score)
    {
        userName = name;
        playerScore = score;
    }

    /**
     * Saves User attributes to a Leaderboard File
     * @throws Exception
     */
    public void saveUserDetails() throws Exception
    {
        FileWriter fileWriter = new FileWriter(file, true);
        BufferedWriter writeToFile = new BufferedWriter(fileWriter);

        writeToFile.write(userName);
        writeToFile.write(" ");
        writeToFile.write(playerScore + "points\n");

        writeToFile.close();
    }


    /**
     * Reads through the Leaderboard file and pushes them into a List
     *
     * @return List containing all the Users in the Leaderboard file
     * @throws Exception
     */
    public List<String> getLeaderboard() throws Exception
    {
        List<String> fileLines = Files.lines(Paths.get("GameFiles/Leaderboard.txt"))
                .sorted()
                .collect(Collectors.toList());

        return fileLines;

    }
}
