package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Matrix3f;
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
public  AnimationManager       animInteract;

public  ArrayList              inventory;
public  String                 heldItem;
public  Player                 player;
public  int                    health;
public  int                    killCount;
public  int                    ammo;
public  int                    attackDelay;
public Node                    placeHolder;
public InteractionManager      interaction;
public SceneManager            item;
public  int                    damageBonus;
private int                    damageTimer;
private int                    rateBonus;
private int                    rateTimer;
public  int                    speedBonus;
public  int                    speedTimer;
 


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
       player.placeHolder = new Node();
       player.playerControl = new BetterCharacterControl(1f, 5f, 1f);
       player.Model = (Node) assetManager.loadModel("Models/Newman2/Newman2.j3o");
       player.health = 20;
       player.setRateBonus(player, 1);
       player.damageBonus = 0;
       player.rateBonus = 1;
       player.speedBonus = 1;
       player.interaction = stateManager.getState(InteractionManager.class);
       player.animInteract = new AnimationManager();
       player.animInteract.animationInit(player.Model);
       player.Model.setLocalScale(.7f);
       player.Model.addControl(player.playerControl);
       player.attackDelay = 0;

       player.playerControl.setGravity(new Vector3f(0f,-9.81f,0f));
       player.playerControl.setJumpForce(new Vector3f(0f,5f,0f));
       
       player.killCount = 0;
       player.ammo = 100;
       player.damageTimer = 0;
  
       physics.getPhysicsSpace().add(player.playerControl);
       rootNode.attachChild(player.Model);
       player.Model.setLocalTranslation(0f, 50f, 0f);
       
    }
    
    //Ammo Methods For Getting and Changing Ammo
    
    public int getAmmo(Player player){
     return player.ammo;
     }
    
    public void changeAmmo(Player player,int change){
      int currentAmmo = player.getAmmo(player);
      player.ammo = currentAmmo + change;
      }
    
    
    //Health Methods For Getting and Changing Health and Dying
    
    public int getHealth(Player player){
      return player.health;
      }
    
    
    public void changeHealth(Player player, int change, AppStateManager stateManager){
      int currentHealth = player.getHealth(player);
      if (currentHealth > 0) {
        player.health = currentHealth + change;
        } else {
        Die(player, stateManager);
        }
      }
    
    
    public void Die(Player player, AppStateManager stateManager){
      player.setItemInHand("", player);
      player.interaction.isDead = true;
      player.Model.setLocalRotation(new Matrix3f(9f, 1f, 1f, 9f, 1f, 9f, 1f, 9f, 1f));
      stateManager.detach(stateManager.getState(MonsterManager.class));
      GUI GUIState = stateManager.getState(GUI.class);
      GUI GUI = GUIState.GUI;
      System.out.println(GUI.screen);
      GUI.screen.removeElement(GUI.handMenu);
      GUI.screen.removeElement(GUI.inventoryMenu);
      GUI.screen.removeElement(GUI.HUD);
      if (GUI.EndMenu == null)
      GUIState.createEndMenu();
      else
      GUI.EndMenu.showWindow();
      InteractionManager interaction = stateManager.getState(InteractionManager.class);
      interaction.inputManager.setCursorVisible(true);
      player.Model.getParent().detachAllChildren();
      }
    
    //Player Equipped Item Methods for Getting and Setting the Item in Hand
    
    
    public String getItemInHand() {
      if (heldItem != null)
        return heldItem;
        else{
        heldItem = "nothing";
        return heldItem;
        }
      
      }
    
    
    
    public void setItemInHand(String item, Player player) {
      try {
      if (item.equals("Gun")){
          
      }
      inventory.remove(item);
      inventory.add(heldItem);
      heldItem = item;

      } catch (NullPointerException e) {
      }
    }
    
    
    //Grabbing Method
    
    
    public void grabItem(SceneManager item, Camera cam,Player player) {
       CollisionResults grabResults = new CollisionResults();
       item.grabbable.collideWith(player.Model.getWorldBound(), grabResults);
       
       String grabbedItem;
 
       if (grabResults.size() > 0) {
         try{
         
         grabbedItem = grabResults.getCollision(0).getGeometry().getName();
         
         }catch (NullPointerException e){
         grabbedItem = "Nothing";
         }

         } else {
         grabbedItem = "Nothing";
         }
        
       if(grabbedItem.equals("AmmoBox")){
         player.changeAmmo(player, 100);
         grabResults.getCollision(0).getGeometry().removeFromParent();
         }

       if(grabbedItem.equals("HealthBox")){
         player.changeHealth(player, 20, stateManager);
         grabResults.getCollision(0).getGeometry().removeFromParent();
         }
        
       if(grabbedItem.equals("Gun")){
         player.inventoryAddItem(grabbedItem, player);
         grabResults.getCollision(0).getGeometry().setLocalTranslation(0f, -10f, 0f);
         player.placeHolder.attachChild(grabResults.getCollision(0).getGeometry());
         }

       if(grabbedItem.equals("DamageBonus")){
         grabResults.getCollision(0).getGeometry().removeFromParent();
         player.setDamageBonus(player, 5);
         }
       
       if(grabbedItem.equals("RateBonus")){
         grabResults.getCollision(0).getGeometry().removeFromParent();
         player.rateBonus = 2;
         }

       if(grabbedItem.equals("SpeedBonus")){
         grabResults.getCollision(0).getGeometry().removeFromParent();
         player.speedBonus = 2;
         }
        }
   
    
    //Methods for getting and changing player inventory
    
    
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
    
    //Check the players weapon delay and whether enough time has passed
    
    public void attackChecker(Camera cam, Player player, AnimationManager animInteract, String legAnim, Node monsterNode, SceneManager item, SoundManager audio){
      int weaponRate;
      try {
          
      if (player.getItemInHand().equals("Gun"))
        weaponRate = 20;
        else
        weaponRate = 50;
      
      } catch (NullPointerException e) {
      weaponRate = 50;
      }
      
      if (player.attackDelay > weaponRate/player.rateBonus){
        player.attackDelay = 0;
        attack(cam, player, animInteract, legAnim, monsterNode, item, audio);
        audio.playSound(player);
        }
        else
        player.attackDelay++;
    
    }
    
    //If the avove requirement is met allow the player to attack
    
    public void attack(Camera cam, Player player, AnimationManager animInteract, String legAnim, Node monsterNode, SceneManager item, SoundManager audio){
      int range;
      int damage;
      
      
      //Get the Item in Hand and Set Damage
      
      if (player.getItemInHand() != null)
        if(player.getItemInHand().equals("Gun")){

          if (getAmmo(player) > 0) {
          changeAmmo(player, -1);
          item.setSparksPosition(player);
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

        //If there is a collision result change the monsters health or kill him
       
       try {
         Monster monster = (Monster) attackResults.getCollision(0).getGeometry().getParent().getParent().getParent();
         Vector3f monsterLocation = monster.Model.getLocalTranslation();
         Vector3f playerLocation  = player.Model.getLocalTranslation();
         float distance = playerLocation.distance(monsterLocation);
         if (distance <= range) {
           
           if (monster.getHealth(monster) > 0) {
             monster.changeHealth(monster, damage + player.damageBonus, player, audio);
             item.setBloodPosition(player, attackResults.getCollision(0).getContactPoint());
  
             } else {
             monster.dropItem(item, monster);
             monster.Die(monster, player);
             }
         }
       } catch (IndexOutOfBoundsException i) {
       }
     }
    
    //Kill Count Methods for Getting and Changing Kill Count
    
    public int getKillCount(Player player){
      return player.killCount;
      }
    
    public void changeKillCount(Player player, int change) {
      player.killCount = player.killCount + change;
      }
    
    //Method for Jumping
    
    public void Jump(BetterCharacterControl playerControl){
        playerControl.jump();
    }
    
    //Methods For Getting and Setting Player Damage Bonus
    
    public int getDamageBonus(Player player){
      return player.damageBonus;
      }

    public void setDamageBonus(Player player, int newDamage){
      player.damageBonus = newDamage;
      }
    
    //Methods for getting and setting player rate bonus
    
    public int getRateBonus(Player player){
      return player.damageBonus;
      }
    
    public void setRateBonus(Player player, int newRate){
      player.rateBonus = newRate;
      }
    
    
    //Update Loop
    
    @Override
      public void update(float tpf){
      if (player.damageBonus > 0)
        damageTimer++;
      if (damageTimer > 3000) {
        player.damageBonus = 0;
        damageTimer = 0;
        }

      if (player.rateBonus > 1)
        player.rateTimer++;
      if (player.rateTimer > 3000) {
        player.rateBonus = 1;
        player.rateTimer = 0;
        }

      if (player.speedBonus > 1)
        player.speedTimer++;
      if (player.speedTimer > 3000) {
        player.speedBonus = 1;
        player.speedTimer = 0;
        }
      }
}
