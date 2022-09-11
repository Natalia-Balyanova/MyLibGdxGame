package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MyAnimation {
    Texture img;
    Animation<TextureRegion> anm;
    private float time;

    public MyAnimation(String name, int row, int col, float fps, Animation.PlayMode playMode) {
        time = 0;
        img = new Texture(name);
        TextureRegion region1 = new TextureRegion(img);
        TextureRegion[][] regions = region1.split(img.getWidth()/col, img.getHeight()/row);
        TextureRegion[] temp = new TextureRegion[regions.length * regions[0].length];

        int cnt  = 0;
        for (TextureRegion[] region : regions) {
            for (int j = 0; j < regions[0].length; j++) {
                temp[cnt++] = region[j];
            }
        }

        anm = new Animation<>(1/fps, temp);//длительность отрисовки кадра в сек
        anm.setPlayMode(playMode);
    }

    public TextureRegion draw() {
        return anm.getKeyFrame(time); //получение кадра по времени
    }

    public void setTime(float dT) {
        time += dT;
    }

    public void dispose() {
        this.img.dispose();
    }
}
