package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {
	private SpriteBatch batch; //обязательный экземпляр класса по умолчанию
	private Texture img; //картинка, загруженная в видеопамять и подготовленная для отработки 3D-ускорителя
	private MyAtlasAnimation carTop, car, tmpA;//машинка вид сверху, машина в движении
	private Music motor;//звук мотора
	private Sound burst;//звук взрыва

	MyInputProcessor myInputProcessor;

	private float x, y;

	int dir = 0,  traffic = 1;
	
	@Override
	public void create () { //запускается один раз, аналог конструктора
		myInputProcessor = new MyInputProcessor();
		Gdx.input.setInputProcessor(myInputProcessor);

		motor = Gdx.audio.newMusic(Gdx.files.internal("Zvuk-motora-nissan-skaylayn.mp3"));
		motor.setVolume(0.0125f);
		//music.setPan(0, 1);//панорамирование
		motor.setLooping(true);
		motor.play();

		burst = Gdx.audio.newSound(Gdx.files.internal("zvuk-vzryva-libgdx.mp3"));

		batch = new SpriteBatch();
		img = new Texture("Title Fon.jpg");//картинки форматов jpg и png
		carTop = new MyAtlasAnimation("atlas/unnamed.atlas", "cartop", 5, Animation.PlayMode.LOOP);
		car = new MyAtlasAnimation("atlas/unnamed.atlas", "car", 5, Animation.PlayMode.LOOP);
		tmpA = carTop;
	}

	@Override
	public void render () {//запускается автоматически с заданной частотой setForegroundFPS(60)
		ScreenUtils.clear(1, 1, 1, 1);//чистим экран, задаем цвет
		tmpA = carTop;
		dir = 0;
		//доступ к gdx, описывающего взаимодействие с окружающим миром
//		float x = Gdx.input.getX() - animation.draw().getRegionWidth()/2f;//возвращает координаты мышки по оси X
//		float y = Gdx.graphics.getHeight() - (Gdx.input.getY() + animation.draw().getRegionHeight()/2f);

		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {burst.play(0.5f, 1, 0);}

		if(myInputProcessor.getOutString().contains("A")) {
			//x--;
			dir = -1;
			tmpA = car;
		}

		if(myInputProcessor.getOutString().contains("D")) {
			dir = 1;
			//x++;
			tmpA = car;
		}

		if(myInputProcessor.getOutString().contains("W")) { y++; }
		if(myInputProcessor.getOutString().contains("S")) { y--; }
		if(myInputProcessor.getOutString().contains("Space")) {
			x = Gdx.graphics.getWidth()/2f;
			y = Gdx.graphics.getHeight()/2f;
		}

		if (dir == -1) { x -= traffic; }
		if (dir == 1) { x += traffic; }

		TextureRegion tmp = tmpA.draw();
		if (!tmpA.draw().isFlipX() & dir == -1) {//проверка на развернут ли регион, если нет, то
			tmpA.draw().flip(true, false); //разворачиваем
		}

		if (tmpA.draw().isFlipX() & dir == 1) {
			tmpA.draw().flip(true, false);
		}

		tmpA.setTime(Gdx.graphics.getDeltaTime());

		System.out.println(myInputProcessor.getOutString());

		//Gdx.input.isKeyPressed(Input.Keys.ALT_RIGHT);

		batch.begin();
		batch.draw(img, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());//растянуть//отрисовка текстуры, координвты нижнего левого угла
//		batch.draw(img, x, y,
//				img.getWidth()/2f, //ширина картинки
//				img.getHeight()/2f,
//				img.getWidth(),
//				img.getHeight(),
//				1, //масштабирование
//				1,
//				0, //поворот
//				0,
//				0,
//				img.getWidth(),
//				img.getHeight(),
//				false, false);//зеркалирование
//		batch.draw(img, x, y);

		batch.draw(tmpA.draw(), x, y);
		batch.end();
	}
	
	@Override
	public void dispose () {//срабатывает при закрытии приложения, необходимо чистить объекты для избежания утечки
		batch.dispose();
		img.dispose();
		motor.dispose();
		burst.dispose();
		carTop.dispose();
		car.dispose();
	}
}
