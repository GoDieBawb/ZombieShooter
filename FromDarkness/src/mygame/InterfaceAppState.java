/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *
 * @author Bob
 */
public class InterfaceAppState extends AbstractAppState implements ScreenController {
    
private SimpleApplication app;
private AssetManager      assetManager;
private AppStateManager   stateManager;
private InputManager      inputManager;
public  ViewPort           viewPort;
private BulletAppState    physics;
private ViewPort          guiViewPort;
private AudioRenderer     audioRenderer;

private Nifty             nifty;
private Screen            screen;

private FlyByCamera       flyCam;

    
    
      @Override
  public void initialize(AppStateManager stateManager, Application app) {
    super.initialize(stateManager, app);
    
    this.app = (SimpleApplication) app; // can cast Application to something more specific
    this.assetManager  = this.app.getAssetManager();
    this.stateManager  = this.app.getStateManager();
    this.inputManager  = this.app.getInputManager();
    this.audioRenderer = this.app.getAudioRenderer();
    this.viewPort      = this.app.getViewPort();
    this.guiViewPort   = this.app.getGuiViewPort();
    this.physics       = this.stateManager.getState(BulletAppState.class);
    this.flyCam        = this.stateManager.getState(FlyCamAppState.class).getCamera();
    
    startMenu();
    
}
      
    public void startMenu(){
    NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
    assetManager, inputManager, audioRenderer, guiViewPort);
    Nifty niftyGetter = niftyDisplay.getNifty();
    niftyGetter.fromXml("Interface/Nifty/GameGUI.xml", "start", this);
    guiViewPort.addProcessor(niftyDisplay);
    niftyGetter.loadControlFile("nifty-default-controls.xml");
    flyCam.setDragToRotate(true);
    }
    
    public void leaveMenu(){
    nifty.gotoScreen("hud");
    gameStart();
    }
    
    public void gameStart(){
    stateManager.detach(new InterfaceAppState());
    physics = new BulletAppState();
    stateManager.attach(physics);
    stateManager.attach(new Player());
    stateManager.attach(new physicalAppState());
    stateManager.attach(new CameraAppState());
    stateManager.attach(new LightingAppState());
    stateManager.attach(new InteractionAppState());
    stateManager.attach(new AnimationAppState());
    stateManager.attach(new GUI());
    }
    
    
    public void bind(Nifty nifty, Screen screen) {
    this.nifty = nifty;
    this.screen = screen;
  }
 
  public void onStartScreen() {
  }
  
 
  public void onEndScreen() {    
  }
}