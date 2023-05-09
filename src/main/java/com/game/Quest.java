package com.game;

import java.util.ArrayList;
import java.util.HashMap;

public class Quest {
    private static Player hasLongestRoad;
    private static int longestRoadAmount;
    private static Player hasLongestArmy;
    private static int longestArmyAmount;
    private static HashMap<Player, Integer> armyAmounts = new HashMap<>();

    public Quest(ArrayList<Player> players){
        Quest.longestRoadAmount = 4;
        Quest.longestArmyAmount = 2;

        for(Player p: players){
            addArmyAmount(p, 0);
        }
    }
    public void setHasLongestRoad(Player player) {
        Quest.hasLongestArmy = player;
    }

    public Player getHasLongestRoad(){
        return Quest.hasLongestRoad;
    }

    public void setLongestRoadAmount(int amount){
        Quest.longestRoadAmount = amount;
    }

    public int getLongestRoadAmount(){
        return Quest.longestRoadAmount;
    }

    public static void setHasLongestArmy(Player player) {
        Quest.hasLongestArmy.subtractPoints(2);
        Quest.hasLongestArmy = player;
        Quest.hasLongestArmy.addPoints(2);
    }

    public Player getHasLongestArmy(){
        return Quest.hasLongestArmy;
    }

    public static void addArmyAmount(Player player, int amount){
        Quest.armyAmounts.put(player, Quest.armyAmounts.get(player)+amount);
        if( Quest.armyAmounts.get(player) > Quest.longestArmyAmount ){
            Quest.longestArmyAmount = Quest.armyAmounts.get(player);
            Quest.setHasLongestArmy(player);
        }
    }

    public HashMap<Player, Integer> getArmyAmounts(){
        return Quest.armyAmounts;
    }
}