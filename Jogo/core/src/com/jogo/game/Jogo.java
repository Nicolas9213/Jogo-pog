package com.jogo.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import java.util.Calendar;

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
	private Temporizador temp;
	private Calendar tempoAtual;
	private int frequenciaAtual = 777777777;
	private int frequenciaAntiga = 777777778;
	private float velocidadeAtual = 10;
	private float velocidadeAntiga = 9;
	private long tempoAuxilio = -5010;
	private int contador = 0;
	private int contadorSh = 0;
	private boolean shieldAct = false;
	private long tempoAuxilioSh = -5010;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("fundo.jpg");
		tCharacter = new Texture("personagem.png");
		character = new Sprite(tCharacter);
		tObstacle = new Texture("inimigo.png");
		obstacles = new Array<Rectangle>();
		posX = 100;
		posY = 100;
		velocity = 12;
		frequenciaObstaculo = 10;
		
		power = 3;
		
		generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
		param = new FreeTypeFontGenerator.FreeTypeFontParameter();
		param.size = 30;
		param.borderWidth = 1;
		param.borderColor = Color.BLACK;
		param.color = Color.WHITE;
		bitmap = generator.generateFont(param);
		
		gameover = false;
		
		temp = new Temporizador();
	}

	@Override
	public void render () {
		
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(img, 0, 0);	
		batch.draw(tCharacter, posX, posY);
				
		if (!gameover) {
			this.moveChar();
			this.moveObstacles(temp.getTempo()/1000);
			this.shield(temp.getTempo()/1000);
			for (Rectangle obstacle : obstacles) {
				batch.draw(tObstacle, obstacle.x, obstacle.y);
			}
			bitmap.draw(
					batch, "Vidas: " + power,
					20,
					Gdx.graphics.getHeight() - 20
					);
			bitmap.draw(batch, "Tempo: " + temp.getTempo()/1000, Gdx.graphics.getWidth() - 160, Gdx.graphics.getHeight() - 20);
		} else {
			bitmap.draw(
					batch, "GAME OVER!",
					Gdx.graphics.getWidth() / 2 - 75,
					Gdx.graphics.getHeight() /2 + 10
					);
			
			bitmap.draw(batch, temp.getTempoMorte(), Gdx.graphics.getWidth() - 160, Gdx.graphics.getHeight() - 20);
			
			posX = 9999999;
			posY = 9999999;
			
		if ( Gdx.input.isKeyPressed(Input.Keys.ENTER) ) {
			gameover = false;
			posX = 0;
			posY = 0;
			power = 3;
			tempoAtual = Calendar.getInstance();
			temp.setTempo(tempoAtual.getTimeInMillis());
			velocidadeAtual = 10;
			velocidadeAntiga = 9;
			frequenciaAtual = 777777777;
			frequenciaAntiga= 777777778;
			tempoAuxilio = -5010;
			tempoAuxilioSh = -5010;
		}
		}
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		tCharacter.dispose();
	}
	
	public void moveChar() {
		if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
			if (posY < Gdx.graphics.getHeight() - character.getHeight()) {
				posY += velocity;
			}
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
			if (posY > 0) {
				posY -= velocity;
			}
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
			if (posX < Gdx.graphics.getWidth() - character.getWidth() ) {
				posX += velocity;
			}
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
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
	
	private void shield (long tempo) {
		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || tempo - tempoAuxilioSh >= -5 && tempo - tempoAuxilioSh <= 0) {
			if (contadorSh == 0) {
				tCharacter = new Texture("escudo.png");
				tempoAuxilioSh = tempo + 5;
				contadorSh++;
				shieldAct = true;
				
			}
	
		}else {
			tCharacter = new Texture("personagem.png");
			shieldAct = false;
			if (tempo - tempoAuxilioSh >= 30) {
				contadorSh = 0;
			}
		}
	}
	private void moveObstacles(long tempo) {
		if(TimeUtils.nanoTime() - frequenciaObstaculo > frequenciaGeracaoObstaculo(tempo)) {
			this.spawnObstacle();
		}
		
		for(Iterator<Rectangle> iter = obstacles.iterator(); iter.hasNext();) {
			Rectangle obstacle = iter.next();
			if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || tempo - tempoAuxilio >= -5 && tempo - tempoAuxilio <= 0) {
				if (contador == 0) {
					tempoAuxilio = tempo + 5;
					contador++;
				}
				obstacle.x -= 10;
			}else {
				obstacle.x -= velocidadeGeracaoObstaculo(tempo);
				if (tempo - tempoAuxilio >= 10) {
					contador = 0;
					System.out.println("Passou");
				}
				System.out.println("TempoAuxilio: " + tempoAuxilio);
				System.out.println("Tempo: " + tempo);
			}
			
			if (collide(obstacle.x, obstacle.y, obstacle.height, obstacle.width, posX, posY, character.getWidth(), character.getHeight() ) && !gameover ) {
				iter.remove();
				if (!shieldAct) {
					power--;
				}
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
	
	private int frequenciaGeracaoObstaculo (long tempo) {
		if (tempo > 15 && tempo < 45) {
			if(frequenciaAtual < frequenciaAntiga) {
				frequenciaAntiga = frequenciaAtual;
				frequenciaAtual -= 75000;
			}
		}else if (tempo > 45 && tempo < 90) {
			if(frequenciaAtual < frequenciaAntiga) {
				frequenciaAntiga = frequenciaAtual;
				frequenciaAtual -= 90000;
			}
		}
		return frequenciaAtual;
	}
	
	private float velocidadeGeracaoObstaculo (long tempo) {
		if (tempo >= 10 && tempo <= 50) {
			if (velocidadeAtual > velocidadeAntiga) {
				velocidadeAntiga = velocidadeAtual;
				velocidadeAtual += 0.005;
				System.out.println(velocidadeAtual);
			}
		}
		return velocidadeAtual;
	}
}