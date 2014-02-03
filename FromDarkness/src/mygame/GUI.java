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
import com.jme3.bullet.BulletAppState;
import com.jme3.input.FlyByCamera;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import com.jme3.scene.Node;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.windows.Window;
import tonegod.gui.core.Screen;

/**
 *
 * @author Bob
 */
public class GUI extends AbstractAppState {
    
    public  Node              rootNode;
    public  SimpleApplication app;
    public  GUI               GUI;
    
    private AppStateManager   stateManager;
    private BulletAppState    physics;
    private FlyByCamera       flyCam;
    
    private Screen            screen;
    private Window            startMenu;
    private Window            inventoryMenu;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app); 
        this.app          = (SimpleApplication) app;
        this.rootNode     = this.app.getRootNode();
        this.stateManager = this.app.getStateManager();
        this.physics      = this.stateManager.getState(BulletAppState.class);
        this.flyCam       = this.stateManager.getState(FlyCamAppState.class).getCamera();
        this.GUI = new GUI();
        System.out.println("GUI IS " + GUI);
        startMenu();
    }
        
        
      public void startMenu(){  
        GUI.screen = new Screen(app);
        this.app.getGuiNode().addControl(GUI.screen);
        startMenu = new Window(GUI.screen, "MainWindow", new Vector2f(15f, 15f));
        startMenu.setWindowTitle("Main Windows");
        startMenu.setMinDimensions(new Vector2f(130, 100));
        startMenu.setWidth(new Float(50));
        startMenu.setHeight(new Float (15));
        startMenu.setIgnoreMouse(true);
        startMenu.setWindowIsMovable(false);
        flyCam.setDragToRotate(true);
        
    // create buttons
    ButtonAdapter makeWindow = new ButtonAdapter( GUI.screen, "Btn1", new Vector2f(15, 15) ) {
        @Override
        public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
            gameStart();
        }  
    };
    makeWindow.setText("Start Game");
 
      // Add it to out initial window
      startMenu.addChild(makeWindow);
      // Add window to the screen
     GUI.screen.addElement(startMenu);
     startMenu.setLocalTranslation(350f, 350f, 350f);
      }
      
    //Set up a new window to list the inventory
    public void inventoryWindow(Player player, GUI GUI) {
      
      GUI.inventoryMenu = new Window(GUI.screen, "InventoryWindow", new Vector2f(15f, 15f));
      GUI.inventoryMenu.setWindowTitle("Inventory Window");
      GUI.inventoryMenu.setMinDimensions(new Vector2f(130, 100));

      //Button Adapter for interactions in the inventory
      ButtonAdapter inventoryLister = new ButtonAdapter(screen, "Btn2", new Vector2f(15, 15)){
      };
      
      //For each item in the inventory add a button
      for(int i = 0; i < player.inventory.size(); i++){
        System.out.println("Real List " + player.inventory.get(i));
        inventoryMenu.addChild(inventoryLister);
        } 

      //Finally add the inventory window to the screen
      GUI.screen.addElement(inventoryMenu);
    }

    
  public void gameStart() {
    System.out.println("Game Started " + GUI.screen + GUI);
    startMenu.hideWindow();
    physics = new BulletAppState();
    stateManager.attach(physics);
    stateManager.attach(new Player());
    stateManager.attach(new physicalAppState());
    stateManager.attach(new CameraAppState());
    stateManager.attach(new LightingAppState());
    stateManager.attach(new InteractionAppState());
    stateManager.attach(new AnimationAppState());
    }
}
