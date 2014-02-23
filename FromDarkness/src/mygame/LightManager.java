/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.light.AmbientLight;
import com.jme3.light.SpotLight;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author Bob
 */
public class LightManager extends AbstractAppState {
    
private SimpleApplication app;
private Node              rootNode;
private AppStateManager   stateManager;

public AmbientLight al;
public BetterCharacterControl player;
public Spatial model;

public SpotLight flashLight;
public Camera cam;
public boolean lightState;

  @Override
  public void initialize(AppStateManager stateManager, Application app) {
    super.initialize(stateManager, app);
    
    this.app = (SimpleApplication) app; // can cast Application to something more specific
    this.rootNode     = this.app.getRootNode();
    this.stateManager = this.app.getStateManager();
    this.cam          = this.app.getCamera();
    this.player       = this.stateManager.getState(Player.class).playerControl;
    this.model        = this.stateManager.getState(Player.class).player.Model;
    
    lightState = false;
    initLight();
    
  }
  
    //Initiates the light
  
    public void initLight(){
    al = new AmbientLight();
    al.setColor(ColorRGBA.White.mult(.3f));
    rootNode.addLight(al);
    
    flashLight = new SpotLight();
    flashLight.setColor(ColorRGBA.White.mult(2.9f));
    flashLight.setSpotInnerAngle(1);
    flashLight.setSpotOuterAngle(1);
    flashLight.setSpotRange(50);
    System.out.println("Light Initialized");
    flashlightOn();
    }
    
    //Turns the Flashlight on
    
    public void flashlightOn(){
    //if(lightState == false){
      System.out.println("Flashlight Toggled");
      rootNode.addLight(flashLight);
      lightState = true;
    
     // }else{
     /// System.out.println("Flashlight Toggled");
     // rootNode.removeLight(flashLight);
     //rootNode.addLight(al);
      //lightState = false;
      //}
    
    }
    
    //Loop keeps the flashlight attached to the player and checks if it should be on
    
    @Override
    public void update(float tpf) {
    if(lightState){
    flashLight.setDirection(cam.getDirection());
    flashLight.setPosition(model.getLocalTranslation().add(0f,5f,0f));
       } 
        
  }

}
