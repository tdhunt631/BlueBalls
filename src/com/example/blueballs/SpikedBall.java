package com.example.blueballs;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class SpikedBall extends Sprite {
	private float vY;
	private float vX;
	private float X;
	private float Y;
	private int BallSize;
	
	public SpikedBall(float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager){
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		this.vY = (float) 0.3;
		this.vX = (float) 0.3;
		this.BallSize = 100;
	}		

	public void MoveBall(float width, float height) {		
		//if we hit the bottom
		if(this.getY() > height-BallSize-15){
			this.vY = -this.vY;			
		}
		//if we hit the top
		if(this.getY() < 15){
			this.vY = -this.vY;
		}
		//if we hit the right
		if(this.getX() > width-BallSize-15){
			this.vX = -this.vX;			
		}
		//if we hit the left
		if(this.getX() < 15){
			this.vX = -this.vX;
		}
		//move the ball
		this.Y = (this.getY() + vY);
		this.setY(this.Y);
		this.X = (this.getX() + vX);
		this.setX(this.X);		
	}
}
