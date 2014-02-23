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
  public    AnimationManager       anim;
  public    Player                 player;
  public    int                    health;
  public    BetterCharacterControl monsterControl;
  public    int                    attackDelay;
    
   //Monster Health Methods  
  
    public int getHealth(Monster monster){
      return monster.health;
      }
    
    public void changeHealth(Monster monster, int change, Player player, SoundManager audio){
      Random rand = new Random();
      float soundOdds = rand.nextInt(15) + 1; 
      if(soundOdds == 10)
      audio.zombieSounds(monster);
      int currentHealth = monster.getHealth(monster);
      if (currentHealth > 0)
      monster.health = currentHealth + change;
      else
      Die(monster, player);
      
      }
    
    public void Die(Monster monster, Player player) {
      player.changeKillCount(player, 1);
      monster.monsterControl.getPhysicsSpace().remove(monster.monsterControl);
      monster.removeFromParent();
      
      }
    
   //Monster Location Methods  
    
    public void monsterSetLocation(Monster monster) {
      Random rand = new Random();
      float firstNumber = rand.nextInt(150) + 5; 
      float secondNumber = rand.nextInt(150) + 5;
      monster.monsterControl.warp(new Vector3f(firstNumber, 0f, secondNumber));
      }
    
    public Vector3f GetLocation(Monster monster){
      return monster.Model.getLocalTranslation();
      }
    

     //Monster Attack Methods  
    
    public void attack(Spatial monster, Player player) {
      anim.animChange("Punch", "StillLegs", monster);
      player.changeHealth(player, -3);
      }
    
    //Monster dropItem Methods 
    
    public void dropItem(SceneManager item, Monster monster){
      Random rand = new Random();
      Node n = (Node) monster.getParent().getParent().getChild("Grabbables");
      float dropChance = rand.nextInt(50) + 1; 
      if (dropChance == 2) {
        n.attachChild(item.makeAmmo("Ammo", monster.Model.getLocalTranslation().x, monster.Model.getLocalTranslation().z));
        }
      if (dropChance == 3) {
        n.attachChild(item.makeHealth("Health", monster.Model.getLocalTranslation().x, monster.Model.getLocalTranslation().z));
        }
      }
    
}
