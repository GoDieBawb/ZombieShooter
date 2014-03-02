/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.scene.Node;

/**
 *
 * @author Bob
 */
public class SoundManager extends AbstractAppState {
   
  public    SimpleApplication      app;
  public    AppStateManager        stateManager;
  public    AssetManager           assetManager;
  public    Node                   rootNode;
  public    AudioNode              gunShot;
  public    AudioNode              emptyGun;
  public    AudioNode              missedPunch;
  public    AudioNode              zombieTalk;
  public    AudioNode              laugh;

@Override
  public void initialize(AppStateManager stateManager, Application app) {
    super.initialize(stateManager, app);
    this.app          = (SimpleApplication) app;
    this.rootNode     = this.app.getRootNode();
    this.assetManager  = this.app.getAssetManager();
    this.stateManager = this.app.getStateManager();
    initAudio();
    System.out.println("AudioManager Attached");
    }

  //Initialize the Sound Nodes

  public void initAudio(){
    gunShot = new AudioNode(assetManager, "Sound/Effects/Gun.wav", false);
    gunShot.setPositional(false);
    gunShot.setLooping(false);
    gunShot.setVolume(.3f);
    rootNode.attachChild(gunShot);

    emptyGun = new AudioNode(assetManager, "Sounds/Gun/Empty.wav", false);
    emptyGun.setPositional(false);
    emptyGun.setLooping(false);
    emptyGun.setVolume(.3f);
    rootNode.attachChild(emptyGun);
    
    missedPunch = new AudioNode(assetManager, "Sounds/Punch/Punch.wav", false);
    missedPunch.setPositional(false);
    missedPunch.setLooping(false);
    missedPunch.setVolume(.3f);
    rootNode.attachChild(missedPunch);
 
    zombieTalk = new AudioNode(assetManager, "Sounds/Monster/zombieTalk.wav", false);
    zombieTalk.setPositional(false);
    zombieTalk.setLooping(false);
    zombieTalk.setVolume(.3f);
    rootNode.attachChild(zombieTalk);  
    
    
    laugh = new AudioNode(assetManager, "Sounds/Laugh/laugh.wav", false);
    laugh.setPositional(false);
    laugh.setLooping(false);
    laugh.setVolume(.3f);
    rootNode.attachChild(laugh);  
    System.out.println("Audio Initialized");
    }
  
   //Play an instance of the sound
  
  public void playSound(Player player){
    if (player.getItemInHand().equals("Gun"))
      if (player.getAmmo(player) > 0)
      gunShot.playInstance();
      else
      emptyGun.playInstance();
    else
    missedPunch.playInstance();
    }
  
  //Zombie sound making methods
  
  public void zombieSounds(Monster monster){
    zombieTalk.playInstance();
    }
  
  public void laughSound(){
    laugh.playInstance();
    }
    
}
