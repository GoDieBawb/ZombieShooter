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
import com.jme3.collision.CollisionResult;
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

public  ArrayList              inventory;
public  String                 heldItem;
public  Player                 player;

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
      System.out.println("initialized" + heldItem + getItemInHand());
      player.inventory = new ArrayList<String>();
      System.out.println(heldItem);
      player.heldItem = "bear";
      player.inventory.add("air");
       
       playerControl = new BetterCharacterControl(1f, 5f, 1f);
      
       player.Model = (Node) assetManager.loadModel("Models/Newman2/Newman2.j3o");
      
       player.Model.setLocalTranslation(0f, 0f, 0f);
       player.Model.setLocalScale(.7f);
       player.Model.addControl(playerControl);

       playerControl.setGravity(new Vector3f(0f,-9.81f,0f));
       playerControl.setJumpForce(new Vector3f(0f,5f,0f));
  
       physics.getPhysicsSpace().add(playerControl);
       rootNode.attachChild(player.Model);
       
       
    }
    
    public String getItemInHand() {
      return heldItem;
    }
    
    public void setItemInHand(String item) {
    inventory.remove(item);
    inventory.add(heldItem);
    heldItem = item;
    }
    
    public void grabItem(Node shootables, Camera cam,Player player) {
       CollisionResults grabResults = new CollisionResults();
       Ray grabRay = new Ray(cam.getLocation(), cam.getDirection());
       shootables.collideWith(grabRay, grabResults);
       String grabbedItem;
       
       for (int i = 0; i < grabResults.size(); i++) {
         String hit = grabResults.getCollision(i).getGeometry().getName();
         System.out.println(hit);
         }

       if (grabResults.size() > 0) {
         grabbedItem = grabResults.getCollision(0).getGeometry().getName();
         } else {
         grabbedItem = "air";
       }
        
        if(grabbedItem.equals("Billy")){
          player.inventoryAddItem(grabbedItem, player);
          System.out.println("Youve shot BILLY! WHY!" + player  +  Model);
          player.Model.attachChild(grabResults.getCollision(0).getGeometry());
          }
        
        if(grabbedItem.equals("Gun")){
          player.inventoryAddItem(grabbedItem, player);
          System.out.println("That's a gun!");
          CollisionResult grabbed = grabResults.getCollision(0);
          player.Model.attachChild(grabbed.getGeometry());
          grabbed.getGeometry().setLocalScale(.3f);
          grabbed.getGeometry().setLocalRotation
                  (new Matrix3f(1f, 5f, 5f, 1f, 1f, 1f, 1f, 5f, 1f));
          grabbed.getGeometry().setLocalTranslation(-1f, 5f, 2.5f);
          
        }
        
    
    }
   
    
    public ArrayList getInventory(Player player) {
      System.out.println("This works");
      for (int i = 0; i < player.inventory.size(); i++) {
      System.out.println(player.inventory.get(i));
      }
      return player.inventory;
      }
    
    
    public void inventoryAddItem(String item, Player player) {
      int inventoryLimit = 10;
      if(player.inventory.size() < inventoryLimit){
        player.inventory.add(item);
         System.out.println("added " + item + player);

        } else {
        System.out.println("There is no more room in your inventory" + player.inventory.size());
        }
    
    }
    
    public void Attack(Camera cam, Player player, AnimationAppState animInteract, String legAnim){
        
        if(player != null){
        if(player.getItemInHand().equals("Gun")){

          System.out.println("Shootbang!");

          } else {
          System.out.println("Puncharoonie!");
          String armAnim = "Punch";
          animInteract.animChange(armAnim, legAnim);
          
          }
        } else {
        System.out.println("Not Player because he is " + player);
        }
    }
    
    public void Jump(BetterCharacterControl playerControl){
        playerControl.jump();
    }
    
}
