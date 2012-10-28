package com.example.blueballs;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.debug.Debug;

public class BlueBalls extends SimpleBaseGameActivity implements ITimerCallback{
	
	private static int CAMERA_WIDTH = 800;
	private static int CAMERA_HEIGHT = 480;
	float vX;
	float vY;
	List<Object> mBallList = new ArrayList<Object>();
	private ITextureRegion mBallTextureRegion, mBackgroundTexture, mSpikedBallTexture;
	private SpikedBall spike;
	private Scene scene;

	public EngineOptions onCreateEngineOptions() {
		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    	return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	}

	@Override
	protected void onCreateResources() {
        try {
            ITexture ballTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
                @Override
                public InputStream open() throws IOException {
                    return getAssets().open("gfx/ball.png");
                }
            });
            ITexture spikedBallTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
                @Override
                public InputStream open() throws IOException {
                    return getAssets().open("gfx/spikedBall.png");
                }
            });
            ITexture bgTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener(){
            	@Override
            	public InputStream open() throws IOException {
            		return getAssets().open("gfx/bg.png");
            	}
            });
            
            ballTexture.load();
            spikedBallTexture.load();
            bgTexture.load();
            
            this.mBallTextureRegion = TextureRegionFactory.extractFromTexture(ballTexture);
            this.mSpikedBallTexture = TextureRegionFactory.extractFromTexture(spikedBallTexture);
            this.mBackgroundTexture = TextureRegionFactory.extractFromTexture(bgTexture);
            
        } catch (IOException e) {
            Debug.e(e);
        }
	}

	@Override
	protected Scene onCreateScene() {
		scene = new Scene();
		Sprite backgroundSprite = new Sprite(0, 0, this.mBackgroundTexture, getVertexBufferObjectManager());				
		scene.attachChild(backgroundSprite);
		
		//start the spiked ball
    	spike = new SpikedBall(400, 200, mSpikedBallTexture, getVertexBufferObjectManager());
    	scene.attachChild(spike);
		
		scene.setOnSceneTouchListener(new IOnSceneTouchListener(){
            @Override
            public boolean onSceneTouchEvent(Scene pScene,TouchEvent pSceneTouchEvent){
            	//get touch location
	            float X = pSceneTouchEvent.getX();
	            float Y = pSceneTouchEvent.getY();
	            
	            //create random float between -1.5 and 1.5
	            Random r = new Random();
	            int R = r.nextInt(100-1) + 1;
	            if(R % 2 == 0){
		           vX = (float) (0.3 + (Math.random() * (1.5 - 0.5)));
		           vY = (float) (0.3 + (Math.random() * (1.5 - 0.5)));
	            }
	            else{
	            	vX = -(float) (0.3 + (Math.random() * (1.5 - 0.5)));
			        vY = -(float) (0.3 + (Math.random() * (1.5 - 0.5)));
	            }
	            
	            if(pSceneTouchEvent.isActionUp()){	            
		            Balls ball = new Balls(vX, vY, X, Y, mBallTextureRegion, getVertexBufferObjectManager());
		     		mBallList.add(ball);
		     		//attach each ball to the scene
		     		scene.attachChild(ball);	
				}
	            
	            //check for ball collisions
	        	scene.registerUpdateHandler(new IUpdateHandler() {
	        		@Override
	        		public void onUpdate(float pSecondsElapsed){
	        			//bounce blue balls off each other
//	        			for(int i=0; i<mBallList.size(); i++){
//	        				for(int j=0; j<mBallList.size(); j++){
//	        					if(i!=j){
//	        						if(((Balls) mBallList.get(i)).collidesWith(((Balls) mBallList.get(j)))) {
//	        							((Balls) mBallList.get(i)).SetvX(-1);
//	        							((Balls) mBallList.get(j)).SetvX(-1);
//	        							((Balls) mBallList.get(i)).SetvY(-1);
//	        							((Balls) mBallList.get(j)).SetvY(-1);
//	    		        			}
//	        					}		        				
//		        			}
//	        			}
	        			
	        			//if we hit the spike pop the blue ball
	        			for(int i=0; i<mBallList.size(); i++){
		        			if(((Balls) mBallList.get(i)).collidesWith(spike)) {
			        			((Balls) mBallList.get(i)).detachSelf();
		        				mBallList.remove(i);
		        			}
	        			}
	        		}
					@Override
					public void reset() {}
	        	}); 	            
				return true;		
            }
        });
		scene.setTouchAreaBindingOnActionDownEnabled(true);
		
	    TimerHandler timerHandler = new TimerHandler(0.003f, true, this);
	    scene.registerUpdateHandler(timerHandler);		
		return scene;
	}
	
	@Override
	public void onTimePassed(TimerHandler pTimerHandler){
		//move the balls and bounce off walls
		for(int i=0; i<mBallList.size(); i++){
			((Balls) mBallList.get(i)).MoveBall(CAMERA_WIDTH,CAMERA_HEIGHT);
		}		
		spike.MoveBall(CAMERA_WIDTH, CAMERA_HEIGHT);
	}	
}
