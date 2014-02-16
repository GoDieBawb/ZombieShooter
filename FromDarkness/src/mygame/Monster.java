/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Bob
 */
public class Monster extends Node {
  public    SimpleApplication      app;
  public    AppStateManager        stateManager;
  public    AssetManager           assetManager;
  public    BulletAppState         physics;
  public    Node                   Model;
  public    Node                   rootNode;
  public    AnimationAppState      anim;
  public    Player                 player;
  public    int                    monsterHealth;
  public    BetterCharacterControl monsterControl;
    
    public int getHealth(Monster monster){
      return monster.monsterHealth;
      }
    
    public void changeHealth(Monster monster, int change){
      int currentHealth = monster.getHealth(monster);
      monster.monsterHealth = currentHealth - change;
      }
    
    public void monsterSetLocation(Monster monster) {
      Random rand = new Random();
      float firstNumber = rand.nextInt(150) + -150; 
      float secondNumber = rand.nextInt(150) + -150;
      monster.monsterControl.warp(new Vector3f(firstNumber, 0f, secondNumber));
      }
    
    public void monsterGetLocation(Monster monster){
      monster.Model.getLocalTranslation();
      }
    
    public void monsterAttack(Spatial monster, Player player) {
      anim.animChange("Punch", "StillLegs", monster);
      System.out.println(player.getHealth(player));
      if (player.getHealth(player) > 0) {
        player.changeHealth(player, -3);
        } else {
        System.out.println("Death");
        }
      }
    
}
