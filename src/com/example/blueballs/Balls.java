package com.example.blueballs;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Balls extends Sprite {
	private float vY;
	private float vX;
	private float X;
	private float Y;
	private int BallSize;
	
	public Balls(float vX, float vY, float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager){
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		this.vY = vY;
		this.vX = vX;
		this.BallSize = 50;
	}		
	
	public void SetvX(float vX){
		this.vX =  this.vX * vX;
	}
	
	public void SetvY(float vY){
		this.vY = this.vY * vY;
	}
	
	public float GetvX(){
		return this.vX;
	}
	
	public float GetvY(){
		return this.vY;
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
