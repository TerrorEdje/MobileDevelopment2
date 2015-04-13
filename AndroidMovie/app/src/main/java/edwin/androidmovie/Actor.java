package edwin.androidmovie;

import java.io.Serializable;

/**
 * Created by Edwin on 8-4-2015.
 */
public class Actor implements Serializable {
    private String name;
    private String characterName;

    public Actor(String name, String characterName)
    {
        setCharacterName(characterName);
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

}
