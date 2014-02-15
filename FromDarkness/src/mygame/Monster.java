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
    
  @Override
  public void initialize(AppStateManager stateManager, Application app) {
    super.initialize(stateManager, app);
    
    this.app = (SimpleApplication) app; // can cast Application to something more specific
    this.assetManager = this.app.getAssetManager();
    this.stateManager = this.app.getStateManager();
    this.physics      = this.stateManager.getState(BulletAppState.class);
    this.rootNode     = this.app.getRootNode();
    this.anim         = this.stateManager.getState(AnimationAppState.class);
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
            AnimationAppState anim = new AnimationAppState();
            anim.animationInit(monster.Model, mat);
            monsterList = new ArrayList<Monster>();
            monsterList.add(monster);
            rootNode.attachChild(monster.Model);
        }
    }
    
  @Override
    public void update(float tpf){
      System.out.println(monsterList.size());
      for (int i = 0; i < monsterList.size(); i++) {
        System.out.println(monsterList.get(i));
        
        }
      }
    
}
