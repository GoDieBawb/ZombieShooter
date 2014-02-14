/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.jme3.animation.Skeleton;
import com.jme3.animation.SkeletonControl;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Matrix3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.SkeletonDebugger;

/**
 *
 * @author Bob
 */
public class AnimationAppState extends AbstractAppState {
    
private SimpleApplication app;
private AssetManager      assetManager;
private AppStateManager   stateManager;


public String             armAnim;
public String             legAnim;

public Player             player;
public AnimChannel        armChannel;
public AnimChannel        legChannel;
public AnimControl        animControl;
public SkeletonControl    skelControl;

    
 @Override
  public void initialize(AppStateManager stateManager, Application app) {
    super.initialize(stateManager, app);
    
    this.app = (SimpleApplication) app; // can cast Application to something more specific
    this.assetManager = this.app.getAssetManager();
    this.stateManager = this.app.getStateManager();
    this.player        = this.stateManager.getState(Player.class).player;
    this.armAnim      = this.stateManager.getState(InteractionAppState.class).armAnim;
    this.legAnim      = this.stateManager.getState(InteractionAppState.class).legAnim;
    
    animationInit();
    }
    
    public void animationInit(){
          
    animControl = findAnimControl(player.Model);
    legChannel  = animControl.createChannel();
    legChannel.addFromRootBone("BottomSpine") ;
    armChannel  = animControl.createChannel();
    armChannel.addFromRootBone("TopSPine");
    
      // add a skeleton debugger to make bones visible
      final Skeleton skel = animControl.getSkeleton();
      final SkeletonDebugger skeletonDebug = new SkeletonDebugger("skeleton",skel);
      final Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
      mat.setColor("Color", ColorRGBA.Green);
      mat.getAdditionalRenderState().setDepthTest(false);
      skeletonDebug.setMaterial(mat);
      ((Node) animControl.getSpatial()).attachChild(skeletonDebug);
      System.out.println("Animations Initialized");

 }

    public void animChange(String armAnim, String legAnim){
      armChannel.setAnim(armAnim);
      armChannel.setLoopMode(LoopMode.Loop);
      legChannel.setAnim(legAnim);
      legChannel.setLoopMode(LoopMode.Loop);
    
    }
    
    
    
    public AnimControl findAnimControl(final Spatial parent){
    
    animControl = parent.getControl(AnimControl.class);
    if (animControl != null) {
      return animControl;
    }
 
    if (parent instanceof Node) {
      for (final Spatial s : ((Node) parent).getChildren()) {
        final AnimControl animControl2 = findAnimControl(s);
        if (animControl2 != null) {
        return animControl2;
        }
      }
    }
    return null;
  }
   
  public void billyEquip(Player player){
    skelControl = findSkelControl(player.Model);
    Node hand = skelControl.getAttachmentsNode(skelControl.getSkeleton().getBone(5).getName());
    hand.attachChild(player.Model.getChild("Billy"));
     
    }
  
  public void gunEqip(Player player) {
    skelControl = findSkelControl(player.Model);
    Node hand = 
        skelControl.getAttachmentsNode(skelControl.getSkeleton().getBone(5).getName());
    hand.attachChild(player.Model.getChild("Gun"));
    player.Model.getChild("Gun").setLocalTranslation(1, 2, 1);
    player.Model.getChild("Gun").setLocalScale(.3f);
    player.Model.getChild("Gun").setLocalRotation
        (new Matrix3f(1f, 5f, 5f, 1f, 1f, 1f, 1f, 5f, 1f));
    player.Model.getChild("Gun").setLocalTranslation(-1f, 5f, 2.5f);
      
    }
  
  public SkeletonControl findSkelControl(final Spatial parent){
    
    skelControl = parent.getControl(SkeletonControl.class);
    if (skelControl != null) {
      return skelControl;
    }
 
    if (parent instanceof Node) {
      for (final Spatial s : ((Node) parent).getChildren()) {
        final SkeletonControl skelControl2 = findSkelControl(s);
        if (skelControl2 != null) {
        return skelControl2;
        }
      }
    }
    return null;
  }
}