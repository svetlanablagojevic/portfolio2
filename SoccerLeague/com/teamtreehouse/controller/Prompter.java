package com.teamtreehouse.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import com.teamtreehouse.model.Player;
import com.teamtreehouse.model.Players;
import com.teamtreehouse.model.Team;




public class Prompter {


    private BufferedReader mReader;
    private Map<String, String> mMenu;
    TreeSet<Team> teams = new TreeSet<>();
    
    Player[] registeredPlayers = Players.load();
    TreeSet<Player> availablePlayers = new TreeSet<>();
       
     
   //konstruktor klase Prompter da se definise mReader promenjiva
    public Prompter() {
        mReader = new BufferedReader(new InputStreamReader(System.in));        
    }
  private String promptMenu() throws IOException{
      
        mMenu = new TreeMap<String, String>();
        mMenu.put("1", "Create a new team");
        mMenu.put("2", "Add a player to a team");
        mMenu.put("3", "Remove a player from a team");
        mMenu.put("4", "Print roster of players in a team");
        mMenu.put("5", "Print height report");
        mMenu.put("6", "League Balance Report");
        mMenu.put("7", "Exit the application");
      
    System.out.printf("%n%nChoose an item from: %n");
    
    for(Map.Entry<String, String> option : mMenu.entrySet())
    {
      System.out.printf("%s. %s %n",option.getKey(),option.getValue());
    }
    System.out.printf("Your choice: ");
    String choice = mReader.readLine();
    return choice.trim().toLowerCase();
  }
  
