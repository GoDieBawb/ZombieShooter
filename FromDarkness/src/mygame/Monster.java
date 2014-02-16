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
public class Monster extends AbstractAppState {
  public    SimpleApplication      app;
  public    AppStateManager        stateManager;
  public    AssetManager           assetManager;
  public    BulletAppState         physics;
  public    Node                   Model;
  public    Node                   rootNode;
  public    ArrayList<Monster>     monsterList;
  public    AnimationAppState      anim;
  public    Player                 player;
  public    int                    monsterCount;
  public    Node                   monsterNode;
  public    int                    monsterHealth;
  public    BetterCharacterControl monsterControl;
    
  @Override
  public void initialize(AppStateManager stateManager, Application app) {
    super.initialize(stateManager, app);
    
    this.app = (SimpleApplication) app; // can cast Application to something more specific
    this.assetManager = this.app.getAssetManager();
    this.stateManager = this.app.getStateManager();
    this.physics      = this.stateManager.getState(BulletAppState.class);
    this.rootNode     = this.app.getRootNode();
    this.anim         = this.stateManager.getState(AnimationAppState.class);
    this.player        = this.stateManager.getState(Player.class).player;
    monsterList  = new ArrayList<Monster>();
    monsterNode  = new Node();
    rootNode.attachChild(monsterNode);
    monsterCount = 5;
    createMonster();
    }
    
    public void createMonster(){
     for (int i = 0; i < monsterCount; i++) {
       Monster monster = new Monster();
       monster.monsterHealth = 20;
       monster.monsterControl = new BetterCharacterControl(1f, 5f, 1f);
       System.out.println("Asset Manager " + assetManager);
       monster.Model = (Node) assetManager.loadModel("Models/Newman2/Newman2.j3o");
       Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md"); 
       monster.Model.setMaterial(mat);
       monster.Model.setLocalScale(.7f);
       monster.Model.addControl(monster.monsterControl);
       monster.monsterControl.setGravity(new Vector3f(0f,-9.81f,0f));
       anim = new AnimationAppState();
       anim.animationInit(monster.Model, mat);
       monsterList.add(monster);
       physics.getPhysicsSpace().add(monster.monsterControl);
       rootNode.attachChild(monster.Model);
       monsterNode.attachChild(monster.Model);
       monsterSetLocation(monster);
       }
    }
    
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
    
  @Override
    public void update(float tpf){
      for (int i = 0; i < monsterList.size(); i++) {
        Vector3f playerLocation = player.Model.getLocalTranslation();
        Vector3f monsterLocation = monsterList.get(i).Model.getLocalTranslation();
        float distance = playerLocation.distance(monsterLocation);
        Vector3f playerDirection = playerLocation.subtract(monsterLocation);
        monsterList.get(i).Model.getControl(BetterCharacterControl.class).setWalkDirection(playerDirection);
        monsterList.get(i).Model.getControl(BetterCharacterControl.class).setViewDirection(playerDirection);
        if (distance < 3) {
          monsterAttack(monsterList.get(i).Model, player);
          } else {
          anim.animChange("UnarmedRun", "RunAction", monsterList.get(i).Model);
          }
        
        }
      }
    
}
