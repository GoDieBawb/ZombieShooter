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
import java.util.ArrayList;

/**
 *
 * @author Bob
 */
public class MonsterManager extends AbstractAppState {
    
  public    SimpleApplication      app;
  public    AppStateManager        stateManager;
  public    AssetManager           assetManager;   
  public    ArrayList<Monster>     monsterList;
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
    monsterList        = new ArrayList<Monster>();
    monsterNode        = new Node();
    monsterCount = 5;
    rootNode.attachChild(monsterNode);
    for(int i = 0; i < 5; i++ )
    createMonster();
    
    }
  
    public void createMonster(){
       Monster monster = new Monster();
       monster.monsterHealth = 20;
       monster.monsterControl = new BetterCharacterControl(1f, 5f, 1f);
       System.out.println("Asset Manager " + assetManager);
       monster.Model = (Node) assetManager.loadModel("Models/Newman2/Newman2.j3o");
       monster.attachChild(monster.Model);
       Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md"); 
       monster.Model.setMaterial(mat);
       monster.Model.setLocalScale(.7f);
       monster.Model.addControl(monster.monsterControl);
       monster.monsterControl.setGravity(new Vector3f(0f,-9.81f,0f));
       monster.anim = new AnimationAppState();
       monster.anim.animationInit(monster.Model, mat);
       physics.getPhysicsSpace().add(monster.monsterControl);
       rootNode.attachChild(monster.Model);
       monsterNode.attachChild(monster);
       monsterList.add(monster);
       monster.monsterSetLocation(monster);
    }
  
    @Override
    public void update(float tpf){
      for (int i = 0; i < monsterList.size(); i++) {
        Monster monster = monsterList.get(i);
        Vector3f playerLocation = player.Model.getLocalTranslation();
        Vector3f monsterLocation = monsterList.get(i).Model.getLocalTranslation();
        float distance = playerLocation.distance(monsterLocation);
        Vector3f playerDirection = playerLocation.subtract(monsterLocation);
        monster.Model.getControl(BetterCharacterControl.class).setWalkDirection(playerDirection);
        monster.Model.getControl(BetterCharacterControl.class).setViewDirection(playerDirection);
        if (distance < 3) {
          monster.monsterAttack(monsterList.get(i).Model, player);
          } else {
          monster.anim.animChange("UnarmedRun", "RunAction", monsterList.get(i).Model);
          }
        
        }
      }
}
