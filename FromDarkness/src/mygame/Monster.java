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
import com.jme3.material.Material;
import com.jme3.scene.Node;
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
    monsterList = new ArrayList<Monster>();
    createMonster();
    }
    
    public void createMonster(){
    
        for (int i = 0; i < 5; i++) {
            Monster monster = new Monster();
            System.out.println("Asset Manager " + assetManager);
            monster.Model = (Node) assetManager.loadModel("Models/Newman2/Newman2.j3o");
            Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md"); 
            monster.Model.setMaterial(mat);
            monster.Model.setLocalScale(.7f);
            Random rand = new Random();
            int firstNumber = rand.nextInt(150) + 5; 
            int secondNumber = rand.nextInt(150) + 5;
            monster.Model.setLocalTranslation(firstNumber, 0f, secondNumber);
            anim = new AnimationAppState();
            anim.animationInit(monster.Model, mat);
            monsterList.add(monster);
            rootNode.attachChild(monster.Model);
        }
    }
    
  @Override
    public void update(float tpf){
      for (int i = 0; i < monsterList.size(); i++) {
        float distance = monsterList.get(i).Model.getLocalTranslation().distance(player.Model.getLocalTranslation());

        if (distance < 3) {
         System.out.println(distance);
         anim.animChange("Punch", "StillLegs", monsterList.get(i).Model);
         } else {
         anim.animChange("StillArms", "StillLegs", monsterList.get(i).Model);
         }
        
        }
      }
    
}
