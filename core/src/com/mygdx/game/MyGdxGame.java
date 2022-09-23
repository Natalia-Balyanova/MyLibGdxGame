package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {
	private SpriteBatch batch; //обязательный экземпляр класса по умолчанию
	private ShapeRenderer shapeRenderer;
	//private Texture img; //картинка, загруженная в видеопамять и подготовленная для отработки 3D-ускорителя
	private MyAtlasAnimation carTop, car, tmpA;//машинка вид сверху, машина в движении
	private Music motor;//звук мотора
	private Sound burst;//звук взрыва
	private Rectangle rectangle, window;
	private PhysX physX;
	private Body body;
	private MyInputProcessor myInputProcessor;

	private OrthographicCamera camera;
	private TiledMap map;
	private OrthogonalTiledMapRenderer mapRenderer;

	private float x, y;

	int dir = 0,  traffic = 1;
	@Override
	public void create () { //запускается один раз, аналог конструктора
		map = new TmxMapLoader().load("map/level1_map.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(map);

		physX = new PhysX();

		BodyDef def = new BodyDef();//описатель тела
		FixtureDef fdef = new FixtureDef();//описатель формы
		PolygonShape shape = new PolygonShape();//прямоугольник

		//def.gravityScale = 1; //взаимодействие тела с гравитацией
		def.type = BodyDef.BodyType.StaticBody;//преграда, не двигается
		fdef.shape = shape;
		fdef.density = 1;//плотность хотя бы 1
		fdef.friction = 0;//шершавость, 0 - лед
		fdef.restitution = 1;//прыгучесть, инерция

		MapLayer env = map.getLayers().get("env");
		Array<RectangleMapObject> rect = env.getObjects().getByType(RectangleMapObject.class);
		physX.world.createBody(def).createFixture(fdef).setUserData("Kubik");
		for (int i = 0; i < rect.size; i++) {
			float x = rect.get(i).getRectangle().x;
			float y = rect.get(i).getRectangle().y;
			float w = rect.get(i).getRectangle().width / 2;
			float h = rect.get(i).getRectangle().height / 2;
			def.position.set(x, y);
			fdef.shape = shape;
			shape.setAsBox(w, h);//прямоугольник
			physX.world.createBody(def).createFixture(fdef).setUserData("Kubik");
		}

		def.type = BodyDef.BodyType.DynamicBody;
		def.gravityScale = 4;
		env = map.getLayers().get("dyn");
		rect = env.getObjects().getByType(RectangleMapObject.class);
		fdef.shape = shape;
		fdef.density = 1;//плотность хотя бы 1
		fdef.friction = 0;//шершавость, 0 - лед
		fdef.restitution = 1;//прыгучесть, инерция

		physX.world.createBody(def).createFixture(fdef).setUserData("Kubik");
		for (int i = 0; i < rect.size; i++) {
			float x = rect.get(i).getRectangle().x;
			float y = rect.get(i).getRectangle().y;
			float w = rect.get(i).getRectangle().width / 2;
			float h = rect.get(i).getRectangle().height / 2;
			def.position.set(x, y);
			shape.setAsBox(w, h);//прямоугольник
			fdef.shape = shape;
			fdef.density = 1;//плотность хотя бы 1
			fdef.friction = 0;//шершавость, 0 - лед
			fdef.restitution = 1;//прыгучесть, инерция
			physX.world.createBody(def).createFixture(fdef).setUserData("Kubik");
		}

		env = map.getLayers().get("hero");
		RectangleMapObject hero = (RectangleMapObject) env.getObjects().get("Hero");
		float x = hero.getRectangle().x;
		float y = hero.getRectangle().y;
		float w = hero.getRectangle().width / 2;
		float h = hero.getRectangle().height / 2;
		def.position.set(x, y);
		shape.setAsBox(w, h);
		fdef.shape = shape;
		fdef.density = 1;//плотность хотя бы 1
		fdef.friction = 0;//шершавость, 0 - лед
		fdef.restitution = 1;//прыгучесть, инерция
		body = physX.world.createBody(def);
		body.createFixture(fdef).setUserData("Hero");

		shape.dispose();

		//shapeRenderer =  new ShapeRenderer();
		rectangle = new Rectangle();
		window = new Rectangle(0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		myInputProcessor = new MyInputProcessor();
		Gdx.input.setInputProcessor(myInputProcessor);

		motor = Gdx.audio.newMusic(Gdx.files.internal("Zvuk-motora-nissan-skaylayn.mp3"));
		motor.setVolume(0.025f);
		//music.setPan(0, 1);//панорамирование
		motor.setLooping(true);
		motor.play();

		burst = Gdx.audio.newSound(Gdx.files.internal("zvuk-vzryva-libgdx.mp3"));

		batch = new SpriteBatch();
		//img = new Texture("Title Fon.jpg");//картинки форматов jpg и png
		carTop = new MyAtlasAnimation("atlas/unnamed.atlas", "cartop", 5, Animation.PlayMode.LOOP);
		car = new MyAtlasAnimation("atlas/unnamed.atlas", "car", 5, Animation.PlayMode.LOOP);
		tmpA = carTop;

		camera =  new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	@Override
	public void render () {//запускается автоматически с заданной частотой setForegroundFPS(60)
		ScreenUtils.clear(Color.BLACK);//чистим экран, задаем цвет

		camera.position.x = body.getPosition().x;
		camera.position.y = body.getPosition().y;
//		camera.position.x = Gdx.graphics.getWidth()/2f;
//		camera.position.y = Gdx.graphics.getHeight()/2f;
		camera.zoom = 1f;
		camera.update();//пересчет обязателен

		mapRenderer.setView(camera);
		mapRenderer.render();

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
			body.applyForceToCenter(new Vector2(-10000, 0f), true);
		}

		if(myInputProcessor.getOutString().contains("D")) {
			dir = 1;
			//x++;
			tmpA = car;
			body.applyForceToCenter(new Vector2(10000, 0f), true);
		}

		if(myInputProcessor.getOutString().contains("W")) { y++; }
		if(myInputProcessor.getOutString().contains("S")) { y--; }
		if(myInputProcessor.getOutString().contains("Space")) {
//			x = Gdx.graphics.getWidth()/2f;
//			y = Gdx.graphics.getHeight()/2f;
			body.applyForceToCenter(new Vector2(0, 100000f), true);
		}

		if (dir == -1) { x -= traffic; }
		if (dir == 1) { x += traffic; }

		tmpA.setTime(Gdx.graphics.getDeltaTime());
		TextureRegion tmp = tmpA.draw();
		if (!tmpA.draw().isFlipX() & dir == -1) {//проверка на развернут ли регион, если нет, то
			tmpA.draw().flip(true, false); //разворачиваем
		}

		if (tmpA.draw().isFlipX() & dir == 1) {
			tmpA.draw().flip(true, false);
		}

		rectangle.x = x;
		rectangle.y = y;
		rectangle.width = tmp.getRegionWidth();
		rectangle.height = tmp.getRegionHeight();

		System.out.println(myInputProcessor.getOutString());

		float x = body.getPosition().x - 2.5f/camera.zoom;
		float y = body.getPosition().y - 2.5f/camera.zoom;

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(tmpA.draw(), x, y);//привязка тела к камере
		//batch.draw(tmpA.draw(), body.getPosition().x, body.getPosition().y);
		batch.end();

		window.overlaps(rectangle);
		if(!window.contains(rectangle)) Gdx.graphics.setTitle("Out"); else Gdx.graphics.setTitle("In");

		physX.step();
		physX.debugDraw(camera);
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportHeight = height;
		camera.viewportWidth = width;
	}
	@Override
	public void dispose () {//срабатывает при закрытии приложения, необходимо чистить объекты для избежания утечки
		batch.dispose();
		tmpA.dispose();
		motor.dispose();
		burst.dispose();
		carTop.dispose();
		car.dispose();
		map.dispose();
		mapRenderer.dispose();
	}
}