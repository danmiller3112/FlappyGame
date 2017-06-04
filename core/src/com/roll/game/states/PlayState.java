package com.roll.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.roll.game.FlappyDemo;
import com.roll.game.sprites.Bird;
import com.roll.game.sprites.Tube;


/**
 * Created by RDL on 04/06/2017.
 */

public class PlayState extends State {

    public static final int TUBE_SPACING = 125;
    public static final int TUBE_COUNT = 4;
    public static final int GROUND_Y_OFFSET = -30;

    private Bird bird;
    private Texture bg;
    private Texture gr;
    private Vector2 grPos1, grPos2;

    private Array<Tube> tubes;

    public PlayState(GameStateManager gsm) {
        super(gsm);
        camera.setToOrtho(false, FlappyDemo.WIDTH / 2, FlappyDemo.HEIGHT / 2);
        bird = new Bird(50, 300);
        bg = new Texture("bg.png");
        gr = new Texture("ground.png");
        grPos1 = new Vector2(camera.position.x - camera.viewportWidth / 2, GROUND_Y_OFFSET);
        grPos2 = new Vector2((camera.position.x - camera.viewportWidth / 2) + gr.getWidth(), GROUND_Y_OFFSET);

        tubes = new Array<Tube>();
        for (int i = 0; i < TUBE_COUNT; i++) {
            tubes.add(new Tube(i * (TUBE_SPACING + Tube.TUBE_WIDTH)));
        }
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            bird.jump();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        updateGround();
        bird.update(dt);
        camera.position.x = bird.getPosition().x + 80;

        for (int i = 0; i < tubes.size; i++) {
            Tube tube = tubes.get(i);
            if (camera.position.x - (camera.viewportWidth / 2) > tube.getPosBotTube().x + tube.getTopTube().getWidth()) {
                tube.reposition(tube.getPosTopTube().x + ((Tube.TUBE_WIDTH + TUBE_SPACING) * TUBE_COUNT));
            }

            if (tube.collides(bird.getBounds()))
                gsm.set(new GameOver(gsm));
        }

        camera.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(bg, camera.position.x - (camera.viewportWidth / 2), 0);
        sb.draw(bird.getBird(), bird.getPosition().x, bird.getPosition().y);
        for (Tube tube : tubes) {
            sb.draw(tube.getTopTube(), tube.getPosBotTube().x, tube.getPosTopTube().y);
            sb.draw(tube.getBottomTube(), tube.getPosBotTube().x, tube.getPosBotTube().y);
        }

        sb.draw(gr, grPos1.x, grPos1.y);
        sb.draw(gr, grPos2.x, grPos2.y);

        sb.end();
    }

    @Override
    public void dispose() {
        bg.dispose();
        bird.dispose();
        gr.dispose();
        for (Tube tube : tubes) {
            tube.dispose();
        }
        System.out.println("PlayState disposed");
    }

    private void updateGround() {
        if (camera.position.x - (camera.viewportWidth / 2) > grPos1.x + gr.getWidth())
            grPos1.add(gr.getWidth() * 2, 0);
        if (camera.position.x - (camera.viewportWidth / 2) > grPos2.x + gr.getWidth())
            grPos2.add(gr.getWidth() * 2, 0);
    }
}
