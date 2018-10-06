package com.nikas.trial;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.nikas.trial.model.entity.configuration.ScreenParameters;
import com.nikas.trial.model.entity.map.MapGenerationParameters;
import com.nikas.trial.screens.ScreenFactory;
import com.nikas.trial.engine.EngineOperator;
import lombok.Getter;
import lombok.Setter;

public class GameLauncher extends Game {

	@Getter
	private Skin uiSkin;
	@Getter
	private static ScreenFactory factory;
	@Getter
	private EngineOperator engineOperator;
	@Getter
	private ScreenParameters screenParameters;
	@Getter
	@Setter
    private MapGenerationParameters mapGenerationParameters = new MapGenerationParameters(200,
			15,
			0.0f,
			9000.0f,
			-4000.0f,
			800.0f);

	private void globalInit() {
		uiSkin = new Skin(Gdx.files.internal("skins/craftacular/craftacular-ui.json"));
		screenParameters = new ScreenParameters(1600, 900);
		factory = new ScreenFactory(this);
		engineOperator = new EngineOperator();
	}

	@Override
	public void create () {
		globalInit();
		this.setScreen(factory.getTitleInstance());
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
	}
}
