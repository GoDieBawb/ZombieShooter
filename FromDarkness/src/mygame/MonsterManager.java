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
import com.jme3.math.Vector3f;
import com.jme3.scene.Node; 

/**
 *
 * @author Bob
 */
public class MonsterManager extends AbstractAppState {
    
  public    SimpleApplication      app;
  public    AppStateManager        stateManager;
  public    AssetManager           assetManager;   
  public    int                    monsterCount;
  public    Node                   monsterNode;
  public    Player                 player;
  public    Node                   rootNode;
  public    BulletAppState         physics;
  
  @Override
    public void initialize(AppStateManager stateManager, Application app) {
    super.initialize(stateManager, app);
    this.app           = (SimpleApplication) app; // can cast Application to something more specific
    this.assetManager  = this.app.getAssetManager();
    this.stateManager  = this.app.getStateManager();
    this.physics       = this.stateManager.getState(BulletAppState.class);
    this.player        = this.stateManager.getState(Player.class).player;
    this.rootNode      = this.app.getRootNode();
    monsterNode        = new Node("Monster Node");
    monsterCount = 5;
    rootNode.attachChild(monsterNode);
    for(int i = 0; i < 5; i++ )
    createMonster();
    
    }
  
    //This is checked on a loop to see if more monsters are necessary
  
    public void creationChecker(Player player, Node monsterNode){
      monsterCount = player.getKillCount(player) * 2;
      if (monsterCount > 5) 
      monsterCount = 5;
      int moreMonsters = monsterCount - monsterNode.getChildren().size();
      for(int i = 0; i < moreMonsters; i++ )
      createMonster();
      }
  
    //Creates a new instance of monster
    
    public void createMonster(){
       Monster monster = new Monster();
       monster.health = 20;
       monster.attackDelay = 0;
       monster.monsterControl = new BetterCharacterControl(1f, 5f, 1f);
       monster.Model = (Node) assetManager.loadModel("Models/RealMonster/RealMonster.j3o");
       monster.Model.setLocalScale(.8f);
       monster.Model.addControl(monster.monsterControl);
       monster.monsterControl.setGravity(new Vector3f(0f,-9.81f,0f));
       monster.anim = new AnimationManager();
       monster.anim.animationInit(monster.Model);
       physics.getPhysicsSpace().add(monster.monsterControl);
       monster.attachChild(monster.Model);
       monsterNode.attachChild(monster);
       monster.monsterSetLocation(monster);
       }
    
    //Checked on a loop to see if the monster is close enough to attack
    
    public void locationChecker(Monster monster){
        Vector3f playerLocation = player.Model.getLocalTranslation();
        Vector3f monsterLocation = monster.Model.getLocalTranslation();
        float distance = playerLocation.distance(monsterLocation);
        Vector3f playerDirection = playerLocation.subtract(monsterLocation);
        monsterRotater(monster, playerDirection);
        if (distance < 3) {
          if (monster.attackDelay == 10) {
          monster.attackDelay = 0;
          monster.attack(monster.Model, player);
          } else {
          monster.attackDelay++;
          }
          } else {
          monster.anim.animChange("UnarmedRun", "RunAction",monster.Model);
          }
      }
    
    //Method to make sure the monsters always face the player.
    
    public void monsterRotater(Monster monster, Vector3f playerDirection){
      monster.Model.getControl(BetterCharacterControl.class).setWalkDirection(playerDirection);
      monster.Model.getControl(BetterCharacterControl.class).setViewDirection(playerDirection);
      }
    
    //Update method to check above specified methods on the loop
  
    @Override
    public void update(float tpf){
      for (int i = 0; i < monsterNode.getChildren().size(); i++) {
        Monster monster = (Monster) monsterNode.getChild(i);
        locationChecker(monster);
        creationChecker(player, monsterNode);
        }
      }
}
