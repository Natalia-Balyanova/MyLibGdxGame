package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {
	private SpriteBatch batch; //обязательный экземпляр класса по умолчанию
	private Texture img; //картинка, загруженная в видеопамять и подготовленная для отработки 3D-ускорителя

	MyAnimation animation;
	
	@Override
	public void create () { //запускается один раз, аналог конструктора
		batch = new SpriteBatch();
		img = new Texture("Title Fon.jpg");//картинки форматов jpg и png
		animation = new MyAnimation("Burn Fire.png", 4, 8, 10, Animation.PlayMode.LOOP);
	}

	@Override
	public void render () {//запускается автоматически с заданной частотой setForegroundFPS(60)
		ScreenUtils.clear(1, 1, 1, 1);//чистим экран, задаем цвет
		//доступ к gdx, описывающего взаимодействие с окружающим миром
		animation.setTime(Gdx.graphics.getDeltaTime());

//		float x = Gdx.input.getX() - img.getWidth()/2f;//возвращает координаты мышки по оси X
//		float y = Gdx.graphics.getHeight() - (Gdx.input.getY() + img.getHeight()/2f);

		float x = Gdx.input.getX() - animation.draw().getRegionWidth()/2f;//возвращает координаты мышки по оси X
		float y = Gdx.graphics.getHeight() - (Gdx.input.getY() + animation.draw().getRegionHeight()/2f);

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

		batch.draw(animation.draw(), x, y);
		batch.end();
	}
	
	@Override
	public void dispose () {//срабатывает при закрытии приложения, необходимо чистить объекты для избежания утечки
		batch.dispose();
		img.dispose();
		animation.dispose();
	}
}
