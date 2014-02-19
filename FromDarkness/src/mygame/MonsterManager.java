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
  
    public void creationChecker(Player player, Node monsterNode){
      monsterCount = player.getKillCount(player) * 2;
      if (monsterCount > 20) 
      monsterCount = 20;
      int moreMonsters = monsterCount - monsterNode.getChildren().size();
      for(int i = 0; i < moreMonsters; i++ )
      createMonster();
      }
  
    public void createMonster(){
       Monster monster = new Monster();
       monster.health = 20;
       monster.attackDelay = 0;
       monster.monsterControl = new BetterCharacterControl(1f, 5f, 1f);
       monster.Model = (Node) assetManager.loadModel("Models/Newman2/Newman2.j3o");
       Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md"); 
       monster.Model.setMaterial(mat);
       monster.Model.setLocalScale(.7f);
       monster.Model.addControl(monster.monsterControl);
       monster.monsterControl.setGravity(new Vector3f(0f,-9.81f,0f));
       monster.anim = new AnimationAppState();
       monster.anim.animationInit(monster.Model, mat);
       physics.getPhysicsSpace().add(monster.monsterControl);
       monster.attachChild(monster.Model);
       monsterNode.attachChild(monster);
       monster.monsterSetLocation(monster);
       }
    
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
    
    public void monsterRotater(Monster monster, Vector3f playerDirection){
      monster.Model.getControl(BetterCharacterControl.class).setWalkDirection(playerDirection);
      monster.Model.getControl(BetterCharacterControl.class).setViewDirection(playerDirection);
      }
  
    @Override
    public void update(float tpf){
      for (int i = 0; i < monsterNode.getChildren().size(); i++) {
        Monster monster = (Monster) monsterNode.getChild(i);
        locationChecker(monster);
        creationChecker(player, monsterNode);
        }
      }
}
