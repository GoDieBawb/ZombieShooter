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
    public  AssetManager      assetManager;   
    
    private AppStateManager   stateManager;
    private BulletAppState    physics;
    private FlyByCamera       flyCam;
    
    public  Screen            screen;
    public  Window            startMenu;
    public  Window            inventoryMenu;
    public  Window            handMenu;
    public  Window            HUD;
    public  Window            EndMenu;
    private String            inventoryCount;
    private Element           killDisplay;
    private Element           healthBar;
    private Element           ammoDisplay;
    
    
    private float             measure;
    private boolean           firstTime;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app); 
        this.app          = (SimpleApplication) app;
        this.rootNode     = this.app.getRootNode();
        this.assetManager  = this.app.getAssetManager();
        this.stateManager = this.app.getStateManager();
        this.physics      = this.stateManager.getState(BulletAppState.class);
        this.flyCam       = this.stateManager.getState(FlyCamAppState.class).getCamera();
        
        firstTime = true;
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
        startMenu.setLocalTranslation(Display.getWidth() / 2 - startMenu.getWidth()/2, Display.getHeight() / 2 + startMenu.getHeight()/2, 0);
     }
      
  //Method to Start the Game and attach Game Application States    
      
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
            new Window(GUI.screen, "HandWindow", new Vector2f(15f, 15f));
    
      GUI.screen.addElement(GUI.handMenu);
      GUI.handMenu.setDimensions(new Vector2f(200,100));
      GUI.handMenu.setIsResizable(false);
      GUI.handMenu.setIsMovable(false);
      GUI.handMenu.setLocalTranslation(0, 0, 0);
      
    GUI.HUD =
          new Window(GUI.screen, "HudWindow", new Vector2f(15f, 15f));
    
    GUI.screen.addElement(GUI.HUD);
    GUI.HUD.setDimensions(new Vector2f(125, 100));
    GUI.HUD.setLocalTranslation(Display.getWidth()- GUI.HUD.getWidth(), 0, 0);
    initializeHUD();
    
    
    GUI.inventoryMenu = 
            new Window(GUI.screen, "InventoryWindow", new Vector2f(15f, 15f));
   
      GUI.screen.addElement(GUI.inventoryMenu);
      GUI.inventoryMenu.setDimensions(new Vector2f(200, 100));
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
     = new Element(GUI.screen,"healthBar",new Vector2f(5,5),new Vector2f(100,100),new Vector4f(5,5,5,5),"Textures/Empty.png"
    );
    GUI.ammoDisplay
     = new Element(GUI.screen,"ammoDisplay",new Vector2f(5,5),new Vector2f(100,100),new Vector4f(5,5,5,5),"Textures/Empty.png"
    );
    GUI.killDisplay
     = new Element(GUI.screen,"killDisplay",new Vector2f(5,5),new Vector2f(100,100),new Vector4f(5,5,5,5),"Textures/Empty.png"
    );
    GUI.HUD.addChild(GUI.healthBar);
    GUI.HUD.addChild(GUI.ammoDisplay);
    GUI.HUD.addChild(GUI.killDisplay);
    GUI.killDisplay.setLocalTranslation(30f, -5f, 50f);
    GUI.ammoDisplay.setLocalTranslation(30f, -30f, 50f);
    GUI.healthBar.setLocalTranslation(30f, -55f, 50f);
    }

  
    /** Inventory Menu Stuff **/
    
 
    //Set up a new window to list the inventory
    public void updateInventoryWindow(Player player, GUI GUI) {
      
      GUI.inventoryMenu.setLocalTranslation(0, Display.getHeight() - GUI.inventoryMenu.getHeight(), 0);
      GUI.inventoryMenu.removeAllChildren();
      
      //For each item in the inventory add a button
      for(int i = 0; i < player.inventory.size(); i++){
        measure = 30f * i + 30;
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
    
    
    //Ending Menu Stuff
    public void createEndMenu(){
      GUI.EndMenu =
        new Window(GUI.screen, "EndWindow", new Vector2f(15f, 15f));
      this.app.getGuiNode().detachChild(this.app.getGuiNode().getChild(5));
      GUI.screen.addElement(GUI.EndMenu);
      GUI.EndMenu.setDimensions(new Vector2f(130, 100));
      GUI.EndMenu.setLocalTranslation
              (Display.getWidth() / 2 - GUI.EndMenu.getWidth()/2, Display.getHeight() / 2 + GUI.EndMenu.getHeight()/2, 0);
      quitButton(GUI);
      restartButton(GUI);
      }
  
    public void quitGame(){
      app.stop();
      }
    
    public void restartGame(){
      stateManager.detach(stateManager.getState(InteractionManager.class));
      stateManager.detach(stateManager.getState(CameraManager.class));
      stateManager.detach(stateManager.getState(AnimationManager.class));
      stateManager.detach(stateManager.getState(SceneManager.class));
      stateManager.detach(stateManager.getState(SoundManager.class));
      stateManager.detach(stateManager.getState(Player.class));
      LightManager light = stateManager.getState(LightManager.class);
      rootNode.removeLight(light.flashLight);
      rootNode.removeLight(light.al);
      stateManager.detach(stateManager.getState(LightManager.class));
      stateManager.detach(physics);
      firstTime = false;
      GUI.EndMenu.hideWindow();
      startMenu.showWindow();
      
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
    
    public void quitButton(final GUI GUI) {
      ButtonAdapter quitButton
             = new ButtonAdapter(GUI.screen, "QuitButton", new Vector2f(20, 12)){
        @Override
        public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
            quitGame();
        }
      };
      GUI.EndMenu.addChild(quitButton);
      quitButton.setLocalTranslation(15f, 15f, 1f);
      quitButton.setText("Quit Game");
    }
    
    public void restartButton(final GUI GUI) {
      ButtonAdapter restartButton =
              new ButtonAdapter(GUI.screen, "RestartButton", new Vector2f(20,12)){
        @Override
        public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled){
          restartGame();
        }
      };
      GUI.EndMenu.addChild(restartButton);
      restartButton.setText("Play Again");
      
    }
    
    public void unequipButton(final GUI GUI, final Player player) {
       ButtonAdapter unequipButton 
              = new ButtonAdapter(GUI.screen, "UnequipButton", new Vector2f(20, 12)){
        @Override
        public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
            handUnequip(player, GUI);
        }
      };
       
      GUI.handMenu.addChild(unequipButton);
      unequipButton.setText("Unequip " + player.getItemInHand());
      unequipButton.setPosition(15f, 15f); 
      }

    public void gunButton(final GUI GUI, final Player player) {
       ButtonAdapter gunButton 
              = new ButtonAdapter(GUI.screen, inventoryCount, new Vector2f(20, 12)) {
        @Override
        public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
            gunEquip(player, GUI);
            unequipButton(GUI, player);
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
    
           
 
 /** Equuipping Methods **/
  
    
  public void gunEquip(Player player, GUI GUI){
     System.out.println("gunEquip");
     player.setItemInHand("Gun", player);
     player.Model.attachChild(player.placeHolder.getChild("Gun"));
     player.animInteract.gunEquip(player);
     
    }
    
  public void billyEquip(Player player, GUI GUI){
     System.out.println("billyEquip");
     GUI.handMenu.setText("Billy");
     player.setItemInHand("Billy", player);
    }
  
  public void airEquip(Player player, GUI GUI){
     System.out.println("airEquip");
     GUI.handMenu.setText("Air");
     player.setItemInHand("Air", player);
    }
  
  public void handUnequip(Player player, GUI GUI){
     GUI.handMenu.removeAllChildren();
     GUI.handMenu.setText("");
     player.placeHolder.attachChild(player.Model.getChild(player.getItemInHand()));
     player.setItemInHand("Nothing", player);
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