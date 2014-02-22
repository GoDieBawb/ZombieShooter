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
import com.jme3.bullet.BulletAppState;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.FlyByCamera;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import com.jme3.scene.Node;
import org.lwjgl.opengl.Display;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.windows.Window;
import tonegod.gui.core.Element;
import tonegod.gui.core.Screen;

/**
 *
 * @author Bob
 */
public class GUI extends AbstractAppState {
    
    public  Node              rootNode;
    public  SimpleApplication app;
    public  GUI               GUI;
    public    AssetManager    assetManager;   
    
    private AppStateManager   stateManager;
    private BulletAppState    physics;
    private FlyByCamera       flyCam;
    
    private Screen            screen;
    private Window            startMenu;
    private Window            inventoryMenu;
    private Window            handMenu;
    private Window            HUD;
    private String            inventoryCount;
    private Element           killDisplay;
    private Element           healthBar;
    private Element           ammoDisplay;
    
    private float             measure;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app); 
        this.app          = (SimpleApplication) app;
        this.rootNode     = this.app.getRootNode();
        this.assetManager  = this.app.getAssetManager();
        this.stateManager = this.app.getStateManager();
        this.physics      = this.stateManager.getState(BulletAppState.class);
        this.flyCam       = this.stateManager.getState(FlyCamAppState.class).getCamera();
        this.GUI = new GUI();
        startMenu();
    }
    
        /** Start Menu Stuff **/
        
        
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
 
        startMenu.addChild(makeWindow);
        GUI.screen.addElement(startMenu);
        startMenu.setLocalTranslation(Display.getWidth() / 2, Display.getHeight() / 2, 0);
     }
      
  public void gameStart() {
    startMenu.hideWindow();
    physics = new BulletAppState();
    stateManager.attach(physics);
    stateManager.attach(new Player());
    stateManager.attach(new MonsterManager());
    stateManager.attach(new SoundManager());
    stateManager.attach(new SceneManager());
    stateManager.attach(new CameraManager());
    stateManager.attach(new LightManager());
    stateManager.attach(new InteractionManager());
    stateManager.attach(new AnimationManager());

    GUI.handMenu = 
            new Window(GUI.screen, "InventoryWindow", new Vector2f(15f, 15f));
    
      GUI.screen.addElement(GUI.handMenu);
      GUI.handMenu.setDimensions(new Vector2f(100, 100));
      GUI.handMenu.setIsResizable(false);
      GUI.handMenu.setIsMovable(true);
      GUI.handMenu.setLocalTranslation(5f, 5f, 500f);
      
    GUI.HUD =
          new Window(GUI.screen, "HudWindow", new Vector2f(15f, 15f));
    
    GUI.screen.addElement(GUI.HUD);
    GUI.HUD.setDimensions(new Vector2f(200, 275));
    GUI.HUD.setLocalTranslation(500f, 5f, 500f);
    initializeHUD();
    
    GUI.inventoryMenu = 
            new Window(GUI.screen, "HandWindow", new Vector2f(15f, 15f));
   
      GUI.screen.addElement(GUI.inventoryMenu);
      GUI.inventoryMenu.setDimensions(new Vector2f(200, 250));
      GUI.inventoryMenu.setIsVisible(false);
      initCrossHairs();
    }
  
  /** HUD Menu Stuff **/
    public void updateHUDWindow(Player player, GUI GUI){
      String currentHealth = String.valueOf(player.getHealth(player));
      String currentAmmo = String.valueOf(player.getAmmo(player));
      String killCount = String.valueOf(player.getKillCount(player));
      GUI.healthBar.setText("Health: " + currentHealth);
      GUI.ammoDisplay.setText("Ammo: " + currentAmmo);
      GUI.killDisplay.setText("Kills: " + killCount);
      }
    
    public void initializeHUD(){
    GUI.healthBar 
     = new Element(GUI.screen,"healthBar",new Vector2f(5,5),new Vector2f(100,100),new Vector4f(5,5,5,5),"Textures/Face.png"
    );
    GUI.ammoDisplay
     = new Element(GUI.screen,"ammoDisplay",new Vector2f(5,5),new Vector2f(100,100),new Vector4f(5,5,5,5),"Textures/Coin.png"
    );
    GUI.killDisplay
     = new Element(GUI.screen,"killDisplay",new Vector2f(5,5),new Vector2f(100,100),new Vector4f(5,5,5,5),"Textures/splatter.png"
    );
    GUI.HUD.addChild(GUI.healthBar);
    GUI.HUD.addChild(GUI.ammoDisplay);
    GUI.HUD.addChild(GUI.killDisplay);
    GUI.killDisplay.setLocalTranslation(50f, 0f, 100f);
    GUI.ammoDisplay.setLocalTranslation(50f, 90f, 100f);
    GUI.healthBar.setLocalTranslation(50f, 180f, 100f);
    }

  
    /** Inventory Menu Stuff **/
    
 
    //Set up a new window to list the inventory
    public void updateInventoryWindow(Player player, GUI GUI) {
      
      GUI.inventoryMenu.setLocalTranslation(5f, 340f, 5f);
      GUI.inventoryMenu.removeAllChildren();
      
      //For each item in the inventory add a button
      for(int i = 0; i < player.inventory.size(); i++){
        measure = 50f * i + 30;
        inventoryCount = "Button" + i;
        
        try {
        buttonTeller(player.inventory.get(i).toString(), GUI, player);
          } catch (NullPointerException e) {
          }
        }
      
      //Finally add the inventory window to the screen
      GUI.inventoryMenu.setWindowTitle("Inventory Window");
      GUI.inventoryMenu.setIsVisible(true);
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
           AnimationManager animation = new AnimationManager();
            animation.billyEquip(player);
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
    
    public void unequipButton(final GUI GUI, final Player player) {
       ButtonAdapter unequipButton 
              = new ButtonAdapter(GUI.screen, inventoryCount, new Vector2f(20, 12)){
        @Override
        public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
            handUnequip(player, GUI);
        }
      };
      handMenu.addChild(unequipButton);
      unequipButton.setText("air");
      unequipButton.setPosition(15f, measure); 
      }
           
 
 /** Equuipping Methods **/
  
    
  public void gunEquip(Player player, GUI GUI){
     System.out.println("gunEquip");
     GUI.handMenu.removeAllChildren();
     GUI.handMenu.setText("Gun");
     player.setItemInHand("Gun", player);
     player.Model.attachChild(player.placeHolder.getChild("Gun"));
     player.animInteract.gunEquip(player);
     
    }
    
  public void billyEquip(Player player, GUI GUI){
     System.out.println("billyEquip");
     GUI.handMenu.removeAllChildren();
     GUI.handMenu.setText("Billy");
     player.setItemInHand("Billy", player);
    }
  
  public void airEquip(Player player, GUI GUI){
     System.out.println("airEquip");
     GUI.handMenu.removeAllChildren();
     GUI.handMenu.setText("Air");
     player.setItemInHand("Air", player);
    }
  
  public void handUnequip(Player player, GUI GUI){
    }
  
  
  //Here is the crosshairs
  protected void initCrossHairs() {
    BitmapFont guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
    BitmapText ch = new BitmapText(guiFont, false);
    ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
    ch.setText("+");
    ch.setLocalTranslation(
      Display.getWidth() / 2 - guiFont.getCharSet().getRenderedSize() / 3 * 2,
      Display.getHeight() / 2 + ch.getLineHeight() / 2, 0);
    
    this.app.getGuiNode().attachChild(ch);
  }
  
}