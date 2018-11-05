package com.aesophor.vigilante.component.sound;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.audio.Sound;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SoundComponent implements Component {

    private Map<SoundType, Sound> sounds;

    public SoundComponent() {
        sounds = new HashMap<>();
    }


    public Sound get(SoundType soundType) {
        return sounds.get(soundType);
    }

    public void put(SoundType soundType, Sound sound) {
        sounds.put(soundType, sound);
    }

    public Collection<Sound> values() {
        return sounds.values();
    }

}