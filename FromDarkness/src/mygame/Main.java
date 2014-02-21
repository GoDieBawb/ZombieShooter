package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;


public class Main extends SimpleApplication {
  


    public static void main(String[] args) {
        Main app = new Main();
        app.setShowSettings(false);
        app.setDisplayStatView(false);
        app.start();
    }

    @Override
    public void simpleInitApp(){
    stateManager.attach(new GUI());
    System.out.println("Main State Attached");
    
    }
       

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
