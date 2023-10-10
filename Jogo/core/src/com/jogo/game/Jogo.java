package com.jogo.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.Iterator;

import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;


public class Jogo extends ApplicationAdapter {

	private SpriteBatch batch;
	private Texture img, tCharacter, tObstacle;
	private Sprite character;	
	private float posX, posY, velocity;
	private Array<Rectangle> obstacles;
	private long frequenciaObstaculo;
	private FreeTypeFontGenerator generator;
	private FreeTypeFontGenerator.FreeTypeFontParameter param;
	private BitmapFont bitmap;
	private int power;
	private boolean gameover;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("fundo.png");
		tCharacter = new Texture("personagem.png");
		character = new Sprite(tCharacter);
		tObstacle = new Texture("enemy.png");
		obstacles = new Array<Rectangle>();
		posX = 100;
		posY = 100;
		velocity = 10;
		frequenciaObstaculo = 0;
		
		power = 3;
		
		generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
		param = new FreeTypeFontGenerator.FreeTypeFontParameter();
		param.size = 30;
		param.borderWidth = 1;
		param.borderColor = Color.BLACK;
		param.color = Color.WHITE;
		bitmap = generator.generateFont(param);
		
		gameover = false;
		
	}

	@Override
	public void render () {
		
		this.moveObstacles();
		this.moveChar();
		
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(img, 0, 0);	
		batch.draw(tCharacter, posX, posY);
		moveChar();
		
		for (Rectangle obstacle : obstacles) {
			batch.draw(tObstacle, obstacle.x, obstacle.y);
		}
				
		bitmap.draw(batch, "Power: " + power, Gdx.graphics.getWidth() - 150, Gdx.graphics.getHeight() - 20);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		tCharacter.dispose();
	}
	
	public void moveChar() {
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			if (posY < Gdx.graphics.getHeight() - character.getHeight()) {
				posY += velocity;
			}
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			if (posY > 0) {
				posY -= velocity;
			}
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			if (posX < Gdx.graphics.getWidth() - character.getWidth() ) {
				posX += velocity;
			}
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			if (posX  > 0) {
				posX -= velocity;
			}
		}
	}
	
	private void spawnObstacle () {
		Rectangle obstacle = new Rectangle(Gdx.graphics.getWidth(), MathUtils.random(0, Gdx.graphics.getHeight() - tObstacle.getHeight()), tObstacle.getWidth(), tObstacle.getHeight());
		obstacles.add(obstacle);
		frequenciaObstaculo = TimeUtils.nanoTime();
	}
	
	private void moveObstacles() {
		if(TimeUtils.nanoTime() - frequenciaObstaculo > 777777777) {
			this.spawnObstacle();
		}
			
		
		for(Iterator<Rectangle> iter = obstacles.iterator(); iter.hasNext();) {
			Rectangle obstacle = iter.next();
			obstacle.x -= 10;
			
			if (collide(obstacle.x, obstacle.y, obstacle.height, obstacle.width, posX, posY, character.getWidth(), character.getHeight() ) && !gameover ) {
				iter.remove();
				power--;
				if (power <= 0) {
					gameover = true;
				}
			}
			if (obstacle.x < 0-obstacle.width) {
				iter.remove();
			}
		}
		
	}
	
	private boolean collide (float x1, float y1, float w1, float h1, float x2, float y2, float w2, float h2) {
		if (x1 + w1 > x2 && x1 < x2 + w2 && y1 + h1 > y2 && y1 < y2 + h2) {
			return true;
		}
		return false;
	}
	
}