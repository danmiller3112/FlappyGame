package com.roll.game.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Stack;

/**
 * Created by RDL on 04/06/2017.
 */

public class GameStateManager {

    private Stack<State> states;

    public GameStateManager() {
        this.states = new Stack<State>();
    }

    public void push(State state){
        states.push(state);
    }

    public void pop(){
        states.pop().dispose();
    }

    public void set(State state){
        states.pop().dispose();
        states.push(state);
    }

    public void update(float dt){
        states.peek().update(dt);
    }

    public void render(SpriteBatch sb){
        states.peek().render(sb);
    }
}
