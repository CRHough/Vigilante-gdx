package com.aesophor.medievania.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.audio.Sound;
import java.util.HashMap;
import java.util.Map;

public class SoundComponent implements Component {

    public Map<SoundType, Sound> sounds;

    public SoundComponent() {
        sounds = new HashMap<>();
    }


    public Sound get(SoundType soundType) {
        return sounds.get(soundType);
    }

    public void put(SoundType soundType, Sound sound) {
        sounds.put(soundType, sound);
    }

}