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
import com.jme3.math.Transform;
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
    private Window            handMenu;
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
    
        /** Start Meu Stuff **/
        
        
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

    GUI.handMenu = 
            new Window(GUI.screen, "InventoryWindow", new Vector2f(15f, 15f));
    
      GUI.screen.addElement(GUI.handMenu);
      GUI.handMenu.setDimensions(new Vector2f(100, 100));
      GUI.handMenu.setIsResizable(false);
      GUI.handMenu.setLocalTranslation(5f, 5f, 500f);
    
    
    GUI.inventoryMenu = 
            new Window(GUI.screen, "HandWindow", new Vector2f(15f, 15f));
  
      GUI.screen.addElement(GUI.inventoryMenu);
      GUI.inventoryMenu.setDimensions(new Vector2f(200, 250));
      GUI.inventoryMenu.setIsVisible(false);
    }
  
  
  
  
  
  

  
    /** Inventory Menu Stuff **/
    
 
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
        buttonTeller(player.inventory.get(i).toString(), GUI, player);
        System.out.println(inventoryCount);
        }
      
      //Finally add the inventory window to the screen
      GUI.inventoryMenu.setWindowTitle("Inventory Window");
      GUI.inventoryMenu.setIsVisible(true);
      }
    }
  

  

/** Inventory Item Button Adapters **/

    public void buttonTeller(String item, GUI GUI, Player player){
        if (item.equals("Gun"))
         gunButton(GUI, player);

        if (item.equals("Billy"))
         billyButton(GUI, player);

        if (item.equals("air"))
         airButton(GUI, player);
    }
   
   
   /** Actual Buttoon Adaptars **/
    

    public void gunButton(final GUI GUI, final Player player) {
       ButtonAdapter gunButton 
              = new ButtonAdapter(GUI.screen, inventoryCount, new Vector2f(20, 12)) {
        @Override
        public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
            gunEquip(player, GUI);
        }    
      };
      inventoryMenu.addChild(gunButton);
      gunButton.setText("Gun");
      gunButton.setPosition(15f, measure);  
    }
    
    
    public void billyButton(final GUI GUI, final Player player) {
       ButtonAdapter billyButton 
              = new ButtonAdapter(GUI.screen, inventoryCount, new Vector2f(20, 12)){
        @Override
        public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
            billyEquip(player, GUI);
        }
      };
      inventoryMenu.addChild(billyButton);
      billyButton.setText("billy");
      billyButton.setPosition(15f, measure);
    }


    
    public void airButton(final GUI GUI, final Player player) {
       ButtonAdapter airButton 
              = new ButtonAdapter(GUI.screen, inventoryCount, new Vector2f(20, 12)){
        @Override
        public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
            airEquip(player, GUI);
        }
      };
      inventoryMenu.addChild(airButton);
      airButton.setText("air");
      airButton.setPosition(15f, measure);  
    }
    
    
    
 
 /** Equuipping Methods **/
  
    
  public void gunEquip(Player player, GUI GUI){
     System.out.println("gunEquip");
     GUI.handMenu.removeAllChildren();
     GUI.handMenu.setText("Gun");
     player.setItemInHand("Gun");
    }
    
  public void billyEquip(Player player, GUI GUI){
     System.out.println("billyEquip");
     GUI.handMenu.removeAllChildren();
     GUI.handMenu.setText("Billy");
     player.setItemInHand("Billy");
    }
  
  public void airEquip(Player player, GUI GUI){
     System.out.println("bilyEquip");
     GUI.handMenu.removeAllChildren();
     GUI.handMenu.setText("Air");
     player.setItemInHand("Air");
    }
}