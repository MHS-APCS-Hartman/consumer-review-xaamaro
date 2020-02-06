import java.util.Scanner;
import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;
import java.io.*;

/**
 * Class that contains helper methods for the Review Lab
 **/
public class Review {
  
  private static HashMap<String, Double> sentiment = new HashMap<String, Double>();
  private static ArrayList<String> posAdjectives = new ArrayList<String>();
  private static ArrayList<String> negAdjectives = new ArrayList<String>();
 
  
  private static final String SPACE = " ";
  
  static{
    try {
      Scanner input = new Scanner(new File("cleanSentiment.csv"));
      while(input.hasNextLine()){
        String[] temp = input.nextLine().split(",");
        sentiment.put(temp[0],Double.parseDouble(temp[1]));
        //System.out.println("added "+ temp[0]+", "+temp[1]);
      }
      input.close();
    }
    catch(Exception e){
      System.out.println("Error reading or parsing cleanSentiment.csv");
    }
  
  
  //read in the positive adjectives in postiveAdjectives.txt
     try {
      Scanner input = new Scanner(new File("positiveAdjectives.txt"));
      while(input.hasNextLine()){
        String temp = input.nextLine().trim();
        System.out.println(temp);
        posAdjectives.add(temp);
      }
      input.close();
    }
    catch(Exception e){
      System.out.println("Error reading or parsing postitiveAdjectives.txt\n" + e);
    }   
 
  //read in the negative adjectives in negativeAdjectives.txt
     try {
      Scanner input = new Scanner(new File("negativeAdjectives.txt"));
      while(input.hasNextLine()){
        negAdjectives.add(input.nextLine().trim());
      }
      input.close();
    }
    catch(Exception e){
      System.out.println("Error reading or parsing negativeAdjectives.txt");
    }   
  }
  
  /** 
   * returns a string containing all of the text in fileName (including punctuation), 
   * with words separated by a single space 
   */
  public static String textToString( String fileName )
  {  
    String temp = "";
    try {
      Scanner input = new Scanner(new File(fileName));
      
      //add 'words' in the file to the string, separated by a single space
      while(input.hasNext()){
        temp = temp + input.next() + " ";
      }
      input.close();
      
    }
    catch(Exception e){
      System.out.println("Unable to locate " + fileName);
    }
    //make sure to remove any additional space that may have been added at the end of the string.
    return temp.trim();
  }
  
  /**
   * @returns the sentiment value of word as a number between -1 (very negative) to 1 (very positive sentiment) 
   */
  public static double sentimentVal( String word )
  {
    try
    {
      return sentiment.get(word.toLowerCase());
    }
    catch(Exception e)
    {
      return 0;
    }
  }
  
  /**
   * Returns the ending punctuation of a string, or the empty string if there is none 
   */
  public static String getPunctuation( String word )
  { 
    String punc = "";
    for(int i=word.length()-1; i >= 0; i--){
      if(!Character.isLetterOrDigit(word.charAt(i))){
        punc = punc + word.charAt(i);
      } else {
        return punc;
      }
    }
    return punc;
  }
  
  /** 
   * Randomly picks a positive adjective from the positiveAdjectives.txt file and returns it.
   */
  public static String randomPositiveAdj()
  {
    int index = (int)(Math.random() * posAdjectives.size());
    return posAdjectives.get(index);
  }
  
  /** 
   * Randomly picks a negative adjective from the negativeAdjectives.txt file and returns it.
   */
  public static String randomNegativeAdj()
  {
    int index = (int)(Math.random() * negAdjectives.size());
    return negAdjectives.get(index);
    
  }
  
  /** 
   * Randomly picks a positive or negative adjective and returns it.
   */
  public static String randomAdjective()
  {
    boolean positive = Math.random() < .5;
    if(positive){
      return randomPositiveAdj();
    } else {
      return randomNegativeAdj();
    }
  }
  
