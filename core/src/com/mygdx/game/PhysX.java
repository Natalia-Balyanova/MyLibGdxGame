package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class PhysX {
    final World world;
    private final Box2DDebugRenderer debugRenderer;//отрисовывает физические объекта мира

    public PhysX() {
        world = new World(new Vector2(0, -9.81f), true); //уходит в сон, если не трогать объекты, есть гравитация
        debugRenderer = new Box2DDebugRenderer();
    }

    public void debugDraw(OrthographicCamera camera){
        debugRenderer.render(world, camera.combined);//для правильного отображения мира
    }

    public void step(){
        world.step(1/60f, 3, 3);//временной диапазон физики; шаги изменения скоростей; кг, м, сек
    }

    public void dispose(){
        world.dispose();
        debugRenderer.dispose();
    }
}