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
import tonegod.gui.controls.buttons.Button;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.windows.Window;
import tonegod.gui.core.Screen;

/**
 *
 * @author Bob
 */
public class GUI extends AbstractAppState {
    
    private Screen            screen;
    public  Node              rootNode;
    public  SimpleApplication app;
    private int               winCount = 0;
    private AppStateManager   stateManager;
    private BulletAppState    physics;
    private FlyByCamera       flyCam;
    private Window            win;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app); 
        this.app          = (SimpleApplication) app;
        this.rootNode     = this.app.getRootNode();
        this.stateManager  = this.app.getStateManager();
        this.physics       = this.stateManager.getState(BulletAppState.class);
        this.flyCam        = this.stateManager.getState(FlyCamAppState.class).getCamera();
        startMenu();
    }
        
        
      public void startMenu(){  
        screen = new Screen(app);
        this.app.getGuiNode().addControl(screen);
        win = new Window(screen, "MainWindow", new Vector2f(15f, 15f));
        win.setWindowTitle("Main Windows");
        win.setMinDimensions(new Vector2f(130, 100));
        win.setWidth(new Float(50));
        win.setIgnoreMouse(true);
        win.setWindowIsMovable(false);
        flyCam.setDragToRotate(true);
        
 
    // create buttons
    ButtonAdapter makeWindow = new ButtonAdapter( screen, "Btn1", new Vector2f(15, 15) ) {
        @Override
        public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
            gameStart();
        }  
    };
    makeWindow.setText("Start Game");
 
    // Add it to out initial window
    win.addChild(makeWindow);
    // Add window to the screen
   screen.addElement(win);
   win.setLocalTranslation(350f, 350f, 350f);
    }
 
 public final void createNewWindow(String someWindowTitle) {
    Window newWin = new Window(
        screen,
        "Window" + winCount,
        new Vector2f( (screen.getWidth()/2)-175, (screen.getHeight()/2)-100 ));
    newWin.setWindowTitle(someWindowTitle);
    screen.addElement(newWin);
    winCount++;
}

    
  public void gameStart() {
    System.out.println("Game Started " + screen);
    win.hideWindow();
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