  //method that checks if string input is valid
  private String getValidStringInput(String message) throws IOException {
        
        String input =mReader.readLine();
        if (input.isEmpty()) {
            System.out.println("Your input cannot be empty.");
            return getValidStringInput(message);
        } else if (!input.matches("^[-a-zA-Z' ]*$")) {
            System.out.println("Your input must contain only alphabetical characters.");
            return getValidStringInput(message);
        } else {
            return input;
        }
    }
  //method that checks if int input is valid
   private boolean isValidInt(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception e) {
            System.out.println("Your input has to be a whole number.\n");
            return false;
        }
    }
   //method that checks if selected number is in range
     private boolean isValidSelection(String input, int minInt, int maxInt) {
        if(!isValidInt(input)) return false;
        int selection = Integer.parseInt(input);
        if(selection < minInt || selection > maxInt) {
            System.out.printf("You have to enter a whole number between %d and %d.\n", minInt,maxInt);
            return false;
        } else {
            return true;
        }
    }
  
   public Team promptForTeamCreation() throws IOException{
       
        System.out.println("Enter the name of a team:");
        String teamName = getValidStringInput("Enter the name of a team:");
        System.out.println("Enter the coach's name:");
        String coach = getValidStringInput("Enter the coach's name:");
        System.out.printf("Created team is %s.\n", teamName);
        
        return new Team(teamName, coach);
    }
   
   //lists all the players
    public static void playersTable(Set<Player> players) { 
        if (players.isEmpty()) {
            System.out.printf("Error. There are no players in a team.");
            return;
        }
    
        System.out.printf("ID, LAST NAME, FIRST NAME, HEIGHT (in.),  EXPERIENCE\n");
        int counter = 1;
        for (Player player : players) {
            System.out.printf("%2d - %-15s %-15s %-15s %-15s\n",
                    counter++,
                    player.getLastName(),
                    player.getFirstName(),
                    player.getHeightInInches(),
                    player.isPreviousExperience());
        }
    }
       
   
   public Player playerSelectionMenu(Set<Player> players) throws IOException {
        ArrayList<Player> playerList = new ArrayList<>();
        for (Player player: players) {
            playerList.add(player);
        }
        System.out.println("Choose a player.");
        
        playersTable(players);
        
        String selection = mReader.readLine();
        if(isValidSelection(selection, 1, playerList.size())) {
            return playerList.get(Integer.parseInt(selection) - 1);
        } else {
            //if the selection is not valid return the list of all players to select again
            return playerSelectionMenu(players);
        }
        
       }
   
    public Team teamSelectionMenu(Set<Team> teams) throws IOException{
        
        ArrayList<Team> teamList = new ArrayList<>();
        System.out.println("Select team:\n");
        
        for (Team team : teams) {
            teamList.add(team);
            System.out.printf("%d - %s\n",teamList.indexOf(team) + 1, team.getTeamName());
        }

        String selection = mReader.readLine();
        if(isValidSelection(selection, 1, teamList.size())) {
            return teamList.get(Integer.parseInt(selection) - 1);
        } else {
            return teamSelectionMenu(teams);
        }
    }
    
      public static void teamExperienceReport(Team team) {
        System.out.println("\nEXPERIENCE REPORT");
        System.out.printf("experienced players:   %-2d\n", team.getExperiencedPlayerCount());
        System.out.printf("inexperienced players: %-2d\n", team.getInexperiencedPlayerCount());
        int xpLevel = team.getExperiencePercentage();
        System.out.printf("team experience percentage: %d%%\n\n", xpLevel);
    }
      
      
      public static void heightOfPlayersReport(Set<Player> players) {
          int range1=0;
          int range2=0;
          int range3=0;
          
         for (Player player:players) {
            
             if (player.getHeightInInches()>=35 && player.getHeightInInches()<=40) {
                 range1++;
             }
             if (player.getHeightInInches()>=41 && player.getHeightInInches()<=46) {
                 range2++;
             }
             if (player.getHeightInInches()>=47 && player.getHeightInInches()<=50) {
                 range3++;
             }
         }
          System.out.println("\nHEIGHT RANGE");
          System.out.printf("There are %d players in a team within a range height 35-40 \n", range1);
          System.out.printf("There are %d players in a team within a range height 41-46 \n", range2);
          System.out.printf("There are %d players in a team within a range height 47-50 \n", range3);
      }
      
      public static void heightCountReport(Set<Player> players) {
        //create and populate a TreeMap with height count data
        TreeMap<Integer, Integer> heightCounts = new TreeMap<>();
        for(Player player : players) {
            heightCounts.put(player.getHeightInInches(), 0); 
        }
        for(Player player : players) {
            for(Map.Entry<Integer, Integer> entry : heightCounts.entrySet()) {
                Integer key = entry.getKey();
                Integer value = entry.getValue();
                if (player.getHeightInInches() == key) {
                    heightCounts.put(key, value + 1);
                }
            }
        }
        
        System.out.println("\nHEIGHT(in.) Number of players");
        for(Map.Entry<Integer, Integer> entry : heightCounts.entrySet()) {
            System.out.printf("%-10d%-5d\n", entry.getKey(), entry.getValue());
        }
     }
     
     public void run() {
         String choice = "";
         //for the beginning put all registreted players to available players Set
          for (Player player : registeredPlayers) {
            availablePlayers.add(player);
           }
            do {
            Team selectedTeam;
            Player selectedPlayer;
 
            try {
                choice = promptMenu();
                switch (choice) {
                    case "1":
                        if (teams.size() * Team.MAX_PLAYERS < registeredPlayers.length) { 
                            teams.add(promptForTeamCreation());
                            } else {
                            System.out.printf("There are too many teams crated.");
                            }
                        break;
                     case "2":
                        //add a player
                        if(teams.size()==0) {
                         System.out.printf("You have to create a team first.");   
                        }
                        else {
                         selectedTeam = teamSelectionMenu(teams);
                         selectedPlayer = playerSelectionMenu(availablePlayers);
                        
                            if (selectedTeam.getPlayers().size() < 11) {
                                
                                selectedTeam.addPlayer(selectedPlayer);
                                availablePlayers.remove(selectedPlayer);
                               
                                System.out.printf("Player %s added to the team %s", selectedPlayer, selectedTeam);
                            } else {
                                System.out.printf("There are more than 11 players in a team.");
                            }
                        }
                         break;
                         
                    case "3":
                        //remove player
                        if(teams.size()==0) {
                         System.out.printf("You have to create a team first.");   
                        }
                        selectedTeam = teamSelectionMenu(teams);
                        if(selectedTeam.getPlayers().size()==0) {
                         System.out.printf("There are no players in a team to remove.");
                        }
                        else {
                               selectedPlayer = playerSelectionMenu(selectedTeam.getPlayers());
                               selectedTeam.getPlayers().remove(selectedPlayer);
                               availablePlayers.add(selectedPlayer); 
                               System.out.printf("Player %s removed froma the team %s", selectedPlayer, selectedTeam);
                        }  
                         break;
                         
                    case "4":
                        //print a roster of players in a team
                        if(teams.size()==0) {
                         System.out.printf("You have to create a team first.");   
                        }
                        selectedTeam = teamSelectionMenu(teams);
                        System.out.printf("Rooster for the team %s, name of the coach %s. \n", selectedTeam.getTeamName(), selectedTeam.getCoach());
                        playersTable(selectedTeam.getPlayers());
                       
                        break;
                     case "5":
                         //Height Report
                         if(teams.size()==0) {
                         System.out.printf("You have to create a team first.");   
                        }
                        selectedTeam = teamSelectionMenu(teams);
                        heightCountReport(selectedTeam.getPlayers());
                        break;
                     case "6":
                         //Laegue Balance Report
                         if(teams.size()==0) {
                         System.out.printf("You have to create a team first.");   
                        }
                        for(Team team:teams) {
                        System.out.printf("\nLeague Balance report for team %s: \n", team.getTeamName());
                        teamExperienceReport(team);
                        heightOfPlayersReport(team.getPlayers());
                        }
                         break;
                    case "7":
                        System.out.println("Goodbye!");
                        break;
                }
            } catch (IOException ioe) {
                System.out.println("Problem with input");
                ioe.printStackTrace();
            }
        } while (!choice.equals("7"));
    }
     
}

