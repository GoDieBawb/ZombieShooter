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
  public    AudioNode              audio_gun;

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

  public void initAudio(){
    audio_gun = new AudioNode(assetManager, "Sound/Effects/Gun.wav", false);
    audio_gun.setPositional(false);
    audio_gun.setLooping(false);
    audio_gun.setVolume(2);
    rootNode.attachChild(audio_gun);
 
    /* nature sound - keeps playing in a loop. */
    AudioNode audio_nature = new AudioNode(assetManager, "Sound/Environment/Ocean Waves.ogg", true);
    audio_nature.setLooping(true);  // activate continuous playing
    audio_nature.setPositional(true);   
    audio_nature.setVolume(3);
    rootNode.attachChild(audio_nature);
    audio_nature.play(); // play continuously!
    System.out.println("Audio Initialized");
    }
  
  public void playSound(Player player){
    if (player.getItemInHand().equals("Gun"))
      if (player.getAmmo(player) > 0)
      audio_gun.playInstance();
    }
    
}
