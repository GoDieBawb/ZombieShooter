package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;

/**
 *
 * @author Bob
 */
public class InteractionAppState extends AbstractAppState implements ActionListener {
    
private SimpleApplication app;
private Node              rootNode;
private AssetManager      assetManager;
private AppStateManager   stateManager;
private InputManager      inputManager;
public  ViewPort          viewPort;
private Camera            cam;

public BetterCharacterControl playerControl;
public Node                   shootables;

private Vector3f            walkDirection = new Vector3f();
private Vector3f            camDir = new Vector3f();
private Vector3f            camLeft = new Vector3f();
private boolean             left = false, right = false, up = false, down = false;

private boolean             initInteraction = false;
private boolean             shoot = false;

public Node                 sceneModel;
public String               armAnim;
public String               legAnim;
private AnimationAppState   animInteract;
private LightingAppState    lightInteract;

public Player               player;

    
  @Override
  public void initialize(AppStateManager stateManager, Application app) {
    super.initialize(stateManager, app);
    
    this.app = (SimpleApplication) app; // can cast Application to something more specific
    this.rootNode      = this.app.getRootNode();
    this.assetManager  = this.app.getAssetManager();
    this.stateManager  = this.app.getStateManager();
    this.inputManager  = this.app.getInputManager();
    this.viewPort      = this.app.getViewPort();
    this.cam           = this.app.getCamera();
    this.shootables    = this.stateManager.getState(physicalAppState.class).shootables;
    this.playerControl = this.stateManager.getState(Player.class).playerControl;
    this.animInteract  = this.stateManager.getState(AnimationAppState.class);
    this.lightInteract = this.stateManager.getState(LightingAppState.class);
    this.player        = this.stateManager.getState(Player.class).player;
    
    setUpKeys();
    armAnim = "StillArms";
    legAnim = "StillLegs";
    initInteraction = true;
  }
  
    private void setUpKeys() {
    inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
    inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
    inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
    inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
    inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
    inputManager.addMapping("Shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
    inputManager.addMapping("Grab", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
    inputManager.addMapping("FlashLight", new KeyTrigger(KeyInput.KEY_F));
    inputManager.addMapping("Inventory", new KeyTrigger(KeyInput.KEY_E));
    inputManager.addListener(this, "Inventory");
    inputManager.addListener(this, "FlashLight");
    inputManager.addListener(this, "Grab");
    inputManager.addListener(this, "Shoot");
    inputManager.addListener(this, "Left");
    inputManager.addListener(this, "Right");
    inputManager.addListener(this, "Up");
    inputManager.addListener(this, "Down");
    inputManager.addListener(this, "Jump");
  }
  

  public void onAction(String binding, boolean isPressed, float tpf) {
    if (binding.equals("Left")) {
        
      left = isPressed;
      
    } else if (binding.equals("Right")) {
        
      right= isPressed;
      
    } else if (binding.equals("Up")) {
      
      up = isPressed;
      if (isPressed){
        armAnim = "UnarmedRun";
        legAnim = "RunAction";
        animInteract.animChange(armAnim, legAnim);
        
        } else {
        armAnim = "StillArms";
        legAnim = "StillLegs";
        animInteract.animChange(armAnim, legAnim);
        }

      
    } else if (binding.equals("Down")) {
        
      down = isPressed;
      
    } else if (binding.equals("Jump")) {
        
      if (isPressed) {         
      player.Jump(playerControl);
      System.out.println("Jumping");
      }
      
    } else if (binding.equals("Shoot")) {
        
        shoot = isPressed;
        if (isPressed){
        player.Attack(cam, player, animInteract, legAnim);
        
        } else {
        armAnim = "StillArms";
        legAnim = "StillLegs";
        animInteract.animChange(armAnim, legAnim);
        }
        
    } else if (binding.equals("Grab") && !isPressed) {
            player.grabItem(shootables, cam, player);
            System.out.println("You've grabbed");
        
          
    } else if (binding.equals("FlashLight") && !isPressed){
            lightInteract.flashlightOn();
        
    } else if (binding.equals("Inventory") && !isPressed) {
            player.getInventory(player);

    }
  }
  
    @Override
    public void update(float tpf) {
        
      
        camDir.set(cam.getDirection()).multLocal(10.0f, 0.0f, 10.0f);
        camLeft.set(cam.getLeft()).multLocal(10.0f);
        walkDirection.set(0, 0, 0);
        if (left) {
            walkDirection.addLocal(camLeft);
        }
        if (right) {
            walkDirection.addLocal(camLeft.negate());
        }
        if (up) {
            walkDirection.addLocal(camDir);
        }
        if (down) {
            walkDirection.addLocal(camDir.negate());
        }
       if(initInteraction){
       playerControl.setWalkDirection(walkDirection);
       //cam.setLocation(model.getLocalTranslation().add(-1f, 5f, 0f));
       
       }
    }
}
    

