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
    private String            inventoryCount;
    
    private float             measure;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app); 
        this.app          = (SimpleApplication) app;
        this.rootNode     = this.app.getRootNode();
        this.stateManager = this.app.getStateManager();
        this.physics      = this.stateManager.getState(BulletAppState.class);
        this.flyCam       = this.stateManager.getState(FlyCamAppState.class).getCamera();
        this.GUI = new GUI();
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
      
     if (GUI.inventoryMenu.getIsVisible()) {
      GUI.inventoryMenu.removeAllChildren();
      GUI.inventoryMenu.setIsVisible(false);

      } else {
      
      //For each item in the inventory add a button
      for(int i = 0; i < player.inventory.size(); i++){
        measure = 50f * i + 30;
        inventoryCount = "Button" + i;
        buttonTeller(player.inventory.get(i).toString(), GUI);
        System.out.println(inventoryCount);
        }
      
      //Finally add the inventory window to the screen
      GUI.inventoryMenu.setWindowTitle("Inventory Window");
      GUI.inventoryMenu.setIsVisible(true);
      }
    }
    
  public void gameStart() {
    startMenu.hideWindow();
    physics = new BulletAppState();
    stateManager.attach(physics);
    stateManager.attach(new Player());
    stateManager.attach(new physicalAppState());
    stateManager.attach(new CameraAppState());
    stateManager.attach(new LightingAppState());
    stateManager.attach(new InteractionAppState());
    stateManager.attach(new AnimationAppState());
    GUI.inventoryMenu = 
            new Window(GUI.screen, "InventoryWindow", new Vector2f(15f, 15f));

    GUI.screen.addElement(GUI.inventoryMenu);
    GUI.inventoryMenu.setMinDimensions(new Vector2f(130, 100));
    GUI.inventoryMenu.setIsVisible(false);
    }
  

  

/** Inventory Item Button Adapters **/

    public void buttonTeller(String item, GUI GUI){
        if (item.equals("Gun"))
         gunButton(GUI);

        if (item.equals("Billy"))
         billyButton(GUI);

        if (item.equals("air"))
         airButton(GUI);
    }
    
    

    public void gunButton(GUI GUI) {
       ButtonAdapter gunButton 
              = new ButtonAdapter(GUI.screen, inventoryCount, new Vector2f(20, 12)){
      };
      inventoryMenu.addChild(gunButton);
      gunButton.setText("Gun");
      gunButton.setPosition(15f, measure);  
    }
    
    
    public void billyButton(GUI GUI) {
       ButtonAdapter billyButton 
              = new ButtonAdapter(GUI.screen, inventoryCount, new Vector2f(20, 12)){
      };
      inventoryMenu.addChild(billyButton);
      billyButton.setText("billy");
      billyButton.setPosition(15f, measure);
    }


    
    public void airButton(GUI GUI) {
       ButtonAdapter airButton 
              = new ButtonAdapter(GUI.screen, inventoryCount, new Vector2f(20, 12)){
      };
      inventoryMenu.addChild(airButton);
      airButton.setText("air");
      airButton.setPosition(15f, measure);  
    }
    
}