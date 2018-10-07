package com.nikas.trial;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.nikas.trial.engine.configuration.ColoringConfiguration;
import com.nikas.trial.engine.configuration.enums.EnvironmentColorNaming;
import com.nikas.trial.engine.configuration.ScreenParameters;
import com.nikas.trial.model.entity.map.MapGenerationParameters;
import com.nikas.trial.screens.ScreenFactory;
import com.nikas.trial.engine.EngineOperator;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

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
    private MapGenerationParameters mapGenerationParameters = new MapGenerationParameters(1000,
			15,
			0.0f,
			9000.0f,
			-4000.0f,
			2000.0f);

	@Getter
	@Setter
	private ColoringConfiguration coloringConfiguration = new ColoringConfiguration();

	//todo file parametrize
	private void globalInit() {
		uiSkin = new Skin(Gdx.files.internal("skins/craftacular/craftacular-ui.json"));
		screenParameters = new ScreenParameters(1600, 900);
		factory = new ScreenFactory(this);
		engineOperator = new EngineOperator(this);

		Map<EnvironmentColorNaming, Color> colorMap = new HashMap<>();
		colorMap.put(EnvironmentColorNaming.WATER_LOW, new Color(0.3f, 0.3f, 0.6f, 1));
		colorMap.put(EnvironmentColorNaming.WATER_HIGH, new Color(0.8f, 0.8f, 0.9f, 1));
		colorMap.put(EnvironmentColorNaming.LAND_LOW, new Color(0.4f, 0.6f, 0.4f, 1));
		colorMap.put(EnvironmentColorNaming.LAND_MEDIUM, new Color(0.92f,0.76f,0.0f, 1));
		colorMap.put(EnvironmentColorNaming.LAND_HIGH, new Color(0.63f,0.11f,0.0f, 1));
		coloringConfiguration.setEnvironmentColors(colorMap);
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
