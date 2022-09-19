package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class MyInputProcessor implements InputProcessor {
    private String outString = "";

    public String getOutString() {
        return outString;
    }

    @Override
    public boolean keyDown(int keycode) {//код нажатой кнопки
        if (!outString.contains(Input.Keys.toString(keycode))) {//если в строке отсутствует строка соответстующая коду нажатой клавищши, то мы ее добавляем
            outString += Input.Keys.toString(keycode);
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {//код поднятой кнопки
        if (outString.contains(Input.Keys.toString(keycode))) {
            outString = outString.replace(Input.Keys.toString(keycode), "");
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {//возврат буквы
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {//касание экран: координаты, номер пальца,номер кнопки
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {//отпустили кнопку
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {//нажали и водим мышь
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {//ведение мыши
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {//прокрутка
        return false;
    }
}
