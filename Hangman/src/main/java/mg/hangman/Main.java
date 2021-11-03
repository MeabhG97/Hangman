/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mg.hangman;

/**
 *
 * @author Meabh
 */
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) {
        String target = "";
        String guess = "";
        int numberOfLives = 0;
        int numberOfPlayers = 0;
        ArrayList<String> pastGuess = new ArrayList<>();
        ArrayList<String> targetVis = new ArrayList<String>();
        Scanner input = new Scanner(System.in);
        boolean playGame = true;
        boolean validTarget = false;
        boolean win = false;

        ArrayList<String> dictionary = new ArrayList<>();
        File words = new File("C:/Users/Meabh/OneDrive/Documents/Resources/words.txt");
        readFile(dictionary, words);
        
        
        
        while (playGame) {
            while(numberOfPlayers == 0){
                System.out.println("Please enter number of Players (1/2):");
                if(input.hasNextInt()){
                    numberOfPlayers = input.nextInt();
                    if(numberOfPlayers > 2){
                        System.out.println("Invalid input");
                        System.out.println("Enter 1 or 2 for number of players");
                        numberOfPlayers = 0;
                    }
                }
                else{
                    System.out.println("Invalid Input");
                    System.out.println("Enter 1 or 2 for number of players");
                    input.nextLine();
                }
            }
            
            if(!validTarget){
              target = getTarget(numberOfPlayers, input, dictionary);
              for(int i = 0; i < target.length(); i++){
                  targetVis.add("-");
              }
              
              validTarget = true;
              numberOfLives = 6;
              guess = "";
            }
            
            
            if(numberOfLives > 0 && !win){
                
                printGraphic(numberOfLives);
                System.out.println("Lives remaing: " + numberOfLives);
                
                guess = nextGuess(pastGuess, targetVis, input);
                int correct = checkGuess(target, guess);
                if(correct == 1){
                    System.out.println("You guessed a character");
                    targetVisable(target, guess, targetVis);
                    win = checkWin(targetVis);
                }
                else if(correct == -1){
                    win = true;
                }
                else{
                    System.out.println("Incorrect");
                    pastGuess.add(guess);
                    numberOfLives--;
                }
            }
            else{
                printGraphic(numberOfLives);
                if(win){
                    System.out.println("You win!");
                    System.out.println("You guessed the target word: " + 
                            target);
                }
                else{
                    System.out.println("You lose.");
                    System.out.println("The target word was: " + target);
                }
                playGame = playAgain(target, input);
                validTarget = false;
                numberOfPlayers = 0;
                input.nextLine();
            }
        }

    }
    
    //Creates Arraylist containing words in dictionary file
    public static void readFile(ArrayList<String> arrayList, File file) {

        try ( Scanner fileRead = new Scanner(file)) {
            while (fileRead.hasNext()) {
                arrayList.add(fileRead.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("words.txt not found");
        }
    }
    
    //Gets target word and checks if it is a valid word
    public static String getTarget(int numberOfPlayers, Scanner input,
            ArrayList<String> dictionary) {
        
        String target = "";
        boolean valid = false;
        Random randGen = new Random();
        
        input.nextLine();
        if(numberOfPlayers == 2){
            do {
                System.out.println("Enter target word");
                target = input.nextLine();
                int i = 0;
                while (!valid && (i < dictionary.size())) {
                    if (target.equalsIgnoreCase(dictionary.get(i))) {
                        valid = true;
                    }
                    i++;
                }
                if (!valid) {
                    System.out.println("***INVALID WORD***");
                }

            } while (!valid);
        }
        else{
            int x = randGen.nextInt(dictionary.size());
            target = dictionary.get(x);
        }
        
        return target;
    }
    
    //Gets next guess from user
    public static String nextGuess(ArrayList<String> pastGuess, 
            ArrayList<String> targetVis, Scanner input){
        
        boolean validGuess = false;
        String guess ="";
        
        while(!validGuess){
            printPastGuess(pastGuess);
            printTarget(targetVis);
            validGuess = true;
            
            System.out.println("Please enter next guess: ");
            guess = input.next();

            if(guess.length() > 1){
                for(int i = 0; i < guess.length(); i++){
                        if(!Character.isLetter(guess.charAt(i))){
                            validGuess = false;
                        }
                    }
            }
            else{
                if(!Character.isLetter(guess.charAt(0))){
                    validGuess = false;
                }
            }
            if(!validGuess){
                System.out.println("***INVALID GUESS***");
                System.out.println("**Please only enter letters or words.**");
            }
            
        }
        return guess;
    }
    
    //Prints past guesses from Arraylist pastGuess
    public static void printPastGuess(ArrayList<String> pastGuess){
        System.out.print("Past guesses: ");
        
        for(int i = 0; i < pastGuess.size(); i++){
            System.out.print(pastGuess.get(i) + " ");
        }
        System.out.println("");
    }
    
    public static void printTarget(ArrayList<String> targetVis){
        System.out.print("Target word: ");
        for(int i = 0; i < targetVis.size(); i++){
            System.out.print(targetVis.get(i));
        }
        System.out.println("");
    }
    
    /*
     *  Compares guess to target
     *  Returns 1 if guess is a single letter and contained in target
     *  Returns -1 if guess is a word and matches target
     *  Returns 0 if guess is not contained in target or matches target
     */
    public static int checkGuess(String target, String guess){
        int c = guess.length();
        int correct = 0;
        //Guessed single letter
        if(c == 1){
            //Target contains guessed letter
            if(target.contains(guess)){
                correct = 1;
            }
        }
        //Guess word
        else{
            //Target word matches guessed word
            if(target.equalsIgnoreCase(guess)){
                correct = -1;
            }
        }
        return correct;
    }
    
    //Asks if player wants to play again
    public static boolean playAgain(String target, Scanner input){
        System.out.println("Play again? Y/N");
        String s = input.next();
        if(s.equalsIgnoreCase("N")){
            return false;
        }
        return true;
    }
    
    //Adds correct char guesses to visable arraylist
    public static void targetVisable(String target, String guess, 
            ArrayList<String> targetVis){
        for(int i = 0; i < target.length(); i++){
            if(Character.toString(target.charAt(i)).equalsIgnoreCase(guess)){
                targetVis.set(i, guess);
            }
        }
    }
    
    //Checks if player has guess word by guessing the individual characters
    public static boolean checkWin(ArrayList<String> targetVis){
        int correctChar = 0;
        for(int i = 0; i < targetVis.size(); i++){
            if(!targetVis.get(i).equalsIgnoreCase("-")){
                correctChar++;
            }
        }
        if(correctChar == targetVis.size()){
            return true;
        }
        
        return false;
    }
    
    public static void printGraphic(int numberOfLives){
        System.out.println(" +---+");
        System.out.println(" |   |");
        
        if(numberOfLives < 6){
            System.out.println(" |   O");
        }
        else{
            System.out.println(" |");
        }
        
        if(numberOfLives < 5){
            if(numberOfLives < 3){
                System.out.println(" |  /|\\");
            }
            else if(numberOfLives < 4){
                System.out.println(" |  /|");
            }
            else{
                System.out.println(" |");
            }
        }
        else{
            System.out.println(" |");
        }
        
        if(numberOfLives < 2){
            if(numberOfLives < 1){
                System.out.println(" |  / \\");
            }
            else{
                System.out.println(" |  /");
            }
        }
        else{
            System.out.println(" |");
        }
        
        System.out.println(" |");
        System.out.println("=========");
        
    }
}