  /**
   * Returns the word after removing any beginning or ending punctuation
   */
  public static String removePunctuation( String word )
  {
    while(word.length() > 0 && !Character.isAlphabetic(word.charAt(0)))
    {
      word = word.substring(1);
    }
    while(word.length() > 0 && !Character.isAlphabetic(word.charAt(word.length()-1)))
    {
      word = word.substring(0, word.length()-1);
    }
    
    return word;
  }
  
 /* Takes an online review and returns the total sentiment value of that review. */
  public static double totalSentiment(String fileName)
  {
      double total = 0;
      String word = "";
      String reviewText = textToString(fileName);

      // moves through the entire review
      for (int i = 0; i < reviewText.length(); i++)
      {
         // checks if a word has been completed
        if (reviewText.substring(i, i+1).equals(" ") || i + 1 == reviewText.length())
         {
            total += sentimentVal(removePunctuation(word));
            word = "";
         }
         else
         {
            word += reviewText.substring(i, i+1);
         }
      }
      return total;
  }

    
  public static int starRating(String fileName)
   {
     double sentiment = totalSentiment(fileName);
     int rating;

     if (sentiment < 0)
     {
       rating = 1;
     }
     else if (sentiment < 3)
     {
       rating = 2;
     }
     else if (sentiment < 6)
     {
       rating = 3;
     }
     else if (sentiment < 30)
     {
       rating = 4;
     }
     else
     {
       rating = 5;
     }
     return rating;
  }
  
  /* Returns a computer generated fake online review. 
  Precondition: fileName must be a .txt file. */
    public static String fakeReview(String fileName)
    {
      String word = "";
      String reviewText = textToString(fileName);
      String newReview = "";
      
      // goes through the entire review
      for (int i = 0; i < reviewText.length(); i++)
      {
          if (reviewText.substring(i, i+1).equals(" ") || i == reviewText.length() -1)
          {
              if (i == reviewText.length() -1) //adds last letter to the review
              {
                  word += reviewText.substring(i, i+1);
              }
              
              // finds the adjectives that start with * and changes them
              if (word.startsWith("*"))
              {
                  String newAdjective = "";
                  while (newAdjective.equals(""))
                  {
                      newAdjective = randomAdjective();
                  }
                  // replaces the old adjective with the new and resets word
                  newReview += newAdjective + getPunctuation(word) + " ";
                  word = "";
              }
              else
              {
                  newReview += word + " ";
                  word = "";
              }
          }
          else
          {
              word += reviewText.substring(i, i+1);
          }
      }
      return newReview;
    }

/* Returns a computer generated online review that can be either positive or negative. 
Precondition: fileName must be a .txt file. */
    public static String fakeReviewStronger(String fileName)
    {
        String word = "";
        String reviewText = textToString(fileName);
        String newReview = "";
        
        // goes through the entire review
        for (int i = 0; i < reviewText.length(); i++)
        {
             if (reviewText.substring(i, i+1).equals(" ") || i == reviewText.length() -1)
             {
                   if (i == reviewText.length() -1) //adds last letter to the review
                   {
                      word += reviewText.substring(i, i+1);
                   }
                 
                 // finds the adjectives that start with *
                 if (word.startsWith("*"))
                 {
                     // gets the sentiment value of the word and replaces it with a positive or negative adjective
                     double sentiment = sentimentVal(word);
                     String newAdjective = "";
                     
                     if (sentiment > 0)
                     {
                         while (newAdjective.equals("") || sentimentVal(newAdjective) <= sentiment)
                         {
                           newAdjective = randomPositiveAdj();
                         }
                     }
                     else if (sentiment < 0)
                     {
                         while (newAdjective.equals("") || sentimentVal(newAdjective) >= sentiment)
                         {
                           newAdjective = randomNegativeAdj();
                         }
                     }
                     else
                     {
                        word = word.substring(1);
                        newAdjective = removePunctuation(word);
                     }
                     
                     //replaces the old adjective with the new adjective
                     newReview += newAdjective + getPunctuation(word) + " ";
                     word = "";
                 }
                 else
                 {
                     newReview += word + " ";
                     word = "";
                 }
               }
               else
               {
                  word += reviewText.substring(i, i+1);
               }

        }
        return newReview;
    }
}

