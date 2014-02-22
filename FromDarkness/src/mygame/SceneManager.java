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
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author Bob
 */
public class SceneManager extends AbstractAppState {
    
private SimpleApplication app;
private Node              rootNode;
private AssetManager      assetManager;
private AppStateManager   stateManager;
private BulletAppState    physics;

public  Node              grabbable;

public Node               sceneModel;
public Node               Model;
public Player             player;

private Material          mat;
public  ParticleEmitter   blood;
public  ParticleEmitter   sparks;
public  int               bleedDelay;
public  int               sparkDelay;

    
      @Override
  public void initialize(AppStateManager stateManager, Application app) {
    super.initialize(stateManager, app);
    
    this.app          = (SimpleApplication) app; // can cast Application to something more specific
    this.rootNode     = this.app.getRootNode();
    this.assetManager = this.app.getAssetManager();
    this.stateManager = this.app.getStateManager();
    this.player       = this.stateManager.getState(Player.class);
    this.physics      = this.stateManager.getState(BulletAppState.class);
    
    bleedDelay = 0;
    sparkDelay = 0;
    mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mat.setColor("Color", ColorRGBA.randomColor());
    grabbable = new Node("Grabbables");
    rootNode.attachChild(grabbable);
    grabbable.attachChild(initScene());
    grabbable.attachChild(initGun());   
    grabbable.attachChild(makeAmmo("Ammo", 15f, 15f));
    grabbable.attachChild(makeHealth("Health", -15f, -15f));
    initParticles();
    }
      
    public Spatial initScene(){
          
        sceneModel = (Node) assetManager.loadModel("Scenes/Bob'sTestCell.j3o");
        sceneModel.setLocalScale(2);
        CollisionShape sceneShape =
        CollisionShapeFactory.createDynamicMeshShape(sceneModel);
        RigidBodyControl scenePhys = new RigidBodyControl(sceneShape);
        sceneModel.addControl(scenePhys);
        sceneModel.setLocalTranslation(5f, 0f, 1f);
        scenePhys.setMass(0f);
        physics.getPhysicsSpace().add(scenePhys);
        System.out.println("Scene Initialized");
        return sceneModel;
        }
   
  /** A cube object for target practice */
  public Geometry makeAmmo(String name, float x, float z) {
    Node ammoBox = (Node) assetManager.loadModel("Models/Items/AmmoBox.j3o");
    Node boxNode = (Node) ammoBox.getChild(0);
    Geometry geom = (Geometry) boxNode.getChild(0);
    geom.setLocalTranslation(new Vector3f(x, 1, z));
    return geom;
    }

  public Geometry makeHealth(String name, float x, float z) {
    Node healthBox = (Node) assetManager.loadModel("Models/Items/healthBox.j3o");
    Node boxNode = (Node) healthBox.getChild(0);
    Geometry geom = (Geometry) boxNode.getChild(0);
    geom.setLocalTranslation(new Vector3f(x, 1, z));
    return geom;
    }
    
    protected Spatial initGun(){   
      Spatial gun = assetManager.loadModel("Models/Gun/Gun.j3o");
      gun.setLocalTranslation(15f, 0f, 15f);
      gun.setLocalScale(.3f);
      System.out.println("Gun Initialized");
      return gun;
      }
    
   public void initParticles(){
    blood = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
    Material mat_red = new Material(assetManager, 
            "Common/MatDefs/Misc/Particle.j3md");
    mat_red.setTexture("Texture", assetManager.loadTexture(
            "Effects/Explosion/flame.png"));
    blood.setMaterial(mat_red);
    blood.setStartColor(ColorRGBA.Red);
    blood.setStartSize(1f);
    blood.setEndSize(1f);
    blood.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 2, 0));
    blood.setGravity(0, 0, 0);
    blood.setLowLife(1f);
    blood.setHighLife(3f);
    blood.getParticleInfluencer().setVelocityVariation(0.3f);

    
    sparks = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
    sparks.setMaterial(mat_red);
    sparks.setStartColor(ColorRGBA.White);
    sparks.setStartSize(1f);
    sparks.setEndSize(1f);
    sparks.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 2, 0));
    sparks.setGravity(0, 0, 0);
    sparks.setLowLife(1f);
    sparks.setHighLife(3f);
    sparks.getParticleInfluencer().setVelocityVariation(0.3f);
    }

   public void setBloodPosition(Player player, Vector3f contactPoint){
     blood = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
     Material mat_red = new Material(assetManager, 
            "Common/MatDefs/Misc/Particle.j3md");
     mat_red.setTexture("Texture", assetManager.loadTexture(
            "Effects/Explosion/flame.png"));
     blood.setMaterial(mat_red);
     blood.setStartColor(ColorRGBA.Red);
     blood.setStartSize(1f);
     blood.setEndSize(1f);
     blood.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 2, 0));
     blood.setGravity(0, 0, 0);
     blood.setLowLife(1f);
     blood.setHighLife(3f);
     blood.getParticleInfluencer().setVelocityVariation(0.3f);
    
    
     rootNode.attachChild(blood);
     blood.setLocalTranslation(contactPoint);
     }
   
   
  public void setSparksPosition(Player player){
    
    sparks = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
    Material mat_red = new Material(assetManager, 
        "Common/MatDefs/Misc/Particle.j3md");
    mat_red.setTexture("Texture", assetManager.loadTexture(
           "Effects/Explosion/flame.png"));
    sparks.setMaterial(mat_red);
    sparks.setStartColor(ColorRGBA.White);
    sparks.setStartSize(1f);
    sparks.setEndSize(1f);
    sparks.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 2, 0));
    sparks.setGravity(0, 0, 0);
    sparks.setLowLife(1f);
    sparks.setHighLife(3f);
    sparks.getParticleInfluencer().setVelocityVariation(0.3f);
    
    rootNode.attachChild(sparks);
    sparks.setLocalTranslation(player.Model.getWorldTranslation().add(0, 5.5f, 0f));
  }
  
  
   @Override
   public void update(float tpf){
     if(blood.isInWorldSpace())
       bleedDelay++;
     if (bleedDelay == 15){
       blood.removeFromParent();
       bleedDelay = 0;
       }
     if(sparks.isInWorldSpace())
       sparkDelay++;
     if (sparkDelay == 10){
       sparks.removeFromParent();
       sparkDelay = 0;
       }
     }
    
}
