/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.Random;

/**
 *
 * @author Bob
 */
public class Monster extends Node {
  public    Node                   Model;
  public    AnimationAppState      anim;
  public    Player                 player;
  public    int                    health;
  public    BetterCharacterControl monsterControl;
  public    int                    attackDelay;
    
    public int getHealth(Monster monster){
      return monster.health;
      }
    
    public void changeHealth(Monster monster, int change, Player player){
      System.out.println("Target Health: " + monster.getHealth(monster));
      int currentHealth = monster.getHealth(monster);
      if (currentHealth > 0)
      monster.health = currentHealth + change;
      else
      Die(monster, player);
      }
    
    public void Die(Monster monster, Player player) {
      player.changeKillCount(player, 1);
      monster.removeFromParent();
      }
    
    public void monsterSetLocation(Monster monster) {
      Random rand = new Random();
      float firstNumber = rand.nextInt(150) + 5; 
      float secondNumber = rand.nextInt(150) + 5;
      monster.monsterControl.warp(new Vector3f(firstNumber, 0f, secondNumber));
      }
    
    public void monsterGetLocation(Monster monster){
      monster.Model.getLocalTranslation();
      }
    

    public void monsterAttack(Spatial monster, Player player) {
      anim.animChange("Punch", "StillLegs", monster);
      player.changeHealth(player, -3);
      }
    
}
