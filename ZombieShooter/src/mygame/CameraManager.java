package mygame;

import com.jme3.app.Application;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.ChaseCamera;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;


public class CameraManager extends AbstractAppState{

private SimpleApplication app;
private AppStateManager   stateManager;
private InputManager      inputManager;
public  Camera            cam;

public FlyByCamera       flyCam;
public ChaseCamera       chaseCam;
    
public Player player;
  
  @Override
  public void initialize(AppStateManager stateManager, Application app) {
    super.initialize(stateManager, app);
    
    this.app          = (SimpleApplication) app; // can cast Application to something more specific
    this.stateManager = this.app.getStateManager();
    this.inputManager = this.app.getInputManager();
    this.cam          = this.app.getCamera();
    this.flyCam       = this.stateManager.getState(FlyCamAppState.class).getCamera();
    
    flyCam.setEnabled(false);
    flyCam.setDragToRotate(false);
    initCamera();
    }
  
    public void initCamera(){
      player = stateManager.getState(Player.class).player;
      Node model = (Node)player.Model.getChild("FaceNode");
      chaseCam = new ChaseCamera(cam, model, inputManager);
      chaseCam.setSmoothMotion(false);
      chaseCam.setDragToRotate(false);
      chaseCam.setMaxDistance(0f);
      chaseCam.setUpVector(new Vector3f(0f, 1f, 0f));
      }
    
    @Override
    public void update(float tpf){
    }
        
    
    
}
