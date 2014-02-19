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
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import java.util.ArrayList;

/**
 *
 * @author Bob
 */
public class Player extends AbstractAppState {
   
public  SimpleApplication      app;
public  AppStateManager        stateManager;
public  AssetManager           assetManager;
public  BulletAppState         physics;
public  Node                   rootNode;
public  Node                   Model;
public  BetterCharacterControl playerControl;

public  ArrayList              inventory;
public  String                 heldItem;
public  Player                 player;
public  int                    health;
public  int                    killCount;
public  int                    ammo;
public  int                    attackDelay;



  @Override
  public void initialize(AppStateManager stateManager, Application app) {
    super.initialize(stateManager, app);
    
    this.app          = (SimpleApplication) app; // can cast Application to something more specific
    this.rootNode     = this.app.getRootNode();
    this.assetManager = this.app.getAssetManager();
    this.stateManager = this.app.getStateManager();
    this.physics      = this.stateManager.getState(BulletAppState.class);
    initPlayer();
    
   
   }
  
  
    public void initPlayer() {
      player = new Player();
      player.inventory = new ArrayList<String>();
       
       player.playerControl = new BetterCharacterControl(1f, 5f, 1f);
      
       player.Model = (Node) assetManager.loadModel("Models/Newman2/Newman2.j3o");
       player.health = 20;
       player.Model.setLocalTranslation(0f, 0f, 0f);
       player.Model.setLocalScale(.7f);
       player.Model.addControl(player.playerControl);
       player.attackDelay = 0;

       player.playerControl.setGravity(new Vector3f(0f,-9.81f,0f));
       player.playerControl.setJumpForce(new Vector3f(0f,5f,0f));
       
       player.killCount = 0;
       player.ammo = 100;
  
       physics.getPhysicsSpace().add(player.playerControl);
       rootNode.attachChild(player.Model);
       
    }
    
    public int getAmmo(Player player){
     return player.ammo;
     }
    
    public void changeAmmo(Player player,int change){
      int currentAmmo = player.getAmmo(player);
      player.ammo = currentAmmo + change;
      }
    
    public int getHealth(Player player){
      return player.health;
      }
    
    public void changeHealth(Player player, int change){
      int currentHealth = player.getHealth(player);
      if (currentHealth > 0) {
        player.health = currentHealth + change;
        //System.out.println(currentHealth);
        } else {
        //System.out.println("Player Death");
        }
    }
    
    
    public String getItemInHand() {
      return heldItem;
    }
    
    
    
    public void setItemInHand(String item, Player player) {
      try {
      inventory.remove(item);
      inventory.add(heldItem);
      heldItem = item;

      } catch (NullPointerException e) {
      }
    }
    
    
    
    public void grabItem(physicalAppState item, Camera cam,Player player) {
       CollisionResults grabResults = new CollisionResults();
       item.grabbable.collideWith(player.Model.getWorldBound(), grabResults);
       
       String grabbedItem;
 
       if (grabResults.size() > 0) {
         grabbedItem = grabResults.getCollision(0).getGeometry().getName();

         } else {
         grabbedItem = "Nothing";
         }
        
       if(grabbedItem.equals("Ammo")){
         player.changeAmmo(player, 100);
         grabResults.getCollision(0).getGeometry().removeFromParent();
         }

       if(grabbedItem.equals("Health")){
         player.changeHealth(player, 20);
         grabResults.getCollision(0).getGeometry().removeFromParent();
         }
        
       if(grabbedItem.equals("Gun")){
         player.inventoryAddItem(grabbedItem, player);
         grabResults.getCollision(0).getGeometry().removeFromParent();
          }      
        }
   
    
    
    public ArrayList getInventory(Player player, GUI GUI) {
      GUI.updateInventoryWindow(player, GUI);
      return player.inventory;
      }
    
    
    
    public void inventoryAddItem(String item, Player player) {
      int inventoryLimit = 10;
      if(player.inventory.size() < inventoryLimit){
        player.inventory.add(item);

        } else {
        }
      }

    public void attackChecker(Camera cam, Player player, AnimationAppState animInteract, String legAnim, Node monsterNode, physicalAppState item){
      int weaponRate;
      try {
      if (player.getItemInHand().equals("Gun"))
        weaponRate = 20;
        else
        weaponRate = 50;
        } catch (NullPointerException e) {
        weaponRate = 50;
        }
      if (player.attackDelay == weaponRate){
        player.attackDelay = 0;
        attack(cam, player, animInteract, legAnim, monsterNode, item);
        }
        else
        player.attackDelay++;
    
    }
    
    public void attack(Camera cam, Player player, AnimationAppState animInteract, String legAnim, Node monsterNode, physicalAppState item){
      int range;
      int damage;
      
      if (player.getItemInHand() != null)
        if(player.getItemInHand().equals("Gun")){

          if (getAmmo(player) > 0) {
          changeAmmo(player, -1);
          range = 20;
          damage = -8;
          } else {
          range = 0;
          damage = 0;
          }

          } else {
          String armAnim = "Punch";
          range = 4;
          damage = -3;
          animInteract.animChange(armAnim, legAnim, player.Model);
          
        } else {
        String armAnim = "Punch";
        range = 4;
        damage = -3;
        animInteract.animChange(armAnim, legAnim, player.Model);
        }
      
       CollisionResults attackResults = new CollisionResults();
       Ray attackRay = new Ray(cam.getLocation(), cam.getDirection());
       monsterNode.collideWith(attackRay, attackResults);

        
       try {
         Monster monster = (Monster) attackResults.getCollision(0).getGeometry().getParent().getParent().getParent();
         Vector3f monsterLocation = monster.Model.getLocalTranslation();
         Vector3f playerLocation  = player.Model.getLocalTranslation();
         float distance = playerLocation.distance(monsterLocation);
         if (distance <= range) {
           
           if (monster.getHealth(monster) < 0) {
             monster.changeHealth(monster, damage, player);
  
             }else {
             monster.dropItem(item, monster);
             monster.Die(monster, player);
             }
         }
       } catch (IndexOutOfBoundsException i) {
       }
     }
    
    public int getKillCount(Player player){
      return player.killCount;
      }
    
    public void changeKillCount(Player player, int change) {
      player.killCount = player.killCount + change;
      }
    
    public void Jump(BetterCharacterControl playerControl){
        playerControl.jump();
    }
    
    
}
